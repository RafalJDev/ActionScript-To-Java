package newash.actionscript.stage.stages;

import newash.actionscript.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class ImportStage extends Stage {

  private Map<String, String> mapWithImports = new HashMap<>();
  
  private Map<String, String> mapWithCanditatesToImport = new HashMap<>();

  private static ImportStage ourInstance = new ImportStage();

  public static ImportStage getInstance() {
    return ourInstance;
  }

  private ImportStage() {
    name = "ImportStage";
    code = new StringBuilder();
    prepareMap();
  }

  private void prepareMap() {
    
  }
}
