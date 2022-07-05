#!/bin/bash
cd ${HOME}/jsongenerator

gradle build

run_experiment() {
    rm ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/results/jsongenerator_* 2> /dev/null
    gradle run -Pdata="['/home/repro/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/']"
    mkdir -p ${HOME}/results/${1//\//-}/
    cp ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/results/jsongenerator_*_results.csv \
        ${HOME}/results/${1//\//-}/jsongenerator_results.csv 2> /dev/null
}

# Containment Data Set
run_experiment subSchema/sat/constAsEnum
run_experiment subSchema/unsat/constAsEnum

# Handwritten Data Set
run_experiment handwritten/testtricky
run_experiment handwritten/testtrickynew
run_experiment handwritten/gennumber

# Combine results
(
    cd ${HOME}/results
    mkdir handwritten-all 2> /dev/null
    awk '(NR == 1) || (FNR > 1)' handwritten-testtricky/jsongenerator_results.csv \
        handwritten-testtrickynew/jsongenerator_results.csv \
        handwritten-gennumber/jsongenerator_results.csv > handwritten-all/jsongenerator_results.csv
)

# GitHub Data Set
run_experiment realWorldSchemas/all
run_experiment realWorldSchemas/unsat

run_experiment kubernetes
run_experiment snowplow
run_experiment wp
run_experiment wp/oneOf
(
    cd ${HOME}/results
    mkdir wp-all 2> /dev/null
    awk '(NR == 1) || (FNR > 1)' wp/jsongenerator_results.csv \
        wp-oneOf/jsongenerator_results.csv > wp-all/jsongenerator_results.csv
)
