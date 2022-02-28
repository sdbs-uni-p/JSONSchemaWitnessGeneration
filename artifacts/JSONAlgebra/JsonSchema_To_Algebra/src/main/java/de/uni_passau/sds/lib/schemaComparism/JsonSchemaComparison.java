package de.uni_passau.sds.lib.schemaComparism;

import java.io.Serializable;
import java.util.Objects;

/**
 * Result returned when comparing two
 * @author Thomas Pilz
 */
public class JsonSchemaComparison implements Serializable {
    protected String leftWitness;
    protected String rightWitness;
    protected JsonSchemaRelationships relationship;

    public JsonSchemaComparison(){}

    public JsonSchemaComparison(JsonSchemaRelationships relationship) {
        this.relationship = relationship;
    }

    public JsonSchemaComparison(String leftWitness, String rightWitness, JsonSchemaRelationships relationship) {
        this.leftWitness = leftWitness;
        this.rightWitness = rightWitness;
        this.relationship = relationship;
    }

    public JsonSchemaRelationships getRelationship() {
        return relationship;
    }

    public void setRelationship(JsonSchemaRelationships relationship) {
        this.relationship = relationship;
    }

    public String getLeftWitness() {
        return leftWitness;
    }

    public void setLeftWitness(String leftWitness) {
        this.leftWitness = leftWitness;
    }

    public String getRightWitness() {
        return rightWitness;
    }

    public void setRightWitness(String rightWitness) {
        this.rightWitness = rightWitness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonSchemaComparison that = (JsonSchemaComparison) o;
        return relationship == that.relationship && Objects.equals(leftWitness, that.leftWitness) && Objects.equals(rightWitness, that.rightWitness);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relationship, leftWitness, rightWitness);
    }

    @Override
    public String toString() {
        return "JsonSchemaComparism{" +
                "relationship=" + relationship +
                ", leftWitness='" + leftWitness + '\'' +
                ", rightWitness='" + rightWitness + '\'' +
                '}';
    }
}
