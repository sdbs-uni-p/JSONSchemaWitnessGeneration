package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonElement;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

public class ExName_Assertion implements Assertion{
	private Assertion names;

	private static Logger logger = LogManager.getLogger(ExName_Assertion.class);

	public ExName_Assertion(Assertion names) {
		this.names = names;
		logger.trace("Created a new ExNames_Assertion: {}", this);
	}

	@Override
	public String toString() {
		return "Names_Assertion [" + names + "]";
	}

	@Override
	public JsonElement toJSONSchema(WitnessVarManager rootVar) {
		return new Not_Assertion(new Names_Assertion(names)).toJSONSchema(rootVar);
	}
	
	@Override
	public Assertion not() {
		AllOf_Assertion and = new AllOf_Assertion();
		Type_Assertion type = new Type_Assertion();
		type.add(AlgebraStrings.TYPE_OBJECT);
		and.add(type);
		if(names.not() != null)
			and.add(new Names_Assertion(names.not()));
		
		return and;
	}

	@Override
	public Assertion notElimination() {
		return new ExName_Assertion(names.notElimination());
	}

	@Override
	public String toGrammarString() {
		return AlgebraStrings.EXNAME(names.toGrammarString());
	}

	@Override
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		return pattReqManager.build(Utils_PattOfS.pattOfS(names, env), new WitnessBoolean(true));
	}
}
