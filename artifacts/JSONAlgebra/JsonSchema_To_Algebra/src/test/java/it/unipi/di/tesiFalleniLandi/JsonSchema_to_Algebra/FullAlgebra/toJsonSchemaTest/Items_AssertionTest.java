package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.toJsonSchemaTest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Boolean_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Const_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Items_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Luca Escher <escher_luca@web.de>
 */

public class Items_AssertionTest {

    private final WitnessVarManager manager = new WitnessVarManager();

    @Test
    public void AdditionalItemsNull() {
        JsonObject object = new JsonObject();
        object.addProperty("type", "integer");

        Boolean_Assertion boolean_assertion = new Boolean_Assertion(false);
        Const_Assertion const_assertion = new Const_Assertion(object);

        //testing:
        // Items_Assertion [itemsArray=[Boolean_Assertion [value=false], Const_Assertion [{"type":"integer"}]],
        // additionalItems=null]
        Items_Assertion items_assertion = new Items_Assertion();
        items_assertion.add(boolean_assertion);
        items_assertion.add(const_assertion);

        //Json Schema expression:   {"items":[false,{"const":{"type":"integer"}}]}
        JsonArray array = new JsonArray();
        JsonObject inner = new JsonObject();
        JsonObject expected = new JsonObject();

        inner.add("const", object);

        array.add(false);
        array.add(inner);

        expected.add("items", array);

        assertEquals(expected, items_assertion.toJSONSchema(manager));
    }

    @Test
    public void AdditionalItemsNotNull() {
        JsonObject object = new JsonObject();
        object.addProperty("type", "integer");

        JsonObject additionalObject = new JsonObject();
        additionalObject.addProperty("type", "boolean");

        Boolean_Assertion boolean_assertion = new Boolean_Assertion(false);
        Const_Assertion const_assertion = new Const_Assertion(object);
        Const_Assertion additionalItem = new Const_Assertion(additionalObject);

        //testing:
        // Items_Assertion [itemsArray=[Boolean_Assertion [value=false], Const_Assertion [{"type":"integer"}]],
        // additionalItems=Const_Assertion [{"type":"boolean"}]]
        Items_Assertion items_assertion = new Items_Assertion();
        items_assertion.add(boolean_assertion);
        items_assertion.add(const_assertion);
        items_assertion.setAdditionalItems(additionalItem);

        //Json Schema expression:
        // {"items":[false,{"const":{"type":"integer"}}],"additionalItems":{"const":{"type":"boolean"}}}
        JsonArray array = new JsonArray();
        JsonObject inner = new JsonObject();
        JsonObject expected = new JsonObject();
        JsonObject addInner = new JsonObject();

        inner.add("const", object);

        array.add(false);
        array.add(inner);
        expected.add("items", array);

        addInner.add("const", additionalObject);
        expected.add("additionalItems", addInner);

        assertEquals(expected, items_assertion.toJSONSchema(manager));
    }

    @Test
    public void ItemsNullAdditionalItemsNull() {

        //testing:  Items_Assertion [itemsArray=null, additionalItems=null]
        Items_Assertion items_assertion = new Items_Assertion();
        //Json Schema expression:   {}
        JsonObject expected = new JsonObject();
        assertEquals(expected, items_assertion.toJSONSchema(manager));
    }

    @Test
    public void ItemsNullAdditionalItemsNotNull() {

        JsonObject object = new JsonObject();
        object.addProperty("type", "boolean");

        Const_Assertion const_assertion = new Const_Assertion(object);

        //testing:  Items_Assertion [itemsArray=null, additionalItems=Const_Assertion [{"type":"boolean"}]]
        Items_Assertion items_assertion = new Items_Assertion();
        items_assertion.setAdditionalItems(const_assertion);

        //building: {"items":{"const":{"type":"boolean"}}}
        JsonObject constInner = new JsonObject();
        JsonObject expected = new JsonObject();

        constInner.add("const", object);
        expected.add("items", constInner);

        assertEquals(expected, items_assertion.toJSONSchema(manager));
    }
}
