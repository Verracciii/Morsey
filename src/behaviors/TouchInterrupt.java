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

    // Enables behaviour and prompts the user to press ENTER
    public void enable() {
        this.enabled = true;
        LCD.clear();
        LCD.drawString("Press ENTER to", 0, 0);
    }

    // Determines if behaviour should take control (when ENTER is pressed)
    @Override
    public boolean takeControl() {
        if (!enabled) return false; // Only activate if enabled
        
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
