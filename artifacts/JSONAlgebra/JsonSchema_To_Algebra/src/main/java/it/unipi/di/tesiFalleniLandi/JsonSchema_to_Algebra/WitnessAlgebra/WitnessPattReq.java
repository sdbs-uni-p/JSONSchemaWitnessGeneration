package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.PatternRequired_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.*;

public class WitnessPattReq implements WitnessAssertion{
    private static Logger logger = LogManager.getLogger(WitnessPattReq.class);

    private ComplexPattern key;
    private WitnessAssertion value;

    private List<WitnessOrPattReq> orpList;

    protected WitnessPattReq(ComplexPattern key, WitnessAssertion assertion){
        if(assertion != null && assertion.getClass() == WitnessAnd.class && ((WitnessAnd) assertion).getIfUnitaryAnd() != null)
            assertion = ((WitnessAnd) assertion).getIfUnitaryAnd();
        logger.trace("Creating a new WitnessPattReq with key: {} and value: {}", key, assertion);
        this.key = key;
        value = assertion;
        orpList = new LinkedList<>();
    }

    public void fullConnect(WitnessOrPattReq orp){
        //if(orpList.contains(orp)) return;

        logger.debug("Connecting {} to {}", orp, this);

        orpList.add(orp);
        orp.halfConnect(this);
    }

    public void halfConnect(WitnessOrPattReq orp){

        orpList.add(orp);
    }

    public void deConnect(WitnessOrPattReq orp){
        if(!orpList.contains(orp)) return;

        logger.debug("De-Connecting {} to {}", orp, this);

        if(orpList.remove(orp))
            orp.deConnect(this);
    }

    public void deConnectAll(List<WitnessOrPattReq> orp){
        for(WitnessOrPattReq a : orp)
            deConnect(a);

    }

    public List<WitnessOrPattReq> getOrpList(){
        return orpList;
    }

    public WitnessAssertion getValue() {
        return value;
    }

    private WitnessPattReq() {
        orpList = new LinkedList<>();
    }

    public ComplexPattern getPattern() {
        return key;
    }

    public void setPattern(ComplexPattern key) {
        logger.trace("Setting {} as pattern in {}", key, this);
        this.key = key;
    }

    public void setValue(WitnessAssertion value) {
        logger.trace("Setting {} as value in {}", value, this);
        this.value = value;
    }

    @Override
    public String toString() {
        return "PattReq{" +
                "key=" + key +
                ", value=" + value +
                ", orpList=" + //orpList +
                '}';
    }

    @Override
    public void checkLoopRef(WitnessEnv env, Collection<WitnessVar> varList) throws RuntimeException {
        return;
    }

    @Override
    public void reachableRefs(Set<WitnessVar> collectedVar, WitnessEnv env) throws RuntimeException {
        value.reachableRefs(collectedVar, env);
    }

    @Override
    public WitnessAssertion mergeWith(WitnessAssertion a, WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException {
        logger.trace("Merging {} with {}", a, this);

        if(a.getClass() == this.getClass())
            return this.mergeElement((WitnessPattReq) a);

        return null;
    }

    @Override
    public WitnessAssertion merge(WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException {
        WitnessPattReq clone = this.clone();

        //Boolean rewritings
        if((this.value.getClass() == WitnessBoolean.class) && (!((WitnessBoolean)this.value).getValue()))
            return new WitnessBoolean(false);

        clone.value = value.merge(varManager, pattReqManager);

        return clone;
    }

    public WitnessAssertion mergeElement(WitnessPattReq a) {
        if(this.key.isSubsetOf(a.key) && this.value.equals(a.value)){
            logger.trace("Merge result {}", this);
            return this;
        }

        if(a.key.isSubsetOf(this.key) && a.value.equals(this.value)){
            logger.trace("Merge result {}", a);
            return a;
        }

        return null;
    }

    @Override
    public WitnessType getGroupType() {
        return new WitnessType(AlgebraStrings.TYPE_OBJECT);
    }

    @Override
    public Assertion getFullAlgebra() {
        PatternRequired_Assertion pra = new PatternRequired_Assertion();
        pra.add(key, this.value.getFullAlgebra());
        return pra;
    }

    @Override
    public WitnessPattReq clone() {
        logger.trace("Cloning WitnessPattReq: {}", this);
        WitnessPattReq clone = new WitnessPattReq();

        clone.key = key.clone();
        clone.value = value.clone();

        for(WitnessOrPattReq orp : orpList)
            clone.orpList.add((WitnessOrPattReq) orp.clone());

        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WitnessPattReq that = (WitnessPattReq) o;

        if (!Objects.equals(key, that.key)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public WitnessAssertion not(WitnessEnv env) throws REException {
        WitnessAnd and = new WitnessAnd();
        and.add(new WitnessType(AlgebraStrings.TYPE_OBJECT));
        and.add(new WitnessProperty(key.clone(), value.not(env)));

        return and;
    }

    @Override
    public WitnessAssertion groupize() throws WitnessException, REException {
        WitnessPattReq pattReq = new WitnessPattReq();
        pattReq.key = key;

        if(value != null){
            if(value.getClass() != WitnessAnd.class) {
                WitnessAnd and = new WitnessAnd();
                and.add(value);
                pattReq.value = and.groupize();
            } else
                pattReq.value = value.groupize();
        }

        return pattReq;
    }

    @Override
    public Float countVarWithoutBDD(WitnessEnv env, List<WitnessVar> visitedVar) {
        return value.countVarWithoutBDD(env, visitedVar);
    }

    @Override
    public int countVarToBeExp(WitnessEnv env) {
        return 0;
    }

    @Override
    public List<Map.Entry<WitnessVar, WitnessAssertion>> varNormalization_separation(WitnessEnv env, WitnessVarManager varManager) throws WitnessException, REException {
        List<Map.Entry<WitnessVar, WitnessAssertion>> newDefinitions = new LinkedList<>();

        if(value.getClass() == WitnessAnd.class && ((WitnessAnd) value).getIfUnitaryAnd() != null)
            value = ((WitnessAnd) value).getIfUnitaryAnd();

        if (value.getClass() != WitnessBoolean.class && value.getClass() != WitnessVar.class) {

            newDefinitions.addAll(value.varNormalization_separation(env, varManager));

            WitnessVar newVar = varManager.buildVar(varManager.getName(value));

            newDefinitions.add(new AbstractMap.SimpleEntry<>(newVar, value));

            value = newVar;
        }

        return newDefinitions;
    }

    @Override
    public WitnessAssertion varNormalization_expansion(WitnessEnv env) {
        return this;
    }

    @Override
    public WitnessAssertion DNF() {
        WitnessPattReq pattReq = new WitnessPattReq();
        pattReq.key = this.key;

        if(value != null) pattReq.value = this.value.clone();

        return pattReq;
    }

    @Override
    public WitnessAssertion toOrPattReq() {
        WitnessOrPattReq orPattReq = new WitnessOrPattReq();
        orPattReq.add(this);
        return orPattReq;
    }

    @Override
    public boolean isBooleanExp() {
        return false;
    }

    @Override
    public boolean isRecursive(WitnessEnv env, LinkedList<WitnessVar> visitedVar) {
        return value.isRecursive(env, visitedVar);
    }

    @Override
    public WitnessVar buildOBDD(WitnessEnv env, WitnessVarManager varManager) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getReport(ReportResults reportResults) {
        value.getReport(reportResults);
    }

    @Override
    public List<WitnessVar> uses() {
        return value.uses();
    }

}