package newash.io.readers;

import lombok.extern.java.Log;
import newash.io.code.IOImportEntity;

import newash.io.readers.reader.Reader;

import java.io.*;

/**
 * Created by Jaszczynski.Rafal on 05.03.2018.
 */
@Log
public class ImportReader extends Reader {

  public ImportReader() {
    fileName = "ListOfAllImports.txt"; //FrmMeterTypeFlash.txt, TabEstateOwner.txt
    ioEntity = IOImportEntity.getInstance();
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
    ((IOImportEntity)ioEntity).setFileDirectory(filePathToReadFrom);
  }
}