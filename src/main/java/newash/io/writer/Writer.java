package newash.io.writer;

import com.sun.media.jfxmedia.logging.Logger;
import lombok.extern.java.Log;
import newash.io.code.IOEntity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Jaszczynski.Rafal on 02.03.2018.
 */
@Log
public class Writer {

  private final String outputDirectory = "output";
  private String outputDirectoryPath;
  private IOEntity ioEntity = IOEntity.getInstance();

  public void saveFile() {

    findPathForOutputDirectory(System.getProperty("user.dir"));
    outputDirectoryPath += "\\" + ioEntity.getFileName() + "Flash.txt";
    log.info("In Writer");

    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(outputDirectoryPath));
      writer.write(ioEntity.getOutputCode().toString());
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
