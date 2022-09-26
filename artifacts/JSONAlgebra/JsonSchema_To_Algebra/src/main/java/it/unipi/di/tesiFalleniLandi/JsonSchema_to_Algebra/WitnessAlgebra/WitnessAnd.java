package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.Utils;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.*;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.*;
//import java.util.stream.Collectors;

import static it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessChoice.newBiSingletonChoice;
import static java.util.stream.Collectors.toList;

public class WitnessAnd implements WitnessAssertion{

    private static Logger logger = LogManager.getLogger(WitnessAnd.class);

    /**
     * list of assertion in and, grouped by class to optimize the merge method
     *
     * Ex:
     *      < WitnessMof, <mof(1), mof(2), mof(3)>>
     *      < WitnessPattern, <pattern("..."), pattern("..."), ...>>
     *      ...
     */
    protected LinkedHashMap<Object, List<WitnessAssertion>> andList;
    private boolean hasFalse; // flag that indicates if the AND contains false (we check this instead of searching into andList)
    //flag useful to avoid re-preparation - works for object and array groups
    private boolean prepared = false; //relevant only for specific And expressions
    private List<WitnessChoice> choices;
    //private List<WitnessPattReq> RPart;
    //private List<WitnessProperty> CPart;

    public LinkedHashMap<Object, List<WitnessAssertion>> getAndList() {
        return andList;
    }

    public List<WitnessChoice> getChoices() {return choices; }
    //public List<WitnessPattReq> getRPart() {return RPart; }
    //public List<WitnessProperty> getCPart() {return CPart; }


    public void setChoices(List<WitnessChoice> choices) {
        this.choices = choices;
    }

    public boolean isPrepared(){
        return (prepared);
    }
    public void setPrepared(){prepared = true;}

    //public void unSetPrepared(){prepared = false;}


    public List<WitnessPattReq> oldComputeRPart()
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
        if (andList.containsKey(WitnessPattReq.class)) {
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
    public void checkObjectTypeInvariants() throws WitnessException {
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

    public void checkObjectInvariants() throws WitnessException {
        checkObjectTypeInvariants();
        checkChoiceInvariants();
    }



    /**
     * used for clearing the list in presence of false
     */
    public void clear(){
        this.andList.clear();
    }

    public WitnessAnd() {
        this.andList = new LinkedHashMap<>();

        //base case AND TODO commented to test whether useless to add a boolean which will be removed by calling add()
        List<WitnessAssertion> list = new LinkedList<>();
        list.add(new WitnessBoolean(true));
        this.andList.put(WitnessBoolean.class, list);
        hasFalse = false;
        logger.trace("Created a new and: {}", this);
    }

    /**
     * modifies the andList so that it now is logically equivalent to andList And el
     * specifically, if andList and el are not compatible, it may transform the AndList in <Bool,<False>>
     * it returns false iff, and only iff, the AndList has not been modified since it already implied "el"
     * if the andList was groupized, it will not be un-groupized
     * if this is blocked we do not add el
     * if el is boolean true we do not add el
     * if el is an instance of WitnessAnd, we add every element of el.andList in this.andList
     * if el is an instance of uniqueItems or repeatedItems, we check if andList do not contain it, then we add it
     * if el is an instance of Type, its list is intersected
     * @param el element to add
     * @return true if the collection has changed, false otherwise
     */
    public boolean add(WitnessAssertion el) {
        // the recursive calls come before any side effect to this
        if (isPrepared())
            throw new RuntimeException("Modifying prepared object {}");
        if(el instanceof WitnessOr && ((WitnessOr) el).getIfUnitaryOr() != null)
            return add(((WitnessOr) el).getIfUnitaryOr());

        if(el instanceof WitnessAnd) { //We flatten the and with a distinct code for add
            return add((WitnessAnd) el);
        }

        if(hasFalse)
            return false; //if hasFalse then we do not add any other assertion

        if(el instanceof WitnessBoolean) { //add boolean
            boolean wb = ((WitnessBoolean) el).getValue();
            if (wb==true) //if el==true then do nothing
                return false;
            else {
                andList = new LinkedHashMap<>(); //if false, remove all and set hasFalse to true
                hasFalse = true;
                List<WitnessAssertion> list = new LinkedList<>();
                list.add(el);
                andList.put(WitnessBoolean.class, list);
                return true;
            }

        }

        //TODO assertion that we remove bool(true)
        if(andList.get(WitnessBoolean.class) != null)
              andList.remove(WitnessBoolean.class); //if we reach this, the boolean inside this is the boolean that we add with the constructor.
                                                    //we can remove it because we are going to add a new element (we "absorb" it)
        // I do not understand this!!
        /*
        if(andList.containsKey(WitnessBoolean.class)){
            //reduce WitnessBoolean and propagate
            List<WitnessAssertion> assertionList = andList.remove(WitnessBoolean.class);
            List<Boolean> booleans = assertionList.stream().map(a->(WitnessBoolean)a)
                    .map(witnessBoolean -> witnessBoolean.getValue())
                    .collect(Collectors.toList());

            if(conjunct(booleans)==false){
                andList.clear();
                List<WitnessAssertion> list = new ArrayList<>();
                list.add(new WitnessBoolean(false));
                andList.put(WitnessBoolean.class, list);
            }
        }*/


        //Optimization: add uniqueItems or repeatedItems only once
        /*if(el.getClass() == WitnessUniqueItems.class || el.getClass() == WitnessRepeateditems.class)
            if(!andList.containsKey(el.getClass())){
                List<WitnessAssertion> list = new LinkedList<>();
                list.add(el);
                andList.put(el.getClass(), list);
                return true;
            } else
                return false;*/

        //Add type
        if(el instanceof WitnessType){
            if(andList.get(WitnessType.class) == null){ //if no type present
                List<WitnessAssertion> list = new LinkedList<>();
                list.add(el);
                andList.put(el.getClass(), list);
                return true;
            }

            //Optimization: try to merge two type
            List<WitnessAssertion> typeList = andList.remove(WitnessType.class);
            WitnessAssertion merged = ((WitnessType) el).mergeElement((WitnessType) typeList.get(0));

            if(merged instanceof WitnessBoolean && ((WitnessBoolean)merged).getValue() == false) //incompatible types
                return add(new WitnessBoolean(false));
            else{ //compatible types
                List<WitnessAssertion> list = new LinkedList<>();
                list.add(merged);
                andList.put(el.getClass(), list);
                return true;
            }
        }

        //old version
        /*if(andList.containsKey(el.getClass())) { //if andList already contains the key
            if (el instanceof WitnessVar && andList.get(WitnessVar.class).contains(el))
                return true;
            andList.get(el.getClass()).add(el); //insert the assertion in the right list
        }*/
        if(andList.containsKey(el.getClass())) { //if orList already contains the key
            if (andList.get(el.getClass()).contains(el))
                return false;
            else {
                andList.get(el.getClass()).add(el); //insert the assertion in the respective list
                return true;
            }
        } else {
            List<WitnessAssertion> list = new LinkedList<>();
            list.add(el);
            andList.put(el.getClass(), list);
            return true;
        }
    }

    public boolean add(WitnessAnd and) {
        if (isPrepared())
            throw new RuntimeException("modifying prepared object {}");
        boolean b = false;

        for (Map.Entry<Object, List<WitnessAssertion>> entry : and.andList.entrySet())
            for (WitnessAssertion assertion : entry.getValue())
                b |= this.add(assertion);

        return b;
    }

    @Override
    public String toString() {
        LinkedList<WitnessAssertion> assList = new LinkedList<>();
        for(Map.Entry<?, List<WitnessAssertion>> ass : andList.entrySet())
            assList.addAll(ass.getValue());

        return "And{" + "\r\n" +
                assList.toString()
                 + ";\r\n" +
                (choices!=null? choices.toString() : "" )
                + ";\r\n" +
                '}';
    }


    @Override
    public void checkLoopRef(WitnessEnv env, Collection<WitnessVar> varList) throws RuntimeException {
        for(Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet())
            for(WitnessAssertion assertion : entry.getValue())
                assertion.checkLoopRef(env, varList);
    }

    // should be also consider refs reachable from choice lists?
    @Override
    public void reachableRefs(Set<WitnessVar> collectedVar, WitnessEnv env) throws RuntimeException {
        for(Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet())
            for(WitnessAssertion assertion : entry.getValue())
                assertion.reachableRefs(collectedVar, env);
        if (choices!=null)
            for(WitnessChoice choice : choices)
                choice.reachableRefs(collectedVar, env);
    }

    @Override
    public WitnessAssertion mergeWith(WitnessAssertion a, WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException {
        logger.trace("Merging {} with {}", a, this);

        if(this.add(a)) //if the list has been modified then apply merge again TODO: can we optimize that?
            return this.merge(varManager, pattReqManager);

        return this;
    }


    /**
     * conjuncts a list of booleans to
     * @param booleans
     * @return
     */
    private boolean conjunct(List<Boolean> booleans){
        Optional<Boolean> res = null;
        res = booleans.stream().reduce((a, b) -> a && b);
        if (res.isPresent())
            return res.get();
        else
            return true;
    }

    //not exhaustive merge
    @Override
    public WitnessAssertion merge(WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException {
        if (isPrepared()) {

            for (WitnessChoice choice : choices)
                choice.schema.merge(varManager, pattReqManager);
            return this;
        } else {
                if (hasFalse) return new WitnessBoolean(false);

                if (getIfUnitaryAnd() != null) return getIfUnitaryAnd();

                WitnessAnd newAnd = new WitnessAnd();
                boolean modified = false;

                //this loop merge only element of the same Class
                for (Map.Entry<Object, List<WitnessAssertion>> sameTypeAssertion : andList.entrySet()) {
                    int size = sameTypeAssertion.getValue().size();
                    WitnessAssertion merged = null;
                    if (size > 0)
                        merged = sameTypeAssertion.getValue().get(0).merge(varManager, pattReqManager);

                    // elements of sameTypeAssertion are divided in two groups: those that can be merged with element 0
                    // end up merged in "merged", the others are inserted into newAnd
                    for (int i = 1; i < size; i++) {
                        WitnessAssertion oldMerge = merged;
                        merged = merged.mergeWith(sameTypeAssertion.getValue().get(i).merge(varManager, pattReqManager), varManager, pattReqManager);

                        if (merged != null) { //element "i" has been merged with "merged"
                            modified = true;
                            if (merged instanceof WitnessBoolean) {
                                WitnessBoolean b = (WitnessBoolean) merged;
                                if (b.getValue() == false) //if the merge result is a false boolean, we return false (AND(..., false, ...) is always false)
                                    return b;
                            }
                        } else { //if the element cannot be merged
                            newAnd.add(sameTypeAssertion.getValue().get(i));
                            merged = oldMerge;
                        }
                    }
                    // finally, we also add "merged" to "newAnd"
                    if (merged != null) { // how could merged be null?
                        newAnd.add(merged);
                    }  // we should add: else { FAIL } otherwise this "if" makes no sense
                }

                //SPECIAL CASES


//        //reduce WitnessBoolean and propagate
//        List<WitnessAssertion> assertionList = newAnd.andList.remove(WitnessBoolean.class);
//        List<Boolean> booleans = assertionList.stream().map(a->(WitnessBoolean)a)
//                                .map(witnessBoolean -> witnessBoolean.getValue())
//                                .collect(Collectors.toList());
//
//        if(conjunct(booleans)==false){
//            newAnd.andList.clear();
//            List<WitnessAssertion> list = new ArrayList<>();
//            list.add(new WitnessBoolean(false));
//            newAnd.andList.put(WitnessBoolean.class, list);
//        }

                //simplify non-singleton Bet and XBet (XBet: TODO)
                //in principle this has already been done by rhe same-class-loop above, since merging the first
                //element of the WitnessBet list with another WitnessBet is always possible, and Yields either
                //false or another WitnessBet
                if (newAnd.andList.containsKey(WitnessBet.class)) {
                    List<WitnessAssertion> witnessList = newAnd.andList.get(WitnessBet.class);
                    List<WitnessBet> witnessBetList = witnessList.stream().map(a -> (WitnessBet) a).collect(toList());
                    //TODO : check the following line, makes no sense to me: you should not "put" the result of mergeBet since
                    //we did not remove the input - luckily, this line is never executed since witnessBetList.size() is always 1
                    if (witnessBetList.size() > 1)
                        newAnd.andList.put(WitnessBet.class, mergeBet(witnessBetList));
                }
                //TODO add the same for XBet

                //merge between bet and xbet
                if (newAnd.andList.containsKey(WitnessBet.class) && newAnd.andList.containsKey(WitnessXBet.class)) {
                    WitnessBet bet = (WitnessBet) newAnd.andList.remove(WitnessBet.class).get(0);
                    WitnessXBet xBet = (WitnessXBet) newAnd.andList.remove(WitnessXBet.class).get(0);
                    WitnessAssertion assertion = bet.mergeElement(xBet, varManager);
                    if (assertion == null) {
                        newAnd.add(bet);
                        newAnd.add(xBet);
                    } else {
                        newAnd.add(assertion);
                        modified = true;

                    }
                }

                //merge between mulOf and notMulOf
                //the result of mergeWith is either null or not(type[Number]) when mof.value and tmp.value are not compatible
                if (newAnd.andList.containsKey(WitnessMof.class) && newAnd.andList.containsKey(WitnessNotMof.class)) {
                    WitnessMof mof = (WitnessMof) newAnd.andList.get(WitnessMof.class).get(0);
                    List<WitnessAssertion> notMofList = newAnd.andList.remove(WitnessNotMof.class);

                    for (WitnessAssertion tmp : notMofList) {
                        WitnessAssertion assertion = mof.mergeWith(tmp, varManager, pattReqManager);
                        if (assertion == null) {
                            newAnd.add(tmp);
                        } else {   //  in this case, assertion is not(type[Number]) - we remove the mofList and we should
                            //  also remove elements re-added to the notMof list by the two lines above
                            newAnd.add(assertion);
                            newAnd.andList.remove(WitnessMof.class);
                            modified = true;
                            break;
                        }
                    }

                }

                //merge between uniqueItems and repeatedItems
                if (newAnd.andList.containsKey(WitnessUniqueItems.class) && newAnd.andList.containsKey(WitnessRepeateditems.class)) {
                    WitnessUniqueItems uni = (WitnessUniqueItems) newAnd.andList.remove(WitnessUniqueItems.class).get(0);
                    WitnessRepeateditems rep = (WitnessRepeateditems) newAnd.andList.remove(WitnessRepeateditems.class).get(0);
                    WitnessAssertion assertion = uni.mergeElement(rep); // it always returns not(type[array])
                    if (assertion == null) {  // dead code
                        newAnd.add(uni);
                        newAnd.add(rep);
                    } else {
                        newAnd.add(assertion);
                        modified = true;
                    }
                }

                //merge items# TODO test
//        if(newAnd.andList.containsKey(WitnessItemsPrepared.class)){
//            //retrieve the list of items#
//            List<WitnessItemsPrepared> witnessItemsPreparedList = newAnd.andList.get(WitnessItemsPrepared.class)
//                    .stream().map(witnessAssertion -> (WitnessItemsPrepared) witnessAssertion).collect(Collectors.toList());
//
//            int wipListSize =  witnessItemsPreparedList.size();
//
//            if(wipListSize>1){
//
//                WitnessItemsPrepared witnessItemsPrepared = new WitnessItemsPrepared();
//
//                for(int i=1; i<wipListSize;i++)
//                    witnessItemsPrepared = (WitnessItemsPrepared)witnessItemsPrepared.mergeWith(witnessItemsPreparedList.get(i), varManager, pattReqManager);
//
//                List<WitnessAssertion> toAdd = new LinkedList<>();
//                toAdd.add(witnessItemsPrepared);
//
//                newAnd.andList.remove(WitnessItemsPrepared.class);
//                newAnd.andList.put(WitnessItemsPrepared.class, toAdd);
//            }
//        }

                //if is unitary and, return only the value
                WitnessAssertion value = getIfUnitaryAnd();
                if (value != null)
                    return value;

                if (modified)
                    return newAnd.merge(varManager, pattReqManager);

                return newAnd;
            }
    }

    /**
     * merges a list of Bet into one Bet constraint
     * @param input
     * @return
     */
    private List<WitnessAssertion> mergeBet(List<WitnessBet> input) {
        WitnessBet tmp, merged = input.get(0);
        for(int i=1;i<input.size();i++)
        {
            tmp = input.get(i);
            merged.setMin(Double.max(merged.getMin(),tmp.getMin()));
            merged.setMax(Double.min(merged.getMax(),tmp.getMax()));
        }
        List<WitnessAssertion> result  = new ArrayList<>();
        result.add(merged);
        return result;
    }

    /**
     * if and contains only one assertion, return that assertion
     * else return null
     */
    public WitnessAssertion getIfUnitaryAnd(){
        if(andList.size() == 0){
            logger.warn("empty WitnessAnd, that should not have happened");
            return new WitnessBoolean(true);
        }
        if(andList.size() == 1) {
            Iterator<Map.Entry<Object, List<WitnessAssertion>>> entry = andList.entrySet().iterator();
            Object key = entry.next().getKey();
            if(andList.get(key).size() == 1)
                return andList.get(key).get(0);
        }

        return null;
    }


    @Override
    public WitnessType getGroupType() {
        return null;
    }

    @Override
    public Assertion getFullAlgebra() {
        if(getIfUnitaryAnd() != null)
            return getIfUnitaryAnd().getFullAlgebra();

        AllOf_Assertion allOf = new AllOf_Assertion();

        for(Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet())
            for(WitnessAssertion assertion : entry.getValue())
                allOf.add(assertion.getFullAlgebra());

        if (choices != null) {
            AnyOf_Assertion anyChoice = new AnyOf_Assertion();
            for (WitnessChoice c : choices) {
                Properties_Assertion pAssertion = new Properties_Assertion();
                pAssertion.addPatternProperties(c.getPattern().complement(), new Boolean_Assertion(false));

                AllOf_Assertion choiceAllAssertion = new AllOf_Assertion();
                // pattern and constraint are both encoded as pattern properties
                choiceAllAssertion.add(pAssertion);
                choiceAllAssertion.add(c.getConstraint().getFullAlgebra());
                // the subList and the schema are both encoded as pattReqs
                for (WitnessPattReq pattReq : c.getSubList())
                    choiceAllAssertion.add(pattReq.getFullAlgebra());

                PatternRequired_Assertion schemaAssertion = new PatternRequired_Assertion();
                schemaAssertion.add (c.getPattern(), c.getSchema().getFullAlgebra());

                choiceAllAssertion.add(schemaAssertion);

                anyChoice.add(choiceAllAssertion);
            }
            allOf.setChoices(anyChoice);
        }

        return allOf;
    }

    @Override
    public WitnessAnd clone() {
        WitnessAnd clone = new WitnessAnd();
        int debug = 0;
        clone.hasFalse = hasFalse;

        for(Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet())
            for(WitnessAssertion assertion : entry.getValue()) {
                clone.add(assertion.clone());
                debug++;
            }

        logger.trace("Cloned WitnessAnd of size {}", debug);

        return clone;
    }

    @Override
    public WitnessAssertion not(WitnessEnv env) throws REException {
        WitnessOr or = new WitnessOr();

        for(Map.Entry<?, List<WitnessAssertion>> entry : andList.entrySet())
            for(WitnessAssertion assertion : entry.getValue())
                or.add(assertion.not(env));

        //if (or.getOrList().isEmpty())
        //    return new WitnessBoolean(false);
        //else
            return or;
    }

    // Returns true if the conjunction is a boolean expression, even if it does not contain a type,
    // or if it is a group: it contains one unitary type, a set of typed assertions
    // of the same type, and a set of boolean expressions and variables
    public boolean isAGroup(){

        if(andList.size() == 1 && andList.containsKey(WitnessBoolean.class))
            return true; // Does not contain a type

        if(isBooleanExp()) //contains only WitnessBoolean/Or/And/Var
            return true;

        List<WitnessAssertion> typeList = andList.get(WitnessType.class);
        if(typeList == null || typeList.size() != 1 || ((WitnessType)typeList.get(0)).separeTypes().size() != 1)
            return false;

        return false;

        /*
        WitnessType groupType = (WitnessType) typeList.get(0);

        for(Map.Entry<?, List<WitnessAssertion>> entry : andList.entrySet())
            if(entry.getKey() == WitnessType.class) continue;
            else
                for(WitnessAssertion assertion : entry.getValue()) {
                    if(assertion.getGroupType() != null && assertion.getGroupType() != groupType)
                        return false;
                    //if(!assertion.isAGroup()) //GG 2/11/2020
                     //   return false;
                }

        return true; // GG 2/11/2020 without a recursive visit we cannot know whether it is groupized
         */
    }

    @Override
    public WitnessAssertion groupize() throws WitnessException, REException {
        //BASE CASES
        if (isPrepared())
            throw new WitnessException("Groupize prepared object");
        if(this.getIfUnitaryAnd() != null && (this.getIfUnitaryAnd() instanceof WitnessVar || this.getIfUnitaryAnd() instanceof WitnessBoolean))
            return this.getIfUnitaryAnd();
        /*
        //WitnessAnd is already a group
        if(isAGroup()) {
            return this;
        }
         */

        //is an empty and (contains only true/false)
        if(andList.size() == 1 && andList.get(WitnessBoolean.class) != null){
            WitnessBoolean b = (WitnessBoolean) andList.get(WitnessBoolean.class).get(0);
            logger.debug("Groupize on an empty WitnessAnd. Returning {}", b);
            return b;
        }

        WitnessAssertion result;

        if (andList.containsKey(WitnessType.class)) {
            List<WitnessAssertion> types = andList.remove(WitnessType.class);
            WitnessAssertion type = types.remove(0);

            for (WitnessAssertion t : types)
                type = type.mergeWith(t, null, null);

            if(type instanceof WitnessBoolean)
                return type;

            //One type
            if (((WitnessType)type).separeTypes().size() == 1)
                result = groupize_oneType((WitnessType) type);

            //More than one type
            else
                result = groupize_multipleTypes((WitnessType) type);

        } else  //No type specified
            result = groupize_noType();


        //TODO: check why we did check var

        /*
        //check var
        if(andList.containsKey(WitnessVar.class)) {
            WitnessAnd and = new WitnessAnd();
            and.add(result);
            for (WitnessAssertion var : andList.get(WitnessVar.class))
                and.add(var);
            result = and;
        }
         */


        logger.debug("Groupize {} from {}", result, this);
        return result;
    }

    @Override
    public Float countVarWithoutBDD(WitnessEnv env, List<WitnessVar> visitedVar) {
        Float count = 0f;

        for (Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet()) {
            for (WitnessAssertion assertion : entry.getValue()) {
                count += assertion.countVarWithoutBDD(env, visitedVar);
            }
        }

        if (choices!=null)
            count += choices.stream().map(c->c.countVarWithoutBDD(env,visitedVar)).reduce(0f,Float::sum);

        return count;
    }

    public WitnessAssertion groupize_oneType(WitnessType type) throws WitnessException, REException {
        WitnessAnd and = new WitnessAnd();
        and.add(type);

        for (Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet()) {
            for (WitnessAssertion assertion : entry.getValue()) {
                if(assertion.getGroupType() != null) {
                    if(assertion.getGroupType().equals(type))
                        and.add(assertion.groupize());
                }else{
                    and.add(assertion.groupize());
                }
            }
        }

        return and;
    }

    public WitnessAssertion groupize_multipleTypes(WitnessType type) throws WitnessException, REException {
        if (isPrepared())
            throw new WitnessException("Groupize prepared object {}");
        WitnessOr or = new WitnessOr();
        WitnessAnd and = new WitnessAnd();

        HashMap<WitnessType, WitnessAnd> groups = new HashMap<>();

        for (WitnessType t : type.separeTypes()) {
            WitnessAnd tmpAnd = new WitnessAnd();
            tmpAnd.add(t);
            groups.put(t, tmpAnd);
        }

        for (Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet()) {
            for (WitnessAssertion assertion : entry.getValue()) {
                if (assertion.getGroupType() != null) {
                    if (groups.containsKey(assertion.getGroupType()))
                        groups.get(assertion.getGroupType()).add(assertion.groupize());
                } else {
                    and.add(assertion.groupize());
                }
            }
        }

        for(Map.Entry<?, WitnessAnd> ands: groups.entrySet()){
            or.add(ands.getValue());
        }

        and.add(or);
        return (and.getIfUnitaryAnd() != null)? and.getIfUnitaryAnd() : and;
    }

    public WitnessAssertion groupize_noType() throws WitnessException, REException {
        if (getIfUnitaryAnd() != null && (getIfUnitaryAnd() instanceof WitnessVar ||
                getIfUnitaryAnd() instanceof WitnessBoolean))
            return getIfUnitaryAnd();

        WitnessAnd and = new WitnessAnd();

        WitnessAnd tmpAnd = new WitnessAnd();
        HashMap<WitnessType, WitnessAnd> groups = new HashMap<>();

        WitnessType type = new WitnessType(AlgebraStrings.TYPE_NUMBER);
        tmpAnd.add(type);
        groups.put(type, tmpAnd);

        tmpAnd = new WitnessAnd();
        type = new WitnessType(AlgebraStrings.TYPE_OBJECT);
        tmpAnd.add(type);
        groups.put(type, tmpAnd);

        tmpAnd = new WitnessAnd();
        type = new WitnessType(AlgebraStrings.TYPE_ARRAY);
        tmpAnd.add(type);
        groups.put(type, tmpAnd);

        tmpAnd = new WitnessAnd();
        type = new WitnessType(AlgebraStrings.TYPE_STRING);
        tmpAnd.add(type);
        groups.put(type, tmpAnd);

        tmpAnd = new WitnessAnd();
        type = new WitnessType(AlgebraStrings.TYPE_BOOLEAN);
        tmpAnd.add(type);
        groups.put(type, tmpAnd);

        tmpAnd = new WitnessAnd();
        type = new WitnessType(AlgebraStrings.TYPE_NULL);
        tmpAnd.add(type);
        groups.put(type, tmpAnd);

        Boolean hasTypedArgument = false;
        // For every assertion in the conjunction, we recursively groupize it and
        // if it is a typed operator we push it into its group in the "groups" disjunction
        // otherwise we leave it at the top level
        for (Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet()) {
            for (WitnessAssertion assertion : entry.getValue()) {
                if (assertion.getGroupType() != null) {
                    hasTypedArgument = true;
                    if (groups.containsKey(assertion.getGroupType()))
                        groups.get(assertion.getGroupType()).add(assertion.groupize());
                } else {
                    and.add(assertion.groupize());
                }
            }
        }

        // if the and operator was purely top-level booleans and vars, then the next step is
        // useless, but, if it had some typed argument, these arguments have been pushed in the groups list
        // and we restore them by adding a disjunction to the "and" variable
        if (hasTypedArgument) {
            WitnessOr or = new WitnessOr();
            for (Map.Entry<?, WitnessAnd> ands : groups.entrySet()) {
               //or.add(ands.getValue().getIfUnitaryAnd() != null ? ands.getValue().getIfUnitaryAnd() : ands.getValue());
               or.add(ands.getValue());
            }
            and.add(or);
        }

        return (and.getIfUnitaryAnd() != null)? and.getIfUnitaryAnd() : and;
    }

    @Override
    public int countVarToBeExp(WitnessEnv env) {
        int count = 0;
        List<WitnessAssertion> witnessOr = andList.get(WitnessOr.class);
        List<WitnessAssertion> witnessVar = andList.get(WitnessVar.class);

        if(witnessOr != null)
            for(WitnessAssertion assertion : witnessOr)
                count += assertion.countVarToBeExp(env);

        if(witnessVar != null)
            for(WitnessAssertion assertion : witnessVar) {
                logger.warn("Espando: "+assertion.toString());
                logger.warn(Utils.beauty(env.getDefinition((WitnessVar) assertion).getFullAlgebra().toGrammarString()));
                //count += 1 + env.getDefinition((WitnessVar) assertion).countVarToBeExp(env);
                count += 1 + ((WitnessVar) assertion).countVarToBeExp(env);
            }

        return count;
    }


    @Override
    public List<Map.Entry<WitnessVar, WitnessAssertion>> varNormalization_separation(WitnessEnv env, WitnessVarManager varManager) throws WitnessException, REException {
        List<Map.Entry<WitnessVar, WitnessAssertion>> newDefinitions = new LinkedList<>();



        if (!isPrepared()) {

            if (choices!=null) {
                for (WitnessChoice choice : choices) {
                    WitnessAssertion schema = choice.getSchema();

                    if(schema.getClass() == WitnessAnd.class && ((WitnessAnd) schema).getIfUnitaryAnd() != null)
                        schema = ((WitnessAnd) schema).getIfUnitaryAnd();
                    choice.setSchema(schema);

                    if (schema.getClass() != WitnessBoolean.class && schema.getClass() != WitnessVar.class) {

                        newDefinitions.addAll(schema.varNormalization_separation(env, varManager));

                        WitnessVar newVar = varManager.buildVar(varManager.getName(schema));

                        newDefinitions.add(new AbstractMap.SimpleEntry<>(newVar, schema));
                        choice.setSchema(newVar);
                    }
                }
            }

            for (Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet())
                for (WitnessAssertion assertion : entry.getValue())
                    newDefinitions.addAll(assertion.varNormalization_separation(env, varManager));
        }
        return newDefinitions;
    }

    @Override
    public WitnessAssertion varNormalization_expansion(WitnessEnv env) throws WitnessException {
        if (isPrepared()) {
            for (WitnessChoice choice : getChoices())
                choice.getSchema().varNormalization_expansion(env);
            return this;
        } else {

            WitnessAnd and = new WitnessAnd();

            for (Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet())
                for (WitnessAssertion assertion : entry.getValue()) {
                    if (assertion.getClass() == WitnessVar.class)
                        and.add(env.getDefinition((WitnessVar) assertion).clone());
                    else
                        and.add(assertion.varNormalization_expansion(env));
                }

            return and;
        }
    }

    @Override
    public WitnessAssertion DNF() throws WitnessException {
        if (isPrepared()) {
            for (WitnessChoice choice : getChoices())
                choice.InPlaceDNF();
            return this;
        } else {
            if(!andList.containsKey(WitnessOr.class))
                return this;
            else {

                WitnessOr or = (WitnessOr) andList.get(WitnessOr.class).remove(0);
                if (andList.get(WitnessOr.class).size() == 0)
                    andList.remove(WitnessOr.class);

                for (Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet()) {
                    for (WitnessAssertion assertion : entry.getValue()) {
                        or = or.AndDistribute(assertion);
                    }
                }

                return or.DNF();
            }
        }
    }

    @Override
    public boolean isBooleanExp() {
        for(Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet()) {
            for(WitnessAssertion assertion : entry.getValue()) {
                if(!assertion.isBooleanExp()) return false;
            }
        }

        return true;
    }

    @Override
    public boolean isRecursive(WitnessEnv env, LinkedList<WitnessVar> visitedVar) {
        for(Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet()) {
            for(WitnessAssertion assertion : entry.getValue()) {
                if(assertion.isRecursive(env, visitedVar)) return true;
            }
        }

        return false;
    }

    @Override
    public WitnessVar buildOBDD(WitnessEnv env, WitnessVarManager varManager) throws WitnessException {
        WitnessVar obbdVarName = null;

        for(Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet()) {
            for(WitnessAssertion assertion : entry.getValue()) {
                if(obbdVarName == null)
                    obbdVarName = assertion.buildOBDD(env, varManager);
                else
                    obbdVarName = env.bdd.and(env, obbdVarName, assertion.buildOBDD(env, varManager));
            }
        }

        return obbdVarName;
    }

    @Override
    public void getReport(ReportResults reportResults) {
        for(Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet()) {
            for(WitnessAssertion assertion : entry.getValue()) {
                reportResults.increaseNumOfElementInAllOf();
                assertion.getReport(reportResults);
            }
        }
    }

    @Override
    public List<WitnessVar> uses() {
        HashSet<WitnessVar> results = new HashSet<>();
        for(List<WitnessAssertion> assertionList: this.andList.values()){
            for(WitnessAssertion assertion: assertionList)
                results.addAll(assertion.uses());
        }
        if (choices!=null)
            for(WitnessChoice choice : this.getChoices()){
                results.addAll(choice.uses());
        }

//        results.addAll(this.andList.values().stream()
//                .flatMap(List::stream).map(witnessAssertion -> witnessAssertion.uses())
//                .flatMap(List::stream).collect(Collectors.toList()));
        return new ArrayList(results);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WitnessAnd witnessAnd = (WitnessAnd) o;

        if(witnessAnd.andList.size() != andList.size())
            return false;

        // when both are prepared we do not check whether they have the
        // same choice list
        if ( this.isPrepared() != witnessAnd.isPrepared() )
            return false;

        if (this.andList.size()!=witnessAnd.andList.size())
            return false;

        for(Map.Entry<Object, List<WitnessAssertion>> entry : witnessAnd.andList.entrySet()){
            if(!this.andList.containsKey(entry.getKey())) return false;
            List<WitnessAssertion> check = null;

            if(witnessAnd.andList.get(entry.getKey()).size() >= this.andList.get(entry.getKey()).size()) {
                check = new LinkedList<>(witnessAnd.andList.get(entry.getKey()));
                check.removeAll(this.andList.get(entry.getKey()));
            }else{
                check = new LinkedList<>(this.andList.get(entry.getKey()));
                check.removeAll(witnessAnd.andList.get(entry.getKey()));
            }

            if (check.size() != 0) return false;
        }

        return true;
    }

    public List<Map.Entry<WitnessVar, WitnessAssertion>> objectPrepare(WitnessEnv env) throws REException, WitnessException {
        //checking if it's an object group

        if (andList.get(WitnessType.class) == null) { //and without type specified
            logger.debug("Preparing WitnessAnd without type specified");
            return new LinkedList<>();
        }
        if (andList.get(WitnessType.class) != null && !andList.get(WitnessType.class).contains(new WitnessType(AlgebraStrings.TYPE_OBJECT))) { //if is not an object type
            logger.debug("Preparing WitnessAnd without type object assertion");
            return new LinkedList<>();
        }
        if (andList.get(WitnessType.class).size() > 1) {//if contains more than one type
            logger.error("Preparing WitnessAnd with more than one type specified");
            return new LinkedList<>();
        }

        if (isPrepared()) return new LinkedList<>();

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

        //setPrepared();

        List<Map.Entry<WitnessVar, WitnessAssertion>> newDefinitions = new LinkedList<>();

        // GG: sicuri che le variabili siano state aggiunte per la normalizzazione?
        //TODO why not rerun locally? do we really need to run varNormalization_separation twice?
        // GG I believe the following line is useless, which is verified by the assertion below
        newDefinitions.addAll(env.varNormalization_separation(env, env.variableNamingSystem));

        //newDefinition contiene variabili rinoninate che dovrebbero essere rimosse
        //se le rimuoviamo cosi non vengono espanse dopo
        //no more necessary
        //newDefinitions = new LinkedList<>(); //reset the newDefinitions list

        if (!RPart.isEmpty())
            splitOriginalRList(compReqs, env);
        else {
            setChoices(new LinkedList<>());
        }

        //newDefinitions.addAll(this.varNormalization_separation(env, env.variableNamingSystem));
        //env.buildOBDD_notElimination();

        // GG I believe the following line is useless
        newDefinitions.addAll(env.varNormalization_separation(env, env.variableNamingSystem));
        //if (newDefinitions.size()!=0) throw new WitnessException("unexpected added variables in objectPrepare");

        newDefinitions.addAll(this.varNormalization_separation(env, env.variableNamingSystem));

        setPrepared();

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



    // for every pair < pattern, list of requests > and for any non-empty subset "subset" of "List of requests"
    // we create a separate request such that
    // (1) its schema is the conjunction of that of all requests in subset
    // (2) it belongs to the union of all the ORPs of all the requests in subset
    public List<WitnessChoice> expandChoiceList(List<WitnessChoice> fullChoices, WitnessEnv env) throws  WitnessException, REException {
        List<WitnessChoice> result = new LinkedList<>();
        for (WitnessChoice currFullChoice : fullChoices) {
            logger.debug("Rewriting WitnessChoice {}", fullChoices);

            //ComplexPattern patt = currFullChoice.getPattern();
            List<WitnessPattReq> currReqList = currFullChoice.getReqList();

            logger.debug("RequestList size: {}", currReqList.size());

            assertElse(currReqList.size() != 0, "impossibile: reqList.size() == 0");

            if (currReqList.size() == 1) {
                WitnessChoice subChoice = new WitnessChoice(currFullChoice.getConstraint(),
                        currReqList,
                        currReqList,
                        currFullChoice.getReqFullSet(),
                        currFullChoice.getPattern()
                );
                WitnessAnd andSchema = new WitnessAnd();
                andSchema.add(currFullChoice.getConstraint().getValue());
                andSchema.add(currReqList.get(0).getValue());
                subChoice.setSchema(andSchema);
                result.add(subChoice);
            } else {
                /* non-trivial set */
                for (List<WitnessPattReq> subset : subsetsOf(new LinkedList<>(currReqList))) { //subset: we create here a fragment that
                    //compute andSchema as intersection of all schemas in subset and cchema of constraint
                    WitnessAnd andSchema = new WitnessAnd();
                    andSchema.add(currFullChoice.getConstraint().getValue());
                    for (WitnessPattReq req : subset)
                        andSchema.add(req.getValue());//  andSchema = andSchema and schemaOf(oldReq)
                    //for (WitnessPattReq oldReq : coSubset )
                    //   ((WitnessAnd) andSchema).add(oldReq.getValue().not(env)); //  andSchema = andSchema and not schemaOf(oldReq)
                    WitnessChoice subChoice = new WitnessChoice(currFullChoice.getConstraint(),
                            currFullChoice.getReqList(),
                            subset,
                            currFullChoice.getReqFullSet(),
                            currFullChoice.getPattern()
                    );
                    subChoice.setSchema(andSchema);
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

    public List<Map.Entry<WitnessVar, WitnessAssertion>> arrayPreparation(WitnessEnv env) throws WitnessException, REException {
        //checking if it's an array group
        if (andList.get(WitnessType.class) == null) { //and without type specified
            logger.debug("Preparing WitnessAnd without type specified");
            return new LinkedList<>();
        }
        if (andList.get(WitnessType.class) != null
                && !andList.get(WitnessType.class).contains(new WitnessType(AlgebraStrings.TYPE_ARRAY))) {
            //if is not an array type
            logger.debug("Preparing WitnessAnd without type array assertion");
            return new LinkedList<>();
        }
        if (andList.get(WitnessType.class).size() > 1) {//if contains more than one type
            logger.error("Preparing WitnessAnd with more than one type specified");
            return new LinkedList<>();
        }

        List<WitnessAssertion> containsList = andList.remove(WitnessContains.class);
        List<WitnessAssertion> itemsList = andList.remove(WitnessItems.class);
        List<WitnessAssertion> preparedItemsList = andList.remove(WitnessItemsPrepared.class);

        if(containsList == null) containsList = new LinkedList<>();
        if(itemsList == null) itemsList = new LinkedList<>();

        if(containsList.isEmpty()) {
            andList.put(WitnessItems.class, itemsList);
            return new LinkedList<>();
        }

    //    if (itemsList.size() > 1) {
    //        this.merge(null,null);
    //    }

        if (itemsList.size() > 1)
            throw new RuntimeException("list of items should contains only one WitnessItems assertion after merge");

        if (preparedItemsList!= null && preparedItemsList.size() > 1)
            throw new RuntimeException("list of prepared items should contains only one WitnessItems assertion");

        /**
         * result.get:
         * 1 --> List of all the contains
         * 2 --> List with the new items#
         * 3 --> List of the new variables
         */
        List<Object> result = WitnessItemsPrepared.prepareArrayGroup(
                itemsList.isEmpty() ? null : (WitnessItems) itemsList.get(0),
                preparedItemsList==null ? null : (WitnessItemsPrepared) preparedItemsList.get(0),
                containsList,
                env);

        this.add((WitnessContains) result.get(0));
        this.add((WitnessItemsPrepared) result.get(1));

        return (List<Map.Entry<WitnessVar, WitnessAssertion>>) result.get(2);

    }


    @Override
    public int hashCode() {
        return andList != null ? andList.hashCode() : 0;
    }

    private static <T> LinkedList<T> mkSingleton(T elem){
        LinkedList<T> list = new LinkedList<>();
        list.add(elem);
        return list;
    }

}
