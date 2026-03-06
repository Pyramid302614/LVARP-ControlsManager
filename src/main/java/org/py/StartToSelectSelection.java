package org.py;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class StartToSelectSelection {

    public static Font calSans;

    public static void start() {

        Polyware.mode = 3;
        Polyware.boot();

    }
    private static int glfwInitTimer = 0;
    private static ArrayList<Integer> selectedJIDs = new ArrayList<>();
    private static ArrayList<Integer> presentJIDs = new ArrayList<>();
    private static boolean[] startPressed = new boolean[16];

    public static void process() {

        // GLFW Polling
        glfwInitTimer--;
        if(glfwInitTimer < 0) {
            glfwInitTimer = 100;
            GLFW.glfwPollEvents();
        }

        // Controller checking
        presentJIDs = new ArrayList<>();
        for(int jid = GLFW.GLFW_JOYSTICK_1; jid < GLFW.GLFW_JOYSTICK_16; jid++) {
            if(GLFW.glfwJoystickPresent(jid)) {
                presentJIDs.add(jid);
                GLFWGamepadState state = GLFWGamepadState.create();
                if(GLFW.glfwGetGamepadState(jid, state)) {
                    if(state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_START) == 1) {
                        if(!startPressed[jid]) { // Just pressed
                            if(selectedJIDs.contains(jid)) selectedJIDs.remove((Integer) jid);
                            else selectedJIDs.add(jid);
                        }
                        startPressed[jid] = true;
                    } else startPressed[jid] = false;
                    if(Polyware.jf != null && selectedJIDs.indexOf(jid) == 0 && state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_BACK) == 1) {
                        finish();
                    }
                } else startPressed[jid] = false;
            }
        }


        // JFrame repainting
        if(Polyware.jf != null) Polyware.jf.repaint();

    }

    private static double[][] moduleYOff = new double[16][2]; // T, C

    public static void draw(Graphics g) {

        int xOffset = 0;

        for(Integer jid : presentJIDs) {

            // Body rendering

            // JID Number
            if(selectedJIDs.contains(jid)) {

                g.setColor(new Color(0,0,0));
                g.setFont(calSans.deriveFont(24f));
                g.drawString(
                        selectedJIDs.indexOf(jid)+1+"",
                        xOffset+70-g.getFontMetrics().stringWidth(selectedJIDs.indexOf(jid)+1+"")/2,
                        Polyware.jf.getHeight()-200
                );

            }

            // Y calculating
            if(selectedJIDs.contains(jid)) {
                moduleYOff[jid][0] = 160.0;
            } else {
                moduleYOff[jid][0] = 90.0;
            }
            if(!presentJIDs.contains(jid)) {
                moduleYOff[jid][0] = 0;
                moduleYOff[jid][1] = 0;
            }
            moduleYOff[jid][1] += (moduleYOff[jid][0]-moduleYOff[jid][1])/5.0;

            g.setColor(new Color(200,200,200));
            g.fillRoundRect(
                    xOffset+20,
                    Polyware.jf.getHeight()-(int)Math.round(moduleYOff[jid][1]),
                    100,
                    100,
                    10,
                    10
            );
            g.setColor(new Color(180,180,180));
            int[] oval = new int[] {
                    xOffset+20 + (int) Math.round(100.0 * 0.20), // x
                    (int) Math.round(Polyware.jf.getHeight()-moduleYOff[jid][1] + 100.0 * 0.20), // y
                    (int) Math.round(100 * 0.60), // w
                    (int) Math.round(100 * 0.60) // h
            };
            g.fillOval(oval[0],oval[1],oval[2],oval[3]);

            xOffset += 120;

        }

    }

    private static void finish() {
        Polyware.exit();
        Polyware.jf = null;
        Polyware.jp = null;
        ArrayList<Controller> controllers = new ArrayList<>();
        for(int i = 0; i < selectedJIDs.size(); i++) controllers.add(new Controller());
        Controls.setControllers(controllers);
        Main.selectedJIDs = selectedJIDs;
        System.out.println("[ControlsManager:Polyware] Updated controllers: JIDs: " + selectedJIDs.toString());
        Sandbox.ready = true;
        new Sandbox();
    }

}
