package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.ANTLR4;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class AntlrJsonObject implements AntlrValue{
	private JsonObject object;
	
	public AntlrJsonObject() {
		object = new JsonObject();
	}
	
	@SuppressWarnings("unchecked")
	public void add(String key, Object value){
		if(object.has(key)) throw new ParseCancellationException("Detected an invalid JSON object (duplicated keys)");

		if(value == null)	value = new JsonObject();

		AntlrValue tmp = (AntlrValue) value;

		if(tmp == null) {
			this.object.add(key, JsonNull.INSTANCE);
		}

		if(value.getClass() == AntlrString.class)
			this.object.addProperty(key, (String) tmp.getValue());
		else if(value.getClass() == AntlrLong.class || value.getClass() == AntlrDouble.class)
			this.object.addProperty(key, (Number) tmp.getValue());
		else if(value.getClass() == AntlrBoolean.class)
			this.object.addProperty(key, (Boolean) tmp.getValue());
		else if(value.getClass() == AntlrJsonObject.class)
			this.object.add(key, (JsonObject) tmp.getValue());
		else if(value.getClass() == AntlrArray.class)
			this.object.add(key, (JsonArray) tmp.getValue());
	}

	@Override
	public Object getValue() {
		return object;
	}

}
