package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Boolean_Assertion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class Format implements JSONSchemaElement{
	private String format;

	private static Logger logger = LogManager.getLogger(Format.class);
	
	public Format(JsonElement obj) {
		logger.trace("Creating a new Format by parsing {}", obj);
		format = obj.getAsString();
	}

	public Format() {
		logger.trace("Creating an empty format");
	}

	@Override
	public JsonObject toJSON() {
		JsonObject obj = new JsonObject();
		obj.addProperty("format", format);

		return obj;
	}

	@Override
	public JSONSchemaElement assertionSeparation() {
		return this.clone();
	}

	@Override
	public Assertion toGrammar() {
		return new Boolean_Assertion(true); //TODO: format in fullAlgebra
	}

	@Override
	public int numberOfTranslatableAssertions() {
		return 0;
	}

	@Override
	public List<Entry<String, Defs>> collectDef() {
		return new LinkedList<>();
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
	public JSONSchemaElement clone() {
		Format clone = new Format();
		clone.format = format;
		return clone;
	}

}
