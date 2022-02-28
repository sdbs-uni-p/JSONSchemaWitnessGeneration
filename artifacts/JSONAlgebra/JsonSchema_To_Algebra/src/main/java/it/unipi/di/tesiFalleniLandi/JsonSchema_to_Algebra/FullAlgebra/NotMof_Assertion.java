package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessNotMof;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessPattReqManager;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

public class NotMof_Assertion implements Assertion {
	private Object notMof;

	private static Logger logger = LogManager.getLogger(NotMof_Assertion.class);
	
	public NotMof_Assertion(Object notMof) {
		this.notMof = notMof;
		logger.trace("Created a new NotMof_Assertion: {}", this);
	}

	@Override
	public String toString() {
		return "NotMof_Assertion [" + notMof + "]";
	}

	@Override
	public JsonObject toJSONSchema(WitnessVarManager rootVar) {
		return new Not_Assertion(new Mof_Assertion(notMof)).toJSONSchema(rootVar);
	}

	@Override
	public Assertion not() {
		AllOf_Assertion mof = new AllOf_Assertion();
		Type_Assertion type = new Type_Assertion();
		type.add(AlgebraStrings.TYPE_NUMBER);
		mof.add(type);
		mof.add(new Mof_Assertion(notMof));
		
		return mof;
	}

	@Override
	public Assertion notElimination() {
		return new NotMof_Assertion(notMof);
	}

	@Override
	public String toGrammarString() {
		return AlgebraStrings.NOTMULTIPLEOF(notMof.toString());
	}

	@Override
	public WitnessNotMof toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		return new WitnessNotMof(Double.parseDouble(notMof.toString()));
	}
}
