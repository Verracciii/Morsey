package main;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MorseReader {

	private static final Map<String, Character> MORSE_CODE_MAP = new HashMap<>();

	static {
		MORSE_CODE_MAP.put("01", 'A');
		MORSE_CODE_MAP.put("1000", 'B');
		MORSE_CODE_MAP.put("1010", 'C');
		MORSE_CODE_MAP.put("100", 'D');
		MORSE_CODE_MAP.put("0", 'E');
		MORSE_CODE_MAP.put("0010", 'F');
		MORSE_CODE_MAP.put("110", 'G');
		MORSE_CODE_MAP.put("0000", 'H');
		MORSE_CODE_MAP.put("00", 'I');
		MORSE_CODE_MAP.put("0111", 'J');
		MORSE_CODE_MAP.put("101", 'K');
		MORSE_CODE_MAP.put("0100", 'L');
		MORSE_CODE_MAP.put("11", 'M');
		MORSE_CODE_MAP.put("10", 'N');
		MORSE_CODE_MAP.put("111", 'O');
		MORSE_CODE_MAP.put("0110", 'P');
		MORSE_CODE_MAP.put("1101", 'Q');
		MORSE_CODE_MAP.put("010", 'R');
		MORSE_CODE_MAP.put("000", 'S');
		MORSE_CODE_MAP.put("1", 'T');
		MORSE_CODE_MAP.put("001", 'U');
		MORSE_CODE_MAP.put("0001", 'V');
		MORSE_CODE_MAP.put("011", 'W');
		MORSE_CODE_MAP.put("1001", 'X');
		MORSE_CODE_MAP.put("1011", 'Y');
		MORSE_CODE_MAP.put("1100", 'Z');
		MORSE_CODE_MAP.put("11111", '0');
		MORSE_CODE_MAP.put("01111", '1');
		MORSE_CODE_MAP.put("00111", '2');
		MORSE_CODE_MAP.put("00011", '3');
		MORSE_CODE_MAP.put("00001", '4');
		MORSE_CODE_MAP.put("00000", '5');
		MORSE_CODE_MAP.put("10000", '6');
		MORSE_CODE_MAP.put("11000", '7');
		MORSE_CODE_MAP.put("11100", '8');
		MORSE_CODE_MAP.put("11110", '9');
		MORSE_CODE_MAP.put("010101", '.');
		MORSE_CODE_MAP.put("110011", ',');
		MORSE_CODE_MAP.put("001100", '?');
		MORSE_CODE_MAP.put("011110", '\'');
		MORSE_CODE_MAP.put("101011", '!');
		MORSE_CODE_MAP.put("10010", '/');
		MORSE_CODE_MAP.put("10110", '(');
		MORSE_CODE_MAP.put("101101", ')');
		MORSE_CODE_MAP.put("01000", '&');
		MORSE_CODE_MAP.put("111000", ':');
		MORSE_CODE_MAP.put("101010", ';');
		MORSE_CODE_MAP.put("10001", '=');
		MORSE_CODE_MAP.put("01010", '+');
		MORSE_CODE_MAP.put("100001", '-');
		MORSE_CODE_MAP.put("010010", '_');
		MORSE_CODE_MAP.put("011010", '"');
		MORSE_CODE_MAP.put("0001001", ' ');
		MORSE_CODE_MAP.put("01101", '@');
	}

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter a morse code as a dot(0) or line(1): ");
		String line = scan.nextLine().trim();

		String[] morseCodes = line.split(" ");
		StringBuilder word = new StringBuilder();

		for (String morseCode : morseCodes) {
			Character letter = MORSE_CODE_MAP.get(morseCode);
			if (letter != null) {
				word.append(letter);
			} else {
				System.out.println("Invalid morse code sequence: " + line);
			}
		}

		System.out.println("Decrypted sequence: " + word.toString());

		scan.close();
	}
}