# JSON Schema Ref Expander
This tool can normalize given JSON schemas and check them for recursion. It is based on [Draft04](https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00).

## Origin of this code
This code has originally been written by [Lukas Ellinger](https://github.com/lukasellinger) . In order to adapt 
this code with our project some changes have been made. If you want to visit the original repository
please visit [jsonschema-refexpander](https://github.com/sdbs-uni-p/jsonschema-refexpander).

## Table of Contents
- [JSON Schema Ref Expander](#json-schema-ref-expander)
  - [Table of Contents](#table-of-contents)
  - [Getting started](#getting-started)
    - [How to use the tool](#how-to-use-the-tool)  
    - [Dockerfile](#dockerfile) 
  - [Normalization process](#normalization-process)
    - [Examples](#examples)
  - [Recursion checking](#recursion-checking)
  - [Applicability on other drafts](#applicability-on-other-drafts)
  - [License](#license)   
## Getting started
### How to use the tool
- Normalization:<br/>
To normalize your schemas some additional parameters are needed:</br>
  - For `-repositorytype`, you can choose between `-normal`, `-testsuite`, `-corpus` depending on where your schemas are from.</br>
  - For `-allowDistributedSchemas` you have to choose between `-true` and `-false` depending on whether you want to allow distributed schemas.</br>
  - For `-fetchSchemasOnline` you have to choose between `-true` and `-false` depending on whether you want to download references from the internet. If `-false`     is chosen, the referenced URI will only be looked up in the file `UriOfFiles.csv`. In `UriOfFiles.csv` each line should look like `file, URI`. This means that  the schema in `file` will be referenced if actually `URI` is referenced. `file` should be stored in a directory called `Store`. If the referenced URI is not in `UriOfFiles.csv` a `StoreException` is thrown. But if `-true` is chosen, afterward it will be tried to load the schema from `URI`.</br>
  - Finally, `"pathToDir"` should be the path to the directory in which the schemas are stored. Path should be in quotation marks<br/>  
`java -jar jarfile -normalize -repositorytype -allowDistributedSchemas -fetchSchemasOnline "pathToDir"`<br/><br/> 
If `corpus` was chosen for the `repositorytype` an additional parameter with the path to the file repos_fullpath.csv (`pathToReposFullpath`) is needed.<br/>  
`java -jar jarfile -normalize -corpus -allowDistributedSchemas -fetchSchemasOnline "pathToDir" "pathToReposFullpath"`<br/><br/>    
- Recursion checking:<br/>
See [here](#recursion-checking) for an explanation.<br/>
Again `pathToDir` should be the path to the directory in which the schemas are stored.<br/>
`java -jar jarfile -recursion "pathToDir"`<br/><br/>  
- Statistics:<br/>
Statistics about the distribution of single-file and distributed schemas and the frequency of recursion in them are made. Additionally, the change of the lines of code from the unnormalized to the normalized schemas is gathered. An overall overview is created, too.<br/>
Again `pathToDir` should be the path to the directory in which the schemas are stored. `pathToNormalizedDir` should be the path to the directory in which the normalized schemas are stored.<br/>
`java -jar jarfile -stats "pathToDir" "pathToNormalizedDir"`<br/><br/>  
### Dockerfile
A dockerfile can be found [here](/Dockerfile). In this, the schemas of the [TestSuite](https://github.com/json-schema-org/JSON-Schema-Test-Suite/tree/0c223de21a1ca08c7a46ee08feae889d58f98de8/tests/draft4) (commit 0c223de), the [SchemaStore](https://github.com/SchemaStore/schemastore/tree/2ad0b3dc9b8cd9b8c814d13e06c265cc540b6064/src/schemas/json) (commit 2ad0b3d) and the [SchemaCorpus](https://github.com/sdbs-uni-p/json-schema-corpus/tree/9c0e7963559c6c632694d5851c081662178ba70b) (commit 9c0e796) will be normalized and afterward the statistics are fetched. To keep this process reproducible all external references have already been downloaded. These downloaded references will be used.

## Normalization process
In a normalized schema, all references should follow the [JSON Pointer Syntax](https://datatracker.ietf.org/doc/html/rfc6901#section-3) 
and all of them should point to direct children of the definitions-section or to the top-level schema. Therefore distributed schemas are consolidated in one file. 
### Examples

```JSON
{
  "properties": {
    "name": {"type": "string"},
    "surname": {"$ref": "#/properties/name"},
    "children": {
          "type": "array",
          "items": {"$ref": "#"}
    }
  }
}
```
All references are in JSON Pointer Syntax, but "#/properties/Vorname" is not pointing to a direct child of definitions-section. Therefore this reference is resolved and copied to the definitions-section. The normalized version of the above schema:

```JSON
{
  "properties": {
    "name": {"type": "string"},
    "surname": {"$ref": "#/definitions/properties_name"},
    "children": {
          "type": "array",
          "items": {"$ref": "#"}
        }
  },
  "definitions": {
    "properties_name": {"type": "string"}
  }
}
``` 
<br><br>
Schemas can be distributed in separate files, too. See following:
```JSON
{
  "type": "object",  
  "properties": {
    "name": {"type": "string"},
    "surname": {"type": "string"},
    "address": {"$ref": "folder/locations.json#/defintions/address"}
  }
}
```
File: schema.json
```JSON
{
  "definitions": {
    "address": {
      "street": {"type": "string"},
      "number": {"type": "integer"},
      "city": {"type": "string"},
      "country": {"type": "string"}
    }
  }
}
```
File: folder/locations.json
<br><br>
The schema in schema.json has a reference to a child in folder/locations.json. Therefore the content of the reference is copied to the definitions-section. The normalized version of the schema in schema.json:
```JSON
{
  "type": "object",  
  "properties": {
    "name": {"type": "string"},
    "surname": {"type": "string"},
    "address": {"$ref": "#/defintions/folder_locations.json_defintions_address"}
  },
  "definitions": {
    "folder_locations.json_defintions_address": {
      "street": {"type": "string"},
      "number": {"type": "integer"},
      "city": {"type": "string"},
      "country": {"type": "string"}
    }
  }
}
```

## Recursion checking
A distinction is made between guarded and unguarded recursiveness. The difference is that the behavior of unguarded-recursive schemas during validation is not defined and therefore possibly leads to no validation in finite time. The tool only guarantees the correct output for normalized schemas.

## Applicability on other drafts
This tool is based on Draft04 and therefore uses its specific keywords. There are two major problems when normalizing schemas using a higher draft.

One is that "id" was replaced with "$id". To keep things working, before normalization the tool scans for the keyword "$id" in the schema. If one is found, "$id" will be used for base-URI resolution.

Another is that in Draft06 and above unknown keywords should be explicitly ignored, which is not the case in Draft04. Therefore it can be referred to ids under unknown keywords. This leads to no problem unless an id is used more than once, which should never be the case.

Keep in mind that schemas using higher drafts are still normalized using Draft04 specific keywords.

## License
This work is licensed under the [Apache 2.0 License](./LICENSE.txt).
