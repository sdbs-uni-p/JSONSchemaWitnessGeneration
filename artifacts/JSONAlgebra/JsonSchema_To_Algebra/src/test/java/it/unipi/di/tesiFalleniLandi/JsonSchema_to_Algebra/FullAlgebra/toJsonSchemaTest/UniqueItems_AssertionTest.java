package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.toJsonSchemaTest;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.UniqueItems_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Luca Escher <escher_luca@web.de>
 */

public class UniqueItems_AssertionTest {

    private final WitnessVarManager manager = new WitnessVarManager();

    @Test
    public void uniqueItemsTrueToJsonSchemaTest() {

        //testing:  UniqueItems_Assertion{}
        UniqueItems_Assertion unique = new UniqueItems_Assertion();

        //Json Schema expression: {"uniqueItems":true}
        JsonObject exp = new JsonObject();
        exp.addProperty("uniqueItems", true);

        assertEquals(exp, unique.toJSONSchema(manager));
    }
    //what about uniqueItems == false ?
}
