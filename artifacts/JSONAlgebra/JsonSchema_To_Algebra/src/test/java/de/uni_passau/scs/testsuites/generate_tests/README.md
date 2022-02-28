# Containment Tests - Internal Schemas

## Source Data
The schemas inside the generated tests are extracted from the  
[json-schema-containment-testsuite](https://github.com/sdbs-uni-p/json-schema-containment-testsuite).

Inside this testsuite you can find the individual drafts under the folder [tests](https://github.com/sdbs-uni-p/json-schema-containment-testsuite/tree/main/tests).

## General Information
The goal of this code is to create a bunch of tests automatically in order
to evaluate the performance of the "Check schema containment" function of the tool.

Therefore schemas are extracted from the
[json-schema-containment-testsuite](https://github.com/sdbs-uni-p/json-schema-containment-testsuite).
Afterwards the schema comparison and a vaildation of them is performed on these.

In order to generate tests for the files from draft 6 you can run the GenerateTestsDraft6 script.
The Output path is fixed and the tests will be generated at JsonSchema_To_Algebra/src/test/java/de/uni_passau/scs/testsuites/draft6/TestsuiteDraft6.java.

In order to generate tests for the files from Jsonsubschema you can run the GenerateTestsJsonsubschema script.
The Output path is fixed and the tests will be generated at JsonSchema_To_Algebra/src/test/java/de/uni_passau/scs/testsuites/jsonsubschema/TestsuiteJsonSubSchema.java.

## Blacklist
Within the blacklist you can define schemas/files/tests that should **not** be generated.
- You can find the Blacklist here: [JsonSchema_To_Algebra/src/test/java/de/uni_passau/scs/testsuites/generate_tests/Blacklist.txt](https://github.com/miniHive/JSONAlgebra/blob/main/JsonSchema_To_Algebra/src/test/java/de/uni_passau/scs/testsuites/generate_tests/Blacklist.txt)


**First, please keep line 1 as a header for the file.**

To exclude an entire file from the generation, specify the path of the file
from the draft onwards. For example if you want to **not** generate the schemas from file
_/testsuites/draft6/valid/additionalProperties_, add the following line in the
text file Blacklist.txt:

```
draft6,valid_additionalProperties
````

As you can see it is necessary to seperate the folder structure by `_`.

To exclude only a single schema from a specific file, just write the dir path plus the id. For example if you want
to exclude the schema where the id is 1 from the following file: _testsuites/draft6/valid/additionalProperties_.
Then write the following to a new line in Blacklist.txt.

```
draft6,valid_additionalProperties,1
```

You also might want to exclude all schemas from an entire draft. If so, just write the
draft version to Blacklist.txt as in the following:

```
draft6
```

If you want to exclude a file from all drafts then type `*`.
For example if you want to exclude the file additionalProperies from all drafts

[
- _/testsuites/draft3/valid/additionalProperties_
- _/testsuites/draft4/valid/additionalProperties_
- _/testsuites/draft6/valid/additionalProperties_
- …

]

Then just add the following line:
```
*,valid_additionalProperties
```
Everything works similar to how one would use the blacklist with only one draft (see above).

In addition, you can also generally define a folder that is to be ignored.
In order to do so, you do not need to specify the path but write the name
of the folder as second parameter and `all` as third parameter to the file.

For example if you would like to exclude the folder 'format' from all drafts
you can write the following:
```
*,format,all
```

### Hints / Rules
- Make sure to **not** have any whitespaces in one line!
- If you want to exclude different folders or more than one specific test make sure
  to write them in different lines. Only one exclusion per line is allowed.
- Line zero will be ignored in Blacklist.txt. This lines' purpose is only for instructions

### Furhter use of the Blacklist
The same blacklist is also used to generate the internal schemas from the
[json-schema-containment-testsuite](https://github.com/sdbs-uni-p/json-schema-containment-testsuite).

For further information please read [here](https://github.com/miniHive/JSONAlgebra/blob/draft6_update/JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/src/main/java/README.md).