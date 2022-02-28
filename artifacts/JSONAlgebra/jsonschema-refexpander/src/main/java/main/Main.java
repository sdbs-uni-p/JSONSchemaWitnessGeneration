package main;

import java.io.File;
import java.io.IOException;
import analysis.Analyser;
import analysis.DirNormalizer;
import analysis.SchemaCorpus;
import analysis.TestSuite;
import dto.LoadSchemaDTO;
import model.normalization.RepositoryType;

/**
 * 
 * @author Lukas Ellinger
 */
public class Main {
  /**
   * If jar has already been executed once there possibly is a directory "Store" and a file
   * "UriOfFiles.csv" next to the jar. These two are used to store all remote refs. If you are
   * analysing the schema dataset again these are used and no remote refs are downloaded from the
   * internet.
   * 
   * @param args As first parameter choose between -normalize | -recursion | -stats. If -normalize
   *        is chosen, second parameter will be the repositorytype. Choose between -corpus |
   *        -testsuite | -normal. Third parameter is -true | -false in respect to whether
   *        distributed schemas should be allowed. Fourth parameter is -true | -false in respect to
   *        whether references should be downloaded online or only be fetched offline. Fifth
   *        parameter is the path to the directory to analyse in quotation marks. If -corpus is
   *        chosen, an additional sixth parameter with the path to repos_fullpath.csv in quotation
   *        marks will be needed. If -recursion is chosen, second parameter will be the path to the
   *        directory in which the normalized schemas are. These will be checked for recursion. If
   *        -stats is chosen, second parameter will be the path to the directory with unnormalized
   *        schemas and third parameter the path to the directory with normalized schemas.
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    if (args.length < 2) {
      throw new IllegalArgumentException("To less parameters");
    } else {
      switch (args[0]) {
        case "-normalize":
          boolean allowDistributedSchemas = Boolean.parseBoolean(args[2].substring(1));
          boolean fetchSchemasOnline = Boolean.parseBoolean(args[3].substring(1));
          LoadSchemaDTO config = new LoadSchemaDTO.Builder()
              .allowDistributedSchemas(allowDistributedSchemas)
              .fetchSchemasOnline(fetchSchemasOnline)
              .build();
          switch (args[1]) {
            case "-corpus":
              SchemaCorpus corpus = new SchemaCorpus();
              config.setRepType(RepositoryType.CORPUS);
              corpus.normalize(new File(args[4]), new File(args[5]), config);
              break;
            case "-testsuite":
              TestSuite suite = new TestSuite();
              config.setRepType(RepositoryType.TESTSUITE);
              suite.normalize(new File(args[4]), config);
              break;
            case "-normal":
              DirNormalizer normalizer = new DirNormalizer();
              config.setRepType(RepositoryType.NORMAL);
              normalizer.normalize(new File(args[4]), config);
              break;
            default:
              throw new IllegalArgumentException("Unexpected value: " + args[1]);
          }
          break;
        case "-recursion":
          Analyser analyser1 = new Analyser();
          analyser1.analyseRecursion(new File(args[1]));
          break;
        case "-stats":
          Analyser analyser2 = new Analyser();
          analyser2.createDetailedStats(new File(args[1]), new File(args[2]));
          break;
        default:
          throw new IllegalArgumentException("Unexpected value: " + args[0]);
      } 
    }
  }
}
