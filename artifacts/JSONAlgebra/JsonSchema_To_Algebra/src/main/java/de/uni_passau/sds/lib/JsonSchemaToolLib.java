package de.uni_passau.sds.lib;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import de.uni_passau.sds.lib.exceptions.InvalidWitnessException;
import de.uni_passau.sds.lib.exceptions.WitnessGenerationException;
import de.uni_passau.sds.lib.schemaComparism.JsonSchemaComparison;
import de.uni_passau.sds.lib.schemaComparism.JsonSchemaRelationships;
import dto.LoadSchemaDTO;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.Utils;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra.GenEnv;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions.SyntaxErrorRuntimeException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Utils_JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessEnv;
import model.normalization.Normalizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

/**
 * Default implementation of JSON Schema library.
 * TODO parse once here and then never again to save resources
 * This class is packages scoped so it can only be instantiated using {@link JsonSchemaLibFactory}.
 *
 * @author Thomas Pilz
 * @author Luca Escher
 */
class JsonSchemaToolLib implements IJsonSchemaLib {

    private static final Logger logger = LogManager.getLogger(JsonSchemaToolLib.class);
    public static final String emptyWitness = "{\"Problem\":\"Empty witness\"}";

    private static final LoadSchemaDTO config = new LoadSchemaDTO.Builder()
            .allowDistributedSchemas(false)
            .fetchSchemasOnline(false)
            .build();

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> generateWitness(String schema) throws WitnessGenerationException, InvalidWitnessException, JsonSyntaxException {
        String witness;
        JSONSchema root;
        try {
            JsonElement jsonElement = new Gson().fromJson(schema, JsonElement.class);
            root = new JSONSchema(jsonElement);
        } catch (SyntaxErrorRuntimeException e) {
            throw new JsonSyntaxException(e);
        }

        Assertion grammar = Utils_JSONSchema.normalize(root).toGrammar();

        try {
            WitnessEnv env = Utils_FullAlgebra.getWitnessAlgebra(grammar);
            env.buildOBDD_notElimination();

            env = (WitnessEnv) env.merge(null, null);
            env = env.groupize();
            env = env.DNF();
            env.varNormalization_separation(null, null);
            env = env.DNF();
            env = env.varNormalization_expansion(null);

            env = (WitnessEnv) env.merge(null, null);
            env.toOrPattReq();


            env.objectPrepare();
            env.arrayPreparation();

            var genv = new GenEnv(env);
            witness = genv.generate().toString();
        } catch (Exception e) {
            throw new WitnessGenerationException("An unexpected error occurred. Witness could not be generated.", e);
        }

        // "Empty witness" is not a valid witness as it is a self-defined identifier for when no witness exists.
        // Therefore we cannot validate if this witness is correct
        if (!witness.equals(emptyWitness)) {
            Set<ValidationMessage> errors;
            try {
                errors = validateWitness(witness, schema);
            } catch (Exception e) {
                String msg = String.format("Witness is invalid for schema: \nSchema: %s\nWitness: %s", schema, witness);
                logger.error(msg);
                throw new InvalidWitnessException(msg, e);
            }
            if (errors.size() > 0) {
                String msg = String.format("Networknt-library has returned errors when validating witness: %s", errors);
                logger.error(msg);
                throw new InvalidWitnessException(String.format("Witness is invalid for schema: \nSchema: %s\nWitness: %s", schema, witness),
                        new RuntimeException(msg));
            }

            return Optional.of(witness);
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Optional<String> generateWitness(String schema, Collection<String> previousWitnesses) throws WitnessGenerationException, InvalidWitnessException, JsonSyntaxException {
        // No previous witnesses provided -> generate first witness
        if (previousWitnesses.isEmpty()) return generateWitness(schema);

        logger.info("Generating yet another unprecedented witness for schema: %s" + schema);

        Object schemaObj;
        List<Object> previousWitnessObjs = new LinkedList<>();
        try { // parse strings for schema and each previous witness
            JSONParser parser = new JSONParser();
            schemaObj = parser.parse(rewriteRefs(schema, 0, null));
            for (String witness : previousWitnesses) {
                previousWitnessObjs.add(parser.parse(witness));
            }
        } catch (ParseException e) {
            throw new JsonSyntaxException(e.getMessage());
        }
        // construct and fill array with the schema and for each previous witness its negation
        JSONArray schemaAndNotPrevWitnesses = new JSONArray();
        schemaAndNotPrevWitnesses.add(schemaObj);
        for (Object witnessObj : previousWitnessObjs) {
            JSONObject notObj = new JSONObject();
            JSONObject constObj = new JSONObject();
            constObj.put("const", witnessObj);
            notObj.put("not", constObj);
            schemaAndNotPrevWitnesses.add(notObj);
        }
        // construct schema object wrapping all assertions in the array
        JSONObject wrapperSchemaObj = new JSONObject();
        wrapperSchemaObj.put("allOf", schemaAndNotPrevWitnesses);
        // get string representation of wrapper schema
        String wrapperSchema = wrapperSchemaObj.toJSONString();

        // now generate witness from the "new" wrapper schema
        logger.info("Successfully generated wrapper: " + wrapperSchema);
        var witness = generateWitness(wrapperSchema);
        logger.info("Generated yet another witness from wrapper schema: " + witness.orElse(emptyWitness));
        return witness;
    }

    /**
     * Validate witness using <a href="https://github.com/networknt/json-schema-validator">Network-NT JSON Schema validator.</a>. }
     *
     * @param witness    JSON document which needs to be validated against a JSON Schema
     * @param jsonSchema JSON Schema to validate against
     * @return Set of error messages
     * @throws JsonProcessingException An exception occured while parsing the JSON document
     * @throws JsonMappingException    An exception occurred while parsing the JSON document
     * @throws InvalidWitnessException An invalid witness has been generated
     */
    private Set<ValidationMessage> validateWitness(String witness, String jsonSchema) throws JsonProcessingException, JsonMappingException, InvalidWitnessException {
        var version = SpecVersion.VersionFlag.V6;
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(version);
        logger.info(String.format("Validating witness %s", witness));

        JsonSchema schema = factory.getSchema(jsonSchema);

        var mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(witness);

        Set<ValidationMessage> errors;
        try {
            errors = schema.validate(node);
        } catch (Exception e) {
            String msg = String.format("Witness cannot be validated by networknt-library for schema: \nSchema: %s\nWitness: %s", jsonSchema, witness);
            // sometimes library method validate() throws NullPointerException! Bug in library?!
            logger.error(msg);
            // Throw an error and as cause provide exception thrown by library
            throw new InvalidWitnessException(msg, e);
        }
        return errors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonSchemaComparison compareSchemas(String leftSchema, String rightSchema) throws WitnessGenerationException, InvalidWitnessException, JsonSyntaxException {
        logger.info(String.format("Starting comparison of left schema: %s and right schema: %s", leftSchema, rightSchema));

        // build schemas
        String schemaOne = generateSchemaDifference(leftSchema, rightSchema);
        String schemaTwo = generateSchemaDifference(rightSchema, leftSchema);

        //get rid of the generated escape characters
        schemaOne = toString_withoutEscape(schemaOne);
        schemaTwo = toString_withoutEscape(schemaTwo);

        //check if it is necessary to normalize the schema, if so then normalize it
        if (containsIdRef(schemaOne)){
            schemaOne = normalizeSchema(schemaOne);
        }
        if (containsIdRef(schemaTwo)){
            schemaTwo = normalizeSchema(schemaTwo);
        }

        // Generate witness for 1st schema
        logger.info("Generated wrapper for comparison (Step 1): " + schemaOne);
        var witnessLeft = this.generateWitness(schemaOne);
        logger.info("Generated witness for comparison (Step 1): " + witnessLeft.orElse(emptyWitness));

        // Generate witness for 2nd schema
        logger.info("Generated wrapper for comparison (Step 2): " + schemaTwo);
        var witnessRight = this.generateWitness(schemaTwo);
        logger.info("Generated witness for comparison (Step 2): " + witnessRight.orElse(emptyWitness));

        // 4 Possible relationships: Decide which one it is
        // Both witnesses are empty ! Right now all witnesses where none were generated will be an emptyWitness.
        if (witnessRight.isEmpty() && witnessLeft.isEmpty()) {
            logger.info("The schemas are equivalent.");
            return new JsonSchemaComparison(emptyWitness, emptyWitness, JsonSchemaRelationships.EQUIVALENT);
        }

        // Left Witness is something and Right Witness is emptyWitness
        else if (witnessLeft.isPresent() && witnessRight.isEmpty()) {
            logger.info("The left schema is a generalization of the right.");
            return new JsonSchemaComparison(witnessLeft.get(), emptyWitness, JsonSchemaRelationships.LEFT_IS_GENERALIZATION);
        }

        // Right witness is something and left is empty
        else if (witnessLeft.isEmpty()) {
            logger.info("The right schema is a generalization of the left.");
            return new JsonSchemaComparison(emptyWitness, witnessRight.get(), JsonSchemaRelationships.RIGHT_IS_GENERALIZATION);
        } else {
            logger.info("The schemas have no containment/ relationship");
            return new JsonSchemaComparison(witnessLeft.get(), witnessRight.get(), JsonSchemaRelationships.NO_RELATIONSHIP);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public String generateSchemaDifference(String schema1, String schema2) throws JsonSyntaxException {
        JSONParser parser = new JSONParser();
        Object schemaObj1;
        Object schemaObj2;
        try {
            schemaObj1 = parser.parse(rewriteRefs(schema1, 0, null));
            schemaObj2 = parser.parse(rewriteRefs(schema2, 1, "not"));
        } catch (ParseException e) {
            throw new JsonSyntaxException(e.getMessage());
        }
        JSONObject schemaObjNot2 = new JSONObject();
        schemaObjNot2.put("not", schemaObj2);
        JSONArray schemaArr1AndNot2 = new JSONArray();
        schemaArr1AndNot2.add(schemaObj1);
        schemaArr1AndNot2.add(schemaObjNot2);
        JSONObject difference = new JSONObject();
        difference.put("allOf", schemaArr1AndNot2);
        return difference.toJSONString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String convJsonSchemaToFullAlg(String jsonSchema) throws JsonSyntaxException {
        logger.info(String.format("Converting JSON Schema %s to full algebra...", jsonSchema));
        Gson gson = new Gson();
        JsonObject object;
        object = gson.fromJson(jsonSchema, JsonObject.class);
        JSONSchema schema = new JSONSchema(object);
        var algString = Utils_JSONSchema.toGrammarString(Utils_JSONSchema.normalize(schema));
        logger.info(String.format("Successfully converted JSON Schema to full algebra: %s", algString));
        return algString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String convFullAlgToJsonSchema(String algString) throws JsonSyntaxException {
        logger.info(String.format("Converting Full Algebra %s to JSON Schema...", algString));
        JsonObject jsonObject;
        JsonPrimitive jsonPrimitive;
        String stringified = null;

        Assertion schema = Utils_FullAlgebra.parseString(algString);
        JsonElement jsonElement = schema.toJSONSchema(null);

        //Check if the JsonElement is a JsonPrimitive oder JsonObject in order to respond to both
        if (jsonElement.isJsonObject()) {
            jsonObject = (JsonObject) schema.toJSONSchema(null);
            logger.info("JSON Object: " + jsonObject);
            stringified = jsonObject.toString();
        } else if (jsonElement.isJsonPrimitive()) {
            jsonPrimitive = schema.toJSONSchema(null).getAsJsonPrimitive();
            logger.info("JSON Primitive: " + jsonPrimitive);
            stringified = jsonPrimitive.toString();
        }

        logger.info("JSON produced:" + stringified);
        return stringified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String doNotElimination(String algString) {
        logger.info(String.format("Performing not elimination on %s ...", algString));
        Assertion schema = Utils_FullAlgebra.parseString(algString);

        var notEliminated = Utils.beauty(schema.notElimination().toGrammarString());
        logger.info(String.format("Successfully performed not elimination. Result: %s", notEliminated));
        return notEliminated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getInternalSchemas(String leftSchema, String rightSchema) throws WitnessGenerationException, InvalidWitnessException, JsonSyntaxException {
        List<String> internSchemas = new ArrayList<>();

        // build and add schemas to list
        internSchemas.add(generateSchemaDifference(leftSchema, rightSchema));
        internSchemas.add(generateSchemaDifference(rightSchema, leftSchema));
        return internSchemas;
    }

    /**
     * Parses a json string, rewrites all $ref paths by calling the method {@code rewrite()} and unescapes slashes.
     *
     * @param json The json string to be rewritten.
     * @param position The position of the object in a new JSON array.
     * @param keyValue The key of the object in a new JSON object.
     * @return A string representation of the input json string with rewritten $ref paths and unescaped slashes.
     * @throws ParseException If json string could not be parsed.
     */
    private static String rewriteRefs(String json, int position, String keyValue) throws ParseException {
        Object obj = new JSONParser().parse(json);
        rewrite(obj, position, keyValue);
        return toString_withoutEscape(obj);
    }

    /**
     * Rewrites the $ref paths of a given object if it is a JSON object or array.
     *
     * @param object The object to rewrite its $ref paths.
     * @param position The position of the object in a new JSON array.
     * @param keyValue The key of the object in a new JSON object.
     */
    @SuppressWarnings("unchecked")
    private static void rewrite(Object object, int position, String keyValue) {
        if (object instanceof JSONArray) {
            JSONArray arr = (JSONArray) object;
            for (Object o : arr) {
                rewrite(o, position, keyValue);
            }
        } else if (object instanceof JSONObject) {
            JSONObject obj = (JSONObject) object;
            for (var key : obj.keySet()) {
                var value = obj.get(key);
                if (value instanceof JSONArray || value instanceof JSONObject) {
                    rewrite(value, position, keyValue);
                } else if (key.equals("$ref") && value instanceof String) {
                    String valueStr = (String) value;
                    String newPrefix = String.format("#/allOf/%d/%s", position, keyValue == null ? "" : keyValue + "/");
                    valueStr = valueStr.equals("#") ? newPrefix : valueStr.replace("#/", newPrefix);
                    obj.put(key, valueStr);
                }
            }
        }
    }

    /**
     * Creates a string representation of a given object. Unescapes slashes and returns the result.
     *
     * @param jObject The object to create its string representation.
     * @return The string representation with unescaped slashes.
     */
    private static String toString_withoutEscape(Object jObject) {
        if (jObject == null) {
            return "null";
        }
        var str = jObject.toString();
        str = str.replace("\\/", "/");
        return str;
    }

    /**
     * This method takes a schema and normalizes it. Therefore the jsonschema-refexpander is used.
     *
     * @param schema
     * @return the normalized schema
     */
    public String normalizeSchema(String schema){
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(schema);
        Normalizer normalizer = new Normalizer(jsonObject, config);
        return normalizer.normalize().toString();
    }

    /**
     * Check whether the schema contains a reference to an id or not.
     * It will be searched for something like #foo.
     *
     * @param schema the JsonSchema to be checked
     * @return if the schema did contain a reference to an id
     */
    public boolean containsIdRef(String schema){
        String pattern = "\"";
        boolean containsRefId = false;
        String[] splitter = schema.split(pattern);

        for (var keyWord : splitter) {
            //Check for #foo… --> id
            if (keyWord.matches("^#[a-z].*")) {
                containsRefId = true;
                break;
            }
        }

        for (var keyWord : splitter) {
            //check if def section already exists, if so do NOT normalise
            if (keyWord.matches("^\\$defs")) {
                containsRefId = false;
            }
        }

        return containsRefId;
    }
}
