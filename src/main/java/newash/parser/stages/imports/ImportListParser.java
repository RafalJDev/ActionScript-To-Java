package newash.parser.stages.imports;

import newash.actionscript.stage.imports.list.ImportList;
import newash.io.code.IOImportEntity;
import newash.io.code.entity.IOEntity;
import newash.io.readers.current.ImportLineEntity;
import newash.regex.Regex;

import javax.management.BadAttributeValueExpException;

/**
 * Created by Jaszczynski.Rafal on 05.03.2018.
 */
public class ImportListParser extends Regex {

  ImportLineEntity importLineEntity = ImportLineEntity.getInstance();
  IOEntity ioEntity = IOImportEntity.getInstance();
  ImportList importList = ImportList.getInstance();

  public void parseImportList() {
    line = importLineEntity.getLine().trim();

    if (isFoundRegex("import .*;")) {
      if (found.contains(".")) {
        if (!found.matches(".*;.*;.*")) {
          importList.addToSet(found);
        }
      }
    }
  }

  public void setToOutputCodeAndPrepareImportMap() throws BadAttributeValueExpException {
    for (String importLine : importList.getImportSet()) {
      ioEntity.appendOutputCode(importLine + "\n");
      line = importLine;
      if (isFoundRegex("\\.(\\w+);", 1)) {
        importList.putOnMap(found, importLine);
      } else {
        throw new BadAttributeValueExpException("Line: " + line + " .Don't match import regex");
      }
    }
  }
}