package org.py;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import java.util.Scanner;

public class main {

    private static Controller controller;

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

        int selectedJID = -1;
        for(int i = 0; i < 15; i++) {
            if(GLFW.glfwJoystickPresent(i)) {
                String name = GLFW.glfwGetJoystickName(i);
                if(name != null) if(name.toLowerCase().contains("controller")) {
                    GLFWGamepadState s = GLFWGamepadState.create();
                    GLFW.glfwGetGamepadState(i,s);
                    if(s.buttons(GLFW.GLFW_GAMEPAD_BUTTON_A) == 1) {
                        System.out.println("Selected controller with JID " + i);
                        selectedJID = i;
                    }
                }
            }
        }
        if(selectedJID == -1) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Select joystick: ");
            selectedJID = sc.nextInt();
            System.out.println("Selected controller with JID " + selectedJID);
        }

        Controller controller = new Controller();

        new Sandbox(controller);

        GLFWGamepadState state = GLFWGamepadState.create();

        while(true) {

            if(GLFW.glfwGetGamepadState(selectedJID,state)) {
                controller.getComponent(Controls.BinaryComponents.A).value = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_A);
                controller.getComponent(Controls.BinaryComponents.B).value = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_B);
                controller.getComponent(Controls.BinaryComponents.X).value = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_X);
                controller.getComponent(Controls.BinaryComponents.Y).value = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_Y);
                controller.getComponent(Controls.BinaryComponents.LB).value = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER);
                controller.getComponent(Controls.BinaryComponents.RB).value = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER);
                controller.getComponent(Controls.BinaryComponents.DL).value = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT);
                controller.getComponent(Controls.BinaryComponents.DR).value = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT);
                controller.getComponent(Controls.BinaryComponents.DU).value = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP);
                controller.getComponent(Controls.BinaryComponents.DD).value = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN);
                controller.getComponent(Controls.BinaryComponents.JA).value = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB);
                controller.getComponent(Controls.BinaryComponents.JB).value = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB);
                controller.getComponent(Controls.BinaryComponents.SA).value = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_BACK);
                controller.getComponent(Controls.BinaryComponents.SB).value = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_START);
                controller.getComponent(Controls.BinaryComponents.BB).value = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_GUIDE);
                controller.getComponent(Controls.ThresholdComponents.LT).value = state.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER);
                controller.getComponent(Controls.ThresholdComponents.RT).value = state.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER);
                controller.getComponent(Controls.JoystickComponents.A).value.x = state.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_X);
                controller.getComponent(Controls.JoystickComponents.A).value.y = state.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y);
                controller.getComponent(Controls.JoystickComponents.B).value.x = state.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y);
                controller.getComponent(Controls.JoystickComponents.B).value.y = state.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X);
            }

            Controls.processAll();

            try {
                Thread.sleep(20);
            } catch(Exception ignored) {}
        }


    }
}
