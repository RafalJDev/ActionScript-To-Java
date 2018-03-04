package newash.actionscript.stage.stages;

import newash.actionscript.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class ActionScriptStage extends Stage {

    private List<String> candidatesForComponents = new ArrayList<>();

    private static ActionScriptStage ourInstance = new ActionScriptStage();

    public static ActionScriptStage getInstance() {
        return ourInstance;
    }

    private ActionScriptStage() {
        name = "ActionScriptStage";
        code = new StringBuilder();
    }

    public List<String> getCandidatesForComponents() {
        return candidatesForComponents;
    }

    public void setCandidatesForComponents(List<String> candidatesForComponents) {
        this.candidatesForComponents = candidatesForComponents;
    }
}
