#!/bin/bash

f1=$(cat $HOME/results/original_results_with_times.csv | column -t -s,)
f2=$(cat $HOME/results/results_with_times.csv | column -t -s,)

printf "\nComparing new results against original results (cf. Table 1 in the Paper).
If differences occur, new results are colored \e[32mgreen\e[0m and original results \e[31mred\e[0m\n\n"
for ((i=1; i<=14; i++))
do
  res=$(colordiff -U 4 <(head -$i <(echo "$f1") | tail -1) <(head -$i <(echo "$f2") | tail -1))
  if (($?==0))
  then
    printf " "
    head -$i  <(echo  "$f1") | tail -1
  else
    echo "$res" | tail -n +4
  fi
done
printf "\nNote: The value of 'errors sat' on dataset 'Containment' with tool 'DG' is 7.5%%.
This is reported as 7%% in the papers' table and rounded to 8%% in this reproduction package.\n"
