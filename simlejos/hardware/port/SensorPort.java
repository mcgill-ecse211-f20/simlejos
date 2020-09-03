package simlejos.hardware.port;

import simlejos.hardware.ev3.LocalEV3;
import simlejos.hardware.port.Port;

/**
 * Basic interface for EV3 sensor ports.
 * @author Andy Shaw/Olivier St-Martin Cormier
 *
 */
public interface SensorPort {
  public static final Port S1 = LocalEV3.get().getPort("S1");
  public static final Port S2 = LocalEV3.get().getPort("S2");
  public static final Port S3 = LocalEV3.get().getPort("S3");
  public static final Port S4 = LocalEV3.get().getPort("S4");
}
