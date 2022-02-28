package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

/**
 * Offers utils for csv-files.
 * 
 * @author Lukas Ellinger
 */
public class CSVUtil {
  
  /**
   * Appends <code>collumns</code> to <code>csv</code>. If file does not exist yet, it is created.
   * 
   * @param csv file to write to.
   * @param collumns to be added.
   * @throws IOException if it cannot be written to <code>csv</code> file.
   */
  public static void writeToCSV(File csv, String[] collumns) throws IOException {    
    try (
        BufferedWriter writer = Files.newBufferedWriter(csv.toPath(),
            StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);) {
      csvPrinter.printRecord(collumns);
      csvPrinter.flush();
    }
  }

  /**
   * Gets all records of csv file at <code>filepath</code>. <code>delim</code> defines the
   * separator.
   * 
   * @param csv file to be loaded.
   * @param delim used in csv-file.
   * @param firstRowHeader <code>true</code>, if first row of csv is header. <code>false</code>, if
   *        not.
   * @return records of csv-file.
   * @throws IOException if it cannot be read from <code>filepath</code>.
   */
  public static List<CSVRecord> loadCSV(File csv, char delim, boolean firstRowHeader)
      throws IOException {
    Reader in = new FileReader(csv);
    List<CSVRecord> records;
    if (firstRowHeader) {
      records =
          CSVFormat.DEFAULT.withDelimiter(delim).parse(in).getRecords();
      records.remove(0);
    } else {
      records = CSVFormat.DEFAULT.withDelimiter(delim).parse(in).getRecords();
    }
    in.close();
    return records;
  }
}
