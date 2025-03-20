package hardware;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;

public class ColorController {

    private EV3ColorSensor colorSensor;

    public ColorController() {
        this.colorSensor = new EV3ColorSensor(SensorPort.S2);
    }

    public EV3ColorSensor getColorSensor() {
        return colorSensor;
    }

    public void close() {
        colorSensor.close();
    }
}