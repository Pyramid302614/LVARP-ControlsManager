package org.py;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Controls {

    public static final int model = 4;

    public static boolean errorLoggerOn = true;
    private static boolean controlsLoggerOn = false;
    private static boolean inputLoggerOn = false;
    private static final ArrayList<Integer> inputLoggerControllers = new ArrayList<>();

    private static boolean suppressJoystickOutput = false;

    private static ArrayList<Controller> controllers;

    private static final ArrayList<Control> controls = new ArrayList<>();

    public enum BinaryComponents { A, B, X, Y, DL, DR, DU, DD, LB, RB, SA, SB, BB, JA, JB };
    public enum ThresholdComponents { LT, RT };
    public enum JoystickComponents { A, B };
    public enum ComponentTypes { Binary, Threshold, Joystick };


    private static int[] allControllers() {
        int[] c = new int[controllers.size()];
        for(int i = 0; i < c.length; i++) c[i] = i+1;
        return c;
    }
    public static void addBinaryControl(String name, BinaryComponents component, String condition) {
        addBinaryControl(name,component,condition,allControllers());
    }
    public static void addBinaryControl(String name, BinaryComponents component) {
        addBinaryControl(name,component,"ACTIVE",allControllers());
    }
    public static void addBinaryControl(String name, BinaryComponents component, int[] controllers) {
        addBinaryControl(name,component,"ACTIVE",controllers);
    }
    public static void addBinaryControl(String name, BinaryComponents component, String condition, int[] controllers) {
        Control c = new Control(name,component,condition,controllers);
        controls.add(c);
    }
    public static void addThresholdControl(String name, ThresholdComponents component, String condition) {
        addThresholdControl(name,component,condition,allControllers());
    }
    public static void addThresholdControl(String name, ThresholdComponents component) {
        addThresholdControl(name,component,"GREATER_THAN:0",allControllers());
    }
    public static void addThresholdControl(String name, ThresholdComponents component, int[] controllers) {
        addThresholdControl(name,component,"GREATER_THAN:0",controllers);
    }
    public static void addThresholdControl(String name, ThresholdComponents component, String condition, int[] controllers) {
        Control c = new Control(name,component,condition,controllers);
        controls.add(c);
    }
    public static void addJoystickControl(String name, JoystickComponents component, String condition) {
        addJoystickControl(name,component,condition,allControllers());
    }
    public static void addJoystickControl(String name, JoystickComponents component, String condition, int[] controllers) {
        Control c = new Control(name,component, condition, controllers);
        controls.add(c);
    }

    public static void setController(Controller controller) {
        ArrayList<Controller> c = new ArrayList<>(); c.add(controller);
        controllers = c;
    }
    public static void setControllers(ArrayList<Controller> controllers) {
        Controls.controllers = controllers;
    }
    public static void addController(Controller controller) {
        controllers.add(controller);
    }

    public static Control getControl(String name) {
        for(int i = 0; i < controls.size(); i++) {
            if(controls.get(i).name.equals(name)) return controls.get(i);
        }
        if(errorLoggerOn) System.out.println("[ControlsManager:ErrorLogger] conditionResolve("+name+") : invalid control name");
        return null;
    }

    public static boolean conditionResolve(String name) {
        Control c = getControl(name);
        if(c != null) for(int i = 0; i < c.controllers.length; i++) {
            if(c.conditionResolve(controllers.get(c.controllers[i]-1))) return true;
        }
        return false;
    }

    public static double getJoystickAngle(JoystickComponents joystick, int controller) {
        return Math.atan2(controllers.get(controller-1).getComponent(joystick).value.y, controllers.get(controller-1).getComponent(joystick).value.x)*180.0/Math.PI;
    }

    public static void bindFunctionToControl(String name, boolean executeOnceWhenBecomeTrue, boolean executeOnInactive, Consumer<String> function) {
        Control c = getControl(name);
        if(c != null) c.bindFunction(function,executeOnceWhenBecomeTrue,executeOnInactive);

    }

    public static void processAll() {

        // Controls Logger
        ArrayList<String> processedNames = new ArrayList<>();
        for(Control control : controls) {
            if(!processedNames.contains(control.name)) {
                processedNames.add(control.name);
                boolean resolved = false;
                Control c = getControl(control.name);
                if(c != null) {
                    for(Integer controller : c.controllers) {
                        if(c.conditionResolve(controllers.get(controller-1))) resolved = true; // OR Configuration (AND would be start as true, mark as false if it doesn't resolve)
                    }
                    control.conditionTrue = resolved;
                    if(controlsLoggerOn && (control.conditionWasTrue != control.conditionTrue))
                        System.out.println("[ControlsManager:ControlsLogger] Control \"" + control.name + "\" new state detected: " + control.conditionTrue);
                    control.process(); // Contains boundFunction execution code
                    control.conditionWasTrue = resolved;
                }
            }
        }

        // Input Logger
        for(int i = 0; i < controllers.size(); i++) {
            if(inputLoggerControllers.isEmpty() || inputLoggerControllers.contains(i+1)) {
                Controller controller = controllers.get(i);
                for(Component component : controller.components) {
                    if(inputLoggerOn && (component.value != component.previousValue))
                        System.out.println("[ControlsManager:InputLogger] Controller " + (i+1) + " - " + component.name + " new state detected: " + component.value);
                    component.previousValue = component.value;
                }
                if(inputLoggerOn && !suppressJoystickOutput) for(Joystick joystick : controller.joysticks) {
                    if(
                            (joystick.value.x != joystick.previousValue.x) ||
                                    (joystick.value.y != joystick.previousValue.y)
                    ) System.out.println("[ControlsManager:InputLogger] Controller " + (i+1) + " - " + joystick.name + " new state detected: " + joystick.value.toString());
                    joystick.previousValue.x = joystick.value.x;
                    joystick.previousValue.y = joystick.value.y;

                }
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
    public static void inputLogger(boolean on, boolean suppressJoystickOutput, int controller) {
        inputLoggerOn = on;
        inputLoggerControllers.add(controller);
        Controls.suppressJoystickOutput = suppressJoystickOutput;
        System.out.println("[ControlsManager:InputLogger] Input Logger set to: " + on + " (Controller " + controller + ")");
    }
    public static void errorLogger(boolean on) {
        errorLoggerOn = on;
        System.out.println("[ControlsManager:ErrorLogger] Error Logger set to: " + on);
    }


}

