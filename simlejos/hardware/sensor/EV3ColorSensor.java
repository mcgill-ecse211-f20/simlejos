package simlejos.hardware.sensor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import simlejos.FakeClasses.Camera;
import simlejos.FakeClasses.LED;
import simlejos.FakeClasses.Robot;
import simlejos.hardware.port.Port;

/**
 * Wrapper class around Webots' Camera class to simulate Lejos' EV3ColorSensor class.
 * We simulate the color sensor by using a single pixel camera (a 1x1 image).
 * 
 * @see <a href="http://www.lejos.org/ev3/docs/lejos/hardware/sensor/EV3ColorSensor.html">Lejos API</a>
 * @see <a href="https://cyberbotics.com/doc/reference/camera?tab-language=java">Webots API</a>
 * 
 * @author Olivier St-Martin Cormier
 */


public class EV3ColorSensor extends BaseSensor implements SensorModes {

  /**
   * The Webots sensor to interface with.
   */
  private final Camera sensor = null;
  
  /**
   * The light on the Webots sensor to interface with.
   */
  private final LED light = null;
  
  /**
   * Lock for the sensor.
   */
  private static Lock lock = new ReentrantLock();
  
  private static final int MODE_COUNT = 3;

  /**
   * Constructs an EV3ColorSensor.
   * 
   * @param robot the robot
   * @param name the sensor name
   */
  public EV3ColorSensor(Robot robot, String name) {
    setModes(new SensorMode[] {new RedMode(), new RGBMode(), new AmbientMode()});
  }
  
  /**
   * Constructs an EV3ColorSensor object.
   * 
   * @param port the port on which the sensor is attached.
   */
  public EV3ColorSensor(Port port) {
    setModes(new SensorMode[] {new RedMode(), new RGBMode(), new AmbientMode()});
  }
  
  public int getModeCount() {
    return MODE_COUNT;
  }

  
  public SensorMode getRedMode() {
    setFloodlight(true);
    return getMode(0);
  }

  public SensorMode getRGBMode() {
    setFloodlight(true);
    return getMode(1);
  }

  public SensorMode getAmbientMode() {
    setFloodlight(false);
    return getMode(2);
  }

    
  
  /**
   * Turns the default LED light on or off.
   */
  public void setFloodlight(boolean floodlight) {
  }
  
  /**
   * Checks if the floodlight is currently on.
   */
  public boolean isFloodlightOn() {
    return false;
  }
  
  private class RGBMode implements SensorMode {
    
    private static final String NAME = "RGB";
    private static final int SAMPLE_SIZE = 3;
  
    @Override
    public int sampleSize() {
      return SAMPLE_SIZE;
    }

    @Override
    public void fetchSample(float[] sample, int offset) {
    }

    @Override
    public String getName() {
      return NAME;
    }
     
  }
  
  private class AmbientMode implements SensorMode {
    
    private static final String NAME = "Ambient";
    private static final int SAMPLE_SIZE = 1;
  
    @Override
    public int sampleSize() {
      return SAMPLE_SIZE;
    }

    @Override
    public void fetchSample(float[] sample, int offset) {
    }

    @Override
    public String getName() {
      return NAME;
    }
     
  }

  private class RedMode implements SensorMode {
    
    private static final String NAME = "Red";
    private static final int SAMPLE_SIZE = 1;
  
    @Override
    public int sampleSize() {
      return SAMPLE_SIZE;
    }

    @Override
    public void fetchSample(float[] sample, int offset) {
    }

    @Override
    public String getName() {
      return NAME;
    }
     
  }
  
}
