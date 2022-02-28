package exception;

/**
 * Abstract class for exceptions with references.
 * 
 * @author Lukas Ellinger
 */
public abstract class InvalidReferenceException extends RuntimeException{

  private static final long serialVersionUID = 1L;
  
  public InvalidReferenceException() {
    super();
  }
  
  public InvalidReferenceException(String message) {
    super(message);
  }
}
