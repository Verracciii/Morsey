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
    
    // Constructor: Initialises the motor controller and sensors
    public ExitHandler(MotorController motorController, EV3ColorSensor colorSensor, 
                       EV3TouchSensor touchSensor, EV3UltrasonicSensor ultrasonicSensor) {
        this.motorController = motorController;
        this.colorSensor = colorSensor;
        this.touchSensor = touchSensor;
        this.ultrasonicSensor = ultrasonicSensor;
    }

    // Determines if behaviour should take control (when ESCAPE button is pressed)
    @Override
    public boolean takeControl() {
        return Button.ESCAPE.isDown(); // Returns true if ESCAPE button is pressed
    }

    // Executes exit procedure when ESCAPE is pressed
    @Override
    public void action() {
        suppressed = false; // Reset suppression flag
        exitProgram();
    }

    // Allows suppression of behaviour
    @Override
    public void suppress() {
        suppressed = true;
    }
    
    // Closes all sensors and safely exits the program
    public void exitProgram() {
        if (colorSensor != null) colorSensor.close();
        if (touchSensor != null) touchSensor.close();
        if (ultrasonicSensor != null) ultrasonicSensor.close();
        if (motorController != null) motorController.close();
        System.exit(0); // Exit the program
    }
}
