package analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.LoadSchemaDTO;
import model.normalization.Normalizer;
import model.normalization.RepositoryType;
import util.Converter;
import util.Log;
import util.SchemaUtil;
import util.TestObject;

/**
 * Used to normalize and to check whether normalization was correct in the JSON Schema TestSuite.
 * 
 * @author Lukas Ellinger
 */
public class TestSuite extends DirNormalizer {

  /**
   * Normalizes all extracted valid schemas of <code>testSuiteDir</code> and stores them.
   * 
   * @param testSuiteDir directory of TestSuite.
   * @param config of how schemas should be loaded.
   * @throws IOException
   */
  @Override
  public void normalize(File testSuiteDir, LoadSchemaDTO config) throws IOException {
    List<Pair<JsonObject, TestObject[]>> testData = getTestData(testSuiteDir);
    File extractedSchemas = new File("extractedSchemas_" + testSuiteDir.getName());
    extractedSchemas.mkdir();
    extractSchemas(extractedSchemas, testData);

    super.normalize(extractedSchemas, config);
  }

  /**
   * Checks whether the normalization was correct for the JSON Schema TestSuite. Therefore it is
   * checked whether the validity of the tests equals the validity if validated with the normalized
   * schema. If not, then a entry in the log-file is written.
   * 
   * @param testSuiteDir directory in which files of the testsuite are stored.
   * @param config of how schemas should be loaded.
   */
  public void checkForCorrectNormalization(File testSuiteDir, LoadSchemaDTO config) {
    if (!testSuiteDir.isDirectory()) {
      throw new IllegalArgumentException(testSuiteDir.getName() + " needs to be a directory");
    }
    config.setRepType(RepositoryType.TESTSUITE);
    List<Pair<JsonObject, TestObject[]>> schemas = getTestData(testSuiteDir);

    for (Pair<JsonObject, TestObject[]> schema : schemas) {
      File tmp = new File("/tmp/schema.json");

      try {
        FileUtils.writeStringToFile(tmp, new Gson().toJson(schema.getLeft()), "UTF-8");
      } catch (IOException e) {
        Log.severe("Cannot write to tmp file");
      }

      try {
        JSONObject normalizedSchema =
            new JSONObject(new Normalizer(tmp, config).normalize().toString());
        for (TestObject test : schema.getRight()) {
          SchemaLoader loader = SchemaLoader.builder().schemaJson(normalizedSchema).build();

          if (test.isValid() != SchemaUtil.isValid(loader.load().build(), test.getObject())) {
            Log.warn(test.getBelongsTo() + " does not match boolean in valid");
          }
        }
      } catch (Exception e) {
        Log.severe(schema.getRight()[1].getBelongsTo(), e);
      }
    }
  }

  /**
   * Splits files of the testsuite in schemas with their specific test data.
   * 
   * @param testSuiteDir directory in which files of the testsuite are stored.
   * @return schemas with their specific test data.
   */
  private List<Pair<JsonObject, TestObject[]>> getTestData(File testSuiteDir) {
    assert testSuiteDir.isDirectory();

    File[] schemas = testSuiteDir.listFiles();
    List<Pair<JsonObject, TestObject[]>> testData = new ArrayList<>();
    for (File file : schemas) {
      try {
        if (file.isDirectory()) {
          testData.addAll(getTestData(file));
        } else {
          JSONArray array = new JSONArray(FileUtils.readFileToString(file, "UTF-8"));

          for (int i = 0; i < array.length(); i++) {
            JSONObject index = array.getJSONObject(i);
            Object schemaObj = index.get("schema");

            if (schemaObj instanceof JSONObject) {
              JSONObject schema = (JSONObject) schemaObj;
              JSONArray testsArray = index.getJSONArray("tests");
              TestObject[] tests = new TestObject[testsArray.length()];

              for (int j = 0; j < testsArray.length(); j++) {
                JSONObject testJ = testsArray.getJSONObject(j);
                tests[j] = new TestObject(file.getName() + " Schema: " + i + " Test: " + j,
                    testJ.get("data"), testJ.getBoolean("valid"));
              }

              testData.add(new ImmutablePair<>(Converter.toJson(schema), tests));
            } else {
              Log.severe(file.getName() + " Schema:" + i + " has no JSONObject schema");
            }
          }
        }
      } catch (Exception e) {
        Log.severe(file.getName() + " " + e.getMessage());
      }
    }

    return testData;
  }

  /**
   * Stores all schemas of <code>splittedTests</code> in <code>dir</code>.
   * 
   * @param dir location of where to store schemas.
   * @param splittedTests to store the schemas from.
   * @throws IOException if schemas cannot be stored to <code>dir</code>.
   */
  private void extractSchemas(File dir, List<Pair<JsonObject, TestObject[]>> splittedTests)
      throws IOException {
    dir.mkdir();

    for (Pair<JsonObject, TestObject[]> pair : splittedTests) {
      String belongsTo = pair.getRight()[0].getBelongsTo();
      belongsTo = belongsTo.substring(0, belongsTo.indexOf(".json Schema:"))
          + belongsTo.charAt(belongsTo.indexOf("Schema: ") + 8) + ".json";
      File file = new File(dir, belongsTo);
      SchemaUtil.writeJsonToFile(pair.getLeft(), file);
    }
  }
}
