package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.AnyOf_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.OneOf_Assertion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class OneOf implements JSONSchemaElement{
	private List<JSONSchema> oneOf;

	public static Boolean asAnyOf = true;

	private static Logger logger = LogManager.getLogger(OneOf.class);
	
	public OneOf(JsonElement obj) {
		logger.trace("Creating pattern by parsing {}", obj);

		JsonArray array = obj.getAsJsonArray();
		oneOf = new LinkedList<>();
		
		Iterator<JsonElement> it = array.iterator();

		while(it.hasNext()) {
			oneOf.add(new JSONSchema(it.next()));
		}
	}
	
	public OneOf() {
		oneOf = new LinkedList<>();
		logger.trace("Creating an empty OneOf");
	}
	
	public void addElement(JSONSchema schema) {
		if(oneOf == null) oneOf = new LinkedList<>();
		logger.trace("Adding {} to {}", schema, this);
		oneOf.add(schema);
	}
	
	@Override
	public String toString() {
		return "OneOf [oneOf=" + oneOf + "]";
	}


	@SuppressWarnings("unchecked")
	@Override
	public JsonObject toJSON() {
		JsonObject obj = new JsonObject();
		JsonArray array = new JsonArray();
		
		for(JSONSchema js : oneOf)
			array.add(js.toJSON());

		obj.add("oneOf", array);

		return obj;
	}


	@Override
	public Assertion toGrammar() {
		if(OneOf.asAnyOf)
			return interpretAsAnyOf();

		OneOf_Assertion oneOf = new OneOf_Assertion();

		for(JSONSchema element : this.oneOf)
			oneOf.add(element.toGrammar());
		return oneOf;
	}


/*  The next code could be useful to measure the impact of oneOf */
	public Assertion interpretAsAnyOf() {
		if(this.oneOf.size() == 1) return this.oneOf.get(0).toGrammar();

		AnyOf_Assertion newAnyOf = new AnyOf_Assertion();

		for(JSONSchemaElement el : this.oneOf)
			newAnyOf.add(el.toGrammar());

		return newAnyOf;
	}


	@Override
	// This method is used to test for equivalence with truth when nOTA==0
	// OneOf[True] is the same as True
	// But OneOf[] and OneOf[True,True,...] are both equivalent to false
	public int numberOfTranslatableAssertions() {
		int returnValue = 0;
		if(oneOf != null) {
			for (JSONSchemaElement jse : oneOf)
				returnValue += jse.numberOfTranslatableAssertions();
		}
		if (returnValue != 0) return returnValue;
		else if (this.oneOf.size() == 1) return returnValue;
			// if size != 1 and the  numberOfTranslatableAssertions is 0
			// oneOf is still not equivalent to true
		else return 1;
	}

	@Override
	public OneOf assertionSeparation() {
		OneOf obj = new OneOf();
		
		obj.oneOf = new LinkedList<>();
		for(JSONSchema s : oneOf)
			obj.oneOf.add(s.assertionSeparation());
			
		
		return obj;
	}
	

	@Override
	public List<URI_JS> getRef() {
		List<URI_JS> returnList = new LinkedList<>();
		
		for(JSONSchema schema : oneOf)
			returnList.addAll(schema.getRef());
		
		return returnList;
	}

	@Override
	public JSONSchema searchDef(Iterator<String> URIIterator) {
		if(URIIterator.hasNext() && URIIterator.next().equals("oneOf")) {
			URIIterator.remove();
			try {
				int i = Integer.parseInt(URIIterator.next());
				logger.debug("searchDef: searching for index {} in oneOf[{}]. URIIterator: {}", i, oneOf.size(), URIIterator);
				if (i < oneOf.size()) {
					URIIterator.remove();
					return oneOf.get(i).searchDef(URIIterator);
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

		for(int i = 0; i < oneOf.size(); i++)
			returnList.addAll(Utils_JSONSchema.addPathElement(""+i, oneOf.get(i).collectDef()));


		return returnList;
	}
	
	
	@Override
	public OneOf clone(){
		OneOf clone = new OneOf();
		
		for(JSONSchema el : oneOf)
			clone.addElement(el.clone());
		
		return clone;
	}
}
