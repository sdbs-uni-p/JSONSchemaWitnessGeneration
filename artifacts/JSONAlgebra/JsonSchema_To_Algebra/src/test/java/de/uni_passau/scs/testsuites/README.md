# Testsuites

## Information
These testsuites are generated from the [JSON-Schema-containment-testsuite](https://github.com/sdbs-uni-p/json-schema-containment-testsuite).

Here you can find the folder from which the tests are generated  for draft 6-->
[draft6](https://github.com/sdbs-uni-p/json-schema-containment-testsuite/tree/main/tests/draft6)

## Process
These testsuites test whether our code delivers the right result/output or not. To do this, the JSON Schemas are taken from the 
individual files and then evaluated by our tool. These results are then compared with the correct results from the files.
If the output is not equal to the results from the files the test will fail.

The tests were named according to their location within the folder and their file name. For better identification, the 
ID was also extracted from the scheme and added to the name. Since a file usually contains several schemes, the exact 
scheme can be identified by the ID.

The tests were generated with the class/script [GenerateTests.java](https://github.com/miniHive/JSONAlgebra/blob/draft6_update/JsonSchema_To_Algebra/src/test/java/de/uni_passau/scs/testsuites/generate_tests/GenerateTestsDraft6.java)

#### _Blacklist_

Furthermore, a blacklist is used to adapt the tests to the current status of the tool. 
Features that are not yet integrated in our tool will be marked with an @Ignore tag and thus excluded 
from the results for the time being.

More information about the blacklist can be found here:
- [Containment Tests - Internal Schemas](https://github.com/miniHive/JSONAlgebra/blob/main/JsonSchema_To_Algebra/src/test/java/de/uni_passau/scs/testsuites/generate_tests/GenerateTestsDraft6.java)

## Reason
These tests are intended to give an overview of the quality of the code and to check the quality in due to time. 
This allows us to track whether our code is improving or not and helps to identify problems.

## Overview
If you want to view the test results from a run, you can find them at the following locations.

- Result for Draft6 --> [draft6Status](https://github.com/miniHive/JSONAlgebra/blob/main/JsonSchema_To_Algebra/src/test/java/de/uni_passau/scs/testsuites/draft6/draft6Status.txt)
- Result for Jsonsubschema tests --> [JsonSubSchemaStatus](https://github.com/miniHive/JSONAlgebra/blob/main/JsonSchema_To_Algebra/src/test/java/de/uni_passau/scs/testsuites/jsonsubschema/JsonSubSchemaStatus.txt)


