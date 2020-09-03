package simlejos.hardware.sensor;

import simlejos.hardware.port.Port;

import com.cyberbotics.webots.controller.Robot;
import com.cyberbotics.webots.controller.DistanceSensor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Wrapper class around Webots' DistanceSensor class to simulate Lejos' EV3UltrasonicSensor class
 * The main difference is that the constructor uses the name of the sensor object on the robot rather than the port number
 Lejos API:
  http://www.lejos.org/ev3/docs/lejos/hardware/sensor/EV3UltrasonicSensor.html
 Webots API:
  https://cyberbotics.com/doc/reference/distancesensor?tab-language=java
 */

public class EV3UltrasonicSensor extends BaseSensor implements SensorModes {
  
  /**
   * The Webots sensor we will interface with
   */
  final DistanceSensor sensor;
 
  /**
   * Lock for the sensor
   */
  private static Lock lock = new ReentrantLock();
  
  public EV3UltrasonicSensor(Robot robot,String name) {
    //Get target sensor
    sensor = robot.getDistanceSensor(name);
    setModes(new SensorMode[]{ new DistanceMode() }); 
    lock.lock();
    try {
      //Set the timestep to that of the robot
      sensor.enable((int)robot.getBasicTimeStep());
    } catch (Exception e) {
      System.err.println("EV3UltrasonicSensor enable exception: " + e.getMessage());
    } finally {
      lock.unlock();
    }
  }
  
  public EV3UltrasonicSensor(Port port) {
    this(port.getRobot(),port.getName());
  }
  
  public SensorMode getDistanceMode(){
   return getMode(0);
  }

  public int getModeCount(){
   return 1;
  }
  
  //Distance mode  
  private class DistanceMode implements SensorMode {
  
    @Override
    public int sampleSize(){
      return 1;
    }

    @Override
    public void fetchSample(float[] sample, int offset){
      lock.lock();
      try {
        sample[offset]=(float)(sensor.getValue()/100.0);
      } catch (Exception e) {
        System.err.println("EV3UltrasonicSensor fetchSample exception: " + e.getMessage());
      } finally {
        lock.unlock();
      }
    }
        
    @Override
    public String getName(){
      return "Distance";
    }
     
  }

}
