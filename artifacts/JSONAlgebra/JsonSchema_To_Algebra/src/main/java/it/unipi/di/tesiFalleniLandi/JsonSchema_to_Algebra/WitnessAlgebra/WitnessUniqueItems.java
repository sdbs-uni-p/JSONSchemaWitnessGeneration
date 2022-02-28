package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Type_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.UniqueItems_Assertion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.*;

public class WitnessUniqueItems implements WitnessAssertion{
    private static Logger logger = LogManager.getLogger(WitnessUniqueItems.class);

    public WitnessUniqueItems() {
        logger.trace("Creating a new WitnessUniqueItems");
    }

    @Override
    public String toString() {
        return "UniqueItems{}";
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
        if(a.getClass() == this.getClass()) return mergeElement((WitnessUniqueItems) a);
        if(a.getClass() == WitnessRepeateditems.class) return mergeElement((WitnessRepeateditems) a);

        return null;
    }

    @Override
    public WitnessAssertion merge(WitnessVarManager varManager, WitnessPattReqManager pattReqManager) {
        return this;
    }

    public WitnessAssertion mergeElement(WitnessUniqueItems a) {
        return a;
    }

    public WitnessAssertion mergeElement(WitnessRepeateditems a) throws REException {
        Type_Assertion t = new Type_Assertion();
        t.add(AlgebraStrings.TYPE_ARRAY);

        logger.trace("Merge returning {} ", t.not());
        return t.not().toWitnessAlgebra(null,null, null);
    }

    @Override
    public WitnessType getGroupType() {
        return new WitnessType(AlgebraStrings.TYPE_ARRAY);
    }

    @Override
    public Assertion getFullAlgebra() {
        return new UniqueItems_Assertion();
    }

    @Override
    public WitnessAssertion clone() {
        logger.trace("Cloning {}", this);
        return new WitnessUniqueItems();
    }

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

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
