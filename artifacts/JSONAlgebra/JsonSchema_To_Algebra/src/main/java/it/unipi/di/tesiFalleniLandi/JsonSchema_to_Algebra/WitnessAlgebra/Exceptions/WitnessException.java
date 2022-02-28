package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions;

public class WitnessException extends Exception{
    public WitnessException() {
    }

    public WitnessException(String message) {
        super(message);
    }

    public WitnessException(String message, Throwable cause) {
        super(message, cause);
    }

    public WitnessException(Throwable cause) {
        super(cause);
    }

    public WitnessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
