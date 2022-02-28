package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.Utils;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Boolean_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Defs_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions.SyntaxErrorRuntimeException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;

public class Defs implements JSONSchemaElement {
	private LinkedHashMap<String, JSONSchema> schemaDefs;
	private JSONSchema rootDef;

	private static Logger logger = LogManager.getLogger(Defs.class);
	
	public Defs(JsonElement obj) {
		logger.trace("Creating a new Defs by parsing {}", obj);

		JsonObject jsonObject;
		try {
			jsonObject = obj.getAsJsonObject();
		}catch(ClassCastException ex) {
			throw new SyntaxErrorRuntimeException("Error: $defs must be valid JSON Object!");
		}
		
		schemaDefs = new LinkedHashMap<>();
		
		for(Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			try{
				schemaDefs.putIfAbsent((String) entry.getKey(), new JSONSchema(entry.getValue()));
			}catch(ClassCastException ex) {
				try{
					entry.getValue().getAsString();
				}catch (ClassCastException exx) {
					throw new SyntaxErrorRuntimeException("Error: no valid Defs Object!\r\n" + entry.toString());
				}
				throw new SyntaxErrorRuntimeException("Error: no valid Defs Object!\r\n"+ex.getLocalizedMessage());
			}
		}
	}
	
	public JSONSchema containsDef(String key) {
		return schemaDefs.get(key);
	}
	
	public void addDef(String key, JSONSchema element) {
		logger.trace("Adding def <{}, {}>", key, element);
		schemaDefs.put(key, element);
	}
	
	public void setRootDef(JSONSchema root) {
		logger.trace("Setting rootdef {}", root);
		rootDef = root;
	}

	public void addDef(Defs defs) {
		logger.trace("Adding def {}", defs);
		schemaDefs.putAll(defs.schemaDefs);
	}
	
	public Defs() {	
		schemaDefs = new LinkedHashMap<>();
		logger.trace("Creating an empty Defs");
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonElement toJSON() {
		JsonObject obj = new JsonObject();
		JsonObject def = new JsonObject();
		
		Set<Entry<String, JSONSchema>> entrySet = schemaDefs.entrySet();
		
		for(Entry<String, JSONSchema> entry : entrySet)
			def.add(entry.getKey(), entry.getValue().toJSON());

		obj.add("$defs", def);

		if(rootDef != null)
			obj.add(Utils.ROOTDEF_FOR_JSONSCHEMA, rootDef.toJSON());

		return obj;
	}

	@Override
	public Defs assertionSeparation() {
		Defs obj = new Defs();
		
		Set<Entry<String, JSONSchema>> entrySet = schemaDefs.entrySet();
		
		for(Entry<String, JSONSchema> entry : entrySet)
			obj.schemaDefs.put(entry.getKey(), entry.getValue().assertionSeparation());
		
		if(rootDef != null)
			obj.setRootDef(this.rootDef.assertionSeparation());
		
		return obj;
	}

	@Override
	public Defs_Assertion toGrammar() {
		Defs_Assertion defs_assertion = new Defs_Assertion();
		String tmp = "";

		if(rootDef != null) defs_assertion.setRootDef(AlgebraStrings.ROOTDEF_DEFAULTNAME, rootDef.toGrammar());

		for(Entry<String, JSONSchema> entry : schemaDefs.entrySet()) {
			tmp = new JsonPrimitive(entry.getKey()).toString();

			if(entry.getValue().numberOfTranslatableAssertions() != 0)
				defs_assertion.add(tmp.substring(1, tmp.length()-1), entry.getValue().toGrammar());
			else
				defs_assertion.add(tmp.substring(1, tmp.length()-1), new Boolean_Assertion(true)); //TODO: definizioni con valore "unknown"
		}

		return defs_assertion;
	}
	
	@Override
	public int numberOfTranslatableAssertions() {
		return schemaDefs.size() + ((rootDef == null) ? 0 : 1);
	}

	@Override
	public List<URI_JS> getRef() {
		List<URI_JS> returnList = new LinkedList<>();
		
		Set<Entry<String, JSONSchema>> entrySet = schemaDefs.entrySet();
		
		for(Entry<String, JSONSchema> entry : entrySet)
			returnList.addAll(entry.getValue().getRef());
		
		if(rootDef != null)
			returnList.addAll(rootDef.getRef());
		
		return returnList;
	}

	@Override
	public JSONSchema searchDef(Iterator<String> URIIterator) {
		return null;
	}

	@Override
	public List<Entry<String,Defs>> collectDef() {
		List<Entry<String,Defs>> returnList = new LinkedList<>();
		Set<Entry<String, JSONSchema>> entrySet = schemaDefs.entrySet();

		returnList.add(new AbstractMap.SimpleEntry<>("",this));

		for(Entry<String, JSONSchema> entry : entrySet)
			returnList.addAll(Utils_JSONSchema.addPathElement(entry.getKey(), entry.getValue().collectDef()));

		return returnList;
	}

	@Override
	public String toString() {
		return "Defs{" +
				"schemaDefs=" + schemaDefs +
				", rootDef=" + rootDef +
				'}';
	}

	public Defs clone() {
		Defs clone = new Defs();
		
		Set<Entry<String, JSONSchema>> entrySet = schemaDefs.entrySet();
		
		for(Entry<String, JSONSchema> entry : entrySet)
			clone.schemaDefs.put(entry.getKey(), entry.getValue().clone());

		if(rootDef != null)
			clone.rootDef = rootDef.clone();
		
		return clone;
	}
}
