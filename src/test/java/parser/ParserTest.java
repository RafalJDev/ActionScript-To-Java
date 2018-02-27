package parser;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Jaszczynski.Rafal on 27.02.2018.
 */
public class ParserTest {
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void logicForLines() throws Exception {
    }

    @Test
    public void testGivenXmlLineWhenSearchForMatchingIdThenMatchIdValue() throws Exception {
        String componentsXml = "<c:TextInput id=\"txtMeter1No\" width=\"80\" label=\"Liczba cyfr\" restrict=\"0-9\" columnName=\"Meter1No\"";

        //actually it's java, but that's enough here
        String actionScriptCode = "cbMeterKind.addEventListener(\"valueChanged\", event -> cbMeterKindChanged(event));\n" +
                "    \n" +
                "    txtMeter1No.addEventListener(\"valueChanged\", event -> txtMeter1NoChanged(event));\n" +
                "    txtMeter1MinNo.addEventListener(\"valueChanged\", event -> txtMeter1NoChanged(event));\n" +
                "    \n" +
                "    txtMeter2No.addEventListener(\"valueChanged\", event -> txtMeter2NoChanged(event));\n" +
                "    txtMeter2MinNo.addEventListener(\"valueChanged\", event -> txtMeter2NoChanged(event));\n" +
                "  }\n" +
                "  \n" +
                "  private void txtMeter1NoChanged(BaseEvent event)\n" +
                "  {\n" +
                "    lblMeter1Digit.text = getDigitLabel(txtMeter1No.getValue(), txtMeter1MinNo.getValue());\n" +
                "  }";
        String[] lines = actionScriptCode.split("\n");

        for (String line : lines) {
            Parser.setLine(line);
            System.out.println(line);

            Parser.convertActionScriptToJava();
        }
        System.out.println(Parser.getCandidatesForComponents().toString());

        Parser.setLine(componentsXml);
        Parser.parseXmlComponents();

        assertEquals("txtMeter1No", Parser.getComponentsThatNeedField());
    }

}