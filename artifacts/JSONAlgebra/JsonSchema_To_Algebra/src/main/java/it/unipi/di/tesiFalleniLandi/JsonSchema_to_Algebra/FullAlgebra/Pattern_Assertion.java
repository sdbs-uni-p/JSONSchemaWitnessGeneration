package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessAssertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessPattReqManager;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

public class Pattern_Assertion implements Assertion{
	private ComplexPattern pattern;

	private static Logger logger = LogManager.getLogger(Pattern_Assertion.class);

	public Pattern_Assertion(ComplexPattern pattern) {
		this.pattern = pattern;
		logger.trace("Created a new Pattern_Assertion: {}", this);
	}

	@Override
	public String toString() {
		return "Pattern_Assertion [" + pattern + "]";
	}

	public ComplexPattern getValue(){
		return pattern;
	}

	@Override
	public JsonElement toJSONSchema(WitnessVarManager rootVar) {
		JsonObject obj = new JsonObject();
		if(pattern.isComplex()) return pattern.toJSONSchema(); //TODO: devo forse chiuderlo dentro un allOf? come evito i duplicati?
		else obj.addProperty("pattern", pattern.getOriginalPattern());

		return obj;
	}

	@Override
	public Assertion not() {
		AllOf_Assertion and = new AllOf_Assertion();
		and.add(new NotPattern_Assertion(pattern));
		Type_Assertion type = new Type_Assertion();
		type.add(AlgebraStrings.TYPE_STRING);
		and.add(type);
		return and;
	}

	@Override
	public Assertion notElimination() {
		return new Pattern_Assertion(pattern);
	}

	@Override
	public String toGrammarString() {
		return AlgebraStrings.PATTERN(pattern.getAlgebraString());
	}

	@Override
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		return new WitnessPattern(pattern);
	}
}
