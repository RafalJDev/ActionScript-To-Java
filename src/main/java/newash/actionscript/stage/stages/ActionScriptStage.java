package newash.actionscript.stage.stages;

import newash.actionscript.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class ActionScriptStage extends Stage {

  private List<String> candidatesForComponents = new ArrayList<>();
  private Map<String, String> componentsThatNeedField = new HashMap<>();

  private List<String> dbMethods = new ArrayList<>();

  private static ActionScriptStage ourInstance = new ActionScriptStage();

  public static ActionScriptStage getInstance() {
    return ourInstance;
  }

  private ActionScriptStage() {
    name = "ActionScriptStage";
    code = new StringBuilder();
  }

  public List<String> getCandidatesForComponents() {
    return candidatesForComponents;
  }

  public void setCandidatesForComponents(List<String> candidatesForComponents) {
    this.candidatesForComponents = candidatesForComponents;
  }

  public Map<String, String> getComponentsThatNeedField() {
    return componentsThatNeedField;
  }

  public void setComponentsThatNeedField(Map<String, String> componentsThatNeedField) {
    this.componentsThatNeedField = componentsThatNeedField;
  }

  public List<String> getDbMethods() {
    return dbMethods;
  }

  public void setDbMethods(List<String> dbMethods) {
    this.dbMethods = dbMethods;
  }
}
