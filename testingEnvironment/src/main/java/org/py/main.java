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
        Controls.setController(controller);

        // ControlsManager code here!!
        Controls.addBinary("test",Controls.BinaryControls.A,"ACTIVE");
        Controls.addThreshold("another_test",Controls.ThresholdControls.LT,"GREATER_THAN:0.0");
        Controls.bindFunctionToControl("test",true,msa -> {
            System.out.println("A button pressed!");
        });
        Controls.bindFunctionToControl("another_test",true,msa -> {
            System.out.println("Left Trigger threshold passed!");
        });

        GLFWGamepadState state = GLFWGamepadState.create();

        while(true) {

            if(GLFW.glfwGetGamepadState(selectedJID,state)) {
                controller.a = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_A) == 1;
                controller.b = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_B) == 1;
                controller.x = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_X) == 1;
                controller.y = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_Y) == 1;
                controller.lb = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER) == 1;
                controller.rb = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER) == 1;
                controller.lt = state.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER);
                controller.rt = state.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER);
            }

            Controls.processAll();

            try {
                Thread.sleep(20);
            } catch(Exception ignored) {}
        }


    }
}
