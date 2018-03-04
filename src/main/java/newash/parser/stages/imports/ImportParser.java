package newash.parser.stages.imports;

import newash.actionscript.stage.stages.ImportStage;
import newash.io.code.IOEntity;
import newash.io.reader.current.LineEntity;
import newash.parser.stages.Parser;
import newash.regex.Regex;


/**
 * Created by Jaszczynski.Rafal on 03.03.2018.
 */
public class ImportParser extends Parser {

    ImportStage importStage;
    IOEntity ioEntity;

    private boolean isPackageAdded = false;

    private String packageName;

    private String stringBuilder = "import logica.data.Dat  aMap;\n" +
            "import pl.logicsynergy.annotations.UiDesign;\n" +
            "import pl.logicsynergy.components.ComboBox;\n" +
            "import pl.logicsynergy.components.mdi.View;\n" +
            "import pl.logicsynergy.creator.UiCreator;\n" +
            "import pl.logicsynergy.database.DBQuery;\n\n";

    public ImportParser() {
        importStage = ImportStage.getInstance();
        ioEntity = IOEntity.getInstance();
        lineEntity = LineEntity.getInstance();

        packageName = "package " + ioEntity.getFilePackage() + ";\n";
    }

    @Override
    public void parseThisStage() {

        line = lineEntity.getLine();

        if (isPackageAdded) {
            line = packageName + line;
        }

        if (line.contains(" mx")) {
            return;
        } else {
            importStage.getCode().append(line + "\n");
        }
    }
}
