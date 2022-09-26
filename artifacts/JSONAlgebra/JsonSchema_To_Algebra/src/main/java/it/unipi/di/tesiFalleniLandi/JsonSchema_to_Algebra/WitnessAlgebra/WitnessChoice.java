package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
//import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.OrPattReq_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.PatternRequired_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class WitnessChoice {
    private static Logger logger = LogManager.getLogger(WitnessChoice.class);

    WitnessProperty constraint;
    List<WitnessPattReq> reqList;
    List<WitnessPattReq> subList;  // this is a sublist of reqList
    List<WitnessPattReq> reqFullSet;  // this is a superlist of reqList, the pattern of the choice depends on the universe
    ComplexPattern pattern;
    WitnessAssertion schema;

    public WitnessProperty getConstraint() {
        return constraint;
    }
    public List<WitnessPattReq> getReqList() {
        return reqList;
    }
    public List<WitnessPattReq> getReqFullSet() {
        return reqFullSet;
    }
    public List<WitnessPattReq> getSubList() {
        return subList;
    }
    public ComplexPattern getPattern() {
        return pattern;
    }
    public WitnessAssertion getSchema() {
        return schema;
    }

    public void setSchema(WitnessAssertion schema) {
        this.schema = schema;
    }

    public void assertElse(Boolean condition,String msg) throws Exception {
        if (!condition) throw new Exception(msg);
    }

    public void checkPatternInvariant() throws Exception {
        ComplexPattern choicePattern = this.getConstraint().getPattern() ;
        for (WitnessPattReq req: getReqFullSet()) {
            if (getReqList().contains(req)) {
                choicePattern = choicePattern.intersect(req.getPattern());
            } else {
                choicePattern = choicePattern.minus(req.getPattern());
            }
        };
        assertElse(choicePattern.isEquivalent(pattern),"Choice pattern not equivalent to the one computed");
    }

    public void checkInclusionInvariant() throws Exception {
        //assertElse(getReqList().subSetOf(getReqFullSet()),"Choice inclusion invariant 1");
        //assertElse(getSubList().subSetOf(getReqList()),"Choice inclusion invariant 2");
    }

    public void checkInvariants() throws Exception {
        checkPatternInvariant();
        checkInclusionInvariant();
    }

    public void setReqFullSet(List<WitnessPattReq> newReqFullSet) {
        reqFullSet = newReqFullSet;
    }

    public void addToReqFullSet(WitnessPattReq newReq) {
        reqFullSet.add(newReq);
    }

    public void addToReqFullSet(List<WitnessPattReq> newReqFullSet) {
        reqFullSet.addAll(newReqFullSet);
    }

    public WitnessChoice(WitnessProperty constraint,
                         List<WitnessPattReq> reqList,
                         List<WitnessPattReq> subList,
                         List<WitnessPattReq> reqFullSet,
                         ComplexPattern pattern) {
        this.constraint = constraint;
        this.reqList = reqList;
        this.subList = subList;
        this.reqFullSet = reqFullSet;
        this.pattern = pattern;
        this.schema = null;
    }

    public WitnessChoice(WitnessProperty constraint,
                         List<WitnessPattReq> reqList,
                         List<WitnessPattReq> subList,
                         List<WitnessPattReq> reqFullSet,
                         ComplexPattern pattern,
                         WitnessAssertion schema) {
        this.constraint = constraint;
        this.reqList = reqList;
        this.subList = subList;
        this.reqFullSet = reqFullSet;
        this.pattern = pattern;
        this.schema = schema;
    }

    public WitnessChoice() {
        reqList = new LinkedList<>();
        subList = new LinkedList<>();
        reqFullSet = new LinkedList<>();
    }

    static WitnessChoice newBiSingletonChoice(WitnessProperty C_assertion,WitnessPattReq R_assertion,ComplexPattern pattern,WitnessAssertion schema) {
        WitnessChoice choice = new WitnessChoice();
        choice.reqList.add(R_assertion);
        choice.subList.add(R_assertion);
        choice.reqFullSet.add(R_assertion);
        choice.constraint = C_assertion;
        choice.pattern = pattern;
        choice.schema = schema;
        return choice;
    }

    // is this used??
    public WitnessChoice varNormalization_expansion(WitnessEnv env) throws WitnessException {
        WitnessProperty expandedConstraint = this.getConstraint().varNormalization_expansion(env);
        List<WitnessPattReq> expReqList = this.getReqList().stream().
                map(r -> r.varNormalization_expansion(env)).collect(toList());
        List<WitnessPattReq> expSubList = this.getSubList().stream().
                map(r -> r.varNormalization_expansion(env)).collect(toList());
        List<WitnessPattReq> expFullList = this.getReqFullSet().stream().
                map(r -> r.varNormalization_expansion(env)).collect(toList());
        WitnessAssertion expSchema = this.schema.varNormalization_expansion(env);
        return new WitnessChoice(expandedConstraint, expReqList, expSubList, expFullList, this.getPattern(), expSchema);
    }

    // is this used??
    public WitnessChoice DNF() throws WitnessException {
        WitnessProperty expandedConstraint = this.getConstraint().DNF();
        List<WitnessPattReq> expReqList = this.getReqList().stream().
                map(r -> r.DNF()).collect(toList());
        List<WitnessPattReq> expSubList = this.getSubList().stream().
                map(r -> r.DNF()).collect(toList());
        List<WitnessPattReq> expFullList = this.getReqFullSet().stream().
                map(r -> r.DNF()).collect(toList());
        WitnessAssertion expSchema = this.schema.DNF();
        return new WitnessChoice(expandedConstraint, expReqList, expSubList, expFullList, this.getPattern(), expSchema);
    }

    public void InPlaceDNF() throws WitnessException {
        schema = this.schema.DNF();
    }

    /*
    public WitnessChoice groupize() throws WitnessException, REException {
        WitnessProperty expandedConstraint = this.getConstraint().groupize();
        List<WitnessPattReq> expReqList = this.getReqList().stream().
                map(r -> r.groupize()).collect(toList());
        List<WitnessPattReq> expSubList = this.getSubList().stream().
                map(r -> r.groupize()).collect(toList());
        List<WitnessPattReq> expFullList = this.getReqFullSet().stream().
                map(r -> r.groupize()).collect(toList());
        WitnessAssertion expSchema = this.schema.groupize();
        return new WitnessChoice(expandedConstraint, expReqList, expSubList, expFullList, this.getPattern(), expSchema);
    }
*/
    public Float countVarWithoutBDD(WitnessEnv env, List<WitnessVar> visitedVar) {
        Float result = 0f;
        result += this.getConstraint().countVarWithoutBDD(env,visitedVar);
        result += this.getReqList().stream().
                map(r -> r.countVarWithoutBDD(env,visitedVar)).reduce(0f,Float::sum);
        result +=  this.getSubList().stream().
                map(r -> r.countVarWithoutBDD(env,visitedVar)).reduce(0f,Float::sum);
        result +=  this.getReqFullSet().stream().
                map(r -> r.countVarWithoutBDD(env,visitedVar)).reduce(0f,Float::sum);
        result +=  this.schema.countVarWithoutBDD(env,visitedVar);
        return result;
    }

    public int countVarToBeExp(WitnessEnv env) {
        return 0;
    }

    public void reachableRefs(Set<WitnessVar> collectedVar, WitnessEnv env) throws RuntimeException {
        this.getConstraint().reachableRefs(collectedVar, env);
        for (WitnessPattReq req : getReqList())
            req.reachableRefs(collectedVar, env);
        for (WitnessPattReq req : getSubList())
            req.reachableRefs(collectedVar, env);
        for (WitnessPattReq req : getReqFullSet())
            req.reachableRefs(collectedVar, env);
        this.schema.reachableRefs(collectedVar, env);
    }

    public boolean isBooleanExp() {
        return false;
    }

   /* public boolean isRecursive(WitnessEnv env, LinkedList<WitnessVar> visitedVar) {
        return value.isRecursive(env, visitedVar);
    }*/

    public WitnessVar buildOBDD(WitnessEnv env, WitnessVarManager varManager) {
        throw new UnsupportedOperationException();
    }

    /*@Override
    public void getReport(ReportResults reportResults) {
        value.getReport(reportResults);
    }*/

    public List<WitnessVar> uses() {
        HashSet<WitnessVar> result = new HashSet<>();
        result.addAll(this.getConstraint().uses().stream().collect(toList()));
        result.addAll(this.getReqList().stream().
                map(r -> r.uses()).flatMap(List::stream).collect(toList()));
        result.addAll(this.getSubList().stream().
                map(r -> r.uses()).flatMap(List::stream).collect(toList()));
        result.addAll(this.getSubList().stream().
                map(r -> r.uses()).flatMap(List::stream).collect(toList()));
        result.addAll(this.getSchema().uses().stream().collect(toList()));
        return new ArrayList<>(result);
    }

    // do we need equals??

}
