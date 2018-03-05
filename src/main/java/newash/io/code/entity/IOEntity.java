package newash.io.code.entity;

import java.io.BufferedReader;

/**
 * Created by Jaszczynski.Rafal on 05.03.2018.
 */
public class IOEntity {

  private BufferedReader inputCode;
  private StringBuilder outputCode;
  private String fileName;

  public BufferedReader getInputCode() {
    return inputCode;
  }

  public void setInputCode(BufferedReader inputCode) {
    this.inputCode = inputCode;
  }

  public StringBuilder getOutputCode() {
    return outputCode;
  }

  public void setOutputCode(StringBuilder outputCode) {
    this.outputCode = outputCode;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
}
