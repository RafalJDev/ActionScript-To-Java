package newash.parser.stages.imports;

import newash.actionscript.stage.stages.ImportStage;
import newash.io.code.IOCodeEntity;
import newash.io.readers.current.CodeLineEntity;
import newash.parser.stages.Parser;
import newash.regex.Regex;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Jaszczynski.Rafal on 03.03.2018.
 */
public class ImportParser extends Parser {

  ImportStage importStage;
  IOCodeEntity ioCodeEntity;

  private boolean isPackageAdded = false;
  private String packageName;
  private Map<String, String> replaceMap = new HashMap<>();

  public ImportParser() {
    importStage = ImportStage.getInstance();
    ioCodeEntity = IOCodeEntity.getInstance();
    codeLineEntity = CodeLineEntity.getInstance();

    prepareReplaceMap();
  }

  @Override
  public void parseThisStage() {

    line = codeLineEntity.getLine();

    simpleReplaceAll();

    if (line.contains(" mx")) {
      return;
    } else {
      addPackage();
      importStage.getCode().append(line + "\n");
    }
  }

  private void addPackage() {
    if (!isPackageAdded) {
      Regex regex = new Regex();
      regex.setLine(ioCodeEntity.getFilePackage());
      String packageWithoutDots = regex.foundRegex("\\.(.*)\\.", 1);
      packageName = "package " + packageWithoutDots + ";\n\n";
      line = packageName + line;
      isPackageAdded = true;
    }
  }

  private void simpleReplaceAll() {
    for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
      String oldValue = entry.getKey();
      String newValue = entry.getValue();
      line = line.replaceAll(oldValue, newValue);
    }
  }

  public void prepareReplaceMap() {
    replaceMap.put("Swf", "Frm");
  }
}