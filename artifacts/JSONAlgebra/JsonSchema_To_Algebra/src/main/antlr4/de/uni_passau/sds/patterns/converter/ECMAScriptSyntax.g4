grammar ECMAScriptSyntax;

expression
 : expression '|' sliceGroupSequence
 | expression '|' slice?
 | sliceGroupSequence
 | slice?
 ;

sliceGroupSequence
 : (slice? group)+ slice?
 ;

group
 : capturing
 | nonCapturing
 | namedCapturing
 | lookaround
 ;

capturing
 : '(' expression ')'
 ;

nonCapturing
 : '(?:' expression ')'
 ;

namedCapturing
 : '(?<' (Character | Digit)+ '>' expression ')'
 ;

lookaround
 : '(?=' expression ')'
 | '(?!' expression ')'
 | '(?<=' expression ')'
 | '(?<!' expression ')'
 ;

slice
 : ((Hat | Dollar | characterOrClass)* quantifyingExpression)+ (Hat | Dollar | characterOrClass)*
 | (Hat | Dollar | characterOrClass)+
 ;

quantifyingExpression
 : (Hat | Dollar | characterOrClass) quantifier
 | group quantifier
 ;

quantifier
 : Star
 | Plus
 | QuestionMark
 | Range
 ;

Range
 : '{' RangeFrag '}'
 | '{' RangeFrag '}?'
 ;

fragment RangeFrag
 : Digit+
 | Digit+ ','
 | Digit+ ',' Digit+
 ;

characterOrClass
 : Character | ']' | Digit
 | CharacterClass
 ;

CharacterClass
 : '[' (Character | Digit | CharacterInClass | EscapeSequenceCharClass | CharacterRange)+ ']'
 | '[^' (Character | Digit | CharacterInClass | EscapeSequenceCharClass | CharacterRange)+ ']'
 | AnyCharacter
 | EscapeSequenceCharClass
 ;

fragment CharacterRange
 : (Character | Digit | CharacterInClass) '-' (Character | Digit | CharacterInClass)
 ;

Star
 : '*'
 | '*?'
 ;

Plus
 : '+'
 | '+?'
 ;

QuestionMark
 : '?'
 | '??'
 ;

Hat
 : '^'
 ;

Dollar
 : '$'
 ;

Character
 : ~[*+|()[\]^$?.\\0-9]
 | '\\' [*+|()[^$?.]
 | '\\\\'
 | '\\' [!"#%&',-/:;<=>@\]_`{}~ ]
 | '\\' ~[DSWdsw]
 | '\\u' [0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]
 | '\\x' [0-9A-Fa-f][0-9A-Fa-f]
 | '\\' [0-7][0-7][0-7]?
 | '\\c' [A-Za-z]
 ;

CharacterInClass
 : [*+|()[^$?.]
 ;

Digit
 : [0-9]
 ;

AnyCharacter
 : '.'
 ;

EscapeSequenceCharClass
 : '\\d'
 | '\\s'
 | '\\w'
 | '\\D'
 | '\\S'
 | '\\W'
 ;