package morse;

import lejos.utility.Delay;

public abstract class MorseReader extends Thread {
    
    public StringBuilder WORD = new StringBuilder();
    public StringBuilder MORSE = new StringBuilder();
    public boolean reading;
    public MorseReaderBase morseReaderBase = new MorseReaderBase();
    
    public void startReading() {
        reading = true;
        start();
    }

    public void stopReading() {
        reading = false;
    }

    protected abstract float[] getSensorData();

    public StringBuilder getMorse() {
        return MORSE;
    }

    public String getMorseString() {
        StringBuilder decodedWord = new StringBuilder();
        String morseString = MORSE.toString();
        String[] morseSymbols = morseString.split(" ");
        
        for (String symbol : morseSymbols) {
            decodedWord.append(MorseReaderBase.decodeMorse(symbol));
        }
        return decodedWord.toString();
    }

    public StringBuilder getDotDash() {
        return new StringBuilder();
    }
    
    @Override
    public void run() {
        while (reading) {
            float[] sensorData = getSensorData();
            Delay.msDelay(100);
        }
    }
}