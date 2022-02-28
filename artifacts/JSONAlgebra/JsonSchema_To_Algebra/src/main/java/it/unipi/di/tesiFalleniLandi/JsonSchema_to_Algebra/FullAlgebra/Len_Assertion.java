package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

public class Len_Assertion implements Assertion{
	private Long min, max;

	private static Logger logger = LogManager.getLogger(Len_Assertion.class);
	
	public Len_Assertion(Long min, Long max) {
		this.min = min;
		this.max = max;

		logger.trace("Created a new Len_Assertion: {}", this);
	}

	public void setMin(Long min) {
		logger.trace("Setting {} as min in {}", min, this);
		this.min = min;
	}
	
	public void setMax(Long max) {
		logger.trace("Setting {} as max in {}", max, this);
		this.max = max;
	}

	@Override
	public String toString() {
		return "Len_Assertion [min=" + min + ", max=" + max + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonObject toJSONSchema(WitnessVarManager rootVar) {
		JsonObject obj = new JsonObject();
		
		if(min != null)	obj.addProperty("minLength", min);
		if(max != null)	obj.addProperty("maxLength", max);
		
		return obj;
	}
	
	@Override
	public Assertion not() {
		AllOf_Assertion and = new AllOf_Assertion();
		if((min == null || min == 0) && max == null) {
			and.add(new Boolean_Assertion(false));
			return and;
		}

		Type_Assertion type = new Type_Assertion();
		type.add(AlgebraStrings.TYPE_STRING);
		and.add(type);

		if(min != null && max != null) {
			AnyOf_Assertion or = new AnyOf_Assertion();
			if(min != 0)
				or.add(new Len_Assertion(null, min-1));

			or.add(new Len_Assertion(max+1, null));
			and.add(or);
			return and;
		}
		
		if(min != null) {
			and.add(new Len_Assertion(null, min-1));
			return and;
		}
		
		and.add(new Len_Assertion(max+1, null));
		return and;
	}

	@Override
	public Assertion notElimination() {
		return new Len_Assertion(min, max);
	}

	@Override
	public String toGrammarString() {
		String min = "0", max = AlgebraStrings.POS_INF;

		if(this.min != null) min = this.min+"";
		if(this.max != null) max = this.max+"";

		return AlgebraStrings.LENGTH(min, max);
	}

	public ComplexPattern toPattern() {
		try {
			String minStr = "0", maxStr = "";
			if (min != null) minStr = min.toString();
			if (max != null) maxStr = max.toString();
			return ComplexPattern.createFromRegexp("^.{" + minStr + "," + maxStr + "}$");
		} catch (REException e) {
			System.out.println("Exception in toPattern");
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		if (max!=null && min!=null && min>max)
		   return new WitnessBoolean(false);
		else
		   return new WitnessPattern(this.toPattern());
	}
}
