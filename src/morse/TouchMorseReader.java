package morse;

import lejos.hardware.sensor.EV3TouchSensor;

public class TouchMorseReader extends Thread implements MorseReader {

	float DOT_DUR;
	float DASH_DUR;
	float SPACE_DUR;
	private EV3TouchSensor TOUCH_SEN;
	private float[] CURR_SAMPLE;
	public StringBuilder WORD;
	public StringBuilder MORSE;
	private boolean running;
	
	public void run() { }
	
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
