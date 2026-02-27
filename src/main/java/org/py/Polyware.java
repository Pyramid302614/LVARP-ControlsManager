package org.py;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// so freaking tuff
public class Polyware {

    private static int mode = 0;
    // 0: inactive
    // 1: diagnostic
    // 2: controller select
    private static JFrame jf;
    private static JPanel jp;

    public static void boot() {

        jf = new JFrame("ControlsManager Polyware");
        jp = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                draw(g);
            }
        };

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int padding = 20;
        Dimension windowDimensions = new Dimension(500,500);
        jf.setBounds(
                screenSize.width-windowDimensions.width-padding,
                screenSize.height-windowDimensions.height-padding-48,
                windowDimensions.width,
                windowDimensions.height
        );
        jf.add(jp);
        jf.toFront();
        jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jf.setVisible(true);

    }
    public static void close() {

    }
    public static void process() {
        if(mode != 0) jf.repaint();
    }
    public static void draw(Graphics g) {
        if(mode == 2) {
            int x = 0;
            for(int jid = GLFW.GLFW_JOYSTICK_1; jid <= GLFW.GLFW_JOYSTICK_16; jid++) {
                if(GLFW.glfwJoystickPresent(jid)) {
                    GLFWGamepadState s = GLFWGamepadState.create();
                    GLFW.glfwGetGamepadState(jid,s);
                    g.drawString("Controller",x,30);
                    x += 30;
                }
            }
        }
    }

    public static boolean controllerSelect() {
        boot();
        while(true) {
            jf.repaint();
        }
    }

}
