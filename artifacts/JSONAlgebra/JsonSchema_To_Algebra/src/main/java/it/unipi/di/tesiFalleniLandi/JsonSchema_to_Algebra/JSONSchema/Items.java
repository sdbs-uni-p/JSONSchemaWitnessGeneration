package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Boolean_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Items_Assertion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;


public class Items implements JSONSchemaElement{
	//what happens when the user defines an arbitrary subset of the three operators?
	private List<JSONSchema> items_array; 		//"items" : []
	private JSONSchema items; 					//"items" : {}
	private JSONSchema additionalItems;
	private JSONSchema unevaluatedItems_array;

	private static Logger logger = LogManager.getLogger(Items.class);
	
	public Items() {
		logger.trace("Creating an empty Items");
	}
	
	public void setItems(JsonElement obj) {
		logger.trace("Setting {} as Items in {}", obj, this);

		JsonArray array;
		
		try{
			array = obj.getAsJsonArray();
		}catch(ClassCastException | IllegalStateException e) {
			items = new JSONSchema(obj);
			return;
		}
		
		items_array = new LinkedList<>();
		
		Iterator<JsonElement> it = array.iterator();
		
		while(it.hasNext()) {
			items_array.add(new JSONSchema(it.next()));
		}
	}
	
	public void setAdditionalItems(JsonElement obj) {
		logger.trace("Setting {} as additionalItems in {}", obj, this);
		additionalItems = new JSONSchema(obj);
	}
	
	public void setUnevaluatedItems(JsonElement obj) {
		logger.trace("Setting {} as unevaluatedItems in {}", obj, this);
		unevaluatedItems_array = new JSONSchema(obj);
	}
	
	@Override
	public String toString() {
		return "Items [items_array=" + items_array + ", items=" + items + ", additionalItems_array="
				+ additionalItems + ", unevaluatedItems_array=" + unevaluatedItems_array + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonElement toJSON() {
		JsonObject obj = new JsonObject();
		
		if(items_array != null) {
			JsonArray array = new JsonArray();
			for(JSONSchema js : items_array)
				array.add(js.toJSON());
			obj.add("items", array);
		}
		
		if(additionalItems != null) obj.add("additionalItems", additionalItems.toJSON());
		
		if(unevaluatedItems_array != null) obj.add("unevaluatedItems", unevaluatedItems_array.toJSON());
		
		if(items != null) obj.add("items", items.toJSON());
		
		return obj;
	}

	@Override
	public Assertion toGrammar()  {
		Items_Assertion item = new Items_Assertion();

		/*⟨"items": [𝑆1,...,𝑆𝑛],"additionalItems":𝑆′ ⟩ = items([⟨𝑆1⟩,...,⟨𝑆𝑛⟩];⟨𝑆′⟩)*/
		if(items_array != null)
		{
			for(JSONSchema element : items_array)
				item.add(element.toGrammar());

			if(additionalItems != null)
				item.setAdditionalItems(additionalItems.toGrammar());
		}
		else /*⟨ "items" : 𝑆 ⟩ = items([]; ⟨𝑆⟩)*/
			if(items != null)
				item.setAdditionalItems(items.toGrammar());
			else
				try {
					throw new Exception("Beware: additionalItems ignored since no items : [] is present!");
				} catch (Exception e) {
					e.printStackTrace();
					return new Boolean_Assertion(true);
				}


		return item;
	}

	@Override
	public Items assertionSeparation() {
		Items obj = new Items();
		
		if(items_array != null) {
			obj.items_array = new LinkedList<>();
			for(JSONSchema s : items_array)
				obj.items_array.add(s.assertionSeparation());
		}
		
		if(items != null) obj.items = items.assertionSeparation();
		if(additionalItems != null) obj.additionalItems = additionalItems.assertionSeparation();
		if(unevaluatedItems_array != null) obj.unevaluatedItems_array = unevaluatedItems_array.assertionSeparation();
		
		return obj;
	}

	@Override
	public List<URI_JS> getRef() {
		List<URI_JS> returnList = new LinkedList<>();
		
		if(items_array != null)
			for(JSONSchema s : items_array)
				returnList.addAll(s.getRef());
		
		if(items != null) returnList.addAll(items.getRef());
		if(additionalItems != null) returnList.addAll(additionalItems.getRef());
		if(unevaluatedItems_array != null) returnList.addAll(unevaluatedItems_array.getRef());
		
		return returnList;
	}

	@Override
	public JSONSchema searchDef(Iterator<String> URIIterator) {
		if (URIIterator.hasNext()) {
			logger.debug("searchDef: searching for {}. URIIterator: {}", URIIterator.next(), URIIterator);

			switch (URIIterator.next()) {
				case "items":
					URIIterator.remove();
					try {
						if(URIIterator.hasNext()) {
							int index = Integer.parseInt(URIIterator.next());
							URIIterator.remove();
							if(items_array != null || index < items_array.size())
								return items_array.get(index).searchDef(URIIterator); //items array case
						}
					} catch (NumberFormatException e) {
						logger.catching(e);
					}
					if(items != null)
						return items.searchDef(URIIterator);  //single item case
				case "additionalItems":
					URIIterator.remove();
					if(additionalItems != null)
						return additionalItems.searchDef(URIIterator);
				case "unevaluatedItems":
					URIIterator.remove();
					if(unevaluatedItems_array != null)
						return unevaluatedItems_array.searchDef(URIIterator);
			}
		}
		return null;
	}

	@Override
	public List<Entry<String,Defs>> collectDef() {
		List<Entry<String,Defs>> returnList = new LinkedList<>();
		
		if(items_array != null) {
			for(int i = 0; i < items_array.size(); i++)
				returnList.addAll(Utils_JSONSchema.addPathElement("items/"+i, items_array.get(i).collectDef()));
		}
		
		if(items != null) returnList.addAll(Utils_JSONSchema.addPathElement("items",items.collectDef()));
		if(additionalItems != null) returnList.addAll(Utils_JSONSchema.addPathElement("additionalItems", additionalItems.collectDef()));
		if(unevaluatedItems_array != null) returnList.addAll(Utils_JSONSchema.addPathElement("unevaluatedItems", unevaluatedItems_array.collectDef()));
		
		return returnList;
	}

	@Override
	public int numberOfTranslatableAssertions() {
		int count = 0;

		if(items != null) count = items.numberOfTranslatableAssertions();

		/*
		if(items_array != null)
			for(JSONSchema s : items_array)
				if(s.numberOfTranslatableAssertions() > 0) return 1;
		*/

		if(items_array != null)
			for(JSONSchema s : items_array)
				count += s.numberOfTranslatableAssertions();

		if(additionalItems != null) count += additionalItems.numberOfTranslatableAssertions();

		if(unevaluatedItems_array != null) count += unevaluatedItems_array.numberOfTranslatableAssertions();

		return count;
	} 
	
	@Override
	public Items clone(){
		Items newItems = new Items();
		
		if(items != null)
			newItems.items = items.clone();

		if(items_array != null) {
			newItems.items_array = new LinkedList<>();
			for (JSONSchema item : items_array) {
				newItems.items_array.add(item.clone());
			}
		}

		if(additionalItems != null) newItems.additionalItems = additionalItems.clone();
		if(unevaluatedItems_array != null) newItems.unevaluatedItems_array = unevaluatedItems_array.clone();
		
		return newItems;
	}
}


