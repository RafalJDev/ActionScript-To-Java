package newash.io.reader;

import lombok.extern.java.Log;
import newash.io.code.IOEntity;

import java.io.*;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
@Log
public class Reader {

    private final String fileToReadFrom = System.getProperty("user.dir") + /*"\\ActionScript-To-Java"+*/"\\files\\flashCodeJustActionScript.txt";

    private IOEntity ioEntity = IOEntity.getInstance();

    private StringBuilder stringBuilder = new StringBuilder(/*"import logica.data.DataMap;\n" +
            "import pl.logicsynergy.annotations.UiDesign;\n" +
            "import pl.logicsynergy.components.ComboBox;\n" +
            "import pl.logicsynergy.components.mdi.View;\n" +
            "import pl.logicsynergy.creator.UiCreator;\n" +
            "import pl.logicsynergy.database.DBQuery;\n\n"*/);

    public void openFileAndGetBufferedReader() {

        log.info("In openFileAndGetBufferedReader");
        // Open the file
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(fileToReadFrom);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ioEntity.setInputCode(new BufferedReader(new InputStreamReader(fstream)));
    }
    
    public void closeBufferedReader() {

        log.info("In closeBufferedReader");
        BufferedReader br = ioEntity.getInputCode();
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new NullPointerException("BufferedReader is null");
        }

//        listFilesAndFilesSubDirectories(System.getProperty("user.dir"));
    }

    public void listFilesAndFilesSubDirectories(String directoryName) {
        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                if (file.getAbsolutePath().contains("flashCodeJustActionScript")) {
                    System.out.println(file.getAbsolutePath());
                }
                System.out.println();
            } else if (file.isDirectory()) {
                listFilesAndFilesSubDirectories(file.getAbsolutePath());
            }
        }
    }

}
