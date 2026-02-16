package org.py;

public class XboxController {

    public boolean a = false;
    public boolean b = false;
    public boolean x = false;
    public boolean y = false;
    public boolean lb = false;
    public boolean rb = false;

    public double lt = 0.0;
    public double rt = 0.0;

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
        return 0.0;
    }
    public double getLeftY() {
        return 0.0;
    }
    public double getRightX() {
        return 0.0;
    }
    public double getRightY() {
        return 0.0;
    }

    public double getLeftTriggerAxis() {
        return lt;
    }
    public double getRightTriggerAxis() {
        return rt;
    }

}
