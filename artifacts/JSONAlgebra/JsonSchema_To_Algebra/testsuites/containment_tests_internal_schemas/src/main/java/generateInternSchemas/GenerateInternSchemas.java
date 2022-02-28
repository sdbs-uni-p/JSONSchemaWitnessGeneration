package generateInternSchemas;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import de.uni_passau.sds.lib.IJsonSchemaLib;
import de.uni_passau.sds.lib.JsonSchemaLibFactory;
import generateInternSchemas.helper.BlackListUtils;
import generateInternSchemas.helper.CreateWriteFile;
import generateInternSchemas.helper.PathUtils;
import generateInternSchemas.helper.Refexpander;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import validator.Validator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author Luca Escher
 */
public class GenerateInternSchemas {

    private final static Logger logger = LoggerFactory.getLogger(GenerateInternSchemas.class);

    private final static long TIMEOUT = 2;
    public static String satFiles = System.getProperty("user.dir") + "/JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/satisfiable/";

    private static final CreateWriteFile hanldeFiles = new CreateWriteFile();
    private static final Validator validator = new Validator();

    private static final IJsonSchemaLib lib = JsonSchemaLibFactory.getJsonSchemaLib();


    /**
     * This method puts the intern generated schemaOne and schemaTwo into a message. Then creates a file
     * with this message. Depending on the case if the JsonSchema is satisfiable or not, it will be
     * stored in either the dir JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/satisfiable
     * or in the dir JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/unsatisfiable.
     *
     * Note:
     * 1. Schemas defined in the Blacklist are ignored.
     * 2. Path references are adjusted.
     * 3. If a Schema contains an external Path referenciation (e.g. https//…) it will always be ignored.
     * 4. The schemas are validated according to draft6.
     *
     * @param filePaths Paths from where to get the data
     * @param blackList contains dirs which are to be excluded
     * @throws FileNotFoundException
     */
    public static void jsonData(List<String> filePaths, String[] blackList) throws IOException, ProcessingException, ParseException {
        int run = 0;

        for (String path : filePaths) {

            //create a testName which is traceable.
            String schema = CreateWriteFile.fileToSchema(path);
            String rawName = path.substring(101, path.length() - 5);
            String rawName2 = rawName.replace("-", "_");
            String testName = rawName2.replace("\\", "_").replace("/", "_");

            String draftVersionPath = PathUtils.getDraftVersionPath(testName.split("_")[0]);

            //check if the test is defined in the blacklist. If so, it is to be ignored.
            if (BlackListUtils.isBlackedTest(blackList, testName, null)) {
                //do nothing
            } else {

                //Get JSON File as Array
                Object obj = null;
                try {
                    obj = new JSONParser().parse(schema);
                } catch (Exception e) {
                    System.out.println("malformed JSON");
                }

                if (obj == null) {
                    String message = "Original Schema was malformed!," + testName + "," + schema;
                    CreateWriteFile.writeResultToFile(PathUtils.malformed_path, message);
                } else {

                    JSONArray jArray = (JSONArray) obj;

                    for (int i = 0; i < jArray.size(); i++) {
                        run += 1;

                        JSONObject j = (JSONObject) jArray.get(i);

                        String id = j.get("id").toString();
                        String schema1 = j.get("schema1").toString();
                        String schema2 = j.get("schema2").toString();

                        schema1 = toString_withoutEscape(schema1);
                        schema2 = toString_withoutEscape(schema2);

                        if (BlackListUtils.isBlackedTest(blackList, testName, id)) {
                            //do nothing
                        } else {

                            //check if unvalid special character is appearing in schema
                            boolean specialChar = false;

                            String pattern = "\"";
                            var splitter = schema1.split(pattern);

                            for (var keyWord : splitter) {
                                if (keyWord.matches(".*\\\\.*")) {
                                    specialChar = true;
                                }
                            }

                            splitter = schema2.split(pattern);

                            for (var keyWord : splitter) {
                                if (keyWord.matches(".*\\\\.*")) {
                                    specialChar = true;
                                }
                            }

                            if (specialChar) {
                                String message = "Schema did contain special char," + testName + "," + id + "," + schema1 + "," + schema2;
                                CreateWriteFile.writeResultToFile(PathUtils.malformed_path, message);
                            } else {

                                //rewrite $ref paths according to the added allOf #{…, not…}
                                //by doing so reference path problems are solved after the internal schemas has been generated.
                                String rewrittenSchema = null;

                                try {
                                    rewrittenSchema = lib.generateSchemaDifference(schema1, schema2);
                                    rewrittenSchema = toString_withoutEscape(rewrittenSchema);
                                } catch (Exception e) {
                                    String message = "Path rewriting did not work," + testName + "_id" + id;
                                    CreateWriteFile.writeResultToFile(PathUtils.malformed_path, message);
                                }

                                if (rewrittenSchema == null) {
                                    //do nothing
                                } else {
                                    //validate Schema
                                    Object rewrittenObj = null;
                                    try {
                                        rewrittenObj = new JSONParser().parse(rewrittenSchema);
                                    } catch (Exception e) {
                                        System.out.println("malformed JSON");
                                    }

                                    //if the rewrittenObj is null something went wrong.
                                    //For further details then see
                                    //JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/src/java/generateInternSchemas/malformedJson.txt
                                    if (rewrittenObj == null) {
                                        String message = "Ref rewritten Schema was malformed," + testName + "_id" + id + "," + rewrittenObj;
                                        CreateWriteFile.writeResultToFile(PathUtils.malformed_path, message);
                                    } else {

                                        //validate if the schema is valid according to draft6
                                        boolean valid = false;

                                        try {
                                            valid = validator.validateJson((JSONObject) rewrittenObj);
                                        } catch (Exception e) {
                                            String message = "Validation Error," + testName + "_id" + id + "," + rewrittenObj + "," + e.getMessage();
                                            CreateWriteFile.writeResultToFile(PathUtils.malformed_path, message);
                                        }

                                        //check if schema has id as ref path and if to use refExpander
                                        boolean refexpanderId = false;
                                        boolean refexpanderRef = false;
                                        boolean containsDefSection = false;
                                        boolean http = false;

                                        pattern = "\"";
                                        splitter = rewrittenSchema.split(pattern);

                                        for (var keyWord : splitter) {
                                            //Check for #foo… --> id
                                            if (keyWord.matches("^#[a-z]*")) {
                                                refexpanderId = true;
                                            }
                                            //Check for #/allOf/… --> ref
                                            if (keyWord.matches("^#\\/.*")) {
                                                refexpanderRef = true;
                                            }
                                            //check if def section already exists, if so do NOT normalise
                                            if (keyWord.matches("^\\$defs")) {
                                                containsDefSection = true;
                                            }
                                        }

                                        //Check if http is accuring
                                        for (var keyWord : splitter) {
                                            if (keyWord.matches("^http:.*") || keyWord.matches("^https:.*")) {
                                                http = true;
                                                break;
                                            }
                                        }

                                        //http is a boolean which will be true if the JsonSchema has an external referenciation.
                                        //Since we are not supporting this it always will be ignored.
                                        if (!http) {

                                            //Normalize schema if needed in order to get the references correct again.
                                            String normalizedSchema = null;

                                            if (refexpanderId && !refexpanderRef && !containsDefSection) {
                                                try {
                                                    normalizedSchema = lib.normalizeSchema(rewrittenSchema);
                                                } catch (Exception e){
                                                    String message = "Normalization Error," + testName + "," + id + "," + rewrittenSchema;
                                                    CreateWriteFile.writeResultToFile(PathUtils.malformed_path, message);
                                                }
                                            }

                                            if (valid) {

                                                JSONObject test = (JSONObject) j.get("tests");

                                                String s1SubsetEqOfs2 = null;
                                                String s2SubsetEqOfs1 = null;

                                                String schemaOne = "failed";
                                                String schemaTwo = "failed";
                                                List<String> internSchemas = null;

                                                //get the witnesses !
                                                try {
                                                    //Add a timeout
                                                    Future<List<String>> future = timeoutTask(schema1, schema2);
                                                    internSchemas = future.get(TIMEOUT, TimeUnit.SECONDS);
                                                } catch (Exception e) {
                                                    System.out.println("Something went wrong --> (compareSchemas get Witnesses)\n");
                                                    System.out.println("Message --> " + e.getMessage());
                                                    System.out.println(e.getCause());
                                                    System.out.println(run + " /8697 --> " + (float) run / 8697 * 100 + "%");
                                                }

                                                assert internSchemas != null;
                                                schemaOne = internSchemas.get(0);
                                                schemaTwo = internSchemas.get(1);

                                                schemaOne = toString_withoutEscape(schemaOne);
                                                schemaTwo = toString_withoutEscape(schemaTwo);

                                                //if normalizedSchema is not null then a normalization was needed.
                                                //Now the normalized Schema shall be used.
                                                if (normalizedSchema != null) {
                                                    schemaOne = normalizedSchema;
                                                    schemaTwo = normalizedSchema;
                                                }

                                                System.out.println();
                                                System.out.println(run + " /8697 --> " + (float) run / 8697 * 100 + "%");
                                                System.out.println();

                                                //get if there has been two or one argument. Then write the internal Schemas
                                                //in their directory in which they belong to.
                                                switch (test.size()) {
                                                    case 1:
                                                        s1SubsetEqOfs2 = test.get("s1SubsetEqOfs2").toString();
                                                        if (s1SubsetEqOfs2 != null) {
                                                            if (s1SubsetEqOfs2.equals("true")) {
                                                                hanldeFiles.writeToFile(PathUtils.unsatisfiable_path + "/" + draftVersionPath + testName + "_id" + id + "_", "subschema1_not_2.json", schemaOne);
                                                            } else {
                                                                hanldeFiles.writeToFile(PathUtils.satisfiable_path + "/" + draftVersionPath + testName + "_id" + id + "_", "subschema1_not_2.json", schemaOne);
                                                            }
                                                        } else {
                                                            s2SubsetEqOfs1 = test.get("s2SubsetEqOfs1").toString();
                                                            if (s2SubsetEqOfs1.equals("true")) {
                                                                hanldeFiles.writeToFile(PathUtils.unsatisfiable_path + "/" + draftVersionPath  + testName + "_id" + id + "_", "subschema2_not_1.json", schemaTwo);
                                                            } else {
                                                                hanldeFiles.writeToFile(PathUtils.satisfiable_path + "/" + draftVersionPath + testName + "_id" + id + "_", "subschema2_not_1.json", schemaTwo);
                                                            }
                                                        }
                                                        break;

                                                    case 2:
                                                        try {
                                                            s1SubsetEqOfs2 = test.get("s1SubsetEqOfs2").toString();
                                                            s2SubsetEqOfs1 = test.get("s2SubsetEqOfs1").toString();

                                                            if (s2SubsetEqOfs1.equals("true") && s1SubsetEqOfs2.equals("true")) {
                                                                hanldeFiles.writeToFile(PathUtils.unsatisfiable_path + "/" + draftVersionPath + testName + "_id" + id + "_", "subschema1_not_2.json", schemaOne);
                                                            } else if (s2SubsetEqOfs1.equals("false") && s1SubsetEqOfs2.equals("true")) {
                                                                hanldeFiles.writeToFile(PathUtils.unsatisfiable_path + "/" + draftVersionPath + testName + "_id" + id + "_", "subschema1_not_2.json", schemaOne);
                                                            } else if (s2SubsetEqOfs1.equals("false") && s1SubsetEqOfs2.equals("false")) {
                                                                hanldeFiles.writeToFile(PathUtils.satisfiable_path + "/" + draftVersionPath + testName + "_id" + id + "_", "subschema1_not_2.json", schemaOne);
                                                            } else {
                                                                hanldeFiles.writeToFile(PathUtils.unsatisfiable_path + "/" + draftVersionPath + testName + "_id" + id + "_", "subschema1_not_2.json", schemaOne);
                                                            }

                                                        } catch (Exception e) {
                                                            System.out.println("Catched");
                                                        }
                                                        break;

                                                    default:
                                                        System.out.println("There was more !");
                                                        break;
                                                }
                                            } else {
                                                String message = "Schema was not valid against draft6," + testName + "," + id + "," + rewrittenSchema;
                                                CreateWriteFile.writeResultToFile(PathUtils.malformed_path, message);
                                            }
                                        } else {
                                            String message = "Schema had external ref," + testName + "," + id + "," + rewrittenSchema;
                                            CreateWriteFile.writeResultToFile(PathUtils.malformed_path, message);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Use an extra Thread to get the intern generated schemas. By doing so it is possible to set a timeout, just in case.
     *
     * @param schema1
     * @param schema2
     * @return
     */
    public static Future<List<String>> timeoutTask(String schema1, String schema2) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<List<String>> future = executorService.submit(() -> {
            var witnesses = lib.getInternalSchemas(schema1, schema2);
            return witnesses;
        });

        executorService.shutdown();
        return future;
    }

    /**
     * Creates a string representation of a given object. Unescapes slashes and returns the result.
     *
     * @param jObject The object to create its string representation.
     * @return The string representation with unescaped slashes.
     */
    private static String toString_withoutEscape(Object jObject) {
        if (jObject == null) {
            return "null";
        }
        var str = jObject.toString();
        str = str.replace("\\/", "/");
        return str;
    }

    /**
     * This method first gets the schema and file data from Draft6.
     * Then it generates a Blacklist in case you want to ignore specific files.
     * And finally the schemas will be written to files which will be stored in the directories
     * "satisfiable" and "unsatisfiable".
     *
     * @param args
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException, TimeoutException, ProcessingException, ParseException {
        String[] blacklist = BlackListUtils.getBlackList(PathUtils.blackList);

        var draft3List = hanldeFiles.getFiles(PathUtils.draftPath + PathUtils.DRAFT3);
        var draft4List = hanldeFiles.getFiles(PathUtils.draftPath + PathUtils.DRAFT4);
        var draft6List = hanldeFiles.getFiles(PathUtils.draftPath + PathUtils.DRAFT6);
        var draft7List = hanldeFiles.getFiles(PathUtils.draftPath + PathUtils.DRAFT7);
        var draft2019_09List = hanldeFiles.getFiles(PathUtils.draftPath + PathUtils.DRAFT2019_09);
        var draft2020_12List = hanldeFiles.getFiles(PathUtils.draftPath + PathUtils.DRAFT2020_12);

        List<String> list = Stream.of(draft3List, draft4List, draft6List, draft7List, draft2019_09List, draft2020_12List)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        jsonData(list, blacklist);
    }

    /**
     * This method checks if all files have been checked. And have been correctly passed over to the correct dir.
     *
     * @param filePaths
     * @param blackList
     * @param satFiles
     * @throws FileNotFoundException
     */
    public static void sanityCheck(List<String> filePaths, String[] blackList, List<String> satFiles) throws FileNotFoundException {
        int suite_false = 0;

        for (String path : filePaths) {
            String schema = CreateWriteFile.fileToSchema(path);

            //Get JSON File as Array
            Object obj = null;
            try {
                obj = new JSONParser().parse(schema);
            } catch (Exception e) {
                System.out.println("malformed JSON");
            }

            if (obj == null) {
                System.out.println("Was null");
            } else {
                JSONArray jArray = (JSONArray) obj;

                for (int i = 0; i < jArray.size(); i++) {

                    JSONObject j = (JSONObject) jArray.get(i);

                    String id = j.get("id").toString();
                    String schema1 = j.get("schema1").toString();
                    String schema2 = j.get("schema2").toString();
                    JSONObject test = (JSONObject) j.get("tests");

                    String s1SubsetEqOfs2 = null;
                    String s2SubsetEqOfs1 = null;

                    switch (test.size()) {
                        case 1:
                            s1SubsetEqOfs2 = test.get("s1SubsetEqOfs2").toString();
                            if (s1SubsetEqOfs2 != null) {
                                if (s1SubsetEqOfs2.equals("true")) {
                                    //nothing
                                } else {
                                    suite_false += 1;
                                }
                            } else {
                                s2SubsetEqOfs1 = test.get("s2SubsetEqOfs1").toString();
                                if (s2SubsetEqOfs1.equals("true")) {
                                    //nothing
                                } else {
                                    suite_false += 1;
                                }
                            }
                            break;

                        case 2:
                            try {
                                s1SubsetEqOfs2 = test.get("s1SubsetEqOfs2").toString();
                                s2SubsetEqOfs1 = test.get("s2SubsetEqOfs1").toString();

                                if (s2SubsetEqOfs1.equals("true") && s1SubsetEqOfs2.equals("true")) {
                                    //nothing
                                } else if (s2SubsetEqOfs1.equals("false") && s1SubsetEqOfs2.equals("true")) {
                                    // nothing
                                } else if (s2SubsetEqOfs1.equals("false") && s1SubsetEqOfs2.equals("false")) {
                                    suite_false += 1;
                                } else {
                                    //nothing
                                }

                            } catch (Exception e) {
                                System.out.println("Catched");
                            }
                            break;

                        default:
                            System.out.println("There was more !");
                            break;
                    }
                }
            }

            int count_files_sat = new File(GenerateInternSchemas.satFiles).list().length;
            System.out.println("False was --> " + suite_false + "\n Suite was --> " + count_files_sat);
        }
    }
}
