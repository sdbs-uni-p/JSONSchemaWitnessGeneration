#!/usr/local/bin/python3.9

import json
import os
from datetime import datetime
from os import listdir, path
from os.path import isfile, join

import pandas as pd
from jsonref import JsonRefError
from jsonschema.exceptions import SchemaError
from jsonsubschema.api import isSubschema
from jsonsubschema.exceptions import (
    UnsupportedEnumCanonicalization,
    UnsupportedNegatedArray,
    UnsupportedNegatedObject,
    UnsupportedRecursiveRef,
)
from argparse import ArgumentParser


pd.set_option("display.max_columns", None)

def analyze_schema(s1, s2):
    try:
        return isSubschema(s1, s2)
    except RecursionError:
        return "RecursionError"
    except AttributeError:
        return "AttributeError"
    except JsonRefError:
        return "JsonRefError"
    except NameError:
        return "NameError"
    except ValueError:
        return "ValueError"
    except SchemaError:
        # this error may be raised when the schema is beyond v4
        # this error is also raised when required is used in v3-schemas (known issue)
        return "SchemaError"
    except SystemExit:
        return "SystemExit"
    except UnsupportedRecursiveRef:
        return "UnsupportedRecursiveRef"
    except UnsupportedEnumCanonicalization:
        return "UnsupportedEnumCanonicalization"
    except UnsupportedNegatedObject:
        return "UnsupportedNegatedObject"
    except UnsupportedNegatedArray:
        return "UnsupportedNegatedArray"
    except Exception as e:
        return str(e)


def bool_to_int(b):
    if b == True:
        return 1
    elif b == False:
        return 0
    else:
        return b


def extractAndCheckTestSuite(pathToFolder):
    ID = "id"
    S1 = "schema1"
    S2 = "schema2"
    TESTS = "tests"
    S1SUBS2 = "s1SubsetEqOfs2"
    S2SUBS1 = "s2SubsetEqOfs1"
    new_dict = {
        "subFolder": [],
        "subSubFolder": [],
        "fileName": [],
        "id": [],
        "s1SUBs2": [],
        "IBM_s1SUBs2": [],
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
                for f in listdir(subFolderPath + "/" + subSubFolder)
                if isfile(join(subFolderPath + "/" + subSubFolder, f))
                and f.endswith(".json")
            ]
            for f in files:
                fileName = f.replace(".json", "")
                inputFile = open(subSubFolderPath + "/" + f, "r")
                data = json.load(inputFile)
                inputFile.close()
                for i in data:
                    print("Processing: " + fileName + " Case " + str(i[ID]))
                    d = i
                    id = d[ID]
                    s1 = d[S1]
                    s2 = d[S2]
                    tests = d[TESTS]
                    s1Subs2 = 9
                    IBM_s1Subs2 = 9
                    if S1SUBS2 in tests:
                        s1Subs2 = bool_to_int(tests[S1SUBS2])
                        start = datetime.now()
                        IBM_s1Subs2 = bool_to_int(analyze_schema(s1, s2))
                        end = datetime.now()
                        total = end - start
                        totalTime = round(total.total_seconds() * 1000, 2)
                    new_dict["subFolder"].append(subFolder)
                    new_dict["subSubFolder"].append(subSubFolder)
                    new_dict["fileName"].append(fileName)
                    new_dict["id"].append(id)
                    new_dict["s1SUBs2"].append(s1Subs2)
                    new_dict["IBM_s1SUBs2"].append(IBM_s1Subs2)
                    new_dict["totalTime"].append(totalTime)     
    df = pd.DataFrame(new_dict)
    return df


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

def extractAndCheck(pathToFolder, bothDirections=False, suffixes=None, suffix_sep=None, suffix_pattern_sep=None):
    ID = "id"
    S1 = "schema1"
    S2 = "schema2"
    TESTS = "tests"
    S1SUBS2 = "s1SubsetEqOfs2"
    new_dict = {
        "fileName": [],
        "id": [],
        "s1SUBs2": [],
        "IBM_s1SUBs2": [],
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
    
    # get all json files in pathToFolder
    files = [f for f in listdir(pathToFolder) if isfile(join(pathToFolder, f)) and f.endswith(".json")]
    

    for f in files:
        fileName = f.replace(".json", "")
        if bothDirections:
            fileName, fileName2 = rename_file(f, suffixes, suffix_sep, suffix_pattern_sep)
        inputFile = open(pathToFolder + "/" + f, "r")
        data = json.load(inputFile)
        inputFile.close()
        for i in data:
            print("Processing: " + fileName + " Case " + str(i[ID]))
            tests = i[TESTS] if TESTS in i else None
            s1Subs2 = 9
            IBM_s1Subs2 = 9
            if tests is None:
                s1Subs2 = None 
            elif S1SUBS2 in tests:
                s1Subs2 = bool_to_int(tests[S1SUBS2])
            start = datetime.now()
            IBM_s1Subs2 = bool_to_int(analyze_schema(i[S1], i[S2]))
            total = datetime.now() - start
            totalTime = round(total.total_seconds() * 1000, 2)
            new_dict["fileName"].append(fileName)
            new_dict["id"].append(i[ID])
            new_dict["s1SUBs2"].append(s1Subs2)
            new_dict["IBM_s1SUBs2"].append(IBM_s1Subs2)
            new_dict["totalTime"].append(totalTime)
            if bothDirections:
                print("Processing: " + fileName2 + " Case " + str(i[ID]))
                s1Subs2 = 9
                IBM_s1Subs2 = 9
                if tests is None:
                    s1Subs2 = None 
                elif S1SUBS2 in tests:
                    s1Subs2 = bool_to_int(tests[S1SUBS2])
                start = datetime.now()
                IBM_s1Subs2 = bool_to_int(analyze_schema(i[S2], i[S1]))
                total = datetime.now() - start
                totalTime = round(total.total_seconds() * 1000, 2)
                new_dict["fileName"].append(fileName2)
                new_dict["id"].append(i[ID])
                new_dict["s1SUBs2"].append(s1Subs2)
                new_dict["IBM_s1SUBs2"].append(IBM_s1Subs2)
                new_dict["totalTime"].append(totalTime)

    df = pd.DataFrame(new_dict)
    return df


def runSubschemaTestsTestSuite(schemaPairs, output, isTestSuite=False):    
    if isTestSuite:
        # Test Suite Containment tests have a different folder strucutre and we need to apply
        # manual corrections to reproduce the results
        subschemaDF = extractAndCheckTestSuite(schemaPairs)
        subschemaDF = subschemaDF.sort_values(by=["subFolder", "subSubFolder", "fileName", "id"])
    else:
        if "allOf_containment_schemaPairs" in schemaPairs:
            subschemaDF = extractAndCheck(schemaPairs, True, suffixes=["orig", "merge"], suffix_sep=".")
        elif "schemastore_containment_schemaPairs" in schemaPairs:
            subschemaDF = extractAndCheck(schemaPairs, True, suffix_sep="_", suffix_pattern_sep="_and_")
        else:
            subschemaDF = extractAndCheck(schemaPairs)
        if subschemaDF is None:
            return
        subschemaDF = subschemaDF.sort_values(by=["fileName", "id"])

    with open(output, "w") as f:
        f.write(subschemaDF.to_csv(index=False))


if __name__ == "__main__":
    parser = ArgumentParser()
    parser.add_argument("-i", "--input", help="Input directory", required=True)
    parser.add_argument("-o", "--output", help="Output file for results", required=True)
    parser.add_argument("-t", "--testsuite", help="Flag to indicate if the input is the containment test suite", action="store_true", default=False)
    args = parser.parse_args()
    
    runSubschemaTestsTestSuite(args.input, args.output, args.testsuite)
