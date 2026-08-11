#!/usr/local/bin/python3.9

import csv
import json
import multiprocessing
import os
import time
from datetime import datetime
from os import listdir, path
from os.path import isfile, join
import math
from jsonsubschema.api import isSubschema
from argparse import ArgumentParser

# Failures are recorded under type(error).__name__, so the individual error classes
# (SchemaError, JsonRefError, RecursionError, UnsupportedRecursiveRef,
# UnsupportedEnumCanonicalization, UnsupportedNegatedObject, UnsupportedNegatedArray,
# ...) no longer need to be imported and enumerated here. This matches how the
# timeout path has always had to classify errors coming back from the worker.


TIMEOUT = 0

# Forking is only used to enforce the per-case timeout. Resolving the context once
# avoids re-doing the (cheap but repeated) context lookup for every schema pair.
_MP_CONTEXT = multiprocessing.get_context("fork") if hasattr(os, "fork") else multiprocessing


def _is_non_boolean_number(value):
    return isinstance(value, (int, float)) and not isinstance(value, bool)


def rewrite_common_schema_issues(schema, root_schema=None):
    if root_schema is None:
        root_schema = schema

    if isinstance(schema, dict):
        rewritten = {}
        for key, value in schema.items():
            if key == "$id" and isinstance(value, str):
                # Some schemas use JSON Pointer fragments in $id (e.g. "#/properties/x"),
                # but the validator used by jsonsubschema expects non-empty fragments to be absent.
                # Keep a stable identifier while normalizing to an accepted form.
                if value.startswith("#/"):
                    rewritten[key] = "#"
                elif "#" in value and not value.endswith("#"):
                    rewritten[key] = value.split("#", 1)[0] + "#"
                else:
                    rewritten[key] = value
                continue
            if key in ("properties", "patternProperties") and isinstance(value, dict):
                rewritten_properties = {}
                for prop_name, prop_schema in value.items():
                    if prop_schema is True:
                        rewritten_properties[prop_name] = {}
                    elif prop_schema is False:
                        rewritten_properties[prop_name] = {"not": {}}
                    else:
                        rewritten_properties[prop_name] = rewrite_common_schema_issues(prop_schema, root_schema)
                rewritten[key] = rewritten_properties
            elif key in ("definitions", "$defs") and isinstance(value, dict):
                rewritten_defs = {}
                for def_name, def_schema in value.items():
                    if def_schema is True:
                        rewritten_defs[def_name] = {}
                    elif def_schema is False:
                        rewritten_defs[def_name] = {"not": {}}
                    else:
                        rewritten_defs[def_name] = rewrite_common_schema_issues(def_schema, root_schema)
                rewritten[key] = rewritten_defs
            elif key == "items":
                if value is True:
                    rewritten[key] = {}
                elif value is False:
                    rewritten[key] = {"not": {}}
                elif isinstance(value, list):
                    rewritten_list = []
                    for sub_schema in value:
                        if sub_schema is True:
                            rewritten_list.append({})
                        elif sub_schema is False:
                            rewritten_list.append({"not": {}})
                        else:
                            rewritten_list.append(rewrite_common_schema_issues(sub_schema, root_schema))
                    rewritten[key] = rewritten_list
                else:
                    rewritten[key] = rewrite_common_schema_issues(value, root_schema)
            elif key in ("anyOf", "oneOf", "allOf") and isinstance(value, list):
                rewritten_list = []
                for sub_schema in value:
                    if sub_schema is True:
                        rewritten_list.append({})
                    elif sub_schema is False:
                        rewritten_list.append({"not": {}})
                    else:
                        rewritten_list.append(rewrite_common_schema_issues(sub_schema, root_schema))
                rewritten[key] = rewritten_list
            elif key == "required" and isinstance(value, list):
                # Some inputs contain required: [] which is rejected by the draft-04
                # metaschema used by the underlying tool; dropping the keyword is equivalent.
                seen_required = set()
                normalized_required = []
                for required_name in value:
                    if isinstance(required_name, str) and required_name not in seen_required:
                        seen_required.add(required_name)
                        normalized_required.append(required_name)
                if len(normalized_required) > 0:
                    rewritten[key] = normalized_required
            elif key == "examples":
                # Some schemas provide a scalar examples value; normalize to array
                # for validator compatibility while preserving intent.
                if isinstance(value, list):
                    rewritten[key] = rewrite_common_schema_issues(value, root_schema)
                else:
                    rewritten[key] = [rewrite_common_schema_issues(value, root_schema)]
            elif key == "not":
                if value is True:
                    rewritten[key] = {}
                elif value is False:
                    rewritten[key] = {"not": {}}
                else:
                    rewritten[key] = rewrite_common_schema_issues(value, root_schema)
            elif key == "enum" and isinstance(value, list):
                # Deduplicate enum elements while preserving order
                # Use JSON serialization for reliable comparison of complex objects
                seen = set()
                deduplicated = []
                for enum_val in value:
                    try:
                        # Use JSON string as key for comparison (handles complex types)
                        json_str = json.dumps(enum_val, sort_keys=True, separators=(',', ':'))
                    except (TypeError, ValueError):
                        # Fallback to string representation for non-JSON-serializable values
                        json_str = str(enum_val)
                    if json_str not in seen:
                        seen.add(json_str)
                        deduplicated.append(enum_val)
                rewritten[key] = deduplicated
            else:
                rewritten[key] = rewrite_common_schema_issues(value, root_schema)

        exclusive_minimum = rewritten.get("exclusiveMinimum")
        if exclusive_minimum is False:
            # Explicit false is equivalent to omitting the keyword and avoids
            # metaschema type conflicts across draft implementations.
            rewritten.pop("exclusiveMinimum", None)
            exclusive_minimum = None
        if _is_non_boolean_number(exclusive_minimum):
            if "minimum" not in rewritten:
                rewritten["minimum"] = exclusive_minimum
            rewritten["exclusiveMinimum"] = True

        exclusive_maximum = rewritten.get("exclusiveMaximum")
        if exclusive_maximum is False:
            # Explicit false is equivalent to omitting the keyword and avoids
            # metaschema type conflicts across draft implementations.
            rewritten.pop("exclusiveMaximum", None)
            exclusive_maximum = None
        if _is_non_boolean_number(exclusive_maximum):
            if "maximum" not in rewritten:
                rewritten["maximum"] = exclusive_maximum
            rewritten["exclusiveMaximum"] = True

        return rewritten

    if isinstance(schema, list):
        return [rewrite_common_schema_issues(item, root_schema) for item in schema]

    return schema


def rewrite_schema(schema):
    ''' Apply the automatic corrections to a whole schema.

        rewrite_common_schema_issues never mutates its argument (it rebuilds every
        dict/list it touches), so the input does not need to be deep-copied first.
    '''
    if schema is True:
        return {}
    if schema is False:
        return {"not": {}}
    return rewrite_common_schema_issues(schema)


class Attempt:
    ''' Outcome of a single isSubschema call.

        elapsed_ms only ever covers the isSubschema call itself, so neither the
        fork/IPC overhead of the timeout guard nor a preceding failed attempt and
        its rewrite leak into the reported runtime.
    '''

    __slots__ = ("ok", "value", "error_name", "error_message", "elapsed_ms")

    def __init__(self, ok, value=None, error_name=None, error_message=None, elapsed_ms=0.0):
        self.ok = ok
        self.value = value
        self.error_name = error_name
        self.error_message = error_message
        self.elapsed_ms = elapsed_ms


def _is_subschema_worker(s1, s2, conn):
    start = time.perf_counter()
    try:
        result = isSubschema(s1, s2)
        conn.send(("ok", result, (time.perf_counter() - start) * 1000))
    except BaseException as error:
        conn.send(("error", type(error).__name__, str(error), (time.perf_counter() - start) * 1000))
    finally:
        conn.close()


def _attempt_in_subprocess(s1, s2):
    ''' Run isSubschema under a wall-clock timeout in a forked child.

        The child times and reports its own isSubschema call, so the fork, the
        pickling of the schemas and the IPC round-trip are excluded from the
        measured runtime. The parent waits on the pipe rather than on the process
        so a result larger than the pipe buffer cannot dead-lock the join.
    '''
    receiver, sender = _MP_CONTEXT.Pipe(duplex=False)
    process = _MP_CONTEXT.Process(target=_is_subschema_worker, args=(s1, s2, sender))
    start = time.perf_counter()
    process.start()
    # Drop the parent's copy of the write end so a child that dies without
    # sending anything shows up as EOF instead of hanging until the timeout.
    sender.close()
    try:
        if not receiver.poll(TIMEOUT):
            process.terminate()
            process.join()
            return Attempt(False, error_name="TimeoutError", error_message="TimeoutException",
                           elapsed_ms=(time.perf_counter() - start) * 1000)
        try:
            message = receiver.recv()
        except EOFError:
            return Attempt(False, error_name="RuntimeError",
                           error_message="Subprocess ended without returning a result",
                           elapsed_ms=(time.perf_counter() - start) * 1000)
        if message[0] == "ok":
            return Attempt(True, value=message[1], elapsed_ms=message[2])
        return Attempt(False, error_name=message[1], error_message=message[2], elapsed_ms=message[3])
    finally:
        receiver.close()
        if process.is_alive():
            process.terminate()
        process.join()


def attempt_subschema(s1, s2):
    ''' Run one isSubschema check and capture its outcome without raising. '''
    if TIMEOUT > 0:
        return _attempt_in_subprocess(s1, s2)

    start = time.perf_counter()
    try:
        result = isSubschema(s1, s2)
        return Attempt(True, value=result, elapsed_ms=(time.perf_counter() - start) * 1000)
    except (Exception, SystemExit) as error:
        # KeyboardInterrupt deliberately stays uncaught. Every other failure -- the
        # jsonsubschema-specific ones (UnsupportedRecursiveRef, UnsupportedNegatedArray,
        # ...) as well as RecursionError, SchemaError, JsonRefError and friends -- is
        # recorded under its own type name.
        return Attempt(False, error_name=type(error).__name__, error_message=str(error),
                       elapsed_ms=(time.perf_counter() - start) * 1000)


def build_error_record(input_name, file_name, case_id, error_name, error_message):
    return {
        "time": datetime.now().isoformat(timespec="seconds"),
        "input": input_name,
        "filename": file_name,
        "id": case_id,
        "error_name": error_name,
        "error_message": error_message,
    }


def analyze_schema(s1, s2, input_name, file_name, case_id, error_log_rows):
    ''' Check whether s1 is a subschema of s2.

        Returns (result, hadRetry, totalTime). A SchemaError triggers one retry on
        the automatically rewritten schemas; in that case totalTime measures only
        the retry, so the automatic rewrite never inflates the reported runtime.
    '''
    attempt = attempt_subschema(s1, s2)
    had_retry = False

    if not attempt.ok and attempt.error_name == "SchemaError":
        had_retry = True
        attempt = attempt_subschema(rewrite_schema(s1), rewrite_schema(s2))

    if attempt.ok:
        return attempt.value, had_retry, attempt.elapsed_ms

    error_log_rows.append(
        build_error_record(input_name, file_name, case_id, attempt.error_name, attempt.error_message)
    )
    return attempt.error_name, had_retry, attempt.elapsed_ms


def bool_to_int(b):
    if b == True:
        return 1
    elif b == False:
        return 0
    else:
        return b


def _column_formatter(values):
    ''' Pick the textual representation pandas' to_csv would have used for a column.

        Reproduces the dtype inference the previous pandas-backed implementation
        relied on: an all-integer column stays integral, the same column with a
        missing value is promoted to float ("0" vs "0.0"), and anything mixed is
        written verbatim. Keeping this behaviour identical matters because the
        downstream evaluation scripts parse these CSVs.
    '''
    present = [v for v in values if v is not None]

    if present and all(isinstance(v, bool) for v in present) and len(present) == len(values):
        return lambda v: "True" if v else "False"

    if present and all(_is_non_boolean_number(v) for v in present):
        if len(present) == len(values) and all(isinstance(v, int) for v in present):
            return lambda v: str(v)
        return lambda v: "" if v is None else repr(float(v))

    return lambda v: "" if v is None else str(v)


def write_columns_csv(output_path, columns, sort_by):
    ''' Write a column-oriented result table, sorted like pandas' sort_values. '''
    fieldnames = list(columns)
    row_count = len(columns[fieldnames[0]]) if fieldnames else 0

    order = range(row_count)
    if sort_by and row_count:
        keys = [tuple(columns[column][i] for column in sort_by) for i in range(row_count)]
        try:
            order = sorted(order, key=lambda i: keys[i])
        except TypeError:
            # Mixed key types (e.g. a non-integer id) are not orderable; fall back
            # to comparing their textual form rather than failing the whole run.
            order = sorted(order, key=lambda i: tuple(str(part) for part in keys[i]))

    formatters = [_column_formatter(columns[column]) for column in fieldnames]
    with open(output_path, "w", newline="") as output_file:
        writer = csv.writer(output_file, lineterminator="\n")
        writer.writerow(fieldnames)
        writer.writerows(
            [formatter(columns[column][i]) for column, formatter in zip(fieldnames, formatters)]
            for i in order
        )


def extractAndCheckTestSuite(pathToFolder, error_log_rows):
    ID = "id"
    S1 = "schema1"
    S2 = "schema2"
    TESTS = "tests"
    S1SUBS2 = "s1SubsetEqOfs2"
    new_dict = {
        "subFolder": [],
        "subSubFolder": [],
        "fileName": [],
        "id": [],
        "s1SUBs2": [],
        "IBM_s1SUBs2": [],
        "hadRetry": [],
        "totalTime": [],
    }

    subFoldersList = [f for f in os.listdir(pathToFolder) if path.isdir(join(pathToFolder, f))]
    for subFolder in subFoldersList:
        subFolderPath = join(pathToFolder, subFolder)
        subSubFolders = [f for f in os.listdir(subFolderPath) if path.isdir(join(subFolderPath, f))]
        for subSubFolder in subSubFolders:
            subSubFolderPath = join(subFolderPath, subSubFolder)
            files = [
                f
                for f in listdir(subSubFolderPath)
                if isfile(join(subSubFolderPath, f))
                and f.endswith(".json")
            ]
            for f in files:
                fileName = f.replace(".json", "")
                with open(join(subSubFolderPath, f), "r") as inputFile:
                    data = json.load(inputFile)
                for d in data:
                    # print("Processing: " + fileName + " Case " + str(d[ID]))
                    id = d[ID]
                    tests = d[TESTS]
                    s1Subs2 = 9
                    IBM_s1Subs2 = 9
                    hadRetry = False
                    totalTime = None
                    if S1SUBS2 in tests:
                        s1Subs2 = bool_to_int(tests[S1SUBS2])
                        schema_result, hadRetry, elapsed = analyze_schema(
                            d[S1], d[S2], pathToFolder, fileName, id, error_log_rows
                        )
                        IBM_s1Subs2 = bool_to_int(schema_result)
                        totalTime = round(elapsed, 2)
                    new_dict["subFolder"].append(subFolder)
                    new_dict["subSubFolder"].append(subSubFolder)
                    new_dict["fileName"].append(fileName)
                    new_dict["id"].append(id)
                    new_dict["s1SUBs2"].append(s1Subs2)
                    new_dict["IBM_s1SUBs2"].append(IBM_s1Subs2)
                    new_dict["hadRetry"].append(hadRetry)
                    new_dict["totalTime"].append(totalTime)
    return new_dict


def rename_file(filename, suffixes, suffix_sep, suffix_pattern_sep):
    if suffixes is not None:
        filename = filename.replace(".json", "")
        filename1 = filename + suffix_sep + suffixes[0] + "_not_" + suffixes[1]
        filename2 = filename + suffix_sep + suffixes[1] + "_not_" + suffixes[0]

    elif suffix_pattern_sep is not None:
        filename = filename.replace(".json", "")
        filename = filename.rsplit(suffix_pattern_sep, 3)
        filename = filename[0].rsplit(suffix_sep, 1) + [filename[1]]
        filename1 = filename[0] + suffix_sep + filename[1] + "_not_" + filename[2]
        filename2 = filename[0] + suffix_sep + filename[2] + "_not_" + filename[1]

    return filename1, filename2

def process_data_entries(data, input_name, fileName, fileName2, bothDirections, ID, S1, S2, TESTS, S1SUBS2, S2SUBS1, error_log_rows, new_dict):
    for i in data:
        # print("Processing: " + fileName + " Case " + str(i[ID]))
        tests = i.get(TESTS)
        s1Subs2 = None if tests is None else 9
        if tests and S1SUBS2 in tests:
            s1Subs2 = bool_to_int(tests[S1SUBS2])
        schema_result, hadRetry, elapsed = analyze_schema(i[S1], i[S2], input_name, fileName, i[ID], error_log_rows)
        new_dict["fileName"].append(fileName)
        new_dict["id"].append(i[ID])
        new_dict["s1SUBs2"].append(s1Subs2)
        new_dict["IBM_s1SUBs2"].append(bool_to_int(schema_result))
        new_dict["hadRetry"].append(hadRetry)
        new_dict["totalTime"].append(round(elapsed, 2))

        if bothDirections:
            # print("Processing: " + fileName2 + " Case " + str(i[ID]))
            s1Subs2 = None if tests is None else 9
            if tests and S2SUBS1 in tests:
                s1Subs2 = bool_to_int(tests[S2SUBS1])
            schema_result, hadRetry, elapsed = analyze_schema(i[S2], i[S1], input_name, fileName2, i[ID], error_log_rows)
            new_dict["fileName"].append(fileName2)
            new_dict["id"].append(i[ID])
            new_dict["s1SUBs2"].append(s1Subs2)
            new_dict["IBM_s1SUBs2"].append(bool_to_int(schema_result))
            new_dict["hadRetry"].append(hadRetry)
            new_dict["totalTime"].append(round(elapsed, 2))
    return new_dict

def extractAndCheck(pathToFolder, error_log_rows, bothDirections=False, suffixes=None, suffix_sep=None, suffix_pattern_sep=None):
    ID = "id"
    S1 = "schema1"
    S2 = "schema2"
    TESTS = "tests"
    S1SUBS2 = "s1SubsetEqOfs2"
    S2SUBS1 = "s2SubsetEqOfs1"
    new_dict = {
        "fileName": [],
        "id": [],
        "s1SUBs2": [],
        "IBM_s1SUBs2": [],
        "hadRetry": [],
        "totalTime": []
    }
    if suffixes is not None and suffix_pattern_sep is not None:
        print("Warning: only one of suffixes and suffix_pattern_sep can be used. Aborting.")
        return None

    if suffix_sep is not None and (suffixes is None and suffix_pattern_sep is None):
        print("Warning: suffix_sep is only used when suffixes or suffix_pattern_sep are used. Aborting.")
        return None

    if bothDirections and suffixes is None and suffix_pattern_sep is None:
        print("Warning: bothDirections can only be used when suffixes or suffix_pattern_sep are used. Aborting.")
        return None

    # get all json files in pathToFolder if pathToFolder is a directory
    if path.isdir(pathToFolder):
        files = [f for f in listdir(pathToFolder) if isfile(join(pathToFolder, f)) and f.endswith(".json")]
    else:
        files = [pathToFolder]

    for f in files:
        if bothDirections:
            fileName, fileName2 = rename_file(f, suffixes, suffix_sep, suffix_pattern_sep)
        else:
            fileName = f.replace(".json", "")
            fileName2 = None

        with open(path.join(pathToFolder, f), "r") as inputFile:
            data = json.load(inputFile)
        process_data_entries(
            data,
            path.basename(f),
            fileName,
            fileName2,
            bothDirections,
            ID,
            S1,
            S2,
            TESTS,
            S1SUBS2,
            S2SUBS1,
            error_log_rows,
            new_dict
        )

    return new_dict


def write_error_log(error_log_path, error_log_rows):
    fieldnames = ["time", "input", "filename", "id", "error_name", "error_message"]
    with open(error_log_path, "w", newline="") as error_log_file:
        writer = csv.DictWriter(error_log_file, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(error_log_rows)


def runSubschemaTestsTestSuite(schemaPairs, output, error_log_path, isTestSuite=False):
    error_log_rows = []
    if isTestSuite:
        # Test Suite Containment tests have a different folder strucutre and we need to apply
        # manual corrections to reproduce the results
        columns = extractAndCheckTestSuite(schemaPairs, error_log_rows)
        sort_by = ["subFolder", "subSubFolder", "fileName", "id"]
    else:
        if "allOf_containment_schemaPairs" in schemaPairs:
            columns = extractAndCheck(schemaPairs, error_log_rows, True, suffixes=["orig", "merge"], suffix_sep=".")
        elif "schemastore_containment_schemaPairs" in schemaPairs:
            columns = extractAndCheck(schemaPairs, error_log_rows, True, suffix_sep="_", suffix_pattern_sep="_and_")
        elif "oneOf_as_anyOf_schemapairs" in schemaPairs:
            columns = extractAndCheck(schemaPairs, error_log_rows, True, suffix_sep=".", suffix_pattern_sep="_vs_")
        elif "oneOf_as_anyOf_schemaPairs" in schemaPairs:
            columns = extractAndCheck(schemaPairs, error_log_rows, True, suffix_sep=".", suffix_pattern_sep="_vs_")
        elif "additional_as_uneval_schemaPairs" in schemaPairs:
            columns = extractAndCheck(schemaPairs, error_log_rows, True, suffix_sep=".", suffix_pattern_sep="_vs_")
        elif "uneval_as_additional_schemaPairs" in schemaPairs:
            columns = extractAndCheck(schemaPairs, error_log_rows, True, suffix_sep=".", suffix_pattern_sep="_vs_")
        elif "uneval_as_items_schemaPairs" in schemaPairs:
            columns = extractAndCheck(schemaPairs, error_log_rows, True, suffix_sep=".", suffix_pattern_sep="_vs_")
        else:
            columns = extractAndCheck(schemaPairs, error_log_rows)
        if columns is None:
            return
        sort_by = ["fileName", "id"]

    write_columns_csv(output, columns, sort_by)
    write_error_log(error_log_path, error_log_rows)


if __name__ == "__main__":
    parser = ArgumentParser()
    parser.add_argument("-i", "--input", help="Input directory", required=True)
    parser.add_argument("-o", "--output", help="Output file for results", required=True)
    parser.add_argument("--error-log", help="Output file for structured error log")
    parser.add_argument("-t", "--testsuite", help="Flag to indicate if the input is the containment test suite", action="store_true", default=False)
    parser.add_argument("--timeout", help="Timeout for each test case", default=0)
    args = parser.parse_args()

    TIMEOUT = math.ceil(int(args.timeout)/1000)
    error_log_path = args.error_log or args.output.replace(".csv", "_errors.csv")

    runSubschemaTestsTestSuite(args.input, args.output, error_log_path, args.testsuite)
