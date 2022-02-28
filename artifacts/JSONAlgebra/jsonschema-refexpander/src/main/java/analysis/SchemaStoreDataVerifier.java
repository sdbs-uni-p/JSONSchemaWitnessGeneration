package analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import dto.LoadSchemaDTO;
import model.normalization.Normalizer;
import model.normalization.RepositoryType;
import util.Converter;
import util.Log;
import util.SchemaUtil;

/**
 * Used to verify normalization
 * 
 * @author Lukas Ellinger
 */
public class SchemaStoreDataVerifier {
  /**
   * Checks whether the normalization was correct for the schemaStore. Therefore it is checked
   * whether the test data for a schema is still valid for the normalized schema. Or if not, if it
   * is not valid for the normalized schema too. If it is not equal, then there is a entry written
   * in the log-file.
   * 
   * @param testDataDir directory of test data.
   * @param SchemaDir directory of schemas.
   * @param config of how schemas should be loaded.
   */
  public static void checkForCorrectNormalization(File testDataDir, File schemaDir,
      LoadSchemaDTO config) {
    if (!testDataDir.isDirectory() || !schemaDir.isDirectory()) {
      throw new IllegalArgumentException(
          testDataDir.getName() + " and " + schemaDir.getName() + " need to be directories");
    }
    config.setRepType(RepositoryType.NORMAL);
    List<Pair<File, File[]>> schemas = getTestDataFiles(testDataDir, schemaDir);
    for (Pair<File, File[]> schema : schemas) {
      File schemaFile = schema.getLeft();
      try {
        JSONObject normalizedSchema =
            Converter.toJSON(new Normalizer(schemaFile, config).normalize());
        JSONObject unnormalizedSchema =
            new JSONObject(FileUtils.readFileToString(schemaFile, "UTF-8"));

        updateSchema(normalizedSchema, unnormalizedSchema);

        for (File file : schema.getRight()) {
          Object testData = new JSONTokener(FileUtils.readFileToString(file, "UTF-8")).nextValue();

          if (SchemaUtil.isValid(SchemaLoader.load(unnormalizedSchema), testData) != SchemaUtil
              .isValid(SchemaLoader.load(normalizedSchema), testData)) {

            Log.warn(schemaFile.getName() + " and its normalization do not match on accepted data");
          }
        }
      } catch (Exception e) {
        Log.severe(schemaFile, e);
      }
    }
  }

  /**
   * Changes "$schema" to desired draft in both files. Therefore it is checked which draft should be
   * used for <code>one</code>.
   * 
   * @param one
   * @param another
   */
  private static void updateSchema(JSONObject one, JSONObject another) {
    if (one.has("$schema") && (SchemaUtil.getValidationDraftNumber(one) == 6)) {
      one.put("$schema", "http://json-schema.org/draft-06/schema#");
      another.put("$schema", "http://json-schema.org/draft-06/schema#");
    }

    if (one.has("$schema") && (SchemaUtil.getValidationDraftNumber(one) == 4)) {
      one.put("$schema", "http://json-schema.org/draft-04/schema#");
      another.put("$schema", "http://json-schema.org/draft-04/schema#");
    }
  }

  /**
   * Gets a list of tupels with schema and its test datas. Test data is gathered from
   * <code>testDataDir</code> and schemas from <code>schemaDir</code>.
   * 
   * @param testDataDir directory of test data.
   * @param SchemaDir directory of schemas.
   * @return list of tupels with schema and its test datas.
   */
  private static List<Pair<File, File[]>> getTestDataFiles(File testDataDir, File schemaDir) {
    assert testDataDir.isDirectory() && schemaDir.isDirectory();

    File[] testDataDirs = testDataDir.listFiles();
    File[] schemas = schemaDir.listFiles();
    List<Pair<File, File[]>> test = new ArrayList<>();
    for (File file : testDataDirs) {
      for (File schema : schemas) {
        if (schema.getName().equals(file.getName() + ".json")) {
          File[] testData = file.listFiles();
          test.add(new ImmutablePair<>(schema, testData));
          break;
        }
      }
    }

    return test;
  }
}
