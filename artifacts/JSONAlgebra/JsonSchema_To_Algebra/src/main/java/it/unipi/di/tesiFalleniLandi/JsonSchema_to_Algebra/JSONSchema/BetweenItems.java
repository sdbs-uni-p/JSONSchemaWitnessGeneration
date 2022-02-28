package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Boolean_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Exist_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions.SyntaxErrorRuntimeException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class BetweenItems implements JSONSchemaElement{
	private Long minItems;
	private Long maxItems;

	private static Logger logger = LogManager.getLogger(BetweenItems.class);

	public BetweenItems() {
		logger.trace("Creating an empty BetweenItems");
	}
	
	public void setMinItems(JsonElement obj) {
		if(!obj.isJsonPrimitive() || !obj.getAsJsonPrimitive().isNumber())
			throw new SyntaxErrorRuntimeException("expected integer as value of minItems got "+obj);

		logger.trace("Setting minItems by parsing {}", obj);

		minItems = obj.getAsLong();
	}
	
	public void setMaxItems(JsonElement obj) {
		if(!obj.isJsonPrimitive() || !obj.getAsJsonPrimitive().isNumber())
			throw new SyntaxErrorRuntimeException("expected integer as value of maxItems got "+obj);

		logger.trace("Setting maxItems by parsing {}", obj);

		maxItems = obj.getAsLong();
	}
	
	@Override
	public String toString() {
		return "BetweenItems [minItems=" + minItems + ", maxItems=" + maxItems + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonElement toJSON() {
		JsonObject obj = new JsonObject();
		
		if(minItems != null) obj.addProperty("minItems", minItems);
		if(maxItems != null) obj.addProperty("maxItems", maxItems);
		
		return obj;
	}

	@Override
	public Exist_Assertion toGrammar() {
		return new Exist_Assertion(minItems, maxItems, new Boolean_Assertion(true));
	}

	@Override
	public BetweenItems assertionSeparation() {
		BetweenItems obj = new BetweenItems();
		
		if(minItems != null) obj.minItems = minItems;
		if(maxItems != null) obj.maxItems = maxItems;
		
		return obj;
	}

	@Override
	public List<URI_JS> getRef() {
		return new LinkedList<>();
	}

	@Override
	public JSONSchema searchDef(Iterator<String> URIIterator) {
		return null;
	}

	@Override
	public List<Entry<String,Defs>> collectDef() {
		return new LinkedList<>();
	}

	@Override
	public int numberOfTranslatableAssertions() {
		return 1;
	}

	public BetweenItems clone() {
		BetweenItems clone = new BetweenItems();
		
		clone.minItems = minItems;
		clone.maxItems = maxItems;
		
		return clone;
	}
}
