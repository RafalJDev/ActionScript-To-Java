package newash.controller;

import newash.io.reader.Reader;
import newash.io.writer.Writer;

/**
 * Created by Jaszczynski.Rafal on 02.03.2018.
 */
public class Controller {

    private Reader reader;
    private Writer writer;

    public void launchIt() {

        reader = new Reader();
        reader.openFileAndGetBufferedReader();

        reader.closeBufferedReader();

        writer = new Writer();
        writer.saveFile();
    }
}
