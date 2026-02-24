package org.py;

import java.util.ArrayList;

public class Sandbox {

    public Sandbox() {

        Controls.controlsLogger(true);

        Controls.addControl("test", Controls.BinaryComponents.A)
                .linkControl(Controls.newControl("link-1", Controls.BinaryComponents.B));



    }
    public static void sandboxProcess() {

    }

}
