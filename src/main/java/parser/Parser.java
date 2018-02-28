package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Parser {

    private static int parsedPart = 1;

    private static String line;

    private static Pattern pattern = null;
    private static Matcher matcher = null;

    //Stage search:1, use:2
    private static Map<String, String> classMap = new HashMap<>();

    //Stage 2
    private static Map<String, String> replaceMap2 = new HashMap<>();
    //Stage 4
    private static Map<String, String> replaceMap4 = new HashMap<>();

    //Stage 2
    private static boolean importIsAlreadyDone = false;
    private static boolean firstImportStatementOccured = false;
    private static boolean thereIsRoCall = false;
    private static String methodToCallAfterRo = "";
    private static boolean letsCreateDataMap = false;
    private static List<String> methodsForEventListeners = new ArrayList<>();
    private static List<String> candidatesForComponents = new ArrayList<>();
    private static Map<String, String> componentsThatNeedField = new HashMap<>();
    private static List<String> dbMethods = new ArrayList<>();

    //Stage 3
    private static List<String> dbQueryAtributes = new ArrayList<>();

    //Stage 4
    private static int countDbQuery = 0;

    private static boolean justOnce = true;

    public static String logicForLines(String lineToParse) {
        line = lineToParse;
        if (line.trim().startsWith("//") || line.trim().startsWith("/**")) {
            return line;
        }

        if (line.contains("fx:Script")) {
            if (parsedPart == 1) {
                parsedPart = 2;
            } else {
                parsedPart = 3;
            }
            line = "";
        } else if (parsedPart == 1) {
            searchFirstStage();
        } else if (parsedPart == 2) {
            convertActionScriptToJava();
        } else if (parsedPart == 3) {
            if (line.contains("<fx:XML") ) {
                parsedPart = 4;
                line = getSpacingAndCutHalf() + line.trim();
                return line;
            } else if (line.contains("</fx:Declarations>")) {
                parsedPart = 5;
                line = getSpacingAndCutHalf() + line.trim();
                return line;
            }
            parseDbQuery();
        } else if (parsedPart == 4) {
            if (line.contains("</fx:XML>")) {
                parsedPart = 5;
                line = getSpacingAndCutHalf() + line.trim();
                return line;
            }
            parseXmlFields();
        } else if (parsedPart == 5) {
            if (justOnce) {
                justOnce = false;
                return "AddMethods";
            }
            parseXmlComponents();
        }

        line = getSpacingAndCutHalf() + line.trim();
        return line;
    }

    public static String getSpacingAndCutHalf() {
        pattern = Pattern.compile("\\s{2,}");
        matcher = pattern.matcher(line);
        String spaces = "";
        if (matcher.find()) {
            int spacesInActionScript = matcher.group().length();

//            while ((spacesInActionScript-- - 4) > 0) {
//                spaces += " ";//TODO CHECK IT, IT MAY WORK
//            }

//            final String spaceFinal = "                  ";
            switch (spacesInActionScript) {
                //TODO UUUUUUUUUUUUUUUUUUUUUUUUUUGLY or ciuuuuuuuuuuuulowe
                case 6:
                    spaces = "  ";
                    break;
                case 8:
                    spaces = "    ";
                    break;
                case 10:
                    spaces = "      ";
                    break;
                case 12:
                    spaces = "        ";
                    break;
                case 14:
                    spaces = "          ";
                    break;
                case 16:
                    spaces = "            ";
                    break;
                case 18:
                    spaces = "              ";
                    break;
                case 20:
                    spaces = "                ";
                    break;
                default:
                    if (line.contains("UiDesign")) {
                        break;
                    }
                    spaces = "                  ";
                    break;
            }
        }
        return spaces;
    }

    public static void searchFirstStage() {
        pattern = Pattern.compile("ui:\\s+\\w");
        matcher = pattern.matcher(line);

        if (matcher.find()) {
            classMap.put("extends", matcher.group().trim());
        }
        pattern = Pattern.compile("formName=\".*\"");
        matcher = pattern.matcher(line);
        if (matcher.find()) {
            classMap.put("formName", matcher.group()
                    .trim()
                    .replace("formName=\"", "")
                    .replace("\"", ""));
        }
        pattern = Pattern.compile("formName=\".*\"");
        matcher = pattern.matcher(line);
        if (matcher.find()) {
            classMap.put("GUID", matcher.group()
                    .trim()
                    .replace("GUID=\"", "")
                    .replace("\"", ""));
        }
        line = "";
    }

    public static void convertActionScriptToJava() {
        if (line.contains("CDATA")) {
            line = "";
            return;
        }
        if (!importIsAlreadyDone) {
            if (line.contains("import ")) {
                firstImportStatementOccured = true;
                if (line.contains(" mx")) {
                    line = "";
                    return;
                }
                return;
            } else if (line.matches(".*\\w+.*") && !line.isEmpty() && firstImportStatementOccured) {
                importIsAlreadyDone = true;

                String classAndConstructor = "@UiDesign(formName = \"" + classMap.get("formName") + "\", guid = \"" + classMap.get("GUID") + "\")\n" +
                        "public class YOURCLASSNAME extends " + classMap.get("extends") + " \n" +
                        "{\n" +
                        "  /** Konstruktor */\n" +
                        "  public YOURCLASSNAME()\n" +
                        "  {\n" +
                        "\tUiCreator.getInstance(self).executeXML();\n" +
                        "  }\n" +
                        "HANYS\n";

                line = classAndConstructor;
                return;
            }
        }
        if (thereIsRoCall && line.contains("ro.call")) {

            String spaces = line.substring(0, line.indexOf("r") - 1);
            pattern = Pattern.compile("\\(.*\\)");
            matcher = pattern.matcher(line);
            String callArguments = "";
            if (matcher.find()) {
                callArguments = matcher.group().substring(1, matcher.group().length() - 1);

                line = spaces + "DataMap dataMap = ROUiEventService.call(" + callArguments + ");";
                if (!methodToCallAfterRo.isEmpty()) {
                    line += "\n" + spaces + methodToCallAfterRo + "()";
                }
            }
            thereIsRoCall = false;
            return;
        }
        if (line.trim().startsWith("for") && line.contains(" in ")) {
            line = line.replace(" in ", " : ");

        }
        pattern = Pattern.compile("\\w* = [{]");
        matcher = pattern.matcher(line);
        if (line.contains(" = {")) {
            letsCreateDataMap = true;
        }
        if (letsCreateDataMap) {

        }

        if (line.contains("ROUiEventService") && !line.contains(" import ")) {
            thereIsRoCall = true;
            pattern = Pattern.compile("\\(.*\\)");
            matcher = pattern.matcher(line);

            if (matcher.find()) {
                methodToCallAfterRo = matcher.group().substring(1, matcher.group().length() - 2);
                line = "";
                return;
            }
        }
        if (line.contains(" function ")) {
            int lastIndexOfColon = line.lastIndexOf(":");
            String returnTypeWithoutSpace = line.substring(lastIndexOfColon + 1, line.length()).trim();
            line = line.substring(0, lastIndexOfColon);
            line = line.replace(" function ", " " + returnTypeWithoutSpace + " ");

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
        if (line.contains("override")) {
            line = "  @Override\n  " + line.replace("override ", "").trim();
        }
        if (line.contains("var")) {
            int firstColon = line.indexOf(":");
            pattern = Pattern.compile(":[a-zA-Z0-9]*\\s{0}");
            matcher = pattern.matcher(line);

            if (matcher.find()) {
                line = line.replace("var", matcher.group().replace(":", ""));
                line = line.replace(matcher.group(), "");
            }
        }

        if (line.contains("protected void load")) {
            line = "  /** Nadpisana metoda load z klasy bazowej */\n" + line;
            line = line.replace("protected void load", "public void load");
        }
        if (line.contains("addEventListener(")) {
            methodsForEventListeners.add(line.substring(line.indexOf(",") + 2, line.indexOf(")")).trim());
            line = line.replace(")", "(event))");
            line = line.replace(", ", ", event -> ");
            line = line.replaceAll("\"valueChanged\"", "BaseEvent.VALUE_CHANGED");
            line = line.replaceAll("\"indexChanged\"", "BaseEvent.VALUE_CHANGED");
        }
        for (String method : methodsForEventListeners) {
            if (line.contains(method) && line.contains(" void ")) {
                String oldArgument = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                String newArgument = "BaseEvent event";
                line = line.replace(oldArgument, newArgument);
                break;
            }
        }

        pattern = Pattern.compile("\\s{3}\\w+.");
        matcher = pattern.matcher(line);
        if (matcher.find()) {
            candidatesForComponents.add(matcher.group().trim().replace(".", ""));
        }

        simpleReplaceAllOnMap();

    }

    public static void parseDbQuery() {

        if (line.contains("db:DBQuery")) {
            countDbQuery++;
        }
        if (countDbQuery >= 1) {
            pattern = Pattern.compile("\\w+=\"[a-zA-Z0-9_, ]+\"");
            matcher = pattern.matcher(line);

            while (matcher.find()) {
                if (matcher.group().contains("sqlTable")) {
                    continue;
                }
                dbQueryAtributes.add(matcher.group().trim());
            }
            if (line.contains("/>")) {
                if (!dbQueryAtributes.isEmpty()) {
                    createMethodForDbQuery();
                }
            }
        }
    }

    private static boolean iHaveDoneIt = false;

    public static void parseXmlFields() {

        if (!iHaveDoneIt) {
            iHaveDoneIt = true;
            pattern = Pattern.compile("\\w+=\"\\w+\"");
            matcher = pattern.matcher(line);

            if (matcher.find()) {
                String idRegex = matcher.group();
                XmlField.setId(idRegex.substring(idRegex.indexOf("\""), idRegex.length() - 1));
            }
        } else {
            XmlField.getXmlFieldLines().add(line);
        }

        if (line.contains("</fields>")) {
            if (!XmlField.xmlFieldLines.isEmpty()) {
                createMethodForFields();
                iHaveDoneIt=false;
            }
        }
    }

    private static void createMethodForDbQuery() {

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

    public static void createMethodForFields() {

        String methodString = "/*\n" +
                "\t * Metoda wyciągająca nazwy pól z XMLa\n" +
                "\t * \n" +
                "\t * @return obiekt Element zawierający nazwy pól\n" +
                "\t */\n" +
                "\t private Element " + XmlField.getId() + "()\n" +
                "\t {\n" +
                "\t\treturn XMLUtils.getXMLElement(\"";

        for (String line : XmlField.getXmlFieldLines()) {
            methodString += line;
        }

        methodString += "); }\n\n";
        dbMethods.add(methodString);
        XmlField.setId("");
        XmlField.getXmlFieldLines().clear();
    }

    //stage 5
    public static void parseXmlComponents() {

        simpleReplaceAllOnMap();

        findComponents();
    }

    public static void findComponents() {
        pattern = Pattern.compile("id=\"[^\"]*\"");
        matcher = pattern.matcher(line);
        if (matcher.find()) {
            String id = matcher.group().substring(4, matcher.group().length() - 1);

            String componentClass = "";
            pattern = Pattern.compile("(<c:){1}(\\w+)");
            matcher = pattern.matcher(line);
            if (matcher.find()) {
                componentClass = matcher.group().trim().substring(3, matcher.group().length());
            }

            for (String candidateForComponent : candidatesForComponents) {
                if (id.equals(candidateForComponent)) {
                    componentsThatNeedField.put(id, componentClass);
                    break;
                }
            }
        }
    }

    public static void initializeReplaceMap() {
        replaceMap2.put("_dbManager", "getDbManager()");
        replaceMap2.put(".dbManager.", ".getDBManager().");
        replaceMap2.put(".recordCount", ".getRecordCount()");
        replaceMap2.put("length", "length()");
        replaceMap2.put(".mode", "getMode()");
        replaceMap2.put(".value", ".getValue()");
        replaceMap2.put("super.load\\(event\\);", "super.load();");
        replaceMap2.put(".getNavigatorContent", ".getContent");
        replaceMap2.put("parseInt\\(", "Integer.valueOf(");
        replaceMap2.put("swf", "frm");
        replaceMap2.put("SWF", "FRM");

        replaceMap4.put("grid.dbManager", "getDbManager");
        replaceMap4.put("_dbManager", "getDbManager");
        replaceMap4.put("frmFile=\"\\w+.frm\"", ""); //TODO something with sorting hashMap
        replaceMap4.put("swf", "frm");
        replaceMap4.put("<s:", "<c:");
        replaceMap4.put("</s:", "</c:");
        replaceMap4.put("<mx:", "</c:");
        replaceMap4.put("</mx:", "</c:");
        replaceMap4.put("skinClass=\".*\"", "");
    }

    public static void simpleReplaceAllOnMap() {
        if (parsedPart == 2) {
            replaceLogic(replaceMap2);
        } else if (parsedPart == 5) {
            replaceLogic(replaceMap4);
        }
    }

    private static void replaceLogic(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String oldValue = entry.getKey();
            String newValue = entry.getValue();
//            if (line.contains(oldValue)) {
            line = line.replaceAll(oldValue, newValue);
//            }
        }
    }

    public static StringBuilder secondParsingForAddingComponents(StringBuilder sb, String oldValue) {

        String componentFields = "\n";
        for (Map.Entry<String, String> entry : componentsThatNeedField.entrySet()) {
            componentFields += "  public " + entry.getValue() + " " + entry.getKey() + ";\n\n";
        }
        int hanysIndex = sb.indexOf(oldValue);

        return sb.replace(hanysIndex, hanysIndex + oldValue.length(), componentFields);
    }

    public static String addDbQueryAndElementFieldsMethods() {
        String methods = "";
        for (String method : dbMethods) {
            methods += method;
        }
        return methods;
    }


    public static int getParsedPart() {
        return parsedPart;
    }

    public static void setParsedPart(int parsedPart) {
        Parser.parsedPart = parsedPart;
    }

    public static String getLine() {
        return line;
    }

    public static void setLine(String line) {
        Parser.line = line;
    }

    public static Pattern getPattern() {
        return pattern;
    }

    public static void setPattern(Pattern pattern) {
        Parser.pattern = pattern;
    }

    public static Matcher getMatcher() {
        return matcher;
    }

    public static void setMatcher(Matcher matcher) {
        Parser.matcher = matcher;
    }

    public static Map<String, String> getClassMap() {
        return classMap;
    }

    public static void setClassMap(Map<String, String> classMap) {
        Parser.classMap = classMap;
    }

    public static Map<String, String> getReplaceMap2() {
        return replaceMap2;
    }

    public static void setReplaceMap2(Map<String, String> replaceMap2) {
        Parser.replaceMap2 = replaceMap2;
    }

    public static Map<String, String> getReplaceMap4() {
        return replaceMap4;
    }

    public static void setReplaceMap4(Map<String, String> replaceMap4) {
        Parser.replaceMap4 = replaceMap4;
    }

    public static boolean isImportIsAlreadyDone() {
        return importIsAlreadyDone;
    }

    public static void setImportIsAlreadyDone(boolean importIsAlreadyDone) {
        Parser.importIsAlreadyDone = importIsAlreadyDone;
    }

    public static boolean isFirstImportStatementOccured() {
        return firstImportStatementOccured;
    }

    public static void setFirstImportStatementOccured(boolean firstImportStatementOccured) {
        Parser.firstImportStatementOccured = firstImportStatementOccured;
    }

    public static boolean isThereIsRoCall() {
        return thereIsRoCall;
    }

    public static void setThereIsRoCall(boolean thereIsRoCall) {
        Parser.thereIsRoCall = thereIsRoCall;
    }

    public static String getMethodToCallAfterRo() {
        return methodToCallAfterRo;
    }

    public static void setMethodToCallAfterRo(String methodToCallAfterRo) {
        Parser.methodToCallAfterRo = methodToCallAfterRo;
    }

    public static boolean isLetsCreateDataMap() {
        return letsCreateDataMap;
    }

    public static void setLetsCreateDataMap(boolean letsCreateDataMap) {
        Parser.letsCreateDataMap = letsCreateDataMap;
    }

    public static List<String> getMethodsForEventListeners() {
        return methodsForEventListeners;
    }

    public static void setMethodsForEventListeners(List<String> methodsForEventListeners) {
        Parser.methodsForEventListeners = methodsForEventListeners;
    }

    public static List<String> getCandidatesForComponents() {
        return candidatesForComponents;
    }

    public static void setCandidatesForComponents(List<String> candidatesForComponents) {
        Parser.candidatesForComponents = candidatesForComponents;
    }

    public static int getCountDbQuery() {
        return countDbQuery;
    }

    public static void setCountDbQuery(int countDbQuery) {
        Parser.countDbQuery = countDbQuery;
    }

    public static List<String> getDbQueryAtributes() {
        return dbQueryAtributes;
    }

    public static void setDbQueryAtributes(List<String> dbQueryAtributes) {
        Parser.dbQueryAtributes = dbQueryAtributes;
    }

    public static Map<String, String> getComponentsThatNeedField() {
        return componentsThatNeedField;
    }

    public static void setComponentsThatNeedField(Map<String, String> componentsThatNeedField) {
        Parser.componentsThatNeedField = componentsThatNeedField;
    }

    public static List<String> getDbMethods() {
        return dbMethods;
    }

    public static void setDbMethods(List<String> dbMethods) {
        Parser.dbMethods = dbMethods;
    }
}

class XmlField {

    static String id;
    static List<String> xmlFieldLines = new ArrayList<>();

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        XmlField.id = id;
    }

    public static List<String> getXmlFieldLines() {
        return xmlFieldLines;
    }

    public static void setXmlFieldLines(List<String> xmlFieldLines) {
        XmlField.xmlFieldLines = xmlFieldLines;
    }
}