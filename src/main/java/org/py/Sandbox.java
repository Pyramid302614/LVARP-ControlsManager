package org.py;

import java.util.ArrayList;

public class Sandbox {

    public Sandbox() {

        Controls.controlsLogger(true);

        Controls.addControl("test", Controls.BinaryComponents.A,new int[] {1})
                .linkControl(Controls.newControl("link-1", Controls.BinaryComponents.B,new int[] {1}));

        Controls.addControl("test2",Controls.ThresholdComponents.AX,"GREATER_THAN:0");

    }

    public static boolean ready = false;
    public static void sandboxProcess() {

    }

}
