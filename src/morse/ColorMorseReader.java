package morse;

import hardware.MotorController;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import hardware.ConfiguredTimer;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.port.UARTPort;

public class ColorMorseReader extends Thread implements MorseReader {

    private static final float DOT_MAX = 789;
    private static final float DASH_MIN = 1400;
    private static final float WORD_SPACE = 8500;
    private static final float TERMINATION_THRESHOLD = 25500;
    private static final float BLACK_THRESH = 0.27f;
    private static final float LETTER_SPACE = 5500;

    private StringBuilder MORSE = new StringBuilder();
    private StringBuilder WORD = new StringBuilder();
    private MotorController motorController;
    private final ConfiguredTimer blackTimer;
    private final SensorMode colorMode;
    private long lastBlackTime;
    private boolean running;
    private final EV3ColorSensor colorSensor;
    private StringBuilder MorseSequence = new StringBuilder(); // Used to store the full Morse sequence

    // Constructor to initialize the ColorMorseReader
    public ColorMorseReader(MotorController motorController, EV3ColorSensor colorSensor) {
        this.motorController = motorController;
        this.colorSensor = colorSensor;
        colorMode = colorSensor.getRedMode();
        this.blackTimer = new ConfiguredTimer();
        this.running = true;
        this.lastBlackTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        motorController.forward(0);

        try {
            while (running && motorController.isMoving()) {
                float intensity = getLightIntensity();
                if (intensity < BLACK_THRESH) {
                    handleBlack();
                   
                } else {
                    handleWhite();
                }

                checkForTermination();
                try {
                    Thread.sleep(90); // Sleep time between sensor checks
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Interrupt handling
                }
            }
        } finally {
            motorController.stop();
            colorSensor.close();
        }
    }

    // Function returns the light intensity from the color sensor
    private float getLightIntensity() {
        float[] sample = new float[colorMode.sampleSize()];
        colorMode.fetchSample(sample, 0);
        return sample[0];
    }

    // Starts the blacktimer to help decide if it is a dot or dash
    private void handleBlack() {
        if (!blackTimer.isMoving()) {
            blackTimer.start();
        }
        lastBlackTime = System.currentTimeMillis(); // Update last black time
    }

    // Handles the white signal (space) between dots and dashes
    private void handleWhite() {
        if (blackTimer.isMoving()) {
            processSignal();
        }
        checkForSpace();
    }

    // Processes the signal to determine whether it's a dot or dash
    private void processSignal() {
        blackTimer.stop();
        long duration = blackTimer.getElapsedTime();

        if (duration <= DOT_MAX) {
            MORSE.append(".");
        } else if (duration >= DASH_MIN) {
            MORSE.append("-");
        }
        blackTimer.reset(); // Reset the timer after processing the signal
    }

    // Checks for a space between letters or words
    private void checkForSpace() {
        long whiteDuration = System.currentTimeMillis() - lastBlackTime;

        // Check for letter space
        if (whiteDuration > LETTER_SPACE && whiteDuration <= WORD_SPACE && MORSE.length() > 0) {
            String letterMorse = MORSE.toString();
            Character morseLetter = decodeSingleMorseLetter(letterMorse);
            WORD.append(morseLetter);
            MORSE.setLength(0); // Clear the MORSE after processing the letter
        }
        // Check for word space
        else if (whiteDuration > WORD_SPACE && whiteDuration <= TERMINATION_THRESHOLD && MORSE.length() > 0) {
         
        	  handleWordSpace();// Clear the MORSE after processing the letter
        }
    }
    private void handleWordSpace() {
    	 System.out.println("This was Reached");
        WORD.append(" "); // Add space between words
        MORSE.setLength(0); 
    }
    // Decodes a Morse code character and returns the decoded character
    private Character decodeSingleMorseLetter(String morseLetter) {
        Character decodedChar = MorseReaderBase.decodeSingleMorseLetter(morseLetter);
        return (decodedChar != null) ? decodedChar : '?'; // Return '?' if decoding fails
    }

    // Terminates program if no morse code is detected for a long period
    private void checkForTermination() {
        long whiteDuration = System.currentTimeMillis() - lastBlackTime;
        if (whiteDuration > TERMINATION_THRESHOLD) {
            running = false; // Stop the thread if no Morse code is detected for too long
        }
    }

    @Override
    public StringBuilder getMorse() {
        return MORSE;
    }

    @Override
    public StringBuilder getMorseWord() {
        return WORD;
    }

    public StringBuilder getMorseSequence() {
        return MorseSequence;
    }
}
