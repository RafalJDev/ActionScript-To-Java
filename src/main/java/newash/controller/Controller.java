package newash.controller;

import newash.actionscript.stage.stages.*;
import newash.io.reader.Reader;
import newash.io.reader.current.LineEntity;
import newash.io.writer.Writer;
import newash.parser.find.stages.StagesFinder;

/**
 * Created by Jaszczynski.Rafal on 02.03.2018.
 */
public class Controller {

    private Reader reader;
    private Writer writer;

    private StagesFinder stagesFinder;

    LineEntity lineEntity = LineEntity.getInstance();

    UiDesignStage uiDesignStage;
    ImportStage importStage = ImportStage.getInstance();
    ActionScriptStage actionScriptStage;
    FxDeclarationStage fxDeclarationStage;
    ComponentsStage componentsStage;

    public Controller() {
        uiDesignStage = UiDesignStage.getInstance();
        importStage = ImportStage.getInstance();
        actionScriptStage = ActionScriptStage.getInstance();
        fxDeclarationStage = FxDeclarationStage.getInstance();
        componentsStage = ComponentsStage.getInstance();

        reader = new Reader();
    }

    public void launchIt() {

        reader.openFileAndGetBufferedReader();

        stagesFinder = new StagesFinder();
        stagesFinder.findStages();

        reader.closeBufferedReader();

        printData();

//        writer = new Writer();
//        writer.saveFile();
    }

    public void printData() {
        System.out.println(lineEntity.toString());

        System.out.println(uiDesignStage.toString());
        System.out.println(importStage.toString());
        System.out.println(actionScriptStage.toString());
        System.out.println(fxDeclarationStage.toString());
        System.out.println(componentsStage.toString());
    }
}
