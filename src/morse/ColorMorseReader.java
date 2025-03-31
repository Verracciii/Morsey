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
	 private static final float DOT_MAX = 700;
     private static final float DASH_MIN = 1400;
     private static final float WORD_SPACE = 11000;
     private static final float TERMINATION_THRESHOLD = 26000;
     private static final float BLACK_THRESH = 0.3f;
     private static final float LETTER_SPACE = 4500;

    private StringBuilder MORSE = new StringBuilder();
    private StringBuilder WORD = new StringBuilder();
    private MotorController motorController;
    private final ConfiguredTimer blackTimer;
    private final SensorMode colorMode;
    private long lastBlackTime;
    private boolean running;
    private final EV3ColorSensor colorSensor;
    private StringBuilder MorseSequence = new StringBuilder(); // Used to store the full Morse sequence

    public ColorMorseReader(MotorController motorController) {
        this.motorController = motorController;
        colorSensor = new EV3ColorSensor(SensorPort.S2);
        colorMode = colorSensor.getRedMode();
        this.blackTimer = new ConfiguredTimer();
        this.running = true;
        this.lastBlackTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        motorController.forward();

        try {
            while (running && motorController.isMoving()) {
                float intensity = getLightIntensity();
                if (intensity < BLACK_THRESH) {
                    handleBlack();
                } else {
                    handleWhite();
                }

                checkForTermination();
                Thread.yield(); // Be CPU-friendly
            }
        } finally {
            motorController.stop();
            colorSensor.close();
        }
    }

    private float getLightIntensity() {
        float[] sample = new float[colorMode.sampleSize()];
        colorMode.fetchSample(sample, 0);
        return sample[0];
    }

    private void handleBlack() {
        if (!blackTimer.isMoving()) {
            blackTimer.start();
        }
        lastBlackTime = System.currentTimeMillis();
    }

    private void handleWhite() {
        if (blackTimer.isMoving()) {
            processSignal();
        }
        checkForSpace();
    }

    private void processSignal() {
        blackTimer.stop();
        long duration = blackTimer.getElapsedTime();

        if (duration <= DOT_MAX) {
            MORSE.append(".");
        } else if (duration > DASH_MIN) {
            MORSE.append("-");
        }
        blackTimer.reset();
    }

    private void checkForSpace() {
        long whiteDuration = System.currentTimeMillis() - lastBlackTime;

        if (whiteDuration > LETTER_SPACE && whiteDuration <= WORD_SPACE && MORSE.length() > 0) {
            String letterMorse = MORSE.toString();
            Character morseLetter = decodeSingleMorseLetter(letterMorse);
            WORD.append(morseLetter);
            MORSE.setLength(0);
        } else if (whiteDuration > WORD_SPACE && whiteDuration < TERMINATION_THRESHOLD && MORSE.length() > 0) {
            decodeSingleMorseLetter(MORSE.toString());
            WORD.append(" ");
            MORSE.setLength(0);
        }
    }

    private Character decodeSingleMorseLetter(String morseLetter) {
        Character decodedChar = MorseReaderBase.decodeSingleMorseLetter(morseLetter);
        return (decodedChar != null) ? decodedChar : '?';
    }

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

    public StringBuilder getMorseSequence() {
        return MorseSequence;
    }
}
