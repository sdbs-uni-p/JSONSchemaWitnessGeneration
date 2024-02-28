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
    failure = len(df[((df["genSuccess"] == False) | (df["genSuccess"].isnull()))
                     | ((df["genSuccess"] == True) & (df["valid"].isnull()))])
    satLogicalErrors = len(df[(df["genSuccess"] == True) & ((df["valid"] == False))])
    time = df["totalTime"].dropna().tolist()

    return success, failure, satLogicalErrors, time


def eval_unsat(df):
    success = len(df[df["genSuccess"] == False])
    failure = len(df[df["genSuccess"].isnull() | ((df["genSuccess"] == True) & (df["valid"].isnull()))])
    unsatLogicalErrors = len(df[((df["genSuccess"] == True) & ((df["valid"] == True) | (df["valid"] == False)))])
    time = df["totalTime"].dropna().tolist()

    return success, failure, unsatLogicalErrors, time


def eval_all(df):
    valid_witness = len(df[(df["genSuccess"] == True) & (df["valid"] == True)])
    invalid_witness = len(df[(df["genSuccess"] == True) & (df["valid"] == False)])
    unsatisfiable = len(df[df["genSuccess"] == False])
    exception = len(df[df["genSuccess"].isnull()]) # genSuccess should only be null if an exception occured
    exception_timeout = df[df.columns[1:]].eq("TimeoutException").any(axis=1).sum()
    time = df["totalTime"].dropna().tolist()
    
    return valid_witness, invalid_witness, unsatisfiable, exception, exception_timeout, time
    

def print_results(valid_witness, invalid_witness, unsatisfiable, exception, exception_timeout, time, corrections, header=""):
    if corrections is None:
        corrections = {}
    valid_witness += 0 if "Success" not in corrections else corrections["Success"]["value"]
    invalid_witness += 0 if "Logical Errors (sat)" not in corrections else corrections["Logical Errors (sat)"]["value"]
    exception += 0 if "Failure" not in corrections else corrections["Failure"]["value"]
    
    if len(header) > 0:
      print(header)  
    processed = valid_witness + invalid_witness + unsatisfiable + exception
    print(f"\tGenerated Valid Witness: {valid_witness} ({round(100 * valid_witness / processed, 2)}% of processed schemas)")
    print(f"\tGenerated Invalid Witness: {invalid_witness} ({round(100 * invalid_witness / processed, 2)}% of processed schemas)")
    print(f"\tConsidered Unsatisfiable by the Tool: {unsatisfiable} ({round(100 * unsatisfiable / processed, 2)}% of processed schemas)")
    print(f"\tExceptions: {exception} ({round(100 * exception / processed, 2)}% of processed schemas)")
    print(f"\t\tIncludes {exception_timeout} Timeouts ({round(100 * exception_timeout / processed, 2)}% of total of processed schemas)")
    
    med_time = round(statistics.median(map(float, time)) / 1000, 3)
    print(f"\tMedian Time: {med_time}s")
    p95_time = round(np.percentile(time, 95) / 1000, 3)
    print(f"\t95th Percentile Time: {p95_time}s")
    avg_time = round(statistics.mean(map(float, time)) / 1000, 3)
    print(f"\tAverage Time: {avg_time}s")
    if any(corrections):
        print("\tCorrections:")
    if "Success" in corrections:
        print(f"\t\t{corrections['Success']['description']}\n",
              f"\t\t\t => Adjusted \"Generated Valid Witness (sat)\" by {corrections['Success']['value']}")
    if "Logical Errors (sat)" in corrections:
        print(f"\t\t{corrections['Logical Errors (sat)']['description']}\n",
              f"\t\t\t => Adjusted \"Generated Invalid Witness (sat)\" by {corrections['Logical Errors (sat)']['value']}")
    if "Failure" in corrections:
        print(f"\t\t{corrections['Failure']['description']}\n",
              f"\t\t\t => Adjusted \"Exceptions\" by {corrections['Failure']['value']}")
        
        
        
def print_results_gt(valid_witness_sat, invalid_witness_sat, unsatisfiable_sat, exception_sat, exception_timeout_sat, time_sat, 
                     valid_witness_unsat, invalid_witness_unsat, unsatisfiable_unsat, exception_unsat, exception_timeout_unsat, time_unsat, corrections):
    exception = exception_sat + exception_unsat
    exception_timeout = exception_timeout_sat + exception_timeout_unsat
    time = time_sat + time_unsat
    
    if corrections is None:
        corrections = {}
    valid_witness_sat += 0 if "Success" not in corrections else corrections["Success"]["value"] # "Success" could also include corrections for unsat but we currently have no such case
    invalid_witness_sat += 0 if "Logical Errors (sat)" not in corrections else corrections["Logical Errors (sat)"]["value"]
    exception += 0 if "Failure" not in corrections else corrections["Failure"]["value"]

    processed = valid_witness_sat + invalid_witness_sat + unsatisfiable_sat + exception + valid_witness_unsat + invalid_witness_unsat + unsatisfiable_unsat
    print(f"\tGenerated Valid Witness (sat): {valid_witness_sat} ({round(100 * valid_witness_sat / processed, 2)}% of processed schemas)")
    print(f"\tGenerated Invalid Witness (sat): {invalid_witness_sat} ({round(100 * invalid_witness_sat / processed, 2)}% of processed schemas)")
    print(f"\tConsidered Unsatisfiable by the Tool (sat): {unsatisfiable_sat} ({round(100 * unsatisfiable_sat / processed, 2)}% of processed schemas)")
    print(f"\tGenerated Valid Witness (unsat): {valid_witness_unsat} ({round(100 * valid_witness_unsat / processed, 2)}% of processed schemas)")
    print(f"\tGenerated Invalid Witness (unsat): {invalid_witness_unsat} ({round(100 * invalid_witness_unsat / processed, 2)}% of processed schemas)")
    print(f"\tConsidered Unsatisfiable by the Tool (unsat): {unsatisfiable_unsat} ({round(100 * unsatisfiable_unsat / processed, 2)}% of processed schemas)")
    print(f"\tExceptions: {exception} ({round(100 * exception / processed, 2)}% of processed schemas)")
    print(f"\t\tIncludes {exception_timeout} Timeouts ({round(100 * exception_timeout / processed, 2)}% of total of processed schemas)")
    
    med_time = round(statistics.median(map(float, time)) / 1000, 3)
    print(f"\tMedian Time: {med_time}s")
    p95_time = round(np.percentile(time, 95) / 1000, 3)
    print(f"\t95th Percentile Time: {p95_time}s")
    avg_time = round(statistics.mean(map(float, time)) / 1000, 3)
    print(f"\tAverage Time: {avg_time}s")
    if any(corrections):
        print("\tCorrections:")
    if "Success" in corrections:
        print(f"\t\t{corrections['Success']['description']}\n",
              f"\t\t\t => Adjusted \"Generated Valid Witness (sat)\" by {corrections['Success']['value']}")
    if "Logical Errors (sat)" in corrections:
        print(f"\t\t{corrections['Logical Errors (sat)']['description']}\n",
              f"\t\t\t => Adjusted \"Generated Invalid Witness (sat)\" by {corrections['Logical Errors (sat)']['value']}")
    if "Failure" in corrections:
        print(f"\t\t{corrections['Failure']['description']}\n",
              f"\t\t\t => Adjusted \"Exceptions\" by {corrections['Failure']['value']}")


def run_evaluation(config, tool, dataset):
    print("")
    if tool not in config["filenames"]:
        print(f"Tool {tool} not configured. Skipping")
        return
    if dataset not in config["datasets"]:
        print(f"Dataset {dataset} not configured. Skipping")
        return
    
    paths = config["datasets"][dataset]["paths"]
    if "sat" not in paths and "unsat" not in paths and "unknown" not in paths:
        print(f"Paths for dataset {dataset} are not configured properly")
        return

    if tool == "DG":
        tool_str = "jsongenerator (DG)"
    else:
        tool_str = tool
    print(f"Dataset: {dataset}\nTool:\t {tool_str}")

    total = config["datasets"][dataset]["files"]
    processed = 0
    corrections = None
    if ("corrections" in config["datasets"][dataset] and tool in config["datasets"][dataset]["corrections"]):
        corrections = config["datasets"][dataset]["corrections"][tool]
    if "unknown" in paths:
        if "sat" in paths or "unsat" in paths:
            print(f"Error: Mixing schemas without ground truth and schemas with ground truth in the same dataset is currently not supported. Skipping dataset {dataset}.")
            return f"{dataset},{tool},NA,NA,NA,NA,NA,NA,NA\n"
        path = f'{config["base_path"]}/{paths["unknown"]}/{config["filenames"][tool]}'
        if not os.path.exists(path):
            print(f"\033[93mWARN: File at {path} not found. Skipping dataset.\033[0m")
        else:
            df = readDF(path)
            if df is None or df.empty:
                print(f"\033[93mWARN: File at {path} is empty. Skipping dataset.\033[0m")
            else:
                valid_witness, invalid_witness, unsatisfiable, exception, exception_timeout, time = eval_all(df)
                print_results(valid_witness, invalid_witness, unsatisfiable, exception, exception_timeout, time, 
                              corrections, "No ground truth is defined for the following results.")
                processed += valid_witness + invalid_witness + unsatisfiable + exception
    else:
        has_sat = False
        has_unsat = False
        if "sat" in paths:
            path = f'{config["base_path"]}/{paths["sat"]}/{config["filenames"][tool]}'
            if not os.path.exists(path):
                print(f"Results for satisfiable schemas at {path} not found. Skipping case.")
            else:
                df = readDF(path)    
                if df is None or df.empty:
                    print(f"Results for satisfiable schemas at {path} is empty. Skipping case.")
                else:
                    has_sat = True
                    valid_witness_sat, invalid_witness_sat, unsatisfiable_sat, exception_sat, exception_timeout_sat, time_sat = eval_all(df)
                    processed += valid_witness_sat + invalid_witness_sat + unsatisfiable_sat + exception_sat
        else:
            valid_witness_sat = 0
            invalid_witness_sat = 0
            unsatisfiable_sat = 0
            exception_sat = 0
            exception_timeout_sat = 0
            time_sat = []
            
        if "unsat" in paths:
            path = f'{config["base_path"]}/{paths["unsat"]}/{config["filenames"][tool]}'
            if not os.path.exists(path):
                print(f"Results for satisfiable schemas at {path} not found. Skipping case.")
            else:
                df = readDF(path)
                if df is None or df.empty:
                    print(f"Results for satisfiable schemas at {path} is empty. Skipping case.")
                else:
                    has_unsat = True
                    valid_witness_unsat, invalid_witness_unsat, unsatisfiable_unsat, exception_unsat, exception_timeout_unsat, time_unsat = eval_all(df)
                    processed += valid_witness_unsat + invalid_witness_unsat + unsatisfiable_unsat + exception_unsat
        else:
            valid_witness_unsat = 0
            invalid_witness_unsat = 0
            unsatisfiable_unsat = 0
            exception_unsat = 0
            exception_timeout_unsat = 0
            time_unsat = []
            
        if not (has_sat or has_unsat):
            print(f"No files found for dataset \"{dataset}\". Skipping dataset.")
            return
            
        print_results_gt(valid_witness_sat, invalid_witness_sat, unsatisfiable_sat, exception_sat, exception_timeout_sat, time_sat,
                         valid_witness_unsat, invalid_witness_unsat, unsatisfiable_unsat, exception_unsat, exception_timeout_unsat, time_unsat, corrections)

    print(f"\tProcessed {processed} of {total} schemas")


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
        print(f"File at {path} is empty. Skipping dataset.")
        
    print(f"Dataset: {dataset}\nTool:\t {tool}")
    if df["s1SUBs2"].isnull().all():
        # No ground truth is defined
        print(f"No ground truth is defined for the following results.")
        no_error = len(df[(df["IBM_s1SUBs2"] == "1") | (df["IBM_s1SUBs2"] == "0")])
        error = len(df[(df["IBM_s1SUBs2"] != "1") & (df["IBM_s1SUBs2"] != "0")])
        processed = no_error + error
        time = df["totalTime"].dropna().tolist()
        print(f"\tCompleted without Exception: {no_error} ({round(100 * no_error / len(df), 2)}% of processed schema pairs)")
        print(f"\tExceptions: {error} ({round(100 * error / len(df), 2)}% of processed schema pairs)")
    elif df["s1SUBs2"].isin([0, 1]).all():
        # Ground truth is defined
        result_correct_sat = len(df[(df["s1SUBs2"] == 1) & (df["IBM_s1SUBs2"] == "1")])
        result_correct_unsat = len(df[(df["s1SUBs2"] == 0) & (df["IBM_s1SUBs2"] == "0")])
        result_wrong_sat = len(df[(df["s1SUBs2"] == 0) & (df["IBM_s1SUBs2"] == "1")])
        result_wrong_unsat = len(df[(df["s1SUBs2"] == 1) & (df["IBM_s1SUBs2"] == "0")])
        # errors occured when "IBM_s1SUBs2" is neither 0 nor 1
        error_sat = len(df[(df["s1SUBs2"] == 1) & (df["IBM_s1SUBs2"] != "0") & (df["IBM_s1SUBs2"] != "1")])
        error_unsat = len(df[(df["s1SUBs2"] == 0) & (df["IBM_s1SUBs2"] != "0") & (df["IBM_s1SUBs2"] != "1")])
        processed = result_correct_sat + result_wrong_sat + result_correct_unsat + result_wrong_unsat + error_sat + error_unsat
        
        time = df["totalTime"].dropna().tolist()
        if processed > 0:
            print(f"\tProduced correct result (sat): {result_correct_sat} ({round(100 * result_correct_sat / processed, 2)}% of processed schema pairs)")
            print(f"\tProduced wrong result (sat): {result_wrong_sat} ({round(100 * result_wrong_sat / processed, 2)}% of processed schema pairs)")
            print(f"\tProduced correct result (unsat): {result_correct_unsat} ({round(100 * result_correct_unsat / processed, 2)}% of processed schema pairs)")
            print(f"\tProduced wrong result (unsat): {result_wrong_unsat} ({round(100 * result_wrong_unsat / processed, 2)}% of processed schema pairs)")
            print(f"\tExceptions (sat): {error_sat} ({round(100 * error_sat / processed, 2)}% of processed schema pairs)")
            print(f"\tExceptions (unsat): {error_unsat} ({round(100 * error_unsat / processed, 2)}% of processed schema pairs)")
            
            
            print(f"\tMedian Time: {round(statistics.median(map(float, time)) / 1000, 3)}s")
            print(f"\t95th Percentile Time: {round(np.percentile(time, 95) / 1000, 3)}s")
            print(f"\tAverage Time: {round(statistics.mean(map(float, time)) / 1000, 3)}s")
        
    else:
        print(f"Ground truth is only partially defined for dataset {dataset}. Skipping dataset.")
    print(f"\tProcessed {processed} of {dataset_config['files']} schema pairs")
        
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
        run_evaluation(conf, c[1], c[0])

    cc_datasets = ["Test Suite Containment", "allOf Containment", "Schemastore Containment"]

    for d in cc_datasets:
        evalSubschema(conf, "CC", d)
