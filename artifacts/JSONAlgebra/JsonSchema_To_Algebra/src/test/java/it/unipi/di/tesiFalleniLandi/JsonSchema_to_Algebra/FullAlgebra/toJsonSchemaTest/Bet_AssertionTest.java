package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.toJsonSchemaTest;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Bet_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Luca Escher <escher_luca@web.de>
 */

public class Bet_AssertionTest {

    private final WitnessVarManager manager = new WitnessVarManager();
    private final JsonObject exp = new JsonObject();

    //why number and in others long ?
    private final Number min = 1;
    private final Number max = 20;

    @Test
    public void toJsonSchemaTest() {

        //testing:  Bet_Assertion [1, 20]
        Bet_Assertion bet = new Bet_Assertion(min, max);

        //Json Schema expression: {"minimum":1,"maximum":20}
        exp.addProperty("minimum", min);
        exp.addProperty("maximum", max);

        assertEquals(exp, bet.toJSONSchema(manager));
    }

    //only min present
    @Test
    public void toJsonSchemOnlyMinTest() {

        //testing:  Bet_Assertion [1, null]
        Bet_Assertion bet = new Bet_Assertion(min, null);

        //Json Schema expression: {"minimum":1}
        exp.addProperty("minimum", min);
        assertEquals(exp, bet.toJSONSchema(manager));
    }

    //only max present
    @Test
    public void toJsonSchemOnlyMaxTest() {

        //testing:  Bet_Assertion [null, 20]
        Bet_Assertion bet = new Bet_Assertion(null, max);

        //Json Schema expression: {"maximum":20}
        exp.addProperty("maximum", max);
        assertEquals(exp, bet.toJSONSchema(manager));
    }

    //max and min are null
    @Test
    public void toJsonSchemNullTest() {
        //testing and expecting with Bet_Assertion [null, null] |  {}
        Bet_Assertion bet = new Bet_Assertion(null, null);
        assertEquals(exp, bet.toJSONSchema(manager));
    }
}
