/**
 * 
 *  [[ Controls.java (ControlsManager-M1) ]]
 * ======================================================================
 *  
 *  ADD BINARY <name> <control>
 * 
 * 
 *  Methods:
 *      Controls.addBinary(name,control)
 *      Controls.addThreshold(name,control,condition)
 *      Controls.get(name)
 * 
 *  Binary Controls:
 *  - "A"
 *  - "B"
 *  - "X"
 *  - "Y"
 *  - "LB"
 *  - "RB"
 * 
 *  Threshold Controls:
 *  - "LT"
 *  - "RT"
 *
 *  Threshold Conditions
 *  --------------------------------------
 *  Threshold condition strings can contain one or more conditions to form
 *  a Complex Condition. <Seperate conditions with '|' (Vertical Pipe)>
 * 
 *  Condition types:
 *   - "LESS_THAN:x"
 *   - "LESS_THAN_OR_EQUAL_TO:x"
 *   - "EQUAL_TO:x"
 *   - "GREATER_THAN:X"
 *   - "GREATER_THAN_OR_EQUAL_TO:x"
 *   - "RANGE:x,y" (Inclusive, meaning it can equal x or y)
 * 
 *  Example complex conditions: (Condition Strings)
 *   - "LESS_THAN:30|GREATER_THAN_OR_EQUAL_TO:70.5" --> x < 30, x >= 70.5
 *   - "EQUAL_TO:2.75|EQUAL_TO:2.78" --> x == 2.75, x == 2.78
 *   - "RANGE:7,7.5" --> 7 <= x <= 7.5
 */

package frc.robot;


import java.util.HashMap;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Controls.BinaryControls;
import frc.robot.Controls.ThresholdControls;

public class Controls {

    public static boolean allowErrorPrinting = true;

    private static HashMap<String,Control> controls = new HashMap<>();
    public static enum BinaryControls { A, B, X, Y, LB, RB };
    public static enum ThresholdControls { LT, RT };
    
    public static Control addBinary(String name, BinaryControls control) {
        Control c = new Control(control);
        controls.put(name,c);
        return c;
    }
    public static Control addThreshold(String name, ThresholdControls control, String condition) {
        Control c = new Control(control,condition);
        controls.put(name,c);
        return c;
    }
    public static Control get(String name) {
        return controls.get(name);
    }
    public static boolean active(String name, XboxController controller) {
        return get(name).active(controller);
    }

    public double getJoystickAngle(String A_or_B, XboxController controller) {
        switch(A_or_B.toLowerCase()) {
            case "a":
                return Math.atan(controller.getLefY()/controller.getLeftX());
            case "b":
                return Math.atan(controller.getRightY()/controller.getRightX());
            default:
                if(allowErrorPrinting) System.err.println("[ControlsManager] Unknown input - Expected A or B and got \"" + A_or_B + "\""); 
        }
}

class Control {

    BinaryControls binaryControl;
    ThresholdControls thresholdControl; String condition;
    String type;

    public Control(BinaryControls control) {
        this.binaryControl = control;
        this.type = "binary";
    }
    public Control(ThresholdControls control, String condition) {
        this.thresholdControl = control;
        type = "threshold";
    }
    public boolean active(XboxController controller) {
        switch(type) {
            case "binary":
                switch(binaryControl) {
                    case A:
                        return controller.getAButton();
                    case B:
                        return controller.getBButton();
                    case X:
                        return controller.getXButton();
                    case Y:
                        return controller.getYButton();
                    case LB:
                        return controller.getLeftBumperButton();
                    case RB:
                        return controller.getRightBumperButton();
                }
            case "threshold":
                switch(thresholdControl) {
                    case LT:
                        return complexConditionTrue(condition,controller.getLeftTriggerAxis());
                    case RT:
                        return complexConditionTrue(condition,controller.getRightTriggerAxis());
                }
        }
        if(allowErrorPrinting) System.err.println("Unknown control type. (Type stored: " + type + ")");
        return false;
    }

    private boolean complexConditionTrue(String complex, double value) {
        String[] conditions = complex.split("\\|");
        boolean output = true;
        for(int i = 0; i < conditions.length; i++) {
            if(!conditionTrue(conditions[i],value)) output = false;
        }
        return output;
    }
    private boolean conditionTrue(String condition, double value) {
        String[] split = condition.split(":");
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
        if(allowErrorPrinting) System.err.println("Unknown condition type: \"" + split[0] + "\"");
        return false;
    }


}
