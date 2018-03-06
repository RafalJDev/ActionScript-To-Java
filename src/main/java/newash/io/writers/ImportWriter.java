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

  public void saveFile() {
    log.info("In ImportWriter");

    outputDirectory = "importsFile";
    ioEntity = IOImportEntity.getInstance();
    findPathForOutputDirectory(System.getProperty("user.dir"));
    outputDirectoryPath = ((IOImportEntity)ioEntity).getFileDirectory();

//    outputDirectoryPath += "\\" + ioEntity.getFileName() + ".txt";
    save();
  }
}
