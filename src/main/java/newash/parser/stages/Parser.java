package newash.parser.stages;

import newash.io.reader.current.LineEntity;
import newash.regex.Regex;

/**
 * Created by Jaszczynski.Rafal on 03.03.2018.
 */
public abstract class Parser extends Regex{

    protected LineEntity lineEntity;

    public abstract void parseThisStage();
}
