package newash.io.reader;

import lombok.extern.java.Log;
import newash.io.code.IOEntity;

import java.io.*;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
@Log
public class Reader {

  private final String fileName = "FrmMeterTypeFlash.txt"; //FrmMeterTypeFlash.txt, TabEstateOwner.txt
  private String filePathToReadFrom;

  private IOEntity ioEntity = IOEntity.getInstance();

  public void openFileAndGetBufferedReader() {

    log.info("In openFileAndGetBufferedReader");
    // Open the file
    findPathForParsedFileByName(System.getProperty("user.dir"));
    FileInputStream fstream = null;
    try {
      fstream = new FileInputStream(filePathToReadFrom);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    ioEntity.setInputCode(new BufferedReader(new InputStreamReader(fstream)));
    ioEntity.setFileName(fileName.replaceAll("\\.\\w+", ""));
    ioEntity.setFilePackage(filePathToReadFrom
       .replace(System.getProperty("user.dir"), "")
       .replace(fileName, "")
       .replace("\\", "."));
  }

  public void closeBufferedReader() {

    log.info("In closeBufferedReader");
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
    File directory = new File(directoryName);
    //get all the files from a directory
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
