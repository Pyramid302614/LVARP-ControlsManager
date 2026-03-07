package org.py;

public class TestingEnvironment {

    public static void main(String[] args) {
        Main.init();
        int lapses = 0;
        while(true) {
            Main.process();
            Sandbox.sandboxProcess();
            Polyware.process();
            Main.sleep(10);
            Polyware.process();
            Main.sleep(10);
            if(lapses > 60000) {
                System.out.println("Testing environment timed out.");
                break;
            }
            lapses++;
        }
    }

}
