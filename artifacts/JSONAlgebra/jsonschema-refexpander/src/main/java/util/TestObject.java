package util;

/**
 * Used for storing a test with its validity of the JSON Schema TestSuite.
 * 
 * @author Lukas Ellinger
 */
public class TestObject {
  // additional text to identify test
  private String belongsTo;
  private Object object;
  private boolean valid;

  public TestObject(String belongsTo, Object object, boolean valid) {
    this.belongsTo = belongsTo;
    this.object = object;
    this.valid = valid;
  }

  public boolean isValid() {
    return valid;
  }

  public Object getObject() {
    return object;
  }

  public String getBelongsTo() {
    return belongsTo;
  }
}
