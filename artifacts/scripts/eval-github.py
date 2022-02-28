#!/usr/local/bin/python3.9

import sys
import os
import json
from os import listdir
from os.path import isfile, join
from os import path
import pandas as pd
import numpy as np
import pathlib
import statistics
pd.set_option('display.max_columns', None)

print("\n######### GitHub Dataset #########")

sat_path = '/home/repro/JSONAlgebra/JsonSchema_To_Algebra/expDataset/realWorldSchemas/all'
sat_files = [f for f in listdir(sat_path) if isfile(join(sat_path, f)) and f.endswith('.json')]
sat_files = [f[:-5] for f in sat_files]

unsat_path = '/home/repro/JSONAlgebra/JsonSchema_To_Algebra/expDataset/realWorldSchemas/unsat'
unsat_files = [f for f in listdir(unsat_path) if isfile(join(unsat_path, f)) and f.endswith('.json')]
unsat_files = [f[:-5] for f in unsat_files]
total = 6427


def readDF(path):
    df = pd.read_csv(path).convert_dtypes()
    df['genSuccess'] = df['genSuccess'] .map({'true': True, 'false': False, True: True, False: False})
    df['valid'] = df['valid'] .map({'true': True, 'false': False, True: True, False: False})
    df = df.astype({'genSuccess': 'boolean', 'valid': 'boolean'})
    return df
    


def evalResults(results):
    print(f'\n--------------------Our Tool--------------------')

    df = readDF(results)
    df['truth'] = (df['objectId'].isin(sat_files)).tolist()
    correct = len(df[(df['truth']==df['genSuccess']) & ((df['valid'].isnull()) | (df['valid']==True))])
    incorrect = len(df[df['genSuccess'].isnull()])
    satLogicalErrors = len(df[(df['truth']==True) & (df['truth']==df['genSuccess']) & (df['valid']==False)])
    unsatLogicalErrors = len(df[(df['truth']==False) & (df['genSuccess']==True)])

    # We excluded some schemas beforehand. Missing schemas are thus added to "incorrect"
    incorrect += total - correct - satLogicalErrors - unsatLogicalErrors - incorrect
    print(f'\t#Success: {correct} ({round(100*(correct)/total,2)}%)')
    print(f'\t#Failure: {incorrect} ({round(100*(incorrect)/total,2)}%)')
    print(f'\t#Logical Errors (sat): {satLogicalErrors} ({round(100*(satLogicalErrors)/total,2)}%)')
    print(f'\t#Logical Errors (unsat): {unsatLogicalErrors} ({round(100*(unsatLogicalErrors)/total,2)}%)')
    print("\tManual corrections: \n" +
        "\t\tEncountered 2 exceptions, for both a valid witness was generated\n" +
        "\t\t => Manually fixed in the csv file, thus no changes here")

    totalTime = df[(df['genSuccess']==True) | (df['genSuccess']==False)]['totalTime'].dropna().tolist()
    print(f'\tAverage Time: {round(statistics.mean(map(float, totalTime))/1000,3)}s')
    print(f'\tMedian Time: {round(statistics.median(map(float, totalTime))/1000,3)}s')

        
def evalJsongenerator(sat, unsat):
    print(f'\n--------------------jsongenerator (DG)--------------------')

    satDF = readDF(sat)
    satDF['truth'] = (satDF['objectId'].isin(sat_files)).tolist()
    correct = len(satDF[(satDF['genSuccess']==True) & (satDF['valid']==True)])
    incorrect = len(satDF[((satDF['truth']!=satDF['genSuccess']) | satDF['genSuccess'].isnull() | satDF['valid'].isnull())])
    satLogicalErrorsDF = satDF[(satDF['truth']==True) & (satDF['truth']==satDF['genSuccess']) & (satDF['valid']==False)]
    satLogicalErrors = len(satLogicalErrorsDF)

    unsatDF = readDF(unsat)
    unsatDF['truth'] = (unsatDF['objectId'].isin(unsat_files)).tolist()
    correct += len(unsatDF[unsatDF['genSuccess']==False])
    incorrect += len(unsatDF[unsatDF['genSuccess'].isnull()])
    unsatLogicalErrorsDF = unsatDF[(unsatDF['genSuccess']==True)]
    unsatLogicalErrors = len(unsatLogicalErrorsDF)
 
    print(f'\t#Success: {correct} + 5 ({round(100*(correct+5)/total,2)}%)')
    print(f'\t#Failure: {incorrect} - 6 ({round(100*(incorrect - 6)/total,2)}%)')
    print(f'\t#Logical Errors (sat): {satLogicalErrors} + 3 ({round(100*(satLogicalErrors + 3)/total,2)}%)')
    print(f'\t#Logical Errors (unsat): {unsatLogicalErrors} ({round(100*(unsatLogicalErrors)/total,2)}%)')
    print("\tManual corrections (satisfiable dataset): \n" +
        "\t\tEncountered 2 JsonSchemaException, for 1 a valid witness was generated\n" +
        "\t\t => Success is increased by 1, Logical Errors (sat) are increased by 1,\n" +
        "\t\t\tFailure is reduced by 2\n" +
        "\t\tEncountered 6 ExceutionException, for 4 a valid witness was generated\n" +
        "\t\t => Success is increased by 4, Logical Errors (sat) are increased by 2,\n" +
        "\t\t\tFailure is  reduced by 6\n" +
        "\t\t2 schemas caused an infinite loop in Jsongenerator and was exlcuded from the experiment\n" +
        "\t\t => Failure is increased by 2")

    totalTime = satDF[satDF['genSuccess']==True]['totalTime'].dropna().tolist() + unsatDF[unsatDF['genSuccess']==True]['totalTime'].dropna().tolist()
    print(f'\tAverage Time: {round(statistics.mean(map(float, totalTime))/1000,3)}s')
    print(f'\tMedian Time: {round(statistics.median(map(float, totalTime))/1000,3)}s')


basePath = '/home/repro/results/'

def runJsongenerator():
    jsongenResults = basePath + 'realWorldSchemas-all/jsongenerator_results.csv'
    satDF = pd.read_csv(basePath + 'realWorldSchemas-all/jsongenerator_results.csv')
    unsatDF = pd.read_csv(basePath + 'realWorldSchemas-unsat/jsongenerator_results.csv')

evalJsongenerator(basePath + 'realWorldSchemas-all/jsongenerator_results.csv', 
            basePath + 'realWorldSchemas-unsat/jsongenerator_results.csv')
evalResults(basePath + 'realWorldSchemas-all/results.csv')
