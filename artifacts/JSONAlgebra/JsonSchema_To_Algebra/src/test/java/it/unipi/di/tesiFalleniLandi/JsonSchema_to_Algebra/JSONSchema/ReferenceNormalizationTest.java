package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

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
}
