package newash.parser.find.stages;

import newash.actionscript.stage.Stage;
import newash.actionscript.stage.stages.*;
import newash.controller.Controller;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class StagesFinderTest {

    UiDesignStage uiDesignStage;
    ImportStage importStage;
    ActionScriptStage actionScriptStage;
    FxDeclarationStage fxDeclarationStage;
    ComponentsStage componentsStage;

    List<Stage> stages;

    Controller controller;

    @Before
    public void setUp() throws Exception {
        uiDesignStage = UiDesignStage.getInstance();
        importStage = ImportStage.getInstance();
        actionScriptStage = ActionScriptStage.getInstance();
        fxDeclarationStage = FxDeclarationStage.getInstance();
        componentsStage = ComponentsStage.getInstance();

        stages = Arrays.asList(uiDesignStage, importStage, actionScriptStage, fxDeclarationStage, componentsStage);

        controller = new Controller();
        controller.launchIt();
    }

    @Test
    public void findStages() {

        for (int i = 0; i < stages.size(); i++) {

            Stage currentStage = stages.get(i);
            assertNotEquals(0, currentStage.firstLine);
            assertNotEquals(0, currentStage.lastLine);
            assertNotEquals(currentStage.firstLine, currentStage.lastLine);

            if (i - 1 > 0) {
                assertNotEquals(stages.get(i - 1).lastLine, currentStage.firstLine);
            }
            if (i + 1 < stages.size()) {
                assertNotEquals(currentStage.lastLine, stages.get(i + 1).firstLine);
            }
        }
    }
}