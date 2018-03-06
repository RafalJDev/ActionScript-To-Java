package newash.parser.stages.coponents;

import newash.actionscript.stage.stages.ActionScriptStage;
import newash.actionscript.stage.stages.ComponentsStage;
import newash.actionscript.stage.stages.ImportStage;
import newash.actionscript.stage.stages.UiDesignStage;
import newash.io.readers.current.CodeLineEntity;
import newash.parser.stages.Parser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jaszczynski.Rafal on 04.03.2018.
 */
public class ComponentsParser extends Parser {

  ActionScriptStage actionScriptStage;
  ComponentsStage componentsStage;
  UiDesignStage uiDesignStage;
  ImportStage importStage = ImportStage.getInstance();

  private String idValueForCurrentComponent = "";
  private String methodBodyForDataExchange = "";
  private String dataExchangeMethodName = "";

  private Map<String, String> replaceMap = new HashMap<>();

  public ComponentsParser() {
    codeLineEntity = CodeLineEntity.getInstance();
    actionScriptStage = ActionScriptStage.getInstance();
    componentsStage = ComponentsStage.getInstance();
    uiDesignStage = UiDesignStage.getInstance();

    prepareReplaceMap();
  }

  @Override
  public void parseThisStage() {
    line = codeLineEntity.getLine();

    getIdValueForComponent();
    createDataExchangeMethod();

    findComponents();
    simpleReplaceAll();

    componentsStage.appendCode(line + "\n");
  }

  public void findComponents() {
    if (isFoundRegex("id=\"[^\"]*\"")) {
      String id = found.substring(4, found.length() - 1);
      String componentClass = "";
      if (isFoundRegex("<\\w:(\\w+) \\w", 1)) {
        componentClass = found.trim();
      }

      for (String candidateForComponent : actionScriptStage.getCandidatesForComponents()) {
        if (id.equals(candidateForComponent)) {
          actionScriptStage.addComponentThatNeedField(componentClass, id);
          importStage.addCandidateToSet(componentClass);
          break;
        }
      }
    }
  }

  public void getIdValueForComponent() {
    if (isFoundRegex("id=\"(\\w+)\"", 1)) {
      idValueForCurrentComponent = found;
    }
  }

  public void createDataExchangeMethod() {
    if (isFoundRegex("dataExchange=\"\\{\\{(.*)\\}\\}", 1)) {
      createMethodNameAndReplaceItWithOldDataExchange();
      String[] arguments = found.trim().split(",");

      for (String argument : arguments) {
        String[] keyValueArray = argument.replaceAll("'", "").split(":");
        if (keyValueArray.length != 2) {
          throw new IllegalStateException("keyValueArray should have 2 objects, but have: " + keyValueArray.length);
        }
        String key = keyValueArray[0].trim();
        String value = "\"" + keyValueArray[1].trim() + "\"";

        String putCall = "\t   dataMap.put(" + key + ", " + value + ");\n";
        methodBodyForDataExchange += putCall;
      }
      sumUpDataExchangeMethodAndToJavaMethods();
    }
  }

  public void createMethodNameAndReplaceItWithOldDataExchange() {
    dataExchangeMethodName = "get" +
       ((Character) idValueForCurrentComponent.charAt(0)).toString().toUpperCase()
       + idValueForCurrentComponent.substring(1) + "()";
    line = line.replace(found, dataExchangeMethodName)
       .replaceAll("\\{\\{", "{")
       .replaceAll("\\}\\}", "}")
       .replaceAll("\\(\\)", "");

  }

  public void sumUpDataExchangeMethodAndToJavaMethods() {
    String comments =
       "\n\t /**\n" +
          "\t * Zwrócenie DataMap dla komponentu " + idValueForCurrentComponent + "\n" +
          "\t *\n" +
          "\t * @return Data Exchange\n" +
          "\t */\n";

    String methodDeclarationAndBegining =
       "\t public DataMap " + dataExchangeMethodName + "\n" +
          "\t {\n" +
          "\t   DataMap dataMap = new DataMap();\n";

    String closeMethodBody =
       "\t   return dataMap;\n" +
          "\t }\n";

    String methodToAdd =
       comments + methodDeclarationAndBegining + methodBodyForDataExchange + closeMethodBody;
    actionScriptStage.getDbMethods().add(methodToAdd);
    methodBodyForDataExchange = "";
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