package simlejos.robotics;

import com.cyberbotics.webots.controller.Motor;
import com.cyberbotics.webots.controller.PositionSensor;
import com.cyberbotics.webots.controller.Robot;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import simlejos.ExecutionController;
import simlejos.hardware.port.Port;

/**
 * Wrapper class around the Webots Motor class to simulate the leJOS RegulatedMotor class.
 * The main difference is that the constructor uses the name of the motor object on the robot
 * rather than the port number.
 * 
 * @see <a href="http://www.lejos.org/ev3/docs/lejos/robotics/RegulatedMotor.html">
 *     Lejos API RegulatedMotor</a>
 * @see <a href="http://www.lejos.org/ev3/docs/lejos/hardware/motor/EV3LargeRegulatedMotor.html">
 *     Lejos API EV3LargeRegulatedMotor</a>
 * @see <a href="https://cyberbotics.com/doc/reference/motor?tab-language=java">
 *     Webots API motor</a>
 * @see <a href="https://cyberbotics.com/doc/reference/positionsensor?tab-language=java">
 *     Webots API positionsensor</a>
 *     
 * @author Olivier St-Martin Cormier
 */
public class RegulatedMotor {

  /**
   * Reference to the robot that contains the sensor.
   */
  private Robot robot;

  /**
   * The Webots motor to interface with.
   */
  private Motor target;

  /**
   * The Webots positionSensor to interface with.
   */
  private PositionSensor sensor;
  

  /**
   * Direction of the robot.
   */
  private int direction;
   
  /**
   * Speed of the robot.
   */
  private double speed;
   
  /**
   * Starting value of the tacho.
   */
  private double sensorOffset;
   
  /**
   * Lock for the motor.
   */
  private static Lock motorLock = new ReentrantLock();
   
  /**
   * Lock for the sensor.
   */
  private static Lock sensorLock = new ReentrantLock();
   

  /**
   * Creates a new RegulatedMotor.
   * 
   * @param port the port where the motor is connected
   */
  public RegulatedMotor(Port port) {
    this(port.getRobot(), port.getName());
  }
  
  /**
   * Creates a new RegulatedMotor.
   * 
   * @param robot the parent robot
   * @param name the name of the device to use
   */
  public RegulatedMotor(Robot robot, String name) {
    Motor temp = robot.getMotor(name);
    //Motor not found, skip initialization
    if (temp == null) {
      return;
    }
    //otherwise initialize motor
    try {
      setRobot(robot);
      direction = 1;
      //Get the motor
      target = temp;
      //Get and enable the positionSensor
      sensor = robot.getPositionSensor(name + "-sensor");
      sensor.enable((int) robot.getBasicTimeStep());
    } catch (Exception e) {
      System.err.println("RegulatedMotor initialization exception: " + e.getMessage());
    }
    resetTachoCount(); // Reset tacho, so that we start at zero
    setSpeed(0); // Set speed to 0 initially
  }
  
  
  /**
   * Causes motor to rotate forward until stop() or flt() is called.
   */
  public void forward() {
    motorLock.lock();
    try {
      direction = 1;
      target.setPosition(Double.POSITIVE_INFINITY);
      target.setVelocity(direction * speed);
    } catch (Exception e) {
      System.err.println("EV3LargeRegulatedMotor forward exception: " + e.getMessage());
    } finally {
      motorLock.unlock();
    }
  }
  
  /**
   * Causes motor to rotate backward until stop() or flt() is called.
   */
  public void backward() {
    motorLock.lock();
    try {
      direction = -1;
      target.setPosition(Double.POSITIVE_INFINITY);
      target.setVelocity(direction * speed);
    } catch (Exception e) {
      System.err.println("EV3LargeRegulatedMotor backward exception: " + e.getMessage());
    } finally {
      motorLock.unlock();
    }
  }

  
  
  /**
   * Sets the acceleration rate of this motor in degrees/sec/sec.
   */
  public void setAcceleration(int acceleration) {
    motorLock.lock();
    try {
      // Need to convert acceleration from degrees/sec/sec to radians/sec/sec
      target.setAcceleration((double) acceleration * Math.PI / 180);
    } catch (Exception e) {
      System.err.println("EV3LargeRegulatedMotor setAcceleration exception: " + e.getMessage());
    } finally {
      motorLock.unlock();
    }
  }
  
  /**
   * Returns true if the motor is moving.
   * TODO: Make this better
   */
  public boolean isMoving() {
    return Math.abs(speed) > 0.0001;
  }
  
  
  /**
   * Sets desired motor speed, in degrees per second; The maximum reliably sustainable velocity is
   * 100 x battery voltage under moderate load, such as a direct drive robot on the level.
   */
  public void setSpeed(int velocity) {
    motorLock.lock();
    try {
      // need to convert to radian for webots
      speed = Math.abs(velocity) * Math.PI / 180;
      // If the direction is negative, we need to be in velocity mode
      if (direction == -1) {
        target.setPosition(Double.POSITIVE_INFINITY);
      }
      target.setVelocity(direction * speed);
    } catch (Exception e) {
      System.err.println("EV3LargeRegulatedMotor setSpeed exception: " + e.getMessage());
    } finally {
      motorLock.unlock();
    }
  }

    

  // TODO: flt should let the motor coast to a stop
  /**
   * Motor loses all power, causing the rotor to float freely to a stop. This is not the same as
   * stopping, which locks the rotor.
   */
  public void flt() {
    setSpeed(0);
  }
  
  /**
   * Causes motor to stop immediately. It will resist any further motion. Cancels any rotate()
   * orders in progress.
   */
  public void stop() {
    stop(true);
  }

  /**
   * Stops the motor.
   * 
   * @param immediateReturn whether to return immediately from the method call or wait until motor
   *     stops
   */
  public void stop(boolean immediateReturn) {
    //Set speed to 0 to stop the motor
    setSpeed(0);
    //Set the target position to the current position to stop motion
    motorLock.lock();
    try {
      target.setPosition(getSensorValue());
    } catch (Exception e) {
      System.err.println("EV3LargeRegulatedMotor stop exception: " + e.getMessage());
    } finally {
      motorLock.unlock();
    }
    if (immediateReturn) {
      return;
    }
    //Wait until the motor stops moving
    waitUntilStopped();
  }

  /**
   * Rotate by the request number of degrees.
   */
  public void rotate(int angle, boolean immediateReturn) {
    double offsetPosition = (double) angle * Math.PI / 180;
    double endPosition = getSensorValue() + offsetPosition;
    motorLock.lock();
    try {
      // Velocity must be positive in position control mode
      direction = 1;
      target.setVelocity(direction * speed);
      target.setPosition(endPosition);
    } catch (Exception e) {
      System.err.println("EV3LargeRegulatedMotor rotate exception: " + e.getMessage());
    } finally {
      motorLock.unlock();
    }
    if (immediateReturn) {
      return;
    }
    // Wait until the motor reaches target
    waitUntilTargetReached();
    waitUntilStopped();
  }
  
  
  
  /**
   * Blocks while motor is moving.
   */
  public void waitUntilTargetReached() {
    motorLock.lock();
    try {
      // If velocity is 0, we can never reach position, return immediately
      if (target.getVelocity() == 0) {
        return;
      }
    } catch (Exception e) {
      System.err.println("EV3LargeRegulatedMotor waitUntilTargetReached exception: "
          + e.getMessage());
    } finally {
      motorLock.unlock();
    }
    // Wait while motor is not at the target position
    while (Math.abs(getSensorValue() - target.getTargetPosition()) > 0.01) {
      // System.out.println("target:" + target.getTargetPosition() + "\tposition: "
      //    + getSensorValue());
      ExecutionController.waitUntilNextStep(); // Sleep for one physics step
    }
  }

  
  /**
   * Blocks while motor is moving.
   */
  public void waitUntilStopped() {
    // Wait while motor is moving
    double position = getSensorValue();
    double previousPosition = Double.POSITIVE_INFINITY;
    while (Math.abs(position - previousPosition) > 0.000001) {
      previousPosition = position;
      // Sleep for two physics steps
      ExecutionController.waitUntilNextStep();
      ExecutionController.waitUntilNextStep();
      position = getSensorValue();
    }
  }





  
  /*-***************************************
   ************************* Tacho Functions
   ***************************************-*/
  
  
  /**
   * Resets the tacho offset.
   */
  public void resetTachoCount() {
    sensorLock.lock();
    try {
      sensorOffset = sensor.getValue();
      if (Double.isNaN(sensorOffset)) {
        sensorOffset = 0;
      }
    } catch (Exception e) {
      System.err.println("EV3LargeRegulatedMotor resetTachoCount exception: " + e.getMessage());
    } finally {
      sensorLock.unlock();
    }
  }

  /**
   * Returns the robot.
   * 
   * @return the robot
   */
  public Robot getRobot() {
    return robot;
  }

  /**
   * Set the robot.
   * 
   * @param robot the robot to set
   */
  public void setRobot(Robot robot) {
    this.robot = robot;
  }

  /**
   * Reads sensor value.
   */
  public double getSensorValue() {
    double value = 0;
    sensorLock.lock();
    try {
      value = sensor.getValue();
    } catch (Exception e) {
      System.err.println("EV3LargeRegulatedMotor getSensorValue exception: " + e.getMessage());
    } finally {
      sensorLock.unlock();
    }
    // Sometimes, the first few measures return NaN, so we fix that by returning 0,
    // as the initial value should be 0
    if (Double.isNaN(value)) {
      value = 0;
    }
    return value;
  }

  /**
   * Reads sensor value, relative to last tacho reset.
   */
  public double getOffsetSensorValue() {
    double value = 0;
    sensorLock.lock();
    try {
      value = sensor.getValue() - sensorOffset;
      if (Double.isNaN(value)) {
        value = 0;
      }
    } catch (Exception e) {
      System.err.println("EV3LargeRegulatedMotor getOffsetSensorValue exception: "
          + e.getMessage());
    } finally {
      sensorLock.unlock();
    }
    return value;
  }

  /**
   * Returns "tacho" count in degrees.
   */
  public int getTachoCount() {
    // Delta between the reset position and the current sensor reading
    double position = getOffsetSensorValue();
    // Convert from radian to degrees
    double degrees = position * 180 / Math.PI;
    // Return the rounded result
    return (int) Math.round(degrees);
  }

}
