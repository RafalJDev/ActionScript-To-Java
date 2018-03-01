package newash.actionscript.stage.stages.mapBAD.stage.bad;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class BoundariesOfStages {
    private static BoundariesOfStages ourInstance = new BoundariesOfStages();

    private static Map<StageBad, LineNumbers> firstAndlastOfStages = new HashMap<>();
    private StageBad stageBad;
    private LineNumbers lineNumbers;

    public static BoundariesOfStages getInstance() {
        return ourInstance;
    }

    private BoundariesOfStages() {
        firstAndlastOfStages.put(new StageBad("UiDesign"), null);
        firstAndlastOfStages.put(new StageBad("Imports"), null);
        firstAndlastOfStages.put(new StageBad("ActionScriptCode"), null);
        firstAndlastOfStages.put(new StageBad("Components"), null);
    }

    public int getfirstLineNumber(String stageName) {
        return firstAndlastOfStages.get(new StageBad(stageName)).getFirstLine();
    }

    public int getlastLineNumber(String stageName) {
        return firstAndlastOfStages.get(new StageBad(stageName)).getLastLine();
    }

    public void putfirstLineNumber(String stageName, int firstLineNumber) {
        stageBad = new StageBad(stageName);
        lineNumbers = new LineNumbers(firstLineNumber);
    }

    public void putlastLineNumber(String stageName, int lastLineNumber) {
        if (stageBad.getStageName() != stageName) {
            throw new IllegalArgumentException("StageBad : " + stageName + ", is not current stageBad");
        }
        lineNumbers.setLastLine(lastLineNumber);
        firstAndlastOfStages.put(stageBad, lineNumbers);
        stageBad = null;
        lineNumbers = null;
    }
}

class StageBad {
    private String stageName;

    public StageBad(String stageName) {
        if(stageName == null) {
            throw new NullPointerException("StageBad name cannot be null");
        }
        this.stageName = stageName;
    }

    public String getStageName() {
        return stageName;
    }
}

class LineNumbers {
    private int firstLine;
    private int lastLine;

    public LineNumbers(int firstLine) {
        if(firstLine <= 0) {
            throw new NumberFormatException("last line cannot be equal or less than 0. Value: " + firstLine);
        }
        this.firstLine = firstLine;
    }

    public int getFirstLine() {
        return firstLine;
    }

    public int getLastLine() {
        return lastLine;
    }

    public void setFirstLine(int firstLine) {
        this.firstLine = firstLine;
    }

    public void setLastLine(int lastLine) {
        if(lastLine <= 1) {
            throw new NumberFormatException("Last line cannot be equal or less than 1. Value: " + lastLine);
        } else if (firstLine >= lastLine) {
            throw new IllegalArgumentException("Last line: " + lastLine
                    + ", is lower or equal last of stageBad: " + firstLine);
        }
        this.lastLine = lastLine;
    }
}
