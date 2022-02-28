package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnyOf_Assertion implements Assertion{
	private List<Assertion> orList;

	private static Logger logger = LogManager.getLogger(AnyOf_Assertion.class);
	
	public AnyOf_Assertion() {
		logger.trace("Creating an empty AnyOf_Assertion");
		this.orList = new LinkedList<>();
	}
	
	public void add(Assertion assertion) {
		if(assertion == null) return;
		logger.trace("Adding {} to {}", assertion, this);
		orList.add(assertion);
	}
	
	public void add(AnyOf_Assertion assertion) {
		addAll(assertion.orList);
	}
	
	public void addAll(List<Assertion> list) {
		logger.trace("Adding {} to {}", list, this);
		orList.addAll(list);
	}

	@Override
	public String toString() {
		return "Or_Assertion [" + orList + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonObject toJSONSchema(WitnessVarManager rootVar) {
		JsonObject obj = new JsonObject();
		JsonArray array = new JsonArray();

		for(Assertion assertion : orList) {
			array.add(assertion.toJSONSchema(rootVar));
		}

		obj.add("anyOf", array);

		return obj;
	}

	@Override
	public Assertion not() {
		AllOf_Assertion and = new AllOf_Assertion();
		
		for(Assertion assertion : orList) {
			Assertion not = assertion.not();
			if(not != null)
				and.add(not);
		}
		
		return and;
	}
	
	public Assertion notElimination() {
		AnyOf_Assertion or = new AnyOf_Assertion();
		
		for(Assertion assertion : orList) {
			Assertion not = assertion.notElimination();
			if(not != null)
				or.add(not);
		}
		
		return or;
	}
	
	
	@Override
	public String toGrammarString() {
		String str = "";
		
		Iterator<Assertion> it = orList.iterator();
			
		while(it.hasNext()) {
			Assertion next = it.next();
			String returnedValue = next.toGrammarString();
			if(returnedValue.isEmpty())
				continue;
			str += AlgebraStrings.COMMA + returnedValue;
		}
		
		if(str.isEmpty()) return "";
		if(orList.size() == 1) return str.substring(AlgebraStrings.COMMA.length());
		return AlgebraStrings.ANYOF(str.substring(AlgebraStrings.COMMA.length()));
	}

	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		WitnessOr or = new WitnessOr();
		for(Assertion a : orList){
			WitnessAssertion addend = a.toWitnessAlgebra(varManager, env, pattReqManager);
			or.add(addend);
		}

		return or;
	}




	public List<Assertion> getOrList() {
		return orList;
	}
}