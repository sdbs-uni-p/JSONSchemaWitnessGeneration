package analysis;

import java.io.File;
import java.io.IOException;
import dto.LoadSchemaDTO;
import exception.DistributedSchemaException;
import exception.DraftValidationException;
import exception.InvalidReferenceException;
import exception.StoreException;
import util.Log;
import util.SchemaUtil;

/**
 * Used to normalize schemas in a directory.
 * 
 * @author Lukas Ellinger
 */
public class DirNormalizer {

  /**
   * Normalizes all valid schemas in <code>dir</code> and stores them.
   * 
   * @param dir directory of schemas to be normalized.
   * @param config of how schemas should be loaded.
   * @throws IOException
   */
  public void normalize(File dir, LoadSchemaDTO config) throws IOException {
    if (!dir.isDirectory()) {
      throw new IllegalArgumentException(dir.getName() + " needs to be a directory");
    }

    DirCleaner cleaner = new DirCleaner();
    //cleaner.removeNoValidSchemas(dir);

    File normalizedDir = new File("Normalized_" + dir.getName());
    normalizedDir.mkdir();
    File csvLineage = new File("Lineage_" + dir.getName() + ".csv");

    int invalidReference = 0;
    int draftValidation = 0;
    for (File schema : dir.listFiles()) {
      try {
        SchemaUtil.normalize(schema, null, normalizedDir, csvLineage, config);
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

    Log.info("Normalization process:");
    Log.info("Invalid reference: " + invalidReference);
    Log.info("Normalized schemas not valid to draft: " + draftValidation);
    Log.info("----------------------------------");
  }
}
