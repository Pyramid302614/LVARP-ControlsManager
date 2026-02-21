package org.py;

public class Sandbox {

    public Sandbox(Controller controller) {

        Controls.setController(controller);

//        Controls.inputLogger(true,false);
//        Controls.controlsLogger(true);

        Controls.addJoystick("test",Controls.JoystickComponents.B,"north:0.5");

        Controls.bindFunctionToControl("test",true,true,msa -> {
            System.out.println("Hi!");
        });
//        Controls.bindFunctionToControl("a",true,msa -> {
//            System.out.println("A pressed (once)");
//        });


    }

}
