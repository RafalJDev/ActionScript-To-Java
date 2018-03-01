package newash.actionscript.stage.stages;

import newash.actionscript.stage.Stage;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class ImportStage extends Stage {

    private static ImportStage ourInstance = new ImportStage();

    public static ImportStage getInstance() {
        return ourInstance;
    }

    private ImportStage() {
    }
}
