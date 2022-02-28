package de.uni_passau.scs.testsuites.generate_tests.templates;


/**
 * This is a helper class.
 *
 * It provides templates for the to be generated tests.
 */
public class TestTemplates {

    // Normal Templates ------------------------

    public static String createTestTemplate2ResultsS2trueS1true(String name, String schema1, String schema2, String s1Subset, String s2Subset) {
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
                        "        Assert.assertEquals(\"schema 1 must be subset of schema 2\", " + s1Subset + ", resultS1Subset);\n" +
                        "        Assert.assertEquals(\"schema 2 must be subset of schema 1\", " + s2Subset + ", resultS2Subset);\n" +
                        "    }";

        return template;
    }

    public static String createTestTemplate2ResultsS2falseS1true(String name, String schema1, String schema2, String s1Subset, String s2Subset) {
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
                        "        Assert.assertEquals(\"schema 1 must be subset of schema 2\", " + s1Subset + ", resultS1Subset);\n" +
                        "        Assert.assertEquals(\"schema 2 must not be subset of schema 1\", " + s2Subset + ", resultS2Subset);\n" +
                        "    }";

        return template;
    }

    public static String createTestTemplate2ResultsS2falseS1false(String name, String schema1, String schema2, String s1Subset, String s2Subset) {
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
                        "        Assert.assertEquals(\"schema 1 must not be subset of schema 2\", " + s1Subset + ", resultS1Subset);\n" +
                        "        Assert.assertEquals(\"schema 2 must not be subset of schema 1\", " + s2Subset + ", resultS2Subset);\n" +
                        "    }";

        return template;
    }

    public static String createTestTemplate2ResultsS2trueS1false(String name, String schema1, String schema2, String s1Subset, String s2Subset) {
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
                        "        Assert.assertEquals(\"schema 1 must be subset of schema 2\", " + s1Subset + ", resultS1Subset);\n" +
                        "        Assert.assertEquals(\"schema 2 must not be subset of schema 1\", " + s2Subset + ", resultS2Subset);\n" +
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
                        "        Assert.assertEquals(\"schema1 must be subschema of schema2\", " + test + ", result);\n" +
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
                        "        Assert.assertEquals(\"schema1 must not be subschema of schema2\", " + test + ", result);\n" +
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
                        "        Assert.assertEquals(\"schema2 must be subset of schema1\", " + test + ", result);\n" +
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
                        "        Assert.assertEquals(\"schema2 must not be subset of schema1\", " + test + ", result);\n" +
                        "    }";

        return template;
    }

    // Ignore Templates ----------------------

    public static String ignoreCreateTestTemplate2ResultsS2trueS1true(String name, String schema1, String schema2, String s1Subset, String s2Subset) {
        String template =
                "    @Test(timeout = 5000)\n" +
                        "    @Ignore\n" +
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
                        "        Assert.assertEquals(\"schema 1 must be subset of schema 2\", " + s1Subset + ", resultS1Subset);\n" +
                        "        Assert.assertEquals(\"schema 2 must be subset of schema 1\", " + s2Subset + ", resultS2Subset);\n" +
                        "    }";

        return template;
    }

    public static String ignoreCreateTestTemplate2ResultsS2falseS1true(String name, String schema1, String schema2, String s1Subset, String s2Subset) {
        String template =
                "    @Test(timeout = 5000)\n" +
                        "    @Ignore\n" +
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
                        "        Assert.assertEquals(\"schema 1 must be subset of schema 2\", " + s1Subset + ", resultS1Subset);\n" +
                        "        Assert.assertEquals(\"schema 2 must not be subset of schema 1\", " + s2Subset + ", resultS2Subset);\n" +
                        "    }";

        return template;
    }

    public static String ignoreCreateTestTemplate2ResultsS2falseS1false(String name, String schema1, String schema2, String s1Subset, String s2Subset) {
        String template =
                "    @Test(timeout = 5000)\n" +
                        "    @Ignore\n" +
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
                        "        Assert.assertEquals(\"schema 1 must not be subset of schema 2\", " + s1Subset + ", resultS1Subset);\n" +
                        "        Assert.assertEquals(\"schema 2 must not be subset of schema 1\", " + s2Subset + ", resultS2Subset);\n" +
                        "    }";

        return template;
    }

    public static String ignoreCreateTestTemplate2ResultsS2trueS1false(String name, String schema1, String schema2, String s1Subset, String s2Subset) {
        String template =
                "    @Test(timeout = 5000)\n" +
                        "    @Ignore\n" +
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
                        "        Assert.assertEquals(\"schema 1 must be subset of schema 2\", " + s1Subset + ", resultS1Subset);\n" +
                        "        Assert.assertEquals(\"schema 2 must not be subset of schema 1\", " + s2Subset + ", resultS2Subset);\n" +
                        "    }";

        return template;
    }

    public static String ignoreCreateTestTemplateS1SubsetOfS2True(String name, String schema1, String schema2, String test) {
        String template =
                "    @Test(timeout = 5000)\n" +
                        "    @Ignore\n" +
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
                        "        Assert.assertEquals(\"schema1 must be subschema of schema2\", " + test + ", result);\n" +
                        "    }";

        return template;
    }

    public static String ignoreCreateTestTemplateS1SubsetOfS2False(String name, String schema1, String schema2, String test) {
        String template =
                "    @Test(timeout = 5000)\n" +
                        "    @Ignore\n" +
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
                        "        Assert.assertEquals(\"schema1 must not be subschema of schema2\", " + test + ", result);\n" +
                        "    }";

        return template;
    }

    public static String ignoreCreateTestTemplateS2SubsetOfS1True(String name, String schema1, String schema2, String test) {
        String template =
                "    @Test(timeout = 5000)\n" +
                        "    @Ignore\n" +
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
                        "        Assert.assertEquals(\"schema2 must be subset of schema1\", " + test + ", result);\n" +
                        "    }";

        return template;
    }

    public static String ignoreCreateTestTemplateS2SubsetOfS1False(String name, String schema1, String schema2, String test) {
        String template =
                "    @Test(timeout = 5000)\n" +
                        "    @Ignore\n" +
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
                        "        Assert.assertEquals(\"schema2 must not be subset of schema1\", " + test + ", result);\n" +
                        "    }";

        return template;
    }
}
