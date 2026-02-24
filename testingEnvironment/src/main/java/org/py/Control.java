package org.py;

import java.util.function.Consumer;

public class Control {

    public Controls.BinaryComponents binaryComponent;
    public Controls.ThresholdComponents thresholdComponent;
    public Controls.JoystickComponents joystickComponent;

    public int[] controllers;
    public String name;
    
    private String condition;
    public boolean conditionTrue;
    public boolean conditionWasTrue;
    private Controls.ComponentTypes type;

    public Function boundFunction;

    public Control(String name, Controls.BinaryComponents component, String condition, int[] controllers) {
        binaryComponent = component;
        this.name = name;
        this.condition = condition;
        type = Controls.ComponentTypes.Binary;
        this.controllers = controllers;
    }
    public Control(String name, Controls.ThresholdComponents component, String condition, int[] controllers) {
        thresholdComponent = component;
        this.name = name;
        this.condition = condition;
        type = Controls.ComponentTypes.Threshold;
        this.controllers = controllers;
    }
    public Control(String name, Controls.JoystickComponents component, String condition, int[] controllers) {
        joystickComponent = component;
        this.name = name;
        this.condition = condition;
        type = Controls.ComponentTypes.Joystick;
        this.controllers = controllers;
    }

    public void process() {
        if(boundFunction != null) {
            // ConditionTrue is set by ControlsLogger loop before all of this
            if(boundFunction.once && conditionTrue != conditionWasTrue)
                if(( boundFunction.onInactive && !conditionTrue)) boundFunction.execute();
                else if(!boundFunction.onInactive && conditionTrue) boundFunction.execute();
            if(conditionTrue && !boundFunction.once) boundFunction.execute();
        }
        // ConditionWasTrue set by controlsLogger as well
    }

    public void boundFunction() {
        if(boundFunction != null) boundFunction.execute();
    }
    public void bindFunction(Consumer<String> consumer,boolean once,boolean onInactive) {
        boundFunction = new Function(consumer,once,onInactive);
    }


    public boolean conditionResolve(Controller controller)  {
        switch(type) {
            case Binary:
                return complexConditionTrue(condition,controller.getComponent(binaryComponent).value,type);
            case Threshold:
                return complexConditionTrue(condition,controller.getComponent(thresholdComponent).value,type);
            case Joystick:
                return complexConditionTrue(condition,controller.getComponent(joystickComponent).value,type); // Value doesn't matter
        }
        if(Controls.errorLoggerOn) System.err.println("[ControlsManager:ErrorLogger] Unknown control type. (Type stored: " + type + ")");
        return false;
    }
    private boolean complexConditionTrue(String complex, double value, Controls.ComponentTypes type) {
        String[] conditions = complex.split("\\|");
        boolean output = true;
        for(int i = 0; i < conditions.length; i++) {
            if(!conditionTrue(conditions[i],value,type)) output = false;
        }
        return output;
    }
    private boolean complexConditionTrue(String complex, DPoint value, Controls.ComponentTypes type) {
        String[] conditions = complex.split("\\|");
        boolean output = true;
        for(int i = 0; i < conditions.length; i++) {
            if(!conditionTrue(conditions[i],value,type)) output = false;
        }
        return output;
    }
    private boolean conditionTrue(String condition, double value, Controls.ComponentTypes type) {
        String[] split = condition.split(":");
        switch(type) {
            case Threshold:
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
            case Binary:
                switch(split[0]) {
                    case "ACTIVE":
                        return value == 1.0;
                    case "INACTIVE":
                        return value == 0.0;
                }
        }
        if(Controls.errorLoggerOn) System.err.println("[ControlsManager:ErrorLogger] Unknown condition type: \"" + split[0] + "\"");
        return false;
    }
    private static boolean conditionTrue(String condition, DPoint value, Controls.ComponentTypes type) {
        if(type == Controls.ComponentTypes.Joystick) {
            boolean invert = (condition.charAt(0) == '!');
            String c = !invert?condition:condition.split("!")[1];
            boolean result = false;
            double angle = Math.atan2(value.y,value.x)*180.0/Math.PI;
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
                    if(Controls.errorLoggerOn) System.err.println("[ControlsManager:ErrorLogger] Unknown direction given: \"" + condition + "\"");
            }
            if(c.split(":").length > 1 && Math.sqrt(value.x*value.x+value.y*value.y) < Double.parseDouble(c.split(":")[1])) result = false;
            return invert != result;
        }
        return false;
    }

    public Control setCondition(String complexCondition) {
        condition = complexCondition;
        return this;
    }
    public Control setComponent(Controls.BinaryComponents control) {
        binaryComponent = control;
        type = Controls.ComponentTypes.Binary;
        return this;
    }
    public Control setComponent(Controls.ThresholdComponents control) {
        thresholdComponent = control;
        type = Controls.ComponentTypes.Threshold;
        return this;
    }
    public Control setComponent(Controls.JoystickComponents control) {
        joystickComponent = control;
        type = Controls.ComponentTypes.Joystick;
        return this;
    }


}
