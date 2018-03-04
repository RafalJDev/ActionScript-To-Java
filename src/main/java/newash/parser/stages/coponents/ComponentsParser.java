package newash.parser.stages.coponents;

import newash.actionscript.stage.stages.ActionScriptStage;
import newash.actionscript.stage.stages.ComponentsStage;
import newash.actionscript.stage.stages.UiDesignStage;
import newash.io.reader.current.LineEntity;
import newash.parser.stages.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Jaszczynski.Rafal on 04.03.2018.
 */
public class ComponentsParser extends Parser {

  ActionScriptStage actionScriptStage;
  ComponentsStage componentsStage;
  UiDesignStage uiDesignStage;

//  private List<String> candidatesForComponents = new ArrayList<>();
//  private Map<String, String> componentsThatNeedField = new HashMap<>();

  private Map<String, String> replaceMap = new HashMap<>();

  public ComponentsParser() {
    lineEntity = LineEntity.getInstance();
    actionScriptStage = ActionScriptStage.getInstance();
    componentsStage = ComponentsStage.getInstance();
    uiDesignStage = UiDesignStage.getInstance();

    prepareReplaceMap();
  }

  @Override
  public void parseThisStage() {
    line = lineEntity.getLine();

    simpleReplaceAll();

    componentsStage.getCode().append(line + "\n");
  }

  public void findComponents() {

    if (isFoundRegex("id=\"[^\"]*\"")) {

      String id = found.substring(4, found.length() - 1);

      String componentClass = "";
      if (isFoundRegex("(<c:){1}(\\w+)")) {
        componentClass = found.trim()
           .substring(3, found.length());
      }

      for (String candidateForComponent : actionScriptStage.getCandidatesForComponents()) {
        if (id.equals(candidateForComponent)) {
          actionScriptStage.getComponentsThatNeedField()
             .put(id, componentClass);
          break;
        }
      }
    }
  }

  private void simpleReplaceAll() {
    for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
      String oldValue = entry.getKey();
      String newValue = entry.getValue();
      line = line.replaceAll(oldValue, newValue);
    }
  }

  private void prepareReplaceMap() {
    replaceMap.put("grid.dbManager", "getDbManager");
    replaceMap.put("_dbManager", "getDbManager");
    replaceMap.put("frmFile=\"\\w+.frm\"", ""); //TODO something with sorting hashMap
    replaceMap.put("swf", "frm");
    replaceMap.put("<s:", "<c:");
    replaceMap.put("</s:", "</c:");
    replaceMap.put("<mx:", "</c:");
    replaceMap.put("</mx:", "</c:");
    replaceMap.put("skinClass=\".*\"", "");
    replaceMap.put(".*(c:Scroller).*", "");
    replaceMap.put(".*(" + uiDesignStage.getClassToExtend() + ").*", "");
    //TODO <c:DateField id="dfReplacementBatteryDate" width="185" label="Data wymiany baterii" columnName="ReplacementBatteryDate"
    //TODO          dataField="REPLACEMENT_BATTERY_DATE" dbProvider="{getDbManager}" expandRatio="1" />
  }
}
