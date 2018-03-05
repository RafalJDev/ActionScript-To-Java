package newash.io.code;

import newash.io.code.entity.IOEntity;

/**
 * Created by Jaszczynski.Rafal on 05.03.2018.
 */
public class IOImportEntity extends IOEntity {

  private static IOImportEntity ourInstance = new IOImportEntity();

  private String fileDirectory;

  public static IOImportEntity getInstance() {
    return ourInstance;
  }

  public String getFileDirectory() {
    return fileDirectory;
  }

  public void setFileDirectory(String fileDirectory) {
    this.fileDirectory = fileDirectory;
  }
}