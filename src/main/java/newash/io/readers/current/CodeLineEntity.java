package newash.io.readers.current;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class CodeLineEntity {
  private static CodeLineEntity ourInstance = new CodeLineEntity();

  private String line;

  private int totalLineCount;

  public static CodeLineEntity getInstance() {
    return ourInstance;
  }

  private CodeLineEntity() {
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
    return "CodeLineEntity{" +
       "totalLineCount=" + totalLineCount +
       '}';
  }
}
