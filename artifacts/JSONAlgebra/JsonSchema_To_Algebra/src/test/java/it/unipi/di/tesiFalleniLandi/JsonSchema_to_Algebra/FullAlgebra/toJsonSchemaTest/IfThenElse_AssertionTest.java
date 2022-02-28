package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.toJsonSchemaTest;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Boolean_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.IfThenElse_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Type_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.junit.Test;

import static it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings.TYPE_INTEGER;
import static org.junit.Assert.assertEquals;

/**
 * @author Luca Escher <escher_luca@web.de>
 */

public class IfThenElse_AssertionTest {

    private final WitnessVarManager manager = new WitnessVarManager();

    private final Type_Assertion ifAssertion = new Type_Assertion(TYPE_INTEGER);
    private final Boolean_Assertion thenAssertion = new Boolean_Assertion(true);
    private final Boolean_Assertion elseAssertion = new Boolean_Assertion(false);

    private final JsonObject ifObject = new JsonObject();
    private final JsonObject integer = new JsonObject();

    @Test
    public void toJsonSchemaTest() {

        //testing:
        //IfThenElse_Assertion [ifStatement=Type_Assertion [[int]], thenStatement=Boolean_Assertion [value=true],
        // elseStatement=Boolean_Assertion [value=false]]
        IfThenElse_Assertion assertion = new IfThenElse_Assertion(ifAssertion, thenAssertion, elseAssertion);

        //Json Schema expression:: {"if":{"type":"integer"},"then":true,"else":false}
        integer.addProperty("type", "integer");

        ifObject.add("if", integer);
        ifObject.addProperty("then", true);
        ifObject.addProperty("else", false);

        assertEquals(ifObject, assertion.toJSONSchema(manager));
    }

    @Test
    public void toJsonSchemaIfIsNullTest() {

        //testing:
        //IfThenElse_Assertion [ifStatement=null, thenStatement=Boolean_Assertion [value=true],
        // elseStatement=Boolean_Assertion [value=false]]
        IfThenElse_Assertion assertion = new IfThenElse_Assertion(null, thenAssertion, elseAssertion);

        //Json Schema expression: {"then":true,"else":false}
        ifObject.addProperty("then", true);
        ifObject.addProperty("else", false);

        assertEquals(ifObject, assertion.toJSONSchema(manager));
    }

    @Test
    public void toJsonSchemaThenIsNullTest() {

        //testing:
        //IfThenElse_Assertion [ifStatement=Type_Assertion [[int]], thenStatement=null,
        // elseStatement=Boolean_Assertion [value=false]]
        IfThenElse_Assertion assertion = new IfThenElse_Assertion(ifAssertion, null, elseAssertion);

        //Json Schema expression: {"if":{"type":"integer"},"else":false}
        integer.addProperty("type", "integer");

        ifObject.add("if", integer);
        ifObject.addProperty("else", false);

        assertEquals(ifObject, assertion.toJSONSchema(manager));
    }

    @Test
    public void toJsonSchemaElseIsNullTest() {

        //testing:
        //IfThenElse_Assertion [ifStatement=Type_Assertion [[int]], thenStatement=Boolean_Assertion [value=true],
        // elseStatement=null]
        IfThenElse_Assertion assertion = new IfThenElse_Assertion(ifAssertion, thenAssertion, null);

        //Json Schema expression: {"if":{"type":"integer"},"then":true}
        integer.addProperty("type", "integer");

        ifObject.add("if", integer);
        ifObject.addProperty("then", true);

        assertEquals(ifObject, assertion.toJSONSchema(manager));
    }
}
