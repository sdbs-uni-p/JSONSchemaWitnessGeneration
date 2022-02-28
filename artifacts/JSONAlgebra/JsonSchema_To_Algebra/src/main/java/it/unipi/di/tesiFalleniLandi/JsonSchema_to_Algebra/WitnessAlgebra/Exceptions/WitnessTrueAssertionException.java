package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions;

public class WitnessTrueAssertionException extends WitnessException {
    public WitnessTrueAssertionException() {
    }

    public WitnessTrueAssertionException(String message) {
        super(message);
    }

    public WitnessTrueAssertionException(String message, Throwable cause) {
        super(message, cause);
    }

    public WitnessTrueAssertionException(Throwable cause) {
        super(cause);
    }

    public WitnessTrueAssertionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
