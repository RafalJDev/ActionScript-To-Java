package newash.io.reader;

import lombok.extern.java.Log;
import newash.io.code.IOEntity;

import java.io.*;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
@Log
public class Reader {

    private final String fileName = "TabEstateOwner.txt";

    private  String filePathToReadFrom; //= System.getProperty("user.dir") + /*"\\ActionScript-To-Java"+*/"\\files\\" + fileName;

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
        listFilesAndFilesSubDirectories(System.getProperty("user.dir"));
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(filePathToReadFrom);
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
    }

    public void listFilesAndFilesSubDirectories(String directoryName) {
        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                if (file.getAbsolutePath().contains(fileName)) {
                    filePathToReadFrom = file.getAbsolutePath().trim();
                }
            } else if (file.isDirectory()) {
                listFilesAndFilesSubDirectories(file.getAbsolutePath());
            }
        }
    }
}
