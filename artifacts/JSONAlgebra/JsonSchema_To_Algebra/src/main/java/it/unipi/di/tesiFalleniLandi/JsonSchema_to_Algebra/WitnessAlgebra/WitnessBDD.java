package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessBDDException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import jdd.bdd.BDD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WitnessBDD {
    private static Logger logger = LogManager.getLogger(WitnessBDD.class);
    private static final int NODESIZE = 1000;
    private static final int CACHESIZE = 1000;

    private BiMap<WitnessVar, Integer> indexNode;
    private BDD bdd;

    private final WitnessVar trueVar;
    private final WitnessVar falseVar;

    private WitnessVarManager varManager;


    public WitnessVar getTrueVar(){
        return trueVar.clone();
    }
    public WitnessVar getFalseVar(){
        return falseVar.clone();
    }

    public WitnessBDD(WitnessVarManager varManager) {
        //indexNode: one-to-one connection between Variables and obdd's
        indexNode = HashBiMap.create();
        //create new obdd, using the defined ordering
        bdd = new BDD(NODESIZE, CACHESIZE);

        this.varManager = varManager;
        trueVar = varManager.buildVar("OBDD_true");
        falseVar = varManager.buildVar("OBDD_false");
        indexNode.put(trueVar, bdd.getOne());
        indexNode.put(falseVar, bdd.getZero());
    }

    public WitnessVar createVar(){
        int i = bdd.ref(bdd.createVar());
        WitnessVar var = varManager.buildVar("OBDD_"+i);
        indexNode.put(var, i);

        return var;
    }


    public void createVar(WitnessVar var){
        if(indexNode.containsKey(var))
            return;

        if(var.getName().startsWith(AlgebraStrings.NOT_DEFS)) {
            WitnessVar coVar = varManager.buildVar(var.getName().replaceFirst(AlgebraStrings.NOT_DEFS, ""));
            Integer coI = indexNode.get(coVar);

            if(coI == null) {
                coI = bdd.ref(bdd.createVar());
                indexNode.put(coVar, coI);
            }

            Integer i = bdd.ref(bdd.not(coI));
            if (indexNode.containsValue(i)) return;
            indexNode.put(var, i);


        }else {
            int i = bdd.ref(bdd.createVar());
            indexNode.put(var, i);
        }
    }


    /*
    //Old version, bdd without not
    public void createVar(WitnessVar var){
        if(indexNode.containsKey(var)) return;

        int i = bdd.createVar();
        indexNode.put(var, i);
    }
    */

    public WitnessVar not(WitnessEnv env, WitnessVar u) throws WitnessException {
        Integer i = indexNode.get(u);

        if(i == null) throw new WitnessBDDException("Variable not found!");

        int newI = bdd.ref(bdd.not(i));

        WitnessVar var = indexNode.inverse().get(newI);

        if(var == null) {
            var = varManager.buildVar("OBDD_" + newI);
            indexNode.put(var, newI);
        }

        return var;
    }

    public WitnessVar and(WitnessEnv env, WitnessVar u1, WitnessVar u2) throws WitnessException {
        Integer i1 = indexNode.get(u1);
        Integer i2 = indexNode.get(u2);

        if(i1 == null || i2 == null) throw new WitnessBDDException("Variable not found!");

        int newI = bdd.ref(bdd.and(i1, i2));

        WitnessVar var = indexNode.inverse().get(newI);

        if(var == null) {
            var = varManager.buildVar("OBDD_" + newI);
            indexNode.put(var, newI);
        }

        return var;
    }


    public WitnessVar or(WitnessEnv env, WitnessVar u1, WitnessVar u2) throws WitnessException {
        Integer i1 = indexNode.get(u1);
        Integer i2 = indexNode.get(u2);

        if(i1 == null || i2 == null) throw new WitnessBDDException("Variable not found!");

        int newI = bdd.ref(bdd.or(i1, i2));

        WitnessVar var = indexNode.inverse().get(newI);

        if(var == null) {
            var = varManager.buildVar("OBDD_" + newI);
            indexNode.put(var, newI);
        }

        return var;
    }

    public void rename(WitnessVar oldName, WitnessVar newName){
        if(!indexNode.containsKey(oldName)) {
            logger.warn("Renaming in BDD oldName: {} with newName: {}", oldName, newName);
            return;
        }

        indexNode.put(newName, indexNode.remove(oldName));
    }


    public boolean contains(WitnessVar var){
        return indexNode.containsKey(var);
    }
}


