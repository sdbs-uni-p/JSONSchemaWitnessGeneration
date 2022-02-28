package exception;

/**
 * Is thrown if schema is not stored in store or cannot be loaded because directory "store" or
 * csv-file "UriOfFiles.csv" is missing.
 * 
 * @author Lukas Ellinger
 */
public class StoreException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public StoreException() {
    super();
  }

  public StoreException(String message) {
    super(message);
  }
}
