package main;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class WelcomeScreen {
	private static final String[] AUTHORS = {
	        "MorseBot v1.0",
	        "Developed by:",
	        "Valters",
	        "Gagan", 
	        "Andre",
	        "Faheem"
	    };
	    
	 public static void show() {
	        LCD.clear();  // Clear the LCD screen

	        // Display the title and developer names
	        for (int i = 0; i < AUTHORS.length; i++) {
	            LCD.drawString(AUTHORS[i], 0, i);
	        }

	        // Show countdown from 5 to 1
	        for (int i = 5; i > 0; i--) {
	            LCD.drawString("Starting in " + i + "s...", 0, 6);  // Update the countdown message
	            Delay.msDelay(1000);  // Wait for 1 second
	        }

	        // Clear the screen after countdown and display starting message
	        LCD.clear();
	        LCD.drawString("Program Starting...", 0, 0);

	        

	        // Clear the screen after the welcome message
	        LCD.clear();
	    }
}
