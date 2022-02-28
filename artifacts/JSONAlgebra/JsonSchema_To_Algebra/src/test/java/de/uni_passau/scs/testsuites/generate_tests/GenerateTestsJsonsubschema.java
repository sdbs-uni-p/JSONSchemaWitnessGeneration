package de.uni_passau.scs.testsuites.generate_tests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Luca Escher
 */
public class GenerateTestsJsonsubschema {

    private static final String blackList = System.getProperty("user.dir") + "/JsonSchema_To_Algebra/src/test/java/de/uni_passau/scs/testsuites/generate_tests/Blacklist.txt";
    private static final String contentPathJsonSubSchema = System.getProperty("user.dir") + "/JsonSchema_To_Algebra/testsuites/tests_jsonsubschema";
    private static final String targetPathJsonSubSchema = System.getProperty("user.dir") + "/JsonSchema_To_Algebra/src/test/java/de/uni_passau/scs/testsuites/jsonsubschema/TestsuiteJsonSubSchema.java";
    private static final int FILE_LENGTH = 1438;


    private String contentPath;
    private String targetPath;

    public GenerateTestsJsonsubschema(String contentPath, String targetPath) {
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

    public void printClosingCurlyBracket(){
        try (FileWriter fw = new FileWriter(targetPath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {

            out.println("}");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String createTestTemplate2ResultsS2TrueS1True(String name, String schema1, String schema2, String s1Subset, String s2Subset) {
        String template =
                "    @Test(timeout = 5000)\n" +
                        "    public void " + name + "() throws Exception {\n" +
                        "\n" +
                        "        String schema1 = \"" + schema1.replace("\\", "\\\\").replace("\"", "\\\"") + "\";" +
                        "\n" +
                        "        String schema2 = \"" + schema2.replace("\\", "\\\\").replace("\"", "\\\"") + "\";" +
                        "\n" +
                        "        data = jsonSchemaLib.compareSchemas(schema1, schema2);\n" +
                        "\n" +
                        "        boolean resultS1Subset = false;\n" +
                        "        boolean resultS2Subset = false;\n" +
                        "\n" +
                        "        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {\n" +
                        "            resultS2Subset = true;\n" +
                        "        }\n" +
                        "\n" +
                        "        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {\n" +
                        "            resultS1Subset = true;\n" +
                        "        }\n" +
                        "\n" +
                        "        Assert.assertEquals(\"schema1 must be subset of schema2\", " + s1Subset + ", resultS1Subset);\n" +
                        "        Assert.assertEquals(\"schema2 must be subset of schema1\", " + s2Subset + ", resultS2Subset);\n" +
                        "    }";

        return template;
    }

    public static String createTestTemplate2ResultsS2FalseS1True(String name, String schema1, String schema2, String s1Subset, String s2Subset) {
        String template =
                "    @Test(timeout = 5000)\n" +
                        "    public void " + name + "() throws Exception {\n" +
                        "\n" +
                        "        String schema1 = \"" + schema1.replace("\\", "\\\\").replace("\"", "\\\"") + "\";" +
                        "\n" +
                        "        String schema2 = \"" + schema2.replace("\\", "\\\\").replace("\"", "\\\"") + "\";" +
                        "\n" +
                        "        data = jsonSchemaLib.compareSchemas(schema1, schema2);\n" +
                        "\n" +
                        "        boolean resultS1Subset = false;\n" +
                        "        boolean resultS2Subset = false;\n" +
                        "\n" +
                        "        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {\n" +
                        "            resultS2Subset = true;\n" +
                        "        }\n" +
                        "\n" +
                        "        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {\n" +
                        "            resultS1Subset = true;\n" +
                        "        }\n" +
                        "\n" +
                        "        Assert.assertEquals(\"schema1 must be subset of schema2\", " + s1Subset + ", resultS1Subset);\n" +
                        "        Assert.assertEquals(\"schema2 must not be subset of schema1\", " + s2Subset + ", resultS2Subset);\n" +
                        "    }";

        return template;
    }

    public static String createTestTemplate2ResultsS2FalseS1False(String name, String schema1, String schema2, String s1Subset, String s2Subset) {
        String template =
                "    @Test(timeout = 5000)\n" +
                        "    public void " + name + "() throws Exception {\n" +
                        "\n" +
                        "        String schema1 = \"" + schema1.replace("\\", "\\\\").replace("\"", "\\\"") + "\";" +
                        "\n" +
                        "        String schema2 = \"" + schema2.replace("\\", "\\\\").replace("\"", "\\\"") + "\";" +
                        "\n" +
                        "        data = jsonSchemaLib.compareSchemas(schema1, schema2);\n" +
                        "\n" +
                        "        boolean resultS1Subset = false;\n" +
                        "        boolean resultS2Subset = false;\n" +
                        "\n" +
                        "        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {\n" +
                        "            resultS2Subset = true;\n" +
                        "        }\n" +
                        "\n" +
                        "        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {\n" +
                        "            resultS1Subset = true;\n" +
                        "        }\n" +
                        "\n" +
                        "        Assert.assertEquals(\"schema1 must not be subset of schema2\", " + s1Subset + ", resultS1Subset);\n" +
                        "        Assert.assertEquals(\"schema2 must not be subset of schema1\", " + s2Subset + ", resultS2Subset);\n" +
                        "    }";

        return template;
    }

    public static String createTestTemplate2ResultsS2TrueS1False(String name, String schema1, String schema2, String s1Subset, String s2Subset) {
        String template =
                "    @Test(timeout = 5000)\n" +
                        "    public void " + name + "() throws Exception {\n" +
                        "\n" +
                        "        String schema1 = \"" + schema1.replace("\\", "\\\\").replace("\"", "\\\"") + "\";" +
                        "\n" +
                        "        String schema2 = \"" + schema2.replace("\\", "\\\\").replace("\"", "\\\"") + "\";" +
                        "\n" +
                        "        data = jsonSchemaLib.compareSchemas(schema1, schema2);\n" +
                        "\n" +
                        "        boolean resultS1Subset = false;\n" +
                        "        boolean resultS2Subset = false;\n" +
                        "\n" +
                        "        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {\n" +
                        "            resultS2Subset = true;\n" +
                        "        }\n" +
                        "\n" +
                        "        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {\n" +
                        "            resultS1Subset = true;\n" +
                        "        }\n" +
                        "\n" +
                        "        Assert.assertEquals(\"schema1 must not be subset of schema2\", " + s1Subset + ", resultS1Subset);\n" +
                        "        Assert.assertEquals(\"schema2 must be subset of schema1\", " + s2Subset + ", resultS2Subset);\n" +
                        "    }";

        return template;
    }

    public static String createTestTemplateS1SubsetOfS2True(String name, String schema1, String schema2, String test) {
        String template =
                "    @Test(timeout = 5000)\n" +
                        "    public void " + name + "() throws Exception {\n" +
                        "\n" +
                        "        String schema1 = \"" + schema1.replace("\\", "\\\\").replace("\"", "\\\"") + "\";" +
                        "\n" +
                        "        String schema2 = \"" + schema2.replace("\\", "\\\\").replace("\"", "\\\"") + "\";" +
                        "\n" +
                        "        data = jsonSchemaLib.compareSchemas(schema1, schema2);\n" +
                        "\n" +
                        "        boolean result = false;\n" +
                        "\n" +
                        "        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {\n" +
                        "            result = true;\n" +
                        "        }\n" +
                        "\n" +
                        "        if (JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {\n" +
                        "            result = true;\n" +
                        "        }\n" +
                        "\n" +
                        "        Assert.assertEquals(\"schema1 must be subset of schema2\", " + test + ", result);\n" +
                        "    }";

        return template;
    }

    public static String createTestTemplateS1SubsetOfS2False(String name, String schema1, String schema2, String test) {
        String template =
                "    @Test(timeout = 5000)\n" +
                        "    public void " + name + "() throws Exception {\n" +
                        "\n" +
                        "        String schema1 = \"" + schema1.replace("\\", "\\\\").replace("\"", "\\\"") + "\";" +
                        "\n" +
                        "        String schema2 = \"" + schema2.replace("\\", "\\\\").replace("\"", "\\\"") + "\";" +
                        "\n" +
                        "        data = jsonSchemaLib.compareSchemas(schema1, schema2);\n" +
                        "\n" +
                        "        boolean result = false;\n" +
                        "\n" +
                        "        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {\n" +
                        "            result = true;\n" +
                        "        }\n" +
                        "\n" +
                        "        if (JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {\n" +
                        "            result = true;\n" +
                        "        }\n" +
                        "\n" +
                        "        Assert.assertEquals(\"schema1 must not be subset of schema2\", " + test + ", result);\n" +
                        "    }";

        return template;
    }

    public static String createTestTemplateS2SubsetOfS1True(String name, String schema1, String schema2, String test) {
        String template =
                "    @Test(timeout = 5000)\n" +
                        "    public void " + name + "() throws Exception {\n" +
                        "\n" +
                        "        String schema1 = \"" + schema1.replace("\\", "\\\\").replace("\"", "\\\"") + "\";" +
                        "\n" +
                        "        String schema2 = \"" + schema2.replace("\\", "\\\\").replace("\"", "\\\"") + "\";" +
                        "\n" +
                        "        data = jsonSchemaLib.compareSchemas(schema1, schema2);\n" +
                        "\n" +
                        "        boolean result = false;\n" +
                        "\n" +
                        "        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship())) {\n" +
                        "            resultS2Subset = true;\n" +
                        "        }\n" +
                        "\n" +
                        "        if (JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {\n" +
                        "            result = true;\n" +
                        "        }\n" +
                        "\n" +
                        "        Assert.assertEquals(\"schema2 must be subset of schema2\", " + test + ", result);\n" +
                        "    }";

        return template;
    }

    public static String createTestTemplateS2SubsetOfS1False(String name, String schema1, String schema2, String test) {
        String template =
                "    @Test(timeout = 5000)\n" +
                        "    public void " + name + "() throws Exception {\n" +
                        "\n" +
                        "        String schema1 = \"" + schema1.replace("\\", "\\\\").replace("\"", "\\\"") + "\";" +
                        "\n" +
                        "        String schema2 = \"" + schema2.replace("\\", "\\\\").replace("\"", "\\\"") + "\";" +
                        "\n" +
                        "        data = jsonSchemaLib.compareSchemas(schema1, schema2);\n" +
                        "\n" +
                        "        boolean result = false;\n" +
                        "\n" +
                        "        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship())) {\n" +
                        "            resultS2Subset = true;\n" +
                        "        }\n" +
                        "\n" +
                        "        if (JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {\n" +
                        "            result = true;\n" +
                        "        }\n" +
                        "\n" +
                        "        Assert.assertEquals(\"schema2 must not be subset of schema2\", " + test + ", result);\n" +
                        "    }";

        return template;
    }

    public void getJsonElements(GenerateTestsJsonsubschema generate, List<String> filePaths, String[] blacklist) throws IOException, JSONException {

        for (String path : filePaths) {

            String schema = fileToSchema(path);
            System.out.println(path);
            String rawName = path.substring(70, path.length() - 5);
            String rawName2 = rawName.replace("-", "_");
            String testName = rawName2.replace("\\", "_").replace("/", "_");

            //Get JSON File as Array
            JSONArray jArray = new JSONArray(schema);

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject j = jArray.getJSONObject(i);

                String id = j.getString("id");
                String schema1 = j.getString("schema1");
                String schema2 = j.getString("schema2");
                JSONObject test = j.getJSONObject("tests");

                if (isBlackedTest(blacklist, testName, id)) {
                    //skip
                } else {

                    String s1SubsetOfs2 = null;
                    String s2SubsetOfs1 = null;
                    String template = null;

                    switch (test.length()) {
                        case 1:

                            s1SubsetOfs2 = test.getString("s1SubsetEqOfs2");

                            if (s1SubsetOfs2 != null) {
                                if (s1SubsetOfs2.equals("true")){
                                    template = createTestTemplateS1SubsetOfS2True(testName + "_id" + id, schema1, schema2, s1SubsetOfs2);
                                } else {
                                    template = createTestTemplateS1SubsetOfS2False(testName + "_id" + id, schema1, schema2, s1SubsetOfs2);
                                }
                            } else {
                                s2SubsetOfs1 = test.getString("s2SubsetEqOfs1");
                                if (s2SubsetOfs1.equals("true")) {
                                    template = createTestTemplateS2SubsetOfS1True(testName + "_id" + id, schema1, schema2, s2SubsetOfs1);
                                } else {
                                    template = createTestTemplateS2SubsetOfS1False(testName + "_id" + id, schema1, schema2, s2SubsetOfs1);
                                }
                            }
                            generateTests(template);
                            break;
                        case 2:
                            try {
                                s1SubsetOfs2 = test.getString("s1SubsetEqOfs2");
                                s2SubsetOfs1 = test.getString("s2SubsetEqOfs1");

                                if (s2SubsetOfs1.equals("true") && s1SubsetOfs2.equals("true")){
                                    template = createTestTemplate2ResultsS2TrueS1True(testName + "_id" + id, schema1, schema2, s1SubsetOfs2, s2SubsetOfs1);
                                } else if (s2SubsetOfs1.equals("false") && s1SubsetOfs2.equals("true")){
                                    template = createTestTemplate2ResultsS2FalseS1True(testName + "_id" + id, schema1, schema2, s1SubsetOfs2, s2SubsetOfs1);
                                } else if (s2SubsetOfs1.equals("false") && s1SubsetOfs2.equals("false")){
                                    template = createTestTemplate2ResultsS2FalseS1False(testName + "_id" + id, schema1, schema2, s1SubsetOfs2, s2SubsetOfs1);
                                } else {
                                    template = createTestTemplate2ResultsS2TrueS1False(testName + "_id" + id, schema1, schema2, s1SubsetOfs2, s2SubsetOfs1);
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

    public String[] getBlackList(){
        File file = new File(blackList);
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

    public boolean isBlackedTest(String[] blackList, String testName, String id){
        boolean isBlacked = false;

        for (int i = 0; i < blackList.length; i++){
            if (i == 0){
                //do nothing
            } else {
                String current = blackList[i];
                String[] tmpArray = current.split(",");

                //length is based on 1 not 0
                switch (tmpArray.length) {
                    case 0:
                        System.out.println("Line was empty!");
                        break;
                    case 1:
                        if (tmpArray[0].equals(testName)){
                            isBlacked = true;
                        }
                        break;
                    case 2:
                        if (tmpArray[0].equals(testName) && tmpArray[1].equals(id)) {
                            isBlacked = true;
                        }
                        break;
                }
            }
        }

        return isBlacked;
    }

    public static void main(String[] args) throws IOException, JSONException {
        GenerateTestsJsonsubschema generate = new GenerateTestsJsonsubschema(contentPathJsonSubSchema, targetPathJsonSubSchema);
        List<String> filePaths = generate.getFiles();

        generate.prepareFile();
        generate.getJsonElements(generate, filePaths, generate.getBlackList());
        generate.printClosingCurlyBracket();
    }
}