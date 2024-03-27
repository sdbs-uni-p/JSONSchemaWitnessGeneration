#!/usr/bin/env bash

while [[ $# -gt 0 ]]; do
  case $1 in
    -i|--input)
      input=$2
      shift
      shift
      ;;
    -*|--*|*)
      printf "Unknown option $1\nPossible options are:
      \tNo Option\tExecute experiments on all datasets
      \t-i | --input\tExecute experiments on the specified dataset (given as a path relative to ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/)\n"
      exit 1
      ;;
  esac
done

cd ${HOME}/jsongenerator

run_experiment() {
    if [ ! -d "${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}" ]; then
      echo "Dataset ${input} not found."
      return
    fi

    rm ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/results/jsongenerator_* 2> /dev/null
    gradle run -Pdata="['/home/repro/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/']"
    mkdir -p ${HOME}/results/${1//\//-}/
    cp ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/results/jsongenerator_*_results.csv \
        ${HOME}/results/${1//\//-}/jsongenerator_results.csv 2> /dev/null
}

# If specified, run experiments only on the given input dataset. Otherwise run experiments on all default datasets
if [ -n "$input" ];
  then
    run_experiment $input
    exit 0
fi

# Trickyschemas Data Set
run_experiment trickyschemas/sat
run_experiment trickyschemas/unsat

# MergeAllOf Data Set
echo "Running experiments on MergeAllOf dataset..."
run_experiment allOf_containment/sat
run_experiment allOf_containment/unsat

# Containment Data Set
echo "Running experiments on Test Suite Containment dataset..."
run_experiment test_suite_containment/sat
run_experiment test_suite_containment/unsat

# Schemastore Containment Data Set
echo "Running experiments on Schemastore Containment dataset..."
run_experiment schemastore_containment

# Handwritten Data Set
echo "Running experiments on Handwritten dataset..."
run_experiment handwritten/sat
run_experiment handwritten/unsat

# GitHub Data Set
echo "Running experiments on GitHub datasets..."
run_experiment github/sat-dg
run_experiment github/unsat

# Move results from github-sat-dg to github-sat
(
    cd ${HOME}/results
    mkdir github-sat 2> /dev/null
    mv github-sat-dg/jsongenerator_results.csv github-sat/jsongenerator_results.csv
    rm -r github-sat-dg
)

echo "Running experiments on Kubernetes datasets..."
run_experiment kubernetes/sat
run_experiment kubernetes/unsat

echo "Running experiments on Snowplow datasets..."
run_experiment snowplow/dg

# Move results from snowplow-dg to snowplow
(
    cd ${HOME}/results
    mkdir snowplow 2> /dev/null
    mv snowplow-dg/jsongenerator_results.csv snowplow/jsongenerator_results.csv
    rm -r snowplow-dg
)

echo "Running experiments on Washington Post dataset ..."
run_experiment wp
