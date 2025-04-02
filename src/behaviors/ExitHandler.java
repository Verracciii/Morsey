package behaviors;

import hardware.MotorController;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class ExitHandler implements Behavior {
    private final MotorController motorController;
    private final EV3ColorSensor colorSensor;
    private final EV3TouchSensor touchSensor;
    private final EV3UltrasonicSensor ultrasonicSensor;
    private boolean suppressed = false;
    
    public ExitHandler(MotorController motorController, EV3ColorSensor colorSensor,
                      EV3TouchSensor touchSensor, EV3UltrasonicSensor ultrasonicSensor) {
        this.motorController = motorController;
        this.colorSensor = colorSensor;
        this.touchSensor = touchSensor;
        this.ultrasonicSensor = ultrasonicSensor;
    }

    @Override
    public boolean takeControl() {
        // More reliable button check with debounce
        if (Button.ESCAPE.isDown()) {
            Delay.msDelay(200); // Debounce delay
            return Button.ESCAPE.isDown(); // Confirm button is still pressed
        }
        return false;
    }

    @Override
    public void action() {
        suppressed = false;
        // Provide visual feedback
        LCD.clear();
        LCD.drawString("", 0, 0);
        exitProgram();
    }

    @Override
    public void suppress() {
        suppressed = true;
    }
    
    private void exitProgram() {
        try {
            // Close resources in reverse order of initialization
            if (motorController != null) {
                motorController.stop();
                motorController.close();
            }
            if (ultrasonicSensor != null) ultrasonicSensor.close();
            if (touchSensor != null) touchSensor.close();
            if (colorSensor != null) colorSensor.close();
            
            // Additional cleanup if needed
            Delay.msDelay(500); // Allow time for resources to release
        } catch (Exception e) {
            // Ignore errors during shutdown
        } finally {
            System.exit(0); // Ensure program terminates
        }
    }
}