import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Controls.JoystickControls;

public class Usage {
    
    public XboxController controller = new XboxController(0);

    public Usage() {
    
        // Do before anything!
        Controls.setController(controller);

        Controls.addBinary("control1",Controls.BinaryControls.A,"ACTIVE");
        Controls.addThreshold("control2",Controls.ThresholdControls.LT,"RANGE:1.25,4");
        Controls.addThreshold("control3",Controls.ThresholdControls.RT,"LESS_THAN:10");

        Controls.conditionResolve("control2");
        Controls.get("control3").conditionResolve(controller);

        double joystick_angle = Controls.getJoystickAngle(Controls.JoystickControls.B);
        if(-22.5 < joystick_angle && joystick_angle < 22.5)
            System.out.println("Right joystick is going right");

        Controls.addJoystick("joystickA", JoystickControls.A, "north");
        if(Controls.conditionResolve("joystickA"))
            System.out.println("Left joystick is going forward");

        Controls.bindFunctionToControl("control1", false, msa -> {
            System.out.println("executeOnceWhenBecomeTrue is set to false, so \n"+
            "this will execute every time ControlsManager processes all binded functions.");
        });
        Controls.bindFunctionToControl("control3", true, msa -> {
            System.out.println("executeOnceWhenBecomeTrue is set to true so \n"+
            "this will only execute once as soon as the condition is met and won't \n"+
            "execute again until it becomes false then true again.");
        });

    }
    public static void functionOnTimer() {
        Controls.processAll();
    }
}
