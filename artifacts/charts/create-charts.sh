#!/bin/bash
cd "${BASH_SOURCE%/*}/"

mkdir -p /home/repro/results/charts/

# Setup Linux Libertine Font
echo 'Setting up fonts ...'
(cd utils && Rscript load_font.r &> /dev/null)

echo 'Preparing data ...'
Rscript clean_data.r > /dev/null

echo 'Creating box plots ...'
Rscript boxplots.r > /dev/null
Rscript boxplots_schemastore_containment.r > /dev/null

echo 'Creating hexplots ...'
Rscript hexplot-histogram_github.r > /dev/null
Rscript hexplot-histogram_kubernetes.r > /dev/null
Rscript hexplot-histogram_snowplow.r > /dev/null
Rscript hexplot-histogram_wp.r > /dev/null
Rscript hexplot-histogram_allOf_containment.r > /dev/null
Rscript hexplot-histogram_schemastore_containment.r > /dev/null
Rscript hexplot-histogram_issta.r > /dev/null

cp output/* ../results/charts/
printf "\nCharts are in ${HOME}/results/charts\n"
