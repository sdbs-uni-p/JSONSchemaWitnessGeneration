package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonElement;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessAssertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessPattReqManager;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

public class NotPattern_Assertion implements Assertion{
	private ComplexPattern notPattern;

	private static Logger logger = LogManager.getLogger(NotPattern_Assertion.class);

	public NotPattern_Assertion(ComplexPattern pattern) {
		this.notPattern = pattern;
		logger.trace("Created a new NotPatternAssertion: {}", this);
	}

	@Override
	public String toString() {
		return "NotPattern_Assertion [" + notPattern + "]";
	}

	@Override
	public JsonElement toJSONSchema(WitnessVarManager rootVar) {
		Type_Assertion t = new Type_Assertion();
		t.add(AlgebraStrings.TYPE_STRING);
		return new IfThenElse_Assertion(t, new Not_Assertion(new Pattern_Assertion(notPattern)), null).toJSONSchema(rootVar);

	}

	@Override
	public Assertion not() {
		AllOf_Assertion and = new AllOf_Assertion();
		Type_Assertion type = new Type_Assertion();
		type.add(AlgebraStrings.TYPE_STRING);
		and.add(type);
		and.add(new Pattern_Assertion(notPattern));

		return and;
	}

	@Override
	public Assertion notElimination() {
		return new NotPattern_Assertion(notPattern);
	}
	
	@Override
	public String toGrammarString() {
		return AlgebraStrings.NOTPATTERN(notPattern.getAlgebraString());
	}

	@Override
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		return new WitnessPattern(notPattern.complement());
	}

	public ComplexPattern getValue() {
		return notPattern;
	}
}
