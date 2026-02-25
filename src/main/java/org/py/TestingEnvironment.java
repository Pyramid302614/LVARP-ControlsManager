package org.py;

public class TestingEnvironment {

    public static void main(String[] args) {
        Main.init();
        new Sandbox();
        while(true) {
            Main.process();
            Sandbox.process();
            Main.sleep(20);
        }
    }

}
