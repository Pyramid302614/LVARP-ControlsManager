package frc.robot;

import edu.wpi.first.wpilibj.XboxController;

public class Usage {
    
    public XboxController controller = new XboxController(0);

    public Usage() {
    
        Controls.addBinary("control1",Controls.BinaryControls.A);
        Controls.addThreshold("control2",Controls.ThresholdControls.LT,"RANGE:1.25,4");
        Controls.addThreshold("control3",Controls.ThresholdControls.RT,"LESS_THAN:10");

        Controls.active("control2",controller);
        Controls.get("control3").active(controller);

        double joystick_angle = Controls.getJoystickAngle("B",controller);
        if(-22.5 < joystick_angle && joystick_angle < 22.5)
            System.out.println("Right joystick is going right");
        if(Controls.getJoystickCondition("A", "north", controller))
            System.out.println("Left joystick is going forward");
    }
}
