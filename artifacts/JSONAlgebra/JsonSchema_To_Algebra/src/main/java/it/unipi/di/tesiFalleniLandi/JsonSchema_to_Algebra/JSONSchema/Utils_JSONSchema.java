package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.*;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.Utils;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Boolean_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions.SyntaxErrorRuntimeException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions.UnsupportedURIRuntimeException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class Utils_JSONSchema {
	private static Logger logger = LogManager.getLogger(Utils_JSONSchema.class);

	public static JSONSchema parse(String path) throws IOException {
		Gson gson = new Gson();
		try (Reader reader = new FileReader(path)){
			JsonObject object = gson.fromJson(reader, JsonObject.class);
			return new JSONSchema(object);
		}catch (JsonSyntaxException ex){
			throw new SyntaxErrorRuntimeException(ex);
		}
	}

	//We first execute the reference normalization operation, then the assertion separation op.
	public static JSONSchema normalize(JSONSchema root) {
		return referenceNormalization(root.clone()).assertionSeparation();
	}

	public static JSONSchema referenceNormalization(JSONSchema root) {
		logger.trace("Performing referenceNormalizzation");

		List<URI_JS> refList = root.getRef(); //collect all refs

		JSONSchema newRoot = new JSONSchema();
		List<Entry<String, Defs>> defsList = addPathElement("#", root.collectDef()); //collect all defs

		logger.trace("List of defs under $defs or definitions: {}", defsList);
		logger.trace("List of ref: {}", refList);

		Defs finalDefs = new Defs();

		for(URI_JS ref : refList) {
			if(ref.toString().equals("#")    ||    ref.toString().charAt(0) != '#') //I cannot resolve (in this phase)
				//ref to the entire file     or    ref to external files
			{
				logger.trace("Cannot resolve ref: {}", ref);
				continue;
			}
			boolean found = false;
			for(Entry<String, Defs> entry : defsList) {
				JSONSchema s = compareDefsRefs(entry, ref);
				if(s != null) {
					ref.found();
					finalDefs.addDef(ref.getNormalizedName(), s);
					found = true;
					logger.trace("Def found: ref {} match {}. \r\n\tBody: {}", ref, entry, s);
					break;
				}
			}
			if(found) {
				continue;
			}

			logger.trace("Trying to search {} across the document", ref);
			//ref is not in the defsList, let's try to search it in the document
			JSONSchema newDef = root.searchDef(ref.iterator());
			if(newDef != null) {
				ref.found();
				finalDefs.addDef(ref.getNormalizedName(), newDef);
			}else
				throw new UnsupportedURIRuntimeException("ref not resolved: "+ref);
		}

		//add all defs defined but not used
		for(Entry<String, Defs> entry : defsList)
			finalDefs.addDef(entry.getValue());

		//schema with only definitions
		if(root.numberOfTranslatableAssertions() != 0)
			finalDefs.setRootDef(root.clone());
		else
			finalDefs.setRootDef(new JSONSchema(new JsonPrimitive(true)));

		//add to schema the normalized defs
		newRoot.addJSONSchemaElement("$defs", finalDefs);

		return newRoot;
	}

	private static JSONSchema compareDefsRefs(Entry<String, Defs> entry, URI_JS ref) {
		String[] defUriSplitted = entry.getKey().split("/");
		String[] refUriSplitted = ref.toString().replace("definitions", "$defs").split("/");

		if(defUriSplitted.length +1 != refUriSplitted.length)
			return null;

		for(int i = 0; i < defUriSplitted.length; i++)
			if(!defUriSplitted[i].equals(refUriSplitted[i])) return null;

		return entry.getValue().containsDef(refUriSplitted[refUriSplitted.length-1]);

	}

	static List<Entry<String,Defs>> addPathElement(String key, List<Entry<String,Defs>> list){
		List<Entry<String,Defs>> newList = new LinkedList<>();
		for(Entry<String, Defs> entry : list)
			newList.add(new AbstractMap.SimpleEntry<>(key+"/"+entry.getKey(), entry.getValue()));

		return newList;
	}


	public static String toGrammarString(JSONSchema root) {
		return Utils.beauty(root.toGrammar().toGrammarString());
	}

	public static JsonObject mergeJsonObject(JsonObject a, JsonObject b){
		JsonObject obj = new JsonObject();


		for(Entry<String, JsonElement> p : a.entrySet())
			obj.add(p.getKey(), p.getValue());


		for(Entry<String, JsonElement> p : b.entrySet())
			obj.add(p.getKey(), p.getValue());

		return obj;
	}

}