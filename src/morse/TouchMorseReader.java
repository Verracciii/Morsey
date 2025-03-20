package morse;

import hardware.MotorController;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.utility.Delay;

public class TouchMorseReader extends Thread implements MorseReader {

    float DOT_DUR;
    float DASH_DUR;
    float SPACE_DUR;
    private EV3TouchSensor TOUCH_SEN;
    private float[] CURR_SAMPLE;
    public StringBuilder WORD;
    public StringBuilder MORSE;
    private boolean running;
    private MotorController motorController;

    public TouchMorseReader(MotorController motorController) {
        this.motorController = motorController; // Initialize the MotorController
        //this.TOUCH_SEN = new EV3TouchSensor(SensorPort.S1); // Initialize the touch sensor
    }
    
    @Override
    public void run() {
        // Implement the logic for touch reader mode
        // This method will be called when the thread starts
        LCD.clear();
        LCD.drawString("Touch Mode", 0, 0);
        // Add your logic here to read Morse code using the touch sensor
        Delay.msDelay(1000);
       // motorController.followInstruction("fw");
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