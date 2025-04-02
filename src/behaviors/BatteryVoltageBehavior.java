package behaviors;

import hardware.MotorController;
import lejos.hardware.Battery;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class BatteryVoltageBehavior implements Behavior {
    private final MotorController motorController;
    private final EV3ColorSensor colorSensor;
    private final EV3TouchSensor touchSensor;
    private final EV3UltrasonicSensor ultrasonicSensor;
    private boolean suppressed = false;
    private static final float SHUTDOWN_VOLTAGE = 6.0f; // Minimum voltage before shutdown

    // Constructor: Initialises the motor controller and sensors
    public BatteryVoltageBehavior(MotorController motorController, EV3ColorSensor colorSensor,
                                  EV3TouchSensor touchSensor, EV3UltrasonicSensor ultrasonicSensor) {
        this.motorController = motorController;
        this.colorSensor = colorSensor;
        this.touchSensor = touchSensor;
        this.ultrasonicSensor = ultrasonicSensor;
    }

    // Determines if behaviour should take control (battery voltage too low)
    @Override
    public boolean takeControl() {
        return Battery.getVoltage() < SHUTDOWN_VOLTAGE;
    }

    // Executes shutdown procedure gracefully
    @Override
    public void action() {
        LCD.clear();
        LCD.drawString("LOW BATTERY", 0, 0);
        LCD.drawString("SHUTTING DOWN", 0, 1);

        if (motorController != null) {
            motorController.stop(); // Stop all motor movement
        }
        
        // Close all sensors safely
        if (colorSensor != null) colorSensor.close();
        if (touchSensor != null) touchSensor.close();
        if (ultrasonicSensor != null) ultrasonicSensor.close();
        if (motorController != null) motorController.close();
        
        try {
            Thread.sleep(3000); // Wait 3 seconds before shutting down
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.exit(0); // Exit program
    }

    // Allows suppression of behaviour
    @Override
    public void suppress() {
        suppressed = true;
    }
}
