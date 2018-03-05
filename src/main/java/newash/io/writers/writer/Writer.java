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
  protected IOEntity ioEntity;

  protected void save() {
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
}
