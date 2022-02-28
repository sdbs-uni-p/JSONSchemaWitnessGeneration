package de.uni_passau.sds.patterns.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ExpressionTree {

    private final Node root;

    public ExpressionTree(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }

    public String convertToString() {
        return root == null ? "#" : root.toString();
    }

    public static class Node {
        private final boolean isLeaf;

        private Node(boolean isLeaf) {
            this.isLeaf = isLeaf;
        }

        public boolean isLeaf() {
            return isLeaf;
        }

        public boolean containsHat() {
            if (isLeaf) {
                return this instanceof Hat;
            }
            if (this instanceof Quantifier) {
                return ((Quantifier) this).input.containsHat();
            }
            if (this instanceof Disjunction) {
                return ((Disjunction) this).left.containsHat() || ((Disjunction) this).right.containsHat();
            }
            if (this instanceof Concatenation) {
                for (Node input : ((Concatenation) this).inputs) {
                    if (input.containsHat()) {
                        return true;
                    }
                }
                return false;
            }
            // should not reach here
            throw new RuntimeException("This kind of node is not supported.");
        }

        public boolean isNullable() {
            if (isLeaf) { // node is character (class), anchor, empty word or symbol for any string
                return this instanceof EmptyWord || this instanceof AnyString
                        || this instanceof Hat || this instanceof Dollar;
            }
            if (this instanceof Star || this instanceof QuestionMark) {
                return true;
            }
            if (this instanceof Plus) {
                return ((Plus) this).getInput().isNullable();
            }
            if (this instanceof Range) {
                return ((Range) this).getMin() == 0
                        || ((Range) this).getInput().isNullable();
            }
            if (this instanceof Disjunction) {
                return ((Disjunction) this).getLeft().isNullable()
                        || ((Disjunction) this).getRight().isNullable();
            }
            if (this instanceof Concatenation) {
                for (Node input : ((Concatenation) this).getInputs()) {
                    if (!input.isNullable()) {
                        return false;
                    }
                }
                return true;
            }
            // should not reach here
            throw new RuntimeException("This kind of node is not supported.");
        }

        public boolean containsDollar() {
            if (isLeaf) {
                return this instanceof Dollar;
            }
            if (this instanceof Quantifier) {
                return ((Quantifier) this).input.containsDollar();
            }
            if (this instanceof Disjunction) {
                if (((Disjunction) this).left.containsDollar()) {
                    return true;
                }
                return ((Disjunction) this).right.containsDollar();
            }
            if (this instanceof Concatenation) {
                for (Node input : ((Concatenation) this).inputs) {
                    if (input.containsDollar()) {
                        return true;
                    }
                }
                return false;
            }
            // should not reach here
            throw new RuntimeException("This kind of node is not supported.");
        }

    }

    public static class EmptyWord extends Node {
        private EmptyWord() {
            super(true);
        }

        public static Node create() {
            return new EmptyWord();
        }

        @Override
        public String toString() {
            return "()";
        }
    }

    public static class Disjunction extends Node {
        private final Node left;
        private final Node right;

        private Disjunction(Node left, Node right) {
            super(false);
            this.left = left;
            this.right = right;
        }

        public static Node create(Node left, Node right) {
            return simplified(left, right);
        }

        private static Node simplified(Node left, Node right) {
            if (left == null) {
                return right;
            }
            if (right == null) {
                return left;
            }
            if (left instanceof EmptyWord) {
                return QuestionMark.create(right);
            }
            if (right instanceof EmptyWord) {
                return QuestionMark.create(left);
            }
            return new Disjunction(left, right);
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        @Override
        public String toString() {
            return left.toString() + "|" + right.toString();
        }
    }

    public static class Quantifier extends Node {
        private final Node input;
        private final String symbol;

        private Quantifier(Node input, String symbol) {
            super(false);
            this.input = input;
            this.symbol = symbol;
        }

        public Node getInput() {
            return input;
        }

        @Override
        public String toString() {
            return input.isLeaf ? input + symbol : "(" + input + ")" + symbol;
        }
    }

    public static class Star extends Quantifier {
        private Star(Node input) {
            super(input, "*");
        }

        public static Node create(Node input) {
            return simplified(input);
        }

        private static Node simplified(Node input) {
            if (input == null || input instanceof EmptyWord) {
                return EmptyWord.create();
            }
            return new Star(input);
        }
    }

    public static class Plus extends Quantifier {
        private Plus(Node input) {
            super(input, "+");
        }

        public static Node create(Node input) {
            return simplified(input);
        }

        private static Node simplified(Node input) {
            if (input == null) {
                return null;
            }
            if (input instanceof EmptyWord) {
                return EmptyWord.create();
            }
            return new Plus(input);
        }
    }

    public static class QuestionMark extends Quantifier {
        private QuestionMark(Node input) {
            super(input, "?");
        }

        public static Node create(Node input) {
            return simplified(input);
        }

        private static Node simplified(Node input) {
            if (input == null || input instanceof EmptyWord) {
                return EmptyWord.create();
            }
            return new QuestionMark(input);
        }
    }

    public static class Range extends Quantifier {
        private final int min;
        private final int max;

        private Range(Node input, String symbol, int min, int max) {
            super(input, symbol);
            this.min = min;
            this.max = max;
        }

        public static Node create(Node input, int min, int max) {
            if (max == -1) {
                return create(input, min, false);
            }
            if (min > max || min < 0) {
                throw new ECMAScriptRangeException("Illegal quantifier range.", ECMAScriptRangeException.QUANTIFIER_ERROR);
            }
            if (min == max) {
                return create(input, min, true);
            }
            return simplified(input, "{" + min + "," + max + "}", min, max);
        }

        public static Node create(Node input, int quantity, boolean exact) {
            if (quantity < 0) {
                throw new ECMAScriptRangeException("Illegal quantifier range.", ECMAScriptRangeException.QUANTIFIER_ERROR);
            }
            return simplified(input, "{" + quantity + (exact ? "" : ",") + "}", quantity, exact ? quantity : -1);
        }

        private static Node simplified(Node input, String symbol, int min, int max) {
            if (input == null) {
                return min == 0 ? EmptyWord.create() : null;
            }
            if (input instanceof EmptyWord || max == 0) {
                return EmptyWord.create();
            }
            if (min == 0) {
                if (max == -1) {
                    return Star.create(input);
                }
                if (max == 1) {
                    return QuestionMark.create(input);
                }
            }
            if (min == 1) {
                if (max == -1) {
                    return Plus.create(input);
                }
                if (max == 1) {
                    return input;
                }
            }
            return new Range(input, symbol, min, max);
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }
    }

    public static class Concatenation extends Node {
        private final Node[] inputs;

        private Concatenation(Node[] inputs) {
            super(false);
            for (Node input : inputs) {
                if (input instanceof Concatenation) {
                    this.inputs = flatten(inputs);
                    return;
                }
            }
            this.inputs = inputs;
        }

        public static Node create(Node[] inputs) {
            return simplified(inputs);
        }

        private static Node simplified(Node[] inputs) {
            for (Node input : inputs) {
                if (input == null) {
                    return null;
                }
            }
            // filter out empty words
            inputs = Arrays.stream(inputs).filter(inp -> !(inp instanceof EmptyWord)).toArray(Node[]::new);
            if (inputs.length == 0) {
                return EmptyWord.create();
            }
            if (inputs.length == 1) {
                return inputs[0];
            }
            return new Concatenation(inputs);
        }

        public Node[] getInputs() {
            return inputs;
        }

        private Node[] flatten(Node[] inputs) {
            List<Node> adjustedInputs = new ArrayList<>();
            for (Node node : inputs) {
                if (node instanceof  Concatenation) {
                    adjustedInputs.addAll(Arrays.asList(((Concatenation) node).getInputs()));
                } else {
                    adjustedInputs.add(node);
                }
            }
            return adjustedInputs.toArray(Node[]::new);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (Node input : inputs) {
                builder.append(input.isLeaf || input instanceof Quantifier ? input : "(" + input + ")");
            }
            return builder.toString();
        }
    }

    public static class CharClass extends Node {
        private final boolean positive;
        private final List<CharClassNode> charClassNodes = new LinkedList<>();

        private CharClass(boolean positive, CharClassNode... charClassNodes) {
            super(true);
            this.positive = positive;

            for (CharClassNode node : charClassNodes) {
                if (node instanceof CharRange && ((CharRange) node).getBegin() > ((CharRange) node).getEnd()) {
                    throw new ECMAScriptRangeException("Illegal character range.", ECMAScriptRangeException.CLASS_ERROR);
                }
                this.charClassNodes.add(node);
            }
        }

        public static Node create(boolean positive, CharClassNode... charClassNodes) {
            if (charClassNodes.length == 0) { // no included or excluded character in class
                return positive ? null : AnyCharacter.create();
            }
            return new CharClass(positive, charClassNodes);
        }

        public List<CharClassNode> getCharClassNodes() {
            return new LinkedList<>(charClassNodes);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("[");
            if (!positive) {
                builder.append("^");
            }

            for (CharClassNode node : charClassNodes) {
                builder.append(node.toString());
            }

            builder.append("]");
            return builder.toString();
        }

        public static class CharClassNode {
            public static CharClassNode create(char c) {
                return new CharInClass(c);
            }
            public static CharClassNode create(char begin, char end) {
                return new CharRange(begin, end);
            }
        }

        private static class CharInClass extends CharClassNode {
            private final char c;

            private CharInClass(char c) {
                super();
                this.c = c;
            }

            @Override
            public String toString() {
                return escapeIfNecessary(c);
            }
        }

        private static class CharRange extends CharClassNode {
            private final char begin;
            private final char end;

            private CharRange(char begin, char end) {
                super();
                this.begin = begin;
                this.end = end;
            }

            public char getBegin() {
                return begin;
            }

            public char getEnd() {
                return end;
            }

            @Override
            public String toString() {
                return escapeIfNecessary(begin) + "-" + escapeIfNecessary(end);
            }
        }

        private static String escapeIfNecessary(char c) {
            switch (c) {
                case ']':
                case '\\':
                case '^':
                case '-':
                case '"':
                    return escape(c);
            }
            return String.valueOf(c);
        }
    }

    public static class AnyCharacter extends CharClass {
        private AnyCharacter() {
            super(false);
        }

        public static Node create() {
            return new AnyCharacter();
        }

        @Override
        public String toString() {
            return ".";
        }
    }

    public static class CharTerminal extends Node {
        private final char c;

        private CharTerminal(char c) {
            super(true);
            this.c = c;
        }

        public static Node create(char c) {
            return new CharTerminal(c);
        }

        public char getC() {
            return c;
        }

        @Override
        public String toString() {
            switch (c) {
                case '*':
                case '+':
                case '#':
                case '.':
                case '|':
                case '(':
                case ')':
                case '\\':
                case '[':
                case '?':
                case '{':
                case '&':
                case '~':
                case '<':
                case '"':
                    return escape(c);
                case '@':
                    if (!(this instanceof AnyString)) {
                        return escape(c);
                    }
            }
            return String.valueOf(c);
        }
    }

    private static String escape(char c) {
        return "\\" + c;
    }

    public static class Hat extends CharTerminal {
        private Hat() {
            super('^');
        }

        public static Node create() {
            return new Hat();
        }
    }

    public static class Dollar extends CharTerminal {
        private Dollar() {
            super('$');
        }

        public static Node create() {
            return new Dollar();
        }
    }

    public static class AnyString extends CharTerminal {
        private AnyString() {
            super('@');
        }

        public static Node create() {
            return new AnyString();
        }
    }

}
