package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

public class XBet_Assertion implements Assertion{
	private Number min, max;

	private static Logger logger = LogManager.getLogger(XBet_Assertion.class);

	public XBet_Assertion() {
		logger.trace("Creating an empty XBet_Assertion");
	}
	
	public XBet_Assertion(Number min, Number max) {
		this.min = min;
		this.max = max;
		logger.trace("Created a new XBet_Assertion: {}", this);
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
		return "XBet_Assertion [min=" + min + ", max=" + max + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonObject toJSONSchema(WitnessVarManager rootVar) {
		JsonObject obj = new JsonObject();

		if(min != null) obj.addProperty("exclusiveMinimum", min);
		if(max != null) obj.addProperty("exclusiveMaximum", max);

		return obj;
	}
	
	@Override
	public Assertion not() {
		AllOf_Assertion and = new AllOf_Assertion();
		Type_Assertion type = new Type_Assertion();
		type.add("num");
		and.add(type);
		
		if(min != null && max != null) {
			AnyOf_Assertion or = new AnyOf_Assertion();
			or.add(new Bet_Assertion(null, min));
			or.add(new Bet_Assertion(max, null));
			and.add(or);
			return and;
		}
		
		if(min != null) {
			and.add(new Bet_Assertion(null, min));
			return and;
		}
		
		
		and.add(new Bet_Assertion(max, null));
		return and;
	}

	@Override
	public Assertion notElimination() {
		return new XBet_Assertion(min, max);
	}

	@Override
	public String toGrammarString() {
		String min = AlgebraStrings.NEG_INF, max = AlgebraStrings.POS_INF;

		if(this.min != null) min = this.min+"";
		if(this.max != null) max = this.max+"";

		return AlgebraStrings.BETWEENNUMBER_EXCLUSIVE(min, max);
	}

	@Override
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		Double min = null, max = null;

		if(this.min != null)
			min = Double.parseDouble(this.min.toString());
		else
			min = Double.NEGATIVE_INFINITY;

		if(this.max != null)
			max = Double.parseDouble(this.max.toString());
		else
			max = Double.POSITIVE_INFINITY;

		if(min > max || min.doubleValue()==max.doubleValue())
			return new WitnessBoolean(false);

		return new WitnessXBet(min, max);
	}

}
