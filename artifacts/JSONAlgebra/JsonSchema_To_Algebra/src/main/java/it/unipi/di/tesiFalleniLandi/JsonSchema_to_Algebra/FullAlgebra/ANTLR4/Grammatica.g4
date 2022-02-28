grammar Grammatica;

@header {package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.ANTLR4;
}

assertion_list : '{' assertion (',' assertion)* '}'																	#ParseAssertionList
				|    BOOLEAN																						#ParseBooleanSchema;

assertion : 		type_assertion																					#NewTypeAssertion
   				|	between_assertion	         																	#NewBetweenAssertion
   				|	not_assertion																					#NewNot
   				| 	xbetween_assertion																				#NewXBetweenAssertion
   				|	bet_items_assertion																				#NewBetweenItems
   				|	length_assertion																				#NewLength
   				|	between_properties_assertion																	#NewBetweenProperties
   				|	all_of_assertion																				#NewAllOf
   				|	any_of_assertion																				#NewAnyOf
   				|	one_of_assertion																				#NewOneOf
   				|	required_assertion																				#NewRequired
   				|	if_then_else_assertion			   																#NewIfThenElse
   				|	multiple_of_assertion																			#NewMultipleOf
   				|	enum_assertion_assertion																		#NewEnum
   				|	unique_items_assertion																			#NewUniqueItems
				|	pattern_assertion																				#NewPattern
				|	contains_assertion				   																#NewContains
				|	contAfter_assertion				   																#NewContAfter
				|	const_assertion			         																#NewConst
				|	items_assertion					   																#NewItems
				|	assertion_list																					#NewAssertionList
				|   properties																						#NewProperties
				|	def_assertion																					#NewDef
				|	ref_assertion																					#NewRef
				|	propertyNames_assertion																			#NewPropertyNames
				|   propertyExNames_assertion                                                                       #NewPropertyExNames
				|	annotations																						#NewAnnotations
				|	not_multiple_of_assertion																		#NewNotMultipleOf
				|	not_pattern_assertion																			#NewNotPattern
				|	repeated_items_assertion																		#NewRepeatedItems
				|	additional_pattern_required																		#NewAdditionalPatternRequired
				|	pattern_required																				#NewPatternRequired
				|   ifBoolThen_assertion                                                                            #NewIfBoolThen
				|   orPattReq_assertion                                                                             #NewOrPattReq
	;

	
type_assertion : 'type''[' (TYPE | NULL) (',' (TYPE | NULL))*']'													#ParseTypeAssertion;

between_assertion : 'bet''(' json_value ',' json_value ')'															#ParseBetweenAssertion;

xbetween_assertion : 'xbet''(' json_value ',' json_value ')'														#ParseXBetweenAssertion;

length_assertion : 'length''('json_value','json_value')'															#ParseLengthAssertion;

bet_items_assertion : 'betItems''('json_value','json_value')'														#ParseBetItemsAssertion;

between_properties_assertion : 'pro''('json_value','json_value')'													#ParseBetProAssertion;

multiple_of_assertion : 'mof''('json_value')'																		#ParseMultipleOf;

not_multiple_of_assertion : 'notMof''('json_value')'																#ParseNotMultipleOf;

not_assertion : 'not''(' assertion ')'																				#ParseNot;

all_of_assertion : 'allOf''[' assertion (',' assertion)* ']'														#ParseAllOf;

one_of_assertion : 'oneOf''[' assertion (',' assertion)* ']'														#ParseOneOf;

any_of_assertion : 'anyOf''[' assertion (',' assertion)* ']'														#ParseAnyOf;

required_assertion : 'req''[' STRING (',' STRING)* ']'																#ParseRequired;

enum_assertion_assertion : 'enum[' json_value (',' json_value)* ']'													#ParseEnum;

if_then_else_assertion : 'ifThenElse''(' assertion ';' assertion ';' assertion ')'									#ParseIfThenElse
						|	'ifThen''(' assertion ';' assertion')'													#ParseIfThen
						;

unique_items_assertion : 'uniqueItems'																				#ParseUniqueItems;

repeated_items_assertion : 'repeatedItems'																			#ParseRepeatedItems;

orPattReq_assertion : 'orPattReq[' pAssertion ':' assertion (',' pAssertion ':' assertion)* ']'             #ParseOrPattReq;

pAllOf : 'pAllOf''[' pAssertion (',' pAssertion)* ']'														#ParsePAllOf;

pAnyOf : 'pAnyOf''[' pAssertion (',' pAssertion)* ']'															#ParsePAnyOf;

pNot : 'pNot''('pAssertion')'																						#ParsepNot;

pAssertion: pAllOf																								#NewpAllOf
                | pAnyOf																						#NewpAnyOf
                | pNot																							#NewpNot
                | STRING																						#NewComplexPatternString;


pattern_assertion : 'pattern''(' pAssertion')'														                #ParsePattern;


not_pattern_assertion : 'notPattern''(' pAssertion ')'												                    #ParseNotPattern;

items_assertion : 'items''[' (assertion (',' assertion)*)?';'assertion?']'										#ParseItems
					;

contains_assertion : 'contains''(' json_value ',' json_value ';' assertion	')'										#ParseContains;

contAfter_assertion : 'contAfter''(' json_value ';' assertion ')'										#ParseContAfter;

properties : 'props''[' ( pAssertion ':' assertion (','pAssertion ':' assertion)*)? (';' assertion?)? ']'						#ParseProperties;

const_assertion : 'const''(' json_value ')'																			#ParseConst;

def_assertion:  STRING 'defs' '[' STRING ':' assertion ( ',' STRING ':' assertion) * ']'                  #ParseDef;

/* ('def'|'rootdef') STRING '=' assertion 	(',' (('def'|'rootdef') STRING '=' assertion))*	*/

DEF_FIRMA: (ESC+ STRING);

ref_assertion: 'ref''(' STRING ')'																					#ParseRef;

propertyNames_assertion: 'names''(' assertion ')'																	#ParsePropertyNames;

propertyExNames_assertion: 'exNames''(' assertion ')'																#ParsePropertyExNames;

annotations: 'annotations''['STRING':'STRING (','STRING':'STRING)*	']'												#ParseAnnotations; //non implemented in JSON_to_Grammar

pattern_required: 'pattReq''[' pAssertion ':' assertion (',' pAssertion ':' assertion)* ']'									#ParsePatternRequired;

additional_pattern_required: 'addPattReq''(' '['(pAssertion (',' pAssertion)*)*']' ':' assertion ')'						#ParseAdditionalPatternRequired;

ifBoolThen_assertion: 'ifBoolThen''('BOOLEAN')'                                                                     #ParseIfBoolThen;


json_value :  			NULL																						#NullValue
				|		INT 																						#IntValue
				|		STRING																						#StringValue
				|		DOUBLE																						#DoubleValue
				|		'['(json_value(',' json_value)*)?']'														#ArrayValue
				|		BOOLEAN																						#BooleanValue
				|		'{' (STRING ':' json_value (',' STRING ':' json_value)*)? '}'								#JsonObjectValue
				|       ('+'|'-')? 'inf'                                                                            #InfValue
				;


NULL : 'null';
TYPE : 'obj' | 'str' | 'num' | 'int' | 'arr' | 'bool' | 'numNotInt';
INT : '-'?[0-9]+; // Define token INT as one or more digits
DOUBLE: '-'?[0-9]+'.'[E0-9]+;
WS : [ \t\r\n]+ -> skip ; // Define whitespace rule, toss it out
STRING : '"' ( ESC | ~[\\"\r\n] )* '"';
BOOLEAN :  't' | 'tt' | 'f' | 'ff' | 'true' | 'false' ;

fragment ESC
   : '\\' (["\\/bfnrt] | UNICODE)
   ;

fragment UNICODE
   : 'u' HEX HEX HEX HEX
   | 'x' HEX HEX
   ;

fragment HEX
   : [0-9a-fA-F]
   ;
fragment SAFECODEPOINT
   : ~ ["\\\u0000-\u001F]
   ;


COMMENT
    : '/*' .*? '*/' -> skip
    ;

LINE_COMMENT
    : '//' ~[\r\n]* -> skip
    ;