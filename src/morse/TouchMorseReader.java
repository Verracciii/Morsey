package morse;

import hardware.MotorController;
import behaviors.ExitHandler;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import lejos.hardware.Button;

public class TouchMorseReader extends Thread implements MorseReader {
    // Timing thresholds
    private static final long DOT_THRESHOLD = 500;
    private static final long DASH_THRESHOLD = 1000;
    private static final long LETTER_TIMEOUT = 2000;
    
    private StringBuilder currentMorse = new StringBuilder();
    private StringBuilder morseDisplay = new StringBuilder();
    private StringBuilder decodedWord = new StringBuilder();
    private MotorController motorController;
    private EV3TouchSensor touchSensor;
    private long lastPressTime;
    private boolean running = true;
    private boolean inputComplete = false;
 

    public TouchMorseReader(MotorController motorController, EV3TouchSensor touchSensor) {
        this.motorController = motorController;
        this.touchSensor = touchSensor;
    }

    @Override
    public void run() {
        initializeDisplay();
        
        // Phase 1: Morse Code Input
        while (running && !inputComplete) {
            processInput();
            checkTimeouts();
            updateDisplay();
            checkEnterButton();
            Delay.msDelay(50);
        }
        
        // Phase 2: Execute Commands
        if (inputComplete) {
            executeMotorCommands();
        }
    }

    private void initializeDisplay() {
        LCD.clear();
        LCD.drawString("Enter Morse Code:", 0, 0);
        LCD.drawString("Short=Dot (.)", 0, 1);
        LCD.drawString("Long=Dash (-)", 0, 2);
        LCD.drawString("Hold=Space", 0, 3);
        LCD.drawString("ENTER when done", 0, 4);

        // Clear lines 5, 6, and 7 to prevent leftover text
        LCD.drawString("                    ", 0, 5);
        LCD.drawString("                    ", 0, 6);
        LCD.drawString("                    ", 0, 7);
    }

    private void processInput() {
        float[] sample = new float[touchSensor.sampleSize()];
        touchSensor.fetchSample(sample, 0);
        
        if (sample[0] == 1) {
            long start = System.currentTimeMillis();
            
            while (sample[0] == 1) {
                touchSensor.fetchSample(sample, 0);
            }
            
            long duration = System.currentTimeMillis() - start;
            lastPressTime = System.currentTimeMillis();
            
            if (duration < DOT_THRESHOLD) {
                currentMorse.append(".");
                morseDisplay.append(".");
            } else if (duration < DASH_THRESHOLD) {
                currentMorse.append("-");
                morseDisplay.append("-");
            } else {
                currentMorse.append(" ");
                morseDisplay.append(" ");
            }
        }
    }

    private void checkTimeouts() {
        long inactiveTime = System.currentTimeMillis() - lastPressTime;
        
        if (inactiveTime > LETTER_TIMEOUT && currentMorse.length() > 0) {
            decodeCurrentLetter();
        }
    }

    private void checkEnterButton() {
        if (Button.ENTER.isDown()) {
            inputComplete = true;
            // Finish current letter if any
            if (currentMorse.length() > 0) {
                decodeCurrentLetter();
            }
        }
    }

    private void decodeCurrentLetter() {
        String morse = currentMorse.toString().trim();
        if (morse.isEmpty()) return;

        LCD.drawString("                    ", 0, 5); // Clear decoding line
        //LCD.drawString("Decoding: " + morse, 0, 5);

        Character letter = MorseReaderBase.decodeSingleMorseLetter(morse);

        LCD.drawString("                    ", 0, 6); // Clear decoded letter line
        //LCD.drawString("Decoded as: " + letter, 0, 6);

        currentMorse.setLength(0); // Clear buffer
        morseDisplay.setLength(0); // Clear buffer

        if (letter != null && letter != '?') {
            decodedWord.append(letter);
        } else {
            showFeedback(morse); // Show "Invalid" message
        }

    }

    private void showFeedback(String morse) {
        // Show "Invalid" on LCD
        LCD.drawString("Invalid: " + morse, 0, 5);
        Delay.msDelay(800); // Display for 800ms

        // Clear input to allow new entry
        morseDisplay.setLength(0);
        currentMorse.setLength(0);
        LCD.drawString("                    ", 0, 5); // Clear invalid message
        updateDisplay(); // Refresh the display
    }


    private void executeMotorCommands() {
        LCD.clear();
        //LCD.drawString("Executing:", 0, 0);
        //LCD.drawString(decodedWord.toString(), 0, 1);
        
        // Convert decoded word to motor instructions
        String commands = decodedWord.toString();
        motorController.followInstruction(commands);
        
        // Signal completion back to main class
        stopReading();
    }

    private void updateDisplay() {
        // Clear lines 5, 6, and 7 properly by overwriting with spaces
        LCD.drawString("                    ", 0, 5);
        LCD.drawString("                    ", 0, 6);
        LCD.drawString("                    ", 0, 7);

        // Redraw new values
        LCD.drawString("Input: " + morseDisplay.toString(), 0, 6);
        LCD.drawString("Output: " + decodedWord.toString(), 0, 7);
    }

    @Override
    public StringBuilder getMorse() {
        return currentMorse;
    }

    @Override
    public StringBuilder getMorseWord() {
        return decodedWord;
    }
    
    public boolean isInputComplete() {
        return inputComplete;
    }
    
    public void stopReading() {
        running = false;
    }
}