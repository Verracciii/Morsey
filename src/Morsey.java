// Packages
import morse.ColorMorseReader;
import morse.TouchMorseReader;

// Library imports
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;


public class Morsey {

	 public static void main(String[] args) {
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
	            // Call the ColorMorseReader class or start its thread
	            ColorMorseReader colorReader = new ColorMorseReader();
	            colorReader.start();
	        } else if (buttonId == Button.ID_RIGHT) {
	            // Start Touch Reader Mode
	            LCD.clear();
	            LCD.drawString("Touch Reader", 0, 0);
	            // Call the TouchMorseReader class or start its thread
	            TouchMorseReader touchReader = new TouchMorseReader();
	            touchReader.start();
	        }
	    }
}
