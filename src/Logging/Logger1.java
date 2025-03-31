package Logging;
import hardware.MotorController;
import hardware.TouchController;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import morse.ColorMorseReader;
import morse.MorseReaderBase;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
public class Logger1 extends Thread{
	 private TextLCD lcd;
	    private MotorController motorController;
	    private TouchController touchController;
	    private ColorMorseReader colorMorseReader;
	    private StringBuilder MorseSequence;
	    private String morseString = "";
	    private String decodedWord = "";
	    private  StringBuilder wordSequence;

	    // Constructor: Initialises the LCD, motor, and sensor

		public Logger1(TextLCD lcd, MotorController motorController, TouchController touchController, ColorMorseReader colorMorseReader) {
			this.lcd = lcd;
	        this.motorController = motorController;
	        this.touchController = touchController;
	        this.colorMorseReader = colorMorseReader;
	        this.wordSequence = new StringBuilder(); 
		}
		
		// Main thread loop: Continuously updates and displays data
	    public void run() {
	        while (true)
	        {
	        	updateMorseData();
	            display();
	            try {
	                Thread.sleep(200); // Updates 10 times per second
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	            }

	        }
	    }
	    // Displays motor speed, tacho count, sensor value, Morse string, and decoded word on the LCD
	    private void display() {
	    	
	        try {
	            lcd.clear();

	            // Display motor speed, tacho count, and touch sensor value
	            lcd.drawString("Motor Speed: " + motorController.getLeftMotor().getSpeed(), 0, 1);
	            lcd.drawString("Motor Tacho: " + motorController.getLeftMotor().getTachoCount(), 0, 2);

	            // Fetch and display touch sensor value
	            float[] sample = new float[touchController.getTouchSensor().sampleSize()];
	            touchController.getTouchSensor().fetchSample(sample, 0);
	            lcd.drawString("Sensor Val: " + sample[0], 0, 3);

	            // Display the current Morse code sequence
	            lcd.drawString("Morse: " + morseString, 0, 4);
	            
	            // Optionally, you can display the decoded word
	            lcd.drawString("Word: " + wordSequence, 0, 5);

	            lcd.refresh();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    private void updateMorseData() {
	        // Get the raw Morse code sequence (no decoding yet)
	        StringBuilder morseSequence = colorMorseReader.getMorse();
	        StringBuilder morseSequenceCollector = new StringBuilder();
	        StringBuilder wordSequence = colorMorseReader.getMorseWord();
	        if (morseSequence != null) {
	            // Update the Morse string for display
	            morseString = morseSequence.toString();
	        }

	    }


	    
}
