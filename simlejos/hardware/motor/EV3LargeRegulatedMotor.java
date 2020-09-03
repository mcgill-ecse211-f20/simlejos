package simlejos.hardware.motor;

import simlejos.robotics.RegulatedMotor;
import simlejos.hardware.port.Port;
import com.cyberbotics.webots.controller.Robot;

public class EV3LargeRegulatedMotor extends RegulatedMotor{
  public EV3LargeRegulatedMotor(Port port) {
    super(port);
  }

  public EV3LargeRegulatedMotor(Robot robot,String name) {
    super(robot,name);
  }
}
