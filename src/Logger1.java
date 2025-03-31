

import hardware.MotorController;
import lejos.hardware.lcd.TextLCD;
import morse.ColorMorseReader;

public class Logger1 extends Thread {
    private final TextLCD lcd;
    private final MotorController motorController;
    private final ColorMorseReader colorMorseReader;
    private String morseString = "";
    private String decodedWord = "";

    public Logger1(TextLCD lcd, MotorController motorController, ColorMorseReader colorMorseReader) {
        this.lcd = lcd;
        this.motorController = motorController;
        this.colorMorseReader = colorMorseReader;
    }

    @Override
    public void run() {
        while (true) {
            updateMorseData();
            display();
            try {
                Thread.sleep(200); // Refresh rate of 5 times per second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void display() {
        try {
            lcd.clear();
            lcd.drawString("Motor Speed: " + motorController.getLeftMotor().getSpeed(), 0, 1);
            lcd.drawString("Motor Tacho: " + motorController.getLeftMotor().getTachoCount(), 0, 2);
            lcd.drawString("Morse: " + morseString, 0, 4);
            lcd.drawString("Word: " + decodedWord, 0, 5);
            lcd.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMorseData() {
        StringBuilder morseSequence = colorMorseReader.getMorse();
        StringBuilder wordSequence = colorMorseReader.getMorseWord();

        if (morseSequence != null) {
            morseString = morseSequence.toString();
        }
        
        if (wordSequence != null) {
            decodedWord = wordSequence.toString();
        }
    }
}
