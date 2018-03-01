package newash.actionscript.stage.stages;

import newash.actionscript.stage.Stage;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class FxDeclarationStage extends Stage {

    private static FxDeclarationStage ourInstance = new FxDeclarationStage();

    public static FxDeclarationStage getInstance() {
        return ourInstance;
    }

    private FxDeclarationStage() {
    }
}
