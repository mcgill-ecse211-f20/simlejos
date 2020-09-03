package simlejos.hardware.sensor;

import java.util.ArrayList;


/**
 * Wrapper class to simulate a leJOS SampleProvider.
 * 
 * @see <a href="http://www.lejos.org/ev3/docs/lejos/hardware/port/package-summary.html">Lejos API</a>
 */
public class BaseSensor implements SensorModes {
  protected int currentMode = 0;
  protected SensorMode[] modes;
  ArrayList<String> modeList;

  /**
   * Define the set of modes to be made available for this sensor.
   * 
   * @param m An array containing a list of modes
   */
  protected void setModes(SensorMode[] m) {
    modes = m;
    // force the list to be rebuilt
    modeList = null;
    currentMode = 0;
  }

  @Override
  public ArrayList<String> getAvailableModes() {
    if (modeList == null && modes != null) {
      modeList = new ArrayList<String>(modes.length);
      for (SensorMode m : modes) {
        modeList.add(m.getName());
      }
    }
    return modeList;
  }

  @Override
  public SensorMode getMode(int mode) {
    if (mode < 0) {
      throw new IllegalArgumentException("Invalid mode " + mode);
    }
    if (modes == null || mode >= modes.length) {
      return null;
    }
    return modes[mode];
  }

  @Override
  public SensorMode getMode(String modeName) {
    int index = getAvailableModes().indexOf(modeName);
    if (index != -1) {
      return modes[index];
    }
    throw new IllegalArgumentException("No such mode " + modeName);
  }

  private boolean isValid(int mode) {
    return mode >= 0 && mode < modes.length && modes != null;
  }

  private int getIndex(String modeName) {
    return getAvailableModes().indexOf(modeName);
  }

  @Override
  public String getName() {
    return modes[currentMode].getName();
  }

  @Override
  public int sampleSize() {
    return modes[currentMode].sampleSize();
  }

  @Override
  public void fetchSample(float[] sample, int offset) {
    modes[currentMode].fetchSample(sample, offset);
  }

  @Override
  public void setCurrentMode(int mode) {
    if (!isValid(mode)) {
      throw new IllegalArgumentException("Invalid mode " + mode);
    } else {
      currentMode = mode;
    }
  }

  @Override
  public void setCurrentMode(String modeName) {
    int mode = getIndex(modeName);
    if (mode == -1) {
      throw new IllegalArgumentException("Invalid mode " + modeName);
    } else {
      currentMode = mode;
    }
  }

  @Override
  public int getCurrentMode() {
    return currentMode;
  }

  @Override
  public int getModeCount() {
    return modes.length;
  }

}
