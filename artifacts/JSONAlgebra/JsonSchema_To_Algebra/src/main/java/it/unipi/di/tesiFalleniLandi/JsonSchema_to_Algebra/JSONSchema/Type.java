package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.*;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions.SyntaxErrorRuntimeException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.units.qual.A;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class Type implements JSONSchemaElement {
	protected List<String> type_array;

	private static Logger logger = LogManager.getLogger(Type.class);
	
	public Type(JsonElement obj){
		logger.trace("Creating a new Type by parsing {}", obj);
		if(obj.isJsonNull())
			throw new SyntaxErrorRuntimeException("Error: Expected JsonArray or Sting in type!\r\n");

		if(obj.isJsonArray()){
			JsonArray array = obj.getAsJsonArray();
			type_array = new LinkedList<>();

			Iterator<JsonElement> it = array.iterator();

			while(it.hasNext()){
				JsonElement str = it.next();
				////Verify the type string value
				if(!str.isJsonPrimitive() || !str.getAsJsonPrimitive().isString())
					throw new SyntaxErrorRuntimeException("Error: Expected JsonArray or Sting in type but was "+ obj);
				jsonTypeToGrammar(str.getAsJsonPrimitive().getAsString());
				type_array.add(str.getAsString());
			}
		}else {
			if(obj.isJsonPrimitive() && obj.getAsJsonPrimitive().isString()) {
				String type_str = obj.getAsString();
				//Verify the type string value
				jsonTypeToGrammar(type_str);
				type_array = new LinkedList<>();
				type_array.add(type_str);
			} else
				throw new SyntaxErrorRuntimeException("Error: Expected JsonArray or Sting in type but was "+ obj);
		}

		logger.trace("Created a new  Type: {}", this);
	}
	
	public Type() { }
	
	@Override
	public String toString() {
		return "Type [type_array=" + type_array + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonElement toJSON() {
		JsonObject obj = new JsonObject();

		//creating "type": "..."
		if(type_array.size() == 1) {
			obj.addProperty("type", type_array.get(0));
			return obj;
		}

		//creating "type": ["...", "...", ...]
		JsonArray array = new JsonArray();
		for(String s : type_array)
			array.add(s);

		obj.add("type", array);

		return obj;
	}

	@Override
	public Assertion toGrammar() {
		Type_Assertion types = new Type_Assertion();
		AnyOf_Assertion or = new AnyOf_Assertion();

		for(String s : type_array){
			if(s.equals("integer")){
				AllOf_Assertion and = new AllOf_Assertion();
				and.add(new Type_Assertion(AlgebraStrings.TYPE_NUMBER));
				and.add(new Mof_Assertion(1));
				if(type_array.size()==1)
					return and;

				or.add(and);
			}else {
				types.add(jsonTypeToGrammar(s));
			}
		}

		if(!types.isEmpty())
			or.add(types);

		return or;
	}
	
	@Override
	public int numberOfTranslatableAssertions() {
		return 1;
	}
	
	private String jsonTypeToGrammar(String type) {
		switch(type) {
		case "array": return AlgebraStrings.TYPE_ARRAY;
		case "integer": return AlgebraStrings.TYPE_INTEGER;
		case "number": return AlgebraStrings.TYPE_NUMBER;
		case "string": return AlgebraStrings.TYPE_STRING;
		case "object": return AlgebraStrings.TYPE_OBJECT;
		case "boolean": return AlgebraStrings.TYPE_BOOLEAN;
		case "null": return AlgebraStrings.TYPE_NULL;
			default:
				throw new SyntaxErrorRuntimeException("Error: type '"+type+"' is not allowed!\r\n");
		}
	}

	@Override
	public Type assertionSeparation() {
		Type obj = new Type();

		if(type_array != null)
			obj.type_array = new LinkedList<>(type_array);

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
	public Type clone() {
		if(type_array.size() == 1) {
			return new Type(new JsonPrimitive(type_array.get(0)));
		}else {
			Type newType = new Type();
			newType.type_array = new LinkedList<>();
			newType.type_array.addAll(type_array);
			return newType;
		}
	}
	
}
