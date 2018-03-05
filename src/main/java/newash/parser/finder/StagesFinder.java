package newash.parser.finder;

import lombok.extern.java.Log;
import newash.actionscript.stage.AllStages;
import newash.actionscript.stage.stages.*;
import newash.io.code.IOCodeEntity;
import newash.io.readers.current.CodeLineEntity;
import newash.regex.Regex;

import java.io.IOException;

/**
 * Created by Jaszczynski.Rafal on 02.03.2018.
 */
@Log
public class StagesFinder extends Regex {

  IOCodeEntity globalReader = IOCodeEntity.getInstance();

  CodeLineEntity codeLineEntity = CodeLineEntity.getInstance();

  //TODO create class for common fields
  UiDesignStage uiDesignStage = UiDesignStage.getInstance();
  ImportStage importStage = ImportStage.getInstance();
  ActionScriptStage actionScriptStage = ActionScriptStage.getInstance();
  FxDeclarationStage fxDeclarationStage = FxDeclarationStage.getInstance();
  ComponentsStage componentsStage = ComponentsStage.getInstance();

  String classThatWillBeExtended = "";
  String currentLine;

  boolean componentStageFirstLaneOccured = false;

  public void findStages() {

    log.info("In FindStages");

    AllStages allStages = AllStages.UIDESIGN;
    int lineCount = 0;
    try {
      while ((currentLine = globalReader.getInputCode().readLine()) != null) {
        line = currentLine;
        lineCount++;
        switch (allStages) {
          case UIDESIGN:
            if (currentLine.contains("<?xml")) {
              uiDesignStage.setFirstLine(lineCount);
            } else if (!currentLine.isEmpty()
               && currentLine.contains("<")
               && classThatWillBeExtended.isEmpty()) {
              classThatWillBeExtended = foundRegex(":(\\w+)\\s", 1);
            } else if (currentLine.contains("<fx:Script>")) {
              uiDesignStage.setLastLine(lineCount);
              allStages = AllStages.IMPORTS;
            }
            break;
          case IMPORTS:
            if (currentLine.contains("<![CDATA[")) {
              importStage.setFirstLine(lineCount + 1);
            } else if (!currentLine.contains("import")) {
              if (currentLine.matches(".*\\w+.*")) {
                importStage.setLastLine(lineCount - 1);
                componentStageFirstLaneOccured = true;
                allStages = AllStages.ASCODE;
              }
            }
            break;
          case ASCODE:
            if (componentStageFirstLaneOccured) {
              actionScriptStage.setFirstLine(lineCount - 1);
              componentStageFirstLaneOccured = false;
            } else if (currentLine.contains("]]>")) {
              actionScriptStage.setLastLine(lineCount - 1);
              allStages = AllStages.DBFIELDS;
            } else if (currentLine.contains("import")) {
              throw new IllegalStateException("Its action script stage and there is import, Line: "
                 + currentLine);
            }
            break;
          case DBFIELDS:
            if (currentLine.contains("<fx:Declarations>")) {
              fxDeclarationStage.setFirstLine(lineCount + 1);
            } else if (currentLine.contains("</fx:Declarations>")) {
              fxDeclarationStage.setLastLine(lineCount - 1);
              componentsStage.setFirstLine(lineCount + 2);
              allStages = AllStages.COMPONENTS;
            }
            break;
          case COMPONENTS:
            if (classThatWillBeExtended.isEmpty()) {
              throw new NullPointerException("Regex for the class that will be extended doesn't work");
            } else if (currentLine.contains(classThatWillBeExtended)) {
              componentsStage.setLastLine(lineCount);
            } else if (currentLine.contains("<fx:Declarations>")) {
              throw new IllegalStateException("Its components stage and there is <fx:Declarations>, Line: "
                 + currentLine);
            }
            break;
          default:
            throw new IllegalArgumentException("Sth bad");
        }
      }
      codeLineEntity.setTotalLineCount(lineCount);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
