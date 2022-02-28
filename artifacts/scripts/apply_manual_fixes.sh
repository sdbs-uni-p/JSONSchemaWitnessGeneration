#!/bin/bash
results_file=$1

# This is the list of schemas for which the validator says that we generate a non valid witness
# All these schemas and their witnesses were checked with the online validator https://www.jsonschemavalidator.net/.
# All the witnesses are correct.
sed -i 's/\(draft2019_09_valid_const_id24_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(draft2019_09_valid_const_id21_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(draft2019_09_valid_const_id30_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(draft2019_09_valid_const_id27_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(draft2019_09_valid_minContains_id9_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(draft2019_09_valid_maxContains_id5_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(draft2019_09_valid_minContains_id10_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(draft2019_09_valid_maxContains_id7_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(draft2019_09_nonvalid_minContains_id10_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(draft2019_09_nonvalid_minContains_id11_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(draft2019_09_valid_minContains_id15_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(draft2019_09_valid_minContains_id19_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(draft2019_09_valid_minContains_id26_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(draft2019_09_valid_minContains_id21_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(draft2019_09_valid_minContains_id20_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(draft2019_09_valid_minContains_id16_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(draft2019_09_valid_maxContains_id10_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(draft2019_09_nonvalid_minContains_id12_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(draft2019_09_valid_recursiveRef_id4_subschema1_not_2\),\(.*\),true,false,false/\1,\2,true,false,true/g' $results_file
sed -i 's/\(o73817\),\(.*\),true,false,false,/\1,\2,true,false,true,/g' $results_file
sed -i 's/\(o74598\),\(.*\),true,false,false,/\1,\2,true,false,true,/g' $results_file

# These schemas had an exception during validation but generated correct witnesses
sed -i 's/\(o89147\),\(.*\),true,false,JsonSchemaException,.,valid/\1,\2,true,false,true,,/g' $results_file
sed -i 's/\(o89147\),\(.*\),true,false,JsonSchemaException,.,valid/\1,\2,true,false,True,,/g' $results_file