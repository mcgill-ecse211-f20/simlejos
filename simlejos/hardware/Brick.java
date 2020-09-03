package simlejos.hardware;

import simlejos.hardware.port.Port;

public interface Brick{
    /**
     * Return a port object for the request port name. This allows access to the
     * hardware associated with the specified port.
     * @param portName The name of port
     * @return the request port
     */
    public Port getPort(String portName);
    
    /**
     * Test whether the brick is a local one
     * @return true iff brick is local
     */
    public boolean isLocal();
    
    /**
     * Get the type of brick, e.g. "EV3", "NXT", "BrickPi"
     * @return the brick type
     */
    public String getType();
    
    /**
     * Get he name of the brick
     * @return the name
     */
    public String getName();
    
    /**
     * Get the local Wifi device
     * @return the local Wifi device
     */
    //public LocalWifiDevice getWifiDevice();
    
    /**
     * Set this brick as the default one for static methods
     */
    public void setDefault();
    
    

}
