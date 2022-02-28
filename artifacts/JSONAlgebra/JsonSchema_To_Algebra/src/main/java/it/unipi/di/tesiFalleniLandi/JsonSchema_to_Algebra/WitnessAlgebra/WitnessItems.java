package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Boolean_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Items_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.*;
import java.util.stream.Collectors;

public class WitnessItems implements WitnessAssertion{
    private static Logger logger = LogManager.getLogger(WitnessItems.class);

    private List<WitnessAssertion> items;
    private WitnessAssertion additionalItems;
    private boolean lastItemIsFalse;

    public WitnessItems() {
        lastItemIsFalse = false;
        items = new LinkedList<>();
        logger.trace("Creating an empty WitnessItems");
    }


    /**
     * TODO check behavior and usage: does it overwrite or append? DONE: it overwrites
     * @param addItem
     */
    public void setAdditionalItems(WitnessAssertion addItem){
        logger.trace("Setting {} as AdditionalItems in {}", addItem, this);
        if(!lastItemIsFalse) {
            if(addItem.getClass() == WitnessAnd.class && ((WitnessAnd) addItem).getIfUnitaryAnd() != null)
                addItem = ((WitnessAnd) addItem).getIfUnitaryAnd();
            additionalItems = addItem;
        }
        else
            logger.warn("Items is blocked --> no action ");
    }

    /**
     * checks whether the item to be added is a boolean assertion with false
     * otherwise adds the item
     *
     * @param item
     */
    public void addItems(WitnessAssertion item){
        logger.trace("Adding {} into Items in {}", item, this);
        try{//if item == false
            if(!((WitnessBoolean)item).getValue()) {
                lastItemIsFalse = true;
                additionalItems = item;
                logger.warn("Setting blocked=true because toAdd={}", item);
                return;
            }
        }catch (ClassCastException e) { }

        //
        if (!lastItemIsFalse) {
            if (item.getClass() == WitnessAnd.class && ((WitnessAnd) item).getIfUnitaryAnd() != null)
                item = ((WitnessAnd) item).getIfUnitaryAnd();
            items.add(item);
        }else
            logger.warn("LastItemIsFalse --> no action ");
    }

    @Override
    public String toString() {
        return "Items{" +
                "items=" + items +
                ", additionalItems=" + additionalItems +
                '}';
    }

    @Override
    public void checkLoopRef(WitnessEnv env, Collection<WitnessVar> varList) throws RuntimeException {
        return;
    }

    @Override
    public void reachableRefs(Set<WitnessVar> collectedVar, WitnessEnv env) throws RuntimeException {
        for(WitnessAssertion item : items)
            item.reachableRefs(collectedVar, env);

        if(additionalItems != null)
            additionalItems.reachableRefs(collectedVar, env);
    }

    /**
     * Note: start by optimizing the calling object's data-structures
     * null indicates "failure" of the merge
     * @param a
     * @param varManager
     * @param pattReqManager
     * @return
     * @throws REException
     */
    @Override
    public WitnessAssertion mergeWith(WitnessAssertion a, WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException {
        WitnessAssertion result = null;

        if(items.size() == 0 && additionalItems instanceof WitnessBoolean)
            //additionalItems = true TODO add a unit test : all{ items[;true] , items[;string]} = item[; string]
            if(((WitnessBoolean)additionalItems).getValue())
                result = a;//additionalItems;
            else {//additionalItems = false -- //gg if a is WitnessItems then it gets absorbed
            //gg    if (a.getClass() == WitnessItems.class)
            //gg            result = this;
            //gg    else {
                        //here we transform items[false] in contains(minItems = maxItems = 0)
                        WitnessAnd and = new WitnessAnd();
                        and.add(a);
                        and.add(new WitnessContains(0l, 0l, new WitnessBoolean(true)));
                        result = and;
            //gg    }

            }
        else {//Note: removes from the tail element when it is identical to additionalItems
            for (int i = items.size() - 1; i >= 0; i--)
                if (additionalItems != null && additionalItems.equals(items.get(i)))
                    items.remove(i);
                else
                    break;
            //merge
            if (a.getClass() == this.getClass())
                result = this.mergeElement((WitnessItems) a, varManager, pattReqManager);
        }

        logger.warn("Merge result: {}", result);
        return result;
    }

    @Override
    public WitnessAssertion merge(WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException {
        WitnessItems newWitnessItems = new WitnessItems();
        for(WitnessAssertion item : items)
            newWitnessItems.addItems(item.merge(varManager, pattReqManager));

        if(additionalItems != null)
            newWitnessItems.setAdditionalItems(additionalItems.merge(varManager, pattReqManager));

        newWitnessItems.lastItemIsFalse = lastItemIsFalse;

        return newWitnessItems;
    }


    /***
     * removes items from the head:
     * - when subsumed with addItems
     * - when they are false, in this case set addItems to false
     * TODO exit the loop
     */
    public void mergeHeadWithTail(){
        //Here we rewrite items[z,u,z,z;z] as items[z,u;z]
        for (int i = items.size() - 1; i >= 0; i--)
            if (additionalItems != null && additionalItems.equals(items.get(i)))
                items.remove(i);
            else break;
            // here, we rewrite items[x,y,false,u,w;z]  as items[x,y;false]
        for (int i = items.size() - 1; i >= 0; i--)
            if (items.get(i).getClass() == WitnessBoolean.class && ((WitnessBoolean) items.get(i)).getValue() == false)
            {
                for (int j = items.size() - 1; j >= i; j--)
                    items.remove(j);
                additionalItems = new WitnessBoolean(false);
            }
    }

    /**
     * specific to WitnessItems
     * TODO find whether  order is switched
     * @param a
     * @param varManager
     * @param pattReqManager
     * @return
     * @throws REException
     */
    public WitnessAssertion mergeElement(WitnessItems a, WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException{
        logger.trace("Merging {} with {}", a, this);

        //a = items[;bool]
        if (a.items.size() == 0 && a.additionalItems.getClass() == WitnessBoolean.class)
            if (((WitnessBoolean) a.additionalItems).getValue() == true) {
                logger.trace("Merge result: {}", a.additionalItems);
//                return a.additionalItems; //return true: this items is always true
                return this;
            }else { // a = items[;false] that is empty arrays only - return and(contains(0,0;true)
                WitnessAnd and = new WitnessAnd();

                and.add(a);
                and.add(new WitnessContains(0l, 0l, new WitnessBoolean(true)));
                logger.trace("Merge result: {}", and);
                return and; //it is true iff array size is empty
            }

        mergeHeadWithTail(); // why do we run here this optimization?

        WitnessItems ite = new WitnessItems();

        ite.additionalItems = a.additionalItems;
        if (a.additionalItems == null) //This means additionalItems == True
            ite.additionalItems = additionalItems;
        // if this.additionalItems == null that we already have ite.additionalItems = a.additionalItems;
        // we only miss the not null - not null case
        if (a.additionalItems != null && this.additionalItems != null){
            WitnessAssertion res = a.additionalItems.clone().mergeWith(this.additionalItems, varManager, pattReqManager);
           //if merge fails then keep the conjunction of the two
            if(res!= null)
                ite.additionalItems = res;
            else
            {
                WitnessAnd and = new WitnessAnd();
                and.add(a.additionalItems.clone());
                and.add(this.additionalItems);
                ite.additionalItems = and;
            }
        }

        /**
         * pairwise merge of items in array of possibly different lengths
         * a1 = [it1, ..., itn ; addItems]
         * a2 = [it'1,..., it'm; addItems']
         * where n<=m
         * when merge not possible, return And(iti, it'i)
         */
        /**
         * common head
         */
        int i;
        int min = (a.items.size() < items.size()) ? a.items.size() : items.size();
        for (i = 0; i < min; i++) {
            WitnessAssertion tmp = items.get(i).mergeWith(a.items.get(i), varManager, pattReqManager);
            if(tmp != null) //merge was possible
                ite.addItems(tmp);
            else{
                WitnessAnd and = new WitnessAnd();
                and.add(items.get(i));
                and.add(a.items.get(i));
                ite.addItems(and);
            }
        }

        /*
         * tail part
         * */

        // branch 1 TODO externalize as a function invoke both because symmetric
        for(; i < a.items.size(); i++) {
            if(this.additionalItems==null)
                ite.addItems(a.items.get(i)); //return this
            else {
                WitnessAssertion tmp = a.items.get(i).mergeWith(this.additionalItems, varManager, pattReqManager);
                if (tmp != null)
                    ite.addItems(tmp);
//            else //TODO combine this
//                ite.addItems(a.items.get(i));
                else { //merge failure
                    WitnessAnd and = new WitnessAnd();
                    and.add(additionalItems);
                    and.add(a.items.get(i));
                    ite.addItems(and);
                }
            }
        }

        // or  branch 2 : i = min
        for(; i < this.items.size(); i++) {
            if(a.additionalItems==null)
                ite.addItems(items.get(i)); //return this
            else
            {
                WitnessAssertion tmp = this.items.get(i).mergeWith(a.additionalItems, varManager, pattReqManager);
                if(tmp != null)
                    ite.addItems(tmp);
//            else //TODO combine this
//                ite.addItems(a.items.get(i));
                else{ //merge failure
                    WitnessAnd and = new WitnessAnd();
                    and.add(this.items.get(i));
                    and.add(a.additionalItems);
                    ite.addItems(and);
                }
            }


        }

//        if(a.additionalItems!=null)
//            for(; i < items.size(); i++) {
//                WitnessAssertion tmp = items.get(i).mergeWith(a.additionalItems, varManager, pattReqManager);
//                if(tmp != null)
//                    ite.addItems(tmp);
//                else
//                    ite.addItems(items.get(i));
//            }


        logger.trace("Merge result: {}", ite);
        return ite;
    }

    /**
     * this method is used in WIP for normalizing items by cutting them to the max indicated in c
     * returns WitnessItems but does not clone
     * @param c
     * @return
     */
    protected WitnessItems mergeElement(WitnessContains c){
        if(c.getContains() instanceof Boolean_Assertion && ((WitnessBoolean) c.getContains()).getValue()){
            Double max = c.getMax();
            if(!max.isInfinite()) {

                if(max > items.size())
                    //Allunghiamo l'items fino a max
                    /*while(items.size() != max)
                        items.add(additionalItems);*/;
                else{
                    //Tagliamo items
                    while(items.size() != max)
                        items.remove(items.size()-1);

                    additionalItems = new WitnessBoolean(false);
                }

            }
        }

        return this;
    }

    @Override
    public WitnessType getGroupType() {
        return new WitnessType(AlgebraStrings.TYPE_ARRAY);
    }

    public List<WitnessAssertion> getItems() {
        return items;
    }

    public WitnessAssertion getAdditionalItems() {
        return additionalItems;
    }

    @Override
    public Assertion getFullAlgebra() {
        Items_Assertion items = new Items_Assertion();

        for(WitnessAssertion a : this.items)
            items.add(a.getFullAlgebra());

        if(additionalItems != null)
            items.setAdditionalItems(additionalItems.getFullAlgebra());

        return items;
    }

    @Override
    public WitnessAssertion clone() {
        logger.trace("Cloning WitnessItems: {}", this);
        WitnessItems clone = new WitnessItems();
        for(WitnessAssertion assertion : items)
            clone.items.add(assertion.clone());

        clone.lastItemIsFalse = lastItemIsFalse;

        if(additionalItems != null)
            clone.additionalItems = additionalItems.clone();

        return clone;
    }

    @Override
    public WitnessAssertion not(WitnessEnv env) throws REException {
        //only additionaItems
        /*if(additionalItems != null && items == null) {
            addItems(additionalItems);
            additionalItems = null;
        }*/

        WitnessAnd rootAnd = new WitnessAnd();
        WitnessOr rootOr = new WitnessOr();
        WitnessType typeArray = new WitnessType();
        typeArray.add("arr");
        rootAnd.add(typeArray);
        //rootAnd.add(rootOr);

        //ITEMS
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                WitnessAnd itemAndAssertion = new WitnessAnd();
                WitnessItems itemAssertion = new WitnessItems();
                itemAndAssertion.add(itemAssertion);

                itemAndAssertion.add(new WitnessContains(i + 1., Double.POSITIVE_INFINITY, new WitnessBoolean(true)));
                rootOr.add(itemAndAssertion);

                for (int j = 0; j < i; j++)
                    itemAssertion.addItems(new WitnessBoolean(true));
                itemAssertion.addItems(items.get(i).not(env));

                /*for (int j = 0; j < items.size(); j++)
                    itemAssertion.addItems((i == j && items.get(i).not(env) != null) ? items.get(i).not(env) : new WitnessBoolean(true));
                */
            }
        }

        if(additionalItems != null) {

            //ContAfter
            long afterShift = 0;
            if (items != null)
                afterShift = (long) items.size();

            WitnessContains contAfter = new WitnessContains(1L, null, afterShift, additionalItems.not(env));
            rootOr.add(contAfter);
        }

        /*
        //ADDITIONAL ITEMS
        Boolean[] bm = new Boolean[items.size()];
        Arrays.fill(bm, false);
        WitnessAssertion notAdditionalItems = additionalItems.not(env);

        do {
            WitnessAnd andAdditionalItems = new WitnessAnd();
            rootOr.add(andAdditionalItems);
            andAdditionalItems.add(new WitnessContains((long) sumbit(bm) + 1, null, notAdditionalItems));
            WitnessItems itemsAdditionalItems = new WitnessItems();
            andAdditionalItems.add(itemsAdditionalItems);

            for(int i = 0; i < bm.length; i++)
                itemsAdditionalItems.addItems( (bm[i] == false) ? additionalItems : notAdditionalItems );

        }while(addbit(bm, 0));
         */

        rootAnd.add(rootOr);
        return rootAnd;
    }

//    private Boolean addbit(Boolean[] bm, int position) {
//        if(bm.length == position) return false;
//
//        if(!bm[position]) {
//            bm[position] = true;
//            return true;
//        }
//
//        bm[position] = false;
//        return addbit(bm, position+1);
//    }
//
//    private int sumbit(Boolean[] bm) {
//        int count = 0;
//
//        for(int i = 0; i < bm.length; i++)
//            if(bm[i]) count++;
//
//        return count;
//    }

    @Override
    public WitnessAssertion groupize() throws WitnessException, REException {
        WitnessItems items = new WitnessItems();

        if(additionalItems != null)
            if(additionalItems.getClass() != WitnessAnd.class){
                WitnessAnd and = new WitnessAnd();
                and.add(additionalItems);
                items.additionalItems = and.groupize();
            }else
                items.additionalItems = additionalItems.groupize();

        for(WitnessAssertion assertion : this.items)
            if(assertion.getClass() != WitnessAnd.class){
                WitnessAnd and = new WitnessAnd();
                and.add(assertion);
                items.addItems(and.groupize());
            }else
                items.addItems(assertion.groupize());

        return items;
    }

    @Override
    public Float countVarWithoutBDD(WitnessEnv env, List<WitnessVar> visitedVar) {
        Float count = 0f;
        if(items != null){
            for(int i = 0; i < items.size(); i++){
                count += items.get(i).countVarWithoutBDD(env, visitedVar);
            }
        }

        if(additionalItems != null)
            count += additionalItems.countVarWithoutBDD(env, visitedVar);

        return count;
    }

    @Override
    public int countVarToBeExp(WitnessEnv env) {
        return 0;
    }

    @Override
    public List<Map.Entry<WitnessVar, WitnessAssertion>> varNormalization_separation(WitnessEnv env, WitnessVarManager varManager) throws WitnessException, REException {
        List<Map.Entry<WitnessVar, WitnessAssertion>> newDefinitions = new LinkedList<>();

        if(items != null){
            for(int i = 0; i < items.size(); i++){
                if(items.get(i).getClass() != WitnessBoolean.class && items.get(i).getClass() != WitnessVar.class) {

                    newDefinitions.addAll(items.get(i).varNormalization_separation(env, varManager));

                    WitnessVar newVar = varManager.buildVar(varManager.getName(items.get(i)));
                    newDefinitions.add(new AbstractMap.SimpleEntry<>(newVar, items.get(i)));
                    items.set(i, newVar);
                }
            }
        }

        if(additionalItems != null){
            if(additionalItems.getClass() != WitnessBoolean.class && additionalItems.getClass() != WitnessVar.class) {

                newDefinitions.addAll(additionalItems.varNormalization_separation(env, varManager));

                WitnessVar newVar = varManager.buildVar(varManager.getName(additionalItems));
                newDefinitions.add(new AbstractMap.SimpleEntry<>(newVar, additionalItems));
                additionalItems = newVar;
            }
        }

        return newDefinitions;
    }

    @Override
    public WitnessAssertion varNormalization_expansion(WitnessEnv env) {
        return this;
    }

    @Override
    public WitnessAssertion DNF() throws WitnessException {
        WitnessItems newItems = new WitnessItems();

        if(items != null){
            for(WitnessAssertion assertion : items)
                newItems.addItems(assertion.DNF());
        }

        if(additionalItems != null)
            newItems.setAdditionalItems(additionalItems.DNF());

        return newItems;
    }

    @Override
    public WitnessAssertion toOrPattReq() {
        if(items!=null)
            for(int i = 0; i < items.size(); i++)
                items.set(i, items.get(i).toOrPattReq());

        //TODO check if needed
        if(additionalItems!=null)
            additionalItems = additionalItems.toOrPattReq();

        return this;
    }

    @Override
    public boolean isBooleanExp() {
        return false;
    }

    @Override
    public boolean isRecursive(WitnessEnv env, LinkedList<WitnessVar> visitedVar) {
        if(items != null){
            for(int i = 0; i < items.size(); i++){
                if(items.get(i).isRecursive(env, visitedVar))
                    return true;
            }
        }

        if(additionalItems != null && additionalItems.isRecursive(env, visitedVar))
            return true;

        return false;
    }

    @Override
    public WitnessVar buildOBDD(WitnessEnv env, WitnessVarManager varManager) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getReport(ReportResults reportResults) {
        if(items != null){
            for(int i = 0; i < items.size(); i++){
                items.get(i).getReport(reportResults);
            }
        }

        if(additionalItems != null)
            additionalItems.getReport(reportResults);
    }

    @Override
    public List<WitnessVar> uses() {
        HashSet<WitnessVar> results = new HashSet<>();
        for(WitnessAssertion assertion: items)
            results.addAll(assertion.uses());
        if(additionalItems!=null)
            results.addAll(additionalItems.uses());
//        List<WitnessVar> result = items.stream()
//                .map(witnessAssertion -> witnessAssertion.uses())
//                .flatMap(List::stream)
//                .collect(Collectors.toList());
//        if(additionalItems!=null)
//            result.addAll(additionalItems.uses());

        return new ArrayList(results);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WitnessItems that = (WitnessItems) o;

        if (lastItemIsFalse != that.lastItemIsFalse) return false;

        if(!Objects.equals(that.additionalItems, additionalItems)) return false;

        if(that.items.size() != items.size()) return false;

           for (int i=0;i < items.size();i++) {
            if (!items.get(i).equals(that.items.get(i))) return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = items != null ? items.hashCode() : 0;
        result = 31 * result + (lastItemIsFalse ? 1 : 0);
        result = 31 * result + (additionalItems != null ? additionalItems.hashCode() : 0);
        return result;
    }
}