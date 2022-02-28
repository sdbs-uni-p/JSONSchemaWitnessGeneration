package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.toJsonSchemaTest;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Pro_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Luca Escher <escher_luca@web.de>
 */

public class Pro_AssertionTest {

    private final JsonObject exp = new JsonObject();
    private final WitnessVarManager manager = new WitnessVarManager();

    private final long min = 1;
    private final long max = 4;

    @Test
    public void toJsonSchemaTest() {

        //testing:  Pro_Assertion [minProperties=1, maxProperties=4]
        Pro_Assertion assertion = new Pro_Assertion(min, max);

        //Json Schema expression: {"minProperties":1,"maxProperties":4}
        exp.addProperty("minProperties", min);
        exp.addProperty("maxProperties", max);

        assertEquals(exp, assertion.toJSONSchema(manager));
    }

    //only min present
    @Test
    public void toJsonSchemOnlyMinTest() {
        //testing:  Pro_Assertion [minProperties=1, maxProperties=null]
        Pro_Assertion assertion = new Pro_Assertion(min, null);
        //Json Schema expression: {"minProperties":1}
        exp.addProperty("minProperties", min);

        assertEquals(exp, assertion.toJSONSchema(manager));
    }

    //only max present
    @Test
    public void toJsonSchemOnlyMaxTest() {
        //testing:  Pro_Assertion [minProperties=null, maxProperties=4]
        Pro_Assertion assertion = new Pro_Assertion(null, max);
        //Json Schema expression: {"maxProperties":4}
        exp.addProperty("maxProperties", max);

        assertEquals(exp, assertion.toJSONSchema(manager));
    }

    //max and min are null
    @Test
    public void toJsonSchemNullTest() {
        //testing:  Pro_Assertion [minProperties=null, maxProperties=null]
        Pro_Assertion assertion = new Pro_Assertion(null, null);
        assertEquals(exp, assertion.toJSONSchema(manager));
    }
}

