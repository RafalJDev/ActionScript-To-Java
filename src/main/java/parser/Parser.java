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

    //Stage 4
    private static int countDbQuery = 0;
    //Stage 3
    private static List<String> dbQueryAtributes = new ArrayList<>();

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
            if (line.contains("</fx:Declarations>")) {
                parsedPart = 4;
            }
            parseDbQuery();
        } else if (parsedPart == 4) {
            parseXmlComponents();
        }

        line = getSpacingAndCutHalf() + line.trim();

        return line;
    }

    private static String getSpacingAndCutHalf() {
        pattern = Pattern.compile("\\s{2,}");
        matcher = pattern.matcher(line);
        String spaces = "";
        if (matcher.find()) {
            int spacesInActionScript = matcher.group().length();

//            final String spaceFinal = "                  ";
            switch (spacesInActionScript) {
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

    private static void searchFirstStage() {
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

    private static void convertActionScriptToJava() {
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
                        "  }\n";

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
        //TODO !!!!!!!!!!!!!!!
        if (line.contains(" = {")) {
            letsCreateDataMap = true;
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
        }
        for (String method : methodsForEventListeners) {
            if (line.contains(method) && line.contains(" void ")) {
                String oldArgument = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                String newArgument = "BaseEvent event";
                line = line.replace(oldArgument, newArgument);
                break;
            }
        }

        parseXmlComponents();

//        pattern = Pattern.compile(":[a-zA-Z0-9]*\\s{0}");
//        matcher = pattern.matcher(line);
//
//        if (matcher.find()) {
//
//        }
    }

    private static void parseDbQuery() {

        if (line.contains("db:DBQuery")) {
            countDbQuery++;
            if (!dbQueryAtributes.isEmpty()) {
                createJavaMethodFromArrayList();
            }
        }
        if (countDbQuery > 1) {
            pattern = Pattern.compile("\\s\\w+\".*\"\\s");
            matcher = pattern.matcher(line);

            while (matcher.find()) {
                if (matcher.group().contains("sqlTable")) {
                    continue;
                }
                dbQueryAtributes.add(matcher.group().trim());
            }
        }
    }

    private static void createJavaMethodFromArrayList() {
        String methodString = "/**\n" +
                " * Zwrócenie obiektu DBQuery dla tabeli XXXXXX\n" +
                " * \n" +
                " * @return From XXXXXX\n" +
                " */\n" +
                " public DBQuery " + dbQueryAtributes.get(0) + "()\n" +
                "{\n" +
                "    DBQuery result = new DBQuery();\n";

        for (int i = 1; i < dbQueryAtributes.size(); i++) {
            methodString += "    result." + dbQueryAtributes.get(i)+";";
        }

        methodString += "\n}";
    }

    private static void parseXmlComponents() {

        if (line.contains("skinClass=\"")) {
            line = line.replaceAll("skinClass=\".*\"", "");
        }
        simpleReplaceAllOnMap();
    }

    public static void initializeReplaceMap() {
        replaceMap2.put("_dbManager", "getDbManager()");
        replaceMap2.put(".dbManager.", ".getDBManager().");
        replaceMap2.put(".recordCount", ".getRecordCount()");
        replaceMap2.put("length", "length()");
        replaceMap2.put(".mode", "getMode()");
        replaceMap2.put(".value", ".getValue()");
        replaceMap2.put("super.load(event);", "super.load();");
        replaceMap2.put(".getNavigatorContent", ".getContent");
        replaceMap2.put("parseInt(", "Integer.valueOf(");

        replaceMap4.put("grid.dbManager", "getDbManager");
        replaceMap4.put("swf", "frm");
        replaceMap4.put("<s:", "<c:");
        replaceMap4.put("</s:", "</c:");
        replaceMap4.put("<mx:", "</c:");
        replaceMap4.put("</mx:", "</c:");
    }

    private static void simpleReplaceAllOnMap() {
        if (parsedPart == 2) {
            for (Map.Entry<String, String> entry : replaceMap2.entrySet()) {
                String oldValue = entry.getKey();
                String newValue = entry.getValue();
                if (line.contains(oldValue)) {
                    line = line.replaceAll(oldValue, newValue);
                }
            }
        } else if (parsedPart == 4) {

            for (Map.Entry<String, String> entry : replaceMap4.entrySet()) {
                String oldValue = entry.getKey();
                String newValue = entry.getValue();
                if (line.contains(oldValue)) {
                    line = line.replaceAll(oldValue, newValue);
                }
            }
        }
    }
}
