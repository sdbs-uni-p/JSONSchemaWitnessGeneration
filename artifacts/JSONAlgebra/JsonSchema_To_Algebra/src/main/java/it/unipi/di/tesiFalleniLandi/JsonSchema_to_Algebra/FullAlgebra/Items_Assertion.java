package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessAssertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessItems;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessPattReqManager;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Items_Assertion implements Assertion{
	private List<Assertion> itemsArray;
	private Assertion additionalItems;

	private static Logger logger = LogManager.getLogger(Items_Assertion.class);
	
	public Items_Assertion() {
		logger.trace("Created an empty Items_Assertion");
	}

	public void add(Assertion assertion) {
		if(itemsArray == null) itemsArray = new LinkedList<>();
		logger.trace("Adding {} to {}", assertion, this);
		itemsArray.add(assertion);
	}

	public void setAdditionalItems(Assertion additionalItems) {
		logger.trace("Setting {} as AdditionalItems in {}", additionalItems, this);
		this.additionalItems = additionalItems;
	}

	@Override
	public String toString() {
		return "Items_Assertion [itemsArray=" + itemsArray + ", additionalItems=" + additionalItems + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonObject toJSONSchema(WitnessVarManager rootVar) {
		JsonArray array = new JsonArray();
		JsonObject obj = new JsonObject();

		if(itemsArray != null) {
			for (Assertion assertion : itemsArray)
				array.add(assertion.toJSONSchema(rootVar));

			obj.add("items", array);
		}

		if(additionalItems != null) {
			if (itemsArray != null)
				obj.add("additionalItems", additionalItems.toJSONSchema(rootVar));
			else
				obj.add("items", additionalItems.toJSONSchema(rootVar));
		}
	
		return obj;
	}

	@Override
	public Assertion not() {
		if(additionalItems == null && itemsArray == null)
			add(new Boolean_Assertion(false));		//TODO: CHECK (from the negation of enum[ [] ] )

		//only additionaItems TODO
//		if(additionalItems != null && itemsArray == null) {
//			add(additionalItems);
//			additionalItems = null;
//		}
		int itemsArraySize = 0;
		if(itemsArray!=null) itemsArraySize = itemsArray.size();

		
		AllOf_Assertion rootAnd = new AllOf_Assertion();
		AnyOf_Assertion rootOr = new AnyOf_Assertion();
		Type_Assertion typeArray = new Type_Assertion();
		typeArray.add("arr");
		rootAnd.add(typeArray);
		rootAnd.add(rootOr);
		
		//ITEMS
		if (itemsArray != null) {
			for(int i = 0; i < itemsArray.size(); i++) {
				AllOf_Assertion itemAndAssertion = new AllOf_Assertion();
				Items_Assertion itemAssertion = new Items_Assertion();
				rootOr.add(itemAndAssertion);
				itemAndAssertion.add(itemAssertion);

				itemAndAssertion.add(new Exist_Assertion((long) (i+1), null, new Boolean_Assertion(true)));

				for(int j = 0; j < itemsArray.size(); j++)
					itemAssertion.add((i == j && itemsArray.get(i).not() != null) ? itemsArray.get(i).not(): new Boolean_Assertion(true));
			}

		}

		if(additionalItems == null) return rootAnd;

		//ContAfter
		Exist_Assertion contAfter = new Exist_Assertion(1L, null, (long) itemsArraySize, additionalItems.not());


		rootOr.add(contAfter);

		/*
		//ADDITIONAL ITEMS
		Boolean[] bm = new Boolean[itemsArray.size()];
		Arrays.fill(bm, false);
		Assertion notAdditionalItems = additionalItems.not();
		
		do {
			AllOf_Assertion andAdditionalItems = new AllOf_Assertion();
			rootOr.add(andAdditionalItems);
			andAdditionalItems.add(new Exist_Assertion((long) sumbit(bm) + 1, null, notAdditionalItems));
			Items_Assertion itemsAdditionalItems = new Items_Assertion();
			andAdditionalItems.add(itemsAdditionalItems);

			for(int i = 0; i < bm.length; i++)
				itemsAdditionalItems.add( (bm[i] == false) ? additionalItems : notAdditionalItems );

		}while(addbit(bm, 0));
		*/
		
		return rootAnd;
	}
	
	private Boolean addbit(Boolean[] bm, int position) {
	    if(bm.length == position) return false;
		
		if(!bm[position]) {
	    	bm[position] = true;
	    	return true;
	    }
	    
		bm[position] = false;
	    return addbit(bm, position+1);
	}
	
	private int sumbit(Boolean[] bm) {
	    int count = 0;
	    
		for(int i = 0; i < bm.length; i++)
	    	if(bm[i]) count++;
		
		return count;
	}

	@Override
	public Assertion notElimination() {
		Items_Assertion items = new Items_Assertion();

		if(itemsArray != null)
			for(Assertion item : itemsArray)
				items.add(item.notElimination());

		if(additionalItems != null)
			items.setAdditionalItems(additionalItems.notElimination());
		
		return items;
	}

	@Override
	public String toGrammarString() {
		StringBuilder str = new StringBuilder();

		if(itemsArray != null) {

			if (itemsArray.size() == 1 && additionalItems == null) {
				String tmp = itemsArray.get(0).toGrammarString();
				if(tmp.isEmpty()) return "";
				return AlgebraStrings.ITEMS(tmp, "");
			}

			Iterator<Assertion> it = itemsArray.iterator();
			String tmp = "";
			while (tmp.isEmpty() && it.hasNext()) {
				tmp = it.next().toGrammarString();
			}
			str.append(tmp);

			while (it.hasNext()) {
				tmp = it.next().toGrammarString();
				if(tmp.isEmpty()) continue;
				str.append(AlgebraStrings.COMMA)
						.append(tmp);
			}
		}
		
		String str2 = "";
		if(additionalItems != null)
			str2 = additionalItems.toGrammarString();

		if(str2.isEmpty() && str.length() == 0)
			return "";

		return AlgebraStrings.ITEMS(str.toString(), str2);
	}

	@Override
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		WitnessItems witIte = new WitnessItems();

		if(additionalItems != null) witIte.setAdditionalItems(additionalItems.toWitnessAlgebra(varManager, env, pattReqManager));

		if(itemsArray != null)
			for(Assertion a : itemsArray)
				witIte.addItems(a.toWitnessAlgebra(varManager, env, pattReqManager));

		return witIte;
	}
}
