package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessAssertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessPattReqManager;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.LinkedList;
import java.util.List;

public class OneOf_Assertion implements Assertion{
	private List<Assertion> xorList;

	private static Logger logger = LogManager.getLogger(OneOf_Assertion.class);
	
	public OneOf_Assertion() {
		logger.trace("Creating an empty OneOf_Assertion");
		this.xorList = new LinkedList<>();
	}
	
	public void add(Assertion assertion) {
		if(assertion == null) return;
		logger.trace("Adding {} to {}", assertion, this);
		xorList.add(assertion);
	}
	
	public void add(OneOf_Assertion assertion) {
		addAll(assertion.xorList);
	}
	
	public void addAll(List<Assertion> list) {
		logger.trace("Adding all {} to {}", list, this);
		xorList.addAll(list);
	}

	@Override
	public String toString() {
		return "Xor_Assertion [" + xorList + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonObject toJSONSchema(WitnessVarManager rootVar) {
		JsonObject obj = new JsonObject();
		JsonArray array = new JsonArray();
		
		for(Assertion assertion : xorList) {
			array.add(assertion.toJSONSchema(rootVar));
		}

		obj.add("oneOf", array);

		return obj;
	}

	@Override
	public Assertion not() {
		List<Assertion> notXorList = new LinkedList<>();

		for(Assertion a : xorList) {
			Assertion not = a.not();
			if(not != null)
				notXorList.add(not);
		}

		AllOf_Assertion andList = new AllOf_Assertion();

		for(int i = 0; i < xorList.size(); i++) {
			AnyOf_Assertion orList = new AnyOf_Assertion();
			for (int j = 0; j < xorList.size(); j++) {
				if (i == j) orList.add(notXorList.get(j));
				else orList.add(xorList.get(j));
			}
			andList.add(orList);
		}

		return andList;
	}

	public Assertion notElimination() {
		OneOf_Assertion xor = new OneOf_Assertion();
		
		for(Assertion assertion : xorList) {
			Assertion not = assertion.notElimination();
			if(not != null)
				xor.add(not);
		}
		
		return xor;
	}
	
	@Override
	public String toGrammarString() {
		StringBuilder str = new StringBuilder();

		for (Assertion assertion : xorList) {
			String returnedValue = assertion.toGrammarString();
			if (returnedValue.isEmpty())
				continue;
			str.append(AlgebraStrings.COMMA)
					.append(returnedValue);
		}
		
		if(str.length() == 0) return "";
		if(xorList.size() == 1) return str.substring(AlgebraStrings.COMMA.length());
		return AlgebraStrings.ONEOF(str.substring(AlgebraStrings.COMMA.length()));
	}

	@Override
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		return this.not().not().toWitnessAlgebra(varManager, env, pattReqManager);
	}
}