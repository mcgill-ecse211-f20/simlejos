package simlejos.hardware.sensor;

import simlejos.hardware.port.Port;

import com.cyberbotics.webots.controller.Robot;
import com.cyberbotics.webots.controller.PositionSensor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Wrapper class around Webots' positionSensor class to simulate Lejos' EV3TouchSensor class
 * @author Olivier St-Martin Cormier
 */

public class EV3TouchSensor extends BaseSensor implements SensorModes {

  /**
   * The Webots sensor we will interface with
   */
  private final PositionSensor sensor;
  
  /**
   * Lock for the sensor
   */
  private static Lock lock = new ReentrantLock();

  public EV3TouchSensor(Robot robot,String name) {
    //Get target sensor
    sensor = robot.getPositionSensor(name);
    setModes(new SensorMode[]{ new TouchMode() , new AnalogMode() }); 
    //Enable sensor
    lock.lock();
    try {
      //Set the timestep to that of the robot
      sensor.enable((int)robot.getBasicTimeStep());
    } catch (Exception e) {
      System.err.println("EV3TouchSensor enable exception: " + e.getMessage());
    } finally {
      lock.unlock();
    }
  }
  
  public EV3TouchSensor(Port port) {
    this(port.getRobot(),port.getName());
  }

  public SensorMode getTouchMode(){
    return getMode(0);
  }
  
  public SensorMode getAnalogMode(){
    return getMode(1);
  }

  public int getModeCount(){
   return 2;
  }

  //Digital touch mode  
  private class TouchMode implements SensorMode {
  
    @Override
    public int sampleSize(){
      return 1;
    }

    @Override
    public void fetchSample(float[] sample, int offset){
      lock.lock();
      try {
        double sensorValue=sensor.getValue()/0.005;
        //Sometimes, the first few measures return NaN, so we fix that by returning 0, as the initial value should be 0
        if(Double.isNaN(sensorValue))sensorValue=0;
        sample[offset]=sensorValue>0.5?1.0f:0.00f;
      } catch (Exception e) {
        System.err.println("EV3TouchSensor fetchSample exception: " + e.getMessage());
      } finally {
        lock.unlock();
      }
    }
        
    @Override
    public String getName(){
      return "Touch";
    }
     
  }
  
  //Analog touch mode  
  private class AnalogMode implements SensorMode {
  
    @Override
    public int sampleSize(){
      return 1;
    }

    @Override
    public void fetchSample(float[] sample, int offset){
      lock.lock();
      try {
        sample[offset]=(float)(sensor.getValue()/0.005);
      } catch (Exception e) {
        System.err.println("EV3TouchSensor fetchSample exception: " + e.getMessage());
      } finally {
        lock.unlock();
      }
    }
        
    @Override
    public String getName(){
      return "Analog";
    }
     
  }

}
