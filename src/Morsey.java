import hardware.MotorController;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.MotorPort;
import morse.ColorMorseReader;

public class Morsey {
    public static void main(String[] args) throws InterruptedException {

        // Initialize hardware
        MotorController motorController = new MotorController(MotorPort.A, MotorPort.D);
        TextLCD lcd = LocalEV3.get().getTextLCD();

        // Declare variables before use
        Logger1 logger = null;
        ColorMorseReader morsereader = null;
      //  TouchMorseReader touchMorseReader = null;

        // Display menu
        LCD.clear();
        LCD.drawString("Select Mode:", 0, 0);
        LCD.drawString("LEFT: Color Reader", 0, 2);
        LCD.drawString("RIGHT: Touch Reader", 0, 3);

        // Wait for left or right button press
        while (true) {
            int buttonId = Button.waitForAnyPress();

            if (buttonId == Button.ID_LEFT) {
                morsereader = new ColorMorseReader(motorController);
                logger = new Logger1(lcd, motorController, morsereader);
                morsereader.start();
                break;
            }

            else if (buttonId == Button.ID_RIGHT) {
             /*   touchMorseReader = new TouchMorseReader(motorController);
                logger = new Logger1(lcd, motorController, touchMorseReader);
                touchMorseReader.start();
                break;*/
            }
        }

        // Start the logger if initialized
        Thread.sleep(500);
        if (logger != null) {
            logger.start();
        }
    }
}

