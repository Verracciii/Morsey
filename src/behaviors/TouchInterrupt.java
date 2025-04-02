package behaviors;

import lejos.robotics.subsumption.Behavior;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.lcd.LCD;
import lejos.hardware.Button;

public class TouchInterrupt implements Behavior {

    private final EV3TouchSensor touchSensor;
    private boolean enabled = false;
    private boolean enterPressed = false;

    // Constructor: Initialises motor controller and touch sensor
    public TouchInterrupt(EV3TouchSensor touchSensor) {
        this.touchSensor = touchSensor;
    }

    // Determines if behaviour should take control (when ENTER is pressed)
    @Override
    public boolean takeControl() {
        enterPressed = Button.ENTER.isDown(); // Check if ENTER button is pressed
        return enterPressed;
    }

    // Executes behaviour when ENTER is pressed
    @Override
    public void action() {
        LCD.clear();
        
        // 1. Close the touch sensor
        touchSensor.close();
        
        // 2. Disable this behavior to prevent repeated execution
        this.enabled = false;
    }

    @Override
    public void suppress() {
        // Not needed in this implementation
    }
}
