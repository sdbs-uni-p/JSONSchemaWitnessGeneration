#!/bin/bash

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

cd ${HOME}/JSONAlgebra
export MAVEN_OPTS="-Xmx10240m"

run_experiment() {
    if [ ! -d "${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}" ]; then
      echo "Dataset ${input} not found."
      return
    fi

    mkdir -p ${HOME}/results/${1//\//-}/
    rm JsonSchema_To_Algebra/expDataset/${1}/results/[0-9]*_results.csv 2> /dev/null
    mvn exec:java -Dexec.mainClass="it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.MassiveTesting.MainClassV2" \
            -Dexec.args="${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1} 1 false" -pl JsonSchema_To_Algebra \
            2> ${HOME}/results/${1//\//-}/${1//\//-}-err.log
    cp ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/results/[0-9]*_results.csv ${HOME}/results/${1//\//-}/results.csv 2> /dev/null
}

# If specified, run experiments only on the given input dataset. Otherwise run experiments on all default datasets
if [ -n "$input" ];
  then
    run_experiment $input
    exit 0
fi

echo "Running experiments on Containment dataset..."
run_experiment containment/sat
run_experiment containment/unsat

echo "Running experiments on Handwritten dataset..."
run_experiment handwritten/sat
run_experiment handwritten/unsat


echo "Running experiments on Snowplow dataset..."
run_experiment snowplow/ours
# Move results from snowplow-ours to snowplow
(
    cd ${HOME}/results
    mkdir snowplow 2> /dev/null
    mv snowplow-ours/results.csv snowplow/results.csv
    rm -r snowplow-ours
)
# Copy Snowplow results to charts
mkdir -p ${HOME}/charts/data/snowplow/ 2> /dev/null
cp ${HOME}/results/snowplow/results.csv ${HOME}/charts/data/snowplow/results.csv

echo "Running experiments on Washington Post dataset ..."
run_experiment wp
# Copy Washington Post results to charts
mkdir -p ${HOME}/charts/data/wp/results.csv 2> /dev/null
cp ${HOME}/results/wp/results.csv ${HOME}/charts/data/wp/results.csv

echo "Running experiments on GitHub dataset ..."
run_experiment github/sat
run_experiment github/unsat

# Combine GitHub results for chart generation
(
    cd ${HOME}/results
    mkdir github 2> /dev/null
    awk '(NR == 1) || (FNR > 1)' github-sat/results.csv \
        github-unsat/results.csv  > github/results.csv
    # Copy GitHub results to charts
    mkdir -p ${HOME}/charts/data/github/results.csv 2> /dev/null 
    cp ${HOME}/results/github/results.csv ${HOME}/charts/data/github/results.csv
    rm -r github
)

echo "Running experiments on Kubernetes dataset..."
run_experiment kubernetes/sat
run_experiment kubernetes/unsat

# Combine Kubernetes results for chart generation
(
    cd ${HOME}/results
    mkdir kubernetes 2> /dev/null
    awk '(NR == 1) || (FNR > 1)' kubernetes-sat/results.csv \
        kubernetes-unsat/results.csv > kubernetes/results.csv
    # Copy Kubernetes results to charts
    mkdir -p ${HOME}/charts/data/kubernetes/results.csv 2> /dev/null
    cp ${HOME}/results/kubernetes/results.csv ${HOME}/charts/data/kubernetes/results.csv
    rm -r kubernetes
)
