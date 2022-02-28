package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Enum_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions.SyntaxErrorRuntimeException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class Enum implements JSONSchemaElement{
	JsonArray _enum;

	private static Logger logger = LogManager.getLogger(Enum.class);

	public Enum(JsonElement array) {
		logger.trace("Creating a new Format by parsing {}", array);

		try {
			_enum = (JsonArray) array;
		}catch(ClassCastException e) {
			throw new SyntaxErrorRuntimeException("Expected array as value of \"enum\"");
		}
	}

	@Override
	public String toString() {
		return "Enum{" +
				"_enum=" + _enum +
				'}';
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonElement toJSON() {
		JsonObject obj = new JsonObject();
		JsonArray array = new JsonArray();

		for(JsonElement element : _enum) {
			if(element.isJsonNull())
				array.add(JsonNull.INSTANCE);

			else if(element.isJsonObject())
				array.add(element.getAsJsonObject());

			else if(element.isJsonArray())
				array.add(element.getAsJsonArray());

			else if(element.getAsJsonPrimitive().isString())
				array.add(element.getAsString());

			else if(element.getAsJsonPrimitive().isNumber())
				array.add(element.getAsNumber());

			else
				array.add(element.getAsBoolean());
		}

		obj.add("enum", array);
		return obj;
	}

	@Override
	public Assertion toGrammar() {
		return new Enum_Assertion(_enum.deepCopy());
	}


	@Override
	public Enum assertionSeparation() {
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
	public int numberOfTranslatableAssertions() {
		return 1; //TODO: 1 or _enum.size?
	}
	
	public Enum clone() {
		return new Enum(_enum.deepCopy());
	}
}
