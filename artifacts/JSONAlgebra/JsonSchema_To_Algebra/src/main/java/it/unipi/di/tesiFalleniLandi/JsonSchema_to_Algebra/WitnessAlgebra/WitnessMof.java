package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Mof_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Type_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.*;

public class WitnessMof implements WitnessAssertion{ //fare anche il caso merge con notMof
    private static Logger logger = LogManager.getLogger(WitnessMof.class);

    private Double value;

    public WitnessMof(Double value) {
        this.value = value;
        logger.trace("Created a new WitnessMof: {}", this);
    }

    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Mof{" +
                "value=" + value +
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
        logger.trace("Merging {} with {}", a, this);

        if(a.getClass() == WitnessMof.class)
            return this.mergeElement((WitnessMof) a);
        else if(a.getClass() == WitnessNotMof.class)
            return this.mergeElement((WitnessNotMof) a);

        return null;
    }

    public WitnessAssertion mergeElement(WitnessMof a) {
        WitnessMof result = new WitnessMof(a.value * (value / gcd(a.value, value)));
        logger.trace("Merge result: {}", result);
        return result;
    }

    public WitnessAssertion mergeElement(WitnessNotMof a) throws REException {

        WitnessNotMof notMof = a;
        Double val1 = notMof.getValue();
        Double val2 = this.value;

        if(val2 % val1 == 0) {
            Type_Assertion type = new Type_Assertion();
            type.add(AlgebraStrings.TYPE_NUMBER);

            logger.trace("Merge result: {}", type.not());
            return type.not().toWitnessAlgebra(null,null, null);
        }else {
            logger.trace("Merge result: null");
            return null;
        }
    }

    @Override
    public WitnessAssertion merge(WitnessVarManager varManager, WitnessPattReqManager pattReqManager) {
        return this;
    }

    @Override
    public WitnessType getGroupType() {
        return new WitnessType(AlgebraStrings.TYPE_NUMBER);
    }

    @Override
    public Assertion getFullAlgebra() {
        return new Mof_Assertion(value);
    }

    @Override
    public WitnessAssertion clone() {
        return new WitnessMof(value);
    }

    private static Double gcd(Double a, Double b)
    {
        while (b > 0.001)
        {
            Double temp = b;
            b = a % b;
            a = temp;
        }
        return a;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WitnessMof that = (WitnessMof) o;

        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    /*
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

     */

    @Override
    public WitnessAssertion not(WitnessEnv env) throws REException {
        return getFullAlgebra().not().toWitnessAlgebra(null,null, null);
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
        return this.clone();
    }

    @Override
    public WitnessAssertion DNF() {
        return this.clone();
    }

    @Override
    public WitnessAssertion toOrPattReq(){
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
    public WitnessVar buildOBDD(WitnessEnv env, WitnessVarManager varManager) throws WitnessException {
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