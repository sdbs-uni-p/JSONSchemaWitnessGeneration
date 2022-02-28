package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Type_Assertion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.*;

public class WitnessType implements WitnessAssertion{
    private static Logger logger = LogManager.getLogger(WitnessType.class);

    private Set<String> type;

    protected WitnessType(){
        type = new HashSet<>();
        logger.trace("Creating an empty WitnessType");
    }

    public Set<String> getType() {
        return type;
    }

    public WitnessType(String str){
        type = new HashSet<>();
        type.add(str);
        logger.trace("Created a new WitnessType: ", this);
    }

    public WitnessType(Collection<String> list){
        type = new HashSet<>();
        for(String s : list)
            add(s);
    }

    public void add(String type){
        logger.trace("Adding {} to {}", type, this);
        this.type.add(type);
    }

    public void add(WitnessType type){
        logger.trace("Adding {} to {}", type, this);
        this.type.addAll(type.type);
    }

    public List<WitnessType> separeTypes(){
        List<WitnessType> returnList = new LinkedList<>();

        for(String str : type)
            returnList.add(new WitnessType(str));

        logger.trace("Splitting {} in {}", this, returnList);

        return returnList;
    }

    @Override
    public String toString() {
        return "Type{" +
                "type=" + type +
                '}';
    }

    @Override
    public void checkLoopRef(WitnessEnv env, Collection<WitnessVar> varList) throws RuntimeException {
        return;
    }

    @Override
    public void reachableRefs(Set<WitnessVar> collectedVar, WitnessEnv env) throws RuntimeException {
        return;
    }

    @Override
    public WitnessAssertion mergeWith(WitnessAssertion a, WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException {
        if(a.getClass() == WitnessOr.class || a.getClass() == WitnessAnd.class) return a.mergeWith(this, varManager, pattReqManager);

        if(a.getClass() == this.getClass())
            return this.mergeElement((WitnessType) a);

        return null;
    }

    @Override
    public WitnessAssertion merge(WitnessVarManager varManager, WitnessPattReqManager pattReqManager) {
        return this;
    }


    public WitnessAssertion mergeElement(WitnessType a) {
        logger.trace("Merging {} with {}", a, this);

        WitnessType newType = new WitnessType();

        //same type
        if(a.equals(this))
            return this;

        for(String str : type){
            if(a.type.contains(str)) {
                newType.add(str);
                continue;
            }

            /*
            if(a.type.contains(AlgebraStrings.TYPE_INTEGER) && str.equals(AlgebraStrings.TYPE_NUMBER)
                || a.type.contains(AlgebraStrings.TYPE_NUMBER) && str.equals(AlgebraStrings.TYPE_INTEGER))
                newType.add(AlgebraStrings.TYPE_INTEGER);

            if(a.type.contains(AlgebraStrings.TYPE_NUMBER) && str.equals(AlgebraStrings.TYPE_NUMNOTINT)
                    || a.type.contains(AlgebraStrings.TYPE_NUMNOTINT) && str.equals(AlgebraStrings.TYPE_NUMBER))
                newType.add(AlgebraStrings.TYPE_NUMNOTINT);

             */
        }

        if(newType.type.isEmpty())
            return new WitnessBoolean(false);

        return newType;
    }

    @Override
    public WitnessType getGroupType() {
        return this;
    }

    @Override
    public Assertion getFullAlgebra() {
        Type_Assertion t = new Type_Assertion();

        for(String str : type)
            t.add(str);

        return t;
    }

    @Override
    public WitnessType clone() {
        logger.trace("Cloning {}", this);
        return new WitnessType(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WitnessType type1 = (WitnessType) o;

        LinkedList<String> list = new LinkedList();

        if(this.type.size() >= type1.type.size()) {
            list.addAll(this.type);
            list.removeAll(type1.type);
        }else{
            list.addAll(type1.type);
            list.removeAll(this.type);
        }

        return list.size() == 0;

    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    @Override
    public WitnessAssertion not(WitnessEnv env) throws REException {
        return getFullAlgebra().not().toWitnessAlgebra(null, null, null);
    }

    @Override
    public WitnessAssertion groupize() {
        return this;
    }

    @Override
    public Float countVarWithoutBDD(WitnessEnv env, List<WitnessVar> visitedVar) {
        return 0f;
    }

    @Override
    public int countVarToBeExp(WitnessEnv env) {
        return 0;
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
        return false;
    }

    @Override
    public boolean isRecursive(WitnessEnv env, LinkedList<WitnessVar> visitedVar) {
        return false;
    }

    @Override
    public WitnessVar buildOBDD(WitnessEnv env, WitnessVarManager varManager) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getReport(ReportResults reportResults) {
        return;
    }

    @Override
    public List<WitnessVar> uses() {
        return new ArrayList<>();
    }

}