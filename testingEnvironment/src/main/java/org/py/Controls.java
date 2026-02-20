package org.py;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class Controls {

    public static boolean errorLoggerOn = true;
    private static boolean controlsLoggerOn = false;
    private static String controlsState = "";
    private static String previousControlsState = "";
    private static boolean inputLoggerOn = false;
    private static String previousControllerState;
    private static boolean suppressJoystickOutput = false;

    private static Controller controller;

    private static HashMap<String,Control> controls = new HashMap<>();
    public static enum BinaryComponents { A, B, X, Y, DL, DR, DU, DD, LB, RB, SA, SB, BB, JA, JB };
    public static enum ThresholdComponents { LT, RT };
    public static enum JoystickComponents { A, B };
    
    public static Control addBinary(String name, BinaryComponents control, String condition) {
        Control c = new Control(control,condition);
        controls.put(name,c);
        return c;
    }
    public static Control addThreshold(String name, ThresholdComponents control, String condition) {
        Control c = new Control(control,condition);
        controls.put(name,c);
        return c;
    }
    public static Control addJoystick(String name, JoystickComponents control, String condition) {
        Control c = new Control(control, condition);
        controls.put(name,c);
        return c;
    }
    public static void setController(Controller controller_) {
        controller = controller_;
    }
    public static Control get(String name) {
        return controls.get(name);
    }
    public static boolean conditionResolve(String name) {
        return get(name).conditionResolve(controller);
    }

    public static double getJoystickAngle(JoystickComponents joystick) {
        switch(joystick) {
            case A:
                return Math.atan2(controller.jay,controller.jax)*180.0/Math.PI;
            case B:
                return Math.atan2(controller.jby,controller.jbx)*180.0/Math.PI;
        }
        return 0.0;
    }

    // Probably doesn't work, i'm not sure how the joysticks respond, just guessed
    public static boolean getJoystickCondition(Control joystick, String condition) {
        double x = 0.0;
        double y = 0.0;
        switch(joystick.joystickComponent) {
            case A:
                x = controller.jax;
                y = controller.jay;
                break;
            case B:
                x = controller.jbx;
                y = controller.jby;
                break;
        } 
        boolean invert = (condition.charAt(0) == '!');
        String c = !invert?condition:condition.split("!")[1];
        boolean result = false;
        double angle = Math.atan2(y,x)*180.0/Math.PI;
        String direction = c.split(":")[0];
        switch(direction) {
            case "east":
                result = -22.5 < angle && angle < 22.5;
                break;
            case "southeast":
                result = 22.5 < angle && angle < 67.5;
                break;
            case "south":
                result = 67.5 < angle && angle < 112.5;
                break;
            case "southwest":
                result = 112.5 < angle && angle < 157.5;
                break;
            case "west":
                result = (157.5 < angle && angle < 180.0) || (-180.0 < angle && angle < -157.5);
                break;
            case "northwest":
                result = -175.5 < angle && angle < -112.5;
                break;
            case "north":
                result = -112.5 < angle && angle < -67.5;
                break;
            case "northeast":
                result = -67.5 < angle && angle < -22.5;
                break;
            default:
                if(errorLoggerOn) System.err.println("[ControlsManager:ErrorLogger] Unknown direction given: \"" + condition + "\"");
        }
        if(c.split(":").length > 1 && Math.sqrt(x*x+y*y) < Double.parseDouble(c.split(":")[1])) result = false;
        return invert?!result:result;
    }

    public static void bindFunctionToControl(String name, boolean executeOnceWhenBecomeTrue, Consumer<String> function) {
        Control c = controls.get(name);
        if(c != null) c.bindFunction(function,executeOnceWhenBecomeTrue);
    }

    @SuppressWarnings("unchecked")
    public static void processAll() {
        if(controller != null) {
            Map.Entry<String,Control>[] c = controls.entrySet().toArray(new Map.Entry[0]);
            for(int i = 0; i < c.length; i++) {
                if(c[i].getValue().conditionResolve(controller)) {
                    if(!c[i].getValue().bindedFunctionExecuteOnce || !c[i].getValue().executedBindedFunctionOnLastProcess)
                        c[i].getValue().bindedFunction();
                    c[i].getValue().executedBindedFunctionOnLastProcess = true;
                } else {
                    c[i].getValue().executedBindedFunctionOnLastProcess = false;
                }
            }
            if(controlsLoggerOn) {
                String[] past = previousControlsState.split(",");
                controlsState = "";
                for(int i = 0; i < c.length; i++) {
                    boolean v = c[i].getValue().conditionResolve(controller);
                    controlsState += v + (i != c.length-1?",":"");
                    if(i < past.length && past[i].equals("true") != v) {
                        System.out.println("[ControlsManager:ControlsLogger] Control \"" + c[i].getKey() + "\" new state detected: " + v);
                    }
                }
                previousControlsState = controlsState;
            }
            controller.updateControllerState();
            if(inputLoggerOn) {
                if(!Objects.equals(previousControllerState, controller.controllerState)) {
                    String[] controlOrder = controller.controllerStateOrder;
                    String[] past = (previousControllerState == null)?new String[0]:previousControllerState.split(",");
                    String[] now = controller.controllerState.split(",");
                    for(int i = 0; i < now.length; i++) {
                        if(i < past.length && !Objects.equals(past[i],now[i])) {
                            if(!suppressJoystickOutput || !controlOrder[i].split(" ")[0].equals("Joystick")) System.out.println("[ControlsManager:InputLogger] " + controlOrder[i] + " new state detected: " + now[i]);
                        }
                    }
                }
            }
            previousControllerState = controller.controllerState;
        }
    }


    public static void controlsLogger(boolean on) {
        controlsLoggerOn = on;
        System.out.println("[ControlsManager:ControlLogger] Controls Logger set to: " + on);
    }
    public static void inputLogger(boolean on, boolean suppressJoystickOutput) {
        inputLoggerOn = on;
        Controls.suppressJoystickOutput = suppressJoystickOutput;
        System.out.println("[ControlsManager:InputLogger] Input Logger set to: " + on);
    }
    public static void errorLogger(boolean on) {
        errorLoggerOn = on;
        System.out.println("[ControlsManager:ErrorLogger] Error Logger set to: " + on);
    }


}

