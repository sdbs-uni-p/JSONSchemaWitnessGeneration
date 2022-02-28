package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

public class IfThenElse_Assertion implements Assertion{
	private Assertion ifStatement, thenStatement, elseStatement;

	private static Logger logger = LogManager.getLogger(IfThenElse_Assertion.class);

	public IfThenElse_Assertion(Assertion ifStatement, Assertion thenStatement, Assertion elseStatement) {
		this.ifStatement = ifStatement;
		this.thenStatement = thenStatement;
		this.elseStatement = elseStatement;

		logger.trace("Created a new IfThenElseAssertion: {}", this);
	}

	@Override
	public String toString() {
		return "IfThenElse_Assertion [ifStatement=" + ifStatement + ", thenStatement=" + thenStatement
				+ ", elseStatement=" + elseStatement + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonElement toJSONSchema(WitnessVarManager rootVar) {
		JsonObject obj = new JsonObject();

		if(ifStatement != null)
			obj.add("if", ifStatement.toJSONSchema(rootVar));

		if(thenStatement != null)
			obj.add("then", thenStatement.toJSONSchema(rootVar));

		if(elseStatement != null)
			obj.add("else", elseStatement.toJSONSchema(rootVar));

		return obj;
	}

	//if A then B else C --> (A and B) or (not(a) and C)
	//(S1 ∧ S2) ∨ ((¬S1) ∧ S3)
	//(¬S1) ∨ (S2)
	//NOT: (S1 ∧ ¬S2)
	//NOT: (not(A) or not(B)) and (A or not(C)) --? if A then not(C) else not(B) ???
	@Override
	public Assertion not() {	
		AllOf_Assertion and = new AllOf_Assertion();

		//if-then-else
		if(elseStatement != null) {
			AnyOf_Assertion orThen = new AnyOf_Assertion();
			AnyOf_Assertion orElse = new AnyOf_Assertion();
			orThen.add(ifStatement.not());
			orThen.add(thenStatement.not());
			and.add(orThen);
			orElse.add(ifStatement);
			orElse.add(elseStatement.not());
			and.add(orElse);
		}else{ //if-then
			and.add(ifStatement);
			and.add(thenStatement.not());
		}

		return and;
	}

	@Override
	public Assertion notElimination() {
		Assertion ifStat = ifStatement.notElimination();
		Assertion thenStat = thenStatement.notElimination();
		Assertion elseStat = null;
		if(elseStatement != null) elseStat = elseStatement.notElimination();

		if(ifStat == null) return thenStat; //if null == true ??
		return new IfThenElse_Assertion(ifStat, thenStat, elseStat);
	}

	@Override
	public String toGrammarString() {
		String if_str = "true", then_str = "true", else_str = "true";
		if(ifStatement != null)		if_str = ifStatement.toGrammarString();
		if(thenStatement != null)	then_str = thenStatement.toGrammarString();
		if(elseStatement != null)	else_str = elseStatement.toGrammarString();
		else
			return AlgebraStrings.IF_THEN(if_str, then_str);

		return AlgebraStrings.IF_THEN(if_str, then_str, else_str);
	}

	@Override
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		WitnessOr or = new WitnessOr();
		if(elseStatement == null){
			or.add(ifStatement.not().toWitnessAlgebra(varManager,env, pattReqManager));
			or.add(thenStatement.toWitnessAlgebra(varManager,env, pattReqManager));

			return or;
		}

		WitnessAnd and = new WitnessAnd();

		and.add(ifStatement.toWitnessAlgebra(varManager,env, pattReqManager));
		and.add(thenStatement.toWitnessAlgebra(varManager,env, pattReqManager));
		or.add(and);

		WitnessAnd and2 = new WitnessAnd();
		and2.add(ifStatement.not().toWitnessAlgebra(varManager,env, pattReqManager));
		and2.add(elseStatement.toWitnessAlgebra(varManager,env, pattReqManager));
		or.add(and2);

		return or;
	}

}
