package org.py;

public class TestingEnvironment {

    public static void main(String[] args) {
        Main.init();
        new Sandbox();
        Polyware.bootDiagnostic();
        while(true) {
            Main.process();
            Sandbox.sandboxProcess();
            Polyware.processDiagnostic();
            Main.sleep(20);
        }
    }

}
