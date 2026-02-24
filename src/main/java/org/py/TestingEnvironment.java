package org.py;

public class TestingEnvironment {

    public static void main(String[] args) {
        Main.init();
        while(true) {
            Main.process();
            Main.sleep(20);
        }
    }

}
