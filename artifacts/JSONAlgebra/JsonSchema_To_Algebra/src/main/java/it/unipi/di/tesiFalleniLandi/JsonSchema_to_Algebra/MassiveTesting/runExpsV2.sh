#!/bin/bash

# To execute all of this : the project should be located in the home directory (git clone ...)

# if [ ! -d $HOME/JSONAlgebra ]
# then
#	git clone https://github.com/miniHive/JSONAlgebra.git
# fi


################### Store Git credentials (to do once) ###########################################################
# Needed to automate git pull
# To save git credentials (stored in plaintext in a file called .git-credentials in the home directory)
# 1. Execute : git config credential.helper store
# 2. Then : git pull or git clone ..., when the username and pass are entered, they will be saved in the file 

# https://git-scm.com/docs/git-credential-store
# https://git-scm.com/docs/git-credential-cache
# https://git-scm.com/docs/gitcredentials
##################################################################################################################




################### Schedule the execution of the tests (the current script) using cron (to do once) ##################
# 1. On a terminal, execute crontab -e 
# 2. Add the following : [Minute] [hour] [Day_of_the_Month] [Month_of_the_Year] [Day_of_the_Week] [command]
#    at the end of the file.
#    For example : 0 23 * * * ./home/.../runExps.sh 
#    means : run the script every day at 11pm

#######################################################################################################################

# Params :
#
# 1. Path to the folder containing the schemas (from the root)
# 2. Nb of procs to use in parallel
# 3. Boolean to specify if we want to use a timeout. If true, we'll have to add another param
# 4. Value of the timeout in milliseconds (the timeout is to processed all the schemas, it's not a timeout per schema)
#
# Example :
#			- ./.../runExps.sh /path_to_schemas 1 true 3600000
#			- ./.../runExps.sh /path_to_schemas 1 false
#



dataset=$1
nbProcs=$2
withTimeout=$3

date=$(date +"%d%m%Y_%Hh%Mm%Ss")


# Checking the params ...

echo Checking th params passed to the script

if [ $withTimeout = true ]
then
	if [ $# -ne 4 ]
	then 
		printf "Params problem (path_to_schema nbProcs booleanTimeout(true) timeout)"
		exit
	else
		timeout=$4
	fi
fi

echo

# Ask for sending the results by email

echo Do you want to send the results of the execution by email "(y/n)" ?
read -r sendByEmail
if [ $sendByEmail == "y" ]
then
	echo Type the email address :
	read -r emailAddress
fi



echo


# Executing "git pull" only if the current git revision is different from the one appearing in the last results.csv

echo Comparing git revision of the latest results with the current revision

cd ${HOME}/JSONAlgebra

currentGitRevision=$(cat $HOME/JSONAlgebra/.git/refs/heads/${branch-main})

if [ "$(ls -A $dataset/results)" ]
then
		lastResultsFile=$(find $dataset/results -name "*results.csv" | sort | tail -n 1)
		if [ ! -z "${lastResultsFile}" ] 
		then
			lastExecGitRevisionOnDataset=$(cat $lastResultsFile | cut -d ',' -f27 | tail -n +2 | head -n +1)
		fi
			
fi

echo Current revision of the main branch : $currentGitRevision

if [ ! -z "${lastExecGitRevisionOnDataset}" ]
then
	echo Git revision appearing in results.csv : $lastExecGitRevisionOnDataset
	echo
	if [ ! $currentGitRevision == $lastExecGitRevisionOnDataset ]
	then
		echo The two revisions are not the same
		echo Running git pull
		git pull
		
	else
		echo The two revisions are the same "(No need to pull the last revision)"
		
# Ask whether to run the experiments in the case we already have results on this dataset with the current git revision

		echo Do you still want to proceed with the exeperiments "(y/n)" ?
		read -r runExps
		if [ $runExps == "n" ]
		then
			exit
		fi
		
	fi
else
	echo Didnt find any results.csv file
	echo Running git pull
	git pull
fi

echo 

# Moving the files in results to oldResults
# results will always contain the latest ones

echo Moving the old results from the folder results to the folder oldResults

if [ -d $dataset/results ]
then
	if [ ! -d $dataset/oldResults ]
	then
		mkdir $dataset/oldResults
	fi
	
	if [ "$(ls -A $dataset/results)" ]
	then
		mv $dataset/results/* $dataset/oldResults
	fi	
fi

echo

# Executing the tests (the project should be located in the home directory)

echo Start running the experiments

mvn exec:java -Dexec.mainClass="it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.MassiveTesting.MainClassV2" \
	-Dexec.args="${dataset} $nbProcs $withTimeout $timeout" -pl JsonSchema_To_Algebra



# Putting the results in a zip file

echo

echo zip the csv files

cd $dataset/results
zip results_${date}.zip *



# Sending an email with the results, only if an email address was given

echo

echo Sending the results by email if selected

if [ ! -z "${emailAddress}" ]
then
	printf "Hello,\nThe results of the expriments on the dataset ${dataset} are attached.\nDate and time of the execution : ${date}" | mail -s \
		"New Exps results on ${dataset} are available" $emailAddress -A results_${date}.zip

fi


echo
echo deleting the zip file
rm results*.zip


# Push the results directly to github

#cd ..
#git add .
#git commit -m "added new results"
#git push -u origin main



	 
