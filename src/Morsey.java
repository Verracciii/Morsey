import morse.ColorMorseReader;
import morse.TouchMorseReader;
import hardware.MotorController;
import hardware.TouchController;
import behaviors.*;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.MotorPort;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.subsumption.Arbitrator;
import Logging.Logger1;

public class Morsey {
    public static void main(String[] args) throws InterruptedException {

        // Initialize hardware
    	 MotorController motorController = new MotorController(MotorPort.A, MotorPort.D);
        TouchController touchController = new TouchController();
         TextLCD lcd = LocalEV3.get().getTextLCD();

        // Create Logger (disabled for now)
         Logger1 logger = null;
        

        // Display menu
         Behavior touchInterrupt = new TouchInterrupt(motorController, touchController);
         Arbitrator arbitrator = new Arbitrator(new Behavior[]{touchInterrupt}, false);
        LCD.clear();
        LCD.drawString("Select Mode:", 0, 0);
        LCD.drawString("LEFT: Color Reader", 0, 2);
        LCD.drawString("RIGHT: Touch Reader", 0, 3);

        // Wait for left or right button press
        while (true) {
            int buttonId = Button.waitForAnyPress();

            // Check for LEFT button press
            if (buttonId == Button.ID_LEFT) {
                // Start color reader
            	ColorMorseReader morsereader = new ColorMorseReader(motorController);
            	logger = new Logger1(lcd, motorController, touchController, morsereader);
                morsereader.start();
               // morsereader.join();
                
               // Ensure ColorMorseReader finishes before continuing
                break; 
            }

            // Check for RIGHT button press
            else if (buttonId == Button.ID_RIGHT) {
                // Start touch reader
                Thread morseReaderThread = new Thread(new TouchMorseReader(motorController, touchController));
                morseReaderThread.start();
                morseReaderThread.join(); // Ensure TouchMorseReader finishes before continuing
                break; // Exit the loop after the right button press
            }
        }

        // Clear LCD before starting arbitrator
        Thread.sleep(500); // Small delay to let previous text be visible
        if (logger != null) {
            logger.start();
        }
        arbitrator.go();
        // Wait for user to exit
       
    }
}
