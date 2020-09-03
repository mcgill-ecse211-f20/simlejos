package simlejos;

import java.util.concurrent.CyclicBarrier;
import simlejos.hardware.ev3.LocalEV3;

/**
 * The execution controller controls the execution of physics steps.
 * 
 * @author Olivier St-Martin Cormier
 */
public class ExecutionController {
  public static final ExecutionController controller = new ExecutionController();
  
  /**
   * CyclicBarrier to control execution, one party per thread (main or UltrasonicController).
   * Defaults to 1 party.
   */
  private static CyclicBarrier barrier = new CyclicBarrier(1);
  
  private static int numberOfParties = 1;
  
  private ExecutionController(){
  }

  /**
   * Performs a physics step. This method returns false if Webots is about to terminate the
   * controller within one second. This happens when the user hits the Reload button or quits
   * Webots.
   * 
   * @see <a href="https://cyberbotics.com/doc/reference/robot?tab-language=java#wb_robot_step">
   *     the Cybernotics documenation</a>
   * 
   * @return true if the physics step is successful and the controller will continue to perform
   *     more steps.
   */
  public static boolean performPhysicsStep() {
    int result = -1;
    try {
      //Wait for all threads to be synchronized
      barrier.await();
      
      // perform the physics step
      result = LocalEV3.getRobot().step((int) LocalEV3.getRobot().getBasicTimeStep());
      
      //Wait for all threads to be synchronized
      barrier.await();
    } catch (Exception e) {
      // Nothing to do here but print information about the exception
      System.err.println("Exception: " + e.getMessage());
    }
    return result != -1;
  }
  
  /**
   * "Sleep" until the next physics step is performed.
   */
  public static void waitUntilNextStep() {
    if (numberOfParties == 1) {
      performPhysicsStep();
    } else {  
      try {
        //Wait until all threads reach this point
        barrier.await();
        //physics step is performed here by the main thread
        //wait until the update is done
        barrier.await();
      } catch (Exception e) {
        // Nothing to do here but print information about the exception
        System.err.println("Exception: " + e.getMessage());
      }
    }
  }
  
  /**
   * "Sleeps" for the specified duration.
   * @param millis the duration in milliseconds
   */
  public static void sleepFor(long millis) {
    for (double i = 0; i < (double) millis / LocalEV3.getRobot().getBasicTimeStep(); i++) {
      waitUntilNextStep();
    }
  }
  
  /**
   * Sets the number of parties (i.e., threads).
   * 
   * @param n the number of parties
   */
  public static void setNumberOfParties(int n) {
    numberOfParties = n;
    barrier = new CyclicBarrier(n);
  }
}
