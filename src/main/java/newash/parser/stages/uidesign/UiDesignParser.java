package newash.parser.stages.uidesign;

import newash.actionscript.stage.stages.UiDesignStage;
import newash.io.readers.current.CodeLineEntity;
import newash.parser.stages.Parser;

/**
 * Created by Jaszczynski.Rafal on 03.03.2018.
 */
public class UiDesignParser extends Parser {

  UiDesignStage uiDesignStage;

  private final String REGEX_CLASS_TO_EXTEND = ":(\\w+)\\s";
  private final String REGEX_FORMNAME = "formName=\"([" + POLISH_WORDS + "\\s]+)\"";
  private final String REGEX_GUID = "GUID=\"(\\w+)\"";
  private int regexIndex = 1;

  public UiDesignParser() {
    uiDesignStage = UiDesignStage.getInstance();
    codeLineEntity = CodeLineEntity.getInstance();
  }

  @Override
  public void parseThisStage() {
    line = codeLineEntity.getLine();

    if (isFoundRegex(REGEX_CLASS_TO_EXTEND, 1)) {
      uiDesignStage.setClassToExtend(found);
      regexIndex++;
    } else if (isFoundRegex(REGEX_FORMNAME, 1)) {
      uiDesignStage.setFormName(found);
      regexIndex++;
    } else if (isFoundRegex(REGEX_GUID, 1)) {
      uiDesignStage.setGUID(found);
    }
  }
}