package hardware;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

/**
 * The MotorController class controls the motors of the Lego EV3 robot.
 * It provides methods for moving forward, stopping, turning, and following instructions.
 */
public class MotorController {

    private RegulatedMotor leftMotor;
    private RegulatedMotor rightMotor;
    private final int WHEEL_SPEED = 300; // Wheel speed in degrees per second

    /**
     * Initialises the MotorController by setting up the left and right motors.
     */
    public MotorController() {
        // Initialise the motors connected to ports A and D
        this.leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
        this.rightMotor = new EV3LargeRegulatedMotor(MotorPort.D);

        // Set the motor speed
        this.leftMotor.setSpeed(WHEEL_SPEED);
        this.rightMotor.setSpeed(WHEEL_SPEED);
    }

    /**
     * Moves the robot forward.
     */
    public void forward() {
        leftMotor.forward();
        rightMotor.forward();
    }

    /**
     * Stops the robot.
     */
    public void stop() {
        leftMotor.stop(true); // Stop the left motor and wait for it to stop
        rightMotor.stop();    // Stop the right motor
    }

    /**
     * Turns the robot by a specified angle.
     * @param angle The angle to turn (positive for right, negative for left).
     */
    public void turn(int angle) {
        // Rotate the left and right motors in opposite directions to turn
        leftMotor.rotate(angle, true);  // Rotate left motor (non-blocking)
        rightMotor.rotate(-angle);     // Rotate right motor (blocking)
    }

    /**
     * Follows a specific instruction (e.g., FW, ST, TL, TR).
     * @param instruction The instruction to follow.
     */
    public void followInstruction(String instruction) {
        switch (instruction.toUpperCase()) {
            case "FW": // Forward
                forward();
                break;
            case "ST": // Stop
                stop();
                break;
            case "TL": // Turn Left
                turn(-90); // Turn left by 90 degrees
                break;
            case "TR": // Turn Right
                turn(90); // Turn right by 90 degrees
                break;
            default:
                System.out.println("Unknown instruction: " + instruction);
                break;
        }
    }

    /**
     * Closes the motors to free up resources.
     */
    public void close() {
        leftMotor.close();
        rightMotor.close();
    }
}