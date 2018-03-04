package newash.actionscript.stage.stages;

import newash.actionscript.stage.Stage;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class UiDesignStage extends Stage {

  private String classToExtend;
  private String formName;
  private String GUID;

  private static UiDesignStage ourInstance = new UiDesignStage();

  public static UiDesignStage getInstance() {
    return ourInstance;
  }

  private UiDesignStage() {
    name = "UiDesignStage";
    code = new StringBuilder();
  }

  public String getClassToExtend() {
    return classToExtend;
  }

  public void setClassToExtend(String classToExtend) {
    this.classToExtend = classToExtend;
  }

  public String getFormName() {
    return formName;
  }

  public void setFormName(String formName) {
    this.formName = formName;
  }

  public String getGUID() {
    return GUID;
  }

  public void setGUID(String GUID) {
    this.GUID = GUID;
  }

  @Override
  public String toString() {
    return "UiDesignStage{" +
       "name='" + name + '\'' +
       ", firstLine=" + firstLine +
       ", lastLine=" + lastLine +
       ", classToExtend='" + classToExtend + '\'' +
       ", formName='" + formName + '\'' +
       ", GUID='" + GUID + '\'' +
       '}';
  }
}
