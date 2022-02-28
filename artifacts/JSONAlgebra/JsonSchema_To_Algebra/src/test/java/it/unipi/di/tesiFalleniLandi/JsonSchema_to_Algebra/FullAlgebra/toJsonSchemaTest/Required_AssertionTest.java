package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.toJsonSchemaTest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Required_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Luca Escher <escher_luca@web.de>
 */

public class Required_AssertionTest {

    private final WitnessVarManager manager = new WitnessVarManager();

    @Test
    public void toJsonSchemaTest() {

        JsonArray array = new JsonArray();
        JsonObject exp = new JsonObject();

        //testing:  Required_Assertion [[foo, bar]]
        Required_Assertion assertion = new Required_Assertion();

        String a = "foo";
        String b = "bar";

        assertion.add(a);
        assertion.add(b);

        //Json Schema expression: {"required":["foo","bar"]}
        array.add(a);
        array.add(b);
        exp.add("required", array);

        assertEquals(exp, assertion.toJSONSchema(manager));
    }
}
