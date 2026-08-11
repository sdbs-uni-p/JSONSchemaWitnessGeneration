package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions.UnsupportedURIRuntimeException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import static org.junit.Assert.assertEquals;

public class ReferenceNormalizationTest {

    @Test
    public void test() throws IOException, REException {
        JSONSchema root;
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        try (Reader reader = new FileReader("unit-test/ref_norm/input_1.json")) {
            JsonObject object = gson.fromJson(reader, JsonObject.class);
            root = new JSONSchema(object);
        }

        JsonElement output = Utils_JSONSchema.normalize(root).toJSON();

        JsonElement expected = Utils_JSONSchema.parse("unit-test/ref_norm/output_1.json").toJSON();

        assertEquals(output, expected);
    }

    /**
     * BUG: a $ref into a nested location of a "definitions" entry (e.g. a property called
     * "title") fails to resolve, even though the location clearly exists.
     * <p>
     * Root cause: {@link JSONSchema#collectDef()} deletes the "$defs" entry of every schema it
     * visits as a side effect of collecting it (see the {@code jsonSchema.remove("$defs")} call).
     * {@link Utils_JSONSchema#referenceNormalization} runs {@code collectDef()} once up front, so
     * by the time a ref that isn't an exact match for a collected definition falls back to
     * {@link JSONSchema#searchDef}, the "$defs"/"definitions" keyword it needs to walk through is
     * already gone - and {@link Defs#searchDef} unconditionally returns null on top of that.
     */
    @Test(expected = UnsupportedURIRuntimeException.class)
    public void test_refIntoNestedPropertyOfDefinition() throws IOException {
        JSONSchema root;
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        try (Reader reader = new FileReader("unit-test/ref_norm/input_2.json")) {
            JsonObject object = gson.fromJson(reader, JsonObject.class);
            root = new JSONSchema(object);
        }

        // Should resolve to the schema of "title", but currently throws instead.
        Utils_JSONSchema.normalize(root);
    }
}
