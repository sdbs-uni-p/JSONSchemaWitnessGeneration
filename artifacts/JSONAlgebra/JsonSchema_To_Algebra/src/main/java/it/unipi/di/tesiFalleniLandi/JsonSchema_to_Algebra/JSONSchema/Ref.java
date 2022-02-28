package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Ref_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions.SyntaxErrorRuntimeException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class Ref implements JSONSchemaElement{
	private URI_JS uri;

	private static Logger logger = LogManager.getLogger(Ref.class);
	
	protected Ref() {}
	
	public Ref(JsonElement uri) {
		try {
			this.uri = new URI_JS(uri.getAsString());
		}catch(ClassCastException ex) {
			throw new SyntaxErrorRuntimeException("Error: $ref must be string!");
		}

		logger.trace("Created a new Ref: {}", this);
	}

	@Override
	public JSONSchemaElement assertionSeparation() {
		return this.clone();
	}

	@Override
	public Ref_Assertion toGrammar() {
		String tmp = new JsonPrimitive(uri.getNormalizedName()).toString();
		return new Ref_Assertion(tmp.substring(1, tmp.length()-1));
	}
	
	@Override
	public int numberOfTranslatableAssertions() {
		return 1;
	}

	@Override
	public JsonElement toJSON() {
		JsonObject obj = new JsonObject();
		obj.addProperty("ref", uri.toString()); // GG 12/12/2020: should it be "#/" + uri.toString())?

		return obj;
	}

	@Override
	public List<URI_JS> getRef() {
		List<URI_JS> returnList = new LinkedList<>();
		returnList.add(uri);
		
		return returnList;
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
	public Ref clone() {
		Ref clone = new Ref();
		clone.uri = uri.clone();
		
		return clone;
	}
	
}
