#!/usr/local/bin/python3.9
import itertools
import json
import os
import statistics
from pathlib import Path

import numpy as np
import pandas as pd
import argparse

pd.set_option("display.max_columns", None)
TIMEOUT = np.inf


def readDF(path):
    try:
        df = pd.read_csv(path).convert_dtypes()
    except pd.errors.EmptyDataError:
        return

    df["genSuccess"] = df["genSuccess"].map(
        {"true": True, "false": False, True: True, False: False}
    )
    # map "true" and "false" to True and False, respectively and all other values to None
    df["valid"] = df["valid"].map(
        {"true": True, "false": False, True: True, False: False}
    )   

    return df


def eval_all(df):
    valid_witness = len(df[(df["genSuccess"] == True) & (df["valid"] == True) & (df["totalTime"] <= TIMEOUT)])
    invalid_witness = len(df[(df["genSuccess"] == True) & (df["valid"] == False) & (df["totalTime"] <= TIMEOUT)])
    no_witness = len(df[(df["genSuccess"] == False) & (df["totalTime"] <= TIMEOUT)])
    unsatisfiable = len(df[(df["noWitness"] == True) & (df["totalTime"] <= TIMEOUT)]) if "noWitness" in df else 0
    # check whether there is an unsatisfiable schema where genSuccess is true
    if unsatisfiable > 0 and len(df[(df["genSuccess"] == True) & (df["noWitness"] == True)]):
        print("Warning: There are unsatisfiable schemas (\"noWitness\" == True) where genSuccess is true. This should not happen.")
    validation_exception = len(df[(df["genSuccess"] == True) & df["valid"].isnull()])
    exception = len(df[df["genSuccess"].isnull()]) # genSuccess should only be null if an exception occured
    exception_timeout = len(df[df["totalTime"] > TIMEOUT])
    exception += validation_exception + exception_timeout
    exception_timeout += df[df.columns[1:]].eq("TimeoutException").any(axis=1).sum() # these timeout exceptions are already counted in exception
    df_no_timeout = df[df["totalTime"] <= TIMEOUT]
    time = df_no_timeout["totalTime"].dropna().tolist()
    
    return valid_witness, invalid_witness, no_witness, unsatisfiable, exception, exception_timeout, validation_exception, time
    

def print_results(valid_witness, invalid_witness, no_witness, unsatisfiable, exception, exception_timeout, validation_exception, time, corrections, total, header=""):
    if corrections is None:
        corrections = {}
    valid_witness += 0 if "Success" not in corrections else corrections["Success"]["value"]
    invalid_witness += 0 if "Logical Errors (sat)" not in corrections else corrections["Logical Errors (sat)"]["value"]
    exception += 0 if "Failure" not in corrections else corrections["Failure"]["value"]

    success = valid_witness + no_witness
    logical_errors = invalid_witness
    failure = exception
    timeouts = exception_timeout

    
    if len(header) > 0:
      print(header)  
    processed = valid_witness + invalid_witness + unsatisfiable + exception
    print(f"\t\"Success\" (Generated Valid Witness or no Witness): {success} ({round(100 * success / total, 2)}% of total schemas)")
    print(f"\t\"Logical Errors\" (Generated Invalid Witness)\": {logical_errors} ({round(100 * logical_errors / total, 2)}% of total schemas)")
    print(f"\t\"Failure\" (Exceptions): {failure} ({round(100 * exception / total, 2)}% of total schemas)")
    print(f"\t\tIncludes {timeouts} Timeouts ({round(100 * exception_timeout / total, 2)}% of total schemas)")
    if validation_exception > 0:
        print(f"\t\tIncludes {validation_exception} Validation Exceptions ({round(100 * validation_exception / total, 2)}% of total schemas)")
    
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


def print_results_gt(valid_witness_sat, invalid_witness_sat, no_witness_sat, unsatisfiable_sat, exception_sat, exception_timeout_sat, validation_exception_sat, time_sat, valid_witness_unsat, 
                     invalid_witness_unsat, no_witness_unsat, unsatisfiable_unsat, exception_unsat, exception_timeout_unsat, validation_exception_unsat, time_unsat, corrections, has_sat, has_unsat, total):
    exception = exception_sat + exception_unsat
    exception_timeout = exception_timeout_sat + exception_timeout_unsat
    time = time_sat + time_unsat
    
    if corrections is None:
        corrections = {}
    valid_witness_sat += 0 if "Success" not in corrections else corrections["Success"]["value"] # "Success" could also include corrections for unsat but we currently have no such case
    invalid_witness_sat += 0 if "Logical Errors (sat)" not in corrections else corrections["Logical Errors (sat)"]["value"]
    exception += 0 if "Failure" not in corrections else corrections["Failure"]["value"]
    success = valid_witness_sat + no_witness_unsat
    failure = exception
    timeouts = exception_timeout_sat + exception_timeout_unsat
    error_sat = invalid_witness_sat + no_witness_sat
    error_unsat = invalid_witness_unsat + valid_witness_unsat

    if has_sat > 0:
        print(f"\t\"Success\" (Generated Valid Witness (sat) + Generated no Witness (unsat)): {success} ({round(100 * success / total, 2)}% of total schemas)")
        print(f"\t\"Failure\" (Exceptions (sat) + Exception (unsat)): {failure} ({round(100 * failure / total, 2)}% of total schemas)")
        print(f"\t\tIncludes {timeouts} Timeouts ({round(100 * timeouts / total, 2)}% of total schemas)")
        print(f"\t\"Logical Errors (sat)\": {error_sat} ({round(100 * error_sat / total, 2)}% of total schemas)")
    if has_unsat > 0:
        print(f"\t\"Logical Errors (unsat)\": {error_unsat} ({round(100 * error_unsat / total, 2)}% of total schemas)")

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
              f"\t\t\t => Adjusted \"Exceptions\" (sat or unsat) by {corrections['Failure']['value']}")


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
    valid_witness_sat = 0
    invalid_witness_sat = 0
    unsatisfiable_sat = 0
    exception_sat = 0
    exception_timeout_sat = 0
    time_sat = []   
    valid_witness_unsat = 0
    invalid_witness_unsat = 0
    unsatisfiable_unsat = 0
    exception_unsat = 0
    exception_timeout_unsat = 0
    no_witness_unsat = 0
    validation_exception_unsat = 0
    time_unsat = []
    
    if ("corrections" in config["datasets"][dataset] and tool in config["datasets"][dataset]["corrections"]):
        corrections = config["datasets"][dataset]["corrections"][tool]
    if "unknown" in paths:
        if "sat" in paths or "unsat" in paths:
            print(f"Error: Mixing schemas without ground truth and schemas with ground truth in the same dataset is currently not supported. Skipping dataset {dataset}.")
            return f"{dataset},{tool},NA,NA,NA,NA,NA,NA,NA\n"
        path = f'{config["base_path"]}/{paths["unknown"]}/{config["filenames"][tool]}'
        if not os.path.exists(path):
            print(f"WARN: File at {path} not found. Skipping dataset.")
        else:
            df = readDF(path)
            if df is None or df.empty:
                print(f"WARN: File at {path} is empty. Skipping dataset.")
            else:
                valid_witness, invalid_witness, no_witness, unsatisfiable, exception, exception_timeout, validation_exception, time = eval_all(df)
                print_results(valid_witness, invalid_witness, no_witness, unsatisfiable, exception, exception_timeout, validation_exception, time, 
                              corrections, total, "No ground truth is defined for the following results.")
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
                    valid_witness_sat, invalid_witness_sat, no_witness_sat, unsatisfiable_sat, exception_sat, exception_timeout_sat, validation_exception_sat, time_sat = eval_all(df)
                    processed += valid_witness_sat + invalid_witness_sat + no_witness_sat + exception_sat

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
                    valid_witness_unsat, invalid_witness_unsat, no_witness_unsat, unsatisfiable_unsat, exception_unsat, exception_timeout_unsat, validation_exception_unsat, time_unsat = eval_all(df)
                    processed += valid_witness_unsat + invalid_witness_unsat + no_witness_unsat + exception_unsat
            
        if not (has_sat or has_unsat):
            print(f"No files found for dataset \"{dataset}\". Skipping dataset.")
            return
            
        print_results_gt(valid_witness_sat, invalid_witness_sat, no_witness_sat, unsatisfiable_sat, exception_sat, exception_timeout_sat, validation_exception_sat, time_sat, valid_witness_unsat, 
                         invalid_witness_unsat, no_witness_unsat, unsatisfiable_unsat, exception_unsat, exception_timeout_unsat, validation_exception_unsat, time_unsat, corrections, has_sat, has_unsat, total)

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
        print(f"WARN: File at {path} not found. Skipping dataset.")
        return f"{dataset},CC,NA,NA,NA,NA,NA,NA,NA\n"

    df = pd.read_csv(path)
    if df is None or df.empty:
        print(f"File at {path} is empty. Skipping dataset.")
    
    if tool == "CC":
        tool_name = "jsonsubschema (CC)"
    print(f"Dataset: {dataset}\nTool:\t {tool_name}")
    total = dataset_config["files"]
    
    exception_offset = 0 
    if "corrections" in dataset_config and "CC" in dataset_config["corrections"]:
        if "Failure" in dataset_config["corrections"]["CC"]:
            exception_correction = dataset_config["corrections"]["CC"]["Failure"]
            exception_offset = exception_correction["value"] if "value" in exception_correction else 0
            exception_message = exception_correction["description"] if "description" in exception_correction else ""
        else:
            print(f"Only Exception correction is currently supported for CC datasets")
    if df["s1SUBs2"].isnull().all():
        # No ground truth is defined
        print(f"No ground truth is defined for the following results.")
        subschema = len(df[(df["IBM_s1SUBs2"] == "1") & (df["totalTime"] <= TIMEOUT)])
        no_subschema = len(df[(df["IBM_s1SUBs2"] == "0") & (df["totalTime"] <= TIMEOUT)])
        error = len(df[(df["IBM_s1SUBs2"] != "1") & (df["IBM_s1SUBs2"] != "0")])
        timeout = len(df[(df["IBM_s1SUBs2"] == "TimeoutException") | (df["totalTime"] > TIMEOUT)])
        error += timeout
        processed = subschema + no_subschema + error
        df_no_timeout = df[df["totalTime"] <= TIMEOUT]
        time = df_no_timeout["totalTime"].dropna().tolist()
        success = subschema + no_subschema
        print(f"\t\"Success\" (No Exceptions): {success} ({round(100 * success / total, 2)}% of total schema pairs)")
        print(f"\t\"Failure\" (Exceptions): {error} ({round(100 * error / total, 2)}% of total schema pairs)")
        print(f"\t\tIncludes {timeout} Timeouts ({round(100 * timeout / total, 2)}% of total schema pairs)")
        print(f"\tMedian Time: {round(statistics.median(map(float, time)) / 1000, 3)}s")
        print(f"\t95th Percentile Time: {round(np.percentile(time, 95) / 1000, 3)}s")
        print(f"\tAverage Time: {round(statistics.mean(map(float, time)) / 1000, 3)}s")
        
    elif df["s1SUBs2"].isin([0, 1]).all():
        # Ground truth is defined
        result_correct_sat = len(df[(df["s1SUBs2"] == 0) & (df["IBM_s1SUBs2"] == "0") & (df["totalTime"] <= TIMEOUT)])
        result_correct_unsat = len(df[(df["s1SUBs2"] == 1) & (df["IBM_s1SUBs2"] == "1") & (df["totalTime"] <= TIMEOUT)])
        result_wrong_sat = len(df[(df["s1SUBs2"] == 0) & (df["IBM_s1SUBs2"] == "1") & (df["totalTime"] <= TIMEOUT)])
        result_wrong_unsat = len(df[(df["s1SUBs2"] == 1) & (df["IBM_s1SUBs2"] == "0") & (df["totalTime"] <= TIMEOUT)])
        # errors occured when "IBM_s1SUBs2" is neither 0 nor 1
        error_sat = len(df[(df["s1SUBs2"] == 0) & (df["IBM_s1SUBs2"] != "0") & (df["IBM_s1SUBs2"] != "1")])
        error_unsat = len(df[(df["s1SUBs2"] == 1) & (df["IBM_s1SUBs2"] != "0") & (df["IBM_s1SUBs2"] != "1")])
        timeout_sat = len(df[(df["s1SUBs2"] == 0) & ((df["IBM_s1SUBs2"] == "TimeoutException") | (df["totalTime"] > TIMEOUT))])
        timeout_unsat = len(df[(df["s1SUBs2"] == 1) & ((df["IBM_s1SUBs2"] == "TimeoutException") | (df["totalTime"] > TIMEOUT))])
        processed = result_correct_sat + result_wrong_sat + result_correct_unsat + result_wrong_unsat + error_sat + error_unsat
        
        df_no_timeout = df[df["totalTime"] <= TIMEOUT]
        time = df_no_timeout["totalTime"].dropna().tolist()
        success = result_correct_sat + result_correct_unsat
        failure = error_sat + error_unsat + exception_offset
        timeouts = timeout_sat + timeout_unsat
        error_sat = result_wrong_sat + timeout_sat
        error_unsat = result_wrong_unsat + timeout_unsat
        if processed > 0:
            print(f"\t\"Success\" (Correctly identified subschema relationship): {success} ({round(100 * success / total, 2)}% of total schema pairs)")
            print(f"\t\"Failure\" (Exceptions): {failure} ({round(100 * failure / total, 2)}% of total schema pairs)")
            print(f"\t\tIncludes {timeouts} Timeouts ({round(100 * timeouts / total, 2)}% of total schema pairs)")
            print(f"\t\"Logical Errors (sat)\": {error_sat} ({round(100 * error_sat / total, 2)}% of total schema pairs)")
            print(f"\t\"Logical Errors (unsat)\": {error_unsat} ({round(100 * error_unsat / total, 2)}% of total schema pairs)")
            if exception_offset > 0:
                print(f"\tCorrections: \n\t\t{exception_message}\n",
                    f"\t\t\t => Increased \"Failure\" (Exceptions) by {exception_offset}")
            
            
            print(f"\tMedian Time: {round(statistics.median(map(float, time)) / 1000, 3)}s")
            print(f"\t95th Percentile Time: {round(np.percentile(time, 95) / 1000, 3)}s")
            print(f"\tAverage Time: {round(statistics.mean(map(float, time)) / 1000, 3)}s")
        
    else:
        print(f"Ground truth is only partially defined for dataset {dataset}. Skipping dataset.")
    print(f"\tProcessed {processed} of {dataset_config['files']} schema pairs")
        
if __name__ == "__main__":
    # add integer argument -t for simulate timeout
    args = argparse.ArgumentParser()
    args.add_argument("-t", "--timeout", type=int, default=0, help="Consider results surpassing the specified milliseconds as timeout (0 for no timeout)")
    timeout = args.parse_args().timeout
    if timeout > 0:
        TIMEOUT = timeout
    else:
        TIMEOUT = np.inf
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
        "Schemastore Containment",
        "Tricky Schemas"
    ]
    cc_datasets = ["Test Suite Containment", "allOf Containment", "Schemastore Containment", "Tricky Schemas"]
    cc_datasets_processed = []
    tools = ["Ours", "DG"]

    combs = itertools.product(datasets, tools)
    results_csv = "dataset,tool,success,failure,errors sat,errors unsat,median time (s),95 percentile (s),average time (s)\n"
    for c in combs:
        run_evaluation(conf, c[1], c[0])
        if c[0] in cc_datasets and c[0] not in cc_datasets_processed:
            evalSubschema(conf, "CC", c[0])
            cc_datasets_processed.append(c[0])

    cc_datasets = [d for d in cc_datasets if d not in cc_datasets_processed]
    for d in cc_datasets:
        evalSubschema(conf, "CC", d)
