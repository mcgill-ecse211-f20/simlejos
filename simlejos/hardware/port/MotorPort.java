package simlejos.hardware.port;

import simlejos.hardware.ev3.LocalEV3;

/**
 * Abstraction for an EV3 output port.
 * @author Andy Shaw/Olivier St-Martin Cormier
 */
public interface MotorPort {
  public static final Port A = LocalEV3.get().getPort("A");
  public static final Port B = LocalEV3.get().getPort("B");
  public static final Port C = LocalEV3.get().getPort("C");
  public static final Port D = LocalEV3.get().getPort("D");
}
