package newash.parser.stages.fxDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaszczynski.Rafal on 04.03.2018.
 */
public class XmlField {

    static String id;
    static List<String> xmlFieldLines = new ArrayList<>();

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        XmlField.id = id;
    }

    public static List<String> getXmlFieldLines() {
        return xmlFieldLines;
    }

    public static void setXmlFieldLines(List<String> xmlFieldLines) {
        XmlField.xmlFieldLines = xmlFieldLines;
    }
}
