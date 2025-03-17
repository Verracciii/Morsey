package morse;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

public class MotorController {

    private RegulatedMotor mL;
    private RegulatedMotor mR;

    public MotorController() {
        // Initialize the motors connected to ports A and D
        this.mL = new EV3LargeRegulatedMotor(MotorPort.A);
        this.mR = new EV3LargeRegulatedMotor(MotorPort.D);

        // Set motor speed (degrees per second)
        this.mL.setSpeed(300); 
        this.mR.setSpeed(300);
    }

    /**
     * Moves the robot forward.
     */
    public void forward() {
        mL.forward();
        mR.forward();
    }

    /**
     * Stops the robot.
     */
    public void stop() {
        mL.stop(true); // Stop the left motor and wait for it to stop
        mR.stop();    // Stop the right motor
    }

    /**
     * Turns the robot by a specified angle.
     * @param angle The angle to turn (positive for right, negative for left).
     */
    public void turn(int angle) {
        // Rotate the left and right motors in opposite directions to turn
        mL.rotate(angle, true);  
        mR.rotate(-angle);
    }

    /**
     * Follows a specific instruction (e.g., forward, stop, turn).
     * @param instruction The instruction to follow.
     */
    public void followInstruction(String instruction) {
        switch (instruction.toLowerCase()) {
            case "forward":
                forward();
                break;
            case "stop":
                stop();
                break;
            case "turn left":
                turn(-90); // Turn left 90 degrees
                break;
            case "turn right":
                turn(90); // Turn right 90 degrees
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
        mL.close();
        mR.close();
    }
}