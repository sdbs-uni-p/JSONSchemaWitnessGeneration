package de.uni_passau.sds.lib.schemaComparism;

/**
 * All four possible relationships two JSON Schemas can be in.
 * Please read documentation for each value.
 * @author Thomas Pilz
 */
public enum JsonSchemaRelationships {
    /**
     * The two JSON Schemas L (= left) and R (= right) are equivalent.
     * There is no JSON document (= witness) which conforms to L but not to R.
     * And there is no witness which conforms to R but not to L.
     */
    EQUIVALENT,
    /**
     * The two JSON Schemas L (= left) and R (= right) have no relationship/ containment.
     * There is a JSON document (= witness) which conforms to L but not to R and at the same time there is a witness which conforms to R but not to L.
     */
    NO_RELATIONSHIP,
    /**
     * The R (= right) JSON Schema is a generalization of the L (= left) JSON Schema.
     * There is a JSON document (= witness) which conforms to R but not to L but at the same time there is no witness which conforms to L but not to R.
     */
    RIGHT_IS_GENERALIZATION,
    /**
     * The L (= left) JSON Schema is a generalization of the R (= right) JSON Schema.
     * There is a JSON document (= witness) which conforms to L but not to R but at the same time there is no witness which conforms to R but not to L.
     */
    LEFT_IS_GENERALIZATION;
}
