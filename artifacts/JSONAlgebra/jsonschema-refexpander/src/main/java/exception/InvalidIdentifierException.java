package exception;

/**
 * Should be thrown if there is something wrong with a id or a reference to a id.
 * 
 * @author Lukas Ellinger
 */
public class InvalidIdentifierException extends InvalidReferenceException {

  private static final long serialVersionUID = 1L;
  
  public InvalidIdentifierException() {
    super();
  }
  
  public InvalidIdentifierException(String message) {
    super(message);
  }
}
