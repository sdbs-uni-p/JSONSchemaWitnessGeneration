package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Properties_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions.SyntaxErrorRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.*;
import java.util.Map.Entry;

public class Properties implements JSONSchemaElement{
	private HashMap<String, JSONSchema> properties;
	private HashMap<String, JSONSchema> patternProperties;
	private JSONSchema additionalProperties;

	private static Logger logger = LogManager.getLogger(Properties.class);
	
	public Properties() {
		logger.trace("Creating an empty Properties");
	}
	
	public void setProperties(JsonElement obj) {
		logger.trace("Parsing properties from JsonElement {}", obj);

		JsonObject object = null;

		try{
			object = obj.getAsJsonObject();
		}catch(ClassCastException ex){
			if(obj.isJsonArray())
				throw new SyntaxErrorRuntimeException("Error: properties value must be JsonObject, not JsonArray!\r\n");
		}

		if(object.size() == 0) return;

		properties = new HashMap<>();
		
		Iterator<?> it = object.keySet().iterator();
		
		while(it.hasNext()) {
			String key = (String) it.next();
			JSONSchema value = null;

			if((object.get(key).isJsonPrimitive() && object.get(key).getAsJsonPrimitive().isBoolean()) || object.get(key).isJsonObject())
				value = new JSONSchema(object.get(key));
			else{
				throw new SyntaxErrorRuntimeException("Error: the value in properties must be String: JsonObject or Boolean!\r\n");
			}
			
			properties.put(key, value);
		}
	}
	
	public void setPatternProperties(JsonElement obj) {
		logger.trace("Parsing PatternProperties from JsonElement {}", obj);

		JsonObject object = null;

		if(((JsonObject)obj).size() == 0) return;

		try{
			object = obj.getAsJsonObject();
		}catch(ClassCastException ex){
			if(obj.getClass() == JsonArray.class)
				throw new SyntaxErrorRuntimeException("Error: patterProperties value must be JsonObject, not JsonArray!\r\n");
		}

		patternProperties = new HashMap<>();
		
		Iterator<String> it = object.keySet().iterator();
		
		while(it.hasNext()) {
			String key = it.next();
			JSONSchema value = null;

			try {
				value = new JSONSchema(object.get(key));
			}catch (ClassCastException ex){
				throw new SyntaxErrorRuntimeException("Error: the value in patterProperties must be String: JsonObject!\r\n");
			}
			
			patternProperties.put(key, value);
		}
	}
	
	public void setAdditionalProperties(JsonElement obj) {
		logger.trace("Parsing additionalProperties from JsonElement {}", obj);

		if(!obj.isJsonObject() && !obj.isJsonPrimitive()) // TODO: trovare modo per controllare i tipi primitivi
			throw new SyntaxErrorRuntimeException("Error: Expected JsonObject in additionalProperties!\r\n");

		additionalProperties = new JSONSchema(obj);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JsonElement toJSON() {
		JsonObject obj = new JsonObject();
		
		if(properties != null){
			JsonObject tmp = new JsonObject();

			for(Entry<String, JSONSchema> entry : properties.entrySet())
				tmp.add(entry.getKey(), entry.getValue().toJSON());
				
			obj.add("properties", tmp);
		}
		
		if(patternProperties != null){
			JsonObject tmp = new JsonObject();

			for(Entry<String, JSONSchema> entry : patternProperties.entrySet())
				tmp.add(entry.getKey(), entry.getValue().toJSON());
				
			obj.add("patternProperties", tmp);
		}
		
		if(additionalProperties != null)
			obj.add("additionalProperties", additionalProperties.toJSON());


		return obj;
	}

	@Override
	public Assertion toGrammar() {
		Properties_Assertion fullAlgebra_properties = new Properties_Assertion();

		if(properties != null){
			for(Map.Entry<String, JSONSchema> entry : properties.entrySet()){
				Assertion returnedValue = entry.getValue().toGrammar();
				if(returnedValue != null) {
					String tmp = new JsonPrimitive(entry.getKey()).toString();
					try {
						fullAlgebra_properties.addProperties(tmp.substring(1, tmp.length()-1), returnedValue); //TODO: check encoding escape
					} catch (REException e) {
						logger.catching(e);
						throw new RuntimeException(e);
					}
				}
			}
		}

		if(patternProperties != null){
			for(Map.Entry<String, JSONSchema> entry : patternProperties.entrySet()){
				Assertion returnedValue = entry.getValue().toGrammar();
				if(returnedValue != null) {
					String tmp = new JsonPrimitive(entry.getKey()).toString();
					try {
						fullAlgebra_properties.addPatternProperties(tmp.substring(1, tmp.length()-1), returnedValue); //TODO: check encoding escape
					} catch (REException e) {
						logger.catching(e);
						throw new RuntimeException(e);
					}
				}
			}
		}

		if(additionalProperties != null)
			fullAlgebra_properties.setAdditionalProperties(additionalProperties.toGrammar());

		return fullAlgebra_properties;
	}

	@Override
	public String toString() {
		return "Properties [properties=" + properties + ", patternProperties=" + patternProperties
				+ ", additionalProperties=" + additionalProperties + "]";
	}

	@Override
	public Properties assertionSeparation() {
		Properties obj = new Properties();

		if(properties != null) {
			obj.properties = new HashMap<>();
			Iterator<Entry<String, JSONSchema>> it = properties.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, JSONSchema> tmp = it.next();
				obj.properties.put(tmp.getKey(), tmp.getValue().assertionSeparation());
			}
		}

		if(patternProperties != null) {
			obj.patternProperties = new HashMap<>();
			Iterator<Entry<String, JSONSchema>> it = patternProperties.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, JSONSchema> tmp = it.next();
				obj.patternProperties.put(tmp.getKey(), tmp.getValue().assertionSeparation());
			}
		}

		if(additionalProperties != null) {
			obj.additionalProperties = additionalProperties.assertionSeparation();
		}
		
		return obj;
	}

	@Override
	public List<URI_JS> getRef() {
		List<URI_JS> returnList = new LinkedList<>();
		
		if(properties != null) {
			Set<Entry<String, JSONSchema>> entrySet = properties.entrySet();
			for(Entry<String, JSONSchema> entry : entrySet) {
				returnList.addAll(entry.getValue().getRef());
			}
		}
		if(patternProperties != null) {
			Set<Entry<String, JSONSchema>> entrySet = patternProperties.entrySet();
			for(Entry<String, JSONSchema> entry : entrySet) {
				returnList.addAll(entry.getValue().getRef());
			}
		}
		if(additionalProperties != null) 
			returnList.addAll(additionalProperties.getRef());

		return returnList;
	}


	@Override
	public List<Entry<String,Defs>> collectDef() {
		List<Entry<String,Defs>> returnList = new LinkedList<>();
		
		if(properties != null) {
			Set<Entry<String, JSONSchema>> entrySet = properties.entrySet();
			for(Entry<String, JSONSchema> entry : entrySet)
				returnList.addAll(Utils_JSONSchema.addPathElement(entry.getKey(), entry.getValue().collectDef()));
		}
		
		if(patternProperties != null) {
			Set<Entry<String, JSONSchema>> entrySet = patternProperties.entrySet();
			for(Entry<String, JSONSchema> entry : entrySet)
				returnList.addAll(Utils_JSONSchema.addPathElement(entry.getKey(), entry.getValue().collectDef()));
		}
		
		if(additionalProperties != null)
			returnList.addAll(additionalProperties.collectDef());
		
		return returnList;
	}

	@Override
	public JSONSchema searchDef(Iterator<String> URIIterator) {
		if(!URIIterator.hasNext())
			return null;

		logger.debug("searchDef: searching for {} in Properties.java. URIIterator: {}", URIIterator.next(), URIIterator);

		switch(URIIterator.next()) {
		case "properties":
			URIIterator.remove();
			String next = URIIterator.next();
			logger.debug("searchDef: searching for {} in {}. URIIterator: {}", next, properties, URIIterator);
			if(properties.containsKey(next)) {
				URIIterator.remove();
				return properties.get(next).searchDef(URIIterator);
			}
			break;
			
		case "patternProperties":
			URIIterator.remove();
			next = URIIterator.next();
			logger.debug("searchDef: searching for {} in {}. URIIterator: {}", next, patternProperties, URIIterator);
			if(patternProperties.containsKey(next)) {
				URIIterator.remove();
				return patternProperties.get(next).searchDef(URIIterator);
			}
			break;
			
		case "additionalProperties":
			URIIterator.remove();
			logger.debug("searchDef: searching for {} in {}. URIIterator: {}", URIIterator.hasNext() ? URIIterator.next() : null, additionalProperties, URIIterator);
			return additionalProperties.searchDef(URIIterator);
		}
		
		return null;
	}

	@Override
	public int numberOfTranslatableAssertions() {
		int count = 0;

		if(properties != null)
			for(Entry<String, JSONSchema> entry : properties.entrySet())
				count += entry.getValue().numberOfTranslatableAssertions();

		if(patternProperties != null)
			for(Entry<String, JSONSchema> entry : patternProperties.entrySet())
				count += entry.getValue().numberOfTranslatableAssertions();

		if(additionalProperties != null)
			count += additionalProperties.numberOfTranslatableAssertions();

		return count;
	}
	
	public void addPatternProperties(String key, JSONSchema value) {
		patternProperties.put(key, value);
	}

	@Override
	public Properties clone() {
		logger.trace("Cloning {}", this);
		Properties newProperties = new Properties();
		
		if(properties != null) {
			newProperties.properties = new HashMap<>();
			Set<Entry<String, JSONSchema>> entrySet = properties.entrySet();
			for(Entry<String, JSONSchema> entry : entrySet) {
				newProperties.properties.put(entry.getKey(), entry.getValue().clone());
			}
		}
		
		if(patternProperties != null) {
			newProperties.patternProperties = new HashMap<>();
			Set<Entry<String, JSONSchema>> entrySet = patternProperties.entrySet();
			for(Entry<String, JSONSchema> entry : entrySet) {
				newProperties.patternProperties.put(entry.getKey(), entry.getValue().clone());
			}
		}
		
		if(additionalProperties != null)
			newProperties.additionalProperties = additionalProperties.clone();

		return newProperties;
	}
}
