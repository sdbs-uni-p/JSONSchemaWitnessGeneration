package analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import dto.LoadSchemaDTO;
import exception.DistributedSchemaException;
import exception.DraftValidationException;
import exception.InvalidReferenceException;
import exception.StoreException;
import model.normalization.Normalizer;
import model.recursion.RecursionChecker;
import util.CSVUtil;
import util.Log;
import util.SchemaUtil;
import util.URIUtil;

/**
 * Is used to normalize the schemas of Schema Corpus
 * (https://github.com/sdbs-uni-p/json-schema-corpus) or to analyse specific schemas of it.
 * 
 * @author Lukas Ellinger
 */
public class SchemaCorpus {

  /**
   * Normalizes all valid schemas of Schema Corpus and stores them.
   * 
   * @param schema_corpus directory of <code>schema_corpus</code>.
   * @param fullPath file of <code>repos_fullPath.csv</code>.
   * @param config of how schemas should be loaded.
   * @throws IOException
   */
  public void normalize(File schema_corpus, File fullPath, LoadSchemaDTO config)
      throws IOException {
    if (!schema_corpus.isDirectory() || !fullPath.exists()) {
      throw new IllegalArgumentException(schema_corpus.getName() + " needs to be a directory and "
          + fullPath.getName() + " needs to exist");
    }

    cleanCorpus(schema_corpus, fullPath);

    List<CSVRecord> records = CSVUtil.loadCSV(fullPath, ' ', false);
    File normalizedDir = new File("Normalized_" + schema_corpus.getName());
    normalizedDir.mkdir();
    File csvLineage = new File("Lineage_" + schema_corpus.getName() + ".csv");

    int invalidReference = 0;
    int draftValidation = 0;
    for (CSVRecord record : records) {
      String file = record.get(0);
      file = file.replaceFirst("js", "pp");
      File schema = new File(schema_corpus, file);

      try {
        URI recordURI = URIUtil.urlToUri(new URL(record.get(1)));
        if (schema.exists()) {
          try {
            SchemaUtil.normalize(schema, recordURI, normalizedDir, csvLineage, config);
          } catch (InvalidReferenceException e) {
            invalidReference++;
            Log.warn(schema, e);
          } catch (DraftValidationException e) {
            draftValidation++;
            Log.warn(schema, e);
          } catch (DistributedSchemaException | StoreException e) {
            Log.warn(schema, e);
          }
        }
      } catch (URISyntaxException e) {
        Log.warn(file, e);
      }
    }

    Log.info("Normalization process:");
    Log.info("Invalid references: " + invalidReference);
    Log.info("Normalized schemas not valid to draft: " + draftValidation);
    Log.info("----------------------------------");
  }

  /**
   * Cleans the Schema Corpus and repos_fullPath.csv. No valid schemas are removed.
   * repos_fullPath.csv is updated.
   * 
   * @param schema_corpus directory of <code>schema_corpus</code>.
   * @param fullPath file of <code>repos_fullPath.csv</code>.
   * @throws IOException
   */
  public void cleanCorpus(File schema_corpus, File fullPath) throws IOException {
    if (!schema_corpus.isDirectory() || !fullPath.exists()) {
      throw new IllegalArgumentException(schema_corpus.getName() + " needs to be a directory and "
          + fullPath.getName() + " needs to exist");
    }

    DirCleaner cleaner = new DirCleaner();
    cleaner.removeNoValidSchemas(schema_corpus);
    markNotExistingFilesFullPath(schema_corpus, fullPath);
    correctURIsFullpath(fullPath);
    removeDeletedLinesFromCSV(fullPath);
  }

  /**
   * Can test specified schemas in <code>csvFile</code> for recursion.
   * 
   * @param schema_corpus directory of <code>schema_corpus</code>.
   * @param fullPath file of <code>repos_fullPath.csv</code>.
   * @param csvFile of to be tested schemas. In each row, there only needs to be the number of one
   *        schema. No header.
   * @param config of how schemas should be loaded.
   * @throws IOException
   * @throws URISyntaxException
   */
  public void testSchemas(File schema_corpus, File fullPath, File csvFile, LoadSchemaDTO config)
      throws IOException, URISyntaxException {
    if (!schema_corpus.isDirectory() || !fullPath.exists() || !csvFile.exists()) {
      throw new IllegalArgumentException(schema_corpus.getName() + " needs to be a directory and "
          + fullPath.getName() + " " + csvFile.getName() + " needs to exist");
    }

    List<CSVRecord> records = CSVUtil.loadCSV(fullPath, ' ', false);
    List<CSVRecord> toBeTested = CSVUtil.loadCSV(csvFile, ' ', false);

    for (CSVRecord record : toBeTested) {
      int reposNumber = Integer.parseInt(record.get(0));
      URI reposURI = new URI(records.get(reposNumber).get(1));
      String file = "pp_" + reposNumber + ".json";
      File unnormalized = new File(schema_corpus, file);

      if (unnormalized.exists()) {
        if (SchemaUtil.isValidToDraft(unnormalized)) {
          try {
            Normalizer normalizer = new Normalizer(unnormalized, reposURI, config);
            RecursionChecker checker = new RecursionChecker(normalizer.normalize());
            Log.info(unnormalized.getName() + ": " + checker.checkForRecursion().name());
          } catch (InvalidReferenceException e) {
            Log.warn(unnormalized.getName(), e);
          }
        }
      }
    }
  }

  /**
   * Corrects all URI of repos_fullPath.csv. Therefore it inserts %20 for spaces.
   * 
   * @param fullPath file of <code>repos_fullPath.csv</code>.
   * @throws IOException
   */
  private void correctURIsFullpath(File fullPath) throws IOException {
    assert fullPath.exists();

    List<CSVRecord> records = CSVUtil.loadCSV(fullPath, ' ', false);
    List<String> lines = new ArrayList<String>();
    int count = 0;

    for (CSVRecord csvRecord : records) {
      String uri = csvRecord.get(1);
      String correctedUri = uri.replace(" ", "%20");

      if (!correctedUri.equals(uri)) {
        count++;
      }

      lines.add(csvRecord.get(0) + " " + correctedUri);
    }

    FileUtils.writeLines(fullPath, lines);
    Log.info(count + " URIS corrected.");
  }

  /**
   * Replaces URI (second column) in <code>repos_fullPath</code> with "deleted", if file does not
   * exist in <code>schema_corpus</code>.
   * 
   * @param schema_corpus directory of <code>schema_corpus</code>.
   * @param fullPath file of <code>repos_fullPath.csv</code>.
   * @throws IOException
   */
  private void markNotExistingFilesFullPath(File schema_corpus, File fullPath) throws IOException {
    assert schema_corpus.isDirectory() && fullPath.exists();

    List<CSVRecord> records = CSVUtil.loadCSV(fullPath, ' ', false);
    List<String> lines = new ArrayList<String>();
    int count = 0;

    for (CSVRecord record : records) {
      String file = record.get(0);
      file = file.replaceFirst("js", "pp");
      File schema = new File(schema_corpus, file);

      if (!schema.exists()) {
        lines.add(record.get(0) + " " + "deleted");
        count++;
      } else {
        lines.add(record.get(0) + " " + record.get(1));
      }
    }

    FileUtils.writeLines(fullPath, lines);
    Log.info(count + " not existing files in corpus marked.");
  }

  /**
   * Removes all lines of <code>fullPath</code> which have "deleted" in second column.
   * 
   * @param fullPath file of <code>repos_fullPath.csv</code>.
   * @throws IOException
   */
  private void removeDeletedLinesFromCSV(File fullPath) throws IOException {
    assert fullPath.exists();

    List<CSVRecord> records = CSVUtil.loadCSV(fullPath, ' ', false);
    List<CSVRecord> newRecords = new ArrayList<>();

    for (CSVRecord record : records) {
      if (!record.get(1).equals("deleted")) {
        newRecords.add(record);
      }
    }

    try (
        BufferedWriter writer = Files.newBufferedWriter(fullPath.toPath(),
            StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withDelimiter(' '));) {
      csvPrinter.printRecords(newRecords);
      csvPrinter.flush();
    }
  }
}
