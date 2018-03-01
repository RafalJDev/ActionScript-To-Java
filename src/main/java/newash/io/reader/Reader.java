package newash.io.reader;

import newash.io.code.IOEntity;

import java.io.*;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class Reader {

    private final String fileToReadFrom = System.getProperty("user.dir") + "\\ActionScript-To-Java\\files\\flashCodeJustActionScript.txt";

    private IOEntity ioEntity = IOEntity.getInstance();

    private StringBuilder stringBuilder = new StringBuilder(/*"import logica.data.DataMap;\n" +
            "import pl.logicsynergy.annotations.UiDesign;\n" +
            "import pl.logicsynergy.components.ComboBox;\n" +
            "import pl.logicsynergy.components.mdi.View;\n" +
            "import pl.logicsynergy.creator.UiCreator;\n" +
            "import pl.logicsynergy.database.DBQuery;\n\n"*/);

    public void openFileAndGetBufferedReader() {

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
}
