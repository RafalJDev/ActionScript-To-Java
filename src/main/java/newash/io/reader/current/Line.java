package newash.io.reader.current;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class Line {
    private static Line ourInstance = new Line();

    private String line;

    public static Line getInstance() {
        return ourInstance;
    }

    private Line() {
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
