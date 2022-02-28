package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import de.uni_passau.sds.patterns.REException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WitnessTypedAnd extends WitnessAnd{
    //flag useful to avoid re-preparation - works for object and array groups
    private Boolean prepared; //null by default since relevant only for specific And expressions
    public Boolean isPrepared(){
        if(prepared) return prepared;
        else return false;
    }
    public void setPrepared(){prepared = true;}

    //invariant: must have a type


    public void checkInvariants() throws Exception {
        //checking if it's an object group
        if (andList.get(WitnessType.class) == null) { //and without type specified
            throw new Exception("WitnessTypedAnd without type specified");
        }

        if (andList.get(WitnessType.class) != null && !andList.get(WitnessType.class).contains(new WitnessType(AlgebraStrings.TYPE_OBJECT))) { //if is not an object type
            throw new Exception("WitnessTypedAnd without type object assertion");
        }
        if (andList.get(WitnessType.class).size() > 1) {//if contains more than one type
            throw new Exception("WitnessTypedAnd with more than one type specified");
        }
    }

    @Override
    public List<Map.Entry<WitnessVar, WitnessAssertion>> objectPrepare(WitnessEnv env) throws REException, WitnessException {
        return super.objectPrepare(env);
    }

    @Override
    public List<Map.Entry<WitnessVar, WitnessAssertion>> arrayPreparation(WitnessEnv env) throws WitnessException, REException {
        return super.arrayPreparation(env);
    }
}
