package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessAssertion;

import java.util.LinkedList;
import java.util.List;

public interface GenAssertion extends Cloneable{
    public String _sep = "\r\n";
    public enum statuses {Open, Empty, Populated };
    public enum GenArrayType {EMPTY, NOITEMS, NOCONTAINS, ONECONTAINS, MANYSIMPLECONTAINS, GENERALCASE};

    public Double _default_min_prop = 0d;
    public Double _default_max_prop = 1d;
    public JsonElement getWitness();
    public statuses generate();
    public JsonElement generateNext();
    public String toString();
    public WitnessAssertion toWitnessAlgebra();

    public JsonElement _default = new JsonPrimitive(123);

    public List<GenVar> usedVars();


}
