package newash.actionscript.stage.stages;

import newash.actionscript.stage.Stage;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class ComponentsStage extends Stage {

    private static ComponentsStage ourInstance = new ComponentsStage();

    public static ComponentsStage getInstance() {
        return ourInstance;
    }

    private ComponentsStage() {
    }
}
