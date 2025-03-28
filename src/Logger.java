import hardware.MotorController;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.sensor.EV3TouchSensor;

public class Logger extends Thread {
    private TextLCD lcd;
    private MotorController motorController;
    private EV3TouchSensor touchSensor;
    private String morseString = "";
    private String decodedWord = "";

    // Constructor: Initialises the LCD, motor, and sensor
    public Logger(TextLCD lcd, MotorController motorController, EV3TouchSensor touchSensorr) {
        this.lcd = lcd;
        this.motorController = motorController;
        this.touchSensor = touchSensor;
    }
    
    // Main thread loop: Continuously updates and displays data
    public void run() {
        while (true) {
            display();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    // Displays motor speed, tacho count, sensor value, Morse string, and decoded word on the LCD
    private void display() {
        try {
            lcd.clear();
            lcd.drawString("Motor Speed: " + motorController.getLeftMotor().getSpeed(), 0, 1);
            lcd.drawString("Motor Tacho: " + motorController.getLeftMotor().getTachoCount(), 0, 2);
            float[] sample = new float[touchSensor.sampleSize()];
            touchSensor.fetchSample(sample, 0);
            lcd.drawString("Sensor Val: " + sample[0], 0, 3);
            lcd.drawString("Morse: " + morseString, 0, 4);
            lcd.drawString("Word: " + decodedWord, 0, 5);
            lcd.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public synchronized void updateMorseData(String newMorseString, String newDecodedWord) {
        this.morseString = newMorseString;
        this.decodedWord = newDecodedWord;
    }
}