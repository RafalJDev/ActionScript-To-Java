package newash.parser.find.stages;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import lombok.extern.java.Log;
import newash.io.code.IOEntity;

import java.io.IOException;

/**
 * Created by Jaszczynski.Rafal on 02.03.2018.
 */
@Log
public class StagesFinder {

    IOEntity ioEntity = IOEntity.getInstance();

    public void findStages() {

        String line;

        try {
            while ((line = ioEntity.getInputCode().readLine()) != null) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
