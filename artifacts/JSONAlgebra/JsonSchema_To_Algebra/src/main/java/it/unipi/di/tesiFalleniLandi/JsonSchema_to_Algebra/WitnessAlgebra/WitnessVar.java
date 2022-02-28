package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Ref_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WitnessVar implements WitnessAssertion{
    private static Logger logger = LogManager.getLogger(WitnessVar.class);

    private String name;

    private Integer height = null;

    public void setHeight(Integer height) {
        this.height = height;
    }

    public boolean isHeightSet(){
        return height != null;
    }

    public Integer getHeight() {
        return height;
    }

    protected WitnessVar(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Var{" +
                name +
                '}';
    }

    @Override
    public WitnessAssertion merge(WitnessVarManager varManager, WitnessPattReqManager pattReqManager) {
        return this;
    }

    @Override
    public void checkLoopRef(WitnessEnv env, Collection<WitnessVar> varList) throws RuntimeException {
        if(varList.contains(this)){
            throw new RuntimeException("Recursive definition: " + name);
        }else{
            varList.add(this);
            env.getDefinition(this).checkLoopRef(env, varList);
            varList.remove(this);
        }
    }

    @Override
    public void reachableRefs(Set<WitnessVar> collectedVar, WitnessEnv env) throws RuntimeException {
        if(!env.containsVar(this))
            throw new RuntimeException("Unreachable ref: " + name);
        else{
            if(collectedVar.contains(this)) return;//loop
            collectedVar.add(this);
            env.getDefinition(this).reachableRefs(collectedVar, env);
        }
    }

    @Override
    public WitnessAssertion mergeWith(WitnessAssertion a, WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException {
        if(a.getClass() == WitnessVar.class)
            return mergeElement((WitnessVar) a);

        return null;
    }

    public WitnessAssertion mergeElement(WitnessVar a) {
        if(a.name.equals(this.name)) return this;

        return null;
    }

    @Override
    public WitnessType getGroupType() {
        return null;
    }

    @Override
    public Assertion getFullAlgebra() {
        return new Ref_Assertion(name);
    }

    @Override
    public WitnessVar clone() {
        logger.trace("Cloning {}", this);
        return this; //TODO: WitnessVarmanager
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WitnessVar that = (WitnessVar) o;

        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public WitnessAssertion not(WitnessEnv env) throws REException {
        return env.getCoVar(this);
    }

    @Override
    public WitnessAssertion groupize() {
        return this;
    }

    @Override
    public int countVarToBeExp(WitnessEnv env) {
        return env.getDefinition(this).countVarToBeExp(env);
    }

    @Override
    public Float countVarWithoutBDD(WitnessEnv env, List<WitnessVar> visitedVar) {
        if(visitedVar.contains(this))
            return Float.POSITIVE_INFINITY;

        if(env.bdd.contains(this))//if there is an obdd with the same name i can return 0;
            return 0f;

        visitedVar.add(this);
        Float count = env.getDefinition(this).countVarWithoutBDD(env, visitedVar);
        visitedVar.remove(this);

        //if(name.startsWith(AlgebraStrings.NOT_DEFS))
        //  return 2f + count;

        return 1f + count;
    }

    public boolean isRecursive(WitnessEnv env, LinkedList<WitnessVar> visitedVar){
        if(visitedVar.contains(this)) return true;

        visitedVar.add(this);
        boolean tmp = env.getDefinition(this).isRecursive(env, visitedVar);
        visitedVar.remove(this);
        return tmp;
    }

    @Override
    public List<Map.Entry<WitnessVar, WitnessAssertion>> varNormalization_separation(WitnessEnv env, WitnessVarManager varManager) {
        return new LinkedList<>();
    }

    @Override
    public WitnessAssertion varNormalization_expansion(WitnessEnv env) {
        return this;
    }

    @Override
    public WitnessAssertion DNF() {
        return this.clone();
    }

    @Override
    public WitnessAssertion toOrPattReq() {
        return this;
    }

    @Override
    public boolean isBooleanExp() {
        return true;
    }

    // I do not understand this code - it seems to assume that when you invoke Var{x}.buildOBDD
    // the actual index node for Var{x} or for var forced_x has already been created
    @Override
    public WitnessVar buildOBDD(WitnessEnv env, WitnessVarManager varManager) throws WitnessException {
        if(env.bdd.contains(new WitnessVar("forced_"+name)))
            return varManager.buildVar("forced_"+name);

        if(!env.bdd.contains(this)) {
            throw new WitnessException("buildOBDD richiamato su variabile senza obdd associato (ne forzato): " + name);
        }

        return this;
    }

    @Override
    public void getReport(ReportResults reportResults) {
        return;
    }

    @Override
    public List<WitnessVar> uses() {
        List<WitnessVar> result = new LinkedList<WitnessVar>();
        result.add(this);
        return result;
//        return Stream.of(this).collect(Collectors.toList());
    }

    protected void forceOBDD(WitnessEnv env, WitnessVarManager varManager){
        logger.warn("forced the creation of {}, that is not ok", new WitnessVar("forced_"+name));
        env.bdd.createVar(varManager.buildVar("forced_"+name));
    }
}