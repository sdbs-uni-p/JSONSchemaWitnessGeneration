package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVar;

public class GenVarTrue extends GenVar {

    public GenVarTrue(String varname) {
        super("true");
        this.setWitness(_default);
        this.setStatus(statuses.Populated);
    }


}
