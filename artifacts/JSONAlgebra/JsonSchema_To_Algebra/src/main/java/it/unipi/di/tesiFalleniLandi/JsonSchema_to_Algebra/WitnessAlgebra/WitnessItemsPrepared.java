package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.*;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.*;
import java.util.stream.Collectors;

/*
List<WitnessAssertion[]> items;
WitnessAssertion[] additionalItems;
Integer  containsLength;
WitnessAssertion[]  contains;
Double[]  min, max;

// invariant: the length of each element of Items and the length of the addditionalItems array is  2^^containsLength
// the length of contains, min, max is containsLength
// at the end of preparation every WitnessAssertion is atomic (variable, true, or false)

// combine: receives two arrays of length 2^^len1 and 2^len2 and returns the product array, that is,
// the one that associates to each bitmap XY  the witness   Arr1[X] And Arr2[Y]
//  len1 and len2 may also be 0
WitnessAssertion[]  multiply (WitnessAssertion[]  Arr1, Integer  len1, WitnessAssertion[]  Arr2, Integer  len2)
     WitnessAssertion[]  result = new WitnessAssertion [2^^(len1+len2)];
    for (i=0; i++; i < 2^^(len1+len2)] {
          bm1 = i / (2^^len) ;           //   shiftRight(i,len2);
          bm2 = i % (2^^len);          //   maskRight(i,len2);
          result(new WitnesAnd().add(Arr1[bm1]).add(Arr2[bm2]);
    }
}
 */

public class WitnessItemsPrepared implements WitnessAssertion{
    private static Logger logger = LogManager.getLogger(WitnessItemsPrepared.class);

    //items#(f1 · · · fn; f | #^M1_m1:ref(x)1, . . . , #^Mk_mk:ref(x)k)

    List<WitnessAssertion[]> items;
    WitnessAssertion[] additionalItems;
    WitnessContains[] contains;

    //private List<Function> functions;
    //private WitnessAssertion additionalItems;
    //private List<WitnessContains> contains;


    /**
     * handy methods
     * @param input
     * @return
     */
    private List<WitnessVar> fromList(WitnessAssertion[] input){
        return Arrays.stream(input).sequential()
                .map(witnessAssertion -> witnessAssertion.uses())
                .flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * handy methods
     * @param input
     * @return
     */
    private List<WitnessVar> fromList(WitnessContains[] input){
        return Arrays.stream(input).sequential()
                .map(witnessAssertion -> witnessAssertion.uses())
                .flatMap(List::stream).collect(Collectors.toList());
    }

    /*Getters*/
    public List<WitnessAssertion[]> getItems() {
        return items;
    }

    public WitnessAssertion[] getAdditionalItems() {
        return additionalItems;
    }

    public WitnessContains[] getContains() {
        return contains;
    }

    public WitnessItemsPrepared(){
        items = new LinkedList<>();
        contains = new WitnessContains[0];
        additionalItems = new WitnessAssertion[0];
    }

    // prepares an array group (item + list of contains)
    /*
     * Returns a list of three elements:
     * 1 --> List of all the minItems/maxItems constraints
     * 2 --> List with the new items#
     * 3 --> List of the new variables built during variable separation
     */

    /**
     * normalizes the bounds
     * @param item
     * @param contains can be null
     * @param env
     * @return
     * @throws REException
     * @throws WitnessException
     */
    public static List prepareArrayGroup(WitnessItems item,
                                         WitnessItemsPrepared preparedItems,
                                         List<WitnessAssertion> contains,
                                         WitnessEnv env) throws REException, WitnessException
    {
        //if item == null make it be items[;true]
        if(item == null ){
            item = new WitnessItems();
//            item.setAdditionalItems(new WitnessBoolean(true));
        }

        if(item == null && preparedItems == null){
            item.setAdditionalItems(new WitnessBoolean(true));
        }


        //if contains == null make it be an empty list
        if (contains == null)
            contains = new LinkedList<>();

        //computes the maxOfMin and removes contains true
        WitnessContains contains_true = null;
        Double maxOfMin = 0d;//was: Double.NEGATIVE_INFINITY;
        for (WitnessAssertion tmp : contains) {
            WitnessContains c = (WitnessContains) tmp;

            //Searching for Max of m in  #_m
            maxOfMin = (maxOfMin < c.getMin()) ? c.getMin() : maxOfMin;

            //Searching for contains_true
            if (c.getContains() instanceof WitnessBoolean && ((WitnessBoolean) c.getContains()).getValue()) {
                if (contains_true != null) //if there is more than 1 #_m^M:t
                    throw new UnsupportedOperationException("Multiple #:true");
                contains_true = c;
                contains.remove(c);

                break;
            }
        }

        if(contains_true == null) //only possible when there is no contains true (otherwise, exit with exception)
            contains_true = new WitnessContains(0, Double.POSITIVE_INFINITY, new WitnessBoolean(true));

        //Normalization of items wrt the upper bound of contains_true (that is, items is cut if longer than c.max)
        WitnessItems normalizedItems = item.mergeElement(contains_true);

        //Normalization of contains_true
        contains_true.setMin(maxOfMin);
        if(maxOfMin > contains_true.getMax())
            contains_true.setContains(new WitnessBoolean(false));
        //TODO: consider exiting by setting items to false

        //containsAfter(m,M,p,true) -> ite_{p+m}^{p+M} TODO check
        double position =contains_true.getPosition();
        if(position>0){
            if (contains_true.getMin()!=0)
                contains_true.setMin(contains_true.getMin()+position);
            contains_true.setMax(contains_true.getMax()+position);
            contains_true.setPosition(0d);
        }


        //Normalization of the other contains (#)
        for (WitnessAssertion tmp : contains) {
            WitnessContains c = (WitnessContains) tmp;
            if(c.getMax() > contains_true.getMax())
                c.setMax(Double.POSITIVE_INFINITY);
        }

        //Transformation of normalizedItems to items#
        //instances is the list f items# that we get by transforming
        //normalizedItems and all the elements of "contains"
        List<WitnessItemsPrepared> instances = new LinkedList<>();
        instances.add(transformToItemsPrepared(normalizedItems));
        if(preparedItems != null)
            instances.add(preparedItems);

        //Transformation of each contains in items#
        //Robust in case "contains" is the empty list
        for(WitnessAssertion tmp : contains) {
            WitnessContains c = (WitnessContains) tmp;
            instances.add(transformToItemsPrepared(c, env));
        }

        //Contains_true does not need to be in "instances", it is
        //returned by the function
        //instances.add(transformToItemsPrepared(contains_true, env));

        //Here we merge all items#
        WitnessItemsPrepared itemsPrepared = null;
        itemsPrepared = instances.remove(0);
        for(WitnessItemsPrepared i : instances)
            itemsPrepared = merge(itemsPrepared, i);

        //separazione TODO:finire - when are we going to do variable expansion?
        //We return a tuple of three values:
        // (1) contains_true
        // (2) itemsPrepared
        // (3) new variables create by separation

        List result = new LinkedList();
        result.add(contains_true);
        result.add(itemsPrepared);
        result.add(itemsPrepared.varNormalization_separation(env, env.variableNamingSystem));

//        System.out.println("==result");
//        result.forEach(r-> System.out.println("item -> " +r));
        env.buildOBDD_notElimination();

        return result;
    }

    /**
     * maps each item to a list whose first element is the assertion of item
     * @param items
     * @return
     */
    private static WitnessItemsPrepared transformToItemsPrepared(WitnessItems items){
        WitnessItemsPrepared itemsPrepared = new WitnessItemsPrepared();

        itemsPrepared.items = new LinkedList<>();

        for(WitnessAssertion wAssert : items.getItems()){
            WitnessAssertion[] witnessAssertionsArray = new WitnessAssertion[1];

            witnessAssertionsArray[0] = wAssert;

            itemsPrepared.items.add(witnessAssertionsArray);
        }

        if(items.getAdditionalItems() != null) {
            itemsPrepared.additionalItems = new WitnessAssertion[1];
            itemsPrepared.additionalItems[0] = items.getAdditionalItems();
        }else{
            //TODO: check -> OK
            itemsPrepared.additionalItems = new WitnessAssertion[1];
            itemsPrepared.additionalItems[0] = new WitnessBoolean(true);
        }

        return itemsPrepared;
    }

    /**
     * returns a WIP whose additionalItems has the content of contains and its negation
     * @param contains
     * @param env
     * @return
     * @throws REException
     */
    private static WitnessItemsPrepared transformToItemsPrepared(WitnessContains contains, WitnessEnv env) throws REException {
        WitnessItemsPrepared itemsPrepared = new WitnessItemsPrepared();

        itemsPrepared.additionalItems = new WitnessAssertion[2];
        itemsPrepared.contains = new WitnessContains[1];

        itemsPrepared.additionalItems[0] = contains.getContains().not(env);
        itemsPrepared.additionalItems[1] = contains.getContains();
        itemsPrepared.contains[0] = contains;

        return itemsPrepared;
    }


    private static WitnessItemsPrepared merge(WitnessItemsPrepared a, WitnessItemsPrepared b){
        WitnessItemsPrepared merged = new WitnessItemsPrepared();

        //We force here the invariant that "a" is shorter than "b"
        if(a.items.size() > b.items.size()){
            WitnessItemsPrepared c = a;
            a=b;
            b=c;
        }

        for(int i = 0; i < a.items.size(); i++)
            merged.items.add(multiply(a.items.get(i), b.items.get(i)));

        for(int i = a.items.size(); i < b.items.size(); i++)
            merged.items.add(multiply(a.additionalItems, b.items.get(i)));

        merged.additionalItems = multiply(a.additionalItems, b.additionalItems);

        //accodiamo i contains
        merged.contains = new WitnessContains[a.contains.length + b.contains.length];
        int count = 0;

        for(int i = 0; i < a.contains.length; i++)
            merged.contains[count++] = a.contains[i];

        for(int i = 0; i < b.contains.length; i++)
            merged.contains[count++] = b.contains[i];

        return merged;
    }

    private static WitnessAssertion[] multiply(WitnessAssertion[] arr1, WitnessAssertion[] arr2){
        WitnessAssertion[] result = new WitnessAssertion[arr1.length*arr2.length];

        for(int i = 0; i < result.length; i++){
            int bm1 = i/arr2.length;
            int bm2 = i%arr2.length;

            WitnessAnd and = new WitnessAnd();
            WitnessAssertion  auxAnd;
            and.add(arr1[bm1]);
            and.add(arr2[bm2]);

            auxAnd = and.getIfUnitaryAnd(); //solves the useless case and(vars)

            if(auxAnd != null)
                result[i] = auxAnd;
            else
                result[i] = and;
        }

        return result;
    }


    @Override
    public String toString() {
        return "ItemsPrepared{\r\n" +
                "items#=" + Arrays.toString(items.toArray()) + "\r\n" +
                ", additionalItems=" + Arrays.toString(additionalItems) +"\r\n" +
                ", contains=" + Arrays.toString(contains) +
                "\r\n}";
    }

    @Override
    public void checkLoopRef(WitnessEnv env, Collection<WitnessVar> varList) throws RuntimeException {
        throw new UnsupportedOperationException("tbd"); //TODO implement
    }

    @Override
    public void reachableRefs(Set<WitnessVar> collectedVar, WitnessEnv env) throws RuntimeException {
        throw new UnsupportedOperationException("tbd"); //TODO implement
    }

    /**
     * @param varManager
     * @param pattReqManager
     * @return
     * @throws REException
     */
    @Override
    public WitnessAssertion mergeWith(WitnessAssertion a, WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException {
        return merge(this, (WitnessItemsPrepared) a);
    }

//    /**
//     * combines two maps with equal length by building pairwise conjunction
//     * @param left
//     * @param right
//     * @return
//     */
//    public WitnessAssertion[] combine(WitnessAssertion[] left, WitnessAssertion[] right,
//                                      WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws Exception {
//        WitnessAssertion[] result = new WitnessAssertion[left.length];
//
//        if(left.length!=right.length)
//            throw new Exception("Trying to combine mappings with different size");
//
//        for(int i = 0; i<left.length; i++)
//            result[i] = left[i].mergeWith(right[i],varManager,pattReqManager);
//
//        return result;
//    }

    @Override
    public WitnessAssertion merge(WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException {
//        WitnessItemsPrepared witnessItemsPrepared = new WitnessItemsPrepared();
//
//        for(WitnessAssertion[] witnessAssertions: items)
//            ;
//
//        if(additionalItems !=null)
//            ;
//        throw  new UnsupportedOperationException();
        return this;
    }

    @Override
    public WitnessType getGroupType() {
        return new WitnessType(AlgebraStrings.TYPE_ARRAY);
    }

    //TODO inspect
    @Override
    public Assertion getFullAlgebra() {
        ItemsPrepared_Assertion itemsPrepared_assertion = new ItemsPrepared_Assertion();

        for(WitnessAssertion[] tmpList : items){
            Assertion[] assertionList = new Assertion[tmpList.length];
            for(int i = 0; i<tmpList.length; i++)
                assertionList[i] = tmpList[i].getFullAlgebra();

            itemsPrepared_assertion.addItems(assertionList);
        }

        Assertion[] additionalItemsArray = new Assertion[additionalItems.length];
        for(int i = 0; i<additionalItems.length; i++)
            additionalItemsArray[i] = additionalItems[i].getFullAlgebra();

        itemsPrepared_assertion.addAdditionalItems(additionalItemsArray);

        Exist_Assertion[] containsArray = new Exist_Assertion[contains.length];
        for(int i = 0; i<contains.length; i++)
            containsArray[i] = (Exist_Assertion) contains[i].getFullAlgebra();

        itemsPrepared_assertion.addContains(containsArray);

        return itemsPrepared_assertion;
    }

    @Override
    public WitnessItemsPrepared clone() {
        logger.trace("Cloning WitnessItemsPrepared: {}", this);
        WitnessItemsPrepared clone = new WitnessItemsPrepared();
        for(WitnessAssertion[] assertionArray : items) {
            WitnessAssertion[] clonedArray = new WitnessAssertion[assertionArray.length];
            for (int i = 0; i < assertionArray.length; i++){
                clonedArray[i] = assertionArray[i].clone();
            }
            clone.items.add(clonedArray);
        }
        if(additionalItems != null) {
            WitnessAssertion[] clonedArray = new WitnessAssertion[additionalItems.length];
            for (int i = 0; i < additionalItems.length; i++) {
                clonedArray[i] = additionalItems[i].clone();
            }
            clone.additionalItems = clonedArray;
        }
        if(contains != null) {
            WitnessContains[] clonedCArray = new WitnessContains[contains.length];
            for (int i = 0; i < contains.length; i++) {
                clonedCArray[i] = contains[i].clone();
            }
            clone.contains = clonedCArray;
        }

        return clone;
        //return null;
    }


    @Override
    public WitnessAssertion not(WitnessEnv env) throws REException {
        throw new UnsupportedOperationException("not on WitnessItemPrepared");
    }

    @Override
    public WitnessAssertion groupize() throws WitnessException, REException {
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

    /**
     * recursively applies separation on preparedItems
     * @param env collection of (variableName, variableContent) where the method add all the new variables
     * @param varManager
     * @return
     * @throws WitnessException
     * @throws REException
     */
    @Override
    public List<Map.Entry<WitnessVar, WitnessAssertion>> varNormalization_separation(WitnessEnv env, WitnessVarManager varManager) throws WitnessException, REException {
        List<Map.Entry<WitnessVar, WitnessAssertion>> newDefinitions = new LinkedList<>();

        if(items != null){
            for(int i = 0; i < items.size(); i++) {
                for (int j = 0; j < items.get(i).length; j++) {
                    if (items.get(i)[j].getClass() != WitnessBoolean.class && items.get(i)[j].getClass() != WitnessVar.class) {

                        newDefinitions.addAll(items.get(i)[j].varNormalization_separation(env, varManager));

                        WitnessVar newVar = varManager.buildVar(varManager.getName(items.get(i)[j]));
                        newDefinitions.add(new AbstractMap.SimpleEntry<>(newVar, items.get(i)[j]));

                        logger.debug("Nuova variabile "+newVar.getName()+ " sostituita a "+ items.get(i)[j]);

                        items.get(i)[j] = newVar;


                    }
                }
            }
        }

        if(additionalItems != null){
            for(int i = 0; i < additionalItems.length; i++) {
                if (additionalItems[i].getClass() != WitnessBoolean.class && additionalItems[i].getClass() != WitnessVar.class) {

                    newDefinitions.addAll(additionalItems[i].varNormalization_separation(env, varManager));

                    WitnessVar newVar = varManager.buildVar(varManager.getName(additionalItems[i]));
                    newDefinitions.add(new AbstractMap.SimpleEntry<>(newVar, additionalItems[i]));

                    logger.debug("Nuova variabile "+newVar.getName()+ " sostituita a "+ additionalItems[i]);

                    additionalItems[i] = newVar;
                }
            }
        }

        return newDefinitions;
    }

    @Override
    public WitnessAssertion varNormalization_expansion(WitnessEnv env) throws WitnessException {
//        throw new UnsupportedOperationException("tbd");
        return this;
    }

    @Override
    public WitnessAssertion DNF() throws WitnessException {
        throw new UnsupportedOperationException("tbd");
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
        throw new UnsupportedOperationException("tbd");
    }

    @Override
    public WitnessVar buildOBDD(WitnessEnv env, WitnessVarManager varManager) throws WitnessException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getReport(ReportResults reportResults) {
        for(WitnessContains c : contains)
            c.getReport(reportResults);
    }

    @Override
    public List<WitnessVar> uses() {
        List<WitnessVar> result = new LinkedList<>();
        if(additionalItems !=null)
            result.addAll(fromList(additionalItems));
        if(contains!=null)
            result.addAll(fromList(contains));
        if(items!=null)
            result.addAll(items.stream()
                    .map(liste -> fromList(liste))
                    .flatMap(List::stream)
                    .collect(Collectors.toList())
            );
        return result;
    }
}

