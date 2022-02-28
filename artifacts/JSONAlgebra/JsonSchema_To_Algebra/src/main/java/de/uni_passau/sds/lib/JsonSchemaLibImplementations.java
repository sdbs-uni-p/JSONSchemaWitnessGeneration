package de.uni_passau.sds.lib;

/**
 * Enum of different implementations of {@link IJsonSchemaLib}.
 * @author Thomas Pilz
 */
public enum JsonSchemaLibImplementations {
    /**
     * Default implementation of {@link IJsonSchemaLib} used e.g. by JSON Schema Webapp.
     * Pass to {@link JsonSchemaLibFactory} to create an instance of {@link JsonSchemaToolLib}.
     */
    JSON_SCHEMA_TOOL_LIB
}
