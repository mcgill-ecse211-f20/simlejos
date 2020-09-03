package simlejos.robotics;

/**
 * Abstraction for classes that fetch samples from a sensor and classes that are able to process
 * samples.<br>
 * Taken from the LejOS distribution.
 * 
 * @author Aswin Bouwmeester
 */
public interface SampleProvider {

  /**
   * Returns the number of elements in a sample.<br>
   * The number of elements does not change during runtime.
   * 
   * @return the number of elements in a sample
   */
  public int sampleSize();

  /**
   * Fetches a sample from a sensor or filter.
   * 
   * @param sample The array to store the sample in.
   * @param offset The sample elements are stored in the array starting at the offset position.
   */
  public void fetchSample(float[] sample, int offset);
}
