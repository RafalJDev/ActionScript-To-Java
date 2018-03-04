package newash.io.code;

import java.io.BufferedReader;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class IOEntity {
  private static IOEntity ourInstance = new IOEntity();

  private BufferedReader inputCode;
  private StringBuilder outputCode;
  private String fileName;
  private String filePackage;

  public static IOEntity getInstance() {
    return ourInstance;
  }

  private IOEntity() {
    outputCode = new StringBuilder();
  }

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

  public String getFilePackage() {
    return filePackage;
  }

  public void setFilePackage(String filePackage) {
    this.filePackage = filePackage;
  }
}
