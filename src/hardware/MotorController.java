package hardware;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class MotorController {

    private EV3LargeRegulatedMotor leftMotor;
    private EV3LargeRegulatedMotor rightMotor;
    private final int WHEEL_SPEED = 300; // Wheel speed in degrees per second

    public MotorController() {
        // Initialise the motors connected to ports A and D
        this.leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
        this.rightMotor = new EV3LargeRegulatedMotor(MotorPort.D);

        // Set the motor speed
        this.leftMotor.setSpeed(WHEEL_SPEED);
        this.rightMotor.setSpeed(WHEEL_SPEED);
    }
    
    public EV3LargeRegulatedMotor getLeftMotor() {
        return leftMotor;
    }

    public EV3LargeRegulatedMotor getRightMotor() {
        return rightMotor;
    }

    public void forward() {
        leftMotor.forward();
        rightMotor.forward();
    }

    public void stop() {
        leftMotor.stop(true); // Stop the left motor and wait for it to stop
        rightMotor.stop();    // Stop the right motor
    }

    public void turn(int angle) {
        // Rotate the left and right motors in opposite directions to turn
        leftMotor.rotate(angle, true);  // Rotate left motor (non-blocking)
        rightMotor.rotate(-angle);     // Rotate right motor (blocking)
    }

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

    public void close() {
        leftMotor.close();
        rightMotor.close();
    }
    
}