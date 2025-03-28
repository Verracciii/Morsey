package behaviors;

import lejos.robotics.subsumption.Behavior;
import lejos.robotics.SampleProvider;
import hardware.MotorController;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import lejos.hardware.Button;

public class TouchInterrupt implements Behavior {

    private final MotorController motorController;
    private final EV3TouchSensor touchSensor;
    private boolean enabled = false;
    private boolean enterPressed = false;

    public TouchInterrupt(MotorController motorController, EV3TouchSensor touchSensor) {
        this.motorController = motorController;
        this.touchSensor = touchSensor;
    }

    public void enable() {
        this.enabled = true;
        LCD.clear();
        LCD.drawString("Press ENTER to", 0, 0);
        LCD.drawString("execute commands", 0, 1);
    }

    @Override
    public boolean takeControl() {
        if (!enabled) return false;
        
        // Check for ENTER button press
        enterPressed = Button.ENTER.isDown();
        return enterPressed;
    }

    @Override
    public void action() {
        LCD.clear();
        LCD.drawString("Executing...", 0, 0);
        
        // 1. Close touch sensor
        touchSensor.close();
        
        // 2. Disable this behavior
        this.enabled = false;
    }

    @Override
    public void suppress() {
        // Not needed in this implementation
    }

}