package model.normalization;

/**
 * Type of Repository. If <code>CORPUS</code> is chosen, it is tried to load a schema again with
 * "raw=true" as query, if an exception occurs. If <code>TESTSUITE</code> is chosen,
 * "http://localhost:1234/" in id is replaced with "/home/TestSuiteDraft4/remotes", if an exception
 * occured.
 * 
 * @author Lukas Ellinger
 */
public enum RepositoryType {
  CORPUS, TESTSUITE, NORMAL
}
