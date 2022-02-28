package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Exceptions;

public class UnsupportedURIRuntimeException extends RuntimeException{
    public UnsupportedURIRuntimeException() {
    }

    public UnsupportedURIRuntimeException(String message) {
        super(message);
    }

    public UnsupportedURIRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedURIRuntimeException(Throwable cause) {
        super(cause);
    }

    public UnsupportedURIRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
