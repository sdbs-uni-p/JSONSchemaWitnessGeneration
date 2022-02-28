package de.uni_passau.sds.patterns.converter;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ECMAScriptSyntaxVisitorImpl extends ECMAScriptSyntaxBaseVisitor<ExpressionTree.Node> {

    @Override
    public ExpressionTree.Node visitExpression(ECMAScriptSyntaxParser.ExpressionContext ctx) {
        List<ParseTree> children = ctx.children;
        if (children == null) {
            return ExpressionTree.EmptyWord.create();
        }
        if (children.get(0) instanceof ErrorNode) {
            throw new ECMAScriptSyntaxException("Syntax error.", ECMAScriptSyntaxException.NONSPEC_ERROR);
        }
        if (children.size() == 1) { // single slice
            return visit(children.get(0));
        }
        if (children.get(1).getText().charAt(0) == '|') { // disjunction
            if (children.size() == 2) { // nothing (empty word) on the right side => simplify to QuestionMark
                return ExpressionTree.Disjunction.create(visit(children.get(0)), ExpressionTree.EmptyWord.create());
            }
            if (children.size() == 3) {
                return ExpressionTree.Disjunction.create(visit(children.get(0)), visit(children.get(2)));
            }
        }
        // should not reach here
        throw new RuntimeException("Parsing error.");
    }

    @Override
    public ExpressionTree.Node visitSliceGroupSequence(ECMAScriptSyntaxParser.SliceGroupSequenceContext ctx) {
        List<ParseTree> children = ctx.children;
        if (children.size() == 1) { // single group
            return visit(children.get(0));
        }
        // several groups and slices
        return ExpressionTree.Concatenation.create(children.stream().map(this::visit)
                .toArray(ExpressionTree.Node[]::new));
    }

    @Override
    public ExpressionTree.Node visitGroup(ECMAScriptSyntaxParser.GroupContext ctx) {
        return visit(ctx.children.get(0));
    }

    @Override
    public ExpressionTree.Node visitCapturing(ECMAScriptSyntaxParser.CapturingContext ctx) {
        return visit(ctx.children.get(1));
    }

    @Override
    public ExpressionTree.Node visitNonCapturing(ECMAScriptSyntaxParser.NonCapturingContext ctx) {
        return visit(ctx.children.get(1));
    }

    @Override
    public ExpressionTree.Node visitNamedCapturing(ECMAScriptSyntaxParser.NamedCapturingContext ctx) {
        return visit(ctx.children.get(ctx.children.size() - 2));
    }

    @Override
    public ExpressionTree.Node visitLookaround(ECMAScriptSyntaxParser.LookaroundContext ctx) {
        throw new ECMAScriptNotSupportedException("Lookahead/lookbehind is not supported.",
                ECMAScriptNotSupportedException.LOOKAROUND_ERROR);
    }

    @Override
    public ExpressionTree.Node visitQuantifyingExpression(ECMAScriptSyntaxParser.QuantifyingExpressionContext ctx) {
        ParseTree input = ctx.getChild(0);
        ParseTree quantifierSymbol = ctx.getChild(1).getChild(0);
        switch (quantifierSymbol.getText().charAt(0)) {
            case '*':
                return ExpressionTree.Star.create(visit(input));
            case '+':
                return ExpressionTree.Plus.create(visit(input));
            case '?':
                return ExpressionTree.QuestionMark.create(visit(input));
            case '{':
                String[] limits = parseQuantifierRange(quantifierSymbol.getText());
                if (limits.length == 1) {
                    return ExpressionTree.Range.create(visit(input), Integer.parseInt(limits[0]), true);
                }
                return limits[1].isEmpty()
                        ? ExpressionTree.Range.create(visit(input), Integer.parseInt(limits[0]), false)
                        : ExpressionTree.Range.create(visit(input), Integer.parseInt(limits[0]), Integer.parseInt(limits[1]));
            default:
                // should not reach here
                throw new RuntimeException("Invalid quantifier symbol.");
        }
    }

    private String[] parseQuantifierRange(String text) {
        if (!hasQuantifierRangeForm(text)) {
            throw new RuntimeException(text + " has not the correct form of a quantifier range.");
        }
        int end = text.length() - (text.charAt(text.length() - 1) == '?' ? 2 : 1);
        return text.substring(1, end).split(",", -1);
    }

    private boolean hasQuantifierRangeForm(String text) {
        if (text.charAt(0) != '{') {
            return false;
        }
        int lastPos = text.length() - 1;
        if (text.charAt(lastPos) != '}') {
            if (text.charAt(lastPos) != '?') {
                return false;
            }
            return text.charAt(lastPos - 1) == '}';
        }
        return true;
    }

    @Override
    public ExpressionTree.Node visitSlice(ECMAScriptSyntaxParser.SliceContext ctx) {
        List<ParseTree> children = ctx.children;
        if (children.size() == 1) {
            return visit(children.get(0));
        }
        return ExpressionTree.Concatenation.create(children.stream().map(this::visit)
                .toArray(ExpressionTree.Node[]::new));
    }

    @Override
    public ExpressionTree.Node visitCharacterOrClass(ECMAScriptSyntaxParser.CharacterOrClassContext ctx) {
        return visit(ctx.getChild(0));
    }

    @Override
    public ExpressionTree.Node visitTerminal(TerminalNode node) {
        char c = node.getText().charAt(0);
        switch (c) {
            case '^':
                return ExpressionTree.Hat.create();
            case '$':
                return ExpressionTree.Dollar.create();
            case '.':
                return ExpressionTree.AnyCharacter.create();
            case '[':
                return parseCharacterClass(node.getText());
            case '\\':
                return parseEscapeSequence(node.getText());
            default:
                return ExpressionTree.CharTerminal.create(c);
        }
    }

    private ExpressionTree.Node parseCharacterClass(String text) {
        int lastPos = text.length() - 1;
        if (text.charAt(0) != '[' || text.charAt(lastPos) != ']') {
            throw new RuntimeException(text + " has not the correct form of a character class.");
        }
        boolean positive = text.charAt(1) != '^';
        String input = text.substring(positive ? 1 : 2, lastPos);
        String[] inputArr = collectCharacters(input);

        int currentPos = 0;
        List<ExpressionTree.CharClass.CharClassNode> nodes = new LinkedList<>();
        List<ExpressionTree.CharClass> otherClasses = new LinkedList<>();

        while (currentPos < inputArr.length) {
            String currentInput = inputArr[currentPos];
            if (currentInput.charAt(0) == '\\' && "dswDSW".indexOf(currentInput.charAt(1)) != -1) {
                char c = currentInput.charAt(1);
                boolean innerPositive = "dsw".indexOf(c) != -1;
                ExpressionTree.Node node = parseEscapeSequence(currentInput);
                if (innerPositive) {
                    nodes.addAll(((ExpressionTree.CharClass) node).getCharClassNodes());
                } else {
                    if (positive) {
                        otherClasses.add((ExpressionTree.CharClass) node);
                    } else {
                        // TODO implement me!
                        throw new ECMAScriptNotSupportedException("\\D, \\S or \\W in negative char class.", ECMAScriptNotSupportedException.NOTSUPP_ERROR);
                        // e.g. "[^\\\\D]" or "[^\\\\da-z\\\\S]" (no occurrence in corpus)
                    }
                }
                currentPos++;
            } else if (currentPos + 2 < inputArr.length && inputArr[currentPos + 1].equals("-")) {
                nodes.add(ExpressionTree.CharClass.CharClassNode.create(getCharFromString(currentInput),
                        getCharFromString(inputArr[currentPos +2])));
                currentPos += 3;
            } else {
                nodes.add(ExpressionTree.CharClass.CharClassNode.create(getCharFromString(currentInput)));
                currentPos++;
            }
        }

        ExpressionTree.Node converted = ExpressionTree.CharClass.create(positive,
                Arrays.copyOf(nodes.toArray(), nodes.size(), ExpressionTree.CharClass.CharClassNode[].class));

        for (ExpressionTree.CharClass otherClass : otherClasses) {
            converted = ExpressionTree.Disjunction.create(converted, otherClass);
        }

        return converted;
    }

    private char getCharFromString(String charString) {
        if (charString.length() > 1) {
            int cpDec;
            switch (charString.charAt(1)) {
                case 'u':
                case 'x':
                    cpDec = Integer.parseInt(charString.substring(2), 16);
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    cpDec = Integer.parseInt(charString.substring(1), 8);
                    break;
                case 'n':
                    return '\n';
                case 't':
                    return '\t';
                case 'r':
                    return '\r';
                case 'f':
                    return '\f';
                case 'v':
                    return '\u000b';
                case 'b':
                    return '\b';
                case 'c':
                    int asciiNumber = Character.toLowerCase(charString.charAt(2)) - 96;
                    cpDec = Integer.parseInt(Integer.toString(asciiNumber), 10);
                    break;
                default:
                    return charString.charAt(1);
            }
            return Character.toChars(cpDec)[0];
        }
        return charString.charAt(0);
    }

    private String[] collectCharacters(String input) {
        int currentPosition = 0;
        List<String> characters = new LinkedList<>();
        while (currentPosition < input.length()) {
            if (input.charAt(currentPosition) == '\\') {
                String escapedCharacter = String.valueOf(input.charAt(currentPosition + 1));
                switch (escapedCharacter) {
                    case "u":
                        if (input.length() < currentPosition + 6) {
                            throw new RuntimeException("Illegal unicode sequence.");
                        }
                        String unicode = input.substring(currentPosition + 2, currentPosition + 6);
                        if (!unicode.matches("[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]")) {
                            throw new RuntimeException("Illegal unicode sequence.");
                        }
                        escapedCharacter += unicode;
                        currentPosition += 6;
                        break;
                    case "x":
                        if (input.length() < currentPosition + 4) {
                            throw new RuntimeException("Illegal ascii (8-bit) sequence.");
                        }
                        String ascii = input.substring(currentPosition + 2, currentPosition + 4);
                        if (!ascii.matches("[0-9A-Fa-f][0-9A-Fa-f]")) {
                            throw new RuntimeException("Illegal ascii (8-bit) sequence.");
                        }
                        escapedCharacter += ascii;
                        currentPosition += 4;
                        break;
                    case "c":
                        if (input.length() < currentPosition + 3) {
                            throw new RuntimeException("Illegal control sequence.");
                        }
                        String controlChar = input.substring(currentPosition + 2, currentPosition + 3);
                        if (!controlChar.matches("[A-Za-z]")) {
                            throw new RuntimeException("Illegal control sequence.");
                        }
                        escapedCharacter += controlChar;
                        currentPosition += 3;
                        break;
                    case "0":
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                    case "6":
                    case "7":
                        int followingChars = input.length() - currentPosition - 1;
                        if (followingChars == 1) {
                            currentPosition += 2;
                        } else if (followingChars == 2) {
                            char second = input.charAt(currentPosition + 2);
                            if ("01234567".indexOf(second) != -1) { // second in [0-7]
                                escapedCharacter += second;
                                currentPosition += 3;
                            } else {
                                currentPosition += 2;
                            }
                        } else if (followingChars > 2) {
                            char second = input.charAt(currentPosition + 2);
                            if ("01234567".indexOf(second) != -1) {
                                escapedCharacter += second;
                                char third = input.charAt(currentPosition + 3);
                                if ("01234567".indexOf(third) != -1) {
                                    escapedCharacter += third;
                                    currentPosition += 4;
                                } else {
                                    currentPosition += 3;
                                }
                            } else {
                                currentPosition += 2;
                            }
                        } else {
                            // should not reach here
                            throw new RuntimeException("No following chars after backslash.");
                        }
                        break;
                    default:
                        currentPosition += 2;
                }
                characters.add("\\" + escapedCharacter);
            } else {
                characters.add(String.valueOf(input.charAt(currentPosition)));
                currentPosition++;
            }
        }
        return Arrays.copyOf(characters.toArray(), characters.size(), String[].class);
    }

    private ExpressionTree.Node parseEscapeSequence(String text) {
        switch (text.charAt(1)) {
            case 'd':
            case 'D':
                return ExpressionTree.CharClass.create(text.charAt(1) == 'd',
                        ExpressionTree.CharClass.CharClassNode.create('0', '9'));
            case 's':
            case 'S':
                List<ExpressionTree.CharClass.CharClassNode> whitespaceCharacters = getWhitespaceCharacters();
                return ExpressionTree.CharClass.create(text.charAt(1) == 's',
//                        ExpressionTree.CharClass.CharClassNode.create('\u0009', '\r'),
//                        ExpressionTree.CharClass.CharClassNode.create('\u0020'),
//                        ExpressionTree.CharClass.CharClassNode.create('\u0085'),
//                        ExpressionTree.CharClass.CharClassNode.create('\u00a0'),
//                        ExpressionTree.CharClass.CharClassNode.create('\u1680'),
//                        ExpressionTree.CharClass.CharClassNode.create('\u180e'),
//                        ExpressionTree.CharClass.CharClassNode.create('\u2000', '\u200d'),
//                        ExpressionTree.CharClass.CharClassNode.create('\u2028'),
//                        ExpressionTree.CharClass.CharClassNode.create('\u2029'),
//                        ExpressionTree.CharClass.CharClassNode.create('\u202f'),
//                        ExpressionTree.CharClass.CharClassNode.create('\u205f'),
//                        ExpressionTree.CharClass.CharClassNode.create('\u2060'),
//                        ExpressionTree.CharClass.CharClassNode.create('\u3000'),
//                        ExpressionTree.CharClass.CharClassNode.create('\ufeff'));
                        Arrays.copyOf(whitespaceCharacters.toArray(), whitespaceCharacters.size(),
                                ExpressionTree.CharClass.CharClassNode[].class));
            case 'w':
            case 'W':
                return ExpressionTree.CharClass.create(text.charAt(1) == 'w',
                        ExpressionTree.CharClass.CharClassNode.create('0', '9'),
                        ExpressionTree.CharClass.CharClassNode.create('A', 'Z'),
                        ExpressionTree.CharClass.CharClassNode.create('a', 'z'),
                        ExpressionTree.CharClass.CharClassNode.create('_'));
            case 'b':
            case 'B':
                // TODO implement me
                throw new ECMAScriptNotSupportedException("Word boundaries not yet supported.", ECMAScriptNotSupportedException.NOTSUPP_ERROR);
            case 'u':
                if (text.length() != 6) {
                    throw new RuntimeException("Illegal unicode sequence.");
                }
                return createCharTerminalFromCodepointString(text.substring(2), 16);
            case 'x':
                if (text.length() != 4) {
                    throw new RuntimeException("Illegal ascii (8-bit) sequence.");
                }
                return createCharTerminalFromCodepointString(text.substring(2), 16);
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
                return createCharTerminalFromCodepointString(text.substring(1), 8);
            case 'n':
                return ExpressionTree.CharTerminal.create('\n');
            case 't':
                return ExpressionTree.CharTerminal.create('\t');
            case 'r':
                return ExpressionTree.CharTerminal.create('\r');
            case 'f':
                return ExpressionTree.CharTerminal.create('\f');
            case 'v':
                return ExpressionTree.CharTerminal.create('\u000b');
            case 'c':
                if (text.length() != 3) {
                    throw new RuntimeException("Illegal control sequence.");
                }
                int asciiNumber = Character.toLowerCase(text.charAt(2)) - 96;
                return createCharTerminalFromCodepointString(Integer.toString(asciiNumber), 10);
            default:
                return ExpressionTree.CharTerminal.create(text.charAt(1));
        }
    }

    private static ExpressionTree.Node createCharTerminalFromCodepointString(String cp, int radix) {
        int cpDec = Integer.parseInt(cp, radix);
        char c = Character.toChars(cpDec)[0];
        return ExpressionTree.CharTerminal.create(c);
    }

    private static List<ExpressionTree.CharClass.CharClassNode> getWhitespaceCharacters() {
        List<ExpressionTree.CharClass.CharClassNode> whitespaceCharacters = new LinkedList<>();
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\n'));
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u0009')); // CHARACTER TABULATION
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u000b')); // LINE FEED
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u000c')); // FORM FEED
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\r')); // CARRIAGE RETURN
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u0020')); // SPACE
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u0085')); // NEXT LINE
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u00a0')); // NO-BREAK SPACE
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u1680')); // OGHAM SPACE MARK
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u180e')); // MONGOLIAN VOWEL SEPARATOR
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u2000')); // EN QUAD
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u2001')); // EM QUAD
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u2002')); // EN SPACE
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u2003')); // EM SPACE
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u2004')); // THREE-PER-EM SPACE
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u2005')); // FOUR-PER-EM SPACE
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u2006')); // SIX-PER-EM SPACE
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u2007')); // FIGURE SPACE
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u2008')); // PUNCTUATION SPACE
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u2009')); // THIN SPACE
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u200a')); // HAIR SPACE
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u200b')); // ZERO WIDTH SPACE
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u200c')); // ZERO WIDTH NON-JOINER
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u200d')); // ZERO WIDTH JOINER
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u2028')); // LINE SEPARATOR
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u2029')); // PARAGRAPH SEPARATOR
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u202f')); // NARROW NO-BREAK SPACE
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u205f')); // MEDIUM MATHEMATICAL SPACE
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u2060')); // WORD JOINER
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\u3000')); // IDEOGRAPHIC SPACE
        whitespaceCharacters.add(ExpressionTree.CharClass.CharClassNode.create('\ufeff'));
        return whitespaceCharacters;
    }

}
