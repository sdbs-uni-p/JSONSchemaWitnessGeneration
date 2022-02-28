package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.Utils;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Utils_JSONSchema;

/**
 * Testing not elimination for schemas from the JSON Schema Test Suite, Draft 2019-09,
 * commit hash 8daea3f.
 * https://github.com/json-schema-org/JSON-Schema-Test-Suite/tree/master/tests/draft2019-09
 */
public class JsonSchemaTestSuite201909NotElim {

	/**
	 * This is just a "smoke test". It illustrates how these tests work.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void smokeTest() {
		String schema = "{\"pattern\": \"^a*$\"}";

		JsonElement jsonElement = new JsonParser().parse(schema);
		JSONSchema root = new JSONSchema(jsonElement);

		Assertion grammar = Utils_JSONSchema.normalize(root).toGrammar();
		String fstr = Utils.beauty(grammar.toGrammarString());		
		Utils.beauty(Utils_FullAlgebra.parseString(fstr).notElimination().toGrammarString());

		assertTrue(true); // must reach this point successfully
	}


	@SuppressWarnings("deprecation")
	@Test
	public void boolean_schema36() {
		String schema = "true";

		JsonElement jsonElement = new JsonParser().parse(schema);
		JSONSchema root = new JSONSchema(jsonElement);

		Assertion grammar = Utils_JSONSchema.normalize(root).toGrammar();
		String fstr = Utils.beauty(grammar.toGrammarString());		
		Utils.beauty(Utils_FullAlgebra.parseString(fstr).notElimination().toGrammarString());

		assertTrue(true);
	}

	
	@SuppressWarnings("deprecation")
	@Test
	public void boolean_schema37() {
		String schema = "false";

		JsonElement jsonElement = new JsonParser().parse(schema);
		JSONSchema root = new JSONSchema(jsonElement);

		Assertion grammar = Utils_JSONSchema.normalize(root).toGrammar();
		String fstr = Utils.beauty(grammar.toGrammarString());		
		Utils.beauty(Utils_FullAlgebra.parseString(fstr).notElimination().toGrammarString());

		assertTrue(true); // must reach this point successfully
	}	

	@Test
	@SuppressWarnings("deprecation")
	public void dependentRequired67() {
		String schema = "{"
				+ "    \"dependentRequired\": {"
				+ "      \"bar\": []"
				+ "    }"
				+ " }";		
		JsonElement jsonElement = new JsonParser().parse(schema);
		JSONSchema root = new JSONSchema(jsonElement);

		Assertion grammar = Utils_JSONSchema.normalize(root).toGrammar();
		String fstr = Utils.beauty(grammar.toGrammarString());		
		Utils.beauty(Utils_FullAlgebra.parseString(fstr).notElimination().toGrammarString());

		assertTrue(true); // must reach this point successfully


		/**
		 * TOASK: dependentRequired: {<key, arrayOfString>} --> if key.isPresent() then
		 * 																	if document.containsAll(arrayOfString) then
		 * 																		return true;
		 *
		 * Cosa vogliamo faccia lo strumento? vedi Utils_JSONSchema.json:92
		 *
		 * arrayOfString must contains unique value. Come si comporta lo strumento?
		 */
	}


	@Test
	@Ignore
	@SuppressWarnings("deprecation")
	// TODO please get this to work.
	// very similar to ref190
	public void dependentRequired69() {
		String schema = "{"
				+ "  \"dependentRequired\": {"
				+ "    \"foo\\nbar\": ["
				+ "      \"foo\\rbar\""
				+ "    ],"
				+ "    \"foo\\\"bar\": ["
				+ "      \"foo'bar\""
				+ "    ]"
				+ "  }"
				+ "}";

		JsonElement jsonElement = new JsonParser().parse(schema);
		JSONSchema root = new JSONSchema(jsonElement);

		Assertion grammar = Utils_JSONSchema.normalize(root).toGrammar();
		String fstr = Utils.beauty(grammar.toGrammarString());		
		Utils.beauty(Utils_FullAlgebra.parseString(fstr).notElimination().toGrammarString());

		assertTrue(true);
	}
	

	@Test
	@Ignore
	@SuppressWarnings("deprecation")
	// TODO please get this to work.
	// very similar to ref190
	public void dependentRequired72() {
		String schema = "{"
				+ "  \"dependentSchemas\": {"
				+ "    \"foo\\tbar\": {"
				+ "      \"minProperties\": 4"
				+ "    },"
				+ "    \"foo'bar\": {"
				+ "      \"required\": ["
				+ "        \"foo\\\"bar\""
				+ "      ]"
				+ "    }"
				+ "  }"
				+ "}";

		JsonElement jsonElement = new JsonParser().parse(schema);
		JSONSchema root = new JSONSchema(jsonElement);

		Assertion grammar = Utils_JSONSchema.normalize(root).toGrammar();
		String fstr = Utils.beauty(grammar.toGrammarString());		
		Utils.beauty(Utils_FullAlgebra.parseString(fstr).notElimination().toGrammarString());

		assertTrue(true);
	}


	@Test
	@SuppressWarnings("deprecation")
	// this schema is always true: we changed the variable initialization in ifThenElse.java line 62
	public void ifThenElse107() {
		String schema = "{"
				+ "  \"if\": {"
				+ "    \"const\": 0"
				+ "  }"
				+ "}";

		JsonElement jsonElement = new JsonParser().parse(schema);
		JSONSchema root = new JSONSchema(jsonElement);

		Assertion grammar = Utils_JSONSchema.normalize(root).toGrammar();
		String fstr = Utils.beauty(grammar.toGrammarString());		
		Utils.beauty(Utils_FullAlgebra.parseString(fstr).notElimination().toGrammarString());

		assertTrue(true);
	}

	
	@Test
	@SuppressWarnings("deprecation")
	// Se mi ricordo male uniqueItems: false non ha nessuna valenza --> va eliminato. --> lo setto a true
	public void uniqueItems267() {
		String schema = "{"
				+ "  \"uniqueItems\": false"
				+ "}";

		JsonElement jsonElement = new JsonParser().parse(schema);
		JSONSchema root = new JSONSchema(jsonElement);

		Assertion grammar = Utils_JSONSchema.normalize(root).toGrammar();
		String fstr = Utils.beauty(grammar.toGrammarString());		
		Utils.beauty(Utils_FullAlgebra.parseString(fstr).notElimination().toGrammarString());

		assertTrue(true);
	}

	
	@Test
	@SuppressWarnings("deprecation")
	// vedi implementazione di UnknownElement.java
	public void refOfUnknownKeyword292() {
		String schema = "{"
				+ "  \"unknown-keyword\": {"
				+ "    \"type\": \"integer\""
				+ "  },"
				+ "  \"properties\": {"
				+ "    \"bar\": {"
				+ "      \"$ref\": \"#/unknown-keyword\""
				+ "    }"
				+ "  }"
				+ "}";

		JsonElement jsonElement = new JsonParser().parse(schema);
		JSONSchema root = new JSONSchema(jsonElement);

		Assertion grammar = Utils_JSONSchema.normalize(root).toGrammar();
		String fstr = Utils.beauty(grammar.toGrammarString());		
		Utils.beauty(Utils_FullAlgebra.parseString(fstr).notElimination().toGrammarString());

		assertTrue(true);

	}	

	/**
	 * Helper function.
	 * @param schema JSON Schema represented as Java string.
	 */
	protected static void json2Grammar2notElimination(String schema) {
		JsonElement jsonElement = new Gson().fromJson(schema, JsonElement.class);
		JSONSchema root = new JSONSchema(jsonElement);

		Assertion grammar = Utils_JSONSchema.normalize(root).toGrammar();
		String fstr = Utils.beauty(grammar.toGrammarString());		
		Utils.beauty(Utils_FullAlgebra.parseString(fstr).notElimination().toGrammarString());
	}

	@Test
	@Ignore
	// TODO - please get this to work
	public void enum74() {
		String schema = "{" +
				"    \"enum\": [" +
				"      6," +
				"      \"foo\"," +
				"      []," +
				"      true," +
				"      {" +
				"        \"foo\": 12" +
				"      }" +
				"    ]" +
				"  }";

		json2Grammar2notElimination(schema);
	}

	
	@Test
	public void ifThenElse108() {
		String schema = "{" +
				"    \"then\": {" +
				"      \"const\": 0" +
				"    }" +
				"  }";

		json2Grammar2notElimination(schema);
	}

	
	@Test
	public void ifThenElse109() {
		String schema = "{" +
				"    \"else\": {" +
				"      \"const\": 0" +
				"    }" +
				"  }";

		json2Grammar2notElimination(schema);
	}

	
	@Test
	public void ifThenElse111() {
		String schema = "{" +
				"    \"if\": {" +
				"      \"exclusiveMaximum\": 0" +
				"    }," +
				"    \"else\": {" +
				"      \"multipleOf\": 2" +
				"    }" +
				"  }";

		json2Grammar2notElimination(schema);
	}

	
	@Test
	public void ifThenElse113() {
		String schema = "{" +
				"    \"allOf\": [" +
				"      {" +
				"        \"if\": {" +
				"          \"exclusiveMaximum\": 0" +
				"        }" +
				"      }," +
				"      {" +
				"        \"then\": {" +
				"          \"minimum\": -10" +
				"        }" +
				"      }," +
				"      {" +
				"        \"else\": {" +
				"          \"multipleOf\": 2" +
				"        }" +
				"      }" +
				"    ]" +
				"  }";

		json2Grammar2notElimination(schema);
	}

	
	@Test	
	public void maxContains125() {
		String schema = " {" +
				"    \"maxContains\": 1" +
				"  }";

		json2Grammar2notElimination(schema);
	}

	@Test
	public void minContains134() {
		String schema = "{" +
				"    \"minContains\": 1" +
				"  }";

		json2Grammar2notElimination(schema);
	}

	@Test
	@Ignore
	// TODO: make sure output is correct
	// We resolved this problem by uncommenting JSONSchema.java:228 (we "rewrite" $recursiveRef as a normal ref)
	public void recursiveRef179() {
		String schema = "{" +
				"    \"properties\": {" +
				"      \"foo\": {" +
				"        \"$recursiveRef\": \"#\"" +
				"      }" +
				"    }," +
				"    \"additionalProperties\": false" +
				"  }";

		json2Grammar2notElimination(schema);
	}

	@Test
	@Ignore
	public void recursiveRef180() {
		String schema = "{" +
				"    \"$id\": \"http://localhost:4242/recursiveRef2/schema.json\"," +
				"    \"$defs\": {" +
				"      \"myobject\": {" +
				"        \"$id\": \"myobject.json\"," +
				"        \"$recursiveAnchor\": true," +
				"        \"anyOf\": [" +
				"          {" +
				"            \"type\": \"string\"" +
				"          }," +
				"          {" +
				"            \"type\": \"object\"," +
				"            \"additionalProperties\": {" +
				"              \"$recursiveRef\": \"#\"" +
				"            }" +
				"          }" +
				"        ]" +
				"      }" +
				"    }," +
				"    \"anyOf\": [" +
				"      {" +
				"        \"type\": \"integer\"" +
				"      }," +
				"      {" +
				"        \"$ref\": \"#/$defs/myobject\"" +
				"      }" +
				"    ]" +
				"  }";

		json2Grammar2notElimination(schema);
	}

	@Test
	@Ignore
	public void recursiveRef181() {
		String schema = "{" +
				"    \"$id\": \"http://localhost:4242/recursiveRef3/schema.json\"," +
				"    \"$recursiveAnchor\": true," +
				"    \"$defs\": {" +
				"      \"myobject\": {" +
				"        \"$id\": \"myobject.json\"," +
				"        \"$recursiveAnchor\": true," +
				"        \"anyOf\": [" +
				"          {" +
				"            \"type\": \"string\"" +
				"          }," +
				"          {" +
				"            \"type\": \"object\"," +
				"            \"additionalProperties\": {" +
				"              \"$recursiveRef\": \"#\"" +
				"            }" +
				"          }" +
				"        ]" +
				"      }" +
				"    }," +
				"    \"anyOf\": [" +
				"      {" +
				"        \"type\": \"integer\"" +
				"      }," +
				"      {" +
				"        \"$ref\": \"#/$defs/myobject\"" +
				"      }" +
				"    ]" +
				"  }";

		json2Grammar2notElimination(schema);
	}

	@Test
	@Ignore
	public void recursiveRef182() {
		String schema = "{" +
				"    \"$id\": \"http://localhost:4242/recursiveRef4/schema.json\"," +
				"    \"$recursiveAnchor\": false," +
				"    \"$defs\": {" +
				"      \"myobject\": {" +
				"        \"$id\": \"myobject.json\"," +
				"        \"$recursiveAnchor\": false," +
				"        \"anyOf\": [" +
				"          {" +
				"            \"type\": \"string\"" +
				"          }," +
				"          {" +
				"            \"type\": \"object\"," +
				"            \"additionalProperties\": {" +
				"              \"$recursiveRef\": \"#\"" +
				"            }" +
				"          }" +
				"        ]" +
				"      }" +
				"    }," +
				"    \"anyOf\": [" +
				"      {" +
				"        \"type\": \"integer\"" +
				"      }," +
				"      {" +
				"        \"$ref\": \"#/$defs/myobject\"" +
				"      }" +
				"    ]" +
				"  }";

		json2Grammar2notElimination(schema);
	}

	@Test
	@Ignore
	public void recursiveRef183() {
		String schema = "{" +
				"    \"$id\": \"http://localhost:4242/recursiveRef5/schema.json\"," +
				"    \"$defs\": {" +
				"      \"myobject\": {" +
				"        \"$id\": \"myobject.json\"," +
				"        \"$recursiveAnchor\": false," +
				"        \"anyOf\": [" +
				"          {" +
				"            \"type\": \"string\"" +
				"          }," +
				"          {" +
				"            \"type\": \"object\"," +
				"            \"additionalProperties\": {" +
				"              \"$recursiveRef\": \"#\"" +
				"            }" +
				"          }" +
				"        ]" +
				"      }" +
				"    }," +
				"    \"anyOf\": [" +
				"      {" +
				"        \"type\": \"integer\"" +
				"      }," +
				"      {" +
				"        \"$ref\": \"#/$defs/myobject\"" +
				"      }" +
				"    ]" +
				"  }";

		json2Grammar2notElimination(schema);
	}

	@Test
	@Ignore
	// TODO: see JSONSchema.java:255
	public void recursiveRef184() {
		String schema = " {" +
				"    \"$id\": \"http://localhost:4242/recursiveRef6/base.json\"," +
				"    \"$recursiveAnchor\": true," +
				"    \"anyOf\": [" +
				"      {" +
				"        \"type\": \"boolean\"" +
				"      }," +
				"      {" +
				"        \"type\": \"object\"," +
				"        \"additionalProperties\": {" +
				"          \"$id\": \"http://localhost:4242/recursiveRef6/inner.json\"," +
				"          \"$comment\": \"there is no $recursiveAnchor: true here, so we do NOT recurse to the base\"," +
				"          \"anyOf\": [" +
				"            {" +
				"              \"type\": \"integer\"" +
				"            }," +
				"            {" +
				"              \"type\": \"object\"," +
				"              \"additionalProperties\": {" +
				"                \"$recursiveRef\": \"#\"" +
				"              }" +
				"            }" +
				"          ]" +
				"        }" +
				"      }" +
				"    ]" +
				"  }";

		json2Grammar2notElimination(schema);
	}

	@Test
	@Ignore
	//TODO - this throws an exception, which was swallowed by the earlier code.
	public void recursiveRef185() {
		String schema = "{" +
				"    \"$id\": \"http://localhost:4242/recursiveRef7/base.json\"," +
				"    \"anyOf\": [" +
				"      {" +
				"        \"type\": \"boolean\"" +
				"      }," +
				"      {" +
				"        \"type\": \"object\"," +
				"        \"additionalProperties\": {" +
				"          \"$id\": \"http://localhost:4242/recursiveRef7/inner.json\"," +
				"          \"$recursiveAnchor\": true," +
				"          \"anyOf\": [" +
				"            {" +
				"              \"type\": \"integer\"" +
				"            }," +
				"            {" +
				"              \"type\": \"object\"," +
				"              \"additionalProperties\": {" +
				"                \"$recursiveRef\": \"#\"" +
				"              }" +
				"            }" +
				"          ]" +
				"        }" +
				"      }" +
				"    ]" +
				"  }";

		json2Grammar2notElimination(schema);
	}

	@Test
	@Ignore
	// TODO - please get this to work - low prio, is probably tricky
	// we need to rewrite, somehow, all the $ref such that we remove the part that contains the $id
	// recursiveRef8_main.json#/$defs/inner --> #/$defs/inner
	// we cannot use a static variable since we need the tool to be threadsafe (web service)
	public void recursiveRef186() {
		String schema = "{" +
				"    \"$id\": \"recursiveRef8_main.json\"," +
				"    \"$defs\": {" +
				"      \"inner\": {" +
				"        \"$id\": \"recursiveRef8_inner.json\"," +
				"        \"$recursiveAnchor\": true," +
				"        \"title\": \"inner\"," +
				"        \"additionalProperties\": {" +
				"          \"$recursiveRef\": \"#\"" +
				"        }" +
				"      }" +
				"    }," +
				"    \"if\": {" +
				"      \"propertyNames\": {" +
				"        \"pattern\": \"^[a-m]\"" +
				"      }" +
				"    }," +
				"    \"then\": {" +
				"      \"title\": \"any type of node\"," +
				"      \"$id\": \"recursiveRef8_anyLeafNode.json\"," +
				"      \"$recursiveAnchor\": true," +
				"      \"$ref\": \"recursiveRef8_main.json#/$defs/inner\"" +
				"    }," +
				"    \"else\": {" +
				"      \"title\": \"integer node\"," +
				"      \"$id\": \"recursiveRef8_integerNode.json\"," +
				"      \"$recursiveAnchor\": true," +
				"      \"type\": [" +
				"        \"object\"," +
				"        \"integer\"" +
				"      ]," +
				"      \"$ref\": \"recursiveRef8_main.json#/$defs/inner\"" +
				"    }" +
				"  }";

		json2Grammar2notElimination(schema);
	}

	@Test
	@Ignore
	// TODO - please get this to work
	// we need to write a function (in URI_JS.java) that translate the ~ (or %25) function into the correct character
	public void ref190() {
		String schema = "{" +
				"    \"$defs\": {" +
				"      \"tilde~field\": {" +
				"        \"type\": \"integer\"" +
				"      }," +
				"      \"slash/field\": {" +
				"        \"type\": \"integer\"" +
				"      }," +
				"      \"percent%field\": {" +
				"        \"type\": \"integer\"" +
				"      }" +
				"    }," +
				"    \"properties\": {" +
				"      \"tilde\": {" +
				"        \"$ref\": \"#/$defs/tilde~0field\"" +
				"      }," +
				"      \"slash\": {" +
				"        \"$ref\": \"#/$defs/slash~1field\"" +
				"      }," +
				"      \"percent\": {" +
				"        \"$ref\": \"#/$defs/percent%25field\"" +
				"      }" +
				"    }" +
				"  }";
		
		json2Grammar2notElimination(schema);
	}

	@Test
	@Ignore
	// TODO - please get this to work
	// same problem of ref186
	public void ref198() {
		String schema = "{" +
				"    \"$id\": \"http://localhost:1234/tree\"," +
				"    \"description\": \"tree of nodes\"," +
				"    \"type\": \"object\"," +
				"    \"properties\": {" +
				"      \"meta\": {" +
				"        \"type\": \"string\"" +
				"      }," +
				"      \"nodes\": {" +
				"        \"type\": \"array\"," +
				"        \"items\": {" +
				"          \"$ref\": \"node\"" + // #/$defs/node?
				"        }" +
				"      }" +
				"    }," +
				"    \"required\": [" +
				"      \"meta\"," +
				"      \"nodes\"" +
				"    ]," +
				"    \"$defs\": {" +
				"      \"node\": {" +
				"        \"$id\": \"http://localhost:1234/node\"," +
				"        \"description\": \"node\"," +
				"        \"type\": \"object\"," +
				"        \"properties\": {" +
				"          \"value\": {" +
				"            \"type\": \"number\"" +
				"          }," +
				"          \"subtree\": {" +
				"            \"$ref\": \"tree\"" +
				"          }" +
				"        }," +
				"        \"required\": [" +
				"          \"value\"" +
				"        ]" +
				"      }" +
				"    }" +
				"  }";

		json2Grammar2notElimination(schema);
	}

	@Test
	@Ignore
	// TODO - please get this to work
	// very similar to ref190
	public void ref199() {
		String schema = "{" +
				"    \"properties\": {" +
				"      \"foo\\\"bar\": {" +
				"        \"$ref\": \"#/$defs/foo%22bar\"" +
				"      }" +
				"    }," +
				"    \"$defs\": {" +
				"      \"foo\\\"bar\": {" +
				"        \"type\": \"number\"" +
				"      }" +
				"    }" +
				"  }";

		json2Grammar2notElimination(schema);
	}

	@Test
	@Ignore
	//TODO - please get this to work
	// very similar to ref190
	public void required212() {
		String schema = "{" +
				"    \"required\": [" +
				"      \"foo\\nbar\"," +
				"      \"foo\\\"bar\"," +
				"      \"foo\\\\bar\"," +
				"      \"foo\\rbar\"," +
				"      \"foo\\tbar\"," +
				"      \"foo\\fbar\"" +
				"    ]" +
				"  }";
		
		json2Grammar2notElimination(schema);
	}
}
