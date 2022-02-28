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
Rscript hexplot-histogram.r


