package newash.io.readers.reader;

import lombok.extern.java.Log;
import newash.io.code.IOImportEntity;
import newash.io.code.entity.IOEntity;

import javax.swing.plaf.ViewportUI;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 * Created by Jaszczynski.Rafal on 05.03.2018.
 */
@Log
public class Reader {

  protected String fileName;
  protected String filePathToReadFrom;

  protected IOEntity ioEntity;

  public void closeBufferedReader() {
    log.info("In closeBufferedReader class " + this.getClass().getName());
    BufferedReader br = ioEntity.getInputCode();
    if (br != null) {
      try {
        br.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      throw new NullPointerException("BufferedReader is null");
    }
  }

  public void findPathForParsedFileByName(String directoryName) {
    BufferedReader br = ioEntity.getInputCode();
    File directory = new File(directoryName);
    File[] fList = directory.listFiles();
    for (File file : fList) {
      if (file.isFile()) {
        if (file.getAbsolutePath().contains(fileName)) {

          filePathToReadFrom = file.getAbsolutePath().trim();
        }
      } else if (file.isDirectory()) {
        findPathForParsedFileByName(file.getAbsolutePath());
      }
    }
  }
}