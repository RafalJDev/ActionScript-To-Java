package newash.parser.stages.actionscript;

import newash.actionscript.stage.stages.ActionScriptStage;
import newash.actionscript.stage.stages.UiDesignStage;
import newash.io.code.IOEntity;
import newash.io.reader.current.LineEntity;
import newash.parser.stages.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jaszczynski.Rafal on 03.03.2018.
 */
public class ActionScriptParser extends Parser {

  private Map<String, String> replaceMap = new HashMap<>();
  private boolean thereIsRoCall = false;
  private String methodToCallAfterRo = "";
  private boolean letsCreateDataMap = false;
  private List<String> methodsForEventListeners = new ArrayList<>();

  UiDesignStage uiDesignStage;
  ActionScriptStage actionScriptStage;
  IOEntity ioEntity;

  public int braces = 0;
  private boolean isClassAndConstructorAdded = false;

  public ActionScriptParser() {
    uiDesignStage = UiDesignStage.getInstance();
    actionScriptStage = ActionScriptStage.getInstance();
    ioEntity = IOEntity.getInstance();
    lineEntity = LineEntity.getInstance();

    prepareReplaceMap();
  }

  @Override
  public void parseThisStage() {

    line = lineEntity.getLine();

    if (!line.contains("/*")) {
      simpleReplaceAll();
    }

    replaceAsToCast();

    isNanToNull();

    ifLineIsClassNameThenCreateFieldWithThatClass();

    changeToSetCall();

    if (thereIsRoCall && line.contains("ro.call")) {

      String spaces = line.substring(0, line.indexOf("r") - 1);
      String callArguments = "";
      if (isFoundRegex("\\(.*\\)")) {
        callArguments = found.substring(1);

        line = spaces + "DataMap dataMap = ROUiEventService.call(" + callArguments + ");";
        if (!methodToCallAfterRo.isEmpty()) {
          line += "\n" + spaces + methodToCallAfterRo + "()";
        }
      }
      thereIsRoCall = false;
      return;
    }
    if (line.contains("ROUiEventService") && !line.contains(" import ")) {
      thereIsRoCall = true;

      if (isFoundRegex("\\(.*\\)")) {
        methodToCallAfterRo = found.substring(1, found.length() - 2);
        line = "";
        return;
      }
    }


    if (line.trim().startsWith("for") && line.contains(" in ")) {
      line = line.replace(" in ", " : ");
    }

//        isFoundRegex("\\w* = [{]");
    if (line.contains(" = {")) {
      letsCreateDataMap = true;
    }
    if (letsCreateDataMap) {

    }

    convertFunctionToJavaMethod();

    if (line.contains("override")) {
      line = "  @Override\n  " + line.replace("override ", "").trim();
    }
    if (line.contains("var")) {
      if (isFoundRegex(":[a-zA-Z0-9]*\\s{0}")) {
        line = line.replace("var", found.replace(":", ""));
        line = line.replace(found, "");
      }
    }

    if (line.contains("protected void load")) {
      line = "  /** Nadpisana metoda load z klasy bazowej */\n" + line;
      line = line.replace("protected void load", "public void load");
    }
    if (line.contains("addEventListener(")) {
      if (line.contains(")")) {
        methodsForEventListeners.add(line.substring(line.indexOf(",") + 2, line.lastIndexOf(")")).trim());
        line = line.replace(")", "(event))");
        line = line.replace(", ", ", event -> ");
        line = line.replaceAll("\"valueChanged\"", "BaseEvent.VALUE_CHANGED");
        line = line.replaceAll("\"indexChanged\"", "BaseEvent.VALUE_CHANGED");
      }
    }
    for (String method : methodsForEventListeners) {
      if (line.contains(method) && line.contains(" void ")) {
        String oldArgument = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
        String newArgument = "BaseEvent event";
        line = line.replace(oldArgument, newArgument);
        break;
      }
    }

    if (isFoundRegex("\\s{3}\\w+\\.")) {
      actionScriptStage.getCandidatesForComponents()
         .add(found.trim().replace(".", ""));
    }

    appendClassAndConstructor();

    actionScriptStage.getCode().append(line + "\n");
  }

  private void convertFunctionToJavaMethod() {
    if (line.contains(" function ")) {
      if (!line.contains(", function ")) {
        int lastIndexOfColon = line.lastIndexOf(":");
        String returnTypeWithoutSpace = line.substring(lastIndexOfColon + 1)
           .trim();
        line = line.substring(0, lastIndexOfColon);
        line = line.replace(" function ", " " + returnTypeWithoutSpace + " ");

        //TODO ucina ostatnią literę, gdy są nawiasy, a to nie jest funkcja
        if (!line.matches(".*\\(\\).*")) {
          String bracketsWithArguments = line.substring(line.indexOf("(") + 1, line.lastIndexOf(")"));
          line = line.replaceAll("\\(.*\\)", "()");
          String[] argumentsWithTypes = bracketsWithArguments.split(",");

          boolean firstArgument = true;
          for (String oneArgument : argumentsWithTypes) {
            String[] split = oneArgument.split(":");
            String addArgument = split[1] + " " + split[0];

            if (firstArgument) {
              line = line.replace("(", "(" + addArgument);
              firstArgument = false;
              continue;
            }
            line = line.replace(")", ", " + addArgument + ")");
          }
        }
      }
    }
  }

  private void countBraces() {

    if (isFoundRegex("\\s*{\\s*")) {
      braces++;
    } else if (isFoundRegex("\\s*}\\s*")) {
      braces--;
    }
  }

  private void appendClassAndConstructor() {
    if (!isClassAndConstructorAdded) {
      String classAndConstructor =
         "@UiDesign(formName = \"" + uiDesignStage.getFormName()
            + "\", guid = \"" + uiDesignStage.getGUID() + "\")\n" +
            "public class " + ioEntity.getFileName() + " extends " + uiDesignStage.getClassToExtend() + " \n" +
            "{\n\n" +
            "APPEND_HERE_FIELDS\n\n" +
            "  /** Konstruktor */\n" +
            "  public " + ioEntity.getFileName() + "()\n" +
            "  {\n" +
            "\tUiCreator.getInstance(self).executeXML();\n" +
            "  }" +
            "\n\n";

      line = classAndConstructor + line;
      isClassAndConstructorAdded = true;
    }
  }

  public void isNanToNull() {
    if (line.contains("(isNaN(")) {
      if (isFoundRegex("\\(isNaN\\((\\w+)\\)", 1)) {
        line = line.replaceAll("isNaN\\(" + found + "\\)", found + " == null");
        line += "//moze trzeba przyrownac do \"\"";
      }
    }
  }

  public void ifLineIsClassNameThenCreateFieldWithThatClass() {
    if (line.matches("\\s*\\w+;")) {
      if (!(line.contains("break;") || line.contains("return;") || line.contains("continue;"))) {
        foundRegex("\\s+(\\w+);", 1);
        String foundWithFirstLetterToLower = ((Character) found.toCharArray()[0]).toString().toLowerCase() + found.substring(1);
        actionScriptStage.getComponentsThatNeedField().put(foundWithFirstLetterToLower, found);
        line = "";
      }
    }
  }

  public void changeToSetCall() {
    if (isFoundRegex("\\w+\\.(\\w+) = .+;", 1)) {
      String createSetToReplace = "\\.set" + ((Character) found.charAt(0)).toString().toUpperCase() + found.substring(1) + "(";
      line = line.replaceAll("\\." + found + " = ", createSetToReplace)
         .replaceAll(";", ");");
    }
  }

  public void replaceAsToCast() {
    if (line.contains(" = ") && line.contains(" as ")) {
      if (isFoundRegex(" as \\w+")) {
        line = line.replaceAll(found, "");
        line = line.replaceAll("= ", "= (" + found.substring(4) + ") ");
      }
    }
  }

  public void addFields() {

    String oldValue = "APPEND_HERE_FIELDS";
    String componentFields = "\n";
    for (Map.Entry<String, String> entry : actionScriptStage.getComponentsThatNeedField().entrySet()) {
      componentFields += "  public " + entry.getValue() + " " + entry.getKey() + ";\t//import or create ME\n\n";
    }
    int hanysIndex = actionScriptStage.getCode().indexOf(oldValue);

    actionScriptStage.getCode().replace(hanysIndex, hanysIndex + oldValue.length(), componentFields);
  }

  public void addMethods() {

    String oldValue = "APPEND_HERE_METHODS";
    String dbMethods = "\n";
    for (String dbMethod : actionScriptStage.getDbMethods()) {
      dbMethods += dbMethod;
    }
    int hanysIndex = actionScriptStage.getCode().indexOf(oldValue);

    actionScriptStage.getCode().replace(hanysIndex, hanysIndex + oldValue.length(), dbMethods);
  }

  public void appendEndClassBraces() {
    actionScriptStage.getCode().append("\nAPPEND_HERE_METHODS\n\n" +
       "}\n");
  }

  private void simpleReplaceAll() {
    for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
      String oldValue = entry.getKey();
      String newValue = entry.getValue();
      line = line.replaceAll(oldValue, newValue);
    }
  }

  public void prepareReplaceMap() {
    replaceMap.put("\\._dbManager", ".getDbManager()");
    replaceMap.put("\\.dbManager\\.", ".getDBManager().");
    replaceMap.put("\\.recordCount", ".getRecordCount()");
    replaceMap.put("\\.length", ".length()");
    replaceMap.put("\\.mode", ".getMode()");
    replaceMap.put("\\.value ", ".getValue() ");
    replaceMap.put("super\\.load\\(event\\);", "super.load();");
    replaceMap.put("\\.getNavigatorContent", ".getContent");
    replaceMap.put("parseInt\\(", "Integer.valueOf(");
    replaceMap.put("swf", "frm");
    replaceMap.put("Swf", "Frm");
    replaceMap.put("SWF", "FRM");
    replaceMap.put("ROManager", "ROUiEventService");
    replaceMap.put("for each", "for");
    replaceMap.put("\\.currentIndex", "\\.getCurrentIndex()");
    replaceMap.put("\\.isResultOK", "\\.isResultOk");
    replaceMap.put("StringUtils\\.IsNullOrEmpty", "StringUtils.isNullOrEmpty");
    replaceMap.put("\\*", "Object");
    replaceMap.put("@id", "getId()");
  }
}