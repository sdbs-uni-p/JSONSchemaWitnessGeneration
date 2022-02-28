package model.normalization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.google.gson.JsonObject;
import dto.LoadSchemaDTO;
import exception.InvalidFragmentException;
import util.FileLoader;

public class NormalizerTest {

  private final static String BASIC_SCHEMAS =
      "src/test/resources/basicSchemas/{1}testSchema{0}.json";
  private final static String FILE_REFS_SCHEMAS =
      "src/test/resources/fileRefsSchemas/{1}testSchema{0}.json";
  private final static String ABSOLUTE_URL_REFS_SCHEMAS =
      "src/test/resources/absoluteURLRefsSchemas/{1}testSchema{0}.json";
  private final static String ID_REFS_SCHEMAS =
      "src/test/resources/idRefsSchemas/{1}testSchema{0}.json";
  private final static String INVALID_FRAGMENT_SCHEMAS =
      "src/test/resources/invalidFragmentSchemas/{1}testSchema{0}.json";
  private final static LoadSchemaDTO config = new LoadSchemaDTO.Builder()
      .allowDistributedSchemas(true)
      .fetchSchemasOnline(true)
      .setRepType(RepositoryType.NORMAL)
      .build();

  @ParameterizedTest
  @ValueSource(ints = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 23,
      24, 26, 30, 31, 32, 34, 35, 36, 38, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53})
  void basicSchemasNormalizationTest(int i) throws IOException {
    Normalizer normalizer =
        new Normalizer(new File(MessageFormat.format(BASIC_SCHEMAS, i, "")), config);
    assertEquals(FileLoader.getStoredNormalizedSchema(BASIC_SCHEMAS, i), normalizer.normalize());
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 23,
      24, 26, 30, 31, 32, 34, 35, 36, 38, 40, 41, 42, 43, 44, 45, 46, 47, 48})
  void normalizeIdempotentTest(int i) throws IOException {
    Normalizer normalizer =
        new Normalizer(new File(MessageFormat.format(BASIC_SCHEMAS, i, "")), config);
    normalizer.normalize();
    assertEquals(FileLoader.getStoredNormalizedSchema(BASIC_SCHEMAS, i), normalizer.normalize());
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12})
  void fileRefsSchemasTest(int i) throws IOException {
    Normalizer normalizer =
        new Normalizer(new File(MessageFormat.format(FILE_REFS_SCHEMAS, i, "")), config);
    assertEquals(FileLoader.getStoredNormalizedSchema(FILE_REFS_SCHEMAS, i),
        normalizer.normalize());
  }

  @Test
  void schemaWithRefToOneDirectoryUpTest() throws IOException {
    String path = "src/test/resources/fileRefsSchemas/test13/";
    JsonObject normalized = FileLoader.loadSchema(path + "Normalized_testSchema13.json");
    Normalizer normalizer = new Normalizer(new File(path + "testSchema13.json"), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3})
  void absoluteURLRefsSchemasTest(int i) throws IOException {
    Normalizer normalizer =
        new Normalizer(new File(MessageFormat.format(ABSOLUTE_URL_REFS_SCHEMAS, i, "")), config);
    assertEquals(FileLoader.getStoredNormalizedSchema(ABSOLUTE_URL_REFS_SCHEMAS, i),
        normalizer.normalize());
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
  void idRefsSchemasTest(int i) throws IOException {
    Normalizer normalizer =
        new Normalizer(new File(MessageFormat.format(ID_REFS_SCHEMAS, i, "")), config);
    assertEquals(FileLoader.getStoredNormalizedSchema(ID_REFS_SCHEMAS, i), normalizer.normalize());
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3})
  void invalidFragmentSchemasTest(int i) throws IOException {
    Normalizer normalizer =
        new Normalizer(new File(MessageFormat.format(INVALID_FRAGMENT_SCHEMAS, i, "")), config);
    assertThrows(InvalidFragmentException.class, () -> normalizer.normalize());
  }
  
  @AfterAll
  public static void cleanUp() throws IOException {
    new File("UriOfFiles.csv").delete();
    FileUtils.deleteDirectory(new File("Store"));
  }
}
