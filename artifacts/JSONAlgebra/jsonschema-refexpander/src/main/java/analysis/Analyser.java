package analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import dto.LoadSchemaDTO;
import exception.DistributedSchemaException;
import exception.InvalidReferenceException;
import model.normalization.Normalizer;
import model.normalization.RepositoryType;
import model.recursion.RecursionChecker;
import model.recursion.RecursionType;
import util.CSVUtil;
import util.Log;
import util.SchemaUtil;

/**
 * Used for analyzing directories with schemas in it.
 * 
 * @author Lukas Ellinger
 */
public class Analyser {

  /**
   * Analyse all files in <code>normalizedDir</code> for recursion. The schemas need to be
   * normalized. A csv file with name "analysis_{name}" is created with {name} being
   * <code>normalizeDir.getName()</code>. In this file each schema has an assigned
   * <code>RecursionType</code>.
   * 
   * @param normalizedDir has to be a directory. All schemas need to be normalized.
   * @throws IOException
   */
  public void analyseRecursion(File normalizedDir) throws IOException {
    if (!normalizedDir.isDirectory()) {
      throw new IllegalArgumentException(normalizedDir.getName() + " has to be a directory");
    }

    int recursive = 0;
    int unguardedRecursive = 0;
    int invalidReference = 0;
    File[] files = normalizedDir.listFiles();
    File analysisFile = new File("recursionanalysis_" + normalizedDir.getName() + ".csv");
    createAnalysisCSV(analysisFile);

    for (File schema : files) {
      RecursionChecker checker = new RecursionChecker(schema);
      String[] fileRow = {schema.getName(), "", "", ""};

      try {
        RecursionType type = checker.checkForRecursion();
        if (type == RecursionType.GUARDED || type == RecursionType.RECURSION) {
          fileRow[1] = "TRUE";
          recursive++;

          if (type != RecursionType.GUARDED) {
            fileRow[2] = "TRUE";
            unguardedRecursive++;
          }
        }
      } catch (InvalidReferenceException e) {
        fileRow[3] = "TRUE";
        Log.warn(schema, e);
        invalidReference++;
      } catch (Exception e) {
        Log.severe(schema, e);
      }

      CSVUtil.writeToCSV(analysisFile, fileRow);
    }

    Log.info("Recursion analysis:");
    Log.info("Total: " + files.length);
    Log.info("Recursive: " + recursive);
    Log.info("Thereof unguarded recursive: " + unguardedRecursive);
    Log.info("Invalid reference: " + invalidReference);
    Log.info("----------------------------------");
  }

  /**
   * Prints stats to log-file and creates csv-file (schmeaTypes_{unnormalizedDir.getName()}.csv)
   * with schema types (single-file schemas, distributed schemas). Uses cleaned schemas (all schemas
   * that could be normalized).
   * 
   * @param csvRecursion csv-file of recursion analysis.
   * @param unnormalizedDir directory of unnormalized schemas.
   * @param normalizedDir directory of normalized schemas.
   * @throws IOException
   */
  public void createDetailedStats(File unnormalizedDir, File normalizedDir) throws IOException {
    if (!unnormalizedDir.isDirectory() || !normalizedDir.isDirectory()) {
      throw new IllegalArgumentException(unnormalizedDir.getName() + " and "
          + normalizedDir.getName() + " need to be directories");
    }

    analyseRecursion(normalizedDir);
    separateSchemasByType(unnormalizedDir, normalizedDir);
    calcDetailedStats(new File("schemaTypes_" + unnormalizedDir.getName() + ".csv"),
        new File("recursionanalysis_" + normalizedDir.getName() + ".csv"), unnormalizedDir,
        normalizedDir);
  }

  private void createAnalysisCSV(File csv) throws IOException {
    assert csv.exists();

    String[] head = {"name", "recursiv", "unguarded_recursiv", "invalid_reference"};
    CSVUtil.writeToCSV(csv, head);
  }

  private int countRows(String s) throws IOException {
    BufferedReader in = new BufferedReader(new StringReader(s));
    int count = 0;

    while (in.readLine() != null) {
      count++;
    }

    return count;
  }

  /**
   * Gets rowcount of <code>file</code>. Uses pretty printing of <code>Gson</code>.
   * 
   * @param file needs to be valid JSON.
   * @return line count of <code>file</code>.
   * @throws IOException
   */
  public int countRowsJSON(File file) throws IOException {
    if (!file.exists()) {
      throw new IllegalArgumentException(file.getName() + " needs to exist.");
    }

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    JsonElement element =
        gson.fromJson(FileUtils.readFileToString(file, "UTF-8"), JsonElement.class);
    return countRows(gson.toJson(element));
  }

  /**
   * Separates all schemas that could be normalized by their type (single-file schema, distributed
   * schema). CSV-File "schemaTypes.csv" is created.
   * 
   * @param unnormalizedDir directory of which schemas should be separated by their type.
   * @param normalizedDir directory of normalized schemas of schemas in
   *        <code>unnormalizedDir</code>.
   * @throws IOException
   */
  public void separateSchemasByType(File unnormalizedDir, File normalizedDir) throws IOException {
    if (!unnormalizedDir.isDirectory() || !normalizedDir.isDirectory()) {
      throw new IllegalArgumentException(unnormalizedDir.getName() + " and "
          + normalizedDir.getName() + " need to be directories");
    }

    File csv = new File("schemaTypes_" + unnormalizedDir.getName() + ".csv");
    String[] head = {"name", "distributed"};
    CSVUtil.writeToCSV(csv, head);

    for (File file : normalizedDir.listFiles()) {
      File unnormalized = new File(unnormalizedDir, file.getName().replace("_Normalized", ""));
      Normalizer normalizer =
          new Normalizer(unnormalized, new LoadSchemaDTO.Builder()
              .allowDistributedSchemas(false)
              .fetchSchemasOnline(false)
              .setRepType(RepositoryType.NORMAL)
              .build());
      try {
        normalizer.normalize();
        String[] row = {unnormalized.getName(), ""};
        CSVUtil.writeToCSV(csv, row);
      } catch (DistributedSchemaException e) {
        String[] row = {unnormalized.getName(), "TRUE"};
        CSVUtil.writeToCSV(csv, row);
      }
    }
  }

  /**
   * Calculates stats and prints them to log-file. Uses cleaned schemas (all schemas that could be
   * normalized.
   * 
   * @param csvRecursion csv file of recursion analysis.
   * @param csvSchemaTypes csv file of type (single or distributed) analysis.
   * @param unnormalizedDir directory of unnormalized schemas.
   * @param normalizedDir directory of normalized schemas.
   * @throws IOException
   */
  public void calcDetailedStats(File csvSchemaTypes, File csvRecursion, File unnormalizedDir,
      File normalizedDir) throws IOException {
    if (!csvRecursion.exists() || !csvSchemaTypes.exists() || !unnormalizedDir.isDirectory()
        || !normalizedDir.isDirectory()) {
      throw new IllegalArgumentException(csvRecursion.getName() + ", " + csvSchemaTypes.getName()
          + " need to exist and " + unnormalizedDir.getName() + ", " + normalizedDir.getName()
          + " need to be directories");
    }

    List<CSVRecord> recordsType = CSVUtil.loadCSV(csvSchemaTypes, ',', true);
    List<CSVRecord> recordsRecursion = CSVUtil.loadCSV(csvRecursion, ',', true);
    int singleFilesCount = 0;
    int distributedFilesCount = 0;
    int totalLocSingleFile = 0;
    int totalLoCSingleFileNormalized = 0;
    int totalLocDistributedFile = 0;
    int totalLoCDistributedFileNormalized = 0;
    int recursiveCountSingleFiles = 0;
    int recursiveCountDistributedFiles = 0;


    for (CSVRecord recordType : recordsType) {
      String fileName = recordType.get(0);
      String normalizedFileName = SchemaUtil.getNormalizedFileName(fileName);
      boolean isRecursive = false;

      for (CSVRecord recordRecursion : recordsRecursion) {
        if (recordRecursion.get(0).equals(normalizedFileName)) {
          if (recordRecursion.get(1).equals("TRUE")) {
            isRecursive = true;
          }
          break;
        }
      }

      if (recordType.get(1).equals("TRUE")) {
        if (isRecursive) {
          recursiveCountDistributedFiles++;
        }
        totalLocDistributedFile += countRowsJSON(new File(unnormalizedDir, fileName));
        totalLoCDistributedFileNormalized +=
            countRowsJSON(new File(normalizedDir, normalizedFileName));
        distributedFilesCount++;
      } else {
        if (isRecursive) {
          recursiveCountSingleFiles++;
        }
        totalLocSingleFile += countRowsJSON(new File(unnormalizedDir, fileName));
        totalLoCSingleFileNormalized += countRowsJSON(new File(normalizedDir, normalizedFileName));
        singleFilesCount++;
      }
    }

    int avgLocSingleFile = totalLocSingleFile / singleFilesCount;
    int avgLocSingleFileNormalized = totalLoCSingleFileNormalized / singleFilesCount;

    int avgLoCDistributedFile = totalLocDistributedFile / distributedFilesCount;
    int avgLocDistributedFileNormalized = totalLoCDistributedFileNormalized / distributedFilesCount;

    int avgLoCOverall =
        (totalLocDistributedFile + totalLocSingleFile) / (singleFilesCount + distributedFilesCount);
    int avgLoCOverallNormalized = (totalLoCDistributedFileNormalized + totalLoCSingleFileNormalized)
        / (singleFilesCount + distributedFilesCount);

    double blowUpSingleFile = calcBlowUp(avgLocSingleFile, avgLocSingleFileNormalized);
    double blowUpDistributedFile =
        calcBlowUp(avgLoCDistributedFile, avgLocDistributedFileNormalized);
    double blowUpOverall = calcBlowUp(avgLoCOverall, avgLoCOverallNormalized);

    Log.info("Total single-file-schemas: " + singleFilesCount);
    Log.info("Single-file-schemas recursion: " + recursiveCountSingleFiles);
    Log.info("Avg LoC single-file-schemas: " + avgLocSingleFile);
    Log.info("Avg LoC single-file-schemas normalized: " + avgLocSingleFileNormalized);
    Log.info("BlowUp single-file-schemas: " + blowUpSingleFile);
    Log.info("----------------------------------");
    Log.info("Total distributed-schemas: " + distributedFilesCount);
    Log.info("Distributed-schemas recursion: " + recursiveCountDistributedFiles);
    Log.info("Avg LoC distributed-schemas: " + avgLoCDistributedFile);
    Log.info("Avg LoC distributed-schemas normalized: " + avgLocDistributedFileNormalized);
    Log.info("BlowUp distributed-schemas: " + blowUpDistributedFile);
    Log.info("----------------------------------");
    Log.info("Avg LoC overall: " + avgLoCOverall);
    Log.info("Avg LoC overall normalized: " + avgLoCOverallNormalized);
    Log.info("BlowUp overall: " + blowUpOverall);
    Log.info("----------------------------------");
  }

  private double calcBlowUp(int base, int value) {
    assert base != 0;

    return ((double) value) / base - 1;
  }
}
