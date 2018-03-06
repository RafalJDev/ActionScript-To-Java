package newash.controller;

import newash.actionscript.stage.stages.*;
import newash.controller.stages.ListImportController;
import newash.controller.stages.StagesController;
import newash.io.readers.CodeReader;
import newash.io.readers.ImportReader;
import newash.io.readers.current.CodeLineEntity;
import newash.io.writers.CodeWriter;
import newash.io.writers.ImportWriter;
import newash.parser.finder.StagesFinder;

/**
 * Created by Jaszczynski.Rafal on 02.03.2018.
 */
public class Controller {

  private CodeReader codeReader;
  private CodeWriter codeWriter;
  private StagesFinder stagesFinder;

  private ImportReader importReader = new ImportReader();
  private ImportWriter importWriter = new ImportWriter();
  private ListImportController listImportController = new ListImportController();

  CodeLineEntity codeLineEntity = CodeLineEntity.getInstance();

  UiDesignStage uiDesignStage = UiDesignStage.getInstance();
  ImportStage importStage = ImportStage.getInstance(); //TODO the fucken machen package name set, and its null
  ActionScriptStage actionScriptStage = ActionScriptStage.getInstance();
  FxDeclarationStage fxDeclarationStage = FxDeclarationStage.getInstance();
  ComponentsStage componentsStage= ComponentsStage.getInstance();

  StagesController stagesController;

  public Controller() {


    codeReader = new CodeReader();
    codeWriter = new CodeWriter();

    stagesFinder = new StagesFinder();
    stagesController = new StagesController();
  }

  public void launchIt() {

    importListLauncher();

    codeReader.openFileAndGetBufferedReader();
    stagesFinder.findStages();
    codeReader.closeBufferedReader();

    codeReader.openFileAndGetBufferedReader();
    stagesController.parseAllStages();
    codeReader.closeBufferedReader();

    saveOutputCode();

    printData();
  }

  public void importListLauncher() {
    importReader.openFileAndGetBufferedReader();
    listImportController.parseImportList();
    importReader.closeBufferedReader();
    importWriter.saveFile();
  }

  public void saveOutputCode() {
    codeWriter.saveFile();
  }

  public void printData() {
    System.out.println(codeLineEntity.toString());

    System.out.println(uiDesignStage.toString());
    System.out.println();

    System.out.println(importStage.toString());
    System.out.println(actionScriptStage.toString());
    System.out.println(fxDeclarationStage.toString());
    System.out.println(componentsStage.toString());
  }
}