package testEnvironment;

import testEnvironment.src.Controls;
import testEnvironment.src.XboxController;

public class main {
    public static void main(String args[]) {

        XboxController controller = new XboxController(1);

        while(true) {
            Controls.processAll();
            try {
                Thread.sleep(20); // Actual WPIlib robotPeriodic() refresh rate
            } catch(Exception ignored) {}
        }

    }
}