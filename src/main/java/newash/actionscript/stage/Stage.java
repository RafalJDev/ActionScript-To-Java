package newash.actionscript.stage;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class Stage {

    public String name;
    public int firstLine;
    public int lastLine;
    public StringBuilder code;

    @Override
    public String toString() {
        return "Stage{" +
                "name='" + name + '\'' +
                ", firstLine=" + firstLine +
                ", lastLine=" + lastLine +
                '}';
    }
}
