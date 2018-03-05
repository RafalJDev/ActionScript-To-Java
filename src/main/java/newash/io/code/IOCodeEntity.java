package newash.io.code;

import newash.io.code.entity.IOEntity;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class IOCodeEntity extends IOEntity {
  private static IOCodeEntity ourInstance = new IOCodeEntity();

  private String filePackage;

  public static IOCodeEntity getInstance() {
    return ourInstance;
  }

  public String getFilePackage() {
    return filePackage;
  }

  public void setFilePackage(String filePackage) {
    this.filePackage = filePackage;
  }
}
