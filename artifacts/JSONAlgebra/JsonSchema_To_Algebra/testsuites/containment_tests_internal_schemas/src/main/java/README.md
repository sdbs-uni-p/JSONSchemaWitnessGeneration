# Containment Tests - Internal Schemas

## Source Data
The schemas inside the generated tests are extracted from the  
[json-schema-containment-testsuite](https://github.com/sdbs-uni-p/json-schema-containment-testsuite).

Inside this testsuite you can find the individual drafts under the folder [tests](https://github.com/sdbs-uni-p/json-schema-containment-testsuite/tree/main/tests).

## General Information
The goal of this code is to intercept the internal schemes of witness generation and store them in folders.

The internal schema generation is used to check whether the tool creates correct schemas for the schema comparison
function of the tool and to identify more precisely which types of schemas cause problems and which already work well.
This data is then further processed.

(GenerateInternSchemas.java is the main script. Here, the JSON data is taken from the Draft files and converted into a string)

### _Seperation in satisfiable and unsatifiable schemas_
Furthermore, the individual generated schemas of the [json-schema-containment-testsuite](https://github.com/sdbs-uni-p/json-schema-containment-testsuite)
are sorted according to whether they
are satifiable or unsatifiable.

In order to make this distinction, the structure of the internal scheme for the JSON Schema
Comparison should first be observed.

The basic construct of the intern generated schemas always is the following:

```
{
  "allOf": [
    {
      <schema1>
    },
    {
      "not": {
      <schema2>
    }
  ]
}
```
In summary, schemas are generated that state that it should be schema 1 but not schema 2.

Inside the individual schemas it already is specified whether schema1 is a subset of
schema 2, if it is the other way around or if they are equal.

##### - If schema 2 is a subset of schema 1 ➡︎ Unsatifiable

A schema in this case is unsatifiable because then the condition
that the schema should be schema 1 but not schema 2 is no longer feasible. Therefore a
witness can not be generated and the schema is unsatifiable.

##### - If schema 2 is **not** a subset of schema 1 ➡︎ satisfiable

In this case it is the other way round. Because schema 2 is not a subset of schema 1,
the condition is satisfiable and the entire schema is able to create a witness.

##### - If the schemas are equal ➡︎ unsatifiable

Since scheme 2 now corresponds exactly to scheme 1, the condition of the scheme cannot be fulfilled here either.
Therefore, in this case too, the schema is classified as unsatifiable.


### Automated generation of the internal schemas
Now that we know how to sort the schemas into satifiable and unsatifiable,
the generation and saving of the internal schemas can begin.

Here the schemes of the individual JSON Schemas are extracted from the [json-schema-containment-testsuite](https://github.com/sdbs-uni-p/json-schema-containment-testsuite).
These are then used to create the internal (alloOf schema1 but schema 2 not) structure.

Once these are created, they are written to the satisfiable and unsatifiable
folders according to where they belong. All generated schemas can now be found
there as JSON files and are ready for further processing.

Location of the JSON files:

- [JSONAlgebra/JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/satisfiable/](https://github.com/miniHive/JSONAlgebra/tree/draft6_update/JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/satisfiable)
- [JSONAlgebra/JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/unsatisfiable/](https://github.com/miniHive/JSONAlgebra/tree/draft6_update/JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/unsatisfiable)

## Blacklist
Within the blacklist you can define schemas/files/tests that should **not** be generated.
- You can find the Blacklist here: [Blacklist.txt](https://github.com/miniHive/JSONAlgebra/tree/main/JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/src/main/java/generateInternSchemas/Blacklist.txt)


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