package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessAssertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessPattReqManager;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessUniqueItems;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

public class UniqueItems_Assertion implements Assertion{
	private static Logger logger = LogManager.getLogger(UniqueItems_Assertion.class);

	public UniqueItems_Assertion() {
		logger.trace("Created a new UniqueItems_Assertion");
	}

	@Override
	public String toString() {
		return "UniqueItems_Assertion{" +
				'}';
	}

	@Override
	public JsonObject toJSONSchema(WitnessVarManager rootVar) {
		JsonObject obj = new JsonObject();
		obj.addProperty("uniqueItems", true);

		return obj;
	}

	@Override
	public Assertion not() {
		AllOf_Assertion and = new AllOf_Assertion();
		Type_Assertion type = new Type_Assertion();
		type.add(AlgebraStrings.TYPE_ARRAY);
		and.add(type);
		and.add(new RepeatedItems_Assertion());

		return and;
	}

	@Override
	public Assertion notElimination() {
		return new UniqueItems_Assertion();
	}

	@Override
	public String toGrammarString() {
		return AlgebraStrings.UNIQUEITEMS;
	}

	@Override
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		return new WitnessUniqueItems();
	}

}
