package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.ANTLR4;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class AntlrArray implements AntlrValue{
	private JsonArray list;
	
	public AntlrArray() {
		list = new JsonArray();
	}
	
	public void add(AntlrValue value) {

		if(value == null) {
			list.add(JsonNull.INSTANCE);
		}
		if(value.getClass() == AntlrString.class)
			list.add((String) value.getValue());
		else if(value.getClass() == AntlrLong.class || value.getClass() == AntlrDouble.class)
			list.add((Number) value.getValue());
		else if(value.getClass() == AntlrBoolean.class)
			list.add((Boolean) value.getValue());
		else if(value.getClass() == AntlrJsonObject.class)
			list.add((JsonObject) value.getValue());
		else if(value.getClass() == AntlrArray.class)
			list.add((JsonArray) value.getValue());
	}

	@Override
	public JsonArray getValue() {
		return list;
	}
}
