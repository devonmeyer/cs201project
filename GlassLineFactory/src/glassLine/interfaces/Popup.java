package glassLine.interfaces;

import glassLine.Glass;

public interface Popup {
	
	// Sent to Popup when Robot is ready for glass
	
    public abstract void msgRobotReady(boolean isTop);
	
	// Sent to Popup when glass is done being processed and is ready to be sent back to popup
	
    public abstract void msgRobotGlassIsReady(boolean isTop);
	
	// Sent to Popup when handing popup a piece of processed glass

    public abstract void msgRobotHereIsGlass(Glass g, boolean isTop);

}