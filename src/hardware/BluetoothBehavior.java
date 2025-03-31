package hardware;
import lejos.hardware.Bluetooth;
import lejos.robotics.subsumption.Behavior;
import lejos.remote.nxt.NXTConnection;
import java.io.DataInputStream;
import java.io.IOException;

public class BluetoothBehavior implements Behavior {
    private volatile boolean suppressed = false;
    private NXTConnection connection;
    private DataInputStream inputStream;
    private boolean connected = false;
    
    public BluetoothBehavior() {
        // Initialize connection in a separate thread
        new ConnectionThread().start();
    }
    
    private class ConnectionThread extends Thread {
        @Override
        public void run() {
            try {
                System.out.println("Waiting for BT connection...");
                connection = Bluetooth.getNXTCommConnector().waitForConnection(10000, NXTConnection.RAW); // 10s timeout
                if (connection != null) {
                    inputStream = connection.openDataInputStream();
                    connected = true;
                    System.out.println("BT Connected!");
                }
            } catch (Exception e) {
                System.err.println("Connection failed: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean takeControl() {
        if (!connected) return false;
        
        try {
            return inputStream != null && inputStream.available() > 0;
        } catch (IOException e) {
            System.err.println("Stream error: " + e.getMessage());
            cleanup();
            return false;
        }
    }

    @Override
    public void action() {
        suppressed = false;
        try {
            String message = inputStream.readUTF();
            System.out.println("Received: " + message);
            
            // Handle different commands
            if ("STOP".equalsIgnoreCase(message.trim())) {
                System.out.println("Stop command processed");
            } 
            else if ("START".equalsIgnoreCase(message.trim())) {
                System.out.println("Start command processed");
            }
            else {
                System.out.println("Unknown command");
            }
        } catch (IOException e) {
            System.err.println("Read error: " + e.getMessage());
            cleanup();
        }
    }

    @Override
    public void suppress() {
        suppressed = true;
    }

    private void cleanup() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.close();
            }
            connected = false;
        } catch (IOException e) {
            System.err.println("Cleanup error: " + e.getMessage());
        }
    }
}