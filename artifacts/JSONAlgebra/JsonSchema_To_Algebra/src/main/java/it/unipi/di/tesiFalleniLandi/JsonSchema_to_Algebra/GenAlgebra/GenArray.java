package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessAssertion;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra.GenAssertion.GenArrayType.EMPTY;
import static it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra.GenAssertion.GenArrayType.NOCONTAINS;
import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class GenArray implements GenAssertion {

    private JsonElement _default = new JsonArray(1);

    private JsonElement witness;
    //useful data structures
    private Integer minItems, maxItems;
    private GenArrayType genArrayType;
    private Integer tupleLength, containsLength;
    //only in the nocont case
    private List<GenVar> items;
    private GenVar additionalItems;
    private ArrayList<GenVar> satItems;

    //in the other cases
    private List<GVarMap> fItems;
    private GVarMap fAdditionalItems;
    private List<GenVar> assertionArray;

    private Integer[] afterIndexArray;
    private Integer[] minArray, maxArray;

    /*Methods*/
    /** Setters  */
    public void setMinItems(Integer minItems) {
        if (minItems < 0)
            this.minItems = 0;
        this.minItems = minItems;
    }

    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

    public void setGenArrayType(GenArrayType genArrayType) {
        this.genArrayType = genArrayType;
    }

    public void setItems(List<GenVar> items) {
        this.items = items;
    }

    public void setAdditionalItems(GenVar additionalItems) {
        this.additionalItems = additionalItems;
    }

    public void setfItems(List<GVarMap> fItems) {
        this.fItems = fItems;
    }

    public void setfAdditionalItems(GVarMap fAdditionalItems) {
        this.fAdditionalItems = fAdditionalItems;
    }

    public void setMinArray(Integer[] minArray) {
        this.minArray = minArray;
    }

    public void setMaxArray(Integer[] maxArray) {
        this.maxArray = maxArray;
    }

    public void setAssertionArray(List<GenVar> assertionArray) {
        this.assertionArray = assertionArray;
    }

    public void setTupleLength() {
        if(items!=null)
            tupleLength = items.size();
        else
            if(fItems!=null)
                tupleLength = fItems.size();
            else tupleLength = 0; //TODO verify this default case
    }

    public void setContainsLength(int l) {
            containsLength = l;
    }



    public void setafterIndexArray(Integer[] afterIndexArray){
        this.afterIndexArray = afterIndexArray;
    }


    /** Getters */


    public GenArrayType getGenArrayType() {
        return genArrayType;
    }

    @Override
    public String toString() {
        return "GenArray{" +
                "_default=" + _default +
                ", witness=" + witness +
                ", minItems=" + minItems +
                ", maxItems=" + maxItems +
                ", genArrayType=" + genArrayType +
                ", tupleLength=" + tupleLength +
                ", containsLength=" + containsLength +
                ", items=" + items +
                ", additionalItems=" + additionalItems +
                ", satItems=" + satItems +
                ", fItems=" + fItems +
                ", fAdditionalItems=" + fAdditionalItems +
                ", assertionArray=" + assertionArray +
                ", afterIndexArray=" + afterIndexArray +
                ", minArray=" + minArray +
                ", maxArray=" + maxArray +
                '}';
    }

    /** Other methods*/


    //used for encoding f_i  : 0-> v1 and v2 and co_v3 and ...
    public static class GVarMap{
        private HashMap<Integer,GenVar> funct;

        public JsonElement getWitness(Integer entry){
            return funct.get(entry).getWitness();
        }

       public GVarMap(GenEnv env, WitnessAssertion[] orItem) {
            funct = new HashMap<>();
            try {
                for (int idx = 0; idx <orItem.length; idx++)
                    funct.put(idx,env.retrieveVar(orItem[idx]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * returns an array of bitMaps whose associated variables have the status status
         * @param status
         * @return
         */
        private List<Integer> getBitMaps(statuses status){
            Iterator it = funct.entrySet().iterator();
            List<Integer> result = new ArrayList<>();
            while (it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();
                if(((GenVar)pair.getValue()).getStatus()==status)
                    result.add((Integer) pair.getKey());
//                it.remove(); // avoids a ConcurrentModificationException
            }
            return  result;//.toArray(new Integer[temporary.size()]);
        }

        public List<Integer> getBitMapsPopulated(){
            return getBitMaps(statuses.Populated);
        }

        public List<Integer> getBitMapsAllowed(){
            List<Integer> result = getBitMapsPopulated();
            result.addAll(getBitMaps(statuses.Open));
            return result;
        }

        /**
         *
         * @return
         */
        public List<GenVar> usedVars(){
            return funct.values().stream().collect(Collectors.toList());
        }
    }

    /*constructors*/

    //TODO fill
    public GenArray() {
        this.genArrayType = EMPTY;
        this.minItems = 0;
        this.maxItems = Integer.MAX_VALUE;

    }


    /*auxiliary functions*/

    /**
     * Gets i th bit of bitmap
     * @param BitMap
     * @param index
     * @return
     */
    private Integer getBit(Integer BitMap, int index){
//        double r = (BitMap / Math.pow(2,index)) %2;
        if((BitMap / (int) Math.pow(2,index)) %2==0)
            return 0;
        else
            return 1;
    }


    /**
     * checks whether the max items if violated for a given request
     * @param bitMapSum
     * @return
     */
    private boolean overflow(Integer[] bitMapSum){
        for (int j=0; j< containsLength; j++)
            if(bitMapSum[j]>maxArray[containsLength-1-j])
                return true;
        return false;
    }

    /**
     * checks whether a given combination is a solution
     * @param bitMapSum
     * @return
     */
    private boolean isASolution(Integer[] bitMapSum){
        for (int j=0; j< containsLength; j++)
            if(bitMapSum[j]>maxArray[containsLength-1-j] || bitMapSum[j]<minArray[containsLength-1-j])
                return false;
        return true;
    }

    /**
     * returns the currentBitMap + candidate when the position constraint is verified
     * @param currentBitMapSum BEWARE: IN THE BITMAP THE BIT 0 (the one for 2^0, the rightmost)
     *                         is associated to assertion assertion/afterIndex[containsLength-1]
     *                         hence the limit for the bit j are found in
     * @param candidate is a bitmap that indicates which assertions are satisfied by the candidate
     * @param position starts from 1
     * @return returns the new array that indicates how many elements satisfy each assertion
     */
    private Integer[] bitMapAdd(Integer[] currentBitMapSum, Integer candidate, Integer position ){
        Integer[] result = new Integer[containsLength];
        for(int j=0; j<containsLength; j++)
            if(position>afterIndexArray[containsLength-1-j])
                result[j]=currentBitMapSum[j]+getBit(candidate,j);
            else
                result[j]=currentBitMapSum[j];
        return result;
    }

    /**
     * recursive function for instantiating a possible solution of length leftLength using fItems
     * rightBitMapSum used for verifying the requests
     * Update: generate a solution of length "position" by guessing the candidate for position
     * "position" (starting from 1) and using a recursive call for positions 1...pos-1
     * @param leftLength we assume we have a tail from leftLength+1 onward described by rightBitMapSum
     *                  we guess a candidate for position leftLength-1 (counting from 0) and return a list
     *                   list with leftLength elements
     * @param rightBitMapSum we return a list of the first "leftLength" bitmaps, the tail is described by
     *                       rightBitMapSum
     * @param fItems is an array such that fItems[i]=[01,10,11] means that [01,11,11] are the
     *               bitmaps/variables that are populated for position i (counting from 0)
     *               where i in [0,...,tupleLength+1]
     * @return returns a list of bitmaps each indicating the bitmap/variable chosen for the positions
     *         from 0 to position-1 - the returned list should be completed with a tail that satisfies
     *         rightBitMapSum
     */
    private List<Integer> solve(Integer leftLength, Integer[] rightBitMapSum, List<Integer>[] fItems){
        List<Integer> result = null;
        Integer[] bitMapSum;

        if(leftLength==0)
            if(isASolution(rightBitMapSum))
                return new LinkedList<>();
            else
                return null;

            // the last element in a list of length "len" is described by FItem[len-1]
        // but if the position > tupleLength, its list of populated
        // bitmaps is described by fItems[tupleLength]
        Integer positionInFItem = leftLength-1;
        if(positionInFItem>tupleLength)
            positionInFItem=tupleLength;

        //examine the tail and recursively examine the head
        //current is the list of candidates for the current position
        List<Integer> currentBitMapList = fItems[positionInFItem];
        for(int c=0; c<currentBitMapList.size(); c++)
        {
            Integer candidateBitMap = currentBitMapList.get(c);
//           bitMapSum counts positions starting from 1, for this reason we use leftLength
            bitMapSum = bitMapAdd(rightBitMapSum, candidateBitMap, leftLength);
            if(! overflow(bitMapSum)){
                result = solve(leftLength-1, bitMapSum, fItems);
                if(result!=null) {
                    result.add(candidateBitMap);
                    break; // GG 23/1/2022
                }
            }
        }
        return result;
    }



    /**
     * initializes an array of len 0-integers
     * @param len
     * @return
     */
    private Integer[] zeroBitMap(int len){
        Integer[] res = new Integer[len];
        for(int i=0; i<len; i++)
            res[i] = 0;
        return res;
    }

    /**
     *
     * @param solution
     */
    private JsonArray synthesizeWitness(List<Integer> solution){
        JsonArray result = new JsonArray();
        int len = solution.size();
        for(int i=len-1; i>=0; i--)
            if(i>=tupleLength)
                result.add(fAdditionalItems.getWitness(solution.get(i)));
            else
                result.add(fItems.get(i).getWitness(solution.get(i)));

//        for(int i=0; i<min(tupleLength,len); i++)
//            result.add(fItems.get(i).getWitness(solution.get(i)));
//        if(len>tupleLength)
//            result.add(fAdditionalItems.getWitness(len-1));

        //reverse order TODO temporary
        JsonArray finalResult = new JsonArray();
        for(int j=1; j<=len; j++)
            finalResult.add(result.get(len-j));

        return finalResult;
    }

    /*witness generation*/

    /**
     *
     * @param len
     * @param tuple
     * @return
     */
    private JsonArray synthesizeWitness(int len, List<GenVar> tuple, GenVar addItems){
        JsonArray result = new JsonArray();
        for(int i=0; i<min(len, tuple.size()); i++)
            result.add(tuple.get(i).getWitness());
        //if len(tuple) < len, complete with default values
        for(int j=result.size();j<len; j++)
            if(addItems !=null)
                result.add(addItems.getWitness());
                else
                result.add(_default);
        return result;
    }
//
//    /**No contains case*/
//    private statuses generateArrayNoItems() {
//        JsonArray result = new JsonArray();
//
//        if(containsVar.isEmpty())
//            return statuses.Empty;
//        else if(containsVar.isOpen())
//            return statuses.Open;
//        else
//            for(int i=0; i<minItems; i++)
//                result.add(containsVar.getWitness());
//        witness = result;
//        return statuses.Populated;
//    }

    /**No contains case*/
    private statuses generateArrayNoCont() {
        if (minItems > tupleLength)
            if (additionalItems instanceof GenVarFalse || additionalItems.isEmpty())
                return statuses.Empty;
            else if (additionalItems.isOpen())
                return statuses.Open;

        int prefixLen = min(minItems, tupleLength);
        GenVar currentVar;
        //find a solution
        for (int i = 0; i < prefixLen; i++) {
            currentVar = items.get(i);
            if (currentVar.isEmpty())
                return statuses.Empty;
            else if (currentVar.isOpen())
                return statuses.Open;
        }
        //all variables are populated in the tuple TODO check
        if(prefixLen<minItems)
            if(additionalItems instanceof GenVar){
                witness = synthesizeWitness(minItems,items,additionalItems);
                return statuses.Populated;
            }
        witness = synthesizeWitness(minItems,items,null);
        return statuses.Populated;

    }

    /**General case */

    private statuses generateArrayGeneral(){
        int sumMinArray = 0;
        for(int i=0; i<minArray.length;i++)
            sumMinArray+=minArray[i]+afterIndexArray[i];
//        double upLimit = tupleLength + min(maxItems,max(minItems, sumMinArray));
        int intUpLimit = sumMinArray + tupleLength;
        double upLimit = min(maxItems,max(minItems, intUpLimit));
        if(upLimit==0)
           if (minItems ==0 && sumMinArray==0)
               return generateEmptyArray();
           else return statuses.Empty; // if we arrive here, then maxItems == 0
        //popList and openList are arrays of integer lists
        List<Integer>[] populatedArray, allowedArray;
        List<Integer> solution = null;
        List<List<Integer>> populatedList, allowedList;

        //popList
        populatedList = fItems.stream().map(item -> item.getBitMapsPopulated()).collect(Collectors.toList());
        populatedList.add(fAdditionalItems.getBitMapsPopulated());
        populatedArray = populatedList.toArray(List[]::new);

        //solution
        //try with popList
        //popList[i] = list of bitmaps that correspond to position i counting from 0
        //up to tupleLength
        for(int l = minItems; l<=upLimit && solution ==null; l++){
            solution = solve(l, zeroBitMap(containsLength),populatedArray);
            if(solution!=null)
            {
                this.witness = synthesizeWitness(solution);
                return statuses.Populated;
            }
        }
        //try with openList
        //GG: I commented this out: since the allowedArray may be big, hence the cost
        // of the optimization may easily beat the benefit
        /*  allowedList = fItems.stream().map(item -> item.getBitMapsAllowed()).collect(Collectors.toList());
            allowedList.add(fAdditionalItems.getBitMapsAllowed());
            allowedArray = allowedList.toArray(List[]::new);
            for(int l = minItems; l<=upLimit && solution ==null; l++){
            solution = solve(l, zeroBitMap(containsLength),allowedArray);
            if(solution!=null)
                return statuses.Open;
            else
                return statuses.Empty;
        }*/
        //empty array
        return statuses.Open;
    }


    @Override
    public statuses generate() {
        switch (genArrayType){
            case EMPTY:
                return generateEmptyArray();
            case NOCONTAINS:
                return generateArrayNoCont();//always generates the empty array
            case GENERALCASE:
                return generateArrayGeneral();
            default:
                return generateArrayGeneral();
        }
    }




   public statuses generateEmptyArray() {
        witness = _default;
        return statuses.Populated;
    }

    public JsonElement getWitness() {
        return witness;
    }

    @Override
    public JsonElement generateNext() {
        return null;
    }

    @Override
    public WitnessAssertion toWitnessAlgebra() {
        return null;
    }

    /**
     * TODO deal with the remaining cases
     * @return
     */
    @Override
    public List<GenVar> usedVars() {
        return usedVarsTemp();//new LinkedList<>();
    }
    public List<GenVar> usedVarsTemp() {
        List<GenVar> used = new LinkedList<>();
        if(genArrayType== NOCONTAINS)
        {
            if(items !=null)
                used.addAll(items);
            if(additionalItems !=null)
                used.add(additionalItems);
        }
        else {
            if(fItems !=null)
                used.addAll(fItems.stream()
                    .flatMap(orItem->orItem.usedVars().stream())
                    .collect(Collectors.toList()));
            if(fAdditionalItems !=null)
                used.addAll(fAdditionalItems.usedVars());
        }
        return used;
    }



}
