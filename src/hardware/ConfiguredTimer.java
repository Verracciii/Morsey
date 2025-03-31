package hardware;

import lejos.utility.Timer;
import lejos.utility.TimerListener;

public class ConfiguredTimer implements TimerListener {
    private long startTime;       
    private long elapsedTime;     
    private boolean isRunning;     
    private Timer timer;         

    // Constructor
    public ConfiguredTimer() {
        this.isRunning = false;
        this.elapsedTime = 0;

        // Initialize the timer
        this.timer = new Timer(1000, this); 
    }

    // Start the timer
    public void start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis() - elapsedTime;  
            isRunning = true;
            timer.start();  
        }
    }

    // Stop the timer
    public void stop() {
        if (isRunning) {
            elapsedTime = System.currentTimeMillis() - startTime;
            isRunning = false;
            
        }
    }

    // Reset the timer
    public void reset() {
    	stop();
        elapsedTime = 0;
            startTime = System.currentTimeMillis();
    }

    // Get elapsed time
    public long getElapsedTime() {
        if (isRunning) {
            return System.currentTimeMillis() - startTime;
        } else {
            return elapsedTime;
        }
    }
    public boolean isMoving() {
        return isRunning;
    }
    
    @Override
    public void timedOut() {
       
    }
}
