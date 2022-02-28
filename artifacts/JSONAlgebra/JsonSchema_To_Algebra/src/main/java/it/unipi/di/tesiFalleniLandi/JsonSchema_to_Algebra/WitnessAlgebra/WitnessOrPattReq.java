package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.OrPattReq_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.*;
import java.util.stream.Collectors;

public class WitnessOrPattReq implements WitnessAssertion{
    private static Logger logger = LogManager.getLogger(WitnessOrPattReq.class);

    List<WitnessPattReq> reqList;  // AnyOf[ req1, ..., reqN ]

    public List<WitnessPattReq> getReqList() {
        return reqList;
    }

    public WitnessOrPattReq() {
        reqList = new LinkedList<>();
    }

    public boolean add(WitnessPattReq toAdd) {
        logger.trace("Adding {} as  in {}", toAdd, this);
        return reqList.add(toAdd);
    }

    public void fullConnect(WitnessPattReq req){
        if(reqList == null) reqList = new LinkedList<>();

        //if(ORP.contains(req)) return;
        logger.trace("Connecting {} to {}", req, this);

        reqList.add(req);
        req.halfConnect(this);
    }

    public void halfConnect(WitnessPattReq req){
        if(reqList == null) reqList = new LinkedList<>();

        reqList.add(req);
    }

    public void deConnect(WitnessPattReq req){
        if(!reqList.contains(req)) return;

        logger.trace("De-Connecting {} to {}", req, this);

        if(reqList.remove(req))
            req.deConnect(this);
    }

    public void remove(WitnessPattReq req){
        reqList.remove(req);
    }

    @Override
    public void checkLoopRef(WitnessEnv env, Collection<WitnessVar> varList) throws RuntimeException {
        for(WitnessAssertion assertion : reqList)
            assertion.checkLoopRef(env, varList);
    }

    @Override
    public void reachableRefs(Set<WitnessVar> collectedVar, WitnessEnv env) throws RuntimeException {
        for(WitnessAssertion req : reqList)
            req.reachableRefs(collectedVar, env);
    }

    @Override
    public WitnessAssertion mergeWith(WitnessAssertion a, WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException {
        return null;
    }

    @Override
    public WitnessAssertion merge(WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException {
        return this;
    }

    @Override
    public WitnessType getGroupType() {
        return new WitnessType(AlgebraStrings.TYPE_OBJECT); //GG 2/11/2020 was: null
    }

    @Override
    public Assertion getFullAlgebra() {
        OrPattReq_Assertion opr_a = new OrPattReq_Assertion();

        for(WitnessPattReq el : reqList){
            opr_a.add(el.getPattern(), el.getValue().getFullAlgebra());
        }

        return opr_a;
    }

    @Override
    public WitnessAssertion clone() {
        logger.trace("Cloning WitnessOrPattReq: {}", this);
        WitnessOrPattReq clone = new WitnessOrPattReq();

        for(WitnessPattReq el : reqList){
            clone.add(el);
        }

        return clone;
    }

    @Override
    public WitnessAssertion not(WitnessEnv env) throws REException {
        WitnessAnd and = new WitnessAnd();

        for(WitnessPattReq req : reqList)
            and.add(req.not(env));

        return and;
    }

    @Override
    public WitnessAssertion groupize() throws WitnessException, REException {
        WitnessOrPattReq orp = new WitnessOrPattReq();

        for(WitnessPattReq req : reqList)
            orp.add((WitnessPattReq) req.groupize());

        return orp;
    }

    @Override
    public Float countVarWithoutBDD(WitnessEnv env, List<WitnessVar> visitedVar) {
        Float count = 0f;

        for (WitnessPattReq req : reqList) {
            count += req.countVarWithoutBDD(env, visitedVar);
        }
        return count;
    }

    @Override
    public int countVarToBeExp(WitnessEnv env) {
        return 0;
    }

    @Override
    public List<Map.Entry<WitnessVar, WitnessAssertion>> varNormalization_separation(WitnessEnv env, WitnessVarManager varManager) throws WitnessException, REException {
        List<Map.Entry<WitnessVar, WitnessAssertion>> newDefinitions = new LinkedList<>();

        for(WitnessPattReq el : reqList){
            newDefinitions.addAll(el.varNormalization_separation(env, varManager));
        }

        return newDefinitions;
    }

    @Override
    public WitnessAssertion varNormalization_expansion(WitnessEnv env) throws WitnessException {
        WitnessOrPattReq newOrp = new WitnessOrPattReq();

        for(WitnessPattReq el : reqList){
            newOrp.add(env.pattReqManager.build(el.getPattern(), el.getValue().varNormalization_expansion(env)));
        }

        return newOrp;
    }

    @Override
    public WitnessAssertion DNF() throws WitnessException {
        return this;
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
        for(WitnessPattReq el : reqList){
            if(el.isRecursive(env, visitedVar)) return true;
        }

        return false;
    }

    @Override
    public WitnessVar buildOBDD(WitnessEnv env, WitnessVarManager varManager) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getReport(ReportResults reportResults) {
        for(WitnessPattReq req : reqList)
            req.getReport(reportResults);
    }

    @Override
    public List<WitnessVar> uses() {
        HashSet<WitnessVar> results = new HashSet<>();
        for(WitnessPattReq pattReq: reqList)
                results.addAll(pattReq.uses());

        return new ArrayList(results);
//        return reqList.stream()
//                .map(wpr -> wpr.uses())
//                .flatMap(List::stream).collect(Collectors.toList());
    }

    public void setReqList(List<WitnessPattReq> reqList) {
        this.reqList = reqList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WitnessOrPattReq that = (WitnessOrPattReq) o;

        List<WitnessPattReq> check = new LinkedList<>(that.reqList);
        check.removeAll(this.reqList);

        return check.size() == 0;
    }

    @Override
    public String toString() {
        return "OrPattReq{" +
                "reqList=" + reqList +
                '}';
    }

    @Override
    public int hashCode() {
        return reqList != null ? reqList.hashCode() : 0;
    }
}
