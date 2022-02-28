#!/bin/bash
export MAVEN_OPTS="-Xmx10240m"
cd "${BASH_SOURCE%/*}/"
# The following experiments are run without interpreting OneOf as AnyOf
(
    cd ${HOME}/JSONAlgebra
    sed -i -e 's/public Boolean asAnyOf = true;/public Boolean asAnyOf = false;/g' \
        JsonSchema_To_Algebra/src/main/java/it/unipi/di/tesiFalleniLandi/JsonSchema_to_Algebra/JSONSchema/OneOf.java
    mvn clean install -DskipTests -pl jsonschema-refexpander,JsonSchema_To_Algebra
)

echo "Running experiments on Containment dataset..."
./run-experiments.sh subSchema/allConstAsEnum
./apply_manual_fixes.sh ${HOME}/results/subSchema-allConstAsEnum/results.csv

echo "Running experiments on Handwritten dataset..."
./run-experiments.sh handwritten/testtricky

echo "Running experiments on GitHub dataset (1/4)..."
./run-experiments.sh realWorldSchemas/processedFiles

echo "Running experiments on GitHub dataset (2/4)..."
./run-experiments.sh realWorldSchemas/unsat

# The following experiments are run with interpreting OneOf as AnyOf
(
    cd ${HOME}/JSONAlgebra
    sed -i -e 's/public Boolean asAnyOf = false;/public Boolean asAnyOf = true;/g' \
        JsonSchema_To_Algebra/src/main/java/it/unipi/di/tesiFalleniLandi/JsonSchema_to_Algebra/JSONSchema/OneOf.java
    mvn clean install -DskipTests -pl jsonschema-refexpander,JsonSchema_To_Algebra
)
echo "Running experiments on GitHub dataset (3/4)..."
./run-experiments.sh realWorldSchemas/unprocessedFiles/generationOK

echo "Running experiments on GitHub dataset (4/4)..."
./run-experiments.sh realWorldSchemas/processedFiles/oneOf

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
