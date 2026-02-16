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
    private static String previousControllerState = XboxController.blankControllerState();
    private static boolean suppressJoystickOutput = false;

    private static XboxController controller;

    private static HashMap<String,Control> controls = new HashMap<>();
    public static enum BinaryComponents { A, B, X, Y, LB, RB };
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
    public static void setController(XboxController controller_) {
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
                return Math.atan2(controller.getLeftY(),controller.getLeftX())*180.0/Math.PI;
            case B:
                return Math.atan2(controller.getRightY(),controller.getRightX())*180.0/Math.PI;
        }
        return 0.0;
    }

    // Probably doesn't work, i'm not sure how the joysticks respond, just guessed
    public static boolean getJoystickCondition(Control joystick, String condition) {
        double x = 0.0;
        double y = 0.0;
        switch(joystick.joystickComponent) {
            case A:
                x = controller.getLeftX();
                y = controller.getLeftY();
                break;
            case B:
                x = controller.getRightX();
                y = controller.getRightY();
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
                    String[] controlOrder = {
                        "A Button",
                        "B Button",
                        "X Button",
                        "Y Button",
                        "Left Bumper",
                        "Right Bumper",
                        "Left Trigger",
                        "Right Trigger",
                        "Joystick A X",
                        "Joystick A Y",
                        "Joystick B X",
                        "Joystick B Y"
                    };
                    String[] past = previousControllerState.split(",");
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

class Control {

    public Controls.BinaryComponents binaryComponent;
    public Controls.ThresholdComponents thresholdComponent;
    public Controls.JoystickComponents joystickComponent;
    
    private String condition;
    private String type;

    private Consumer<String> bindedFunction = null;
    public boolean executedBindedFunctionOnLastProcess = false;
    public boolean bindedFunctionExecuteOnce;

    public Control(Controls.BinaryComponents component, String condition) {
        this.binaryComponent = component;
        this.condition = condition;
        this.type = "binary";
    }
    public Control(Controls.ThresholdComponents component, String condition) {
        this.thresholdComponent = component;
        this.condition = condition;
        type = "threshold";
    }
    public Control(Controls.JoystickComponents component, String condition) {
        this.joystickComponent = component;
        this.condition = condition;
        type = "joystick";
    }
    public void bindedFunction() {
        if(bindedFunction != null) {
            bindedFunction.accept("");
        }
    }
    public void bindFunction(Consumer<String> consumer,boolean once) {
        this.bindedFunctionExecuteOnce = once;
        this.bindedFunction = consumer;
    }
    public boolean conditionResolve(XboxController controller) {
        switch(type) {
            case "binary":
                switch(binaryComponent) {
                    case A:
                        return complexConditionTrue(condition,controller.getAButton()?1.0:0.0,type);
                    case B:
                        return complexConditionTrue(condition,controller.getBButton()?1.0:0.0,type);
                    case X:
                        return complexConditionTrue(condition,controller.getXButton()?1.0:0.0,type);
                    case Y:
                        return complexConditionTrue(condition,controller.getYButton()?1.0:0.0,type);
                    case LB:
                        return complexConditionTrue(condition,controller.getLeftBumperButton()?1.0:0.0,type);
                    case RB:
                        return complexConditionTrue(condition,controller.getRightBumperButton()?1.0:0.0,type);
                }
            case "threshold":
                switch(thresholdComponent) {
                    case LT:
                        return complexConditionTrue(condition,controller.getLeftTriggerAxis(),"threshold");
                    case RT:
                        return complexConditionTrue(condition,controller.getRightTriggerAxis(),"threshold");
                }
            case "joystick":
                return complexConditionTrue(condition,0,"joystick");
        }
        if(Controls.errorLoggerOn) System.err.println("[ControlsManager:ErrorLogger] Unknown control type. (Type stored: " + type + ")");
        return false;
    }

    private boolean complexConditionTrue(String complex, double value, String type) {
        String[] conditions = complex.split("\\|");
        boolean output = true;
        for(int i = 0; i < conditions.length; i++) {
            if(!conditionTrue(conditions[i],value,type)) output = false;
        }
        return output;
    }
    private boolean conditionTrue(String condition, double value, String type) { // Leaving value empty is ok for joysticks
        String[] split = condition.split(":");
        switch(type) {
            case "threshold":
                switch(split[0]) {
                    case "RANGE":
                        return Double.parseDouble(split[1].split(",")[0]) <= value
                        && value <= Double.parseDouble(split[1].split(",")[1]);
                    case "LESS_THAN":
                        return value < Double.parseDouble(split[1]);
                    case "LESS_THAN_OR_EQUAL_TO":
                        return value <= Double.parseDouble(split[1]);
                    case "EQUAL_TO":
                        return value == Double.parseDouble(split[1]);
                    case "GREATER_THAN_OR_EQUAL_TO":
                        return value >= Double.parseDouble(split[1]);
                    case "GREATER_THAN":
                        return value > Double.parseDouble(split[1]);
                }
            case "binary":
                switch(split[0]) {
                    case "ACTIVE":
                        return value == 1.0;
                    case "INACTIVE":
                        return value == 0.0;
                }
            case "joystick":
                return Controls.getJoystickCondition(this,condition);
        }
        if(Controls.errorLoggerOn) System.err.println("[ControlsManager:ErrorLogger] Unknown condition type: \"" + split[0] + "\"");
        return false;
    }

    public Control setCondition(String complexCondition) {
        condition = complexCondition;
        return this;
    }
    public Control setComponent(Controls.BinaryComponents control) {
        binaryComponent = control;
        type = "binary";
        return this;
    }
    public Control setComponent(Controls.ThresholdComponents control) {
        thresholdComponent = control;
        type = "threshold";
        return this;
    }
    public Control setComponent(Controls.JoystickComponents control) {
        joystickComponent = control;
        type = "joystick";
        return this;
    }


}
