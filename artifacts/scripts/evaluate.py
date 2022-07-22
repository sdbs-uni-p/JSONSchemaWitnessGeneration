#!/usr/local/bin/python3.9
import itertools
import os
import json
import pandas as pd
import statistics
pd.set_option('display.max_columns', None)


def readDF(path):
    df = pd.read_csv(path).convert_dtypes()
    df['genSuccess'] = df['genSuccess'] .map({'true': True, 'false': False, True: True, False: False})
    df['valid'] = df['valid'] .map({'true': True, 'false': False, True: True, False: False,
                                    'JsonSchemaException': False})
    #df = df.astype({'genSuccess': 'boolean', 'valid': 'boolean'})
    return df


def eval_sat(data):
    df = readDF(data)
    success = len(df[(df['genSuccess'] == True) & (df['valid'] == True)])
    failure = len(df[((df['genSuccess'] == False) | (df['genSuccess'].isnull())) |
                     ((df['genSuccess'] == True) & (df['valid'].isnull()))])
    satLogicalErrors = len(df[(df['genSuccess'] == True) & ((df['valid'] == False))])
    time = df[df['genSuccess']==True]['totalTime'].dropna().tolist()

    return success, failure, satLogicalErrors, time


def eval_unsat(data):
    df = readDF(data)
    success = len(df[df['genSuccess'] == False])
    failure = len(df[df['genSuccess'].isnull() | ((df['genSuccess'] == True) & (df['valid'].isnull()))])
    unsatLogicalErrors = len(df[((df['genSuccess'] == True) & ((df['valid']==True) | (df['valid']==False)))])

    time = df[df['genSuccess'] == True]['totalTime'].dropna().tolist()

    return success, failure, unsatLogicalErrors, time


def run_evaluation(config, tool, dataset):
    if tool not in config["filenames"]:
        print(f"Tool {tool} not configured. Skipping")
        return
    if dataset not in config["datasets"]:
        print(f"Dataset {dataset} not configured. Skipping")
        return

    if tool == "ours":
        print('\n-------------------------- Ours --------------------------')
    elif tool == "jsongenerator":
        print('\n--------------------jsongenerator (DG)--------------------')
    print('{:^58s}'.format(f'Dataset: {dataset}'))

    paths = config["datasets"][dataset]["paths"]
    if "sat"  not in paths and "unsat" not in paths:
        print(f"Paths for dataset {dataset} are not configured properly")
        return

    total = config["datasets"][dataset]["files"]
    sat_success, sat_failure, sat_time = 0, 0, []
    unsat_success, unsat_failure, unsat_time = 0, 0, []
    sat_logical_errors, unsat_logical_errors = None, None

    if "sat" in paths:
        path = f'{config["base_path"]}/{paths["sat"]}/{config["filenames"][tool]}'
        if not os.path.exists(path):
            print(f"File at {path} not found. Skipping dataset.")
            return
        sat_success, sat_failure, sat_logical_errors, sat_time = eval_sat(path)
    if "unsat" in paths:
        path = f'{config["base_path"]}/{paths["unsat"]}/{config["filenames"][tool]}'
        if not os.path.exists(path):
            print(f"File at {path} not found. Skipping dataset.")
            return
        unsat_success, unsat_failure, unsat_logical_errors, unsat_time = eval_unsat(path)

    success = sat_success + unsat_success
    failure = sat_failure + unsat_failure
    time = sat_time + unsat_time

    if len(time) == 0:
        print("Found no execution times in results. Does the file contain any entries?\nSkipping dataset ...")
        return

    corrections = None
    if "corrections" in config["datasets"][dataset] and tool in config["datasets"][dataset]["corrections"]:
        corrections = config["datasets"][dataset]["corrections"][tool]

    if corrections and "Success" in corrections:
        corr = corrections["Success"]["value"]
        print(f'\t#Success: {success + corr} ({round(100*(success + corr)/total,2)}%)'
              f'\n\t\tCorrection: {corrections["Success"]["description"]}')
    else:
        print(f'\t#Success: {success} ({round(100*success/total,2)}%)')

    if corrections and "Failure" in corrections:
        corr = corrections["Failure"]["value"]
        print(f'\t#Failure: {failure + corr} ({round(100 * (failure + corr) / total, 2)}%)'
              f'\n\t\tCorrection: {corrections["Failure"]["description"]}')
    else:
        print(f'\t#Failure: {failure} ({round(100*failure/total,2)}%)')

    if sat_logical_errors is None:
        print(f'\t#Logical Errors (sat): na')
    else:
        if corrections and "Logical Errors (sat)" in corrections:
            corr = corrections["Logical Errors (sat)"]["value"]
            print(f'\t#Logical Errors (sat): {sat_logical_errors + corr} ({round(100 * (sat_logical_errors + corr) / total, 2)}%)' 
                  f'\n\t\tCorrection: {corrections["Logical Errors (sat)"]["description"]}')
        else:
            print(f'\t#Logical Errors (sat): {sat_logical_errors} ({round(100*(sat_logical_errors)/total,2)}%)')
    if unsat_logical_errors is None:
        print(f'\t#Logical Errors (unsat): na')
    else:
        if corrections and "Logical Errors (unsat)" in corrections:
            corr =  corrections["Logical Errors (sat)"]["value"]
            print(f'\t#"Logical Errors (unsat)": {unsat_logical_errors + corr} ({round(100 * (unsat_logical_errors + corr) / total, 2)}%)' 
                  f'\n\t\tCorrection: {corrections["Logical Errors (unsat)"]["description"]}')
        else:
            print(f'\t#Logical Errors (unsat): {unsat_logical_errors} ({round(100*(unsat_logical_errors)/total,2)}%)')
    print(f'\tAverage Time: {round(statistics.mean(map(float, time))/1000,3)}s')
    print(f'\tMedian Time: {round(statistics.median(map(float, time))/1000,3)}s')


if __name__ == '__main__':
    with open("config.json") as f:
        conf = json.load(f)
    basePath = 'dg_results/'
    datasets = ["GitHub", "Handwritten", "WashingtonPost", "Snowplow", "Kubernetes", "Containment"]
    #datasets = ["GitHub"]
    tools = ["jsongenerator", "ours"]

    combs = itertools.product(tools, datasets)
    for c in combs:
        run_evaluation(conf, c[0], c[1])

    exit(0)
    # TODO: Handwritten does not match, also makes no sense in Paper - Probably needs updating
    # Manipualted data to match old results
    # TODO: Still off, check back with results in repo

    # TODO: Containment matches Jupyter Notebook, but not Paper
