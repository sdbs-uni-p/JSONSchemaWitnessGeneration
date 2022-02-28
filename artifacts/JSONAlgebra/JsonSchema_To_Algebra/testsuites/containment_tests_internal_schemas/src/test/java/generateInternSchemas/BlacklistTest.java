package generateInternSchemas;

import generateInternSchemas.helper.BlackListUtils;
import generateInternSchemas.helper.CreateWriteFile;
import generateInternSchemas.helper.PathUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlacklistTest {

    private static String path = System.getProperty("user.dir") + "/JsonSchema_To_Algebra/testsuites/JSONSchemaContainmentTestSuite/draft6/valid/const.json";

    public static boolean isBlackedTest(String[] blackList, String testName, String id) {
        boolean isBlacked = false;
        //get rid of draft6_ start.
        testName = testName.substring(7);

        for (int i = 0; i < blackList.length; i++) {
            if (i == 0) {
                //do nothing
            } else {
                String current = blackList[i];
                String[] tmpArray = current.split(",");
                String[] testNameData = testName.split("_");


                if (tmpArray.length == 1 || tmpArray.length == 2) {
                    switch (tmpArray.length) {
                        case 1:
                            if (tmpArray[0].equals(testName)) {
                                isBlacked = true;
                            }
                            break;

                        case 2:
                            if (tmpArray[0].equals(testName) && tmpArray[1].equals(id)) {
                                isBlacked = true;
                            }
                            break;

                        default:
                            System.out.println("case not implemented yet");
                            break;
                    }
                } else if (tmpArray.length == 3) {
                    var toBeExcluded = tmpArray[2];
                    for (var t : testNameData) {
                        if (t.equals(toBeExcluded)) {
                            isBlacked = true;
                        }
                    }
                }
            }
        }

        return isBlacked;
    }

    public static void main(String[] args) throws IOException {

        int alpha = 0;

        CreateWriteFile hanldeFiles = new CreateWriteFile();
        String[] blacklist = BlackListUtils.getBlackList(PathUtils.blackList);
        var filePaths = hanldeFiles.getFiles(PathUtils.draftPath + PathUtils.DRAFT6);
        //List<String> filePaths = new ArrayList<>();
        //filePaths.add(path);

        for (String path : filePaths) {

            //create a testName which is traceable.
            String schema = CreateWriteFile.fileToSchema(path);
            String rawName = path.substring(101, path.length() - 5);
            String rawName2 = rawName.replace("-", "_");
            String testName = rawName2.replace("\\", "_").replace("/", "_");

            if (isBlackedTest(blacklist, testName, null)) {
                System.out.println();
                System.out.println(testName);
            } else {

                Object obj = null;
                try {
                    obj = new JSONParser().parse(schema);
                } catch (Exception e) {
                    System.out.println("malformed JSON");
                }

                boolean b = false;

                JSONArray jArray = (JSONArray) obj;
                if (jArray == null) {
                    System.out.println();
                    System.out.println("NULL !!!");
                    System.out.println();
                }


                for (int i = 0; i < jArray.size(); i++) {

                    JSONObject j = (JSONObject) jArray.get(i);

                    String id = j.get("id").toString();
                    String schema1 = j.get("schema1").toString();
                    String schema2 = j.get("schema2").toString();

                    b = isBlackedTest(blacklist, testName, id);
                    if (b) {
                        System.out.println("was true \t-->" + testName + ", " + id);
                        alpha += 1;
                    }
                }
            }
        }
        System.out.println();
        System.out.println(alpha);
    }
}

// draft6_valid_uniqueItems
// valid_const
