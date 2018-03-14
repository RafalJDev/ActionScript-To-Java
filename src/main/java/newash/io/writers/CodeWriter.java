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


//  private String outputDirectoryPath;

  public void saveFile() {
    log.info("In CodeWriter");

    outputDirectory = "output";
    ioEntity = (IOCodeEntity)IOCodeEntity.getInstance();
    findPathForOutputDirectory(System.getProperty("user.dir"));
    outputDirectoryPath += "\\" + ioEntity.getFileName() + "Przeparsowane.txt";

    save();
  }
}
