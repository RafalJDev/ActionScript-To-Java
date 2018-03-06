package newash.io.readers.current;

/**
 * Created by Jaszczynski.Rafal on 05.03.2018.
 */
public class ImportLineEntity {
  private static ImportLineEntity ourInstance = new ImportLineEntity();

  private String line;
  private int totalLineCount;

  public static ImportLineEntity getInstance() {
    return ourInstance;
  }

  private ImportLineEntity() {
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
}