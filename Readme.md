# JSON Schema Witness Generation - Reproduction Package

This is a reproduction package for the article [Witness Generation for JSON Schema](http://arxiv.org/abs/2202.12849),
by Lyes Attouche, Mohamed-Amine Baazizi, Dario Colazzo, Giorgio Ghelli, Carlo Sartiani, and Stefanie Scherzinger.

This reproduction package has been created by Stefan Klessinger.
It is provided as a Docker container.

Our original results are available in directory [results](artifacts/results). These files are overwritten if you calculate new results in the container. 

## Setting up the Docker container
First off, clone this repository with 
``git clone https://github.com/sdbs-uni-p/JSONSchemaWitnessGeneration.git``. 

To build the container run ``docker build -t wg_repro .`` inside the root directory of this repository.

After building, you can start the container with ``docker run -it wg_repro``. You can include the flag ``--name <name>`` (replacing ``<name>`` with a name of your choice) to easier identify the container.

## Data Sets
In this reproduciton package, our own tool, as well as, competitor tools are executed on the same data sets as in the accompanying paper. The datasets are stored in [expDataset](artifacts/JSONAlgebra/JsonSchema_To_Algebra/expDataset). Datasets containing both, satisfiable and unsatisfibale, schemas are divided according into subdirectories ``sat``  and ``unsat``. In the GitHub dataset, we have different satisifiable datasets for jsongenerator (sat-dg) and our tool (sat), where different files are excluded as the tools had problems with processing them. These files are considered as failures in our evaluation script. Likewise, in the Snowplow dataset, two files in the subdirectory ``dg`` are excluded from our tool and only processed by jsongenerator.

## Running experiments
We provide a number of scripts for running our experiments inside the container.

To run all experiments, execute ``./doAll.sh``. Note that this can take **several hours**.
For this reason, we also make our original results available, allowing to only replicate the evaluation.


There are several scripts in [scripts](artifacts/scripts) with the following functionality:
* ``patch-jsongenerator.sh`` is executed when building the container and it should not be necessary to run this script.
* ``./setup.sh`` builds and installs our tool, jsongenerator and jsonsubschema.
* ``./run-JSONAlgebra`` performs experiments on the *GitHub*, *Handwritten* and *Containment-draft4* dataset with our tool. Note that the originals results we provide are overwritten by this script, as well as the results used for generating charts.
* ``./run-jsongenerator`` performs experiments on the *GitHub*, *Handwritten* and *Containment-draft4* dataset with jsongenerator (DG). Note that the originals results we provide are overwritten by this script.
* ``./run-evaluation.sh`` performs the evaluation on all datasets. Results from our tool and jsongenerator are taken from [results](artifacts/results) while results for jsonsubschema (CC) (which is only used on the *Containment-draft4* dataset) are calculated when running this script. Configuration, including paths and manual corrections is performed in [config.json](artifacts/scripts)

## Comparing the Results
To compare the computed results with the results stated in the paper, inspect the file evaluation.txt in the directory [results](artifacts/results) after running ``./doAll.sh``. Evaluation.txt also contains comments on a few selected manual changes that we had to make, e.g., due to wrong results produced by the third-party schema validator.

As the algorithm of our tool is not entirely deterministic, results may vary slightly. However, we have only experienced deviations of no more than 3 files, which accounted for roughly 0.05% in our GitHub data set.

## Generating Charts
To generate charts, execute ``./create-charts.sh`` at [Charts/](artifacts/Charts) inside the docker container. The generated charts are stored in [Charts/charts](artifacts/Charts/charts).

## Moving Output to the Host System
All results are stored at /home/repro/results in the container. To copy the results to the host system, use ``docker cp <containerID>:/home/repro/results .`` after obtaining the containerID using ``docker ps``. If you use the ``--name`` flag, as explained above, you can replaced the containerID with the chosen name.
