package hardware;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class MotorController {
	private final EV3LargeRegulatedMotor leftMotor;
	private final EV3LargeRegulatedMotor rightMotor;

	private final static float WHEEL_DIAMETER = 51; // mm
	private final static float AXLE_LENGTH = 105; // mm
	private final static float ANGULAR_SPEED = 90; // deg/sec
	private final static float LINEAR_SPEED = 70; // mm/sec

	public MotorController(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
	}

	public void forward() {
		setMoveSpeed();
		leftMotor.forward();
		rightMotor.forward();
	}

	public void stop() {
		leftMotor.stop(true);
		rightMotor.stop();
	}

	public void turn(int degrees) {
		// Calculate wheel rotation needed for turn
		float wheelRotation = (degrees * AXLE_LENGTH) / WHEEL_DIAMETER;

		leftMotor.setSpeed(ANGULAR_SPEED);
		rightMotor.setSpeed(ANGULAR_SPEED);

		leftMotor.rotate((int) wheelRotation, true);
		rightMotor.rotate(-(int) wheelRotation);

		setMoveSpeed();
	}

	private void setMoveSpeed() {
		// Convert linear speed to motor degrees/sec
		float motorSpeed = (LINEAR_SPEED * 360) / (float) (Math.PI * WHEEL_DIAMETER);
		leftMotor.setSpeed(motorSpeed);
		rightMotor.setSpeed(motorSpeed);
	}

	public void followInstruction(String instruction) {
		switch (instruction.toUpperCase()) {
		case "FW":
			forward();
			break;
		case "ST":
			stop();
			break;
		case "TL":
			turn(-90);
			break;
		case "TR":
			turn(90);
			break;
		default:
			System.out.println("Unknown instruction");
		}
	}

	public void close() {
		leftMotor.close();
		rightMotor.close();
	}

	public EV3LargeRegulatedMotor getLeftMotor() {
		return leftMotor;
	}

	public EV3LargeRegulatedMotor getRightMotor() {
		return rightMotor;
	}
}