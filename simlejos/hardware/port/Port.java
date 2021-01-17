package simlejos.hardware.port;

import simlejos.FakeClasses.Robot;

/**
 * Interface that provides a binding between a physical port and the different
 * types of sensor interfaces that can be used with it.
 * @author andy
 */
public class Port {
  
  /**
   * Reference to the parent robot.
   */
  private Robot robot;
  
  /**
   * Name of the referenced object.
   */
  private String name;

  /**
   * Returns the name of the referenced object on the robot.
   * 
   * @return a string representation of the port
   */
  public String getName() {
    return this.name;
  }
  
  /**
   * Returns a reference to the parent robot.
   * 
   * @return a reference to the parent robot
   */
  public Robot getRobot() {
    return this.robot;
  }

  /**
   * Creates a new port.
   * 
   * @param robot Reference to the containing robot
   * @param name Name of the target device
   */
  public Port(Robot robot, String name) {
    this.robot = robot;
    this.name = name;
  }
    
}
