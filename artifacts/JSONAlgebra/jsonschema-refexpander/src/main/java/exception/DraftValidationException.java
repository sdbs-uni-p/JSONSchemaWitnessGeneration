package exception;

/**
 * Should be thrown if a schema does not pass draft validation.
 * 
 * @author Lukas Ellinger
 */
public class DraftValidationException extends RuntimeException {
  
  private static final long serialVersionUID = 1L;

  public DraftValidationException() {
    super();
  }

  public DraftValidationException(String message) {
    super(message);
  }
}
