package newash.controller.stages;

import lombok.extern.java.Log;
import newash.actionscript.stage.stages.*;
import newash.io.code.IOEntity;
import newash.io.reader.current.LineEntity;
import newash.parser.stages.actionscript.ActionScriptParser;
import newash.parser.stages.coponents.ComponentsParser;
import newash.parser.stages.fxDeclaration.FxDeclarationParser;
import newash.parser.stages.imports.ImportParser;
import newash.parser.stages.uidesign.UiDesignParser;

import javax.sound.sampled.Line;
import java.io.IOException;

/**
 * Created by Jaszczynski.Rafal on 03.03.2018.
 */
@Log
public class StagesController {

  IOEntity ioEntity = IOEntity.getInstance();

  LineEntity lineEntity = LineEntity.getInstance();

  //TODO create class for common fields
  UiDesignStage uiDesignStage = UiDesignStage.getInstance();
  ImportStage importStage = ImportStage.getInstance();
  ActionScriptStage actionScriptStage = ActionScriptStage.getInstance();
  FxDeclarationStage fxDeclarationStage = FxDeclarationStage.getInstance();
  ComponentsStage componentsStage = ComponentsStage.getInstance();

  UiDesignParser uiDesignParser = new UiDesignParser();
  ImportParser importParser = new ImportParser();
  ActionScriptParser actionScriptParser = new ActionScriptParser();
  FxDeclarationParser fxDeclarationParser = new FxDeclarationParser();
  ComponentsParser componentsParser;

  String classThatWillBeExtended = "";
  String currentLine;

  public void parseAllStages() {

    log.info("In parseAllStages");
    int lineCount = 0;

    try {
      while ((currentLine = ioEntity.getInputCode().readLine()) != null) {
        lineCount++;
        lineEntity.setLine(currentLine);
        if (uiDesignStage.getFirstLine() <= lineCount && uiDesignStage.getLastLine() >= lineCount) {
          uiDesignParser.parseThisStage();
        } else if (importStage.getFirstLine() <= lineCount && importStage.getLastLine() >= lineCount) {
          importParser.parseThisStage();
        } else if (actionScriptStage.getFirstLine() <= lineCount && actionScriptStage.getLastLine() > lineCount) {
          actionScriptParser.parseThisStage();
        } else if (actionScriptStage.getLastLine() == lineCount) {
          actionScriptParser.appendEndClassBraces();
        } else if (fxDeclarationStage.getFirstLine() <= lineCount && fxDeclarationStage.getLastLine() >= lineCount) {
          fxDeclarationParser.parseThisStage();
        } else if (componentsStage.getFirstLine() == lineCount) {
          componentsParser = new ComponentsParser();
          componentsParser.parseThisStage();
        } else if (componentsStage.getFirstLine() < lineCount && componentsStage.getLastLine() > lineCount) {
          componentsParser.parseThisStage();
        } else if (componentsStage.getLastLine() == lineCount) {
          componentsParser.parseThisStage();
          actionScriptParser.secondParsingForAddingComponents();
        }

      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
      log.info("OCCURED ON LINE :" + lineCount);
    }

    addOutputCode();
  }

  public void addOutputCode() {
    StringBuilder outputCode = new StringBuilder();
    outputCode.append(importStage.getCode());
    outputCode.append(actionScriptStage.getCode());
    outputCode.append(fxDeclarationStage.getCode());
    outputCode.append(componentsStage.getCode());
    ioEntity.setOutputCode(outputCode);
  }
}
