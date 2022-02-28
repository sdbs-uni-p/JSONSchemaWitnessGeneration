package generateInternSchemas.helper;

import java.util.List;

/**
 * @author Luca Escher
 */
public class GetInternSchemas {

    public static final String emptyWitness = "{\"Problem\":\"Empty witness\"}";

    /**
     * After generating a list with the two intern generated schemas
     * you can receive schemaOne as String with this method.
     *
     * @param list
     * @return
     */
    public String getSchemaOne(List<String> list){
        if (list == null){
            return "no schemas were generated.";
        }
        int size = list.size();
        if (size >= 1) {
            return list.get(0);
        } else {
            return "Something went wrong.";
        }
    }

    /**
     * After generating a list with the two intern generated schemas
     * you can receive schemaTwo as String with this method.
     *
     * @param list
     * @return
     */
    public String getSchemaTwo(List<String> list){
        if (list == null){
            return "no schemas were generated.";
        }
        int size = list.size();
        if (size == 2) {
            return list.get(1);
        } else {
            return "Something went wrong.";
        }
    }
}
