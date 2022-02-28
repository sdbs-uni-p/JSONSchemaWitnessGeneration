package validator;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.simple.JSONObject;

/**
 * @author Luca Escher
 */
public class Validator {

    /**
     * This method is validating a JSONObject / a JsonSchema according to draft6/draft7.
     * It is able to detect reference problems when $ref is used, malformed json files and more.
     *
     * If you want to validate according to draft7 you can change draftV6Support() to draftV7Support().
     *
     * @param jObject
     * @return
     */
    public boolean validateJson(JSONObject jObject) {
        try {

            SchemaLoader loader = SchemaLoader.builder()
                    .schemaJson(jObject)
                    .draftV6Support() // or draftV7Support()
                    .build();

            //the following line is the validation. Only if this succeeds the Schema is valid.The variable
            //has not to be used furthermore.
            Schema schema = loader.load().build();

        } catch (Exception e) {
            System.out.println("Schema was invalid:\n");
            System.out.println("Cause: " + e.getCause());
            System.out.println("Message: " + e.getMessage());

            //if there is no cause for failure then the schema also is valid after draft 6.
            if (e.getCause() == null){
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
}