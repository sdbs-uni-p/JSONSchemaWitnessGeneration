package model.normalization;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dto.LoadSchemaDTO;
import exception.InvalidIdentifierException;
import exception.StoreException;
import model.Draft;
import util.SchemaUtil;
import util.Store;
import util.URIUtil;
import util.URLLoader;

/**
 * Encapsulates a <code>URI</code> with its content parsed as a <code>JsonObject</code> and a
 * <code>SchemaStore</code>.
 * 
 * @author Lukas Ellinger
 */
public class SchemaFile {
  private static final String TESTSUITE_REMOTES_DIR = "/home/TestSuiteDraft4/remotes/";
  private URI id;
  private URI locatedAt;
  private JsonObject object;
  private SchemaStore store;
  private Stack<URI> resScope = new Stack<>();
  private Draft draft;

  /**
   * Creates a new <code>SchemaFile</code>. The stored <code>SchemaStore</code> is initialized with
   * this.
   * 
   * @param file of which the <code>SchemaFile</code> should be created.
   * @param config of how schema should be loaded.
   */
  public SchemaFile(File file, LoadSchemaDTO config) {
    locatedAt = file.toURI();
    store = new SchemaStore(config);
    loadJsonObject(locatedAt);
    draft = SchemaUtil.getDraft(object);
    setIdFromSchema();
    store.addRootSchemaFile(this);
  }

  /**
   * This constructor shoud be used if you want to directly use a JsonObject as Schema without File and Directory.
   * Using this constructor you can give a JsonObject as first Parameter and get an JsonObject as Result.
   * You will still need to create a config. (LoadSchemaDTO)
   *
   * @param jsonObjectSchema
   * @param config
   *
   * @author Luca Escher
   */
  public SchemaFile(JsonObject jsonObjectSchema, LoadSchemaDTO config){
    this.store = new SchemaStore(config);
    try {
      this.locatedAt = new URI("/Default/url");
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    this.object = jsonObjectSchema;
    this.draft = Draft.DraftHigher;
    try {
      this.id = new URI("/Default/url");
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    setIdFromSchema();
    store.addRootSchemaFile(this);
    try {
      Store.storeSchema(jsonObjectSchema, new URI("/Default/url"));
    } catch (Exception e) {
      System.out.println("Sth. wnt wrong storeSchema");
      System.out.println(e.getMessage());
    }
  }

  /**
   * 
   * @param id of where the schema is localized.
   * @param store to use.
   */
  public SchemaFile(URI id, SchemaStore store) {
    locatedAt = id;
    this.store = store;
    loadJsonObject(locatedAt);
    draft = SchemaUtil.getDraft(object);
    setIdFromSchema();
  }

  /**
   * 
   * @param file of which the <code>SchemaFile</code> should be created.
   * @param id location of where the file is from. Is used as id if no id is declared in the schema.
   * @param config of how schema should be loaded.
   */
  public SchemaFile(File file, URI id, LoadSchemaDTO config) {
    locatedAt = id;
    store = new SchemaStore(config);
    loadJsonObject(file.toURI());
    draft = SchemaUtil.getDraft(object);
    setIdFromSchema();
    store.addRootSchemaFile(this);
  }

  public URI getRoot() {
    return store.getRoot();
  }

  public Draft getDraft() {
    return draft;
  }

  private void loadJsonObject(URI location) {
    Gson gson = new Gson();
    
    try {
      try {
        if (location.getScheme().equals("file")) {
          object = gson.fromJson(URLLoader.loadWithRedirect(location.toURL()), JsonObject.class);
        } else {
          object = Store.getSchema(location); 
        }
      } catch (StoreException e) {
        
        if (store.isFetchSchemasOnline()) {
          object = gson.fromJson(URLLoader.loadWithRedirect(location.toURL()), JsonObject.class);

          if (!location.getScheme().equals("file")) {
            Store.storeSchema(object, location);
          } 
        } else {
          throw e;
        }
      }
    } catch (IOException | JsonSyntaxException e) {
      try {
        if (store.getRepType().equals(RepositoryType.TESTSUITE)) {
          File file = new File(
              location.toString().replace("http://localhost:1234/", TESTSUITE_REMOTES_DIR));
          object = gson.fromJson(FileUtils.readFileToString(file, "UTF-8"), JsonObject.class);
        } else if (store.getRepType().equals(RepositoryType.CORPUS)) {
          try {
            URI locationRaw = new URI(location.getScheme(), location.getAuthority(),
                location.getPath(), "raw=true", location.getFragment());
            object =
                gson.fromJson(URLLoader.loadWithRedirect(locationRaw.toURL()), JsonObject.class);

            if (!location.getScheme().equals("file")) {
              Store.storeSchema(object, location);
            }
          } catch (URISyntaxException e1) {
            throw new InvalidIdentifierException(location + " is no valid URI with query raw=true");
          }
        } else {
          throw new InvalidIdentifierException("Schema with " + location + " cannot be loaded");
        }
      } catch (IOException e2) {
        throw new InvalidIdentifierException("Schema with " + location + " cannot be loaded");
      } catch (JsonSyntaxException e2) {
        throw new InvalidIdentifierException("At " + location + " is no valid JsonObject");
      }
    }
  }

  /**
   * If there is a id set in schema then the <code>id</code> is set to this.
   */
  private void setIdFromSchema() {
    try {
      URI schemaId = SchemaUtil.getId(object, draft);

      if (!schemaId.toString().equals("")) {
        URI resolved = locatedAt.resolve(schemaId);
        id = URIUtil.removeFragment(resolved);
      } else {
        id = locatedAt;
      }
    } catch (IllegalArgumentException e) {
      throw new InvalidIdentifierException("id declared in schema is no valid URI");
    } catch (URISyntaxException e) {
      throw new InvalidIdentifierException(id + " has an invalid identifier in it");
    }
  }

  /**
   * Gets the current resolution scope.
   * 
   * @return current resolution scope you are in. On the highest level, <code>id</code> is returned.
   */
  public URI getResScope() {
    if (resScope.empty()) {
      return id;
    } else {
      return resScope.peek();
    }
  }

  /**
   * Sets the resolution scope to <code>scope</code>.
   * 
   * @param scope to be set. If <code>null</code> old resolution scope stays.
   */
  public void setResScope(URI scope) {
    if (scope.toString().equals("")) {
      resScope.push(getResScope());
    } else {
      resScope.push(URIUtil.removeTrailingHash(getResScope().resolve(scope)));
    }
  }

  /**
   * Sets the resolution scope to the previous one and returns it.
   * 
   * @return current resolution scope.
   */
  public URI oneScopeUp() {
    return resScope.pop();
  }

  /**
   * Sets the resolution scope to the scope of the top level schema.
   */
  public void setResScopeToTopLevel() {
    resScope.push(resScope.firstElement());
  }

  /**
   * Gets the relative path between the stored <code>root</code> in <code>store</code> and this.
   * 
   * @return relative path between the stored <code>root</code> in <code>store</code> and this.
   */
  public String getRelIdentifier() {
    Optional<String> rootScheme = Optional.ofNullable(store.getRoot().getScheme());
    Optional<String> idScheme = Optional.ofNullable(id.getScheme());
    Optional<String> rootAuthority = Optional.ofNullable(store.getRoot().getAuthority());
    Optional<String> idAuthority = Optional.ofNullable(id.getAuthority());

    if (!rootScheme.equals(idScheme) || !rootAuthority.equals(idAuthority)) {
      return id.toString();
    } else {
      Optional<Path> rootPath =
          Optional.ofNullable(Paths.get(URIUtil.getParentURI(store.getRoot()).getPath()));
      Path idPath = new File(id.getPath()).toPath();
      if (rootPath.isEmpty()) {
        return FilenameUtils.separatorsToUnix(idPath.toString().substring(1));
      } else {
        return FilenameUtils.separatorsToUnix(rootPath.get().relativize(idPath).toString());
      }
    }
  }


  /**
   * Checks whether this has the same <code>id</code> as <code>root</code> of stored
   * <code>SchemaStore</code>.
   * 
   * @return <code>true</code>, if <code>id</code> equals <code>root</code>. <code>false</code>, if
   *         not.
   */
  public boolean isRootFile() {
    return store.isRoot(this);
  }

  /**
   * Adds <code>element</code> to the visited <code>JsonElements</code>.
   * 
   * @param element to be added.
   */
  public void addVisited(JsonElement element) {
    store.addVisited(element);
  }

  /**
   * Checks whether <code>element</code> as already been visited.
   * 
   * @param element to be checked.
   * @return <code>true</code>, if it has already been visited. <code>false</code>, if not.
   */
  public boolean alreadyVisited(JsonElement element) {
    return store.alreadyVisited(element);
  }

  /**
   * Gets <code>SchemaFile</code> of <code>file</code>. If the corresponding is already stored in
   * the <code>store</code>, then the stored one is returned. Otherwise the new
   * <code>SchemaFile</code> is added to the <code>store</coded> and returned.
   * 
   * @param file of which the <code>SchemaFile</code> should be returned.
   * 
   * @return <code>SchemaFile</code> of <code>file</code>. If the corresponding is already stored in
   *         the <code>store</code>, then the stored one is returned
   */
  public SchemaFile getLoadedFile(URI identifier) {
    return store.getLoadedFile(resScope.peek().resolve(identifier));
  }

  public URI getId() {
    return id;
  }

  public JsonObject getObject() {
    return object;
  }

  /**
   * Gets a set of <code>Strings</code> of all loaded <code>URIs</code>. This are all
   * <code>URIs</code> of schemas used for normalization.
   * 
   * @return set of <code>Strings</code> of all loaded URIs. This are all <code>URIs</code> of
   *         schemas used for normalization.
   */
  public Set<String> getLoadedFiles() {
    return store.getLoadedFiles().stream().map(file -> file.locatedAt.toString())
        .collect(Collectors.toSet());
  }

  /**
   * Only checks whether they have the same <code>identifier</code>.
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof SchemaFile) {
      SchemaFile otherSchema = (SchemaFile) other;
      return id.equals(otherSchema.id);
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return id.toString();
  }
  
  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
