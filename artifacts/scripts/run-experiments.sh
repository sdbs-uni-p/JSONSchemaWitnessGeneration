#!/bin/bash

dataset=$1
out_dir=${HOME}/results//${dataset//\//-}
mkdir -p ${out_dir}
log_path=${out_dir}/${dataset//\//-}

cd ${HOME}/JSONAlgebra

rm JsonSchema_To_Algebra/expDataset/${dataset}/results/[0-3]*_results.csv 2> /dev/null
mvn exec:java -Dexec.mainClass="it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.MassiveTesting.MainClass" \
        -Dexec.args="${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${dataset} 1 false" -pl JsonSchema_To_Algebra \
        2> ${log_path}-err.log

cp ${HOME}/JSONAlgebra/JsonSchema_To_Algebra/expDataset/${dataset}/results/[0-3]*_results.csv ${HOME}/results/${dataset//\//-}/results.csv 2> /dev/null
