package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonElement;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PatternRequired_Assertion implements Assertion{
	private HashMap<ComplexPattern, Assertion> pattReq;

	private static Logger logger = LogManager.getLogger(PatternRequired_Assertion.class);

	public PatternRequired_Assertion() {
		logger.trace("Creating an empty PatternRequired_Assertion");
		pattReq = new HashMap<>();
	}

	public PatternRequired_Assertion(ComplexPattern name, Assertion assertion) {
		pattReq = new HashMap<>();
		pattReq.put(name, assertion);

		logger.trace("Created a new PatternRequired_Assertion: {}", this);
	}

	public void add(ComplexPattern key, Assertion value) {
		logger.trace("Adding <{}, {}> to {}", key, value, this);
		if(pattReq.containsKey(key)) throw new ParseCancellationException("Detected 2 patternRequired with the same name");
		pattReq.put(key, value);
	}

	@Override
	public String toString() {
		return "PatternRequired_Assertion [" + pattReq + "]";
	}

	@Override
	public JsonElement toJSONSchema(WitnessVarManager rootVar) {
		Type_Assertion t = new Type_Assertion();
		t.add(AlgebraStrings.TYPE_OBJECT);
		AllOf_Assertion and = new AllOf_Assertion();

		for(Map.Entry<ComplexPattern, Assertion> entry : pattReq.entrySet()) {
			Properties_Assertion pro = new Properties_Assertion();
			pro.addPatternProperties(entry.getKey(), entry.getValue().not());
			and.add(new Not_Assertion(pro));
		}

		return new IfThenElse_Assertion(t, and, null).toJSONSchema(rootVar);
	}
	
	@Override
	public Assertion not() {
		AllOf_Assertion and = new AllOf_Assertion();
		AnyOf_Assertion or = new AnyOf_Assertion();
		Type_Assertion type = new Type_Assertion();
		type.add("obj");
		
		Set<Map.Entry<ComplexPattern, Assertion>> entrySet = pattReq.entrySet();
		
		for(Map.Entry<ComplexPattern, Assertion> entry : entrySet) {
			Properties_Assertion properties = new Properties_Assertion();
			Assertion not = entry.getValue().not();
			if(not != null)
				properties.addPatternProperties(entry.getKey(), not);
			or.add(properties);
		}
		
		and.add(type);
		and.add(or);
		
		return and;
	}

	@Override
	public Assertion notElimination() {
		PatternRequired_Assertion pattReqAss = new PatternRequired_Assertion();
		Set<Map.Entry<ComplexPattern, Assertion>> entrySet = pattReq.entrySet();
		
		for(Map.Entry<ComplexPattern, Assertion> entry : entrySet) {
			Assertion not = entry.getValue().notElimination();
			if(not != null)
				pattReqAss.add(entry.getKey(), not);
		}
		
		return pattReqAss;
	}
	
	@Override
	public String toGrammarString() {
		String str = "";
		
		if(pattReq != null) {
			Set<Map.Entry<ComplexPattern, Assertion>> entrySet = pattReq.entrySet();
			for(Map.Entry<ComplexPattern, Assertion> entry : entrySet) {
				String returnedValue = entry.getValue().toGrammarString();
				if(!returnedValue.isEmpty())
					str += AlgebraStrings.COMMA + entry.getKey().getAlgebraString()+":"+ returnedValue;
			}
		}

		if(str.isEmpty()) return "";
		return String.format(AlgebraStrings.PATTERNREQUIRED, str.substring(AlgebraStrings.COMMA.length()), "");
	}

	@Override
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		//WitnessOr or = new WitnessOr();
		WitnessAnd and = new WitnessAnd();
		//Type_Assertion tmp = new Type_Assertion();
		//tmp.add(AlgebraStrings.TYPE_OBJECT);
		//WitnessAssertion type = tmp.not().toWitnessAlgebra(varManager,env, pattReqManager);

		Set<Map.Entry<ComplexPattern, Assertion>> entrySet = pattReq.entrySet();

		for(Map.Entry<ComplexPattern, Assertion> entry : entrySet) {
			ComplexPattern p = entry.getKey().clone();
			WitnessPattReq pattReq = pattReqManager.build(p, entry.getValue().toWitnessAlgebra(varManager,env, pattReqManager));
			and.add(pattReq);
		}

		//or.add(type);
		//or.add(and);

		//return or;
		return and;
	}

}
