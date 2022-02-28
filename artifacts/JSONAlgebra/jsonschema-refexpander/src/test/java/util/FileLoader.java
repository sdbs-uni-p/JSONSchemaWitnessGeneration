package util;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import org.apache.commons.io.FileUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Offers utils to load a schema.
 * 
 * @author Lukas Ellinger
 */
public class FileLoader {

  /**
   * Loads the stored schema at <code>path</code>.
   * 
   * @param path to the schema to be loaded..
   * @return loaded schema.
   * @throws IOException if file could not be loaded.
   */
  public static JsonObject loadSchema(String path) throws IOException {
    return new Gson().fromJson(FileUtils.readFileToString(new File(path), "UTF-8"),
        JsonObject.class);
  }

  /**
   * Loads the stored normalized schema with number i.
   * 
   * @param schemas default path to schemas with placeholder. Should look like
   *        "path/{1}testSchema{0}.json".
   * @param i number of normalized schema to be loaded.
   * @return loaded schema.
   * @throws IOException if file could not be loaded.
   */
  public static JsonObject getStoredNormalizedSchema(String schemas, int i) throws IOException {
    return loadSchema(MessageFormat.format(schemas, i, "Normalized_"));
  }
}
