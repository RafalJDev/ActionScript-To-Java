package newash.io.readers;

import lombok.extern.java.Log;
import newash.io.code.IOCodeEntity;
import newash.io.readers.reader.Reader;

import java.io.*;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
@Log
public class CodeReader extends Reader {

  private IOCodeEntity ioCodeEntity = IOCodeEntity.getInstance();

  public CodeReader() {
    ioEntity = IOCodeEntity.getInstance();
    fileName = "FrmMeterTypeFlash.txt"; //FrmMeterTypeFlash.txt, TabEstateOwner.txt
  }

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
    ((IOCodeEntity)ioEntity).setFilePackage(filePathToReadFrom
       .replace(System.getProperty("user.dir"), "")
       .replace(fileName, "")
       .replace("\\", "."));
    System.out.println(((IOCodeEntity)ioEntity).getFilePackage());
  }

//  public void closeBufferedReader() {
//    close();
//  }
}
