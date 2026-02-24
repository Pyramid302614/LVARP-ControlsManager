package org.py;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    private static final boolean startToSelect = true; // Autoselects if joystick Start button (Special Button B) is pressed
    private static final boolean nameToSelect = false; // Autoselects joystick if it contains "Controller" in the name (Recommended only if one controller is being used)

    public static void main(String[] args) {

        System.out.println("============ ControlsManager Model " + Controls.model + " || Controller Selection ============");

        if(!GLFW.glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }
        System.out.println("\nGLFW initialized.");

        System.out.println("\nJID | NAME             Detected Controllers\n--------------------------------------------");
        short detected = 0;
        for(int jid = GLFW.GLFW_JOYSTICK_1; jid <= GLFW.GLFW_JOYSTICK_16; jid++) {
            if(GLFW.glfwJoystickPresent(jid)) {
                GLFWGamepadState s = GLFWGamepadState.create();
                GLFW.glfwGetGamepadState(jid,s);
                System.out.println(" " + jid + "  |  " + GLFW.glfwGetJoystickName(jid) + (s.buttons(GLFW.GLFW_GAMEPAD_BUTTON_A) == 1? " (A is pressed)":""));
                detected++;
            }
        }

        if(detected == 0) System.out.println("<No controllers detected>");

        ArrayList<Integer> selectedJIDs = new ArrayList<>();
        boolean manual = !(nameToSelect || startToSelect);

        if(nameToSelect) {
            System.out.println("\nEntering name-to-select mode.");
            for(int i = 0; i < 15; i++) {
                if(GLFW.glfwJoystickPresent(i)) {
                    String name = GLFW.glfwGetJoystickName(i);
                    if(name != null) if(name.toLowerCase().contains("controller")) {
                        System.out.println("| Selected controller with JID " + i);
                        selectedJIDs.add(i);
                    }
                }
            }
            System.out.println("Finished selection.");
        }
        else if(startToSelect) {
            boolean done = false;
            System.out.println("\nEntering start-to-select mode. Press the START button on your controller to select it. The master controller determines when selection is over.");
            while(!done) {
                for(int i = 0; i < 15; i++) {
                    GLFWGamepadState s = GLFWGamepadState.create();
                    GLFW.glfwGetGamepadState(i,s);
                    if(!selectedJIDs.contains(i) && s.buttons(GLFW.GLFW_GAMEPAD_BUTTON_START) == 1) {
                        selectedJIDs.add(i);
                        if(selectedJIDs.size() == 1) System.out.println("| Master Controller selected. (JID:" + i + ")\n| | [HOLD BACK ON MASTER CONTROLLER TO FINISH SELECTION, HOLD A TO SELECT ADDITIONAL CONTROLLERS MANUALLY]");
                        else System.out.println("| Controller " + selectedJIDs.size() + " selected. (JID:" + i + ")");
                    } else if(s.buttons(GLFW.GLFW_GAMEPAD_BUTTON_BACK) == 1 && !selectedJIDs.isEmpty() && selectedJIDs.get(0) == i) {
                        done = true;
                        System.out.println("Finished selection.");
                    } else if(s.buttons(GLFW.GLFW_GAMEPAD_BUTTON_A) == 1 && !selectedJIDs.isEmpty() && selectedJIDs.get(0) == i) {
                        done = true;
                        manual = true;
                    }
                }
                sleep(200);
            }
        }

        if(manual) {
            System.out.println("\nEntering manual mode. [ENTER \"DONE\" TO FINISH SELECTION]");
            while(true) {
                Scanner sc = new Scanner(System.in);
                System.out.print("| [As controller " + (selectedJIDs.size()+1) + "] Enter joystick by JID: ");
                String input = sc.nextLine();
                if(Objects.equals(input,"done")) break;
                try { if(!selectedJIDs.contains(Integer.parseInt(input))) {
                    selectedJIDs.add(Integer.parseInt(input));
                    if(selectedJIDs.size() == 1) System.out.println("| | Master Controller selected. (JID:" + input + ")");
                    else System.out.println("| | Controller " + selectedJIDs.size() + " selected. (JID:" + input + ")");
                } else {
                    System.out.println("| | (!) That JID is already selected as Controller " + selectedJIDs.indexOf(Integer.parseInt(input)));
                } } catch(Exception ignored) {}
            }
            System.out.println("Finished Selection.");
        }

        System.out.println();

        ArrayList<Controller> controllers = new ArrayList<>();
        for(int i = 0; i < selectedJIDs.size(); i++) controllers.add(new Controller());
        new Sandbox(controllers);

        GLFWGamepadState state = GLFWGamepadState.create();

        while(true) {

            for(int i = 0; i < selectedJIDs.size(); i++) {
                Controller controller = controllers.get(i);
                if(GLFW.glfwGetGamepadState(selectedJIDs.get(i),state)) {
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
                    controller.getComponent(Controls.JoystickComponents.A).value.y = state.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y);
                    controller.getComponent(Controls.JoystickComponents.B).value.x = state.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X);
                    controller.getComponent(Controls.JoystickComponents.B).value.y = state.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y);
                }
            }

            Controls.processAll();
            Sandbox.sandboxProcess();

            sleep(20);
        }


    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch(Exception ignored) {}
    }

}
