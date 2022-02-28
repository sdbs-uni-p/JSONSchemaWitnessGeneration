package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.toJsonSchemaTest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.AnyOf_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Boolean_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Const_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Luca Escher <escher_luca@web.de>
 */

public class AnyOf_AssertionTest {

    private final WitnessVarManager manager = new WitnessVarManager();
    private final AnyOf_Assertion assertion = new AnyOf_Assertion();

    @Test
    public void toJsonSchemaTest() {

        JsonObject a = new JsonObject();
        JsonObject constInner = new JsonObject();
        JsonObject expected = new JsonObject();
        JsonArray array = new JsonArray();
        JsonArray expArray = new JsonArray();

        //testing:  Or_Assertion [[Boolean_Assertion [value=false], Const_Assertion [[{"type":"string"}]]]]
        a.addProperty("type", "string");
        array.add(a);
        Const_Assertion const_assertion = new Const_Assertion(array);
        Boolean_Assertion boolean_assertion = new Boolean_Assertion(false);

        assertion.add(boolean_assertion);
        assertion.add(const_assertion);

        //Json Schema expression: {"anyOf":[false,{"const":[{"type":"string"}]}]}
        expArray.add(false);
        constInner.add("const", array);

        expArray.add(constInner);
        expected.add("anyOf", expArray);

        assertEquals(expected, assertion.toJSONSchema(manager));
    }
}
