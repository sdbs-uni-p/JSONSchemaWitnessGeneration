package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.toJsonSchemaTest;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Const_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Not_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Luca Escher <escher_luca@web.de>
 */

public class Not_AssertionTest {

    private final WitnessVarManager manager = new WitnessVarManager();

    @Test
    public void testToJsonSchemaNotTranslation() {

        JsonObject inner = new JsonObject();
        JsonObject object = new JsonObject();
        JsonObject exp = new JsonObject();

        //Algebra expression:   Not_Assertion [Const_Assertion [{"type":"string"}]]
        inner.addProperty("type", "string");
        Const_Assertion a = new Const_Assertion(inner);
        Not_Assertion notAssertion = new Not_Assertion(a);

        //Json Schema expression: {"not":{"const":{"type":"string"}}}
        object.add("const", inner);
        exp.add("not", object);

        assertEquals(exp, notAssertion.toJSONSchema(manager));
    }
}
