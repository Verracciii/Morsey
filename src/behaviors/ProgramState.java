package behaviors;

public class ProgramState {
	public static String currentState = "Morsey"; // Always start in Morsey
	
	public static boolean isInMorsey(){
		return currentState.equals("Morsey");
	}
	
	public static void setState(String state) {
		currentState = state;
	}
}
