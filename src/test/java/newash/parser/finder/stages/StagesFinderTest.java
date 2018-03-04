package newash.parser.finder.stages;

import newash.actionscript.stage.Stage;
import newash.actionscript.stage.stages.*;
import newash.controller.Controller;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class StagesFinderTest {

  static UiDesignStage uiDesignStage;
  static ImportStage importStage;
  static ActionScriptStage actionScriptStage;
  static FxDeclarationStage fxDeclarationStage;
  static ComponentsStage componentsStage;

  static List<Stage> stages;

  static Controller controller;

  @BeforeClass
  public static void setUp() throws Exception {
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
      assertNotEquals(0, currentStage.getFirstLine());
      assertNotEquals(0, currentStage.getLastLine());
      assertNotEquals(currentStage.getFirstLine(), currentStage.getLastLine());

      if (i - 1 > 0) {
        assertNotEquals(stages.get(i - 1).getLastLine(), currentStage.getFirstLine());
        assertTrue(stages.get(i - 1).getLastLine() < currentStage.getFirstLine());
      }
      if (i + 1 < stages.size()) {
        assertNotEquals(currentStage.getLastLine(), stages.get(i + 1).getFirstLine());
        assertTrue(stages.get(i + 1).getFirstLine() > currentStage.getLastLine());
      }
    }
  }

  @Test
  public void uiDesignParser() {
//    assertEquals("UiMDITab", uiDesignStage.getClassToExtend());
//    assertEquals("Właściciel", uiDesignStage.getFormName());
//    assertEquals("54BCD12D", uiDesignStage.getGUID());
  }

  @Test
  public void importParser() {

    String code = importStage.getCode().toString();

    assertTrue(!code.contains(" mx"));
  }
}