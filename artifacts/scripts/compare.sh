#!/bin/bash

original_results=${HOME}/results/original_results.csv
new_results=${HOME}/results/results.csv

[ ! -f ${new_results} ] && echo "$new_results does not exist. Please run evaluate.py first" && exit 1
[ ! -f ${original_results} ] && echo "Original results at ${original_results} are missing, aborting ..." && exit 1

clmns="1,2,3,4,5,6"
diff="colordiff"

while [[ $# -gt 0 ]]; do
  case $1 in
    -t|--compare-times)
      clmns="1,2,3,4,5,6,7,8,9"
      shift
      ;;
    -c|--no-colors)
      diff="diff"
      shift
      ;;
    -*|--*|*)
      printf "Unknown option $1\nPossible options are:
      \tNo Option\tCompare without runtimes and use coloured output 
      \t-c | --no-colors\tDisable colored output
      \t-t | --compare-times\tEnable comparison of runtimes\n"
      exit 1
      ;;
  esac
done

f1=$(cat ${original_results} | cut -d, -f$clmns | column -t -s,)
f2=$(cat ${new_results} | cut -d, -f$clmns | column -t -s,)

printf "Comparing new results against original results (cf. Table 1 in the Paper).
If differences occur, new results start with - and original results with +\n\n"
for ((i=1; i<=14; i++))
do
  res=$(eval "$diff" -U 4 <(head -$i <(echo "$f1") | tail -1) <(head -$i <(echo "$f2") | tail -1))
  if (($?==0))
  then
    printf " "
    head -$i  <(echo  "$f1") | tail -1
  else
    echo "$res" | tail -n +4
  fi
done
printf "\nNote: The value of 'errors sat' on dataset 'Containment' with tool 'DG' is 7.5%%. This
is reported as 7%% in the papers' table and rounded to 8%% in this reproduction package.
Likewise, median time for DG on Kubernetes is 0.0225s which is reported as 0.023s in
the paper and 0.22s in the reproduction package.\n"
