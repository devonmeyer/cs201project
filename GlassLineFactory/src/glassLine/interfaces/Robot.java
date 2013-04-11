package glassLine.interfaces;

import glassLine.Glass;

public interface Robot {

	// Sent to Robot when popup has a piece of glass that is ready to be processed
	
    public abstract void msgPopupGlassIsReady();

	// Sent to Robot when popup is handing Robot a piece of glass
	
    public abstract void msgPopupHereIsGlass(Glass g);
	
	// Sent to Robot when popup is ready to take back the processed glass
	
    public abstract void msgPopupReady();


}
