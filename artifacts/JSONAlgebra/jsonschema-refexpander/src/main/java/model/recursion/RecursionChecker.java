package model.recursion;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import exception.InvalidIdentifierException;
import model.Pointer;
import util.SchemaUtil;
import util.URIUtil;

/**
 * Can check whether Json-Schema are recursive or not. It is differed between guarded and unguarded
 * recursion.
 * 
 * @author Lukas Ellinger
 */
public class RecursionChecker {

  private JsonObject schema;

  /**
   * 
   * @param schema needs to be normalized in order to get the right result when checking for
   *        recursion.
   */
  public RecursionChecker(String schema) {
    this.schema = new Gson().fromJson(schema, JsonObject.class);
  }
  
  public RecursionChecker(File file) throws IOException {
    this(FileUtils.readFileToString(file, "UTF-8"));
  }

  /**
   * 
   * @param schema needs to be normalized in order to get the right result when checking for
   *        recursion.
   */
  public RecursionChecker(JsonObject schema) {
    this.schema = schema;
  }

  public JsonObject getSchema() {
    return schema;
  }

  public void setSchema(JsonObject schema) {
    this.schema = schema;
  }

  /**
   * Checks whether given normalized <code>schema</code> is recursive or not. Also differs between
   * guarded and unguarded recursion. <code>schema</code> gets normalized at first.
   * 
   * @return <code>RECURSION</code>, if <code>schema</code> is recursive, <code>GUARDED</code>, if
   *         guarded recursive and else <code>NONE</code>.
   */
  public RecursionType checkForRecursion() {
    boolean guarded = false;

    for (Entry<String, JsonElement> entry : SchemaUtil.getDefinitions(schema).entrySet()) {
      RecursionType type = checkForCycle(entry.getValue());

      if (type.equals(RecursionType.RECURSION)) {
        return RecursionType.RECURSION;
      } else if (type.equals(RecursionType.GUARDED)) {
        guarded = true;
      }
    }

    List<Pointer> allRefs = getEdges(schema);
    for (Pointer pointer : allRefs) {
      if (pointer.referencesRoot()) {
        if (pointer.isGuarded()) {
          return RecursionType.GUARDED;
        } else {
          return RecursionType.RECURSION;
        }
      }
    }

    if (guarded) {
      return RecursionType.GUARDED;
    } else {
      return RecursionType.NONE;
    }
  }

  /**
   * Checks whether there exists a cycle for specific <code>JsonElement</code> in
   * <code>schema</code>.
   * 
   * @param element
   * @return <code>RECURSION</code>, if <code>element</code> has a reference to itself,
   *         <code>GUARDED</code>, if this reference is guarded and else <code>NONE</code>.
   */
  private RecursionType checkForCycle(JsonElement element) {
    Node node = new Node(element, false);
    List<Node> toBeVisited = new ArrayList<>();
    List<Node> alreadyVisited = new ArrayList<>();

    toBeVisited.add(node);

    while (!toBeVisited.isEmpty()) {
      Node u = toBeVisited.remove(0);

      for (Pointer pointer : getEdges(u.getElement())) {
        Node reachable;
        if (u.isGuarded() || pointer.isGuarded()) {
          reachable = new Node(pointer.getRefElement(schema), true);
        } else {
          reachable = new Node(pointer.getRefElement(schema), false);
        }

        if (reachable.equals(node)) {
          if (reachable.isGuarded()) {
            return RecursionType.GUARDED;
          } else {
            return RecursionType.RECURSION;
          }
        } else {
          if (!alreadyVisited.contains(reachable)) {
            if (toBeVisited.contains(reachable) && !reachable.isGuarded()) {
              toBeVisited.remove(reachable);
            }
            toBeVisited.add(reachable);
          }
        }
      }
      alreadyVisited.add(u);
    }

    return RecursionType.NONE;
  }

  /**
   * Gets all edges to other nodes of specific <code>JsonElement</code>. These are returned as
   * <code>Pointer</code> in which is also stored, whether these are guarded or not.
   * 
   * @param element to get edges from.
   * @return all edges of <code>element</code>.
   */
  private List<Pointer> getEdges(JsonElement element) {
    return getEdges(element, false);
  }

  /**
   * Gets all edges to other nodes of specific <code>JsonElement</code>. These are returned as
   * <code>Pointer</code> in which is also stored, whether these are guarded or not.
   * 
   * @param element to get edges from.
   * @param guarded <code>true</code>, if parent of <code>element</code> is guarded.
   *        <code>false</code> if not.
   * @return all edges of <code>element</code>.
   */
  private List<Pointer> getEdges(JsonElement element, boolean guarded) {
    if (element.isJsonObject()) {
      return getEdgesObject(element.getAsJsonObject(), guarded);
    } else if (element.isJsonArray()) {
      return getEdgesArray(element.getAsJsonArray(), guarded);
    } else {
      return new ArrayList<Pointer>();
    }
  }

  /**
   * Gets all edges to other nodes of specific <code>JsonObject</code>. These are returned as
   * <code>Pointer</code> in which is also stored, whether these are guarded or not.
   * 
   * @param object to get edges from.
   * @param guarded <code>true</code>, if parent of <code>array</code> is guarded.
   *        <code>false</code> if not.
   * @return all edges of <code>element</code>.
   */
  private List<Pointer> getEdgesObject(JsonObject object, boolean guarded) {
    List<Pointer> pointers = new ArrayList<>();
    
    for (Entry<String, JsonElement> entry : object.entrySet()) {
      if (entry.getValue().isJsonPrimitive() && entry.getKey().equals("$ref")) {
        String refString = entry.getValue().getAsString();
        URI ref;
        try {
          ref = URIUtil.toURI(refString);
        } catch (URISyntaxException e) {
          throw new InvalidIdentifierException("reference which is not a valid URI: " + refString);
        }
        Pointer pointer = new Pointer(ref, guarded);
        pointers.add(pointer);
      } else {
        if (isEntryGuarded(entry)) {
          pointers.addAll(getEdges(entry.getValue(), true));
        } else {
          pointers.addAll(getEdges(entry.getValue(), guarded));
        }
      }
    }

    return pointers;
  }

  /**
   * Gets all edges to other nodes of specific <code>JsonArray</code>. These are returned as
   * <code>Pointer</code> in which is also stored, whether these are guarded or not.
   * 
   * @param array to get edges from.
   * @param guarded <code>true</code>, if parent of <code>array</code> is guarded.
   *        <code>false</code> if not.
   * @return all edges of <code>array</code>.
   */
  private List<Pointer> getEdgesArray(JsonArray array, boolean guarded) {
    List<Pointer> pointers = new ArrayList<>();
    for (JsonElement jsonElement : array) {
      pointers.addAll(getEdges(jsonElement, guarded));
    }

    return pointers;
  }

  /**
   * Checks whether specific <code>Entry</code> is guarded because of his own key.
   * 
   * @param entry to be checked.
   * @return <code>true</code>, if entry is guarded. <code>false</code> if not.
   */
  private boolean isEntryGuarded(Entry<String, JsonElement> entry) {
    switch (entry.getKey()) {
      case "allOf":
      case "anyOf":
      case "oneOf":
      case "not":
        return false;
      default:
        return true;
    }
  }
}
