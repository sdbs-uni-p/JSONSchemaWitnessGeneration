#!/usr/bin/env bash

threads=1
timeout=""

while [[ $# -gt 0 ]]; do
  case $1 in
    -i|--input)
      input=$2
      shift
      shift
      ;;
    -w|--witness)
      extract_witness=true
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
    --timeout)
      if ! [[ $2 =~ $re_int ]]; then
        echo "Timeout must be a number greater than 0" >&2
        exit 1
      else
        timeout="--timeout ${2}";
      fi
      shift
      shift
      ;;
    -*|--*|*)
      printf "Unknown option $1\nPossible options are:
      \tNo Option\tExecute experiments on all datasets with 1 thread and no timeout.
      \t-i | --input\tExecute experiments on the specified dataset (given as a path relative to ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/)\n
      \t-w | --witness\tExtract witnesses from the results\n"
      exit 1
      ;;
  esac
done

run_experiment() {
  if [ ! -d "${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}" ]; then
    echo "Dataset ${input} not found."
    return
  fi
  json_files=$(find ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1} -name "*.json")
  output_dir=${HOME}/results/${1//\//-}
  parts_dir=${HOME}/results/${1//\//-}/hypothesis-parts
  mkdir -p ${parts_dir}
  witness_param=""
  if [ -n "$extract_witness" ]; then
    witness_param="--witness ${output_dir}/hypothesis-witness"
  fi

  parallel -j ${threads} --bar python3 ${HOME}/scripts/run-hypothesis.py ${witness_param} -q -i {} -o ${parts_dir} ${timeout} --csv hypothesis-results_{#}.csv --err hypothesis-errors_{#}.txt ::: $json_files
  
  # Merge csv files
  header_written=false
  for file in ${parts_dir}/*.csv; do
    if [[ $header_written = false ]]; then
      cat "$file" > ${output_dir}/hypothesis-results.csv
      header_written=true
    else
      tail -n +2 "$file" >> ${output_dir}/hypothesis-results.csv
    fi
  done
  
  # Merge error files
  rm ${output_dir}/hypothesis-errors.txt 2>/dev/null
  for file in ${parts_dir}/*.txt; do
    cat "$file" >> ${output_dir}/hypothesis-errors.txt
  done

  rm -r ${parts_dir}
}

# If specified, run experiments only on the given input dataset. Otherwise run experiments on all default datasets
if [ -n "$input" ]; then
    run_experiment $input
    exit 0
fi

echo "Running experiments on tricky schemas..."
run_experiment trickyschemas/sat
run_experiment trickyschemas/unsat

echo "Running experiments on ISSTA dataset..."
run_experiment issta/sat
run_experiment issta/unsat

echo "Running experiments on MergeAllOf dataset..."
run_experiment allOf_containment/sat
run_experiment allOf_containment/unsat

echo "Running experiments on Test Suite Containment dataset..."
run_experiment test_suite_containment/sat
run_experiment test_suite_containment/unsat

echo "Running experiments on Schemastore Containment dataset..."
run_experiment schemastore_containment

echo "Running experiments on Handwritten dataset..."
run_experiment handwritten/sat
run_experiment handwritten/unsat

echo "Running experiments on Snowplow dataset..."
run_experiment snowplow/ours
# Move results from snowplow-ours to snowplow
(
    cd ${HOME}/results
    mkdir -p snowplow
    mv snowplow-ours/hypothesis-results.csv snowplow/hypothesis-results.csv
    rm -r snowplow-ours
)

echo "Running experiments on Washington Post dataset ..."
run_experiment wp

echo "Running experiments on GitHub dataset ..."
run_experiment github/sat
run_experiment github/unsat

echo "Running experiments on Kubernetes dataset..."
run_experiment kubernetes/sat
run_experiment kubernetes/unsat
