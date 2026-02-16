package org.py;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import java.util.Scanner;

public class main {

    private static XboxController controller;

    public static void main(String[] args) {

        if(!GLFW.glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }
        System.out.println("GLFW initialized.");

        System.out.println("Detected Joysticks:");
        for(int jid = GLFW.GLFW_JOYSTICK_1; jid <= GLFW.GLFW_JOYSTICK_16; jid++) {
            if(GLFW.glfwJoystickPresent(jid)) {
                System.out.println(jid + " | " + GLFW.glfwGetJoystickName(jid));
            }
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Selected joystick: ");
        int selectedJID = sc.nextInt();

        XboxController controller = new XboxController();

        // ControlsManager code here!!
        Controls.setController(controller);
        Controls.controlsLogger(true);
        Controls.addBinary("jump", Controls.BinaryComponents.A,"ACTIVE");
        Controls.addBinary("exitMenu", Controls.BinaryComponents.B,"INACTIVE");
        Controls.addThreshold("place", Controls.ThresholdComponents.LT,"GREATER_THAN:0.5");
        Controls.addThreshold("break", Controls.ThresholdComponents.RT,"LESS_THAN:-0.5");
        Controls.addJoystick("moveForward", Controls.JoystickComponents.A,"north:0.5");
        Controls.addJoystick("turnButNotRightForSomeReason", Controls.JoystickComponents.B,"!east");

        GLFWGamepadState state = GLFWGamepadState.create();

        while(true) {

            if(GLFW.glfwGetGamepadState(selectedJID,state)) {
                controller.a = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_A) == 1;
                controller.b = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_B) == 1;
                controller.x = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_X) == 1;
                controller.y = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_Y) == 1;
                controller.lb = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER) == 1;
                controller.rb = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER) == 1;
                controller.dl = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT) == 1;
                controller.dr = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT) == 1;
                controller.du = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP) == 1;
                controller.dd = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN) == 1;
                controller.lt = state.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER);
                controller.rt = state.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER);
                controller.lx = state.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_X);
                controller.ly = state.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y);
                controller.rx = state.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X);
                controller.ry = state.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y);
            }

            Controls.processAll();

            try {
                Thread.sleep(20);
            } catch(Exception ignored) {}
        }


    }
}
