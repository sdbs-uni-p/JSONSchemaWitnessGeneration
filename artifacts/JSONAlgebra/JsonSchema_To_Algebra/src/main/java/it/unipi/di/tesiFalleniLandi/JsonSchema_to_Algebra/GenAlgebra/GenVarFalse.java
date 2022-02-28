package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import com.google.gson.JsonElement;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GenVarFalse extends GenVar {

    public GenVarFalse(String varname) {
        super("false");
        this.setStatus(statuses.Empty);
    }
//
//    @Override
//    public JsonElement getWitness() {
//        return null;
//    }
}
