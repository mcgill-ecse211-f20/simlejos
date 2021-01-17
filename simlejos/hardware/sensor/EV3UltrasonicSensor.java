package simlejos.hardware.sensor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import simlejos.FakeClasses.DistanceSensor;
import simlejos.FakeClasses.Robot;
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
  final DistanceSensor sensor = null;
 
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
    setModes(new SensorMode[] {new DistanceMode()});
  }

  /**
   * Constructs an EV3UltrasonicSensor object.
   * 
   * @param port the port on which the sensor is attached.
   */
  public EV3UltrasonicSensor(Port port) {
    setModes(new SensorMode[] {new DistanceMode()});
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
    }
        
    @Override
    public String getName() {
      return NAME;
    }
     
  }

}
