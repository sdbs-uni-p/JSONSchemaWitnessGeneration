package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Mof_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions.SyntaxErrorRuntimeException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class MultipleOf implements JSONSchemaElement{
	private Number value;

	private static Logger logger = LogManager.getLogger(MultipleOf.class);

	private MultipleOf(Number obj) {
		this.value = obj;
		logger.trace("Creating a new MultipleOf: {}", this);
	}

	public MultipleOf(JsonElement obj) {
		if(!obj.isJsonPrimitive() || !obj.getAsJsonPrimitive().isNumber())
			throw new SyntaxErrorRuntimeException("expected number as value of multipleOf got " + obj);

		this.value = obj.getAsNumber();
		logger.trace("Creating a new MultipleOf: {}", this);
	}
	
	@Override
	public String toString() {
		return "MultipleOf [value=" + value + "]";
	}

	@Override
	public JsonElement toJSON() {
		JsonObject obj = new JsonObject();
		obj.addProperty("multipleOf", value);

		return obj;
	}

	@Override
	public Assertion toGrammar() {
		return new Mof_Assertion(value);
	}

	@Override
	public MultipleOf assertionSeparation() {
		return new MultipleOf(value);
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
	public int numberOfTranslatableAssertions() {
		return 1;
	}
	
	@Override
	public MultipleOf clone(){
		return new MultipleOf(value);
	}
}
