package model;

import java.net.URI;
import org.apache.commons.lang3.StringUtils;
import com.google.gson.JsonElement;
import exception.InvalidFragmentException;

/**
 * Stores a Json-Pointer reference.
 * 
 * @author Lukas Ellinger
 */
public class Pointer {
  protected String ref;
  protected boolean guarded;

  public Pointer(URI ref) {
    this.ref = ref.toString();
  }

  public Pointer(URI ref, boolean guarded) {
    this.ref = ref.toString();
    this.guarded = guarded;
  }

  public String getRef() {
    return ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public void setGuarded(boolean guarded) {
    this.guarded = guarded;
  }

  public boolean isGuarded() {
    return guarded;
  }

  /**
   * Returns unescaped version of <code>string</code>.
   * 
   * @param string to be unescaped.
   * @return unescaped version of <code>string</code>.
   */
  public static String getUnescaped(String string) {
    string = string.replace("~1", "/");
    string = string.replace("~0", "~");
    char[] array = string.toCharArray();

    for (int i = 0; i < array.length - 2; i++) {
      if (array[i] == '%' && Character.digit(array[i + 1], 16) != -1
          && Character.digit(array[i + 2], 16) != -1) {
        String escaped = Character.toString(array[i + 1]) + Character.toString(array[i + 2]);
        string =
            string.replace("%" + escaped, String.valueOf((char) Integer.parseInt(escaped, 16)));
        i = i + 2;
      }
    }
    return string;
  }

  /**
   * Returns whether the pointer references the root.
   * 
   * @return <code>true</code>, if root is referenced. <code>false</code> if not.
   */
  public boolean referencesRoot() {
    return ref.equals("#");
  }

  /**
   * Returns whether the pointer references a direct child of "definitions".
   * 
   * @return <code>true</code>, if a direct child of "definitions" is referenced. <code>false</code>
   *         if not.
   */
  public boolean referencesDefChild() {
    return ref.startsWith("#/definitions/") && ref.split("/").length == 3;
  }

  /**
   * Converts a pointer to its name as it should be stored in definitions. Therefore it discards
   * "#/definitions/" if it starts with it. "#/" is always removed at the beginning. If ref is to an
   * id, "id_" is added to the beginning. Afterwards all '/' are replaced with '_'.
   * 
   * @param pointer to be converted.
   * @return name as it should be stored in definitions.
   */
  public String convertPointer() {
    if (ref.equals("#")) {
      return ref;
    } else {
      String convertedRef;
      if (ref.startsWith("#/")) {
        convertedRef = ref.substring(2);
      } else {
        if (ref.startsWith("#")) {
          convertedRef = "id_" + ref.substring(1);
        } else {
          convertedRef = "id_" + ref;
        }
      }
      convertedRef = convertedRef.replace('/', '_');
      convertedRef = convertedRef.replace('#', '_');

      return convertedRef;
    }
  }

  /**
   * Gets the element which is referenced by the stored reference.
   * <code>IllegalFragmentException</code> is thrown if reference is invalid or other documents are
   * referenced.
   * 
   * @param schema in which the referenced element should be found.
   * @return referenced element.
   */
  public JsonElement getRefElement(JsonElement schema) {
    return getRecursivePointerElement(ref, schema);
  }

  /**
   * Recursive help method of <code>getPointerElement()</code>
   */
  protected JsonElement getRecursivePointerElement(String pointer, JsonElement element) {
    if (pointer.equals("#")) {
      return element;
    } else {
      String currentLevel = pointer.substring(2);
      currentLevel = currentLevel.split("/")[0];
      pointer = StringUtils.replaceOnce(pointer, "/" + currentLevel, "");
      currentLevel = getUnescaped(currentLevel);

      JsonElement child;
      if (element.isJsonObject()) {
        if (element.getAsJsonObject().has(currentLevel)) {
          child = element.getAsJsonObject().get(currentLevel);
        } else {
          throw new InvalidFragmentException("No element referenced by " + ref);
        }
      } else if (element.isJsonArray()) {
        try {
          int i = Integer.parseInt(currentLevel);

          if (element.getAsJsonArray().size() <= i) {
            throw new InvalidFragmentException("No element referenced by " + ref);
          }
          child = element.getAsJsonArray().get(i);
        } catch (NumberFormatException e) {
          throw new InvalidFragmentException(ref + " includes array index. But index is no number");
        }
      } else {
        throw new InvalidFragmentException("No element referenced by " + ref);
      }

      return getRecursivePointerElement(pointer, child);
    }
  }

  @Override
  public String toString() {
    return ref + " guarded:" + guarded;
  }
}
