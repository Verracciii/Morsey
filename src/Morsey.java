<<<<<<< Updated upstream
=======
// Packages
import morse.ColorMorseReader;
import morse.TouchMorseReader;
import behaviors.*;
import hardware.MotorController;

// Library imports
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.subsumption.Arbitrator;

>>>>>>> Stashed changes

public class Morsey {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

<<<<<<< Updated upstream
=======
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
	        
	        // Create an instance of MotorController
	        MotorController motorController = new MotorController();

	        // Create instances of the behaviors
	        Behavior touchInterrupt = new TouchInterrupt(motorController);
	        //Behavior obstacleAvoider = new ObstacleAvoider(motorController);
	        //Behavior lineFollower = new LineFollower(motorController);

	        // Create an array of behaviors for the arbitrator
	        Behavior[] behaviors = { touchInterrupt };

	        // Create the arbitrator and start it
	        Arbitrator arbitrator = new Arbitrator(behaviors);
	        arbitrator.go();
	    }
>>>>>>> Stashed changes
}
