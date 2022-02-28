package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Names_Assertion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class PropertyNames implements JSONSchemaElement{
	private JSONSchema propertyNames;

	private static Logger logger = LogManager.getLogger(PropertyNames.class);
	
	public PropertyNames() {
		logger.trace("Created a new  Type: {}", this);
	}
	
	public PropertyNames(JsonElement obj) {
		this.propertyNames = new JSONSchema(obj);
		logger.trace("Created a new PropertyNames: {}", this);
	}

	@Override
	public JsonElement toJSON() {
		JsonObject obj = new JsonObject();
		obj.add("propertyNames", propertyNames.toJSON());

		return obj;
	}

	@Override
	public JSONSchemaElement assertionSeparation() {
		PropertyNames p = new PropertyNames();
		p.propertyNames = this.propertyNames.assertionSeparation();
		
		return p;
	}

	@Override
	public Names_Assertion toGrammar() {
		return new Names_Assertion(propertyNames.toGrammar());
	}

	@Override
	public int numberOfTranslatableAssertions() {
		return propertyNames.numberOfTranslatableAssertions();
	}

	@Override
	public List<Entry<String, Defs>> collectDef() {
		return propertyNames.collectDef();
	}

	@Override
	public List<URI_JS> getRef() {
		return propertyNames.getRef();
	}

	@Override
	public JSONSchema searchDef(Iterator<String> URIIterator) {
		if(URIIterator.hasNext() && URIIterator.next().equals("propertyNames")) {
			logger.debug("searchDef: searching for {} in {}. URIIterator: {}", URIIterator.hasNext() ? URIIterator.next() : null, this, URIIterator);
			URIIterator.remove();
			return propertyNames.searchDef(URIIterator);
		}

		return null;
	}

	@Override
	public JSONSchemaElement clone() {
		PropertyNames clone = new PropertyNames();
		clone.propertyNames = propertyNames.clone();
		
		return clone;
	}

}
