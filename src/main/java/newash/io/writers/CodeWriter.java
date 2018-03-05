package newash.io.writers;

import lombok.extern.java.Log;
import newash.io.code.IOCodeEntity;
import newash.io.writers.writer.Writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Jaszczynski.Rafal on 02.03.2018.
 */
@Log
public class CodeWriter extends Writer {

  private final String outputDirectory = "output";
//  private String outputDirectoryPath;

  public void saveFile() {

    ioEntity = (IOCodeEntity)IOCodeEntity.getInstance();
    findPathForOutputDirectory(System.getProperty("user.dir"));
    outputDirectoryPath += "\\" + ioEntity.getFileName() + "Flash.txt";
    log.info("In CodeWriter");

    save();
  }

  public void findPathForOutputDirectory(String directoryName) {
    File directory = new File(directoryName);
    //get all the files from a directory
    File[] fList = directory.listFiles();
    for (File file : fList) {
      if (file.isDirectory()) {
        if (file.getAbsolutePath().contains(outputDirectory)) {
          outputDirectoryPath = file.getAbsolutePath().trim();
        } else {
          findPathForOutputDirectory(file.getAbsolutePath());
        }
      }
    }
  }
}
