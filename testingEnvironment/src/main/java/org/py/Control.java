package org.py;

import java.util.function.Consumer;

public class Control {

    public Controls.BinaryComponents binaryComponent;
    public Controls.ThresholdComponents thresholdComponent;
    public Controls.JoystickComponents joystickComponent;
    
    private String condition;
    public boolean conditionTrue;
    public boolean conditionWasTrue;
    private Controls.ComponentTypes type;

    public Function bindedFunction;

    public Control(Controls.BinaryComponents component, String condition) {
        this.binaryComponent = component;
        this.condition = condition;
        this.type = Controls.ComponentTypes.Binary;
    }
    public Control(Controls.ThresholdComponents component, String condition) {
        this.thresholdComponent = component;
        this.condition = condition;
        type = Controls.ComponentTypes.Threshold;
    }
    public Control(Controls.JoystickComponents component, String condition) {
        this.joystickComponent = component;
        this.condition = condition;
        type = Controls.ComponentTypes.Joystick;
    }

    public void process(Controller controller) {
        boolean resolved = conditionResolve(controller);
        if(bindedFunction != null) {
            // ConditionTrue is set by ControlsLogger loop
            if(bindedFunction.once && resolved != conditionWasTrue)
                if((bindedFunction.onInactive && !resolved)) bindedFunction.execute();
                else if(!bindedFunction.onInactive && resolved) bindedFunction.execute();
            if(resolved && !bindedFunction.once) bindedFunction.execute();
        }
        conditionWasTrue = resolved;
    }

    public void bindedFunction() {
        if(bindedFunction != null) bindedFunction.execute();
    }
    public void bindFunction(Consumer<String> consumer,boolean once,boolean onInactive) {
        bindedFunction = new Function(consumer,once,onInactive);
    }

    public boolean conditionResolve(Controller controller)  {
        switch(type) {
            case Binary:
                return complexConditionTrue(condition,controller.getComponent(binaryComponent).value,type);
            case Threshold:
                return complexConditionTrue(condition,controller.getComponent(thresholdComponent).value,type);
            case Joystick:
                return complexConditionTrue(condition,0.0,type); // Value doesn't matter
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
    private boolean conditionTrue(String condition, double value, Controls.ComponentTypes type) { // Leaving value empty is ok for joysticks
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
            case Joystick:
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
