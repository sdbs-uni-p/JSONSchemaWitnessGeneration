package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.toJsonSchemaTest;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.XBet_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Luca Escher <escher_luca@web.de>
 */

public class XBet_AssertionTest {

    private final WitnessVarManager manager = new WitnessVarManager();
    private final JsonObject exp = new JsonObject();

    //Why is number used here and in len = long ? bet has  number too
    private final Number min = 2;
    private final Number max = 20;

    @Test
    public void toJsonSchemaTest() {

        //testing:  XBet_Assertion [min=2, max=20]
        XBet_Assertion xBet = new XBet_Assertion(min, max);

        //Json Schema expression: {"exclusiveMinimum":2,"exclusiveMaximum":20}
        exp.addProperty("exclusiveMinimum", min);
        exp.addProperty("exclusiveMaximum", max);

        assertEquals(exp, xBet.toJSONSchema(manager));
    }

    //only min present
    @Test
    public void toJsonSchemOnlyMinTest() {
        //testing:  XBet_Assertion [min=2, max=null]
        XBet_Assertion xBet = new XBet_Assertion(min, null);
        //Json Schema expression: {"exclusiveMinimum":2}
        exp.addProperty("exclusiveMinimum", min);

        assertEquals(exp, xBet.toJSONSchema(manager));
    }

    //only max present
    @Test
    public void toJsonSchemOnlyMaxTest() {
        //testing:  XBet_Assertion [min=null, max=20]
        XBet_Assertion xBet = new XBet_Assertion(null, max);
        //Json Schema expression: {"exclusiveMaximum":20}
        exp.addProperty("exclusiveMaximum", max);

        assertEquals(exp, xBet.toJSONSchema(manager));
    }

    //max and min are null
    @Test
    public void toJsonSchemNullTest() {
        //testing:  XBet_Assertion [min=null, max=null]
        XBet_Assertion xBet = new XBet_Assertion(null, null);
        assertEquals(exp, xBet.toJSONSchema(manager));
    }
}


