package newash.io.writers.writer;

import newash.io.code.entity.IOEntity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Jaszczynski.Rafal on 05.03.2018.
 */
public class Writer {

  protected String outputDirectoryPath;
  protected String outputDirectory;
  protected IOEntity ioEntity;

  protected void save() {
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(outputDirectoryPath));
      writer.write(ioEntity.outputCodeToString());
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
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