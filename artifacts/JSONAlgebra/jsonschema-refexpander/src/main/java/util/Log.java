package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Simple Logger
 * 
 * @author Lukas Ellinger
 */
public class Log {
  private static Logger logger;

  static {
    try (
        InputStream stream = Log.class.getClassLoader().getResourceAsStream("logging.properties")) {
      LogManager.getLogManager().readConfiguration(stream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    logger = Logger.getAnonymousLogger();
  }

  public static void info(String msg) {
    logger.info(msg);
  }

  public static void warn(String msg) {
    logger.warning(msg);
  }

  public static void warn(File file, Throwable throwable) {
    logger.warning(file.getName() + ": " + throwable.getClass().getSimpleName() + ": "
        + throwable.getMessage());
  }
  
  public static void warn(String additionalInfo, Throwable throwable) {
    logger.warning(additionalInfo + " - " + throwable.getClass().getSimpleName() + ": "
        + throwable.getMessage());
  }

  public static void severe(String msg) {
    logger.severe(msg);
  }
  
  public static void severe(File file, Throwable throwable) {
    logger.severe(file.getName() + ": " + throwable.getClass().getSimpleName() + ": "
        + throwable.getMessage());
  }
  
  public static void severe(String additionalInfo, Throwable throwable) {
    logger.warning(additionalInfo + " - " + throwable.getClass().getSimpleName() + ": "
        + throwable.getMessage());
  }
}
