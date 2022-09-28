#!/bin/bash

logdir=${HOME}/logs
mkdir -p $logdir

echo "Running JSONAlgebra experiments... (this may take a few hours)"
./run-JSONAlgebra.sh 

echo "Running experiments with the DG-tool (jsongenerator)..."
./run-jsongenerator.sh > $logdir/jsongenerator.log

echo "Running evaluation..."
./run-evaluation.sh > ${HOME}/results/evaluation.txt
printf "\nEvaluation are in ${HOME}/results/evaluation.txt\n"

echo "Creating charts..."
(
  cd ${HOME}/charts
  ./create-charts.sh
  printf "\nDone: Results are in ${HOME}/results.\n"
)
