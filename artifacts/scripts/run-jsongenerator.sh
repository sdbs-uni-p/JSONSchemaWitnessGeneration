#!/bin/bash
cd ${HOME}/jsongenerator

gradle build

rm ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/subSchema/sat/constAsEnum/results/jsongenerator_* 2> /dev/null
gradle run -Pdata="['/home/repro/JSONAlgebra/JsonSchema_To_Algebra/expDataset/subSchema/sat/constAsEnum/']"
mkdir -p ${HOME}/results/subSchema-sat-constAsEnum/
cp ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/subSchema/sat/constAsEnum/results/jsongenerator_*_results.csv \
    ${HOME}/results/subSchema-sat-constAsEnum/jsongenerator_results.csv 2> /dev/null

rm ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/subSchema/unsat/constAsEnum/results/jsongenerator_* 2> /dev/null
gradle run -Pdata="['/home/repro/JSONAlgebra/JsonSchema_To_Algebra/expDataset/subSchema/unsat/constAsEnum/']"
mkdir -p ${HOME}/results/subSchema-unsat-constAsEnum/
cp ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/subSchema/unsat/constAsEnum/results/jsongenerator_*_results.csv \
    ${HOME}/results/subSchema-unsat-constAsEnum/jsongenerator_results.csv 2> /dev/null

rm ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/handwritten/testtrickyDG/results/jsongenerator_* 2> /dev/null
gradle run -Pdata="['/home/repro/JSONAlgebra/JsonSchema_To_Algebra/expDataset/handwritten/testtrickyDG/']"
mkdir -p ${HOME}/results/handwritten-testtricky/
cp ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/handwritten/testtrickyDG/results/jsongenerator_*_results.csv \
    ${HOME}/results/handwritten-testtricky/jsongenerator_results.csv 2> /dev/null

rm ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/realWorldSchemas/all/results/jsongenerator_* 2> /dev/null
gradle run -Pdata="['/home/repro/JSONAlgebra/JsonSchema_To_Algebra/expDataset/realWorldSchemas/all/']"
mkdir -p ${HOME}/results/realWorldSchemas-all/
cp ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/realWorldSchemas/all/results/jsongenerator_*_results.csv \
    ${HOME}/results/realWorldSchemas-all/jsongenerator_results.csv 2> /dev/null

rm ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/realWorldSchemas/unsatDG/results/jsongenerator_* 2> /dev/null
gradle run -Pdata="['/home/repro/JSONAlgebra/JsonSchema_To_Algebra/expDataset/realWorldSchemas/unsatDG/']"
mkdir -p ${HOME}/results/realWorldSchemas-unsat/
cp ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/realWorldSchemas/unsatDG/results/jsongenerator_*_results.csv \
    ${HOME}/results/realWorldSchemas-unsat/jsongenerator_results.csv 2> /dev/null
