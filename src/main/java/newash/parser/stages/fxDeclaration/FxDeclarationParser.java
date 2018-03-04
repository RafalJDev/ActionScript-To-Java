package newash.parser.stages.fxDeclaration;

import newash.actionscript.stage.stages.FxDeclarationStage;
import newash.io.code.IOEntity;
import newash.io.reader.current.LineEntity;
import newash.parser.stages.Parser;
import newash.regex.Regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Jaszczynski.Rafal on 03.03.2018.
 */
public class FxDeclarationParser extends Parser {

    private boolean iHaveDoneIt = false;
    private List<String> dbMethods = new ArrayList<>();

    FxDeclarationStage fxDeclarationStage;

    public FxDeclarationParser() {
        lineEntity = LineEntity.getInstance();
        fxDeclarationStage = FxDeclarationStage.getInstance();
    }

    @Override
    public void parseThisStage() {
        line = lineEntity.getLine();
        parseXmlFields();
        fxDeclarationStage.getCode().append(line + "\n");
    }

    public void parseXmlFields() {

        if (!iHaveDoneIt) {
            iHaveDoneIt = true;

            if (isFoundRegex("\\w+=\"\\w+\"")) {
                String idRegex = found;
                XmlField.setId(idRegex.substring(idRegex.indexOf("\""), idRegex.length() - 1));
            }
        } else {
            XmlField.getXmlFieldLines().add(line);
        }
    }

    public void createMethodForFields() {

        if (!XmlField.xmlFieldLines.isEmpty()) {
        String methodString = "/*\n" +
                "\t * Metoda wyciągająca nazwy pól z XMLa\n" +
                "\t * \n" +
                "\t * @return obiekt Element zawierający nazwy pól\n" +
                "\t */\n" +
                "\t private Element " + XmlField.getId() + "()\n" +
                "\t {\n" +
                "\t\treturn XMLUtils.getXMLElement(\"";

        for (String line : XmlField.getXmlFieldLines()) {
            methodString += line + "\n";
        }

        methodString += "\");\n }\n\n";
        dbMethods.add(methodString);
        XmlField.setId("");
        XmlField.getXmlFieldLines().clear();
        iHaveDoneIt = false;
    }
    }

}
