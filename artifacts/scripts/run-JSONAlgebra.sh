#!/bin/bash
cd ${HOME}/JSONAlgebra
export MAVEN_OPTS="-Xmx10240m"

run_experiment() {
    mkdir -p ${HOME}/results/${1//\//-}/
    rm JsonSchema_To_Algebra/expDataset/${1}/results/[0-9]*_results.csv 2> /dev/null
    mvn exec:java -Dexec.mainClass="it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.MassiveTesting.MainClass" \
            -Dexec.args="${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1} 1 false" -pl JsonSchema_To_Algebra \
            2> ${HOME}/results/${1//\//-}/${1//\//-}-err.log
    cp ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1}/results/[0-9]*_results.csv ${HOME}/results/${1//\//-}/results.csv 2> /dev/null
}

# The following experiments are run without interpreting OneOf as AnyOf
(
    cd ${HOME}/JSONAlgebra
    sed -i -e 's/public Boolean asAnyOf = true;/public Boolean asAnyOf = false;/g' \
        JsonSchema_To_Algebra/src/main/java/it/unipi/di/tesiFalleniLandi/JsonSchema_to_Algebra/JSONSchema/OneOf.java
    mvn clean install -DskipTests -pl jsonschema-refexpander,JsonSchema_To_Algebra
)

echo "Running experiments on Containment dataset..."
run_experiment containment/sat
run_experiment containment/unsat

echo "Running experiments on Handwritten dataset..."
run_experiment handwritten/sat/testtricky
run_experiment handwritten/sat/testtrickynew
run_experiment handwritten/sat/gennumber
run_experiment handwritten/unsat

# Combine Handwritten results
(
    cd ${HOME}/results
    mkdir handwritten-sat 2> /dev/null
    awk '(NR == 1) || (FNR > 1)' handwritten-sat-testtricky/results.csv \
        handwritten-sat-testtrickynew/results.csv \
        handwritten-sat-gennumber/results.csv > handwritten-sat/results.csv
)

echo "Running experiments on Kubernetes dataset..."
run_experiment kubernetes/sat
run_experiment kubernetes/unsat

# Combine Kubernetes results
(
    cd ${HOME}/results
    mkdir kubernetes 2> /dev/null
    awk '(NR == 1) || (FNR > 1)' kubernetes-sat/results.csv \
        kubernetes-unsat/results.csv > kubernetes/results.csv
)
# Copy Kubernetes results to charts
cp ${HOME}/results/kubernetes/results.csv ${HOME}/charts/data/kubernetes/results.csv

echo "Running experiments on Snowplow dataset..."
run_experiment snowplow
cp ${HOME}/results/snowplow/results.csv ${HOME}/charts/data/snowplow/results.csv

echo "Running experiments on Washington Post dataset (1/2)..."
run_experiment wp

echo "Running experiments on GitHub dataset (1/4)..."
run_experiment github/sat

echo "Running experiments on GitHub dataset (2/4)..."
run_experiment github/unsat

# The following experiments are run with interpreting OneOf as AnyOf
(
    cd ${HOME}/JSONAlgebra
    sed -i -e 's/public Boolean asAnyOf = false;/public Boolean asAnyOf = true;/g' \
        JsonSchema_To_Algebra/src/main/java/it/unipi/di/tesiFalleniLandi/JsonSchema_to_Algebra/JSONSchema/OneOf.java
    mvn clean install -DskipTests -pl jsonschema-refexpander,JsonSchema_To_Algebra
)

echo "Running experiments on GitHub dataset (3/3)..."
run_experiment github/sat/oneOf

echo "Running experiments on Washington Post dataset (1/2)..."
run_experiment wp/oneOf

# Combine GitHub results
(
    cd ${HOME}/results
    mkdir github-all 2> /dev/null
    mv github-sat/results.csv github-sat/results_part.csv
    awk '(NR == 1) || (FNR > 1)' github-sat/results_part.csv \
        github-sat-oneOf/results.csv > github-sat/results.csv

    awk '(NR == 1) || (FNR > 1)' github-sat/results.csv \
        github-unsat/results.csv  > github-all/results.csv
	# Copy GitHub results to charts
	cp ${HOME}/results/github-all/results.csv ${HOME}/charts/data/github/results.csv
	rm -r github-all
)


# Combine Washington Post results
(
    cd ${HOME}/results
    mkdir wp-all 2> /dev/null
    awk '(NR == 1) || (FNR > 1)' wp/results.csv \
        wp-oneOf/results.csv > wp-all/results.csv
		
	# Copy Washington Post results to charts
	cp ${HOME}/results/wp-all/results.csv ${HOME}/charts/data/wp/results.csv
	rm -r wp-all
)
