package morse;

import hardware.MotorController;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class TouchMorseReader extends Thread implements MorseReader {

    float DOT_DUR;
    float DASH_DUR;
    float SPACE_DUR;
    private float[] CURR_SAMPLE;
    public StringBuilder WORD;
    public StringBuilder MORSE;
    private boolean running;
    private MotorController motorController;
   // private TouchController touchController;

  /*  public TouchMorseReader(MotorController motorController, TouchController touchController) {
        this.motorController = motorController; // Initialize the MotorController
        this.touchController = touchController; // Initialize the touch sensor
    }*/
    
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