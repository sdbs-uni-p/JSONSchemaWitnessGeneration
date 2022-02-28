package de.uni_passau.sds.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.text.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JsonSchemaToolLibTest {

    private final IJsonSchemaLib jsonSchemaLib = new JsonSchemaToolLib();
    private final JSONParser parser = new JSONParser();

    private List<Path> getFiles(Path contentPath) throws IOException {
        List<Path> filePaths = new ArrayList<>();

        Files.walk(contentPath)
                .filter(Files::isRegularFile)
                .forEach((filePaths::add));

        filePaths.sort(Comparator.naturalOrder());
        return filePaths;
    }

    private String testTemplate(String name, String schema1, String schema2, String expected) {
        return "    @Test\n" +
                "    public void test_" + name + "() throws ParseException {\n" +
                "        String schema1 = \"" + formatJsonString(schema1) + "\";\n" +
                "        String schema2 = \"" + formatJsonString(schema2) + "\";\n" +
                "        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);\n" +
                "        String expected = \"" + formatJsonString(expected) + "\";\n" +
                "        JSONObject expectedObj = (JSONObject) parser.parse(expected);\n" +
                "        JSONObject differenceObj = (JSONObject) parser.parse(difference);\n" +
                "        assertEquals(expectedObj, differenceObj);\n" +
                "    }";
    }

    private String formatJsonString(String jsonString) {
        StringBuilder builder = new StringBuilder();
        String escaped = StringEscapeUtils.escapeJava(jsonString);
        String[] parts = escaped.split("\\\\n");
        for (int i = 0; i < parts.length - 1; i++) {
            builder.append(parts[i]).append("\" + \n");
            builder.append("                         \"");
        }
        builder.append(parts[parts.length - 1]);
        return builder.toString();
    }

    private String generateTest(Path file) throws IOException, ParseException {
        String name = file.getFileName().toString().replace(".json", "");
        String schema1 = "";
        String schema2 = "";
        String expected = "";
        Path testfile = Paths.get("JsonSchema_To_Algebra/testsuites/JSONSchemaContainmentTestSuite");
        String[] dir = name.split("_");
        testfile = testfile.resolve(dir[0]).resolve(dir[1]);
        List<Path> filePaths = new ArrayList<>();

        Files.walk(testfile)
                .filter(Files::isRegularFile)
                .forEach((filePaths::add));

        for (Path f : filePaths) {
            if (f.getFileName().toString().startsWith(dir[2])) {
                testfile = f;
                break;
            }
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileReader reader = new FileReader(testfile.toString())) {
            Object obj = parser.parse(reader);
            JSONArray array = (JSONArray) obj;
            int i = 3;
            int id = 0;
            int firstSubschema = 0;
            while (i < dir.length) {
                if (dir[i].startsWith("id")) {
                    id = Integer.parseInt(dir[i].substring(2));
                    i++;
                    break;
                }
                i++;
            }
            if (dir[i].startsWith("subschema")) {
                firstSubschema = Integer.parseInt(dir[i].substring(9));
            }
            for (var o : array) {
                JSONObject jsonObj = (JSONObject) o;
                if ((Long) jsonObj.get("id") == id) {
                    Object schemaObj1 = jsonObj.get("schema" + firstSubschema);
                    Object schemaObj2 = jsonObj.get("schema" + (firstSubschema == 1 ? 2 : 1));
                    schema1 = gson.toJson(schemaObj1);
                    schema2 = gson.toJson(schemaObj2);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try (FileReader reader = new FileReader(file.toString())) {
            Object expectedObj = parser.parse(reader);
            expected = gson.toJson(expectedObj);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return testTemplate(name, schema1, schema2, expected);
    }

    private String generateTests(Path contentPath) throws IOException, ParseException {
        StringBuilder builder = new StringBuilder();
        for (Path file : getFiles(contentPath)) {
            builder.append(generateTest(file));
            builder.append("\n\n");
        }
        return builder.toString();
    }

    /**
     * Start this method to generate tests for problematic JSON Schema differences.
     * Store the files with the wrong results in {@code contentPath}.
     * The variable {@code targetPath} stores the file in which to write the tests, at default it is this file.
     * Delete already generated tests before generating new ones.
     * After generating the test methods adjust the expected JSON string.
     */
    public static void main(String[] args) throws IOException, ParseException {
        JsonSchemaToolLibTest test = new JsonSchemaToolLibTest();
        Path contentPath = Paths.get(System.getProperty("user.dir")).getParent().resolve("ref_problems");
        Path targetPath = Paths.get("JsonSchema_To_Algebra/src/test/java/de/uni_passau/sds/lib/JsonSchemaToolLibTest.java");
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(targetPath.toString(), true)));
        // Delete last curly bracket
        RandomAccessFile raf = new RandomAccessFile(targetPath.toString(), "rw");
        long length = raf.length();
        raf.setLength(length - 2);
        raf.close();
        // Append tests
        out.print(test.generateTests(contentPath));
        // Append curly bracket
        out.print("}\n");
        out.close();
    }

    @Test
    public void test_refOnlyHashtag() throws ParseException {
        String schema1 = "{" +
                         "  \"const\": {" +
                         "    \"definitions\": {" +
                         "      \"foo\": {" +
                         "        \"type\": \"integer\"" +
                         "      }" +
                         "    }" +
                         "  }" +
                         "}";
        String schema2 = "{" +
                         "  \"not\": {" +
                         "    \"$ref\": \"#\"" +
                         "  }" +
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" +
                         "  \"allOf\": [" +
                         "    {" +
                         "      \"const\": {" +
                         "        \"definitions\": {" +
                         "          \"foo\": {" +
                         "            \"type\": \"integer\"" +
                         "          }" +
                         "        }" +
                         "      }" +
                         "    }," +
                         "    {" +
                         "      \"not\": {" +
                         "        \"not\": {" +
                         "          \"$ref\": \"#/allOf/1/not/\"" +
                         "        }" +
                         "      }" +
                         "    }" +
                         "  ]" +
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    // Generated tests starting here

    @Test
    public void test_draft6_nonvalid_definitions_id1_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"definitions\": {" + 
                         "      \"foo\": {" + 
                         "        \"type\": \"integer\"" + 
                         "      }" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"not\": {" + 
                         "    \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"definitions\": {" + 
                         "          \"foo\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"not\": {" + 
                         "          \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_nonvalid_infinite_loop_detection_id1_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"foo\": 1" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"not\": {" + 
                         "    \"allOf\": [" + 
                         "      {" + 
                         "        \"properties\": {" + 
                         "          \"foo\": {" + 
                         "            \"$ref\": \"#/definitions/int\"" + 
                         "          }" + 
                         "        }" + 
                         "      }," + 
                         "      {" + 
                         "        \"additionalProperties\": {" + 
                         "          \"$ref\": \"#/definitions/int\"" + 
                         "        }" + 
                         "      }" + 
                         "    ]" + 
                         "  }," + 
                         "  \"definitions\": {" + 
                         "    \"int\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"foo\": 1" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"not\": {" + 
                         "          \"allOf\": [" + 
                         "            {" + 
                         "              \"properties\": {" + 
                         "                \"foo\": {" + 
                         "                  \"$ref\": \"#/allOf/1/not/definitions/int\"" +
                         "                }" + 
                         "              }" + 
                         "            }," + 
                         "            {" + 
                         "              \"additionalProperties\": {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/int\"" +
                         "              }" + 
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        \"definitions\": {" + 
                         "          \"int\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_nonvalid_items_id14_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": [" + 
                         "    [" + 
                         "      {}," + 
                         "      {}" + 
                         "    ]," + 
                         "    [" + 
                         "      {}," + 
                         "      {}" + 
                         "    ]," + 
                         "    [" + 
                         "      {}," + 
                         "      {}" + 
                         "    ]" + 
                         "  ]" + 
                         "}";
        String schema2 = "{" + 
                         "  \"not\": {" + 
                         "    \"items\": [" + 
                         "      {" + 
                         "        \"$ref\": \"#/definitions/item\"" + 
                         "      }," + 
                         "      {" + 
                         "        \"$ref\": \"#/definitions/item\"" + 
                         "      }," + 
                         "      {" + 
                         "        \"$ref\": \"#/definitions/item\"" + 
                         "      }" + 
                         "    ]" + 
                         "  }," + 
                         "  \"additionalItems\": false," + 
                         "  \"type\": \"array\"," + 
                         "  \"definitions\": {" + 
                         "    \"item\": {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    \"sub-item\": {" + 
                         "      \"type\": \"object\"," + 
                         "      \"required\": [" + 
                         "        \"foo\"" + 
                         "      ]" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": [" + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]," + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]," + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"not\": {" + 
                         "          \"items\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "            }," + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "            }," + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        \"additionalItems\": false," + 
                         "        \"type\": \"array\"," + 
                         "        \"definitions\": {" + 
                         "          \"item\": {" + 
                         "            \"additionalItems\": false," + 
                         "            \"type\": \"array\"," + 
                         "            \"items\": [" + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"sub-item\": {" + 
                         "            \"type\": \"object\"," + 
                         "            \"required\": [" + 
                         "              \"foo\"" + 
                         "            ]" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_nonvalid_items_id15_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": [" + 
                         "    [" + 
                         "      {}" + 
                         "    ]," + 
                         "    [" + 
                         "      {}" + 
                         "    ]" + 
                         "  ]" + 
                         "}";
        String schema2 = "{" + 
                         "  \"not\": {" + 
                         "    \"items\": [" + 
                         "      {" + 
                         "        \"$ref\": \"#/definitions/item\"" + 
                         "      }," + 
                         "      {" + 
                         "        \"$ref\": \"#/definitions/item\"" + 
                         "      }," + 
                         "      {" + 
                         "        \"$ref\": \"#/definitions/item\"" + 
                         "      }" + 
                         "    ]" + 
                         "  }," + 
                         "  \"additionalItems\": false," + 
                         "  \"type\": \"array\"," + 
                         "  \"definitions\": {" + 
                         "    \"item\": {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    \"sub-item\": {" + 
                         "      \"type\": \"object\"," + 
                         "      \"required\": [" + 
                         "        \"foo\"" + 
                         "      ]" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": [" + 
                         "        [" + 
                         "          {}" + 
                         "        ]," + 
                         "        [" + 
                         "          {}" + 
                         "        ]" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"not\": {" + 
                         "          \"items\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "            }," + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "            }," + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        \"additionalItems\": false," + 
                         "        \"type\": \"array\"," + 
                         "        \"definitions\": {" + 
                         "          \"item\": {" + 
                         "            \"additionalItems\": false," + 
                         "            \"type\": \"array\"," + 
                         "            \"items\": [" + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"sub-item\": {" + 
                         "            \"type\": \"object\"," + 
                         "            \"required\": [" + 
                         "              \"foo\"" + 
                         "            ]" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_nonvalid_ref_id10_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"foo\": [" + 
                         "      1," + 
                         "      2," + 
                         "      3" + 
                         "    ]" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"not\": {" + 
                         "    \"properties\": {" + 
                         "      \"foo\": {" + 
                         "        \"maxItems\": 2," + 
                         "        \"$ref\": \"#/definitions/reffed\"" + 
                         "      }" + 
                         "    }" + 
                         "  }," + 
                         "  \"definitions\": {" + 
                         "    \"reffed\": {" + 
                         "      \"type\": \"array\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"foo\": [" + 
                         "          1," + 
                         "          2," + 
                         "          3" + 
                         "        ]" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"not\": {" + 
                         "          \"properties\": {" + 
                         "            \"foo\": {" + 
                         "              \"maxItems\": 2," + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/reffed\"" + 
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        \"definitions\": {" + 
                         "          \"reffed\": {" + 
                         "            \"type\": \"array\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_nonvalid_ref_id11_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"minLength\": 1" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"not\": {" + 
                         "    \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"minLength\": 1" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"not\": {" + 
                         "          \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_nonvalid_ref_id13_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"$ref\": \"a\"" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"not\": {" + 
                         "    \"properties\": {" + 
                         "      \"$ref\": {" + 
                         "        \"$ref\": \"#/definitions/is-string\"" + 
                         "      }" + 
                         "    }" + 
                         "  }," + 
                         "  \"definitions\": {" + 
                         "    \"is-string\": {" + 
                         "      \"type\": \"string\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"$ref\": \"a\"" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"not\": {" + 
                         "          \"properties\": {" + 
                         "            \"$ref\": {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/is-string\"" + 
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        \"definitions\": {" + 
                         "          \"is-string\": {" + 
                         "            \"type\": \"string\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_nonvalid_ref_id14_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": \"foo\"" + 
                         "}";
        String schema2 = "{" + 
                         "  \"not\": {" + 
                         "    \"allOf\": [" + 
                         "      {" + 
                         "        \"$ref\": \"#/definitions/bool\"" + 
                         "      }" + 
                         "    ]" + 
                         "  }," + 
                         "  \"definitions\": {" + 
                         "    \"bool\": true" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": \"foo\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"not\": {" + 
                         "          \"allOf\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/bool\"" + 
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        \"definitions\": {" + 
                         "          \"bool\": true" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_nonvalid_ref_id15_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"nodes\": [" + 
                         "      {" + 
                         "        \"subtree\": {" + 
                         "          \"nodes\": [" + 
                         "            {" + 
                         "              \"value\": 1.1" + 
                         "            }," + 
                         "            {" + 
                         "              \"value\": 1.2" + 
                         "            }" + 
                         "          ]," + 
                         "          \"meta\": \"child\"" + 
                         "        }," + 
                         "        \"value\": 1" + 
                         "      }," + 
                         "      {" + 
                         "        \"subtree\": {" + 
                         "          \"nodes\": [" + 
                         "            {" + 
                         "              \"value\": 2.1" + 
                         "            }," + 
                         "            {" + 
                         "              \"value\": 2.2" + 
                         "            }" + 
                         "          ]," + 
                         "          \"meta\": \"child\"" + 
                         "        }," + 
                         "        \"value\": 2" + 
                         "      }" + 
                         "    ]," + 
                         "    \"meta\": \"root\"" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"not\": {" + 
                         "    \"properties\": {" + 
                         "      \"nodes\": {" + 
                         "        \"type\": \"array\"," + 
                         "        \"items\": {" + 
                         "          \"$ref\": \"node\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"meta\": {" + 
                         "        \"type\": \"string\"" + 
                         "      }" + 
                         "    }" + 
                         "  }," + 
                         "  \"description\": \"tree of nodes\"," + 
                         "  \"type\": \"object\"," + 
                         "  \"definitions\": {" + 
                         "    \"node\": {" + 
                         "      \"description\": \"node\"," + 
                         "      \"type\": \"object\"," + 
                         "      \"properties\": {" + 
                         "        \"subtree\": {" + 
                         "          \"$ref\": \"tree\"" + 
                         "        }," + 
                         "        \"value\": {" + 
                         "          \"type\": \"number\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"required\": [" + 
                         "        \"value\"" + 
                         "      ]," + 
                         "      \"$id\": \"http://localhost:1234/node\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"required\": [" + 
                         "    \"meta\"," + 
                         "    \"nodes\"" + 
                         "  ]," + 
                         "  \"$id\": \"http://localhost:1234/tree\"" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"nodes\": [" + 
                         "          {" + 
                         "            \"subtree\": {" + 
                         "              \"nodes\": [" + 
                         "                {" + 
                         "                  \"value\": 1.1" + 
                         "                }," + 
                         "                {" + 
                         "                  \"value\": 1.2" + 
                         "                }" + 
                         "              ]," + 
                         "              \"meta\": \"child\"" + 
                         "            }," + 
                         "            \"value\": 1" + 
                         "          }," + 
                         "          {" + 
                         "            \"subtree\": {" + 
                         "              \"nodes\": [" + 
                         "                {" + 
                         "                  \"value\": 2.1" + 
                         "                }," + 
                         "                {" + 
                         "                  \"value\": 2.2" + 
                         "                }" + 
                         "              ]," + 
                         "              \"meta\": \"child\"" + 
                         "            }," + 
                         "            \"value\": 2" + 
                         "          }" + 
                         "        ]," + 
                         "        \"meta\": \"root\"" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"not\": {" + 
                         "          \"properties\": {" + 
                         "            \"nodes\": {" + 
                         "              \"type\": \"array\"," + 
                         "              \"items\": {" + 
                         "                \"$ref\": \"node\"" + 
                         "              }" + 
                         "            }," + 
                         "            \"meta\": {" + 
                         "              \"type\": \"string\"" + 
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        \"description\": \"tree of nodes\"," + 
                         "        \"type\": \"object\"," + 
                         "        \"definitions\": {" + 
                         "          \"node\": {" + 
                         "            \"description\": \"node\"," + 
                         "            \"type\": \"object\"," + 
                         "            \"properties\": {" + 
                         "              \"subtree\": {" + 
                         "                \"$ref\": \"tree\"" + 
                         "              }," + 
                         "              \"value\": {" + 
                         "                \"type\": \"number\"" + 
                         "              }" + 
                         "            }," + 
                         "            \"required\": [" + 
                         "              \"value\"" + 
                         "            ]," + 
                         "            \"$id\": \"http://localhost:1234/node\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"required\": [" + 
                         "          \"meta\"," + 
                         "          \"nodes\"" + 
                         "        ]," + 
                         "        \"$id\": \"http://localhost:1234/tree\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_nonvalid_ref_id16_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"foo\\\"bar\": 1" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"not\": {" + 
                         "    \"properties\": {" + 
                         "      \"foo\\\"bar\": {" + 
                         "        \"$ref\": \"#/definitions/foo%22bar\"" + 
                         "      }" + 
                         "    }" + 
                         "  }," + 
                         "  \"definitions\": {" + 
                         "    \"foo\\\"bar\": {" + 
                         "      \"type\": \"number\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"foo\\\"bar\": 1" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"not\": {" + 
                         "          \"properties\": {" + 
                         "            \"foo\\\"bar\": {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/foo%22bar\"" + 
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        \"definitions\": {" + 
                         "          \"foo\\\"bar\": {" + 
                         "            \"type\": \"number\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_nonvalid_ref_id17_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": 1" + 
                         "}";
        String schema2 = "{" + 
                         "  \"not\": {" + 
                         "    \"allOf\": [" + 
                         "      {" + 
                         "        \"$ref\": \"#foo\"" + 
                         "      }" + 
                         "    ]" + 
                         "  }," + 
                         "  \"definitions\": {" + 
                         "    \"A\": {" + 
                         "      \"type\": \"integer\"," + 
                         "      \"$id\": \"#foo\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": 1" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"not\": {" + 
                         "          \"allOf\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#foo\"" + 
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        \"definitions\": {" + 
                         "          \"A\": {" + 
                         "            \"type\": \"integer\"," + 
                         "            \"$id\": \"#foo\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_nonvalid_ref_id18_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": 1" + 
                         "}";
        String schema2 = "{" + 
                         "  \"not\": {" + 
                         "    \"allOf\": [" + 
                         "      {" + 
                         "        \"$ref\": \"http://localhost:1234/nested.json#foo\"" + 
                         "      }" + 
                         "    ]" + 
                         "  }," + 
                         "  \"definitions\": {" + 
                         "    \"A\": {" + 
                         "      \"definitions\": {" + 
                         "        \"B\": {" + 
                         "          \"type\": \"integer\"," + 
                         "          \"$id\": \"#foo\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"$id\": \"nested.json\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"$id\": \"http://localhost:1234/root\"" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": 1" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"not\": {" + 
                         "          \"allOf\": [" + 
                         "            {" + 
                         "              \"$ref\": \"http://localhost:1234/nested.json#foo\"" + 
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        \"definitions\": {" + 
                         "          \"A\": {" + 
                         "            \"definitions\": {" + 
                         "              \"B\": {" + 
                         "                \"type\": \"integer\"," + 
                         "                \"$id\": \"#foo\"" + 
                         "              }" + 
                         "            }," + 
                         "            \"$id\": \"nested.json\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"$id\": \"http://localhost:1234/root\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_nonvalid_ref_id5_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"slash\": 123" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"not\": {" + 
                         "    \"properties\": {" + 
                         "      \"slash\": {" + 
                         "        \"$ref\": \"#/definitions/slash~1field\"" + 
                         "      }," + 
                         "      \"tilde\": {" + 
                         "        \"$ref\": \"#/definitions/tilde~0field\"" + 
                         "      }," + 
                         "      \"percent\": {" + 
                         "        \"$ref\": \"#/definitions/percent%25field\"" + 
                         "      }" + 
                         "    }" + 
                         "  }," + 
                         "  \"definitions\": {" + 
                         "    \"percent%field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"tilde~field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"slash/field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"slash\": 123" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"not\": {" + 
                         "          \"properties\": {" + 
                         "            \"slash\": {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/slash~1field\"" + 
                         "            }," + 
                         "            \"tilde\": {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/tilde~0field\"" + 
                         "            }," + 
                         "            \"percent\": {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/percent%25field\"" + 
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        \"definitions\": {" + 
                         "          \"percent%field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"tilde~field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"slash/field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_nonvalid_ref_id6_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"tilde\": 123" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"not\": {" + 
                         "    \"properties\": {" + 
                         "      \"slash\": {" + 
                         "        \"$ref\": \"#/definitions/slash~1field\"" + 
                         "      }," + 
                         "      \"tilde\": {" + 
                         "        \"$ref\": \"#/definitions/tilde~0field\"" + 
                         "      }," + 
                         "      \"percent\": {" + 
                         "        \"$ref\": \"#/definitions/percent%25field\"" + 
                         "      }" + 
                         "    }" + 
                         "  }," + 
                         "  \"definitions\": {" + 
                         "    \"percent%field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"tilde~field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"slash/field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"tilde\": 123" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"not\": {" + 
                         "          \"properties\": {" + 
                         "            \"slash\": {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/slash~1field\"" + 
                         "            }," + 
                         "            \"tilde\": {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/tilde~0field\"" + 
                         "            }," + 
                         "            \"percent\": {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/percent%25field\"" + 
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        \"definitions\": {" + 
                         "          \"percent%field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"tilde~field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"slash/field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_nonvalid_ref_id7_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"percent\": 123" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"not\": {" + 
                         "    \"properties\": {" + 
                         "      \"slash\": {" + 
                         "        \"$ref\": \"#/definitions/slash~1field\"" + 
                         "      }," + 
                         "      \"tilde\": {" + 
                         "        \"$ref\": \"#/definitions/tilde~0field\"" + 
                         "      }," + 
                         "      \"percent\": {" + 
                         "        \"$ref\": \"#/definitions/percent%25field\"" + 
                         "      }" + 
                         "    }" + 
                         "  }," + 
                         "  \"definitions\": {" + 
                         "    \"percent%field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"tilde~field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"slash/field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"percent\": 123" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"not\": {" + 
                         "          \"properties\": {" + 
                         "            \"slash\": {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/slash~1field\"" + 
                         "            }," + 
                         "            \"tilde\": {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/tilde~0field\"" + 
                         "            }," + 
                         "            \"percent\": {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/percent%25field\"" + 
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        \"definitions\": {" + 
                         "          \"percent%field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"tilde~field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"slash/field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_nonvalid_ref_id8_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": 5" + 
                         "}";
        String schema2 = "{" + 
                         "  \"not\": {" + 
                         "    \"allOf\": [" + 
                         "      {" + 
                         "        \"$ref\": \"#/definitions/c\"" + 
                         "      }" + 
                         "    ]" + 
                         "  }," + 
                         "  \"definitions\": {" + 
                         "    \"a\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"b\": {" + 
                         "      \"$ref\": \"#/definitions/a\"" + 
                         "    }," + 
                         "    \"c\": {" + 
                         "      \"$ref\": \"#/definitions/b\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": 5" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"not\": {" + 
                         "          \"allOf\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/c\"" + 
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        \"definitions\": {" + 
                         "          \"a\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"b\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/a\"" + 
                         "          }," + 
                         "          \"c\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/b\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_nonvalid_ref_id9_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"foo\": []" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"not\": {" + 
                         "    \"properties\": {" + 
                         "      \"foo\": {" + 
                         "        \"maxItems\": 2," + 
                         "        \"$ref\": \"#/definitions/reffed\"" + 
                         "      }" + 
                         "    }" + 
                         "  }," + 
                         "  \"definitions\": {" + 
                         "    \"reffed\": {" + 
                         "      \"type\": \"array\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"foo\": []" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"not\": {" + 
                         "          \"properties\": {" + 
                         "            \"foo\": {" + 
                         "              \"maxItems\": 2," + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/reffed\"" + 
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        \"definitions\": {" + 
                         "          \"reffed\": {" + 
                         "            \"type\": \"array\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_definitions_id1_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "}";
        String schema2 = "{" + 
                         "  \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_definitions_id1_subschema2_not_1() throws ParseException {
        String schema1 = "{" + 
                         "  \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "}";
        String schema2 = "{" + 
                         "  \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_infinite_loop_detection_id1_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"properties\": {" + 
                         "        \"foo\": {" + 
                         "          \"$ref\": \"#/definitions/int\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"additionalProperties\": {" + 
                         "        \"$ref\": \"#/definitions/int\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"int\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"properties\": {" + 
                         "        \"foo\": {" + 
                         "          \"$ref\": \"#/definitions/int\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"additionalProperties\": {" + 
                         "        \"$ref\": \"#/definitions/int\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"int\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"properties\": {" + 
                         "            \"foo\": {" + 
                         "              \"$ref\": \"#/allOf/0/definitions/int\"" +
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        {" + 
                         "          \"additionalProperties\": {" + 
                         "            \"$ref\": \"#/allOf/0/definitions/int\"" +
                         "          }" + 
                         "        }" + 
                         "      ]," + 
                         "      \"definitions\": {" + 
                         "        \"int\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"properties\": {" + 
                         "              \"foo\": {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/int\"" +
                         "              }" + 
                         "            }" + 
                         "          }," + 
                         "          {" + 
                         "            \"additionalProperties\": {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/int\"" +
                         "            }" + 
                         "          }" + 
                         "        ]," + 
                         "        \"definitions\": {" + 
                         "          \"int\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_infinite_loop_detection_id1_subschema2_not_1() throws ParseException {
        String schema1 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"properties\": {" + 
                         "        \"foo\": {" + 
                         "          \"$ref\": \"#/definitions/int\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"additionalProperties\": {" + 
                         "        \"$ref\": \"#/definitions/int\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"int\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"properties\": {" + 
                         "        \"foo\": {" + 
                         "          \"$ref\": \"#/definitions/int\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"additionalProperties\": {" + 
                         "        \"$ref\": \"#/definitions/int\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"int\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"properties\": {" + 
                         "            \"foo\": {" + 
                         "              \"$ref\": \"#/allOf/0/definitions/int\"" +
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        {" + 
                         "          \"additionalProperties\": {" + 
                         "            \"$ref\": \"#/allOf/0/definitions/int\"" +
                         "          }" + 
                         "        }" + 
                         "      ]," + 
                         "      \"definitions\": {" + 
                         "        \"int\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"properties\": {" + 
                         "              \"foo\": {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/int\"" +
                         "              }" + 
                         "            }" + 
                         "          }," + 
                         "          {" + 
                         "            \"additionalProperties\": {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/int\"" +
                         "            }" + 
                         "          }" + 
                         "        ]," + 
                         "        \"definitions\": {" + 
                         "          \"int\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_items_id6_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"additionalItems\": false," + 
                         "  \"type\": \"array\"," + 
                         "  \"definitions\": {" + 
                         "    \"item\": {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    \"sub-item\": {" + 
                         "      \"type\": \"object\"," + 
                         "      \"required\": [" + 
                         "        \"foo\"" + 
                         "      ]" + 
                         "    }" + 
                         "  }," + 
                         "  \"items\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "{" + 
                         "  \"additionalItems\": false," + 
                         "  \"type\": \"array\"," + 
                         "  \"definitions\": {" + 
                         "    \"item\": {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    \"sub-item\": {" + 
                         "      \"type\": \"object\"," + 
                         "      \"required\": [" + 
                         "        \"foo\"" + 
                         "      ]" + 
                         "    }" + 
                         "  }," + 
                         "  \"items\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"definitions\": {" + 
                         "        \"item\": {" + 
                         "          \"additionalItems\": false," + 
                         "          \"type\": \"array\"," + 
                         "          \"items\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/0/definitions/sub-item\"" +
                         "            }," + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/0/definitions/sub-item\"" +
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        \"sub-item\": {" + 
                         "          \"type\": \"object\"," + 
                         "          \"required\": [" + 
                         "            \"foo\"" + 
                         "          ]" + 
                         "        }" + 
                         "      }," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/item\"" +
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/item\"" +
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/item\"" +
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"additionalItems\": false," + 
                         "        \"type\": \"array\"," + 
                         "        \"definitions\": {" + 
                         "          \"item\": {" + 
                         "            \"additionalItems\": false," + 
                         "            \"type\": \"array\"," + 
                         "            \"items\": [" + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"sub-item\": {" + 
                         "            \"type\": \"object\"," + 
                         "            \"required\": [" + 
                         "              \"foo\"" + 
                         "            ]" + 
                         "          }" + 
                         "        }," + 
                         "        \"items\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }" + 
                         "        ]" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_items_id6_subschema2_not_1() throws ParseException {
        String schema1 = "{" + 
                         "  \"additionalItems\": false," + 
                         "  \"type\": \"array\"," + 
                         "  \"definitions\": {" + 
                         "    \"item\": {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    \"sub-item\": {" + 
                         "      \"type\": \"object\"," + 
                         "      \"required\": [" + 
                         "        \"foo\"" + 
                         "      ]" + 
                         "    }" + 
                         "  }," + 
                         "  \"items\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "{" + 
                         "  \"additionalItems\": false," + 
                         "  \"type\": \"array\"," + 
                         "  \"definitions\": {" + 
                         "    \"item\": {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    \"sub-item\": {" + 
                         "      \"type\": \"object\"," + 
                         "      \"required\": [" + 
                         "        \"foo\"" + 
                         "      ]" + 
                         "    }" + 
                         "  }," + 
                         "  \"items\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"definitions\": {" + 
                         "        \"item\": {" + 
                         "          \"additionalItems\": false," + 
                         "          \"type\": \"array\"," + 
                         "          \"items\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/0/definitions/sub-item\"" +
                         "            }," + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/0/definitions/sub-item\"" +
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        \"sub-item\": {" + 
                         "          \"type\": \"object\"," + 
                         "          \"required\": [" + 
                         "            \"foo\"" + 
                         "          ]" + 
                         "        }" + 
                         "      }," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/item\"" +
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/item\"" +
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/item\"" +
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"additionalItems\": false," + 
                         "        \"type\": \"array\"," + 
                         "        \"definitions\": {" + 
                         "          \"item\": {" + 
                         "            \"additionalItems\": false," + 
                         "            \"type\": \"array\"," + 
                         "            \"items\": [" + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"sub-item\": {" + 
                         "            \"type\": \"object\"," + 
                         "            \"required\": [" + 
                         "              \"foo\"" + 
                         "            ]" + 
                         "          }" + 
                         "        }," + 
                         "        \"items\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }" + 
                         "        ]" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_ref_id12_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"description\": \"tree of nodes\"," + 
                         "  \"type\": \"object\"," + 
                         "  \"definitions\": {" + 
                         "    \"node\": {" + 
                         "      \"description\": \"node\"," + 
                         "      \"type\": \"object\"," + 
                         "      \"properties\": {" + 
                         "        \"subtree\": {" + 
                         "          \"$ref\": \"tree\"" + 
                         "        }," + 
                         "        \"value\": {" + 
                         "          \"type\": \"number\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"required\": [" + 
                         "        \"value\"" + 
                         "      ]," + 
                         "      \"$id\": \"http://localhost:1234/node\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"nodes\": {" + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": {" + 
                         "        \"$ref\": \"node\"" + 
                         "      }" + 
                         "    }," + 
                         "    \"meta\": {" + 
                         "      \"type\": \"string\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"required\": [" + 
                         "    \"meta\"," + 
                         "    \"nodes\"" + 
                         "  ]," + 
                         "  \"$id\": \"http://localhost:1234/tree\"" + 
                         "}";
        String schema2 = "{" + 
                         "  \"description\": \"tree of nodes\"," + 
                         "  \"type\": \"object\"," + 
                         "  \"definitions\": {" + 
                         "    \"node\": {" + 
                         "      \"description\": \"node\"," + 
                         "      \"type\": \"object\"," + 
                         "      \"properties\": {" + 
                         "        \"subtree\": {" + 
                         "          \"$ref\": \"tree\"" + 
                         "        }," + 
                         "        \"value\": {" + 
                         "          \"type\": \"number\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"required\": [" + 
                         "        \"value\"" + 
                         "      ]," + 
                         "      \"$id\": \"http://localhost:1234/node\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"nodes\": {" + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": {" + 
                         "        \"$ref\": \"node\"" + 
                         "      }" + 
                         "    }," + 
                         "    \"meta\": {" + 
                         "      \"type\": \"string\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"required\": [" + 
                         "    \"meta\"," + 
                         "    \"nodes\"" + 
                         "  ]," + 
                         "  \"$id\": \"http://localhost:1234/tree\"" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"description\": \"tree of nodes\"," + 
                         "      \"type\": \"object\"," + 
                         "      \"definitions\": {" + 
                         "        \"node\": {" + 
                         "          \"description\": \"node\"," + 
                         "          \"type\": \"object\"," + 
                         "          \"properties\": {" + 
                         "            \"subtree\": {" + 
                         "              \"$ref\": \"tree\"" + 
                         "            }," + 
                         "            \"value\": {" + 
                         "              \"type\": \"number\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"required\": [" + 
                         "            \"value\"" + 
                         "          ]," + 
                         "          \"$id\": \"http://localhost:1234/node\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"properties\": {" + 
                         "        \"nodes\": {" + 
                         "          \"type\": \"array\"," + 
                         "          \"items\": {" + 
                         "            \"$ref\": \"node\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"meta\": {" + 
                         "          \"type\": \"string\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"required\": [" + 
                         "        \"meta\"," + 
                         "        \"nodes\"" + 
                         "      ]," + 
                         "      \"$id\": \"http://localhost:1234/tree\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"description\": \"tree of nodes\"," + 
                         "        \"type\": \"object\"," + 
                         "        \"definitions\": {" + 
                         "          \"node\": {" + 
                         "            \"description\": \"node\"," + 
                         "            \"type\": \"object\"," + 
                         "            \"properties\": {" + 
                         "              \"subtree\": {" + 
                         "                \"$ref\": \"tree\"" + 
                         "              }," + 
                         "              \"value\": {" + 
                         "                \"type\": \"number\"" + 
                         "              }" + 
                         "            }," + 
                         "            \"required\": [" + 
                         "              \"value\"" + 
                         "            ]," + 
                         "            \"$id\": \"http://localhost:1234/node\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"nodes\": {" + 
                         "            \"type\": \"array\"," + 
                         "            \"items\": {" + 
                         "              \"$ref\": \"node\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"meta\": {" + 
                         "            \"type\": \"string\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"required\": [" + 
                         "          \"meta\"," + 
                         "          \"nodes\"" + 
                         "        ]," + 
                         "        \"$id\": \"http://localhost:1234/tree\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_ref_id12_subschema2_not_1() throws ParseException {
        String schema1 = "{" + 
                         "  \"description\": \"tree of nodes\"," + 
                         "  \"type\": \"object\"," + 
                         "  \"definitions\": {" + 
                         "    \"node\": {" + 
                         "      \"description\": \"node\"," + 
                         "      \"type\": \"object\"," + 
                         "      \"properties\": {" + 
                         "        \"subtree\": {" + 
                         "          \"$ref\": \"tree\"" + 
                         "        }," + 
                         "        \"value\": {" + 
                         "          \"type\": \"number\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"required\": [" + 
                         "        \"value\"" + 
                         "      ]," + 
                         "      \"$id\": \"http://localhost:1234/node\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"nodes\": {" + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": {" + 
                         "        \"$ref\": \"node\"" + 
                         "      }" + 
                         "    }," + 
                         "    \"meta\": {" + 
                         "      \"type\": \"string\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"required\": [" + 
                         "    \"meta\"," + 
                         "    \"nodes\"" + 
                         "  ]," + 
                         "  \"$id\": \"http://localhost:1234/tree\"" + 
                         "}";
        String schema2 = "{" + 
                         "  \"description\": \"tree of nodes\"," + 
                         "  \"type\": \"object\"," + 
                         "  \"definitions\": {" + 
                         "    \"node\": {" + 
                         "      \"description\": \"node\"," + 
                         "      \"type\": \"object\"," + 
                         "      \"properties\": {" + 
                         "        \"subtree\": {" + 
                         "          \"$ref\": \"tree\"" + 
                         "        }," + 
                         "        \"value\": {" + 
                         "          \"type\": \"number\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"required\": [" + 
                         "        \"value\"" + 
                         "      ]," + 
                         "      \"$id\": \"http://localhost:1234/node\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"nodes\": {" + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": {" + 
                         "        \"$ref\": \"node\"" + 
                         "      }" + 
                         "    }," + 
                         "    \"meta\": {" + 
                         "      \"type\": \"string\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"required\": [" + 
                         "    \"meta\"," + 
                         "    \"nodes\"" + 
                         "  ]," + 
                         "  \"$id\": \"http://localhost:1234/tree\"" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"description\": \"tree of nodes\"," + 
                         "      \"type\": \"object\"," + 
                         "      \"definitions\": {" + 
                         "        \"node\": {" + 
                         "          \"description\": \"node\"," + 
                         "          \"type\": \"object\"," + 
                         "          \"properties\": {" + 
                         "            \"subtree\": {" + 
                         "              \"$ref\": \"tree\"" + 
                         "            }," + 
                         "            \"value\": {" + 
                         "              \"type\": \"number\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"required\": [" + 
                         "            \"value\"" + 
                         "          ]," + 
                         "          \"$id\": \"http://localhost:1234/node\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"properties\": {" + 
                         "        \"nodes\": {" + 
                         "          \"type\": \"array\"," + 
                         "          \"items\": {" + 
                         "            \"$ref\": \"node\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"meta\": {" + 
                         "          \"type\": \"string\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"required\": [" + 
                         "        \"meta\"," + 
                         "        \"nodes\"" + 
                         "      ]," + 
                         "      \"$id\": \"http://localhost:1234/tree\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"description\": \"tree of nodes\"," + 
                         "        \"type\": \"object\"," + 
                         "        \"definitions\": {" + 
                         "          \"node\": {" + 
                         "            \"description\": \"node\"," + 
                         "            \"type\": \"object\"," + 
                         "            \"properties\": {" + 
                         "              \"subtree\": {" + 
                         "                \"$ref\": \"tree\"" + 
                         "              }," + 
                         "              \"value\": {" + 
                         "                \"type\": \"number\"" + 
                         "              }" + 
                         "            }," + 
                         "            \"required\": [" + 
                         "              \"value\"" + 
                         "            ]," + 
                         "            \"$id\": \"http://localhost:1234/node\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"nodes\": {" + 
                         "            \"type\": \"array\"," + 
                         "            \"items\": {" + 
                         "              \"$ref\": \"node\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"meta\": {" + 
                         "            \"type\": \"string\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"required\": [" + 
                         "          \"meta\"," + 
                         "          \"nodes\"" + 
                         "        ]," + 
                         "        \"$id\": \"http://localhost:1234/tree\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_ref_id13_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"foo\\\"bar\": {" + 
                         "      \"type\": \"number\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"foo\\\"bar\": {" + 
                         "      \"$ref\": \"#/definitions/foo%22bar\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"foo\\\"bar\": {" + 
                         "      \"type\": \"number\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"foo\\\"bar\": {" + 
                         "      \"$ref\": \"#/definitions/foo%22bar\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"definitions\": {" + 
                         "        \"foo\\\"bar\": {" + 
                         "          \"type\": \"number\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"properties\": {" + 
                         "        \"foo\\\"bar\": {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/foo%22bar\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"foo\\\"bar\": {" + 
                         "            \"type\": \"number\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"foo\\\"bar\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/foo%22bar\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_ref_id13_subschema2_not_1() throws ParseException {
        String schema1 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"foo\\\"bar\": {" + 
                         "      \"type\": \"number\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"foo\\\"bar\": {" + 
                         "      \"$ref\": \"#/definitions/foo%22bar\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"foo\\\"bar\": {" + 
                         "      \"type\": \"number\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"foo\\\"bar\": {" + 
                         "      \"$ref\": \"#/definitions/foo%22bar\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"definitions\": {" + 
                         "        \"foo\\\"bar\": {" + 
                         "          \"type\": \"number\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"properties\": {" + 
                         "        \"foo\\\"bar\": {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/foo%22bar\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"foo\\\"bar\": {" + 
                         "            \"type\": \"number\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"foo\\\"bar\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/foo%22bar\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_ref_id14_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#foo\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"A\": {" + 
                         "      \"type\": \"integer\"," + 
                         "      \"$id\": \"#foo\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#foo\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"A\": {" + 
                         "      \"type\": \"integer\"," + 
                         "      \"$id\": \"#foo\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#foo\"" + 
                         "        }" + 
                         "      ]," + 
                         "      \"definitions\": {" + 
                         "        \"A\": {" + 
                         "          \"type\": \"integer\"," + 
                         "          \"$id\": \"#foo\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#foo\"" + 
                         "          }" + 
                         "        ]," + 
                         "        \"definitions\": {" + 
                         "          \"A\": {" + 
                         "            \"type\": \"integer\"," + 
                         "            \"$id\": \"#foo\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_ref_id14_subschema2_not_1() throws ParseException {
        String schema1 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#foo\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"A\": {" + 
                         "      \"type\": \"integer\"," + 
                         "      \"$id\": \"#foo\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#foo\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"A\": {" + 
                         "      \"type\": \"integer\"," + 
                         "      \"$id\": \"#foo\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#foo\"" + 
                         "        }" + 
                         "      ]," + 
                         "      \"definitions\": {" + 
                         "        \"A\": {" + 
                         "          \"type\": \"integer\"," + 
                         "          \"$id\": \"#foo\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#foo\"" + 
                         "          }" + 
                         "        ]," + 
                         "        \"definitions\": {" + 
                         "          \"A\": {" + 
                         "            \"type\": \"integer\"," + 
                         "            \"$id\": \"#foo\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_ref_id15_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"http://localhost:1234/nested.json#foo\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"A\": {" + 
                         "      \"definitions\": {" + 
                         "        \"B\": {" + 
                         "          \"type\": \"integer\"," + 
                         "          \"$id\": \"#foo\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"$id\": \"nested.json\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"$id\": \"http://localhost:1234/root\"" + 
                         "}";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"http://localhost:1234/nested.json#foo\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"A\": {" + 
                         "      \"definitions\": {" + 
                         "        \"B\": {" + 
                         "          \"type\": \"integer\"," + 
                         "          \"$id\": \"#foo\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"$id\": \"nested.json\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"$id\": \"http://localhost:1234/root\"" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"$ref\": \"http://localhost:1234/nested.json#foo\"" + 
                         "        }" + 
                         "      ]," + 
                         "      \"definitions\": {" + 
                         "        \"A\": {" + 
                         "          \"definitions\": {" + 
                         "            \"B\": {" + 
                         "              \"type\": \"integer\"," + 
                         "              \"$id\": \"#foo\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"$id\": \"nested.json\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"$id\": \"http://localhost:1234/root\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"http://localhost:1234/nested.json#foo\"" + 
                         "          }" + 
                         "        ]," + 
                         "        \"definitions\": {" + 
                         "          \"A\": {" + 
                         "            \"definitions\": {" + 
                         "              \"B\": {" + 
                         "                \"type\": \"integer\"," + 
                         "                \"$id\": \"#foo\"" + 
                         "              }" + 
                         "            }," + 
                         "            \"$id\": \"nested.json\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"$id\": \"http://localhost:1234/root\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_ref_id15_subschema2_not_1() throws ParseException {
        String schema1 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"http://localhost:1234/nested.json#foo\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"A\": {" + 
                         "      \"definitions\": {" + 
                         "        \"B\": {" + 
                         "          \"type\": \"integer\"," + 
                         "          \"$id\": \"#foo\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"$id\": \"nested.json\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"$id\": \"http://localhost:1234/root\"" + 
                         "}";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"http://localhost:1234/nested.json#foo\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"A\": {" + 
                         "      \"definitions\": {" + 
                         "        \"B\": {" + 
                         "          \"type\": \"integer\"," + 
                         "          \"$id\": \"#foo\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"$id\": \"nested.json\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"$id\": \"http://localhost:1234/root\"" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"$ref\": \"http://localhost:1234/nested.json#foo\"" + 
                         "        }" + 
                         "      ]," + 
                         "      \"definitions\": {" + 
                         "        \"A\": {" + 
                         "          \"definitions\": {" + 
                         "            \"B\": {" + 
                         "              \"type\": \"integer\"," + 
                         "              \"$id\": \"#foo\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"$id\": \"nested.json\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"$id\": \"http://localhost:1234/root\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"http://localhost:1234/nested.json#foo\"" + 
                         "          }" + 
                         "        ]," + 
                         "        \"definitions\": {" + 
                         "          \"A\": {" + 
                         "            \"definitions\": {" + 
                         "              \"B\": {" + 
                         "                \"type\": \"integer\"," + 
                         "                \"$id\": \"#foo\"" + 
                         "              }" + 
                         "            }," + 
                         "            \"$id\": \"nested.json\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"$id\": \"http://localhost:1234/root\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_ref_id4_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"percent%field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"tilde~field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"slash/field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"slash\": {" + 
                         "      \"$ref\": \"#/definitions/slash~1field\"" + 
                         "    }," + 
                         "    \"tilde\": {" + 
                         "      \"$ref\": \"#/definitions/tilde~0field\"" + 
                         "    }," + 
                         "    \"percent\": {" + 
                         "      \"$ref\": \"#/definitions/percent%25field\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"percent%field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"tilde~field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"slash/field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"slash\": {" + 
                         "      \"$ref\": \"#/definitions/slash~1field\"" + 
                         "    }," + 
                         "    \"tilde\": {" + 
                         "      \"$ref\": \"#/definitions/tilde~0field\"" + 
                         "    }," + 
                         "    \"percent\": {" + 
                         "      \"$ref\": \"#/definitions/percent%25field\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"definitions\": {" + 
                         "        \"percent%field\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }," + 
                         "        \"tilde~field\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }," + 
                         "        \"slash/field\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"properties\": {" + 
                         "        \"slash\": {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/slash~1field\"" + 
                         "        }," + 
                         "        \"tilde\": {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/tilde~0field\"" +
                         "        }," + 
                         "        \"percent\": {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/percent%25field\"" +
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"percent%field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"tilde~field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"slash/field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"slash\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/slash~1field\"" + 
                         "          }," + 
                         "          \"tilde\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/tilde~0field\"" +
                         "          }," + 
                         "          \"percent\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/percent%25field\"" +
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_ref_id4_subschema2_not_1() throws ParseException {
        String schema1 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"percent%field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"tilde~field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"slash/field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"slash\": {" + 
                         "      \"$ref\": \"#/definitions/slash~1field\"" + 
                         "    }," + 
                         "    \"tilde\": {" + 
                         "      \"$ref\": \"#/definitions/tilde~0field\"" + 
                         "    }," + 
                         "    \"percent\": {" + 
                         "      \"$ref\": \"#/definitions/percent%25field\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"percent%field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"tilde~field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"slash/field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"slash\": {" + 
                         "      \"$ref\": \"#/definitions/slash~1field\"" + 
                         "    }," + 
                         "    \"tilde\": {" + 
                         "      \"$ref\": \"#/definitions/tilde~0field\"" + 
                         "    }," + 
                         "    \"percent\": {" + 
                         "      \"$ref\": \"#/definitions/percent%25field\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"definitions\": {" + 
                         "        \"percent%field\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }," + 
                         "        \"tilde~field\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }," + 
                         "        \"slash/field\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"properties\": {" + 
                         "        \"slash\": {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/slash~1field\"" + 
                         "        }," + 
                         "        \"tilde\": {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/tilde~0field\"" +
                         "        }," + 
                         "        \"percent\": {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/percent%25field\"" +
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"percent%field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"tilde~field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"slash/field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"slash\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/slash~1field\"" + 
                         "          }," + 
                         "          \"tilde\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/tilde~0field\"" +
                         "          }," + 
                         "          \"percent\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/percent%25field\"" +
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_ref_id5_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/c\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"a\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"b\": {" + 
                         "      \"$ref\": \"#/definitions/a\"" + 
                         "    }," + 
                         "    \"c\": {" + 
                         "      \"$ref\": \"#/definitions/b\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/c\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"a\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"b\": {" + 
                         "      \"$ref\": \"#/definitions/a\"" + 
                         "    }," + 
                         "    \"c\": {" + 
                         "      \"$ref\": \"#/definitions/b\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/c\"" + 
                         "        }" + 
                         "      ]," + 
                         "      \"definitions\": {" + 
                         "        \"a\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }," + 
                         "        \"b\": {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/a\"" +
                         "        }," + 
                         "        \"c\": {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/b\"" +
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/c\"" +
                         "          }" + 
                         "        ]," + 
                         "        \"definitions\": {" + 
                         "          \"a\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"b\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/a\"" + 
                         "          }," + 
                         "          \"c\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/b\"" +
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_ref_id5_subschema2_not_1() throws ParseException {
        String schema1 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/c\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"a\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"b\": {" + 
                         "      \"$ref\": \"#/definitions/a\"" + 
                         "    }," + 
                         "    \"c\": {" + 
                         "      \"$ref\": \"#/definitions/b\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/c\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"a\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"b\": {" + 
                         "      \"$ref\": \"#/definitions/a\"" + 
                         "    }," + 
                         "    \"c\": {" + 
                         "      \"$ref\": \"#/definitions/b\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/c\"" + 
                         "        }" + 
                         "      ]," + 
                         "      \"definitions\": {" + 
                         "        \"a\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }," + 
                         "        \"b\": {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/a\"" +
                         "        }," + 
                         "        \"c\": {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/b\"" +
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/c\"" +
                         "          }" + 
                         "        ]," + 
                         "        \"definitions\": {" + 
                         "          \"a\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"b\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/a\"" + 
                         "          }," + 
                         "          \"c\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/b\"" +
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_ref_id6_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"reffed\": {" + 
                         "      \"type\": \"array\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"foo\": {" + 
                         "      \"maxItems\": 2," + 
                         "      \"$ref\": \"#/definitions/reffed\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"reffed\": {" + 
                         "      \"type\": \"array\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"foo\": {" + 
                         "      \"maxItems\": 2," + 
                         "      \"$ref\": \"#/definitions/reffed\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"definitions\": {" + 
                         "        \"reffed\": {" + 
                         "          \"type\": \"array\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"properties\": {" + 
                         "        \"foo\": {" + 
                         "          \"maxItems\": 2," + 
                         "          \"$ref\": \"#/allOf/0/definitions/reffed\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"reffed\": {" + 
                         "            \"type\": \"array\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"foo\": {" + 
                         "            \"maxItems\": 2," + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/reffed\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_ref_id6_subschema2_not_1() throws ParseException {
        String schema1 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"reffed\": {" + 
                         "      \"type\": \"array\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"foo\": {" + 
                         "      \"maxItems\": 2," + 
                         "      \"$ref\": \"#/definitions/reffed\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"reffed\": {" + 
                         "      \"type\": \"array\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"foo\": {" + 
                         "      \"maxItems\": 2," + 
                         "      \"$ref\": \"#/definitions/reffed\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"definitions\": {" + 
                         "        \"reffed\": {" + 
                         "          \"type\": \"array\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"properties\": {" + 
                         "        \"foo\": {" + 
                         "          \"maxItems\": 2," + 
                         "          \"$ref\": \"#/allOf/0/definitions/reffed\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"reffed\": {" + 
                         "            \"type\": \"array\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"foo\": {" + 
                         "            \"maxItems\": 2," + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/reffed\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_ref_id7_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "}";
        String schema2 = "{" + 
                         "  \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_ref_id7_subschema2_not_1() throws ParseException {
        String schema1 = "{" + 
                         "  \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "}";
        String schema2 = "{" + 
                         "  \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_ref_id9_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"is-string\": {" + 
                         "      \"type\": \"string\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"$ref\": {" + 
                         "      \"$ref\": \"#/definitions/is-string\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"is-string\": {" + 
                         "      \"type\": \"string\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"$ref\": {" + 
                         "      \"$ref\": \"#/definitions/is-string\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"definitions\": {" + 
                         "        \"is-string\": {" + 
                         "          \"type\": \"string\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"properties\": {" + 
                         "        \"$ref\": {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/is-string\"" +
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"is-string\": {" + 
                         "            \"type\": \"string\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"$ref\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/is-string\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_reflexive_ref_id9_subschema2_not_1() throws ParseException {
        String schema1 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"is-string\": {" + 
                         "      \"type\": \"string\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"$ref\": {" + 
                         "      \"$ref\": \"#/definitions/is-string\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"is-string\": {" + 
                         "      \"type\": \"string\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"$ref\": {" + 
                         "      \"$ref\": \"#/definitions/is-string\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"definitions\": {" + 
                         "        \"is-string\": {" + 
                         "          \"type\": \"string\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"properties\": {" + 
                         "        \"$ref\": {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/is-string\"" +
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"is-string\": {" + 
                         "            \"type\": \"string\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"$ref\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/is-string\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_unions_items_id5_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"anyOf\": [" + 
                         "    {" + 
                         "      \"const\": [" + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]," + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]," + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"const\": [" + 
                         "        [" + 
                         "          {}" + 
                         "        ]," + 
                         "        [" + 
                         "          {}" + 
                         "        ]" + 
                         "      ]" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "{" + 
                         "  \"additionalItems\": false," + 
                         "  \"type\": \"array\"," + 
                         "  \"definitions\": {" + 
                         "    \"item\": {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    \"sub-item\": {" + 
                         "      \"type\": \"object\"," + 
                         "      \"required\": [" + 
                         "        \"foo\"" + 
                         "      ]" + 
                         "    }" + 
                         "  }," + 
                         "  \"items\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"anyOf\": [" + 
                         "        {" + 
                         "          \"const\": [" + 
                         "            [" + 
                         "              {}," + 
                         "              {}" + 
                         "            ]," + 
                         "            [" + 
                         "              {}," + 
                         "              {}" + 
                         "            ]," + 
                         "            [" + 
                         "              {}," + 
                         "              {}" + 
                         "            ]" + 
                         "          ]" + 
                         "        }," + 
                         "        {" + 
                         "          \"const\": [" + 
                         "            [" + 
                         "              {}" + 
                         "            ]," + 
                         "            [" + 
                         "              {}" + 
                         "            ]" + 
                         "          ]" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"additionalItems\": false," + 
                         "        \"type\": \"array\"," + 
                         "        \"definitions\": {" + 
                         "          \"item\": {" + 
                         "            \"additionalItems\": false," + 
                         "            \"type\": \"array\"," + 
                         "            \"items\": [" + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"sub-item\": {" + 
                         "            \"type\": \"object\"," + 
                         "            \"required\": [" + 
                         "              \"foo\"" + 
                         "            ]" + 
                         "          }" + 
                         "        }," + 
                         "        \"items\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }" + 
                         "        ]" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_unions_ref_id2_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"anyOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"slash\": 123" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"tilde\": 123" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"percent\": 123" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"percent%field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"tilde~field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"slash/field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"slash\": {" + 
                         "      \"$ref\": \"#/definitions/slash~1field\"" + 
                         "    }," + 
                         "    \"tilde\": {" + 
                         "      \"$ref\": \"#/definitions/tilde~0field\"" + 
                         "    }," + 
                         "    \"percent\": {" + 
                         "      \"$ref\": \"#/definitions/percent%25field\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"anyOf\": [" + 
                         "        {" + 
                         "          \"const\": {" + 
                         "            \"slash\": 123" + 
                         "          }" + 
                         "        }," + 
                         "        {" + 
                         "          \"const\": {" + 
                         "            \"tilde\": 123" + 
                         "          }" + 
                         "        }," + 
                         "        {" + 
                         "          \"const\": {" + 
                         "            \"percent\": 123" + 
                         "          }" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"percent%field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"tilde~field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"slash/field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"slash\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/slash~1field\"" + 
                         "          }," + 
                         "          \"tilde\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/tilde~0field\"" + 
                         "          }," + 
                         "          \"percent\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/percent%25field\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_unions_ref_id3_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"anyOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"foo\": []" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"foo\": [" + 
                         "          1," + 
                         "          2," + 
                         "          3" + 
                         "        ]" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"reffed\": {" + 
                         "      \"type\": \"array\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"foo\": {" + 
                         "      \"maxItems\": 2," + 
                         "      \"$ref\": \"#/definitions/reffed\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"anyOf\": [" + 
                         "        {" + 
                         "          \"const\": {" + 
                         "            \"foo\": []" + 
                         "          }" + 
                         "        }," + 
                         "        {" + 
                         "          \"const\": {" + 
                         "            \"foo\": [" + 
                         "              1," + 
                         "              2," + 
                         "              3" + 
                         "            ]" + 
                         "          }" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"reffed\": {" + 
                         "            \"type\": \"array\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"foo\": {" + 
                         "            \"maxItems\": 2," + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/reffed\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_universal_definitions_id1_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"anyOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "true";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"anyOf\": [" + 
                         "        {" + 
                         "          \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"not\": {" + 
                         "            \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "          }" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": true" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_universal_definitions_id1_subschema2_not_1() throws ParseException {
        String schema1 = "true";
        String schema2 = "{" + 
                         "  \"anyOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    true," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"anyOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "          }," + 
                         "          {" + 
                         "            \"not\": {" + 
                         "              \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "            }" + 
                         "          }" + 
                         "        ]" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_universal_infinite_loop_detection_id1_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"anyOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"properties\": {" + 
                         "            \"foo\": {" + 
                         "              \"$ref\": \"#/anyOf/0/definitions/int\"" + 
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        {" + 
                         "          \"additionalProperties\": {" + 
                         "            \"$ref\": \"#/anyOf/0/definitions/int\"" + 
                         "          }" + 
                         "        }" + 
                         "      ]," + 
                         "      \"definitions\": {" + 
                         "        \"int\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"properties\": {" + 
                         "              \"foo\": {" + 
                         "                \"$ref\": \"#/anyOf/1/definitions/int\"" + 
                         "              }" + 
                         "            }" + 
                         "          }," + 
                         "          {" + 
                         "            \"additionalProperties\": {" + 
                         "              \"$ref\": \"#/anyOf/1/definitions/int\"" + 
                         "            }" + 
                         "          }" + 
                         "        ]" + 
                         "      }," + 
                         "      \"definitions\": {" + 
                         "        \"int\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "true";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"anyOf\": [" + 
                         "        {" + 
                         "          \"allOf\": [" + 
                         "            {" + 
                         "              \"properties\": {" + 
                         "                \"foo\": {" + 
                         "                  \"$ref\": \"#/allOf/0/anyOf/0/definitions/int\"" +
                         "                }" + 
                         "              }" + 
                         "            }," + 
                         "            {" + 
                         "              \"additionalProperties\": {" + 
                         "                \"$ref\": \"#/allOf/0/anyOf/0/definitions/int\"" +
                         "              }" + 
                         "            }" + 
                         "          ]," + 
                         "          \"definitions\": {" + 
                         "            \"int\": {" + 
                         "              \"type\": \"integer\"" + 
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        {" + 
                         "          \"not\": {" + 
                         "            \"allOf\": [" + 
                         "              {" + 
                         "                \"properties\": {" + 
                         "                  \"foo\": {" + 
                         "                    \"$ref\": \"#/allOf/0/anyOf/1/definitions/int\"" +
                         "                  }" + 
                         "                }" + 
                         "              }," + 
                         "              {" + 
                         "                \"additionalProperties\": {" + 
                         "                  \"$ref\": \"#/allOf/0/anyOf/1/definitions/int\"" +
                         "                }" + 
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"definitions\": {" + 
                         "            \"int\": {" + 
                         "              \"type\": \"integer\"" + 
                         "            }" + 
                         "          }" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": true" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_universal_infinite_loop_detection_id1_subschema2_not_1() throws ParseException {
        String schema1 = "true";
        String schema2 = "{" + 
                         "  \"anyOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"properties\": {" + 
                         "            \"foo\": {" + 
                         "              \"$ref\": \"#/anyOf/0/definitions/int\"" + 
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        {" + 
                         "          \"additionalProperties\": {" + 
                         "            \"$ref\": \"#/anyOf/0/definitions/int\"" + 
                         "          }" + 
                         "        }" + 
                         "      ]," + 
                         "      \"definitions\": {" + 
                         "        \"int\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"properties\": {" + 
                         "              \"foo\": {" + 
                         "                \"$ref\": \"#/anyOf/1/definitions/int\"" + 
                         "              }" + 
                         "            }" + 
                         "          }," + 
                         "          {" + 
                         "            \"additionalProperties\": {" + 
                         "              \"$ref\": \"#/anyOf/1/definitions/int\"" + 
                         "            }" + 
                         "          }" + 
                         "        ]" + 
                         "      }," + 
                         "      \"definitions\": {" + 
                         "        \"int\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    true," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"anyOf\": [" + 
                         "          {" + 
                         "            \"allOf\": [" + 
                         "              {" + 
                         "                \"properties\": {" + 
                         "                  \"foo\": {" + 
                         "                    \"$ref\": \"#/allOf/1/not/anyOf/0/definitions/int\"" +
                         "                  }" + 
                         "                }" + 
                         "              }," + 
                         "              {" + 
                         "                \"additionalProperties\": {" + 
                         "                  \"$ref\": \"#/allOf/1/not/anyOf/0/definitions/int\"" +
                         "                }" + 
                         "              }" + 
                         "            ]," + 
                         "            \"definitions\": {" + 
                         "              \"int\": {" + 
                         "                \"type\": \"integer\"" + 
                         "              }" + 
                         "            }" + 
                         "          }," + 
                         "          {" + 
                         "            \"not\": {" + 
                         "              \"allOf\": [" + 
                         "                {" + 
                         "                  \"properties\": {" + 
                         "                    \"foo\": {" + 
                         "                      \"$ref\": \"#/allOf/1/not/anyOf/1/definitions/int\"" +
                         "                    }" + 
                         "                  }" + 
                         "                }," + 
                         "                {" + 
                         "                  \"additionalProperties\": {" + 
                         "                    \"$ref\": \"#/allOf/1/not/anyOf/1/definitions/int\"" +
                         "                  }" + 
                         "                }" + 
                         "              ]" + 
                         "            }," + 
                         "            \"definitions\": {" + 
                         "              \"int\": {" + 
                         "                \"type\": \"integer\"" + 
                         "              }" + 
                         "            }" + 
                         "          }" + 
                         "        ]" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_universal_items_id6_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"anyOf\": [" + 
                         "    {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"definitions\": {" + 
                         "        \"item\": {" + 
                         "          \"additionalItems\": false," + 
                         "          \"type\": \"array\"," + 
                         "          \"items\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#/anyOf/0/definitions/sub-item\"" + 
                         "            }," + 
                         "            {" + 
                         "              \"$ref\": \"#/anyOf/0/definitions/sub-item\"" + 
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        \"sub-item\": {" + 
                         "          \"type\": \"object\"," + 
                         "          \"required\": [" + 
                         "            \"foo\"" + 
                         "          ]" + 
                         "        }" + 
                         "      }," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/anyOf/0/definitions/item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/anyOf/0/definitions/item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/anyOf/0/definitions/item\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"items\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/anyOf/1/definitions/item\"" + 
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/anyOf/1/definitions/item\"" + 
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/anyOf/1/definitions/item\"" + 
                         "          }" + 
                         "        ]" + 
                         "      }," + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"definitions\": {" + 
                         "        \"item\": {" + 
                         "          \"additionalItems\": false," + 
                         "          \"type\": \"array\"," + 
                         "          \"items\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#/anyOf/1/definitions/sub-item\"" + 
                         "            }," + 
                         "            {" + 
                         "              \"$ref\": \"#/anyOf/1/definitions/sub-item\"" + 
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        \"sub-item\": {" + 
                         "          \"type\": \"object\"," + 
                         "          \"required\": [" + 
                         "            \"foo\"" + 
                         "          ]" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "true";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"anyOf\": [" + 
                         "        {" + 
                         "          \"additionalItems\": false," + 
                         "          \"type\": \"array\"," + 
                         "          \"definitions\": {" + 
                         "            \"item\": {" + 
                         "              \"additionalItems\": false," + 
                         "              \"type\": \"array\"," + 
                         "              \"items\": [" + 
                         "                {" + 
                         "                  \"$ref\": \"#/allOf/0/anyOf/0/definitions/sub-item\"" +
                         "                }," + 
                         "                {" + 
                         "                  \"$ref\": \"#/allOf/0/anyOf/0/definitions/sub-item\"" +
                         "                }" + 
                         "              ]" + 
                         "            }," + 
                         "            \"sub-item\": {" + 
                         "              \"type\": \"object\"," + 
                         "              \"required\": [" + 
                         "                \"foo\"" + 
                         "              ]" + 
                         "            }" + 
                         "          }," + 
                         "          \"items\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/0/anyOf/0/definitions/item\"" +
                         "            }," + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/0/anyOf/0/definitions/item\"" +
                         "            }," + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/0/anyOf/0/definitions/item\"" +
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        {" + 
                         "          \"not\": {" + 
                         "            \"items\": [" + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/0/anyOf/1/definitions/item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/0/anyOf/1/definitions/item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/0/anyOf/1/definitions/item\"" +
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"additionalItems\": false," + 
                         "          \"type\": \"array\"," + 
                         "          \"definitions\": {" + 
                         "            \"item\": {" + 
                         "              \"additionalItems\": false," + 
                         "              \"type\": \"array\"," + 
                         "              \"items\": [" + 
                         "                {" + 
                         "                  \"$ref\": \"#/allOf/0/anyOf/1/definitions/sub-item\"" +
                         "                }," + 
                         "                {" + 
                         "                  \"$ref\": \"#/allOf/0/anyOf/1/definitions/sub-item\"" +
                         "                }" + 
                         "              ]" + 
                         "            }," + 
                         "            \"sub-item\": {" + 
                         "              \"type\": \"object\"," + 
                         "              \"required\": [" + 
                         "                \"foo\"" + 
                         "              ]" + 
                         "            }" + 
                         "          }" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": true" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_universal_items_id6_subschema2_not_1() throws ParseException {
        String schema1 = "true";
        String schema2 = "{" + 
                         "  \"anyOf\": [" + 
                         "    {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"definitions\": {" + 
                         "        \"item\": {" + 
                         "          \"additionalItems\": false," + 
                         "          \"type\": \"array\"," + 
                         "          \"items\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#/anyOf/0/definitions/sub-item\"" + 
                         "            }," + 
                         "            {" + 
                         "              \"$ref\": \"#/anyOf/0/definitions/sub-item\"" + 
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        \"sub-item\": {" + 
                         "          \"type\": \"object\"," + 
                         "          \"required\": [" + 
                         "            \"foo\"" + 
                         "          ]" + 
                         "        }" + 
                         "      }," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/anyOf/0/definitions/item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/anyOf/0/definitions/item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/anyOf/0/definitions/item\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"items\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/anyOf/1/definitions/item\"" + 
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/anyOf/1/definitions/item\"" + 
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/anyOf/1/definitions/item\"" + 
                         "          }" + 
                         "        ]" + 
                         "      }," + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"definitions\": {" + 
                         "        \"item\": {" + 
                         "          \"additionalItems\": false," + 
                         "          \"type\": \"array\"," + 
                         "          \"items\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#/anyOf/1/definitions/sub-item\"" + 
                         "            }," + 
                         "            {" + 
                         "              \"$ref\": \"#/anyOf/1/definitions/sub-item\"" + 
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        \"sub-item\": {" + 
                         "          \"type\": \"object\"," + 
                         "          \"required\": [" + 
                         "            \"foo\"" + 
                         "          ]" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    true," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"anyOf\": [" + 
                         "          {" + 
                         "            \"additionalItems\": false," + 
                         "            \"type\": \"array\"," + 
                         "            \"definitions\": {" + 
                         "              \"item\": {" + 
                         "                \"additionalItems\": false," + 
                         "                \"type\": \"array\"," + 
                         "                \"items\": [" + 
                         "                  {" + 
                         "                    \"$ref\": \"#/allOf/1/not/anyOf/0/definitions/sub-item\"" +
                         "                  }," + 
                         "                  {" + 
                         "                    \"$ref\": \"#/allOf/1/not/anyOf/0/definitions/sub-item\"" +
                         "                  }" + 
                         "                ]" + 
                         "              }," + 
                         "              \"sub-item\": {" + 
                         "                \"type\": \"object\"," + 
                         "                \"required\": [" + 
                         "                  \"foo\"" + 
                         "                ]" + 
                         "              }" + 
                         "            }," + 
                         "            \"items\": [" + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/anyOf/0/definitions/item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/anyOf/0/definitions/item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/anyOf/0/definitions/item\"" +
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          {" + 
                         "            \"not\": {" + 
                         "              \"items\": [" + 
                         "                {" + 
                         "                  \"$ref\": \"#/allOf/1/not/anyOf/1/definitions/item\"" +
                         "                }," + 
                         "                {" + 
                         "                  \"$ref\": \"#/allOf/1/not/anyOf/1/definitions/item\"" +
                         "                }," + 
                         "                {" + 
                         "                  \"$ref\": \"#/allOf/1/not/anyOf/1/definitions/item\"" +
                         "                }" + 
                         "              ]" + 
                         "            }," + 
                         "            \"additionalItems\": false," + 
                         "            \"type\": \"array\"," + 
                         "            \"definitions\": {" + 
                         "              \"item\": {" + 
                         "                \"additionalItems\": false," + 
                         "                \"type\": \"array\"," + 
                         "                \"items\": [" + 
                         "                  {" + 
                         "                    \"$ref\": \"#/allOf/1/not/anyOf/1/definitions/sub-item\"" +
                         "                  }," + 
                         "                  {" + 
                         "                    \"$ref\": \"#/allOf/1/not/anyOf/1/definitions/sub-item\"" +
                         "                  }" + 
                         "                ]" + 
                         "              }," + 
                         "              \"sub-item\": {" + 
                         "                \"type\": \"object\"," + 
                         "                \"required\": [" + 
                         "                  \"foo\"" + 
                         "                ]" + 
                         "              }" + 
                         "            }" + 
                         "          }" + 
                         "        ]" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_universal_ref_id12_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"anyOf\": [" + 
                         "    {" + 
                         "      \"description\": \"tree of nodes\"," + 
                         "      \"type\": \"object\"," + 
                         "      \"definitions\": {" + 
                         "        \"node\": {" + 
                         "          \"description\": \"node\"," + 
                         "          \"type\": \"object\"," + 
                         "          \"properties\": {" + 
                         "            \"subtree\": {" + 
                         "              \"$ref\": \"tree\"" + 
                         "            }," + 
                         "            \"value\": {" + 
                         "              \"type\": \"number\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"required\": [" + 
                         "            \"value\"" + 
                         "          ]," + 
                         "          \"$id\": \"http://localhost:1234/node\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"properties\": {" + 
                         "        \"nodes\": {" + 
                         "          \"type\": \"array\"," + 
                         "          \"items\": {" + 
                         "            \"$ref\": \"node\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"meta\": {" + 
                         "          \"type\": \"string\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"required\": [" + 
                         "        \"meta\"," + 
                         "        \"nodes\"" + 
                         "      ]," + 
                         "      \"$id\": \"http://localhost:1234/tree\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"properties\": {" + 
                         "          \"nodes\": {" + 
                         "            \"type\": \"array\"," + 
                         "            \"items\": {" + 
                         "              \"$ref\": \"node1\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"meta\": {" + 
                         "            \"type\": \"string\"" + 
                         "          }" + 
                         "        }" + 
                         "      }," + 
                         "      \"description\": \"tree of nodes\"," + 
                         "      \"type\": \"object\"," + 
                         "      \"definitions\": {" + 
                         "        \"node\": {" + 
                         "          \"description\": \"node\"," + 
                         "          \"type\": \"object\"," + 
                         "          \"properties\": {" + 
                         "            \"subtree\": {" + 
                         "              \"$ref\": \"tree1\"" + 
                         "            }," + 
                         "            \"value\": {" + 
                         "              \"type\": \"number\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"required\": [" + 
                         "            \"value\"" + 
                         "          ]," + 
                         "          \"$id\": \"http://localhost:1234/node1\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"required\": [" + 
                         "        \"meta\"," + 
                         "        \"nodes\"" + 
                         "      ]," + 
                         "      \"$id\": \"http://localhost:1234/tree1\"" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "true";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"anyOf\": [" + 
                         "        {" + 
                         "          \"description\": \"tree of nodes\"," + 
                         "          \"type\": \"object\"," + 
                         "          \"definitions\": {" + 
                         "            \"node\": {" + 
                         "              \"description\": \"node\"," + 
                         "              \"type\": \"object\"," + 
                         "              \"properties\": {" + 
                         "                \"subtree\": {" + 
                         "                  \"$ref\": \"tree\"" + 
                         "                }," + 
                         "                \"value\": {" + 
                         "                  \"type\": \"number\"" + 
                         "                }" + 
                         "              }," + 
                         "              \"required\": [" + 
                         "                \"value\"" + 
                         "              ]," + 
                         "              \"$id\": \"http://localhost:1234/node\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"properties\": {" + 
                         "            \"nodes\": {" + 
                         "              \"type\": \"array\"," + 
                         "              \"items\": {" + 
                         "                \"$ref\": \"node\"" + 
                         "              }" + 
                         "            }," + 
                         "            \"meta\": {" + 
                         "              \"type\": \"string\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"required\": [" + 
                         "            \"meta\"," + 
                         "            \"nodes\"" + 
                         "          ]," + 
                         "          \"$id\": \"http://localhost:1234/tree\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"not\": {" + 
                         "            \"properties\": {" + 
                         "              \"nodes\": {" + 
                         "                \"type\": \"array\"," + 
                         "                \"items\": {" + 
                         "                  \"$ref\": \"node1\"" + 
                         "                }" + 
                         "              }," + 
                         "              \"meta\": {" + 
                         "                \"type\": \"string\"" + 
                         "              }" + 
                         "            }" + 
                         "          }," + 
                         "          \"description\": \"tree of nodes\"," + 
                         "          \"type\": \"object\"," + 
                         "          \"definitions\": {" + 
                         "            \"node\": {" + 
                         "              \"description\": \"node\"," + 
                         "              \"type\": \"object\"," + 
                         "              \"properties\": {" + 
                         "                \"subtree\": {" + 
                         "                  \"$ref\": \"tree1\"" + 
                         "                }," + 
                         "                \"value\": {" + 
                         "                  \"type\": \"number\"" + 
                         "                }" + 
                         "              }," + 
                         "              \"required\": [" + 
                         "                \"value\"" + 
                         "              ]," + 
                         "              \"$id\": \"http://localhost:1234/node1\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"required\": [" + 
                         "            \"meta\"," + 
                         "            \"nodes\"" + 
                         "          ]," + 
                         "          \"$id\": \"http://localhost:1234/tree1\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": true" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_universal_ref_id13_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"anyOf\": [" + 
                         "    {" + 
                         "      \"definitions\": {" + 
                         "        \"foo\\\"bar\": {" + 
                         "          \"type\": \"number\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"properties\": {" + 
                         "        \"foo\\\"bar\": {" + 
                         "          \"$ref\": \"#/anyOf/0/definitions/foo%22bar\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"properties\": {" + 
                         "          \"foo\\\"bar\": {" + 
                         "            \"$ref\": \"#/anyOf/1/definitions/foo%22bar\"" + 
                         "          }" + 
                         "        }" + 
                         "      }," + 
                         "      \"definitions\": {" + 
                         "        \"foo\\\"bar\": {" + 
                         "          \"type\": \"number\"" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "true";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"anyOf\": [" + 
                         "        {" + 
                         "          \"definitions\": {" + 
                         "            \"foo\\\"bar\": {" + 
                         "              \"type\": \"number\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"properties\": {" + 
                         "            \"foo\\\"bar\": {" + 
                         "              \"$ref\": \"#/allOf/0/anyOf/0/definitions/foo%22bar\"" + 
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        {" + 
                         "          \"not\": {" + 
                         "            \"properties\": {" + 
                         "              \"foo\\\"bar\": {" + 
                         "                \"$ref\": \"#/allOf/0/anyOf/1/definitions/foo%22bar\"" + 
                         "              }" + 
                         "            }" + 
                         "          }," + 
                         "          \"definitions\": {" + 
                         "            \"foo\\\"bar\": {" + 
                         "              \"type\": \"number\"" + 
                         "            }" + 
                         "          }" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": true" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_universal_ref_id15_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"anyOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"$ref\": \"http://localhost:1234/nested.json#foo\"" + 
                         "        }" + 
                         "      ]," + 
                         "      \"definitions\": {" + 
                         "        \"A\": {" + 
                         "          \"definitions\": {" + 
                         "            \"B\": {" + 
                         "              \"type\": \"integer\"," + 
                         "              \"$id\": \"#foo\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"$id\": \"nested.json\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"$id\": \"http://localhost:1234/root\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"http://localhost:1234/nested1.json#foo1\"" + 
                         "          }" + 
                         "        ]" + 
                         "      }," + 
                         "      \"definitions\": {" + 
                         "        \"A\": {" + 
                         "          \"definitions\": {" + 
                         "            \"B\": {" + 
                         "              \"type\": \"integer\"," + 
                         "              \"$id\": \"#foo1\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"$id\": \"nested1.json\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"$id\": \"http://localhost:1234/root1\"" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "true";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"anyOf\": [" + 
                         "        {" + 
                         "          \"allOf\": [" + 
                         "            {" + 
                         "              \"$ref\": \"http://localhost:1234/nested.json#foo\"" + 
                         "            }" + 
                         "          ]," + 
                         "          \"definitions\": {" + 
                         "            \"A\": {" + 
                         "              \"definitions\": {" + 
                         "                \"B\": {" + 
                         "                  \"type\": \"integer\"," + 
                         "                  \"$id\": \"#foo\"" + 
                         "                }" + 
                         "              }," + 
                         "              \"$id\": \"nested.json\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"$id\": \"http://localhost:1234/root\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"not\": {" + 
                         "            \"allOf\": [" + 
                         "              {" + 
                         "                \"$ref\": \"http://localhost:1234/nested1.json#foo1\"" + 
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"definitions\": {" + 
                         "            \"A\": {" + 
                         "              \"definitions\": {" + 
                         "                \"B\": {" + 
                         "                  \"type\": \"integer\"," + 
                         "                  \"$id\": \"#foo1\"" + 
                         "                }" + 
                         "              }," + 
                         "              \"$id\": \"nested1.json\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"$id\": \"http://localhost:1234/root1\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": true" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_universal_ref_id4_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"anyOf\": [" + 
                         "    {" + 
                         "      \"definitions\": {" + 
                         "        \"percent%field\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }," + 
                         "        \"tilde~field\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }," + 
                         "        \"slash/field\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"properties\": {" + 
                         "        \"slash\": {" + 
                         "          \"$ref\": \"#/anyOf/0/definitions/slash~1field\"" + 
                         "        }," + 
                         "        \"tilde\": {" + 
                         "          \"$ref\": \"#/anyOf/0/definitions/tilde~0field\"" + 
                         "        }," + 
                         "        \"percent\": {" + 
                         "          \"$ref\": \"#/anyOf/0/definitions/percent%25field\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"properties\": {" + 
                         "          \"slash\": {" + 
                         "            \"$ref\": \"#/anyOf/1/definitions/slash~1field\"" + 
                         "          }," + 
                         "          \"tilde\": {" + 
                         "            \"$ref\": \"#/anyOf/1/definitions/tilde~0field\"" + 
                         "          }," + 
                         "          \"percent\": {" + 
                         "            \"$ref\": \"#/anyOf/1/definitions/percent%25field\"" + 
                         "          }" + 
                         "        }" + 
                         "      }," + 
                         "      \"definitions\": {" + 
                         "        \"percent%field\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }," + 
                         "        \"tilde~field\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }," + 
                         "        \"slash/field\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "true";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"anyOf\": [" + 
                         "        {" + 
                         "          \"definitions\": {" + 
                         "            \"percent%field\": {" + 
                         "              \"type\": \"integer\"" + 
                         "            }," + 
                         "            \"tilde~field\": {" + 
                         "              \"type\": \"integer\"" + 
                         "            }," + 
                         "            \"slash/field\": {" + 
                         "              \"type\": \"integer\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"properties\": {" + 
                         "            \"slash\": {" + 
                         "              \"$ref\": \"#/allOf/0/anyOf/0/definitions/slash~1field\"" + 
                         "            }," + 
                         "            \"tilde\": {" + 
                         "              \"$ref\": \"#/allOf/0/anyOf/0/definitions/tilde~0field\"" + 
                         "            }," + 
                         "            \"percent\": {" + 
                         "              \"$ref\": \"#/allOf/0/anyOf/0/definitions/percent%25field\"" + 
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        {" + 
                         "          \"not\": {" + 
                         "            \"properties\": {" + 
                         "              \"slash\": {" + 
                         "                \"$ref\": \"#/allOf/0/anyOf/1/definitions/slash~1field\"" + 
                         "              }," + 
                         "              \"tilde\": {" + 
                         "                \"$ref\": \"#/allOf/0/anyOf/1/definitions/tilde~0field\"" +
                         "              }," + 
                         "              \"percent\": {" + 
                         "                \"$ref\": \"#/allOf/0/anyOf/1/definitions/percent%25field\"" +
                         "              }" + 
                         "            }" + 
                         "          }," + 
                         "          \"definitions\": {" + 
                         "            \"percent%field\": {" + 
                         "              \"type\": \"integer\"" + 
                         "            }," + 
                         "            \"tilde~field\": {" + 
                         "              \"type\": \"integer\"" + 
                         "            }," + 
                         "            \"slash/field\": {" + 
                         "              \"type\": \"integer\"" + 
                         "            }" + 
                         "          }" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": true" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_universal_ref_id5_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"anyOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/anyOf/0/definitions/c\"" + 
                         "        }" + 
                         "      ]," + 
                         "      \"definitions\": {" + 
                         "        \"a\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }," + 
                         "        \"b\": {" + 
                         "          \"$ref\": \"#/anyOf/0/definitions/a\"" + 
                         "        }," + 
                         "        \"c\": {" + 
                         "          \"$ref\": \"#/anyOf/0/definitions/b\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/anyOf/1/definitions/c\"" + 
                         "          }" + 
                         "        ]" + 
                         "      }," + 
                         "      \"definitions\": {" + 
                         "        \"a\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }," + 
                         "        \"b\": {" + 
                         "          \"$ref\": \"#/anyOf/1/definitions/a\"" + 
                         "        }," + 
                         "        \"c\": {" + 
                         "          \"$ref\": \"#/anyOf/1/definitions/b\"" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "true";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"anyOf\": [" + 
                         "        {" + 
                         "          \"allOf\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/0/anyOf/0/definitions/c\"" + 
                         "            }" + 
                         "          ]," + 
                         "          \"definitions\": {" + 
                         "            \"a\": {" + 
                         "              \"type\": \"integer\"" + 
                         "            }," + 
                         "            \"b\": {" + 
                         "              \"$ref\": \"#/allOf/0/anyOf/0/definitions/a\"" +
                         "            }," + 
                         "            \"c\": {" + 
                         "              \"$ref\": \"#/allOf/0/anyOf/0/definitions/b\"" +
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        {" + 
                         "          \"not\": {" + 
                         "            \"allOf\": [" + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/0/anyOf/1/definitions/c\"" + 
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"definitions\": {" + 
                         "            \"a\": {" + 
                         "              \"type\": \"integer\"" + 
                         "            }," + 
                         "            \"b\": {" + 
                         "              \"$ref\": \"#/allOf/0/anyOf/1/definitions/a\"" + 
                         "            }," + 
                         "            \"c\": {" + 
                         "              \"$ref\": \"#/allOf/0/anyOf/1/definitions/b\"" + 
                         "            }" + 
                         "          }" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": true" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_universal_ref_id7_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"anyOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "true";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"anyOf\": [" + 
                         "        {" + 
                         "          \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"not\": {" + 
                         "            \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "          }" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": true" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_unsatisfiable_definitions_id1_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "false";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"not\": {" + 
                         "            \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "          }" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": false" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_unsatisfiable_definitions_id1_subschema2_not_1() throws ParseException {
        String schema1 = "false";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    false," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "          }," + 
                         "          {" + 
                         "            \"not\": {" + 
                         "              \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "            }" + 
                         "          }" + 
                         "        ]" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_unsatisfiable_infinite_loop_detection_id1_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"properties\": {" + 
                         "            \"foo\": {" + 
                         "              \"$ref\": \"#/allOf/0/definitions/int\"" + 
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        {" + 
                         "          \"additionalProperties\": {" + 
                         "            \"$ref\": \"#/allOf/0/definitions/int\"" + 
                         "          }" + 
                         "        }" + 
                         "      ]," + 
                         "      \"definitions\": {" + 
                         "        \"int\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"properties\": {" + 
                         "              \"foo\": {" + 
                         "                \"$ref\": \"#/allOf/1/definitions/int\"" + 
                         "              }" + 
                         "            }" + 
                         "          }," + 
                         "          {" + 
                         "            \"additionalProperties\": {" + 
                         "              \"$ref\": \"#/allOf/1/definitions/int\"" + 
                         "            }" + 
                         "          }" + 
                         "        ]" + 
                         "      }," + 
                         "      \"definitions\": {" + 
                         "        \"int\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "false";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"allOf\": [" + 
                         "            {" + 
                         "              \"properties\": {" + 
                         "                \"foo\": {" + 
                         "                  \"$ref\": \"#/allOf/0/allOf/0/definitions/int\"" +
                         "                }" + 
                         "              }" + 
                         "            }," + 
                         "            {" + 
                         "              \"additionalProperties\": {" + 
                         "                \"$ref\": \"#/allOf/0/allOf/0/definitions/int\"" +
                         "              }" + 
                         "            }" + 
                         "          ]," + 
                         "          \"definitions\": {" + 
                         "            \"int\": {" + 
                         "              \"type\": \"integer\"" + 
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        {" + 
                         "          \"not\": {" + 
                         "            \"allOf\": [" + 
                         "              {" + 
                         "                \"properties\": {" + 
                         "                  \"foo\": {" + 
                         "                    \"$ref\": \"#/allOf/0/allOf/1/definitions/int\"" +
                         "                  }" + 
                         "                }" + 
                         "              }," + 
                         "              {" + 
                         "                \"additionalProperties\": {" + 
                         "                  \"$ref\": \"#/allOf/0/allOf/1/definitions/int\"" +
                         "                }" + 
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"definitions\": {" + 
                         "            \"int\": {" + 
                         "              \"type\": \"integer\"" + 
                         "            }" + 
                         "          }" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": false" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_unsatisfiable_infinite_loop_detection_id1_subschema2_not_1() throws ParseException {
        String schema1 = "false";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"properties\": {" + 
                         "            \"foo\": {" + 
                         "              \"$ref\": \"#/allOf/0/definitions/int\"" + 
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        {" + 
                         "          \"additionalProperties\": {" + 
                         "            \"$ref\": \"#/allOf/0/definitions/int\"" + 
                         "          }" + 
                         "        }" + 
                         "      ]," + 
                         "      \"definitions\": {" + 
                         "        \"int\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"properties\": {" + 
                         "              \"foo\": {" + 
                         "                \"$ref\": \"#/allOf/1/definitions/int\"" + 
                         "              }" + 
                         "            }" + 
                         "          }," + 
                         "          {" + 
                         "            \"additionalProperties\": {" + 
                         "              \"$ref\": \"#/allOf/1/definitions/int\"" + 
                         "            }" + 
                         "          }" + 
                         "        ]" + 
                         "      }," + 
                         "      \"definitions\": {" + 
                         "        \"int\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    false," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"allOf\": [" + 
                         "              {" + 
                         "                \"properties\": {" + 
                         "                  \"foo\": {" + 
                         "                    \"$ref\": \"#/allOf/1/not/allOf/0/definitions/int\"" +
                         "                  }" + 
                         "                }" + 
                         "              }," + 
                         "              {" + 
                         "                \"additionalProperties\": {" + 
                         "                  \"$ref\": \"#/allOf/1/not/allOf/0/definitions/int\"" +
                         "                }" + 
                         "              }" + 
                         "            ]," + 
                         "            \"definitions\": {" + 
                         "              \"int\": {" + 
                         "                \"type\": \"integer\"" + 
                         "              }" + 
                         "            }" + 
                         "          }," + 
                         "          {" + 
                         "            \"not\": {" + 
                         "              \"allOf\": [" + 
                         "                {" + 
                         "                  \"properties\": {" + 
                         "                    \"foo\": {" + 
                         "                      \"$ref\": \"#/allOf/1/not/allOf/1/definitions/int\"" +
                         "                    }" + 
                         "                  }" + 
                         "                }," + 
                         "                {" + 
                         "                  \"additionalProperties\": {" + 
                         "                    \"$ref\": \"#/allOf/1/not/allOf/1/definitions/int\"" +
                         "                  }" + 
                         "                }" + 
                         "              ]" + 
                         "            }," + 
                         "            \"definitions\": {" + 
                         "              \"int\": {" + 
                         "                \"type\": \"integer\"" + 
                         "              }" + 
                         "            }" + 
                         "          }" + 
                         "        ]" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_unsatisfiable_items_id6_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"definitions\": {" + 
                         "        \"item\": {" + 
                         "          \"additionalItems\": false," + 
                         "          \"type\": \"array\"," + 
                         "          \"items\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/0/definitions/sub-item\"" + 
                         "            }," + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/0/definitions/sub-item\"" + 
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        \"sub-item\": {" + 
                         "          \"type\": \"object\"," + 
                         "          \"required\": [" + 
                         "            \"foo\"" + 
                         "          ]" + 
                         "        }" + 
                         "      }," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/item\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"items\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/definitions/item\"" + 
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/definitions/item\"" + 
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/definitions/item\"" + 
                         "          }" + 
                         "        ]" + 
                         "      }," + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"definitions\": {" + 
                         "        \"item\": {" + 
                         "          \"additionalItems\": false," + 
                         "          \"type\": \"array\"," + 
                         "          \"items\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/1/definitions/sub-item\"" + 
                         "            }," + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/1/definitions/sub-item\"" + 
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        \"sub-item\": {" + 
                         "          \"type\": \"object\"," + 
                         "          \"required\": [" + 
                         "            \"foo\"" + 
                         "          ]" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "false";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"additionalItems\": false," + 
                         "          \"type\": \"array\"," + 
                         "          \"definitions\": {" + 
                         "            \"item\": {" + 
                         "              \"additionalItems\": false," + 
                         "              \"type\": \"array\"," + 
                         "              \"items\": [" + 
                         "                {" + 
                         "                  \"$ref\": \"#/allOf/0/allOf/0/definitions/sub-item\"" +
                         "                }," + 
                         "                {" + 
                         "                  \"$ref\": \"#/allOf/0/allOf/0/definitions/sub-item\"" +
                         "                }" + 
                         "              ]" + 
                         "            }," + 
                         "            \"sub-item\": {" + 
                         "              \"type\": \"object\"," + 
                         "              \"required\": [" + 
                         "                \"foo\"" + 
                         "              ]" + 
                         "            }" + 
                         "          }," + 
                         "          \"items\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/0/allOf/0/definitions/item\"" +
                         "            }," + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/0/allOf/0/definitions/item\"" +
                         "            }," + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/0/allOf/0/definitions/item\"" +
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        {" + 
                         "          \"not\": {" + 
                         "            \"items\": [" + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/0/allOf/1/definitions/item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/0/allOf/1/definitions/item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/0/allOf/1/definitions/item\"" +
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"additionalItems\": false," + 
                         "          \"type\": \"array\"," + 
                         "          \"definitions\": {" + 
                         "            \"item\": {" + 
                         "              \"additionalItems\": false," + 
                         "              \"type\": \"array\"," + 
                         "              \"items\": [" + 
                         "                {" + 
                         "                  \"$ref\": \"#/allOf/0/allOf/1/definitions/sub-item\"" +
                         "                }," + 
                         "                {" + 
                         "                  \"$ref\": \"#/allOf/0/allOf/1/definitions/sub-item\"" +
                         "                }" + 
                         "              ]" + 
                         "            }," + 
                         "            \"sub-item\": {" + 
                         "              \"type\": \"object\"," + 
                         "              \"required\": [" + 
                         "                \"foo\"" + 
                         "              ]" + 
                         "            }" + 
                         "          }" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": false" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_unsatisfiable_items_id6_subschema2_not_1() throws ParseException {
        String schema1 = "false";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"definitions\": {" + 
                         "        \"item\": {" + 
                         "          \"additionalItems\": false," + 
                         "          \"type\": \"array\"," + 
                         "          \"items\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/0/definitions/sub-item\"" + 
                         "            }," + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/0/definitions/sub-item\"" + 
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        \"sub-item\": {" + 
                         "          \"type\": \"object\"," + 
                         "          \"required\": [" + 
                         "            \"foo\"" + 
                         "          ]" + 
                         "        }" + 
                         "      }," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/item\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"items\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/definitions/item\"" + 
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/definitions/item\"" + 
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/definitions/item\"" + 
                         "          }" + 
                         "        ]" + 
                         "      }," + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"definitions\": {" + 
                         "        \"item\": {" + 
                         "          \"additionalItems\": false," + 
                         "          \"type\": \"array\"," + 
                         "          \"items\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/1/definitions/sub-item\"" + 
                         "            }," + 
                         "            {" + 
                         "              \"$ref\": \"#/allOf/1/definitions/sub-item\"" + 
                         "            }" + 
                         "          ]" + 
                         "        }," + 
                         "        \"sub-item\": {" + 
                         "          \"type\": \"object\"," + 
                         "          \"required\": [" + 
                         "            \"foo\"" + 
                         "          ]" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    false," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"additionalItems\": false," + 
                         "            \"type\": \"array\"," + 
                         "            \"definitions\": {" + 
                         "              \"item\": {" + 
                         "                \"additionalItems\": false," + 
                         "                \"type\": \"array\"," + 
                         "                \"items\": [" + 
                         "                  {" + 
                         "                    \"$ref\": \"#/allOf/1/not/allOf/0/definitions/sub-item\"" +
                         "                  }," + 
                         "                  {" + 
                         "                    \"$ref\": \"#/allOf/1/not/allOf/0/definitions/sub-item\"" +
                         "                  }" + 
                         "                ]" + 
                         "              }," + 
                         "              \"sub-item\": {" + 
                         "                \"type\": \"object\"," + 
                         "                \"required\": [" + 
                         "                  \"foo\"" + 
                         "                ]" + 
                         "              }" + 
                         "            }," + 
                         "            \"items\": [" + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/allOf/0/definitions/item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/allOf/0/definitions/item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/allOf/0/definitions/item\"" +
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          {" + 
                         "            \"not\": {" + 
                         "              \"items\": [" + 
                         "                {" + 
                         "                  \"$ref\": \"#/allOf/1/not/allOf/1/definitions/item\"" +
                         "                }," + 
                         "                {" + 
                         "                  \"$ref\": \"#/allOf/1/not/allOf/1/definitions/item\"" +
                         "                }," + 
                         "                {" + 
                         "                  \"$ref\": \"#/allOf/1/not/allOf/1/definitions/item\"" +
                         "                }" + 
                         "              ]" + 
                         "            }," + 
                         "            \"additionalItems\": false," + 
                         "            \"type\": \"array\"," + 
                         "            \"definitions\": {" + 
                         "              \"item\": {" + 
                         "                \"additionalItems\": false," + 
                         "                \"type\": \"array\"," + 
                         "                \"items\": [" + 
                         "                  {" + 
                         "                    \"$ref\": \"#/allOf/1/not/allOf/1/definitions/sub-item\"" +
                         "                  }," + 
                         "                  {" + 
                         "                    \"$ref\": \"#/allOf/1/not/allOf/1/definitions/sub-item\"" +
                         "                  }" + 
                         "                ]" + 
                         "              }," + 
                         "              \"sub-item\": {" + 
                         "                \"type\": \"object\"," + 
                         "                \"required\": [" + 
                         "                  \"foo\"" + 
                         "                ]" + 
                         "              }" + 
                         "            }" + 
                         "          }" + 
                         "        ]" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_unsatisfiable_ref_id12_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"description\": \"tree of nodes\"," + 
                         "      \"type\": \"object\"," + 
                         "      \"definitions\": {" + 
                         "        \"node\": {" + 
                         "          \"description\": \"node\"," + 
                         "          \"type\": \"object\"," + 
                         "          \"properties\": {" + 
                         "            \"subtree\": {" + 
                         "              \"$ref\": \"tree\"" + 
                         "            }," + 
                         "            \"value\": {" + 
                         "              \"type\": \"number\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"required\": [" + 
                         "            \"value\"" + 
                         "          ]," + 
                         "          \"$id\": \"http://localhost:1234/node\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"properties\": {" + 
                         "        \"nodes\": {" + 
                         "          \"type\": \"array\"," + 
                         "          \"items\": {" + 
                         "            \"$ref\": \"node\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"meta\": {" + 
                         "          \"type\": \"string\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"required\": [" + 
                         "        \"meta\"," + 
                         "        \"nodes\"" + 
                         "      ]," + 
                         "      \"$id\": \"http://localhost:1234/tree\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"properties\": {" + 
                         "          \"nodes\": {" + 
                         "            \"type\": \"array\"," + 
                         "            \"items\": {" + 
                         "              \"$ref\": \"node1\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"meta\": {" + 
                         "            \"type\": \"string\"" + 
                         "          }" + 
                         "        }" + 
                         "      }," + 
                         "      \"description\": \"tree of nodes\"," + 
                         "      \"type\": \"object\"," + 
                         "      \"definitions\": {" + 
                         "        \"node\": {" + 
                         "          \"description\": \"node\"," + 
                         "          \"type\": \"object\"," + 
                         "          \"properties\": {" + 
                         "            \"subtree\": {" + 
                         "              \"$ref\": \"tree1\"" + 
                         "            }," + 
                         "            \"value\": {" + 
                         "              \"type\": \"number\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"required\": [" + 
                         "            \"value\"" + 
                         "          ]," + 
                         "          \"$id\": \"http://localhost:1234/node1\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"required\": [" + 
                         "        \"meta\"," + 
                         "        \"nodes\"" + 
                         "      ]," + 
                         "      \"$id\": \"http://localhost:1234/tree1\"" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "false";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"description\": \"tree of nodes\"," + 
                         "          \"type\": \"object\"," + 
                         "          \"definitions\": {" + 
                         "            \"node\": {" + 
                         "              \"description\": \"node\"," + 
                         "              \"type\": \"object\"," + 
                         "              \"properties\": {" + 
                         "                \"subtree\": {" + 
                         "                  \"$ref\": \"tree\"" + 
                         "                }," + 
                         "                \"value\": {" + 
                         "                  \"type\": \"number\"" + 
                         "                }" + 
                         "              }," + 
                         "              \"required\": [" + 
                         "                \"value\"" + 
                         "              ]," + 
                         "              \"$id\": \"http://localhost:1234/node\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"properties\": {" + 
                         "            \"nodes\": {" + 
                         "              \"type\": \"array\"," + 
                         "              \"items\": {" + 
                         "                \"$ref\": \"node\"" + 
                         "              }" + 
                         "            }," + 
                         "            \"meta\": {" + 
                         "              \"type\": \"string\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"required\": [" + 
                         "            \"meta\"," + 
                         "            \"nodes\"" + 
                         "          ]," + 
                         "          \"$id\": \"http://localhost:1234/tree\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"not\": {" + 
                         "            \"properties\": {" + 
                         "              \"nodes\": {" + 
                         "                \"type\": \"array\"," + 
                         "                \"items\": {" + 
                         "                  \"$ref\": \"node1\"" + 
                         "                }" + 
                         "              }," + 
                         "              \"meta\": {" + 
                         "                \"type\": \"string\"" + 
                         "              }" + 
                         "            }" + 
                         "          }," + 
                         "          \"description\": \"tree of nodes\"," + 
                         "          \"type\": \"object\"," + 
                         "          \"definitions\": {" + 
                         "            \"node\": {" + 
                         "              \"description\": \"node\"," + 
                         "              \"type\": \"object\"," + 
                         "              \"properties\": {" + 
                         "                \"subtree\": {" + 
                         "                  \"$ref\": \"tree1\"" + 
                         "                }," + 
                         "                \"value\": {" + 
                         "                  \"type\": \"number\"" + 
                         "                }" + 
                         "              }," + 
                         "              \"required\": [" + 
                         "                \"value\"" + 
                         "              ]," + 
                         "              \"$id\": \"http://localhost:1234/node1\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"required\": [" + 
                         "            \"meta\"," + 
                         "            \"nodes\"" + 
                         "          ]," + 
                         "          \"$id\": \"http://localhost:1234/tree1\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": false" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_unsatisfiable_ref_id13_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"definitions\": {" + 
                         "        \"foo\\\"bar\": {" + 
                         "          \"type\": \"number\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"properties\": {" + 
                         "        \"foo\\\"bar\": {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/foo%22bar\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"properties\": {" + 
                         "          \"foo\\\"bar\": {" + 
                         "            \"$ref\": \"#/allOf/1/definitions/foo%22bar\"" + 
                         "          }" + 
                         "        }" + 
                         "      }," + 
                         "      \"definitions\": {" + 
                         "        \"foo\\\"bar\": {" + 
                         "          \"type\": \"number\"" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "false";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"definitions\": {" + 
                         "            \"foo\\\"bar\": {" + 
                         "              \"type\": \"number\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"properties\": {" + 
                         "            \"foo\\\"bar\": {" + 
                         "              \"$ref\": \"#/allOf/0/allOf/0/definitions/foo%22bar\"" + 
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        {" + 
                         "          \"not\": {" + 
                         "            \"properties\": {" + 
                         "              \"foo\\\"bar\": {" + 
                         "                \"$ref\": \"#/allOf/0/allOf/1/definitions/foo%22bar\"" + 
                         "              }" + 
                         "            }" + 
                         "          }," + 
                         "          \"definitions\": {" + 
                         "            \"foo\\\"bar\": {" + 
                         "              \"type\": \"number\"" + 
                         "            }" + 
                         "          }" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": false" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_unsatisfiable_ref_id14_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#foo\"" + 
                         "        }" + 
                         "      ]," + 
                         "      \"definitions\": {" + 
                         "        \"A\": {" + 
                         "          \"type\": \"integer\"," + 
                         "          \"$id\": \"#foo\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#foo1\"" + 
                         "          }" + 
                         "        ]" + 
                         "      }," + 
                         "      \"definitions\": {" + 
                         "        \"A\": {" + 
                         "          \"type\": \"integer\"," + 
                         "          \"$id\": \"#foo1\"" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "false";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"allOf\": [" + 
                         "            {" + 
                         "              \"$ref\": \"#foo\"" + 
                         "            }" + 
                         "          ]," + 
                         "          \"definitions\": {" + 
                         "            \"A\": {" + 
                         "              \"type\": \"integer\"," + 
                         "              \"$id\": \"#foo\"" + 
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        {" + 
                         "          \"not\": {" + 
                         "            \"allOf\": [" + 
                         "              {" + 
                         "                \"$ref\": \"#foo1\"" + 
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"definitions\": {" + 
                         "            \"A\": {" + 
                         "              \"type\": \"integer\"," + 
                         "              \"$id\": \"#foo1\"" + 
                         "            }" + 
                         "          }" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": false" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_unsatisfiable_ref_id15_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"$ref\": \"http://localhost:1234/nested.json#foo\"" + 
                         "        }" + 
                         "      ]," + 
                         "      \"definitions\": {" + 
                         "        \"A\": {" + 
                         "          \"definitions\": {" + 
                         "            \"B\": {" + 
                         "              \"type\": \"integer\"," + 
                         "              \"$id\": \"#foo\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"$id\": \"nested.json\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"$id\": \"http://localhost:1234/root\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"http://localhost:1234/nested1.json#foo1\"" + 
                         "          }" + 
                         "        ]" + 
                         "      }," + 
                         "      \"definitions\": {" + 
                         "        \"A\": {" + 
                         "          \"definitions\": {" + 
                         "            \"B\": {" + 
                         "              \"type\": \"integer\"," + 
                         "              \"$id\": \"#foo1\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"$id\": \"nested1.json\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"$id\": \"http://localhost:1234/root1\"" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "false";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"allOf\": [" + 
                         "            {" + 
                         "              \"$ref\": \"http://localhost:1234/nested.json#foo\"" + 
                         "            }" + 
                         "          ]," + 
                         "          \"definitions\": {" + 
                         "            \"A\": {" + 
                         "              \"definitions\": {" + 
                         "                \"B\": {" + 
                         "                  \"type\": \"integer\"," + 
                         "                  \"$id\": \"#foo\"" + 
                         "                }" + 
                         "              }," + 
                         "              \"$id\": \"nested.json\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"$id\": \"http://localhost:1234/root\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"not\": {" + 
                         "            \"allOf\": [" + 
                         "              {" + 
                         "                \"$ref\": \"http://localhost:1234/nested1.json#foo1\"" + 
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"definitions\": {" + 
                         "            \"A\": {" + 
                         "              \"definitions\": {" + 
                         "                \"B\": {" + 
                         "                  \"type\": \"integer\"," + 
                         "                  \"$id\": \"#foo1\"" + 
                         "                }" + 
                         "              }," + 
                         "              \"$id\": \"nested1.json\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"$id\": \"http://localhost:1234/root1\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": false" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_unsatisfiable_ref_id4_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"definitions\": {" + 
                         "        \"percent%field\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }," + 
                         "        \"tilde~field\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }," + 
                         "        \"slash/field\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"properties\": {" + 
                         "        \"slash\": {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/slash~1field\"" + 
                         "        }," + 
                         "        \"tilde\": {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/tilde~0field\"" + 
                         "        }," + 
                         "        \"percent\": {" + 
                         "          \"$ref\": \"#/allOf/0/definitions/percent%25field\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"properties\": {" + 
                         "          \"slash\": {" + 
                         "            \"$ref\": \"#/allOf/1/definitions/slash~1field\"" + 
                         "          }," + 
                         "          \"tilde\": {" + 
                         "            \"$ref\": \"#/allOf/1/definitions/tilde~0field\"" + 
                         "          }," + 
                         "          \"percent\": {" + 
                         "            \"$ref\": \"#/allOf/1/definitions/percent%25field\"" + 
                         "          }" + 
                         "        }" + 
                         "      }," + 
                         "      \"definitions\": {" + 
                         "        \"percent%field\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }," + 
                         "        \"tilde~field\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }," + 
                         "        \"slash/field\": {" + 
                         "          \"type\": \"integer\"" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "false";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"definitions\": {" + 
                         "            \"percent%field\": {" + 
                         "              \"type\": \"integer\"" + 
                         "            }," + 
                         "            \"tilde~field\": {" + 
                         "              \"type\": \"integer\"" + 
                         "            }," + 
                         "            \"slash/field\": {" + 
                         "              \"type\": \"integer\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"properties\": {" + 
                         "            \"slash\": {" + 
                         "              \"$ref\": \"#/allOf/0/allOf/0/definitions/slash~1field\"" + 
                         "            }," + 
                         "            \"tilde\": {" + 
                         "              \"$ref\": \"#/allOf/0/allOf/0/definitions/tilde~0field\"" +
                         "            }," + 
                         "            \"percent\": {" + 
                         "              \"$ref\": \"#/allOf/0/allOf/0/definitions/percent%25field\"" +
                         "            }" + 
                         "          }" + 
                         "        }," + 
                         "        {" + 
                         "          \"not\": {" + 
                         "            \"properties\": {" + 
                         "              \"slash\": {" + 
                         "                \"$ref\": \"#/allOf/0/allOf/1/definitions/slash~1field\"" + 
                         "              }," + 
                         "              \"tilde\": {" + 
                         "                \"$ref\": \"#/allOf/0/allOf/1/definitions/tilde~0field\"" + 
                         "              }," + 
                         "              \"percent\": {" + 
                         "                \"$ref\": \"#/allOf/0/allOf/1/definitions/percent%25field\"" + 
                         "              }" + 
                         "            }" + 
                         "          }," + 
                         "          \"definitions\": {" + 
                         "            \"percent%field\": {" + 
                         "              \"type\": \"integer\"" + 
                         "            }," + 
                         "            \"tilde~field\": {" + 
                         "              \"type\": \"integer\"" + 
                         "            }," + 
                         "            \"slash/field\": {" + 
                         "              \"type\": \"integer\"" + 
                         "            }" + 
                         "          }" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": false" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_unsatisfiable_ref_id7_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String schema2 = "false";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"allOf\": [" + 
                         "        {" + 
                         "          \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"not\": {" + 
                         "            \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "          }" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": false" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_definitions_id1_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"definitions\": {" + 
                         "      \"foo\": {" + 
                         "        \"type\": \"integer\"" + 
                         "      }" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"definitions\": {" + 
                         "          \"foo\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_definitions_id2_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"definitions\": {" + 
                         "      \"foo\": {" + 
                         "        \"type\": 1" + 
                         "      }" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"definitions\": {" + 
                         "          \"foo\": {" + 
                         "            \"type\": 1" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_infinite_loop_detection_id1_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"foo\": 1" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"properties\": {" + 
                         "        \"foo\": {" + 
                         "          \"$ref\": \"#/definitions/int\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"additionalProperties\": {" + 
                         "        \"$ref\": \"#/definitions/int\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"int\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"foo\": 1" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"properties\": {" + 
                         "              \"foo\": {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/int\"" +
                         "              }" + 
                         "            }" + 
                         "          }," + 
                         "          {" + 
                         "            \"additionalProperties\": {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/int\"" +
                         "            }" + 
                         "          }" + 
                         "        ]," + 
                         "        \"definitions\": {" + 
                         "          \"int\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_infinite_loop_detection_id2_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"foo\": \"a string\"" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"properties\": {" + 
                         "        \"foo\": {" + 
                         "          \"$ref\": \"#/definitions/int\"" + 
                         "        }" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"additionalProperties\": {" + 
                         "        \"$ref\": \"#/definitions/int\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"int\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"foo\": \"a string\"" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"properties\": {" + 
                         "              \"foo\": {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/int\"" +
                         "              }" + 
                         "            }" + 
                         "          }," + 
                         "          {" + 
                         "            \"additionalProperties\": {" + 
                         "              \"$ref\": \"#/allOf/1/not/definitions/int\"" +
                         "            }" + 
                         "          }" + 
                         "        ]," + 
                         "        \"definitions\": {" + 
                         "          \"int\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_items_id18_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": [" + 
                         "    [" + 
                         "      {}," + 
                         "      {}" + 
                         "    ]," + 
                         "    [" + 
                         "      {}," + 
                         "      {}" + 
                         "    ]," + 
                         "    [" + 
                         "      {}," + 
                         "      {}" + 
                         "    ]" + 
                         "  ]" + 
                         "}";
        String schema2 = "{" + 
                         "  \"additionalItems\": false," + 
                         "  \"type\": \"array\"," + 
                         "  \"definitions\": {" + 
                         "    \"item\": {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    \"sub-item\": {" + 
                         "      \"type\": \"object\"," + 
                         "      \"required\": [" + 
                         "        \"foo\"" + 
                         "      ]" + 
                         "    }" + 
                         "  }," + 
                         "  \"items\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": [" + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]," + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]," + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"additionalItems\": false," + 
                         "        \"type\": \"array\"," + 
                         "        \"definitions\": {" + 
                         "          \"item\": {" + 
                         "            \"additionalItems\": false," + 
                         "            \"type\": \"array\"," + 
                         "            \"items\": [" + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"sub-item\": {" + 
                         "            \"type\": \"object\"," + 
                         "            \"required\": [" + 
                         "              \"foo\"" + 
                         "            ]" + 
                         "          }" + 
                         "        }," + 
                         "        \"items\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }" + 
                         "        ]" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_items_id19_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": [" + 
                         "    [" + 
                         "      {}," + 
                         "      {}" + 
                         "    ]," + 
                         "    [" + 
                         "      {}," + 
                         "      {}" + 
                         "    ]," + 
                         "    [" + 
                         "      {}," + 
                         "      {}" + 
                         "    ]," + 
                         "    [" + 
                         "      {}," + 
                         "      {}" + 
                         "    ]" + 
                         "  ]" + 
                         "}";
        String schema2 = "{" + 
                         "  \"additionalItems\": false," + 
                         "  \"type\": \"array\"," + 
                         "  \"definitions\": {" + 
                         "    \"item\": {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    \"sub-item\": {" + 
                         "      \"type\": \"object\"," + 
                         "      \"required\": [" + 
                         "        \"foo\"" + 
                         "      ]" + 
                         "    }" + 
                         "  }," + 
                         "  \"items\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": [" + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]," + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]," + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]," + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"additionalItems\": false," + 
                         "        \"type\": \"array\"," + 
                         "        \"definitions\": {" + 
                         "          \"item\": {" + 
                         "            \"additionalItems\": false," + 
                         "            \"type\": \"array\"," + 
                         "            \"items\": [" + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"sub-item\": {" + 
                         "            \"type\": \"object\"," + 
                         "            \"required\": [" + 
                         "              \"foo\"" + 
                         "            ]" + 
                         "          }" + 
                         "        }," + 
                         "        \"items\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }" + 
                         "        ]" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_items_id20_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": [" + 
                         "    [" + 
                         "      {}," + 
                         "      {}," + 
                         "      {}" + 
                         "    ]," + 
                         "    [" + 
                         "      {}," + 
                         "      {}" + 
                         "    ]," + 
                         "    [" + 
                         "      {}," + 
                         "      {}" + 
                         "    ]" + 
                         "  ]" + 
                         "}";
        String schema2 = "{" + 
                         "  \"additionalItems\": false," + 
                         "  \"type\": \"array\"," + 
                         "  \"definitions\": {" + 
                         "    \"item\": {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    \"sub-item\": {" + 
                         "      \"type\": \"object\"," + 
                         "      \"required\": [" + 
                         "        \"foo\"" + 
                         "      ]" + 
                         "    }" + 
                         "  }," + 
                         "  \"items\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": [" + 
                         "        [" + 
                         "          {}," + 
                         "          {}," + 
                         "          {}" + 
                         "        ]," + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]," + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"additionalItems\": false," + 
                         "        \"type\": \"array\"," + 
                         "        \"definitions\": {" + 
                         "          \"item\": {" + 
                         "            \"additionalItems\": false," + 
                         "            \"type\": \"array\"," + 
                         "            \"items\": [" + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"sub-item\": {" + 
                         "            \"type\": \"object\"," + 
                         "            \"required\": [" + 
                         "              \"foo\"" + 
                         "            ]" + 
                         "          }" + 
                         "        }," + 
                         "        \"items\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }" + 
                         "        ]" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_items_id21_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": [" + 
                         "    {}," + 
                         "    [" + 
                         "      {}," + 
                         "      {}" + 
                         "    ]," + 
                         "    [" + 
                         "      {}," + 
                         "      {}" + 
                         "    ]" + 
                         "  ]" + 
                         "}";
        String schema2 = "{" + 
                         "  \"additionalItems\": false," + 
                         "  \"type\": \"array\"," + 
                         "  \"definitions\": {" + 
                         "    \"item\": {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    \"sub-item\": {" + 
                         "      \"type\": \"object\"," + 
                         "      \"required\": [" + 
                         "        \"foo\"" + 
                         "      ]" + 
                         "    }" + 
                         "  }," + 
                         "  \"items\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": [" + 
                         "        {}," + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]," + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"additionalItems\": false," + 
                         "        \"type\": \"array\"," + 
                         "        \"definitions\": {" + 
                         "          \"item\": {" + 
                         "            \"additionalItems\": false," + 
                         "            \"type\": \"array\"," + 
                         "            \"items\": [" + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"sub-item\": {" + 
                         "            \"type\": \"object\"," + 
                         "            \"required\": [" + 
                         "              \"foo\"" + 
                         "            ]" + 
                         "          }" + 
                         "        }," + 
                         "        \"items\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }" + 
                         "        ]" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_items_id22_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": [" + 
                         "    [" + 
                         "      {}," + 
                         "      {}" + 
                         "    ]," + 
                         "    [" + 
                         "      {}," + 
                         "      {}" + 
                         "    ]," + 
                         "    [" + 
                         "      {}," + 
                         "      {}" + 
                         "    ]" + 
                         "  ]" + 
                         "}";
        String schema2 = "{" + 
                         "  \"additionalItems\": false," + 
                         "  \"type\": \"array\"," + 
                         "  \"definitions\": {" + 
                         "    \"item\": {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    \"sub-item\": {" + 
                         "      \"type\": \"object\"," + 
                         "      \"required\": [" + 
                         "        \"foo\"" + 
                         "      ]" + 
                         "    }" + 
                         "  }," + 
                         "  \"items\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": [" + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]," + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]," + 
                         "        [" + 
                         "          {}," + 
                         "          {}" + 
                         "        ]" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"additionalItems\": false," + 
                         "        \"type\": \"array\"," + 
                         "        \"definitions\": {" + 
                         "          \"item\": {" + 
                         "            \"additionalItems\": false," + 
                         "            \"type\": \"array\"," + 
                         "            \"items\": [" + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"sub-item\": {" + 
                         "            \"type\": \"object\"," + 
                         "            \"required\": [" + 
                         "              \"foo\"" + 
                         "            ]" + 
                         "          }" + 
                         "        }," + 
                         "        \"items\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }" + 
                         "        ]" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_items_id23_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": [" + 
                         "    [" + 
                         "      {}" + 
                         "    ]," + 
                         "    [" + 
                         "      {}" + 
                         "    ]" + 
                         "  ]" + 
                         "}";
        String schema2 = "{" + 
                         "  \"additionalItems\": false," + 
                         "  \"type\": \"array\"," + 
                         "  \"definitions\": {" + 
                         "    \"item\": {" + 
                         "      \"additionalItems\": false," + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": [" + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }," + 
                         "        {" + 
                         "          \"$ref\": \"#/definitions/sub-item\"" + 
                         "        }" + 
                         "      ]" + 
                         "    }," + 
                         "    \"sub-item\": {" + 
                         "      \"type\": \"object\"," + 
                         "      \"required\": [" + 
                         "        \"foo\"" + 
                         "      ]" + 
                         "    }" + 
                         "  }," + 
                         "  \"items\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/item\"" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": [" + 
                         "        [" + 
                         "          {}" + 
                         "        ]," + 
                         "        [" + 
                         "          {}" + 
                         "        ]" + 
                         "      ]" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"additionalItems\": false," + 
                         "        \"type\": \"array\"," + 
                         "        \"definitions\": {" + 
                         "          \"item\": {" + 
                         "            \"additionalItems\": false," + 
                         "            \"type\": \"array\"," + 
                         "            \"items\": [" + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }," + 
                         "              {" + 
                         "                \"$ref\": \"#/allOf/1/not/definitions/sub-item\"" +
                         "              }" + 
                         "            ]" + 
                         "          }," + 
                         "          \"sub-item\": {" + 
                         "            \"type\": \"object\"," + 
                         "            \"required\": [" + 
                         "              \"foo\"" + 
                         "            ]" + 
                         "          }" + 
                         "        }," + 
                         "        \"items\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }," + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/item\"" +
                         "          }" + 
                         "        ]" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id10_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"tilde\": \"aoeu\"" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"percent%field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"tilde~field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"slash/field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"slash\": {" + 
                         "      \"$ref\": \"#/definitions/slash~1field\"" + 
                         "    }," + 
                         "    \"tilde\": {" + 
                         "      \"$ref\": \"#/definitions/tilde~0field\"" + 
                         "    }," + 
                         "    \"percent\": {" + 
                         "      \"$ref\": \"#/definitions/percent%25field\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"tilde\": \"aoeu\"" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"percent%field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"tilde~field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"slash/field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"slash\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/slash~1field\"" + 
                         "          }," + 
                         "          \"tilde\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/tilde~0field\"" + 
                         "          }," + 
                         "          \"percent\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/percent%25field\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id11_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"percent\": \"aoeu\"" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"percent%field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"tilde~field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"slash/field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"slash\": {" + 
                         "      \"$ref\": \"#/definitions/slash~1field\"" + 
                         "    }," + 
                         "    \"tilde\": {" + 
                         "      \"$ref\": \"#/definitions/tilde~0field\"" + 
                         "    }," + 
                         "    \"percent\": {" + 
                         "      \"$ref\": \"#/definitions/percent%25field\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"percent\": \"aoeu\"" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"percent%field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"tilde~field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"slash/field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"slash\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/slash~1field\"" + 
                         "          }," + 
                         "          \"tilde\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/tilde~0field\"" + 
                         "          }," + 
                         "          \"percent\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/percent%25field\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id12_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"slash\": 123" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"percent%field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"tilde~field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"slash/field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"slash\": {" + 
                         "      \"$ref\": \"#/definitions/slash~1field\"" + 
                         "    }," + 
                         "    \"tilde\": {" + 
                         "      \"$ref\": \"#/definitions/tilde~0field\"" + 
                         "    }," + 
                         "    \"percent\": {" + 
                         "      \"$ref\": \"#/definitions/percent%25field\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"slash\": 123" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"percent%field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"tilde~field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"slash/field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"slash\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/slash~1field\"" + 
                         "          }," + 
                         "          \"tilde\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/tilde~0field\"" + 
                         "          }," + 
                         "          \"percent\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/percent%25field\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id13_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"tilde\": 123" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"percent%field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"tilde~field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"slash/field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"slash\": {" + 
                         "      \"$ref\": \"#/definitions/slash~1field\"" + 
                         "    }," + 
                         "    \"tilde\": {" + 
                         "      \"$ref\": \"#/definitions/tilde~0field\"" + 
                         "    }," + 
                         "    \"percent\": {" + 
                         "      \"$ref\": \"#/definitions/percent%25field\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"tilde\": 123" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"percent%field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"tilde~field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"slash/field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"slash\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/slash~1field\"" + 
                         "          }," + 
                         "          \"tilde\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/tilde~0field\"" + 
                         "          }," + 
                         "          \"percent\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/percent%25field\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id14_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"percent\": 123" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"percent%field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"tilde~field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"slash/field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"slash\": {" + 
                         "      \"$ref\": \"#/definitions/slash~1field\"" + 
                         "    }," + 
                         "    \"tilde\": {" + 
                         "      \"$ref\": \"#/definitions/tilde~0field\"" + 
                         "    }," + 
                         "    \"percent\": {" + 
                         "      \"$ref\": \"#/definitions/percent%25field\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"percent\": 123" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"percent%field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"tilde~field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"slash/field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"slash\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/slash~1field\"" + 
                         "          }," + 
                         "          \"tilde\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/tilde~0field\"" + 
                         "          }," + 
                         "          \"percent\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/percent%25field\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id15_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": 5" + 
                         "}";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/c\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"a\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"b\": {" + 
                         "      \"$ref\": \"#/definitions/a\"" + 
                         "    }," + 
                         "    \"c\": {" + 
                         "      \"$ref\": \"#/definitions/b\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": 5" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/c\"" + 
                         "          }" + 
                         "        ]," + 
                         "        \"definitions\": {" + 
                         "          \"a\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"b\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/a\"" + 
                         "          }," + 
                         "          \"c\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/b\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id16_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": \"a\"" + 
                         "}";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/c\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"a\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"b\": {" + 
                         "      \"$ref\": \"#/definitions/a\"" + 
                         "    }," + 
                         "    \"c\": {" + 
                         "      \"$ref\": \"#/definitions/b\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": \"a\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/c\"" + 
                         "          }" + 
                         "        ]," + 
                         "        \"definitions\": {" + 
                         "          \"a\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"b\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/a\"" + 
                         "          }," + 
                         "          \"c\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/b\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id17_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"foo\": []" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"reffed\": {" + 
                         "      \"type\": \"array\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"foo\": {" + 
                         "      \"maxItems\": 2," + 
                         "      \"$ref\": \"#/definitions/reffed\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"foo\": []" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"reffed\": {" + 
                         "            \"type\": \"array\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"foo\": {" + 
                         "            \"maxItems\": 2," + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/reffed\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id18_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"foo\": [" + 
                         "      1," + 
                         "      2," + 
                         "      3" + 
                         "    ]" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"reffed\": {" + 
                         "      \"type\": \"array\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"foo\": {" + 
                         "      \"maxItems\": 2," + 
                         "      \"$ref\": \"#/definitions/reffed\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"foo\": [" + 
                         "          1," + 
                         "          2," + 
                         "          3" + 
                         "        ]" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"reffed\": {" + 
                         "            \"type\": \"array\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"foo\": {" + 
                         "            \"maxItems\": 2," + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/reffed\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id19_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"foo\": \"string\"" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"reffed\": {" + 
                         "      \"type\": \"array\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"foo\": {" + 
                         "      \"maxItems\": 2," + 
                         "      \"$ref\": \"#/definitions/reffed\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"foo\": \"string\"" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"reffed\": {" + 
                         "            \"type\": \"array\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"foo\": {" + 
                         "            \"maxItems\": 2," + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/reffed\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id20_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"minLength\": 1" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"minLength\": 1" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id21_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"minLength\": -1" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"minLength\": -1" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"$ref\": \"http://json-schema.org/draft-06/schema#\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id24_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"$ref\": \"a\"" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"is-string\": {" + 
                         "      \"type\": \"string\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"$ref\": {" + 
                         "      \"$ref\": \"#/definitions/is-string\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"$ref\": \"a\"" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"is-string\": {" + 
                         "            \"type\": \"string\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"$ref\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/is-string\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id25_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"$ref\": 2" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"is-string\": {" + 
                         "      \"type\": \"string\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"$ref\": {" + 
                         "      \"$ref\": \"#/definitions/is-string\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"$ref\": 2" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"is-string\": {" + 
                         "            \"type\": \"string\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"$ref\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/is-string\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id26_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": \"foo\"" + 
                         "}";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/bool\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"bool\": true" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": \"foo\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/bool\"" + 
                         "          }" + 
                         "        ]," + 
                         "        \"definitions\": {" + 
                         "          \"bool\": true" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id27_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": \"foo\"" + 
                         "}";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#/definitions/bool\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"bool\": false" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": \"foo\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/bool\"" + 
                         "          }" + 
                         "        ]," + 
                         "        \"definitions\": {" + 
                         "          \"bool\": false" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id28_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"nodes\": [" + 
                         "      {" + 
                         "        \"subtree\": {" + 
                         "          \"nodes\": [" + 
                         "            {" + 
                         "              \"value\": 1.1" + 
                         "            }," + 
                         "            {" + 
                         "              \"value\": 1.2" + 
                         "            }" + 
                         "          ]," + 
                         "          \"meta\": \"child\"" + 
                         "        }," + 
                         "        \"value\": 1" + 
                         "      }," + 
                         "      {" + 
                         "        \"subtree\": {" + 
                         "          \"nodes\": [" + 
                         "            {" + 
                         "              \"value\": 2.1" + 
                         "            }," + 
                         "            {" + 
                         "              \"value\": 2.2" + 
                         "            }" + 
                         "          ]," + 
                         "          \"meta\": \"child\"" + 
                         "        }," + 
                         "        \"value\": 2" + 
                         "      }" + 
                         "    ]," + 
                         "    \"meta\": \"root\"" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"description\": \"tree of nodes\"," + 
                         "  \"type\": \"object\"," + 
                         "  \"definitions\": {" + 
                         "    \"node\": {" + 
                         "      \"description\": \"node\"," + 
                         "      \"type\": \"object\"," + 
                         "      \"properties\": {" + 
                         "        \"subtree\": {" + 
                         "          \"$ref\": \"tree\"" + 
                         "        }," + 
                         "        \"value\": {" + 
                         "          \"type\": \"number\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"required\": [" + 
                         "        \"value\"" + 
                         "      ]," + 
                         "      \"$id\": \"http://localhost:1234/node\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"nodes\": {" + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": {" + 
                         "        \"$ref\": \"node\"" + 
                         "      }" + 
                         "    }," + 
                         "    \"meta\": {" + 
                         "      \"type\": \"string\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"required\": [" + 
                         "    \"meta\"," + 
                         "    \"nodes\"" + 
                         "  ]," + 
                         "  \"$id\": \"http://localhost:1234/tree\"" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"nodes\": [" + 
                         "          {" + 
                         "            \"subtree\": {" + 
                         "              \"nodes\": [" + 
                         "                {" + 
                         "                  \"value\": 1.1" + 
                         "                }," + 
                         "                {" + 
                         "                  \"value\": 1.2" + 
                         "                }" + 
                         "              ]," + 
                         "              \"meta\": \"child\"" + 
                         "            }," + 
                         "            \"value\": 1" + 
                         "          }," + 
                         "          {" + 
                         "            \"subtree\": {" + 
                         "              \"nodes\": [" + 
                         "                {" + 
                         "                  \"value\": 2.1" + 
                         "                }," + 
                         "                {" + 
                         "                  \"value\": 2.2" + 
                         "                }" + 
                         "              ]," + 
                         "              \"meta\": \"child\"" + 
                         "            }," + 
                         "            \"value\": 2" + 
                         "          }" + 
                         "        ]," + 
                         "        \"meta\": \"root\"" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"description\": \"tree of nodes\"," + 
                         "        \"type\": \"object\"," + 
                         "        \"definitions\": {" + 
                         "          \"node\": {" + 
                         "            \"description\": \"node\"," + 
                         "            \"type\": \"object\"," + 
                         "            \"properties\": {" + 
                         "              \"subtree\": {" + 
                         "                \"$ref\": \"tree\"" + 
                         "              }," + 
                         "              \"value\": {" + 
                         "                \"type\": \"number\"" + 
                         "              }" + 
                         "            }," + 
                         "            \"required\": [" + 
                         "              \"value\"" + 
                         "            ]," + 
                         "            \"$id\": \"http://localhost:1234/node\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"nodes\": {" + 
                         "            \"type\": \"array\"," + 
                         "            \"items\": {" + 
                         "              \"$ref\": \"node\"" + 
                         "            }" + 
                         "          }," + 
                         "          \"meta\": {" + 
                         "            \"type\": \"string\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"required\": [" + 
                         "          \"meta\"," + 
                         "          \"nodes\"" + 
                         "        ]," + 
                         "        \"$id\": \"http://localhost:1234/tree\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id29_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"nodes\": [" + 
                         "      {" + 
                         "        \"subtree\": {" + 
                         "          \"nodes\": [" + 
                         "            {" + 
                         "              \"value\": \"string is invalid\"" + 
                         "            }," + 
                         "            {" + 
                         "              \"value\": 1.2" + 
                         "            }" + 
                         "          ]," + 
                         "          \"meta\": \"child\"" + 
                         "        }," + 
                         "        \"value\": 1" + 
                         "      }," + 
                         "      {" + 
                         "        \"subtree\": {" + 
                         "          \"nodes\": [" + 
                         "            {" + 
                         "              \"value\": 2.1" + 
                         "            }," + 
                         "            {" + 
                         "              \"value\": 2.2" + 
                         "            }" + 
                         "          ]," + 
                         "          \"meta\": \"child\"" + 
                         "        }," + 
                         "        \"value\": 2" + 
                         "      }" + 
                         "    ]," + 
                         "    \"meta\": \"root\"" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"description\": \"tree of nodes\"," + 
                         "  \"type\": \"object\"," + 
                         "  \"definitions\": {" + 
                         "    \"node\": {" + 
                         "      \"description\": \"node\"," + 
                         "      \"type\": \"object\"," + 
                         "      \"properties\": {" + 
                         "        \"subtree\": {" + 
                         "          \"$ref\": \"tree\"" + 
                         "        }," + 
                         "        \"value\": {" + 
                         "          \"type\": \"number\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"required\": [" + 
                         "        \"value\"" + 
                         "      ]," + 
                         "      \"$id\": \"http://localhost:1234/node\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"nodes\": {" + 
                         "      \"type\": \"array\"," + 
                         "      \"items\": {" + 
                         "        \"$ref\": \"node\"" + 
                         "      }" + 
                         "    }," + 
                         "    \"meta\": {" + 
                         "      \"type\": \"string\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"required\": [" + 
                         "    \"meta\"," + 
                         "    \"nodes\"" + 
                         "  ]," + 
                         "  \"$id\": \"http://localhost:1234/tree\"" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"nodes\": [" + 
                         "          {" + 
                         "            \"subtree\": {" + 
                         "              \"nodes\": [" + 
                         "                {" + 
                         "                  \"value\": \"string is invalid\"" + 
                         "                }," + 
                         "                {" + 
                         "                  \"value\": 1.2" + 
                         "                }" + 
                         "              ]," + 
                         "              \"meta\": \"child\"" + 
                         "            }," + 
                         "            \"value\": 1" + 
                         "          }," + 
                         "          {" + 
                         "            \"subtree\": {" + 
                         "              \"nodes\": [" + 
                         "                {" + 
                         "                  \"value\": 2.1" + 
                         "                }," + 
                         "                {" + 
                         "                  \"value\": 2.2" + 
                         "                }" + 
                         "              ]," + 
                         "              \"meta\": \"child\"" + 
                         "            }," + 
                         "            \"value\": 2" + 
                         "          }" + 
                         "        ]," + 
                         "        \"meta\": \"root\"" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"description\": \"tree of nodes\"," + 
                         "        \"type\": \"object\"," + 
                         "        \"definitions\": {" + 
                         "          \"node\": {" + 
                         "            \"description\": \"node\"," + 
                         "            \"type\": \"object\"," + 
                         "            \"properties\": {" + 
                         "              \"subtree\": {" + 
                         "                \"$ref\": \"tree\"" + 
                         "              }," + 
                         "              \"value\": {" + 
                         "                \"type\": \"number\"" + 
                         "              }" + 
                         "            }," + 
                         "            \"required\": [" + 
                         "              \"value\"" + 
                         "            ]," + 
                         "            \"$id\": \"http://localhost:1234/node\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"nodes\": {" + 
                         "            \"type\": \"array\"," + 
                         "            \"items\": {" + 
                         "              \"$ref\": \"node\"" +
                         "            }" + 
                         "          }," + 
                         "          \"meta\": {" + 
                         "            \"type\": \"string\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"required\": [" + 
                         "          \"meta\"," + 
                         "          \"nodes\"" + 
                         "        ]," + 
                         "        \"$id\": \"http://localhost:1234/tree\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id30_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"foo\\\"bar\": 1" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"foo\\\"bar\": {" + 
                         "      \"type\": \"number\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"foo\\\"bar\": {" + 
                         "      \"$ref\": \"#/definitions/foo%22bar\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"foo\\\"bar\": 1" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"foo\\\"bar\": {" + 
                         "            \"type\": \"number\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"foo\\\"bar\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/foo%22bar\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id31_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"foo\\\"bar\": \"1\"" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"foo\\\"bar\": {" + 
                         "      \"type\": \"number\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"foo\\\"bar\": {" + 
                         "      \"$ref\": \"#/definitions/foo%22bar\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"foo\\\"bar\": \"1\"" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"foo\\\"bar\": {" + 
                         "            \"type\": \"number\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"foo\\\"bar\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/foo%22bar\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id32_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": 1" + 
                         "}";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#foo\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"A\": {" + 
                         "      \"type\": \"integer\"," + 
                         "      \"$id\": \"#foo\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": 1" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#foo\"" + 
                         "          }" + 
                         "        ]," + 
                         "        \"definitions\": {" + 
                         "          \"A\": {" + 
                         "            \"type\": \"integer\"," + 
                         "            \"$id\": \"#foo\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id33_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": \"a\"" + 
                         "}";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"#foo\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"A\": {" + 
                         "      \"type\": \"integer\"," + 
                         "      \"$id\": \"#foo\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": \"a\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"#foo\"" + 
                         "          }" + 
                         "        ]," + 
                         "        \"definitions\": {" + 
                         "          \"A\": {" + 
                         "            \"type\": \"integer\"," + 
                         "            \"$id\": \"#foo\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id34_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": 1" + 
                         "}";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"http://localhost:1234/nested.json#foo\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"A\": {" + 
                         "      \"definitions\": {" + 
                         "        \"B\": {" + 
                         "          \"type\": \"integer\"," + 
                         "          \"$id\": \"#foo\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"$id\": \"nested.json\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"$id\": \"http://localhost:1234/root\"" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": 1" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"http://localhost:1234/nested.json#foo\"" + 
                         "          }" + 
                         "        ]," + 
                         "        \"definitions\": {" + 
                         "          \"A\": {" + 
                         "            \"definitions\": {" + 
                         "              \"B\": {" + 
                         "                \"type\": \"integer\"," + 
                         "                \"$id\": \"#foo\"" + 
                         "              }" + 
                         "            }," + 
                         "            \"$id\": \"nested.json\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"$id\": \"http://localhost:1234/root\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id35_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": \"a\"" + 
                         "}";
        String schema2 = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"$ref\": \"http://localhost:1234/nested.json#foo\"" + 
                         "    }" + 
                         "  ]," + 
                         "  \"definitions\": {" + 
                         "    \"A\": {" + 
                         "      \"definitions\": {" + 
                         "        \"B\": {" + 
                         "          \"type\": \"integer\"," + 
                         "          \"$id\": \"#foo\"" + 
                         "        }" + 
                         "      }," + 
                         "      \"$id\": \"nested.json\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"$id\": \"http://localhost:1234/root\"" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": \"a\"" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"allOf\": [" + 
                         "          {" + 
                         "            \"$ref\": \"http://localhost:1234/nested.json#foo\"" + 
                         "          }" + 
                         "        ]," + 
                         "        \"definitions\": {" + 
                         "          \"A\": {" + 
                         "            \"definitions\": {" + 
                         "              \"B\": {" + 
                         "                \"type\": \"integer\"," + 
                         "                \"$id\": \"#foo\"" + 
                         "              }" + 
                         "            }," + 
                         "            \"$id\": \"nested.json\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"$id\": \"http://localhost:1234/root\"" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

    @Test
    public void test_draft6_valid_ref_id9_subschema1_not_2() throws ParseException {
        String schema1 = "{" + 
                         "  \"const\": {" + 
                         "    \"slash\": \"aoeu\"" + 
                         "  }" + 
                         "}";
        String schema2 = "{" + 
                         "  \"definitions\": {" + 
                         "    \"percent%field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"tilde~field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }," + 
                         "    \"slash/field\": {" + 
                         "      \"type\": \"integer\"" + 
                         "    }" + 
                         "  }," + 
                         "  \"properties\": {" + 
                         "    \"slash\": {" + 
                         "      \"$ref\": \"#/definitions/slash~1field\"" + 
                         "    }," + 
                         "    \"tilde\": {" + 
                         "      \"$ref\": \"#/definitions/tilde~0field\"" + 
                         "    }," + 
                         "    \"percent\": {" + 
                         "      \"$ref\": \"#/definitions/percent%25field\"" + 
                         "    }" + 
                         "  }" + 
                         "}";
        String difference = jsonSchemaLib.generateSchemaDifference(schema1, schema2);
        String expected = "{" + 
                         "  \"allOf\": [" + 
                         "    {" + 
                         "      \"const\": {" + 
                         "        \"slash\": \"aoeu\"" + 
                         "      }" + 
                         "    }," + 
                         "    {" + 
                         "      \"not\": {" + 
                         "        \"definitions\": {" + 
                         "          \"percent%field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"tilde~field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }," + 
                         "          \"slash/field\": {" + 
                         "            \"type\": \"integer\"" + 
                         "          }" + 
                         "        }," + 
                         "        \"properties\": {" + 
                         "          \"slash\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/slash~1field\"" + 
                         "          }," + 
                         "          \"tilde\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/tilde~0field\"" + 
                         "          }," + 
                         "          \"percent\": {" + 
                         "            \"$ref\": \"#/allOf/1/not/definitions/percent%25field\"" + 
                         "          }" + 
                         "        }" + 
                         "      }" + 
                         "    }" + 
                         "  ]" + 
                         "}";
        JSONObject expectedObj = (JSONObject) parser.parse(expected);
        JSONObject differenceObj = (JSONObject) parser.parse(difference);
        assertEquals(expectedObj, differenceObj);
    }

}
