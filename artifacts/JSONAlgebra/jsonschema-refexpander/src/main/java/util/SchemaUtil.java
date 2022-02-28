package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dto.LoadSchemaDTO;
import exception.DraftValidationException;
import model.Draft;
import model.normalization.Normalizer;

/**
 * Offers utils for JSON Schemas.
 * 
 * @author Lukas Ellinger
 */
public class SchemaUtil {

  /**
   * Checks whether a object is valid to a schema. <code>org.json.</code> is used.
   * 
   * @param schema to validate testData. Has to be valid to Draft04, 06 or 07.
   * @param testData to be validated.
   * @return <code>true</code>, if it is valid. <code>false</code>, if not.
   */
  public static boolean isValid(Schema schema, Object testData) {
    try {
      schema.validate(testData);
      return true;
    } catch (ValidationException e) {
      return false;
    }
  }

  /**
   * {@link #isValidToDraft(JSONObject)}
   * 
   * @param file to be checked.
   * @return <code>true</code>, if it is valid to the draft. <code>false</code> if not.
   */
  public static boolean isValidToDraft(File file) {
    try {
      JSONObject schema = new JSONObject(FileUtils.readFileToString(file, "UTF-8"));
      return isValidToDraft(schema);
    } catch (Exception e) {
      Log.severe(file, e);
      return false;
    }
  }

  /**
   * {@link #isValidToDraft(JSONObject)}
   * 
   * @param schema to be checked.
   * @return <code>true</code>, if <code>schema</code> is valid to the draft. <code>false</code> if
   *         not.
   * @throws IOException
   */
  public static boolean isValidToDraft(JsonObject schema) throws IOException {
    return isValidToDraft(Converter.toJSON(schema));
  }

  /**
   * Checks whether a schema is valid to its specified draft. If no draft was specified or does not
   * equal draft04, draft06 or draft07, draft04 will be used for verification.
   * 
   * @param schema to be checked.
   * @return <code>true</code>, if <code>schema</code> is valid to the draft. <code>false</code> if
   *         not.
   * @throws IOException
   */
  public static boolean isValidToDraft(JSONObject schema) throws IOException {
    int validationDraftNumber = getValidationDraftNumber(schema);

    try (InputStream draftStream = SchemaUtil.class.getClassLoader()
        .getResourceAsStream("drafts/draft" + validationDraftNumber + ".json")) {
      JSONObject draft = new JSONObject(IOUtils.toString(draftStream, "UTF-8"));
      SchemaLoader.load(draft).validate(schema);
      return true;
    } catch (ValidationException e) {
      return false;
    }
  }

  /**
   * Currently in conflict with other dependencies. Not usable at the moment!
   *
   * Checks whether <code>file</code> contains a valid JSON schema. Uses Draft04 as default. Draft06
   * and Draft07 is also supported if they are used in "$schema".
   * 
   * @param file to be checked.
   * @return <code>true</code>, if it contains a valid JSON schema. <code>false</code>, if not.
   * @throws IOException if file cannot be loaded or does not conatain valid JSON.
   */
  /*public static boolean isValidSchema(File file) throws IOException {
    JSONObject fileObject = new JSONObject(FileUtils.readFileToString(file, "UTF-8"));

    try {
      SchemaLoader.load(fileObject);
      return true;
    } catch (ValidationException | JSONException e) {
      return false;
    }
  }
   */

  /**
   * Gets the id of <code>object</code>. If there is no id then an <code>URI</code> with an empty
   * string is returned.<code>com.google.gson.JsonObject</code> is used.
   * 
   * @param object to get the id from.
   * @return id of <code>object</code>. If there is no id then <code>URI</code> with an empty
   *         string.
   * @throws URISyntaxException if id is no valid <code>URI</code>.
   */
  public static URI getId(JsonObject object, Draft draft) throws URISyntaxException {
    if (draft.equals(Draft.Draft4)) {
      if (object.has("id") && object.get("id").isJsonPrimitive()) {
        return URIUtil.toURI(object.get("id").getAsString());
      }
    } else if (draft.equals(Draft.DraftHigher)) {
      if (object.has("$id") && object.get("$id").isJsonPrimitive()) {
        return URIUtil.toURI(object.get("$id").getAsString());
      }
    }

    return new URI("");
  }

  /**
   * Gets its draft to be used.
   * 
   * @param object to get draft from.
   * @return <code>Draft4</code>, if "id" should be used. <code>DraftHigher</code>, if "$id" should
   *         be used.
   */
  public static Draft getDraft(JsonObject object) {
    if (getValidationDraftNumber(object) == 4) {
      return Draft.Draft4;
    } else {
      return Draft.DraftHigher;
    }
  }

  /**
   * Gets the specified draft of <code>object</code>. <code>org.json.JSONObject</code> is used.
   * 
   * @param object of which the draft should be returned.
   * @return specified draft of <code>object</code>. <code>null</code>, if there is no schema
   *         specified.
   */
  public static String getDraftString(JSONObject object) {
    if (object.has("$schema")) {
      return object.get("$schema").toString();
    } else {
      return null;
    }
  }

  /**
   * Gets draft validation number of <code>object</code>. Returns 7 for draft07, 6 for draft06 and 4
   * for draft04. If no or no supported draft was specified with "$schema", it will be checked
   * whether "id" or "$id" is used. If it is "$id" then 6 is returned. As default 4 is returned.
   * 
   * @param object to get the draft version number from.
   * @return Returns 7 for draft07, 6 for draft06 and 4 for draft04. If no or no supported draft was
   *         specified with "$schema", it will be checked whether "id" or "$id" is used. If it is
   *         "$id" then 6 is returned. As default 4 is returned.
   */
  public static int getValidationDraftNumber(JsonObject object) {
    if (object.has("$schema")) {
      String draft = object.get("$schema").getAsString();

      if (draft.contains("draft-07")) {
        return 7;
      } else if (draft.contains("draft-06")) {
        return 6;
      } else if (draft.contains("draft-04") || draft.contains("draft-03")) {
        return 4;
      }
    }

    return getDraftOfIdKeyword(object);
  }

  private static int getDraftOfIdKeyword(JsonElement element) {
    if (element.isJsonObject()) {
      return getRecursiveDraftOfIdKeyword(element.getAsJsonObject());
    } else if (element.isJsonArray()) {
      return getRecursiveDraftOfIdKeyword(element.getAsJsonArray());
    } else {
      return 4;
    }
  }

  private static int getRecursiveDraftOfIdKeyword(JsonObject object) {
    if (object.has("$id")) {
      return 6;
    } else {
      for (Entry<String, JsonElement> entry : object.entrySet()) {
        if (!entry.getKey().equals("enum")) {
          int draft = getDraftOfIdKeyword(entry.getValue());
          if (draft == 6) {
            return draft;
          }
        }
      }

      return 4;
    }
  }

  private static int getRecursiveDraftOfIdKeyword(JsonArray array) {
    for (JsonElement element : array) {
      int draft = getDraftOfIdKeyword(element);
      if (draft == 6) {
        return draft;
      }
    }
    return 4;
  }

  /**
   * {@link #getValidationDraftNumber(JsonObject)}
   */
  public static int getValidationDraftNumber(JSONObject object) {
    return getValidationDraftNumber(Converter.toJson(object));
  }

  /**
   * Deletes all schemas which are not valid for draft0<code>i</code>. Draft04, 06 and 07 are
   * supported. If a file does not contain a valid <code>JSONObject</code>, it is deleted, too.
   * 
   * @param dir directory in which the schemas are.
   * @param i draftNumber. 4, 6 and 7 are supported.
   * @throws IOException if draftfile cannot be loaded.
   */
  public static void deleteInvalidSchemasForDraft(File dir, int i) throws IOException {
    JSONObject draft = (JSONObject) new JSONTokener(
        SchemaUtil.class.getClassLoader().getResourceAsStream("drafts/draft" + i + ".json"))
            .nextValue();
    for (File file : dir.listFiles()) {
      JSONObject obj;
      try {
        obj = (JSONObject) new JSONTokener(FileUtils.readFileToString(file, "UTF-8")).nextValue();

        if (!isValid(SchemaLoader.load(draft), obj)) {
          file.delete();
        }
      } catch (JSONException e) {
        file.delete();
      }
    }
  }

  /**
   * Removes the id in <code>element</code>. <code>com.google.gson.JsonObject</code> is used.
   * 
   * @param element in which id should be removed.
   */
  public static void removeIdInElement(JsonElement element) {
    if (element.isJsonObject()) {
      JsonObject object = element.getAsJsonObject();
      if (object.has("$id") && object.get("$id").isJsonPrimitive()) {
        try {
          object.get("$id").getAsString();
          object.remove("$id");
        } catch (ClassCastException e) {
        }
      } else if (object.has("id") && object.get("id").isJsonPrimitive()) {
        try {
          object.get("id").getAsString();
          object.remove("id");
        } catch (ClassCastException e) {
        }
      }
    }
  }

  /**
   * Removes all ids in subschemas of <code>object</code>. <code>com.google.gson.JsonObject</code>
   * is used.
   * 
   * @param object in which the ids in subschemas should be removed.
   */
  public static void removeIds(JsonObject object) {
    for (Entry<String, JsonElement> entry : object.entrySet()) {
      if (!entry.getKey().equals("enum")) {
        removeRecursiveIds(entry.getValue());
      }
    }
  }

  private static void removeRecursiveIds(JsonElement element) {
    if (element.isJsonObject()) {
      removeRecursiveIds(element.getAsJsonObject());
    } else if (element.isJsonArray()) {
      removeRecursiveIds(element.getAsJsonArray());
    }
  }

  private static void removeRecursiveIds(JsonObject object) {
    removeIdInElement(object);
    for (Entry<String, JsonElement> entry : object.entrySet()) {
      if (!entry.getKey().equals("enum")) {
        removeRecursiveIds(entry.getValue());
      }
    }
  }

  private static void removeRecursiveIds(JsonArray array) {
    for (JsonElement element : array) {
      removeRecursiveIds(element);
    }
  }

  /**
   * Deletes all schemas which are not using <code>draft</code>. This simply checks whether
   * <code>"$schema"</code> contains <code>draft</code>. If a file does not contain a valid
   * <code>JSONObject</code>, it is deleted, too.
   * 
   * @param dir directory in which the schemas are.
   * @param draft of which schemas should be kept.
   * @throws IOException
   */
  public static void deleteSchemasNotUsingDraft(File dir, String draft) throws IOException {
    for (File file : dir.listFiles()) {
      JSONObject obj;
      try {
        obj = (JSONObject) new JSONTokener(FileUtils.readFileToString(file, "UTF-8")).nextValue();

        if (getDraftString(obj).contains(draft)) {
          file.delete();
        }
      } catch (JSONException e) {
        file.delete();
      }
    }
  }

  /**
   * Stores <code>element</code> in <code>file</code>.
   * 
   * @param element to be stored.
   * @param file to store <code>element</code> in.
   * @throws IOException
   */
  public static void writeJsonToFile(JsonElement element, File file) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    FileUtils.writeStringToFile(file, gson.toJson(element), "UTF-8");
  }

  /**
   * Normalizes the schema in <code>unnormalized</code> and stores it under the directory
   * <code>store</code>.
   * 
   * @param unnormalized file of schema to be normalized.
   * @param uri base uri of schema. If <code>null</code>, <code>URI</code> of
   *        <code>unnormalized</code> is used.
   * @param store to store normalized schema.
   * @param csvLineage csv-file of where to store lineage of normalized schema.
   * @param config of how schema should be loaded.
   * @return normalized schema.
   * @throws IOException
   */
  public static JsonObject normalize(File unnormalized, URI uri, File store, File csvLineage,
      LoadSchemaDTO config) throws IOException {
    Normalizer normalizer;
    if (uri != null) {
      normalizer = new Normalizer(unnormalized, uri, config);
    } else {
      uri = unnormalized.toURI();
      normalizer = new Normalizer(unnormalized, config);
    }

    File normalizedFile = new File(store, getNormalizedFileName(unnormalized.getName()));
    JsonObject normalizedSchema = normalizer.normalize();

    if (isValidToDraft(normalizedSchema)) {
      Set<String> loadedFiles = normalizer.getRootSchema().getLoadedFiles();
      String[] csvEntry = {normalizedFile.getName(), uri.toString(), loadedFiles.toString()};
      CSVUtil.writeToCSV(csvLineage, csvEntry);
      writeJsonToFile(normalizedSchema, normalizedFile);
      return normalizedSchema;
    } else {
      throw new DraftValidationException(
          "Normalized schema of " + unnormalized.getName() + " is not valid to draft");
    }
  }

  /**
   * Gets the filename of the normalized schema.
   * 
   * @param unnormalizedFileName to get normalized filename of.
   * @return normalized filename.
   */
  public static String getNormalizedFileName(String unnormalizedFileName) {
    int lastOccurence = unnormalizedFileName.lastIndexOf(".json");

    if (lastOccurence != -1) {
      unnormalizedFileName = unnormalizedFileName.substring(0, lastOccurence);
      return unnormalizedFileName + "_Normalized.json";
    } else {
      throw new IllegalArgumentException(unnormalizedFileName + " does not equal normal filename");
    }
  }

  /**
   * Gets the definitions object of <code>schema</code>. If there is no definitions object, then an
   * empty <code>JsonObject</code> gets returned.
   * 
   * @param schema of which definitions should be fetched.
   * @return definitions object of <code>schema</code>.
   */
  public static JsonObject getDefinitions(JsonObject schema) {
    JsonObject defs;

    if (schema.get("definitions") != null) {
      defs = schema.get("definitions").getAsJsonObject();
    } else {
      defs = new JsonObject();
    }

    return defs;
  }
}
