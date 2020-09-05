package simlejos.hardware.sensor;

import com.cyberbotics.webots.controller.Camera;
import com.cyberbotics.webots.controller.LED;
import com.cyberbotics.webots.controller.Robot;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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
  private final Camera sensor;
  
  /**
   * The light on the Webots sensor to interface with.
   */
  private final LED light;
  
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
    //Get target sensor
    sensor = robot.getCamera(name);
    light = robot.getLED(name + "-light");
    setModes(new SensorMode[] {new RedMode(), new RGBMode(), new AmbientMode()});
    //Enable sensor
    lock.lock();
    try {
      //Set the timestep to that of the robot
      sensor.enable((int) robot.getBasicTimeStep());
    } catch (Exception e) {
      System.err.println("EV3ColorSensor enable exception: " + e.getMessage());
    } finally {
      lock.unlock();
    }
    //Turn the light on
    setFloodlight(true);
  }
  
  /**
   * Constructs an EV3ColorSensor object.
   * 
   * @param port the port on which the sensor is attached.
   */
  public EV3ColorSensor(Port port) {
    this(port.getRobot(), port.getName());
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
    lock.lock();
    try {
      light.set(floodlight ? 1 : 0);
    } catch (Exception e) {
      System.err.println("ColorSensor setFloodlight exception: " + e.getMessage());
    } finally {
      lock.unlock();
    }
  }
  
  /**
   * Checks if the floodlight is currently on.
   */
  public boolean isFloodlightOn() {
    int state = 0;
    lock.lock();
    try {
      state = light.get();
    } catch (Exception e) {
      System.err.println("ColorSensor isFloodlightOn exception: " + e.getMessage());
    } finally {
      lock.unlock();
    }
    return state == 1;
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
      lock.lock();
      try {
        sample[offset] = Camera.imageGetRed(sensor.getImage(), sensor.getWidth(), 0, 0);
        sample[offset + 1] = Camera.imageGetGreen(sensor.getImage(), sensor.getWidth(), 0, 0);
        sample[offset + 2] = Camera.imageGetBlue(sensor.getImage(), sensor.getWidth(), 0, 0);
      } catch (Exception e) {
        System.err.println("EV3ColorSensor fetchSample exception: " + e.getMessage());
      } finally {
        lock.unlock();
      }
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
      lock.lock();
      try {
        float r = Camera.imageGetRed(sensor.getImage(), sensor.getWidth(), 0, 0);
        float g = Camera.imageGetGreen(sensor.getImage(), sensor.getWidth(), 0, 0);
        float b = Camera.imageGetBlue(sensor.getImage(), sensor.getWidth(), 0, 0);
        sample[offset] = (r + g + b) / 3;
      } catch (Exception e) {
        System.err.println("EV3ColorSensor fetchSample exception: " + e.getMessage());
      } finally {
        lock.unlock();
      }
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
      lock.lock();
      try {
        sample[offset] = Camera.imageGetRed(sensor.getImage(), sensor.getWidth(), 0, 0);
      } catch (Exception e) {
        System.err.println("EV3ColorSensor fetchSample exception: " + e.getMessage());
      } finally {
        lock.unlock();
      }
    }

    @Override
    public String getName() {
      return NAME;
    }
     
  }
  
}
