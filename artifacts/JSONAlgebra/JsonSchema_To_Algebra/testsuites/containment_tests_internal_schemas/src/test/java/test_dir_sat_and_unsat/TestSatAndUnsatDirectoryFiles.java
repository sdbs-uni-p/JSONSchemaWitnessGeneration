package test_dir_sat_and_unsat;

import generateInternSchemas.helper.CreateWriteFile;
import generateInternSchemas.helper.PathUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import validator.Validator;

import java.io.IOException;

/**
 * The main class goes through all files inside the directories satisfiable and unsatisfiable.
 * Then the validator will check if the are valid according to draft6. If so a :) is written, if not
 * the cause and the name will be written.
 *
 * @author Luca Escher
 */
public class TestSatAndUnsatDirectoryFiles {

    private static final String test_path = System.getProperty("user.dir") + "/JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/src/main/resources/test_result.txt";

    public static void main(String[] args) throws IOException {
        CreateWriteFile createWriteFile = new CreateWriteFile();
        Validator validator = new Validator();
        var listSat = createWriteFile.getFiles(PathUtils.satisfiable_path);
        var listUn = createWriteFile.getFiles(PathUtils.unsatisfiable_path);

        for (String file : listSat){
            String schema = CreateWriteFile.fileToSchema(file);
            String rawName = file.substring(90, file.length() - 5);
            String rawName2 = rawName.replace("-", "_");
            String testName = rawName2.replace("\\", "_").replace("/", "_");

            Object obj = null;
            try {
                obj = new JSONParser().parse(schema);
            } catch (Exception e) {
                System.out.println("malformed JSON");
            }

            if (obj == null) {
                String message = "Original Schema was malformed!\t\t-->\t" + testName + "\t" + schema;
                CreateWriteFile.writeResultToFile(test_path, message);
            }

            JSONObject jObject = (JSONObject) obj;
            boolean valid = validator.validateJson(jObject);

            if (!valid){
                CreateWriteFile.writeResultToFile(test_path, "sat----> " + testName );
            } else {
                CreateWriteFile.writeResultToFile(test_path, "sat----> :)");
            }

        }

        for (String file : listUn){
            String schema = CreateWriteFile.fileToSchema(file);
            String rawName = file.substring(90, file.length() - 5);
            String rawName2 = rawName.replace("-", "_");
            String testName = rawName2.replace("\\", "_").replace("/", "_");

            Object obj = null;
            try {
                obj = new JSONParser().parse(schema);
            } catch (Exception e) {
                System.out.println("malformed JSON");
            }

            if (obj == null) {
                String message = "Original Schema was malformed!\t\t-->\t" + testName + "\t" + schema;
                CreateWriteFile.writeResultToFile(test_path, message);
            }

            JSONObject jObject = (JSONObject) obj;
            boolean valid = validator.validateJson(jObject);

            if (!valid){
                CreateWriteFile.writeResultToFile(test_path, "UnSat----> " + testName );
            } else {
                CreateWriteFile.writeResultToFile(test_path, "UnSat----> :)");
            }

        }
    }
}
