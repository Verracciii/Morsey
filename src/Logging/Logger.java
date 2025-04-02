package Logging;

import hardware.MotorController;
import lejos.hardware.lcd.TextLCD;
import morse.ColorMorseReader;

public class Logger extends Thread {
    private final TextLCD lcd;
    private final MotorController motorController;
    private final ColorMorseReader colorMorseReader;
    private String morseString = "";
    private String decodedWord = "";

    // Constructor: Initialises the LCD, motor controller, and Morse reader sensor
    public Logger(TextLCD lcd, MotorController motorController, ColorMorseReader colorMorseReader) {
        this.lcd = lcd;
        this.motorController = motorController;
        this.colorMorseReader = colorMorseReader;
    }

    // Main thread loop: Continuously updates and displays data
    @Override
    public void run() {
        while (true) {
            updateMorseData(); // Fetch the latest Morse code data
            display(); // Update the LCD with new values
            try {
                Thread.sleep(200); // Refresh rate: 5 times per second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status and exit
                break;
            }
        }
    }

    // Displays motor speed, tacho count, sensor value, Morse string, and decoded word on the LCD
    private void display() {
        try {
            lcd.clear(); // Clear the screen before displaying new data
            lcd.drawString("Motor Speed: " + motorController.getLeftMotor().getSpeed(), 0, 1);
            lcd.drawString("Motor Tacho: " + motorController.getLeftMotor().getTachoCount(), 0, 2);
            lcd.drawString("Morse: " + morseString, 0, 4);
            lcd.drawString("Word: " + decodedWord, 0, 5); // Display decoded word, including spaces
            lcd.refresh(); // Show updated values
        } catch (Exception e) {
            e.printStackTrace(); // Print any errors to the console
        }
    }

    // Updates Morse data from the ColorMorseReader
    private void updateMorseData() {
        StringBuilder morseSequence = colorMorseReader.getMorse(); // Get raw Morse sequence
        StringBuilder wordSequence = colorMorseReader.getMorseWord(); // Get decoded word sequence

        if (morseSequence != null) {
            morseString = morseSequence.toString(); // Convert Morse sequence to string
        }
        
        if (wordSequence != null) {
            decodedWord = wordSequence.toString(); // Convert decoded word sequence to string
        }
    }
}
