package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Boolean_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Exist_Assertion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class Contains implements JSONSchemaElement{
	private JSONSchema contains;
	private Long minContains;
	private Long maxContains;

	private static Logger logger = LogManager.getLogger(Contains.class);
	
	public Contains() {
		minContains = 1L;
		maxContains = Long.MAX_VALUE;
		logger.trace("Creating an empty Contains");
	}
	
	public void setContains(JsonElement obj) {
		logger.trace("Setting contains by parsing {}", obj);
		contains = new JSONSchema(obj);
	}
	
	public void setMinContains(JsonElement obj) {
		logger.trace("Setting minContains by parsing {}", obj);
		Long value = obj.getAsLong();
		minContains = value;
	}
	
	public void setMaxContains(JsonElement obj) {
		logger.trace("Setting maxContains by parsing {}", obj);
		Long value = obj.getAsLong();
		maxContains = value;
	}
	
	@Override
	public String toString() {
		return "Contains [contains=" + contains + ", minContains=" + minContains + ", maxContains=" + maxContains + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonObject toJSON() {
		JsonObject obj = new JsonObject();
		
		if(contains != null) obj.add("contains", contains.toJSON());
		if(minContains != null) obj.addProperty("minContains", minContains);
		if(maxContains != null) obj.addProperty("maxContains", maxContains);
		
		return obj;
	}

	@Override
	public Assertion toGrammar() {
		if (contains == null)
			return new Boolean_Assertion(true);
		else return new Exist_Assertion(minContains, maxContains, contains.toGrammar());
	}

	@Override
	public Contains assertionSeparation() {
		Contains obj = new Contains();
		
		if(contains != null) obj.contains = contains.assertionSeparation();
		if(minContains != null) obj.minContains = minContains;
		if(maxContains != null) obj.maxContains = maxContains;
		
		return obj;
	}
	
	@Override
	public List<URI_JS> getRef() {
		if(contains != null)
			return contains.getRef();
		else
			return new LinkedList<>();
	}

	@Override
	public JSONSchema searchDef(Iterator<String> URIIterator) {
		if(URIIterator.hasNext() && URIIterator.next().equals("contains")){
			URIIterator.remove();
			logger.debug("searchDef: searching in contains. URIIterator: {}", URIIterator);
			return contains.searchDef(URIIterator);
		}

		logger.debug("searchDef: End node --> returning null");
		return null;
	}

	@Override
	public List<Entry<String,Defs>> collectDef() {
		List<Entry<String,Defs>> returnList = new LinkedList<>();

		if(contains != null)
			returnList.addAll(Utils_JSONSchema.addPathElement("contains",contains.collectDef()));
		
		return returnList;
	}

	@Override
	public int numberOfTranslatableAssertions() {
		if(contains != null)
			return contains.numberOfTranslatableAssertions();

		return 0;
	}
	
	public Contains clone() {
		Contains clone = new Contains();

		if(contains != null) clone.contains = contains.clone();
		clone.minContains = minContains;
		clone.maxContains = maxContains;
		
		return clone;
	}
}
