package newash.io.code.entity;

import java.io.BufferedReader;

/**
 * Created by Jaszczynski.Rafal on 05.03.2018.
 */
public class IOEntity {

  private BufferedReader inputCode;
  private StringBuilder outputCode = new StringBuilder();
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

  public void appendOutputCode(String append) {
    outputCode.append(append);
  }

  public String outputCodeToString() {
    return outputCode.toString();
  }
}
