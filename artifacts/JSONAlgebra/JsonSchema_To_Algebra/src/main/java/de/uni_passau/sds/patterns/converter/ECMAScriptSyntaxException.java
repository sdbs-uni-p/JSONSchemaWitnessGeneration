package de.uni_passau.sds.patterns.converter;

public class ECMAScriptSyntaxException extends RuntimeException {

    private final int type;

    public static int NONSPEC_ERROR = 0;

    public ECMAScriptSyntaxException(String message, int type) {
        super(message);
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
