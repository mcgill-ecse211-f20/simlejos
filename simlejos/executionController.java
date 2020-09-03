package simlejos;

import simlejos.hardware.ev3.LocalEV3;

import java.util.concurrent.CyclicBarrier;

public class executionController {
  public static final executionController controller = new executionController();
  
  /**
   * CyclicBarrier to control execution, one party per thread (main/UltrasonicController)
   * Defaults to 1 party
   */
  private static CyclicBarrier barrier = new CyclicBarrier(1);
  
  private static int numberOfParties = 1;
  
  private executionController(){
  }

  public static boolean performPhysicsStep(){
    int result=-1;
    try {
      //Wait for all threads to be synchronized
      barrier.await();
      // perform the physics step
      result=LocalEV3.getRobot().step((int)LocalEV3.getRobot().getBasicTimeStep());
      //Wait for all threads to be synchronized
      barrier.await();
    } catch (Exception e) {
      // Nothing to do here but print information about the exception
      System.err.println("Exception: " + e.getMessage());
    }
    return result==-1?false:true;
  }
  
  /**
   * "Sleep" until the next physics step is performed
   */
  public static void waitUntilNextStep(){
    if(numberOfParties==1){
      performPhysicsStep();
    }else{  
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
    for(double i=0;i<(double)millis/LocalEV3.getRobot().getBasicTimeStep();i++){
      waitUntilNextStep();
    }
  }
  
  public static void setNumberOfParties(int n){
   numberOfParties=n;
   barrier=new CyclicBarrier(n);
  }
}
