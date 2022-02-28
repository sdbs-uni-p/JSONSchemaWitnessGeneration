package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Type_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.XBet_Assertion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.*;

public class WitnessXBet implements WitnessAssertion{
    private static Logger logger = LogManager.getLogger(WitnessXBet.class);

    Double min, max;

    public WitnessXBet(Double min, Double max) {
        if(min == null)
            this.min = Double.NEGATIVE_INFINITY;
        else
            this.min = min;

        if(max == null)
            this.max = Double.POSITIVE_INFINITY;
        else
            this.max = max;

        logger.trace("Created a new WitnessXBet : {}", this);
    }

    @Override
    public String toString() {
        return "XBet{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    @Override
    public WitnessAssertion merge(WitnessVarManager varManager, WitnessPattReqManager pattReqManager) {
        return this;
    }

    @Override
    public void checkLoopRef(WitnessEnv env, Collection<WitnessVar> varList) throws RuntimeException {

    }

    @Override
    public void reachableRefs(Set<WitnessVar> collectedVar, WitnessEnv env) throws RuntimeException {
    }

    @Override
    public WitnessAssertion mergeWith(WitnessAssertion a, WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException {
        if(min > max) {
            Type_Assertion type = new Type_Assertion();
            type.add(AlgebraStrings.TYPE_NUMBER);

            return type.not().toWitnessAlgebra(varManager,null,null);
        }

        if(a.getClass() == this.getClass())
            return this.mergeElement((WitnessXBet) a);

        return null;
    }

    public WitnessAssertion mergeElement(WitnessXBet a) throws REException {
        logger.trace("Merging {} with {}", a, this);
        Double m = (min < a.min) ? a.min : min;
        Double M = (max > a.max) ? a.max : max;

        if(a.min > max || a.max < min || m.equals(M)){
            Type_Assertion type = new Type_Assertion();
            type.add(AlgebraStrings.TYPE_NUMBER);

            logger.trace("Merge result: ", type.not());
            return type.not().toWitnessAlgebra(null,null,null);
        }

        WitnessXBet newXBet = new WitnessXBet(m, M);
        logger.trace("Merge result: ", newXBet);
        return (this.equals(newXBet) && a.equals(newXBet)) ? null : newXBet;
    }

    @Override
    public WitnessType getGroupType() {
        return new WitnessType(AlgebraStrings.TYPE_NUMBER);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WitnessXBet that = (WitnessXBet) o;

        if (min != null ? !min.equals(that.min) : that.min != null) return false;
        return max != null ? max.equals(that.max) : that.max == null;
    }

    @Override
    public int hashCode() {
        int result = min != null ? min.hashCode() : 0;
        result = 31 * result + (max != null ? max.hashCode() : 0);
        return result;
    }

    public Assertion getFullAlgebra() {
        return new XBet_Assertion(min == Double.NEGATIVE_INFINITY ? null : min, max == Double.POSITIVE_INFINITY ? null : max);
    }

    @Override
    public WitnessAssertion clone() {
        logger.trace("Cloning WitenssXBet {}", this);
        return new WitnessXBet(min, max);
    }

    @Override
    public WitnessAssertion not(WitnessEnv env) throws REException {
        return getFullAlgebra().not().toWitnessAlgebra(null,null,null);
    }

    @Override
    public WitnessAssertion groupize() {
        return this;
    }

    @Override
    public int countVarToBeExp(WitnessEnv env) {
        return 0;
    }

    @Override
    public Float countVarWithoutBDD(WitnessEnv env, List<WitnessVar> visitedVar) {
        return 0f;
    }

    @Override
    public List<Map.Entry<WitnessVar, WitnessAssertion>> varNormalization_separation(WitnessEnv env, WitnessVarManager varManager){
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