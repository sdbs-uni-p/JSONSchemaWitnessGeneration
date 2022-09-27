#!/bin/bash

f1=$(cat $HOME/results/original_results.csv | column -t -s,)
f2=$(cat $HOME/results/results.csv | column -t -s,)

printf "\nComparing new results against original results (cf. Table 1 in the Paper).
If differences occur, new results are colored \e[32mgreen\e[0m and original results \e[31mred\e[0m\n\n"
colordiff -U 15 <(echo "$f1") <(echo "$f2")| tail -n +4
printf "\nNote: The value of 'errors sat' on dataset 'Containment' with tool 'DG' is 7.5%%.
This is reported as 7%% in the papers' table and rounded to 8%% in this reproduction package.\n"