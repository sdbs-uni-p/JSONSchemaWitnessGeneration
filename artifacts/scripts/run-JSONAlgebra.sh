#!/usr/bin/env bash

# TODO: can we remove some initializations?
threads=1
timeout="false 0"
re_int='^[0-9]+$'
quiet=false
quietquiet=false
bar=""
jar_file="${HOME}/JSONAlgebra/JsonSchema_To_Algebra/target/JsonSchema_to_Algebra-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
extract_witness=false
warmup="false 0"

while [[ $# -gt 0 ]]; do
  case $1 in
    -i|--input)
      input=$2
      shift
      shift
      ;;
    --timeout)
      if ! [[ $2 =~ $re_int ]]; then
        echo "Timeout must be a number greater than 0 (ms)" >&2
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
    -q)
      quiet=true
      shift
      ;;
    -qq)
      quietquiet=true
      shift
      ;;
    --bar)
      bar="--bar"
      shift
      ;;
    --warmup)
      if ! [[ $2 =~ $re_int ]]; then
        echo "Warmup must be a number greater than 0 (number of warmup runs)" >&2
        exit 1
      else
        warmup="true ${2}";
      fi
      shift
      shift
      ;;
    -*|--*|*)
      printf "Unknown option $1\nPossible options are:
      \tNo Option\tExecute experiments on all datasets with 1 thread and no timeout.
      \t-i | --input\tExecute experiments on the specified dataset (given as a path relative to ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/)
      \t--threads\tSet the number of threads to be used (default 1). When using more than one thread, consider increasing the heap size (in this script).
      \t--timeout\tSet the timeout in Milliseconds (default no timeout)\n
      \t-w | --witness\tExtract witnesses from the results.
      \t-q\tQuiet mode: Do not print the output of the witness generation, excluding errors.
      \t-qq\tQuiet quiet mode: Do not print the output of the witness generation, including errors."
      exit 1
      ;;
  esac
done

cd ${HOME}/JSONAlgebra

extract_witnesses() {
  cd $1
  rm -rf witness/
  mkdir witness
  {
    read #skip first line (csv header)
    while IFS= read -r line
    do
      # Remove leading and trailing quotes, and replace double quotes ("") with single quotes (")
      witness_cleaned=$(echo "${line}" | cut -d, -f2- -s | sed 's/^\"//g' | sed 's/\"$//g' | sed 's/\"\"/\"/g')
      filename=$(echo "${line}" | cut -d, -f1 -s)
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
    mkdir -p ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/results/archive
    rm -rf ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/results/parts
    mkdir -p ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/results/parts
    mv ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/results/[0-9]*.csv ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/results/archive 2> /dev/null
    export _JAVA_OPTIONS="-Xmx10G"
    json_files=$(find ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1} -name "*.json")
    start_time=$(date +%s)
    timestamp=$(date +%y%m%d_%H%M%S)

    if [ "$quietquiet" = true ]; then
      parallel -j ${threads} ${bar} java -jar "${jar_file}" {} 1 ${timeout} "parts/${timestamp}_{#}" ${warmup} '>/dev/null 2>&1' ::: $json_files
    elif [ "$quiet" = true ]; then
      parallel -j ${threads} ${bar} java -jar "${jar_file}" {} 1 ${timeout} "parts/${timestamp}_{#}" ${warmup} '>/dev/null' ::: $json_files
    else
      parallel -j ${threads} ${bar} java -jar "${jar_file}" {} 1 ${timeout} "parts/${timestamp}_{#}" ${warmup} ::: $json_files
    fi
    file_endings_to_merge=("results.csv" "witness.csv" "size.csv" "validation.csv" "validationException.csv" "exception.csv")
    output_file_parts="${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/results/parts/${timestamp}"
    output_file_res="${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/results/${timestamp}"
    for file_ending in "${file_endings_to_merge[@]}"; do
      output_file="${output_file_res}_${file_ending}"
      counter=0
      file_parts=$(ls ${output_file_parts}*${file_ending} 2> /dev/null)
      for file in $file_parts; do
        if [ $counter -eq 0 ]; then
          mv "$file" "$output_file"
        else
          tail -n +2 "$file" >> "$output_file"
          rm "$file"
        fi
        counter=$((counter + 1))
      done
    done
    cp ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/results/[0-9]*_results.csv ${HOME}/results/${1//\//-}/results.csv 2> /dev/null
    if [ "$extract_witness" = true ]; then
        cp ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/results/[0-9]*_witness.csv ${HOME}/results/${1//\//-}/witness.csv
        (
          extract_witnesses ${HOME}/results/${1//\//-}/
        )
    fi
}

if [ "$quietquiet" = true ]; then
  echo "Warning: -qq is set; errors in Witness Generation won't be displayed."
fi

# If specified, run experiments only on the given input dataset. Otherwise run experiments on all default datasets
if [ -n "$input" ];
  then
    run_experiment $input
    exit 0
fi

if [ ! -f "${jar_file}" ]; then
  echo "Jar file not found. Building project..."
  (cd "${HOME}/JSONAlgebra/JsonSchema_To_Algebra" && mvn clean install -DskipTests)
fi

echo "Running experiments on tricky schemas..."
run_experiment trickyschemas/sat
run_experiment trickyschemas/unsat

echo "Running experiments on ISSTA dataset..."
run_experiment issta/sat
run_experiment issta/unsat

# Combine ISSTA results for chart generation
(
    cd ${HOME}/results
    mkdir -p issta
    awk '(NR == 1) || (FNR > 1)' issta-sat/results.csv \
        issta-unsat/results.csv > issta/results.csv

    mkdir -p ${HOME}/charts/data/issta/
    cp ${HOME}/results/issta/results.csv ${HOME}/charts/data/issta/results.csv
    rm -r issta
)

echo "Running experiments on MergeAllOf dataset..."
run_experiment allOf_containment/sat
run_experiment allOf_containment/unsat

# Combine allOf Containment results for chart generation
(
    cd ${HOME}/results
    mkdir -p allOf_containment
    awk '(NR == 1) || (FNR > 1)' allOf_containment-sat/results.csv \
        allOf_containment-unsat/results.csv  > allOf_containment/results.csv

    mkdir -p ${HOME}/charts/data/allOf_containment/
    cp ${HOME}/results/allOf_containment/results.csv ${HOME}/charts/data/allOf_containment/results.csv
    rm -r allOf_containment
)

echo "Running experiments on Test Suite Containment dataset..."
run_experiment test_suite_containment/sat
run_experiment test_suite_containment/unsat

echo "Running experiments on Schemastore Containment dataset..."
run_experiment schemastore_containment
mkdir -p ${HOME}/charts/data/schemastore_containment/
cp ${HOME}/results/schemastore_containment/results.csv ${HOME}/charts/data/schemastore_containment/results.csv

echo "Running experiments on Handwritten dataset..."
run_experiment handwritten/sat
run_experiment handwritten/unsat


echo "Running experiments on Snowplow dataset..."
run_experiment snowplow/ours
# Move results from snowplow-ours to snowplow
(
    cd ${HOME}/results
    mkdir -p snowplow
    mv snowplow-ours/results.csv snowplow/results.csv
    rm -r snowplow-ours
)
mkdir -p ${HOME}/charts/data/snowplow/
cp ${HOME}/results/snowplow/results.csv ${HOME}/charts/data/snowplow/results.csv

echo "Running experiments on Washington Post dataset ..."
run_experiment wp

mkdir -p ${HOME}/charts/data/wp/
cp ${HOME}/results/wp/results.csv ${HOME}/charts/data/wp/results.csv

echo "Running experiments on GitHub dataset ..."
run_experiment github/sat
run_experiment github/unsat

# Combine GitHub results for chart generation
(
    cd ${HOME}/results
    mkdir -p github
    awk '(NR == 1) || (FNR > 1)' github-sat/results.csv \
        github-unsat/results.csv  > github/results.csv

    mkdir -p ${HOME}/charts/data/github/
    cp ${HOME}/results/github/results.csv ${HOME}/charts/data/github/results.csv
    rm -r github
)

echo "Running experiments on Kubernetes dataset..."
run_experiment kubernetes/sat
run_experiment kubernetes/unsat

# Combine Kubernetes results for chart generation
(
    cd ${HOME}/results
    mkdir -p kubernetes
    awk '(NR == 1) || (FNR > 1)' kubernetes-sat/results.csv \
        kubernetes-unsat/results.csv > kubernetes/results.csv

    mkdir -p ${HOME}/charts/data/kubernetes/
    cp ${HOME}/results/kubernetes/results.csv ${HOME}/charts/data/kubernetes/results.csv
    rm -r kubernetes
)

run_experiment additional_as_uneval_eliminated_containment/sat
run_experiment additional_as_uneval_eliminated_containment/unsat
run_experiment additional_as_uneval_eliminated_containment/unknown

run_experiment oneOf_as_anyOf_containment/sat
run_experiment oneOf_as_anyOf_containment/unknown

run_experiment uneval_eliminated_as_additional_containment/sat
run_experiment uneval_eliminated_as_additional_containment/unknown