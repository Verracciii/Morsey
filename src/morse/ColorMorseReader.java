package morse;
import hardware.MotorController;
import hardware.ColorController;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import hardware.ConfiguredTimer;

public class ColorMorseReader extends Thread implements MorseReader {
    private static final float DOT_MAX = 300;
    private static final float DASH_MIN = 300;
    private static final float WORD_SPACE = 3000;
    private static final float TERMINATION_THRESHOLD = 10000;
    private static final float BLACK_THRESH = 0.3f;
    private StringBuilder WORD;
    private StringBuilder MORSE;
    private final ColorController colorController;
    private final EV3ColorSensor colorSensor;
    private final MotorController motorController;
    private final ConfiguredTimer blackTimer;
    private SensorMode colorMode;
    private long lastBlackTime;
    private boolean running;

    // Constructor for the ColorMorseReader 
    public ColorMorseReader() {
        this.colorController = new ColorController();  
        this.colorSensor = colorController.getColorSensor(); 
        this.colorMode = colorSensor.getRedMode();
        this.motorController = new MotorController();
        this.blackTimer = new ConfiguredTimer();
        this.MORSE = new StringBuilder();
        this.WORD = new StringBuilder();
        this.running = true;
        this.lastBlackTime = System.currentTimeMillis();
    }

    // Runs the program
    @Override
    public void run() {
        motorController.forward();

        try {
            while (running && motorController.isMoving()) {
                float intensity = getLightIntensity();

                if (intensity < BLACK_THRESH) {
                    handleColor();
                } else {
                    handleWhite();
                }

                checkForTermination();
            }
        } finally {
            motorController.stop();
            colorController.close();  
        }
    }

    // Gets the light intensity from the color sensor
    private float getLightIntensity() {
        float[] sample = new float[colorMode.sampleSize()];
        colorMode.fetchSample(sample, 0);
        return sample[0];
    }

    // method called when th black color is detected 
    private void handleColor() {
        if (!blackTimer.isMoving()) {
            blackTimer.start();
        }
        lastBlackTime = System.currentTimeMillis();
    }

    // method called when the white color is detected
    private void handleWhite() {
        if (blackTimer.isMoving()) {
            processBlackDuration();
        }
        handleSpace();
    }

    // Determines whether it was a dot or a dash
    private void processBlackDuration() {
        blackTimer.stop();
        long duration = blackTimer.getElapsedTime();

        if (duration <= DOT_MAX) {
            MORSE.append(".");
        } else if (duration > DASH_MIN) {
            MORSE.append("-");
        }
        blackTimer.reset();
    }

    // Handles spacing logic for words
    private void handleSpace() {
        long whiteDuration = System.currentTimeMillis() - lastBlackTime;

        if (whiteDuration > WORD_SPACE && MORSE.length() > 0) {
            WORD.append(MORSE.toString()).append(" "); // Adds space between Morse sequences
            MORSE.setLength(0);  // Reset for new word
        }
    }

    // Terminates the program if white is detected for more than 10 seconds  ( could be powsiblly edited later)
    private void checkForTermination() {
        long whiteDuration = System.currentTimeMillis() - lastBlackTime;
        if (whiteDuration > TERMINATION_THRESHOLD) {
            running = false;
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
}


