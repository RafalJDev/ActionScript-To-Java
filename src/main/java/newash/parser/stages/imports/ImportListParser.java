package newash.parser.stages.imports;

import newash.actionscript.stage.imports.ImportSet;
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
  ImportSet importSet = ImportSet.getInstance();

  public void parseImportList() {
    line = importLineEntity.getLine().trim();

    if (isFoundRegex("import .*;")) {
      if (found.contains(".")) {
        if (!found.matches(".*;.*;.*")) {
          importSet.addToSet(found);
        }
      }
    }
  }

  public void setToOutputCodeAndPrepareImportMap() throws BadAttributeValueExpException {
    for (String importLine : importSet.getImportSet()) {
      ioEntity.appendOutputCode(importLine + "\n");
      line = importLine;
      if (isFoundRegex("\\.(\\w+);", 1)) {
        importSet.putOnMap(found, importLine);
      } else {
        throw new BadAttributeValueExpException("Line: " + line + " .Don't match import regex");
      }
    }
  }
}