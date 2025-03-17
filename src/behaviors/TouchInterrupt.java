package behaviors;

import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.SampleProvider;
import morse.MotorController;

public class TouchInterrupt implements Behavior {

    private EV3TouchSensor touchSensor;
    private MotorController motorController;
    private SampleProvider touchSampleProvider;
    private float[] sample;

    public TouchInterrupt(MotorController motorController) {
        this.touchSensor = new EV3TouchSensor(SensorPort.S1);
        this.touchSampleProvider = touchSensor.getTouchMode();
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
    }

    @Override
    public void suppress() {
        // No suppression needed since this behavior stops the robot
    }
}