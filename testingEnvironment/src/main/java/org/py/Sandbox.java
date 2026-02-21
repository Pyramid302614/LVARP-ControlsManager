package org.py;

public class Sandbox {

    public Sandbox(Controller controller) {

        Controls.setController(controller);

//        Controls.inputLogger(true,false);
        Controls.controlsLogger(true);

        Controls.addBinary("a",Controls.BinaryComponents.A,"ACTIVE");
//        Controls.bindFunctionToControl("a",true,msa -> {
//            System.out.println("A pressed (once)");
//        });


    }

}
