package newash.parser.stages.uidesign;

import newash.actionscript.stage.stages.UiDesignStage;
import newash.io.reader.current.LineEntity;
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
    lineEntity = LineEntity.getInstance();
  }

  @Override
  public void parseThisStage() {
    line = lineEntity.getLine();

    switch (regexIndex) {
      case 1:
        if (isFoundRegex(REGEX_CLASS_TO_EXTEND, 1)) {
          uiDesignStage.setClassToExtend(found);
          regexIndex++;
        }
        break;
      case 2:
        if (isFoundRegex(REGEX_FORMNAME, 1)) {
          uiDesignStage.setFormName(found);
          regexIndex++;
        }
        break;
      case 3:
        if (isFoundRegex(REGEX_GUID, 1)) {
          uiDesignStage.setGUID(found);
        }
        break;
      default:
        throw new IllegalArgumentException("Regex Index can't be > 3, regexIndex" + regexIndex);
    }
  }
}