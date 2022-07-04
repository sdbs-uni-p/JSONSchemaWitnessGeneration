#!/bin/bash
cd ${HOME}/JSONAlgebra
export MAVEN_OPTS="-Xmx10240m"

run_experiment() {
    rm JsonSchema_To_Algebra/expDataset/${1}/results/[0-9]*_results.csv 2> /dev/null
    mvn exec:java -Dexec.mainClass="it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.MassiveTesting.MainClass" \
            -Dexec.args="${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${1} 1 false" -pl JsonSchema_To_Algebra \
            2> ${HOME}/results/${1//\//-}/${1//\//-}-err.log
    mkdir -p ${HOME}/results/${1//\//-}/
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
run_experiment subSchema/allConstAsEnum
${HOME}/scripts/apply_manual_fixes.sh ${HOME}/results/subSchema-allConstAsEnum/results.csv

echo "Running experiments on Handwritten dataset..."
run_experiment handwritten/testtricky

echo "Running experiments on Kubernetes dataset..."
./run_experiment.sh kubernetes

echo "Running experiments on Snowplow dataset..."
./run_experiment.sh snowplow

echo "Running experiments on Washington Post dataset (1/2)..."
./run_experiment.sh wp

echo "Running experiments on GitHub dataset (1/4)..."
run_experiment realWorldSchemas/processedFiles

echo "Running experiments on GitHub dataset (2/4)..."
run_experiment realWorldSchemas/unsat

# The following experiments are run with interpreting OneOf as AnyOf
(
    cd ${HOME}/JSONAlgebra
    sed -i -e 's/public Boolean asAnyOf = false;/public Boolean asAnyOf = true;/g' \
        JsonSchema_To_Algebra/src/main/java/it/unipi/di/tesiFalleniLandi/JsonSchema_to_Algebra/JSONSchema/OneOf.java
    mvn clean install -DskipTests -pl jsonschema-refexpander,JsonSchema_To_Algebra
)
echo "Running experiments on GitHub dataset (3/4)..."
run_experiment realWorldSchemas/unprocessedFiles/generationOK

echo "Running experiments on GitHub dataset (4/4)..."
run_experiment realWorldSchemas/processedFiles/oneOf

echo "Running experiments on Washington Post dataset (1/2)..."
./run_experiment.sh wp/oneOf

# Combine realWorldSchemas results
(
    cd ${HOME}/results
    mkdir realWorldSchemas-all 2> /dev/null
    awk '(NR == 1) || (FNR > 1)' realWorldSchemas-processedFiles/results.csv \
        realWorldSchemas-processedFiles-oneOf/results.csv \
        realWorldSchemas-unsat/results.csv \
        realWorldSchemas-unprocessedFiles-generationOK/results.csv > realWorldSchemas-all/results.csv
    cp realWorldSchemas-all/results.csv ${HOME}/Charts/data/
    cd ${HOME}/scripts
    ./apply_manual_fixes.sh ${HOME}/results/realWorldSchemas-all/results.csv
)

# Combine WashingtonPost results
(
    cd ${HOME}/results
    mkdir wp-all 2> /dev/null
    awk '(NR == 1) || (FNR > 1)' wp/results.csv \
        wp-oneOf/results.csv > wp-all/results.csv
)
