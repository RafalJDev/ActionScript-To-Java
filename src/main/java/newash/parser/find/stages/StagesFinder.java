package newash.parser.find.stages;

import lombok.extern.java.Log;
import newash.actionscript.stage.AllStages;
import newash.actionscript.stage.stages.*;
import newash.io.code.IOEntity;
import newash.io.reader.current.LineEntity;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jaszczynski.Rafal on 02.03.2018.
 */
@Log
public class StagesFinder {

    IOEntity globalReader = IOEntity.getInstance();

    LineEntity lineEntity = LineEntity.getInstance();

    UiDesignStage uiDesignStage = UiDesignStage.getInstance();
    ImportStage importStage = ImportStage.getInstance();
    ActionScriptStage actionScriptStage = ActionScriptStage.getInstance();
    FxDeclarationStage fxDeclarationStage = FxDeclarationStage.getInstance();
    ComponentsStage componentsStage = ComponentsStage.getInstance();

    String classThatWillBeExtended = "";
    String line;

    boolean componentStageFirstLineOccured = false;

    public void findStages() {

        log.info("In FindStages");

        AllStages allStages = AllStages.UIDESIGN;
        int lineCount = 0;
        try {
            while ((line = globalReader.getInputCode().readLine()) != null) {
                lineCount++;
                switch (allStages) {
                    case UIDESIGN:
                        if (line.contains("<?xml")) {
                            uiDesignStage.firstLine = lineCount;
                        } else if (!line.isEmpty() && line.contains("<") && classThatWillBeExtended.isEmpty()) {
                            classThatWillBeExtended = doRegexOnLine(":(\\w+)\\s", 1);
                        } else if (line.contains("<fx:Script>")) {
                            uiDesignStage.lastLine = lineCount;
                            allStages = AllStages.IMPORTS;

                        }
                        break;
                    case IMPORTS:
                        if (line.contains("<![CDATA[")) {
                            importStage.firstLine = lineCount;
                        } else if (!line.contains("import")) {
                            if (line.matches(".*\\w+.*")) {
                                importStage.lastLine = lineCount - 2;
                                componentStageFirstLineOccured = true;
                                allStages = AllStages.ASCODE;
                            }
                        }
                        break;
                    case ASCODE:
                        if (componentStageFirstLineOccured) {
                            actionScriptStage.firstLine = lineCount;
                            componentStageFirstLineOccured = false;
                        }
                        if (line.contains("]]>")) {
                            actionScriptStage.lastLine = lineCount;
                            allStages = AllStages.DBFIELDS;
                        } else if (line.contains("import")) {
                            throw new IllegalStateException("Its action script stage and there is import, Line: " + line);
                        }
                        break;
                    case DBFIELDS:
                        if (line.contains("<fx:Declarations>")) {
                            fxDeclarationStage.firstLine = lineCount;
                        } else if (line.contains("</fx:Declarations>")) {
                            fxDeclarationStage.lastLine = lineCount;
                            componentsStage.firstLine = lineCount + 2;
                            allStages = AllStages.COMPONENTS;
                        }
                        break;
                    case COMPONENTS:
                        if (classThatWillBeExtended.isEmpty()) {
                            throw new NullPointerException("Regex for the class that will be extended doesn't work");
                        } else if (line.contains(classThatWillBeExtended)) {
                            componentsStage.lastLine = lineCount;
                        } else if (line.contains("<fx:Declarations>")) {
                            throw new IllegalStateException("Its components stage and there is <fx:Declarations>, Line: " + line);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Sth bad");
                }
            }
            lineEntity.setTotalLineCount(lineCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String doRegexOnLine(String regex, int groupNumber) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(groupNumber);
        }
        return "";
    }

    public String doRegexOnLine(String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }
}
