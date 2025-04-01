package morse;

import behaviors.ExitBehavior; // Import ExitBehavior
import hardware.MotorController;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.subsumption.Arbitrator;
import behaviors.ProgramState;

public class TouchMorseReader implements Behavior, MorseReader { // Implements both Behavior and MorseReader
    private static final long DOT_THRESHOLD = 500;
    private static final long DASH_THRESHOLD = 1000;
    private static final long NEW_LETTER_DURATION = 2000;
    private static final long NEW_WORD_DURATION = 3000;
    private long lastPressTime = System.currentTimeMillis();
    private StringBuilder morseSequence = new StringBuilder();
    private StringBuilder decodedWord = new StringBuilder();
    private MotorController motorController;
    private EV3TouchSensor touchSensor;
    private ExitBehavior exitBehavior; // ExitBehavior instance
    private boolean suppressed = false;

    public TouchMorseReader(MotorController motorController, EV3TouchSensor touchSensor) {
        this.motorController = motorController;
        this.touchSensor = touchSensor;
        this.exitBehavior = new ExitBehavior(touchSensor, motorController); // Initialize ExitBehavior
    }

    public void start() {
        ProgramState.setState("TouchMorseReader");

        LCD.clear();
        LCD.drawString("Touch Mode", 0, 0);
        LCD.drawString("Enter Morse Code: ", 0, 1);
        LCD.drawString("Short = Dot", 0, 2);
        LCD.drawString("Long = Dash", 0, 3);
        LCD.drawString("Wait = Space", 0, 4);

        Delay.msDelay(5000);

        // Create the Arbitrator with both behaviors
        Behavior[] behaviors = { exitBehavior, this };  // Add this class as a Behavior
        Arbitrator arbitrator = new Arbitrator(behaviors);
        arbitrator.go(); // Start the behavior loop
    }

    @Override
    public boolean takeControl() {
        return true; // Always take control, since this behavior reads input continuously
    }

    @Override
    public void action() {
        suppressed = false;

        // Morse reading logic
        while (!suppressed) {
            readMorseInput();
            checkForNewLetterOrWord();
            updateDisplay();
            Delay.msDelay(50); // Prevent CPU overload
        }
    }

    @Override
    public void suppress() {
        suppressed = true; // Handle suppression of the behavior
    }

    private void readMorseInput() {
        float[] sample = new float[touchSensor.sampleSize()];
        touchSensor.fetchSample(sample, 0);

        if (sample[0] == 1) { // Sensor is pressed
            long pressStartTime = System.currentTimeMillis();

            while (sample[0] == 1) {
                touchSensor.fetchSample(sample, 0);
            }

            long pressDuration = System.currentTimeMillis() - pressStartTime;

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

        if (timeSinceLastPress > NEW_WORD_DURATION && morseSequence.length() > 0) {
            decodeMorseSequence();
            decodedWord.append(" ");
            morseSequence.setLength(0); // Clear sequence
        } else if (timeSinceLastPress > NEW_LETTER_DURATION && morseSequence.length() > 0) {
            decodeMorseSequence();
            morseSequence.setLength(0);
        }
    }

    private void decodeMorseSequence() {
        String morseString = morseSequence.toString();
        Character decodedChar = MorseReaderBase.decodeMorse(morseString);

        if (decodedChar != null && decodedChar != '?') {
            decodedWord.append(decodedChar);
        } else {
            LCD.drawString("Invalid code: " + morseString, 0, 7);
        }

        updateDisplay();
    }

    private void updateDisplay() {
        LCD.drawString("Input: " + morseSequence.toString(), 0, 5);
        LCD.drawString("Word: " + decodedWord.toString(), 0, 6);
    }

    // Implementing MorseReader methods
    @Override
    public StringBuilder getMorse() {
        return morseSequence;
    }

    @Override
    public StringBuilder getMorseWord() {
        return decodedWord;
    }
}
