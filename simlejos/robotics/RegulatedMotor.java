package simlejos.robotics;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import simlejos.ExecutionController;
import simlejos.FakeClasses.Motor;
import simlejos.FakeClasses.PositionSensor;
import simlejos.FakeClasses.Robot;
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
  }
  
  
  /**
   * Causes motor to rotate forward until stop() or flt() is called.
   */
  public void forward() {
  }
  
  /**
   * Causes motor to rotate backward until stop() or flt() is called.
   */
  public void backward() {
  }

  /**
   * Sets the acceleration rate of this motor in degrees/sec/sec.
   */
  public void setAcceleration(int acceleration) {
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
  }
  
  /**
   * Returns the current motor speed in degrees per second.
   */
  public int getSpeed() {
    motorLock.lock();
    // need to convert to degrees
    double velocity = speed * 180 / Math.PI;
    motorLock.unlock();
    return (int) Math.round(velocity);
  }

  /**
   * Returns the maximum speed that can be maintained.
   */
  public float getMaxSpeed() {
    return 0;
  }

  /**
   * Returns the current motor torque in Newton per Meters.
   */
  public double getTorque() {
    return 0;
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
  }

  /**
   * Rotate by the request number of degrees.
   */
  public void rotate(int angle, boolean immediateReturn) {
  }
  
  
  
  /**
   * Blocks while motor is moving.
   */
  public void waitUntilTargetReached() {
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
    return 0;
  }

  /**
   * Reads sensor value, relative to last tacho reset.
   */
  public double getOffsetSensorValue() {
    return 0;
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
