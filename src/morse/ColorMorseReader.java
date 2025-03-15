package morse;

import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;

public class ColorMorseReader extends Thread implements MorseReader {

    float DOT_DUR;
    float DASH_DUR;
    float SPACE_DUR;
    private EV3ColorSensor COLOR_SEN;
    private float[] CURR_SAMPLE;
    public StringBuilder WORD;
    public StringBuilder MORSE;
    private boolean running;
    private float BLACK_MIN;
    private float BLACK_MAX;

    @Override
    public void run() {
        // Implement the logic for color reader mode
        // This method will be called when the thread starts
        LCD.clear();
        LCD.drawString("Color Mode", 0, 0);
        // Add your logic here to read Morse code using the color sensor
    }

    @Override
    public StringBuilder getMorse() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StringBuilder getMorseWord() {
        // TODO Auto-generated method stub
        return null;
    }
}