package morse;

import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import behaviors.TouchInterrupt;

public class TouchMorseReader extends Thread implements MorseReader {

    float DOT_DUR;
    float DASH_DUR;
    float SPACE_DUR;
    private EV3TouchSensor TOUCH_SEN;
    private float[] CURR_SAMPLE;
    public StringBuilder WORD;
    public StringBuilder MORSE;
    private boolean running;
    private MotorController motorController; // MotorController for motor control
    private Arbitrator arbitrator; // Arbitrator to manage behaviors

    public TouchMorseReader() {
        // Initialize the touch sensor
        this.TOUCH_SEN = new EV3TouchSensor(SensorPort.S1);

        // Initialize the MotorController
        this.motorController = new MotorController();

        // Create the TouchInterrupt behavior
        Behavior touchInterrupt = new TouchInterrupt(motorController);

        // Create an array of behaviors for the arbitrator
        Behavior[] behaviors = { touchInterrupt };

        // Initialize the arbitrator with the behaviors
        this.arbitrator = new Arbitrator(behaviors);
    }

    
    @Override
    public void run() {
        // Implement the logic for touch reader mode
        // This method will be called when the thread starts
        LCD.clear();
        LCD.drawString("Touch Mode", 0, 0);
        // Add your logic here to read Morse code using the touch sensor
        Delay.msDelay(1000);
        
        // Start the arbitrator to manage behaviors
        arbitrator.go();
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