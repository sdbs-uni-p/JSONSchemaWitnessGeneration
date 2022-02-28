package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Properties_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.*;

public class WitnessProperty implements WitnessAssertion{
    private static Logger logger = LogManager.getLogger(WitnessProperty.class);

    private ComplexPattern key;
    private WitnessAssertion value;

    protected WitnessProperty() {
        logger.trace("Creating an empty WitnessProperty");
    }

    public WitnessProperty(ComplexPattern key, WitnessAssertion assertion){
        if(assertion != null && assertion instanceof WitnessAnd && ((WitnessAnd) assertion).getIfUnitaryAnd() != null)
            assertion = ((WitnessAnd) assertion).getIfUnitaryAnd();

        logger.trace("Creating a new WitnessPropertiy with key: {} and value: {}", key, assertion);
        this.key = key;
        value = assertion;
    }

    public WitnessAssertion getValue() {
        return value;
    }

    public ComplexPattern getPattern() {
        return key;
    }

    public void setPattern(ComplexPattern key) {
        logger.trace("Set {} as key of {}", key, this);
        this.key = key;
    }

    @Override
    public String toString() {
        return "Property{" +
                "key=" + key +
                ", value=" + value +
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
            return this.mergeElement((WitnessProperty) a, varManager, pattReqManager);

        return null;
    }

    @Override
    public WitnessAssertion merge(WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException {
        //Boolean Rewritings
//        if(value.getClass() == WitnessBoolean.class && ((WitnessBoolean) value).getValue()) return value;

        WitnessProperty newProp = this.clone();
        newProp.value = value.merge(varManager, pattReqManager);

        return newProp;
    }

    public WitnessAssertion mergeElement(WitnessProperty a, WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException {
        WitnessProperty result = null;

        if(a.value.equals(this.value))
            result =  new WitnessProperty(a.key.union(this.key), this.value);

        if(a.key.toString().equals(this.key.toString())
                && a.value.mergeWith(this.value, varManager, pattReqManager) != null) // we could use And instead of mergeWith
            result = new WitnessProperty(a.key.clone(), a.value.mergeWith(this.value, varManager, pattReqManager));

        logger.trace("Merge result: ", result);

        return result;
    }

    @Override
    public WitnessType getGroupType() {
        return new WitnessType(AlgebraStrings.TYPE_OBJECT);
    }

    @Override
    public Assertion getFullAlgebra() {
        Properties_Assertion p = new Properties_Assertion();
        p.addPatternProperties(key, value.getFullAlgebra());

        return p;
    }

    @Override
    public WitnessProperty clone() {
        WitnessProperty clone = new WitnessProperty();

        clone.key = key.clone();
        clone.value = value.clone();

        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WitnessProperty that = (WitnessProperty) o;

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
        WitnessType type = new WitnessType();
        type.add(AlgebraStrings.TYPE_OBJECT);
        and.add(type);
        and.add(env.pattReqManager.build(key.clone(), value.not(env)));

        return and;
    }

    @Override
    public WitnessAssertion groupize() throws WitnessException, REException {
        WitnessProperty prop = new WitnessProperty();
        prop.key = key;

        if(value.getClass() != WitnessAnd.class) {
            WitnessAnd and = new WitnessAnd();
            and.add(value);
            prop.value = and.groupize();
        }else
            prop.value = value.groupize();

        return prop;
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

        if(value instanceof WitnessAnd && ((WitnessAnd) value).getIfUnitaryAnd() != null) //to avoid AND[ref(x)]
            value = ((WitnessAnd) value).getIfUnitaryAnd();

        if (!(value instanceof WitnessBoolean || value instanceof WitnessVar)) {
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
    public WitnessAssertion DNF() throws WitnessException {
        WitnessProperty prop = new WitnessProperty();
        prop.key = this.key;
        if(value != null)   prop.value = this.value.DNF();

        return prop;
    }

    @Override
    public WitnessAssertion toOrPattReq() {
        value = value.toOrPattReq();

        return this;
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