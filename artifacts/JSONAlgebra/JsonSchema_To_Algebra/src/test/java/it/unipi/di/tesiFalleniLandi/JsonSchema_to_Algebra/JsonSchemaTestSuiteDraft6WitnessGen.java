package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra.GenEnv;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Utils_JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessEnv;
import org.junit.Test;

import java.util.Set;

public class JsonSchemaTestSuiteDraft6WitnessGen {

	// default value returned when no witness is generated
	// essential when checking the validity of a witness of a JSON schema of type object (as noWitness is an object)
	protected final static String noWitness = "{\"Problem\":\"Empty witness\"}";


	/**
	 *
	 * @param schema
	 * @return String value which corresponds to the generated witness if it exists, otherwise returns null
	 * @throws Exception
	 */
	protected static String generateWitness(String schema) throws Exception {
		String witness = null;

		JsonElement jsonElement = new Gson().fromJson(schema, JsonElement.class);
		JSONSchema root = new JSONSchema(jsonElement);

		Assertion grammar = Utils_JSONSchema.normalize(root).toGrammar();

		WitnessEnv env = Utils_FullAlgebra.getWitnessAlgebra(grammar);
		env.buildOBDD_notElimination();


		env = (WitnessEnv) env.merge(null, null);
		env = env.groupize();
		env = env.DNF();
		env.varNormalization_separation(null, null);
		env = env.DNF();
		env = env.varNormalization_expansion(null);


		env = (WitnessEnv) env.merge(null, null);
		//env.toOrPattReq();


		env.objectPrepare();
		env.arrayPreparation();


		GenEnv genv = null;
		genv = new GenEnv(env);

		witness = genv.generate().toString();



		return witness;
	}


	/**
	 *
	 * @param stringSchema JSON schema
	 * @param witness the generated witness for the given schema
	 * @return number of errors found during validation (=0 if valid)
	 * @throws JsonProcessingException
	 */
	protected static int validateWitness(String stringSchema, String witness) throws JsonProcessingException {


		JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);
		ObjectMapper mapper = new ObjectMapper();

		JsonSchema schema = factory.getSchema(stringSchema);

		JsonNode node = mapper.readTree(witness);
		Set<ValidationMessage> errors = schema.validate(node);

		return errors.size();
	}


	@Test
	public void smokeTest() throws Exception {
		String schema = "{\"type\":\"string\",\"pattern\": \"^a+$\"}";

		String witness = generateWitness(schema);
		System.out.println(witness);

		assertNotEquals(null,witness);
		assertNotEquals(noWitness,witness);
		assertEquals(0,validateWitness(schema,witness));
	}


	@Test
	public void testAdditionalItems1() throws Exception {
		String schema = "{"
				+ "  \"items\": {},"
				+ "  \"additionalItems\": false"
				+ "}";

		String witness = generateWitness(schema);
		System.out.println(witness);

		assertNotEquals(null,witness);
		assertNotEquals(noWitness,witness);
		assertEquals(0,validateWitness(schema,witness));
	}

	@Test
	public void testAdditionalItems0() throws Exception {
		String schema = "{"
				+ "  \"items\": ["
				+ "    {}"
				+ "  ],"
				+ "  \"additionalItems\": {"
				+ "    \"type\": \"integer\""
				+ "  }"
				+ "}";

		String witness = generateWitness(schema);
		System.out.println(witness);

		assertNotEquals(null,witness);
		assertNotEquals(noWitness,witness);
		assertEquals(0,validateWitness(schema,witness));
	}
	

}
