package de.uni_passau.sds.lib;

import com.google.gson.JsonSyntaxException;
import de.uni_passau.sds.lib.exceptions.InvalidWitnessException;
import de.uni_passau.sds.lib.exceptions.WitnessGenerationException;
import de.uni_passau.sds.lib.schemaComparism.JsonSchemaComparison;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Interface providing a client with the core functionality of the JSON Schema project.
 * Core functionality is considered to be:
 * - Generation of (unprecedented) witnesses for a JSON Schema.
 * - Checking JSON Schema containment respectively the relationship between two JSON Schemas.
 *
 * Clients using this interface currently (14/06/2021) include
 *  - JSON Schema Webapp
 *  - JSON Schema Commandline Tool
 *  - Yet Another Witness API
 * @author Thomas Pilz
 */
public interface IJsonSchemaLib {

    /**
     * Generates a witness for a JSON Schema document.
     *
     * @param schema JSON Schema, a witness should be generated for.
     * @return A valid witness for given JSON Schema of an empty optional if no witness exists
     * @throws WitnessGenerationException An exception occurred during witness generation
     * @throws InvalidWitnessException An invalid witness has been generated
     * @throws JsonSyntaxException provided JSON Schemas are not valid JSON (GSON Exception)
     */
    Optional<String> generateWitness(String schema) throws WitnessGenerationException, InvalidWitnessException, JsonSyntaxException;

    /**
     * Generates an (unprecedented) witness for a JSON Schema document.
     *
     * @param schema JSON Schema, a witness should be generated for.
     * @param previousWitnesses Collection of previously generated witnesses (or witnesses to be excluded from the result set)
     * @return A valid witness for given JSON Schema of an empty optional if no witness exists
     * @throws WitnessGenerationException An exception occurred during witness generation
     * @throws InvalidWitnessException An invalid witness has been generated
     * @throws JsonSyntaxException provided JSON Schemas are not valid JSON (GSON Exception)
     */
    Optional<String> generateWitness(String schema, Collection<String> previousWitnesses) throws WitnessGenerationException, InvalidWitnessException, JsonSyntaxException;

    /**
     * Compares two JSON Schemas. Returns relationship the two schemas are in or in other words checks schema containment.
     * An empty witness (left or right witness) will be denoted with the empty witness "{\"Problem\":\"Empty witness\"}".
     *
     * @param leftSchema a JSON Schema
     * @param rightSchema another JSON Schema
     * @throws WitnessGenerationException An exception occurred during witness generation
     * @throws InvalidWitnessException An invalid witness has been generated
     * @throws JsonSyntaxException provided JSON Schemas are not valid JSON (GSON Exception)
     * @return Relationship between schemas and generated witnesses which can be used to proof why this relationship exists.
     */
    JsonSchemaComparison compareSchemas(String leftSchema, String rightSchema) throws WitnessGenerationException, InvalidWitnessException, JsonSyntaxException;

    /**
     * Generates the schema "s1 and not s2" for given schemas s1 and s2.
     *
     * @param schema1 The first schema.
     * @param schema2 The second schema.
     * @return A string representation of the schema difference.
     * @throws JsonSyntaxException JSON Schema could not be parsed due to wrong syntax.
     */
    String generateSchemaDifference(String schema1, String schema2) throws JsonSyntaxException;

    /**
     * Convert JSON Schema to Full Algebra string.
     *
     * @param jsonSchema JSON Schema as string
     * @return Full Algebra string
     * @throws JsonSyntaxException JSON Schema could not be parsed due to wrong syntax
     */
    String convJsonSchemaToFullAlg(String jsonSchema) throws JsonSyntaxException;

    /**
     * Convert Full Algebra string to JSON Schema.
     *
     * @param algString String in correct full algebra
     * @return JSON Schema as string
     */
    String convFullAlgToJsonSchema(String algString);

    /**
     * Perform "not"-Elimination on algebraic string.
     *
     * @param algString String in correct full algebra
     * @return Full Algebra string with "not" eliminated.
     */
    String doNotElimination(String algString);

    /**
     * This is a copy from the original Method compareSchemas (JsonSchemaToolLib).
     * This is cut off and will return the inner schemas that are generated in a list.
     *
     * @param leftSchema The left inner schema.
     * @param rightSchema The right inner schema.
     * @return A list consisting the left and the right inner schema.
     * @throws WitnessGenerationException An exception occurred during witness generation.
     * @throws InvalidWitnessException An invalid witness has been generated.
     * @throws JsonSyntaxException JSON Schema could not be parsed due to wrong syntax.
     */
    List<String> getInternalSchemas(String leftSchema, String rightSchema) throws WitnessGenerationException, InvalidWitnessException, JsonSyntaxException;

    /**
     * Normalize a given JsonSchema.
     *
     * @param schema the schema that is going to be normalized
     * @return the normalized Schema as String
     */
    String normalizeSchema(String schema);
}
