package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.toJsonSchemaTest;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Const_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Luca Escher <escher_luca@web.de>
 */

public class Const_AssertionTest {

    private final WitnessVarManager manager = new WitnessVarManager();

    @Test
    public void testToJsonSchemaConst() {

        JsonObject expected = new JsonObject();
        JsonObject inner = new JsonObject();

        //testing:  Const_Assertion [{"type":"string"}]
        inner.addProperty("type", "string");
        Const_Assertion assertion = new Const_Assertion(inner);

        //Json Schema expression: {"const":{"type":"string"}}
        expected.add("const", inner);

        assertEquals(expected, assertion.toJSONSchema(manager));
    }
}
