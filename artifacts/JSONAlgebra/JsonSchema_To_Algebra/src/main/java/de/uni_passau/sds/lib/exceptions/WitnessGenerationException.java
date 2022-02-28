package de.uni_passau.sds.lib.exceptions;

/**
 * Checked exception for when an exception occurs during witness generation.
 * Serves as a general exception for any (of the many) possible exceptions when generating a witness.
 * @author Thomas Pilz
 */
public class WitnessGenerationException extends Exception{
    public WitnessGenerationException(String message, Throwable cause){
        super(message, cause);
    }

    public WitnessGenerationException(String message){
        super(message);
    }

    public WitnessGenerationException(Throwable cause){
        super(cause);
    }

    public WitnessGenerationException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(message, cause, enableSuppression, writableStackTrace);
    }
}