package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Boolean_Assertion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class UnknownElement implements JSONSchemaElement {
	private HashMap<String, JSONSchema> obj;

	private static Logger logger = LogManager.getLogger(UnknownElement.class);
	
	public UnknownElement() {
		 obj = new HashMap<>();
		logger.trace("Creating an empty UnknownElement");
	}
	
	public void add(String key, JSONSchema value) {
		logger.trace("Adding <{}, {}> to {}", key, value, this);
		obj.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JsonElement toJSON() {
		JsonObject obj = new JsonObject();
		
		//obj.putAll(obj);
		
		return obj;
	}

	@Override
	public Assertion toGrammar() {
		/*String str = "";
		
		Set<Entry<String, Object>> entrySet = obj.entrySet();
		
		for(Entry<String, Object> entry : entrySet)
			str += GrammarStringDefinitions.COMMA + String.format(GrammarStringDefinitions.SINGLEUNKNOW, entry.getKey(), entry.getValue().toString());
		
		return String.format(GrammarStringDefinitions.UNKNOW, str.substring(GrammarStringDefinitions.COMMA.length()));*/
//		return null;
		return new Boolean_Assertion(true);
	}

	
	@Override
	public int numberOfTranslatableAssertions() {
		//return obj.size();
		return 0;
	}

	@Override
	public JSONSchemaElement assertionSeparation() {
		return this.clone();
	}

	@Override
	public List<URI_JS> getRef() {
		return new LinkedList<>();
	}

	@Override
	public JSONSchema searchDef(Iterator<String> URIIterator) {
		if(!URIIterator.hasNext()) return null; //this should never happen

		String key = URIIterator.next();
		if(obj.get(key) != null) {
			URIIterator.remove();
			return obj.get(key).searchDef(URIIterator);
		}

		logger.debug("searchDef: End node --> returning null");
		return null;
	}

	@Override
	public List<Entry<String,Defs>> collectDef() {
		return new LinkedList<>();
	}
	
	@Override
	public UnknownElement clone() {
		UnknownElement clone =  new UnknownElement();
		
		clone.obj.putAll(obj);
		
		return clone;
	}
	
}
