package frc.robot;


import java.util.HashMap;
import java.util.function.Consumer;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Controls.BinaryControls;
import frc.robot.Controls.JoystickControls;
import frc.robot.Controls.ThresholdControls;

public class Controls {

    public static boolean allowErrorPrinting = true;

    private static XboxController controller;

    private static HashMap<String,Control> controls = new HashMap<>();
    public static enum BinaryControls { A, B, X, Y, LB, RB };
    public static enum ThresholdControls { LT, RT };
    public static enum JoystickControls { A, B };
    
    public static Control addBinary(String name, BinaryControls control, String condition) {
        Control c = new Control(control,condition);
        controls.put(name,c);
        return c;
    }
    public static Control addThreshold(String name, ThresholdControls control, String condition) {
        Control c = new Control(control,condition);
        controls.put(name,c);
        return c;
    }
    public static Control addJoystick(String name, JoystickControls control, String condition) {
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

    public static double getJoystickAngle(JoystickControls joystick) {
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
        switch(joystick.joystickControl) {
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
        switch(c) {
            case "east":
                result = -22.5 < angle && angle < 22.5;
                break;
            case "northeast":
                result = 22.5 < angle && angle < 67.5;
                break;
            case "north":
                result = 67.5 < angle && angle < 112.5;
                break;
            case "northwest":
                result = 112.5 < angle && angle < 157.5;
                break;
            case "west":
                result = 157.5 < angle && angle < -157.5;
                break;
            case "southwest":
                result = -175.5 < angle && angle < -112.5;
                break;
            case "south":
                result = -112.5 < angle && angle < -67.5;
                break;
            case "southeast":
                result = -67.5 < angle && angle < -22.5;
                break;
            default:
                if(allowErrorPrinting) System.err.println("[ControlsManager] Unknown direction given: \"" + condition + "\"");
        }
        return invert?!result:result;
    }

    public static void bindFunctionToControl(String name, boolean executeOnceWhenBecomeTrue, Consumer<String> function) {
        Control c = controls.get(name);
        if(c != null) c.bindFunction(function,executeOnceWhenBecomeTrue);
    }

    public static void processAll() {
        Control[] c = (Control[]) controls.entrySet().toArray(); // Chance of working: a decent 5%
        for(int i = 0; i < c.length; i++) {
            if(c[i].conditionResolve(controller)) {
                if(
                    (c[i].bindedFunctionExecuteOnce && !c[i].executedBindedFunctionOnLastProcess)
                    || !c[i].bindedFunctionExecuteOnce)
                        c[i].bindedFunction();
                c[i].executedBindedFunctionOnLastProcess = true;
            } else {
                c[i].executedBindedFunctionOnLastProcess = false;
            }
        }
    }

}

class Control {

    public BinaryControls binaryControl;
    public ThresholdControls thresholdControl;
    public JoystickControls joystickControl;
    
    String condition;
    String type;

    Consumer<String> bindedFunction = null;
    public boolean executedBindedFunctionOnLastProcess = false;
    public boolean bindedFunctionExecuteOnce;

    public Control(BinaryControls control, String condition) {
        this.binaryControl = control;
        this.condition = condition;
        this.type = "binary";
    }
    public Control(ThresholdControls control, String condition) {
        this.thresholdControl = control;
        type = "threshold";
    }
    public Control(JoystickControls control, String condition) {
        this.joystickControl = control;
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
                switch(binaryControl) {
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
                switch(thresholdControl) {
                    case LT:
                        return complexConditionTrue(condition,controller.getLeftTriggerAxis(),"threshold");
                    case RT:
                        return complexConditionTrue(condition,controller.getRightTriggerAxis(),"threshold");
                }
            case "joystick":
                return Controls.getJoystickCondition(this,condition);
        }
        if(Controls.allowErrorPrinting) System.err.println("[ControlsManager] Unknown control type. (Type stored: " + type + ")");
        return false;
    }

    private static boolean complexConditionTrue(String complex, double value, String type) {
        String[] conditions = complex.split("\\|");
        boolean output = true;
        for(int i = 0; i < conditions.length; i++) {
            if(!conditionTrue(conditions[i],value,type)) output = false;
        }
        return output;
    }
    private static boolean conditionTrue(String condition, double value, String type) {
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
                        return value <= Double.parseDouble(split[1]);
                }
            case "binary":
                switch(split[0]) {
                    case "ACTIVE":
                        return value == 1.0;
                    case "INACTIVE":
                        return value == 0.0;
                }
            }
        if(Controls.allowErrorPrinting) System.err.println("[ControlsManager] Unknown condition type: \"" + split[0] + "\"");
        return false;
    }

    private boolean superConditionTrue(String condition,XboxController controller) {
        switch(type) {
            case "binary":
                if(condition.equals("ACTIVE")) return conditionResolve(controller);
                else if(condition.equals("INACTIVE")) return !conditionResolve(controller);
                break;
            case "threshold":
                switch(thresholdControl) {
                    case LT:
                        return complexConditionTrue(condition,controller.getLeftTriggerAxis(),"threshold");
                    case RT:
                        return complexConditionTrue(condition,controller.getRightTriggerAxis(),"threshold");
                }
        }
        if(Controls.allowErrorPrinting) System.err.println("[ControlsManager] Invalid type or condition string.");
        return false;
    }


}
