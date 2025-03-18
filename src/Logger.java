import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class Logger extends Thread {
    private TextLCD lcd;
    private EV3LargeRegulatedMotor motor;
    private EV3TouchSensor sensor;
    private String morseString = "";
    private String decodedWord = "";

    // Constructor: Initializes the LCD, motor, and sensor
    public Logger(TextLCD lcd) {
        this.lcd = lcd;
        this.motor = new EV3LargeRegulatedMotor(MotorPort.A);
        this.sensor = new EV3TouchSensor(SensorPort.S1);
       
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
            lcd.drawString("Motor Speed: " + motor.getSpeed(), 0, 1);
            lcd.drawString("Motor Tacho: " + motor.getTachoCount(), 0, 2);
            float[] sample = new float[sensor.sampleSize()];
            sensor.fetchSample(sample, 0);
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
