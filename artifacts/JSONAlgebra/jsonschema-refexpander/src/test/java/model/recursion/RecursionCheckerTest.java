package model.recursion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.google.gson.JsonSyntaxException;
import dto.LoadSchemaDTO;
import model.normalization.Normalizer;
import model.normalization.RepositoryType;
import util.FileLoader;

class RecursionCheckerTest {

  private final static String BASIC_SCHEMAS =
      "src/test/resources/basicSchemas/{1}testSchema{0}.json";
  private final static LoadSchemaDTO config = new LoadSchemaDTO.Builder()
      .allowDistributedSchemas(true)
      .fetchSchemasOnline(true)
      .setRepType(RepositoryType.NORMAL)
      .build();

  @Test
  void noSchemaTest() throws IOException {
    assertThrows(JsonSyntaxException.class,
        () -> FileLoader.loadSchema(MessageFormat.format(BASIC_SCHEMAS, 1, "")));
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 3, 17, 18, 19, 20, 21, 23, 24, 26, 30, 31, 32, 34, 36, 38, 40, 41, 42, 44,
      45, 46, 48})
  void noRecursionTest(int i) throws IOException {
    Normalizer normalizer =
        new Normalizer(new File(MessageFormat.format(BASIC_SCHEMAS, i, "")), config);
    RecursionChecker checker = new RecursionChecker(normalizer.normalize());
    assertEquals(RecursionType.NONE, checker.checkForRecursion());
  }

  @ParameterizedTest
  @ValueSource(ints = {5, 7, 9, 10, 11, 12, 13, 14, 49, 50, 51, 52, 53})
  void recursionTest(int i) throws IOException {
    Normalizer normalizer =
        new Normalizer(new File(MessageFormat.format(BASIC_SCHEMAS, i, "")), config);
    RecursionChecker checker = new RecursionChecker(normalizer.normalize());
    assertEquals(RecursionType.RECURSION, checker.checkForRecursion());
  }

  @ParameterizedTest
  @ValueSource(ints = {4, 6, 8, 15, 16, 35, 43, 47})
  void guardedRecursionTest(int i) throws IOException {
    Normalizer normalizer =
        new Normalizer(new File(MessageFormat.format(BASIC_SCHEMAS, i, "")), config);
    RecursionChecker checker = new RecursionChecker(normalizer.normalize());
    assertEquals(RecursionType.GUARDED, checker.checkForRecursion());
  }
}
