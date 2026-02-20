package org.py;

public class Controller {

    public String controllerState = updateControllerState();
    public String updateControllerState() {
        controllerState =
            a + "," +
            b + "," +
            x + "," +
            y + "," +
            lb + "," +
            rb + "," +
            dl + "," +
            dr + "," +
            du + "," +
            dd + "," +
            ja + "," +
            jb + "," +
            sa + "," +
            sb + "," +
            bb + "," +
            lt + "," +
            rt + "," +
            jax + "," +
            jay + "," +
            jbx + "," +
            jby + ",";
        return controllerState;
    }

    public String[] controllerStateOrder = {
        "Button A",
        "Button B",
        "Button X",
        "Button Y",
        "Left Trigger",
        "Right Trigger",
        "DPad Left",
        "DPad Right",
        "DPad Up",
        "DPad Down",
        "Joystick A Push",
        "Joystick B Push",
        "Special Button A",
        "Special Button B",
        "Big Button",
        "Left Bumper",
        "Right Bumper",
        "Joystick A - X",
        "Joystick A - Y",
        "Joystick B - X",
        "Joystick B - Y"
    };

    // Binary Controls
    public boolean a = false;
    public boolean b = false;
    public boolean x = false;
    public boolean y = false;
    public boolean dl = false;
    public boolean dr = false;
    public boolean du = false;
    public boolean dd = false;
    public boolean ja = false;
    public boolean jb = false;
    public boolean lb = false;
    public boolean rb = false;
    public boolean sa = false;
    public boolean sb = false;
    public boolean bb = false;

    // Threshold Controls
    public double lt = 0.0;
    public double rt = 0.0;

    // Joystick Controls
    public double jax = 0.0;
    public double jay = 0.0;
    public double jbx = 0.0;
    public double jby = 0.0;

}
