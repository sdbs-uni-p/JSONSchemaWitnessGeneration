#!/bin/bash

OUTDIR=${HOME}/results/

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
    outdir=${OUTDIR}/${1//\//-}/
    mkdir -p ${outdir}
    python3 ${HOME}/scripts/run-jsonsubschema_journal.py -i ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1} \
            -o ${outdir}/jsonsubschema_results.csv ${testsuite}
}

run_experiment allOf_containment_schemaPairs
run_experiment test_suite_containment/schemaPairs True
run_experiment schemastore_containment_schemaPairs