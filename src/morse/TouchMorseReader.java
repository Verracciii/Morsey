package morse;

import hardware.MotorController;
import hardware.TouchController;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class TouchMorseReader extends Thread implements MorseReader {

    private static final long DOT_THRESHOLD = 500; // Time in milliseconds for a dot press
    private static final long DASH_THRESHOLD = 1000; // Time in milliseconds for a dash press
    private static final long NEW_LETTER_DURATION = 2000; // Time in milliseconds for a new letter
    private static final long NEW_WORD_DURATION = 3000; // Time in milliseconds for a new word
    
    private StringBuilder morseSequence = new StringBuilder();
    private StringBuilder decodedWord = new StringBuilder();
    private MotorController motorController;
    private TouchController touchController;
    private long lastPressTime = System.currentTimeMillis();
    private boolean running = true;

    public TouchMorseReader(MotorController motorController, TouchController touchController) {
        this.motorController = motorController;
        this.touchController = touchController;
    }
    
    @Override
    public void run() {
        LCD.clear();
        LCD.drawString("Touch Mode", 0, 0);
        LCD.drawString("Enter Morse Code: ", 0, 1);
        LCD.drawString("Short = Dot", 0, 2);
        LCD.drawString("Long = Dash", 0, 3);
        LCD.drawString("Wait = Space", 0, 4);
        
        Delay.msDelay(5000);
        
        while (running) {
            readMorseInput();
            //checkForNewLetterOrWord();
            updateDisplay();
            Delay.msDelay(50); // Small delay to prevent CPU overload
        }
    }

    private void readMorseInput() {
        float[] sample = new float[touchController.getTouchSensor().sampleSize()];
        touchController.getTouchSensor().fetchSample(sample, 0);
        
        if (sample[0] == 1) { // Sensor is pressed
            long pressStartTime = System.currentTimeMillis();
            
            // Wait for release
            while (sample[0] == 1) {
                touchController.getTouchSensor().fetchSample(sample, 0);
            }
            
            long pressDuration = System.currentTimeMillis() - pressStartTime;
            lastPressTime = System.currentTimeMillis();
            
            if (pressDuration < DOT_THRESHOLD) {
                morseSequence.append("0"); // Dot
            } else if (pressDuration < DASH_THRESHOLD) {
                morseSequence.append("1"); // Dash
            } else {
                morseSequence.append(" "); // Long press (space)
            }
        }
    }

    private void checkForNewLetterOrWord() {
    	long currentTime = System.currentTimeMillis();
    	long timeSinceLastPress = currentTime - lastPressTime;
    	if(timeSinceLastPress > NEW_WORD_DURATION && morseSequence.length() > 0) {
    		 decodeMorseSequence();
    	        decodedWord.append(" ");
    	        morseSequence.setLength(0); // Clears sequence
    	}
    	else if(timeSinceLastPress > NEW_LETTER_DURATION && morseSequence.length() > 0) {
    		decodeMorseSequence();
    		morseSequence.setLength(0);
    	}
    	
    }

    private void decodeMorseSequence() {
    	String morseString = morseSequence.toString();
    	Character decodedChar = MorseReaderBase.decodeMorse(morseString);
    	if(decodedChar != null && decodedChar != '?') {
    		decodedWord.append(decodedChar);
    	}
    	else {
    		LCD.drawString("Invalid code: " + morseString, 0, 7);
    	}
    	updateDisplay();
    }

    private void updateDisplay() {
        LCD.drawString("Input: " + morseSequence.toString(), 0, 5);
        LCD.drawString("Word: " + decodedWord.toString(), 0, 6);
    }

    @Override
    public StringBuilder getMorse() {
        return morseSequence;
    }

    @Override
    public StringBuilder getMorseWord() {
        return decodedWord;
    }
    
    public void stopReading() {
        running = false;
    }
}