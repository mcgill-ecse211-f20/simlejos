package ca.mcgill.ecse211.project;

import com.cyberbotics.webots.controller.Robot;


/**
 * Wrapper class to simulate a Lejos Brick
 Lejos API:
  http://www.lejos.org/ev3/docs/lejos/hardware/port/package-summary.html
 * @author Olivier St-Martin Cormier
 */

 

public class Brick {
  /**
   * The Webots sensor we will interface with
   */
  private Robot parentRobot;


  /**
   * The Webots sensor we will interface with
   */
  final Speaker target;

  /**
   * Lock for the speaker
   */
  private static Lock lock = new ReentrantLock();
  
  public EV3Speaker(Robot robot) {
   //Get target sensor
   target = robot.getSpeaker("speaker");
  }

  /**
   * Beeps once
   */
  public void beep() {
    lock.lock();
    try {
      Speaker.playSound(target,target,"beep.wav",1,1,0,false);
    } catch (Exception e) {
      System.err.println("EV3Speaker beep exception: " + e.getMessage());
    } finally {
      lock.unlock();
    }
  }
  
  public void playSample(String filename) {
    lock.lock();
    try {
      Speaker.playSound(target,target,filename,1,1,0,false);
    } catch (Exception e) {
      System.err.println("EV3Speaker playSample exception: " + e.getMessage());
    } finally {
      lock.unlock();
    }
  }
  
  public void speak(String text) {
    lock.lock();
    try {
      target.setLanguage("en-US");
      target.speak(text,1);
    } catch (Exception e) {
      System.err.println("EV3Speaker speak exception: " + e.getMessage());
    } finally {
      lock.unlock();
    }
  }
  
  
}
