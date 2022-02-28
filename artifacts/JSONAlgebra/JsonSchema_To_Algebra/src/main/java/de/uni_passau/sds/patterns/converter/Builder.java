package de.uni_passau.sds.patterns.converter;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Objects;

public class Builder {

    public static ParseTree buildParseTree(String input) {
        if (input.contains(">") && !input.contains("(?<")) {
            input = escapeGreaterThan(input); // see javadoc at method declaration
        }
        CharStream inputStream = CharStreams.fromString(input);
        ECMAScriptSyntaxLexer grammarLexer = new ECMAScriptSyntaxLexer(inputStream);
        ECMAScriptSyntaxParser grammarParser = new ECMAScriptSyntaxParser(new CommonTokenStream(grammarLexer));
        ECMAScriptSyntaxParser.ExpressionContext expressionContext = grammarParser.expression();

        if (!expressionContext.getText().equals(input)) {
            throw new ECMAScriptSyntaxException("Syntax error.", ECMAScriptSyntaxException.NONSPEC_ERROR);
        }

        return expressionContext;
    }

    public static ExpressionTree buildExpressionTree(String input) {
        ECMAScriptSyntaxVisitor<ExpressionTree.Node> visitor = new ECMAScriptSyntaxVisitorImpl();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(new ByteArrayOutputStream(0)));
        ParseTree parseTree = buildParseTree(input);
        ExpressionTree.Node root = Objects.requireNonNull(visitor.visit(parseTree));
        System.setErr(originalErr);
        return new ExpressionTree(root);
    }

    /**
     * Converts an input string into a string which escapes all greater-than signs.
     * This method is necessary since the ECMAScriptSyntax grammar has problems recognizing '>'
     * for unexplainable reasons.
     *
     * @param input The string to escape the greater-than signs.
     * @return A string which escapes all greater-than signs.
     */
    private static String escapeGreaterThan(String input) {
        StringBuilder builder = new StringBuilder();
        int last = -1;
        int current = input.indexOf('>');

        while (current != -1) {
            builder.append(input, last + 1, current);
            if (!endsWithOpenEscapeSymbol(input.substring(last + 1, current))) {
                builder.append("\\");
            }
            builder.append(">");
            last = current;
            current = input.indexOf('>', current + 1);
        }
        builder.append(input, last + 1, input.length());
        return builder.toString();
    }

    private static boolean endsWithOpenEscapeSymbol(String str) {
        int count = 0;
        int position = str.length() - 1;
        while (position >= 0 && str.charAt(position) == '\\') {
            count++;
            position--;
        }
        return count % 2 == 1;
    }

}
