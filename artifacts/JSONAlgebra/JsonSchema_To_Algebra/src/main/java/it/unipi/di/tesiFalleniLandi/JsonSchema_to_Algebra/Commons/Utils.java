package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons;

public class Utils {

	//Usata da Defs e JSONSchema per ricostruire l'oggetto json originale:
		//dopo normalizzazione ho: { "$defs": { ...: ... , ROOTDEF_FOR_JSONSCHEMA: rootDefAssertions} }
	public static final String ROOTDEF_FOR_JSONSCHEMA = "putContent";


	/**
	 * Indenta l'algebra
	 * INPUT: algebra, già dotata di \r\n
	 * OUTPUT: algebra, tabulata
	 * @param input
	 * @return
	 */
	public static String beauty(String input) {
		StringBuilder output = new StringBuilder();
		int tab = 0;

		for(int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			Character c1 = (i+1 == input.length()) ? null : input.charAt(i+1);

			switch(c) {
				case '\n':
					output.append((c1 != null && (c1 == '}' || c1 == ']')) ? c+tabs(tab-1) : c+tabs(tab));
					continue;

				case '[': case '{': case '(': //<-- TODO: vedere come stampa con questo ultimo caso
					tab++;
					break;

				case ']': case '}': case ')': //<-- TODO: vedere come stampa con questo ultimo caso
					tab--;
					break;

				default:
					break;
			}

			output.append(c);
		}

		return output.toString();
	}
	
	//restituisce una stringa contenente n tab ("\t")
	private static String tabs(int n) {
		String output = "";
		
		for(int i = 0; i < n; i++) output += "\t";
		
		return output;
	}
}
