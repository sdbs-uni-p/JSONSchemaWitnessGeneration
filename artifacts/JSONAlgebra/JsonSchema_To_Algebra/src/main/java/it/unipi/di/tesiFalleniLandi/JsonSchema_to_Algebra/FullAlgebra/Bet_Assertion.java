package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

public class Bet_Assertion implements Assertion{
	private Number min;
	private Number max;

	private static Logger logger = LogManager.getLogger(Bet_Assertion.class);
	
	public Bet_Assertion() {
		logger.trace("Creating an empty Bet_Assertion");
	}
	
	public Bet_Assertion(Number min, Number max) {
		this.min = min;
		this.max = max;

		logger.trace("Created a Bet_Assertion: {}", this);
	}
	
	public void setMin(Number min) {
		logger.trace("Setting {} as min in {}", min, this);
		this.min = min;
	}
	
	public void setMax(Number max) {
		logger.trace("Setting {} as max in {}", max, this);
		this.max = max;
	}

	@Override
	public String toString() {
		return "Bet_Assertion [" + min + ", " + max + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonElement toJSONSchema(WitnessVarManager rootVar) {
		JsonObject obj = new JsonObject();
		
		if(min != null) obj.addProperty("minimum", min);
		if(max != null) obj.addProperty("maximum", max);
		
		return obj;
	}

	@Override
	public Assertion not() {
		AllOf_Assertion and = new AllOf_Assertion();
		Type_Assertion type = new Type_Assertion();
		type.add(AlgebraStrings.TYPE_NUMBER);
		and.add(type);
		
		if(min != null && max != null) {
			AnyOf_Assertion or = new AnyOf_Assertion();
			or.add(new XBet_Assertion(null, min));
			or.add(new XBet_Assertion(max, null));
			and.add(or);
			return and;
		}
		
		if(min != null) {
			and.add(new XBet_Assertion(null, min));
			return and;
		}
		
		
		and.add(new XBet_Assertion(max, null));
		return and;
	}

	@Override
	public Assertion notElimination() {
		return new Bet_Assertion(min, max);
	}
	
	public String toGrammarString() {
		String min = AlgebraStrings.NEG_INF;
		String max = AlgebraStrings.POS_INF;

		if(this.min != null) min = this.min+"";
		if(this.max != null) max = this.max+"";

		return AlgebraStrings.BETWEENNUMBER(min, max);
	}

	@Override
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		Double min = null;
		Double max = null;

		if(this.min != null)
			min = Double.parseDouble(this.min.toString());
		else
			min = Double.NEGATIVE_INFINITY;

		if(this.max != null)
			max = Double.parseDouble(this.max.toString());
		else
			max = Double.POSITIVE_INFINITY;

		if(min > max)
			return new WitnessBoolean(false);

		return new WitnessBet(min, max);
	}
}
