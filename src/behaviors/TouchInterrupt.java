package behaviors;

import lejos.robotics.subsumption.Behavior;
import lejos.robotics.SampleProvider;
import hardware.MotorController;
import hardware.TouchController;

public class TouchInterrupt implements Behavior {

    private MotorController motorController;
    private TouchController touchController;
    private SampleProvider touchSampleProvider;
    private float[] sample;

    public TouchInterrupt(MotorController motorController, TouchController touchController) {
        this.touchController = touchController;
        this.touchSampleProvider = touchController.getTouchSensor().getTouchMode();
        this.sample = new float[touchSampleProvider.sampleSize()];
        this.motorController = motorController;
    }

    @Override
    public boolean takeControl() {
        touchSampleProvider.fetchSample(sample, 0);
        return sample[0] == 1; // 1 means pressed, 0 means not pressed
    }

    @Override
    public void action() {
        System.out.println("Touch sensor pressed! Stopping robot.");
        motorController.stop(); // Stop the motors
        System.exit(0);
    }

    @Override
    public void suppress() {
        // No suppression needed since this behavior stops the robot
    }
}