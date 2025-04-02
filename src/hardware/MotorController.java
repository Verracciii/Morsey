package hardware;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.utility.Delay;

public class MotorController {
	private final EV3LargeRegulatedMotor leftMotor;
	private final EV3LargeRegulatedMotor rightMotor;

	// Physical constants
	private final static float WHEEL_DIAMETER = 51; // mm
	private final static float AXLE_LENGTH = 105; // mm
	private final static float ANGULAR_SPEED = 38; // deg/sec
	private final static float LINEAR_SPEED = 70; // mm/sec

	 // Constructor: Initialises motor controllers
    public MotorController(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
    }

    // Moves forward for specified number of seconds
    public void forward(int seconds) {
        setMoveSpeed();
        leftMotor.forward();
        rightMotor.forward();
        if (seconds > 0) {
            Delay.msDelay(seconds * 1000);
            stop();
        }
    }
    
    // Moves forward indefinitely
    public void forward() {
        setMoveSpeed();
        leftMotor.forward();
        rightMotor.forward();
    }

    // Stops both motors
    public void stop() {
        leftMotor.stop(true);
        rightMotor.stop();
    }

    // Rotates robot by a given angle in degrees
    public void turn(int degrees) {
        float wheelRotation = (degrees * AXLE_LENGTH) / WHEEL_DIAMETER; // Compute wheel rotation
        leftMotor.setSpeed(ANGULAR_SPEED);
        rightMotor.setSpeed(ANGULAR_SPEED);
        leftMotor.rotate((int) wheelRotation, true);
        rightMotor.rotate(-(int) wheelRotation);
        setMoveSpeed(); // Restore default movement speed
    }

    // Sets speed of both motors based on linear speed
    private void setMoveSpeed() {
        float motorSpeed = (LINEAR_SPEED * 360) / (float) (Math.PI * WHEEL_DIAMETER);
        leftMotor.setSpeed(motorSpeed);
        rightMotor.setSpeed(motorSpeed);
    }

    // Parses and executes movement instructions
    public void followInstruction(String instruction) {
        String cmd = instruction.toUpperCase().trim();

        // Handle Forward commands
        if (cmd.contains("F")) {
            if (cmd.contains("FW")) { // Legacy "FW" command
                forward(10);
            } else {
                try {
                    String numStr = cmd.substring(cmd.indexOf("F") + 1).replaceAll("[^0-9]", "");
                    int seconds = numStr.isEmpty() ? 1 : Integer.parseInt(numStr);
                    forward(seconds);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid duration after F");
                }
            }
        }

        // Handle Turn commands with degrees
        if (cmd.contains("T")) {
            try {
                if (cmd.contains("TL")) {
                    String numStr = cmd.substring(cmd.indexOf("TL") + 2).replaceAll("[^0-9]", "");
                    int degrees = numStr.isEmpty() ? 90 : Integer.parseInt(numStr);
                    turn(-degrees); // Negative for left turn
                } else if (cmd.contains("TR")) {
                    String numStr = cmd.substring(cmd.indexOf("TR") + 2).replaceAll("[^0-9]", "");
                    int degrees = numStr.isEmpty() ? 90 : Integer.parseInt(numStr);
                    turn(degrees); // Positive for right turn
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid degrees in turn command");
            }
        }

        // Handle Forward (no seconds specified)
        if (cmd.contains("FW")) {
            forward(10);
        }
        
        // Handle Turn Left (default 90 degrees)
        if (cmd.contains("TL")) {
            turn(-90);
        }
        
        // Handle Turn Right (default 90 degrees)
        if (cmd.contains("TR")) {
            turn(90);
        }
        
        // Handle Stop command
        if (cmd.contains("S")) {
            stop();
        }
    }

    // Closes motors when no longer needed
    public void close() {
        leftMotor.close();
        rightMotor.close();
    }
	
	public boolean isMoving() {
		return leftMotor.isMoving() || rightMotor.isMoving();
	}

    // Returns left motor instance
    public EV3LargeRegulatedMotor getLeftMotor() {
        return leftMotor;
    }

    // Returns right motor instance
    public EV3LargeRegulatedMotor getRightMotor() {
        return rightMotor;
    }


}