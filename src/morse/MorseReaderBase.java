package morse;

import java.util.HashMap;
import java.util.Map;

public class MorseReaderBase {
    // Shared Morse Code Map (static so created only once)
    private static final Map<String, Character> MORSE_CODE_MAP = new HashMap<>();

    static {
        // Map Morse Code to respective characters
        MORSE_CODE_MAP.put(".-", 'A');   MORSE_CODE_MAP.put("-...", 'B');
        MORSE_CODE_MAP.put("-.-.", 'C'); MORSE_CODE_MAP.put("-..", 'D');
        MORSE_CODE_MAP.put(".", 'E');    MORSE_CODE_MAP.put("..-.", 'F');
        MORSE_CODE_MAP.put("--.", 'G');  MORSE_CODE_MAP.put("....", 'H');
        MORSE_CODE_MAP.put("..", 'I');   MORSE_CODE_MAP.put(".---", 'J');
        MORSE_CODE_MAP.put("-.-", 'K');  MORSE_CODE_MAP.put(".-..", 'L');
        MORSE_CODE_MAP.put("--", 'M');   MORSE_CODE_MAP.put("-.", 'N');
        MORSE_CODE_MAP.put("---", 'O');  MORSE_CODE_MAP.put(".--.", 'P');
        MORSE_CODE_MAP.put("--.-", 'Q'); MORSE_CODE_MAP.put(".-.", 'R');
        MORSE_CODE_MAP.put("...", 'S');  MORSE_CODE_MAP.put("-", 'T');
        MORSE_CODE_MAP.put("..-", 'U');  MORSE_CODE_MAP.put("...-", 'V');
        MORSE_CODE_MAP.put(".--", 'W');  MORSE_CODE_MAP.put("-..-", 'X');
        MORSE_CODE_MAP.put("-.--", 'Y'); MORSE_CODE_MAP.put("--..", 'Z');

        // Numbers 0-9
        MORSE_CODE_MAP.put("-----", '0'); MORSE_CODE_MAP.put(".----", '1');
        MORSE_CODE_MAP.put("..---", '2'); MORSE_CODE_MAP.put("...--", '3');
        MORSE_CODE_MAP.put("....-", '4'); MORSE_CODE_MAP.put(".....", '5');
        MORSE_CODE_MAP.put("-....", '6'); MORSE_CODE_MAP.put("--...", '7');
        MORSE_CODE_MAP.put("---..", '8'); MORSE_CODE_MAP.put("----.", '9');
    }

    // Method to decode single Morse code letter into a character
    public static Character decodeSingleMorseLetter(String morseLetter) {
        // Check if input Morse letter is null or empty
        if (morseLetter == null || morseLetter.isEmpty()) {
            System.out.println("Null or empty Morse code input");
            return null;
        }
        
        // Trim spaces from Morse code and retrieve decoded character
        Character decodedChar = MORSE_CODE_MAP.get(morseLetter.trim());
        
        // If character is found, return character; otherwise return '?' as an error marker
        if(decodedChar == null) {
            return '?';
        } else {
            return decodedChar;
        }
    }

}