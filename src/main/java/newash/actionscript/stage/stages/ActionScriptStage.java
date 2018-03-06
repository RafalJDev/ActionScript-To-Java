package newash.actionscript.stage.stages;

import newash.actionscript.stage.Stage;
import newash.actionscript.stage.stages.field.ComponentField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class ActionScriptStage extends Stage {

  private List<String> candidatesForComponents = new ArrayList<>();

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

  public List<String> getDbMethods() {
    return dbMethods;
  }

  public void setDbMethods(List<String> dbMethods) {
    this.dbMethods = dbMethods;
  }

  private List<ComponentField> componentsThatNeedField = new ArrayList<>();

  public List<ComponentField> getComponentsThatNeedField() {
    return componentsThatNeedField;
  }

  public void addComponentThatNeedField(String componentClass, String id) {
    componentsThatNeedField.add(new ComponentField(componentClass, id));
  }
}
