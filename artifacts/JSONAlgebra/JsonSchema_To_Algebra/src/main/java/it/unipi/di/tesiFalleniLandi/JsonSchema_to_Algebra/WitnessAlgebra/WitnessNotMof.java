package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.NotMof_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.*;

public class WitnessNotMof implements WitnessAssertion{
    private static Logger logger = LogManager.getLogger(WitnessNotMof.class);

    private Double value;

    public WitnessNotMof(Double value) {
        this.value = value;
        logger.trace("Created a new WitnessNotMof: {}", this);
    }

    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "NotMof{" +
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
    public WitnessAssertion mergeWith(WitnessAssertion a, WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException { //caso base: tipi diversi => non dovrebbe mai succedere
        logger.trace("Merging {} with {}", a, this);

        if(a.getClass() == this.getClass())
            return this.mergeElement((WitnessNotMof) a);

        return null;
    }

    @Override
    public WitnessAssertion merge(WitnessVarManager varManager, WitnessPattReqManager pattReqManager) {
        return this;
    }

    public WitnessAssertion mergeElement(WitnessNotMof notMof) {
        WitnessNotMof result = null;

        Double val1 = notMof.value;
        Double val2 = this.value;

        if (val1 % val2 == 0)
            result = new WitnessNotMof(val2);
        else if (val2 % val1 == 0)
            result = new WitnessNotMof(val1);

        logger.trace("Merge result: {}", result);
        return result;
    }

    public static double lcm(Double number1, Double number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }
        double absNumber1 = Math.abs(number1);
        double absNumber2 = Math.abs(number2);
        double absHigherNumber = Math.max(absNumber1, absNumber2);
        double absLowerNumber = Math.min(absNumber1, absNumber2);
        double lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }

    @Override
    public WitnessType getGroupType() {
        return new WitnessType(AlgebraStrings.TYPE_NUMBER);
    }

    @Override
    public Assertion getFullAlgebra() {
        return new NotMof_Assertion(value);
    }

    @Override
    public WitnessAssertion clone() {
        return new WitnessNotMof(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WitnessNotMof that = (WitnessNotMof) o;

        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
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
        return this.clone();
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
    public WitnessVar buildOBDD(WitnessEnv env, WitnessVarManager varManager) throws WitnessException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getReport(ReportResults reportResults) {

    }

    @Override
    public List<WitnessVar> uses() {
        return new ArrayList<>();
    }
}