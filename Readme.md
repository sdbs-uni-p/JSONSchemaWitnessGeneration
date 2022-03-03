# JSON Schema Witness Generation - Reproduction Package

This is a reproduction package for the article [Witness Generation for JSON Schema](http://arxiv.org/abs/2202.12849),
by Lyes Attouche, Mohamed-Amine Baazizi, Dario Colazzo, Giorgio Ghelli, Carlo Sartiani, and Stefanie Scherzinger.

This reproduction package has been created by Stefan Klessinger.
The package is provided as a Docker container.

Our original results are provided in directory [results](artifacts/results). These files are overwritten if you calculate new results in the container. 

## Setting up the Docker container
First off, clone this repository, including the sub-modules with 
``git clone --recurse-submodules https://github.com/sdbs-uni-p/JSONSchemaWitnessGeneration.git``. 
The sub-modules are provided at specific commits. Please, make sure not to update them.

To build the container run ``docker build -t js_repro .`` inside the root directory of this repository.

After building you can start the container with ``docker run -it js_repro``. 

## Running experiments
We provide a number of scripts for running our experiments inside the container.

To run all experiments, execute ``./doAll.sh``. Note that this can take **several hours**.
For this reason, we also make our original results available. 


There are several scripts in [scripts](artifacts/scripts) with the following functionality:
* ``./run-evaluation.sh`` performs the evaluation on all datasets. Results from our tool and jsongenerator are taken from [results](artifacts/results) while results for jsonsubschema (CC) (which is only used on the *Containment-draft4* dataset) are calculated when running this script.
* ``./run-JSONAlgebra`` performs experiments on the *GitHub*, *Handwritten* and *Containment-draft4* dataset with our tool. Note that the originals results we provide are overwritten by this script, as well as the results used for generating charts.
* ``./run-jsongenerator`` performs experiments on the *GitHub*, *Handwritten* and *Containment-draft4* dataset with jsongenerator (DG). Note that the originals results we provide are overwritten by this script.
* ``./setup.sh`` builds and installs our tool, jsongenerator and jsonsubschema. You do **not** have to run this in order to use the scripts above.
* ``run-experiments.sh`` is used to run our tool on specific datasets. It takes an input folder as a parameter, specified as a path relative to [JSONAlgebra/JsonSchema_To_Algebra/expDataset/](artifacts/JSONAlgebra/JsonSchema_To_Algebra/expDataset/)
* ``patch-jsongenerator.sh`` is executed when building the container and it should not be necessary to run this script.

## Comparing the Results
To compare the computed results with the results stated in the paper, inspect the file evaluation.txt in the directory [results](artifacts/results) after running ``./doAll.sh``. Evaluation.txt also contains comments on a few selected manual changes that we had to make, e.g., due to wrong results produced by the third-party schmema validator.

The values of average and median times for DG, as well as the values for "Success" and "Logical Errors unsatisfiable" differ from Table 2 in the submitted paper. However, caluclations in this reproduction package are actually correct.

## Generating Charts
To generate charts, execute ``./create-charts.sh`` at [Charts/](artifacts/Charts) inside the docker container. The generated charts are stored in [Charts/charts](artifacts/Charts/charts)

## Moving Output to the Host System
All results are stored at /home/repro/results in the container. To copy the results to the host system, use ``docker cp <containerID>:/home/repro/results .`` after obtaining the containerID using ``docker ps``
