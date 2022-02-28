package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

public class Pro_Assertion implements Assertion{
	private Long min, max;

	private static Logger logger = LogManager.getLogger(Pro_Assertion.class);
	
	public Pro_Assertion(Long min, Long max) {
		this.min = min;
		this.max = max;
		logger.trace("Created a new Pro_Assertion: {}", this);
	}

	@Override
	public String toString() {
		return "Pro_Assertion [minProperties=" + min + ", maxProperties=" + max + "]";
	}


	@SuppressWarnings("unchecked")
	@Override
	public JsonObject toJSONSchema(WitnessVarManager rootVar) {
		JsonObject obj = new JsonObject();
		
		if(min != null)	obj.addProperty("minProperties", min);
		if(max != null)	obj.addProperty("maxProperties", max);
		
		return obj;
	}

	@Override
	public Assertion not() {
		AllOf_Assertion and = new AllOf_Assertion();
		//type[obj]
		Type_Assertion type = new Type_Assertion();
		type.add(AlgebraStrings.TYPE_OBJECT);
		and.add(type);
		AnyOf_Assertion or = new AnyOf_Assertion();

		if(min!=null)
			if(max!=null)
			{
				or.add(new Pro_Assertion(null,min-1));
				or.add(new Pro_Assertion(max+1,null));
			}
			else //max = null
			{
				or.add(new Pro_Assertion(null,min-1));
			}
		else //min = null
		{
			or.add(new Pro_Assertion(max+1,null));
		}

		and.add(or);
		return and;
	}


/*
* Old version
		public Assertion not() {
		AllOf_Assertion and = new AllOf_Assertion();
		if((min == null || min == 0) && max == null) {
			and.add(new Boolean_Assertion(false));
			return and;
		}

		Type_Assertion type = new Type_Assertion();
		type.add(AlgebraStrings.TYPE_OBJECT);
		and.add(type);

		if(min != null && max != null) {
			AnyOf_Assertion or = new AnyOf_Assertion();
			if(min != 0)
				or.add(new Pro_Assertion(null, min-1));

			or.add(new Pro_Assertion(max+1, null));
			and.add(or);
			return and;
		}

		if(min != null)
			return new Pro_Assertion(null, min-1);


		return new Pro_Assertion(max+1, null);
	}
*/
	@Override
	public Assertion notElimination() {
		return new Pro_Assertion(min, max);
	}

	@Override
	public String toGrammarString() {
		String min = "0", max = AlgebraStrings.POS_INF;

		if(this.min != null) min = this.min+"";
		if(this.max != null) max = this.max+"";

		return AlgebraStrings.BETWEENPROPERTIES(min, max);
	}

	@Override
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		Double min = null;
		Double max = null;

		if(this.min != null)
			min = Double.parseDouble(this.min.toString());
		else
			min = 0.0;

		if(this.max != null)
			max = Double.parseDouble(this.max.toString());
		else
			max = Double.POSITIVE_INFINITY;

		if(min > max)
			return new WitnessBoolean(false);

		return new WitnessPro(min, max);
	}
}
