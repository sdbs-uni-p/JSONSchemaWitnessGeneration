package de.uni_passau.sds.patterns.converter;

public class ECMAScriptRangeException extends ECMAScriptSyntaxException {

    private final int type;

    public static final int QUANTIFIER_ERROR = 0;
    public static final int CLASS_ERROR = 1;

    public ECMAScriptRangeException(String message, int type) {
        super(message, type);
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
