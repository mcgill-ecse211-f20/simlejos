package simlejos.hardware.motor;

import simlejos.FakeClasses.Robot;
import simlejos.hardware.port.Port;
import simlejos.robotics.RegulatedMotor;

public class EV3LargeRegulatedMotor extends RegulatedMotor {
  public EV3LargeRegulatedMotor(Port port) {
    super(port);
  }

  public EV3LargeRegulatedMotor(Robot robot, String name) {
    super(robot, name);
  }
}
