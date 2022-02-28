package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.toJsonSchemaTest;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Len_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Luca Escher <escher_luca@web.de>
 */

public class Len_AssertionTest {

    private final JsonObject exp = new JsonObject();
    private final WitnessVarManager manager = new WitnessVarManager();

    private final long min = 1;
    private final long max = 20;

    @Test
    public void toJsonSchemaTest() {

        //testing:  Len_Assertion [min=1, max=20]
        Len_Assertion len = new Len_Assertion(min, max);

        //Json Schema expression:: {"minLength":1,"maxLength":20}
        exp.addProperty("minLength", min);
        exp.addProperty("maxLength", max);

        assertEquals(exp, len.toJSONSchema(manager));
    }

    //only min present
    @Test
    public void toJsonSchemOnlyMinTest() {
        //testing:  Len_Assertion [min=1, max=null]
        Len_Assertion len = new Len_Assertion(min, null);

        //Json Schema expression:: {"minLength":1}
        exp.addProperty("minLength", min);
        assertEquals(exp, len.toJSONSchema(manager));
    }

    //only max present
    @Test
    public void toJsonSchemOnlyMaxTest() {
        //testing:  Len_Assertion [min=null, max=20]
        Len_Assertion len = new Len_Assertion(null, max);

        //Json Schema expression:: {"maxLength":20}
        exp.addProperty("maxLength", max);
        assertEquals(exp, len.toJSONSchema(manager));
    }

    //max and min are null
    @Test
    public void toJsonSchemNullTest() {
        //testing:  Len_Assertion [min=null, max=null]
        Len_Assertion len = new Len_Assertion(null, null);
        assertEquals(exp, len.toJSONSchema(manager));
    }
}
