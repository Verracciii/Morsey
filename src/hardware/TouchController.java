package hardware;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;

public class TouchController {

    private EV3TouchSensor touchSensor;

    public TouchController() {
        this.touchSensor = new EV3TouchSensor(SensorPort.S1);
    }

    public EV3TouchSensor getTouchSensor() {
        return touchSensor;
    }

    public void close() {
        touchSensor.close();
    }
}
