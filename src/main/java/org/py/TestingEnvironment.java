package org.py;

public class TestingEnvironment {

    public static void main(String[] args) {
        Main.init();
        new Sandbox();
        Polyware.boot();
        while(true) {
            Main.process();
            Sandbox.sandboxProcess();
            Main.sleep(20);
        }
    }

}
