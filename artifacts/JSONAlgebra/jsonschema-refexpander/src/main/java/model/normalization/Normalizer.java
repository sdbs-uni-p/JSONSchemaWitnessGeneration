package model.normalization;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dto.LoadSchemaDTO;
import exception.InvalidIdentifierException;
import util.SchemaUtil;
import util.URIUtil;

/**
 * Can normalize a File of Json-Schema. References can be within the schema and absolute or relative
 * to the File.
 * 
 * @author Lukas Ellinger
 */
public class Normalizer {
  private SchemaFile rootSchema;

  /**
   * 
   * @param file which should be normalized.
   * @param config of how schema should be loaded.
   */
  public Normalizer(File file, LoadSchemaDTO config) {
    rootSchema = new SchemaFile(file, config);
  }

  /**
   * This constructor shoud be used if you want to directly use a JsonObject as Schema without File and Directory.
   * Using this constructor you can give a JsonObject as first Parameter and get an JsonObject as Result.
   *
   * @param schema
   * @param config
   *
   * @author Luca Escher
   */
  public Normalizer(JsonObject schema, LoadSchemaDTO config){
    rootSchema = new SchemaFile(schema, config);
  }

  /**
   * 
   * @param file which should be normalized
   * @param id location of where the file is from. Is used as id if no id is declared in the schema.
   * @param config of how schema should be loaded.
   */
  public Normalizer(File file, URI id, LoadSchemaDTO config) {
    rootSchema = new SchemaFile(file, id, config);
  }

  public SchemaFile getRootSchema() {
    return rootSchema;
  }

  public void setSchema(SchemaFile rootSchema) {
    this.rootSchema = rootSchema;
  }

  /**
   * Normalizes the stored schema and also returns it.
   * 
   * @return normalized schema.
   */
  public JsonObject normalize() {
    List<Entry<String, JsonElement>> defsList = new ArrayList<Entry<String, JsonElement>>();
    traverseTree(rootSchema.getObject(), defsList, rootSchema);

    JsonObject defs = SchemaUtil.getDefinitions(rootSchema.getObject());
    for (Entry<String, JsonElement> entry : defsList) {
      defs.add(entry.getKey(), entry.getValue());
    }

    if (!rootSchema.getObject().has("definitions")) {
      if (!defs.entrySet().isEmpty()) {
        rootSchema.getObject().add("definitions", defs);
      }
    }
    SchemaUtil.removeIds(rootSchema.getObject());
    return rootSchema.getObject();
  }

  /**
   * Traverses <code>element</code> in preorder. <code>element</code> is the root. While traversing
   * references are normalized. All <code>JsonElements</code> which therefore need to be added to
   * the "definitions"-section are stored in <code>defList</code>.
   * 
   * @param element root of the schema-tree.
   * @param defsList all <code>JsonElements</code> which need to be added to the
   *        "definitions"-section get stored in it. May be empty at the beginning. Gets filled
   *        recursively.
   * @param schema in which current File and corresponding <code>JsonObject</code> with additional
   *        information are stored.
   */
  private void traverseTree(JsonElement element, List<Entry<String, JsonElement>> defsList,
      SchemaFile schema) {
    // elements should not be visited twice
    if (!schema.alreadyVisited(element)) {
      if (element.isJsonObject()) {
        traverseObject(element.getAsJsonObject(), defsList, schema);
      } else if (element.isJsonArray()) {
        traverseArray(element.getAsJsonArray(), defsList, schema);
      }
    }
  }

  /**
   * Traverses all children of <code>object</code>. While traversing references are normalized. All
   * <code>JsonElements</code> which therefore need to be added to the "definitions"
   * <code>JsonElement</code> are stored in <code>defList</code>.
   * 
   * @param object current node.
   * @param defsList all <code>JsonElements</code> which need to be added to the "definitions"
   *        <code>JsonElement</code> get stored in it. May be empty at the beginning. Gets filled
   *        recursively.
   * @param schema in which current File and corresponding <code>JsonObject</code> with additional
   *        information are stored.
   */
  private void traverseObject(JsonObject object, List<Entry<String, JsonElement>> defsList,
      SchemaFile schema) {
    schema.addVisited(object);
    try {
      schema.setResScope(SchemaUtil.getId(object, schema.getDraft()));
    } catch (URISyntaxException e) {
      throw new InvalidIdentifierException(schema + " has an identifier which is not a valid URI");
    }

    for (Entry<String, JsonElement> entry : object.entrySet()) {
      if (!(entry.getKey().equals("enum") && entry.getValue().isJsonArray())) {
        if (entry.getValue().isJsonPrimitive() && entry.getKey().equals("$ref")) {
          String refString = entry.getValue().getAsString();
          URI ref;
          try {
            ref = URIUtil.toURI(refString);
          } catch (URISyntaxException e) {
            throw new InvalidIdentifierException(
                schema + " has a reference which is not a valid URI: " + refString);
          }
          FilePointer pointer = new FilePointer(ref, schema);

          JsonElement refElement = pointer.getRefElement();
          if (!pointer.referencesDefChild()) {
            if (!pointer.referencesRoot()) {
              String convertedPointer = pointer.convertPointer();
              convertedPointer = FilePointer.getUnescaped(convertedPointer);
              entry.setValue(new JsonPrimitive("#/definitions/" + convertedPointer));

              Entry<String, JsonElement> defsEntry =
                  new AbstractMap.SimpleEntry<>(convertedPointer, refElement);
              defsList.add(defsEntry);

              traverseTree(refElement, defsList, pointer.getSchema());
            } else {
              entry.setValue(new JsonPrimitive("#"));
            }
          } else {
            entry.setValue(new JsonPrimitive(pointer.getRef()));
          }
        } else {
          traverseTree(entry.getValue(), defsList, schema);
        }
      }
    }
    schema.oneScopeUp();
  }

  /**
   * Traverses all elements of <code>array</code>. While traversing references are normalized. All
   * <code>JsonElements</code> which therefore need to be added to the "definitions"
   * <code>JsonElement</code> are stored in <code>defList</code>.
   * 
   * @param array current node.
   * @param defsList all <code>JsonElements</code> which need to be added to the "definitions"
   *        <code>JsonElement</code> get stored in it. May be empty at the beginning. Gets filled
   *        recursively.
   * @param schema in which current File and corresponding <code>JsonObject</code> with additional
   *        information are stored.
   */
  private void traverseArray(JsonArray array, List<Entry<String, JsonElement>> defsList,
      SchemaFile schema) {
    schema.addVisited(array);
    for (JsonElement element : array) {
      traverseTree(element, defsList, schema);
    }
  }
}
