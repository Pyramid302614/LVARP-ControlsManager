package org.py;

public class TestingEnvironment {

    public static void main(String[] args) {
        Main.init();
        while(true) {
            Main.process();
            Sandbox.sandboxProcess();
            Polyware.process();
            Main.sleep(10);
            Polyware.process();
            Main.sleep(10);
        }
    }

}
