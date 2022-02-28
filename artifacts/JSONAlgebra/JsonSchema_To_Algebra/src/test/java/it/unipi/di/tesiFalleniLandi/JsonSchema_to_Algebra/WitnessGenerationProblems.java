package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Collecting interesting schemas.
 * @author Stefanie Scherzinger
 *
 */

public class WitnessGenerationProblems {

	@Test
	// TODO - fails with NullPointer Exception
	// Cannot invoke "com.google.gson.JsonElement.toString()"
	public void witnessForUnsatisfiableSchema() throws Exception {
		String schema = "{"
				+ "  \"allOf\": ["
				+ "    {"
				+ "      \"items\": ["
				+ "        {"
				+ "          \"type\": \"integer\""
				+ "        }"
				+ "      ]"
				+ "    },"
				+ "    {"
				+ "      \"not\": true"
				+ "    }"
				+ "  ],"
				+ "  \"additionalItems\": {"
				+ "    \"type\": \"boolean\""
				+ "  }"
				+ "}";
		
		String witness = JsonSchemaTestSuiteDraft6WitnessGen.generateWitness(schema);

		assertNotEquals(null,witness);
		assertEquals(JsonSchemaTestSuiteDraft6WitnessGen.noWitness,witness);
	}
	
	@Test
	// Produce a witness that is neither "true" or "false".
	public void noTrueFalseWitness() throws Exception {
		String schema = "{"
				+ "  \"allOf\": ["
				+ "    {\r\n"
				+ "      \"additionalProperties\": {"
				+ "        \"type\": \"boolean\""
				+ "      }"
				+ "    },"
				+ "    {"
				+ "      \"not\": {"
				+ "        \"const\": true"
				+ "      }"
				+ "    },"
				+ "    {"
				+ "      \"not\": {"
				+ "        \"const\": false"
				+ "      }"
				+ "    }"
				+ "  ]\r\n"
				+ "}";
		
		String witness = JsonSchemaTestSuiteDraft6WitnessGen.generateWitness(schema);
		
		assertNotEquals(null,witness);
		assertNotEquals(JsonSchemaTestSuiteDraft6WitnessGen.noWitness,witness);
		assertEquals(0, JsonSchemaTestSuiteDraft6WitnessGen.validateWitness(schema,witness));
	}

	@Test
	// TODO fails with NullPointerException
	// Cannot invoke "java.util.HashMap.values()" because "this.typedAssertionMap" is null
	public void testSchema11() throws Exception {
		String schema = "{"
				+ "  \"properties\": {"
				+ "    \"foo\": {},"
				+ "    \"bar\": {}"
				+ "  }"
				+ "}";
		
		String witness = JsonSchemaTestSuiteDraft6WitnessGen.generateWitness(schema);
		
		assertNotEquals(null,witness);
		assertNotEquals(JsonSchemaTestSuiteDraft6WitnessGen.noWitness,witness);
		assertEquals(0, JsonSchemaTestSuiteDraft6WitnessGen.validateWitness(schema,witness));
	}
	
	@Test
	// TODO - validation fails because of integer/float encoding: {"bar":0.0,"foo":""}
	public void generateFooBarObject() throws Exception {
		String schema = "{"
				+ "  \"allOf\": ["
				+ "    {"
				+ "      \"properties\": {"
				+ "        \"bar\": {"
				+ "          \"type\": \"integer\""
				+ "        }"
				+ "      },"
				+ "      \"required\": ["
				+ "        \"bar\""
				+ "      ]"
				+ "    },"
				+ "    {"
				+ "      \"properties\": {"
				+ "        \"foo\": {"
				+ "          \"type\": \"string\""
				+ "        }"
				+ "      },"
				+ "      \"required\": ["
				+ "        \"foo\""
				+ "      ]"
				+ "    },"
				+ "    {"
				+ "      \"type\": \"object\""
				+ "    }"
				+ "  ]"
				+ "}";
		
		String witness = JsonSchemaTestSuiteDraft6WitnessGen.generateWitness(schema);
		
		assertNotEquals(null,witness);
		assertNotEquals(JsonSchemaTestSuiteDraft6WitnessGen.noWitness,witness);
		assertEquals(0, JsonSchemaTestSuiteDraft6WitnessGen.validateWitness(schema,witness));
	}
	
	@Test
	// TODO - NullPointerException
	public void testSchema16() throws Exception {
		String schema = "{"
				+ "  \"allOf\": ["
				+ "    true,"
				+ "    true"
				+ "  ]"
				+ "}";
		
		String witness = JsonSchemaTestSuiteDraft6WitnessGen.generateWitness(schema);
		
		assertNotEquals(null,witness);
		assertNotEquals(JsonSchemaTestSuiteDraft6WitnessGen.noWitness,witness);
		assertEquals(0, JsonSchemaTestSuiteDraft6WitnessGen.validateWitness(schema,witness));
	}
	
	@Test
	// TODO - NullPointerException
	public void testSchema18() throws Exception {
		String schema = "{"
				+ "  \"allOf\": ["
				+ "    false,"
				+ "    false"
				+ "  ]"
				+ "}";
		
		String witness = JsonSchemaTestSuiteDraft6WitnessGen.generateWitness(schema);
		
		assertEquals(JsonSchemaTestSuiteDraft6WitnessGen.noWitness,witness);
	}

	@Test
	public void testSchema19() throws Exception {
		String schema = "{"
				+ "  \"allOf\": ["
				+ "    {}"
				+ "  ]"
				+ "}";
		
		String witness = JsonSchemaTestSuiteDraft6WitnessGen.generateWitness(schema);
		
		assertNotEquals(null,witness);
		assertNotEquals(JsonSchemaTestSuiteDraft6WitnessGen.noWitness,witness);
		assertEquals(0, JsonSchemaTestSuiteDraft6WitnessGen.validateWitness(schema,witness));
	}
	
	@Test
	// This used to have termination problems.
	public void testSchema24WithExtras() throws Exception {
		String schema = "{"
				+ "  \"allOf\": ["
				+ "    {\r\n"
				+ "      \"type\": \"integer\""
				+ "    },"
				+ "    {\r\n"
				+ "      \"multipleOf\": 2"
				+ "    },"
				+ "    {\r\n"
				+ "      \"not\": {"
				+ "        \"const\": 0"
				+ "      }"
				+ "    }"
				+ "  ],"
				+ "  \"anyOf\": ["
				+ "    {"
				+ "      \"multipleOf\": 3"
				+ "    }"
				+ "  ],"
				+ "  \"oneOf\": ["
				+ "    {"
				+ "      \"multipleOf\": 5"
				+ "    }"
				+ "  ]"
				+ "}";
		
		String witness = JsonSchemaTestSuiteDraft6WitnessGen.generateWitness(schema);
		
		assertNotEquals(null,witness);
		assertNotEquals(JsonSchemaTestSuiteDraft6WitnessGen.noWitness,witness);
		assertEquals(0, JsonSchemaTestSuiteDraft6WitnessGen.validateWitness(schema,witness));
	}
	
	@Test
	// Schema 1 from our EDBT demo paper.
	public void requiredX() throws Exception {
		String schema = "{"
				+ "  \"type\": \"object\","
				+ "  \"properties\": {"
				+ "    \"x\": {"
				+ "      \"type\": \"integer\""
				+ "    }"
				+ "  },"
				+ "  \"required\": [" // x is required
				+ "    \"x\""
				+ "  ]"
				+ "}";
		
		String witness = JsonSchemaTestSuiteDraft6WitnessGen.generateWitness(schema);
		
		assertNotEquals(null,witness);
		assertNotEquals(JsonSchemaTestSuiteDraft6WitnessGen.noWitness,witness);
		assertEquals(0, JsonSchemaTestSuiteDraft6WitnessGen.validateWitness(schema,witness));
	}
	
	@Test
	// Schema 2 from our EDBT demo paper.
	public void notRequiredX() throws Exception {
		String schema = "{"
				+ "  \"type\": \"object\","
				+ "  \"properties\": {"
				+ "    \"x\": {"
				+ "      \"type\": \"integer\""
				+ "    }"
				+ "  },"
				+ " \"not\": { \"required\": [" // not-required
				+ "    \"x\""
				+ "  ] }"
				+ "}";
		
		String witness = JsonSchemaTestSuiteDraft6WitnessGen.generateWitness(schema);
		
		assertNotEquals(null,witness);
		assertNotEquals(JsonSchemaTestSuiteDraft6WitnessGen.noWitness,witness);
		assertEquals(0, JsonSchemaTestSuiteDraft6WitnessGen.validateWitness(schema,witness));
	}
	
	@Test
	// Schema from our demo paper, produce another witness.
	public void notRequiredXAnother() throws Exception {
		String schema = "{"
				+ "  \"allOf\": ["
				+ "    {"
				+ "      \"type\": \"object\","
				+ "      \"properties\": {"
				+ "        \"x\": {"
				+ "          \"type\": \"integer\""
				+ "        }"
				+ "      },"
				+ "      \"not\": {"
				+ "        \"required\": ["
				+ "          \"x\""
				+ "        ]"
				+ "      }"
				+ "    },"
				+ "    {"
				+ "      \"not\": {"
				+ "        \"const\": {}"
				+ "      }"
				+ "    }"
				+ "  ]"
				+ "}";
		

		String witness = JsonSchemaTestSuiteDraft6WitnessGen.generateWitness(schema);
		
		assertNotEquals(null,witness);
		assertNotEquals(JsonSchemaTestSuiteDraft6WitnessGen.noWitness,witness);
		assertEquals(0, JsonSchemaTestSuiteDraft6WitnessGen.validateWitness(schema,witness));
	}
	
	@Test
	public void invalidSchema() throws Exception {
		String schema = "{"
				+ "  \"type\": \"object\","
				+ "  \"properties\": {"
				+ "    \"x\": {\r\n"
				+ "      \"type\": \"integer\""
				+ "    }"
				+ "  },"
				+ "  \"not\": {"
				+ "    \"required\": ["
				+ "      \"x\""
				+ "    ]"
				+ "  },"
				+ "  \"not\": {"
				+ "    \"const\": {}"
				+ "  }"
				+ "}";
		
		String witness = JsonSchemaTestSuiteDraft6WitnessGen.generateWitness(schema);
		
		assertNotEquals(null,witness);
		assertNotEquals(JsonSchemaTestSuiteDraft6WitnessGen.noWitness,witness);
		assertEquals(0, JsonSchemaTestSuiteDraft6WitnessGen.validateWitness(schema,witness));
	}
	
	@Test
	// Schema 2 but not schema 1 from the EDBT demo paper.
	public void schema2ButNot1() throws Exception {
		String schema = "{"
				+ "  \"allOf\": ["
				+ "    {"
				+ "      \"not\": {"              // not
				+ "        \"type\": \"object\"," // schema 1
				+ "        \"properties\": {"
				+ "          \"x\": {"
				+ "            \"type\": \"integer\""
				+ "          }\r\n"
				+ "        },\r\n"
				+ "        \"required\": ["
				+ "          \"x\""
				+ "        ]"
				+ "      }"
				+ "    },"
				+ "    {"
				+ "      \"type\": \"object\"," // schema 2
				+ "      \"properties\": {"
				+ "        \"x\": {"
				+ "          \"type\": \"integer\""
				+ "        }"
				+ "      },"
				+ "      \"not\": {"
				+ "        \"required\": ["
				+ "          \"x\""
				+ "        ]"
				+ "      }"
				+ "    }"
				+ "  ]"
				+ "}";
				
		String witness = JsonSchemaTestSuiteDraft6WitnessGen.generateWitness(schema);
		
		assertNotEquals(null,witness);
		assertNotEquals(JsonSchemaTestSuiteDraft6WitnessGen.noWitness,witness);
		assertEquals(0, JsonSchemaTestSuiteDraft6WitnessGen.validateWitness(schema,witness));
	}
	
	@Test
	// Schema 1 but not schema 2 from the EDBT demo paper.
	public void schema1ButNot2() throws Exception {
		String schema = "{"
				+ "  \"allOf\": ["
				+ "    {"                            // schema 1
				+ "      \"type\": \"object\","
				+ "      \"properties\": {"
				+ "        \"x\": {"
				+ "          \"type\": \"integer\""
				+ "        }"
				+ "      },"
				+ "      \"required\": ["
				+ "        \"x\""
				+ "      ]"
				+ "    },"
				+ "    {"
				+ "      \"not\": {"                  // not
				+ "        \"type\": \"object\","    // schema 2
				+ "        \"properties\": {"
				+ "          \"x\": {"
				+ "            \"type\": \"integer\""
				+ "          }"
				+ "        },"
				+ "        \"not\": {"
				+ "          \"required\": ["
				+ "            \"x\""
				+ "          ]"
				+ "        }"
				+ "      }"
				+ "    }"
				+ "  ]"
				+ "}";
		
		String witness = JsonSchemaTestSuiteDraft6WitnessGen.generateWitness(schema);
		
		assertNotEquals(null,witness);
		assertNotEquals(JsonSchemaTestSuiteDraft6WitnessGen.noWitness,witness);
		assertEquals(0, JsonSchemaTestSuiteDraft6WitnessGen.validateWitness(schema,witness));
	}
	
	@Test
	public void testDBLPSchema() throws Exception {
		String schema = "{\r\n"
				+ "  \"allOf\": [\r\n"
				+ "    {\r\n"
				+ "      \"type\": \"object\",\r\n"
				+ "      \"properties\": {\r\n"
				+ "        \"month\": {\r\n"
				+ "          \"type\": [\r\n"
				+ "            \"string\",\r\n"
				+ "            \"integer\"\r\n"
				+ "          ]\r\n"
				+ "        },\r\n"
				+ "        \"year\": {\r\n"
				+ "          \"type\": [\r\n"
				+ "            \"string\",\r\n"
				+ "            \"integer\"\r\n"
				+ "          ]\r\n"
				+ "        },\r\n"
				+ "        \"_id\": {\r\n"
				+ "          \"type\": \"object\"\r\n"
				+ "        },\r\n"
				+ "        \"title\": {\r\n"
				+ "          \"type\": \"string\"\r\n"
				+ "        },\r\n"
				+ "        \"authors\": {\r\n"
				+ "          \"type\": \"array\",\r\n"
				+ "          \"items\": {\r\n"
				+ "            \"anyOf\": [\r\n"
				+ "              {\r\n"
				+ "                \"type\": \"object\",\r\n"
				+ "                \"properties\": {\r\n"
				+ "                  \"firstname\": {\r\n"
				+ "                    \"type\": \"string\"\r\n"
				+ "                  },\r\n"
				+ "                  \"lastname\": {\r\n"
				+ "                    \"type\": \"string\"\r\n"
				+ "                  }\r\n"
				+ "                },\r\n"
				+ "                \"required\": [\r\n"
				+ "                  \"firstname\",\r\n"
				+ "                  \"lastname\"\r\n"
				+ "                ]\r\n"
				+ "              },\r\n"
				+ "              {\r\n"
				+ "                \"type\": \"string\"\r\n"
				+ "              }\r\n"
				+ "            ]\r\n"
				+ "          }\r\n"
				+ "        }\r\n"
				+ "      },\r\n"
				+ "      \"required\": [\r\n"
				+ "        \"year\",\r\n"
				+ "        \"_id\",\r\n"
				+ "        \"title\",\r\n"
				+ "        \"authors\"\r\n"
				+ "      ]\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "      \"not\": {\r\n"
				+ "        \"type\": \"object\",\r\n"
				+ "        \"properties\": {\r\n"
				+ "          \"month\": {\r\n"
				+ "            \"type\": [\r\n"
				+ "              \"string\",\r\n"
				+ "              \"integer\"\r\n"
				+ "            ]\r\n"
				+ "          },\r\n"
				+ "          \"year\": {\r\n"
				+ "            \"type\": [\r\n"
				+ "              \"string\",\r\n"
				+ "              \"integer\"\r\n"
				+ "            ]\r\n"
				+ "          },\r\n"
				+ "          \"_id\": {\r\n"
				+ "            \"type\": \"object\"\r\n"
				+ "          },\r\n"
				+ "          \"title\": {\r\n"
				+ "            \"type\": \"string\"\r\n"
				+ "          },\r\n"
				+ "          \"authors\": {\r\n"
				+ "            \"type\": \"array\",\r\n"
				+ "            \"items\": {\r\n"
				+ "              \"anyOf\": [\r\n"
				+ "                {\r\n"
				+ "                  \"type\": \"object\",\r\n"
				+ "                  \"properties\": {\r\n"
				+ "                    \"firstname\": {\r\n"
				+ "                      \"type\": \"string\"\r\n"
				+ "                    },\r\n"
				+ "                    \"lastname\": {\r\n"
				+ "                      \"type\": \"string\"\r\n"
				+ "                    }\r\n"
				+ "                  },\r\n"
				+ "                  \"required\": [\r\n"
				+ "                    \"firstname\",\r\n"
				+ "                    \"lastname\"\r\n"
				+ "                  ]\r\n"
				+ "                },\r\n"
				+ "                {\r\n"
				+ "                  \"type\": \"string\"\r\n"
				+ "                }\r\n"
				+ "              ]\r\n"
				+ "            }\r\n"
				+ "          }\r\n"
				+ "        },\r\n"
				+ "        \"required\": [\r\n"
				+ "          \"year\",\r\n"
				+ "          \"_id\",\r\n"
				+ "          \"title\",\r\n"
				+ "          \"authors\"\r\n"
				+ "        ]\r\n"
				+ "      }\r\n"
				+ "    }\r\n"
				+ "  ]\r\n"
				+ "}";
		
		String witness = JsonSchemaTestSuiteDraft6WitnessGen.generateWitness(schema);
		
		// Schema is not satisfiable.
		assertEquals(JsonSchemaTestSuiteDraft6WitnessGen.noWitness, witness);
	}
}	
