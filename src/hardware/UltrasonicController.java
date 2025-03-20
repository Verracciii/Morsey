package hardware;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class UltrasonicController {

    private EV3UltrasonicSensor ultrasonicSensor;

    public UltrasonicController() {
        this.ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S3);
    }

    public EV3UltrasonicSensor getUltrasonicSensor() {
        return ultrasonicSensor;
    }

    public void close() {
        ultrasonicSensor.close();
    }
}