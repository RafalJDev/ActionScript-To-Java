package parser;

import oldasf.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jaszczynski.Rafal on 27.02.2018.
 */
public class ParserTest {

  @Test
  public void givenXmlLineWhenSearchForMatchingIdThenMatchIdValue() throws Exception {
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

//    Parser.findComponents();
    assertEquals("TextInput", Parser.getComponentsThatNeedField().get("txtMeter1No"));
  }

  @Test
  public void givenParsedSbWhenAddingComponentsThenComponentFieldsAdded() throws Exception {

    StringBuilder actionsScriptCode = new StringBuilder("\n" +
       "  \n" +
       "  import pl.logicsynergy.common.SwfObject;\n" +
       "  import pl.logicsynergy.components.mdi.View;\n" +
       "  import pl.logicsynergy.components.ui.UiCommon;\n" +
       "  import pl.logicsynergy.components.ui.UiMessageBox;\n" +
       "  import pl.logicsynergy.skins.TextInputDecimalSkin;\n" +
       "  import pl.logicsynergy.utils.StringUtils;\n" +
       "  import pl.logicsynergy.utils.UiUtils;\n" +
       "  \n" +
       "@UiDesign(formName = \"Typy liczników\", guid = \"formName=Typy liczników\")\n" +
       "public class YOURCLASSNAME extends null \n" +
       "{\n" +
       "  /** Konstruktor */\n" +
       "  public YOURCLASSNAME()\n" +
       "  {\n" +
       "\tUiCreator.getInstance(self).executeXML();\n" +
       "  }\n" +
       "  HANYS\n" +
       "  ViewTypeCounter;\n" +
       "  ViewTypeHeatCounter;\n" +
       "  ViewTypeWaterCounter;\n" +
       "  ViewTypePairTemperatureSensor;\n" +
       "  ViewTypeFlowCounter;\n" +
       "  \n" +
       "  private Object m_viewList =\n" +
       "    {\n" +
       "      I:{SWF:\"eMediaUiNetwork.swf\", FUNC:\"pl.logicsynergy.ui.network.meter.ViewTypeIntegrator\"},\n" +
       "      L:{SWF:\"eMediaUiNetwork.swf\", FUNC:\"pl.logicsynergy.ui.network.meter.ViewTypeCounter\"},\n" +
       "      C:{SWF:\"eMediaUiNetwork.swf\", FUNC:\"pl.logicsynergy.ui.network.meter.ViewTypeHeatCounter\"},\n" +
       "      W:{SWF:\"eMediaUiNetwork.swf\", FUNC:\"pl.logicsynergy.ui.network.meter.ViewTypeWaterCounter\"},\n" +
       "      T:{SWF:\"eMediaUiNetwork.swf\", FUNC:\"pl.logicsynergy.ui.network.meter.ViewTypePairTemperatureSensor\"},\n" +
       "      P:{SWF:\"eMediaUiNetwork.swf\", FUNC:\"pl.logicsynergy.ui.network.meter.ViewTypeFlowCounter\"}\n" +
       "    };");

    Parser.getComponentsThatNeedField().put("txtMeter1No", "TextInput");
    Parser.getComponentsThatNeedField().put("cbMeterKind", "TextInput");
    Parser.getComponentsThatNeedField().put("lblMeter1Digit", "TextInput");
    actionsScriptCode = Parser.secondParsingForAddingComponents(actionsScriptCode, "HANYS");

    assertTrue(actionsScriptCode.toString().contains("public TextInput cbMeterKind;"));

    System.out.println(actionsScriptCode);
  }

  @Test
  public void givenDbQueryThenCreateDbQueryMethod() throws Exception {

    String dbQueryString = "<fx:Declarations>\n" +
       "                  <db:DBQuery id=\"dbQuery\" sqlFrom=\"Network_MeterType\"\n" +
       "            sqlField=\"{dbField}\"\n" +
       "            sqlOrderBy=\"Id\"\n" +
       "            sqlIdentityColumn=\"Id\" sqlTable=\"Network_MeterType\"/>\n" +
       "                  \n" +
       "                  <db:DBQuery id=\"dbDescMeterProducer\" sqlFrom=\"Network_MeterProducer\"\n" +
       "            sqlFieldString=\"Id, Symbol, Name\" sqlOrderBy=\"Symbol\"\n" +
       "            sqlTable=\"Network_MeterProducer\" sqlIdentityColumn=\"Id\"  />\n" +
       "                  </fx:Declarations>";

    String[] dbQueryLines = dbQueryString.split("\n");

    for (String line : dbQueryLines) {
      Parser.setLine(line);
//      Parser.parseDbQuery();
    }

    System.out.println(Parser.getDbMethods().toString());
    assertTrue(Parser.getDbMethods().size() == 2);
  }
}