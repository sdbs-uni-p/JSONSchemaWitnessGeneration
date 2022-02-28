package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonElement;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.LinkedList;
import java.util.List;

public class ItemsPrepared_Assertion implements Assertion{
    private static Logger logger = LogManager.getLogger(WitnessItemsPrepared.class);

    //items#(f1 · · · fn; f | #^M1_m1:ref(x)1, . . . , #^Mk_mk:ref(x)k)

    private List<Assertion[]> items;
    private Assertion[] additionalItems;
    private Exist_Assertion[] contains;

    public ItemsPrepared_Assertion(){
        items = new LinkedList<>();
        contains = new Exist_Assertion[0];
        additionalItems = new Assertion[0];
    }

    public void addItems(Assertion[] itemArray){
        items.add(itemArray);
    }

    public void addContains(Exist_Assertion[] containsArray){
        contains = containsArray;
    }

    public void addAdditionalItems(Assertion[] addionalItemsArray){
        additionalItems = addionalItemsArray;
    }

    @Override
    public JsonElement toJSONSchema(WitnessVarManager rootVar) {
        throw new UnsupportedOperationException("tbd");
    }

    @Override
    public Assertion not() {
        throw new UnsupportedOperationException("tbd");
    }

    @Override
    public Assertion notElimination() {
        throw new UnsupportedOperationException("tbd");
        /*
        for(int i = 0; i < items.size(); i++)
            for(int j = 0; j < items.get(i).length; j++)
                items.get(i)[j] = items.get(i)[j].notElimination();

        for(int i = 0; i < additionalItems.length; i++)
            additionalItems[i] = additionalItems[i].notElimination();

        for(int i = 0; i < contains.length; i++)
            contains[i] = (Exist_Assertion) contains[i].notElimination();
         */
    }

    @Override
    public String toGrammarString() {
        List<String[]> itemsStr = new LinkedList<>();
        String[] additionalItemsStr = new String[additionalItems.length];
        String[] containsStr = new String[contains.length];

        for(int i = 0; i < items.size(); i++) {
            String[] tmpList = new String[items.get(i).length];
            for (int j = 0; j < items.get(i).length; j++) {
                tmpList[j] = items.get(i)[j].toGrammarString();
            }
            itemsStr.add(tmpList);
        }

        for (int i = 0; i < additionalItems.length; i++)
            additionalItemsStr[i] = additionalItems[i].toGrammarString();

        for (int i = 0; i < contains.length; i++)
            containsStr[i] = contains[i].toGrammarString();

        return AlgebraStrings.ITEMSPREPARED(itemsStr, additionalItemsStr, containsStr);
    }

    @Override
    public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
        return null;
    }
}
