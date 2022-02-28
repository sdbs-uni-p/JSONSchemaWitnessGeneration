package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessAssertion;

import java.util.LinkedList;
import java.util.List;

public class GenNull implements GenAssertion{

    @Override
    public String toString() {
        return "GenNull";
    }

    @Override
    public JsonElement getWitness() {
        return new JsonNull();
    }

    @Override
    public statuses generate() {
        return statuses.Populated;
    }

    @Override
    public JsonElement generateNext() {
        return null;
    }

    @Override
    public WitnessAssertion toWitnessAlgebra() {
        return null;
    }

    @Override
    public List<GenVar> usedVars() {
        return new LinkedList<>();
    }
}
