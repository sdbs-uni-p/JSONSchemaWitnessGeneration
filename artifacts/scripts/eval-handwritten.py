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

print("\n######### Handwritten Dataset #########")

sat_path = '/home/repro/JSONAlgebra/JsonSchema_To_Algebra/expDataset/handwritten/testtricky'
sat_files = [f for f in listdir(sat_path) if isfile(join(sat_path, f)) and f.endswith('.json')]
sat_files = [f[:-5] for f in sat_files]

total = 93


def readDF(path):
    df = pd.read_csv(path).convert_dtypes()
    df['genSuccess'] = df['genSuccess'] .map({'true': True, 'false': False, True: True, False: False})
    df['valid'] = df['valid'] .map({'true': True, 'false': False, True: True, False: False})
    df = df.astype({'genSuccess': 'boolean', 'valid': 'boolean'})
    return df


def evalOurTool(results):
    print(f'\n--------------------Our Tool--------------------')

    df = readDF(results)
    df['truth'] = (df['objectId'].isin(sat_files)).tolist()
    correct = len(df[(df['truth']==df['genSuccess']) & ((df['valid'].isnull()) | (df['valid']==True))])
    incorrect = len(df[((df['truth']!=df['genSuccess']))])
    satLogicalErrors = len(df[(df['truth']==True) & (df['truth']==df['genSuccess']) & (df['valid']==False)])

    print(f'\t#Success: {correct} ({round(100*(correct)/total,2)}%)')
    print(f'\t#Failure: {incorrect} ({round(100*(incorrect)/total,2)}%)')
    print(f'\t#Logical Errors (sat): {satLogicalErrors} ({round(100*(satLogicalErrors)/total,2)}%)')
    print('\t#Logical Errors (unsat): na')

    totalTime = df[(df['genSuccess']==True) | (df['genSuccess']==False)]['totalTime'].dropna().tolist()
    print(f'\tAverage Time: {round(statistics.mean(map(float, totalTime))/1000,3)}s')
    print(f'\tMedian Time: {round(statistics.median(map(float, totalTime))/1000,3)}s')


def evalJsongenerator(sat):
    print(f'\n--------------------jsongenerator (DG)--------------------')

    satDF = readDF(sat)
    satDF['truth'] = (satDF['objectId'].isin(sat_files)).tolist()
    correct = len(satDF[(satDF['truth']==satDF['genSuccess']) & (satDF['valid']==True)])
    incorrect = len(satDF[((satDF['truth']!=satDF['genSuccess']) | satDF['genSuccess'].isnull())])
    satLogicalErrors = len(satDF[(satDF['truth']==True) & (satDF['truth']==satDF['genSuccess']) & (satDF['valid']==False)])

    print(f'\t#Success: {correct} ({round(100*(correct)/total,2)}%)')
    print(f'\t#Failure: {incorrect} + 1 ({round(100*(incorrect+1)/total,2)}%)')
    print(f'\t#Logical Errors (sat): {satLogicalErrors} ({round(100*(satLogicalErrors)/total,2)}%)')
    print('\t#Logical Errors (unsat): na')
    print('\tManual correction: 1 Schema caused an infinite loop in Jsongenerator and was exlcuded from the experiment\n' +
	    '\t\t => Increase Failure by 1')
    totalTime = satDF[satDF['genSuccess']==True]['totalTime'].dropna().tolist()
    print(f'\tAverage Time: {round(statistics.mean(map(float, totalTime))/1000,3)}s')
    print(f'\tMedian Time: {round(statistics.median(map(float, totalTime))/1000,3)}s')


basePath = '/home/repro/results/handwritten-testtricky/'
evalJsongenerator(basePath + 'jsongenerator_results.csv')
evalOurTool(basePath + 'results.csv')
