package util;

import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Offers conversion from <code>com.google.gson.JsonObject</code> to
 * <code>org.json.JSONObject</code> and vice versa.
 * 
 * @author Lukas Ellinger
 */
public class Converter {

  public static JSONObject toJSON(JsonObject object) {
    return new JSONObject(new Gson().toJson(object));
  }

  public static JsonObject toJson(JSONObject object) {
    return new Gson().fromJson(object.toString(), JsonObject.class);
  }
}
