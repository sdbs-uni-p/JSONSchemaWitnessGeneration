#!/usr/bin/env bash

echo "=== Comparing results for schemastore_containment ==="
python3 compare_tools.py --ours /home/repro/results/schemastore_containment/results.csv \
                         --dg /home/repro/results/schemastore_containment/jsongenerator_results.csv \
                         --cc /home/repro/results/schemastore_containment_schemaPairs/jsonsubschema_results.csv

echo -e "\n=== Comparing results for trickyschemas ==="
python3 compare_tools.py --ours /home/repro/results/trickyschemas-sat/results.csv /home/repro/results/trickyschemas-unsat/results.csv \
                         --dg /home/repro/results/trickyschemas-sat/jsongenerator_results.csv /home/repro/results/trickyschemas-unsat/jsongenerator_results.csv \
                         --cc /home/repro/results/trickyschemas_schemaPairs/jsonsubschema_results.csv

echo -e "\n=== Comparing results for allOf_containment ==="
python3 compare_tools.py --ours /home/repro/results/allOf_containment-sat/results.csv /home/repro/results/allOf_containment-unsat/results.csv \
                         --dg /home/repro/results/allOf_containment-sat/jsongenerator_results.csv /home/repro/results/allOf_containment-unsat/jsongenerator_results.csv \
                         --cc /home/repro/results/allOf_containment_schemaPairs/jsonsubschema_results.csv