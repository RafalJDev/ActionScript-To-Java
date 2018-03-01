package newash.actionscript.stage.stages;

import newash.actionscript.stage.Stage;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class UiDesignStage extends Stage {

    private static UiDesignStage ourInstance = new UiDesignStage();

    public static UiDesignStage getInstance() {
        return ourInstance;
    }

    private UiDesignStage() {
    }
}
