package simlejos.hardware.port;

import com.cyberbotics.webots.controller.Robot;

/**
 * Interface that provides a binding between a physical port and the different
 * types of sensor interfaces that can be used with it
 * @author andy
 *
 */
public class Port {
  /**
   * Reference to the parent robot 
   */
  private Robot robot;
  
  /**
   * Name of the referenced object
   */
  private String name;

  /**
   * return the name of the referenced object on the robot
   * @return a string representation of the port
   */
  public String getName(){
    return this.name;
  }
  
  /**
   * return a reference to the parent robot
   * @return a reference to the parent robot
   */
  public Robot getRobot(){
    return this.robot;
  }

  /**
   * create a new port
   * @param robot
   * Reference to the containing robot
   * @param name
   * Name of the target device
   */
  public Port(Robot robot,String name) {
    this.robot=robot;
    this.name=name;
  }
    
}
