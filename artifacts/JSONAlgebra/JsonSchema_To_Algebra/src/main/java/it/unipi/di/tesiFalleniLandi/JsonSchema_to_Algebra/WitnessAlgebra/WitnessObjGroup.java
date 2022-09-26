package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import de.uni_passau.sds.patterns.REException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessChoice.newBiSingletonChoice;

public class WitnessObjGroup extends WitnessAnd {
    private static Logger logger = LogManager.getLogger(WitnessObjGroup.class);

    //flag useful to avoid re-preparation - works for object and array groups
    private Boolean prepared; //null by default since relevant only for specific And expressions
    private List<WitnessChoice> choices;
    //private List<WitnessPattReq> RPart;
    //private List<WitnessProperty> CPart;

    public List<WitnessChoice> getChoices() {return choices; }
    //public List<WitnessPattReq> getRPart() {return RPart; }
    //public List<WitnessProperty> getCPart() {return CPart; }
    public boolean isPrepared(){
        if(prepared) return prepared;
        else return false;
    }
    public void setPrepared(){prepared = true;}

    public WitnessObjGroup(WitnessAnd assertion) {
        super();
        add(assertion);
        choices = new LinkedList<>();
        //CPart = computeCPart();
        //RPart = computeRPart();
        prepared = false;
    }


    public List<WitnessPattReq> computeRPart()
    {
        if (andList.containsKey(WitnessPattReq.class)) {
            List<WitnessPattReq> assertions = new LinkedList<>();
            for (WitnessAssertion assertion : andList.get(WitnessPattReq.class)) {
                assertions.add((WitnessPattReq) assertion);
            }
            return assertions;
        } else {
            return new LinkedList<>();
        }
    }

    public List<WitnessAssertion> getCPart()
    {
        if (andList.containsKey(WitnessProperty.class)) {
            List<WitnessAssertion> assertions = andList.get(WitnessProperty.class);
            return assertions;
        }
        else
            return new LinkedList<>();
    }

    public List<WitnessAssertion> getRPart()
    {
        if (andList.containsKey(WitnessProperty.class)) {
            List<WitnessAssertion> assertions = andList.get(WitnessPattReq.class);
            return assertions;
        }
        else
            return new LinkedList<>();
    }

    public void assertElse(Boolean condition,String msg) throws WitnessException {
        if (!condition) throw new WitnessException(msg);
    }

    public void checkChoiceInvariants() throws WitnessException {
        for (WitnessChoice choice : choices) {
            for (WitnessPattReq choiceReq : choice.getReqList())
                assertElse(getRPart().contains(choiceReq), "Choice List contains unexpected PattReq");
            assertElse(getCPart().contains(choice.getConstraint()), "Choice List contains unexpected WitnessProperty");
        }
    }

    //invariant: must have object type
    public void checkTypeInvariants() throws WitnessException {
        //checking if it's an object group
        if (andList.get(WitnessType.class) == null) { //and without type specified
            throw new WitnessException("WitnessTypedAnd without type specified");
        }
        if (andList.get(WitnessType.class) != null
                && !andList.get(WitnessType.class).contains(new WitnessType(AlgebraStrings.TYPE_OBJECT))) { //if is not an object type
            throw new WitnessException("WitnessTypedAnd without type object assertion");
        }
        if (andList.get(WitnessType.class).size() > 1) {//if contains more than one type
            throw new WitnessException("WitnessTypedAnd with more than one type specified");
        }
    }

    public void checkInvariants() throws WitnessException {
        checkTypeInvariants();
        checkChoiceInvariants();
    }


      /*  @Override
    public List<Map.Entry<WitnessVar, WitnessAssertion>> objectPrepare(WitnessEnv env) throws REException, WitnessException {
        return super.objectPrepare(env);
    }*/



    /**
     * Assuming that before was executed and-merging, groupize, separation, expansione, dnf
     *
     */
    public List<Map.Entry<WitnessVar, WitnessAssertion>> objectPrepare(WitnessEnv env) throws REException, WitnessException {
        //checking if it's an object group

        List<WitnessAssertion> CPart = andList.remove(WitnessProperty.class); //List<WitnessProperty>

        //complete and splitCPart - observe that this modified the value of the WitnessAnd
        if (CPart == null || CPart.isEmpty()) {//vuota
            logger.debug("Complete empty CPart");
            CPart = new LinkedList<>();
            CPart.add(new WitnessProperty(ComplexPattern.createFromRegexp(".*"), new WitnessBoolean(true)));
            this.add(CPart.get(0));
        } else {
            logger.debug("Completing non-empty CPart");
            ComplexPattern p = ((WitnessProperty) CPart.get(0)).getPattern();

            for (int i = 1; i < CPart.size(); i++)
                p = p.union(((WitnessProperty) CPart.get(i)).getPattern());

            p = p.complement();
            if (!p.isEmptyDomain())
                CPart.add(new WitnessProperty(p, new WitnessBoolean(true)));

            //splitCPart
            CPart = splitClist(CPart);

            logger.debug("Adding completed property in CPart with key: {}", p.toString());

            for (WitnessAssertion tmp : CPart)
                this.add(tmp);
        }

        //Combine CP-RP - here we are not modifying the WitnessAnd but we are creating the
        //Choices
        //compReq maps each element of the CP to the all related Choices
        HashMap<WitnessAssertion, List<WitnessChoice>> compReqs = new HashMap<>();

        List<WitnessAssertion> RPart = getRPart();

        for (WitnessAssertion c_assertion : CPart)
            compReqs.put(c_assertion, new LinkedList<>());

        // now we create all singleton choices C({C},{R},{R}) - their pattern is not computed
        // wrt the entire set of requests, but only with respect to R
        for (WitnessAssertion R_assertion : RPart) { //WitnessOrPattReq
            //List<WitnessChoice> newChoiceList = new LinkedList<>();
            WitnessPattReq r_assertion = (WitnessPattReq) R_assertion;
            for (WitnessAssertion C_assertion : CPart) {
                WitnessProperty c_assertion = (WitnessProperty) C_assertion;
                logger.debug("[CP-RP] Computing intersection between {} and {}", c_assertion.getPattern(), r_assertion.getPattern());
                ComplexPattern pattInt = c_assertion.getPattern().intersect(r_assertion.getPattern());
                if (!pattInt.isEmptyDomain()) {
                    logger.debug("[CP-RP] Creating new WitnessAnd");
                    WitnessAnd newAnd = new WitnessAnd();
                    logger.debug("[CP-RP] Adding {} to {}", c_assertion.getValue(), newAnd);
                    newAnd.add(c_assertion.getValue());
                    logger.debug("[CP-RP] Adding {} to {}", (r_assertion).getValue(), newAnd);
                    newAnd.add((r_assertion).getValue());
                    if (newAnd.notObviouslyEmpty()) {
                        //WitnessPattReq newReq = env.pattReqManager.build(pattInt, newAnd);
                        WitnessChoice biSingletonChoice = newBiSingletonChoice(c_assertion,r_assertion,pattInt,newAnd);
                        //newChoiceList.add(biSingletonChoice);
                        compReqs.get(C_assertion).add(biSingletonChoice); 
                    }
                }
            }
        }

        List<Map.Entry<WitnessVar, WitnessAssertion>> newDefinitions = new LinkedList<>();

        // GG: sicuri che le variabili siano state aggiunte per la normalizzazione?
        //TODO why not rerun locally? do we really need to run varNormalization_separation twice?
        newDefinitions.addAll(env.varNormalization_separation(env, env.variableNamingSystem));

        //newDefinition contiene variabili rinoninate che dovrebbero essere rimosse
        //se le rimuoviamo cosi non vengono espanse dopo
        //no more necessary
        //newDefinitions = new LinkedList<>(); //reset the newDefinitions list

        if (!RPart.isEmpty())
            splitOriginalRList(compReqs, env);

        //newDefinitions.addAll(this.varNormalization_separation(env, env.variableNamingSystem));
        //env.buildOBDD_notElimination();

        newDefinitions.addAll(env.varNormalization_separation(env, env.variableNamingSystem));

        return newDefinitions;
    }

    public boolean notObviouslyEmpty(){
    /*    WitnessAssertion unitary = this.getIfUnitaryAnd();
        if (unitary != null) {
            if (unitary.getClass() == WitnessVar.class
                && ((WitnessVar) unitary).getName() == "OBDD_false")
                return false;
            else
                return true;
        }
        else */
        return true; //TODO: do something better
    }

    /**
     * Returns a CP list that is equivalent to list but where all different
     * de.uni_passau.sds.patterns are disjoint
     */
    private List<WitnessAssertion> splitClist(List<WitnessAssertion> list) //throws WitnessException
     {
        //RECURSIVE
        if(list.isEmpty()) return list;
        List<WitnessAssertion> expandedTail = splitClist(list.subList(1, list.size()));
        Map.Entry<WitnessProperty, LinkedList<WitnessAssertion>> subHead_reducedExpandedTail = reduceElemWithList((WitnessProperty) list.get(0), expandedTail);
        WitnessProperty subHead = subHead_reducedExpandedTail.getKey();
        LinkedList<WitnessAssertion> reducedExpandedTail = subHead_reducedExpandedTail.getValue();

        if(!subHead.getPattern().isEmptyDomain())
            reducedExpandedTail.addFirst(subHead);

        return reducedExpandedTail;

    }

    /**
     * given prop and list,
     * it returns a pair subProp, reducedList with the following properties:
     * - dom(subProp) = dom(prop) - dom(list) where dom(X) are the names
     *                  matched by the de.uni_passau.sds.patterns in X
     * - reducedList is obtained by splitting all elements 'elem' of list that
     *   intersect with prop into a pair : ( (elem and prop) ; (elem minus prop) ),
     *   where the schema of (elem and prop) is (elem.schema and prop.schema)
     */
    private Map.Entry<WitnessProperty, LinkedList<WitnessAssertion>> reduceElemWithList(WitnessProperty prop, List<WitnessAssertion> list) {
        if(list.isEmpty())
            return new AbstractMap.SimpleEntry<>(prop, new LinkedList<>());

        Map.Entry<WitnessProperty, LinkedList<WitnessAssertion>> subProp_reducedTail = reduceElemWithList(prop, list.subList(1, list.size()));
        WitnessProperty subProp = subProp_reducedTail.getKey();
        LinkedList<WitnessAssertion> reducedTail= subProp_reducedTail.getValue();

        logger.debug("Reducing subProperty {} with reduceTail {} >", subProp, reducedTail);

        WitnessProperty head = (WitnessProperty) list.get(0);

        ComplexPattern intersection = subProp.getPattern().intersect(head.getPattern());
        if(intersection.isEmptyDomain()) {
            reducedTail.addFirst(head); //TODO: bisogna clonare? a regola no...
            return new AbstractMap.SimpleEntry<>(subProp, reducedTail);
        }

        ComplexPattern propMinHead = subProp.getPattern().minus(head.getPattern());
        ComplexPattern headMinProp = head.getPattern().minus(subProp.getPattern());

        WitnessProperty newSubProp = new WitnessProperty(propMinHead, subProp.getValue());

        WitnessAnd and = new WitnessAnd();
        and.add(subProp.getValue());
        and.add(head.getValue());
        LinkedList<WitnessAssertion> newReducedList = new LinkedList<>(reducedTail); //non penso vada clonata
        newReducedList.addFirst(new WitnessProperty(intersection, and));

        if(!headMinProp.isEmptyDomain()){
            newReducedList.add(new WitnessProperty(headMinProp, head.getValue()));
        }

        return new AbstractMap.SimpleEntry<>(newSubProp, newReducedList);
    }

    // this is used when both oldPReqs and newPReqs are singleton pairs
    // <pattern1,{WPR1}> and <pattern2,{WPR2}> and the equals method recognizes
    // WPR1 and WPR 2 are the same. In this case we may merge them in the original
    // list
    // private void mergePReqs(WitnessChoice oldPReqs, WitnessChoice newPReqs) {
    //
    // }

    /*
       In order to split the RList, we first transform each Request (p:A) into a pair
       p, {(p:A)}. When we generate an intersection pattern, we keep track of all the
       original requests that are its ancestors.
       Then, the function rewriteWitnessChoice, will explode each pair (p, {r1,...,rn})
       into all the different 2^n subcases
    */
    private void splitOriginalRList(Map<WitnessAssertion, List<WitnessChoice>> compReqs, WitnessEnv env)
            throws WitnessException, REException {
        // a WitnessChoice is a pair < pattern, set of WitnessPatternReq >
        // we now transform the original RList into a list "ReqList" of WitnessChoice
        List<WitnessChoice> result = new LinkedList<>();

        for (WitnessAssertion constraint : getCPart()) {
            List<WitnessChoice> bsChoices = compReqs.get(constraint);
            result.addAll(splitRList(bsChoices));
        }
        this.choices = expandChoiceList(result, env);
        this.prepared = true;
    }

    /*
       Splits the elements of "list"   and returns the result of this operation.
    */
    private List<WitnessChoice> splitRList(List<WitnessChoice> list) throws WitnessException {
        if(list.isEmpty()) return list;

        List<WitnessChoice> expandedTail = splitRList(list.subList(1, list.size()) );

        // reduceReqWithList returns a pair <subHead, reducedExpandedTail> where subHead is what remains of list.get(0) after
        // it has been reduced against all elements of the tail; here we assign the two elements of the pair to
        // subHead and to reducedExpandedTail
        Map.Entry<WitnessChoice, List<WitnessChoice>> subHead_reducedExpandedTail = reduceListWithChoice(list.get(0), expandedTail);
        WitnessChoice subHead = subHead_reducedExpandedTail.getKey();
        List<WitnessChoice> reducedExpandedTail = subHead_reducedExpandedTail.getValue();

        if(subHead.getPattern().isEmptyDomain()) return reducedExpandedTail;
        else{
            reducedExpandedTail.add(subHead);
            return reducedExpandedTail;
        }
    }

    /*
      Returns a pair (WitnessChoice,NewList) obtained by reducing list, that is a list of full choicess, against
      a singleton choice.
      Reducing "head" with "choice" means that the domain of "choice" is enriched with the request of "head"
      and hence head is split into "head and choice" and "head minus choice",
      The pattern of choice could be reduced to what remains (choice-head), but we do not employ here this optimization.
      Reduction only combines two elements head <c1><r1,...,rn> and <c,r> if c=c1, since, when c!=c1, then the
      intersection between r and r1/\.../\rn is guaranteed to be empty
     */
    private Map.Entry<WitnessChoice, List<WitnessChoice>> reduceListWithChoice(WitnessChoice choice, List<WitnessChoice> list) throws WitnessException {
        if (list.isEmpty())
            return new AbstractMap.SimpleEntry<>(choice, new LinkedList<>());

        Map.Entry<WitnessChoice, List<WitnessChoice>> rc_reducedTail = reduceListWithChoice(choice, list.subList(1, list.size()));

        WitnessChoice reducedChoice = rc_reducedTail.getKey();
        List<WitnessChoice> reducedTail = rc_reducedTail.getValue();

        logger.debug("Reduced choice: {}; reduced tail: {}", reducedChoice, reducedTail);

        WitnessChoice head = list.get(0);
        ComplexPattern reducedChoicePattern = reducedChoice.getPattern();

        /* if dom(reducedChoice) is already empty there is no reduction left to perform */
        if (!compatible(reducedChoice, head) || reducedChoicePattern.isEmptyDomain()) {
            logger.debug("reducedChoice {} is not compatible with head {}", reducedChoice, head);
            head.addToReqFullSet(reducedChoice.getReqFullSet());
            reducedTail.add(head);
            reducedChoice.addToReqFullSet(head.getReqFullSet());
            return new AbstractMap.SimpleEntry<>(reducedChoice, reducedTail);
        }

        ComplexPattern intersection = reducedChoicePattern.intersect(head.getPattern());

        if (intersection.isEmptyDomain()) {
            head.addToReqFullSet(reducedChoice.getReqFullSet());
            reducedTail.add(head);
            return new AbstractMap.SimpleEntry<>(reducedChoice, reducedTail);
        }

        /* if we arrive here, then reducedChoice is compatible with head and their pattern-intersection is
         * not empty, and we add an "intersectReq" to the reduced tail */
        /* why is unionList a list? would a set be a better choice? */
        List<WitnessPattReq> reqUnion = new LinkedList<>(head.getReqList());
        reqUnion.addAll(reducedChoice.getReqList());
        List<WitnessPattReq> fullReqUnion = new LinkedList<>(head.getReqFullSet());
        fullReqUnion.addAll(reducedChoice.getReqFullSet());
        WitnessChoice intersectChoice = new WitnessChoice(head.getConstraint(),reqUnion,reqUnion,fullReqUnion,intersection);
        LinkedList<WitnessChoice> newReducedList = new LinkedList<>(reducedTail); //is this cloning useful or necessary??
        newReducedList.add(intersectChoice);

        /* newReducedChoice = <choicePattern-head.Pattern, reducedChoice.list> is what remains of choice after reduction*/
        ComplexPattern choiceMinusHead = reducedChoicePattern.minus(head.getPattern());
        WitnessChoice newReducedChoice = new WitnessChoice(reducedChoice.getConstraint(),
                                                    reducedChoice.getReqList(),reducedChoice.getSubList(),fullReqUnion,choiceMinusHead);

        /* finally, we also generate headMinusChoice, in case it is not empty */
        ComplexPattern headMinusChoice = head.getPattern().minus(reducedChoicePattern);
        if (!headMinusChoice.isEmptyDomain()) {
            WitnessChoice reducedHead = new WitnessChoice(head.getConstraint(),
                    head.getReqList(),head.getSubList(),fullReqUnion,headMinusChoice);
            newReducedList.add(reducedHead); // <right minus left , rightSchema> goes in the list
        }

        // here we remove all the other colist entries that talk about "head" and "reducedChoice"

        return new AbstractMap.SimpleEntry<>(newReducedChoice, newReducedList);
    }

    private boolean compatible(WitnessChoice r1, WitnessChoice r2){
        return (r1.getConstraint()==r2.getConstraint());
    }

    /*private List<WitnessChoice> compWith(List<Map.Entry<WitnessChoice, WitnessChoice>> coList, WitnessChoice req){
        List<WitnessChoice> returnList = new LinkedList<>();

        for(Map.Entry<WitnessChoice, WitnessChoice> entry : coList)
            if(entry.getKey().equals(req)) returnList.add(entry.getValue());
            else if(entry.getValue().equals(req)) returnList.add(entry.getKey());

        logger.debug("CompableWith list of WitnessChoice {} is {}", req, returnList);

        return returnList;
    }*/

    /*private List<WitnessChoice> intersect(List<WitnessChoice> l1, List<WitnessChoice> l2){
        List<WitnessChoice> returnPattReqs = new LinkedList<>();

        logger.debug("Computing WitnessChoice intersection between {} and {}", l1, l2);

        for(WitnessChoice pr1 : l1)
            for(WitnessChoice pr2 : l2)
                if(pr1.equals(pr2))
                    returnPattReqs.add(pr1);

        logger.debug("WitnessChoice intersection result: {}", returnPattReqs);

        return returnPattReqs;
    }

    private void addPairs(List<Map.Entry<WitnessChoice, WitnessChoice>> coList, WitnessChoice req, List<WitnessChoice> reqList){
        for(WitnessChoice tmp : reqList){
            logger.debug("Adding to coList the entry < {} ; {} >",  req, tmp);
            coList.add(new AbstractMap.SimpleEntry<>(req, tmp));
        }
    }*/

    // apply rewriteWitnessChoice to all pairs patt-list in list
    // then, for every oldPatternRequest oldReq invoke oldReq.deconnectAll
    // beware, this version does not work because deconnect invokes remove which removes too much
    // we need to move to a different version that does not update the ORPs of this in place, but that creates
    // fresh new ORPs and updates the ORP list of this. Of course we may have a memory accumulation problem, since
    // the patternBuilder keeps a pointer (is that weak?) to the existing ORPs
    /*private void rewriteWitnessChoiceList (List<WitnessChoice> list, WitnessEnv env) throws WitnessException, REException {
        List<WitnessAssertion> oldOrPattRequests = this.andList.get(WitnessOrPattReq.class); //getAllOldRequests

        logger.debug("Old ORP size: ", oldOrPattRequests.size());

        Set<WitnessPattReq> oldPattRequests = new HashSet<>();

        for(WitnessAssertion pr : oldOrPattRequests)
            oldPattRequests.addAll(((WitnessOrPattReq) pr).reqList);

        logger.debug("Old Pattern Requests size:", oldPattRequests.size());

        // after the operation below, every fresh PatternRequests refers to all its ORPs
        // and every ORP refers to both the oldPR and to the fresh PR
        for (WitnessChoice pReqs : list ) {
            rewriteWitnessChoice(pReqs, env);
        }

        // here we deconnect the oldPatternRequests from the ORPs and the
        // ORPs from the OldPatternRequests
        for (WitnessAssertion oldReq : oldPattRequests ) {
            WitnessPattReq oldReq_ = (WitnessPattReq) oldReq;
            oldReq_.fullDeConnectAll(new LinkedList<>(oldReq_.getOrpList()));
        }
    }*/

    // for every pair < pattern, list of requests > and for any non-empty subset "subset" of "List of requests"
    // we create a separate request such that
    // (1) its schema is the conjunction of that of all requests in subset
    // (2) it belongs to the union of all the ORPs of all the requests in subset
    public List<WitnessChoice> expandChoiceList(List<WitnessChoice> fullChoices, WitnessEnv env) throws  WitnessException, REException {
        List<WitnessChoice> result = new LinkedList<>();
        for (WitnessChoice fullChoice : fullChoices) {
            logger.debug("Rewriting WitnessChoice {}", fullChoices);

            //ComplexPattern patt = fullChoice.getPattern();
            List<WitnessPattReq> reqList = fullChoice.getReqList();

            logger.debug("RequestList size: {}", reqList.size());

            assertElse(reqList.size() != 0, "impossibile: reqList.size() == 0");

            if (reqList.size() == 1) {
                result.add(fullChoice);
            } else {
                /* non-trivial set */
                for (List<WitnessPattReq> subset : subsetsOf(new LinkedList<>(reqList))) { //subset: we create here a fragment that
                    //compute schema as intersection of all schemas in subset
                    WitnessAnd schema = new WitnessAnd();
                    for (WitnessPattReq req : subset)
                        schema.add(req.getValue());//  schema = schema and schemaOf(oldReq)
                    //for (WitnessPattReq oldReq : coSubset )
                    //   ((WitnessAnd) schema).add(oldReq.getValue().not(env)); //  schema = schema and not schemaOf(oldReq)
                    WitnessChoice subChoice = new WitnessChoice(fullChoice.getConstraint(),
                            fullChoice.getReqList(),
                            subset,
                            fullChoice.getReqFullSet(),
                            fullChoice.getPattern()
                    );
                    subChoice.setSchema(schema);
                    result.add(subChoice);
                }
            }
        }
        return result;
    }

    /* we assume the following invariant: every two fragments that belonged to the same ORP before simplification
    * do not need to be combined - this is a bit risky
    we do not do any other compatibility checks
     */
    List<List<WitnessPattReq>> subsetsOf(List<WitnessPattReq> reqList) {
        if (reqList.size() == 1) { return mkSingleton(reqList); } // receives { r } and return {{r}}
        List<WitnessPattReq> reqListCopy = new LinkedList<>(reqList);
        WitnessPattReq head = reqListCopy.remove(0);
        List<List<WitnessPattReq>> subsetsNoHead = subsetsOf(reqListCopy); // we compute the comp. non-empty subsets of the reqList with no head
        List<List<WitnessPattReq>> returnList = new LinkedList<>(subsetsNoHead);

        for (List<WitnessPattReq> subsetNoHead : subsetsNoHead) {
                List<WitnessPattReq> subNoHead_clone = new LinkedList<>(subsetNoHead);
                subNoHead_clone.add(head);
                returnList.add(subNoHead_clone);
        }
        returnList.add(mkSingleton(head)); // subsetsNoHead does not contain the empty set, hence we must add this singleton
        return returnList;
    }

    //TODO: hashCode
    @Override
    public int hashCode() {
        return andList != null ? andList.hashCode() : 0;
    }

    private static <T> LinkedList<T> mkSingleton(T elem){
        LinkedList<T> list = new LinkedList<>();
        list.add(elem);
        return list;
    }

    // TODO: equality
    @Override
    public boolean equals(Object o) {
        boolean b = true;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WitnessAnd witnessAnd = (WitnessAnd) o;

        if(witnessAnd.andList.size() != andList.size())
            return false;

        for(Map.Entry<Object, List<WitnessAssertion>> entry : witnessAnd.andList.entrySet()){
            if(!this.andList.containsKey(entry.getKey())) return false;
            List<WitnessAssertion> check;

            if(witnessAnd.andList.get(entry.getKey()).size() >= this.andList.get(entry.getKey()).size()) {
                check = new LinkedList<>(witnessAnd.andList.get(entry.getKey()));
                check.removeAll(this.andList.get(entry.getKey()));
            }else{
                check = new LinkedList<>(this.andList.get(entry.getKey()));
                check.removeAll(witnessAnd.andList.get(entry.getKey()));
            }

            b &= check.size() == 0;
        }

        return b;
    }

//@Override
    //public List<Map.Entry<WitnessVar, WitnessAssertion>> arrayPreparation(WitnessEnv env) throws WitnessException, REException {
      //  return super.arrayPreparation(env);
    //}
}
