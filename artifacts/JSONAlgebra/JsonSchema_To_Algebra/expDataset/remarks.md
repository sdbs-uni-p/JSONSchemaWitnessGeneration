Containment/sat : This is the list of schemas for which the validator says that we generate a non valid witness

	- draft2019_09_valid_const_id24_subschema1_not_2 : we generated [1] instead of [1.0]
	- draft2019_09_valid_const_id21_subschema1_not_2 : [0] instead of [0.0]
	- draft2019_09_valid_const_id30_subschema1_not_2 : {"a":1} instead of {"a":1.0}
	- draft2019_09_valid_const_id27_subschema1_not_2 : {"a":0} instead of {"a":0.0}
	- draft2019_09_valid_minContains_id9_subschema1_not_2 : minContains not recognized by the validator
	- draft2019_09_valid_maxContains_id5_subschema1_not_2 : maxContains not recognized by the validator
	- draft2019_09_valid_minContains_id10_subschema1_not_2 : minContains not recognized by the validator
	- draft2019_09_valid_maxContains_id7_subschema1_not_2 : maxContains not recognized by the validator
	- draft2019_09_nonvalid_minContains_id10_subschema1_not_2 : minContains not recognized by the validator
	- draft2019_09_nonvalid_minContains_id11_subschema1_not_2 : minContains not recognized by the validator
	- draft2019_09_valid_minContains_id15_subschema1_not_2 : minContains and maxContains not recognized by the validator
	- draft2019_09_valid_minContains_id19_subschema1_not_2 : minContains and maxContains not recognized by the validator
	- draft2019_09_valid_minContains_id26_subschema1_not_2 : minContains and maxContains not recognized by the validator
	- draft2019_09_valid_minContains_id21_subschema1_not_2 : minContains and maxContains not recognized by the validator
	- draft2019_09_valid_minContains_id20_subschema1_not_2 : minContains and maxContains not recognized by the validator
	- draft2019_09_valid_minContains_id16_subschema1_not_2 : minContains and maxContains not recognized by the validator
	- draft2019_09_valid_maxContains_id10_subschema1_not_2 : minContains and maxContains not recognized by the validator
	- draft2019_09_nonvalid_minContains_id12_subschema1_not_2 : minContains and maxContains not recognized by the validator
	- draft2019_09_valid_recursiveRef_id4_subschema1_not_2 : recursiveRef not recognized by the validator
	
	All these schemas and their witnesses were checked with the online validator https://www.jsonschemavalidator.net/
	
	- All the witnesses are correct.
	==> For these schemas, I had manually modified false to true in the column valid of the file results.csv
	    And, I also deleted the rows in the file validation.csv
	    
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Same remark for the 4 first schemas in list before when comparing our Results with subschema of IBM



----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Containment/sat : schemas for which jsongenerator generates a correct witness but considered False by the validator

	- draft2019_09_valid_const_id24_subschema1_not_2
	- draft2019_09_valid_const_id21_subschema1_not_2
	- draft2019_09_valid_const_id30_subschema1_not_2
	- draft2019_09_nonvalid_type_id2_subschema1_not_2
	- draft2019_09_valid_const_id27_subschema1_not_2
	- draft2019_09_valid_minContains_id9_subschema1_not_2
	- draft2019_09_valid_maxContains_id5_subschema1_not_2
	- draft2019_09_valid_minContains_id10_subschema1_not_2
	- draft2019_09_valid_dependentRequired_id4_subschema1_not_2
	- draft2019_09_valid_maxContains_id7_subschema1_not_2
	- draft2019_09_nonvalid_minContains_id10_subschema1_not_2
	- draft2019_09_nonvalid_minContains_id11_subschema1_not_2
	- draft2019_09_valid_dependentRequired_id16_subschema1_not_2
	- draft2019_09_valid_dependentSchemas_id7_subschema1_not_2
	- draft2019_09_valid_minContains_id15_subschema1
	- draft2019_09_valid_minContains_id19_subschema1_not_2
	- draft2019_09_valid_minContains_id26_subschema1_not_2
	- draft2019_09_valid_minContains_id21_subschema1_not_2
	- draft2019_09_valid_minContains_id20_subschema1_not_2
	- draft2019_09_valid_minContains_id16_subschema1_not_2
	- draft2019_09_valid_maxContains_id10_subschema1_not_2
	- draft2019_09_nonvalid_minContains_id12_subschema1_not_2
	- draft2019_09_valid_dependentRequired_id14_subschema1_not_2
	- draft2019_09_valid_dependentSchemas_id8_subschema1_not_2
	- draft2019_09_valid_recursiveRef_id4_subschema1_not_2
	- draft2019_09_valid_dependentSchemas_id3_subschema1_not_2
	- draft2019_09_valid_dependentSchemas_id4_subschema1_not_2
	- draft2019_09_valid_dependentSchemas_id5_subschema1_not_2
	
	
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Containment/unsat : schemas for which jsongenerator generates a witness and the valiator consider it as True

	- draft2019_09_valid_type_id2_subschema1_not_2
	- draft2019_09_valid_minContains_id22_subschema1_not_2
	- draft2019_09_valid_minContains_id23_subschema1_not_2
	- draft2019_09_valid_minContains_id24_subschema1_not_2
	- draft4_unions_minimum_id2_subschema1_not_2


----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

realWorldSchemas : 
	- our tool :  2 schemas had an exception during validation, both were correct witnesses (modified manually on the csv files)
	- jsongenerator : same 2 JsonSchemaException (checked : 1 valid witness and the other no) and 6 ExceutionException (checked : 4 valid and 2 not)
	
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Containment draft4

	subschema IBM : 
		- containment/sat : 10 schemas removed because they cause problems (cannot catch the exception), these 10 will be counted as errors.
		- containment/unsat : 33 removed because they cause problems (cannot catch the exception), these 33 will be counted as errors.
		
	jsongenerator : 
		The following sat schemas are considered false by the validator but are true
		- draft2019_09_nonvalid_type_id2_subschema1_not_2
		- draft2019_09_valid_const_id24_subschema1_not_2
		- draft2019_09_valid_const_id21_subschema1_not_2
		- draft2019_09_valid_const_id30_subschema1_not_2
		- draft2019_09_valid_const_id27_subschema1_not_2
		
		The following unsat schema is considered true but is false:
		- draft2019_09_valid_type_id2_subschema1_not_2
		
	
	

	
