package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.ANTLR4;

import com.google.gson.*;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.ANTLR4.GrammaticaParser.AssertionContext;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.ANTLR4.GrammaticaParser.Json_valueContext;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.TerminalNode;
import de.uni_passau.sds.patterns.REException;

import java.util.List;

/**
 * Methods to parse the algebra. See ANTLR4 documentation
 */
public class AlgebraParser extends GrammaticaBaseVisitor<AlgebraParserElement>{

	@Override
	public Boolean_Assertion visitParseBooleanSchema(GrammaticaParser.ParseBooleanSchemaContext ctx) {
		switch(ctx.BOOLEAN().getText()){
			case "true":
			case "t":
			case "tt":
				return new Boolean_Assertion(true);
			default:
				return new Boolean_Assertion(false);
		}
	}

	@Override
	public AllOf_Assertion visitParseAssertionList(GrammaticaParser.ParseAssertionListContext ctx) {
		AllOf_Assertion list = new AllOf_Assertion();

		List<AssertionContext> keywords = ctx.assertion();

		for(AssertionContext key : keywords) {
			Assertion schema = (Assertion) visit(key);
			list.add(schema);
		}

		return list;
	}

	@Override
	public Bet_Assertion visitNewBetweenAssertion(GrammaticaParser.NewBetweenAssertionContext ctx) {

		return (Bet_Assertion) visit(ctx.between_assertion());
	}

	@Override
	public Bet_Assertion visitParseBetweenAssertion(GrammaticaParser.ParseBetweenAssertionContext ctx) {
		AntlrValue min = (AntlrValue) visit(ctx.json_value(0));
		AntlrValue max = (AntlrValue) visit(ctx.json_value(1));

		return new Bet_Assertion((Number) min.getValue(), (Number) max.getValue());
	}

	@Override
	public XBet_Assertion visitNewXBetweenAssertion(GrammaticaParser.NewXBetweenAssertionContext ctx) {

		return (XBet_Assertion) visit(ctx.xbetween_assertion());
	}

	@Override
	public XBet_Assertion visitParseXBetweenAssertion(GrammaticaParser.ParseXBetweenAssertionContext ctx) {
		AntlrValue min = (AntlrValue) visit(ctx.json_value(0));
		AntlrValue max = (AntlrValue) visit(ctx.json_value(1));

		return new XBet_Assertion((Number) min.getValue(), (Number) max.getValue());
	}


	@Override
	public AntlrLong visitIntValue(GrammaticaParser.IntValueContext ctx) {
		return new AntlrLong(Long.valueOf(ctx.INT().getText()));
	}

	@Override
	public AntlrDouble visitDoubleValue(GrammaticaParser.DoubleValueContext ctx) {
		return new AntlrDouble(Double.valueOf(ctx.DOUBLE().getText()));
	}

	@Override
	public AntlrLong visitNullValue(GrammaticaParser.NullValueContext ctx) {
		return new AntlrLong(null);
	}

	@Override
	public AntlrArray visitArrayValue(GrammaticaParser.ArrayValueContext ctx) {
		AntlrArray array = new AntlrArray();
		List<Json_valueContext> list = ctx.json_value();

		for(Json_valueContext el : list)
			array.add((AntlrValue) visit(el));

		return array;
	}

	@Override
	public AntlrJsonObject visitJsonObjectValue(GrammaticaParser.JsonObjectValueContext ctx) {
		AntlrJsonObject jsonObject = new AntlrJsonObject();

		List<Json_valueContext> list = ctx.json_value();
		List<TerminalNode> idList = ctx.STRING();

		for(int i = 0; i < list.size(); i++)
			jsonObject.add(idList.get(i).getText().subSequence(1, idList.get(i).getText().length()-1).toString(),  visit(list.get(i)));

		return jsonObject;
	}

	@Override
	public Type_Assertion visitNewTypeAssertion(GrammaticaParser.NewTypeAssertionContext ctx) {
		Type_Assertion type = new Type_Assertion();
		JsonArray typeList = ((AntlrArray)visit(ctx.type_assertion())).getValue();

		for(JsonElement s : typeList)
			type.add(s.getAsString());

		return type;
	}

	@Override
	public AntlrArray visitParseTypeAssertion(GrammaticaParser.ParseTypeAssertionContext ctx) {
		AntlrArray array = new AntlrArray();

		for(TerminalNode terminalNode : ctx.TYPE())
			array.add(new AntlrString(terminalNode.getText()));

		if(!ctx.NULL().isEmpty())
			array.add(new AntlrString("null"));

		return array;
	}

	@Override
	public Not_Assertion visitNewNot(GrammaticaParser.NewNotContext ctx) {
		return (Not_Assertion) visit(ctx.not_assertion());
	}

	@Override
	public Not_Assertion visitParseNot(GrammaticaParser.ParseNotContext ctx) {
		return new Not_Assertion((Assertion) visit(ctx.assertion()));
	}

	@Override
	public AllOf_Assertion visitNewAllOf(GrammaticaParser.NewAllOfContext ctx) {
		return (AllOf_Assertion) visit(ctx.all_of_assertion());
	}

	@Override
	public AllOf_Assertion visitParseAllOf(GrammaticaParser.ParseAllOfContext ctx) {
		List<AssertionContext> list = ctx.assertion();
		AllOf_Assertion and = new AllOf_Assertion();

		for(AssertionContext el : list)
			and.add((Assertion) visit(el));

		return and;
	}

	@Override
	public AnyOf_Assertion visitNewAnyOf(GrammaticaParser.NewAnyOfContext ctx) {
		return (AnyOf_Assertion) visit(ctx.any_of_assertion());
	}

	@Override
	public AnyOf_Assertion visitParseAnyOf(GrammaticaParser.ParseAnyOfContext ctx) {
		List<AssertionContext> list = ctx.assertion();
		AnyOf_Assertion or = new AnyOf_Assertion();

		for(AssertionContext el : list)
			or.add((Assertion) visit(el));

		return or;
	}

	@Override
	public OneOf_Assertion visitNewOneOf(GrammaticaParser.NewOneOfContext ctx) {
		return (OneOf_Assertion) visit(ctx.one_of_assertion());
	}

	@Override
	public OneOf_Assertion visitParseOneOf(GrammaticaParser.ParseOneOfContext ctx) {
		List<AssertionContext> list = ctx.assertion();
		OneOf_Assertion xor = new OneOf_Assertion();

		for(AssertionContext el : list)
			xor.add((Assertion) visit(el));

		return xor;
	}

	@Override
	public Required_Assertion visitNewRequired(GrammaticaParser.NewRequiredContext ctx) {
		return (Required_Assertion) visit(ctx.required_assertion());
	}

	@Override
	public Required_Assertion visitParseRequired(GrammaticaParser.ParseRequiredContext ctx) {
		Required_Assertion req = new Required_Assertion();

		List<TerminalNode> strList = ctx.STRING();

		for(TerminalNode str : strList) {
			req.add(str.getText().subSequence(1, str.getText().length()-1).toString());
		}

		return req;
	}

	@Override
	public IfThenElse_Assertion visitNewIfThenElse(GrammaticaParser.NewIfThenElseContext ctx) {
		return (IfThenElse_Assertion) visit(ctx.if_then_else_assertion());
	}

	@Override
	public IfThenElse_Assertion visitParseIfThenElse(GrammaticaParser.ParseIfThenElseContext ctx) {
		Assertion ifStat = (Assertion) visit(ctx.assertion(0));
		Assertion thenStat = (Assertion) visit(ctx.assertion(1));
		Assertion elseStat = (Assertion) visit(ctx.assertion(2));

		return  new IfThenElse_Assertion(ifStat, thenStat, elseStat);
	}

	@Override
	public IfThenElse_Assertion visitParseIfThen(GrammaticaParser.ParseIfThenContext ctx) {
		Assertion ifStat = (Assertion) visit(ctx.assertion(0));
		Assertion thenStat = (Assertion) visit(ctx.assertion(1));

		return new IfThenElse_Assertion(ifStat, thenStat, null);
	}

	@Override
	public Enum_Assertion visitNewEnum(GrammaticaParser.NewEnumContext ctx) {
		return (Enum_Assertion) visit(ctx.enum_assertion_assertion());
	}

	@Override
	public Enum_Assertion visitParseEnum(GrammaticaParser.ParseEnumContext ctx) {
		Enum_Assertion _enum = new Enum_Assertion();

		List<Json_valueContext> valueList = ctx.json_value();

		//trattare null con classe NullAntlr ?
		for(Json_valueContext value : valueList) {
			AntlrValue tmp = (AntlrValue) visit(value);
			JsonElement val = null;

			if(tmp.getValue() == null)
				val = JsonNull.INSTANCE;
			else if(tmp.getValue().getClass() == String.class)
				val = new JsonPrimitive((String) tmp.getValue());
			else if(tmp.getValue().getClass() == Long.class || tmp.getValue().getClass() == Double.class)
				val = new JsonPrimitive((Number) tmp.getValue());
			else if(tmp.getValue().getClass() == Boolean.class)
				val = new JsonPrimitive((Boolean) tmp.getValue());
			else if(tmp.getValue().getClass() == JsonArray.class)
				val = (JsonArray) tmp.getValue();
			else
				val = (JsonObject) tmp.getValue();


			_enum.add(val);
		}

		return _enum;
	}

	@Override
	public Mof_Assertion visitNewMultipleOf(GrammaticaParser.NewMultipleOfContext ctx) {
		return (Mof_Assertion) visit(ctx.multiple_of_assertion());
	}

	@Override
	public Mof_Assertion visitParseMultipleOf(GrammaticaParser.ParseMultipleOfContext ctx) {
		AntlrValue value = (AntlrValue) visit(ctx.json_value());

		try{
			if((long)value.getValue() <= 0)
				throw new ParseCancellationException("Unexpected negative value in Mof assertion");
		}catch(ClassCastException e){
			if((double)value.getValue() <= 0)
				throw new ParseCancellationException("Unexpected negative value in Mof assertion");
		}

		return new Mof_Assertion(value.getValue());
	}

	@Override
	public NotMof_Assertion visitNewNotMultipleOf(GrammaticaParser.NewNotMultipleOfContext ctx) {
		return (NotMof_Assertion) visit(ctx.not_multiple_of_assertion());
	}

	@Override
	public NotMof_Assertion visitParseNotMultipleOf(GrammaticaParser.ParseNotMultipleOfContext ctx) {
		AntlrValue value = (AntlrValue) visit(ctx.json_value());

		try{
			if((long)value.getValue() <= 0)
				throw new ParseCancellationException("Unexpected negative value in NotMof assertion");
		}catch(ClassCastException e){
			if((double)value.getValue() <= 0)
				throw new ParseCancellationException("Unexpected negative value in NotMof assertion");
		}

		return new NotMof_Assertion(value.getValue());
	}

	@Override
	public AntlrString visitStringValue(GrammaticaParser.StringValueContext ctx) {
		String str = ctx.STRING().getText();

		return new AntlrString(str.substring(1, str.length()-1));
	}

	@Override
	public AntlrBoolean visitBooleanValue(GrammaticaParser.BooleanValueContext ctx) {
		String value = ctx.BOOLEAN().getText();
		if(value.equals("t") || value.equals("tt") || value.equals("true"))
			return new AntlrBoolean(true);
		else if(value.equals("f") || value.equals("ff") || value.equals("false"))
			return new AntlrBoolean(false);
		else return null;
	}

	@Override
	public UniqueItems_Assertion visitNewUniqueItems(GrammaticaParser.NewUniqueItemsContext ctx) {
		return (UniqueItems_Assertion) visit(ctx.unique_items_assertion());
	}

	@Override
	public UniqueItems_Assertion visitParseUniqueItems(GrammaticaParser.ParseUniqueItemsContext ctx) {
		return new UniqueItems_Assertion();
	}

	@Override
	public RepeatedItems_Assertion visitNewRepeatedItems(GrammaticaParser.NewRepeatedItemsContext ctx) {
		return (RepeatedItems_Assertion) visit(ctx.repeated_items_assertion());
	}

	@Override
	public RepeatedItems_Assertion visitParseRepeatedItems(GrammaticaParser.ParseRepeatedItemsContext ctx) {
		return new RepeatedItems_Assertion();
	}

	@Override
	public Pattern_Assertion visitNewPattern(GrammaticaParser.NewPatternContext ctx) {
		return (Pattern_Assertion) visit(ctx.pattern_assertion());
	}

	@Override
	public Pattern_Assertion visitParsePattern(GrammaticaParser.ParsePatternContext ctx) {
		Pattern_Assertion pattern = null;

		try {
			pattern = new Pattern_Assertion((ComplexPattern) visit(ctx.pAssertion()));
		} catch (java.lang.IllegalArgumentException e) {
			throw new ParseCancellationException("REException: "+e.getMessage());
		}


		return pattern;
	}

	@Override
	public ComplexPattern visitNewComplexPatternString(GrammaticaParser.NewComplexPatternStringContext ctx) {
		try {
			String tmp = ctx.STRING().getText();
			return ComplexPattern.createFromRegexp(tmp.substring(1, tmp.length()-1));
		} catch (REException e) {
			throw new ParseCancellationException("REException: "+ e.toString());
		}
	}

	@Override
	public ComplexPattern visitNewpAllOf(GrammaticaParser.NewpAllOfContext ctx) {
		return (ComplexPattern) visit(ctx.pAllOf());
	}

	@Override
	public ComplexPattern visitParsePAllOf(GrammaticaParser.ParsePAllOfContext ctx) {
		List<GrammaticaParser.PAssertionContext> list = ctx.pAssertion();

		ComplexPattern p = (ComplexPattern) visit(list.get(0));

		for(int i = 1; i < list.size(); i++){
			GrammaticaParser.PAssertionContext el = list.get(i);
			p = p.intersect((ComplexPattern) visit(el));
		}

		return p;
	}

	@Override
	public ComplexPattern visitNewpAnyOf(GrammaticaParser.NewpAnyOfContext ctx) {
		return (ComplexPattern) visit(ctx.pAnyOf());
	}

	@Override
	public ComplexPattern visitParsePAnyOf(GrammaticaParser.ParsePAnyOfContext ctx) {
		List<GrammaticaParser.PAssertionContext> list = ctx.pAssertion();

		ComplexPattern p = (ComplexPattern) visit(list.get(0));

		for(int i = 1; i < list.size(); i++){
			GrammaticaParser.PAssertionContext el = list.get(i);
			p = p.union((ComplexPattern) visit(el));
		}

		return p;
	}

	@Override
	public ComplexPattern visitNewpNot(GrammaticaParser.NewpNotContext ctx) {
		return (ComplexPattern) visit(ctx.pNot());
	}

	@Override
	public ComplexPattern visitParsepNot(GrammaticaParser.ParsepNotContext ctx) {
		return ((ComplexPattern) visit(ctx.pAssertion())).complement();
	}

	@Override
	public NotPattern_Assertion visitNewNotPattern(GrammaticaParser.NewNotPatternContext ctx) {
		return (NotPattern_Assertion) visit(ctx.not_pattern_assertion());
	}

	@Override
	public NotPattern_Assertion visitParseNotPattern(GrammaticaParser.ParseNotPatternContext ctx) {
		 AlgebraParserElement el = visit(ctx.pAssertion());

		return new NotPattern_Assertion((ComplexPattern) el);
	}

	@Override
	public Exist_Assertion visitNewContains(GrammaticaParser.NewContainsContext ctx) {
		return (Exist_Assertion) visit(ctx.contains_assertion());
	}

	@Override
	public Exist_Assertion visitParseContains(GrammaticaParser.ParseContainsContext ctx) {
		try {
			AntlrLong min = (AntlrLong) visit(ctx.json_value(0));
			AntlrLong max = (AntlrLong) visit(ctx.json_value(1));

			if(min.getValue() == null)
				throw new ParseCancellationException("Unexpected -inf/null value in contains Assertion");

			if (min.getValue() < 0 || ( max.getValue() != null && max.getValue() < 0))
				throw new ParseCancellationException("Unexpected negative value in Contains assertion");

			Assertion schema = (Assertion) visit(ctx.assertion());

			return new Exist_Assertion(min.getValue(), max.getValue(), schema);
		}catch (ClassCastException e){
			throw new ParseCancellationException("Unexpected Double value in contains assertion");
		}
	}

	@Override
	public Exist_Assertion visitNewContAfter(GrammaticaParser.NewContAfterContext ctx) {
		return (Exist_Assertion) visit(ctx.contAfter_assertion());
	}

	@Override
	public Exist_Assertion visitParseContAfter(GrammaticaParser.ParseContAfterContext ctx) {
		try {
			AntlrLong position = (AntlrLong) visit(ctx.json_value());

			Assertion schema = (Assertion) visit(ctx.assertion());

			return new Exist_Assertion(1L, null, position.getValue(), schema);
		}catch (ClassCastException e){
			throw new ParseCancellationException("Unexpected Double value in contAfter assertion");
		}
	}

	@Override
	public Const_Assertion visitNewConst(GrammaticaParser.NewConstContext ctx) {
		return (Const_Assertion) visit(ctx.const_assertion());
	}

	@Override
	public Const_Assertion visitParseConst(GrammaticaParser.ParseConstContext ctx) {
		AntlrValue value = (AntlrValue) visit(ctx.json_value());
		JsonElement val = null;

		if(value.getValue() == null)
			val = JsonNull.INSTANCE;
		else if(value.getValue().getClass() == String.class)
			val = new JsonPrimitive((String) value.getValue());
		else if(value.getValue().getClass() == Long.class || value.getValue().getClass() == Double.class)
			val = new JsonPrimitive((Number) value.getValue());
		else if(value.getValue().getClass() == Boolean.class)
			val = new JsonPrimitive((Boolean) value.getValue());
		else if(value.getValue().getClass() == JsonArray.class)
			val = (JsonArray) value.getValue();
		else
			val = (JsonObject) value.getValue();

		return new Const_Assertion(val);
	}

	@Override
	public Items_Assertion visitNewItems(GrammaticaParser.NewItemsContext ctx) {
		return (Items_Assertion) visit(ctx.items_assertion());
	}

	@Override
	public Items_Assertion visitParseItems(GrammaticaParser.ParseItemsContext ctx) {
		List<AssertionContext> list = ctx.assertion();
		Items_Assertion items = new Items_Assertion();
		boolean thereIsAnAdditional = ctx.getText().contains(";"+ list.get(list.size()-1).getText()+"]");

		if(thereIsAnAdditional)
			items.setAdditionalItems((Assertion) visit(list.remove(list.size()-1)));

		for(int i = 0; i < list.size(); i++)
			items.add((Assertion) visit(list.get(i)));


		return items;
	}

	@Override
	public Properties_Assertion visitParseProperties(GrammaticaParser.ParsePropertiesContext ctx) {
		Properties_Assertion prop = new Properties_Assertion();

		List<AssertionContext> list = ctx.assertion();
		List<GrammaticaParser.PAssertionContext> idList = ctx.pAssertion();

		if(list.size() == idList.size()+1)      // if there is one extra assertion then...
			prop.setAdditionalProperties((Assertion) visit(list.remove(list.size()-1)));

			for(int i = 0; i < list.size(); i++) {
				if(idList.get(i).getClass() == GrammaticaParser.NewComplexPatternStringContext.class) { //se è una stringa
					if (idList.get(i).getText().charAt(1) == '^') {
						try {
							prop.addPatternProperties(ComplexPattern.createFromRegexp(idList.get(i).getText().subSequence(1, idList.get(i).getText().length() - 1).toString()), (Assertion) visit(list.get(i)));
						} catch (java.lang.IllegalArgumentException | REException e) {
							throw new ParseCancellationException("REException: " + e.getMessage());
						}
					} else {
						//prop.addProperties(idList.get(i).getText().subSequence(1, idList.get(i).getText().length() - 1).toString(), (Assertion) visit(list.get(i)));
						try {
							prop.addPatternProperties(idList.get(i).getText().subSequence(1, idList.get(i).getText().length() - 1).toString(), (Assertion) visit(list.get(i)));
						} catch (java.lang.IllegalArgumentException | REException e) {
							throw new ParseCancellationException("REException: " + e.getMessage());
						}
					}
				}else{
					prop.addPatternProperties((ComplexPattern) visit(idList.get(i)), (Assertion) visit(list.get(i)));
				}

			}


		return prop;
	}

	@Override
	public PatternRequired_Assertion visitNewPatternRequired(GrammaticaParser.NewPatternRequiredContext ctx) {
		return (PatternRequired_Assertion) visit(ctx.pattern_required());
	}

	@Override
	public PatternRequired_Assertion visitParsePatternRequired(GrammaticaParser.ParsePatternRequiredContext ctx) {
		PatternRequired_Assertion p = new PatternRequired_Assertion();

		List<AssertionContext> list = ctx.assertion();
		List<GrammaticaParser.PAssertionContext> idList = ctx.pAssertion();

		for(int i = 0; i < list.size(); i++) {
			if(idList.get(i).getClass() == GrammaticaParser.NewComplexPatternStringContext.class) {
				try {
					p.add(ComplexPattern.createFromRegexp(idList.get(i).getText().subSequence(1, idList.get(i).getText().length() - 1).toString()), (Assertion) visit(list.get(i)));
				} catch (java.lang.IllegalArgumentException | REException e) {
					throw new ParseCancellationException("REException: " + e.getMessage());
				}
			}else{
				p.add((ComplexPattern) visit(idList.get(i)), (Assertion) visit(list.get(i)));
			}
		}

		return p;
	}

	@Override
	public AddPatternRequired_Assertion visitNewAdditionalPatternRequired(GrammaticaParser.NewAdditionalPatternRequiredContext ctx) {
		return (AddPatternRequired_Assertion) visit(ctx.additional_pattern_required());
	}

	@Override
	public AddPatternRequired_Assertion visitParseAdditionalPatternRequired(GrammaticaParser.ParseAdditionalPatternRequiredContext ctx) {
		AddPatternRequired_Assertion addPattReq = new AddPatternRequired_Assertion();

		Assertion assertion = (Assertion) visit(ctx.assertion());
		List<GrammaticaParser.PAssertionContext> idList = ctx.pAssertion();

		for(int i = 0; i < idList.size(); i++) {
			if(idList.get(i).getClass() == GrammaticaParser.NewComplexPatternStringContext.class) {
				try {
					addPattReq.addName(ComplexPattern.createFromRegexp((idList.get(i).getText().subSequence(1, idList.get(i).getText().length()-1).toString())));
				} catch (java.lang.IllegalArgumentException | REException e) {
					throw new ParseCancellationException("REException: "+e.getMessage());
				}
			}else{
				addPattReq.addName((ComplexPattern) visit(idList.get(i)));
			}
		}

		addPattReq.setAdditionalProperties(assertion);

		return addPattReq;
	}

	/*@Override
	public OrPattReq_Assertion visitParseOrPattReq(GrammaticaParser.ParseOrPattReqContext ctx) {
		OrPattReq_Assertion orPattReq = new OrPattReq_Assertion();

		List<AssertionContext> list = ctx.assertion();
		List<GrammaticaParser.PAssertionContext> idList = ctx.pAssertion();

		for(int i = 0; i < idList.size(); i++) {
			if(idList.get(i).getClass() == GrammaticaParser.NewComplexPatternStringContext.class) {
				try {
					orPattReq.add(ComplexPattern.createFromRegexp(idList.get(i).getText().subSequence(1, idList.get(i).getText().length()-1).toString()), (Assertion) visit(list.get(i)));
				} catch (java.lang.IllegalArgumentException | REException e) {
					throw new ParseCancellationException("REException: "+e.getMessage());
				}
			}else{
				orPattReq.add((ComplexPattern) visit(idList.get(i)), (Assertion) visit(list.get(i)));
			}
		}

		return orPattReq;
	}*/

	/*@Override
	public OrPattReq_Assertion visitNewOrPattReq(GrammaticaParser.NewOrPattReqContext ctx) {
		return (OrPattReq_Assertion) visit(ctx.orPattReq_assertion());
	}*/

	@Override
	public Properties_Assertion visitNewProperties(GrammaticaParser.NewPropertiesContext ctx) {
		return (Properties_Assertion) visit(ctx.properties());
	}

	@Override
	public Len_Assertion visitNewLength(GrammaticaParser.NewLengthContext ctx) {
		return (Len_Assertion) visit(ctx.length_assertion());
	}

	@Override
	public Len_Assertion visitParseLengthAssertion(GrammaticaParser.ParseLengthAssertionContext ctx) {
		try {
			AntlrLong min = (AntlrLong) visit(ctx.json_value(0));
			AntlrLong max = (AntlrLong) visit(ctx.json_value(1));

			if(min.getValue() == null)
				throw new ParseCancellationException("Unexpected -inf/null value in length Assertion");

			if ((long) min.getValue() < 0 || (max.getValue() != null && (long) max.getValue() < 0))
				throw new ParseCancellationException("Unexpected negative value in length assertion");


			return new Len_Assertion(min.getValue(), max.getValue());
		}
		catch (ClassCastException e){
			throw new ParseCancellationException("Unexpected Double value in length assertion");
		}
	}

	@Override
	public Pro_Assertion visitNewBetweenProperties(GrammaticaParser.NewBetweenPropertiesContext ctx) {
		return (Pro_Assertion) visit(ctx.between_properties_assertion());
	}

	@Override
	public Pro_Assertion visitParseBetProAssertion(GrammaticaParser.ParseBetProAssertionContext ctx) {
		try {
			AntlrLong min = (AntlrLong) visit(ctx.json_value(0));
			AntlrLong max = (AntlrLong) visit(ctx.json_value(1));

			if(min.getValue() == null)
				throw new ParseCancellationException("Unexpected -inf/null value in pro Assertion");

			if (min.getValue() < 0 || (max.getValue() != null && max.getValue() < 0))
				throw new ParseCancellationException("Unexpected negative value in pro Assertion");

			return new Pro_Assertion(min.getValue(), max.getValue());
		}catch(ClassCastException e){
			throw new ParseCancellationException("Unexpected Double value in pro assertion");
		}
	}

	@Override
	public Exist_Assertion visitNewBetweenItems(GrammaticaParser.NewBetweenItemsContext ctx) {
		return (Exist_Assertion) visit(ctx.bet_items_assertion());
	}

	@Override
	public Exist_Assertion visitParseBetItemsAssertion(GrammaticaParser.ParseBetItemsAssertionContext ctx) {
		try {
			AntlrLong min = (AntlrLong) visit(ctx.json_value(0));
			AntlrLong max = (AntlrLong) visit(ctx.json_value(1));

			if(min.getValue() == null)
				throw new ParseCancellationException("Unexpected -inf/null value in betItems Assertion");

			if (min.getValue() < 0 || (max.getValue() != null && max.getValue() < 0))
				throw new ParseCancellationException("Unexpected negative value in betItems Assertion");

			return new Exist_Assertion(min.getValue(), max.getValue(), new Boolean_Assertion(true));
		}catch(ClassCastException e){
			throw new ParseCancellationException("Unexpected Double value in betItems assertion");
		}
	}

	@Override
	public Defs_Assertion visitNewDef(GrammaticaParser.NewDefContext ctx) {
		return (Defs_Assertion) visit(ctx.def_assertion());
	}

	@Override
	public Defs_Assertion visitParseDef(GrammaticaParser.ParseDefContext ctx) {
		Defs_Assertion defs = new Defs_Assertion();

		List<AssertionContext> list = ctx.assertion();
		List<TerminalNode> idList = ctx.STRING();

		String rootName = idList.remove(0).getText().replace("\"","");
		Boolean rootFound = false;
		for(int i = 0; i < list.size(); i++) {
			String defName = idList.get(i).getText().replace("\"","");
			if(defName.equals("OBDD_false") || defName.equals("OBDD_true")) continue;
			if (defName.equals(rootName)) {
				defs.setRootDef(defName, (Assertion) visit(list.get(i)));
				rootFound = true;
			} else {
				defs.add(defName, (Assertion) visit(list.get(i)));
			}
		}
		if (!rootFound) { throw new ParseCancellationException("root variable not defined"); }
		return defs;
	}

	@Override
	public Ref_Assertion visitNewRef(GrammaticaParser.NewRefContext ctx) {
		return (Ref_Assertion) visit(ctx.ref_assertion());
	}

	@Override
	public Ref_Assertion visitParseRef(GrammaticaParser.ParseRefContext ctx) {
		String str = ctx.STRING().getText();
		return new Ref_Assertion(str.substring(1, str.length()-1));
	}

	@Override
	public Names_Assertion visitNewPropertyNames(GrammaticaParser.NewPropertyNamesContext ctx) {
		return (Names_Assertion) visit(ctx.propertyNames_assertion());
	}

	@Override
	public Names_Assertion visitParsePropertyNames(GrammaticaParser.ParsePropertyNamesContext ctx) {
		return new Names_Assertion((Assertion) visit(ctx.assertion()));
	}


	@Override
	public AlgebraParserElement visitInfValue(GrammaticaParser.InfValueContext ctx) {
		return new AntlrLong(null);
	}


	@Override
	public ExName_Assertion visitNewPropertyExNames(GrammaticaParser.NewPropertyExNamesContext ctx) {
		return (ExName_Assertion) visit(ctx.propertyExNames_assertion());
	}

	@Override
	public ExName_Assertion visitParsePropertyExNames(GrammaticaParser.ParsePropertyExNamesContext ctx) {
		return new ExName_Assertion((Assertion) visit(ctx.assertion()));
	}

	@Override
	public IfBoolThen_Assertion visitNewIfBoolThen(GrammaticaParser.NewIfBoolThenContext ctx){
		return (IfBoolThen_Assertion) visit(ctx.ifBoolThen_assertion());
	}

	@Override
	public IfBoolThen_Assertion visitParseIfBoolThen(GrammaticaParser.ParseIfBoolThenContext ctx){
		switch(ctx.BOOLEAN().getText()){
			case "true":
			case "t":
			case "tt":
				return new IfBoolThen_Assertion(true);
			default:
				return new IfBoolThen_Assertion(false);
		}
	}
}