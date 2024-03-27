#!/usr/local/bin/python3.9

import pandas as pd
import argparse

def readDF(path):
    try:
        df = pd.read_csv(path).convert_dtypes()
    except pd.errors.EmptyDataError:
        return

    if "IBM_s1SUBs2" in df.columns:
        df["IBM_s1SUBs2"] = df["IBM_s1SUBs2"].map(
            {"1": True, "0": False, 1: True, 0: False}
        )
        return df
    
    df["genSuccess"] = df["genSuccess"].map(
        {"true": True, "false": False, True: True, False: False}
    )
    # map "true" and "false" to True and False, respectively and all other values to None
    df["valid"] = df["valid"].map(
        {"true": True, "false": False, True: True, False: False}
    )   

    return df

if __name__ == "__main__":
    # set parameters ours, dg, cc. Ours and DG take up to 2 arguments, CC takes 1 argument
    parser = argparse.ArgumentParser()
    parser.add_argument("--ours", nargs='+', required=True, help="Path to the csv file(s) of our tool")
    parser.add_argument("--dg", nargs='+', required=True, help="Path to the csv file(s) of DG")
    parser.add_argument("--cc", nargs='+', required=True, help="Path to the csv file(s) of CC")
    args = parser.parse_args()
    
    df_ours = pd.concat([readDF(path) for path in args.ours])
    df_dg = pd.concat([readDF(path) for path in args.dg])
    df_cc = pd.concat([readDF(path) for path in args.cc])
    df_cc = df_cc.rename(columns={"fileName": "objectId"})
    
    # drop columns that are not relevant for this analysis
    df_ours = df_ours.drop(columns=["inSize", "outSize", "totalTime", "correctTime", "oneOfAsAnyOf", "parsing", 
                                    "extractSchema", "2Full", "2Witness", "notElim", "merge1", "groupize", 
                                    "separation", "expansion", "preparation", "merge3", "initGEnv", "genWitness",
                                    "#iterations", "cancelledAt","dateAndTime","timeout","git","machine"])
    
    # rename columns
    df_ours = df_ours.rename(columns={"genSuccess": "ours_genSuccess", "noWitness": "ours_noWitness", "valid": "ours_valid"})
    
    df_dg = df_dg.drop(columns=["inSize", "totalTime", "loadSchema", "initGenerator", "generation"])
    df_dg = df_dg.rename(columns={"genSuccess": "dg_genSuccess", "valid": "dg_valid"})
    
    df_cc = df_cc.drop(columns=["id", "s1SUBs2", "totalTime"])
    df_cc = df_cc.rename(columns={"fileName": "objectId"})
    
    # match dfs on objectId
    df = df_ours.merge(df_dg, on="objectId", how="outer").merge(df_cc, on="objectId", how="outer")

    df_unsat = df[(df["ours_genSuccess"] == False) & ((df["dg_genSuccess"] == False) | (df["dg_valid"] == False) | (df["dg_valid"].isna()))]
    df_sat = df[((df["ours_genSuccess"] == True) & (df["ours_valid"] == True)) | ((df["dg_genSuccess"] == True) & (df["dg_valid"] == True))]
    df_sat_ours = df[(df["ours_genSuccess"] == True) & (df["ours_valid"] == True)]
    df_error = df[(df["ours_genSuccess"].isna())]
    print("Unsat according to our tool and DG: ", len(df_unsat))
    print("\tThis is counting cases where our tool produced no witness and DG produced no witness or an invalid witness.")
    print("Sat according to our tool or DG: ", len(df_sat))
    print("\tThis is counting cases where our tool or DG (or both) produced a valid witness.")
    print("Sat according to our tool: ", len(df_sat_ours))
    print("\tThis is counting cases where our tool produced a valid witness.")
    print("Errors in our tool: ", len(df_error), "\n")
    
    df_errors_ours_not_cc = df[df["ours_genSuccess"].isna() & ((df["IBM_s1SUBs2"] == True) | (df["IBM_s1SUBs2"] == False))]
    df_errors_cc_not_ours = df[((df["ours_genSuccess"] == True) | (df["ours_genSuccess"] == False)) & (df["IBM_s1SUBs2"].isna())]
    df_errors_ours_and_cc = df[(df["ours_genSuccess"].isna()) & (df["IBM_s1SUBs2"].isna())]
    df_unsat_but_no_subschema = df[(df["ours_genSuccess"] == False) & (df["IBM_s1SUBs2"] == False)]
    df_sat_but_subschema = df[((df["ours_genSuccess"] == True)) & (df["IBM_s1SUBs2"] == True)]
    print("Errors in our tool but not in CC: ", len(df_errors_ours_not_cc))
    print("Errors in CC but not in our tool: ", len(df_errors_cc_not_ours))
    print("Errors in both our tool and CC: ", len(df_errors_ours_and_cc), "\n")
    print("Unsat according to our tool but no subschema according to CC: ", len(df_unsat_but_no_subschema))
    if len(df_unsat_but_no_subschema) > 0:
        print("\tSchemas: ")
        for i, row in df_unsat_but_no_subschema.iterrows():
            print("\t\t", row["objectId"])
    print("Sat according to our tool but subschema according to CC: ", len(df_sat_but_subschema))
    if len(df_sat_but_subschema) > 0:
        print("\tSchemas: ")
        for i, row in df_sat_but_subschema.iterrows():
            print("\t\t", row["objectId"])
    