package behaviors;

import hardware.MotorController;
import lejos.hardware.Battery;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class BatteryVoltageBehavior implements Behavior {
    private final MotorController motorController;
    private final EV3ColorSensor colorSensor;
    private final EV3TouchSensor touchSensor;
    private final EV3UltrasonicSensor ultrasonicSensor;
    private boolean suppressed = false;
    private static final float SHUTDOWN_VOLTAGE = 6.0f;

    public BatteryVoltageBehavior(MotorController motorController, 
                                EV3ColorSensor colorSensor,
                                EV3TouchSensor touchSensor,
                                EV3UltrasonicSensor ultrasonicSensor) {
        this.motorController = motorController;
        this.colorSensor = colorSensor;
        this.touchSensor = touchSensor;
        this.ultrasonicSensor = ultrasonicSensor;
    }

    @Override
    public boolean takeControl() {
        return Battery.getVoltage() < SHUTDOWN_VOLTAGE;
    }

    @Override
    public void action() {
        LCD.clear();
        LCD.drawString("LOW BATTERY", 0, 0);
        LCD.drawString("SHUTTING DOWN", 0, 1);
        
        if (motorController != null) {
            motorController.stop();
        }
        
        if (colorSensor != null) colorSensor.close();
        if (touchSensor != null) touchSensor.close();
        if (ultrasonicSensor != null) ultrasonicSensor.close();
        if (motorController != null) motorController.close();
        
        System.exit(0);
    }

    @Override
    public void suppress() {
        suppressed = true;
    }
}
