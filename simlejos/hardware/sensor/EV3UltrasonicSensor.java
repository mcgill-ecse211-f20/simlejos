package simlejos.hardware.sensor;

import com.cyberbotics.webots.controller.DistanceSensor;
import com.cyberbotics.webots.controller.Robot;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import simlejos.hardware.port.Port;

/**
 * Wrapper class around the Webots DistanceSensor class to simulate the leJOS EV3UltrasonicSensor
 * class. The main difference is that the constructor uses the name of the sensor object on the
 * robot rather than the port number.
 * 
 * @see <a href="http://www.lejos.org/ev3/docs/lejos/hardware/sensor/EV3UltrasonicSensor.html">Lejos API</a>
 * @see <a href="https://cyberbotics.com/doc/reference/distancesensor?tab-language=java">Webots API</a>
 */
public class EV3UltrasonicSensor extends BaseSensor implements SensorModes {
  
  /**
   * The Webots sensor to interface with.
   */
  final DistanceSensor sensor;
 
  /**
   * Lock for the sensor.
   */
  private static Lock lock = new ReentrantLock();
  
  private static final int DISTANCE_MODE = 0;
  private static final int MODE_COUNT = 1;

  /**
   * Constructs an EV3UltrasonicSensor.
   * 
   * @param robot the robot
   * @param name the sensor name
   */
  public EV3UltrasonicSensor(Robot robot, String name) {
    // Get target sensor
    sensor = robot.getDistanceSensor(name);
    setModes(new SensorMode[] {new DistanceMode()});
    lock.lock();
    try {
      // Set the timestep to that of the robot
      sensor.enable((int) robot.getBasicTimeStep());
    } catch (Exception e) {
      System.err.println("EV3UltrasonicSensor enable exception: " + e.getMessage());
    } finally {
      lock.unlock();
    }
  }

  public EV3UltrasonicSensor(Port port) {
    this(port.getRobot(), port.getName());
  }

  public SensorMode getDistanceMode() {
    return getMode(DISTANCE_MODE);
  }

  public int getModeCount() {
    return MODE_COUNT;
  }
  
  //Distance mode  
  private class DistanceMode implements SensorMode {
    
    private static final String NAME = "Distance";
    private static final int SAMPLE_SIZE = 1;
    private static final double SCALING_FACTOR = 100.0;
  
    @Override
    public int sampleSize() {
      return SAMPLE_SIZE;
    }

    @Override
    public void fetchSample(float[] sample, int offset) {
      lock.lock();
      try {
        sample[offset] = (float) (sensor.getValue() / SCALING_FACTOR);
      } catch (Exception e) {
        System.err.println("EV3UltrasonicSensor fetchSample exception: " + e.getMessage());
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
