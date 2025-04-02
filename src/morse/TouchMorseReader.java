package morse;

import hardware.MotorController;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import lejos.hardware.Button;

public class TouchMorseReader extends Thread implements MorseReader {
    // Timing thresholds for different Morse code elements
    private static final long DOT_THRESHOLD = 500;
    private static final long DASH_THRESHOLD = 1000;
    private static final long LETTER_TIMEOUT = 2000;
    
    private StringBuilder currentMorse = new StringBuilder();  // Buffer for current Morse code input
    private StringBuilder morseDisplay = new StringBuilder(); // Display buffer for Morse input
    private StringBuilder decodedWord = new StringBuilder(); // Buffer for decoded word
    private MotorController motorController;
    private EV3TouchSensor touchSensor;
    private long lastPressTime;
    private boolean running = true;
    private boolean inputComplete = false;

    // Constructor to initialise motor controller, touch sensor
    public TouchMorseReader(MotorController motorController, EV3TouchSensor touchSensor) {
        this.motorController = motorController;
        this.touchSensor = touchSensor;
    }

    @Override
    public void run() {
        initializeDisplay();  // Initialise display with instructions
        
        // Phase 1: Capture Morse Code Input
        while (running && !inputComplete) {
            processInput();      // Capture input from touch sensor
            checkTimeouts();     // Check if input timeout occurred
            updateDisplay();     // Update display with current values
            checkEnterButton();  // Check if user presses ENTER to complete input
            Delay.msDelay(50);   // Short delay to avoid high polling rate
        }
        
        // Phase 2: Execute motor commands after input completion
        if (inputComplete) {
            executeMotorCommands();  // Perform motor instructions based on the decoded word
        }
    }

    // Initialises LCD display with instructions for user input
    private void initializeDisplay() {
       // LCD.clear();  // Clear the LCD screen
        LCD.drawString("Enter Morse Code:", 0, 0);  // Display instructions
        LCD.drawString("Short=Dot (.)", 0, 1);
        LCD.drawString("Long=Dash (-)", 0, 2);
        LCD.drawString("ENTER when done", 0, 3);

        // Clear other lines to prevent leftover text
        LCD.drawString("                    ", 0, 5);
        LCD.drawString("                    ", 0, 6);
        LCD.drawString("                    ", 0, 7);
    }

    // Process touch sensor input and determine if it's a dot or dash
    private void processInput() {
        float[] sample = new float[touchSensor.sampleSize()];
        touchSensor.fetchSample(sample, 0);  // Fetch sample from touch sensor
        
        if (sample[0] == 1) {  // Check if touch sensor is pressed
            long start = System.currentTimeMillis();  // Record start time of the press
            
            // Wait until button is released
            while (sample[0] == 1) {
                touchSensor.fetchSample(sample, 0);
            }
            
            long duration = System.currentTimeMillis() - start;  // Calculate press duration
            lastPressTime = System.currentTimeMillis();  // Update last press time
            
            // Classify duration of press as a dot or dash
            if (duration < DOT_THRESHOLD) {
                currentMorse.append(".");  // Dot
                morseDisplay.append(".");
            } else if (duration < DASH_THRESHOLD) {
                currentMorse.append("-");  // Dash
                morseDisplay.append("-");
            }
        }
    }

    // Check for a timeout between inputs, and decode current letter if needed
    private void checkTimeouts() {
        long inactiveTime = System.currentTimeMillis() - lastPressTime;
        
        if (inactiveTime > LETTER_TIMEOUT && currentMorse.length() > 0) {
            decodeCurrentLetter();  // Decode the current letter if there is a timeout
        }
    }

    // Check if user pressed the ENTER button to complete input
    private void checkEnterButton() {
        if (Button.ENTER.isDown()) {
            inputComplete = true;  // Mark input as complete
            // Finish current letter if any
            if (currentMorse.length() > 0) {
                decodeCurrentLetter();  // Decode last letter
            }
        }
    }

    // Decode current Morse code input and append result to decoded word
    private void decodeCurrentLetter() {
        String morse = currentMorse.toString().trim();  // Get trimmed Morse code
        if (morse.isEmpty()) return;  // Do nothing if input empty

        LCD.drawString("                    ", 0, 5);  // Clear decoding line

        // Decode letter from Morse code
        Character letter = MorseReaderBase.decodeSingleMorseLetter(morse);

        LCD.drawString("                    ", 0, 6);  // Clear decoded letter line

        currentMorse.setLength(0);  // Clear current Morse code buffer
        morseDisplay.setLength(0);  // Clear display buffer

        if (letter != null && letter != '?') {
            decodedWord.append(letter);  // Append decoded letter to word
        } else {
            showFeedback(morse);  // Show feedback for invalid input
        }
    }

    // Display feedback for invalid Morse code input
    private void showFeedback(String morse) {
        LCD.drawString("Invalid: " + morse, 0, 5);  // Show invalid input message
        Delay.msDelay(800);  // Display for 800ms

        // Clear input to allow for new entry
        morseDisplay.setLength(0);
        currentMorse.setLength(0);
        LCD.drawString("                    ", 0, 5);  // Clear invalid message
        updateDisplay();  // Refresh display
    }

    // Execute motor commands based on the decoded word
    private void executeMotorCommands() {
        LCD.clear();  // Clear screen
        // Convert decoded word to motor instructions and execute
        String commands = decodedWord.toString();
        motorController.followInstruction(commands);
        
        // Signal completion by stopping reading process
        stopReading();
    }

    // Update the LCD display with current input and output
    private void updateDisplay() {
        // Clear the display lines
        LCD.drawString("                    ", 0, 5);
        LCD.drawString("                    ", 0, 6);
        LCD.drawString("                    ", 0, 7);

        // Redraw current Morse input and decoded word
        LCD.drawString("Input: " + morseDisplay.toString(), 0, 6);
        LCD.drawString("Output: " + decodedWord.toString(), 0, 7);
    }

    // Get current Morse code being input
    @Override
    public StringBuilder getMorse() {
        return currentMorse;
    }

    // Get decoded word
    @Override
    public StringBuilder getMorseWord() {
        return decodedWord;
    }
    
    // Check if input is complete
    public boolean isInputComplete() {
        return inputComplete;
    }
    
    // Stop reading process
    public void stopReading() {
        running = false;
    }
}