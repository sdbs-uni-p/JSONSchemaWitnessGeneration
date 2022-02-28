package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.toJsonSchemaTest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Enum_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Luca Escher <escher_luca@web.de>
 */

public class Enum_AssertionTest {

    private final JsonObject exp = new JsonObject();
    private final JsonObject input1 = new JsonObject();
    private final JsonArray arrayInput = new JsonArray();
    private final JsonArray array = new JsonArray();

    private final WitnessVarManager manager = new WitnessVarManager();

    @Test
    public void toJsonSchemaTestWith2Objects() {

        //testing:  Enum_Assertion [["foo",2]]
        array.add("foo");
        array.add(2);
        Enum_Assertion assertion = new Enum_Assertion(array);

        //Json Schema expression:: {"enum":["foo",2]}
        exp.add("enum", array);

        assertEquals(exp, assertion.toJSONSchema(manager));
    }

    @Test
    public void toJsonSchemaTestNull() {

        //testing:  Enum_Assertion [[]]
        Enum_Assertion assertion = new Enum_Assertion(array);

        //Json Schema expression:: {"enum":[]}
        exp.add("enum", array);
        assertEquals(exp, assertion.toJSONSchema(manager));
    }

    @Test
    public void toJsonSchemaTestElementIsJsonObject() {

        //testing:  Enum_Assertion [[{"type":"string"}]]
        input1.addProperty("type", "string");
        array.add(input1);
        Enum_Assertion assertion = new Enum_Assertion(array);

        //Json Schema expression: {"enum":[{"type":"string"}]}
        exp.add("enum", array);

        assertEquals(exp, assertion.toJSONSchema(manager));
    }

    @Test
    public void toJsonSchemaTestElementIsJsonArray() {

        //testing:  Enum_Assertion [[["foo","bar"]]]
        arrayInput.add("foo");
        arrayInput.add("bar");
        array.add(arrayInput);
        Enum_Assertion assertion = new Enum_Assertion(array);

        //Json Schema expression: {"enum":[["foo","bar"]]}
        exp.add("enum", array);

        assertEquals(exp, assertion.toJSONSchema(manager));
    }

    @Test
    public void toJsonSchemaTestElementIsString() {

        //testing:  Enum_Assertion [["input"]]
        array.add("input");
        Enum_Assertion assertion = new Enum_Assertion(array);

        //Json Schema expression: {"enum":["input"]}
        exp.add("enum", array);

        assertEquals(exp, assertion.toJSONSchema(manager));
    }

    @Test
    public void toJsonSchemaTestElementIsNumber() {

        //testing:  Enum_Assertion [[2]]
        array.add(2);
        Enum_Assertion assertion = new Enum_Assertion(array);

        //Json Schema expression: {"enum":[2]}
        exp.add("enum", array);

        assertEquals(exp, assertion.toJSONSchema(manager));
    }

    @Test
    public void toJsonSchemaTestElementIsBoolean() {

        //testing:  Enum_Assertion [[false]]
        array.add(false);
        Enum_Assertion assertion = new Enum_Assertion(array);

        //Json Schema expression: {"enum":[false]}
        exp.add("enum", array);

        assertEquals(exp, assertion.toJSONSchema(manager));
    }
}
