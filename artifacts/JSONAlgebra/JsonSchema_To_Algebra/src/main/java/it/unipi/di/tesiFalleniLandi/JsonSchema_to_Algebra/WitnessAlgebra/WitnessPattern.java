package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Pattern_Assertion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.*;

public class WitnessPattern implements WitnessAssertion{
    private static Logger logger = LogManager.getLogger(WitnessPattern.class);

    private ComplexPattern pattern;

    public WitnessPattern(ComplexPattern pattern){
        this.pattern = pattern;
        logger.trace("Created a new WitnessPattern {}", this);
    }

    private WitnessPattern() { }

    @Override
    public String toString() {
        return "Pattern{" +
                "pattern=" + pattern +
                '}';
    }

    @Override
    public void checkLoopRef(WitnessEnv env, Collection<WitnessVar> varList) throws RuntimeException {
        return;
    }

    @Override
    public void reachableRefs(Set<WitnessVar> collectedVar, WitnessEnv env) throws RuntimeException {

    }

    @Override
    public WitnessAssertion mergeWith(WitnessAssertion a, WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException {
        logger.trace("Merging {} with {}", a, this);
        if(a.getClass() == this.getClass())
            return mergeElement((WitnessPattern) a);

        return null;
    }

    @Override
    public WitnessAssertion merge(WitnessVarManager varManager, WitnessPattReqManager pattReqManager) {
        return this;
    }

    public WitnessAssertion mergeElement(WitnessPattern a) {
        WitnessPattern result = new WitnessPattern(pattern.intersect(a.pattern));

        logger.trace("Merge result {}", this);

        return result;
    }

    @Override
    public WitnessType getGroupType() {
        return new WitnessType(AlgebraStrings.TYPE_STRING);
    }

    @Override
    public Assertion getFullAlgebra() {
        return new Pattern_Assertion(pattern);
    }

    @Override
    public WitnessAssertion clone() {
        logger.trace("Cloning WitnessPattern {}", this);
        WitnessPattern clone = new WitnessPattern();
        clone.pattern = pattern.clone();

        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WitnessPattern that = (WitnessPattern) o;

        return pattern.isEquivalent(that.pattern);
    }

    @Override
    public int hashCode() {
        return pattern != null ? pattern.hashCode() : 0;
    }

    @Override
    public WitnessAssertion not(WitnessEnv env) throws REException {
        WitnessAnd and = new WitnessAnd();

        and.add(new WitnessType(AlgebraStrings.TYPE_STRING));
        and.add(new WitnessPattern(pattern.complement()));

        return and;
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

    public ComplexPattern getPattern() {
        return pattern;
    }
}