package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.toJsonSchemaTest;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Mof_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Luca Escher <escher_luca@web.de>
 */

public class Mof_AssertionTest {

    private final Number mof = 2;
    private final WitnessVarManager manager = new WitnessVarManager();

    @Test
    public void toJsonSchemaTest() {

        //testing:  Mof_Assertion [2]
        Mof_Assertion assertion = new Mof_Assertion(mof);
        JsonObject expected = new JsonObject();

        //Json Schema expression: {"multipleOf":2}
        expected.addProperty("multipleOf", mof);

        assertEquals(expected, assertion.toJSONSchema(manager));
    }
}
