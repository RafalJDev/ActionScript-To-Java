package newash.io.writer;

import newash.io.code.IOEntity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Jaszczynski.Rafal on 02.03.2018.
 */
public class Writer {

    private final String fileToWrite = System.getProperty("user.dir")
            + "\\ActionScript-To-Java\\files\\javaCodeByRJ.txt";
    private IOEntity ioEntity = IOEntity.getInstance();

    public void saveFile() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileToWrite));
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
