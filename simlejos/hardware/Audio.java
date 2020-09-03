package simlejos.hardware;

import com.cyberbotics.webots.controller.Robot;
import com.cyberbotics.webots.controller.Speaker;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Wrapper class around Webots' Speaker class to simulate Lejos' Sound class.
 * 
 * @see <a href="http://www.lejos.org/ev3/docs/lejos/hardware/Sound.html">Lejos API</a>
 * @see <a href="https://www.cyberbotics.com/doc/reference/speaker?tab-language=java">Webots API</a>
 * 
 * @author Olivier St-Martin Cormier
 */
public class Audio {

  /**
   * The audio file used to beep. The filepath is with respect to the root of the project.
   */
  public static final String BEEP_AUDIO_FILE = "beep.wav";

  /**
   * The default langauge used for text to speech.
   */
  public static final String TEXT_TO_SPEECH_DEFAULT_LANGUAGE = "en-US";

  /**
   * The Webots speaker to interface with.
   */
  final Speaker speaker;

  /**
   * Lock for the speaker.
   */
  private static Lock lock = new ReentrantLock();

  public Audio(Robot robot) {
    // Get target sensor
    speaker = robot.getSpeaker("speaker");
  }

  /**
   * Beeps once.
   */
  public void beep() {
    lock.lock();
    try {
      Speaker.playSound(speaker, speaker, BEEP_AUDIO_FILE, 1, 1, 0, false);
    } catch (Exception e) {
      System.err.println("Audio exception: " + e.getMessage());
    } finally {
      lock.unlock();
    }
  }

  /**
   * Plays the input sound file.
   * 
   * @param filename the path of the file
   */
  public void playSample(String filename) {
    lock.lock();
    try {
      Speaker.playSound(speaker, speaker, filename, 1, 1, 0, false);
    } catch (Exception e) {
      System.err.println("Audio exception: " + e.getMessage());
    } finally {
      lock.unlock();
    }
  }

  /**
   * Speaks the given text in American English.
   * 
   * @param text the text to speak
   */
  public void speak(String text) {
    lock.lock();
    try {
      speaker.setLanguage(TEXT_TO_SPEECH_DEFAULT_LANGUAGE);
      speaker.speak(text, 1);
    } catch (Exception e) {
      System.err.println("Audio exception: " + e.getMessage());
    } finally {
      lock.unlock();
    }
  }

  /**
   * Speaks the given text in the given language.
   * 
   * @param text the text to speak
   * @param language the language used to speak text
   */
  public void speak(String text, String language) {
    lock.lock();
    try {
      speaker.setLanguage(language);
      speaker.speak(text, 1);
    } catch (Exception e) {
      System.err.println("Audio exception: " + e.getMessage());
    } finally {
      lock.unlock();
    }
  }

}
