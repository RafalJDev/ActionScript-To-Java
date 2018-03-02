package newash.actionscript.stage.stages;

import newash.actionscript.stage.Stage;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class ActionScriptStage extends Stage {


    private static ActionScriptStage ourInstance = new ActionScriptStage();

    public static ActionScriptStage getInstance() {
        return ourInstance;
    }

    private ActionScriptStage() {
        name = "ActionScriptStage";
    }
}
