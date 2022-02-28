package de.uni_passau.sds.patterns.converter;

public class ECMAScriptNotSupportedException extends RuntimeException {

    private final int type;

    public static final int LOOKAROUND_ERROR = 0;
    public static final int NOTSUPP_ERROR = 1;

    public ECMAScriptNotSupportedException(String message, int type) {
        super(message);
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
