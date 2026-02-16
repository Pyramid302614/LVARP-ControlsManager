package org.py;

public class XboxController {

    public String controllerState = blankControllerState();
    public void updateControllerState() {
        controllerState =
                a + "," +
                b + "," +
                x + "," +
                y + "," +
                lb + "," +
                rb + "," +
                lt + "," +
                rt + "," +
                lx + "," +
                ly + "," +
                rx + "," +
                ry + ",";
    }
    public static String blankControllerState() {
        return "false,false,false,false,false,false,-1.0,-1.0,0.0,0.0,0.0,0.0";
    }

    public boolean a = false; // A Button
    public boolean b = false; // B Button
    public boolean x = false; // X Button
    public boolean y = false; // Y Button
    public boolean lb = false; // Left Bumper
    public boolean rb = false; // Right Bumper
    public boolean dl = false; // DPad Left
    public boolean dr = false; // DPad Right
    public boolean du = false; // DPad Up
    public boolean dd = false; // DPad Down
    public boolean ja = false; // Left Joystick Push
    public boolean jb = false; // Right Joystick Push
    public boolean sa = false; // Special Button A (Top Left)
    public boolean sb = false; // Special Button B (Top Right)
    public boolean sc = false; // Special Button C (Bottom Left)
    public boolean bb = false; // Big Button

    public double lt = 0.0; // Left Trigger
    public double rt = 0.0; // Right Trigger

    public double lx = 0.0; // Left Joystick X
    public double ly = 0.0; // Left Joystick Y
    public double rx = 0.0; // Right Joystick X
    public double ry = 0.0; // Right Joystick Y

    public boolean getAButton() {
        return a;
    }
    public boolean getBButton() {
        return b;
    }
    public boolean getXButton() {
        return x;
    }
    public boolean getYButton() {
        return y;
    }
    public boolean getLeftBumperButton() {
        return lb;
    }
    public boolean getRightBumperButton() {
        return rb;
    }

    public double getLeftX() {
        return lx;
    }
    public double getLeftY() {
        return ly;
    }
    public double getRightX() {
        return rx;
    }
    public double getRightY() {
        return ry;
    }

    public double getLeftTriggerAxis() {
        return lt;
    }
    public double getRightTriggerAxis() {
        return rt;
    }

}
