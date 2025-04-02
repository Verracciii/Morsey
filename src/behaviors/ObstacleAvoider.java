package behaviors;

import lejos.robotics.subsumption.Behavior;
import hardware.MotorController;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class ObstacleAvoider implements Behavior {
    private MotorController motorController;
    private EV3UltrasonicSensor ultrasonicSensor;
    private boolean suppressed = false;
    
    private static final float OBSTACLE_DISTANCE = 0.20f; // Distance threshold to detect obstacles (meters)

    // Constructor: Initialises motor controller and ultrasonic sensor
    public ObstacleAvoider(MotorController motorController, EV3UltrasonicSensor ultrasonicSensor) {
        this.motorController = motorController;
        this.ultrasonicSensor = ultrasonicSensor;
    }
    
    // Determines if behaviour should take control (if obstacle detected within the threshold distance)
    @Override
    public boolean takeControl() {
        float[] sample = new float[1]; // Array to store sensor reading
        ultrasonicSensor.getDistanceMode().fetchSample(sample, 0); // Get distance
        return sample[0] < OBSTACLE_DISTANCE; // Return true if obstacle too close
    }

    // Executes obstacle avoidance behavior
    @Override
    public void action() {
        motorController.stop(); // Stop the motors when an obstacle detected
    }

    // Allows suppression of behavior
    @Override
    public void suppress() {
    	suppressed = false;
    }
}
