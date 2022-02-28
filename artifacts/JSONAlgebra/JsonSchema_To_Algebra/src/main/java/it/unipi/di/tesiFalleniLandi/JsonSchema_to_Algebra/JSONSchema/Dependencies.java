package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.*;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions.SyntaxErrorRuntimeException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;

public class Dependencies implements JSONSchemaElement{
	private HashMap<String, List<String>> dependentRequired;
	private HashMap<String, JSONSchema> dependentSchemas;

	private static Logger logger = LogManager.getLogger(Dependencies.class);
	
	public Dependencies() {
		logger.trace("Creating an empty Dependencies");
		dependentSchemas = new HashMap<>();
		dependentRequired = new HashMap<>();
	}
	
	public void setDependencies(JsonElement obj) {
		logger.trace("Parsing {}", obj);

		if(!obj.isJsonObject()){ //TODO: check tipi dependencies, nel caso dai errore
			throw new SyntaxErrorRuntimeException("Error: Expected JsonObject in dependencies!");
		}

		JsonObject object = obj.getAsJsonObject();
		
		for(Entry<String,JsonElement> entry : object.entrySet()) {

			try {
				JSONSchema js = new JSONSchema(entry.getValue());
				dependentSchemas.put((String)entry.getKey(), js);
				logger.trace("Parsed as dependentSchema {}", dependentSchemas);
			}catch(SyntaxErrorRuntimeException | ClassCastException ex) {
				LinkedList<String> list = new LinkedList<>();
				JsonArray array = entry.getValue().getAsJsonArray();
				
				Iterator<JsonElement> it_array = array.iterator();
				
				while(it_array.hasNext()) {
					list.add(it_array.next().getAsString());
				}
				
				dependentRequired.put((String) entry.getKey(), list);
				logger.trace("Parsed as dependentRequired {}", dependentRequired);
			}
		}
	}

	public void setDependentRequired(JsonElement obj) {
		logger.trace("Setting dependentRequired by parsing {}", obj);
		JsonObject object = obj.getAsJsonObject();
		

		for(String key : object.keySet()) {
			LinkedList<String> list = new LinkedList<>();
			JsonArray array = object.get(key).getAsJsonArray();

			if(array.size() == 0)
				continue;
			for(JsonElement el : array) {
				list.add(el.getAsString());
			}
			
			dependentRequired.put(key, list);
		}
	}
	
	public void setDependentSchemas(JsonElement obj){
		logger.trace("Setting dependentSchema by parsing {}", obj);

		JsonObject object = obj.getAsJsonObject();
		dependentSchemas = new HashMap<>();

		
		for(Entry<String,JsonElement> entry : object.entrySet())
			dependentSchemas.put(entry.getKey(), new JSONSchema(entry.getValue()));
	}

	@SuppressWarnings("unchecked")
	//TODO: json schema version
	@Override
	public JsonElement toJSON() {
		JsonObject obj = new JsonObject();
		
		if(!dependentSchemas.isEmpty()){
			JsonObject tmp = new JsonObject();
			Set<String> keys = dependentSchemas.keySet();
				
			for(String key : keys)
				tmp.add(key, dependentSchemas.get(key).toJSON());
				
			obj.add("dependentSchemas", tmp);
		}
		
		if(!dependentRequired.isEmpty()){
			JsonObject tmp = new JsonObject();
			Set<String> keys = dependentRequired.keySet();
				
			for(String key : keys) {
				JsonArray array = new JsonArray();
				List<String> list = dependentRequired.get(key);
				for(String str : list) {
					array.add(str);
				}
				tmp.add(key, array);
			}
				
			obj.add("dependentRequired", tmp);
		}
		
		return obj;
	}

	@Override
	public JSONSchemaElement assertionSeparation() {
		return this.clone();
	}

	@Override
	public Assertion toGrammar() {
		Type_Assertion typeObj = new Type_Assertion(AlgebraStrings.TYPE_OBJECT);
		IfThenElse_Assertion ds = null;
		IfThenElse_Assertion dr = null;
		
		if(!dependentSchemas.isEmpty()) {
			AllOf_Assertion allOf = new AllOf_Assertion();
			Required_Assertion required;
			IfThenElse_Assertion ifThenElse;

			Set<Entry<String, JSONSchema>> entrySet = dependentSchemas.entrySet();
			for(Entry<String, JSONSchema> entry : entrySet) {
				required = new Required_Assertion();
				required.add(entry.getKey());
				ifThenElse = new IfThenElse_Assertion(required, entry.getValue().numberOfTranslatableAssertions() != 0 ? entry.getValue().toGrammar() : new Boolean_Assertion(true), null); //TODO: attenzione (f22116)!!!
				allOf.add(ifThenElse);
			}
			ds = new IfThenElse_Assertion(typeObj, allOf, null);
		}
		
		if(!dependentRequired.isEmpty()) {
			AllOf_Assertion allOf = new AllOf_Assertion();

			Set<String> keys = dependentRequired.keySet();

			for (String key : keys) {
				Required_Assertion reqList = new Required_Assertion();
				Required_Assertion req = new Required_Assertion();

				List<String> list = dependentRequired.get(key);
				for (String tmp : list) {
					reqList.add(tmp);
				}

				req.add(key);
				IfThenElse_Assertion ifThen = new IfThenElse_Assertion(req, reqList, null);
				allOf.add(ifThen);

			}

			dr = new IfThenElse_Assertion( typeObj, allOf, null);
		}

		if(ds != null && dr != null) {
			AllOf_Assertion allOf = new AllOf_Assertion();
			allOf.add(ds);
			allOf.add(dr);
			return allOf;
		}else if(ds != null)
			return ds;
		else if(dr != null)
			return dr;
		else
			return new Boolean_Assertion(true);
	}

	@Override
	public int numberOfTranslatableAssertions() {
		int count = 0;

		if(dependentSchemas != null)
			for(Entry<String, JSONSchema> entry : dependentSchemas.entrySet())
				count += entry.getValue().numberOfTranslatableAssertions();

		if(dependentRequired != null)
			count += dependentRequired.size();

		return count;
	}

	@Override
	public List<Entry<String, Defs>> collectDef() {
		List<Entry<String, Defs>> list = new LinkedList<>();
		
		if(dependentSchemas == null) return list;
		
		Set<Entry<String, JSONSchema>> entrySet = dependentSchemas.entrySet();
		
		for(Entry<String, JSONSchema> entry : entrySet)
			list.addAll(Utils_JSONSchema.addPathElement(entry.getKey(), entry.getValue().collectDef()));
		
		return list;
	}

	@Override
	public List<URI_JS> getRef() {
		List<URI_JS> list = new LinkedList<>();
		
		if(dependentSchemas == null) return list;
		
		Set<Entry<String, JSONSchema>> entrySet = dependentSchemas.entrySet();
		
		for(Entry<String, JSONSchema> entry : entrySet)
			list.addAll(entry.getValue().getRef());
		
		return list;
	}

	@Override
	public JSONSchema searchDef(Iterator<String> URIIterator) {
		if(!URIIterator.hasNext() && (URIIterator.next().equals("dependencies") || URIIterator.next().equals("dependentSchemas")))
			return null;
		
		URIIterator.remove();
		logger.debug("searchDef: searching for {} in allOf[{}]. URIIterator: {}", URIIterator.next(), this, URIIterator);
		if(dependentSchemas.containsKey(URIIterator.next())) {
			JSONSchema tmp = dependentSchemas.get(URIIterator.next());
			URIIterator.remove();
			return tmp.searchDef(URIIterator);
		}
		
		return null; //TODO: check
	}

	@Override
	public Dependencies clone() {
		Dependencies newDependencies = new Dependencies();

		for(Entry<String, List<String>> entry : dependentRequired.entrySet()) {
			List<String> list = new LinkedList<>();
			list.addAll(entry.getValue());
			newDependencies.dependentRequired.put(entry.getKey(), list);
		}

			
		for(Entry<String, JSONSchema> entry : dependentSchemas.entrySet())
			newDependencies.dependentSchemas.put(entry.getKey(), entry.getValue().clone());

		return newDependencies;
	}

}
