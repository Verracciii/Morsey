package main;
import Logging.Logger;
import behaviors.*;
import hardware.MotorController;
import morse.*;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import lejos.robotics.subsumption.Arbitrator;

public class Morsey {

    public static void main(String[] args) {
    	WelcomeScreen.show();
        // Initialise motor controller
    	EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
    	EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.D);
        MotorController motorController = new MotorController(leftMotor, rightMotor);

        // Initialise sensor controllers
        EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S2);
        EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
        EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S3);
        TextLCD textLCD = LocalEV3.get().getTextLCD();
        
        
        // Create instances of the behaviors
        Behavior touchInterrupt = new TouchInterrupt(touchSensor);
        Behavior exitHandler = new ExitHandler(motorController, colorSensor, touchSensor, ultrasonicSensor);
        Behavior batteryVol = new  BatteryVoltageBehavior(motorController, colorSensor, touchSensor, ultrasonicSensor);
        Behavior obstacleAvoider = new ObstacleAvoider(motorController, ultrasonicSensor);
        
        // Create an array of behaviors for the arbitrator
        Behavior[] behaviors = { obstacleAvoider, touchInterrupt, batteryVol, exitHandler };

        // Display the main menu
        Delay.msDelay(10);
        LCD.clear();
        LCD.drawString("Select Mode:", 0, 0);
        LCD.drawString("LEFT: Color Reader", 0, 2);
        LCD.drawString("RIGHT: Touch Reader", 0, 3);

        // Wait for user input
        int buttonId;
        do {
            buttonId = Button.waitForAnyPress();
        } while (buttonId != Button.ID_LEFT && buttonId != Button.ID_RIGHT);
        
        LCD.clear();

        // Handle the selected mode
        if (buttonId == Button.ID_LEFT) {
            // Start Color Reader Mode
            LCD.drawString("Color Reader", 0, 0);
            ColorMorseReader colorReader = new ColorMorseReader(motorController, colorSensor);
            Logger logger = new Logger(textLCD, motorController, colorReader);
            colorReader.start();
            logger.start();
           
        } else if (buttonId == Button.ID_RIGHT) {
            // Start Touch Reader Mode
            LCD.drawString("Touch Reader", 0, 0);
            TouchMorseReader touchReader = new TouchMorseReader(motorController, touchSensor);
            touchReader.start();
            
        }
       
        // Create the arbitrator and start it
        Arbitrator arbitrator = new Arbitrator(behaviors);
        arbitrator.go();
    }
}