package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons;

/**
 * Eccezione lanciata quando vengono trovati nel documento asserzioni del tipo required(true)
 */

public class UnsenseAssertion extends RuntimeException{
    public UnsenseAssertion() {
    }

    public UnsenseAssertion(String message) {
        super(message);
    }

    public UnsenseAssertion(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsenseAssertion(Throwable cause) {
        super(cause);
    }

    public UnsenseAssertion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
