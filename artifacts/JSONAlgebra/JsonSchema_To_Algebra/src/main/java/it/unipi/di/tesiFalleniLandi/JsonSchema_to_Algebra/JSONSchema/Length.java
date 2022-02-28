package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Len_Assertion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class Length implements JSONSchemaElement{
	private Long minLength;
	private Long maxLength;

	private static Logger logger = LogManager.getLogger(Length.class);
	
	public Length () {
		logger.trace("Creating an empty Length");
	}
	
	public void setMinLength(JsonElement obj) {
		logger.trace("Setting {} as min in {}", obj, this);
		minLength = obj.getAsLong();
	}
	
	public void setMaxLength(JsonElement obj) {
		logger.trace("Setting {} as max in {}", obj, this);
		maxLength = obj.getAsLong();
	}
	
	@Override
	public String toString() {
		return "Length [minLength=" + minLength + ", maxLength=" + maxLength + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonElement toJSON() {
		JsonObject obj = new JsonObject();
		
		if(minLength != null) obj.addProperty("minLength", minLength);
		if(maxLength != null) obj.addProperty("maxLength", maxLength);
		
		return obj;
	}

	@Override
	public Assertion toGrammar() {
		return new Len_Assertion(minLength, maxLength);
	}
	
	@Override
	public int numberOfTranslatableAssertions() {
		return 1;
	}

	@Override
	public Length assertionSeparation() {
		Length obj = new Length();
		
		if(minLength != null) obj.minLength = minLength;
		if(maxLength != null) obj.maxLength = maxLength;
		
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
	public Length clone(){
		Length newLen = new Length();

		newLen.minLength = minLength;
		newLen.maxLength = maxLength;
		
		return newLen;
	}

}
