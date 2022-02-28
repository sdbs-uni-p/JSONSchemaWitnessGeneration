package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessAssertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessContains;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessPattReqManager;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

public class Exist_Assertion implements Assertion{
	private Long min;
	private Long max;
	private Long position;
	private Assertion contains;

	private static Logger logger = LogManager.getLogger(Exist_Assertion.class);

	public Exist_Assertion(Long min, Long max, Assertion schema) {
		this.min = min;
		this.max = max;
		if(schema == null) this.contains = new Boolean_Assertion(true);
		else this.contains = schema;

		logger.trace("Created a new Exist_Assertion: {}", this);
	}

	public Exist_Assertion(Long min, Long max, Long pos, Assertion schema) {
		this.min = min;
		this.max = max;
		this.position = pos;
		if(schema == null) this.contains = new Boolean_Assertion(true);
		else this.contains = schema;

		logger.trace("Created a new Exist_Assertion: {}", this);
	}

	public void setMin(Long min) {
		logger.trace("Setting {} as min in {}", min, this);
		this.min = min;
	}
	
	public void setMax(Long max) {
		logger.trace("Setting {} as max in {}", max, this);
		this.max = max;
	}

	public Long getPosition() {
		return position;
	}

	public void setPosition(Long position) {
		logger.trace("Setting {} as position in {}", position, this);
		this.position = position;
	}

	public void setContains(Assertion schema) {
		logger.trace("Setting {} as contains in {}", schema, this);
		this.contains = schema;
	}

	@Override
	public String toString() {
		return "Exist_Assertion{" +
				"min=" + min +
				", max=" + max +
				", position=" + position +
				", contains=" + contains +
				'}';
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonObject toJSONSchema(WitnessVarManager rootVar) {
		JsonObject obj = new JsonObject();

		if(position != null){
			throw new UnsupportedOperationException("TODO: ContAfter()");
		}

		if(contains.getClass() == Boolean_Assertion.class && ((Boolean_Assertion) contains).getValue()) {

			if (min != null) obj.addProperty("minItems", min);
			if (max != null) obj.addProperty("maxItems", max);

			return obj;
		}

		if(contains != null)
			obj.add("contains", contains.toJSONSchema(rootVar));
		
		if(min != null) obj.addProperty("minContains", min);
		if(max != null) obj.addProperty("maxContains", max);
		
		return obj;
	}
	
	@Override
	public Assertion not() {
		AllOf_Assertion and = new AllOf_Assertion();

		//ContAfter negation -- in this case we must have min==1 and max==Inf
		if(position != null){
			and.add(new Type_Assertion(AlgebraStrings.TYPE_ARRAY));
			Items_Assertion item = new Items_Assertion();
			for(int i=0; i<position; i++){
				item.add(new Boolean_Assertion(true));
			}
			item.setAdditionalItems(contains.not());
			and.add(item);

			return and;
		}

		if((min == null || min == 0) && max == null){
			and.add(new Boolean_Assertion(false));
			return and;
		}

		Type_Assertion type = new Type_Assertion();
		type.add("arr");
		and.add(type);
		
		if(min != null && max != null) {
			//added a case for contains(S) TODO check
			if(min==1 && max == Long.MAX_VALUE)
			{
				Items_Assertion items = new Items_Assertion();
				items.setAdditionalItems(contains.not());
				and.add(items);
			}
			else { //general case of min and max
				AnyOf_Assertion or = new AnyOf_Assertion();
				if(min > 0)
					or.add(new Exist_Assertion(0L, min - 1, contains));
				if(max != Long.MAX_VALUE)
					or.add(new Exist_Assertion(max + 1, null, contains));
				and.add(or);
			}

			return and;
		}
		
		if(min != null && min > 0) {
			and.add(new Exist_Assertion(0L, min - 1, contains));
			return and;
		}

		if(max != null) {
			and.add(new Exist_Assertion(max + 1, null, contains));
		}

		return and;
	}

	@Override
	public Assertion notElimination() {
		Assertion tmp = contains.notElimination();
		return new Exist_Assertion(min, max, position, tmp);
	}

	@Override
	public String toGrammarString() {

		if(position != null){
			return AlgebraStrings.CONTAFTER(position + "", contains.toGrammarString());
		}

		String min = "0";
		String max = AlgebraStrings.POS_INF;

		if (this.min != null) min = this.min + "";
		if (this.max != null) max = this.max + "";

		return AlgebraStrings.CONTAINS(min, max, contains.toGrammarString());
	}

	@Override
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		return new WitnessContains(min, max, position, contains.toWitnessAlgebra(varManager, env, pattReqManager));
	}
}
