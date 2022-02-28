package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.AnyOf_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class AnyOf implements JSONSchemaElement{
	private List<JSONSchema> anyOf;

	private static Logger logger = LogManager.getLogger(AnyOf.class);

	public AnyOf(JsonElement obj) {
		logger.trace("Creating a new AnyOf by parsing {}", obj);

		JsonArray array = obj.getAsJsonArray();
		anyOf = new LinkedList<>();
		
		Iterator<JsonElement> it = array.iterator();
		
		while(it.hasNext()) {
			anyOf.add(new JSONSchema(it.next()));
		}
	}

	public AnyOf() {
		logger.trace("Creating an empty AnyOf");
		anyOf = new LinkedList<>();
	}
	
	public void addElement(JSONSchema schema) {
		if(anyOf == null) anyOf = new LinkedList<>();

		logger.trace("Adding {} to {}", schema, this);

		anyOf.add(schema);
	}

	@Override
	public String toString() {
		return "AnyOf [anyOf=" + anyOf + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonElement toJSON() {
		JsonObject obj = new JsonObject();
		JsonArray array = new JsonArray();
		
		for(JSONSchema js : anyOf)
			array.add(js.toJSON());

		obj.add("anyOf", array);
		return obj;
	}

	public Assertion toGrammar() {
		if(anyOf.size() == 1) return anyOf.get(0).toGrammar();

		AnyOf_Assertion newAnyOf = new AnyOf_Assertion();

		for(JSONSchemaElement el : anyOf)
			newAnyOf.add(el.toGrammar());

		return newAnyOf;
	}
	
	@Override
	public int numberOfTranslatableAssertions() {
		int returnValue = 0;
		if(anyOf != null)
			for(JSONSchemaElement jse : anyOf)
				returnValue += jse.numberOfTranslatableAssertions();

		return returnValue;
	}

	@Override
	public AnyOf assertionSeparation() {
		AnyOf obj = new AnyOf();
		
		obj.anyOf = new LinkedList<>();
		for(JSONSchema s : anyOf)
			obj.anyOf.add(s.assertionSeparation());
		
		return obj;
	}

	@Override
	public List<URI_JS> getRef() {
		List<URI_JS> returnList = new LinkedList<>();
		
		Iterator<JSONSchema> it = anyOf.iterator();
		while(it.hasNext())
			returnList.addAll(it.next().getRef());
		
		return returnList;
	}

	@Override
	public JSONSchema searchDef(Iterator<String> URIIterator) {
		if(URIIterator.hasNext() && URIIterator.next().equals("anyOf")) {
			URIIterator.remove();
			try {
				int i = Integer.parseInt(URIIterator.next());
				logger.debug("searchDef: searching for index {} in anyOf[{}]. URIIterator: {}", i, anyOf.size(), URIIterator);
				if (i < anyOf.size()) {
					URIIterator.remove();
					return anyOf.get(i).searchDef(URIIterator);
				}
			} catch (ClassCastException | NumberFormatException e) {
				logger.catching(e); //error in the ref URI
			}
		}

		return null;
	}


	@Override
	public List<Entry<String,Defs>> collectDef() {
		LinkedList<Entry<String, Defs>> returnList = new LinkedList<>();

		for(int i = 0; i < anyOf.size(); i++)
			returnList.addAll(Utils_JSONSchema.addPathElement(""+i, anyOf.get(i).collectDef()));

		return returnList;
	}
	
	@Override
	public AnyOf clone(){
		AnyOf clone = new AnyOf();
		
		for(JSONSchema el : anyOf)
			clone.addElement(el.clone());
		
		return clone;
	}
}
