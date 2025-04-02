package hardware;

import lejos.utility.Timer;
import lejos.utility.TimerListener;

public class ConfiguredTimer implements TimerListener {
    private long startTime;
    private long elapsedTime;
    private boolean isRunning;
    private Timer timer;

    // Constructor: Initialises timer and sets default values
    public ConfiguredTimer() {
        this.isRunning = false;
        this.elapsedTime = 0;
        
        // Initialise timer with an interval of 1000ms (1 second)
        this.timer = new Timer(1000, this); 
    }

    // Starts timer
    public void start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis() - elapsedTime;  // Adjust start time to resume from pause
            isRunning = true;
            timer.start();  // Start timer
        }
    }

    // Stops timer
    public void stop() {
        if (isRunning) {
            elapsedTime = System.currentTimeMillis() - startTime; // Calculate elapsed time
            isRunning = false;
        }
    }

    // Reset timer
    public void reset() {
        stop(); // Ensure timer is stopped before resetting
        elapsedTime = 0;
        startTime = System.currentTimeMillis(); // Reset start time
    }

    // Returns elapsed time in milliseconds
    public long getElapsedTime() {
        if (isRunning) {
            return System.currentTimeMillis() - startTime;
        } else {
            return elapsedTime;
        }
    }
    
    // Checks if timer is currently running
    public boolean isMoving() {
        return isRunning;
    }

	@Override
	public void timedOut() {
		// Not needed to be implementedS
	}
   
}
