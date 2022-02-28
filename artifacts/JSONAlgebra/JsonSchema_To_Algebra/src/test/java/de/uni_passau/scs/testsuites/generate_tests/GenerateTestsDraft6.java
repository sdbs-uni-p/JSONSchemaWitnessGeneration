package de.uni_passau.scs.testsuites.generate_tests;

import de.uni_passau.scs.testsuites.generate_tests.templates.TestTemplates;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Ignore;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Luca Escher
 */
public class GenerateTestsDraft6 {

    private static final String blackList = System.getProperty("user.dir") + "/JsonSchema_To_Algebra/src/test/java/de/uni_passau/scs/testsuites/generate_tests/Blacklist.txt";
    public static String draft6Path = System.getProperty("user.dir") + "/JsonSchema_To_Algebra/testsuites/JSONSchemaContainmentTestSuite/draft6/";
    private static final String targetPathDraft6 = System.getProperty("user.dir") + "/JsonSchema_To_Algebra/src/test/java/de/uni_passau/scs/testsuites/draft6/TestsuiteDraft6.java";
    private static final int FILE_LENGTH = 1398;

    private String contentPath;
    private String targetPath;

    public GenerateTestsDraft6(String contentPath, String targetPath) {
        this.contentPath = contentPath;
        this.targetPath = targetPath;
    }

    public List<String> getFiles() throws IOException {
        List<String> filePaths = new ArrayList<>();

        Files.walk(Paths.get(contentPath))
                .filter(Files::isRegularFile)
                .forEach((path -> filePaths.add(path.toString())));

        return filePaths;
    }

    public List<String> readFiles(List<String> files) {
        List<String> schemas = new ArrayList<>();
        files.listIterator().forEachRemaining((schema) -> {
            try {
                schemas.add(
                        fileToSchema(schema) + "------------------");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        return schemas;
    }

    public String fileToSchema(String fileName) throws FileNotFoundException {

        File file = new File(fileName);
        StringBuilder fileContents = new StringBuilder((int) file.length());

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + System.lineSeparator());
            }
            return fileContents.toString();
        }
    }

    public void prepareFile() {
        try (FileWriter fw = new FileWriter(targetPath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            //Delete last curly bracket and append tests
            RandomAccessFile raf = new RandomAccessFile(targetPath, "rw");
            System.out.println("File Length=" + raf.length());
            raf.setLength(FILE_LENGTH);
            System.out.println("File Length=" + raf.length());
            raf.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void generateTests(String template) {
        try (FileWriter fw = new FileWriter(targetPath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            //beauty one line space
            out.println();

            out.println(template);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printClosingCurlyBracket() {
        try (FileWriter fw = new FileWriter(targetPath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println("}");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getJsonElements(GenerateTestsDraft6 generate, List<String> filePaths, String[] blackList) throws IOException, JSONException {

        for (String path : filePaths) {
            boolean toBeIgnored = false;

            String schema = fileToSchema(path);
            System.out.println(path);
            String rawName = path.substring(101, path.length() - 5);
            String rawName2 = rawName.replace("-", "_");
            String testName = rawName2.replace("\\", "_").replace("/", "_");

            //check if the test is defined in the blacklist. If so, it is to be ignored.
            if (BlackListUtils.isBlackedTest(blackList, testName, null)) {
                toBeIgnored = true;
            }

            //Get JSON File as Array
            JSONArray jArray = new JSONArray(schema);

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject j = jArray.getJSONObject(i);

                String id = j.getString("id");
                String schema1 = j.getString("schema1");
                String schema2 = j.getString("schema2");
                JSONObject test = j.getJSONObject("tests");

                schema1 = toString_withoutEscape(schema1);
                schema2 = toString_withoutEscape(schema2);


                if (BlackListUtils.isBlackedTest(blackList, testName, id)) {
                    toBeIgnored = true;
                }

                String s1SubsetEqOfs2 = null;
                String s2SubsetEqOfs1 = null;
                String template = null;

                if (!toBeIgnored) {
                    switch (test.length()) {
                        case 1:
                            s1SubsetEqOfs2 = test.getString("s1SubsetEqOfs2");

                            if (s1SubsetEqOfs2 != null) {
                                if (s1SubsetEqOfs2.equals("true")) {
                                    template = TestTemplates.createTestTemplateS1SubsetOfS2True(testName + "_id" + id, schema1, schema2, s1SubsetEqOfs2);
                                } else {
                                    template = TestTemplates.createTestTemplateS1SubsetOfS2False(testName + "_id" + id, schema1, schema2, s1SubsetEqOfs2);
                                }
                            } else {
                                s2SubsetEqOfs1 = test.getString("s2SubsetEqOfs1");
                                if (s2SubsetEqOfs1.equals("true")) {
                                    template = TestTemplates.createTestTemplateS2SubsetOfS1True(testName + "_id" + id, schema1, schema2, s2SubsetEqOfs1);
                                } else {
                                    template = TestTemplates.createTestTemplateS2SubsetOfS1False(testName + "_id" + id, schema1, schema2, s2SubsetEqOfs1);
                                }
                            }
                            generateTests(template);
                            break;
                        case 2:
                            try {
                                s1SubsetEqOfs2 = test.getString("s1SubsetEqOfs2");
                                s2SubsetEqOfs1 = test.getString("s2SubsetEqOfs1");

                                if (s2SubsetEqOfs1.equals("true") && s1SubsetEqOfs2.equals("true")) {
                                    template = TestTemplates.createTestTemplate2ResultsS2trueS1true(testName + "_id" + id, schema1, schema2, s1SubsetEqOfs2, s2SubsetEqOfs1);
                                } else if (s2SubsetEqOfs1.equals("false") && s1SubsetEqOfs2.equals("true")) {
                                    template = TestTemplates.createTestTemplate2ResultsS2falseS1true(testName + "_id" + id, schema1, schema2, s1SubsetEqOfs2, s2SubsetEqOfs1);
                                } else if (s2SubsetEqOfs1.equals("false") && s1SubsetEqOfs2.equals("false")) {
                                    template = TestTemplates.createTestTemplate2ResultsS2falseS1false(testName + "_id" + id, schema1, schema2, s1SubsetEqOfs2, s2SubsetEqOfs1);
                                } else {
                                    template = TestTemplates.createTestTemplate2ResultsS2trueS1false(testName + "_id" + id, schema1, schema2, s1SubsetEqOfs2, s2SubsetEqOfs1);
                                }

                                generate.generateTests(template);

                            } catch (Exception e) {
                                System.out.println("Catched");
                            }
                            break;
                        default:
                            System.out.println("There was more !");
                            break;
                    }
                } else {
                    switch (test.length()) {
                        case 1:
                            s1SubsetEqOfs2 = test.getString("s1SubsetEqOfs2");

                            if (s1SubsetEqOfs2 != null) {
                                if (s1SubsetEqOfs2.equals("true")) {
                                    template = TestTemplates.ignoreCreateTestTemplateS1SubsetOfS2True(testName + "_id" + id, schema1, schema2, s1SubsetEqOfs2);
                                } else {
                                    template = TestTemplates.ignoreCreateTestTemplateS1SubsetOfS2False(testName + "_id" + id, schema1, schema2, s1SubsetEqOfs2);
                                }
                            } else {
                                s2SubsetEqOfs1 = test.getString("s2SubsetEqOfs1");
                                if (s2SubsetEqOfs1.equals("true")) {
                                    template = TestTemplates.ignoreCreateTestTemplateS2SubsetOfS1True(testName + "_id" + id, schema1, schema2, s2SubsetEqOfs1);
                                } else {
                                    template = TestTemplates.ignoreCreateTestTemplateS2SubsetOfS1False(testName + "_id" + id, schema1, schema2, s2SubsetEqOfs1);
                                }
                            }
                            generateTests(template);
                            break;
                        case 2:
                            try {
                                s1SubsetEqOfs2 = test.getString("s1SubsetEqOfs2");
                                s2SubsetEqOfs1 = test.getString("s2SubsetEqOfs1");

                                if (s2SubsetEqOfs1.equals("true") && s1SubsetEqOfs2.equals("true")) {
                                    template = TestTemplates.ignoreCreateTestTemplate2ResultsS2trueS1true(testName + "_id" + id, schema1, schema2, s1SubsetEqOfs2, s2SubsetEqOfs1);
                                } else if (s2SubsetEqOfs1.equals("false") && s1SubsetEqOfs2.equals("true")) {
                                    template = TestTemplates.ignoreCreateTestTemplate2ResultsS2falseS1true(testName + "_id" + id, schema1, schema2, s1SubsetEqOfs2, s2SubsetEqOfs1);
                                } else if (s2SubsetEqOfs1.equals("false") && s1SubsetEqOfs2.equals("false")) {
                                    template = TestTemplates.ignoreCreateTestTemplate2ResultsS2falseS1false(testName + "_id" + id, schema1, schema2, s1SubsetEqOfs2, s2SubsetEqOfs1);
                                } else {
                                    template = TestTemplates.ignoreCreateTestTemplate2ResultsS2trueS1false(testName + "_id" + id, schema1, schema2, s1SubsetEqOfs2, s2SubsetEqOfs1);
                                }

                                generate.generateTests(template);

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
        }
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

    @Ignore
    public static void main(String[] args) throws IOException, JSONException {
        GenerateTestsDraft6 generate = new GenerateTestsDraft6(draft6Path, targetPathDraft6);
        List<String> filePaths = generate.getFiles();

        generate.prepareFile();
        generate.getJsonElements(generate, filePaths, BlackListUtils.getBlackList(blackList));
        generate.printClosingCurlyBracket();
    }
}