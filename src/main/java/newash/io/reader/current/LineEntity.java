package newash.io.reader.current;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class LineEntity {
    private static LineEntity ourInstance = new LineEntity();

    private String line;

    private int totalLineCount;

    public static LineEntity getInstance() {
        return ourInstance;
    }

    private LineEntity() {
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public int getTotalLineCount() {
        return totalLineCount;
    }

    public void setTotalLineCount(int totalLineCount) {
        this.totalLineCount = totalLineCount;
    }

    @Override
    public String toString() {
        return "LineEntity{" +
                "totalLineCount=" + totalLineCount +
                '}';
    }
}
