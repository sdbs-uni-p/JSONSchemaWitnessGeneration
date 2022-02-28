package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessAssertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessOr;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessPattReqManager;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

public class Enum_Assertion implements Assertion{

	private JsonArray _enum;

	private static Logger logger = LogManager.getLogger(Enum_Assertion.class);
	
	public Enum_Assertion(JsonArray _enum) {
		logger.trace("Creating an Enum_Assertion: {}", _enum);
		this._enum = _enum;
	}

	public Enum_Assertion() {
		logger.trace("Creating an empty Enum_Assertion");
		_enum = new JsonArray();
	}

	public JsonArray getEnumList() {
		return this._enum;
	}

	public void add(JsonElement obj) {
		logger.trace("Adding {} to ", obj, this);
		_enum.add(obj);
	}

	@Override
	public String toString() {
		return "Enum_Assertion [" + _enum + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonElement toJSONSchema(WitnessVarManager rootVar) {
		JsonObject obj = new JsonObject();
		JsonArray array = new JsonArray();

		for(JsonElement element : _enum) {
			if(element.isJsonNull())
				array.add(JsonNull.INSTANCE);

			else if(element.isJsonObject())
				array.add(element.getAsJsonObject());

			else if(element.isJsonArray())
				array.add(element.getAsJsonArray());

			else if(element.getAsJsonPrimitive().isString())
				array.add(element.getAsString());

			else if(element.getAsJsonPrimitive().isNumber())
				array.add(element.getAsNumber());


			else
				array.add(element.getAsBoolean());
		}

		obj.add("enum", array);
		return obj;
	}

	@Override
	public Assertion not() {
		AllOf_Assertion notEnum = new AllOf_Assertion();
		
		for(JsonElement element : _enum)
			notEnum.add((new Const_Assertion(element)).not());
		
		return notEnum;
	}

	@Override
	public Assertion notElimination() {
		return new Enum_Assertion(_enum);
	}

	@Override
	public String toGrammarString() {
		if(_enum.size() == 0)
			return "";

		String tmp = _enum.toString();
		tmp = tmp.substring(1, tmp.length()-1);
		return AlgebraStrings.ENUM(tmp);
	}

	@Override
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		WitnessOr or = new WitnessOr();
		for(JsonElement element : _enum)
			or.add(new Const_Assertion(element).toWitnessAlgebra(varManager, env, pattReqManager));


		return or;
	}
	
}
