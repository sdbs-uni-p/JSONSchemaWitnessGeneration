package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Type_Assertion implements Assertion{
	private List<String> types;

	private static Logger logger = LogManager.getLogger(Type_Assertion.class);
	//private boolean _treatIntegerAsNumber = true; //TODO part of the extension to accommodate the integer type

	public Type_Assertion() {
		logger.trace("Creating an empty Type_Assertion");
		types = new LinkedList<>();
	}

	public Type_Assertion(String type) {
		this();
		types.add(type);
		logger.trace("Created a new Type_Assertion: {}", this);
	}
	
	public void add(String toAdd) {
		logger.trace("Adding {} to {}", toAdd, this);
		types.add(toAdd);
	}

	public boolean isEmpty(){
		return types.isEmpty();
	}

	@Override
	public String toString() {
		return "Type_Assertion [" + types + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonElement toJSONSchema(WitnessVarManager rootVar) {
		JsonObject obj = new JsonObject();

		/*
		if(types.contains(AlgebraStrings.TYPE_NUMNOTINT)){
			AnyOf_Assertion or = new AnyOf_Assertion();
			if(types.size() == 1){
				AllOf_Assertion allOf = new AllOf_Assertion();
				allOf.add(new Type_Assertion(AlgebraStrings.TYPE_NUMBER));
				allOf.add(new NotMof_Assertion(1));
				return allOf.toJSONSchema(rootVar);
			}

			Type_Assertion type = new Type_Assertion();
			AllOf_Assertion allOf = new AllOf_Assertion();
			allOf.add(new Type_Assertion(AlgebraStrings.TYPE_NUMBER));
			allOf.add(new NotMof_Assertion(1));
			or.add(allOf);
			or.add(type);
			for(String str : types)
				if(!str.equals(AlgebraStrings.TYPE_NUMNOTINT))
					type.add(str);

			return or.toJSONSchema(rootVar);
		}

		 */

		if(types.size() == 1){
			obj.addProperty("type", toJsonTypeName(types.get(0)));
			return obj;
		}

		JsonArray array = new JsonArray();
		for(String str : types)
			array.add(toJsonTypeName(str));

		obj.add("type", array);

		return obj;
	}

	@Override
	public Assertion not() {
		// add all types
		Type_Assertion notType = new Type_Assertion();
		notType.add(AlgebraStrings.TYPE_STRING);
		notType.add(AlgebraStrings.TYPE_OBJECT);
		notType.add(AlgebraStrings.TYPE_NUMBER);
		notType.add(AlgebraStrings.TYPE_ARRAY);
		notType.add(AlgebraStrings.TYPE_BOOLEAN);
		notType.add(AlgebraStrings.TYPE_NULL);

		// remove the type contained in this
		for(String type : types) {
			notType.types.remove(type);
		}

		/*
		if(types.contains(AlgebraStrings.TYPE_INTEGER) && !types.contains(AlgebraStrings.TYPE_NUMNOTINT)){
			notType.types.remove(AlgebraStrings.TYPE_NUMBER);
			notType.types.add(AlgebraStrings.TYPE_NUMNOTINT);
		}
		if(types.contains(AlgebraStrings.TYPE_NUMNOTINT) && !types.contains(AlgebraStrings.TYPE_INTEGER)) {
			notType.types.remove(AlgebraStrings.TYPE_NUMBER);
			notType.types.add(AlgebraStrings.TYPE_INTEGER);
		}

		if(types.contains(AlgebraStrings.TYPE_NUMBER)) {
			notType.types.remove(AlgebraStrings.TYPE_NUMNOTINT);
			notType.types.remove(AlgebraStrings.TYPE_INTEGER);
		}

		if(notType.types.contains(AlgebraStrings.TYPE_NUMBER)) {
			notType.types.remove(AlgebraStrings.TYPE_INTEGER);
		}

		if(types.contains(AlgebraStrings.TYPE_NUMNOTINT) && types.contains(AlgebraStrings.TYPE_INTEGER))
			notType.types.remove(AlgebraStrings.TYPE_NUMBER);
		 */

		// not of a type with all possibile types
		if(notType.types.isEmpty()) {
			AllOf_Assertion a =new AllOf_Assertion();
			a.add(new Boolean_Assertion(false));
			return a;
		}

		return notType;
	}

	@Override
	public Assertion notElimination() {
		Type_Assertion t = new Type_Assertion();
		t.types.addAll(types);
		
		return t;
	}

	@Override
	public String toGrammarString() {
		StringBuilder str = new StringBuilder();
		
		Iterator <String> it = types.iterator();
		
		while(it.hasNext()) {
			str.append(AlgebraStrings.FLAT_COMMA)
					.append(it.next());
		}
		
		return AlgebraStrings.TYPE(str.substring(AlgebraStrings.FLAT_COMMA.length()));
	}

	@Override
	public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		/*
		if(types.size() == 1) {
			if(_treatIntegerAsNumber){
				if(types.get(0).equals(AlgebraStrings.TYPE_INTEGER)){
					WitnessAnd and = new WitnessAnd();
					and.add(new WitnessMof(1.0));
					and.add(new WitnessType(AlgebraStrings.TYPE_NUMBER));

					return and;
				}
			}
			return new WitnessType(types.get(0));
		}

		if(_treatIntegerAsNumber){
			if(types.contains(AlgebraStrings.TYPE_INTEGER)){
				types.remove(AlgebraStrings.TYPE_INTEGER);
				WitnessAnd and = new WitnessAnd();
				and.add(new WitnessMof(1.0));
				and.add(new WitnessType(AlgebraStrings.TYPE_NUMBER));

				WitnessOr or = new WitnessOr();
				or.add(new WitnessType(types));
				or.add(and);

				return or;
			}
		}
		 */

		if(types.size() == 1) {
			return new WitnessType(types.get(0));
		}

		return new WitnessType(types);
	}

	private String toJsonTypeName(String type) {
		switch(type) {
		case "arr": return "array";
		case "int": return "integer";
		case "num": return "number";
		case "str": return "string";
		case "obj": return "object";
		case "bool": return "boolean";
		case "null": return "null";
		}
		return null;
	}

	public boolean contains(String type){
		return types.contains(type);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Type_Assertion type1 = (Type_Assertion) o;

		LinkedList<String> list = new LinkedList();

		if(this.types.size() >= type1.types.size()) {
			list.addAll(this.types);
			list.removeAll(type1.types);
		}else{
			list.addAll(type1.types);
			list.removeAll(this.types);
		}

		return list.size() == 0;
	}

}
  