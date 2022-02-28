package de.uni_passau.scs.testsuites.jsonsubschema;

import de.uni_passau.sds.lib.IJsonSchemaLib;
import de.uni_passau.sds.lib.JsonSchemaLibFactory;
import de.uni_passau.sds.lib.JsonSchemaLibImplementations;
import de.uni_passau.sds.lib.schemaComparism.JsonSchemaComparison;
import de.uni_passau.sds.lib.schemaComparism.JsonSchemaRelationships;
import org.junit.Assert;
import org.junit.Test;

/**
 * The schemas used in this testsuite are from the json-schema-containment-testsuite.
 * You can find this testsuite here --> https://github.com/sdbs-uni-p/json-schema-containment-testsuite
 * 
 * To be more specific the schemas you can find in folder "test_jsonsubschema" here 
 * --> https://github.com/sdbs-uni-p/json-schema-containment-testsuite/tree/main/tests/tests_jsonsubschema
 * 
 * json-schema-containment-testsuite last commit:   e89f6596d20faae443a5a1f5d2584e6f08c773ec
 * JsonSchema_To_Algebra last commit:               56312921f0e5f09a5a29e5678b15c6d938278d9f
 * 
 * Please read the README.md for further informations.
 * https://github.com/miniHive/JSONAlgebra/blob/main/JsonSchema_To_Algebra/src/test/java/de/uni_passau/scs/testsuites/README.md
 * 
 * @author Luca Escher
 */
public class TestsuiteJsonSubSchema {

    private final IJsonSchemaLib jsonSchemaLib = JsonSchemaLibFactory.getJsonSchemaLib(JsonSchemaLibImplementations.JSON_SCHEMA_TOOL_LIB);
    private JsonSchemaComparison data = new JsonSchemaComparison();

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id1() throws Exception {

        String schema1 = "{\"not\":{}}";
        String schema2 = "{\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id2() throws Exception {

        String schema1 = "{\"not\":{},\"description\":\"bottom\"}";
        String schema2 = "{\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id3() throws Exception {

        String schema1 = "{}";
        String schema2 = "{\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id4() throws Exception {

        String schema1 = "{\"description\":\"top\"}";
        String schema2 = "{}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id5() throws Exception {

        String schema1 = "{\"allOf\":[{\"type\":[\"string\",\"boolean\"]}],\"type\":[\"string\",\"boolean\"]}";
        String schema2 = "{\"anyOf\":[{\"type\":\"string\"},{\"type\":\"boolean\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id6() throws Exception {

        String schema1 = "{\"enum\":[1,2,\"test\",false]}";
        String schema2 = "{\"type\":[\"integer\",\"string\"],\"minimum\":10,\"enum\":[1,2]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id7() throws Exception {

        String schema1 = "{\"allOf\":[{\"enum\":[1,2,3]},{\"type\":\"integer\"}],\"enum\":[3,4,5]}";
        String schema2 = "{\"type\":\"integer\",\"enum\":[1,2,3]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id8() throws Exception {

        String schema1 = "{\"enum\":[3,4,5]}";
        String schema2 = "{\"enum\":[1,2,3]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id9() throws Exception {

        String schema1 = "{\"enum\":[3,4,5]}";
        String schema2 = "{\"enum\":[4,5,3]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id10() throws Exception {

        String schema1 = "{\"allOf\":[{\"enum\":[1,2]}],\"enum\":[3,4,5]}";
        String schema2 = "{\"enum\":[4,5,3]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id11() throws Exception {

        String schema1 = "{\"type\":\"string\",\"enum\":[3,4,5]}";
        String schema2 = "{\"enum\":[4,5,3]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id12() throws Exception {

        String schema1 = "{\"description\":\"checking_status\",\"enum\":[\"<0\",\"0<=X<200\",\">=200\",\"no checking\"]}";
        String schema2 = "{\"not\":{\"type\":\"number\"}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id13() throws Exception {

        String schema1 = "{\"type\":[\"string\",\"boolean\"]}";
        String schema2 = "{\"anyOf\":[{\"type\":\"string\"},{\"type\":\"boolean\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id14() throws Exception {

        String schema1 = "{\"allOf\":[{\"pattern\":\"b+\",\"type\":\"string\"},{\"allOf\":[{\"type\":\"string\",\"maxLength\":10}]}],\"pattern\":\"a+\",\"type\":\"string\"}";
        String schema2 = "{\"type\":\"integer\",\"maxLength\":1}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id15() throws Exception {

        String schema1 = "{\"type\":\"number\"}";
        String schema2 = "{\"type\":\"array\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id16() throws Exception {

        String schema1 = "{\"type\":\"number\"}";
        String schema2 = "{\"type\":[\"number\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id17() throws Exception {

        String schema1 = "{\"type\":\"integer\"}";
        String schema2 = "{\"type\":[\"number\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id18() throws Exception {

        String schema1 = "{\"type\":\"integer\"}";
        String schema2 = "{\"type\":[\"number\",\"string\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id19() throws Exception {

        String schema1 = "{\"type\":[\"string\",\"array\"]}";
        String schema2 = "{\"type\":[\"number\",\"string\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id20() throws Exception {

        String schema1 = "{}";
        String schema2 = "{\"type\":\"string\",\"enum\":[1,2,3]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id21() throws Exception {

        String schema1 = "{}";
        String schema2 = "{\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_mix_id22() throws Exception {

        String schema1 = "{\"type\":\"string\",\"enum\":[2]}";
        String schema2 = "{\"type\":\"boolean\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_null_id1() throws Exception {

        String schema1 = "{\"enum\":[null]}";
        String schema2 = "{\"type\":\"null\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_null_id2() throws Exception {

        String schema1 = "{\"type\":\"null\"}";
        String schema2 = "{}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_null_id3() throws Exception {

        String schema1 = "{\"enum\":[null]}";
        String schema2 = "{\"enum\":[0]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id1() throws Exception {

        String schema1 = "{\"allOf\":[{\"multipleOf\":3},{\"minimum\":5}],\"type\":\"integer\"}";
        String schema2 = "{\"multipleOf\":3,\"allOf\":[{\"type\":\"integer\"},{\"multipleOf\":5,\"type\":\"number\"}],\"type\":\"number\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id2() throws Exception {

        String schema1 = "{\"allOf\":[{\"multipleOf\":3}],\"type\":\"integer\"}";
        String schema2 = "{\"multipleOf\":3,\"allOf\":[{\"type\":\"integer\"},{\"multipleOf\":3,\"type\":\"number\"}],\"type\":\"number\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id3() throws Exception {

        String schema1 = "{\"allOf\":[{\"multipleOf\":0.3}],\"type\":\"number\"}";
        String schema2 = "{\"multipleOf\":3,\"allOf\":[{\"type\":\"integer\"},{\"multipleOf\":3,\"type\":\"number\"}],\"type\":\"number\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id4() throws Exception {

        String schema1 = "{\"enum\":[1,2,3]}";
        String schema2 = "{\"type\":\"number\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id5() throws Exception {

        String schema1 = "{\"enum\":[1,2,3]}";
        String schema2 = "{\"enum\":[1,2]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id6() throws Exception {

        String schema1 = "{\"enum\":[1,2,3]}";
        String schema2 = "{\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id7() throws Exception {

        String schema1 = "{\"enum\":[1,2,3]}";
        String schema2 = "{\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id8() throws Exception {

        String schema1 = "{\"type\":\"integer\"}";
        String schema2 = "{\"allOf\":[{\"type\":\"integer\"},{\"type\":\"number\",\"minimum\":10}],\"type\":\"number\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id9() throws Exception {

        String schema1 = "{\"multipleOf\":5,\"type\":\"integer\"}";
        String schema2 = "{\"allOf\":[{\"type\":\"integer\"},{\"type\":\"number\",\"minimum\":10}],\"type\":\"number\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id10() throws Exception {

        String schema1 = "{\"multipleOf\":5,\"type\":\"integer\"}";
        String schema2 = "{\"multipleOF\":3,\"allOf\":[{\"type\":\"integer\"},{\"multipleOf\":3,\"type\":\"number\"}],\"type\":\"number\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id11() throws Exception {

        String schema1 = "{\"multipleOf\":15,\"type\":\"integer\"}";
        String schema2 = "{\"multipleOf\":3,\"allOf\":[{\"type\":\"integer\"},{\"multipleOf\":5,\"type\":\"number\"}],\"type\":\"number\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id12() throws Exception {

        String schema1 = "{\"type\":\"integer\"}";
        String schema2 = "{\"allOf\":[\"\"],\"type\":\"number\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id13() throws Exception {

        String schema1 = "{\"type\":\"integer\"}";
        String schema2 = "{\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id14() throws Exception {

        String schema1 = "{\"anyOf\":[{\"maximum\":10,\"type\":\"integer\",\"minimum\":5},{\"type\":\"integer\"}]}";
        String schema2 = "{\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id15() throws Exception {

        String schema1 = "{\"anyOf\":[{\"maximum\":10,\"type\":\"integer\",\"minimum\":5},{\"type\":\"integer\",\"minimum\":0}]}";
        String schema2 = "{\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id16() throws Exception {

        String schema1 = "{\"anyOf\":[{\"maximum\":10,\"type\":\"integer\",\"minimum\":5},{\"maximum\":3,\"type\":\"integer\",\"minimum\":0}]}";
        String schema2 = "{\"type\":\"integer\",\"minimum\":-1}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id17() throws Exception {

        String schema1 = "{\"anyOf\":[{\"maximum\":10,\"type\":\"integer\",\"minimum\":5},{\"maximum\":4,\"type\":\"integer\",\"minimum\":0}]}";
        String schema2 = "{\"maximum\":8,\"type\":\"integer\",\"minimum\":1}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id18() throws Exception {

        String schema1 = "{\"anyOf\":[{\"maximum\":10,\"type\":\"integer\",\"exclusiveMinimum\":true,\"minimum\":5},{\"maximum\":4,\"type\":\"integer\",\"minimum\":0}]}";
        String schema2 = "{\"maximum\":8,\"type\":\"integer\",\"minimum\":1}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id19() throws Exception {

        String schema1 = "{\"anyOf\":[{\"maximum\":10,\"type\":\"integer\",\"minimum\":0},{\"type\":\"integer\",\"minimum\":11}]}";
        String schema2 = "{\"type\":\"integer\",\"minimum\":0}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id20() throws Exception {

        String schema1 = "{\"anyOf\":[{\"multipleOf\":5,\"type\":\"integer\"},{\"type\":\"integer\"}]}";
        String schema2 = "{\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id21() throws Exception {

        String schema1 = "{\"enum\":[1,3,5,7,9,10]}";
        String schema2 = "{\"anyOf\":[{\"multipleOf\":10,\"maximum\":20,\"type\":\"integer\",\"minimum\":0},{\"multipleOf\":5,\"maximum\":10,\"type\":\"integer\",\"minimum\":1},{\"enum\":[1,3,7,9]}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id22() throws Exception {

        String schema1 = "{\"anyOf\":[{\"multipleOf\":5,\"type\":\"integer\"},{\"multipleOf\":7,\"type\":\"integer\"}]}";
        String schema2 = "{\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id23() throws Exception {

        String schema1 = "{\"anyOf\":[{\"multipleOf\":5,\"type\":\"integer\"},{\"multipleOf\":7,\"type\":\"integer\"}]}";
        String schema2 = "{\"multipleOf\":35,\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id24() throws Exception {

        String schema1 = "{\"anyOf\":[{\"multipleOf\":5,\"type\":\"integer\"},{\"multipleOf\":7,\"type\":\"integer\"}]}";
        String schema2 = "{\"multipleOf\":5,\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id25() throws Exception {

        String schema1 = "{\"anyOf\":[{\"multipleOf\":3,\"type\":\"integer\"},{\"multipleOf\":6,\"type\":\"integer\"}]}";
        String schema2 = "{\"multipleOf\":3,\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id26() throws Exception {

        String schema1 = "{\"anyOf\":[{\"multipleOf\":12,\"type\":\"integer\"},{\"multipleOf\":9,\"type\":\"integer\"}]}";
        String schema2 = "{\"multipleOf\":3,\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id27() throws Exception {

        String schema1 = "{\"anyOf\":[{\"multipleOf\":3,\"maximum\":10,\"type\":\"integer\"},{\"multipleOf\":5,\"type\":\"integer\"}]}";
        String schema2 = "{\"multipleOf\":3,\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id28() throws Exception {

        String schema1 = "{\"anyOf\":[{\"multipleOf\":5,\"maximum\":15,\"type\":\"integer\",\"minimum\":5},{\"multipleOf\":3,\"maximum\":15,\"type\":\"integer\",\"minimum\":5}]}";
        String schema2 = "{\"anyOf\":[{\"multipleOf\":3,\"maximum\":12,\"type\":\"integer\",\"minimum\":0},{\"multipleOf\":5,\"maximum\":20,\"type\":\"integer\",\"minimum\":1}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id29() throws Exception {

        String schema1 = "{\"multipleOf\":5,\"maximum\":10,\"type\":\"integer\",\"minimum\":-4}";
        String schema2 = "{\"anyOf\":[{\"multipleOf\":10,\"maximum\":20,\"type\":\"integer\",\"minimum\":0},{\"multipleOf\":5,\"maximum\":10,\"type\":\"integer\",\"minimum\":1}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id30() throws Exception {

        String schema1 = "{\"maximum\":10,\"type\":\"integer\"}";
        String schema2 = "{\"maximum\":5,\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id31() throws Exception {

        String schema1 = "{\"maximum\":10,\"type\":\"integer\"}";
        String schema2 = "{\"type\":\"integer\",\"minimum\":5}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id32() throws Exception {

        String schema1 = "{\"type\":\"integer\",\"minimum\":10}";
        String schema2 = "{\"maximum\":20,\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id33() throws Exception {

        String schema1 = "{\"maximum\":10,\"type\":\"integer\",\"minimum\":5}";
        String schema2 = "{\"maximum\":20,\"type\":\"integer\",\"minimum\":1}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id34() throws Exception {

        String schema1 = "{\"maximum\":20,\"type\":\"integer\",\"minimum\":5}";
        String schema2 = "{\"maximum\":20,\"type\":\"integer\",\"minimum\":10}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id35() throws Exception {

        String schema1 = "{\"maximum\":20,\"type\":\"integer\",\"minimum\":5}";
        String schema2 = "{\"maximum\":100,\"type\":\"integer\",\"minimum\":40}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id36() throws Exception {

        String schema1 = "{\"multipleOf\":15,\"maximum\":10,\"type\":\"integer\",\"minimum\":5}";
        String schema2 = "{\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id37() throws Exception {

        String schema1 = "{\"exclusiveMaximum\":true,\"maximum\":20,\"type\":\"integer\",\"exclusiveMinimum\":true,\"minimum\":5}";
        String schema2 = "{\"maximum\":19,\"type\":\"integer\",\"minimum\":6}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id38() throws Exception {

        String schema1 = "{\"exclusiveMaximum\":true,\"maximum\":20,\"type\":\"integer\",\"exclusiveMinimum\":true,\"minimum\":5}";
        String schema2 = "{\"maximum\":20,\"type\":\"integer\",\"minimum\":6}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id39() throws Exception {

        String schema1 = "{\"type\":\"integer\",\"minimum\":5}";
        String schema2 = "{\"type\":\"integer\",\"minimum\":1}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id40() throws Exception {

        String schema1 = "{\"multipleOf\":10,\"type\":\"integer\"}";
        String schema2 = "{\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id41() throws Exception {

        String schema1 = "{\"multipleOf\":10,\"type\":\"integer\"}";
        String schema2 = "{\"multipleOf\":5,\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id42() throws Exception {

        String schema1 = "{\"multipleOf\":10,\"type\":\"integer\"}";
        String schema2 = "{\"multipleOf\":98,\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id43() throws Exception {

        String schema1 = "{\"multipleOf\":10,\"type\":\"integer\"}";
        String schema2 = "{\"type\":\"integer\",\"minimum\":5}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id44() throws Exception {

        String schema1 = "{\"multipleOf\":10,\"type\":\"integer\",\"minimum\":10}";
        String schema2 = "{\"type\":\"integer\",\"minimum\":5}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id45() throws Exception {

        String schema1 = "{\"multipleOf\":10,\"type\":\"integer\",\"minimum\":10}";
        String schema2 = "{\"maximum\":500,\"type\":\"integer\",\"minimum\":5}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id46() throws Exception {

        String schema1 = "{\"maximum\":20,\"type\":\"integer\",\"exclusiveMinimum\":true,\"minimum\":5}";
        String schema2 = "{\"maximum\":20,\"type\":\"integer\",\"minimum\":5}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id47() throws Exception {

        String schema1 = "{\"maximum\":20,\"type\":\"integer\",\"exclusiveMinimum\":true,\"minimum\":5}";
        String schema2 = "{\"exclusiveMaximum\":true,\"maximum\":20,\"type\":\"integer\",\"minimum\":5}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id48() throws Exception {

        String schema1 = "{\"exclusiveMaximum\":true,\"maximum\":20,\"type\":\"integer\",\"exclusiveMinimum\":true,\"minimum\":5}";
        String schema2 = "{\"maximum\":20,\"type\":\"integer\",\"minimum\":5}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id49() throws Exception {

        String schema1 = "{\"exclusiveMaximum\":true,\"maximum\":20,\"type\":\"integer\",\"exclusiveMinimum\":false,\"minimum\":5}";
        String schema2 = "{\"exclusiveMaximum\":true,\"maximum\":20,\"type\":\"integer\",\"exclusiveMinimum\":true,\"minimum\":5}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id50() throws Exception {

        String schema1 = "{\"type\":\"number\"}";
        String schema2 = "{\"type\":\"number\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id51() throws Exception {

        String schema1 = "{\"maximum\":10,\"type\":\"number\"}";
        String schema2 = "{\"maximum\":5,\"type\":\"number\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id52() throws Exception {

        String schema1 = "{\"maximum\":10,\"type\":\"number\"}";
        String schema2 = "{\"type\":\"number\",\"minimum\":5}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id53() throws Exception {

        String schema1 = "{\"type\":\"number\",\"minimum\":10}";
        String schema2 = "{\"maximum\":20,\"type\":\"number\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id54() throws Exception {

        String schema1 = "{\"maximum\":10,\"type\":\"number\",\"minimum\":5}";
        String schema2 = "{\"maximum\":20,\"type\":\"number\",\"minimum\":1}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id55() throws Exception {

        String schema1 = "{\"maximum\":20,\"type\":\"number\",\"minimum\":5}";
        String schema2 = "{\"maximum\":20,\"type\":\"number\",\"minimum\":10}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id56() throws Exception {

        String schema1 = "{\"maximum\":20,\"type\":\"number\",\"minimum\":5}";
        String schema2 = "{\"maximum\":100,\"type\":\"number\",\"minimum\":40}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id57() throws Exception {

        String schema1 = "{\"exclusiveMaximum\":true,\"maximum\":20,\"type\":\"number\",\"exclusiveMinimum\":true,\"minimum\":5}";
        String schema2 = "{\"maximum\":19,\"type\":\"number\",\"minimum\":6}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id58() throws Exception {

        String schema1 = "{\"exclusiveMaximum\":true,\"maximum\":20,\"type\":\"number\",\"exclusiveMinimum\":true,\"minimum\":5}";
        String schema2 = "{\"maximum\":20,\"type\":\"number\",\"minimum\":6}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id59() throws Exception {

        String schema1 = "{\"type\":\"number\",\"minimum\":5}";
        String schema2 = "{\"type\":\"number\",\"minimum\":1}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id60() throws Exception {

        String schema1 = "{\"multipleOf\":10.5,\"type\":\"number\"}";
        String schema2 = "{\"type\":\"number\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id61() throws Exception {

        String schema1 = "{\"multipleOf\":1.5,\"type\":\"number\"}";
        String schema2 = "{\"multipleOf\":6,\"type\":\"number\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id62() throws Exception {

        String schema1 = "{\"multipleOf\":0.5,\"type\":\"number\"}";
        String schema2 = "{\"multipleOf\":-0.5,\"type\":\"number\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id63() throws Exception {

        String schema1 = "{\"multipleOf\":1,\"type\":\"number\"}";
        String schema2 = "{\"type\":\"number\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id64() throws Exception {

        String schema1 = "{\"multipleOf\":10,\"type\":\"number\"}";
        String schema2 = "{\"type\":\"number\",\"minimum\":5}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id65() throws Exception {

        String schema1 = "{\"multipleOf\":10,\"type\":\"number\",\"minimum\":10}";
        String schema2 = "{\"type\":\"number\",\"minimum\":5}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id66() throws Exception {

        String schema1 = "{\"multipleOf\":10,\"type\":\"number\",\"minimum\":10}";
        String schema2 = "{\"maximum\":500,\"type\":\"number\",\"minimum\":5}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id67() throws Exception {

        String schema1 = "{\"maximum\":20,\"type\":\"number\",\"exclusiveMinimum\":true,\"minimum\":5}";
        String schema2 = "{\"maximum\":20,\"type\":\"number\",\"minimum\":5}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id68() throws Exception {

        String schema1 = "{\"maximum\":20,\"type\":\"number\",\"exclusiveMinimum\":true,\"minimum\":5}";
        String schema2 = "{\"exclusiveMaximum\":true,\"maximum\":20,\"type\":\"number\",\"minimum\":5}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id69() throws Exception {

        String schema1 = "{\"exclusiveMaximum\":true,\"maximum\":20,\"type\":\"number\",\"exclusiveMinimum\":true,\"minimum\":5}";
        String schema2 = "{\"maximum\":20,\"type\":\"number\",\"minimum\":5}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id70() throws Exception {

        String schema1 = "{\"exclusiveMaximum\":true,\"maximum\":20,\"type\":\"number\",\"exclusiveMinimum\":false,\"minimum\":5}";
        String schema2 = "{\"exclusiveMaximum\":true,\"maximum\":20,\"type\":\"number\",\"exclusiveMinimum\":true,\"minimum\":5}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id71() throws Exception {

        String schema1 = "{\"maximum\":10}";
        String schema2 = "{\"maximum\":10}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id72() throws Exception {

        String schema1 = "{\"type\":\"integer\"}";
        String schema2 = "{\"type\":\"number\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id73() throws Exception {

        String schema1 = "{\"type\":\"number\",\"minimum\":1.5}";
        String schema2 = "{\"type\":\"integer\",\"minimum\":1}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id74() throws Exception {

        String schema1 = "{\"multipleOf\":10,\"type\":\"number\"}";
        String schema2 = "{\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id75() throws Exception {

        String schema1 = "{\"multipleOf\":1,\"type\":\"number\"}";
        String schema2 = "{\"type\":\"integer\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_numeric_id76() throws Exception {

        String schema1 = "{\"multipleOf\":10,\"type\":\"number\"}";
        String schema2 = "{\"type\":\"integer\",\"minimum\":5}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id1() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id2() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id3() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_components\":{\"enum\":[null,\"mle\"]},\"svd_solver\":{\"enum\":[\"full\"]},\"whiten\":{\"default\":false,\"type\":\"boolean\"}},\"required\":[\"svd_solver\",\"n_components\",\"whiten\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_components\":{\"enum\":[null,\"mle\"]},\"svd_solver\":{\"enum\":[\"auto\",\"full\"]},\"whiten\":{\"default\":false,\"type\":\"boolean\"}},\"required\":[\"svd_solver\",\"n_components\",\"whiten\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id4() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_components\":{\"enum\":[null,\"mle\"]},\"svd_solver\":{\"enum\":[\"auto\",\"full\"]},\"whiten\":{\"default\":false,\"type\":\"boolean\"}},\"required\":[\"svd_solver\",\"n_components\",\"whiten\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_components\":{\"enum\":[null,\"mle\"]},\"svd_solver\":{\"enum\":[\"full\"]},\"whiten\":{\"default\":false,\"type\":\"boolean\"}},\"required\":[\"svd_solver\",\"n_components\",\"whiten\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id5() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_components\":{\"enum\":[null,\"mle\"]},\"svd_solver\":{\"enum\":[\"full\"]},\"whiten\":{\"default\":false,\"type\":\"boolean\"}},\"required\":[\"svd_solver\",\"n_components\",\"whiten\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_components\":{\"enum\":[null,\"mle\"]},\"svd_solver\":{\"enum\":[\"auto\",\"full\"]},\"whiten\":{\"default\":false,\"type\":\"boolean\"}},\"required\":[\"svd_solver\",\"n_components\",\"whiten\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id6() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id7() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id8() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id9() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id10() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id11() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id12() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id13() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id14() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id15() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id16() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id17() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id18() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id19() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id20() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id21() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id22() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id23() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id24() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id25() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id26() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id27() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id28() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id29() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id30() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id31() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id32() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id33() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id34() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"minimumForOptimizer\":1.0E-10,\"maximumForOptimizer\":1,\"default\":0.9,\"type\":\"number\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id35() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id36() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id37() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id38() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"ls\",\"lad\",\"huber\",\"quantile\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"alpha\":{\"default\":0.9,\"enum\":[0.9]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id39() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id40() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id41() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id42() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id43() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id44() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id45() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id46() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id47() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id48() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id49() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id50() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id51() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id52() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id53() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id54() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id55() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id56() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id57() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id58() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id59() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id60() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id61() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id62() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id63() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id64() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id65() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id66() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id67() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id68() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id69() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id70() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id71() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id72() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id73() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id74() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id75() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id76() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id77() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"boosting_type\":{\"not\":{\"enum\":[\"rf\"]}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{\"enum\":[1]}},\"subsample_freq\":{\"not\":{\"enum\":[0]}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id78() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id79() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{\"enum\":[1]}},\"subsample_freq\":{\"not\":{\"enum\":[0]}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"boosting_type\":{\"not\":{\"enum\":[\"rf\"]}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id80() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"boosting_type\":{\"not\":{\"enum\":[\"rf\",\"goss\"]}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{\"enum\":[1]}},\"subsample_freq\":{\"not\":{\"enum\":[0]}},\"boosting_type\":{\"not\":{\"enum\":[\"goss\"]}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id81() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"boosting_type\":{\"not\":{\"enum\":[\"rf\",\"goss\"]}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{}},\"subsample_freq\":{\"not\":{}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id82() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{\"enum\":[1]}},\"subsample_freq\":{\"not\":{\"enum\":[0]}},\"boosting_type\":{\"not\":{\"enum\":[\"goss\"]}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"boosting_type\":{\"not\":{\"enum\":[\"rf\",\"goss\"]}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id83() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{\"enum\":[1]}},\"subsample_freq\":{\"not\":{\"enum\":[0]}},\"boosting_type\":{\"not\":{\"enum\":[\"goss\"]}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"enum\":[1]},\"boosting_type\":{\"not\":{\"enum\":[\"rf\"]}},\"subsample_freq\":{\"enum\":[0]}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id84() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{\"enum\":[1]}},\"subsample_freq\":{\"not\":{\"enum\":[0]}},\"boosting_type\":{\"not\":{\"enum\":[\"goss\"]}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{}},\"subsample_freq\":{\"not\":{}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id85() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{}},\"subsample_freq\":{\"not\":{}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"boosting_type\":{\"not\":{\"enum\":[\"rf\",\"goss\"]}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id86() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{}},\"subsample_freq\":{\"not\":{}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"enum\":[1]},\"boosting_type\":{\"not\":{\"enum\":[\"rf\"]}},\"subsample_freq\":{\"enum\":[0]}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id87() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{}},\"subsample_freq\":{\"not\":{}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{\"enum\":[1]}},\"subsample_freq\":{\"not\":{\"enum\":[0]}},\"boosting_type\":{\"not\":{\"enum\":[\"goss\"]}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id88() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"type\":\"integer\"},\"min_child_samples\":{\"minimumForOptimizer\":1,\"maximumForOptimizer\":20,\"default\":20,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":50,\"maximumForOptimizer\":500,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.1,\"default\":1,\"maximum\":1,\"type\":\"number\",\"exclusiveMinimum\":true,\"minimum\":0},\"boosting_type\":{\"enum\":[\"gbdt\",\"dart\"]},\"subsample_freq\":{\"minimumForOptimizer\":0,\"maximumForOptimizer\":10,\"default\":0,\"type\":\"integer\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"}},\"required\":[\"min_child_samples\",\"max_depth\",\"n_estimators\",\"subsample_freq\",\"boosting_type\",\"subsample\",\"learning_rate\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"type\":\"integer\"},\"min_child_samples\":{\"minimumForOptimizer\":1,\"maximumForOptimizer\":20,\"default\":20,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":50,\"maximumForOptimizer\":500,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.1,\"exclusiveMaximum\":true,\"default\":1,\"maximum\":1,\"type\":\"number\",\"exclusiveMinimum\":true,\"minimum\":0},\"boosting_type\":{\"enum\":[\"gbdt\",\"dart\",\"rf\"]},\"subsample_freq\":{\"allOf\":[{\"minimumForOptimizer\":0,\"maximumForOptimizer\":10,\"default\":0,\"type\":\"integer\",\"exclusiveMinimumForOptimizer\":true},{\"not\":{\"enum\":[0]}}]},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"}},\"required\":[\"min_child_samples\",\"max_depth\",\"n_estimators\",\"subsample_freq\",\"boosting_type\",\"subsample\",\"learning_rate\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id89() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id90() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"type\":\"integer\"},\"min_child_samples\":{\"minimumForOptimizer\":1,\"maximumForOptimizer\":20,\"default\":20,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":50,\"maximumForOptimizer\":500,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.1,\"exclusiveMaximum\":true,\"default\":1,\"maximum\":1,\"type\":\"number\",\"exclusiveMinimum\":true,\"minimum\":0},\"boosting_type\":{\"enum\":[\"gbdt\",\"dart\",\"rf\"]},\"subsample_freq\":{\"allOf\":[{\"minimumForOptimizer\":0,\"maximumForOptimizer\":10,\"default\":0,\"type\":\"integer\",\"exclusiveMinimumForOptimizer\":true},{\"not\":{\"enum\":[0]}}]},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"}},\"required\":[\"min_child_samples\",\"max_depth\",\"n_estimators\",\"subsample_freq\",\"boosting_type\",\"subsample\",\"learning_rate\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"type\":\"integer\"},\"min_child_samples\":{\"minimumForOptimizer\":1,\"maximumForOptimizer\":20,\"default\":20,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":50,\"maximumForOptimizer\":500,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.1,\"default\":1,\"maximum\":1,\"type\":\"number\",\"exclusiveMinimum\":true,\"minimum\":0},\"boosting_type\":{\"enum\":[\"gbdt\",\"dart\"]},\"subsample_freq\":{\"minimumForOptimizer\":0,\"maximumForOptimizer\":10,\"default\":0,\"type\":\"integer\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"}},\"required\":[\"min_child_samples\",\"max_depth\",\"n_estimators\",\"subsample_freq\",\"boosting_type\",\"subsample\",\"learning_rate\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id91() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"boosting_type\":{\"not\":{\"enum\":[\"rf\"]}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{\"enum\":[1]}},\"subsample_freq\":{\"not\":{\"enum\":[0]}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id92() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{\"enum\":[1]}},\"subsample_freq\":{\"not\":{\"enum\":[0]}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"boosting_type\":{\"not\":{\"enum\":[\"rf\"]}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id93() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"boosting_type\":{\"not\":{\"enum\":[\"rf\",\"goss\"]}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{\"enum\":[1]}},\"subsample_freq\":{\"not\":{\"enum\":[0]}},\"boosting_type\":{\"not\":{\"enum\":[\"goss\"]}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id94() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"boosting_type\":{\"not\":{\"enum\":[\"rf\",\"goss\"]}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{}},\"subsample_freq\":{\"not\":{}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id95() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{\"enum\":[1]}},\"subsample_freq\":{\"not\":{\"enum\":[0]}},\"boosting_type\":{\"not\":{\"enum\":[\"goss\"]}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"boosting_type\":{\"not\":{\"enum\":[\"rf\",\"goss\"]}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id96() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{\"enum\":[1]}},\"subsample_freq\":{\"not\":{\"enum\":[0]}},\"boosting_type\":{\"not\":{\"enum\":[\"goss\"]}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"enum\":[1]},\"boosting_type\":{\"not\":{\"enum\":[\"rf\"]}},\"subsample_freq\":{\"enum\":[0]}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id97() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{\"enum\":[1]}},\"subsample_freq\":{\"not\":{\"enum\":[0]}},\"boosting_type\":{\"not\":{\"enum\":[\"goss\"]}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{}},\"subsample_freq\":{\"not\":{}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id98() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{}},\"subsample_freq\":{\"not\":{}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"boosting_type\":{\"not\":{\"enum\":[\"rf\",\"goss\"]}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id99() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{}},\"subsample_freq\":{\"not\":{}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"enum\":[1]},\"boosting_type\":{\"not\":{\"enum\":[\"rf\"]}},\"subsample_freq\":{\"enum\":[0]}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id100() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id101() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{}},\"subsample_freq\":{\"not\":{}}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"subsample\":{\"not\":{\"enum\":[1]}},\"subsample_freq\":{\"not\":{\"enum\":[0]}},\"boosting_type\":{\"not\":{\"enum\":[\"goss\"]}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id102() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"type\":\"integer\"},\"min_child_samples\":{\"minimumForOptimizer\":1,\"maximumForOptimizer\":20,\"default\":20,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":50,\"maximumForOptimizer\":500,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.1,\"default\":1,\"maximum\":1,\"type\":\"number\",\"exclusiveMinimum\":true,\"minimum\":0},\"boosting_type\":{\"enum\":[\"gbdt\",\"dart\"]},\"subsample_freq\":{\"minimumForOptimizer\":0,\"maximumForOptimizer\":10,\"default\":0,\"type\":\"integer\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"}},\"required\":[\"min_child_samples\",\"max_depth\",\"n_estimators\",\"subsample_freq\",\"boosting_type\",\"subsample\",\"learning_rate\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"type\":\"integer\"},\"min_child_samples\":{\"minimumForOptimizer\":1,\"maximumForOptimizer\":20,\"default\":20,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":50,\"maximumForOptimizer\":500,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.1,\"exclusiveMaximum\":true,\"default\":1,\"maximum\":1,\"type\":\"number\",\"exclusiveMinimum\":true,\"minimum\":0},\"boosting_type\":{\"enum\":[\"gbdt\",\"dart\",\"rf\"]},\"subsample_freq\":{\"allOf\":[{\"minimumForOptimizer\":0,\"maximumForOptimizer\":10,\"default\":0,\"type\":\"integer\",\"exclusiveMinimumForOptimizer\":true},{\"not\":{\"enum\":[0]}}]},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"}},\"required\":[\"min_child_samples\",\"max_depth\",\"n_estimators\",\"subsample_freq\",\"boosting_type\",\"subsample\",\"learning_rate\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id103() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"type\":\"integer\"},\"min_child_samples\":{\"minimumForOptimizer\":1,\"maximumForOptimizer\":20,\"default\":20,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":50,\"maximumForOptimizer\":500,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.1,\"exclusiveMaximum\":true,\"default\":1,\"maximum\":1,\"type\":\"number\",\"exclusiveMinimum\":true,\"minimum\":0},\"boosting_type\":{\"enum\":[\"gbdt\",\"dart\",\"rf\"]},\"subsample_freq\":{\"allOf\":[{\"minimumForOptimizer\":0,\"maximumForOptimizer\":10,\"default\":0,\"type\":\"integer\",\"exclusiveMinimumForOptimizer\":true},{\"not\":{\"enum\":[0]}}]},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"}},\"required\":[\"min_child_samples\",\"max_depth\",\"n_estimators\",\"subsample_freq\",\"boosting_type\",\"subsample\",\"learning_rate\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"type\":\"integer\"},\"min_child_samples\":{\"minimumForOptimizer\":1,\"maximumForOptimizer\":20,\"default\":20,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":50,\"maximumForOptimizer\":500,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.1,\"default\":1,\"maximum\":1,\"type\":\"number\",\"exclusiveMinimum\":true,\"minimum\":0},\"boosting_type\":{\"enum\":[\"gbdt\",\"dart\"]},\"subsample_freq\":{\"minimumForOptimizer\":0,\"maximumForOptimizer\":10,\"default\":0,\"type\":\"integer\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"}},\"required\":[\"min_child_samples\",\"max_depth\",\"n_estimators\",\"subsample_freq\",\"boosting_type\",\"subsample\",\"learning_rate\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id104() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"enum\":[\"euclidean\"]}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id105() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"enum\":[\"euclidean\"]}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id106() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"complete\",\"average\",\"single\"]},\"affinity\":{\"enum\":[\"euclidean\",\"l1\",\"l2\",\"manhattan\",\"cosine\",\"precomputed\"]}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id107() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"complete\",\"average\",\"single\"]},\"affinity\":{\"enum\":[\"euclidean\",\"l1\",\"l2\",\"manhattan\",\"cosine\",\"precomputed\"]}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id108() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"enum\":[\"euclidean\"]}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id109() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"enum\":[\"euclidean\"]}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id110() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"complete\",\"average\",\"single\"]},\"affinity\":{\"enum\":[\"euclidean\",\"l1\",\"l2\",\"manhattan\",\"cosine\",\"precomputed\"]}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id111() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"enum\":[\"auto\"]},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id112() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"complete\",\"average\",\"single\"]},\"affinity\":{\"enum\":[\"euclidean\",\"l1\",\"l2\",\"manhattan\",\"cosine\",\"precomputed\"]}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id113() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"enum\":[\"euclidean\"]}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id114() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"complete\",\"average\",\"single\"]}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id115() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"enum\":[\"euclidean\"]}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id116() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"complete\",\"average\",\"single\"]},\"affinity\":{\"enum\":[\"euclidean\",\"l1\",\"l2\",\"manhattan\",\"cosine\",\"precomputed\"]}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id117() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"complete\",\"average\",\"single\"]},\"affinity\":{\"forOptimizer\":{\"not\":{}},\"type\":\"object\"}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id118() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id119() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"complete\",\"average\",\"single\"]},\"affinity\":{\"forOptimizer\":false,\"type\":\"object\"}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id120() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"complete\",\"average\",\"single\"]},\"affinity\":{\"forOptimizer\":{\"not\":{}},\"type\":\"object\"}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id121() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"complete\",\"average\",\"single\"]},\"affinity\":{\"forOptimizer\":{\"not\":{}},\"type\":\"object\"}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id122() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"enum\":[null]},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"tol\":{\"minimumForOptimizer\":1.0E-8,\"maximumForOptimizer\":0.01,\"default\":1.0E-4,\"type\":\"number\"},\"loss\":{\"enum\":[\"deviance\",\"exponential\"]},\"max_features\":{\"enum\":[\"auto\",\"sqrt\",\"log2\",null]},\"min_samples_split\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"},\"max_depth\":{\"minimumForOptimizer\":3,\"maximumForOptimizer\":5,\"default\":3,\"type\":\"integer\"},\"presort\":{\"type\":\"boolean\"},\"n_iter_no_change\":{\"minimumForOptimizer\":5,\"maximumForOptimizer\":10,\"type\":\"integer\"},\"n_estimators\":{\"minimumForOptimizer\":10,\"maximumForOptimizer\":100,\"default\":100,\"type\":\"integer\"},\"subsample\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":1,\"type\":\"number\"},\"learning_rate\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":1,\"default\":0.1,\"type\":\"number\"},\"min_samples_leaf\":{\"minimumForOptimizer\":0.01,\"maximumForOptimizer\":0.5,\"type\":\"number\"}},\"required\":[\"presort\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id123() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"enum\":[\"euclidean\"]}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id124() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"complete\",\"average\",\"single\"]},\"affinity\":{\"enum\":[\"euclidean\",\"l1\",\"l2\",\"manhattan\",\"cosine\",\"precomputed\"]}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id125() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"enum\":[\"euclidean\"]}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id126() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"complete\",\"average\",\"single\"]},\"affinity\":{\"enum\":[\"euclidean\",\"l1\",\"l2\",\"manhattan\",\"cosine\",\"precomputed\"]}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id127() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id128() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"complete\",\"average\",\"single\"]},\"affinity\":{\"forOptimizer\":{\"not\":{}},\"type\":\"object\"}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id129() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"complete\",\"average\",\"single\"]},\"affinity\":{\"forOptimizer\":{\"not\":{}},\"type\":\"object\"}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id130() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"complete\",\"average\",\"single\"]},\"affinity\":{\"forOptimizer\":{\"not\":{}},\"type\":\"object\"}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"enum\":[\"auto\"]},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id131() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"complete\",\"average\",\"single\"]},\"affinity\":{\"forOptimizer\":{\"not\":{}},\"type\":\"object\"}},\"required\":[\"compute_full_tree\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_clusters\":{\"minimumForOptimizer\":2,\"maximumForOptimizer\":8,\"default\":2,\"type\":\"integer\"},\"compute_full_tree\":{\"type\":\"boolean\"},\"linkage\":{\"enum\":[\"ward\",\"complete\",\"average\",\"single\"]},\"affinity\":{\"not\":{}}},\"required\":[\"compute_full_tree\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_from_lale_id132() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_components\":{\"enum\":[null,\"mle\"]},\"svd_solver\":{\"enum\":[\"auto\",\"full\"]},\"whiten\":{\"default\":false,\"type\":\"boolean\"}},\"required\":[\"svd_solver\",\"n_components\",\"whiten\"]}";
        String schema2 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"n_components\":{\"enum\":[null,\"mle\"]},\"svd_solver\":{\"enum\":[\"full\"]},\"whiten\":{\"default\":false,\"type\":\"boolean\"}},\"required\":[\"svd_solver\",\"n_components\",\"whiten\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_array_id1() throws Exception {

        String schema1 = "{\"type\":\"array\",\"items\":{\"type\":\"string\"}}";
        String schema2 = "{\"type\":\"array\",\"items\":[{\"type\":\"string\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_array_id2() throws Exception {

        String schema1 = "{\"type\":\"array\",\"items\":{\"type\":\"string\"}}";
        String schema2 = "{\"type\":\"array\",\"items\":[{\"type\":\"string\"},{\"type\":\"string\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_array_id3() throws Exception {

        String schema1 = "{\"type\":\"array\",\"items\":[{\"type\":\"string\"}]}";
        String schema2 = "{\"type\":\"array\",\"items\":[{\"type\":\"string\"},{\"type\":\"number\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_array_id4() throws Exception {

        String schema1 = "{\"additionalItems\":false,\"type\":\"array\",\"items\":[{\"type\":\"string\"}]}";
        String schema2 = "{\"type\":\"array\",\"items\":[{\"type\":\"string\"},{\"type\":\"number\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_array_id5() throws Exception {

        String schema1 = "{\"additionalItems\":true,\"type\":\"array\",\"items\":[{\"type\":\"string\"}]}";
        String schema2 = "{\"type\":\"array\",\"items\":[{\"type\":\"string\"},{\"type\":\"number\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_array_id6() throws Exception {

        String schema1 = "{\"additionalItems\":{},\"type\":\"array\",\"items\":[{\"type\":\"string\"}]}";
        String schema2 = "{\"type\":\"array\",\"items\":[{\"type\":\"string\"},{\"type\":\"number\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_array_id7() throws Exception {

        String schema1 = "{\"type\":\"array\"}";
        String schema2 = "{\"type\":\"array\",\"items\":{}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_array_id8() throws Exception {

        String schema1 = "{\"additionalItems\":false,\"type\":\"array\"}";
        String schema2 = "{\"type\":\"array\",\"items\":{}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_array_id9() throws Exception {

        String schema1 = "{\"additionalItems\":false,\"type\":\"array\",\"items\":[{},{}]}";
        String schema2 = "{\"type\":\"array\",\"items\":{}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_array_id10() throws Exception {

        String schema1 = "{\"additionalItems\":true,\"type\":\"array\",\"items\":[{},{}]}";
        String schema2 = "{\"type\":\"array\",\"items\":{}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_array_id11() throws Exception {

        String schema1 = "{\"additionalItems\":false,\"type\":\"array\",\"items\":[{},{}]}";
        String schema2 = "{\"additionalItems\":false,\"type\":\"array\",\"items\":[{}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_array_id12() throws Exception {

        String schema1 = "{\"minItems\":5,\"maxItems:\":10,\"type\":\"array\"}";
        String schema2 = "{\"minItems\":5,\"maxItems:\":10,\"type\":\"array\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_array_id13() throws Exception {

        String schema1 = "{\"minItems\":5,\"maxItems:\":10,\"type\":\"array\"}";
        String schema2 = "{\"minItems\":1,\"maxItems:\":20,\"type\":\"array\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_array_id14() throws Exception {

        String schema1 = "{\"uniqueItems\":true,\"type\":\"array\"}";
        String schema2 = "{\"uniqueItems\":false,\"type\":\"array\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_array_id15() throws Exception {

        String schema1 = "{\"minItems\":150,\"maxItems\":150,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":4,\"maxItems\":4,\"type\":\"array\",\"items\":{\"type\":\"number\"}}}";
        String schema2 = "{\"description\":\"Features; the outer array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"minItems\":1,\"maxItems\":1,\"type\":\"array\",\"items\":{\"type\":\"string\"}}}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id1() throws Exception {

        String schema1 = "{\"allOf\":[{\"type\":\"string\"},{\"pattern\":\"a\",\"type\":\"string\"}]}";
        String schema2 = "{\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id2() throws Exception {

        String schema1 = "{\"allOf\":[{\"minimum\":10},{\"maximum\":20}]}";
        String schema2 = "{\"maximum\":20,\"minimum\":10}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id3() throws Exception {

        String schema1 = "{\"not\":{\"allOf\":[{\"type\":\"string\"},{\"pattern\":\"a\",\"type\":\"string\"}]}}";
        String schema2 = "{\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id4() throws Exception {

        String schema1 = "{\"not\":{\"allOf\":[{\"type\":\"string\"},{\"pattern\":\"a\",\"type\":\"string\"}]}}";
        String schema2 = "{\"anyOf\":[{\"type\":\"integer\"},{\"type\":\"number\"},{\"type\":\"boolean\"},{\"type\":\"array\"},{\"type\":\"object\"},{\"type\":\"string\"},{\"type\":\"null\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id5() throws Exception {

        String schema1 = "{\"not\":{\"allOf\":[{\"type\":\"string\"},{\"pattern\":\"a\",\"type\":\"string\"}]}}";
        String schema2 = "{\"anyOf\":[{\"type\":\"integer\"},{\"type\":\"number\"},{\"type\":\"boolean\"},{\"type\":\"array\"},{\"type\":\"object\"},{\"pattern\":\"^[^a]*$\",\"type\":\"string\"},{\"type\":\"null\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id6() throws Exception {

        String schema1 = "{\"not\":{\"allOf\":[{\"type\":\"string\"},{\"type\":\"boolean\"}]}}";
        String schema2 = "{}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id7() throws Exception {

        String schema1 = "{\"not\":{\"anyOf\":[{\"type\":\"string\"},{\"type\":\"null\"}]}}";
        String schema2 = "{\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id8() throws Exception {

        String schema1 = "{\"not\":{\"anyOf\":[{\"type\":\"string\"},{\"type\":\"null\"}]}}";
        String schema2 = "{\"anyOf\":[{\"type\":\"integer\"},{\"type\":\"number\"},{\"type\":\"boolean\"},{\"type\":\"array\"},{\"type\":\"object\"},{\"type\":\"string\"},{\"type\":\"null\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id9() throws Exception {

        String schema1 = "{\"not\":{\"oneOf\":[{\"type\":\"string\"},{\"type\":\"null\"}]}}";
        String schema2 = "{\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id10() throws Exception {

        String schema1 = "{\"not\":{\"oneOf\":[{\"enum\":[1,2,3]},{\"enum\":[1,2]}]}}";
        String schema2 = "{\"not\":{\"enum\":[3]}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id11() throws Exception {

        String schema1 = "{\"allOf\":[{\"type\":\"integer\"},{\"enum\":[5]}],\"not\":{\"type\":\"string\"}}";
        String schema2 = "{\"enum\":[5]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id12() throws Exception {

        String schema1 = "{\"not\":{\"type\":\"string\"},\"anyOf\":[{\"type\":\"integer\"},{\"type\":\"boolean\"}]}";
        String schema2 = "{\"type\":[\"integer\",\"boolean\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id13() throws Exception {

        String schema1 = "{\"allOf\":[{\"minimum\":10}],\"not\":{\"type\":\"string\"},\"anyOf\":[{\"type\":\"integer\"},{\"type\":\"boolean\"}]}";
        String schema2 = "{\"type\":[\"integer\",\"boolean\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id14() throws Exception {

        String schema1 = "{\"allOf\":[{\"minimum\":10},{\"maximum\":20}],\"anyOf\":[{\"type\":\"integer\"},{\"type\":\"boolean\"}]}";
        String schema2 = "{\"maximum\":20,\"type\":[\"integer\",\"boolean\"],\"minimum\":10}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id15() throws Exception {

        String schema1 = "{\"oneOf\":[{\"enum\":[1,2,3]},{\"enum\":[1,2]}]}";
        String schema2 = "{\"enum\":[3]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id16() throws Exception {

        String schema1 = "{\"oneOf\":[{\"enum\":[1,2,3]},{\"enum\":[1,2]}]}";
        String schema2 = "{\"enum\":[1,2]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id17() throws Exception {

        String schema1 = "{\"oneOf\":[{\"type\":\"string\"},{}]}";
        String schema2 = "{\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id18() throws Exception {

        String schema1 = "{\"oneOf\":[{\"type\":\"string\"},{}]}";
        String schema2 = "{\"not\":{\"type\":\"string\"}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id19() throws Exception {

        String schema1 = "{\"oneOf\":[{\"type\":\"boolean\"},{\"enum\":[true]}]}";
        String schema2 = "{\"enum\":[false]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id20() throws Exception {

        String schema1 = "{\"allOf\":[{\"type\":\"string\"}]}";
        String schema2 = "{\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id21() throws Exception {

        String schema1 = "{\"allOf\":[{\"type\":\"string\"}]}";
        String schema2 = "{\"oneOf\":[{\"type\":\"string\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id22() throws Exception {

        String schema1 = "{\"anyOf\":[{\"type\":\"string\"}]}";
        String schema2 = "{\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_boolean_id23() throws Exception {

        String schema1 = "{\"oneOf\":[{\"type\":\"string\"}]}";
        String schema2 = "{\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id1() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"title\":\"Transaction Schema\",\"definitions\":{\"output\":{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"amount\",\"condition\",\"public_keys\"],\"properties\":{\"public_keys\":{\"$ref\":\"#\\/definitions\\/public_keys\"},\"amount\":{\"pattern\":\"^[0-9]{1,20}$\",\"type\":\"string\"},\"condition\":{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"details\",\"uri\"],\"properties\":{\"details\":{\"$ref\":\"#\\/definitions\\/condition_details\"},\"uri\":{\"pattern\":\"^ni:\\/\\/\\/sha-256;([a-zA-Z0-9_-]{0,86})[?](fpt=(ed25519|threshold)-sha-256(&)?|cost=[0-9]+(&)?|subtypes=ed25519-sha-256(&)?){2,3}$\",\"type\":\"string\"}}}}},\"public_keys\":{\"anyOf\":[{\"type\":\"array\",\"items\":{\"$ref\":\"#\\/definitions\\/base58\"}},{\"type\":\"null\"}]},\"input\":{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"owners_before\",\"fulfillment\"],\"properties\":{\"owners_before\":{\"$ref\":\"#\\/definitions\\/public_keys\"},\"fulfills\":{\"anyOf\":[{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"output_index\",\"transaction_id\"],\"properties\":{\"transaction_id\":{\"$ref\":\"#\\/definitions\\/sha3_hexdigest\"},\"output_index\":{\"$ref\":\"#\\/definitions\\/offset\"}}},{\"type\":\"null\"}]},\"fulfillment\":{\"anyOf\":[{\"pattern\":\"^[a-zA-Z0-9_-]*$\",\"type\":\"string\"},{\"$ref\":\"#\\/definitions\\/condition_details\"}]}}},\"metadata\":{\"anyOf\":[{\"additionalProperties\":true,\"type\":\"object\",\"minProperties\":1},{\"type\":\"null\"}]},\"condition_details\":{\"anyOf\":[{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"type\",\"public_key\"],\"properties\":{\"public_key\":{\"$ref\":\"#\\/definitions\\/base58\"},\"type\":{\"pattern\":\"^ed25519-sha-256$\",\"type\":\"string\"}}},{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"type\",\"threshold\",\"subconditions\"],\"properties\":{\"subconditions\":{\"type\":\"array\",\"items\":{\"anyOf\":[{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"type\",\"public_key\"],\"properties\":{\"public_key\":{\"$ref\":\"#\\/definitions\\/base58\"},\"type\":{\"pattern\":\"^ed25519-sha-256$\",\"type\":\"string\"}}},{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"type\",\"threshold\",\"subconditions\"],\"properties\":{\"subconditions\":{\"type\":\"array\",\"items\":{\"anyOf\":[{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"type\",\"public_key\"],\"properties\":{\"public_key\":{\"$ref\":\"#\\/definitions\\/base58\"},\"type\":{\"pattern\":\"^ed25519-sha-256$\",\"type\":\"string\"}}},{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"type\",\"threshold\",\"subconditions\"],\"properties\":{\"subconditions\":{\"type\":\"array\",\"items\":{}},\"threshold\":{\"maximum\":100,\"type\":\"integer\",\"minimum\":1},\"type\":{\"pattern\":\"^threshold-sha-256$\",\"type\":\"string\"}}}]}},\"threshold\":{\"maximum\":100,\"type\":\"integer\",\"minimum\":1},\"type\":{\"pattern\":\"^threshold-sha-256$\",\"type\":\"string\"}}}]}},\"threshold\":{\"maximum\":100,\"type\":\"integer\",\"minimum\":1},\"type\":{\"pattern\":\"^threshold-sha-256$\",\"type\":\"string\"}}}]},\"base58\":{\"pattern\":\"[1-9a-zA-Z^OIl]{43,44}\",\"type\":\"string\"},\"offset\":{\"type\":\"integer\",\"minimum\":0},\"asset\":{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"data\":{\"anyOf\":[{\"additionalProperties\":true,\"type\":\"object\"},{\"type\":\"null\"}]},\"id\":{\"$ref\":\"#\\/definitions\\/sha3_hexdigest\"}}},\"operation\":{\"type\":\"string\",\"enum\":[\"CREATE\",\"TRANSFER\",\"GENESIS\"]},\"uuid4\":{\"pattern\":\"[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89ab][a-f0-9]{3}-[a-f0-9]{12}\",\"type\":\"string\"},\"sha3_hexdigest\":{\"pattern\":\"[0-9a-f]{64}\",\"type\":\"string\"}},\"required\":[\"id\",\"inputs\",\"outputs\",\"operation\",\"metadata\",\"asset\",\"version\"],\"properties\":{\"outputs\":{\"type\":\"array\",\"items\":{\"$ref\":\"#\\/definitions\\/output\"}},\"metadata\":{\"$ref\":\"#\\/definitions\\/metadata\"},\"inputs\":{\"type\":\"array\",\"title\":\"Transaction inputs\",\"items\":{\"$ref\":\"#\\/definitions\\/input\"}},\"id\":{\"anyOf\":[{\"$ref\":\"#\\/definitions\\/sha3_hexdigest\"},{\"type\":\"null\"}]},\"asset\":{\"$ref\":\"#\\/definitions\\/asset\"},\"operation\":{\"$ref\":\"#\\/definitions\\/operation\"},\"version\":{\"pattern\":\"^1\\\\.0$\",\"type\":\"string\"}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"title\":\"Transaction Schema\",\"definitions\":{\"output\":{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"amount\",\"condition\",\"public_keys\"],\"properties\":{\"public_keys\":{\"$ref\":\"#\\/definitions\\/public_keys\"},\"amount\":{\"pattern\":\"^[0-9]{1,20}$\",\"type\":\"string\"},\"condition\":{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"details\",\"uri\"],\"properties\":{\"details\":{\"$ref\":\"#\\/definitions\\/condition_details\"},\"uri\":{\"pattern\":\"^ni:\\/\\/\\/sha-256;([a-zA-Z0-9_-]{0,86})[?](fpt=(ed25519|threshold)-sha-256(&)?|cost=[0-9]+(&)?|subtypes=ed25519-sha-256(&)?){2,3}$\",\"type\":\"string\"}}}}},\"public_keys\":{\"anyOf\":[{\"type\":\"array\",\"items\":{\"$ref\":\"#\\/definitions\\/base58\"}},{\"type\":\"null\"}]},\"input\":{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"owners_before\",\"fulfillment\"],\"properties\":{\"owners_before\":{\"$ref\":\"#\\/definitions\\/public_keys\"},\"fulfills\":{\"anyOf\":[{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"output_index\",\"transaction_id\"],\"properties\":{\"transaction_id\":{\"$ref\":\"#\\/definitions\\/sha3_hexdigest\"},\"output_index\":{\"$ref\":\"#\\/definitions\\/offset\"}}},{\"type\":\"null\"}]},\"fulfillment\":{\"anyOf\":[{\"pattern\":\"^[a-zA-Z0-9_-]*$\",\"type\":\"string\"},{\"$ref\":\"#\\/definitions\\/condition_details\"}]}}},\"metadata\":{\"anyOf\":[{\"additionalProperties\":true,\"type\":\"object\",\"minProperties\":1},{\"type\":\"null\"}]},\"condition_details\":{\"anyOf\":[{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"type\",\"public_key\"],\"properties\":{\"public_key\":{\"$ref\":\"#\\/definitions\\/base58\"},\"type\":{\"pattern\":\"^ed25519-sha-256$\",\"type\":\"string\"}}},{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"type\",\"threshold\",\"subconditions\"],\"properties\":{\"subconditions\":{\"type\":\"array\",\"items\":{\"anyOf\":[{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"type\",\"public_key\"],\"properties\":{\"public_key\":{\"$ref\":\"#\\/definitions\\/base58\"},\"type\":{\"pattern\":\"^ed25519-sha-256$\",\"type\":\"string\"}}},{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"type\",\"threshold\",\"subconditions\"],\"properties\":{\"subconditions\":{\"type\":\"array\",\"items\":{\"anyOf\":[{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"type\",\"public_key\"],\"properties\":{\"public_key\":{\"$ref\":\"#\\/definitions\\/base58\"},\"type\":{\"pattern\":\"^ed25519-sha-256$\",\"type\":\"string\"}}},{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"type\",\"threshold\",\"subconditions\"],\"properties\":{\"subconditions\":{\"type\":\"array\",\"items\":{}},\"threshold\":{\"maximum\":100,\"type\":\"integer\",\"minimum\":1},\"type\":{\"pattern\":\"^threshold-sha-256$\",\"type\":\"string\"}}}]}},\"threshold\":{\"maximum\":100,\"type\":\"integer\",\"minimum\":1},\"type\":{\"pattern\":\"^threshold-sha-256$\",\"type\":\"string\"}}}]}},\"threshold\":{\"maximum\":100,\"type\":\"integer\",\"minimum\":1},\"type\":{\"pattern\":\"^threshold-sha-256$\",\"type\":\"string\"}}}]},\"base58\":{\"pattern\":\"[1-9a-zA-Z^OIl]{43,44}\",\"type\":\"string\"},\"offset\":{\"type\":\"integer\",\"minimum\":0},\"asset\":{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"data\":{\"anyOf\":[{\"additionalProperties\":true,\"type\":\"object\"},{\"type\":\"null\"}]},\"id\":{\"$ref\":\"#\\/definitions\\/sha3_hexdigest\"}}},\"operation\":{\"type\":\"string\",\"enum\":[\"CREATE\",\"TRANSFER\",\"VALIDATOR_ELECTION\",\"CHAIN_MIGRATION_ELECTION\",\"VOTE\"]},\"uuid4\":{\"pattern\":\"[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89ab][a-f0-9]{3}-[a-f0-9]{12}\",\"type\":\"string\"},\"sha3_hexdigest\":{\"pattern\":\"[0-9a-f]{64}\",\"type\":\"string\"}},\"required\":[\"id\",\"inputs\",\"outputs\",\"operation\",\"metadata\",\"asset\",\"version\"],\"properties\":{\"outputs\":{\"type\":\"array\",\"items\":{\"$ref\":\"#\\/definitions\\/output\"}},\"metadata\":{\"$ref\":\"#\\/definitions\\/metadata\"},\"inputs\":{\"type\":\"array\",\"title\":\"Transaction inputs\",\"items\":{\"$ref\":\"#\\/definitions\\/input\"}},\"id\":{\"anyOf\":[{\"$ref\":\"#\\/definitions\\/sha3_hexdigest\"},{\"type\":\"null\"}]},\"asset\":{\"$ref\":\"#\\/definitions\\/asset\"},\"operation\":{\"$ref\":\"#\\/definitions\\/operation\"},\"version\":{\"pattern\":\"^2\\\\.0$\",\"type\":\"string\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id2() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"object\",\"title\":\"Transaction Schema - CREATE\\/GENESIS specific constraints\",\"required\":[\"asset\",\"inputs\"],\"properties\":{\"inputs\":{\"minItems\":1,\"maxItems\":1,\"type\":\"array\",\"title\":\"Transaction inputs\",\"items\":{\"type\":\"object\",\"required\":[\"fulfills\"],\"properties\":{\"fulfills\":{\"type\":\"null\"}}}},\"asset\":{\"additionalProperties\":false,\"properties\":{\"data\":{\"anyOf\":[{\"additionalProperties\":true,\"type\":\"object\"},{\"type\":\"null\"}]}},\"required\":[\"data\"]}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"object\",\"title\":\"Transaction Schema - CREATE specific constraints\",\"required\":[\"asset\",\"inputs\"],\"properties\":{\"inputs\":{\"minItems\":1,\"maxItems\":1,\"type\":\"array\",\"title\":\"Transaction inputs\",\"items\":{\"type\":\"object\",\"required\":[\"fulfills\"],\"properties\":{\"fulfills\":{\"type\":\"null\"}}}},\"asset\":{\"additionalProperties\":false,\"properties\":{\"data\":{\"anyOf\":[{\"additionalProperties\":true,\"type\":\"object\"},{\"type\":\"null\"}]}},\"required\":[\"data\"]}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id3() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"object\",\"title\":\"Transaction Schema - TRANSFER specific properties\",\"definitions\":{\"sha3_hexdigest\":{\"pattern\":\"[0-9a-f]{64}\",\"type\":\"string\"}},\"required\":[\"asset\"],\"properties\":{\"inputs\":{\"minItems\":1,\"type\":\"array\",\"title\":\"Transaction inputs\",\"items\":{\"type\":\"object\",\"required\":[\"fulfills\"],\"properties\":{\"fulfills\":{\"type\":\"object\"}}}},\"asset\":{\"additionalProperties\":false,\"properties\":{\"id\":{\"$ref\":\"#\\/definitions\\/sha3_hexdigest\"}},\"required\":[\"id\"]}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"object\",\"title\":\"Transaction Schema - CREATE\\/GENESIS specific constraints\",\"required\":[\"asset\",\"inputs\"],\"properties\":{\"inputs\":{\"minItems\":1,\"maxItems\":1,\"type\":\"array\",\"title\":\"Transaction inputs\",\"items\":{\"type\":\"object\",\"required\":[\"fulfills\"],\"properties\":{\"fulfills\":{\"type\":\"null\"}}}},\"asset\":{\"additionalProperties\":false,\"properties\":{\"data\":{\"anyOf\":[{\"additionalProperties\":true,\"type\":\"object\"},{\"type\":\"null\"}]}},\"required\":[\"data\"]}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id4() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"object\",\"title\":\"Transaction Schema - TRANSFER specific properties\",\"definitions\":{\"sha3_hexdigest\":{\"pattern\":\"[0-9a-f]{64}\",\"type\":\"string\"}},\"required\":[\"asset\"],\"properties\":{\"inputs\":{\"minItems\":1,\"type\":\"array\",\"title\":\"Transaction inputs\",\"items\":{\"type\":\"object\",\"required\":[\"fulfills\"],\"properties\":{\"fulfills\":{\"type\":\"object\"}}}},\"asset\":{\"additionalProperties\":false,\"properties\":{\"id\":{\"$ref\":\"#\\/definitions\\/sha3_hexdigest\"}},\"required\":[\"id\"]}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"object\",\"title\":\"Transaction Schema - CREATE specific constraints\",\"required\":[\"asset\",\"inputs\"],\"properties\":{\"inputs\":{\"minItems\":1,\"maxItems\":1,\"type\":\"array\",\"title\":\"Transaction inputs\",\"items\":{\"type\":\"object\",\"required\":[\"fulfills\"],\"properties\":{\"fulfills\":{\"type\":\"null\"}}}},\"asset\":{\"additionalProperties\":false,\"properties\":{\"data\":{\"anyOf\":[{\"additionalProperties\":true,\"type\":\"object\"},{\"type\":\"null\"}]}},\"required\":[\"data\"]}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id5() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"object\",\"title\":\"Transaction Schema - TRANSFER specific properties\",\"definitions\":{\"sha3_hexdigest\":{\"pattern\":\"[0-9a-f]{64}\",\"type\":\"string\"}},\"required\":[\"asset\"],\"properties\":{\"inputs\":{\"minItems\":1,\"type\":\"array\",\"title\":\"Transaction inputs\",\"items\":{\"type\":\"object\",\"required\":[\"fulfills\"],\"properties\":{\"fulfills\":{\"type\":\"object\"}}}},\"asset\":{\"additionalProperties\":false,\"properties\":{\"id\":{\"$ref\":\"#\\/definitions\\/sha3_hexdigest\"}},\"required\":[\"id\"]}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"object\",\"title\":\"Transaction Schema - TRANSFER specific properties\",\"definitions\":{\"sha3_hexdigest\":{\"pattern\":\"[0-9a-f]{64}\",\"type\":\"string\"}},\"required\":[\"asset\"],\"properties\":{\"inputs\":{\"minItems\":1,\"type\":\"array\",\"title\":\"Transaction inputs\",\"items\":{\"type\":\"object\",\"required\":[\"fulfills\"],\"properties\":{\"fulfills\":{\"type\":\"object\"}}}},\"asset\":{\"additionalProperties\":false,\"properties\":{\"id\":{\"$ref\":\"#\\/definitions\\/sha3_hexdigest\"}},\"required\":[\"id\"]}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id6() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"object\",\"title\":\"Validator Election Schema - Propose a change to validator set\",\"definitions\":{\"output\":{\"type\":\"object\",\"properties\":{\"condition\":{\"type\":\"object\",\"required\":[\"uri\"],\"properties\":{\"uri\":{\"pattern\":\"^ni:\\/\\/\\/sha-256;([a-zA-Z0-9_-]{0,86})[?](fpt=ed25519-sha-256(&)?|cost=[0-9]+(&)?|subtypes=ed25519-sha-256(&)?){2,3}$\",\"type\":\"string\"}}}}},\"positiveInteger\":{\"type\":\"integer\",\"minimum\":0}},\"required\":[\"operation\",\"asset\",\"outputs\"],\"properties\":{\"outputs\":{\"type\":\"array\",\"items\":{\"$ref\":\"#\\/definitions\\/output\"}},\"asset\":{\"additionalProperties\":false,\"properties\":{\"data\":{\"additionalProperties\":false,\"properties\":{\"public_key\":{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"value\",\"type\"],\"properties\":{\"type\":{\"type\":\"string\",\"enum\":[\"ed25519-base16\",\"ed25519-base32\",\"ed25519-base64\"]},\"value\":{\"type\":\"string\"}}},\"seed\":{\"type\":\"string\"},\"power\":{\"$ref\":\"#\\/definitions\\/positiveInteger\"},\"node_id\":{\"type\":\"string\"}},\"required\":[\"node_id\",\"public_key\",\"power\"]}},\"required\":[\"data\"]},\"operation\":{\"type\":\"string\",\"value\":\"VALIDATOR_ELECTION\"}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"object\",\"title\":\"Vote Schema - Vote on an election\",\"definitions\":{\"output\":{\"type\":\"object\",\"properties\":{\"condition\":{\"type\":\"object\",\"required\":[\"uri\"],\"properties\":{\"uri\":{\"pattern\":\"^ni:\\/\\/\\/sha-256;([a-zA-Z0-9_-]{0,86})[?](fpt=ed25519-sha-256(&)?|cost=[0-9]+(&)?|subtypes=ed25519-sha-256(&)?){2,3}$\",\"type\":\"string\"}}}}}},\"required\":[\"operation\",\"outputs\"],\"properties\":{\"outputs\":{\"type\":\"array\",\"items\":{\"$ref\":\"#\\/definitions\\/output\"}},\"operation\":{\"type\":\"string\",\"value\":\"VOTE\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id7() throws Exception {

        String schema1 = "{\"not\":{\"enum\":[true]},\"type\":\"boolean\",\"enum\":[true]}";
        String schema2 = "{\"allOf\":[{},{\"not\":{}}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id8() throws Exception {

        String schema1 = "{\"enum\":[true]}";
        String schema2 = "{\"enum\":[true,false]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id9() throws Exception {

        String schema1 = "{\"allOf\":[{\"enum\":[true,false]},{\"enum\":[true]}]}";
        String schema2 = "{\"enum\":[true,false]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id10() throws Exception {

        String schema1 = "{\"allOf\":[{\"enum\":[true,false]},{\"enum\":[true]}]}";
        String schema2 = "{\"enum\":[true]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id11() throws Exception {

        String schema1 = "{\"enum\":[true,false]}";
        String schema2 = "{\"anyOf\":[{\"enum\":[true,false]},{\"enum\":[true]}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id12() throws Exception {

        String schema1 = "{\"enum\":[true]}";
        String schema2 = "{\"anyOf\":[{\"enum\":[true,false]},{\"enum\":[true]}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id13() throws Exception {

        String schema1 = "{\"minLength\":1,\"type\":[\"string\",\"null\"]}";
        String schema2 = "{\"pattern\":\".+\",\"type\":[\"null\",\"string\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id14() throws Exception {

        String schema1 = "{\"minLength\":1,\"type\":[\"string\",\"null\"]}";
        String schema2 = "{\"anyOf\":[{\"pattern\":\".{1,}\",\"type\":\"string\"},{\"enum\":[null]}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id15() throws Exception {

        String schema1 = "{\"minLength\":1,\"type\":[\"string\",\"null\"]}";
        String schema2 = "{\"not\":{\"enum\":[\"\"]},\"anyOf\":[{\"type\":\"string\"},{\"type\":\"null\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id16() throws Exception {

        String schema1 = "{\"pattern\":\".+\",\"type\":[\"null\",\"string\"]}";
        String schema2 = "{\"anyOf\":[{\"pattern\":\".{1,}\",\"type\":\"string\"},{\"enum\":[null]}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id17() throws Exception {

        String schema1 = "{\"pattern\":\".+\",\"type\":[\"null\",\"string\"]}";
        String schema2 = "{\"not\":{\"enum\":[\"\"]},\"anyOf\":[{\"type\":\"string\"},{\"type\":\"null\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id18() throws Exception {

        String schema1 = "{\"anyOf\":[{\"pattern\":\".{1,}\",\"type\":\"string\"},{\"enum\":[null]}]}";
        String schema2 = "{\"not\":{\"enum\":[\"\"]},\"anyOf\":[{\"type\":\"string\"},{\"type\":\"null\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id19() throws Exception {

        String schema1 = "{\"anyOf\":[{\"type\":\"array\"},{\"type\":\"boolean\"},{\"type\":\"integer\"},{\"type\":\"null\"},{\"type\":\"number\"},{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"referent\":{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"provider\":{\"type\":\"string\"},\"service\":{\"type\":\"string\"},\"id\":{\"type\":\"string\"},\"type\":{\"type\":\"string\"}},\"required\":[\"id\",\"provider\"]},\"additional_properties\":{\"additionalProperties\":true,\"type\":\"object\"},\"type\":{\"pattern\":\"^reference$\",\"type\":\"string\",\"enum\":[\"reference\"]}},\"required\":[\"referent\",\"type\"]},{\"type\":\"string\"}]}";
        String schema2 = "{\"anyOf\":[{\"type\":\"array\"},{\"type\":\"boolean\"},{\"type\":\"integer\"},{\"type\":\"null\"},{\"type\":\"number\"},{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"channels\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}},\"subtype\":{\"type\":\"string\"},\"referent\":{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"referent_properties\":{\"additionalProperties\":true,\"type\":\"object\"},\"provider\":{\"type\":\"string\"},\"service\":{\"type\":\"string\"},\"id\":{\"type\":\"string\"},\"type\":{\"type\":\"string\"}},\"required\":[\"id\",\"provider\"]},\"additional_properties\":{\"additionalProperties\":true,\"type\":\"object\"},\"_id\":{\"type\":\"string\"},\"type\":{\"pattern\":\"^reference$\",\"type\":\"string\",\"enum\":[\"reference\"]}},\"required\":[\"referent\",\"type\"]},{\"type\":\"string\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id20() throws Exception {

        String schema1 = "{\"type\":[\"array\",\"object\"]}";
        String schema2 = "{\"type\":[\"object\",\"array\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_more_id21() throws Exception {

        String schema1 = "{\"anyOf\":[{\"minItems\":2,\"additionalItems\":false,\"type\":\"array\",\"items\":[{\"type\":\"string\",\"maxLength\":1},{\"type\":\"array\"}]},{\"minItems\":2,\"additionalItems\":false,\"type\":\"array\",\"items\":[{\"type\":\"integer\"},{\"type\":\"array\"}]}]}";
        String schema2 = "{\"minItems\":2,\"additionalItems\":false,\"type\":\"array\",\"items\":[{\"anyOf\":[{\"type\":\"string\",\"maxLength\":1},{\"type\":\"integer\"}]},{\"type\":\"array\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id1() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":150,\"maxItems\":150,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":4,\"maxItems\":4,\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"minItems\":150,\"maxItems\":150,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"number\"}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id2() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":120,\"maxItems\":120,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":4,\"maxItems\":4,\"type\":\"array\",\"items\":[{\"description\":\"sepal length (cm)\",\"type\":\"number\"},{\"description\":\"sepal width (cm)\",\"type\":\"number\"},{\"description\":\"petal length (cm)\",\"type\":\"number\"},{\"description\":\"petal width (cm)\",\"type\":\"number\"}]}},\"y\":{\"minItems\":120,\"maxItems\":120,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"target\",\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"number\"}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id3() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":1437,\"maxItems\":1437,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":64,\"maxItems\":64,\"type\":\"array\",\"items\":{\"maximum\":16,\"type\":\"number\",\"minimum\":0}}},\"y\":{\"minItems\":1437,\"maxItems\":1437,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"target\",\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"number\"}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id4() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":16512,\"maxItems\":16512,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":8,\"maxItems\":8,\"type\":\"array\",\"items\":[{\"description\":\"MedInc\",\"type\":\"number\",\"minimum\":0},{\"description\":\"HouseAge\",\"type\":\"number\",\"minimum\":0},{\"description\":\"AveRooms\",\"type\":\"number\",\"minimum\":0},{\"description\":\"AveBedrms\",\"type\":\"number\",\"minimum\":0},{\"description\":\"Population\",\"type\":\"number\",\"minimum\":0},{\"description\":\"AveOccup\",\"type\":\"number\",\"minimum\":0},{\"description\":\"Latitude\",\"type\":\"number\",\"minimum\":0},{\"description\":\"Longitude\",\"type\":\"number\"}]}},\"y\":{\"minItems\":16512,\"maxItems\":16512,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"target\",\"type\":\"number\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"number\"}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id5() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":670,\"maxItems\":670,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":20,\"maxItems\":20,\"type\":\"array\",\"items\":[{\"description\":\"checking_status\",\"enum\":[\"<0\",\"0<=X<200\",\">=200\",\"no checking\"]},{\"description\":\"duration\",\"type\":\"number\"},{\"description\":\"credit_history\",\"enum\":[\"no credits\\/all paid\",\"all paid\",\"existing paid\",\"delayed previously\",\"critical\\/other existing credit\"]},{\"description\":\"purpose\",\"enum\":[\"new car\",\"used car\",\"furniture\\/equipment\",\"radio\\/tv\",\"domestic appliance\",\"repairs\",\"education\",\"vacation\",\"retraining\",\"business\",\"other\"]},{\"description\":\"credit_amount\",\"type\":\"number\"},{\"description\":\"savings_status\",\"enum\":[\"<100\",\"100<=X<500\",\"500<=X<1000\",\">=1000\",\"no known savings\"]},{\"description\":\"employment\",\"enum\":[\"unemployed\",\"<1\",\"1<=X<4\",\"4<=X<7\",\">=7\"]},{\"description\":\"installment_commitment\",\"type\":\"number\"},{\"description\":\"personal_status\",\"enum\":[\"male div\\/sep\",\"female div\\/dep\\/mar\",\"male single\",\"male mar\\/wid\",\"female single\"]},{\"description\":\"other_parties\",\"enum\":[\"none\",\"co applicant\",\"guarantor\"]},{\"description\":\"residence_since\",\"type\":\"number\"},{\"description\":\"property_magnitude\",\"enum\":[\"real estate\",\"life insurance\",\"car\",\"no known property\"]},{\"description\":\"age\",\"type\":\"number\"},{\"description\":\"other_payment_plans\",\"enum\":[\"bank\",\"stores\",\"none\"]},{\"description\":\"housing\",\"enum\":[\"rent\",\"own\",\"for free\"]},{\"description\":\"existing_credits\",\"type\":\"number\"},{\"description\":\"job\",\"enum\":[\"unemp\\/unskilled non res\",\"unskilled resident\",\"skilled\",\"high qualif\\/self emp\\/mgmt\"]},{\"description\":\"num_dependents\",\"type\":\"number\"},{\"description\":\"own_telephone\",\"enum\":[\"none\",\"yes\"]},{\"description\":\"foreign_worker\",\"enum\":[\"yes\",\"no\"]}]}},\"y\":{\"minItems\":670,\"maxItems\":670,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"class\",\"enum\":[0,1]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"number\"}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id6() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":10662,\"maxItems\":10662,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"type\":\"string\"}},\"y\":{\"minItems\":10662,\"maxItems\":10662,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"number\"}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id7() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":161297,\"maxItems\":161297,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":5,\"maxItems\":5,\"type\":\"array\",\"items\":[{\"description\":\"drugName\",\"type\":\"string\"},{\"description\":\"condition\",\"anyOf\":[{\"type\":\"string\"},{\"enum\":[null]}]},{\"description\":\"review\",\"type\":\"string\"},{\"description\":\"date\",\"type\":\"string\"},{\"description\":\"usefulCount\",\"type\":\"integer\",\"minimum\":0}]}},\"y\":{\"minItems\":161297,\"maxItems\":161297,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"rating\",\"enum\":[1,2,3,4,5,6,7,8,9,10]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"number\"}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id8() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"minItems\":14,\"maxItems\":14,\"type\":\"array\",\"items\":[{\"description\":\"age\",\"type\":\"number\"},{\"description\":\"workclass\",\"type\":\"string\"},{\"description\":\"fnlwgt\",\"type\":\"number\"},{\"description\":\"education\",\"type\":\"string\"},{\"description\":\"education-num\",\"type\":\"number\"},{\"description\":\"marital-status\",\"type\":\"string\"},{\"description\":\"occupation\",\"type\":\"string\"},{\"description\":\"relationship\",\"type\":\"string\"},{\"description\":\"race\",\"type\":\"string\"},{\"description\":\"sex\",\"type\":\"string\"},{\"description\":\"capital-gain\",\"type\":\"number\"},{\"description\":\"capital-loss\",\"type\":\"number\"},{\"description\":\"hours-per-week\",\"type\":\"number\"},{\"description\":\"native-country\",\"type\":\"string\"}]}},\"y\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"description\":\"class\",\"enum\":[\"<=50K\",\">50K\"]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"number\"}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id9() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"minItems\":105,\"maxItems\":105,\"type\":\"array\",\"items\":[{\"description\":\"workclass_Federal-gov\",\"enum\":[0,1]},{\"description\":\"workclass_Local-gov\",\"enum\":[0,1]},{\"description\":\"workclass_Never-worked\",\"enum\":[0,1]},{\"description\":\"workclass_Private\",\"enum\":[0,1]},{\"description\":\"workclass_Self-emp-inc\",\"enum\":[0,1]},{\"description\":\"workclass_Self-emp-not-inc\",\"enum\":[0,1]},{\"description\":\"workclass_State-gov\",\"enum\":[0,1]},{\"description\":\"workclass_Without-pay\",\"enum\":[0,1]},{\"description\":\"education_10th\",\"enum\":[0,1]},{\"description\":\"education_11th\",\"enum\":[0,1]},{\"description\":\"education_12th\",\"enum\":[0,1]},{\"description\":\"education_1st-4th\",\"enum\":[0,1]},{\"description\":\"education_5th-6th\",\"enum\":[0,1]},{\"description\":\"education_7th-8th\",\"enum\":[0,1]},{\"description\":\"education_9th\",\"enum\":[0,1]},{\"description\":\"education_Assoc-acdm\",\"enum\":[0,1]},{\"description\":\"education_Assoc-voc\",\"enum\":[0,1]},{\"description\":\"education_Bachelors\",\"enum\":[0,1]},{\"description\":\"education_Doctorate\",\"enum\":[0,1]},{\"description\":\"education_HS-grad\",\"enum\":[0,1]},{\"description\":\"education_Masters\",\"enum\":[0,1]},{\"description\":\"education_Preschool\",\"enum\":[0,1]},{\"description\":\"education_Prof-school\",\"enum\":[0,1]},{\"description\":\"education_Some-college\",\"enum\":[0,1]},{\"description\":\"marital-status_Divorced\",\"enum\":[0,1]},{\"description\":\"marital-status_Married-AF-spouse\",\"enum\":[0,1]},{\"description\":\"marital-status_Married-civ-spouse\",\"enum\":[0,1]},{\"description\":\"marital-status_Married-spouse-absent\",\"enum\":[0,1]},{\"description\":\"marital-status_Never-married\",\"enum\":[0,1]},{\"description\":\"marital-status_Separated\",\"enum\":[0,1]},{\"description\":\"marital-status_Widowed\",\"enum\":[0,1]},{\"description\":\"occupation_Adm-clerical\",\"enum\":[0,1]},{\"description\":\"occupation_Armed-Forces\",\"enum\":[0,1]},{\"description\":\"occupation_Craft-repair\",\"enum\":[0,1]},{\"description\":\"occupation_Exec-managerial\",\"enum\":[0,1]},{\"description\":\"occupation_Farming-fishing\",\"enum\":[0,1]},{\"description\":\"occupation_Handlers-cleaners\",\"enum\":[0,1]},{\"description\":\"occupation_Machine-op-inspct\",\"enum\":[0,1]},{\"description\":\"occupation_Other-service\",\"enum\":[0,1]},{\"description\":\"occupation_Priv-house-serv\",\"enum\":[0,1]},{\"description\":\"occupation_Prof-specialty\",\"enum\":[0,1]},{\"description\":\"occupation_Protective-serv\",\"enum\":[0,1]},{\"description\":\"occupation_Sales\",\"enum\":[0,1]},{\"description\":\"occupation_Tech-support\",\"enum\":[0,1]},{\"description\":\"occupation_Transport-moving\",\"enum\":[0,1]},{\"description\":\"relationship_Husband\",\"enum\":[0,1]},{\"description\":\"relationship_Not-in-family\",\"enum\":[0,1]},{\"description\":\"relationship_Other-relative\",\"enum\":[0,1]},{\"description\":\"relationship_Own-child\",\"enum\":[0,1]},{\"description\":\"relationship_Unmarried\",\"enum\":[0,1]},{\"description\":\"relationship_Wife\",\"enum\":[0,1]},{\"description\":\"race_Amer-Indian-Eskimo\",\"enum\":[0,1]},{\"description\":\"race_Asian-Pac-Islander\",\"enum\":[0,1]},{\"description\":\"race_Black\",\"enum\":[0,1]},{\"description\":\"race_Other\",\"enum\":[0,1]},{\"description\":\"race_White\",\"enum\":[0,1]},{\"description\":\"sex_Female\",\"enum\":[0,1]},{\"description\":\"sex_Male\",\"enum\":[0,1]},{\"description\":\"native-country_Cambodia\",\"enum\":[0,1]},{\"description\":\"native-country_Canada\",\"enum\":[0,1]},{\"description\":\"native-country_China\",\"enum\":[0,1]},{\"description\":\"native-country_Columbia\",\"enum\":[0,1]},{\"description\":\"native-country_Cuba\",\"enum\":[0,1]},{\"description\":\"native-country_Dominican-Republic\",\"enum\":[0,1]},{\"description\":\"native-country_Ecuador\",\"enum\":[0,1]},{\"description\":\"native-country_El-Salvador\",\"enum\":[0,1]},{\"description\":\"native-country_England\",\"enum\":[0,1]},{\"description\":\"native-country_France\",\"enum\":[0,1]},{\"description\":\"native-country_Germany\",\"enum\":[0,1]},{\"description\":\"native-country_Greece\",\"enum\":[0,1]},{\"description\":\"native-country_Guatemala\",\"enum\":[0,1]},{\"description\":\"native-country_Haiti\",\"enum\":[0,1]},{\"description\":\"native-country_Holand-Netherlands\",\"enum\":[0,1]},{\"description\":\"native-country_Honduras\",\"enum\":[0,1]},{\"description\":\"native-country_Hong\",\"enum\":[0,1]},{\"description\":\"native-country_Hungary\",\"enum\":[0,1]},{\"description\":\"native-country_India\",\"enum\":[0,1]},{\"description\":\"native-country_Iran\",\"enum\":[0,1]},{\"description\":\"native-country_Ireland\",\"enum\":[0,1]},{\"description\":\"native-country_Italy\",\"enum\":[0,1]},{\"description\":\"native-country_Jamaica\",\"enum\":[0,1]},{\"description\":\"native-country_Japan\",\"enum\":[0,1]},{\"description\":\"native-country_Laos\",\"enum\":[0,1]},{\"description\":\"native-country_Mexico\",\"enum\":[0,1]},{\"description\":\"native-country_Nicaragua\",\"enum\":[0,1]},{\"description\":\"native-country_Outlying-US(Guam-USVI-etc)\",\"enum\":[0,1]},{\"description\":\"native-country_Peru\",\"enum\":[0,1]},{\"description\":\"native-country_Philippines\",\"enum\":[0,1]},{\"description\":\"native-country_Poland\",\"enum\":[0,1]},{\"description\":\"native-country_Portugal\",\"enum\":[0,1]},{\"description\":\"native-country_Puerto-Rico\",\"enum\":[0,1]},{\"description\":\"native-country_Scotland\",\"enum\":[0,1]},{\"description\":\"native-country_South\",\"enum\":[0,1]},{\"description\":\"native-country_Taiwan\",\"enum\":[0,1]},{\"description\":\"native-country_Thailand\",\"enum\":[0,1]},{\"description\":\"native-country_Trinadad&Tobago\",\"enum\":[0,1]},{\"description\":\"native-country_United-States\",\"enum\":[0,1]},{\"description\":\"native-country_Vietnam\",\"enum\":[0,1]},{\"description\":\"native-country_Yugoslavia\",\"enum\":[0,1]},{\"description\":\"age\",\"type\":\"number\"},{\"description\":\"fnlwgt\",\"type\":\"number\"},{\"description\":\"education-num\",\"type\":\"number\"},{\"description\":\"capital-gain\",\"type\":\"number\"},{\"description\":\"capital-loss\",\"type\":\"number\"},{\"description\":\"hours-per-week\",\"type\":\"number\"}]}},\"y\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"description\":\"class\",\"enum\":[0,1]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"number\"}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id10() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"documentation_url\":\"https:\\/\\/scikit-learn.org\\/0.20\\/datasets\\/index.html#forest-covertypes\",\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"type\":\"array\",\"items\":{\"minItems\":54,\"maxItems\":54,\"type\":\"array\",\"items\":[{\"description\":\"Elevation\",\"type\":\"integer\"},{\"description\":\"Aspect\",\"type\":\"integer\"},{\"description\":\"Slope\",\"type\":\"integer\"},{\"description\":\"Horizontal_Distance_To_Hydrology\",\"type\":\"integer\"},{\"description\":\"Vertical_Distance_To_Hydrology\",\"type\":\"integer\"},{\"description\":\"Horizontal_Distance_To_Roadways\",\"type\":\"integer\"},{\"description\":\"Hillshade_9am\",\"type\":\"integer\"},{\"description\":\"Hillshade_Noon\",\"type\":\"integer\"},{\"description\":\"Hillshade_3pm\",\"type\":\"integer\"},{\"description\":\"Horizontal_Distance_To_Fire_Points\",\"type\":\"integer\"},{\"description\":\"Wilderness_Area1\",\"enum\":[0,1]},{\"description\":\"Wilderness_Area2\",\"enum\":[0,1]},{\"description\":\"Wilderness_Area3\",\"enum\":[0,1]},{\"description\":\"Wilderness_Area4\",\"enum\":[0,1]},{\"description\":\"Soil_Type1\",\"enum\":[0,1]},{\"description\":\"Soil_Type2\",\"enum\":[0,1]},{\"description\":\"Soil_Type3\",\"enum\":[0,1]},{\"description\":\"Soil_Type4\",\"enum\":[0,1]},{\"description\":\"Soil_Type5\",\"enum\":[0,1]},{\"description\":\"Soil_Type6\",\"enum\":[0,1]},{\"description\":\"Soil_Type7\",\"enum\":[0,1]},{\"description\":\"Soil_Type8\",\"enum\":[0,1]},{\"description\":\"Soil_Type9\",\"enum\":[0,1]},{\"description\":\"Soil_Type10\",\"enum\":[0,1]},{\"description\":\"Soil_Type11\",\"enum\":[0,1]},{\"description\":\"Soil_Type12\",\"enum\":[0,1]},{\"description\":\"Soil_Type13\",\"enum\":[0,1]},{\"description\":\"Soil_Type14\",\"enum\":[0,1]},{\"description\":\"Soil_Type15\",\"enum\":[0,1]},{\"description\":\"Soil_Type16\",\"enum\":[0,1]},{\"description\":\"Soil_Type17\",\"enum\":[0,1]},{\"description\":\"Soil_Type18\",\"enum\":[0,1]},{\"description\":\"Soil_Type19\",\"enum\":[0,1]},{\"description\":\"Soil_Type20\",\"enum\":[0,1]},{\"description\":\"Soil_Type21\",\"enum\":[0,1]},{\"description\":\"Soil_Type22\",\"enum\":[0,1]},{\"description\":\"Soil_Type23\",\"enum\":[0,1]},{\"description\":\"Soil_Type24\",\"enum\":[0,1]},{\"description\":\"Soil_Type25\",\"enum\":[0,1]},{\"description\":\"Soil_Type26\",\"enum\":[0,1]},{\"description\":\"Soil_Type27\",\"enum\":[0,1]},{\"description\":\"Soil_Type28\",\"enum\":[0,1]},{\"description\":\"Soil_Type29\",\"enum\":[0,1]},{\"description\":\"Soil_Type30\",\"enum\":[0,1]},{\"description\":\"Soil_Type31\",\"enum\":[0,1]},{\"description\":\"Soil_Type32\",\"enum\":[0,1]},{\"description\":\"Soil_Type33\",\"enum\":[0,1]},{\"description\":\"Soil_Type34\",\"enum\":[0,1]},{\"description\":\"Soil_Type35\",\"enum\":[0,1]},{\"description\":\"Soil_Type36\",\"enum\":[0,1]},{\"description\":\"Soil_Type37\",\"enum\":[0,1]},{\"description\":\"Soil_Type38\",\"enum\":[0,1]},{\"description\":\"Soil_Type39\",\"enum\":[0,1]},{\"description\":\"Soil_Type40\",\"enum\":[0,1]}]}},\"y\":{\"type\":\"array\",\"items\":{\"description\":\"The cover type, i.e., the domiNonet species of trees.\",\"enum\":[\"spruce_fir\",\"lodgepole_pine\",\"ponderosa_pine\",\"cottonwood_willow\",\"aspen\",\"douglas_fir\",\"krummholz\"]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"number\"}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id11() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":150,\"maxItems\":150,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":4,\"maxItems\":4,\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"minItems\":150,\"maxItems\":150,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"number\"}},{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"type\":\"boolean\"}}]}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id12() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":120,\"maxItems\":120,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":4,\"maxItems\":4,\"type\":\"array\",\"items\":[{\"description\":\"sepal length (cm)\",\"type\":\"number\"},{\"description\":\"sepal width (cm)\",\"type\":\"number\"},{\"description\":\"petal length (cm)\",\"type\":\"number\"},{\"description\":\"petal width (cm)\",\"type\":\"number\"}]}},\"y\":{\"minItems\":120,\"maxItems\":120,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"target\",\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"number\"}},{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"type\":\"boolean\"}}]}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id13() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":1437,\"maxItems\":1437,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":64,\"maxItems\":64,\"type\":\"array\",\"items\":{\"maximum\":16,\"type\":\"number\",\"minimum\":0}}},\"y\":{\"minItems\":1437,\"maxItems\":1437,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"target\",\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"number\"}},{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"type\":\"boolean\"}}]}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id14() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":16512,\"maxItems\":16512,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":8,\"maxItems\":8,\"type\":\"array\",\"items\":[{\"description\":\"MedInc\",\"type\":\"number\",\"minimum\":0},{\"description\":\"HouseAge\",\"type\":\"number\",\"minimum\":0},{\"description\":\"AveRooms\",\"type\":\"number\",\"minimum\":0},{\"description\":\"AveBedrms\",\"type\":\"number\",\"minimum\":0},{\"description\":\"Population\",\"type\":\"number\",\"minimum\":0},{\"description\":\"AveOccup\",\"type\":\"number\",\"minimum\":0},{\"description\":\"Latitude\",\"type\":\"number\",\"minimum\":0},{\"description\":\"Longitude\",\"type\":\"number\"}]}},\"y\":{\"minItems\":16512,\"maxItems\":16512,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"target\",\"type\":\"number\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"number\"}},{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"type\":\"boolean\"}}]}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id15() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":670,\"maxItems\":670,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":20,\"maxItems\":20,\"type\":\"array\",\"items\":[{\"description\":\"checking_status\",\"enum\":[\"<0\",\"0<=X<200\",\">=200\",\"no checking\"]},{\"description\":\"duration\",\"type\":\"number\"},{\"description\":\"credit_history\",\"enum\":[\"no credits\\/all paid\",\"all paid\",\"existing paid\",\"delayed previously\",\"critical\\/other existing credit\"]},{\"description\":\"purpose\",\"enum\":[\"new car\",\"used car\",\"furniture\\/equipment\",\"radio\\/tv\",\"domestic appliance\",\"repairs\",\"education\",\"vacation\",\"retraining\",\"business\",\"other\"]},{\"description\":\"credit_amount\",\"type\":\"number\"},{\"description\":\"savings_status\",\"enum\":[\"<100\",\"100<=X<500\",\"500<=X<1000\",\">=1000\",\"no known savings\"]},{\"description\":\"employment\",\"enum\":[\"unemployed\",\"<1\",\"1<=X<4\",\"4<=X<7\",\">=7\"]},{\"description\":\"installment_commitment\",\"type\":\"number\"},{\"description\":\"personal_status\",\"enum\":[\"male div\\/sep\",\"female div\\/dep\\/mar\",\"male single\",\"male mar\\/wid\",\"female single\"]},{\"description\":\"other_parties\",\"enum\":[\"none\",\"co applicant\",\"guarantor\"]},{\"description\":\"residence_since\",\"type\":\"number\"},{\"description\":\"property_magnitude\",\"enum\":[\"real estate\",\"life insurance\",\"car\",\"no known property\"]},{\"description\":\"age\",\"type\":\"number\"},{\"description\":\"other_payment_plans\",\"enum\":[\"bank\",\"stores\",\"none\"]},{\"description\":\"housing\",\"enum\":[\"rent\",\"own\",\"for free\"]},{\"description\":\"existing_credits\",\"type\":\"number\"},{\"description\":\"job\",\"enum\":[\"unemp\\/unskilled non res\",\"unskilled resident\",\"skilled\",\"high qualif\\/self emp\\/mgmt\"]},{\"description\":\"num_dependents\",\"type\":\"number\"},{\"description\":\"own_telephone\",\"enum\":[\"none\",\"yes\"]},{\"description\":\"foreign_worker\",\"enum\":[\"yes\",\"no\"]}]}},\"y\":{\"minItems\":670,\"maxItems\":670,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"class\",\"enum\":[0,1]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"number\"}},{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"type\":\"boolean\"}}]}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id16() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":10662,\"maxItems\":10662,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"type\":\"string\"}},\"y\":{\"minItems\":10662,\"maxItems\":10662,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"number\"}},{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"type\":\"boolean\"}}]}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id17() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":161297,\"maxItems\":161297,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":5,\"maxItems\":5,\"type\":\"array\",\"items\":[{\"description\":\"drugName\",\"type\":\"string\"},{\"description\":\"condition\",\"anyOf\":[{\"type\":\"string\"},{\"enum\":[null]}]},{\"description\":\"review\",\"type\":\"string\"},{\"description\":\"date\",\"type\":\"string\"},{\"description\":\"usefulCount\",\"type\":\"integer\",\"minimum\":0}]}},\"y\":{\"minItems\":161297,\"maxItems\":161297,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"rating\",\"enum\":[1,2,3,4,5,6,7,8,9,10]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"number\"}},{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"type\":\"boolean\"}}]}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id18() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"minItems\":14,\"maxItems\":14,\"type\":\"array\",\"items\":[{\"description\":\"age\",\"type\":\"number\"},{\"description\":\"workclass\",\"type\":\"string\"},{\"description\":\"fnlwgt\",\"type\":\"number\"},{\"description\":\"education\",\"type\":\"string\"},{\"description\":\"education-num\",\"type\":\"number\"},{\"description\":\"marital-status\",\"type\":\"string\"},{\"description\":\"occupation\",\"type\":\"string\"},{\"description\":\"relationship\",\"type\":\"string\"},{\"description\":\"race\",\"type\":\"string\"},{\"description\":\"sex\",\"type\":\"string\"},{\"description\":\"capital-gain\",\"type\":\"number\"},{\"description\":\"capital-loss\",\"type\":\"number\"},{\"description\":\"hours-per-week\",\"type\":\"number\"},{\"description\":\"native-country\",\"type\":\"string\"}]}},\"y\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"description\":\"class\",\"enum\":[\"<=50K\",\">50K\"]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"number\"}},{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"type\":\"boolean\"}}]}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id19() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"minItems\":105,\"maxItems\":105,\"type\":\"array\",\"items\":[{\"description\":\"workclass_Federal-gov\",\"enum\":[0,1]},{\"description\":\"workclass_Local-gov\",\"enum\":[0,1]},{\"description\":\"workclass_Never-worked\",\"enum\":[0,1]},{\"description\":\"workclass_Private\",\"enum\":[0,1]},{\"description\":\"workclass_Self-emp-inc\",\"enum\":[0,1]},{\"description\":\"workclass_Self-emp-not-inc\",\"enum\":[0,1]},{\"description\":\"workclass_State-gov\",\"enum\":[0,1]},{\"description\":\"workclass_Without-pay\",\"enum\":[0,1]},{\"description\":\"education_10th\",\"enum\":[0,1]},{\"description\":\"education_11th\",\"enum\":[0,1]},{\"description\":\"education_12th\",\"enum\":[0,1]},{\"description\":\"education_1st-4th\",\"enum\":[0,1]},{\"description\":\"education_5th-6th\",\"enum\":[0,1]},{\"description\":\"education_7th-8th\",\"enum\":[0,1]},{\"description\":\"education_9th\",\"enum\":[0,1]},{\"description\":\"education_Assoc-acdm\",\"enum\":[0,1]},{\"description\":\"education_Assoc-voc\",\"enum\":[0,1]},{\"description\":\"education_Bachelors\",\"enum\":[0,1]},{\"description\":\"education_Doctorate\",\"enum\":[0,1]},{\"description\":\"education_HS-grad\",\"enum\":[0,1]},{\"description\":\"education_Masters\",\"enum\":[0,1]},{\"description\":\"education_Preschool\",\"enum\":[0,1]},{\"description\":\"education_Prof-school\",\"enum\":[0,1]},{\"description\":\"education_Some-college\",\"enum\":[0,1]},{\"description\":\"marital-status_Divorced\",\"enum\":[0,1]},{\"description\":\"marital-status_Married-AF-spouse\",\"enum\":[0,1]},{\"description\":\"marital-status_Married-civ-spouse\",\"enum\":[0,1]},{\"description\":\"marital-status_Married-spouse-absent\",\"enum\":[0,1]},{\"description\":\"marital-status_Never-married\",\"enum\":[0,1]},{\"description\":\"marital-status_Separated\",\"enum\":[0,1]},{\"description\":\"marital-status_Widowed\",\"enum\":[0,1]},{\"description\":\"occupation_Adm-clerical\",\"enum\":[0,1]},{\"description\":\"occupation_Armed-Forces\",\"enum\":[0,1]},{\"description\":\"occupation_Craft-repair\",\"enum\":[0,1]},{\"description\":\"occupation_Exec-managerial\",\"enum\":[0,1]},{\"description\":\"occupation_Farming-fishing\",\"enum\":[0,1]},{\"description\":\"occupation_Handlers-cleaners\",\"enum\":[0,1]},{\"description\":\"occupation_Machine-op-inspct\",\"enum\":[0,1]},{\"description\":\"occupation_Other-service\",\"enum\":[0,1]},{\"description\":\"occupation_Priv-house-serv\",\"enum\":[0,1]},{\"description\":\"occupation_Prof-specialty\",\"enum\":[0,1]},{\"description\":\"occupation_Protective-serv\",\"enum\":[0,1]},{\"description\":\"occupation_Sales\",\"enum\":[0,1]},{\"description\":\"occupation_Tech-support\",\"enum\":[0,1]},{\"description\":\"occupation_Transport-moving\",\"enum\":[0,1]},{\"description\":\"relationship_Husband\",\"enum\":[0,1]},{\"description\":\"relationship_Not-in-family\",\"enum\":[0,1]},{\"description\":\"relationship_Other-relative\",\"enum\":[0,1]},{\"description\":\"relationship_Own-child\",\"enum\":[0,1]},{\"description\":\"relationship_Unmarried\",\"enum\":[0,1]},{\"description\":\"relationship_Wife\",\"enum\":[0,1]},{\"description\":\"race_Amer-Indian-Eskimo\",\"enum\":[0,1]},{\"description\":\"race_Asian-Pac-Islander\",\"enum\":[0,1]},{\"description\":\"race_Black\",\"enum\":[0,1]},{\"description\":\"race_Other\",\"enum\":[0,1]},{\"description\":\"race_White\",\"enum\":[0,1]},{\"description\":\"sex_Female\",\"enum\":[0,1]},{\"description\":\"sex_Male\",\"enum\":[0,1]},{\"description\":\"native-country_Cambodia\",\"enum\":[0,1]},{\"description\":\"native-country_Canada\",\"enum\":[0,1]},{\"description\":\"native-country_China\",\"enum\":[0,1]},{\"description\":\"native-country_Columbia\",\"enum\":[0,1]},{\"description\":\"native-country_Cuba\",\"enum\":[0,1]},{\"description\":\"native-country_Dominican-Republic\",\"enum\":[0,1]},{\"description\":\"native-country_Ecuador\",\"enum\":[0,1]},{\"description\":\"native-country_El-Salvador\",\"enum\":[0,1]},{\"description\":\"native-country_England\",\"enum\":[0,1]},{\"description\":\"native-country_France\",\"enum\":[0,1]},{\"description\":\"native-country_Germany\",\"enum\":[0,1]},{\"description\":\"native-country_Greece\",\"enum\":[0,1]},{\"description\":\"native-country_Guatemala\",\"enum\":[0,1]},{\"description\":\"native-country_Haiti\",\"enum\":[0,1]},{\"description\":\"native-country_Holand-Netherlands\",\"enum\":[0,1]},{\"description\":\"native-country_Honduras\",\"enum\":[0,1]},{\"description\":\"native-country_Hong\",\"enum\":[0,1]},{\"description\":\"native-country_Hungary\",\"enum\":[0,1]},{\"description\":\"native-country_India\",\"enum\":[0,1]},{\"description\":\"native-country_Iran\",\"enum\":[0,1]},{\"description\":\"native-country_Ireland\",\"enum\":[0,1]},{\"description\":\"native-country_Italy\",\"enum\":[0,1]},{\"description\":\"native-country_Jamaica\",\"enum\":[0,1]},{\"description\":\"native-country_Japan\",\"enum\":[0,1]},{\"description\":\"native-country_Laos\",\"enum\":[0,1]},{\"description\":\"native-country_Mexico\",\"enum\":[0,1]},{\"description\":\"native-country_Nicaragua\",\"enum\":[0,1]},{\"description\":\"native-country_Outlying-US(Guam-USVI-etc)\",\"enum\":[0,1]},{\"description\":\"native-country_Peru\",\"enum\":[0,1]},{\"description\":\"native-country_Philippines\",\"enum\":[0,1]},{\"description\":\"native-country_Poland\",\"enum\":[0,1]},{\"description\":\"native-country_Portugal\",\"enum\":[0,1]},{\"description\":\"native-country_Puerto-Rico\",\"enum\":[0,1]},{\"description\":\"native-country_Scotland\",\"enum\":[0,1]},{\"description\":\"native-country_South\",\"enum\":[0,1]},{\"description\":\"native-country_Taiwan\",\"enum\":[0,1]},{\"description\":\"native-country_Thailand\",\"enum\":[0,1]},{\"description\":\"native-country_Trinadad&Tobago\",\"enum\":[0,1]},{\"description\":\"native-country_United-States\",\"enum\":[0,1]},{\"description\":\"native-country_Vietnam\",\"enum\":[0,1]},{\"description\":\"native-country_Yugoslavia\",\"enum\":[0,1]},{\"description\":\"age\",\"type\":\"number\"},{\"description\":\"fnlwgt\",\"type\":\"number\"},{\"description\":\"education-num\",\"type\":\"number\"},{\"description\":\"capital-gain\",\"type\":\"number\"},{\"description\":\"capital-loss\",\"type\":\"number\"},{\"description\":\"hours-per-week\",\"type\":\"number\"}]}},\"y\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"description\":\"class\",\"enum\":[0,1]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"number\"}},{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"type\":\"boolean\"}}]}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id20() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"documentation_url\":\"https:\\/\\/scikit-learn.org\\/0.20\\/datasets\\/index.html#forest-covertypes\",\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"type\":\"array\",\"items\":{\"minItems\":54,\"maxItems\":54,\"type\":\"array\",\"items\":[{\"description\":\"Elevation\",\"type\":\"integer\"},{\"description\":\"Aspect\",\"type\":\"integer\"},{\"description\":\"Slope\",\"type\":\"integer\"},{\"description\":\"Horizontal_Distance_To_Hydrology\",\"type\":\"integer\"},{\"description\":\"Vertical_Distance_To_Hydrology\",\"type\":\"integer\"},{\"description\":\"Horizontal_Distance_To_Roadways\",\"type\":\"integer\"},{\"description\":\"Hillshade_9am\",\"type\":\"integer\"},{\"description\":\"Hillshade_Noon\",\"type\":\"integer\"},{\"description\":\"Hillshade_3pm\",\"type\":\"integer\"},{\"description\":\"Horizontal_Distance_To_Fire_Points\",\"type\":\"integer\"},{\"description\":\"Wilderness_Area1\",\"enum\":[0,1]},{\"description\":\"Wilderness_Area2\",\"enum\":[0,1]},{\"description\":\"Wilderness_Area3\",\"enum\":[0,1]},{\"description\":\"Wilderness_Area4\",\"enum\":[0,1]},{\"description\":\"Soil_Type1\",\"enum\":[0,1]},{\"description\":\"Soil_Type2\",\"enum\":[0,1]},{\"description\":\"Soil_Type3\",\"enum\":[0,1]},{\"description\":\"Soil_Type4\",\"enum\":[0,1]},{\"description\":\"Soil_Type5\",\"enum\":[0,1]},{\"description\":\"Soil_Type6\",\"enum\":[0,1]},{\"description\":\"Soil_Type7\",\"enum\":[0,1]},{\"description\":\"Soil_Type8\",\"enum\":[0,1]},{\"description\":\"Soil_Type9\",\"enum\":[0,1]},{\"description\":\"Soil_Type10\",\"enum\":[0,1]},{\"description\":\"Soil_Type11\",\"enum\":[0,1]},{\"description\":\"Soil_Type12\",\"enum\":[0,1]},{\"description\":\"Soil_Type13\",\"enum\":[0,1]},{\"description\":\"Soil_Type14\",\"enum\":[0,1]},{\"description\":\"Soil_Type15\",\"enum\":[0,1]},{\"description\":\"Soil_Type16\",\"enum\":[0,1]},{\"description\":\"Soil_Type17\",\"enum\":[0,1]},{\"description\":\"Soil_Type18\",\"enum\":[0,1]},{\"description\":\"Soil_Type19\",\"enum\":[0,1]},{\"description\":\"Soil_Type20\",\"enum\":[0,1]},{\"description\":\"Soil_Type21\",\"enum\":[0,1]},{\"description\":\"Soil_Type22\",\"enum\":[0,1]},{\"description\":\"Soil_Type23\",\"enum\":[0,1]},{\"description\":\"Soil_Type24\",\"enum\":[0,1]},{\"description\":\"Soil_Type25\",\"enum\":[0,1]},{\"description\":\"Soil_Type26\",\"enum\":[0,1]},{\"description\":\"Soil_Type27\",\"enum\":[0,1]},{\"description\":\"Soil_Type28\",\"enum\":[0,1]},{\"description\":\"Soil_Type29\",\"enum\":[0,1]},{\"description\":\"Soil_Type30\",\"enum\":[0,1]},{\"description\":\"Soil_Type31\",\"enum\":[0,1]},{\"description\":\"Soil_Type32\",\"enum\":[0,1]},{\"description\":\"Soil_Type33\",\"enum\":[0,1]},{\"description\":\"Soil_Type34\",\"enum\":[0,1]},{\"description\":\"Soil_Type35\",\"enum\":[0,1]},{\"description\":\"Soil_Type36\",\"enum\":[0,1]},{\"description\":\"Soil_Type37\",\"enum\":[0,1]},{\"description\":\"Soil_Type38\",\"enum\":[0,1]},{\"description\":\"Soil_Type39\",\"enum\":[0,1]},{\"description\":\"Soil_Type40\",\"enum\":[0,1]}]}},\"y\":{\"type\":\"array\",\"items\":{\"description\":\"The cover type, i.e., the domiNonet species of trees.\",\"enum\":[\"spruce_fir\",\"lodgepole_pine\",\"ponderosa_pine\",\"cottonwood_willow\",\"aspen\",\"douglas_fir\",\"krummholz\"]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"number\"}},{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"type\":\"boolean\"}}]}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id21() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":150,\"maxItems\":150,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":4,\"maxItems\":4,\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"minItems\":150,\"maxItems\":150,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training Project.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"anyOf\":[{\"type\":\"number\"},{\"type\":\"string\"}]}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id22() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":120,\"maxItems\":120,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":4,\"maxItems\":4,\"type\":\"array\",\"items\":[{\"description\":\"sepal length (cm)\",\"type\":\"number\"},{\"description\":\"sepal width (cm)\",\"type\":\"number\"},{\"description\":\"petal length (cm)\",\"type\":\"number\"},{\"description\":\"petal width (cm)\",\"type\":\"number\"}]}},\"y\":{\"minItems\":120,\"maxItems\":120,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"target\",\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training Project.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"anyOf\":[{\"type\":\"number\"},{\"type\":\"string\"}]}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id23() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":1437,\"maxItems\":1437,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":64,\"maxItems\":64,\"type\":\"array\",\"items\":{\"maximum\":16,\"type\":\"number\",\"minimum\":0}}},\"y\":{\"minItems\":1437,\"maxItems\":1437,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"target\",\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training Project.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"anyOf\":[{\"type\":\"number\"},{\"type\":\"string\"}]}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id24() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":16512,\"maxItems\":16512,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":8,\"maxItems\":8,\"type\":\"array\",\"items\":[{\"description\":\"MedInc\",\"type\":\"number\",\"minimum\":0},{\"description\":\"HouseAge\",\"type\":\"number\",\"minimum\":0},{\"description\":\"AveRooms\",\"type\":\"number\",\"minimum\":0},{\"description\":\"AveBedrms\",\"type\":\"number\",\"minimum\":0},{\"description\":\"Population\",\"type\":\"number\",\"minimum\":0},{\"description\":\"AveOccup\",\"type\":\"number\",\"minimum\":0},{\"description\":\"Latitude\",\"type\":\"number\",\"minimum\":0},{\"description\":\"Longitude\",\"type\":\"number\"}]}},\"y\":{\"minItems\":16512,\"maxItems\":16512,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"target\",\"type\":\"number\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training Project.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"anyOf\":[{\"type\":\"number\"},{\"type\":\"string\"}]}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id25() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":670,\"maxItems\":670,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":20,\"maxItems\":20,\"type\":\"array\",\"items\":[{\"description\":\"checking_status\",\"enum\":[\"<0\",\"0<=X<200\",\">=200\",\"no checking\"]},{\"description\":\"duration\",\"type\":\"number\"},{\"description\":\"credit_history\",\"enum\":[\"no credits\\/all paid\",\"all paid\",\"existing paid\",\"delayed previously\",\"critical\\/other existing credit\"]},{\"description\":\"purpose\",\"enum\":[\"new car\",\"used car\",\"furniture\\/equipment\",\"radio\\/tv\",\"domestic appliance\",\"repairs\",\"education\",\"vacation\",\"retraining\",\"business\",\"other\"]},{\"description\":\"credit_amount\",\"type\":\"number\"},{\"description\":\"savings_status\",\"enum\":[\"<100\",\"100<=X<500\",\"500<=X<1000\",\">=1000\",\"no known savings\"]},{\"description\":\"employment\",\"enum\":[\"unemployed\",\"<1\",\"1<=X<4\",\"4<=X<7\",\">=7\"]},{\"description\":\"installment_commitment\",\"type\":\"number\"},{\"description\":\"personal_status\",\"enum\":[\"male div\\/sep\",\"female div\\/dep\\/mar\",\"male single\",\"male mar\\/wid\",\"female single\"]},{\"description\":\"other_parties\",\"enum\":[\"none\",\"co applicant\",\"guarantor\"]},{\"description\":\"residence_since\",\"type\":\"number\"},{\"description\":\"property_magnitude\",\"enum\":[\"real estate\",\"life insurance\",\"car\",\"no known property\"]},{\"description\":\"age\",\"type\":\"number\"},{\"description\":\"other_payment_plans\",\"enum\":[\"bank\",\"stores\",\"none\"]},{\"description\":\"housing\",\"enum\":[\"rent\",\"own\",\"for free\"]},{\"description\":\"existing_credits\",\"type\":\"number\"},{\"description\":\"job\",\"enum\":[\"unemp\\/unskilled non res\",\"unskilled resident\",\"skilled\",\"high qualif\\/self emp\\/mgmt\"]},{\"description\":\"num_dependents\",\"type\":\"number\"},{\"description\":\"own_telephone\",\"enum\":[\"none\",\"yes\"]},{\"description\":\"foreign_worker\",\"enum\":[\"yes\",\"no\"]}]}},\"y\":{\"minItems\":670,\"maxItems\":670,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"class\",\"enum\":[0,1]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training Project.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"anyOf\":[{\"type\":\"number\"},{\"type\":\"string\"}]}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id26() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":10662,\"maxItems\":10662,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"type\":\"string\"}},\"y\":{\"minItems\":10662,\"maxItems\":10662,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training Project.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"anyOf\":[{\"type\":\"number\"},{\"type\":\"string\"}]}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id27() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":161297,\"maxItems\":161297,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":5,\"maxItems\":5,\"type\":\"array\",\"items\":[{\"description\":\"drugName\",\"type\":\"string\"},{\"description\":\"condition\",\"anyOf\":[{\"type\":\"string\"},{\"enum\":[null]}]},{\"description\":\"review\",\"type\":\"string\"},{\"description\":\"date\",\"type\":\"string\"},{\"description\":\"usefulCount\",\"type\":\"integer\",\"minimum\":0}]}},\"y\":{\"minItems\":161297,\"maxItems\":161297,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"rating\",\"enum\":[1,2,3,4,5,6,7,8,9,10]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training Project.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"anyOf\":[{\"type\":\"number\"},{\"type\":\"string\"}]}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id28() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"minItems\":14,\"maxItems\":14,\"type\":\"array\",\"items\":[{\"description\":\"age\",\"type\":\"number\"},{\"description\":\"workclass\",\"type\":\"string\"},{\"description\":\"fnlwgt\",\"type\":\"number\"},{\"description\":\"education\",\"type\":\"string\"},{\"description\":\"education-num\",\"type\":\"number\"},{\"description\":\"marital-status\",\"type\":\"string\"},{\"description\":\"occupation\",\"type\":\"string\"},{\"description\":\"relationship\",\"type\":\"string\"},{\"description\":\"race\",\"type\":\"string\"},{\"description\":\"sex\",\"type\":\"string\"},{\"description\":\"capital-gain\",\"type\":\"number\"},{\"description\":\"capital-loss\",\"type\":\"number\"},{\"description\":\"hours-per-week\",\"type\":\"number\"},{\"description\":\"native-country\",\"type\":\"string\"}]}},\"y\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"description\":\"class\",\"enum\":[\"<=50K\",\">50K\"]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training Project.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"anyOf\":[{\"type\":\"number\"},{\"type\":\"string\"}]}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id29() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"minItems\":105,\"maxItems\":105,\"type\":\"array\",\"items\":[{\"description\":\"workclass_Federal-gov\",\"enum\":[0,1]},{\"description\":\"workclass_Local-gov\",\"enum\":[0,1]},{\"description\":\"workclass_Never-worked\",\"enum\":[0,1]},{\"description\":\"workclass_Private\",\"enum\":[0,1]},{\"description\":\"workclass_Self-emp-inc\",\"enum\":[0,1]},{\"description\":\"workclass_Self-emp-not-inc\",\"enum\":[0,1]},{\"description\":\"workclass_State-gov\",\"enum\":[0,1]},{\"description\":\"workclass_Without-pay\",\"enum\":[0,1]},{\"description\":\"education_10th\",\"enum\":[0,1]},{\"description\":\"education_11th\",\"enum\":[0,1]},{\"description\":\"education_12th\",\"enum\":[0,1]},{\"description\":\"education_1st-4th\",\"enum\":[0,1]},{\"description\":\"education_5th-6th\",\"enum\":[0,1]},{\"description\":\"education_7th-8th\",\"enum\":[0,1]},{\"description\":\"education_9th\",\"enum\":[0,1]},{\"description\":\"education_Assoc-acdm\",\"enum\":[0,1]},{\"description\":\"education_Assoc-voc\",\"enum\":[0,1]},{\"description\":\"education_Bachelors\",\"enum\":[0,1]},{\"description\":\"education_Doctorate\",\"enum\":[0,1]},{\"description\":\"education_HS-grad\",\"enum\":[0,1]},{\"description\":\"education_Masters\",\"enum\":[0,1]},{\"description\":\"education_Preschool\",\"enum\":[0,1]},{\"description\":\"education_Prof-school\",\"enum\":[0,1]},{\"description\":\"education_Some-college\",\"enum\":[0,1]},{\"description\":\"marital-status_Divorced\",\"enum\":[0,1]},{\"description\":\"marital-status_Married-AF-spouse\",\"enum\":[0,1]},{\"description\":\"marital-status_Married-civ-spouse\",\"enum\":[0,1]},{\"description\":\"marital-status_Married-spouse-absent\",\"enum\":[0,1]},{\"description\":\"marital-status_Never-married\",\"enum\":[0,1]},{\"description\":\"marital-status_Separated\",\"enum\":[0,1]},{\"description\":\"marital-status_Widowed\",\"enum\":[0,1]},{\"description\":\"occupation_Adm-clerical\",\"enum\":[0,1]},{\"description\":\"occupation_Armed-Forces\",\"enum\":[0,1]},{\"description\":\"occupation_Craft-repair\",\"enum\":[0,1]},{\"description\":\"occupation_Exec-managerial\",\"enum\":[0,1]},{\"description\":\"occupation_Farming-fishing\",\"enum\":[0,1]},{\"description\":\"occupation_Handlers-cleaners\",\"enum\":[0,1]},{\"description\":\"occupation_Machine-op-inspct\",\"enum\":[0,1]},{\"description\":\"occupation_Other-service\",\"enum\":[0,1]},{\"description\":\"occupation_Priv-house-serv\",\"enum\":[0,1]},{\"description\":\"occupation_Prof-specialty\",\"enum\":[0,1]},{\"description\":\"occupation_Protective-serv\",\"enum\":[0,1]},{\"description\":\"occupation_Sales\",\"enum\":[0,1]},{\"description\":\"occupation_Tech-support\",\"enum\":[0,1]},{\"description\":\"occupation_Transport-moving\",\"enum\":[0,1]},{\"description\":\"relationship_Husband\",\"enum\":[0,1]},{\"description\":\"relationship_Not-in-family\",\"enum\":[0,1]},{\"description\":\"relationship_Other-relative\",\"enum\":[0,1]},{\"description\":\"relationship_Own-child\",\"enum\":[0,1]},{\"description\":\"relationship_Unmarried\",\"enum\":[0,1]},{\"description\":\"relationship_Wife\",\"enum\":[0,1]},{\"description\":\"race_Amer-Indian-Eskimo\",\"enum\":[0,1]},{\"description\":\"race_Asian-Pac-Islander\",\"enum\":[0,1]},{\"description\":\"race_Black\",\"enum\":[0,1]},{\"description\":\"race_Other\",\"enum\":[0,1]},{\"description\":\"race_White\",\"enum\":[0,1]},{\"description\":\"sex_Female\",\"enum\":[0,1]},{\"description\":\"sex_Male\",\"enum\":[0,1]},{\"description\":\"native-country_Cambodia\",\"enum\":[0,1]},{\"description\":\"native-country_Canada\",\"enum\":[0,1]},{\"description\":\"native-country_China\",\"enum\":[0,1]},{\"description\":\"native-country_Columbia\",\"enum\":[0,1]},{\"description\":\"native-country_Cuba\",\"enum\":[0,1]},{\"description\":\"native-country_Dominican-Republic\",\"enum\":[0,1]},{\"description\":\"native-country_Ecuador\",\"enum\":[0,1]},{\"description\":\"native-country_El-Salvador\",\"enum\":[0,1]},{\"description\":\"native-country_England\",\"enum\":[0,1]},{\"description\":\"native-country_France\",\"enum\":[0,1]},{\"description\":\"native-country_Germany\",\"enum\":[0,1]},{\"description\":\"native-country_Greece\",\"enum\":[0,1]},{\"description\":\"native-country_Guatemala\",\"enum\":[0,1]},{\"description\":\"native-country_Haiti\",\"enum\":[0,1]},{\"description\":\"native-country_Holand-Netherlands\",\"enum\":[0,1]},{\"description\":\"native-country_Honduras\",\"enum\":[0,1]},{\"description\":\"native-country_Hong\",\"enum\":[0,1]},{\"description\":\"native-country_Hungary\",\"enum\":[0,1]},{\"description\":\"native-country_India\",\"enum\":[0,1]},{\"description\":\"native-country_Iran\",\"enum\":[0,1]},{\"description\":\"native-country_Ireland\",\"enum\":[0,1]},{\"description\":\"native-country_Italy\",\"enum\":[0,1]},{\"description\":\"native-country_Jamaica\",\"enum\":[0,1]},{\"description\":\"native-country_Japan\",\"enum\":[0,1]},{\"description\":\"native-country_Laos\",\"enum\":[0,1]},{\"description\":\"native-country_Mexico\",\"enum\":[0,1]},{\"description\":\"native-country_Nicaragua\",\"enum\":[0,1]},{\"description\":\"native-country_Outlying-US(Guam-USVI-etc)\",\"enum\":[0,1]},{\"description\":\"native-country_Peru\",\"enum\":[0,1]},{\"description\":\"native-country_Philippines\",\"enum\":[0,1]},{\"description\":\"native-country_Poland\",\"enum\":[0,1]},{\"description\":\"native-country_Portugal\",\"enum\":[0,1]},{\"description\":\"native-country_Puerto-Rico\",\"enum\":[0,1]},{\"description\":\"native-country_Scotland\",\"enum\":[0,1]},{\"description\":\"native-country_South\",\"enum\":[0,1]},{\"description\":\"native-country_Taiwan\",\"enum\":[0,1]},{\"description\":\"native-country_Thailand\",\"enum\":[0,1]},{\"description\":\"native-country_Trinadad&Tobago\",\"enum\":[0,1]},{\"description\":\"native-country_United-States\",\"enum\":[0,1]},{\"description\":\"native-country_Vietnam\",\"enum\":[0,1]},{\"description\":\"native-country_Yugoslavia\",\"enum\":[0,1]},{\"description\":\"age\",\"type\":\"number\"},{\"description\":\"fnlwgt\",\"type\":\"number\"},{\"description\":\"education-num\",\"type\":\"number\"},{\"description\":\"capital-gain\",\"type\":\"number\"},{\"description\":\"capital-loss\",\"type\":\"number\"},{\"description\":\"hours-per-week\",\"type\":\"number\"}]}},\"y\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"description\":\"class\",\"enum\":[0,1]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training Project.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"anyOf\":[{\"type\":\"number\"},{\"type\":\"string\"}]}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id30() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"documentation_url\":\"https:\\/\\/scikit-learn.org\\/0.20\\/datasets\\/index.html#forest-covertypes\",\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"type\":\"array\",\"items\":{\"minItems\":54,\"maxItems\":54,\"type\":\"array\",\"items\":[{\"description\":\"Elevation\",\"type\":\"integer\"},{\"description\":\"Aspect\",\"type\":\"integer\"},{\"description\":\"Slope\",\"type\":\"integer\"},{\"description\":\"Horizontal_Distance_To_Hydrology\",\"type\":\"integer\"},{\"description\":\"Vertical_Distance_To_Hydrology\",\"type\":\"integer\"},{\"description\":\"Horizontal_Distance_To_Roadways\",\"type\":\"integer\"},{\"description\":\"Hillshade_9am\",\"type\":\"integer\"},{\"description\":\"Hillshade_Noon\",\"type\":\"integer\"},{\"description\":\"Hillshade_3pm\",\"type\":\"integer\"},{\"description\":\"Horizontal_Distance_To_Fire_Points\",\"type\":\"integer\"},{\"description\":\"Wilderness_Area1\",\"enum\":[0,1]},{\"description\":\"Wilderness_Area2\",\"enum\":[0,1]},{\"description\":\"Wilderness_Area3\",\"enum\":[0,1]},{\"description\":\"Wilderness_Area4\",\"enum\":[0,1]},{\"description\":\"Soil_Type1\",\"enum\":[0,1]},{\"description\":\"Soil_Type2\",\"enum\":[0,1]},{\"description\":\"Soil_Type3\",\"enum\":[0,1]},{\"description\":\"Soil_Type4\",\"enum\":[0,1]},{\"description\":\"Soil_Type5\",\"enum\":[0,1]},{\"description\":\"Soil_Type6\",\"enum\":[0,1]},{\"description\":\"Soil_Type7\",\"enum\":[0,1]},{\"description\":\"Soil_Type8\",\"enum\":[0,1]},{\"description\":\"Soil_Type9\",\"enum\":[0,1]},{\"description\":\"Soil_Type10\",\"enum\":[0,1]},{\"description\":\"Soil_Type11\",\"enum\":[0,1]},{\"description\":\"Soil_Type12\",\"enum\":[0,1]},{\"description\":\"Soil_Type13\",\"enum\":[0,1]},{\"description\":\"Soil_Type14\",\"enum\":[0,1]},{\"description\":\"Soil_Type15\",\"enum\":[0,1]},{\"description\":\"Soil_Type16\",\"enum\":[0,1]},{\"description\":\"Soil_Type17\",\"enum\":[0,1]},{\"description\":\"Soil_Type18\",\"enum\":[0,1]},{\"description\":\"Soil_Type19\",\"enum\":[0,1]},{\"description\":\"Soil_Type20\",\"enum\":[0,1]},{\"description\":\"Soil_Type21\",\"enum\":[0,1]},{\"description\":\"Soil_Type22\",\"enum\":[0,1]},{\"description\":\"Soil_Type23\",\"enum\":[0,1]},{\"description\":\"Soil_Type24\",\"enum\":[0,1]},{\"description\":\"Soil_Type25\",\"enum\":[0,1]},{\"description\":\"Soil_Type26\",\"enum\":[0,1]},{\"description\":\"Soil_Type27\",\"enum\":[0,1]},{\"description\":\"Soil_Type28\",\"enum\":[0,1]},{\"description\":\"Soil_Type29\",\"enum\":[0,1]},{\"description\":\"Soil_Type30\",\"enum\":[0,1]},{\"description\":\"Soil_Type31\",\"enum\":[0,1]},{\"description\":\"Soil_Type32\",\"enum\":[0,1]},{\"description\":\"Soil_Type33\",\"enum\":[0,1]},{\"description\":\"Soil_Type34\",\"enum\":[0,1]},{\"description\":\"Soil_Type35\",\"enum\":[0,1]},{\"description\":\"Soil_Type36\",\"enum\":[0,1]},{\"description\":\"Soil_Type37\",\"enum\":[0,1]},{\"description\":\"Soil_Type38\",\"enum\":[0,1]},{\"description\":\"Soil_Type39\",\"enum\":[0,1]},{\"description\":\"Soil_Type40\",\"enum\":[0,1]}]}},\"y\":{\"type\":\"array\",\"items\":{\"description\":\"The cover type, i.e., the domiNonet species of trees.\",\"enum\":[\"spruce_fir\",\"lodgepole_pine\",\"ponderosa_pine\",\"cottonwood_willow\",\"aspen\",\"douglas_fir\",\"krummholz\"]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training Project.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"anyOf\":[{\"type\":\"number\"},{\"type\":\"string\"}]}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id31() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":150,\"maxItems\":150,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":4,\"maxItems\":4,\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"minItems\":150,\"maxItems\":150,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\",\"minimum\":0}}},\"y\":{}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id32() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":120,\"maxItems\":120,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":4,\"maxItems\":4,\"type\":\"array\",\"items\":[{\"description\":\"sepal length (cm)\",\"type\":\"number\"},{\"description\":\"sepal width (cm)\",\"type\":\"number\"},{\"description\":\"petal length (cm)\",\"type\":\"number\"},{\"description\":\"petal width (cm)\",\"type\":\"number\"}]}},\"y\":{\"minItems\":120,\"maxItems\":120,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"target\",\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\",\"minimum\":0}}},\"y\":{}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id33() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":1437,\"maxItems\":1437,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":64,\"maxItems\":64,\"type\":\"array\",\"items\":{\"maximum\":16,\"type\":\"number\",\"minimum\":0}}},\"y\":{\"minItems\":1437,\"maxItems\":1437,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"target\",\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\",\"minimum\":0}}},\"y\":{}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id34() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":16512,\"maxItems\":16512,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":8,\"maxItems\":8,\"type\":\"array\",\"items\":[{\"description\":\"MedInc\",\"type\":\"number\",\"minimum\":0},{\"description\":\"HouseAge\",\"type\":\"number\",\"minimum\":0},{\"description\":\"AveRooms\",\"type\":\"number\",\"minimum\":0},{\"description\":\"AveBedrms\",\"type\":\"number\",\"minimum\":0},{\"description\":\"Population\",\"type\":\"number\",\"minimum\":0},{\"description\":\"AveOccup\",\"type\":\"number\",\"minimum\":0},{\"description\":\"Latitude\",\"type\":\"number\",\"minimum\":0},{\"description\":\"Longitude\",\"type\":\"number\"}]}},\"y\":{\"minItems\":16512,\"maxItems\":16512,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"target\",\"type\":\"number\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\",\"minimum\":0}}},\"y\":{}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id35() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":670,\"maxItems\":670,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":20,\"maxItems\":20,\"type\":\"array\",\"items\":[{\"description\":\"checking_status\",\"enum\":[\"<0\",\"0<=X<200\",\">=200\",\"no checking\"]},{\"description\":\"duration\",\"type\":\"number\"},{\"description\":\"credit_history\",\"enum\":[\"no credits\\/all paid\",\"all paid\",\"existing paid\",\"delayed previously\",\"critical\\/other existing credit\"]},{\"description\":\"purpose\",\"enum\":[\"new car\",\"used car\",\"furniture\\/equipment\",\"radio\\/tv\",\"domestic appliance\",\"repairs\",\"education\",\"vacation\",\"retraining\",\"business\",\"other\"]},{\"description\":\"credit_amount\",\"type\":\"number\"},{\"description\":\"savings_status\",\"enum\":[\"<100\",\"100<=X<500\",\"500<=X<1000\",\">=1000\",\"no known savings\"]},{\"description\":\"employment\",\"enum\":[\"unemployed\",\"<1\",\"1<=X<4\",\"4<=X<7\",\">=7\"]},{\"description\":\"installment_commitment\",\"type\":\"number\"},{\"description\":\"personal_status\",\"enum\":[\"male div\\/sep\",\"female div\\/dep\\/mar\",\"male single\",\"male mar\\/wid\",\"female single\"]},{\"description\":\"other_parties\",\"enum\":[\"none\",\"co applicant\",\"guarantor\"]},{\"description\":\"residence_since\",\"type\":\"number\"},{\"description\":\"property_magnitude\",\"enum\":[\"real estate\",\"life insurance\",\"car\",\"no known property\"]},{\"description\":\"age\",\"type\":\"number\"},{\"description\":\"other_payment_plans\",\"enum\":[\"bank\",\"stores\",\"none\"]},{\"description\":\"housing\",\"enum\":[\"rent\",\"own\",\"for free\"]},{\"description\":\"existing_credits\",\"type\":\"number\"},{\"description\":\"job\",\"enum\":[\"unemp\\/unskilled non res\",\"unskilled resident\",\"skilled\",\"high qualif\\/self emp\\/mgmt\"]},{\"description\":\"num_dependents\",\"type\":\"number\"},{\"description\":\"own_telephone\",\"enum\":[\"none\",\"yes\"]},{\"description\":\"foreign_worker\",\"enum\":[\"yes\",\"no\"]}]}},\"y\":{\"minItems\":670,\"maxItems\":670,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"class\",\"enum\":[0,1]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\",\"minimum\":0}}},\"y\":{}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id36() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":10662,\"maxItems\":10662,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"type\":\"string\"}},\"y\":{\"minItems\":10662,\"maxItems\":10662,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\",\"minimum\":0}}},\"y\":{}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id37() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":161297,\"maxItems\":161297,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":5,\"maxItems\":5,\"type\":\"array\",\"items\":[{\"description\":\"drugName\",\"type\":\"string\"},{\"description\":\"condition\",\"anyOf\":[{\"type\":\"string\"},{\"enum\":[null]}]},{\"description\":\"review\",\"type\":\"string\"},{\"description\":\"date\",\"type\":\"string\"},{\"description\":\"usefulCount\",\"type\":\"integer\",\"minimum\":0}]}},\"y\":{\"minItems\":161297,\"maxItems\":161297,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"rating\",\"enum\":[1,2,3,4,5,6,7,8,9,10]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\",\"minimum\":0}}},\"y\":{}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id38() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"minItems\":14,\"maxItems\":14,\"type\":\"array\",\"items\":[{\"description\":\"age\",\"type\":\"number\"},{\"description\":\"workclass\",\"type\":\"string\"},{\"description\":\"fnlwgt\",\"type\":\"number\"},{\"description\":\"education\",\"type\":\"string\"},{\"description\":\"education-num\",\"type\":\"number\"},{\"description\":\"marital-status\",\"type\":\"string\"},{\"description\":\"occupation\",\"type\":\"string\"},{\"description\":\"relationship\",\"type\":\"string\"},{\"description\":\"race\",\"type\":\"string\"},{\"description\":\"sex\",\"type\":\"string\"},{\"description\":\"capital-gain\",\"type\":\"number\"},{\"description\":\"capital-loss\",\"type\":\"number\"},{\"description\":\"hours-per-week\",\"type\":\"number\"},{\"description\":\"native-country\",\"type\":\"string\"}]}},\"y\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"description\":\"class\",\"enum\":[\"<=50K\",\">50K\"]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\",\"minimum\":0}}},\"y\":{}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id39() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"minItems\":105,\"maxItems\":105,\"type\":\"array\",\"items\":[{\"description\":\"workclass_Federal-gov\",\"enum\":[0,1]},{\"description\":\"workclass_Local-gov\",\"enum\":[0,1]},{\"description\":\"workclass_Never-worked\",\"enum\":[0,1]},{\"description\":\"workclass_Private\",\"enum\":[0,1]},{\"description\":\"workclass_Self-emp-inc\",\"enum\":[0,1]},{\"description\":\"workclass_Self-emp-not-inc\",\"enum\":[0,1]},{\"description\":\"workclass_State-gov\",\"enum\":[0,1]},{\"description\":\"workclass_Without-pay\",\"enum\":[0,1]},{\"description\":\"education_10th\",\"enum\":[0,1]},{\"description\":\"education_11th\",\"enum\":[0,1]},{\"description\":\"education_12th\",\"enum\":[0,1]},{\"description\":\"education_1st-4th\",\"enum\":[0,1]},{\"description\":\"education_5th-6th\",\"enum\":[0,1]},{\"description\":\"education_7th-8th\",\"enum\":[0,1]},{\"description\":\"education_9th\",\"enum\":[0,1]},{\"description\":\"education_Assoc-acdm\",\"enum\":[0,1]},{\"description\":\"education_Assoc-voc\",\"enum\":[0,1]},{\"description\":\"education_Bachelors\",\"enum\":[0,1]},{\"description\":\"education_Doctorate\",\"enum\":[0,1]},{\"description\":\"education_HS-grad\",\"enum\":[0,1]},{\"description\":\"education_Masters\",\"enum\":[0,1]},{\"description\":\"education_Preschool\",\"enum\":[0,1]},{\"description\":\"education_Prof-school\",\"enum\":[0,1]},{\"description\":\"education_Some-college\",\"enum\":[0,1]},{\"description\":\"marital-status_Divorced\",\"enum\":[0,1]},{\"description\":\"marital-status_Married-AF-spouse\",\"enum\":[0,1]},{\"description\":\"marital-status_Married-civ-spouse\",\"enum\":[0,1]},{\"description\":\"marital-status_Married-spouse-absent\",\"enum\":[0,1]},{\"description\":\"marital-status_Never-married\",\"enum\":[0,1]},{\"description\":\"marital-status_Separated\",\"enum\":[0,1]},{\"description\":\"marital-status_Widowed\",\"enum\":[0,1]},{\"description\":\"occupation_Adm-clerical\",\"enum\":[0,1]},{\"description\":\"occupation_Armed-Forces\",\"enum\":[0,1]},{\"description\":\"occupation_Craft-repair\",\"enum\":[0,1]},{\"description\":\"occupation_Exec-managerial\",\"enum\":[0,1]},{\"description\":\"occupation_Farming-fishing\",\"enum\":[0,1]},{\"description\":\"occupation_Handlers-cleaners\",\"enum\":[0,1]},{\"description\":\"occupation_Machine-op-inspct\",\"enum\":[0,1]},{\"description\":\"occupation_Other-service\",\"enum\":[0,1]},{\"description\":\"occupation_Priv-house-serv\",\"enum\":[0,1]},{\"description\":\"occupation_Prof-specialty\",\"enum\":[0,1]},{\"description\":\"occupation_Protective-serv\",\"enum\":[0,1]},{\"description\":\"occupation_Sales\",\"enum\":[0,1]},{\"description\":\"occupation_Tech-support\",\"enum\":[0,1]},{\"description\":\"occupation_Transport-moving\",\"enum\":[0,1]},{\"description\":\"relationship_Husband\",\"enum\":[0,1]},{\"description\":\"relationship_Not-in-family\",\"enum\":[0,1]},{\"description\":\"relationship_Other-relative\",\"enum\":[0,1]},{\"description\":\"relationship_Own-child\",\"enum\":[0,1]},{\"description\":\"relationship_Unmarried\",\"enum\":[0,1]},{\"description\":\"relationship_Wife\",\"enum\":[0,1]},{\"description\":\"race_Amer-Indian-Eskimo\",\"enum\":[0,1]},{\"description\":\"race_Asian-Pac-Islander\",\"enum\":[0,1]},{\"description\":\"race_Black\",\"enum\":[0,1]},{\"description\":\"race_Other\",\"enum\":[0,1]},{\"description\":\"race_White\",\"enum\":[0,1]},{\"description\":\"sex_Female\",\"enum\":[0,1]},{\"description\":\"sex_Male\",\"enum\":[0,1]},{\"description\":\"native-country_Cambodia\",\"enum\":[0,1]},{\"description\":\"native-country_Canada\",\"enum\":[0,1]},{\"description\":\"native-country_China\",\"enum\":[0,1]},{\"description\":\"native-country_Columbia\",\"enum\":[0,1]},{\"description\":\"native-country_Cuba\",\"enum\":[0,1]},{\"description\":\"native-country_Dominican-Republic\",\"enum\":[0,1]},{\"description\":\"native-country_Ecuador\",\"enum\":[0,1]},{\"description\":\"native-country_El-Salvador\",\"enum\":[0,1]},{\"description\":\"native-country_England\",\"enum\":[0,1]},{\"description\":\"native-country_France\",\"enum\":[0,1]},{\"description\":\"native-country_Germany\",\"enum\":[0,1]},{\"description\":\"native-country_Greece\",\"enum\":[0,1]},{\"description\":\"native-country_Guatemala\",\"enum\":[0,1]},{\"description\":\"native-country_Haiti\",\"enum\":[0,1]},{\"description\":\"native-country_Holand-Netherlands\",\"enum\":[0,1]},{\"description\":\"native-country_Honduras\",\"enum\":[0,1]},{\"description\":\"native-country_Hong\",\"enum\":[0,1]},{\"description\":\"native-country_Hungary\",\"enum\":[0,1]},{\"description\":\"native-country_India\",\"enum\":[0,1]},{\"description\":\"native-country_Iran\",\"enum\":[0,1]},{\"description\":\"native-country_Ireland\",\"enum\":[0,1]},{\"description\":\"native-country_Italy\",\"enum\":[0,1]},{\"description\":\"native-country_Jamaica\",\"enum\":[0,1]},{\"description\":\"native-country_Japan\",\"enum\":[0,1]},{\"description\":\"native-country_Laos\",\"enum\":[0,1]},{\"description\":\"native-country_Mexico\",\"enum\":[0,1]},{\"description\":\"native-country_Nicaragua\",\"enum\":[0,1]},{\"description\":\"native-country_Outlying-US(Guam-USVI-etc)\",\"enum\":[0,1]},{\"description\":\"native-country_Peru\",\"enum\":[0,1]},{\"description\":\"native-country_Philippines\",\"enum\":[0,1]},{\"description\":\"native-country_Poland\",\"enum\":[0,1]},{\"description\":\"native-country_Portugal\",\"enum\":[0,1]},{\"description\":\"native-country_Puerto-Rico\",\"enum\":[0,1]},{\"description\":\"native-country_Scotland\",\"enum\":[0,1]},{\"description\":\"native-country_South\",\"enum\":[0,1]},{\"description\":\"native-country_Taiwan\",\"enum\":[0,1]},{\"description\":\"native-country_Thailand\",\"enum\":[0,1]},{\"description\":\"native-country_Trinadad&Tobago\",\"enum\":[0,1]},{\"description\":\"native-country_United-States\",\"enum\":[0,1]},{\"description\":\"native-country_Vietnam\",\"enum\":[0,1]},{\"description\":\"native-country_Yugoslavia\",\"enum\":[0,1]},{\"description\":\"age\",\"type\":\"number\"},{\"description\":\"fnlwgt\",\"type\":\"number\"},{\"description\":\"education-num\",\"type\":\"number\"},{\"description\":\"capital-gain\",\"type\":\"number\"},{\"description\":\"capital-loss\",\"type\":\"number\"},{\"description\":\"hours-per-week\",\"type\":\"number\"}]}},\"y\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"description\":\"class\",\"enum\":[0,1]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\",\"minimum\":0}}},\"y\":{}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id40() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"documentation_url\":\"https:\\/\\/scikit-learn.org\\/0.20\\/datasets\\/index.html#forest-covertypes\",\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"type\":\"array\",\"items\":{\"minItems\":54,\"maxItems\":54,\"type\":\"array\",\"items\":[{\"description\":\"Elevation\",\"type\":\"integer\"},{\"description\":\"Aspect\",\"type\":\"integer\"},{\"description\":\"Slope\",\"type\":\"integer\"},{\"description\":\"Horizontal_Distance_To_Hydrology\",\"type\":\"integer\"},{\"description\":\"Vertical_Distance_To_Hydrology\",\"type\":\"integer\"},{\"description\":\"Horizontal_Distance_To_Roadways\",\"type\":\"integer\"},{\"description\":\"Hillshade_9am\",\"type\":\"integer\"},{\"description\":\"Hillshade_Noon\",\"type\":\"integer\"},{\"description\":\"Hillshade_3pm\",\"type\":\"integer\"},{\"description\":\"Horizontal_Distance_To_Fire_Points\",\"type\":\"integer\"},{\"description\":\"Wilderness_Area1\",\"enum\":[0,1]},{\"description\":\"Wilderness_Area2\",\"enum\":[0,1]},{\"description\":\"Wilderness_Area3\",\"enum\":[0,1]},{\"description\":\"Wilderness_Area4\",\"enum\":[0,1]},{\"description\":\"Soil_Type1\",\"enum\":[0,1]},{\"description\":\"Soil_Type2\",\"enum\":[0,1]},{\"description\":\"Soil_Type3\",\"enum\":[0,1]},{\"description\":\"Soil_Type4\",\"enum\":[0,1]},{\"description\":\"Soil_Type5\",\"enum\":[0,1]},{\"description\":\"Soil_Type6\",\"enum\":[0,1]},{\"description\":\"Soil_Type7\",\"enum\":[0,1]},{\"description\":\"Soil_Type8\",\"enum\":[0,1]},{\"description\":\"Soil_Type9\",\"enum\":[0,1]},{\"description\":\"Soil_Type10\",\"enum\":[0,1]},{\"description\":\"Soil_Type11\",\"enum\":[0,1]},{\"description\":\"Soil_Type12\",\"enum\":[0,1]},{\"description\":\"Soil_Type13\",\"enum\":[0,1]},{\"description\":\"Soil_Type14\",\"enum\":[0,1]},{\"description\":\"Soil_Type15\",\"enum\":[0,1]},{\"description\":\"Soil_Type16\",\"enum\":[0,1]},{\"description\":\"Soil_Type17\",\"enum\":[0,1]},{\"description\":\"Soil_Type18\",\"enum\":[0,1]},{\"description\":\"Soil_Type19\",\"enum\":[0,1]},{\"description\":\"Soil_Type20\",\"enum\":[0,1]},{\"description\":\"Soil_Type21\",\"enum\":[0,1]},{\"description\":\"Soil_Type22\",\"enum\":[0,1]},{\"description\":\"Soil_Type23\",\"enum\":[0,1]},{\"description\":\"Soil_Type24\",\"enum\":[0,1]},{\"description\":\"Soil_Type25\",\"enum\":[0,1]},{\"description\":\"Soil_Type26\",\"enum\":[0,1]},{\"description\":\"Soil_Type27\",\"enum\":[0,1]},{\"description\":\"Soil_Type28\",\"enum\":[0,1]},{\"description\":\"Soil_Type29\",\"enum\":[0,1]},{\"description\":\"Soil_Type30\",\"enum\":[0,1]},{\"description\":\"Soil_Type31\",\"enum\":[0,1]},{\"description\":\"Soil_Type32\",\"enum\":[0,1]},{\"description\":\"Soil_Type33\",\"enum\":[0,1]},{\"description\":\"Soil_Type34\",\"enum\":[0,1]},{\"description\":\"Soil_Type35\",\"enum\":[0,1]},{\"description\":\"Soil_Type36\",\"enum\":[0,1]},{\"description\":\"Soil_Type37\",\"enum\":[0,1]},{\"description\":\"Soil_Type38\",\"enum\":[0,1]},{\"description\":\"Soil_Type39\",\"enum\":[0,1]},{\"description\":\"Soil_Type40\",\"enum\":[0,1]}]}},\"y\":{\"type\":\"array\",\"items\":{\"description\":\"The cover type, i.e., the domiNonet species of trees.\",\"enum\":[\"spruce_fir\",\"lodgepole_pine\",\"ponderosa_pine\",\"cottonwood_willow\",\"aspen\",\"douglas_fir\",\"krummholz\"]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\",\"minimum\":0}}},\"y\":{}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id41() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":150,\"maxItems\":150,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":4,\"maxItems\":4,\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"minItems\":150,\"maxItems\":150,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training the TfidfVectorizer from scikit-learn.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"minItems\":1,\"maxItems\":1,\"type\":\"array\",\"items\":{\"type\":\"string\"}}}]},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id42() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":120,\"maxItems\":120,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":4,\"maxItems\":4,\"type\":\"array\",\"items\":[{\"description\":\"sepal length (cm)\",\"type\":\"number\"},{\"description\":\"sepal width (cm)\",\"type\":\"number\"},{\"description\":\"petal length (cm)\",\"type\":\"number\"},{\"description\":\"petal width (cm)\",\"type\":\"number\"}]}},\"y\":{\"minItems\":120,\"maxItems\":120,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"target\",\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training the TfidfVectorizer from scikit-learn.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"minItems\":1,\"maxItems\":1,\"type\":\"array\",\"items\":{\"type\":\"string\"}}}]},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id43() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":1437,\"maxItems\":1437,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":64,\"maxItems\":64,\"type\":\"array\",\"items\":{\"maximum\":16,\"type\":\"number\",\"minimum\":0}}},\"y\":{\"minItems\":1437,\"maxItems\":1437,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"target\",\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training the TfidfVectorizer from scikit-learn.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"minItems\":1,\"maxItems\":1,\"type\":\"array\",\"items\":{\"type\":\"string\"}}}]},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id44() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":16512,\"maxItems\":16512,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":8,\"maxItems\":8,\"type\":\"array\",\"items\":[{\"description\":\"MedInc\",\"type\":\"number\",\"minimum\":0},{\"description\":\"HouseAge\",\"type\":\"number\",\"minimum\":0},{\"description\":\"AveRooms\",\"type\":\"number\",\"minimum\":0},{\"description\":\"AveBedrms\",\"type\":\"number\",\"minimum\":0},{\"description\":\"Population\",\"type\":\"number\",\"minimum\":0},{\"description\":\"AveOccup\",\"type\":\"number\",\"minimum\":0},{\"description\":\"Latitude\",\"type\":\"number\",\"minimum\":0},{\"description\":\"Longitude\",\"type\":\"number\"}]}},\"y\":{\"minItems\":16512,\"maxItems\":16512,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"target\",\"type\":\"number\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training the TfidfVectorizer from scikit-learn.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"minItems\":1,\"maxItems\":1,\"type\":\"array\",\"items\":{\"type\":\"string\"}}}]},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id45() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":670,\"maxItems\":670,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":20,\"maxItems\":20,\"type\":\"array\",\"items\":[{\"description\":\"checking_status\",\"enum\":[\"<0\",\"0<=X<200\",\">=200\",\"no checking\"]},{\"description\":\"duration\",\"type\":\"number\"},{\"description\":\"credit_history\",\"enum\":[\"no credits\\/all paid\",\"all paid\",\"existing paid\",\"delayed previously\",\"critical\\/other existing credit\"]},{\"description\":\"purpose\",\"enum\":[\"new car\",\"used car\",\"furniture\\/equipment\",\"radio\\/tv\",\"domestic appliance\",\"repairs\",\"education\",\"vacation\",\"retraining\",\"business\",\"other\"]},{\"description\":\"credit_amount\",\"type\":\"number\"},{\"description\":\"savings_status\",\"enum\":[\"<100\",\"100<=X<500\",\"500<=X<1000\",\">=1000\",\"no known savings\"]},{\"description\":\"employment\",\"enum\":[\"unemployed\",\"<1\",\"1<=X<4\",\"4<=X<7\",\">=7\"]},{\"description\":\"installment_commitment\",\"type\":\"number\"},{\"description\":\"personal_status\",\"enum\":[\"male div\\/sep\",\"female div\\/dep\\/mar\",\"male single\",\"male mar\\/wid\",\"female single\"]},{\"description\":\"other_parties\",\"enum\":[\"none\",\"co applicant\",\"guarantor\"]},{\"description\":\"residence_since\",\"type\":\"number\"},{\"description\":\"property_magnitude\",\"enum\":[\"real estate\",\"life insurance\",\"car\",\"no known property\"]},{\"description\":\"age\",\"type\":\"number\"},{\"description\":\"other_payment_plans\",\"enum\":[\"bank\",\"stores\",\"none\"]},{\"description\":\"housing\",\"enum\":[\"rent\",\"own\",\"for free\"]},{\"description\":\"existing_credits\",\"type\":\"number\"},{\"description\":\"job\",\"enum\":[\"unemp\\/unskilled non res\",\"unskilled resident\",\"skilled\",\"high qualif\\/self emp\\/mgmt\"]},{\"description\":\"num_dependents\",\"type\":\"number\"},{\"description\":\"own_telephone\",\"enum\":[\"none\",\"yes\"]},{\"description\":\"foreign_worker\",\"enum\":[\"yes\",\"no\"]}]}},\"y\":{\"minItems\":670,\"maxItems\":670,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"class\",\"enum\":[0,1]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training the TfidfVectorizer from scikit-learn.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"minItems\":1,\"maxItems\":1,\"type\":\"array\",\"items\":{\"type\":\"string\"}}}]},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id46() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":10662,\"maxItems\":10662,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"type\":\"string\"}},\"y\":{\"minItems\":10662,\"maxItems\":10662,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"type\":\"integer\"}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training the TfidfVectorizer from scikit-learn.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"minItems\":1,\"maxItems\":1,\"type\":\"array\",\"items\":{\"type\":\"string\"}}}]},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id47() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":161297,\"maxItems\":161297,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":5,\"maxItems\":5,\"type\":\"array\",\"items\":[{\"description\":\"drugName\",\"type\":\"string\"},{\"description\":\"condition\",\"anyOf\":[{\"type\":\"string\"},{\"enum\":[null]}]},{\"description\":\"review\",\"type\":\"string\"},{\"description\":\"date\",\"type\":\"string\"},{\"description\":\"usefulCount\",\"type\":\"integer\",\"minimum\":0}]}},\"y\":{\"minItems\":161297,\"maxItems\":161297,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"rating\",\"enum\":[1,2,3,4,5,6,7,8,9,10]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training the TfidfVectorizer from scikit-learn.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"minItems\":1,\"maxItems\":1,\"type\":\"array\",\"items\":{\"type\":\"string\"}}}]},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id48() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"minItems\":14,\"maxItems\":14,\"type\":\"array\",\"items\":[{\"description\":\"age\",\"type\":\"number\"},{\"description\":\"workclass\",\"type\":\"string\"},{\"description\":\"fnlwgt\",\"type\":\"number\"},{\"description\":\"education\",\"type\":\"string\"},{\"description\":\"education-num\",\"type\":\"number\"},{\"description\":\"marital-status\",\"type\":\"string\"},{\"description\":\"occupation\",\"type\":\"string\"},{\"description\":\"relationship\",\"type\":\"string\"},{\"description\":\"race\",\"type\":\"string\"},{\"description\":\"sex\",\"type\":\"string\"},{\"description\":\"capital-gain\",\"type\":\"number\"},{\"description\":\"capital-loss\",\"type\":\"number\"},{\"description\":\"hours-per-week\",\"type\":\"number\"},{\"description\":\"native-country\",\"type\":\"string\"}]}},\"y\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"description\":\"class\",\"enum\":[\"<=50K\",\">50K\"]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training the TfidfVectorizer from scikit-learn.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"minItems\":1,\"maxItems\":1,\"type\":\"array\",\"items\":{\"type\":\"string\"}}}]},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id49() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"minItems\":105,\"maxItems\":105,\"type\":\"array\",\"items\":[{\"description\":\"workclass_Federal-gov\",\"enum\":[0,1]},{\"description\":\"workclass_Local-gov\",\"enum\":[0,1]},{\"description\":\"workclass_Never-worked\",\"enum\":[0,1]},{\"description\":\"workclass_Private\",\"enum\":[0,1]},{\"description\":\"workclass_Self-emp-inc\",\"enum\":[0,1]},{\"description\":\"workclass_Self-emp-not-inc\",\"enum\":[0,1]},{\"description\":\"workclass_State-gov\",\"enum\":[0,1]},{\"description\":\"workclass_Without-pay\",\"enum\":[0,1]},{\"description\":\"education_10th\",\"enum\":[0,1]},{\"description\":\"education_11th\",\"enum\":[0,1]},{\"description\":\"education_12th\",\"enum\":[0,1]},{\"description\":\"education_1st-4th\",\"enum\":[0,1]},{\"description\":\"education_5th-6th\",\"enum\":[0,1]},{\"description\":\"education_7th-8th\",\"enum\":[0,1]},{\"description\":\"education_9th\",\"enum\":[0,1]},{\"description\":\"education_Assoc-acdm\",\"enum\":[0,1]},{\"description\":\"education_Assoc-voc\",\"enum\":[0,1]},{\"description\":\"education_Bachelors\",\"enum\":[0,1]},{\"description\":\"education_Doctorate\",\"enum\":[0,1]},{\"description\":\"education_HS-grad\",\"enum\":[0,1]},{\"description\":\"education_Masters\",\"enum\":[0,1]},{\"description\":\"education_Preschool\",\"enum\":[0,1]},{\"description\":\"education_Prof-school\",\"enum\":[0,1]},{\"description\":\"education_Some-college\",\"enum\":[0,1]},{\"description\":\"marital-status_Divorced\",\"enum\":[0,1]},{\"description\":\"marital-status_Married-AF-spouse\",\"enum\":[0,1]},{\"description\":\"marital-status_Married-civ-spouse\",\"enum\":[0,1]},{\"description\":\"marital-status_Married-spouse-absent\",\"enum\":[0,1]},{\"description\":\"marital-status_Never-married\",\"enum\":[0,1]},{\"description\":\"marital-status_Separated\",\"enum\":[0,1]},{\"description\":\"marital-status_Widowed\",\"enum\":[0,1]},{\"description\":\"occupation_Adm-clerical\",\"enum\":[0,1]},{\"description\":\"occupation_Armed-Forces\",\"enum\":[0,1]},{\"description\":\"occupation_Craft-repair\",\"enum\":[0,1]},{\"description\":\"occupation_Exec-managerial\",\"enum\":[0,1]},{\"description\":\"occupation_Farming-fishing\",\"enum\":[0,1]},{\"description\":\"occupation_Handlers-cleaners\",\"enum\":[0,1]},{\"description\":\"occupation_Machine-op-inspct\",\"enum\":[0,1]},{\"description\":\"occupation_Other-service\",\"enum\":[0,1]},{\"description\":\"occupation_Priv-house-serv\",\"enum\":[0,1]},{\"description\":\"occupation_Prof-specialty\",\"enum\":[0,1]},{\"description\":\"occupation_Protective-serv\",\"enum\":[0,1]},{\"description\":\"occupation_Sales\",\"enum\":[0,1]},{\"description\":\"occupation_Tech-support\",\"enum\":[0,1]},{\"description\":\"occupation_Transport-moving\",\"enum\":[0,1]},{\"description\":\"relationship_Husband\",\"enum\":[0,1]},{\"description\":\"relationship_Not-in-family\",\"enum\":[0,1]},{\"description\":\"relationship_Other-relative\",\"enum\":[0,1]},{\"description\":\"relationship_Own-child\",\"enum\":[0,1]},{\"description\":\"relationship_Unmarried\",\"enum\":[0,1]},{\"description\":\"relationship_Wife\",\"enum\":[0,1]},{\"description\":\"race_Amer-Indian-Eskimo\",\"enum\":[0,1]},{\"description\":\"race_Asian-Pac-Islander\",\"enum\":[0,1]},{\"description\":\"race_Black\",\"enum\":[0,1]},{\"description\":\"race_Other\",\"enum\":[0,1]},{\"description\":\"race_White\",\"enum\":[0,1]},{\"description\":\"sex_Female\",\"enum\":[0,1]},{\"description\":\"sex_Male\",\"enum\":[0,1]},{\"description\":\"native-country_Cambodia\",\"enum\":[0,1]},{\"description\":\"native-country_Canada\",\"enum\":[0,1]},{\"description\":\"native-country_China\",\"enum\":[0,1]},{\"description\":\"native-country_Columbia\",\"enum\":[0,1]},{\"description\":\"native-country_Cuba\",\"enum\":[0,1]},{\"description\":\"native-country_Dominican-Republic\",\"enum\":[0,1]},{\"description\":\"native-country_Ecuador\",\"enum\":[0,1]},{\"description\":\"native-country_El-Salvador\",\"enum\":[0,1]},{\"description\":\"native-country_England\",\"enum\":[0,1]},{\"description\":\"native-country_France\",\"enum\":[0,1]},{\"description\":\"native-country_Germany\",\"enum\":[0,1]},{\"description\":\"native-country_Greece\",\"enum\":[0,1]},{\"description\":\"native-country_Guatemala\",\"enum\":[0,1]},{\"description\":\"native-country_Haiti\",\"enum\":[0,1]},{\"description\":\"native-country_Holand-Netherlands\",\"enum\":[0,1]},{\"description\":\"native-country_Honduras\",\"enum\":[0,1]},{\"description\":\"native-country_Hong\",\"enum\":[0,1]},{\"description\":\"native-country_Hungary\",\"enum\":[0,1]},{\"description\":\"native-country_India\",\"enum\":[0,1]},{\"description\":\"native-country_Iran\",\"enum\":[0,1]},{\"description\":\"native-country_Ireland\",\"enum\":[0,1]},{\"description\":\"native-country_Italy\",\"enum\":[0,1]},{\"description\":\"native-country_Jamaica\",\"enum\":[0,1]},{\"description\":\"native-country_Japan\",\"enum\":[0,1]},{\"description\":\"native-country_Laos\",\"enum\":[0,1]},{\"description\":\"native-country_Mexico\",\"enum\":[0,1]},{\"description\":\"native-country_Nicaragua\",\"enum\":[0,1]},{\"description\":\"native-country_Outlying-US(Guam-USVI-etc)\",\"enum\":[0,1]},{\"description\":\"native-country_Peru\",\"enum\":[0,1]},{\"description\":\"native-country_Philippines\",\"enum\":[0,1]},{\"description\":\"native-country_Poland\",\"enum\":[0,1]},{\"description\":\"native-country_Portugal\",\"enum\":[0,1]},{\"description\":\"native-country_Puerto-Rico\",\"enum\":[0,1]},{\"description\":\"native-country_Scotland\",\"enum\":[0,1]},{\"description\":\"native-country_South\",\"enum\":[0,1]},{\"description\":\"native-country_Taiwan\",\"enum\":[0,1]},{\"description\":\"native-country_Thailand\",\"enum\":[0,1]},{\"description\":\"native-country_Trinadad&Tobago\",\"enum\":[0,1]},{\"description\":\"native-country_United-States\",\"enum\":[0,1]},{\"description\":\"native-country_Vietnam\",\"enum\":[0,1]},{\"description\":\"native-country_Yugoslavia\",\"enum\":[0,1]},{\"description\":\"age\",\"type\":\"number\"},{\"description\":\"fnlwgt\",\"type\":\"number\"},{\"description\":\"education-num\",\"type\":\"number\"},{\"description\":\"capital-gain\",\"type\":\"number\"},{\"description\":\"capital-loss\",\"type\":\"number\"},{\"description\":\"hours-per-week\",\"type\":\"number\"}]}},\"y\":{\"minItems\":32724,\"maxItems\":32724,\"type\":\"array\",\"items\":{\"description\":\"class\",\"enum\":[0,1]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training the TfidfVectorizer from scikit-learn.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"minItems\":1,\"maxItems\":1,\"type\":\"array\",\"items\":{\"type\":\"string\"}}}]},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_ai_subschema_id50() throws Exception {

        String schema1 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"additionalProperties\":false,\"documentation_url\":\"https:\\/\\/scikit-learn.org\\/0.20\\/datasets\\/index.html#forest-covertypes\",\"type\":\"object\",\"required\":[\"X\",\"y\"],\"properties\":{\"X\":{\"type\":\"array\",\"items\":{\"minItems\":54,\"maxItems\":54,\"type\":\"array\",\"items\":[{\"description\":\"Elevation\",\"type\":\"integer\"},{\"description\":\"Aspect\",\"type\":\"integer\"},{\"description\":\"Slope\",\"type\":\"integer\"},{\"description\":\"Horizontal_Distance_To_Hydrology\",\"type\":\"integer\"},{\"description\":\"Vertical_Distance_To_Hydrology\",\"type\":\"integer\"},{\"description\":\"Horizontal_Distance_To_Roadways\",\"type\":\"integer\"},{\"description\":\"Hillshade_9am\",\"type\":\"integer\"},{\"description\":\"Hillshade_Noon\",\"type\":\"integer\"},{\"description\":\"Hillshade_3pm\",\"type\":\"integer\"},{\"description\":\"Horizontal_Distance_To_Fire_Points\",\"type\":\"integer\"},{\"description\":\"Wilderness_Area1\",\"enum\":[0,1]},{\"description\":\"Wilderness_Area2\",\"enum\":[0,1]},{\"description\":\"Wilderness_Area3\",\"enum\":[0,1]},{\"description\":\"Wilderness_Area4\",\"enum\":[0,1]},{\"description\":\"Soil_Type1\",\"enum\":[0,1]},{\"description\":\"Soil_Type2\",\"enum\":[0,1]},{\"description\":\"Soil_Type3\",\"enum\":[0,1]},{\"description\":\"Soil_Type4\",\"enum\":[0,1]},{\"description\":\"Soil_Type5\",\"enum\":[0,1]},{\"description\":\"Soil_Type6\",\"enum\":[0,1]},{\"description\":\"Soil_Type7\",\"enum\":[0,1]},{\"description\":\"Soil_Type8\",\"enum\":[0,1]},{\"description\":\"Soil_Type9\",\"enum\":[0,1]},{\"description\":\"Soil_Type10\",\"enum\":[0,1]},{\"description\":\"Soil_Type11\",\"enum\":[0,1]},{\"description\":\"Soil_Type12\",\"enum\":[0,1]},{\"description\":\"Soil_Type13\",\"enum\":[0,1]},{\"description\":\"Soil_Type14\",\"enum\":[0,1]},{\"description\":\"Soil_Type15\",\"enum\":[0,1]},{\"description\":\"Soil_Type16\",\"enum\":[0,1]},{\"description\":\"Soil_Type17\",\"enum\":[0,1]},{\"description\":\"Soil_Type18\",\"enum\":[0,1]},{\"description\":\"Soil_Type19\",\"enum\":[0,1]},{\"description\":\"Soil_Type20\",\"enum\":[0,1]},{\"description\":\"Soil_Type21\",\"enum\":[0,1]},{\"description\":\"Soil_Type22\",\"enum\":[0,1]},{\"description\":\"Soil_Type23\",\"enum\":[0,1]},{\"description\":\"Soil_Type24\",\"enum\":[0,1]},{\"description\":\"Soil_Type25\",\"enum\":[0,1]},{\"description\":\"Soil_Type26\",\"enum\":[0,1]},{\"description\":\"Soil_Type27\",\"enum\":[0,1]},{\"description\":\"Soil_Type28\",\"enum\":[0,1]},{\"description\":\"Soil_Type29\",\"enum\":[0,1]},{\"description\":\"Soil_Type30\",\"enum\":[0,1]},{\"description\":\"Soil_Type31\",\"enum\":[0,1]},{\"description\":\"Soil_Type32\",\"enum\":[0,1]},{\"description\":\"Soil_Type33\",\"enum\":[0,1]},{\"description\":\"Soil_Type34\",\"enum\":[0,1]},{\"description\":\"Soil_Type35\",\"enum\":[0,1]},{\"description\":\"Soil_Type36\",\"enum\":[0,1]},{\"description\":\"Soil_Type37\",\"enum\":[0,1]},{\"description\":\"Soil_Type38\",\"enum\":[0,1]},{\"description\":\"Soil_Type39\",\"enum\":[0,1]},{\"description\":\"Soil_Type40\",\"enum\":[0,1]}]}},\"y\":{\"type\":\"array\",\"items\":{\"description\":\"The cover type, i.e., the domiNonet species of trees.\",\"enum\":[\"spruce_fir\",\"lodgepole_pine\",\"ponderosa_pine\",\"cottonwood_willow\",\"aspen\",\"douglas_fir\",\"krummholz\"]}}}}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training the TfidfVectorizer from scikit-learn.\",\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"X\"],\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"anyOf\":[{\"type\":\"array\",\"items\":{\"type\":\"string\"}},{\"type\":\"array\",\"items\":{\"minItems\":1,\"maxItems\":1,\"type\":\"array\",\"items\":{\"type\":\"string\"}}}]},\"y\":{\"description\":\"Target class labels; the array is over samples.\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_schema_errors_id1() throws Exception {

        String schema1 = "{\"type\":[\"foo\",\"string\"]}";
        String schema2 = "{}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_schema_errors_id2() throws Exception {

        String schema1 = "{\"type\":\"foo\"}";
        String schema2 = "{}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id1() throws Exception {

        String schema1 = "{\"type\":\"object\",\"dependencies\":{\"foo\":{\"type\":\"string\"}}}";
        String schema2 = "{\"type\":\"object\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id2() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"},\"email\":{\"format\":\"email\",\"type\":\"string\"}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"M\",\"F\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"},\"email\":{\"format\":\"email\",\"type\":\"string\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id3() throws Exception {

        String schema1 = "{\"maxProperties\":3,\"type\":\"object\"}";
        String schema2 = "{\"type\":\"object\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id4() throws Exception {

        String schema1 = "{\"maxProperties\":3,\"type\":\"object\",\"minProperties\":1}";
        String schema2 = "{\"type\":\"object\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id5() throws Exception {

        String schema1 = "{\"maxProperties\":3,\"type\":\"object\",\"minProperties\":1}";
        String schema2 = "{\"maxProperties\":5,\"type\":\"object\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id6() throws Exception {

        String schema1 = "{\"maxProperties\":3,\"type\":\"object\",\"minProperties\":1}";
        String schema2 = "{\"maxProperties\":2,\"type\":\"object\",\"minProperties\":5}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id7() throws Exception {

        String schema1 = "{\"maxProperties\":10,\"type\":\"object\",\"minProperties\":1}";
        String schema2 = "{\"maxProperties\":5,\"type\":\"object\",\"minProperties\":2}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id8() throws Exception {

        String schema1 = "{\"type\":\"object\",\"minProperties\":1}";
        String schema2 = "{\"type\":\"object\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id9() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"X\":{\"minItems\":120,\"maxItems\":120,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":4,\"maxItems\":4,\"type\":\"array\",\"items\":[{\"description\":\"sepal length (cm)\",\"type\":\"number\"},{\"description\":\"sepal width (cm)\",\"type\":\"number\"},{\"description\":\"petal length (cm)\",\"type\":\"number\"},{\"description\":\"petal width (cm)\",\"type\":\"number\"}]}},\"y\":{\"minItems\":120,\"maxItems\":120,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"description\":\"target\",\"type\":\"integer\"}}},\"required\":[\"X\",\"y\"]}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"required\":[\"X\",\"y\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id10() throws Exception {

        String schema1 = "{\"type\":\"object\",\"minProperties\":1}";
        String schema2 = "{\"type\":\"object\",\"required\":[\"p1\",\"p2\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id11() throws Exception {

        String schema1 = "{\"maxProperties\":1,\"type\":\"object\"}";
        String schema2 = "{\"type\":\"object\",\"required\":[\"p1\",\"p2\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id12() throws Exception {

        String schema1 = "{\"type\":\"object\",\"required\":[\"p2\",\"p1\"]}";
        String schema2 = "{\"type\":\"object\",\"required\":[\"p1\",\"p2\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id13() throws Exception {

        String schema1 = "{\"type\":\"object\",\"required\":[\"p1\"]}";
        String schema2 = "{\"type\":\"object\",\"required\":[\"p2\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id14() throws Exception {

        String schema1 = "{\"type\":\"object\",\"required\":[\"p1\",\"p2\"]}";
        String schema2 = "{\"type\":\"object\",\"required\":[\"p2\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id15() throws Exception {

        String schema1 = "{\"type\":\"object\",\"required\":[\"p1\",\"p2\"]}";
        String schema2 = "{\"additionalProperties\":{\"type\":\"boolean\"},\"type\":\"object\",\"required\":[\"p2\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id16() throws Exception {

        String schema1 = "{\"type\":\"object\",\"minProperties\":1}";
        String schema2 = "{\"type\":\"object\",\"required\":[\"p1\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id17() throws Exception {

        String schema1 = "{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"X\":{\"minItems\":150,\"maxItems\":150,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"minItems\":4,\"maxItems\":4,\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"minItems\":150,\"maxItems\":150,\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"type\":\"array\",\"items\":{\"type\":\"integer\"}}},\"required\":[\"X\",\"y\"]}";
        String schema2 = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-04\\/schema#\",\"description\":\"Input data schema for training.\",\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"X\":{\"description\":\"Features; the outer array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"y\":{\"description\":\"Target class labels; the array is over samples.\",\"type\":\"array\",\"items\":{\"type\":\"number\"}}},\"required\":[\"X\",\"y\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id18() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"},\"email\":{\"format\":\"email\",\"type\":\"string\"}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id19() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"},\"email\":{\"format\":\"email\",\"type\":\"string\"}}}";
        String schema2 = "{\"patternProperties\":{\"^b.*b$\":{\"type\":\"boolean\"}},\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"},\"email\":{\"format\":\"email\",\"type\":\"string\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id20() throws Exception {

        String schema1 = "{\"patternProperties\":{\"b.*b\":{\"type\":\"boolean\"}},\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"},\"email\":{\"format\":\"email\",\"type\":\"string\"}}}";
        String schema2 = "{\"patternProperties\":{\"^ba+b$\":{\"type\":\"boolean\"}},\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"},\"email\":{\"format\":\"email\",\"type\":\"string\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id21() throws Exception {

        String schema1 = "{\"patternProperties\":{\"b.*b\":{\"type\":\"integer\"}},\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"},\"email\":{\"format\":\"email\",\"type\":\"string\"}}}";
        String schema2 = "{\"patternProperties\":{\"^ba+b$\":{\"type\":\"boolean\"}},\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"},\"email\":{\"format\":\"email\",\"type\":\"string\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id22() throws Exception {

        String schema1 = "{\"patternProperties\":{\"b.*b\":{\"type\":\"integer\"}},\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"},\"email\":{\"format\":\"email\",\"type\":\"string\"}}}";
        String schema2 = "{\"patternProperties\":{\"^b(\\\\w)+b$\":{\"type\":\"integer\",\"minimum\":10}},\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"},\"email\":{\"format\":\"email\",\"type\":\"string\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id23() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"emaik\":{\"format\":\"email\",\"type\":\"string\"},\"age\":{\"type\":\"integer\"},\"email\":{\"format\":\"email\",\"type\":\"string\"}}}";
        String schema2 = "{\"patternProperties\":{\"^emai(l|k)$\":{\"type\":\"string\"}},\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"}},\"required\":[\"name\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id24() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"emaik\":{\"format\":\"email\",\"type\":\"string\"},\"age\":{\"type\":\"integer\"},\"email\":{\"format\":\"email\",\"type\":\"string\"}}}";
        String schema2 = "{\"patternProperties\":{\"^emai(l|k)$\":{\"type\":\"string\"}},\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id25() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"emaik\":{\"format\":\"email\",\"type\":\"string\"},\"age\":{\"type\":\"integer\"},\"email\":{\"format\":\"email\",\"type\":\"string\"}}}";
        String schema2 = "{\"patternProperties\":{\"emai\":{\"type\":\"string\"}},\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id26() throws Exception {

        String schema1 = "{\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"emaik\":{\"format\":\"email\",\"type\":\"string\"},\"age\":{\"type\":\"integer\"},\"email\":{\"format\":\"email\",\"type\":\"string\"}}}";
        String schema2 = "{\"patternProperties\":{\"emai\":{\"minLength\":10,\"type\":\"string\"}},\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id27() throws Exception {

        String schema1 = "{\"additionalProperties\":{\"type\":\"boolean\"},\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"emaik\":{\"format\":\"email\",\"type\":\"string\"},\"age\":{\"type\":\"integer\"},\"email\":{\"format\":\"email\",\"type\":\"string\"}}}";
        String schema2 = "{\"patternProperties\":{\"emai\":{\"minLength\":10,\"type\":\"string\"}},\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id28() throws Exception {

        String schema1 = "{\"additionalProperties\":{\"type\":\"boolean\"},\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"emaik\":{\"format\":\"email\",\"type\":\"string\"},\"age\":{\"type\":\"integer\"},\"email\":{\"format\":\"email\",\"type\":\"string\"}}}";
        String schema2 = "{\"patternProperties\":{\"emai\":{\"minLength\":10,\"type\":\"string\"}},\"additionalProperties\":{\"type\":\"boolean\"},\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_object_id29() throws Exception {

        String schema1 = "{\"additionalProperties\":{\"type\":\"string\"},\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"emaik\":{\"format\":\"email\",\"type\":\"string\"},\"age\":{\"type\":\"integer\"},\"email\":{\"format\":\"email\",\"type\":\"string\"}}}";
        String schema2 = "{\"patternProperties\":{\"emai\":{\"type\":\"string\"}},\"type\":\"object\",\"properties\":{\"gender\":{\"type\":\"string\",\"maxLength\":1,\"enum\":[\"F\",\"M\"]},\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_refs_id1() throws Exception {

        String schema1 = "{\"type\":\"array\",\"items\":{\"$ref\":\"#\\/definitions\\/positiveInteger\"},\"definitions\":{\"positiveInteger\":{\"type\":\"integer\",\"exclusiveMinimum\":true,\"minimum\":0}}}";
        String schema2 = "{\"type\":\"array\",\"items\":{\"$ref\":\"#\\/definitions\\/positiveInteger\"},\"definitions\":{\"positiveInteger\":{\"type\":\"integer\",\"exclusiveMinimum\":true,\"minimum\":-1}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_refs_id2() throws Exception {

        String schema1 = "{\"type\":\"array\",\"items\":{\"$ref\":\"#\\/definitions\\/positiveInteger\"},\"definitions\":{\"positiveInteger\":{\"type\":\"integer\",\"exclusiveMinimum\":true,\"minimum\":0}}}";
        String schema2 = "{\"type\":\"array\",\"items\":{\"type\":\"integer\"}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_refs_id3() throws Exception {

        String schema1 = "{\"type\":\"array\",\"items\":{\"$ref\":\"#\\/definitions\\/positiveInteger\"},\"definitions\":{\"positiveInteger\":{\"type\":\"integer\",\"exclusiveMinimum\":true,\"minimum\":-1}}}";
        String schema2 = "{\"type\":\"array\",\"items\":{\"type\":\"integer\"}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_refs_id4() throws Exception {

        String schema1 = "{\"type\":\"array\",\"items\":{\"$ref\":\"#\\/definitions\\/positiveInteger\"},\"definitions\":{\"positiveInteger\":{\"type\":\"integer\",\"exclusiveMinimum\":true,\"minimum\":0}}}";
        String schema2 = "{\"type\":\"array\",\"items\":{\"type\":\"string\"}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_refs_id5() throws Exception {

        String schema1 = "{\"type\":\"array\",\"items\":{\"$ref\":\"#\\/definitions\\/positiveInteger\"},\"definitions\":{\"positiveInteger\":{\"type\":\"integer\",\"exclusiveMinimum\":true,\"minimum\":-1}}}";
        String schema2 = "{\"type\":\"array\",\"items\":{\"type\":\"string\"}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_refs_id6() throws Exception {

        String schema1 = "{\"type\":\"array\",\"items\":{\"$ref\":\"#\\/definitions\\/positiveInteger\"},\"definitions\":{\"positiveInteger\":{\"type\":\"integer\",\"exclusiveMinimum\":true,\"minimum\":0}}}";
        String schema2 = "{\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_refs_id7() throws Exception {

        String schema1 = "{\"type\":\"array\",\"items\":{\"$ref\":\"#\\/definitions\\/positiveInteger\"},\"definitions\":{\"positiveInteger\":{\"type\":\"integer\",\"exclusiveMinimum\":true,\"minimum\":-1}}}";
        String schema2 = "{\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_refs_id8() throws Exception {

        String schema1 = "{\"enum\":[null]}";
        String schema2 = "{\"definitions\":{\"S\":{\"anyOf\":[{\"enum\":[null]},{\"allOf\":[{\"minItems\":2,\"maxItems\":2,\"type\":\"array\",\"items\":[{\"$ref\":\"#\\/definitions\\/S\"},{\"$ref\":\"#\\/definitions\\/S\"}]},{\"not\":{\"uniqueItems\":true,\"type\":\"array\"}}]}]}},\"$ref\":\"#\\/definitions\\/S\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_refs_id9() throws Exception {

        String schema1 = "{\"enum\":[null]}";
        String schema2 = "{\"type\":\"object\",\"definitions\":{\"person\":{\"type\":\"object\",\"properties\":{\"children\":{\"default\":[],\"type\":\"array\",\"items\":{\"$ref\":\"#\\/definitions\\/person\"}},\"name\":{\"type\":\"string\"}}}},\"properties\":{\"person\":{\"$ref\":\"#\\/definitions\\/person\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_refs_id10() throws Exception {

        String schema1 = "{\"type\":\"object\",\"definitions\":{\"bom\":{\"type\":\"string\"},\"tak\":{\"type\":\"integer\"}},\"properties\":{\"foo\":{\"type\":\"integer\",\"$ref\":\"#\\/definitions\\/bom\"}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"foo\":{\"type\":\"string\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_refs_id11() throws Exception {

        String schema1 = "{\"type\":\"object\",\"definitions\":{\"bom\":{\"type\":\"string\"},\"tak\":{\"type\":\"integer\"}},\"properties\":{\"foo\":{\"type\":\"integer\",\"$ref\":\"#\\/definitions\\/bom\"}}}";
        String schema2 = "{\"type\":\"object\",\"properties\":{\"foo\":{\"pattern\":\"a\",\"type\":\"string\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id1() throws Exception {

        String schema1 = "{\"allOf\":[{\"type\":\"string\"},{\"not\":{\"minLength\":2,\"type\":\"string\"}}]}";
        String schema2 = "{\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id2() throws Exception {

        String schema1 = "{\"allOf\":[{\"type\":\"string\"},{\"not\":{\"minLength\":2,\"type\":\"string\"}}]}";
        String schema2 = "{\"type\":\"string\",\"maxLength\":1}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id3() throws Exception {

        String schema1 = "{\"allOf\":[{\"type\":\"string\"},{\"not\":{\"minLength\":2,\"pattern\":\"ab\",\"type\":\"string\"}}]}";
        String schema2 = "{\"type\":\"string\",\"maxLength\":1}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id4() throws Exception {

        String schema1 = "{\"minLength\":1,\"type\":[\"string\",\"null\"]}";
        String schema2 = "{\"anyOf\":[{\"minLength\":1,\"type\":\"string\"},{\"type\":\"null\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id5() throws Exception {

        String schema1 = "{\"minLength\":1,\"type\":[\"string\",\"null\"]}";
        String schema2 = "{\"anyOf\":[{\"pattern\":\".+\",\"type\":\"string\"},{\"enum\":[null]}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id6() throws Exception {

        String schema1 = "{\"minLength\":1,\"type\":[\"string\",\"null\"]}";
        String schema2 = "{\"pattern\":\".{1,}\",\"type\":[\"string\",\"null\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id7() throws Exception {

        String schema1 = "{\"minLength\":1,\"type\":[\"string\",\"null\"]}";
        String schema2 = "{\"not\":{\"enum\":[\"\"]},\"type\":[\"string\",\"null\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id8() throws Exception {

        String schema1 = "{\"anyOf\":[{\"minLength\":1,\"type\":\"string\"},{\"type\":\"null\"}]}";
        String schema2 = "{\"anyOf\":[{\"pattern\":\".+\",\"type\":\"string\"},{\"enum\":[null]}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id9() throws Exception {

        String schema1 = "{\"anyOf\":[{\"minLength\":1,\"type\":\"string\"},{\"type\":\"null\"}]}";
        String schema2 = "{\"pattern\":\".{1,}\",\"type\":[\"string\",\"null\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id10() throws Exception {

        String schema1 = "{\"anyOf\":[{\"minLength\":1,\"type\":\"string\"},{\"type\":\"null\"}]}";
        String schema2 = "{\"not\":{\"enum\":[\"\"]},\"type\":[\"string\",\"null\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id11() throws Exception {

        String schema1 = "{\"anyOf\":[{\"pattern\":\".+\",\"type\":\"string\"},{\"enum\":[null]}]}";
        String schema2 = "{\"pattern\":\".{1,}\",\"type\":[\"string\",\"null\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id12() throws Exception {

        String schema1 = "{\"anyOf\":[{\"pattern\":\".+\",\"type\":\"string\"},{\"enum\":[null]}]}";
        String schema2 = "{\"not\":{\"enum\":[\"\"]},\"type\":[\"string\",\"null\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id13() throws Exception {

        String schema1 = "{\"pattern\":\".{1,}\",\"type\":[\"string\",\"null\"]}";
        String schema2 = "{\"not\":{\"enum\":[\"\"]},\"type\":[\"string\",\"null\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id14() throws Exception {

        String schema1 = "{\"pattern\":\".{2,}\",\"type\":[\"string\",\"null\"]}";
        String schema2 = "{\"minLength\":2,\"type\":[\"string\",\"null\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id15() throws Exception {

        String schema1 = "{\"pattern\":\".{2,}\",\"type\":[\"string\",\"null\"]}";
        String schema2 = "{\"minLength\":1,\"type\":[\"string\",\"null\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id16() throws Exception {

        String schema1 = "{\"minLength\":1,\"type\":[\"string\",\"null\"]}";
        String schema2 = "{\"minLength\":2,\"type\":[\"string\",\"null\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean result = false;

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship())) {
            result = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, result);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id17() throws Exception {

        String schema1 = "{\"allOf\":[{\"type\":\"string\"},{\"not\":{\"minLength\":5,\"pattern\":\"a\",\"type\":\"string\"}}]}";
        String schema2 = "{\"anyOf\":[{\"type\":\"string\",\"maxLength\":4},{\"pattern\":\"[^a]\",\"type\":\"string\"}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id18() throws Exception {

        String schema1 = "{\"not\":{\"type\":\"string\"}}";
        String schema2 = "{\"not\":{\"not\":{\"not\":{\"type\":\"string\"}}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id19() throws Exception {

        String schema1 = "{\"not\":{\"type\":\"string\"}}";
        String schema2 = "{\"not\":{\"not\":{\"type\":\"string\"}}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id20() throws Exception {

        String schema1 = "{\"type\":\"string\"}";
        String schema2 = "{\"not\":{\"type\":\"string\"}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id21() throws Exception {

        String schema1 = "{\"type\":\"string\"}";
        String schema2 = "{\"allOf\":[{\"type\":\"string\"},{\"not\":{\"minLength\":2,\"type\":\"string\"}}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id22() throws Exception {

        String schema1 = "{\"type\":\"string\",\"maxLength\":1}";
        String schema2 = "{\"allOf\":[{\"type\":\"string\"},{\"not\":{\"minLength\":2,\"type\":\"string\"}}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id23() throws Exception {

        String schema1 = "{\"minLength\":1,\"type\":\"string\",\"maxLength\":5}";
        String schema2 = "{\"allOf\":[{\"type\":\"string\"},{\"not\":{\"minLength\":2,\"type\":\"string\"}}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id24() throws Exception {

        String schema1 = "{\"minLength\":1,\"type\":\"string\",\"maxLength\":5}";
        String schema2 = "{\"allOf\":[{\"type\":\"string\"},{\"not\":{\"minLength\":2,\"type\":\"string\"}}]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id25() throws Exception {

        String schema1 = "{\"type\":\"string\",\"enum\":[\"a\"]}";
        String schema2 = "{\"enum\":[\"a\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id26() throws Exception {

        String schema1 = "{\"type\":\"string\",\"enum\":[\"a\"]}";
        String schema2 = "{\"enum\":[\"a\",\"b\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id27() throws Exception {

        String schema1 = "{\"type\":\"string\",\"enum\":[\"a\",\"\"]}";
        String schema2 = "{\"enum\":[\"a\",\"b\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id28() throws Exception {

        String schema1 = "{\"not\":{\"enum\":[\"a\"]},\"type\":\"string\"}";
        String schema2 = "{\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id29() throws Exception {

        String schema1 = "{\"not\":{\"enum\":[\"a\",\"b\"]},\"type\":\"string\"}";
        String schema2 = "{\"type\":\"string\",\"enum\":[\"a\",\"b\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id30() throws Exception {

        String schema1 = "{\"pattern\":\"\",\"type\":\"string\"}";
        String schema2 = "{\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id31() throws Exception {

        String schema1 = "{\"minLength\":5,\"type\":\"string\"}";
        String schema2 = "{\"type\":\"integer\",\"maxLength\":1}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id32() throws Exception {

        String schema1 = "{\"pattern\":\"(ab)*\",\"type\":\"string\",\"maxLength\":5}";
        String schema2 = "{\"pattern\":\"(ab){3}\",\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_string_id33() throws Exception {

        String schema1 = "{\"pattern\":\"^(ab)*$\",\"type\":\"string\",\"maxLength\":5}";
        String schema2 = "{\"pattern\":\"^(ab){0,3}$\",\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_enum_id1() throws Exception {

        String schema1 = "{\"enum\":[1]}";
        String schema2 = "{\"enum\":[1,2]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_enum_id2() throws Exception {

        String schema1 = "{\"enum\":[true]}";
        String schema2 = "{\"enum\":[1,2]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_enum_id3() throws Exception {

        String schema1 = "{\"type\":\"integer\",\"enum\":[1,2]}";
        String schema2 = "{\"type\":\"boolean\",\"enum\":[true]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_enum_id4() throws Exception {

        String schema1 = "{\"enum\":[\"1\",2]}";
        String schema2 = "{\"enum\":[1,\"2\"]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_enum_id5() throws Exception {

        String schema1 = "{\"type\":\"string\",\"enum\":[1,2]}";
        String schema2 = "{\"type\":\"string\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_enum_id6() throws Exception {

        String schema1 = "{\"type\":\"string\",\"enum\":[0,1]}";
        String schema2 = "{\"type\":\"boolean\",\"enum\":[0]}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_enum_id7() throws Exception {

        String schema1 = "{\"enum\":[]}";
        String schema2 = "{\"type\":\"boolean\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_enum_id8() throws Exception {

        String schema1 = "{\"enum\":[]}";
        String schema2 = "{\"not\":{}}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must be subset of schema2", true, resultS1Subset);
        Assert.assertEquals("schema2 must be subset of schema1", true, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_enum_id9() throws Exception {

        String schema1 = "{\"enum\":[[]]}";
        String schema2 = "{\"type\":\"array\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }

    @Test(timeout = 5000)
    public void tests_jsonsubschema_test_enum_id10() throws Exception {

        String schema1 = "{\"enum\":[{\"foo\":1}]}";
        String schema2 = "{\"type\":\"object\"}";
        data = jsonSchemaLib.compareSchemas(schema1, schema2);

        boolean resultS1Subset = false;
        boolean resultS2Subset = false;

        if (JsonSchemaRelationships.LEFT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS2Subset = true;
        }

        if (JsonSchemaRelationships.RIGHT_IS_GENERALIZATION.equals(data.getRelationship()) || JsonSchemaRelationships.EQUIVALENT.equals(data.getRelationship())) {
            resultS1Subset = true;
        }

        Assert.assertEquals("schema1 must not be subset of schema2", false, resultS1Subset);
        Assert.assertEquals("schema2 must not be subset of schema1", false, resultS2Subset);
    }
}
