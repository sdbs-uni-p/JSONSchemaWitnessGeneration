#!/bin/bash
cd ${HOME}/jsonsubschema
python3.9 setup.py install --user &> /dev/null

cd ${HOME}/scripts
./eval-github.py
./eval-handwritten.py
./eval-containment.py
