package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.toJsonSchemaTest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.AllOf_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Boolean_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Const_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Luca Escher <escher_luca@web.de>
 */

public class AllOf_AssertionTest {

    private final WitnessVarManager manager = new WitnessVarManager();
    private final AllOf_Assertion assertion = new AllOf_Assertion();

    @Test
    public void testToJsonSchemaFalseBoolean() {

        JsonObject expected = new JsonObject();
        JsonArray array = new JsonArray();

        //testing:  And_Assertion [[Boolean_Assertion [value=false]]]
        Boolean_Assertion boolean_assertion = new Boolean_Assertion(false);
        assertion.add(boolean_assertion);

        //Json Schema expression: {"allOf":[false]}
        array.add(false);
        expected.add("allOf", array);

        assertEquals(expected, assertion.toJSONSchema(manager));
    }


    // tests if the value(true) of the Boolean_Assertion is translated correctly
    @Test
    public void testToJsonSchemaTrueBoolean() {

        JsonObject expected = new JsonObject();
        JsonArray array = new JsonArray();

        //testing:  And_Assertion [[Boolean_Assertion [value=true]]]
        Boolean_Assertion boolean_assertion = new Boolean_Assertion(true);
        assertion.add(boolean_assertion);

        //Json Schema expression: {"allOf":[true]}
        array.add(true);
        expected.add("allOf", array);
        assertEquals(expected, assertion.toJSONSchema(manager));
    }

    //true + false = false
    @Test
    public void twoBooleanAssertions() {

        //testing:  And_Assertion [[Boolean_Assertion [value=true], Boolean_Assertion [value=false]]]
        Boolean_Assertion trueAssertion = new Boolean_Assertion(true);
        Boolean_Assertion falseAssertion = new Boolean_Assertion(false);

        assertion.add(trueAssertion);
        assertion.add(falseAssertion);

        //Json Schema expression:   {"allOf":[false]}
        JsonObject expected = new JsonObject();
        JsonArray array = new JsonArray();

        array.add(false);
        expected.add("allOf", array);

        assertEquals(expected, assertion.toJSONSchema(manager));
    }

    @Test
    public void differentAssertionsBooleanAndConst() {

        //testing:
        // And_Assertion [[Boolean_Assertion [value=true],
        // Const_Assertion [{"type":"integer"}], Const_Assertion [{"type":"boolean"}]]]
        JsonObject object = new JsonObject();
        JsonObject object2 = new JsonObject();

        object.addProperty("type", "integer");
        object2.addProperty("type", "boolean");

        Boolean_Assertion one = new Boolean_Assertion(true);
        Const_Assertion two = new Const_Assertion(object);
        Const_Assertion three = new Const_Assertion(object2);

        assertion.add(one);
        assertion.add(two);
        assertion.add(three);

        //Json Schema expression:
        //  {"allOf":[true,{"const":{"type":"integer"}},{"const":{"type":"boolean"}}]}
        JsonObject expConst1 = new JsonObject();
        JsonObject expConst2 = new JsonObject();
        JsonObject expected = new JsonObject();
        JsonArray expArray = new JsonArray();

        expConst1.add("const", object);
        expConst2.add("const", object2);
        expArray.add(true);
        expArray.add(expConst1);
        expArray.add(expConst2);
        expected.add("allOf", expArray);

        assertEquals(expected, assertion.toJSONSchema(manager));
    }

    @Test
    public void testingTwoNotBooleanAssertions() {

        //testing:
        // And_Assertion [[Const_Assertion [{"type":"integer"}], Const_Assertion [{"type":"string"}]]]
        JsonObject one = new JsonObject();
        JsonObject two = new JsonObject();

        one.addProperty("type", "integer");
        two.addProperty("type", "string");

        Const_Assertion const1 = new Const_Assertion(one);
        Const_Assertion const2 = new Const_Assertion(two);

        assertion.add(const1);
        assertion.add(const2);

        //Json Schema expression:
        // {"allOf":[{"const":{"type":"integer"}},{"const":{"type":"string"}}]}
        JsonObject expConst1 = new JsonObject();
        JsonObject expConst2 = new JsonObject();
        JsonObject expected = new JsonObject();
        JsonArray expArray = new JsonArray();

        expConst1.add("const", one);
        expConst2.add("const", two);
        expArray.add(expConst1);
        expArray.add(expConst2);
        expected.add("allOf", expArray);

        assertEquals(expected, assertion.toJSONSchema(manager));
    }
}
