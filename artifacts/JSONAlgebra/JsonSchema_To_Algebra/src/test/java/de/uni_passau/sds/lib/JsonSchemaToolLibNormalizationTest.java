package de.uni_passau.sds.lib;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Helpers.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.nio.file.Paths;

/**
 * This class tests if schemas with referenced id's are normalized correctly after they have been rewritten.
 * @schema is already a rewritten schema like {allOf:[…], not : […]}
 * @Path the path to the resource dir within the test dir
 *
 * @author Luca Escher
 */
public class JsonSchemaToolLibNormalizationTest {

    private static String resourceDirectory = Paths.get("src","test","resources").toFile().getAbsolutePath();

    private static String PATH_TO_NORMALIZE_TESTING = "/normalize_tests/testing/";
    private static String PATH_TO_NORMALIZE_EXPECTED = "/normalize_tests/expected/";

    private static String PATH_TEST = resourceDirectory + PATH_TO_NORMALIZE_TESTING;
    private static String PATH_EXPECTED = resourceDirectory + PATH_TO_NORMALIZE_EXPECTED;

    private static final IJsonSchemaLib lib = JsonSchemaLibFactory.getJsonSchemaLib();

    @Test
    public void test1() throws FileNotFoundException {
        String schema = FileUtils.fileToSchema(PATH_TEST, "1.json");
        String expected = FileUtils.fileToSchema(PATH_EXPECTED, "1.json");

        String resultSchema = lib.normalizeSchema(schema);

        JsonObject expectedJson = (JsonObject) JsonParser.parseString(expected);
        JsonObject actualJson = (JsonObject) JsonParser.parseString(resultSchema);

        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void test2() throws FileNotFoundException {
        String schema = FileUtils.fileToSchema(PATH_TEST, "2.json");
        String expected = FileUtils.fileToSchema(PATH_EXPECTED, "2.json");

        String resultSchema = lib.normalizeSchema(schema);

        JsonObject expectedJson = (JsonObject) JsonParser.parseString(expected);
        JsonObject actualJson = (JsonObject) JsonParser.parseString(resultSchema);

        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void test3() throws FileNotFoundException {
        String schema = FileUtils.fileToSchema(PATH_TEST, "3.json");
        String expected = FileUtils.fileToSchema(PATH_EXPECTED, "3.json");

        String resultSchema = lib.normalizeSchema(schema);
        System.out.println(resultSchema);
        JsonObject expectedJson = (JsonObject) JsonParser.parseString(expected);
        JsonObject actualJson = (JsonObject) JsonParser.parseString(resultSchema);

        Assert.assertEquals(expectedJson, actualJson);
    }
}