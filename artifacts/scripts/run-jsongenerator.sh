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
run_experiment containment/sat
run_experiment containment/unsat

# Handwritten Data Set
run_experiment handwritten/sat/testtricky
run_experiment handwritten/sat/testtrickynew
run_experiment handwritten/sat/gennumber
run_experiment handwritten/unsat

# Combine results
(
    cd ${HOME}/results
    mkdir handwritten-sat 2> /dev/null
    awk '(NR == 1) || (FNR > 1)' handwritten-sat-testtricky/jsongenerator_results.csv \
        handwritten-sat-testtrickynew/jsongenerator_results.csv \
        handwritten-sat-gennumber/jsongenerator_results.csv > handwritten-sat/jsongenerator_results.csv
	rm -r handwritten-sat-*
)

# GitHub Data Set
run_experiment github/sat-dg
run_experiment github/unsat
(
    cd ${HOME}/results
	mkdir github-sat 2> /dev/null
	mv github-sat-dg/jsongenerator_results.csv github-sat/jsongenerator_results.csv
    rm -r github-sat-dg
)

run_experiment kubernetes/sat
run_experiment kubernetes/unsat
run_experiment snowplow
run_experiment snowplow/dg
(
    cd ${HOME}/results
	mv snowplow/jsongenerator_results.csv snowplow/jsongenerator_results_part.csv
    awk '(NR == 1) || (FNR > 1)' snowplow/jsongenerator_results_part.csv \
        snowplow-dg/jsongenerator_results.csv > snowplow/jsongenerator_results.csv
	rm snowplow/jsongenerator_results_part.csv
	rm -r snowplow-dg
)
run_experiment wp
run_experiment wp/oneOf
(
    cd ${HOME}/results
    mkdir wp 2> /dev/null
	mv wp/jsongenerator_results.csv wp/jsongenerator_results_part.csv
    awk '(NR == 1) || (FNR > 1)' wp/jsongenerator_results_part.csv \
        wp-oneOf/jsongenerator_results.csv > wp/jsongenerator_results.csv
	rm wp/jsongenerator_results_part.csv
	rm -r wp-oneOf
)
