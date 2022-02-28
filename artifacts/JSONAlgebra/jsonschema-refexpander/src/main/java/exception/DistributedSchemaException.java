package exception;

/**
 * Should be thrown if it there is a remote reference when not being allowed.
 * 
 * @author Lukas Ellinger
 */
public class DistributedSchemaException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  
  public DistributedSchemaException() {
    super();
  }
  
  public DistributedSchemaException(String message) {
    super(message);
  }
}
