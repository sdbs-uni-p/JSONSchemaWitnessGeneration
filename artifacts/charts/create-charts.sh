#!/bin/bash
cd "${BASH_SOURCE%/*}/"

mkdir -p /home/repro/results/charts/

# Setup Linux Libertine Font
echo 'Setting up fonts ...'
(cd utils && Rscript load_font.r &> /dev/null)

echo 'Preparing data ...'
Rscript clean_data.r

echo 'Creating box plots ...'
Rscript boxplots.r

echo 'Creating hexplots ...'
Rscript hexplot-histogram_github.r
Rscript hexplot-histogram_kubernetes.r
Rscript hexplot-histogram_snowplow.r
Rscript hexplot-histogram_wp.r

cp charts/* ../results/charts/
printf "\nCharts are in ${HOME}/results/charts\n"
