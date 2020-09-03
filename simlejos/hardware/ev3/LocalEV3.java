package simlejos.hardware.ev3;

import simlejos.hardware.port.Port;
import simlejos.hardware.Audio;

import com.cyberbotics.webots.controller.Robot;
import com.cyberbotics.webots.controller.Speaker;
import simlejos.executionController;

/**
 * This class represents the local instance of an EV3 device. It can be used to
 * obtain access to the various system resources (Sensors, Motors etc.).
 */
public class LocalEV3 implements EV3 {
  public static final LocalEV3 ev3 = new LocalEV3();
  //The Webots Robot
  private static Robot robot;
  //The speaker on the robot
  public static final Audio audio = new Audio(robot);
    

    
  private LocalEV3(){
    //Initialize Robot
    robot = new Robot();
    //run one physics step to make sure all devices are initialized before reading/writing to them
    robot.step((int)robot.getBasicTimeStep());
  }
    
  public static EV3 get(){
    return ev3;
  }
    
  @Override
  public Port getPort(String portName){
    return new Port(robot,portName);
  }

  @Override
  public boolean isLocal(){
    return true;
  }

  @Override
  public String getType(){
    return "EV3";
  }

  @Override
  public String getName(){
    return "WeBots Robot";
  }
  
  @Override
  public void setDefault() {
  }
  
  public static Robot getRobot(){
    return robot;
  }

    /**
     * return a Audio object which can be used to access the device's audio playback
     * @return A Audio device
     */
  public static Audio getAudio(){
    return audio;
  }
  
}
