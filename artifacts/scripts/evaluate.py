#!/usr/local/bin/python3.9
import itertools
import json
import os
import statistics
from pathlib import Path

import numpy as np
import pandas as pd

pd.set_option("display.max_columns", None)


def readDF(path):
    try:
        df = pd.read_csv(path).convert_dtypes()
    except pd.errors.EmptyDataError:
        return

    df["genSuccess"] = df["genSuccess"].map(
        {"true": True, "false": False, True: True, False: False}
    )
    df["valid"] = df["valid"].map(
        {
            "true": True,
            "false": False,
            True: True,
            False: False,
            "JsonSchemaException": True,
            "ExecutionException": True,
        }
    )

    return df

def eval_sat(df):
    success = len(df[(df["genSuccess"] == True) & (df["valid"] == True)])
    failure = len(
        df[
            ((df["genSuccess"] == False) | (df["genSuccess"].isnull()))
            | ((df["genSuccess"] == True) & (df["valid"].isnull()))
        ]
    )
    satLogicalErrors = len(df[(df["genSuccess"] == True) & ((df["valid"] == False))])
    time = df["totalTime"].dropna().tolist()

    return success, failure, satLogicalErrors, time


def eval_unsat(df):
    success = len(df[df["genSuccess"] == False])
    failure = len(
        df[
            df["genSuccess"].isnull()
            | ((df["genSuccess"] == True) & (df["valid"].isnull()))
        ]
    )
    unsatLogicalErrors = len(
        df[
            (
                (df["genSuccess"] == True)
                & ((df["valid"] == True) | (df["valid"] == False))
            )
        ]
    )

    time = df["totalTime"].dropna().tolist()

    return success, failure, unsatLogicalErrors, time


def print_results(type, val, total, corrections):
    if val is None:
        print(f"\t#{type}: na")
        return "NA"

    result_str = ""
    if corrections and type in corrections:
        val += corrections[type]["value"]
        change_type = "Increased" if corrections[type]["value"] >= 0 else "Decreased"
        result_str = (
            f'\n\t\tCorrection: {corrections[type]["description"]}\n'
            f'\t\t\t=> {change_type} {type} by {abs(corrections[type]["value"])}'
        )

    percentage = round(100 * (val) / total, 2)
    print(f"\t#{type}: {val} ({percentage}%){result_str}")

    return percentage

def eval_schemastore_containment(df):
    success = len(df[(df["genSuccess"] == True) | (df["genSuccess"] == False)])
    failure = len(df[df["genSuccess"].isnull()])
    timeout = df[df.columns[1:]].eq("TimeoutException").any(axis=1).sum()
    satLogicalErrors = None
    time = df["totalTime"].dropna().tolist()

    return success, failure, timeout, satLogicalErrors, time

def run_evaluation(config, tool, dataset):
    print("")
    if tool not in config["filenames"]:
        print(f"Tool {tool} not configured. Skipping")
        return
    if dataset not in config["datasets"]:
        print(f"Dataset {dataset} not configured. Skipping")
        return
    paths = config["datasets"][dataset]["paths"]
    if "sat" not in paths and "unsat" not in paths:
        print(f"Paths for dataset {dataset} are not configured properly")
        return

    if tool == "DG":
        tool_str = "jsongenerator (DG)"
    else:
        tool_str = tool
    print(f"Dataset: {dataset}\nTool:\t {tool_str}")

    total = config["datasets"][dataset]["files"]
    sat_success, sat_failure, sat_time = 0, 0, []
    unsat_success, unsat_failure, unsat_time = 0, 0, []
    sat_logical_errors, unsat_logical_errors = None, None
    has_sat, has_unsat = False, False
    is_schema_store_containment = dataset == "Schemastore Containment"
    if "sat" in paths:
        path = f'{config["base_path"]}/{paths["sat"]}/{config["filenames"][tool]}'
        if not os.path.exists(path):
            print(f"\033[93mWARN: File at {path} not found. Skipping dataset.\033[0m")
        else:
            df = readDF(path)
            # TODO: integrate properly
    
            if df is None or df.empty:
                print(f"\033[93mWARN: File at {path} is empty. Skipping dataset.\033[0m")
            else:
                if is_schema_store_containment:
                    print(f"No ground truth defined. Only checking for sucess and failure")
                    sat_success, sat_failure, timeout, sat_logical_errors, sat_time = eval_schemastore_containment(df)
                else:
                    sat_success, sat_failure, sat_logical_errors, sat_time = eval_sat(df)
                has_sat = True
    if not is_schema_store_containment and "unsat" in paths:
        path = f'{config["base_path"]}/{paths["unsat"]}/{config["filenames"][tool]}'
        if not os.path.exists(path):
            print(f"\033[93mWARN: File at {path} not found. Skipping dataset.\033[0m")
        else:
            df = readDF(path)
            if df is None or df.empty:
                print(f"\033[93mWARN: File at {path} is empty. Skipping dataset.\033[0m")
            else:
                (
                    unsat_success,
                    unsat_failure,
                    unsat_logical_errors,
                    unsat_time,
                ) = eval_unsat(df)
                has_unsat = True

    # Return empty line if datasets are missing
    if not (has_sat or has_unsat):
        return f"{dataset},{tool},NA,NA,NA,NA,NA,NA,NA\n"

    success = sat_success + unsat_success
    failure = sat_failure + unsat_failure
    time = sat_time + unsat_time

    if len(time) == 0:
        print("Found no execution times in results. Does the file contain any entries?\nSkipping dataset ...")
        return f"{dataset},{tool},NA,NA,NA,NA,NA,NA,NA\n"

    corrections = None
    if ("corrections" in config["datasets"][dataset] and tool in config["datasets"][dataset]["corrections"]):
        corrections = config["datasets"][dataset]["corrections"][tool]

    total_processed = success + failure + (sat_logical_errors or 0) + (unsat_logical_errors or 0)
    failure += total - total_processed
    
    types = [
        ("Success", success),
        ("Failure", failure),
        ("Logical Errors (sat)", sat_logical_errors),
        ("Logical Errors (unsat)", unsat_logical_errors),
    ]
    results = ""
    for t in types:
        results += str(print_results(t[0], t[1], total, corrections)) + ","
        if t[0] == "Failure" and is_schema_store_containment:
            print("\t\tIncludes " + str(timeout) + " timeouts (" + str(round(100 * timeout / total, 2)) + "%)")
    results = results[:-1]

    med_time = round(statistics.median(map(float, time)) / 1000, 3)
    print(f"\tMedian Time: {med_time}s")
    p95_time = round(np.percentile(time, 95) / 1000, 3)
    print(f"\t95th Percentile Time: {p95_time}s")
    avg_time = round(statistics.mean(map(float, time)) / 1000, 3)
    print(f"\tAverage Time: {avg_time}s")

    csv = f"{dataset},{tool},{results},{med_time},{p95_time},{avg_time}\n"
    return csv


def evalSubschema(config, tool, dataset):
    print("")
    if tool not in config["filenames"]:
        print(f"Tool {tool} not configured. Skipping")
        return
    if dataset not in config["datasets"]:
        print(f"Dataset {dataset} not configured. Skipping")
        return
    dataset_config = config["datasets"][dataset]
    if "schemaPairs" not in dataset_config["paths"]:
        print(f"Dataset {dataset} does not have schemaPairs configured. Skipping")
        return

    path = f'{config["base_path"]}/{dataset_config["paths"]["schemaPairs"]}/{config["filenames"][tool]}'
    if not os.path.exists(path):
        print(f"\033[93mWARN: File at {path} not found. Skipping dataset.\033[0m")
        return f"{dataset},CC,NA,NA,NA,NA,NA,NA,NA\n"

    df = pd.read_csv(path)
    if df is None or df.empty:
        print(f"\033[93mWARN: File at {path} is empty. Skipping dataset.\033[0m")
        
    # if column s1SUBs2 is not empty or not present, we have no ground truth
    has_ground_truth = "s1SUBs2" in df and not df["s1SUBs2"].isnull().all()
    
    # TODO: currently only properly supports manual corrections for Test Suite Containment
    total_files = len(df)
    failure_offset = 0 
    if "corrections" in dataset_config and "CC" in dataset_config["corrections"]:
        if "Failure" in dataset_config["corrections"]["CC"]:
            failure_correction = dataset_config["corrections"]["CC"]["Failure"]
            failure_offset = failure_correction["value"] if "value" in failure_correction else 0
            failure_message = failure_correction["description"] if "description" in failure_correction else ""
            total_files += failure_offset
        else:
            print(f"Only Failure correction is currently supported for CC datasets")

    if has_ground_truth:
        # convert IBM_s1SUBs2 and s1SUBs2 to numeric
        df["IBM_s1SUBs2"] = pd.to_numeric(df["IBM_s1SUBs2"], errors="coerce")
        df["s1SUBs2"] = pd.to_numeric(df["s1SUBs2"], errors="coerce")

        success = len(df[df["IBM_s1SUBs2"] == df["s1SUBs2"]])

        notEqual = df[df["IBM_s1SUBs2"] != df["s1SUBs2"]]

        notEqual_dict = notEqual[["s1SUBs2", "IBM_s1SUBs2"]].value_counts().to_dict()
        notEqual_list = [(k[0], k[1], v) for k, v in notEqual_dict.items()]
        notEqualDF = pd.DataFrame(notEqual_list, columns=["s1SUBs2", "IBM_s1SUBs2", "count"])

        sat_logical_errors = notEqualDF[(notEqualDF["s1SUBs2"] == 0) & (notEqualDF["IBM_s1SUBs2"] == 1)]
        unsat_logical_errors = (df[(df["s1SUBs2"] == 1)]["IBM_s1SUBs2"].value_counts(dropna=False).to_frame())

        sat_logical_errors_count = sat_logical_errors.iloc[0]["count"] if len(sat_logical_errors) > 0 else 0
        unsat_logical_errors_count = unsat_logical_errors.loc[0]["IBM_s1SUBs2"] if len(unsat_logical_errors) > 0 else 0
        
        failure = (len(notEqual) - sat_logical_errors_count - unsat_logical_errors_count)
        success_perc = round(100 * success / total_files, 2)
        failure_perc = round(100 * (failure + failure_offset) / total_files, 2)
        sat_err_perc = round(100 * sat_logical_errors_count / total_files, 2)
        unsat_err_perc = round(100 * unsat_logical_errors_count / total_files, 2)
    else:
        failure = len(df[(df["IBM_s1SUBs2"] != "0") & (df["IBM_s1SUBs2"] != "1")])
        success = total_files - failure
        failure_perc = round(100 * (failure + failure_offset) / total_files, 2)
        success_perc = round(100 * success / total_files, 2)
        sat_logical_errors_count = "NA"
        unsat_logical_errors_count = "NA"
        sat_err_perc = "NA"
        unsat_err_perc = "NA"

    med_time = round(df.totalTime.median() / 1000, 3)
    p95_time = round(df.totalTime.quantile(0.95) / 1000, 3)
    avg_time = round(df.totalTime.mean() / 1000, 3)

    csv = f"{dataset},CC,{success_perc},{failure_perc},{sat_err_perc},{unsat_err_perc},{med_time},{p95_time},{avg_time}\n"

    print(f"\nDataset: {dataset}\nTool:\t jsonsubschema (CC)")
    if not has_ground_truth:
        print(f"No ground truth defined. Only checking for sucess and failure")
    print(f"\t#Success: {success} ({success_perc}%)")
    print(f"\t#Failure: {failure+failure_offset} ({failure_perc}%)")
    if failure_offset > 0:
        print(f"\t\tCorrection: {failure_message}\n",
            f"\t\t\t => Increased Failure by {failure_offset}",)
    print(f"\tLogical errors sat: {sat_logical_errors_count} ({sat_err_perc})%")
    print(f"\tLogical errors unsat: {unsat_logical_errors_count} ({unsat_err_perc}%)")
    print(f"\tMedian Time: {med_time}s")
    print(f"\t95th Percentile Time: {p95_time}s")
    print(f"\tAverage Time: {avg_time}s")
    
    return csv


if __name__ == "__main__":
    home = str(Path.home())

    with open(f"{home}/scripts/config.json") as f:
        conf = json.load(f)
    basePath = "dg_results/"
    datasets = [
        "GitHub",
        "Kubernetes",
        "Snowplow",
        "WashingtonPost",
        "Handwritten",
        "Test Suite Containment",
        "allOf Containment",
        "Schemastore Containment"
    ]
    tools = ["Ours", "DG"]

    combs = itertools.product(datasets, tools)
    results_csv = "dataset,tool,success,failure,errors sat,errors unsat,median time (s),95 percentile (s),average time (s)\n"
    for c in combs:
        results_csv += run_evaluation(conf, c[1], c[0])

    cc_datasets = ["Test Suite Containment", "allOf Containment", "Schemastore Containment"]

    for d in cc_datasets:
        results_csv += evalSubschema(conf, "CC", d)

    with open(f"{home}/results/results.csv", "w") as f:
        f.write(results_csv)
