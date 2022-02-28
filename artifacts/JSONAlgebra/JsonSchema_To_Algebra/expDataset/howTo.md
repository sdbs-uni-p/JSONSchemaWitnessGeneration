Containment dataset without duplicates used for the comparison between our tool and our competitor jsongenerator :
	
	
	The steps followed to get the sat schemas in expDataset/containment/withoutDuplicates/sat :

		1. I took the schemas in JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/satisfiable/* (number of schemas : 2156),
		then I've put them all in the folder expDataset/containment/withDuplicates.
		2. Filtering : I ran the experiments on these schemas in order to get rid of those that don't work.
		Those that don't work (221 schemas) are then moved to expDataset/containment/excluded/sat and separated into different 
		categories (check expDataset/datasetsOrganization.md).
		3. The ones that worked (1935 schemas) are put in expDataset/withDuplicates/sat. Afterwards, I ran the duplicates elimination on these schemas using
		the notebook expDataset/notebooks/resultsAnalysis.ipynb (look for Duplicates checking in the notebook).
		4. The duplicates checking is done using "string comparison" between the schemas contained in the files. It works as follow :
			- Select the folder containing the schemas we want to check.
			- We find the duplicates and then move them to a folder named duplicates inside the folder.
		5. Atfer finding the duplicates (1344 schemas), we had the unique schemas (591 schemas) in expDataset/withDuplicates/sat and the duplicates in 
		expDataset/withDuplicates/sat/duplicates, but then I decided to keep the original dataset minus the excluded ones as it is. So the original ones are kept in 
		expDataset/withDuplicates/sat, and the unique schemas we got from duplicates elimination are moved to expDataset/withoutDuplicates/sat.
	
	
	The steps followed to get the unsat schemas in expDataset/containment/withoutDuplicates/unsat : same steps as the previous ones (just replace sat by unsat). 
	The numbers are :

		1. Number of schemas in JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/unsatisfiable/* : 3365 schemas.
		2. Number of schemas in expDataset/containment/excluded/unsat : 380 schemas.
		3. Number of schemas in expDataset/containment/withDuplicates/unsat : 2985 schemas.
		4. Number of schemas in expDataset/containment/withoutDuplicates/unsat : 1122 schemas.

	
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


Containment dataset without duplicates and of draft4 used for the comparison between our tool and our competitor subschema of IBM :


	The steps followed to get the schemas in expDataset/subSchema/allConstAsEnum :
	
		1. Copied the schemas in expDataset/withoutDuplicates/sat (591 schemas inside it) and in expDataset/withoutDuplicates/unsat (1122) into expDataset/subSchema.
		2. Created in both folders a subfolder called notDraft4.
		3. Moved all schemas that are not of draft4 inside these subfolders (from subSchema/sat to subSchema/sat/notDraft4 (129 schemas), 
		same for unsat (221 schemas not of draft4). And this using a grep by selecting the schemas containing the following :
			- if|else|then.
			- exclusiveMin/exclusiveMax with a number as a value.
			- propertyNames.
			- recursiveDef.
			- contains|minContains|maxContains.
			- dependentSchemas|dependentRequired.
			- required with the list of required properties being empty (required:[]).
		4. So atfer the step 3, the schemas compatible with draft4 are in both expDataset/subSchema/sat (462) and expDataset/subSchema/unsat (901).
		Then I run the script that replaces const with enum on these schemas using "Change const to enum" in expDataset/notebooks/resultsAnalaysis.ipynb.
		It works as follow : 
			- It takes the files in expDataset/subSchema/sat, create new files that are the same excpet of const:v changed to enum:[v].
			- The new files created are put in expDataset/subSchema/sat/constAsEnum.
			* Same is done for the schemas in expDataset/subSchema/unsat.
		5. Afterwards I decided to put the original schemas that contain const in the subfolder expDataset/subSchema/sat/originalWithConst.
		Same thing for the unsat schemas.
		6. At this point both the sat and unsat folders contain 3 subfolders : constAsEnum, notDraft4 and originalWithConst.
		I did again a duplicates elimination on the schemas that are in sat/constAsEnum and unsat/constAsEnum.
		We had new duplicates because of the modification const:v to enum:[v] (some schemas already contain enum with the same structure ...).
		7. After the duplicates elimination, we had a subfolder in expDataset/subSchema/sat/constAsEnum (the folder contains 450 schemas) 
		called duplicates (the subfolder contains 12 schemas) that contains only the duplicates.
		Same for expDataset/subSchema/unsat/constAsEnum (881 unique and 20 duplicates). So inside expDataset/subSchema/sat/constAsEnum and expDataset/subSchema/unsat/constAsEnum
		we have schemas that are compatible with draft4 and where we changed const to enum and no schema appears in multiple files.
		8. I copied the schemas in expDataset/subSchema/unsat/constAsEnum and in expDataset/subSchema/sat/constAsEnum to
		expDataset/subSchema/allConstAsEnum (contains 1331 schemas).
		* I put both sat and unsat in one folder because this how I run the experiments for this dataset (we can easily find the measures separately
		for sat and unsat).
		
	The steps followed to get the schema pairs in expDatset/subSchema/schemaPairs : 
	
		1. Copied the subfolders draft4, draft6, draft7 and draft2019_09 in 
		JsonSchema_To_Algebra/testsuites/JSONSchemaContainmentTestSuite to expDataset/subSchema/schemaPairs.
		2. Then I remove the files or the pairs that are in the files that don't appear in expDataset/subSchema/allConstAsEnum.
		By doing the following manually (went for this solution, I didn't try to write a script to do this).
		For example if we want to check which the files or pairs of schemas we want to keep in expDatset/subSchema/schemaPairs/draft6
			- I do : ls -1 expDataset/subSchema/allConstAsEnum/draft6*, this will print all the schemas labeled draft6.
			Note that the names of the files correspond to the location of the pairs in expDatset/subSchema/schemaPairs.
			For example for the file draft6_valid_ref_id35_subschema1_not_2.json, the pair of schemas corresponding
			is in the folder draft6, in the subfolder valid, in the file ref.json and its id is 35.
			- Then check manually which not appear and remove it from the corresponding file.
			For example when we do ls -1 expDataset/subSchema/allConstAsEnum/draft6*, the file draft6_valid_ref_id1_subschema1_not_2.json
			doesn't appear, so I open the file expDataset/subSchema/schemaPairs/draft6/valid/ref.json and remove the pair with id 1.
		...
			
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
