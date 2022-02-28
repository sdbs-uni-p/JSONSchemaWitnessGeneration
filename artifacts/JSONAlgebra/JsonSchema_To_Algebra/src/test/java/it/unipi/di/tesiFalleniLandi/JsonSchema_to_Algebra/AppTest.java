package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra;

import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Utils_JSONSchema;
import de.uni_passau.sds.patterns.REException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
    
    @Test
    @Ignore
    public void testPatternProperties() throws IOException, REException {
        JSONSchema root;
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        // TODO - the regexp in this grammar, "A|B|C" causes OOM and long runtime,
        // need to fix translation to Bricks, rather than "@A@|@B@|@C@", encode as "@(A|B|C)@".
        try (Reader reader = new FileReader("unit-test/json2grammar/input_1.json")) {
            JsonObject object = gson.fromJson(reader, JsonObject.class);
            root = new JSONSchema(object);
        }

        JSONSchema normalize = Utils_JSONSchema.normalize(root);
        System.out.println(normalize);
        
        // TODO - causes long runtime/OOM.
		normalize.toGrammar();     
    }
}
