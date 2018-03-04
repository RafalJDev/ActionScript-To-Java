package newash.parser.stages.coponents;

import newash.actionscript.stage.stages.ActionScriptStage;
import newash.actionscript.stage.stages.ComponentsStage;
import newash.actionscript.stage.stages.UiDesignStage;
import newash.io.reader.current.LineEntity;
import newash.parser.stages.Parser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jaszczynski.Rafal on 04.03.2018.
 */
public class ComponentsParser extends Parser {

    ComponentsStage componentsStage;
    UiDesignStage uiDesignStage;

    private Map<String, String> replaceMap = new HashMap<>();

    public ComponentsParser() {
        lineEntity = LineEntity.getInstance();
        componentsStage = ComponentsStage.getInstance();
        uiDesignStage = UiDesignStage.getInstance();

        prepareReplaceMap();
    }

    @Override
    public void parseThisStage() {
        line = lineEntity.getLine();

        simpleReplaceAll();

        componentsStage.getCode().append(line + "\n");
    }

    private void simpleReplaceAll() {
        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            String oldValue = entry.getKey();
            String newValue = entry.getValue();
            line = line.replaceAll(oldValue, newValue);
        }
    }

    private void prepareReplaceMap() {
        replaceMap.put("grid.dbManager", "getDbManager");
        replaceMap.put("_dbManager", "getDbManager");
        replaceMap.put("frmFile=\"\\w+.frm\"", ""); //TODO something with sorting hashMap
        replaceMap.put("swf", "frm");
        replaceMap.put("<s:", "<c:");
        replaceMap.put("</s:", "</c:");
        replaceMap.put("<mx:", "</c:");
        replaceMap.put("</mx:", "</c:");
        replaceMap.put("skinClass=\".*\"", "");
        replaceMap.put(".*(c:Scroller).*", "");
        replaceMap.put(".*("+ uiDesignStage.getClassToExtend() +").*", "");
        //TODO <c:DateField id="dfReplacementBatteryDate" width="185" label="Data wymiany baterii" columnName="ReplacementBatteryDate"
        //TODO          dataField="REPLACEMENT_BATTERY_DATE" dbProvider="{getDbManager}" expandRatio="1" />
    }
}
