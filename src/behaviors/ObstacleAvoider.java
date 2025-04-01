package behaviors;

import lejos.robotics.subsumption.Behavior;
import hardware.MotorController;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class ObstacleAvoider implements Behavior {
	private MotorController motorController;
	private EV3UltrasonicSensor ultrasonicSensor;
	private boolean suppressed = false;
	private static final float OBSTACLE_DISTANCE = 0.20f;
	private static final int REVERSE_DURATION = 3000;
	private static final int FORWARD_DURATION = 5000;
	
	public ObstacleAvoider(MotorController motorController, EV3UltrasonicSensor ultrasonicSensor) {
		this.motorController = motorController;
		this.ultrasonicSensor = ultrasonicSensor;
	}
	@Override
	public boolean takeControl() {
		float[] sample = new float[1];
		ultrasonicSensor.getDistanceMode().fetchSample(sample, 0);
		return sample[0]<OBSTACLE_DISTANCE;
	}

	@Override
	public void action() {
		motorController.stop();
	}

	@Override
	public void suppress() {
	}
}
