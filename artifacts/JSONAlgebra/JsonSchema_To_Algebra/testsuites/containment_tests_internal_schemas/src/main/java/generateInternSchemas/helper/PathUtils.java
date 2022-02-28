package generateInternSchemas.helper;

/**
 * This is a helper class.
 * In this class all paths (needed for the generation of the internals schemas) are stored and ready for use.
 *
 * @author Luca Escher
 */
public class PathUtils {

    public static String malformed_path = System.getProperty("user.dir") + "/JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/src/main/java/generateInternSchemas/malformedJson.txt";
    public static String normalized_path = System.getProperty("user.dir") + "/Normalized_files/";

    public static String satisfiable_path = System.getProperty("user.dir") +"/JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/satisfiable/";
    public static String unsatisfiable_path = System.getProperty("user.dir") + "/JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/unsatisfiable/";

    public static final String blackList = System.getProperty("user.dir") + "/JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/src/main/java/generateInternSchemas/Blacklist.txt";
    public static String draftPath = System.getProperty("user.dir") + "/JsonSchema_To_Algebra/testsuites/JSONSchemaContainmentTestSuite/";

    public static final String DRAFT3 = "draft3";
    public static final String DRAFT4 = "draft4";
    public static final String DRAFT6 = "draft6";
    public static final String DRAFT7 = "draft7";
    public static final String DRAFT2019_09 = "draft2019_09";
    public static final String DRAFT2020_12 = "draft2020_12";

    public static String getDraftVersionPath(String draft){
        String version = "default";

        switch (draft){
            case "draft3":
                version = "draft3/";
                break;
            case "draft4":
                version = "draft4/";
                break;
            case "draft6":
                version = "draft6/";
                break;
            case "draft7":
                version = "draft7/";
                break;
            case "draft2019":
                version = "draft2019_09/";
                break;
            case "draft2020":
                version = "draft2020_12/";
                break;

            default: return "Fail\t" + draft;
        }

        return version;
    }
}
