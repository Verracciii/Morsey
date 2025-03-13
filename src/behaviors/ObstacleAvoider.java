package behaviors;

import lejos.robotics.subsumption.Behavior;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class ObstacleAvoider implements Behavior {
	
	EV3LargeRegulatedMotor mL;
	EV3LargeRegulatedMotor mR;
	EV3UltrasonicSensor US_SEN;

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub

	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
