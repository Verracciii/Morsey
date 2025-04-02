package morse;

import hardware.MotorController;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import hardware.ConfiguredTimer;
import lejos.hardware.port.SensorPort;

public class ColorMorseReader extends Thread implements MorseReader {

    // Thresholds for dot, dash, word space, colours.
    private static final float DOT_MAX = 789;
    private static final float DASH_MIN = 1400;
    private static final float WORD_SPACE = 8500;
    private static final float TERMINATION_THRESHOLD = 25500;
    private static final float BLACK_THRESH = 0.27f;
    private static final float LETTER_SPACE = 5500;

    // Variables for Morse code storage and timing
    private StringBuilder MORSE = new StringBuilder();
    private StringBuilder WORD = new StringBuilder();
    private StringBuilder MorseSequence = new StringBuilder(); // Used to store the full Morse sequence
    private MotorController motorController;
    private EV3ColorSensor colorSensor;
    private final ConfiguredTimer blackTimer;
    private final SensorMode colorMode;
    private long lastBlackTime;
    private boolean running;

    // Constructor to initialise the ColorMorseReader
    public ColorMorseReader(MotorController motorController, EV3ColorSensor colorSensor) {
        this.motorController = motorController;
        this.colorSensor = colorSensor;
        colorMode = colorSensor.getRedMode(); // Use red mode of colour sensor
        this.blackTimer = new ConfiguredTimer();
        this.running = true; // Set running flag to true
        this.lastBlackTime = System.currentTimeMillis(); // Record current time as last black time
    }

    @Override
    public void run() {
        motorController.forward(); // Start robot's forward movement

        try {
            // Continuously read sensor data while robot is moving
            while (running && motorController.isMoving()) {
                float intensity = getLightIntensity(); // Get current light intensity
                if (intensity < BLACK_THRESH) {
                    handleBlack(); // Handle black signal (dot or dash)
                } else {
                    handleWhite(); // Handle white signal (space)
                }

                checkForTermination(); // Check if program should terminate
                try {
                    Thread.sleep(90); // Sleep time between sensor checks
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Handle interrupt
                }
            }
        } finally {
            motorController.stop(); // Stop motor once done
            colorSensor.close(); // Close colour sensor after use
        }
    }

    // Function returns the light intensity from the colour sensor
    private float getLightIntensity() {
        float[] sample = new float[colorMode.sampleSize()];
        colorMode.fetchSample(sample, 0);
        return sample[0];
    }

    // Starts blackTimer to decide if sensor is reading dot or dash
    private void handleBlack() {
        if (!blackTimer.isMoving()) {
            blackTimer.start(); // Start timer when black is detected
        }
        lastBlackTime = System.currentTimeMillis(); // Update time when black is detected
    }

    // Handles white signal (space) between dots and dashes
    private void handleWhite() {
        if (blackTimer.isMoving()) {
            processSignal(); // Process signal when white is detected
        }
        checkForSpace(); // Check for letter or word space
    }

    // Processes signal to determine whether it's a dot or dash
    private void processSignal() {
        blackTimer.stop(); // Stop black timer to measure signal duration
        long duration = blackTimer.getElapsedTime(); // Get duration of black signal

        if (duration <= DOT_MAX) {
            MORSE.append("."); // Append dot if signal is short
        } else if (duration >= DASH_MIN) {
            MORSE.append("-"); // Append dash if signal is long
        }
        blackTimer.reset(); // Reset timer for next signal
    }

    // Checks for space between letters or words
    private void checkForSpace() {
        long whiteDuration = System.currentTimeMillis() - lastBlackTime; // Calculate duration of white space

        // Check for letter space
        if (whiteDuration > LETTER_SPACE && whiteDuration <= WORD_SPACE && MORSE.length() > 0) {
            String letterMorse = MORSE.toString();
            Character morseLetter = decodeSingleMorseLetter(letterMorse); // Decode letter
            WORD.append(morseLetter); // Append decoded letter to WORD
            MORSE.setLength(0); // Clear MORSE after processing letter
        }
        // Check for word space
        else if (whiteDuration > WORD_SPACE && whiteDuration <= TERMINATION_THRESHOLD && MORSE.length() > 0) {
            handleWordSpace(); // Handle word space if detected
        }
    }

    // Handles word space by appending space to the word
    private void handleWordSpace() {
        WORD.append(" "); // Add space between words
        MORSE.setLength(0); // Clear the Morse sequence after word space
    }

    // Decodes Morse code character and returns the decoded character
    private Character decodeSingleMorseLetter(String morseLetter) {
        Character decodedChar = MorseReaderBase.decodeSingleMorseLetter(morseLetter);
        return (decodedChar != null) ? decodedChar : '?'; // Return '?' if decoding fails
    }

    // Terminates program if no Morse code is detected for a long period
    private void checkForTermination() {
        long whiteDuration = System.currentTimeMillis() - lastBlackTime;
        if (whiteDuration > TERMINATION_THRESHOLD) {
            running = false; // Stop thread if no Morse code is detected for too long
        }
    }

    @Override
    public StringBuilder getMorse() {
        return MORSE; // Return current Morse sequence
    }

    @Override
    public StringBuilder getMorseWord() {
        return WORD; // Return current word in Morse
    }

    public StringBuilder getMorseSequence() {
        return MorseSequence; // Return full Morse sequence
    }
}
