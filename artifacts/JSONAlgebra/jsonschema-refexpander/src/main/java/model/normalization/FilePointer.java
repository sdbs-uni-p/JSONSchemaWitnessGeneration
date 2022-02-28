package model.normalization;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map.Entry;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import exception.InvalidFragmentException;
import exception.InvalidIdentifierException;
import model.Pointer;
import util.SchemaUtil;
import util.URIUtil;

/**
 * Extends <code>Pointer</code> with being capable of resolving references to other files.
 * 
 * @author Lukas Ellinger
 */
public class FilePointer extends Pointer {
  private SchemaFile schema;

  /**
   * 
   * @param ref JSON-Reference
   * @param schema of where JSON-Reference is from.
   */
  public FilePointer(URI ref, SchemaFile schema) {
    super(ref);
    this.schema = schema;
  }

  public SchemaFile getSchema() {
    return schema;
  }

  /**
   * Checks whether the pointer references the root schema of the root file.
   * 
   * @return <code>true</code>, if root is referenced. <code>false</code> if not.
   */
  @Override
  public boolean referencesRoot() {
    return schema.isRootFile()
        && (URIUtil.removeTrailingHash(schema.getResScope().resolve(ref)).equals(schema.getId())
            || ref.equals("#"));
  }

  /**
   * Checks whether the pointer references a direct child of "definitions" of the root file.
   * 
   * @return <code>true</code>, if a direct child of "definitions" is referenced. <code>false</code>
   *         if not.
   */
  @Override
  public boolean referencesDefChild() {
    return schema.isRootFile() && ref.startsWith("#/definitions/") && ref.split("/").length == 3;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String convertPointer() {
    if (!schema.isRootFile()) {
      return convertPointerToOtherFile();
    } else {
      if (ref.contains("#/") && !ref.startsWith("#/")) {
        return convertPointerToIdAndRef();
      } else if (ref.startsWith("#/")) {
        return super.convertPointer();
      } else {
        return convertPointerToId();
      }
    }
  }

  /**
   * Gets converted pointer if its reference is to another file than root-file.
   * 
   * @return converted pointer.
   */
  private String convertPointerToOtherFile() {
    String convertedRef = schema.getRelIdentifier();
    if (ref.startsWith("#/")) {
      convertedRef += ref.replace("#/", "_");
    } else if (!ref.equals("#")) {
      if (ref.startsWith("#")) {
        convertedRef += "_id_" + ref.substring(1);
      } else {
        convertedRef += "_id_" + ref;
      }
    }

    convertedRef = convertedRef.replace("/", "_");
    return convertedRef;
  }

  /**
   * Gets converted pointer if it consists of a reference to a schema inside root-file and a
   * JSON-Pointer reference inside of this.
   * 
   * @return converted pointer.
   */
  private String convertPointerToIdAndRef() {
    Optional<Path> rootPath =
        Optional.ofNullable(Paths.get(URIUtil.getParentURI(schema.getRoot()).getPath()));
    Path idPath = new File(schema.getResScope().getPath()).toPath();

    if (rootPath.isEmpty()) {
      if (idPath.toString().equals("")) {
        return super.convertPointer();
      } else {
        return idPath + "_" + super.convertPointer();
      }
    } else {
      String relId = rootPath.get().relativize(idPath).toString();
      String pointer = ref.substring(ref.indexOf("#/"));

      ref = pointer;
      return relId + "_" + super.convertPointer();
    }
  }

  /**
   * Gets converted pointer if it is a reference to an id inside of root-file.
   * 
   * @return converted pointer.
   */
  private String convertPointerToId() {
    Optional<String> schemeId = Optional.ofNullable(schema.getId().getScheme());
    Optional<String> schemeRef = Optional.ofNullable(schema.getResScope().getScheme());
    Optional<String> authorityId = Optional.ofNullable(schema.getId().getAuthority());
    Optional<String> authorityRef = Optional.ofNullable(schema.getResScope().getAuthority());

    if (!schemeId.equals(schemeRef) || !authorityId.equals(authorityRef)) {
      ref = schema.getResScope().toString();
    } else {
      URI rel = URIUtil.getParentURI(schema.getId()).relativize(schema.getResScope());
      Optional<String> path = Optional.ofNullable(rel.getPath());
      Optional<String> fragment = Optional.ofNullable(rel.getFragment());

      if (schema.getId().equals(URIUtil.removeFragment(schema.getResScope()))) {
        ref = "";
      } else {
        ref = path.orElse("");
      }

      if (fragment.isPresent()) {
        ref += "#" + fragment.get();
      }
      if (ref.startsWith("/")) {
        ref = ref.substring(1);
      }
    }
    return super.convertPointer();
  }

  /**
   * Gets the element which is referenced by the stored reference. Before, the stored
   * <code>SchemaFile</code> is updated to reference the corresponding file and <code>ref</code> is
   * updated to only point to the <code>JsonElement</code> within the corresponding file. Subtype of
   * <code>IllegalReferenceException</code> is thrown if reference is invalid.
   * 
   * @return referenced element.
   */
  public JsonElement getRefElement() {
    String idToSearch = schema.getResScope().resolve(ref).toString();
    schema.setResScopeToTopLevel();
    JsonElement element = searchForId(idToSearch, schema.getObject());
    if (element != null) {
      return element;
    }
    schema.oneScopeUp();

    updatePointer();

    if (ref.startsWith("#/") || ref.equals("#")) {
      return getRecursivePointerElement(ref, schema.getObject());
    } else {
      JsonElement refElement = searchForId(ref, schema.getObject());

      if (refElement != null) {
        return refElement;
      } else {
        throw new InvalidFragmentException("Id" + ref + " does not exist");
      }
    }
  }

  @Override
  protected JsonElement getRecursivePointerElement(String pointer, JsonElement element) {
    if (pointer.equals("#")) {
      return element;
    } else {
      String currentLevel = pointer.substring(2);
      currentLevel = currentLevel.split("/")[0];
      pointer = StringUtils.replaceOnce(pointer, "/" + currentLevel, "");
      currentLevel = getUnescaped(currentLevel);

      JsonElement child;
      if (element.isJsonObject()) {
        JsonObject elementObj = element.getAsJsonObject();
        if (elementObj.has(currentLevel)) {
          try {
            schema.setResScope(SchemaUtil.getId(elementObj, schema.getDraft()));
          } catch (URISyntaxException e) {
            throw new InvalidIdentifierException(schema + " has an invalid identifier in it");
          }
          child = element.getAsJsonObject().get(currentLevel);
        } else {
          throw new InvalidFragmentException("No element referenced by " + ref);
        }
      } else if (element.isJsonArray()) {
        try {
          int i = Integer.parseInt(currentLevel);

          if (element.getAsJsonArray().size() <= i) {
            throw new InvalidFragmentException("No element referenced by " + ref);
          }
          child = element.getAsJsonArray().get(i);
        } catch (NumberFormatException e) {
          throw new InvalidFragmentException(ref + " includes array index. But index is no number");
        }
      } else {
        throw new InvalidFragmentException("No element referenced by " + ref);
      }

      return getRecursivePointerElement(pointer, child);
    }
  }

  private JsonElement searchForId(String id, JsonElement element) {
    if (element.isJsonObject()) {
      return searchForId(id, element.getAsJsonObject());
    } else if (element.isJsonArray()) {
      return searchForId(id, element.getAsJsonArray());
    } else {
      return null;
    }
  }

  private JsonElement searchForId(String id, JsonObject object) {
    URI objectId;
    try {
      objectId = SchemaUtil.getId(object, schema.getDraft());
    } catch (URISyntaxException e) {
      throw new InvalidIdentifierException(schema + " has an invalid identifier in it");
    }
    schema.setResScope(objectId);

    URI idURI = URIUtil.removeTrailingHash(schema.getResScope().resolve(id));
    if (schema.getResScope().equals(idURI)) {
      if (idURI.equals(schema.getId())) {
        updatePointer();
      }
      return object;
    }

    URI idURIWithoutFragment = URIUtil.removeFragment(idURI);
    if (schema.getResScope().equals(idURIWithoutFragment)) {
      String pointer = idURI.getFragment();

      if (pointer.startsWith("/") || pointer.equals("")) {
        JsonElement element = getRecursivePointerElement("#" + pointer, object);

        if (schema.getId().equals(idURIWithoutFragment)) {
          updatePointer();
        }

        return element;
      }
    }

    for (Entry<String, JsonElement> entry : object.entrySet()) {
      if (!(entry.getKey().equals("enum") && entry.getValue().isJsonArray())) {
        JsonElement element = searchForId(id, entry.getValue());
        if (element != null) {
          return element;
        }
      }
    }
    schema.oneScopeUp();
    return null;
  }

  private JsonElement searchForId(String id, JsonArray array) {
    for (JsonElement jsonElement : array) {
      JsonElement element = searchForId(id, jsonElement);
      if (element != null) {
        return element;
      }
    }
    return null;
  }

  /**
   * Updates the pointer. Therefore its <code>SchemaFile</code> is updated to reference the
   * corresponding file and <code>ref</code> is updated to only point to the
   * <code>JsonElement</code> within the corresponding file.
   */
  private void updatePointer() {
    if (!ref.startsWith("#")) {
      URI identifier;
      try {
        identifier = new URI(ref);
        Optional<String> fragment = Optional.ofNullable(identifier.getFragment());
        identifier = URIUtil.removeFragment(identifier);
        ref = "#" + fragment.orElse("");
      } catch (URISyntaxException e) {
        throw new InvalidIdentifierException(ref + " has an invalid identifier");
      }

      schema = schema.getLoadedFile(identifier);
    }
  }

  @Override
  public String toString() {
    return schema + " " + super.toString();
  }
}
