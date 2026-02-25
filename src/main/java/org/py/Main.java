//
//        __|              |             |        \  |
//       (      _ \    \    _|   _| _ \  | (_-<  |\/ |   _` |    \    _` |   _` |   -_)   _|
//      \___| \___/ _| _| \__| _| \___/ _| ___/ _|  _| \__,_| _| _| \__,_| \__, | \___| _|
//                                                                         ____/
//      Testing Environment - CM-M4

package org.py;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    private static boolean wpiMode = false;

    private static final boolean startToSelect = true; // Autoselects if joystick Start button (Special Button B) is pressed
    private static final boolean nameToSelect = false; // Autoselects joystick if it contains "Controller" in the name (Recommended only if one controller is being used)

    // (!) Start-to-select and Name-to-select is not compatible with WPIMode.

    public static ArrayList<Integer> selectedJIDs = new ArrayList<>(); // GLFW Mode
    public static ArrayList<Controller> controllers = new ArrayList<>();
    public static ArrayList<XboxControllerAdapter> xboxControllerAdapters = new ArrayList<>(); // WPI Mode

    public static void init() {

        System.out.println("============ ControlsManager Model " + Controls.model + " || Controller Selection ============");

        if(!GLFW.glfwInit()) {
            System.out.println("Failed to initialize GLFW. Falling back to WPI mode.");
            wpiMode = true;
        }
        if(!wpiMode) {

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

            boolean manual = !(nameToSelect || startToSelect);

            if(nameToSelect) {
                System.out.println("\nEntering name-to-select mode.");
                for(int i = 0; i < 15; i++) {
                    if(GLFW.glfwJoystickPresent(i)) {
                        String name = GLFW.glfwGetJoystickName(i);
                        if(name != null) if(name.toLowerCase().contains("controller")||name.toLowerCase().contains("logitech")) {
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

            for(int i = 0; i < selectedJIDs.size(); i++) controllers.add(new Controller());

        } else {
            if(startToSelect) System.out.println("(!) Start-to-select is not compatible with WPIMode.");
            if(nameToSelect) System.out.println("(!) Name-to-select is not compatible with WPIMode.");
            System.out.println("WPI Mode - Add controllers by their port number. Type \"done\" to finish.");
            Scanner sc = new Scanner(System.in);
            while(true) {
                System.out.print("| [As controller " + controllers.size() + "] Enter port number: ");
                String input = sc.nextLine();
                if(input.equals("done")) break;
                try {
                    int in = Integer.parseInt(input);
                    Controller controller = new Controller();
                    boolean add = true;
                    for(XboxControllerAdapter adapter : xboxControllerAdapters) {
                        if(adapter.xboxController.port == in) {
                            System.out.println("| (!) That port is already added.");
                            add = false;
                        }
                    }
                    // These two are using the same object, so they will sync, which is kinda beautiful
                    if(add) {
                        xboxControllerAdapters.add(new XboxControllerAdapter(new XboxController(0),controller));
                        controllers.add(controller);
                    }
                } catch(Exception ignored) {}
            }

        }

        Controls.setControllers(controllers);

    }

    public static void process() {

        if(!wpiMode) for(int i = 0; i < selectedJIDs.size(); i++) {
            Controller controller = controllers.get(i);
            GLFWGamepadState state = GLFWGamepadState.create();
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
                controller.getComponent(Controls.ThresholdComponents.AX).value = state.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_X);
                controller.getComponent(Controls.ThresholdComponents.BY).value = state.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y);
                controller.getComponent(Controls.ThresholdComponents.BX).value = state.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X);
                controller.getComponent(Controls.ThresholdComponents.AY).value = state.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y);
                controller.getComponent(Controls.JoystickComponents.A).value.x = state.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_X);
                controller.getComponent(Controls.JoystickComponents.A).value.y = state.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y);
                controller.getComponent(Controls.JoystickComponents.B).value.x = state.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X);
                controller.getComponent(Controls.JoystickComponents.B).value.y = state.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y);
            }
        }
        else for(int i = 0; i < xboxControllerAdapters.size(); i++) {
            xboxControllerAdapters.get(i).sync();
        }

        Controls.processAll();

    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch(Exception ignored) {}
    }

}
