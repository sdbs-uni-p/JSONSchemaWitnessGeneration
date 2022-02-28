package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessBoolean;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessPattReqManager;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

public class Boolean_Assertion implements Assertion{
	private boolean value;

	private static Logger logger = LogManager.getLogger(Boolean_Assertion.class);
	
	public Boolean_Assertion(boolean value) {
		this.value = value;
		logger.trace("Creating a Boolean_Assertion with value {}", value);
	}

	@Override
	public String toString() {
		return "Boolean_Assertion [value=" + value + "]";
	}

	@Override
	public JsonElement toJSONSchema(WitnessVarManager rootVar) {
		return new JsonPrimitive(value);
	}

	@Override
	public Assertion not() {
		return new Boolean_Assertion(!value);
	}

	@Override
	public Assertion notElimination() {
		return new Boolean_Assertion(value);
	}

	@Override
	public String toGrammarString() {
		return value+"";
	}

	@Override
	public WitnessBoolean toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		return new WitnessBoolean(value);
	}

	public boolean getValue() {
		return value;
	}
}
