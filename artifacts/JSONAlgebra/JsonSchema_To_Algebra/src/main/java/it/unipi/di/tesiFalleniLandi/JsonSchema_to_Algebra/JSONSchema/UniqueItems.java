package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Boolean_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.UniqueItems_Assertion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class UniqueItems implements JSONSchemaElement{
	private boolean uniqueItems;

	private static Logger logger = LogManager.getLogger(UniqueItems.class);

	public UniqueItems(JsonElement obj){
		uniqueItems = obj.getAsBoolean();
		logger.trace("Created a new UniqueItems: {}", this);
	}

	private UniqueItems(boolean obj){
		uniqueItems = obj;
		logger.trace("Created a new UniqueItems: {}", this);
	}

	@Override
	public String toString() {
		return "UniqueItems [uniqueItems=" + uniqueItems + "]";
	}

	@Override
	public JsonObject toJSON() {
		JsonObject obj = new JsonObject();
		obj.addProperty("uniqueItems", uniqueItems);

		return obj;
	}

	@Override
	public Assertion toGrammar() {
		return uniqueItems? new UniqueItems_Assertion() : new Boolean_Assertion(true);
	}
	
	@Override
	public int numberOfTranslatableAssertions() {
		return 1;
	}

	@Override
	public UniqueItems assertionSeparation() {
		return this.clone();
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
	public UniqueItems clone() {
		return new UniqueItems(uniqueItems);
	}
}