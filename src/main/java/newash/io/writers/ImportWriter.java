package newash.io.writers;

import lombok.extern.java.Log;
import newash.io.code.IOImportEntity;
import newash.io.writers.writer.Writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Jaszczynski.Rafal on 05.03.2018.
 */
@Log
public class ImportWriter extends Writer {

  private String outputDirectoryPath;

  public void saveFile() {

    ioEntity = IOImportEntity.getInstance();
    outputDirectoryPath = ((IOImportEntity)ioEntity).getFileDirectory();
    log.info("In CodeWriter");

    save();
  }
}
