package model.bugfixes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonObject;
import dto.LoadSchemaDTO;
import exception.InvalidFragmentException;
import model.normalization.Normalizer;
import model.normalization.RepositoryType;
import util.FileLoader;

public class BugfixesTest {

  private final static File BUGFIX_DIR = new File("src/test/resources/bugfixes");
  private final static LoadSchemaDTO config = new LoadSchemaDTO.Builder()
      .allowDistributedSchemas(true)
      .fetchSchemasOnline(true)
      .setRepType(RepositoryType.NORMAL)
      .build();

  @Test
  void refToNoPresentChildOfDefinitions() throws IOException {
    Normalizer normalizer =
        new Normalizer(new File(BUGFIX_DIR, "refToNoPresentChildOfDefinitions.json"), config);
    assertThrows(InvalidFragmentException.class, () -> normalizer.normalize());
  }

  @Test
  void refWithSpecialRegexLetters() throws IOException {
    final String fileName = "refWithSpecialRegexLetters.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR.getPath() + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refWithEscapedChars() throws IOException {
    final String fileName = "refWithEscapedChars.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void idWithEmptyFragment() throws IOException {
    final String fileName = "idWithEmptyFragment.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refWithChangedBase() throws IOException {
    final String fileName = "refWithChangedBase.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refWithRelativeURI() throws IOException {
    final String fileName = "refWithRelativeURI.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refWithIllegalURI() throws IOException {
    final String fileName = "refWithIllegalURI.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refToRootWithTrailingHash() throws IOException {
    final String fileName = "refToRootWithTrailingHash.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refToChildOfChildOfDefinitions() throws IOException {
    final String fileName = "refToChildOfChildOfDefinitions.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refWithIdAndPointer() throws IOException {
    final String fileName = "refWithIdAndPointer.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void idInEnum() throws IOException {
    final String fileName = "idInEnum.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refToLastId() throws IOException {
    final String fileName = "refToLastId.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void idWithNoPath() throws IOException {
    final String fileName = "idWithNoPath.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void escapePercent() throws IOException {
    final String fileName = "escapePercent.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refToTopIdOfSchema() throws IOException {
    final String fileName = "refToTopIdOfSchema.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refWithTrailingHash() throws IOException {
    final String fileName = "refWithTrailingHash.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }
  
  @AfterAll
  public static void cleanUp() throws IOException {
    new File("UriOfFiles.csv").delete();
    FileUtils.deleteDirectory(new File("Store"));
  }
}
