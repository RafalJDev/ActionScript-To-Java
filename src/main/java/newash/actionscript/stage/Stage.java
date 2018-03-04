package newash.actionscript.stage;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class Stage {

    protected String name;
    protected int firstLine;
    protected int lastLine;
    protected StringBuilder code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFirstLine() {
        return firstLine;
    }

    public void setFirstLine(int firstLine) {
        this.firstLine = firstLine;
    }

    public int getLastLine() {
        return lastLine;
    }

    public void setLastLine(int lastLine) {
        this.lastLine = lastLine;
    }

    public StringBuilder getCode() {
        return code;
    }

    public void setCode(StringBuilder code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Stage{" +
                "name='" + name + '\'' +
                ", firstLine=" + firstLine +
                ", lastLine=" + lastLine +
                '}';
    }
}
