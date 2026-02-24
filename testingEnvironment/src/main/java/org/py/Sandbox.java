package org.py;

import java.util.ArrayList;

public class Sandbox {

    public Sandbox(ArrayList<Controller> controllers) {

        Controls.setControllers(controllers);

        Controls.addBinaryControl("test", Controls.BinaryComponents.A, "ACTIVE");

        // TODO: Add & and | in conditions
        // TODO: Add linkage abilities
        // TODO: Test bind functions (include non-once)


    }
    public static void sandboxProcess() {

        if(Controls.conditionResolve("test")) {
            System.out.println("Test is active!");
        }

    }

}
