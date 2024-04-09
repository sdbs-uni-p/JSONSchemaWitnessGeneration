#!/usr/bin/env bash

logdir=${HOME}/logs
mkdir -p $logdir

cd scripts

echo "Running JSONAlgebra experiments... (this may take a few hours)"
./run-JSONAlgebra.sh --timeout 600000 2>&1 | tee $logdir/JSONAlgebra.log

echo "Running experiments with the DG-tool (jsongenerator)..."
./run-jsongenerator.sh 2>&1 | tee $logdir/jsongenerator.log

echo "Running experiments with the CC-tool (jsonsubschema)..."
./run-jsonsubschema.sh 2>&1 | tee $logdir/jsonsubschema.log

echo "Running evaluation..."
./evaluate.py > ${HOME}/results/evaluation.txt
printf "\nEvaluation results are in ${HOME}/results/evaluation.txt\n"

echo "Running evaluation (Journal version)..."
./evaluate_journal.py > ${HOME}/results/evaluation_journal.txt
printf "\nEvaluation results are in ${HOME}/results/evaluation_journal.txt\n"

echo "Comparing results for the different tools..."
./compare_tools.sh > ${HOME}/results/comparison.txt

echo "Creating charts..."
(
  cd ${HOME}/charts
  ./create-charts.sh
  printf "\nDone: Results are in ${HOME}/results.\n"
)

echo "To compare the results against our original results, run compare.sh in ${HOME}/scripts/"
