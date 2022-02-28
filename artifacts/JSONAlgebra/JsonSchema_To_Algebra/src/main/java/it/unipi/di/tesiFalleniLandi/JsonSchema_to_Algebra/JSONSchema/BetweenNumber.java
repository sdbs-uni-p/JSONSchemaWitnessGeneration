package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.AllOf_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Bet_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.XBet_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions.SyntaxErrorRuntimeException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class BetweenNumber implements JSONSchemaElement{
	private Number maximum;
	private Number minimum;
	private Number exclusiveMaximum;
	private Number exclusiveMinimum;
	private Boolean booleanExclusiveMaximum;
	private Boolean booleanExclusiveMinimum;

	private static Logger logger = LogManager.getLogger(BetweenNumber.class);
	
	public BetweenNumber() {
		logger.trace("Creating an empty BetweenNumber");
	}
	
	public void setMax(JsonElement obj) {
		if(!obj.isJsonPrimitive() || !obj.getAsJsonPrimitive().isNumber())
			throw new SyntaxErrorRuntimeException("expected number as value of maximum got " + obj);

		logger.trace("Setting max by parsing {}", obj);

		if(booleanExclusiveMaximum != null && booleanExclusiveMaximum)
			this.exclusiveMaximum = obj.getAsNumber();
		else
			this.maximum = obj.getAsNumber();
	}
	
	public void setMin(JsonElement obj) {
		if(!obj.isJsonPrimitive() || !obj.getAsJsonPrimitive().isNumber())
			throw new SyntaxErrorRuntimeException("expected number as value of minimum got "+ obj);

		logger.trace("Setting min by parsing {}", obj);

		if(booleanExclusiveMinimum != null && booleanExclusiveMinimum)
			this.exclusiveMinimum = obj.getAsNumber();
		else
			this.minimum = obj.getAsNumber();
	}
	
	
	public void setExclusiveMax(JsonElement obj) {
		if(!obj.isJsonPrimitive() || (!obj.getAsJsonPrimitive().isBoolean() && !obj.getAsJsonPrimitive().isNumber()))
			throw new SyntaxErrorRuntimeException("expected number or boolean as value of exclusiveMaximum got "+obj);

		logger.trace("Setting ExclusiveMax by parsing {}", obj);
		JsonPrimitive jsonPrimitive = obj.getAsJsonPrimitive();
		if(jsonPrimitive.isBoolean()) {
			booleanExclusiveMaximum = jsonPrimitive.getAsBoolean();
			if(booleanExclusiveMaximum != null && booleanExclusiveMaximum && maximum != null) {
				exclusiveMaximum = maximum;
				maximum = null;
			}
		}else
			this.exclusiveMaximum = obj.getAsNumber();

	}
	
	public void setExclusiveMin(JsonElement obj) {
		if(!obj.isJsonPrimitive() || (!obj.getAsJsonPrimitive().isBoolean() && !obj.getAsJsonPrimitive().isNumber()))
			throw new SyntaxErrorRuntimeException("expected number or boolean as value of exclusiveMinimum got "+obj);

		logger.trace("Setting ExclusiveMin by parsing {}", obj);

		JsonPrimitive jsonPrimitive = obj.getAsJsonPrimitive();
		if(jsonPrimitive.isBoolean()) {
			booleanExclusiveMinimum = jsonPrimitive.getAsBoolean();
			if(booleanExclusiveMinimum != null && booleanExclusiveMinimum && minimum != null) {
				exclusiveMinimum = minimum;
				minimum = null;
			}
		}else
			this.exclusiveMinimum = obj.getAsNumber();
	}

	@Override
	public String toString() {
		return "BetweenNumber [maximum=" + maximum + "\\r\\n minimum=" + minimum + "\\r\\n exclusiveMaximum="
				+ exclusiveMaximum + "\\r\\n exclusiveMinimum=" + exclusiveMinimum + "\\r\\n booleanExclusiveMaximum="
				+ booleanExclusiveMaximum + "\\r\\n booleanExclusiveMinimum=" + booleanExclusiveMinimum + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonElement toJSON() {
		JsonObject obj = new JsonObject();
		
		if(maximum != null) obj.addProperty("maximum", maximum);
		if(minimum != null) obj.addProperty("minimum", minimum);
		
		if(exclusiveMaximum != null) obj.addProperty("exclusiveMaximum", exclusiveMaximum);
		if(exclusiveMinimum != null) obj.addProperty("exclusiveMinimum", exclusiveMinimum);
		
		if(booleanExclusiveMaximum != null) obj.addProperty("exclusiveMaximum", booleanExclusiveMaximum);
		if(booleanExclusiveMinimum != null) obj.addProperty("exclusiveMinimum", booleanExclusiveMinimum);
		
		return obj;
	}

	@Override
	public Assertion toGrammar() {
		AllOf_Assertion allOf = new AllOf_Assertion();
		Bet_Assertion bet = new Bet_Assertion();
		XBet_Assertion xbet = new XBet_Assertion();
		allOf.add(bet);
		allOf.add(xbet);

		if(minimum != null) {
			bet.setMin(minimum);
		}
		if(maximum != null){
			bet.setMax(maximum);
		}

		if(exclusiveMinimum != null) {
			xbet.setMin(exclusiveMinimum);
		}
		if(exclusiveMaximum != null) {
			xbet.setMax(exclusiveMaximum);
		}

		if(exclusiveMinimum == null && exclusiveMaximum == null)
			return bet;
		else if(minimum == null && maximum == null)
			return xbet;

		return allOf;
	}

	@Override
	public BetweenNumber assertionSeparation() {
		BetweenNumber obj = new BetweenNumber();
		
		if(maximum != null) obj.maximum = maximum;
		if(minimum != null) obj.minimum = minimum;
		
		if(exclusiveMaximum != null) obj.exclusiveMaximum = exclusiveMaximum;
		if(exclusiveMinimum != null) obj.exclusiveMinimum = exclusiveMinimum;
		
		if(booleanExclusiveMaximum != null) obj.booleanExclusiveMaximum = booleanExclusiveMaximum;
		if(booleanExclusiveMinimum != null) obj.booleanExclusiveMinimum = booleanExclusiveMinimum;
		
		return obj;
	}

	@Override
	public List<URI_JS> getRef() {
		return new LinkedList<>();
	}

	@Override
	public JSONSchema searchDef(Iterator<String> URIIterator) {
		logger.debug("searchDef: End node --> returning null");
		return null;
	}

	@Override
	public List<Entry<String,Defs>> collectDef() {
		return new LinkedList<>();
	}

	@Override
	public int numberOfTranslatableAssertions() {
		return 1;
	}
	
	public BetweenNumber clone() {
		BetweenNumber clone = new BetweenNumber();
		
		clone.maximum = maximum;
		clone.minimum = minimum;
		clone.exclusiveMaximum = exclusiveMaximum;
		clone.exclusiveMinimum = exclusiveMinimum;
		clone.booleanExclusiveMaximum = booleanExclusiveMaximum;
		clone.booleanExclusiveMinimum = booleanExclusiveMinimum;
		
		return clone;
	}
}