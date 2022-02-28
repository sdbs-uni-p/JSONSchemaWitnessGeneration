package de.uni_passau.scs.testsuites.generate_tests;

import java.io.File;
import java.util.Scanner;

public class BlackListUtils {

    /**
     * In order to get if the corresponding test/file is listed in the blacklist,
     * all files inside the blacklist are compared to the corresponding "testName" defined as an variable.
     *
     * @param blackList
     * @param testName
     * @param id
     * @return
     */
    public static boolean isBlackedTest(String[] blackList, String testName, String id) {
        boolean isBlacked = false;
        var draftV = testName.substring(0, 6);
        String oldTestName = testName;
        testName = testName.substring(7);

        for (int i = 0; i < blackList.length; i++) {
            if (i == 0) {
                //do nothing
            } else {
                String current = blackList[i];
                String[] tmpArray = current.split(",");
                String[] testNameData = testName.split("_");
                String[] oldTestNameData = oldTestName.split("_");

                if (oldTestNameData[0].equals("draft2019") && oldTestNameData[1].equals("09")) {
                    draftV = "draft2019";
                    oldTestName = oldTestName.substring(8);
                    testNameData = oldTestName.split("_");
                    testName = testName.substring(6);
                } else if (oldTestNameData[0].equals("draft2020") && oldTestNameData[1].equals("12")) {
                    draftV = "draft2020";
                    oldTestName = oldTestName.substring(8);
                    testNameData = oldTestName.split("_");
                    testName = testName.substring(6);
                }

                switch (tmpArray.length) {
                    case 1:
                        if (draftV.equals(tmpArray[0]) || tmpArray[0].equals("*")) {
                            isBlacked = true;
                        }
                        break;
                    case 2:
                        if ((draftV.equals(tmpArray[0]) || tmpArray[0].equals("*")) && tmpArray[1].equals(testName)) {
                            isBlacked = true;
                        }
                        break;

                    case 3:
                        if ((draftV.equals(tmpArray[0]) || tmpArray[0].equals("*")) && tmpArray[1].equals(testName) && tmpArray[2].equals(id)) {
                            isBlacked = true;
                        }
                        else if (tmpArray[2].equals("all")) {
                            if ((draftV.equals(tmpArray[0]) || tmpArray[0].equals("*"))) {
                                var toBeExcluded = tmpArray[1];
                                for (var t : testNameData) {
                                    if (t.equals(toBeExcluded)) {
                                        isBlacked = true;
                                        break;
                                    }
                                }
                            }
                        }
                        break;

                    case 4:
                        if ((draftV.equals(tmpArray[0]) || tmpArray[0].equals("*"))) {
                            var toBeExcluded = tmpArray[3];
                            for (var t : testNameData) {
                                if (t.equals(toBeExcluded)) {
                                    isBlacked = true;
                                }
                            }
                        }
                        break;

                    default:
                        System.out.println("case not implemented yet");
                        break;
                }
            }
        }
        return isBlacked;
    }

    /**
     * This method gets all Data from the Blacklist.
     * JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/src/java/Blacklist.txt
     *
     * @return Blacklist data as String Array.
     */
    public static String[] getBlackList(String pathToBlacklist){
        File file = new File(pathToBlacklist);
        StringBuilder fileContents = new StringBuilder((int) file.length());

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + System.lineSeparator());
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        String fileContent = fileContents.toString();
        return fileContent.split("\\n");
    }

}
