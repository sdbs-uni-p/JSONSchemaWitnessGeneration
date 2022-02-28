package analysis;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.io.FileUtils;
import com.google.common.collect.Lists;
import util.Log;
import util.SchemaUtil;

/**
 * Offers methods to clean directories with potential JSON Schemas.
 * 
 * @author Lukas Ellinger
 */
public class DirCleaner {
  
  /**
   * Deletes the files of all no valid schemas in <code>dir</code>.
   * 
   * @param dir directory to delete no valid schemas.
   * @throws IOException
   */
  public void removeNoValidSchemas(File dir) throws IOException {
    if (!dir.isDirectory()) {
      throw new IllegalArgumentException(dir.getName() + " needs to be a directory");
    }

    int count = 0;
    
    for (File schema : dir.listFiles()) {
      if (!SchemaUtil.isValidToDraft(schema)) {
        schema.delete();
        count++;
      }
    }
    
    Log.info(count + " not valid schemas in directory deleted");
  }

  /**
   * Removes all duplicate files. The content is checked for equivality.
   * 
   * @param dir to remove duplicates.
   * @throws IOException
   */
  public void removeDuplicateSchemas(File dir) throws IOException {
    if (!dir.isDirectory()) {
      throw new IllegalArgumentException(dir.getName() + " needs to be a directory");
    }

    List<File> dirList = Arrays.asList(dir.listFiles());
    ListIterator<File> iterator = dirList.listIterator();
    int count = 0;

    while (iterator.hasNext()) {
      File file1 = iterator.next();
      ListIterator<File> innerIterator = dirList.listIterator(iterator.nextIndex());
      if (file1.exists()) {
        while (innerIterator.hasNext()) {
          File file2 = innerIterator.next();
          if (FileUtils.contentEquals(file1, file2)) {
            file2.delete();
            count++;
          }
        }
      }
    }

    Log.info("Duplicates count: " + count);
  }

  /**
   * Removes all files which are included in other files.
   * 
   * @param dir to remove all included schemas.
   * @throws IOException
   */
  public void removeIncludedSchemas(File dir) throws IOException {
    if (!dir.isDirectory()) {
      throw new IllegalArgumentException(dir.getName() + " needs to be a directory");
    }

    List<File> dirList = Arrays.asList(dir.listFiles());
    int count = 0;

    for (File file1 : dirList) {
      if (file1.exists()) {
        long file1Length = file1.length();
        String file1Lines = FileUtils.readFileToString(file1, "UTF-8");
        file1Lines = file1Lines.replace("\n", "");
        file1Lines = file1Lines.replace(" ", "");
        List<Character> file1List = Lists.charactersOf(file1Lines);


        for (File file2 : dir.listFiles()) {
          if (file2.length() < file1Length) {
            String file2Lines = FileUtils.readFileToString(file2, "UTF-8");
            file2Lines = file2Lines.replace("\n", "");
            file2Lines = file2Lines.replace(" ", "");
            List<Character> file2List = Lists.charactersOf(file2Lines);

            if (Collections.indexOfSubList(file1List, file2List) != -1) {
              file2.delete();
              count++;
            }
          }
        }

      }
    }

    Log.info("Included count: " + count);
  }
}
