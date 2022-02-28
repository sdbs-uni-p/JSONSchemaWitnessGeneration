package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.toJsonSchemaTest;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Pattern_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import static org.junit.Assert.assertEquals;

/**
 * @author Luca Escher <escher_luca@web.de>
 */

public class Pattern_AssertionTest {

    private final WitnessVarManager manager = new WitnessVarManager();

    @Test
    public void toJsonSchemaTest() throws REException {

        //testing:  Pattern_Assertion ["^$|(^(?:\S+\s+){0,99}\S+$)"]
        Pattern_Assertion pattern =
                new Pattern_Assertion(ComplexPattern.createFromRegexp("^$|(^(?:\\S+\\s+){0,99}\\S+$)"));

        //Json Schema expression:   {"pattern":"^$|(^(?:\\S+\\s+){0,99}\\S+$)"}
        JsonObject expected = new JsonObject();
        expected.addProperty("pattern", "^$|(^(?:\\S+\\s+){0,99}\\S+$)");

        assertEquals(expected, pattern.toJSONSchema(manager));
    }
}
