import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Logger extends Thread {
    private TextLCD lcd; 
    private EV3LargeRegulatedMotor motorA; 
    private EV3LargeRegulatedMotor motorB; 
    private EV3TouchSensor touchSensor; 
    private EV3ColorSensor colorSensor; 
    private EV3UltrasonicSensor ultrasonicSensor; 
    private String morseString = ""; 
    private String decodedWord = ""; 
    private boolean running = true; 

  
    private float[] touchSample;
    private float[] colorSample;
    private float[] ultrasonicSample;

    // Constructor: Initialises the LCD, motors, and sensors
    public Logger(TextLCD lcd) {
        this.lcd = lcd;
        this.motorA = new EV3LargeRegulatedMotor(MotorPort.A); 
        this.motorB = new EV3LargeRegulatedMotor(MotorPort.B); 
        this.touchSensor = new EV3TouchSensor(SensorPort.S1); 
        this.colorSensor = new EV3ColorSensor(SensorPort.S2); 
        this.ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S3);

        // Initializes the  sample arrays
        this.touchSample = new float[touchSensor.sampleSize()];
        this.colorSample = new float[colorSensor.sampleSize()];
        this.ultrasonicSample = new float[ultrasonicSensor.sampleSize()];
    }

    //  Continuously updates and displays data
    @Override
    public void run() {
        while (running) { // Loop while the running flag is true
            display(); 
            try {
                Thread.sleep(1000); // Sleep for 1 second to advoid overwelming LCD 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Stops the logger  when called 
    public synchronized void stopRunning() {
        this.running = false;
    }

    // Displays motor speeds, tacho counts, sensor values, Morse string, and decoded word on the LCD
    private void display() {
        try {
            lcd.clear(); 
            lcd.drawString("Motor A Speed: " + motorA.getSpeed(), 0, 1);
            lcd.drawString("Motor A Tacho: " + motorA.getTachoCount(), 0, 2);
            lcd.drawString("Motor B Speed: " + motorB.getSpeed(), 0, 3);
            lcd.drawString("Motor B Tacho: " + motorB.getTachoCount(), 0, 4);
            touchSensor.fetchSample(touchSample, 0);
            lcd.drawString("Touch Val: " + touchSample[0], 0, 5);
            colorSensor.fetchSample(colorSample, 0);
            lcd.drawString("Color Val: " + colorSample[0], 0, 6);
            ultrasonicSensor.fetchSample(ultrasonicSample, 0);
            lcd.drawString("Ultrasonic: " + ultrasonicSample[0], 0, 7);
            lcd.drawString("Morse: " + morseString, 0, 8);
            lcd.drawString("Word: " + decodedWord, 0, 9);

            lcd.refresh(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Updates the Morse string and decoded at the same time 
    public synchronized void updateMorseData(String newMorseString, String newDecodedWord) {
        this.morseString = newMorseString; 
        this.decodedWord = newDecodedWord;
    }
}
