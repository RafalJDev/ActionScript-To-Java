package newash.parser.stages.imports;

import newash.actionscript.stage.imports.ImportSet;
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
  ImportSet importSet = ImportSet.getInstance();
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

    line = codeLineEntity.getLine().trim();

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
      packageName = "package " + packageWithoutDots + ";\n";
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

  public void addImportLineIfCandidateHaveLine() {
    importStage.appendCode("\n");
    for (String candidate : importStage.getCandidatesToImportSet()) {
      String importLineForCandidate = getImportLineForCandidate(candidate);
      if (importLineForCandidate != null && importLineForCandidate != "") {
        if (!checkIfThisImportIsNotAlreadyInCode(importLineForCandidate))
          importStage.appendCode(importLineForCandidate + "\n");
      }
    }
    importStage.appendCode("\n");
  }

  public String getImportLineForCandidate(String candidate) {
    return importSet.getImportMap().entrySet().stream()
       .filter(e -> e.getKey().equals(candidate))
       .map(Map.Entry::getValue)
       .findFirst()
       .orElse(null);
  }

  public boolean checkIfThisImportIsNotAlreadyInCode(String importLineForCandidate) {
    String[] splitLines = importStage.getCode().toString().split("\n");
    for (String line : splitLines) {
      if (line.contains(importLineForCandidate)) {
        return true;
      }
    }
    return false;
  }
}