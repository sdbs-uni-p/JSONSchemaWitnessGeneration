package de.uni_passau.sds.lib.exceptions;

/**
 * Checked exception for when an invalid witness is generated.
 * @author Thomas Pilz
 */
public class InvalidWitnessException extends Exception{

    public InvalidWitnessException(String message, Throwable cause){
        super(message, cause);
    }

    public InvalidWitnessException(String message){
        super(message);
    }

    public InvalidWitnessException(Throwable cause){
        super(cause);
    }

    public InvalidWitnessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
