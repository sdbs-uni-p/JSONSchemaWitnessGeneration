#!/bin/bash

# Build our tool
export MAVEN_OPTS="-Xmx10240m"
cd ${HOME}/JSONAlgebra
mvn clean install -DskipTests -pl jsonschema-refexpander,JsonSchema_To_Algebra

# Build jsongenerator
cd ${HOME}/jsongenerator
gradle build

# Build jsonsubschema
cd ../jsonsubschema
python3 setup.py install --user
cd ..