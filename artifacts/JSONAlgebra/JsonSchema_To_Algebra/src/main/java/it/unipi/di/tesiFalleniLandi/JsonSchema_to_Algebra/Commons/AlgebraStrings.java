package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons;

import java.util.List;

/**
 * Metodi e classi utilizzati per costruire l'output in grammatica.
 */
public class AlgebraStrings {
	//LOGIC OP
	public static final String COMMA = ",\r\n";
	public static final String FLAT_COMMA = ",";

	//+inf AND -inf
	public static final String POS_INF = "+inf";
	public static final String NEG_INF = "-inf";

	//TYPES
	public static final String TYPE_BOOLEAN = "bool";
	public static final String TYPE_STRING = "str";
	public static final String TYPE_NULL = "null";
	public static final String TYPE_NUMBER = "num";
	public static final String TYPE_INTEGER = "int";
	public static final String TYPE_ARRAY = "arr";
	public static final String TYPE_OBJECT = "obj";
	public static final String TYPE_NUMNOTINT = "numNotInt";

	//UNIQUE ITEMS
	public static final String UNIQUEITEMS = "uniqueItems";

	//REPEATED ITEMS
	public static final String REPEATEDITEMS = "repeatedItems";

	//DEFS
	public static final String ROOTDEF_DEFAULTNAME = "root";
	public static final String NOT_DEFS = "not_";










	//JSONSchema
	public static String JSONSCHEMA(String s){
		StringBuilder b = new StringBuilder();

		b.append("{\r\n")
				.append(s)
				.append("\r\n}");

		return b.toString();
	}

	//RANGES
	public static String BETWEENNUMBER(String min, String max){return tonde("bet", min, max);}
	public static String BETWEENNUMBER_EXCLUSIVE(String min, String max){return tonde("xbet", min, max);}
	public static String BETWEENITEMS(String min, String max){return tonde("betItems", min, max);}
	public static String BETWEENPROPERTIES(String min, String max){return tonde("pro", min, max);}


	//MULTIPLEOF
	public static String MULTIPLEOF(String s){ return tonde("mof", s);}
	public static String NOTMULTIPLEOF(String s){ return tonde("notMof", s); }

	//TYPE
	public static String TYPE(String s){ return flat_quadre("type", s); }

	//ALLOF, ANYOF, ONEOF
	public static String ALLOF(String s){ return quadre("allOf", s); }
	public static String ANYOF(String s){ return quadre("anyOf", s); }
	public static String ONEOF(String s){ return quadre("oneOf", s); }

	//NOT
	public static String NOT(String s){ return tonde("not", s); };

	//REQUIRED
	public static String REQUIRED(String s){ return quadre("req", s); };

	//PATTERN
	public static String PATTERN(String s){ return tonde("pattern",s ); }
	public static String NOTPATTERN(String s){ return tonde("notPattern", s); }

	//LENGTH
	public static String LENGTH(String min, String max){ return tonde("length", min, max); }

	//ITEMS
	public static String ITEMS(String items, String addItems){ return quadre("items", items, addItems); }

	//CONTAINS
	public static String CONTAINS(String s1, String s2, String s3) {
		StringBuilder b = new StringBuilder();

		b.append("contains(")
				.append(s1)
				.append(",")
				.append(s2)
				.append(";")
				.append(s3)
				.append(")");

		return b.toString();
	}

	//CONTAFTER
	public static String CONTAFTER(String s1, String s2) {
		StringBuilder b = new StringBuilder();

		b.append("contAfter(")
				.append(s1)
				.append(";")
				.append(s2)
				.append(")");

		return b.toString();
	}

	//ENUM
	public static String ENUM(String s) { return quadre("enum", s); }

	//CONST
	public static String CONST(String s) { return tonde("const", s); }

	//DEFS
	public static String DEFS(String name, String value){
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("def \"")
				.append(name)
				.append("\" = ")
				.append(value);

		return stringBuilder.toString();
	}
	public static String ROOTDEF(String name, String value){
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("rootdef ")
				.append(name)
				.append(" = ")
				.append(value);

		return stringBuilder.toString();
	}

	//IFTHEN-IFTHENELSE
	public static String IF_THEN(String ...s){
		StringBuilder b = new StringBuilder();
		if(s.length == 2) b.append("ifThen");
		if(s.length == 3) b.append("ifThenElse");

		b.append("(")
				.append(s[0])
				.append(";\r\n")
				.append(s[1]);
		if(s.length == 3)
			b.append(";\r\n")
					.append(s[2]);

		b.append(")");

		return b.toString();
	}

	//PROPERTIES
	public static String PROPERTIES( ){ return quadre("props"); }
	public static String PROPERTIES(String p){ return quadre("props", p); }
	public static String PROPERTIES(String p, String addProp){ return quadre("props", p, addProp); }
	public static String SINGLEPROPERTIES(String s1, String s2) {return new StringBuilder().append("\"").append(s1).append("\": ").append(s2).toString(); }
	public static String SINGLEPATTERNPROPERTIES(String s1, String s2) {return new StringBuilder().append(s1).append(": ").append(s2).toString(); }

	//PATTREQ
	public static  String PATTERNREQUIRED = "pattReq[\r\n%s\r\n]";

	//addPattReq
	public static final String ADDPATTERNREQUIRED(String s1, String s2){
		return new StringBuilder().append("addPattReq([")
				.append(s1)
				.append("] : ")
				.append(s2)
				.append(")")
				.toString();
	}

	//PROPERTYNAMES
	public static String PROPERTYNAMES(String s){ return tonde("names", s); }

	//REF
	public static String REF(String s){
		return tonde("ref", new StringBuilder().append("\"").append(s).append("\"").toString()); }

	//EXNAME
	public static String EXNAME(String s){ return tonde("exNames", s); }

	//IfBoolThen
	public static String IFBOOLTHEN(String s) { return tonde("ifBoolThen", s); }




	//OrPattReq
	public static String ORPATTREQ(String s) { return quadre("orPattReq", s); }



	//ItemsPrepared
	public static String ITEMSPREPARED(List<String[]> items, String[] additionalItem, String[] contains){
		StringBuilder b = new StringBuilder("items#(\r\n");

		boolean run = false;
		for(int i = 0; i < items.size(); i++) {
			b.append(printFunction(items.get(i)))
					.append(",\r\n");

			run = true;
		}

		if(run) b.delete(b.length()-3, b.length()); //removing ",\r\n"

		b.append(";\r\n");

		if(additionalItem != null)
			b.append(printFunction(additionalItem));

		b.append("\r\n|\r\n ");

		run = false;

		for(String s : contains){
			run = true;
			b.append(s)
					.append(",\r\n");
		}

		if(run) b.delete(b.length()-3, b.length()); //removing ",\r\n"

		return b.append(")")
				.toString();
	}


	private static String printFunction(String[] f){
		StringBuilder b = new StringBuilder();


		boolean run = false;
		for(int i = 0; i < f.length; i++) {
			b.append("{")
					.append(i)
					.append("->[")
					.append(f[i])
					.append("] }");
			run = true;
		}

		return b.toString();
	}





	private static String tonde(String keyword, String ...s){
		StringBuilder b = new StringBuilder();

		b.append(keyword)
				.append("(")
				.append(s[0]);
		for(int i = 1; i < s.length; i++) b.append(",").append(s[1]);

		b.append(")");

		return b.toString();
	}

	private static String flat_quadre(String keyword, String ...s){
		StringBuilder b = new StringBuilder();

		b.append(keyword)
				.append("[")
				.append(s[0]);
		for(int i = 1; i < s.length; i++) b.append(";").append(s[1]);

		b.append("]");

		return b.toString();
	}

	private static String quadre(String keyword, String ...s){
		StringBuilder b = new StringBuilder();

		b.append(keyword)
				.append("[\r\n")
				.append(s[0]);
		for(int i = 1; i < s.length; i++) b.append(";\r\n").append(s[1]);

		b.append("\r\n]");

		return b.toString();
	}

	private static String quadre(String keyword){
		StringBuilder b = new StringBuilder();

		b.append(keyword).append("[\r\n]");

		return b.toString();
	}

}