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

    UiDesignStage uiDesignStage = UiDesignStage.getInstance();
    ImportStage importStage = ImportStage.getInstance();
    ActionScriptStage actionScriptStage = ActionScriptStage.getInstance();
    FxDeclarationStage fxDeclarationStage = FxDeclarationStage.getInstance();
    ComponentsStage componentsStage = ComponentsStage.getInstance();

    public void launchIt() {

        reader = new Reader();
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
