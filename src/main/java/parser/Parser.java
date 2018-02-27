package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public static String logicForLines(String line) {

        Pattern pattern = null;
        Matcher matcher = null;

        if (line.contains("function")) {
            int funcIndex = line.indexOf("function");
            int lastIndexOfColon = line.lastIndexOf(":");
            String returnTypeWithoutSpace = line.substring(lastIndexOfColon+1,line.length()).trim();
            line = line.substring(0, lastIndexOfColon);
            line = line.replace(" function ", " " + returnTypeWithoutSpace + " ");

            String bracketsWithArguments = line.substring(line.indexOf("(")+1, line.lastIndexOf(")")-1);
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

            int firstIndexOfColon;
            while ((firstIndexOfColon = line.indexOf(":"))!= -1) {
                String type = line.replaceAll("", "");
                break;
            }
        }
        if(line.contains("override")) {
            line ="@Override\n" + line.replace("override ", "");
        }
        if (line.contains("var")) {
               int firstColon = line.indexOf(":");
               pattern = Pattern.compile(":[a-zA-Z0-9]*\\s{0}");
               matcher = pattern.matcher(line);

            if (matcher.find()){
                line = line.replace("var", matcher.group().replace(":", ""));
                line = line.replace(matcher.group(), "");
            }
        }
        if (line.contains("_dbManager")) {
            line = line.replace("_dbManager", "getDbManager()");
        }
        if (line.contains("length")) {
            line = line.replace("length", "length()");
        }
        if (line.contains(".mode")) {
            line = line.replace("mode", "getMode()");
        }

        System.out.println(line);
        return null;
    }
}
