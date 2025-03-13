package morse;

import lejos.hardware.sensor.EV3ColorSensor;

public class ColorMorseReader extends Thread implements MorseReader {

	float DOT_DUR;
	float DASH_DUR;
	float SPACE_DUR;
	private EV3ColorSensor COLOR_SEN;
	private float[] CURR_SAMPLE;
	public StringBuilder WORD;
	public StringBuilder MORSE;
	private boolean running;
	private float BLACK_MIN;
	private float BLACK_MAX;
	
	public void run() {

	}
	
	@Override
	public StringBuilder getMorse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder getMorseWord() {
		// TODO Auto-generated method stub
		return null;
	}

}
