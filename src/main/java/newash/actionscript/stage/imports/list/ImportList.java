package newash.actionscript.stage.imports.list;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ImportList {

  private static ImportList ourInstance = new ImportList();

  public static ImportList getInstance() {
    return ourInstance;
  }

  private ImportList() {
  }

  private Set<String> importSet = new HashSet<>();
  private Map<String, String> importMap = new HashMap<>();

  public Set<String> getImportSet() {
    return importSet;
  }

  public void setImportSet(Set<String> importSet) {
    this.importSet = importSet;
  }

  public Map<String, String> getImportMap() {
    return importMap;
  }

  public void setImportMap(Map<String, String> importMap) {
    this.importMap = importMap;
  }

  public void addToSet(String add) {
    importSet.add(add);
  }

  public void putOnMap(String key, String value) {
    importMap.put(key, value);
  }
}
