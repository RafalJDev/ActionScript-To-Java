package newash.actionscript.stage.imports;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ImportSet {

  private static ImportSet ourInstance = new ImportSet();

  public static ImportSet getInstance() {
    return ourInstance;
  }

  private ImportSet() {
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
