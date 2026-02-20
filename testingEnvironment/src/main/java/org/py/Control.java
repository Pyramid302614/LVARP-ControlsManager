package org.py;

import java.util.function.Consumer;

public class Control {

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
    public boolean conditionResolve(Controller controller) {
        switch(type) {
            case "binary":
                switch(binaryComponent) {
                    case A:
                        return complexConditionTrue(condition,controller.a?1.0:0.0,type);
                    case B:
                        return complexConditionTrue(condition,controller.b?1.0:0.0,type);
                    case X:
                        return complexConditionTrue(condition,controller.x?1.0:0.0,type);
                    case Y:
                        return complexConditionTrue(condition,controller.y?1.0:0.0,type);
                    case DL:
                        return complexConditionTrue(condition,controller.dl?1.0:0.0,type);
                    case DR:
                        return complexConditionTrue(condition,controller.dr?1.0:0.0,type);
                    case DU:
                        return complexConditionTrue(condition,controller.du?1.0:0.0,type);
                    case DD:
                        return complexConditionTrue(condition,controller.dd?1.0:0.0,type);
                    case LB:
                        return complexConditionTrue(condition,controller.lb?1.0:0.0,type);
                    case RB:
                        return complexConditionTrue(condition,controller.rb?1.0:0.0,type);
                    case SA:
                        return complexConditionTrue(condition,controller.sa?1.0:0.0,type);
                    case SB:
                        return complexConditionTrue(condition,controller.sb?1.0:0.0,type);
                    case BB:
                        return complexConditionTrue(condition,controller.bb?1.0:0.0,type);
                    case JA:
                        return complexConditionTrue(condition,controller.ja?1.0:0.0,type);
                    case JB:
                        return complexConditionTrue(condition,controller.jb?1.0:0.0,type);
                }
            case "threshold":
                switch(thresholdComponent) {
                    case LT:
                        return complexConditionTrue(condition,controller.lt,"threshold");
                    case RT:
                        return complexConditionTrue(condition,controller.rt,"threshold");
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
