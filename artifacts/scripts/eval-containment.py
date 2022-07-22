#!/usr/local/bin/python3.9

import sys
import jsonsubschema
import os
import json
from os import listdir
from os.path import isfile, join
from os import path
import pandas as pd
import numpy as np
from datetime import datetime
import pathlib
import statistics
from jsonsubschema._utils import load_json_file
from jsonsubschema.api import isSubschema
from jsonref import JsonRefError
from jsonschema.exceptions import SchemaError
from jsonsubschema.exceptions import UnsupportedRecursiveRef
from jsonsubschema.exceptions import UnsupportedEnumCanonicalization
from jsonsubschema.exceptions import UnsupportedNegatedObject
from jsonsubschema.exceptions import UnsupportedNegatedArray
pd.set_option('display.max_columns', None)

def analyze_schema(s1,s2):
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
         #this error may be raised when the schema is beyond v4
         #this error is also raised when required is used in v3-schemas (known issue)
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


def bool_to_int(b):
    if b==True:
        return 1
    elif b==False:
        return 0
    else:
        return b


def extractAndCheck(pathToFolder):
    ID = 'id'
    S1 = 'schema1'
    S2 = 'schema2'
    TESTS = 'tests'
    S1SUBS2 = 's1SubsetEqOfs2'
    new_dict = {'subFolder':[],'subSubFolder':[],'fileName':[],'id':[],'s1SUBs2':[], 
                'IBM_s1SUBs2':[], 'totalTime':[]}
    parentFolder = os.path.basename(pathToFolder)
    subFoldersList = [f for f in os.listdir(pathToFolder) if path.isdir(join(pathToFolder, f))]
    for subFolder in subFoldersList:
        subFolderPath = join(pathToFolder, subFolder)
        subSubFolders = [f for f in os.listdir(subFolderPath) if path.isdir(join(subFolderPath, f))]
        for subSubFolder in subSubFolders:
            subSubFolderPath = join(subFolderPath,subSubFolder)
            files = [f for f in listdir(subFolderPath+'/'+subSubFolder) if isfile(join(subFolderPath+'/'+subSubFolder, f)) and f.endswith('.json')]
            for f in files:
                fileName = f.replace('.json','')
                inputFile = open(subSubFolderPath+'/'+f,'r')
                data = json.load(inputFile)
                inputFile.close()
                for i in data:
                    d = i
                    id = d['id']
                    s1 = d[S1]
                    s2 = d[S2]
                    tests = d[TESTS]
                    s1Subs2 = 9
                    IBM_s1Subs2 = 9
                    if S1SUBS2 in tests:
                        s1Subs2 = bool_to_int(tests[S1SUBS2])
                        start = datetime.now()
                        IBM_s1Subs2 = bool_to_int(analyze_schema(s1,s2))
                        end = datetime.now()
                        total = end-start
                        totalTime = round(total.total_seconds()*1000,2)
                    new_dict['subFolder'].append(subFolder)
                    new_dict['subSubFolder'].append(subSubFolder)
                    new_dict['fileName'].append(fileName)
                    new_dict['id'].append(id)
                    new_dict['s1SUBs2'].append(s1Subs2)
                    new_dict['IBM_s1SUBs2'].append(IBM_s1Subs2)
                    new_dict['totalTime'].append(totalTime)
    df = pd.DataFrame(new_dict)
    return df


def evalSubschema(df):
    print(f'\n--------------------jsonsubschema (CC)--------------------')
    print(f'                   Dataset: Containment                    ')
    equal = df[df['IBM_s1SUBs2']==df['s1SUBs2']]

    equal_dict = equal[['s1SUBs2','IBM_s1SUBs2']].value_counts().to_dict()
    equal_list =[(k[0],k[1],v) for k,v in equal_dict.items()]

    equalDF = pd.DataFrame(equal_list, columns=['s1SUBs2','IBM_s1SUBs2','count'])

    notEqual = df[df['IBM_s1SUBs2']!=df['s1SUBs2']]

    notEqual_dict = notEqual[['s1SUBs2','IBM_s1SUBs2']].value_counts().to_dict()
    notEqual_list = [(k[0],k[1],v) for k,v in notEqual_dict.items()]
    notEqualDF = pd.DataFrame(notEqual_list, columns=['s1SUBs2','IBM_s1SUBs2','count'])
    
    logErrorSat = notEqualDF[(notEqualDF['s1SUBs2']==0) & (notEqualDF['IBM_s1SUBs2']==1)]
    logErrorUnsat = df[(df['s1SUBs2']==1)]['IBM_s1SUBs2'].value_counts(dropna=False).to_frame()

    incorrect = len(notEqual) - logErrorSat.iloc[0]['count'] - logErrorUnsat.loc[0, 'IBM_s1SUBs2']
    print(f'\t#Success: {len(equal)} ({round(100*len(equal)/1331, 2)}%)')
    print(f'\t#Failure: {incorrect} + 43 ({round(100*(incorrect+43)/1331, 2)}%)')
    print(f"\tLogical errors sat: {logErrorSat.iloc[0]['count']} ({round(100*logErrorSat.iloc[0]['count']/1331, 2)})%")
    print(f"\tLogical errors unsat: {logErrorUnsat.loc[0, 'IBM_s1SUBs2']} ({round(100*logErrorUnsat.loc[0, 'IBM_s1SUBs2']/1331, 2)}%)")

    print("\tManual corrections: \n" +
        "\t\t43 schemas removed because they cause problems (cannot catch the exception)\n" +
        "\t\t => Increase Failure by 43")

def runSubschemaTests():
    schemaPairs = '/home/repro/JSONAlgebra/JsonSchema_To_Algebra/expDataset/containment/schemaPairs'
    subschemaDF = extractAndCheck(schemaPairs)   
    subschemaDF = subschemaDF.sort_values(by=['subFolder', 'subSubFolder','fileName','id'])
    subschemaSatDf = subschemaDF[subschemaDF['s1SUBs2']==0]
    subschemaUnsatDf = subschemaDF[subschemaDF['s1SUBs2']==1]
    evalSubschema(subschemaDF)
    print(f'\tAverage Time: {round(subschemaDF.totalTime.mean()/1000,3)}s')
    print(f'\tMedian Time: {round(subschemaDF.totalTime.median()/1000,3)}s')


runSubschemaTests()