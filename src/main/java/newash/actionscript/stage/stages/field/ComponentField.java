package newash.actionscript.stage.stages.field;

public class ComponentField {
  private String componentClass;
  private String fieldId;

  public ComponentField(String componentClass, String fieldId) {
    this.componentClass = componentClass;
    this.fieldId = fieldId;
  }

  public String getComponentClass() {
    return componentClass;
  }

  public String createField() {
    return "  public " + componentClass + " " + fieldId + "\n\n";
  }
}
