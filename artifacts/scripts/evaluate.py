#!/usr/local/bin/python3.9
import itertools
import os
import json
import re
import numpy as np
import pandas as pd
import statistics
import evalContainment
from pathlib import Path
pd.set_option('display.max_columns', None)


def readDF(path):
    try:
        df = pd.read_csv(path).convert_dtypes()
    except pd.errors.EmptyDataError:
      return

    df['genSuccess'] = df['genSuccess'] .map({'true': True, 'false': False, True: True, False: False})
    df['valid'] = df['valid'] .map({'true': True, 'false': False, True: True, False: False,
                                    'JsonSchemaException': True, 'ExecutionException': True})

    return df


def eval_sat(df):
    success = len(df[(df['genSuccess'] == True) & (df['valid'] == True)])
    failure = len(df[((df['genSuccess'] == False) | (df['genSuccess'].isnull())) |
                     ((df['genSuccess'] == True) & (df['valid'].isnull()))])
    satLogicalErrors = len(df[(df['genSuccess'] == True) & ((df['valid'] == False))])
    time = df['totalTime'].dropna().tolist()

    return success, failure, satLogicalErrors, time


def eval_unsat(df):
    success = len(df[df['genSuccess'] == False])
    failure = len(df[df['genSuccess'].isnull() | ((df['genSuccess'] == True) & (df['valid'].isnull()))])
    unsatLogicalErrors = len(df[((df['genSuccess'] == True) & ((df['valid']==True) | (df['valid']==False)))])

    time = df['totalTime'].dropna().tolist()

    return success, failure, unsatLogicalErrors, time


def print_results(type, val, total, corrections):
    if val is None:
       print(f'\t#{type}: na') 
       return "NA"

    result_str = ""
    if corrections and type in corrections:
        val += corrections[type]["value"]
        change_type = "Increased" if corrections[type]["value"] >= 0 else "Decreased" 
        result_str = (f'\n\t\tCorrection: {corrections[type]["description"]}\n'
                      f'\t\t\t=> {change_type} {type} by {abs(corrections[type]["value"])}')

    percentage = round(100*(val)/total,2)
    print(f'\t#{type}: {val} ({percentage}%){result_str}')

    return percentage

def run_evaluation(config, tool, dataset):
    print('')
    if tool not in config["filenames"]:
        print(f"Tool {tool} not configured. Skipping")
        return
    if dataset not in config["datasets"]:
        print(f"Dataset {dataset} not configured. Skipping")
        return

    paths = config["datasets"][dataset]["paths"]
    if "sat"  not in paths and "unsat" not in paths:
        print(f"Paths for dataset {dataset} are not configured properly")
        return

    if tool == "DG":
        tool_str = "jsongenerator (DG)"
    else:
        tool_str = tool
    print(f'Dataset: {dataset}\nTool:\t {tool_str}')

    total = config["datasets"][dataset]["files"]
    sat_success, sat_failure, sat_time = 0, 0, []
    unsat_success, unsat_failure, unsat_time = 0, 0, []
    sat_logical_errors, unsat_logical_errors = None, None

    has_sat, has_unsat = False, False
    if "sat" in paths:
        path = f'{config["base_path"]}/{paths["sat"]}/{config["filenames"][tool]}'
        if not os.path.exists(path):
            print(f"\033[93mWARN: File at {path} not found. Skipping dataset.\033[0m")
        else:
            df = readDF(path)
            if df is None or df.empty:
                print(f"\033[93mWARN: File at {path} is empty. Skipping dataset.\033[0m")
            else:
                sat_success, sat_failure, sat_logical_errors, sat_time = eval_sat(df)
                has_sat = True
    if "unsat" in paths:
        path = f'{config["base_path"]}/{paths["unsat"]}/{config["filenames"][tool]}'
        if not os.path.exists(path):
            print(f"\033[93mWARN: File at {path} not found. Skipping dataset.\033[0m")
        else:
            df = readDF(path)
            if df is None or df.empty:
                print(f"\033[93mWARN: File at {path} is empty. Skipping dataset.\033[0m")
            else:
                unsat_success, unsat_failure, unsat_logical_errors, unsat_time = eval_unsat(df)
                has_unsat = True

    # Return empty line if datasets are missing
    if not (has_sat or has_unsat):
        return " "

    success = sat_success + unsat_success
    failure = sat_failure + unsat_failure
    time = sat_time + unsat_time

    if len(time) == 0:
        print("Found no execution times in results. Does the file contain any entries?\nSkipping dataset ...")
        return

    corrections = None
    if "corrections" in config["datasets"][dataset] and tool in config["datasets"][dataset]["corrections"]:
        corrections = config["datasets"][dataset]["corrections"][tool]

    types = [("Success", success), ("Failure", failure), ("Logical Errors (sat)", sat_logical_errors),
             ("Logical Errors (unsat)", unsat_logical_errors)]
    results = ""
    for t in types:
        results += str(print_results(t[0], t[1], total, corrections)) + ","
    results = results[:-1]

    med_time = round(statistics.median(map(float, time))/1000,3)
    print(f'\tMedian Time: {med_time}s')
    p95_time = round(np.percentile(time,95)/1000,3)
    print(f'\t95th Percentile Time: {p95_time}s')
    avg_time = round(statistics.mean(map(float, time))/1000,3)
    print(f'\tAverage Time: {avg_time}s')

    csv = f'{dataset},{tool},{results},{med_time},{p95_time},{avg_time}\n'
    return csv


if __name__ == '__main__':
    home = str(Path.home())

    with open(f"{home}/scripts/config.json") as f:
        conf = json.load(f)
    basePath = 'dg_results/'
    datasets = ["GitHub", "Kubernetes", "Snowplow", "WashingtonPost", "Handwritten","Containment"]
    tools = ["Ours", "DG"]

    combs = itertools.product(datasets, tools)
    results_csv = "dataset,tool,success,failure,errors sat,errors unsat,median time,95 percentile,average time\n"
    for c in combs:
        results_csv += run_evaluation(conf, c[1], c[0])

    results_csv += evalContainment.runSubschemaTests()

    with open(f"{home}/results/results.csv", "w") as f:
        f.write(results_csv)
