package org.py;

import java.util.ArrayList;

public class Usage {

    public Usage() {

        // Handled by ControlsManager Controller Selection Interface, disregard this
        Controller masterController = new Controller();
        Controller controller2 = new Controller();
        Controller controller3 = new Controller();

        // ========================= MOST IMPORTANT PART! SET THE CONTROLLER(S)! =========================

            // Allows you to set controllers as one
            Controls.setController(masterController);
            // Allows you to set controllers as several
            ArrayList<Controller> controllers = new ArrayList<>();
            controllers.add(masterController);
            controllers.add(controller2);
            Controls.setControllers(controllers);
            // Allows you to add controllers later
            Controls.addController(controller3);

        // ========================= LOGGERS =========================

        // Joystick output is quite messy, so suppressJoystickOutput disables any joystick output
        Controls.inputLogger(true,true); // On for all controllers
        Controls.inputLogger(true,true,1); // On for just controller 1
        Controls.inputLogger(false,false,0); // When turning off, other values don't matter, because it won't be on!

        Controls.controlsLogger(true); // On
        Controls.controlsLogger(false); // Off

        Controls.errorLogger(true); // On
        Controls.errorLogger(false); // Off

        // =========================  CONTROLS =========================

        Controls.addBinaryControl("jump",Controls.BinaryComponents.A); // Defaults to "ACTIVE"
        Controls.addBinaryControl("releaseJump",Controls.BinaryComponents.A,"INACTIVE");
        Controls.addThresholdControl("shoot",Controls.ThresholdComponents.RT,"GREATER_THAN:0",new int[] { 1, 3 }); // Only for controllers 1 and 3
        Controls.addThresholdControl("shoot",Controls.ThresholdComponents.RT); // Defaults to "GREATER_THAN:0"
        Controls.addJoystickControl("moveForward",Controls.JoystickComponents.A,"north:0.5", new int[] { 2 }); // Only for controller 2

        // ========================= FUNCTIONS =========================

        // Must have identical name to the control made
        Controls.bindFunctionToControl("jump",true,false,msa -> {
            // When I coded this, I knew what MSA meant. Now I don't. MSA is pointless, and only exists so I can have the Consumer object.
            System.out.println("Jumped!");
        });

        // You can do these inside loops
        if(Controls.conditionResolve("jump")) {
            System.out.println("Currently holding jump!");
        }

    }

}
