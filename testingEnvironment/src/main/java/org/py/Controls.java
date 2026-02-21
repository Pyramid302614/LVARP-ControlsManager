package org.py;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class Controls {

    public static boolean errorLoggerOn = true;
    private static boolean controlsLoggerOn = false;
    private static boolean inputLoggerOn = false;

    private static boolean suppressJoystickOutput = false;

    private static Controller controller;

    private static HashMap<String,Control> controls = new HashMap<>();
    public static enum BinaryComponents { A, B, X, Y, DL, DR, DU, DD, LB, RB, SA, SB, BB, JA, JB };
    public static enum ThresholdComponents { LT, RT };
    public static enum JoystickComponents { A, B };
    public static enum ComponentTypes { Binary, Threshold, Joystick };
    
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
                return Math.atan2(controller.getComponent("Joystick A - Y").value,controller.getComponent("Joystick A - X").value)*180.0/Math.PI;
            case B:
                return Math.atan2(controller.getComponent("Joystick B - X").value,controller.getComponent("Joystick B - Y").value)*180.0/Math.PI;
        }
        return 0.0;
    }
    public static boolean getJoystickCondition(Control joystick, String condition) {
        double x = 0.0;
        double y = 0.0;
        switch(joystick.joystickComponent) {
            case A:
                x = controller.getComponent(JoystickComponents.A).value.x;
                y = controller.getComponent(JoystickComponents.A).value.y;
                break;
            case B:
                x = controller.getComponent(JoystickComponents.B).value.x;
                y = controller.getComponent(JoystickComponents.B).value.y;
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

    public static void bindFunctionToControl(String name, boolean executeOnceWhenBecomeTrue, boolean executeOnActive, Consumer<String> function) {
        Control c = controls.get(name);
        if(c != null) c.bindFunction(function,executeOnceWhenBecomeTrue,executeOnActive);
    }

    @SuppressWarnings("unchecked")
    public static void processAll() {
        if(controller != null) {

            // Controls Logger
            Map.Entry<String,Control>[] controls = Controls.controls.entrySet().toArray(new Map.Entry[0]);
            for(Map.Entry<String,Control> control : controls) {

                Control c = control.getValue();
                String name = control.getKey();
                boolean resolved = c.conditionResolve(controller);

                if(controlsLoggerOn && c.conditionWasTrue != resolved) System.out.println("[ControlsManager:ControlsLogger] Control \"" + name + "\" new state detected: " + resolved);
                c.process(controller); // Must be last, because this syncs conditionWasTrue for the next time it's called

            }

            // Input Logger
            for(Component component : controller.components) {
                if(inputLoggerOn && component.value != component.previousValue) System.out.println("[ControlsManager:InputLogger] " + component.name + " new state detected: " + component.value);
                component.previousValue = component.value;

            }
            if(!suppressJoystickOutput) for(Joystick joystick : controller.joysticks) {

                if(
                    (joystick.value.x != joystick.previousValue.x) ||
                    (joystick.value.y != joystick.previousValue.y)
                ) System.out.println("[ControlsManager:InputLogger] " + joystick.name + " new state detected: " + joystick.value.toString());
                joystick.previousValue.x = joystick.value.x;
                joystick.previousValue.y = joystick.value.y;

            }
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

