package behaviors;
import lejos.hardware.Button;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import hardware.MotorController;
import main.Morsey;
public class ExitBehavior implements Behavior{
	private boolean suppressed = false;
	private static final long LONG_PRESS_DURATION = 6000;
	private EV3TouchSensor touchSensor;
	private EV3ColorSensor colorSensor;
	private EV3UltrasonicSensor ultrasonicSensor;
	private MotorController motorController;

    public ExitBehavior(EV3TouchSensor touchSensor, EV3ColorSensor colorSensor, EV3UltrasonicSensor ultrasonicSensor, MotorController motorController) {
        this.touchSensor = touchSensor;
        this.colorSensor = colorSensor;
        this.ultrasonicSensor = ultrasonicSensor;
        this.motorController = motorController;
    }
    public ExitBehavior(EV3TouchSensor touchSensor, MotorController motorController) {
        this(touchSensor, null, null, motorController);
    }
    public ExitBehavior(EV3ColorSensor colorSensor,EV3UltrasonicSensor ultrasonicSensor, MotorController motorController) {
        this(null, colorSensor, ultrasonicSensor, motorController);
    }
	@Override
	public boolean takeControl() {
		return Button.ESCAPE.isDown();
	}
	@Override
	public void action() {
		suppressed = false;
		long pressStartTime = System.currentTimeMillis();
		while(Button.ESCAPE.isDown()) {
			if(suppressed) return;
			Delay.msDelay(10);
		}
		long pressDuration = System.currentTimeMillis() - pressStartTime;
		if (pressDuration >= LONG_PRESS_DURATION) {
			cleanUpResources();
			System.exit(0);
		}
		else {
			if(!ProgramState.isInMorsey()) {
				ProgramState.setState("Morsey");
				Morsey.main(new String[] {});
			}
		}
	}
	@Override
	public void suppress() {
		suppressed = true;
	}
	private void cleanUpResources() {
		if (touchSensor != null) touchSensor.close();
        if (colorSensor != null) colorSensor.close();
        if (ultrasonicSensor != null) ultrasonicSensor.close();
        if (motorController != null) motorController.close();
	}
}
