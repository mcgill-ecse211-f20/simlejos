package simlejos.hardware.motor;

import simlejos.hardware.port.MotorPort;
import simlejos.robotics.RegulatedMotor;

/**
 * Motor class contains 3 instances of regulated motors.
 * 
 * @author Roger Glassey/Andy Shaw/Olivier St-Martin Cormier
 */
public class Motor {
  public static final RegulatedMotor A = new RegulatedMotor(MotorPort.A);
  public static final RegulatedMotor B = new RegulatedMotor(MotorPort.B);
  public static final RegulatedMotor C = new RegulatedMotor(MotorPort.C);
  public static final RegulatedMotor D = new RegulatedMotor(MotorPort.D);

  private Motor() {
    // Motor class cannot be instantiated
  }

}
