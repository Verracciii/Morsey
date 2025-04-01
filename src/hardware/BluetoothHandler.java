package hardware;

import lejos.hardware.Bluetooth;
import lejos.hardware.LocalBTDevice;
import lejos.remote.nxt.NXTConnection;
import lejos.utility.Delay;
import lejos.hardware.lcd.LCD;
import java.io.*;

public class BluetoothHandler {
    private static final String OUTPUT_FILENAME = "MorseOutput.txt";
    private final String targetAddress;
    private boolean isAvailable;
    private static final int CONNECTION_TIMEOUT = 10000; // 10 seconds
    
    public BluetoothHandler(String bluetoothAddress) {
        // Store address without separators
        this.targetAddress = bluetoothAddress.replaceAll("[:-]", "").toUpperCase();
        this.isAvailable = checkBluetooth();
    }

    private boolean checkBluetooth() {
        try {
            LocalBTDevice btDevice = new LocalBTDevice();
            String localName = btDevice.getFriendlyName();
            String localAddress = btDevice.getBluetoothAddress();
            
            System.out.println("EV3 BT Name: " + localName);
            System.out.println("EV3 BT Address: " + localAddress);
            
            return (localName != null && !localName.isEmpty());
        } catch (UnsatisfiedLinkError e) {
            System.out.println("BT: Not available");
            return false;
        }
    }

    public void sendMessage(String message) {
        if (!isAvailable) {
            LCD.drawString("BT: Disabled", 0, 6);
            return;
        }

        // First write to file (for backup)
        File outputFile = new File(OUTPUT_FILENAME);
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile))) {
            dos.writeBytes(message);
            System.out.println("Saved to file: " + message);
        } catch (IOException e) {
            System.out.println("File write error");
        }

        // Then send via Bluetooth
        NXTConnection connection = null;
        try {
            String connString = "btspp://" + targetAddress + ":1";
            LCD.clear(6);
            LCD.drawString("Connecting...", 0, 6);
            
            connection = Bluetooth.getNXTCommConnector()
                                .connect(connString, NXTConnection.RAW);
            
            if (connection == null) {
                LCD.drawString("BT: No connection", 0, 6);
                return;
            }

            LCD.drawString("Sending...", 0, 6);
            try (OutputStream out = connection.openOutputStream()) {
                out.write(message.getBytes());
                out.flush();
                LCD.drawString("Sent " + message.length() + "B", 0, 6);
                System.out.println("Sent: " + message);
                Delay.msDelay(2000);
            }
        } catch (IOException e) {
            LCD.drawString("BT Error", 0, 6);
            System.out.println("BT Error: " + e.getMessage());
        } finally {
            if (connection != null) {
                try { connection.close(); } 
                catch (IOException e) { /* Ignore */ }
            }
            outputFile.delete(); // Clean up temp file
            LCD.clear(6);
        }
    }

    public static String getLocalBluetoothInfo() {
        try {
            LocalBTDevice btDevice = new LocalBTDevice();
            return "Name: " + btDevice.getFriendlyName() + 
                   "\nAddress: " + btDevice.getBluetoothAddress();
        } catch (UnsatisfiedLinkError e) {
            return "BT: Not available";
        }
    }
}