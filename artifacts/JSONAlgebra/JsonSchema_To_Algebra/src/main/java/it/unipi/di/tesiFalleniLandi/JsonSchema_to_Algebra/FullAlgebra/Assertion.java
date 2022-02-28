package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonElement;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessAssertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessPattReqManager;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import de.uni_passau.sds.patterns.REException;

public interface Assertion extends AlgebraParserElement{
	/**
	 * Exports the class as a json object (using GSON).
	 * @return return an object that represent the schema, see implementations for more details
	 * @param rootVar
	 */
	public JsonElement toJSONSchema(WitnessVarManager rootVar);
	
	/**
	 *
	 * @return return the complement of the current object
	 */
	public Assertion not();

	/**
	 *
	 * @return apply the not-elimination/not-pushing step as described in the paper, then return the new instance
	 */
	public Assertion notElimination(); //not pushing?

	/**
	 * Convert the class in a printable string using the full-algebra syntax
	 * @return return the printable string
	 */
	public String toGrammarString();

	/**
	 * Translate the object from the full-algebra representation to the witness-algebra (core-algebra plus some useful operators)
	 * @return return a WitnessAlgebra object
     * @param varManager
     * @param env
     * @param pattReqManager
     */
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException;
}
