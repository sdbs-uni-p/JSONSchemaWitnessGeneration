package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.Map;
import java.util.Set;

//TODO: controlla cosa succede quando creiamo un pattern da una stringa che in verità è un pattern (se viene interpretata come stringa statica [corretto] o come pattern [sbagliato])
public class Const_Assertion implements Assertion{
	private JsonElement value;

	private static Logger logger = LogManager.getLogger(Const_Assertion.class);

	public Const_Assertion(JsonElement value) {
		this.value = value;
		logger.trace("Created a Const_Assertion with value {}", value);
	}

	@Override
	public String toString() {
		return "Const_Assertion [" + value + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonElement toJSONSchema(WitnessVarManager rootVar) {
		JsonObject obj = new JsonObject();

		if(value.isJsonNull()) {
			obj.add("const", JsonNull.INSTANCE);
			return obj;
		}

		if(value.isJsonObject())
			obj.add("const", value);
		else if(value.isJsonArray())
			obj.add("const", value);
		else if(value.getAsJsonPrimitive().isString())
			obj.addProperty("const", value.getAsString());
		else if(value.getAsJsonPrimitive().isNumber())
			obj.addProperty("const", value.getAsNumber());
		else if(value.getAsJsonPrimitive().isBoolean())
			obj.addProperty("const", value.getAsBoolean());

		return obj;
	}

	@Override
	public Assertion not() {
		Type_Assertion type = new Type_Assertion();
		
		if(value.isJsonNull()) {
			logger.trace("Applying not elimination to {} as null value", value);
			type.add(AlgebraStrings.TYPE_NULL);
			return type.not();
		}

		if(value.isJsonObject()) {
			logger.trace("Applying not elimination to {} as JsonObject", value);
			JsonObject object = value.getAsJsonObject();
			AllOf_Assertion and = new AllOf_Assertion();
			Properties_Assertion properties = new Properties_Assertion();
			Long size = (long) object.size();
			Pro_Assertion pro = new Pro_Assertion(size, size);
			Required_Assertion req = new Required_Assertion();

			@SuppressWarnings("unchecked")
			Set<Map.Entry<String, JsonElement>> keys = object.entrySet();

			for (Map.Entry<String, JsonElement> entry : keys) {
				try {
					properties.addProperties(entry.getKey(), new Const_Assertion(entry.getValue()));
				} catch (REException e) {
					logger.catching(e);
					throw new RuntimeException(e);
				}
				req.add(entry.getKey());
			}

			type.add("obj");
			and.add(properties);
			and.add(type);
			and.add(req);
			and.add(pro);

			return and.not();
		}

		//array
		if(value.isJsonArray()) {
			logger.trace("Applying not elimination to {} as JsonArray", value);
			JsonArray array = value.getAsJsonArray();
			Items_Assertion items = new Items_Assertion();
			type.add("arr");
			AnyOf_Assertion or = new AnyOf_Assertion();
			Exist_Assertion betItems = new Exist_Assertion((long) array.size(), (long) array.size(), new Boolean_Assertion(true));

			for (JsonElement element : array)
				items.add(new Const_Assertion(element));

			or.add(betItems.not());
			or.add(items.not());
			or.add(type.not());

			return or;
		}

		if(value.getAsJsonPrimitive().isBoolean()) {
			logger.trace("Applying not elimination to {} as Boolean", value);
			AnyOf_Assertion or = new AnyOf_Assertion();
			type.add("bool");
			or.add(type.not());
			or.add(new IfBoolThen_Assertion(value.getAsBoolean()).not());

			return or;
		}
		
		if(value.getAsJsonPrimitive().isNumber()) {
			logger.trace("Applying not elimination to {} as Number", value);
			AnyOf_Assertion or = new AnyOf_Assertion();
			Bet_Assertion bet = new Bet_Assertion(value.getAsNumber(), value.getAsNumber());
			type.add("num");
			or.add(type.not());
			or.add(bet.not());
			
			return or;
		}

		//String
		logger.trace("Applying not elimination to {} as String", value);
		AnyOf_Assertion or = new AnyOf_Assertion();
		Pattern_Assertion pattern = new Pattern_Assertion(ComplexPattern.createFromName(value.getAsString())); //TODO: getAsString or toString?

		type.add("str");
		or.add(type.not());
		or.add(pattern.not());

		return or;
	}

	@Override
	public Assertion notElimination() {
		return new Const_Assertion(value);
	}

	
	@Override
	public String toGrammarString() {
		if(value.isJsonNull()) return AlgebraStrings.CONST("null");

		if(value.isJsonObject() || value.isJsonArray())
			return AlgebraStrings.CONST(value.toString());

		if(value.getAsJsonPrimitive().isString())
			return AlgebraStrings.CONST("\"" + value.getAsString() + "\""  );

		return AlgebraStrings.CONST(value.getAsString());
	}

	@Override
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		if (value.isJsonNull()) return new WitnessType(AlgebraStrings.TYPE_NULL);

		if (value.isJsonObject()) {
			logger.trace("Translating {} to WitnessAlgebra as JsonObject", value);
			WitnessAnd and = new WitnessAnd();
			and.add(new WitnessType(AlgebraStrings.TYPE_OBJECT));
			Set<Map.Entry<String, JsonElement>> entrySet = ((JsonObject) value).entrySet();
			Required_Assertion req = new Required_Assertion();

			for (Map.Entry<String, JsonElement> entry : entrySet) {
				req.add(entry.getKey());
				and.add(new WitnessProperty(ComplexPattern.createFromName(entry.getKey()), new Const_Assertion(entry.getValue()).toWitnessAlgebra(varManager,env, pattReqManager)));
			}
			and.add(req.toWitnessAlgebra(varManager,env, pattReqManager));
			and.add(new WitnessPro(Double.parseDouble("" + entrySet.size()), Double.parseDouble("" + entrySet.size())));
			return and;
		}

		if (value.isJsonArray()) {
			logger.trace("Translating {} to WitnessAlgebra as JsonArray", value);
			WitnessAnd and = new WitnessAnd();
			and.add(new WitnessType(AlgebraStrings.TYPE_ARRAY));
			WitnessItems items = new WitnessItems();
			JsonArray array = value.getAsJsonArray();
			for (JsonElement element : array)
				items.addItems(new Const_Assertion(element).toWitnessAlgebra(varManager,env, pattReqManager));

			and.add(new WitnessContains(Long.parseLong("" + array.size()), Long.parseLong("" + array.size()), new WitnessBoolean(true)));

			if(array.size()!=0)
				and.add(items);

			return and;
		}

		if (value.getAsJsonPrimitive().isString()) {
			logger.trace("Translating {} to WitnessAlgebra as String", value);
			WitnessAnd and = new WitnessAnd();
			and.add(new WitnessType(AlgebraStrings.TYPE_STRING));
			and.add(new WitnessPattern(ComplexPattern.createFromName(value.getAsString())));
			return and;
		}

		if (value.getAsJsonPrimitive().isBoolean()) {
			logger.trace("Translating {} to WitnessAlgebra as Boolean", value);
			WitnessAnd and = new WitnessAnd();
			and.add(new WitnessType(AlgebraStrings.TYPE_BOOLEAN));
			and.add(new WitnessIfBoolThen(value.getAsBoolean()));
			return and;
		}

		logger.trace("Translating {} to WitnessAlgebra as Number", value);
		WitnessAnd and = new WitnessAnd();
		and.add(new WitnessType(AlgebraStrings.TYPE_NUMBER));
		and.add(new WitnessBet(Double.parseDouble(value.toString()), Double.parseDouble(value.toString())));

		return and;

	}

	public Object getValue(){
		return value;
	}

	public JsonElement getJsonElementValue(){
		return value;
	}
}
