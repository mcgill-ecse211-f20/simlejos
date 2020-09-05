package simlejos.hardware.sensor;

import com.cyberbotics.webots.controller.PositionSensor;
import com.cyberbotics.webots.controller.Robot;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import simlejos.hardware.port.Port;

/**
 * Wrapper class around the Webots position sensor class to simulate the leJOS EV3TouchSensor class.
 * 
 * @author Olivier St-Martin Cormier
 */
public class EV3TouchSensor extends BaseSensor implements SensorModes {

  /**
   * The Webots sensor to interface with.
   */
  private final PositionSensor sensor;
  
  /**
   * Lock for the sensor.
   */
  private static Lock lock = new ReentrantLock();
  
  private static final int TOUCH_MODE = 0;
  private static final int ANALOG_MODE = 1;
  
  private static final int MODE_COUNT = 2;

  /**
   * Constructs an EV3TouchSensor object.
   * 
   * @param robot the robot
   * @param name the sensor name
   */
  public EV3TouchSensor(Robot robot, String name) {
    //Get target sensor
    sensor = robot.getPositionSensor(name);
    setModes(new SensorMode[] {new TouchMode(), new AnalogMode()});
    //Enable sensor
    lock.lock();
    try {
      //Set the timestep to that of the robot
      sensor.enable((int) robot.getBasicTimeStep());
    } catch (Exception e) {
      System.err.println("EV3TouchSensor enable exception: " + e.getMessage());
    } finally {
      lock.unlock();
    }
  }
  
  /**
   * Constructs an EV3TouchSensor object.
   * 
   * @param port the port on which the sensor is attached.
   */
  public EV3TouchSensor(Port port) {
    this(port.getRobot(), port.getName());
  }

  /**
   * Returns digital touch mode.
   * 
   * @see TouchMode
   * @return digital touch mode
   */
  public SensorMode getTouchMode() {
    return getMode(TOUCH_MODE);
  }
  
  /**
  * Returns analog touch mode.
  * 
  * @see AnalogMode
  * @return analog touch mode
  */
  public SensorMode getAnalogMode() {
    return getMode(ANALOG_MODE);
  }

  public int getModeCount() {
    return MODE_COUNT;
  }

  /**
   * Digital touch mode for the touch sensor. In this mode, the value of the sample is 0 if the
   * sensor is not pressed and 1 if it is.
   */
  private class TouchMode implements SensorMode {
    
    private static final String NAME = "Touch";
    private static final int SAMPLE_SIZE = 1;
    private static final double SCALING_FACTOR = 0.005;

    @Override
    public int sampleSize() {
      return SAMPLE_SIZE;
    }

    @Override
    public void fetchSample(float[] sample, int offset) {
      lock.lock();
      try {
        double sensorValue = sensor.getValue() / SCALING_FACTOR;
        // Sometimes, the first few measures return NaN, so we fix that by returning 0,
        // as the initial value should be 0
        if (Double.isNaN(sensorValue)) {
          sensorValue = 0;
        }
        sample[offset] = sensorValue > 0.5 ? 1.00f : 0.00f;
      } catch (Exception e) {
        System.err.println("EV3TouchSensor fetchSample exception: " + e.getMessage());
      } finally {
        lock.unlock();
      }
    }
        
    @Override
    public String getName() {
      return NAME;
    }
     
  }
  
  /**
   * Analog touch mode for the touch sensor. In this mode, the value of the sample ranges from 0
   * to 1 depending on the extent to which the sensor is pressed.
   */
  private class AnalogMode implements SensorMode {
    
    private static final String NAME = "Analog";
    private static final int SAMPLE_SIZE = 1;
    private static final double SCALING_FACTOR = 0.005;
  
    @Override
    public int sampleSize() {
      return SAMPLE_SIZE;
    }

    @Override
    public void fetchSample(float[] sample, int offset) {
      lock.lock();
      try {
        sample[offset] = (float) (sensor.getValue() / SCALING_FACTOR);
        // Make sure the result is between 0 and 1.
        // Simulated values can be outside that range in extreme cases
        if (sample[offset] < 0) {
          sample[offset] = 0;
        }
        if (sample[offset] > 1) {
          sample[offset] = 1;
        }
      } catch (Exception e) {
        System.err.println("EV3TouchSensor fetchSample exception: " + e.getMessage());
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
