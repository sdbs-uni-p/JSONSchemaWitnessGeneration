package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Testing not elimination for selected schemas from the JSON Schema Test Suite, Draft 6, commit hash 8daea3f.
 * https://github.com/json-schema-org/JSON-Schema-Test-Suite/tree/master/tests/draft6
 */
public class JsonSchemaTestSuiteDraft6NotElim {

	@Test
	@Ignore
	// TODO: Please fix.
	public void dependencies61() {
		String schema = "{"
				+ "  \"dependencies\": {"
				+ "    \"bar\": []"
				+ "  }"
				+ "}";
	
		JsonSchemaTestSuite201909NotElim.json2Grammar2notElimination(schema);
	}
	
	@Test
	@Ignore
	// TODO: Please fix.
	public void dependencies65() {
		String schema = "{"
				+ "  \"dependencies\": {"
				+ "    \"foo\\nbar\": ["
				+ "      \"foo\\rbar\""
				+ "    ],"
				+ "    \"foo\\tbar\": {"
				+ "      \"minProperties\": 4"
				+ "    },"
				+ "    \"foo'bar\": {"
				+ "      \"required\": ["
				+ "        \"foo\\\"bar\""
				+ "      ]"
				+ "    },"
				+ "    \"foo\\\"bar\": ["
				+ "      \"foo'bar\""
				+ "    ]"
				+ "  }"
				+ "}";
		
		JsonSchemaTestSuite201909NotElim.json2Grammar2notElimination(schema);
	}
	
	@Test
	@Ignore
	// TODO: Please fix
	public void bignum190() {
		String schema = "{ \"maximum\": 18446744073709551615 }";
		JsonSchemaTestSuite201909NotElim.json2Grammar2notElimination(schema);
	}
	
	@Test
	@Ignore
	// TODO: Please fix
	public void bignum191() {
		String schema = "{ \"exclusiveMaximum\": 9.7278379818798712E+26 }";
		JsonSchemaTestSuite201909NotElim.json2Grammar2notElimination(schema);
	}
	
	@Test
	@Ignore
	// TODO: Please fix
	public void bignum192() {
		String schema = "{ \"minimum\": -18446744073709551615 }";
		JsonSchemaTestSuite201909NotElim.json2Grammar2notElimination(schema);
	}
	
	@Test
	@Ignore
	// TODO: Please fix
	public void bignum193() {
		String schema = "{ \"exclusiveMinimum\": -9.727837981879871e+26 }";
		JsonSchemaTestSuite201909NotElim.json2Grammar2notElimination(schema);
	}
}
