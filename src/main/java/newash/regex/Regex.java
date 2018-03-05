package newash.regex;

import sun.plugin2.main.client.MessagePassingExecutionContext;
import sun.plugin2.main.client.PrintBandDescriptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jaszczynski.Rafal on 03.03.2018.
 */
public class Regex {

  private Pattern pattern;
  private Matcher matcher;

  protected final String POLISH_WORDS = "[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]";

  protected String line;
  protected String found;
  protected boolean isFound;

  public String foundRegex(String regex) {
    doFindAndSetFound(regex, 0);
    return found;
  }

  public String foundRegex(String regex, int groupNumber) {
    doFindAndSetFound(regex, groupNumber);
    return found;
  }

  public boolean isFoundRegex(String regex) {
    doFindAndSetFound(regex, 0);
    return isFound;
  }

  public boolean isFoundRegex(String regex, int groupNumber) {
    doFindAndSetFound(regex, groupNumber);
    return isFound;
  }

  private void doFindAndSetFound(String regex, int groupNumber) {
    pattern = Pattern.compile(regex);
    matcher = pattern.matcher(line);
    found = "";
    isFound = matcher.find();
    if (isFound) {
      found = matcher.group(groupNumber);
    }
    pattern = null;
    matcher = null;
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }
}
