import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

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
	        LCD.clear();
	        
	        // Display each line centered
	        for (int i = 0; i < AUTHORS.length; i++) {
	            LCD.drawString(AUTHORS[i], 0, i);
	        }
	        
	        // Wait for any button press
	        LCD.drawString("Press any button", 0, 6);
	        Button.waitForAnyPress();
	        LCD.clear();
	    }
}
