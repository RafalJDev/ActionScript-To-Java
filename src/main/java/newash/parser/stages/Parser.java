package newash.parser.stages;

import newash.io.readers.current.CodeLineEntity;
import newash.regex.Regex;

/**
 * Created by Jaszczynski.Rafal on 03.03.2018.
 */
public abstract class Parser extends Regex {

  protected CodeLineEntity codeLineEntity;

  public abstract void parseThisStage();
}
