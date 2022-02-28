package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions;

public class WitnessBDDException extends WitnessException {
    public WitnessBDDException() {
    }

    public WitnessBDDException(String message) {
        super(message);
    }

    public WitnessBDDException(String message, Throwable cause) {
        super(message, cause);
    }

    public WitnessBDDException(Throwable cause) {
        super(cause);
    }

    public WitnessBDDException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
