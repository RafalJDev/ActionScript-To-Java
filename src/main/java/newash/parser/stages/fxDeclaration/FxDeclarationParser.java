package newash.parser.stages.fxDeclaration;

import newash.actionscript.stage.stages.FxDeclarationStage;
import newash.io.reader.current.LineEntity;
import newash.parser.stages.Parser;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jaszczynski.Rafal on 03.03.2018.
 */
public class FxDeclarationParser extends Parser {

  private boolean iHaveDoneIt = false;
  private List<String> dbMethods = new ArrayList<>();
  private static List<String> dbQueryAtributes = new ArrayList<>();
  private static int countDbQuery = 0;
  

  FxDeclarationStage fxDeclarationStage;

  public FxDeclarationParser() {
    lineEntity = LineEntity.getInstance();
    fxDeclarationStage = FxDeclarationStage.getInstance();
  }

  @Override
  public void parseThisStage() {
    line = lineEntity.getLine();
    parseXmlFields();
    fxDeclarationStage.getCode().append(line + "\n");
  }

  public void parseXmlFields() {

    if (!iHaveDoneIt) {
      iHaveDoneIt = true;

      if (isFoundRegex("\\w+=\"\\w+\"")) {
        String idRegex = found;
        XmlField.setId(idRegex.substring(idRegex.indexOf("\""), idRegex.length() - 1));
      }
    } else {
      XmlField.getXmlFieldLines().add(line);
    }
  }

  public void parseDbQuery() {

    if (line.contains("<db:DBQuery")) {
      countDbQuery++;
    }
    if (countDbQuery >= 1) {
      while (isFoundRegex("\\w+=\"[a-zA-Z0-9_, {}]+\"")) {
        if (found.contains("sqlTable")) {
          continue;
        }
        dbQueryAtributes.add(found.trim());
      }
      if (line.contains("/>")) {
        if (!dbQueryAtributes.isEmpty()) {
//                    createMethodForDbQuery();
        }
      }
    }
  }
//
//  public static void parseXmlFields() {
//
//    if (!iHaveDoneIt) {
//      iHaveDoneIt = true;
//      pattern = Pattern.compile("\\w+=\"\\w+\"");
//      matcher = pattern.matcher(line);
//
//      if (matcher.find()) {
//        String idRegex = matcher.group();
//        XmlField.setId(idRegex.substring(idRegex.indexOf("\""), idRegex.length() - 1));
//      }
//    } else {
//      XmlField.getXmlFieldLines().add(line);
//    }
//
//    if (line.contains("</fields>"))  {
//      if (!XmlField.xmlFieldLines.isEmpty()) {
////                createMethodForFields();
//        iHaveDoneIt = false;
//      }
//    }
//  }
//
  public void createMethodForDbQuery() {

    String firstString = dbQueryAtributes.get(0);
    String methodName = firstString.substring(firstString.indexOf("\"") + 1, firstString.lastIndexOf("\""));
    String methodString = "/**\n" +
       " * Zwrócenie obiektu DBQuery dla tabeli XXXXXX\n" +
       " * \n" +
       " * @return From XXXXXX\n" +
       " */\n" +
       " public DBQuery " + methodName + "()\n" +
       " {\n" +
       "    DBQuery result = new DBQuery();\n";

    for (int i = 1; i < dbQueryAtributes.size(); i++) {
      methodString += "    result." + dbQueryAtributes.get(i).trim() + ";\n";
    }
    methodString += "    return result;\n }\n\n";
    dbMethods.add(methodString);
    dbQueryAtributes.clear();
  }

  public void createMethodForFields() {

    String methodString = "/*\n" +
       "\t * Metoda wyciągająca nazwy pól z XMLa\n" +
       "\t * \n" +
       "\t * @return obiekt Element zawierający nazwy pól\n" +
       "\t */\n" +
       "\t private Element " + XmlField.getId() + "()\n" +
       "\t {\n" +
       "\t\treturn XMLUtils.getXMLElement(\"";

    for (String line : XmlField.getXmlFieldLines()) {
      methodString += line +"\n";
    }

    methodString += "); }\n\n";
    dbMethods.add(methodString);
    XmlField.setId("");
    XmlField.getXmlFieldLines().clear();
  }
}