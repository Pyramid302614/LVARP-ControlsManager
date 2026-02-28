package org.py;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.util.ArrayList;

// so freaking tuff
public class Polyware {

    public static String assetsDirectory = System.getProperty("user.dir")+"/src/main/java/org/py/assets";

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

    private static Font calSans;
    private static boolean focused = false;
    private static double[] focusIndicator = new double[2]; // T, C
    private static double[][] modulePositions = new double[16][4]; // TX, CX, TY, CY
    private static double[][] moduleDimensions = new double[16][4]; // TW, CW, TH, CH
    private static boolean[] presentJIDs = new boolean[16];
    private static boolean[] previousPresentJIDs = new boolean[16];
    public static void draw(Graphics g) {
        if(calSans == null) try {
            calSans = Font.createFont(
                    Font.TRUETYPE_FONT,new File(assetsDirectory+"/CalSans.ttf")
            );
        } catch(Exception ignored) {
            System.out.println(ignored);
            calSans = new Font("Arial",Font.PLAIN,12);
        }
        if(mode == 2) {
            int x = 0;

            g.setFont(calSans.deriveFont(40f));
            g.setColor(new Color(0,0,0));
            g.drawString("Detected Controllers",20,30+20);

            // Focus indicator
            focusIndicator[0] = focused?10.0:0.0;
            focusIndicator[1] += (focusIndicator[0]-focusIndicator[1])/5.0;
            int f = (int)Math.round(focusIndicator[1]);
            g.setColor(new Color(0, 139, 255));
            g.fillRect(0,f,f,jp.getHeight());
            g.fillRect(0,jp.getHeight()-f,jp.getWidth(),f);
            g.fillRect(jp.getWidth()-f,0,f,jp.getHeight());
            g.fillRect(0,0,jp.getWidth(),f);

            Point mousePos = jp.getMousePosition();
            if(mousePos == null) mousePos = new Point(0,0);

            for(int jid = GLFW.GLFW_JOYSTICK_1; jid <= GLFW.GLFW_JOYSTICK_16; jid++) {
                if(presentJIDs[jid]) {

                    boolean aPressed = false;
                    if(GLFW.glfwJoystickPresent(jid)) {
                        GLFWGamepadState s = GLFWGamepadState.create();
                        GLFW.glfwGetGamepadState(jid, s);
                        aPressed = s.buttons(GLFW.GLFW_GAMEPAD_BUTTON_A) == 1;
                    }

                    moduleDimensions[jid][1] += (moduleDimensions[jid][0]-moduleDimensions[jid][1])/5.0;
                    moduleDimensions[jid][3] += (moduleDimensions[jid][2]-moduleDimensions[jid][3])/5.0;
                    modulePositions[jid][1] += (modulePositions[jid][0]-modulePositions[jid][1])/5.0;
                    modulePositions[jid][3] += (modulePositions[jid][2]-modulePositions[jid][3])/5.0;

                    DPoint d_dim = new DPoint(
                            100+moduleDimensions[jid][1],
                            100+moduleDimensions[jid][3]
                    );
                    Dimension dim = new Dimension(
                            (int)Math.round(d_dim.x),
                            (int)Math.round(d_dim.y)
                    );
                    Point pos = new Point(
                            x+20+(int)Math.round(modulePositions[jid][1]-(int)Math.round(moduleDimensions[jid][1])/2.0),
                            50+20+(int)Math.round(modulePositions[jid][3]-(int)Math.round(moduleDimensions[jid][3]/2.0))
                    );


                    if(mousePos.x > pos.x && mousePos.x < pos.x+dim.width &&
                        mousePos.y > pos.y && mousePos.y < pos.y+dim.height) {
                        moduleDimensions[jid][0] = -10.0;
                        moduleDimensions[jid][2] = -10.0;
                    } else {
                        moduleDimensions[jid][0] = 0.0;
                        moduleDimensions[jid][2] = 0.0;
                    }
                    if(previousPresentJIDs[jid] != presentJIDs[jid]) {
                        if(presentJIDs[jid]) {
                            moduleDimensions[jid][0] = 0.0;
                            moduleDimensions[jid][2] = 0.0;
                        }
                    }

                    g.setColor(new Color(200,200,200));
                    g.fillRoundRect(pos.x,pos.y,dim.width,dim.height,20,20);

                    g.setColor(new Color(150,150,150));
                    g.setFont(calSans.deriveFont(12f+(float)(moduleDimensions[jid][3]/2)));
                    g.drawString(Integer.toString(jid),pos.x+8,pos.y+8+(int)Math.round(12+moduleDimensions[jid][3]/2));

                    g.setColor(new Color(180,180,180));
                    int[] oval = new int[] {
                            pos.x + (int) Math.round(dim.width * 0.20), // x
                            pos.y + (int) Math.round(dim.height * 0.20), // y
                            (int) Math.round(dim.width * 0.60), // w
                            (int) Math.round(dim.height * 0.60) // h
                    };
                    g.fillOval(oval[0],oval[1],oval[2],oval[3]);

                    g.setColor(new Color(150,150,150));
                    g.setFont(calSans.deriveFont(30f+(float)(moduleDimensions[jid][3])/2));
                    g.drawString("A",(int)Math.round(oval[0]+oval[2]/2.0-g.getFontMetrics().stringWidth("A")/2.0),(int)Math.round(oval[1]+oval[3]/2.0+dim.height*0.09));
                    x += 100+20;

                }
            }
        }
    }

    private static int glfwInitTimer = 0;
    public static boolean controllerSelect() {
        mode = 2;
        boot();
        jf.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                focused = true;
            }
            @Override
            public void windowLostFocus(WindowEvent e) {
                focused = false;
            }
        });
        while(true) {
            jf.repaint();
            glfwInitTimer--;
            if(glfwInitTimer <= 0) {
                glfwInitTimer = 100;
                GLFW.glfwTerminate();
                GLFW.glfwInit();
                for(int i = GLFW.GLFW_JOYSTICK_1; i < GLFW.GLFW_JOYSTICK_16; i++) {
                    previousPresentJIDs[i] = presentJIDs[i];
                    presentJIDs[i] = GLFW.glfwJoystickPresent(i);
                }
            }
            Main.sleep(10);
        }
    }

}
