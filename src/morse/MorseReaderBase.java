package morse;

import java.util.HashMap;
import java.util.Map;

public class MorseReaderBase {
    // Shared Morse Code Map (static so it's created only once)
    private static final Map<String, Character> MORSE_CODE_MAP = new HashMap<>();

    static {
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

 
    public static Character decodeSingleMorseLetter(String morseLetter) {
        if (morseLetter == null || morseLetter.isEmpty()) {
            System.out.println("Null or empty Morse code input");
            return null;
        }
        
        // Clean the morse code string: trim spaces and remove non-Morse characters
        
        
        Character decodedChar = MORSE_CODE_MAP.get(morseLetter.trim());
        
        if(decodedChar == null) {
        	return'?';
        }
        else {
        	return decodedChar;
        }
    }
    

    }
  /*  public static String decodeMorseWithSpaces(String morseSequence) {
        StringBuilder decodedSequence = new StringBuilder();
        String[] morseWords = morseSequence.split("\\s{3}"); // Split into words (3 spaces = word separation)

        for (String morseWord : morseWords) {
            String[] morseLetters = morseWord.split("\\s"); // Split into individual letters (1 space)

            for (int i = 0; i < morseLetters.length; i++) {
                // Decode the letter
                Character decodedChar = decodeSingleMorseLetter(morseLetters[i]);
                
                // Append the decoded character
                decodedSequence.append(decodedChar);
            }
            // Append space between words (but not at the end of the sentence)
            decodedSequence.append(" ");
        }

        // Remove the last space (if any) to avoid trailing spaces in the final sentence
        if (decodedSequence.length() > 0 && decodedSequence.charAt(decodedSequence.length() - 1) == ' ') {
            decodedSequence.setLength(decodedSequence.length() - 1);
        }

        return decodedSequence.toString(); // Return the full decoded sentence without the unnecessary space at the end
    }*/

