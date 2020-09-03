package simlejos.hardware;

import com.cyberbotics.webots.controller.Robot;
import com.cyberbotics.webots.controller.Speaker;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Wrapper class around Webots' Speaker class to simulate Lejos' Sound class
 Lejos API:
  http://www.lejos.org/ev3/docs/lejos/hardware/Sound.html
 Webots API:
  https://www.cyberbotics.com/doc/reference/speaker?tab-language=java
 * @author Olivier St-Martin Cormier
 */


public class Audio { 
  /**
   * The Webots speaker we will interface with
   */
  final Speaker speaker;

  /**
   * Lock for the speaker
   */
  private static Lock lock = new ReentrantLock();
  
  public Audio(Robot robot) {
   //Get target sensor
   speaker = robot.getSpeaker("speaker");
  }
  
  /**
   * Beeps once
   */
  public void beep() {
    lock.lock();
    try {
      Speaker.playSound(speaker,speaker,"beep.wav",1,1,0,false);
    } catch (Exception e) {
      System.err.println("Audio exception: " + e.getMessage());
    } finally {
      lock.unlock();
    }
  }

  
  public void playSample(String filename) {
    lock.lock();
    try {
      Speaker.playSound(speaker,speaker,filename,1,1,0,false);
    } catch (Exception e) {
      System.err.println("Audio exception: " + e.getMessage());
    } finally {
      lock.unlock();
    }
  }
  
  public void speak(String text) {
    lock.lock();
    try {
      speaker.setLanguage("en-US");
      speaker.speak(text,1);
    } catch (Exception e) {
      System.err.println("Audio exception: " + e.getMessage());
    } finally {
      lock.unlock();
    }
  }

}
