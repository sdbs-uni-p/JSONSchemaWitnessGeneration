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
# 1. Path to the folder containing the schemas
# 2. Nb of procs to use in parallel
# 3. Boolean to specify if we want to use a timeout. If true, we'll have to add another param
# 4. Value of the timeout in milliseconds (the timeout is to processed all the schemas, it's not a timeout per schema)
# 5. The email address where to send the results
#
# Example :
#			- ./.../runExps.sh /path_to_schemas 1 true 3600000 lyes.attouche@dauphine.psl.eu
#			- ./.../runExps.sh /path_to_schemas 1 false lyes.attouche@dauphine.psl.eu


dataset=$1
nbProcs=$2
withTimeout=$3

date=$(date +"%d%m%Y_%Hh%Mm%Ss")



# Moving the files in results to oldResults
# results will always contain the latest ones

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


# Checking the params ...


if [ $withTimeout = true ]
then
	if [ $# -ne 5 ]
	then 
		printf "Params problem (path_to_schema nbProcs booleanTimeout(true) timeout emailAddress)"
		exit
	else
		timeout=$4
		emailAddress=$5
	fi
else
	if [ $# -ne 4 ]
	then
		printf "Params problem (path_to_schema nbProcs booleanTimeout(false) emailAddress)"
		exit
	else
		emailAddress=$4
	fi
		
fi


# Executing the tests (the project should be located in the home directory)

cd ${HOME}/JSONAlgebra

git pull


mvn exec:java -Dexec.mainClass="it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.MassiveTesting.MainClassV2" \
	-Dexec.args="${dataset} $nbProcs $withTimeout $timeout" -pl JsonSchema_To_Algebra



# Putting the results in a zip file

cd $dataset/results
zip results_${date}.zip *



# Sending an email with the results

printf "Hello,\nThe results of the expriments on the dataset ${dataset} are attached.\nDate and time of the execution : ${date}" | mail -s \
	"New Exps results on ${dataset} are available" $emailAddress -A results_${date}.zip

rm results*.zip


# Push the results directly to github

#cd ..
#git add .
#git commit -m "added new results"
#git push -u origin main



	 
