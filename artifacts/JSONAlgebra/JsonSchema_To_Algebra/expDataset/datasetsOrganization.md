containment : We had at the beginning 2156 files for sat and 3365 for unsat. This folder contains the following

	- excluded : contains 2 subfolders (sat and unsat), they contain all the the excluded schemas
		- sat : (221 files) contains the following
			- ifElseThen : contains 4 files, all don't generate a witness for the same reason (contain if..then without else)
			and according to the semantics (https://json-schema.org/understanding-json-schema/reference/conditionals.html#if-then-else)
			If then or else is not defined, if behaves as if they have a value of true, so this schema is'nt supposed to generate a witness.
			- recDef : contains 2 files, that end up with the error "Recursive definition".
			- specialChar : contains 8 files, all don't generate a witness due to the presence of the char 💩.
			- unevaluatedItems : contains 33 schemas, all contain the keyword unevaluatedItems, some don't work, so I excluded all of them.
			- unevaluatedProperties : contains 65 schemas, all contain the keyword unevaluatedProperties (not supported).
			- unsupportedURI : contains 105 schemas. 101 of them have the error "ref not resolved" that occurs during the translation to the fullAlgebra
			and the other 4 the error "Unsupported URI" that occurs during the schema extraction.
			- Also contains 4 schemas inside it : all don't generate a witness (probably because of the ref).
			
		- unsat : (380 files) contains the following
			- content : contains 32 schemas, the schemas either contain the keyword contentMediaType or contentEncoding wich are not supported.
			- generatesValidWitness : contains 14 schemas that are supposed to be empty but generate valid witnesses (not clear schemas).
			- intMofFloat : contains 8 schemas, all excluded for the same reason. They are of the shape allOf:[x,not:x] ane some other variants.
			where x is : {type[int],mof(0.123456789)}, we end up with the root : {type[num],mof(LCM(1,0.123456789)),notMof(0.123456789), ...}
			and since LCM(1,0.123456789) is an approximation, and not an exact mof(1,0.123456789) we generate a witness.
			- recDef : contains 4 schemas, that end up with the error "Recursive definition".
			- specialChar : contains 8 files, all don't generate a witness due to the presence of the char 💩.
			- stackOverFlow : contains 1 schema with a stachOverFlow error.
			- unevaluatedItems : contains 76 schemas, all contain the keyword unevaluatedItems, some don't work, so I excluded all of them.
			- unevaluatedProperties : contains 128 schemas, all contain the keyword unevaluatedProperties (not supported).
			- unsupportedURI : contains 109 schemas. 101 of them have the error "ref not resolved" that occurs during the translation to the fullAlgebra
			and the other 8 the error "Unsupported URI" that occurs during the schema extraction.			
	
	- withDuplicates : 
		- sat : contains 1935 files, many of them contain the same schema.
		- unsat : contains 2985 files, many of them contain the same schema.
		
	- withoutDuplicates : 
		- sat : contains 591 files that contain a different schema from one another, this resulted after removing the duplicates 
		from the sat folder that is inside withDuplicates.
		- unsat : contains 1122 files that contain a different schema from one another, this results after removing the duplicates
		from the unsat folder that is inside withDuplicates.
		


------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


handwritten : contains subfolders in which we can find schemas that we designed manually to stress our tool or to test the competitor with tricky schemas.

	- The schemas we use in our experiments are in the subfolder testtricky. It contains 93 schemas and the folder excluded,
	in which we can find 1 schema that doesn't generate a witness.


------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


realWorldSchemas : contains 7046 (6387+40+619) schemas that are separated into different categories. It contains the following 6 subfolders :

	- all : contains 6387 of the real world schemas (github schemas) that we test (6387). This folder is dedicated to the competitor 
	(in our case the schemas are separated in many folders, because of the interpretation of oneOf as anyOf ...). 
	
	- processedFiles : contains in total 6149 schemas(5404+745), the 5404 schemas are tested without interpreting "oneOf" as "anyOf". 
	The other 745 schemas are located in the subfolder oneOf, for these 745 schemas the experiments are ran by interpreting "oneOf" as "anyOf". 
	All these schemas were originally those for which we didn't have timeouts and any other problem, but afterwards I decided to put aside those containing "oneOf" 
	and for which the interpretation of "oneOf" as "anyOf" still produces correct witnesses, this allows to have better execution times.	

	- unprocessedFiles : contains 238(195+22+1+19+1) schemas that are put into different subfolders.
	originally, this folder contains the schemas for which we had timeouts, memory problems, etc. Now it is separated into 4 subfolders
		- generationOk : contains 195 schemas for which the interpretation of oneOf as anyOf generated a witness (either correct or not correct).
		- outOfMemory2Witness : contains 22 schemas that induce an outOfMemory exception (mainly schemas with patterns with high bounds, big values of maxLength ...).
		- outOfMemoryMerge1 : contains 1 schema, which particulairy is having high values for maxLength (2 maxLength of 1000000000, etc.).
		- timeout : contains 19 schemas for which the generation didn't terminate (does not mean it never ends, but I didn't wait for it) 
		(tested the original schemas as they were and by interpreting oneOf as anyOf).
		- Also contains under it 1 schema for which we had the error "Schema with definition-reference not completed".
		
	- unsat : contains in total 40 schemas. It contains 41 unsatisfiable schemas.
		
	- results : contains the subfolder toCombine, in which I put the experiments results of our tool on the different folders 
	(processedFiles, processedFiles/oneOf, etc.), then I combine those results into one result that I put directly inside the folder results.
	
	- excluded : contains 619(122+355+105+10+13+4+1+9) schemas that are separated as follow :
		- 2FullPbms : contains 122 schemas that are organized as follow :
			- 61 schemas with a pattern problem : 52 Lookahead lookbehind + 7 syntax error + 1 illegal char range + 1 boundaries not yet supported.
			- 60 schemas with the error "ref not resolved" (e.g. #/definitions/common/properties/formatversion (file o4198)).
			- 1 schema with the error "detected 2 defs with the same name".
		- draft3 : contains 355 schemas of draft3.
		- extractPbms : contains 105 schemas (95% or more of them are because of type video and audio, the rest are illformed e.g. additionItems:123, path problems, etc.).
		- parsingPbms : contains 10 schemas (all illformed).
		- recursiveDef : contains 13 schemas that induce the RuntimeException recursive definition.
		- unreachableRed : contains 4 schemas with unreachable references.
		
		and a subfolder noWitnessBecauseOfRef, that contains 3 subfolders, each one of them contains schemas that are actually satisfiable but for which we don't generate 
		a witness (we return noWitness) because we don't support the references used in those schemas.
		- inexistantRef : contains 3 schemas with inexistant references.
		- withDefs : contains 2 schemas where the references used are the values of the IDs of properties defined inside the "definitions" block.
		- withoutDefs : contains 4 schemas. We have errors when putting these schemas in jsonschemavalidator.net (but valid against the metaSchema). 
		If we put the properties referenced in definitions (no error). The refs also points to the values of the IDs of properties.
		
		
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


subSchema : contains the schemas that are used for containment checking VS subschema of IBM. It contains the following subfolders : 

	- sat : contains satisfiable schemas organized in 3 subfolders as follow :
		- notDraft4 : contains 129 schemas that are not compatible with draft4 (e.g. schemas containing the keyword contains, etc.)
		- originalWithConst : contains 462 schemas that are compatible with draft4.
		* 462+129 = 591 schemas (schemas I copied from the folder containment/withoutDuplicates/sat).
		- constAsEnum : contains the 462 previous schemas where we replaced cont:v by enum:[v].
		I applied again a duplicates elimination on the 462 schemas, we end up with 450 schemas + the subfolder duplicates that contains 12 schemas
		(the duplicates resulted from the modification from const to enum and the presence of equivalent schemas with enum in the original schemas)
		
	- unsat : same explanations as the previous given for sat
		- notDraft4 : 221 schemas.
		- originalWithConst : 901 schemas.
		* 901+221 = 1122 schemas (schemas I copied from the folder containment/withoutDuplicates/unsat).
		- constAsEnum : contains 881 schemas and the subFolder duplicates with 20 schemas.
		
	- allConstAsEnum : contains the schemas in sat + schemas in unsat (I needed one that contains both because I ran the experiments at once, will be probably modified,
	or maybe not, we can easily get the sum of totalTime for sat and all the other measures).
	
	
	- schemasPairs : contains the files used to test subschema of IBM (the files are of the form : [{s1,s2,groundtruth},...].
	All the pairs correspond exactly to the schemas we have in allConstAsEnum minus some excluded due to some errors (43 in total, 10 sat and 33 unsat)
	


------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


validationTestSuite : contains 4 subfolders

	- excluded : contains 8 subfolders, each one them contains schemas excluded for a given reason (check the titles of the subfolders to understand why the schemas weer excluded).
	
	- results : contains the results of the experiments on the 849 original schemas we can find in the folder withDuplicates.
	
	- withDuplicates : contains 849 schemas, but many of them have the same content.
	
	- withoutDuplicates : contains all the schemas minus the duplicates (only 296 out of 849 remain)
