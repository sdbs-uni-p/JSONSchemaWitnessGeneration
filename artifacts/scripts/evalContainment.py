#!/usr/local/bin/python3.9

import sys
import os
import json
from os import listdir
from os.path import isfile, join
from os import path
import pandas as pd
from datetime import datetime
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
                    id = d[ID]
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
    print(f'\nDataset: Containment\nTool:\t jsonsubschema (CC)')
    success = len(df[df['IBM_s1SUBs2']==df['s1SUBs2']])

    notEqual = df[df['IBM_s1SUBs2']!=df['s1SUBs2']]

    notEqual_dict = notEqual[['s1SUBs2','IBM_s1SUBs2']].value_counts().to_dict()
    notEqual_list = [(k[0],k[1],v) for k,v in notEqual_dict.items()]
    notEqualDF = pd.DataFrame(notEqual_list, columns=['s1SUBs2','IBM_s1SUBs2','count'])
    
    sat_logical_errors = notEqualDF[(notEqualDF['s1SUBs2']==0) & (notEqualDF['IBM_s1SUBs2']==1)]
    unsat_logical_errors = df[(df['s1SUBs2']==1)]['IBM_s1SUBs2'].value_counts(dropna=False).to_frame()

    failure = len(notEqual) - sat_logical_errors.iloc[0]['count'] - unsat_logical_errors.loc[0, 'IBM_s1SUBs2']
    success_perc = round(100*success/1331, 2)
    print(f'\t#Success: {success} ({success_perc}%)')
    failure_perc = round(100*(failure+43)/1331, 2)
    print(f'\t#Failure: {failure+43} ({failure_perc}%)')
    print("\t\tCorrection: 43 schemas removed because they cause problems (cannot catch the exception)\n",
          "\t\t\t => Increased Failure by 43")
    sat_err_perc = round(100*sat_logical_errors.iloc[0]['count']/1331, 2)
    print(f"\tLogical errors sat: {sat_logical_errors.iloc[0]['count']} ({sat_err_perc})%")
    unsat_err_perc = round(100*unsat_logical_errors.loc[0, 'IBM_s1SUBs2']/1331, 2)
    print(f"\tLogical errors unsat: {unsat_logical_errors.loc[0, 'IBM_s1SUBs2']} ({unsat_err_perc}%)")
    
    return f'{success_perc},{failure_perc},{sat_err_perc},{unsat_err_perc}'

def runSubschemaTests():
    schemaPairs = '/home/repro/JSONAlgebra/JsonSchema_To_Algebra/expDataset/containment/schemaPairs'
    subschemaDF = extractAndCheck(schemaPairs)   
    subschemaDF = subschemaDF.sort_values(by=['subFolder', 'subSubFolder','fileName','id'])
    
    results = evalSubschema(subschemaDF)
    
    med_time = round(subschemaDF.totalTime.median()/1000,3)
    print(f'\tMedian Time: {med_time}s')
    p95_time = round(subschemaDF.totalTime.quantile(0.95)/1000,3)
    print(f'\t95th Percentile Time: {p95_time}s')
    avg_time = round(subschemaDF.totalTime.mean()/1000,3)
    print(f'\tAverage Time: {avg_time}s')
    
    csv = f'Containment,CC,{results}\n' 
    csv_times = f'Containment,CC,{results},{med_time},{p95_time},{avg_time}\n'
    return csv, csv_times
    
if __name__ == '__main__':
    runSubschemaTests()
