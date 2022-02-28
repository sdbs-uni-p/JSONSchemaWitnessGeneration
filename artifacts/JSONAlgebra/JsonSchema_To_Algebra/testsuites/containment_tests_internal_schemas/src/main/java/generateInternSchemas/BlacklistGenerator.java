package generateInternSchemas;

import generateInternSchemas.helper.CreateWriteFile;
import generateInternSchemas.helper.PathUtils;

import java.util.stream.Collectors;

/**
 * This is a helper class.
 */
public class BlacklistGenerator {

    private static final String PATH_TO_DATA = System.getProperty("user.dir") + "/JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/src/main/java/generateInternSchemas/malformedJson.txt";

    public static void main(String[] args) {
        addProblemSchemasToBlacklist(PATH_TO_DATA, PathUtils.blackList);
    }

    public static void addProblemSchemasToBlacklist(String pathToData, String pathToBlacklist) {
        String data = CreateWriteFile.fileToSchema(pathToData);
        String split = ",";

        for (String line : data.lines().collect(Collectors.toList())) {
            var splitted = line.split(split);

            String testPath = splitted[1];
            String testId = splitted[2];

            var split1 = testPath.split("_");

            CreateWriteFile.writeResultToFile(pathToBlacklist, split1[0] + "," + testPath.substring(7) + "," + testId);
        }
    }
}