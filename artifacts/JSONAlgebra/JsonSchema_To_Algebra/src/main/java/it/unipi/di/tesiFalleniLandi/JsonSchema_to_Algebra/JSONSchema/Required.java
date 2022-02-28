package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.UnsenseAssertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Required_Assertion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class Required implements JSONSchemaElement{
	private List<String> required;

	private static Logger logger = LogManager.getLogger(Required.class);

	public Required(JsonElement obj) {
		logger.trace("Creating a new Required by parsing {}", obj);
		if(obj.isJsonArray()){
			JsonArray array = obj.getAsJsonArray();
			required = new LinkedList<>();

			Iterator<JsonElement> it = array.iterator();

			while(it.hasNext())
				required.add(it.next().getAsString());

		} else {
			try {
				//per consistenza con draft-4
				required = new LinkedList<>();
				required.add(obj.getAsString());
			}catch (ClassCastException ex) {
				throw new UnsenseAssertion(ex.getMessage());
			}
		}

		logger.trace("Created a new  Required: {}", this);
	}

	public Required() {
		required = new LinkedList<>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonElement toJSON() {
		JsonObject obj = new JsonObject();
		JsonArray array = new JsonArray();
		
		for(String s : required)
			array.add(s);

		obj.add("required", array);

		return obj;
	}

	@Override
	public Required_Assertion toGrammar() {
		Required_Assertion req = new Required_Assertion();

		for(String s : required)
			req.add(s);

		return req;
	}
	
	@Override
	public int numberOfTranslatableAssertions() {
		return (required.size() == 0)? 0: 1;
	}

	@Override
	public Required assertionSeparation() {
		Required obj = new Required();
		
		obj.required = new LinkedList<>(this.required);
		
		return obj;
	}

	@Override
	public List<URI_JS> getRef() {
		return new LinkedList<>();
	}

	@Override
	public JSONSchema searchDef(Iterator<String> URIIterator) {
		logger.debug("searchDef: End node --> returning null");
		return null;
	}

	@Override
	public List<Entry<String,Defs>> collectDef() {
		return new LinkedList<>();
	}
	
	@Override
	public Required clone() {
		Required newReq = new Required();
		newReq.required.addAll(required);
		return newReq;
	}
}
