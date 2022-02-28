package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonElement;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

interface JSONSchemaElement extends Cloneable{	
	/**
	 * Exports the java object as a json object (using GSON).
	 * @return an object that represent the schema, see implementations for more details
	 */
	public JsonElement toJSON();
	
	/**
	 * Apply the assertion separation step.
	 * @return a new instance with the assertion separation rule applied
	 */
	public JSONSchemaElement assertionSeparation();
	
	/**
	 * Convert the JSON Schema keyword to the corresponding representation in algebra.
	 * @return a string containing the representation of the current object
	 */
	public Assertion toGrammar();
	
	/**
	 * Questo metodo ritorna il numero di asserzioni che verranno stampate da
	 * @return the number of assertions
	 */
	public int numberOfTranslatableAssertions();
	
	/**
	 * Collects and deletes all the definitions contained by the object
	 * @return a list containing all the definitions found
	 */
	public List<Entry<String, Defs>> collectDef();
	
	/**
	 * Returns a list containing all the URIs of the $ref found in the schema
	 * @return a list containing all the uri of the various $ref, empty in case there was none
	 */
	public List<URI_JS> getRef();
	
	/**
	 * Tries to find the JSONSchema associated with a particular URI
	 * @param URIIterator iterator on the components of the URI
	 * @return an instance of Defs that contains all the definitions found, null otherwise
	 */
	public JSONSchema searchDef(Iterator<String> URIIterator);
	
	/**
	 * Clone the object
	 * @return a clone of the current object
	 */
	public JSONSchemaElement clone();
}
