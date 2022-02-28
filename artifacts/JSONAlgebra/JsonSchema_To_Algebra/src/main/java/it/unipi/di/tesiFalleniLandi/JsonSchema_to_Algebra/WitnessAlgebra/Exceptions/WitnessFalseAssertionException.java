package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions;

public class WitnessFalseAssertionException extends WitnessException {
    public WitnessFalseAssertionException() {
    }

    public WitnessFalseAssertionException(String message) {
        super(message);
    }

    public WitnessFalseAssertionException(String message, Throwable cause) {
        super(message, cause);
    }

    public WitnessFalseAssertionException(Throwable cause) {
        super(cause);
    }

    public WitnessFalseAssertionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
