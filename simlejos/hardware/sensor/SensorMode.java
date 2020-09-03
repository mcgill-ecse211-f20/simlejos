package simlejos.hardware.sensor;

import simlejos.robotics.SampleProvider;


public interface SensorMode extends SampleProvider {
  
  /**
   * Returns a string description of this sensor mode.
   * @return The description/name of this mode
   */
  public String getName();
  
  // TODO: Return additional mode information

}
