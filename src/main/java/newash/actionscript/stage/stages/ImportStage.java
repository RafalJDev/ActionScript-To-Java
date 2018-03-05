package newash.actionscript.stage.stages;

import newash.actionscript.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class ImportStage extends Stage {

  private Map<String, String> mapWithCandidatesToImport = new HashMap<>();

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
    mapWithCandidatesToImport.put("Label", "import pl.logicsynergy.components.Label;");
    mapWithCandidatesToImport.put("TextInput", "import pl.logicsynergy.components.TextInput;");
    mapWithCandidatesToImport.put("ComboBox", "import pl.logicsynergy.components.ComboBox;");
    mapWithCandidatesToImport.put("UiDictionary", "import pl.logicsynergy.components.ui.UiDictionary;");
    mapWithCandidatesToImport.put("View", "import pl.logicsynergy.components.mdi.View;");
    mapWithCandidatesToImport.put("DBQuery", "import pl.logicsynergy.database.DBQuery;");
    mapWithCandidatesToImport.put("Element", "import org.w3c.dom.Element;");
    mapWithCandidatesToImport.put("DataMap", "import logica.data.DataMap;");
    mapWithCandidatesToImport.put("Grid", "import pl.logicsynergy.components.grid.Grid;");
    mapWithCandidatesToImport.put("XMLUtils", "import pl.logicsynergy.utils.XMLUtils;;");
  }
}
