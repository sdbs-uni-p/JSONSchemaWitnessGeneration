#!/bin/bash

threads=1
timeout="false"
re_int='^[0-9]+$'

while [[ $# -gt 0 ]]; do
  case $1 in
    -i|--input)
      input=$2
      shift
      shift
      ;;
    --timeout)
      if ! [[ $2 =~ $re_int ]]; then
        echo "Timeout must be a number greater than 0" >&2
        exit 1
      else
        timeout="true ${2}";
      fi
      shift
      shift
      ;;
    --threads)
      if ! [[ $2 =~ $re_int ]]; then
        echo "Thread count must be a number greater than 0" >&2
        exit 1
      else
        threads=$2;
      fi
      shift
      shift
      ;;
    -w|--witness)
      extract_witness=true
      shift
      ;;
    -*|--*|*)
      printf "Unknown option $1\nPossible options are:
      \tNo Option\tExecute experiments on all datasets with 1 thread and no timeout.
      \t-i | --input\tExecute experiments on the specified dataset (given as a path relative to ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/)
      \t--threads\tSet the number of threads to be used (default 1). When using more than one thread, consider increasing the heap size (in this script).
      \t--timeout\tSet the timeout in Milliseconds (default no timeout)\n"
      exit 1
      ;;
  esac
done

cd ${HOME}/JSONAlgebra
export MAVEN_OPTS="-Xmx10240m"

extract_witnesses() {
  cd $1
  rm -rf witness/
  mkdir witness
  {
    read #skip first line (csv header)
    while read line
    do
      witness_cleaned=$(echo "${line}" | cut -d, -f2- -s | sed 's/^\"//g' | sed 's/\"$//g' | sed 's/\"\"/\"/g')
      filename=$(echo "${line}" | cut -d, -f1 -s)
      #echo $filename
      echo "${witness_cleaned}" | jq '.' > witness/"${filename}_witness.json"
    done
  } < "witness.csv"
}

run_experiment() {
    if [ ! -d "${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}" ]; then
      echo "Dataset ${input} not found."
      return
    fi
    mkdir -p ${HOME}/results/${1//\//-}/
    rm JsonSchema_To_Algebra/expDataset/${1}/results/[0-9]*_results.csv 2> /dev/null
    rm JsonSchema_To_Algebra/expDataset/${1}/results/[0-9]*_witness.csv 2> /dev/null
    mvn exec:java -Dexec.mainClass="it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.MassiveTesting.MainClassV2" \
            -Dexec.args="${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1} ${threads} ${timeout}" -pl JsonSchema_To_Algebra \
            2> >(tee ${HOME}/results/${1//\//-}/${1//\//-}-err.log >&2)
    cp ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/results/[0-9]*_results.csv ${HOME}/results/${1//\//-}/results.csv 2> /dev/null
    if [ "$extract_witness" = true ]; then
        cp ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/results/[0-9]*_witness.csv ${HOME}/results/${1//\//-}/witness.csv
        extract_witnesses ${HOME}/results/${1//\//-}/
    fi
}

# If specified, run experiments only on the given input dataset. Otherwise run experiments on all default datasets
if [ -n "$input" ];
  then
    run_experiment $input
    exit 0
fi

echo "Running experiments on MergeAllOf dataset..."
run_experiment allOf_containment/sat
run_experiment allOf_containment/unsat

# Combine allOf Containment results for chart generation
(
    cd ${HOME}/results
    mkdir allOf_containment 2> /dev/null
    awk '(NR == 1) || (FNR > 1)' allOf_containment-sat/results.csv \
        allOf_containment-unsat/results.csv  > allOf_containment/results.csv
    # Copy allOf Containment results to charts
    mkdir -p ${HOME}/charts/data/allOf_containment/ 2> /dev/null
    cp ${HOME}/results/allOf_containment/results.csv ${HOME}/charts/data/allOf_containment/results.csv
    rm -r allOf_containment
)

echo "Running experiments on Containment dataset..."
run_experiment test_suite_containment/sat
run_experiment test_suite_containment/unsat

echo "Running experiments on Schemastore Containment dataset..."
run_experiment schemastore_containment
mkdir -p ${HOME}/charts/data/schemastore_containment/ 2> /dev/null
cp ${HOME}/results/schemastore_containment/results.csv ${HOME}/charts/data/schemastore_containment/results.csv

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
mkdir -p ${HOME}/charts/data/wp/ 2> /dev/null
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
    mkdir -p ${HOME}/charts/data/github/ 2> /dev/null
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
    mkdir -p ${HOME}/charts/data/kubernetes/ 2> /dev/null
    cp ${HOME}/results/kubernetes/results.csv ${HOME}/charts/data/kubernetes/results.csv
    rm -r kubernetes
)
