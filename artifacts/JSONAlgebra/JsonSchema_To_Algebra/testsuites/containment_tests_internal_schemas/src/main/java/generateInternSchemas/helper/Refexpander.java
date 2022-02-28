package generateInternSchemas.helper;

import java.io.*;

/**
 * This code is based on https://github.com/sdbs-uni-p/jsonschema-refexpander coded by Lukas Ellinger.
 *
 * With the help of this code it is possible to normalize JsonSchemas stored in a directory.
 * Through doing so, it is possible to generate a schema which contains a $ref mentioning another id, after
 * the internal schema has been created.
 *
 * For example the following Schema:
 * {
 *   "allOf": [
 *     {
 *       "const": 1
 *     },
 *     {
 *       "not": {
 *         "not": {
 *           "allOf": [
 *             {
 *               "$ref": "#foo"
 *             }
 *           ]
 *         },
 *         "definitions": {
 *           "A": {
 *             "type": "integer",
 *             "$id": "#foo"
 *           }
 *         }
 *       }
 *     }
 *   ]
 * }
 *
 * will be written to :
 *
 *{
 *   "allOf": [
 *     {
 *       "const": 1
 *     },
 *     {
 *       "not": {
 *         "not": {
 *           "allOf": [
 *             {
 *               "$ref": "#/definitions/id_foo"
 *             }
 *           ]
 *         },
 *         "definitions": {
 *           "A": {
 *             "type": "integer"
 *           }
 *         }
 *       }
 *     }
 *   ],
 *   "definitions": {
 *     "id_foo": {
 *       "type": "integer"
 *     }
 *   }
 * }
 *
 * Using this code the internal Schema now is valid according to draft6.
 *
 * For further information please read the corresponding README here:
 * https://github.com/sdbs-uni-p/jsonschema-refexpander
 *
 * @author Luca Escher
 */
public class Refexpander {

    /**
     * In Order to use the code you will need the JAR file refexpander.jar stored in
     * JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/src/resources.
     *
     * It will take a directory, create a new directory called '*_Normalized' which will be stored in
     * the directory JSONAlgebra. From here you can extract the nedded schemas after the normalization.
     *
     * @param command the terminal command which is to be run. Most probably this will be the following:
     *                "java -jar " + PathUtils.path_to_jar + " -normalize -normal -false -false " + PathUtils.refexpander_path;
     * @throws IOException
     * @throws InterruptedException
     */
    public void runRefexpanderCommand(String command) throws IOException, InterruptedException {
        String result = null;

        Process proc = Runtime.getRuntime().exec(command);

        // Read the output
        proc.waitFor();

        int exitValue = proc.exitValue();
        System.out.println(exitValue);

        //Something went wrong
        if (exitValue == 1) {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                result = "Something went wrong! \n" + line;
            }
        } else {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                result = line;
            }
        }
        System.out.println(result);
    }
}