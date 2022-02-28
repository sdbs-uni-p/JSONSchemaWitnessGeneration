package exception;

/**
 * Should be thrown if there is something wrong with a fragment of a reference.
 * 
 * @author Lukas Ellinger
 */
public class InvalidFragmentException extends InvalidReferenceException{

  private static final long serialVersionUID = 1L;
  
  public InvalidFragmentException() {
    super();
  }
  
  public InvalidFragmentException(String message) {
    super(message);
  }
}
