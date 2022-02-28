package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.toJsonSchemaTest;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Type_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.junit.Test;

import static it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Luca Escher <escher_luca@web.de>
 */

public class Type_AssertionTest {

    private final WitnessVarManager witnessVarManager = new WitnessVarManager();
    private final JsonObject expected = new JsonObject();

    @Test
    public void testIntegerToJsonSchema() {
        //testing:  Type_Assertion [[int]]
        Type_Assertion testInteger = new Type_Assertion(TYPE_INTEGER);

        //Json Schema expression: {"type":"integer"}
        expected.addProperty("type", "integer");
        assertEquals(expected, testInteger.toJSONSchema(witnessVarManager));
    }

    @Test
    public void testBooleanToJsonSchema() {

        //testing:  Type_Assertion [[bool]]
        Type_Assertion testBoolean = new Type_Assertion(TYPE_BOOLEAN);

        //Json Schema expression: {"type":"boolean"}
        expected.addProperty("type", "boolean");
        assertEquals(expected, testBoolean.toJSONSchema(witnessVarManager));
    }

    @Test
    public void testNullToJsonSchema() {

        //testing:  Type_Assertion [[null]]
        Type_Assertion testNull = new Type_Assertion(TYPE_NULL);

        //Json Schema expression: {"type":"null"}
        expected.addProperty("type", "null");
        assertEquals(expected, testNull.toJSONSchema(witnessVarManager));
    }

    @Test
    public void testNumberToJsonSchema() {

        //testing:  Type_Assertion [[num]]
        Type_Assertion testNumber = new Type_Assertion(TYPE_NUMBER);

        //Json Schema expression: {"type":"number"}
        expected.addProperty("type", "number");
        assertEquals(expected, testNumber.toJSONSchema(witnessVarManager));
    }

    @Test
    public void testStringToJsonSchema() {

        //testing:  Type_Assertion [[str]]
        Type_Assertion testString = new Type_Assertion(TYPE_STRING);

        //Json Schema expression: {"type":"string"}
        expected.addProperty("type", "string");
        assertEquals(expected, testString.toJSONSchema(witnessVarManager));
    }

    @Test
    public void testArrayToJsonSchema() {

        //testing:  Type_Assertion [[arr]]
        Type_Assertion testArray = new Type_Assertion(TYPE_ARRAY);

        //Json Schema expression: {"type":"array"}
        expected.addProperty("type", "array");
        assertEquals(expected, testArray.toJSONSchema(witnessVarManager));
    }

    @Test
    public void testObjectToJsonSchema() {

        //testing:  Type_Assertion [[obj]]
        Type_Assertion testObject = new Type_Assertion(TYPE_OBJECT);

        //Json Schema expression: {"type":"object"}
        expected.addProperty("type", "object");
        assertEquals(expected, testObject.toJSONSchema(witnessVarManager));
    }
}
