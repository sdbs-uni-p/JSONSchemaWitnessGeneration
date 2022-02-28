package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions;

public class SyntaxErrorRuntimeException extends RuntimeException{
    public SyntaxErrorRuntimeException() {
    }

    public SyntaxErrorRuntimeException(String message) {
        super(message);
    }

    public SyntaxErrorRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SyntaxErrorRuntimeException(Throwable cause) {
        super(cause);
    }

    public SyntaxErrorRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
