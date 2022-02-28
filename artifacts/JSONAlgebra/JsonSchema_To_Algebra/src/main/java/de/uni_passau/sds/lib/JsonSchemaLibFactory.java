package de.uni_passau.sds.lib;


/**
 * Simple factory for creating instance of the JSON Schema Library for convenient use by client applications.
 * Using abstract and concrete factory is a bit of an overkill here so here just this simple factory is used.
 * @author Thomas Pilz
 */
public class JsonSchemaLibFactory {

    /**
     * Factory method to create instance of the JSON Schema Library.
     * @param variant variant of the library to be created
     * @return instance of a JSON Schema Library
     */
    public static IJsonSchemaLib getJsonSchemaLib(JsonSchemaLibImplementations variant){
        IJsonSchemaLib instance;
        switch (variant){
            /*
            case "ANOTHER_IMPLEMENTATION":
                 instance = new AnotherImplementationXy();
                 break;
             */
            default:
                instance = new JsonSchemaToolLib();

        }
        return instance;
    }

    /**
     * Factory method to create instance of the JSON Schema Library.
     * @return instance of the default JSON Schema Library implementation.
     */
    public static IJsonSchemaLib getJsonSchemaLib(){
        return getJsonSchemaLib(JsonSchemaLibImplementations.JSON_SCHEMA_TOOL_LIB);
    }
}
