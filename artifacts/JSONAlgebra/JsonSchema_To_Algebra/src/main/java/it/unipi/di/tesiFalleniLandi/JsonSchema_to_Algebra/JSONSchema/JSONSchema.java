package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.UnsenseAssertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.Utils;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.AllOf_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Boolean_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions.SyntaxErrorRuntimeException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;

public class JSONSchema implements JSONSchemaElement{
	private Boolean booleanAsJSONSchema; //To handle boolean value
	private HashMap<String, JSONSchemaElement> jsonSchema; //Dictionary of keywords in the schema

	private static Logger logger = LogManager.getLogger(JSONSchema.class);

	public JSONSchema(JsonElement obj) {
		logger.trace("Creating JSONSchema by parsing {}", obj);
		jsonSchema = new HashMap<>();
		JsonObject object = null;

		if(obj.isJsonPrimitive() && obj.getAsJsonPrimitive().isBoolean()){
			booleanAsJSONSchema = obj.getAsBoolean();
			logger.trace("JSONSchema is boolean[{}]", booleanAsJSONSchema);
			return;
		}else if(obj.isJsonObject())
			object = obj.getAsJsonObject();
		else
			throw new SyntaxErrorRuntimeException("Expected Object or boolean but was "
					+ obj.getClass() + "("+ obj.toString() + ")" );
		
		Iterator<?> it = object.keySet().iterator();
		
		if(object.keySet().size() == 0) { // empty schema
			booleanAsJSONSchema = true;
			return;			
		}
		
		while(it.hasNext()) {
			String key = (String) it.next();
			switch(key) { 
			
			//properties, patternProperties and additionalProperties grouped in Properties.
			case "properties":
				jsonSchema.putIfAbsent("properties", new Properties());
				((Properties) jsonSchema.get("properties")).setProperties(object.get(key));
				break;
			
			case "patternProperties":
				jsonSchema.putIfAbsent("properties", new Properties());
				((Properties) jsonSchema.get("properties")).setPatternProperties(object.get(key));
				break;
				
			case "additionalProperties":
				jsonSchema.putIfAbsent("properties", new Properties());
				((Properties) jsonSchema.get("properties")).setAdditionalProperties(object.get(key));
				break;
				
			//dependencies, dependentSchemas and dependentRequired grouped in Dependencies.
			case "dependencies":
				jsonSchema.putIfAbsent("dependencies", new Dependencies());
				((Dependencies) jsonSchema.get("dependencies")).setDependencies(object.get(key));
				break;
			
			case "dependentSchemas":
				jsonSchema.putIfAbsent("dependencies", new Dependencies());
				((Dependencies) jsonSchema.get("dependencies")).setDependentSchemas(object.get(key));
				break;
				
			case "dependentRequired":
				jsonSchema.putIfAbsent("dependencies", new Dependencies());
				((Dependencies) jsonSchema.get("dependencies")).setDependentRequired(object.get(key));
				break;
				
			//items and additionalItems grouped in Items.
			case "items":
				jsonSchema.putIfAbsent("items", new Items());
				((Items) jsonSchema.get("items")).setItems(object.get(key));
				break;
				
			case "additionalItems":
				jsonSchema.putIfAbsent("items", new Items());
				((Items) jsonSchema.get("items")).setAdditionalItems(object.get(key));
				break;
			
			//if, then and else grouped IfThenElse.
			case "if":
				jsonSchema.putIfAbsent("ifThenElse", new IfThenElse());
				((IfThenElse) jsonSchema.get("ifThenElse")).setIf(object.get(key));
				break;
				
			case "then":
				jsonSchema.putIfAbsent("ifThenElse", new IfThenElse());
				((IfThenElse) jsonSchema.get("ifThenElse")).setThen(object.get(key));
				break;
				
			case "else":
				jsonSchema.putIfAbsent("ifThenElse", new IfThenElse());
				((IfThenElse) jsonSchema.get("ifThenElse")).setElse(object.get(key));
				break;
			
			case "not":
				jsonSchema.put("not", new Not(object.get(key)));
				break;
				
			case "type":
				jsonSchema.put("type", new Type(object.get(key)));
				break;
			
			case "oneOf":
				jsonSchema.put("oneOf", new OneOf(object.get(key)));
				break;
			
			case "allOf":
				jsonSchema.put("allOf", new AllOf(object.get(key)));
				break;
			
			case "anyOf":
				jsonSchema.put("anyOf", new AnyOf(object.get(key)));
				break;
			
			//minItems and maxItems grouped in BetweenItems.
			case "minItems":
				jsonSchema.putIfAbsent("betweenItems", new BetweenItems());
				((BetweenItems) jsonSchema.get("betweenItems")).setMinItems(object.get(key));
				break;
				
			case "maxItems":
				jsonSchema.putIfAbsent("betweenItems", new BetweenItems());
				((BetweenItems) jsonSchema.get("betweenItems")).setMaxItems(object.get(key));
				break;
				
			case "multipleOf":
				jsonSchema.put("multipleOf", new MultipleOf(object.get(key)));
				break;
				
			//minLength and maxLength grouped in Length.
			case "minLength":
				jsonSchema.putIfAbsent("length", new Length());
				((Length) jsonSchema.get("length")).setMinLength(object.get(key));
				break;
				
			case "maxLength":
				jsonSchema.putIfAbsent("length", new Length());
				((Length) jsonSchema.get("length")).setMaxLength(object.get(key));
				break;
				
			//contains, minContains and maxContains grouped in Contains.
			case "contains":
				jsonSchema.putIfAbsent("contains", new Contains());
				((Contains) jsonSchema.get("contains")).setContains(object.get(key));
				break;
			
			case "minContains":
				jsonSchema.putIfAbsent("contains", new Contains());
				((Contains) jsonSchema.get("contains")).setMinContains(object.get(key));
				break;
			
			case "maxContains":
				jsonSchema.putIfAbsent("contains", new Contains());
				((Contains) jsonSchema.get("contains")).setMaxContains(object.get(key));
				break;
				
			//minimum, maximum, exclusiveMinimum and exclusiveMaximum grouped in BetweenNumber.
			case "minimum":
				jsonSchema.putIfAbsent("betweenNumber", new BetweenNumber());
				((BetweenNumber) jsonSchema.get("betweenNumber")).setMin(object.get(key));
				break;
				
			case "maximum":
				jsonSchema.putIfAbsent("betweenNumber", new BetweenNumber());
				((BetweenNumber) jsonSchema.get("betweenNumber")).setMax(object.get(key));
				break;
				
			case "exclusiveMinimum":
				jsonSchema.putIfAbsent("betweenNumber", new BetweenNumber());
				((BetweenNumber) jsonSchema.get("betweenNumber")).setExclusiveMin(object.get(key));
				break;
				
			case "exclusiveMaximum":
				jsonSchema.putIfAbsent("betweenNumber", new BetweenNumber());
				((BetweenNumber) jsonSchema.get("betweenNumber")).setExclusiveMax(object.get(key));
				break;

			case "required":
				try {
					jsonSchema.put("required", new Required(object.get(key)));
				}catch(UnsenseAssertion e){
					logger.catching(e);
					//TODO: required : true
				}
				break;
				
			case "pattern":
				jsonSchema.put("pattern", new Pattern(object.get(key)));
				break;
				
			case "uniqueItems":
				jsonSchema.put("uniqueItems", new UniqueItems(object.get(key)));
				break;
				
			//minProperties and maxProperties grouped in BetweenProperties.
			case "minProperties":
				jsonSchema.putIfAbsent("betweenProperties", new BetweenProperties());
				((BetweenProperties) jsonSchema.get("betweenProperties")).setMinProperties(object.get(key));
				break;
				
			case "maxProperties":
				jsonSchema.putIfAbsent("betweenProperties", new BetweenProperties());
				((BetweenProperties) jsonSchema.get("betweenProperties")).setMaxProperties(object.get(key));
				break;
				
			case "enum":
				jsonSchema.put("enum", new Enum(object.get(key)));
				break;
				
			case "const":
				jsonSchema.put("const", new Const(object.get(key)));
				break;

			case "$recursiveRef":
			case "$ref": case "ref":
				jsonSchema.put("$ref", new Ref(object.get(key)));
				break;
				
			case "$defs": case "definitions":
					jsonSchema.put("$defs", new Defs(object.get(key)));
				break;
				
			case "format":
				jsonSchema.put("format", new Format(object.get(key)));
				break;
				
			case "propertyNames":
				jsonSchema.put("propertyNames", new PropertyNames(object.get(key)));
				break;

			case "$id": case "id": case "comment": case "description":
					break;//nothing to do
			//Amine: I added the following test to allow more schemas to pass
			case "$schema" : case "title": case "default":
					break;
			
			default:
				jsonSchema.putIfAbsent("unknown", new UnknownElement());
				try {
					((UnknownElement) jsonSchema.get("unknown")).add(key, new JSONSchema(object.get(key)));
				}catch (SyntaxErrorRuntimeException e){
					//it's a primitive object
					//TODO: do we need to memorize this (--> can be referenced?)
					logger.info("Unknown element {}", key);
					//e.printStackTrace();
					//throw e;
				}
				break;
			}
		}
	}
	
	
	public JSONSchema() { }

	public void addJSONSchemaElement(String key, JSONSchemaElement value) {
		if(jsonSchema == null)	jsonSchema = new HashMap<>();
		jsonSchema.put(key, value);
	}
	
	@Override
	public JsonElement toJSON() {
		JsonObject schema = new JsonObject();

		//boolean as a schema
		if(booleanAsJSONSchema != null){
			return new JsonPrimitive(booleanAsJSONSchema);
		}
		
		Set<Entry<String, JSONSchemaElement>> entries = jsonSchema.entrySet();
		for(Entry<String, JSONSchemaElement> entry : entries){
			JsonObject toAdd = (JsonObject)entry.getValue().toJSON();
			Set<String> subSchemaKeySet = toAdd.keySet();
			for (String key : subSchemaKeySet){
				if(key.equals(Utils.ROOTDEF_FOR_JSONSCHEMA))
					schema = Utils_JSONSchema.mergeJsonObject(schema, (JsonObject) toAdd.get(key));
				else
					schema.add(key, toAdd.get(key));
			}
		}
		
		return schema;
	}
	
	
	public JSONSchema assertionSeparation() {
		JSONSchema schema = new JSONSchema();
		schema.jsonSchema = new HashMap<>();

		if(booleanAsJSONSchema != null) {
			schema.booleanAsJSONSchema = booleanAsJSONSchema;
			return schema;
		}
		
		schema.jsonSchema.putIfAbsent("allOf", new AllOf());

		Set<Entry<String, JSONSchemaElement>> entries = jsonSchema.entrySet();
		
		for(Entry<String, JSONSchemaElement> entry : entries) {
			
			//$schema, id e %defs non vanno dentro allOf
			if(entry.getKey().equals("$schema")
					|| entry.getKey().equals("id")
					|| entry.getKey().equals("$id")
					|| entry.getKey().equals("$defs")
					|| entry.getKey().equals("unknown")) {
				schema.jsonSchema.put(entry.getKey(), entry.getValue());
				continue;
			}

			JSONSchema tmp = new JSONSchema();
			tmp.jsonSchema = new HashMap<>();
			tmp.jsonSchema.put(entry.getKey(), entry.getValue().assertionSeparation());
			((AllOf) schema.jsonSchema.get("allOf")).addElement(tmp);

		}

		if(((AllOf) schema.jsonSchema.get("allOf")).isEmpty())
			schema.jsonSchema.remove("allOf");
		
		return schema;
	}

	

	@Override
	public String toString() {
		return "JSONSchema [booleanAsJSONSchema=" + booleanAsJSONSchema + "\r\n jsonSchema=" + jsonSchema + "]";
	}
	

	@Override
	public Assertion toGrammar() {
		AllOf_Assertion allOf = new AllOf_Assertion();

		if(booleanAsJSONSchema != null)
			return new Boolean_Assertion(booleanAsJSONSchema);

		if(jsonSchema.size() == 1){
			JSONSchemaElement tmp = jsonSchema.entrySet().iterator().next().getValue();

			return tmp.toGrammar();
		}


		Set<Entry<String, JSONSchemaElement>> entries = jsonSchema.entrySet();
		for(Entry<String, JSONSchemaElement> entry : entries){
			if(entry.getValue().getClass() == UnknownElement.class)
				continue;

			if(entry.getValue().numberOfTranslatableAssertions() != 0)
				allOf.add(entry.getValue().toGrammar());
		}

		return allOf;
	}
	
	public int numberOfTranslatableAssertions() {
		int count = 0;
		
		Set<Entry<String, JSONSchemaElement>> entries = jsonSchema.entrySet();

		for(Entry<String, JSONSchemaElement> entry : entries)
			count += entry.getValue().numberOfTranslatableAssertions();

		if(booleanAsJSONSchema != null)
			count++;

		return count;
	}


	@Override
	public List<URI_JS> getRef() {
		List<URI_JS> returnList = new LinkedList<>();
		
		if(booleanAsJSONSchema != null) return returnList;
		
		Set<Entry<String, JSONSchemaElement>> entrySet = jsonSchema.entrySet();

		//cerca i ref all'interno dello schema
		for(Entry<String, JSONSchemaElement> entry : entrySet)
			returnList.addAll(entry.getValue().getRef());
		
		return returnList;
	}


	@Override
	public JSONSchema searchDef(Iterator<String> URIIterator) {
		if(!URIIterator.hasNext())
			return this;
		logger.debug("searchDef: searching for {} in {}. URIIterator: {}", URIIterator.next(), this, URIIterator);

		String nextElement = URIIterator.next();
		//URIIterator.remove(); <-- Only assertion classes must perform .remove (think about patternProperties)

		if(jsonSchema.containsKey(getGroupName(nextElement))) //here we don't care about calling searchDef on assertion like between***** or similar (because cannot be ref, right??)
			return jsonSchema.get(getGroupName(nextElement)).searchDef(URIIterator);
		else if(jsonSchema.containsKey("unknown"))
			return jsonSchema.get("unknown").searchDef(URIIterator);
		
		return null;
	}

	//We use this method to get, from a keyword, its "semantic group"
	private String getGroupName(String s){
		switch (s){
			case "patternProperties": case "additionalProperties": return "properties";

			case "dependentSchemas": case "dependentRequired": return "dependencies";

			case "additionalItems": return "items";

			case "if": case "then": case "else": return "ifThenElse";

			case "minItems": case "maxItems": return "betweenItems";

			case "minumum": case "maximum": case "exclusiveMaximum": case "exclusiveMinimum": return "betweenNumber";

			case "minProperties": case "maxProperties": return "betweenProperties";

			case "minLength": case "maxLength": return "length";

			case "minContains": case "maxContains": return "contains";

			case "ref": return "$ref";

			case "definitions": return "$defs";
		}

		return s;
	}


	@Override
	public List<Entry<String, Defs>> collectDef() {
		List<Entry<String, Defs>> returnList = new LinkedList<>();
		
		Set<Entry<String, JSONSchemaElement>> entrySet = jsonSchema.entrySet();
		
		for(Entry<String, JSONSchemaElement> entry : entrySet)
			returnList.addAll(Utils_JSONSchema.addPathElement(entry.getKey(), entry.getValue().collectDef()));
		
		jsonSchema.remove("$defs");
		
		return returnList;
	}

	
	@Override
	public JSONSchema clone(){
		JSONSchema clone = new JSONSchema();
		
		if(booleanAsJSONSchema != null)
			clone.booleanAsJSONSchema = booleanAsJSONSchema;
		
		if(jsonSchema != null) {
			Set<Entry<String, JSONSchemaElement>> entrySet = jsonSchema.entrySet();
			clone.jsonSchema = new HashMap<>();
			
			for(Entry<String, JSONSchemaElement> entry : entrySet)
				clone.jsonSchema.put(entry.getKey(), entry.getValue().clone());
		}
		
		return clone;
	}
}
