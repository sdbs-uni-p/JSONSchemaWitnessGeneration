#!/usr/bin/env bash

OUTDIR=${HOME}/results/
timeout=""
input=""
threads=1
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
        timeout="--timeout ${2}";
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
    -*|--*|*)
      printf "Unknown option $1\nPossible options are:
      \tNo Option\tExecute experiments on all datasets with one thread and no timeout.
      \t-i | --input\tExecute experiments on the specified dataset (given as a path relative to ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/).\n
      \t--threads\tSet the number of threads to be used (default 1). When using more than one thread, consider increasing the heap size (in this script).
      \t--timeout\tSet the timeout in Milliseconds (default: no timeout). Timeouts are rounded up to the nearest second. 
      \t\t\tIt is expected in milliseconds to be consistent with the other tools/scripts.\n"
      exit 1
      ;;
  esac
done
run_experiment() {
    if [ ! -d "${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}" ]; then
      echo "Dataset ${input} not found."
      return
    fi
    # if 2nd parameter is set and has value True, set flag -t
    if [ ! -z "${2}" ] && [ "${2}" == "True" ]; then
        testsuite="-t"
    else
        testsuite=""
    fi

    json_files=$(find ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1} -name "*.json")
    output_dir=${HOME}/results/${1//\//-}
    parts_dir=${HOME}/results/${1//\//-}/jsonsubschema-parts
    mkdir -p ${parts_dir}

    outdir=${OUTDIR}/${1//\//-}/
    mkdir -p ${outdir}
    parallel -j ${threads} --bar python3 -u ${HOME}/scripts/run-jsonsubschema_journal.py -i {} \
            -o ${parts_dir}/jsonsubschema_results_{#}.csv ${testsuite} ${timeout} ::: $json_files

    # Merge csv files
    header_written=false
    for file in ${parts_dir}/*.csv; do
      if [[ $header_written = false ]]; then
        cat "$file" > ${output_dir}/jsonsubschema_results.csv
        header_written=true
      else
        tail -n +2 "$file" >> ${output_dir}/jsonsubschema_results.csv
      fi
    done

    rm -r ${parts_dir}
}

# If specified, run experiments only on the given input dataset. Otherwise run experiments on all default datasets
if [ -n "$input" ];
  then
    run_experiment $input
    exit 0
fi

run_experiment trickyschemas_schemaPairs
run_experiment allOf_containment_schemaPairs
run_experiment test_suite_containment/schemaPairs True
run_experiment schemastore_containment_schemaPairs
run_experiment issta_schemaPairs
