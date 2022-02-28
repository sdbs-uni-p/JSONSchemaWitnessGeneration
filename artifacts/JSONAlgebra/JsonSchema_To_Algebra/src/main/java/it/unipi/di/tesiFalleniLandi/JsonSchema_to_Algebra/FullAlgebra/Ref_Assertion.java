package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessAssertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessPattReqManager;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

public class Ref_Assertion implements Assertion{
	private String ref;

	private static Logger logger = LogManager.getLogger(Ref_Assertion.class);
	
	public Ref_Assertion(String ref) {
		this.ref = ref;
		logger.trace("Created a new Ref_Assertion: {}", this);
	}
	
	@Override
	public String toString() {
		return "Ref_Assertion [" + ref + "]";
	}

	@Override
	public JsonElement toJSONSchema(WitnessVarManager rootVar) {
		JsonObject obj = new JsonObject();

		if(ref.equals(rootVar)) {
			obj.addProperty("$ref", "#" + ref);
		}else {
			obj.addProperty("$ref", "#/$defs/" + ref);
		}

		return obj;
	}

	@Override
	public Assertion not() { //le definizioni sono not completate, mi basta cambiare il nome TODO: check in witness
		if(ref.startsWith(AlgebraStrings.NOT_DEFS))
			return new Ref_Assertion(ref.substring(AlgebraStrings.NOT_DEFS.length()));
		else return new Ref_Assertion(AlgebraStrings.NOT_DEFS+ref);
	}

	@Override
	public Assertion notElimination() {
		return new Ref_Assertion(ref);
	}
	
	@Override
	public String toGrammarString() {
		return AlgebraStrings.REF(ref);
	}

	@Override
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		return varManager.buildVar(ref);
	}

	public String getRef(){
		return ref;
	}
}
