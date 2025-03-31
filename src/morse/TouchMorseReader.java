package morse;

import hardware.MotorController;
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
                currentMorse.append("0");
                morseDisplay.append(".");
                System.out.println(".");
            } else if (duration < DASH_THRESHOLD) {
                currentMorse.append("1");
                morseDisplay.append("-");
                System.out.println("-");

            } else {
                currentMorse.append(" ");
                morseDisplay.append(" ");
                System.out.println(" ");

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
        	System.out.println("Enter pressed");
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
        
        Character letter = MorseReaderBase.decodeMorse(morse);
        if (letter != null && letter != '?') {
            decodedWord.append(letter);
            showFeedback(morseDisplay + " = " + letter);
        } else {
            showFeedback("Invalid: " + morseDisplay);
        }
        
        currentMorse.setLength(0);
        morseDisplay.setLength(0);
    }

    private void executeMotorCommands() {
        LCD.clear();
        LCD.drawString("Executing:", 0, 0);
        LCD.drawString(decodedWord.toString(), 0, 1);
        
        // Convert decoded word to motor instructions
        String commands = decodedWord.toString();
        motorController.followInstruction(commands);
        
        // Signal completion back to main class
        stopReading();
    }

    private void showFeedback(String message) {
        LCD.clear(0, 5, 20);
        System.out.println(message);
        LCD.drawString(message, 0, 5);
        Delay.msDelay(800);
        LCD.clear(0, 5, 20);
    }

    private void updateDisplay() {
        LCD.clear(0, 6, 20);
        LCD.clear(0, 7, 20);
        System.out.println(morseDisplay);
        System.out.println(decodedWord);
        LCD.drawString("Input: " + morseDisplay, 0, 6);
        LCD.drawString("Output: " + decodedWord, 0, 7);
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