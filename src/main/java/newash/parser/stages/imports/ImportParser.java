package newash.parser.stages.imports;

import newash.actionscript.stage.stages.ImportStage;
import newash.io.code.IOEntity;
import newash.io.reader.current.LineEntity;
import newash.parser.stages.Parser;
import newash.regex.Regex;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Jaszczynski.Rafal on 03.03.2018.
 */
public class ImportParser extends Parser {

  ImportStage importStage;
  IOEntity ioEntity;

  private boolean isPackageAdded = false;
  private String packageName;
  private Map<String, String> replaceMap = new HashMap<>();

  private String stringBuilder =
     "import logica.data.Dat  aMap;\n" +
        "import pl.logicsynergy.annotations.UiDesign;\n" +
        "import pl.logicsynergy.components.ComboBox;\n" +
        "import pl.logicsynergy.components.mdi.View;\n" +
        "import pl.logicsynergy.creator.UiCreator;\n" +
        "import pl.logicsynergy.database.DBQuery;\n\n";

  public ImportParser() {
    importStage = ImportStage.getInstance();
    ioEntity = IOEntity.getInstance();
    lineEntity = LineEntity.getInstance();

    prepareReplaceMap();
  }

  @Override
  public void parseThisStage() {

    line = lineEntity.getLine();

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
      regex.setLine(ioEntity.getFilePackage());
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