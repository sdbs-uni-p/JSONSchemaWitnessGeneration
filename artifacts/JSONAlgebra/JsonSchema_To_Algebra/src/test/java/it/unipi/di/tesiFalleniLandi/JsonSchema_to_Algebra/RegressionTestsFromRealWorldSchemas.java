package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.Utils;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Utils_JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions.SyntaxErrorRuntimeException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions.UnsupportedURIRuntimeException;

/**
 * Bugs found when testing real-world JSON Schemas.
 * Regression tests.
 */
public class RegressionTestsFromRealWorldSchemas {


	/**
	 * Inspired by schema 90284 (February batch).
	 * Cannot handle external references.
	 */
	@Test(expected=UnsupportedURIRuntimeException.class)
	public void testExternalFileRef() {
		String schema = "{ \"$ref\": \"spatialReference_schema.json\" }";
		
		JsonElement jsonElement = new Gson().fromJson(schema, JsonElement.class);
		new JSONSchema(jsonElement);
	}
	

	/**
	 * Inspired by schema 44454.
	 * Problems with parsing unexpected string constant.
	 */
	@Test(expected=SyntaxErrorRuntimeException.class)
	public void testUnexpectedValue() {
		String schema = "{ \"properties\": {\"error\": \"unexpectedstring\"} }";

		JsonElement jsonElement = new Gson().fromJson(schema, JsonElement.class);
		new JSONSchema(jsonElement);
	}
	
	/**
	 * Inspired by schema 48425.
	 * patternProperties with many options. Does not terminate within
	 * reasonable time when directly compiled to Brics automaton,
	 * but will terminate since we lazily construct the Brics automaton.
	 */
	@Test
	public void testLongPatternProps() {
		String schema = "{"
				+ "    \"patternProperties\": {"
				+ "      \"Red|Blue|Yellow|Gold|Silver|Crystal|Ruby|Sapphire|Emerald|FireRed|LeafGreen|Diamond|Pearl|Platinum|HeartGold|SoulSilver|Black|White|Black 2|White 2|X|Y|Omega Ruby|Alpha Sapphire|Sun|Moon|Ultra Sun|Ultra Moon|Let's Go Pikachu|Let's Go Eevee\": true\r\n"
				+ "    },"
				+ "    \"additionalProperties\": false"
				+ "}";
		JsonElement jsonElement = new Gson().fromJson(schema, JsonElement.class);
		JSONSchema root = new JSONSchema(jsonElement);
		
		Assertion grammar = Utils_JSONSchema.normalize(root).toGrammar();
		String fstr = Utils.beauty(grammar.toGrammarString());		
		Utils.beauty(Utils_FullAlgebra.parseString(fstr).notElimination().toGrammarString());
	}
	
}
