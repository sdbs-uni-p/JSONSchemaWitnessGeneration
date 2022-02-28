package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class Properties_Assertion implements Assertion{
	private HashMap<ComplexPattern, Assertion> properties_patternProperties;
	private Assertion additionalProperties;

	private static Logger logger = LogManager.getLogger(Properties_Assertion.class);
	
	public Properties_Assertion() {
		properties_patternProperties = new HashMap<>();
		logger.trace("Created a new Properties_Assertion: {}", this);
	}
	
	public void addProperties(String key, Assertion value) throws REException {
		if(properties_patternProperties.containsKey(key))
			throw new ParseCancellationException("Detected 2 properties with the same name");
		logger.trace("Adding as Properties <{}, {}> to {}", key, value, this);
		properties_patternProperties.put(ComplexPattern.createFromName(key), value);
	}

	public void addPatternProperties(String key, Assertion value) throws REException {
		if(properties_patternProperties.containsKey(key))
			throw new ParseCancellationException("Detected 2 patternProperties with the same pattern");
		logger.trace("Adding as PatternProperties <{}, {}> to {}", key, value, this);
		properties_patternProperties.put(ComplexPattern.createFromRegexp(key), value);
	}

	public void addPatternProperties(ComplexPattern key, Assertion value) {
		if(properties_patternProperties.containsKey(key))
			throw new ParseCancellationException("Detected 2 patternProperties with the same pattern");
		logger.trace("Adding as PatternProperties <{}, {}> to {}", key, value, this);
		properties_patternProperties.put(key, value);
	}
	
	public void setAdditionalProperties(Assertion additionalProperties) {
		logger.trace("Adding as AdditionalProperties {} to {}", additionalProperties, this);
		this.additionalProperties = additionalProperties;
	}

	@Override
	public String toString() {
		return "Properties_Assertion{" +
				"properties_patternProperties=" + properties_patternProperties +
				", additionalProperties=" + additionalProperties +
				'}';
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonObject toJSONSchema(WitnessVarManager rootVar) {
		JsonObject obj = new JsonObject();
		
		if(properties_patternProperties != null && !properties_patternProperties.isEmpty()){
			JsonObject tmpProps = new JsonObject();
			JsonObject tmpPattProps = new JsonObject();
			Set<ComplexPattern> keys = properties_patternProperties.keySet();

			for(ComplexPattern key : keys) {
				String keyString = key.toString();
				keyString = keyString.substring(1, keyString.length() -1);
				if(key.domainSize() == 1 && !key.isComplex()) //TODO: verificare
					tmpProps.add(keyString.substring(1, keyString.length()-1), properties_patternProperties.get(key).toJSONSchema(rootVar));
				else
					tmpPattProps.add(keyString, properties_patternProperties.get(key).toJSONSchema(rootVar));
			}

			if(tmpProps.size() > 0)
				obj.add("properties", tmpProps);
			if(tmpPattProps.size() > 0)
				obj.add("patternProperties", tmpPattProps);
		}
		
		if(additionalProperties != null)
			obj.add("additionalProperties", additionalProperties.toJSONSchema(rootVar));
		
		return obj;
	}

	@Override
	public Assertion not() {
		AllOf_Assertion and = new AllOf_Assertion();

		if(properties_patternProperties.isEmpty() && additionalProperties == null) {
			and.add(new Boolean_Assertion(false));
			return and;
		}

		Type_Assertion type = new Type_Assertion();
		AddPatternRequired_Assertion addPattRequired = new AddPatternRequired_Assertion();
		AnyOf_Assertion or = new AnyOf_Assertion();
		type.add(AlgebraStrings.TYPE_OBJECT);
		and.add(type);
		and.add(or);
		
		/*Set<Entry<String, Assertion>> entrySet = properties.entrySet();
		
		for(Entry<String, Assertion> entry : entrySet) {
			Assertion not = entry.getValue().not();
			if(not != null) {
				or.add(new PatternRequired_Assertion(ComplexPattern.createFromName(entry.getKey()), not));
				addPattRequired.addName(ComplexPattern.createFromName(entry.getKey()));
			}
		}*/

		Set<Entry<ComplexPattern, Assertion>> entrySetPatt = properties_patternProperties.entrySet();

		for(Entry<ComplexPattern, Assertion> entry : entrySetPatt) {
			Assertion not = entry.getValue().not();
			if(not != null) {
				or.add(new PatternRequired_Assertion(entry.getKey(), not));
				addPattRequired.addName(entry.getKey());
			}
		}
		
		if(additionalProperties != null) {
			Assertion not = additionalProperties.not();
			if(not != null) {
				addPattRequired.setAdditionalProperties(not);
				or.add(addPattRequired);
			}
		}
		
		return and;
	}

	@Override
	public Assertion notElimination() {
		Properties_Assertion prop = new Properties_Assertion();

		/*
		Set<Entry<String, Assertion>> entrySet = properties.entrySet();
		
		for(Entry<String, Assertion> entry : entrySet) {
			Assertion not = entry.getValue().notElimination();
			if(not != null)
				prop.addProperties(entry.getKey(), not);
		}

		 */

		Set<Entry<ComplexPattern, Assertion>> entrySetPatt = properties_patternProperties.entrySet();

		for(Entry<ComplexPattern, Assertion> entry : entrySetPatt) {
			Assertion not = entry.getValue().notElimination();
			if (not != null)
				prop.addPatternProperties(entry.getKey(), not);
		}
		
		if(additionalProperties != null) {
			Assertion not = additionalProperties.notElimination();
			if (not != null)
				prop.setAdditionalProperties(additionalProperties.notElimination());
		}
		
		return prop;
	}

	@Override
	public String toGrammarString() {
		StringBuilder str = new StringBuilder();

		/*
		if(properties != null) {
			Set<Entry<String, Assertion>> entrySet = properties.entrySet();
			for(Entry<String, Assertion> entry : entrySet) {
				String returnedValue = entry.getValue().toGrammarString();
				if(!returnedValue.isEmpty())
					str.append(FullAlgebraString.COMMA)
							.append(FullAlgebraString.SINGLEPROPERTIES(entry.getKey(), returnedValue));
			}
		}

		 */

		if(properties_patternProperties != null) {
			Set<Entry<ComplexPattern, Assertion>> entrySet = properties_patternProperties.entrySet();
			for(Entry<ComplexPattern, Assertion> entry : entrySet) {
				String returnedValue = entry.getValue().toGrammarString();
				if(!returnedValue.isEmpty())
					str.append(AlgebraStrings.COMMA)
							.append(AlgebraStrings.SINGLEPATTERNPROPERTIES(entry.getKey().getAlgebraString(), returnedValue));

			}
		}

		if(additionalProperties != null)
			if(str.length() == 0)
				return AlgebraStrings.PROPERTIES("", additionalProperties.toGrammarString());
			else
				return AlgebraStrings.PROPERTIES(str.substring(AlgebraStrings.COMMA.length()), additionalProperties.toGrammarString());

		if(str.length() == 0 && additionalProperties == null) return AlgebraStrings.PROPERTIES();
		return AlgebraStrings.PROPERTIES(str.substring(AlgebraStrings.COMMA.length())); // deleted: , "");
	}

	@Override
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		WitnessAnd and = new WitnessAnd();
		ComplexPattern usedPatt = null;//= ComplexPattern.createFromRegexp(".*").complement();//TODO: correggere

		/*
		Set<Entry<String, Assertion>> entrySet = properties.entrySet();

		for (Entry<String, Assertion> entry : entrySet) {
			ComplexPattern p = ComplexPattern.createFromName(entry.getKey());
			WitnessProperty prop = new WitnessProperty(p, entry.getValue().toWitnessAlgebra());
			and.add(prop);
			if(usedPatt == null) usedPatt = p;
			else usedPatt = usedPatt.union(p);
		}


		 */
		Set<Entry<ComplexPattern, Assertion>> entrySetPatt = properties_patternProperties.entrySet();

		for (Entry<ComplexPattern, Assertion> entry : entrySetPatt) {
			ComplexPattern p = entry.getKey().clone();
			WitnessProperty pattProp = new WitnessProperty(p, entry.getValue().toWitnessAlgebra(varManager,env,pattReqManager));
			and.add(pattProp);
			if(usedPatt == null) usedPatt = p;
			else usedPatt = usedPatt.union(p);
		}

		if (additionalProperties != null) {
			WitnessProperty addProp;
			if(usedPatt == null)
				addProp = new WitnessProperty(ComplexPattern.createFromRegexp(".*"), additionalProperties.toWitnessAlgebra(varManager,env,pattReqManager));
			else
				addProp = new WitnessProperty(usedPatt.complement(), additionalProperties.toWitnessAlgebra(varManager,env,pattReqManager));

			and.add(addProp);
		}


		return and;
	}
}
