package behaviors;

import hardware.MotorController;
import lejos.hardware.Button;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class ExitHandler implements Behavior {
    private MotorController motorController;
    private EV3ColorSensor colorSensor;
    private EV3TouchSensor touchSensor;
    private EV3UltrasonicSensor ultrasonicSensor;
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
        // Take control when ESCAPE button is pressed
        return Button.ESCAPE.isDown();
    }

    @Override
    public void action() {
        suppressed = false;
        exitProgram();
    }

    @Override
    public void suppress() {
        suppressed = true;
    }
    
    public void exitProgram() {
        if (colorSensor != null) colorSensor.close();
        if (touchSensor != null) touchSensor.close();
        if (ultrasonicSensor != null) ultrasonicSensor.close();
        if (motorController != null) motorController.close();
        System.exit(0);
    }
}