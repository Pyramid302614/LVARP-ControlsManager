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
                controller.ja = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB) == 1;
                controller.jb = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB) == 1;
                controller.sa = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_BACK) == 1;
                controller.sb = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_START) == 1;
                controller.bb = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_GUIDE) == 1;
                controller.lt = state.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER);
                controller.rt = state.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER);
                controller.jax = state.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_X);
                controller.jay = state.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y);
                controller.jby = state.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y);
                controller.jbx = state.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X);
            }

            Controls.processAll();

            try {
                Thread.sleep(20);
            } catch(Exception ignored) {}
        }


    }
}
