package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.toJsonSchemaTest;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.NotMof_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Luca Escher <escher_luca@web.de>
 */

public class NotMof_AssertionTest {

    private final Number notMof = 2;
    private final WitnessVarManager manager = new WitnessVarManager();

    @Test
    public void toJsonSchemaTest() {

        JsonObject inner = new JsonObject();
        JsonObject expected = new JsonObject();

        //testing:  NotMof_Assertion [2]
        NotMof_Assertion assertion = new NotMof_Assertion(notMof);

        //Json Schema expression: {"not":{"multipleOf":2}}
        inner.addProperty("multipleOf", notMof);
        expected.add("not", inner);

        assertEquals(expected, assertion.toJSONSchema(manager));
    }
}
