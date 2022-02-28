package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.Utils;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.AllOf_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.*;
import java.util.stream.Collectors;

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

    public LinkedHashMap<Object, List<WitnessAssertion>> getAndList() {
        return andList;
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
        List list = new LinkedList<>();
        list.add(new WitnessBoolean(true));
        this.andList.put(WitnessBoolean.class, list);
        hasFalse = false;
        logger.trace("Created a new and: {}", this);
    }

    /**
     * Add an assertion - el - to the andList.
     * if this is blocked we do not add el
     * if el is boolean true we do not add el
     * if el is an instance of WitnessAnd, we add every element of el.andList in this.andList
     * if el is an instance of uniqueItems or repeatedItems, we check if andList do not contain it, then we add it
     * if el is “false” or is a type assertion that is not compatible with the type assertion that is present, we raise
     * the exception WitnessFalseAssertionException
     * @param el element to add
     * @return true if the collection has changed, false otherwise
     */
    public boolean add(WitnessAssertion el) {
        // the recursive calls come before any side effect to this
        if(el instanceof WitnessOr && ((WitnessOr) el).getIfUnitaryOr() != null)
            return add(((WitnessOr) el).getIfUnitaryOr());

        if(el instanceof WitnessAnd) { //We flatten the and with a distinct code for add
            return add((WitnessAnd) el);
        }

        if(hasFalse)
            return false; //if hasFalse then we do not add any other assertion

        if(el instanceof WitnessBoolean) { //add boolean
            Boolean wb = ((WitnessBoolean) el).getValue();
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
                 + "\r\n" +
                '}';
    }


    @Override
    public void checkLoopRef(WitnessEnv env, Collection<WitnessVar> varList) throws RuntimeException {
        for(Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet())
            for(WitnessAssertion assertion : entry.getValue())
                assertion.checkLoopRef(env, varList);
    }

    @Override
    public void reachableRefs(Set<WitnessVar> collectedVar, WitnessEnv env) throws RuntimeException {
        for(Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet())
            for(WitnessAssertion assertion : entry.getValue())
                assertion.reachableRefs(collectedVar, env);
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
        if(hasFalse) return new WitnessBoolean(false);

        if(getIfUnitaryAnd() != null) return getIfUnitaryAnd();

        WitnessAnd newAnd = new WitnessAnd();
        boolean modified = false;

        //this loop merge only element of the same Class
        for (Map.Entry<Object, List<WitnessAssertion>> sameTypeAssertion : andList.entrySet()) {
            int size = sameTypeAssertion.getValue().size();
            WitnessAssertion merged = null;
            if(size>0)
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
        if(newAnd.andList.containsKey(WitnessBet.class)){
            List<WitnessAssertion> witnessList = newAnd.andList.get(WitnessBet.class);
            List<WitnessBet> witnessBetList = witnessList.stream().map(a->(WitnessBet)a).collect(Collectors.toList());
            //TODO : check the following line, makes no sense to me: you should not "put" the result of mergeBet since
            //we did not remove the input - luckily, this line is never executed since witnessBetList.size() is always 1
            if(witnessBetList.size()>1)
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

        for(Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet())
            for(WitnessAssertion assertion : entry.getValue())
                newDefinitions.addAll(assertion.varNormalization_separation(env, varManager));

        return newDefinitions;
    }

    @Override
    public WitnessAssertion varNormalization_expansion(WitnessEnv env) throws WitnessException {
        WitnessAnd and = new WitnessAnd();

        for(Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet())
            for(WitnessAssertion assertion : entry.getValue()){
                if(assertion.getClass() == WitnessVar.class)
                    and.add(env.getDefinition((WitnessVar) assertion).clone());
                else
                    and.add(assertion.varNormalization_expansion(env));
            }

        return and;
    }

    @Override
    public WitnessAssertion DNF() throws WitnessException {
        if(!andList.containsKey(WitnessOr.class))
            return this;

        WitnessOr or = (WitnessOr) andList.get(WitnessOr.class).remove(0);
        if(andList.get(WitnessOr.class).size() == 0)
            andList.remove(WitnessOr.class);

        for(Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet()) {
            for(WitnessAssertion assertion : entry.getValue()) {
                or = or.AndDistribute(assertion);
            }
        }

        return or.DNF();
    }

    @Override
    public WitnessAssertion toOrPattReq() {
        WitnessAnd newAnd = new WitnessAnd(); //to avoid ConcurrentModificationException

        for (Map.Entry<Object, List<WitnessAssertion>> entry : andList.entrySet())
            for (WitnessAssertion assertion : entry.getValue())
                if (entry.getKey() == WitnessPattReq.class)
                    newAnd.add(assertion.toOrPattReq());
                else
                    assertion.toOrPattReq();

        andList.remove(WitnessPattReq.class);
        this.add(newAnd);

        return this;
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

//        results.addAll(this.andList.values().stream()
//                .flatMap(List::stream).map(witnessAssertion -> witnessAssertion.uses())
//                .flatMap(List::stream).collect(Collectors.toList()));
        return new ArrayList(results);
    }

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
            List<WitnessAssertion> check = null;

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

    /**
     * Assuming that before was executed and-merging, groupize, separation, expansione, dnf
     *
     */
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

        //check the presence of WitnessPattReq --> if present ERROR!  since toOrPattReq() invoked in WitnessEnv.objectPrepare()
        if (andList.containsKey(WitnessPattReq.class)) {
            String s = "PatternReq should have already been rewritten!";
            logger.error(s);
            throw new WitnessException(s);
        }

        List<WitnessAssertion> CPart = andList.remove(WitnessProperty.class); //List<WitnessProperty>

        //complete and splitCPart --> da ottimizzare in una seconda fase
        if (CPart == null) {//vuota
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

        //Combine CP-RP
        //maps each element of the CP to the all related elements of the RP
        HashMap<WitnessAssertion, List<WitnessPattReq>> coReqs = new HashMap<>();

        List<WitnessAssertion> ORPart = andList.get(WitnessOrPattReq.class);

        for (WitnessAssertion c_assertion : CPart)
            coReqs.put(c_assertion, new LinkedList<>());

        if (ORPart != null) {

            for (WitnessAssertion ORP_assertion : ORPart) { //WitnessOrPattReq

                //List<WitnessPattReq> newRList = new LinkedList<>();
                Set<WitnessPattReq> newRList = new HashSet<>();

                for (WitnessPattReq R_Assertion : ((WitnessOrPattReq) ORP_assertion).reqList) {
                    for (WitnessAssertion C_assertion : CPart) {

                        logger.debug("[CP-RP] Computing intersection between {} and {}", ((WitnessProperty) C_assertion).getPattern(), R_Assertion.getPattern());

                        ComplexPattern pattInt = ((WitnessProperty) C_assertion).getPattern().intersect(R_Assertion.getPattern());

                        if (!pattInt.isEmptyDomain()) {

                            logger.debug("[CP-RP] Creating new WitnessAnd");
                            WitnessAnd newAnd = new WitnessAnd();

                            logger.debug("[CP-RP] Adding {} to {}", ((WitnessProperty) C_assertion).getValue(), newAnd);
                            newAnd.add(((WitnessProperty) C_assertion).getValue());

                            logger.debug("[CP-RP] Adding {} to {}", ((WitnessPattReq) R_Assertion).getValue(), newAnd);
                            newAnd.add(((WitnessPattReq) R_Assertion).getValue());

                            if (newAnd.notObviouslyEmpty()) {
                                WitnessPattReq newReq = env.pattReqManager.build(pattInt, newAnd);

                                newRList.add(newReq);
                                coReqs.get(C_assertion).add(newReq);
                            }
                        }
                    }
                }

                // empty the ReqList of ORP_assertion and fill it again with the new
                // requests collected in newRList
                ((WitnessOrPattReq) ORP_assertion).setReqList(new LinkedList<>());
                for (WitnessPattReq req : new LinkedList<>(newRList))   //to avoid concurrentModificationException
                    ((WitnessOrPattReq) ORP_assertion).fullConnect(req);
            }
        }


        //init coMatrix
        LinkedList<Map.Entry<WitnessPattReq, WitnessPattReq>> CoMatrix = new LinkedList<>();

        for (WitnessAssertion assertion : CPart) {

            List<WitnessPattReq> coList = coReqs.get(assertion);

            for (int i = 0; i < coList.size() - 1; i++) {
                for (int j = i + 1; j < coList.size(); j++) {
                    logger.debug("Adding to coMatrix the entry < {} ; {} >", coList.get(i), coList.get(j));
                    CoMatrix.add(new AbstractMap.SimpleEntry<>(coList.get(i), coList.get(j)));
                }
            }
        }

        List<Map.Entry<WitnessVar, WitnessAssertion>> newDefinitions = new LinkedList<>();

        //TODO why not rerun locally?
        newDefinitions.addAll(env.varNormalization_separation(env, env.variableNamingSystem));

        //newDefinition contiene variabili rinoninate che dovrebbero essere rimosse
        //se le rimuoviamo cosi non vengono espanse dopo
        //no more necessary
        //newDefinitions = new LinkedList<>(); //reset the newDefinitions list

        if (ORPart != null)
            splitOriginalRList(ORPart, CoMatrix, env);

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
            return true; //TODO: x e not(x) nella stessa congiunzione??
    }

    /**
     * Splits a CP list from index position forwards leaving position before index untouched
     * it returns a CP list that is equivalent to list but where all different
     * de.uni_passau.sds.patterns are disjoint
     */
    private List<WitnessAssertion> splitClist(List<WitnessAssertion> list) throws WitnessException {
        //RECURSIVE
        if(list.isEmpty()) return list;
        List expandedTail = splitClist(list.subList(1, list.size()));
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


    /*
       In order to split the RList, we first transform each Request (p:A) into a pair
       p, {(p:A)}. When we generate an intersection pattern, we keep track of all the
       original requests that are its ancestors.
       Then, the function rewritePatternReqs, will explode each pair (p, {r1,...,rn})
       into all the different 2^n subcases
    */
    private void splitOriginalRList(List<WitnessAssertion> ORList, List<Map.Entry<WitnessPattReq, WitnessPattReq>> coMatrix, WitnessEnv env) throws WitnessException, REException {
        List<PatternReqs> ReqList = new LinkedList<>();

        for(WitnessAssertion or : ORList)
            for(WitnessPattReq req : ((WitnessOrPattReq) or).reqList)
                ReqList.add(new PatternReqs(req.getPattern(), creaSingoletto(req)));

        List<Map.Entry<PatternReqs, PatternReqs>> coList = new LinkedList<>();

        for(Map.Entry<WitnessPattReq, WitnessPattReq> entry : coMatrix) {
            PatternReqs x = new PatternReqs(entry.getKey().getPattern(), creaSingoletto(entry.getKey()));
            PatternReqs y = new PatternReqs(entry.getValue().getPattern(), creaSingoletto(entry.getValue()));
            logger.debug("Adding to coList the entry < {} ; {} >",  x, y);
            coList.add(new AbstractMap.SimpleEntry(x, y));
        }

        List<PatternReqs> splitList = splitRList(ReqList, coList);
        rewritePatternReqsList(splitList, env);
    }

    /*
       Splits the elements of list from the one with index "index"
       and returns the result of this operation.
       To this aim, it first splits the elements from index+1 onwards (the tail), and then it takes
       the element of position index, and it "reduces" it against all elements of the tail
    */
    private List<PatternReqs> splitRList(List<PatternReqs> list, List<Map.Entry<PatternReqs, PatternReqs>> coList) throws WitnessException {
        if(list.isEmpty()) return list;

        List<PatternReqs> expandedTail = splitRList(list.subList(1, list.size()), coList );

        Map.Entry<PatternReqs, List<PatternReqs>> subHead_reducedExpandedTail = reduceReqWithList(list.get(0), expandedTail, coList);
        PatternReqs subHead = subHead_reducedExpandedTail.getKey();
        List<PatternReqs> reducedExpandedTail = subHead_reducedExpandedTail.getValue();

        if(subHead.complexPattern.isEmptyDomain()) return reducedExpandedTail;
        else{
            reducedExpandedTail.add(subHead);
            return reducedExpandedTail;
        }
    }

    /*
      Returns a pair (PatternReqs,NewList) obtained by reducing list from position index onwards, and ignoring the
      first part
      A PatternReqs is a pair (pattern-List Of Requests) that indicates that, for each req in List Of Requests,
      its ORP should contain one element whose pattern is "pattern"
      Reducing "req" with "head" means that "head" is split into "head and req" and "head minus req",
      while req is reduced to what remains (req-head).
      Reduction only combines two elements that are compatible according to coList, and updates the coList,
      but this is just an optimization in order to reduce the amount of pattern intersections to compute
      coList is just an optimization
     */
    private Map.Entry<PatternReqs, List<PatternReqs>> reduceReqWithList(PatternReqs req, List<PatternReqs> list,
                                                                        List<Map.Entry<PatternReqs, PatternReqs>> coList) throws WitnessException {
        if(list.isEmpty())
            return new AbstractMap.SimpleEntry<>(req, new LinkedList<>());

        Map.Entry<PatternReqs, List<PatternReqs>> subReq_reducedTail = reduceReqWithList(req, list.subList(1, list.size()), coList);

        PatternReqs subReq = subReq_reducedTail.getKey();
        List<PatternReqs> reducedTail = subReq_reducedTail.getValue();

        logger.debug("Splitting subReq {} with {} reducedTail",  subReq, reducedTail);

        /* if dom(subReq) is already empty there is no reduction left to perform */
        PatternReqs head = list.get(0);
        ComplexPattern subReqPatt = subReq.complexPattern;

        if(!compatible(subReq, head, coList) || subReqPatt.isEmptyDomain()) {
            logger.debug("subReq {} is compatible with head {}", subReq, head);
            reducedTail.add(head);
            return new AbstractMap.SimpleEntry<>(subReq, reducedTail);
        }

        /* if we arrive here, then subReq is not empty and it is coList-compatible with head */
        coList.remove(new AbstractMap.SimpleEntry<>(subReq, head));
        coList.remove(new AbstractMap.SimpleEntry<>(head, subReq));

        logger.debug("Removing to coList the entry < {} ; {} >", subReq, head);

        ComplexPattern intersection = subReqPatt.intersect(head.complexPattern);

        if(intersection.isEmptyDomain()){
            reducedTail.add(head);
            return new AbstractMap.SimpleEntry<>(subReq, reducedTail);
        }

        List<WitnessPattReq> unionList = new LinkedList<>(head.list);
        unionList.addAll(subReq.list);
        PatternReqs intersectReq = new PatternReqs(intersection, unionList);
        LinkedList<PatternReqs> newReducedList = new LinkedList<>(reducedTail); //forse si può rimuovere se siamo ricorsivi

        newReducedList.add(intersectReq);
        addPairs(coList, intersectReq, intersect(compWith(coList, subReq), compWith(coList, head)) );

        /* we also reduce req - the reducedReq may have empty pattern*/
        ComplexPattern reqMinHead = subReqPatt.minus(head.complexPattern);
        PatternReqs newSubReq = new PatternReqs(reqMinHead, subReq.list); // req minus getValue is the reduced req
        if (!reqMinHead.isEmptyDomain()) { // no need to do this is reqMinHead is empty
            addPairs(coList, newSubReq, compWith(coList, subReq));

        }

        /* finally, we also generate headMinReq, in case it is not empty */
        ComplexPattern headMinReq = head.complexPattern.minus(subReqPatt);
        if(!headMinReq.isEmptyDomain()) {
            PatternReqs headMinReqReq = new PatternReqs(headMinReq, head.list);
            newReducedList.add(headMinReqReq); // right minus left : rightSchema goes in the list
            addPairs(coList, headMinReqReq, compWith(coList, head));
        }

        for(int i = coList.size() -1; i >= 0; i--) {
            Map.Entry<PatternReqs, PatternReqs> entry = coList.get(i);
            if (entry.getKey().equals(head) || entry.getValue().equals(head)
                    || entry.getKey().equals(subReq) || entry.getValue().equals(subReq)) {
                logger.debug("Removing to coList the entry < {} ; {} >", coList.get(i));
                coList.remove(i);
            }
        }

        return new AbstractMap.SimpleEntry<>(newSubReq, newReducedList);
    }

    private boolean compatible(PatternReqs r1, PatternReqs r2, List<Map.Entry<PatternReqs, PatternReqs>> coList){
        return coList.contains(new AbstractMap.SimpleEntry<>(r1,r2)) || coList.contains(new AbstractMap.SimpleEntry<>(r2,r1));
    }

    private List<PatternReqs> compWith(List<Map.Entry<PatternReqs, PatternReqs>> coList, PatternReqs req){
        List<PatternReqs> returnList = new LinkedList<>();

        for(Map.Entry<PatternReqs, PatternReqs> entry : coList)
            if(entry.getKey().equals(req)) returnList.add(entry.getValue());
            else if(entry.getValue().equals(req)) returnList.add(entry.getKey());

        logger.debug("CompableWith list of PatternReqs {} is {}", req, returnList);

        return returnList;
    }

    private List<PatternReqs> intersect(List<PatternReqs> l1, List<PatternReqs> l2){
        List<PatternReqs> returnPattReqs = new LinkedList<>();

        logger.debug("Computing PatternReqs intersection between {} and {}", l1, l2);

        for(PatternReqs pr1 : l1)
            for(PatternReqs pr2 : l2)
                if(pr1.equals(pr2))
                    returnPattReqs.add(pr1);

        logger.debug("PatternReqs intersection result: {}", returnPattReqs);

        return returnPattReqs;
    }

    private void addPairs(List<Map.Entry<PatternReqs, PatternReqs>> coList, PatternReqs req, List<PatternReqs> reqList){
        for(PatternReqs tmp : reqList){
            logger.debug("Adding to coList the entry < {} ; {} >",  req, tmp);
            coList.add(new AbstractMap.SimpleEntry<>(req, tmp));
        }
    }

    private void rewritePatternReqsList (List<PatternReqs> list, WitnessEnv env) throws WitnessException, REException {
        List<WitnessAssertion> oldOrPattRequests = this.andList.get(WitnessOrPattReq.class); //getAllOldRequests

        logger.debug("Old ORP size: ", oldOrPattRequests.size());

        Set<WitnessPattReq> oldPattRequests = new HashSet<>();

        for(WitnessAssertion pr : oldOrPattRequests)
            oldPattRequests.addAll(((WitnessOrPattReq) pr).reqList);

        logger.debug("Old Pattern Requests size:", oldPattRequests.size());

        for (PatternReqs pReqs : list ) {
            rewritePatternReqs(pReqs, env);
        }

        for (WitnessAssertion oldReq : oldPattRequests ) {
            WitnessPattReq oldReq_ = (WitnessPattReq) oldReq;
            oldReq_.deConnectAll(new LinkedList<>(oldReq_.getOrpList()));
        }
    }

    private void rewritePatternReqs(PatternReqs reqs, WitnessEnv env) throws WitnessException, REException {
        logger.debug("Rewriting PatternReqs {}", reqs);

        ComplexPattern patt = reqs.complexPattern;
        List<WitnessPattReq> reqList = reqs.list;

        logger.debug("RequestList size:", reqList.size());

        if (reqList.size() == 0) { throw new UnsupportedOperationException("impossibile: reqList.size() == 0");}  // impossible case
        if (reqList.size() == 1 ) {
            WitnessPattReq clone = reqList.get(0).clone();
            clone.setPattern(patt);

            for(WitnessOrPattReq orp : reqList.get(0).getOrpList())
                clone.fullConnect(orp);

            return;
        }
        /* non-trivial set */
        for (List<WitnessPattReq> subset : compSubsetsOf(new LinkedList<>(reqList))) { //subset: we create here a fragment that
            //satisfies all and only the assertions in "subset"
            //subset is never empty
            //subset: the set of Ancestors that are compatible with this fragment
            WitnessAssertion schema = new WitnessAnd();
            List <WitnessPattReq> coSubset = new LinkedList<>(reqList);
            coSubset.removeAll(subset);

            for (WitnessPattReq oldReq : new LinkedList<>(subset) )
                ((WitnessAnd) schema).add(oldReq.getValue());//  schema = schema and schemaOf(oldReq)


            //removed for optimization
            /*
            if(schema.getClass() == WitnessAnd.class)
                for (WitnessPattReq oldReq : coSubset )
                    ((WitnessAnd) schema).add(oldReq.getValue().not(env)); //  schema = schema and not schemaOf(oldReq)
            else{
                String s = "schema is not a WitnessAnd";
                logger.error(s);
                throw new WitnessException(s);
            }
             */

            // WitnessPattReq fragment = env.pattReqManager.build(patt, schema); // using "build" is not a good idea
            WitnessPattReq fragment = new WitnessPattReq(patt, schema);
            //fragment: satisfies subset and fails all assertions in coSubset
            for (WitnessPattReq compAncestor : subset ) {            //we put the fragment in all and only the ORPs that contain
                //this.add(fragment);
                for (WitnessOrPattReq orp : new LinkedList<>(compAncestor.getOrpList() )) {   //one req that is satisfied
                    fragment.fullConnect(orp);
                }
            }
            //stop
        }
    }

    /* this is a trivial version that generates all non-empty subsets with no compatibility check */
    List<List<WitnessPattReq>> compSubsetsOf(List<WitnessPattReq> reqList) {
        if (reqList.size() == 1) { return creaSingoletto(reqList); } // receives { r } and return {{r}}
        WitnessPattReq head = reqList.remove(0);
        List<List<WitnessPattReq>> subsetsNoHead = compSubsetsOf(reqList); // we compute the non-empty subsets of the reqList with no head
        List<List<WitnessPattReq>> returnList = new LinkedList<>(subsetsNoHead);

        for (List<WitnessPattReq> subNoHead : subsetsNoHead) {
            List<WitnessPattReq> subNoHead_clone = new LinkedList<>(subNoHead);
            subNoHead_clone.add(head);
            returnList.add(subNoHead_clone);
        }
        returnList.add(creaSingoletto(head)); // subsetsNoHead does not contain the empty set, hence we must add this singleton
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

    private static <T> LinkedList<T> creaSingoletto(T elem){
        LinkedList<T> list = new LinkedList<>();
        list.add(elem);
        return list;
    }

}

/**
 * pair <pattern, ancestors list>
 * used during ObjectPreparation
 */
class PatternReqs{
    public ComplexPattern complexPattern;
    public List<WitnessPattReq> list;

    public PatternReqs() {
        list = new LinkedList<>();
    }

    public PatternReqs(ComplexPattern complexPattern, List<WitnessPattReq> list) {
        this.complexPattern = complexPattern;
        this.list = list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PatternReqs that = (PatternReqs) o;

        if (complexPattern != null ? !complexPattern.equals(that.complexPattern) : that.complexPattern != null)
            return false;

        List<WitnessPattReq> check = new LinkedList<>(this.list);
        check.removeAll(that.list);
        if(check.size() != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = complexPattern != null ? complexPattern.hashCode() : 0;
        result = 31 * result + (list != null ? list.hashCode() : 0);
        return result;
    }
}