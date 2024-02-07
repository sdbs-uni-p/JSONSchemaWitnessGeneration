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

def extractAndCheckTestSuite(pathToFolder):
    ID = "id"
    S1 = "schema1"
    S2 = "schema2"
    TESTS = "tests"
    S1SUBS2 = "s1SubsetEqOfs2"
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


def extractAndCheck(pathToFolder):
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
        "totalTime": [],
    }

    # get all json files in pathToFolder
    files = [f for f in listdir(pathToFolder) if isfile(join(pathToFolder, f)) and f.endswith(".json")]

    for f in files:
        fileName = f.replace(".json", "")
        inputFile = open(pathToFolder + "/" + f, "r")
        data = json.load(inputFile)
        inputFile.close()
        for i in data:
            tests = i[TESTS]
            s1Subs2 = 9
            IBM_s1Subs2 = 9
            if S1SUBS2 in tests:
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
    df = pd.DataFrame(new_dict)
    return df


def runSubschemaTestsTestSuite(schemaPairs, output, isTestSuite=False):    
    if isTestSuite:
        # Test Suite Containment tests have a different folder strucutre and we need to apply
        # manual corrections to reproduce the results
        subschemaDF = extractAndCheckTestSuite(schemaPairs,)
        subschemaDF = subschemaDF.sort_values(by=["subFolder", "subSubFolder", "fileName", "id"])
    else:
        subschemaDF = extractAndCheck(schemaPairs)
        subschemaDF = subschemaDF.sort_values(by=["fileName", "id"])

    with open(output, "w") as f:
        f.write(subschemaDF.to_csv(index=False))

if __name__ == "__main__":
    parser = ArgumentParser()
    parser.add_argument("-i", "--input", help="Input directory", required=True)
    parser.add_argument("-o", "--output", help="Output file for results", required=True)
    parser.add_argument("-t", "--testsuite", help="Flag to indicate if the input is a test suite", action="store_true", default=False)
    args = parser.parse_args()
    
    runSubschemaTestsTestSuite(args.input, args.output, args.testsuite)


