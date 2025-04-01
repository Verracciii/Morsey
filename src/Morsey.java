import behaviors.*;
import hardware.MotorController;
import morse.*;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.subsumption.Arbitrator;

public class Morsey {

    public static void main(String[] args) {
        // Initialize motor controller
        MotorController motorController = new MotorController();

        // Initialize sensor controllers
        EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S2);
        EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
        EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S3);
        Behavior exitHandler = new ExitHandler(motorController,colorSensor,touchSensor,ultrasonicSensor);
        // Create instances of the behaviors
        Behavior touchInterrupt = new TouchInterrupt(motorController, touchSensor);

        // Create an array of behaviors for the arbitrator
        Behavior[] behaviors = { touchInterrupt, exitHandler };

        // Create the arbitrator and start it
        Arbitrator arbitrator = new Arbitrator(behaviors);

        // Display the main menu
        LCD.clear();
        LCD.drawString("Select Mode:", 0, 0);
        LCD.drawString("LEFT: Color Reader", 0, 2);
        LCD.drawString("RIGHT: Touch Reader", 0, 3);

        // Wait for user input
        int buttonId;
        do {
            buttonId = Button.waitForAnyPress();
        } while (buttonId != Button.ID_LEFT && buttonId != Button.ID_RIGHT);

        // Handle the selected mode
        if (buttonId == Button.ID_LEFT) {
            // Start Color Reader Mode
            LCD.clear();
            LCD.drawString("Color Reader", 0, 0);
            ColorMorseReader colorReader = new ColorMorseReader();
            colorReader.start();
        } else if (buttonId == Button.ID_RIGHT) {
            // Start Touch Reader Mode
            LCD.clear();
            LCD.drawString("Touch Reader", 0, 0);
            TouchMorseReader touchReader = new TouchMorseReader(motorController, touchSensor);
            touchReader.start();
            if (touchReader.isInputComplete()) {
            	arbitrator.go();
            	LCD.clear(0, 0, 18);
            }
        }

    }
}