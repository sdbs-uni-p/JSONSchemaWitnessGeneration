package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.toJsonSchemaTest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.OneOf_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Type_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.junit.Test;

import static it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings.TYPE_INTEGER;
import static it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings.TYPE_STRING;
import static org.junit.Assert.assertEquals;

/**
 * @author Luca Escher <escher_luca@web.de>
 */

public class OneOf_AssertionTest {

    private final WitnessVarManager manager = new WitnessVarManager();

    @Test
    public void toJsonSchemaTest() {

        //testing:  Xor_Assertion [[Type_Assertion [[int]], Type_Assertion [[str]]]]
        OneOf_Assertion assertion = new OneOf_Assertion();
        Type_Assertion aAdd = new Type_Assertion(TYPE_INTEGER);
        Type_Assertion bAdd = new Type_Assertion(TYPE_STRING);

        assertion.add(aAdd);
        assertion.add(bAdd);

        //Json Schema expression: {"oneOf":[{"type":"integer"},{"type":"string"}]}
        JsonArray array = new JsonArray();
        JsonObject a = new JsonObject();
        JsonObject b = new JsonObject();
        JsonObject exp = new JsonObject();

        a.addProperty("type", "integer");
        b.addProperty("type", "string");

        array.add(a);
        array.add(b);

        exp.add("oneOf", array);

        assertEquals(exp, assertion.toJSONSchema(manager));
    }
}
