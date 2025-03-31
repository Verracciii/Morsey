package hardware;

import lejos.hardware.Bluetooth;
import lejos.remote.nxt.NXTConnection;
import lejos.hardware.lcd.LCD;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import lejos.hardware.LocalBTDevice;

public class BluetoothHandler {
    private static final String OUTPUT_FILENAME = "MorseOutput.txt";
    private boolean bluetoothAvailable;

    public BluetoothHandler() {
        try {
            // Check if Bluetooth is available by attempting to get local device info
            LocalBTDevice btDevice = new LocalBTDevice();
            String localName = btDevice.getFriendlyName();
            System.out.println("localName = " + localName);
            bluetoothAvailable = (localName != null && !localName.isEmpty());
            
            if (bluetoothAvailable) {
                LCD.drawString("BT: Available", 0, 6);
            } else {
                LCD.drawString("BT: Not available", 0, 6);
            }
        } catch (UnsatisfiedLinkError e) {
            bluetoothAvailable = false;
            LCD.drawString("BT: Not available", 0, 6);
        }
    }

    public boolean isBluetoothAvailable() {
        return bluetoothAvailable;
    }

    public void sendMessage(String message) {
        if (!bluetoothAvailable) {
            LCD.drawString("BT: No connection", 0, 6);
            return;
        }

        try {
            // Write message to file first
            File outputFile = new File(OUTPUT_FILENAME);
            try (FileOutputStream fos = new FileOutputStream(outputFile);
                 DataOutputStream dos = new DataOutputStream(fos)) {
                dos.writeBytes(message);
                System.out.println("message = " + message);

            }

            // Connect to last paired device (empty string connects to last device)
            LCD.drawString("BT: Connecting...", 0, 6);
            NXTConnection connection = Bluetooth.getNXTCommConnector().connect("", NXTConnection.RAW);
            
            if (connection != null) {
                LCD.drawString("BT: Sending...", 0, 6);
                System.out.println("Sending");
                try (OutputStream out = connection.openOutputStream()) {
                    out.write(message.getBytes());
                    out.flush();
                    LCD.drawString("BT: Sent!", 0, 6);
                    System.out.println("Sent");
                }
                connection.close();
            } else {
                LCD.drawString("BT: Failed", 0, 6);
                System.out.println("Failed");
            }
            
            outputFile.delete();
            
        } catch (IOException e) {
            LCD.drawString("BT Error", 0, 6);
            System.out.println("Error");
        }
    }
}