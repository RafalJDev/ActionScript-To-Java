package newash.controller.stages;

import lombok.extern.java.Log;
import newash.io.code.IOImportEntity;
import newash.io.code.entity.IOEntity;
import newash.io.readers.current.CodeLineEntity;
import newash.io.readers.current.ImportLineEntity;
import newash.parser.stages.imports.ImportListParser;

import javax.management.BadAttributeValueExpException;
import java.io.IOException;

/**
 * Created by Jaszczynski.Rafal on 05.03.2018.
 */
@Log
public class ListImportController {

  IOEntity ioEntity = IOImportEntity.getInstance();
  ImportLineEntity importLineEntity = ImportLineEntity.getInstance();
  ImportListParser importListParser = new ImportListParser();
  String currentLine;

  public void parseImportList() {

    log.info("In parseAllStages");
    int lineCount = 0;

    try {
      while ((currentLine = ioEntity.getInputCode().readLine()) != null) {
        lineCount++;
        importLineEntity.setLine(currentLine);
        importListParser.parseImportList();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
      log.info("OCCURED ON LINE :" + lineCount);
    }

    try {
      importListParser.setToOutputCodeAndPrepareImportMap();
    } catch (BadAttributeValueExpException e) {
      e.printStackTrace();
    }
    importLineEntity.setTotalLineCount(lineCount);
  }
}
