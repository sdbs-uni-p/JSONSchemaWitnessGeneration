package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Const_Assertion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Const implements JSONSchemaElement{
	JsonElement _const;

	private static Logger logger = LogManager.getLogger(Const.class);
	
	public Const(JsonElement value) {
		logger.trace("Creating Const by parsing {}", value);
		this._const = value;
	}

	@Override
	public String toString() {
		return "Const{" +
				"_const=" + _const +
				'}';
	}

	@Override
	public JsonObject toJSON() {
		JsonObject obj = new JsonObject();

		if(_const.isJsonNull()) {
			obj.add("const", JsonNull.INSTANCE);
			return obj;
		}

		if(_const.isJsonObject())
			obj.add("const", _const);
		else if(_const.isJsonArray())
			obj.add("const", _const);
		else if(_const.getAsJsonPrimitive().isString())
			obj.addProperty("const", _const.getAsString());
		else if(_const.getAsJsonPrimitive().isNumber())
			obj.addProperty("const", _const.getAsNumber());
		else if(_const.getAsJsonPrimitive().isBoolean())
			obj.addProperty("const", _const.getAsBoolean());

		return obj;
	}

	@Override
	public Const assertionSeparation() {
		return clone();
	}

	@Override
	public Assertion toGrammar() {
		return new Const_Assertion(_const.deepCopy());
	}

	@Override
	public int numberOfTranslatableAssertions() {
		return 1;
	}

	@Override
	public List<Map.Entry<String, Defs>> collectDef() {
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
	public Const clone() {

		return new Const(_const.deepCopy());
	}
}
