package glassLine.interfaces;

import glassLine.Glass;

public interface Popup {
	
	// Sent to Popup when Robot is ready for glass
	
    public abstract void msgRobotReady();
	
	// Sent to Popup when glass is done being processed and is ready to be sent back to popup
	
    public abstract void msgRobotGlassIsReady();
	
	// Sent to Popup when handing popup a piece of processed glass

    public abstract void msgRobotHereIsGlass(Glass g);

}