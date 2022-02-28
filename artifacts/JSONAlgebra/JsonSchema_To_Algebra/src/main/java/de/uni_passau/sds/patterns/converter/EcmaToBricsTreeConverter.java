package de.uni_passau.sds.patterns.converter;

import java.util.Arrays;

public class EcmaToBricsTreeConverter {

    public static ExpressionTree convert(ExpressionTree tree) {
        ExpressionTree.Node root = tree.getRoot();
        ExpressionTree.Node hatRemoved = removeHat(root);
        ExpressionTree.Node dollarRemoved = removeDollar(hatRemoved);
        return new ExpressionTree(dollarRemoved);
    }

    private static ExpressionTree.Node removeHat(ExpressionTree.Node input) {
        ExpressionTree.Node hat = hat(input);
        ExpressionTree.Node noHat = noHat(input);
        ExpressionTree.Node[] c = new ExpressionTree.Node[]{ExpressionTree.AnyString.create(), noHat};
        return ExpressionTree.Disjunction.create(hat, ExpressionTree.Concatenation.create(c));
    }

    private static ExpressionTree.Node hat(ExpressionTree.Node input) {
        if (input == null) { // input is empty language
            return null;
        }
        if (input.isLeaf()) { // input is character (class), anchor, empty word or symbol for any string
            return input instanceof ExpressionTree.Hat ? ExpressionTree.EmptyWord.create() : null;
        }
        if (!(input.containsHat() || input.containsDollar())) { // input does not contain an anchor
            return null;
        }
        if (input instanceof ExpressionTree.Disjunction) {
            ExpressionTree.Node left = hat(((ExpressionTree.Disjunction) input).getLeft());
            ExpressionTree.Node right = hat(((ExpressionTree.Disjunction) input).getRight());
            return ExpressionTree.Disjunction.create(left, right);
        }
        if (input instanceof ExpressionTree.Concatenation) {
            ExpressionTree.Node[] inputs = ((ExpressionTree.Concatenation) input).getInputs();
            int firstNonNullable = getFirstNonNullableIndex(inputs);
            return hatConcatenation(inputs, firstNonNullable);
        }
        if (input instanceof ExpressionTree.Star || input instanceof ExpressionTree.Plus) {
            ExpressionTree.Node inp = ((ExpressionTree.Quantifier) input).getInput();
            return ExpressionTree.Concatenation.create(
                    new ExpressionTree.Node[]{hat(inp), ExpressionTree.Star.create(noHat(inp))});
        }
        if (input instanceof ExpressionTree.QuestionMark) {
            return hat(((ExpressionTree.QuestionMark) input).getInput());
        }
        if (input instanceof ExpressionTree.Range) {
            ExpressionTree.Node inp = ((ExpressionTree.Range) input).getInput();
            ExpressionTree.Node hat = hat(inp);
            ExpressionTree.Node noHat = noHat(inp);
            int max = ((ExpressionTree.Range) input).getMax();
            ExpressionTree.Node[] c = new ExpressionTree.Node[]{hat, max == -1 ?
                    ExpressionTree.Star.create(noHat) : ExpressionTree.Range.create(noHat, 0, max - 1)};
            return ExpressionTree.Concatenation.create(c);
        }
        // should not reach here
        throw new RuntimeException("This kind of node is not supported.");
    }

    private static ExpressionTree.Node hatConcatenation(ExpressionTree.Node[] inputs, int firstNonNullable) {
        int n = inputs.length;
        if (n == 1) {
            return hat(inputs[0]);
        }
        ExpressionTree.Node hatN = hat(inputs[n - 1]);
        ExpressionTree.Node noHatN = noHat(inputs[n - 1]);
        ExpressionTree.Node hatOneToNMinusOne
                = hatConcatenation(Arrays.copyOfRange(inputs, 0, n - 1), firstNonNullable);
        ExpressionTree.Node left = ExpressionTree.Concatenation.create(
                new ExpressionTree.Node[]{hatOneToNMinusOne, ExpressionTree.Disjunction.create(hatN, noHatN)});
        return firstNonNullable >= n - 1 ? ExpressionTree.Disjunction.create(left, hatN) : left;
    }

    private static ExpressionTree.Node noHat(ExpressionTree.Node input) {
        if (input == null) { // input is empty language
            return null;
        }
        if (input.isLeaf()) { // input is character (class), anchor, empty word or symbol for any string
            return input instanceof ExpressionTree.Hat ? null : input;
        }
        if (!input.containsHat()) { // input does not contain hat
            return input;
        }
        if (input instanceof ExpressionTree.Disjunction) {
            ExpressionTree.Node left = noHat(((ExpressionTree.Disjunction) input).getLeft());
            ExpressionTree.Node right = noHat(((ExpressionTree.Disjunction) input).getRight());
            return ExpressionTree.Disjunction.create(left, right);
        }
        if (input instanceof ExpressionTree.Concatenation) {
            ExpressionTree.Node[] inputs = ((ExpressionTree.Concatenation) input).getInputs();
            ExpressionTree.Node[] inputsNoHat = Arrays.stream(inputs).map(EcmaToBricsTreeConverter::noHat)
                    .toArray(ExpressionTree.Node[]::new);
            return ExpressionTree.Concatenation.create(inputsNoHat);
        }
        if (input instanceof ExpressionTree.Star) {
            ExpressionTree.Node inp = ((ExpressionTree.Star) input).getInput();
            return ExpressionTree.Star.create(noHat(inp));
        }
        if (input instanceof ExpressionTree.Plus) {
            ExpressionTree.Node inp = ((ExpressionTree.Plus) input).getInput();
            return ExpressionTree.Plus.create(noHat(inp));
        }
        if (input instanceof ExpressionTree.QuestionMark) {
            ExpressionTree.Node inp = ((ExpressionTree.QuestionMark) input).getInput();
            return ExpressionTree.QuestionMark.create(noHat(inp));
        }
        if (input instanceof ExpressionTree.Range) {
            ExpressionTree.Node inp = ((ExpressionTree.Range) input).getInput();
            int min = ((ExpressionTree.Range) input).getMin();
            int max = ((ExpressionTree.Range) input).getMax();
            return ExpressionTree.Range.create(noHat(inp), min, max);
        }
        // should not reach here
        throw new RuntimeException("This kind of node is not supported.");
    }

    private static ExpressionTree.Node removeDollar(ExpressionTree.Node input)  {
        ExpressionTree.Node dollar = dollar(input);
        ExpressionTree.Node noDollar = noDollar(input);
        ExpressionTree.Node[] c = new ExpressionTree.Node[]{noDollar, ExpressionTree.AnyString.create()};
        return ExpressionTree.Disjunction.create(dollar, ExpressionTree.Concatenation.create(c));
    }

    private static ExpressionTree.Node dollar(ExpressionTree.Node input) {
        if (input == null) { // input is empty language
            return null;
        }
        if (input.isLeaf()) { // input is character (class), anchor, empty word or symbol for any string
            return input instanceof ExpressionTree.Dollar ? ExpressionTree.EmptyWord.create() : null;
        }
        if (!(input.containsHat() || input.containsDollar())) { // input does not contain an anchor
            return null;
        }
        if (input instanceof ExpressionTree.Disjunction) {
            ExpressionTree.Node left = dollar(((ExpressionTree.Disjunction) input).getLeft());
            ExpressionTree.Node right = dollar(((ExpressionTree.Disjunction) input).getRight());
            return ExpressionTree.Disjunction.create(left, right);
        }
        if (input instanceof ExpressionTree.Concatenation) {
            ExpressionTree.Node[] inputs = ((ExpressionTree.Concatenation) input).getInputs();
            return dollarConcatenation(inputs);
        }
        if (input instanceof ExpressionTree.Star || input instanceof ExpressionTree.Plus) {
            ExpressionTree.Node inp = ((ExpressionTree.Quantifier) input).getInput();
            return ExpressionTree.Concatenation.create(
                    new ExpressionTree.Node[]{ExpressionTree.Star.create(noDollar(inp)), dollar(inp)});
        }
        if (input instanceof ExpressionTree.QuestionMark) {
            return dollar(((ExpressionTree.QuestionMark) input).getInput());
        }
        if (input instanceof ExpressionTree.Range) {
            ExpressionTree.Node inp = ((ExpressionTree.Range) input).getInput();
            ExpressionTree.Node dollar = dollar(inp);
            ExpressionTree.Node noDollar = noDollar(inp);
            int max = ((ExpressionTree.Range) input).getMax();
            ExpressionTree.Node[] c = new ExpressionTree.Node[]{max == -1 ? ExpressionTree.Star.create(noDollar)
                    : ExpressionTree.Range.create(noDollar, 0, max - 1), dollar};
            return ExpressionTree.Concatenation.create(c);
        }
        // should not reach here
        throw new RuntimeException("This kind of node is not supported.");
    }

    private static ExpressionTree.Node dollarConcatenation(ExpressionTree.Node[] inputs) {
        int n = inputs.length;
        if (n == 1) {
            return dollar(inputs[0]);
        }
        ExpressionTree.Node inputN  = inputs[n - 1];
        ExpressionTree.Node dollarN = dollar(inputN);
        ExpressionTree.Node[] inputsOneToNMinusOne = Arrays.copyOfRange(inputs, 0, n - 1);
        ExpressionTree.Node dollarOneToNMinusOne = dollarConcatenation(inputsOneToNMinusOne);
        ExpressionTree.Node noDollarOneToNMinusOne = ExpressionTree.Concatenation.create(Arrays
                .stream(inputsOneToNMinusOne).map(EcmaToBricsTreeConverter::noDollar)
                .toArray(ExpressionTree.Node[]::new));
        ExpressionTree.Node right = ExpressionTree.Concatenation.create(new ExpressionTree.Node[]{
                ExpressionTree.Disjunction.create(noDollarOneToNMinusOne, dollarOneToNMinusOne), dollarN});
        return inputN.isNullable() ? ExpressionTree.Disjunction.create(dollarOneToNMinusOne, right) : right;
    }

    private static ExpressionTree.Node noDollar(ExpressionTree.Node input) {
        if (input == null) { // input is empty language
            return null;
        }
        if (input.isLeaf()) { // input is character (class), anchor, empty word or symbol for any string
            return input instanceof ExpressionTree.Dollar ? null : input;
        }
        if (!input.containsDollar()) { // input does not contain dollar
            return input;
        }
        if (input instanceof ExpressionTree.Disjunction) {
            ExpressionTree.Node left = noDollar(((ExpressionTree.Disjunction) input).getLeft());
            ExpressionTree.Node right = noDollar(((ExpressionTree.Disjunction) input).getRight());
            return ExpressionTree.Disjunction.create(left, right);
        }
        if (input instanceof ExpressionTree.Concatenation) {
            ExpressionTree.Node[] inputs = ((ExpressionTree.Concatenation) input).getInputs();
            ExpressionTree.Node[] inputsNoDollar = Arrays.stream(inputs).map(EcmaToBricsTreeConverter::noDollar)
                    .toArray(ExpressionTree.Node[]::new);
            return ExpressionTree.Concatenation.create(inputsNoDollar);
        }
        if (input instanceof ExpressionTree.Star) {
            ExpressionTree.Node inp = ((ExpressionTree.Star) input).getInput();
            return ExpressionTree.Star.create(noDollar(inp));
        }
        if (input instanceof ExpressionTree.Plus) {
            ExpressionTree.Node inp = ((ExpressionTree.Plus) input).getInput();
            return ExpressionTree.Plus.create(noDollar(inp));
        }
        if (input instanceof ExpressionTree.QuestionMark) {
            ExpressionTree.Node inp = ((ExpressionTree.QuestionMark) input).getInput();
            return ExpressionTree.QuestionMark.create(noDollar(inp));
        }
        if (input instanceof ExpressionTree.Range) {
            ExpressionTree.Node inp = ((ExpressionTree.Range) input).getInput();
            int min = ((ExpressionTree.Range) input).getMin();
            int max = ((ExpressionTree.Range) input).getMax();
            return ExpressionTree.Range.create(noDollar(inp), min, max);
        }
        // should not reach here
        throw new RuntimeException("This kind of node is not supported.");
    }

    /**
     * Determines the first node of an array, which is not nullable.
     * If the result is n, then the concatenation of inputs[0],...,inputs[m] for all m less than n is nullable.
     *
     * @param inputs The array to determine its first non-nullable node.
     * @return The index of the first non-nullable node.
     */
    private static int getFirstNonNullableIndex(ExpressionTree.Node[] inputs) {
        int index = 0;
        for (ExpressionTree.Node input : inputs) {
            if (!input.isNullable()) {
                return index;
            }
            index++;
        }
        return index;
    }

}
