package newash.parser.stages.fxDeclaration;

import newash.actionscript.stage.stages.ActionScriptStage;
import newash.actionscript.stage.stages.FxDeclarationStage;
import newash.io.reader.current.LineEntity;
import newash.parser.stages.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Created by Jaszczynski.Rafal on 03.03.2018.
 */
public class FxDeclarationParser extends Parser {

  private boolean isIdForFieldsFound = false;
  private List<String> dbQueryAtributes = new ArrayList<>();

  private boolean isDbQueryElement = false;
  private boolean itXmlFields = false;

  FxDeclarationStage fxDeclarationStage;
  ActionScriptStage actionScriptStage;

  public FxDeclarationParser() {
    lineEntity = LineEntity.getInstance();
    fxDeclarationStage = FxDeclarationStage.getInstance();
    actionScriptStage = ActionScriptStage.getInstance();
  }

  @Override
  public void parseThisStage() {
    line = lineEntity.getLine();

    if (line.contains("db:DBQuery") || isDbQueryElement) {
      parseDbQuery();
    } else if (line.contains("<fx:XML") || itXmlFields) {
      itXmlFields = true;
      parseXmlFields();
    }

    fxDeclarationStage.getCode().append(line + "\n");
  }

  public void parseDbQuery() {
    isDbQueryElement = true;

    Pattern pattern = Pattern.compile("\\w+=\"[a-zA-Z0-9_, {}]+\"");
    Matcher matcher = pattern.matcher(line);
    while (matcher.find()) {
      found = matcher.group();
      if (found.contains("sqlTable")) {
        continue;
      }
      dbQueryAtributes.add(found.trim());
    }
    if (line.contains("/>")) {
      isDbQueryElement = false;
      if (!dbQueryAtributes.isEmpty()) {
        createMethodForDbQuery();
      }
    }
  }

  public void parseXmlFields() {
    if (line.contains("</fx:XML>")) {
      itXmlFields = false;
      isIdForFieldsFound = false;
      if (!XmlField.getXmlFieldLines().isEmpty()) {
        createMethodForFields();
      }
    } else {
      if (isFoundRegex("\\w+=\"\\w+\"") && !isIdForFieldsFound) {
        String idValue = found;
        idValue = idValue.substring(idValue.indexOf("\"") + 1, idValue.length() - 1);
        idValue = "get" + ((Character) idValue.charAt(0)).toString().toUpperCase() + idValue.substring(0);
        XmlField.setId(idValue);
        isIdForFieldsFound = true;
      } else {
        String plusSign = "";
        if (!line.contains("<fields>")) {
          plusSign = "\t\t+ ";
        }
        XmlField.getXmlFieldLines().add(plusSign + "\"" + line + "\"");
      }
    }
  }

  public void createMethodForDbQuery() {

    String firstString = dbQueryAtributes.get(0);
    String methodName = firstString.substring(firstString.indexOf("\"") + 1, firstString.lastIndexOf("\""));
    String fromTable = dbQueryAtributes.stream()
       .filter(attribute -> attribute.contains("sqlFrom"))
       .findFirst()
       .get();
    line = fromTable;
    isFoundRegex("\"(\\w+)\"", 1);

    String methodString = "/**\n" +
       " * Zwrócenie obiektu DBQuery dla tabeli " + found + "\n" +
       " * \n" +
       " * @return From " + found + "\n" +
       " */\n" +
       " public DBQuery " + methodName + "()\n" +
       " {\n" +
       "    DBQuery result = new DBQuery();\n";

    for (int i = 1; i < dbQueryAtributes.size(); i++) {
      String attribute = dbQueryAtributes.get(i);
      if (attribute.contains("sqlField=\"")) {

        line = attribute;
        foundRegex("\\{(\\w+)\\}", 1);
        String sqlFieldToUpperCaseFirstLetter = ((Character) found.charAt(0)).toString().toUpperCase() + found.substring(1);
        attribute = attribute.replace(found, "get" + sqlFieldToUpperCaseFirstLetter + "()");
        attribute = attribute.replace("sqlField", "setSqlField")
           .replaceAll("=\"", "(")
           .replaceAll("\"", ")")
           .replaceAll("[{}]", "");
//        setSqlField(getDBQueryFields())
      }
      methodString += "    result." + attribute.trim() + ";\n";
    }
    methodString += "    return result;\n }\n\n";
    actionScriptStage.getDbMethods().add(methodString);
    dbQueryAtributes.clear();
  }

  public void createMethodForFields() {

    String methodString =
       "\t /*\n" +
       "\t * Metoda wyciągająca nazwy pól z XMLa\n" +
       "\t * \n" +
       "\t * @return obiekt Element zawierający nazwy pól\n" +
       "\t */\n" +
       "\t private Element " + XmlField.getId() + "()\n" +
       "\t {\n" +
          "\t\treturn XMLUtils.getXMLElement(";

    for (String fieldLine : XmlField.getXmlFieldLines()) {
      if (!fieldLine.matches(".*\\w+.*")) {
        fieldLine = "";
      }
      methodString += fieldLine + "\n";
    }

    methodString += "\t  );\n\t }\n\n";
    actionScriptStage.getDbMethods().add(methodString);
    XmlField.setId("");
    XmlField.getXmlFieldLines().clear();
  }


}