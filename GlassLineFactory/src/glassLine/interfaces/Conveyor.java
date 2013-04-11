package glassLine.interfaces;

import glassLine.Glass;

public interface Conveyor {

	// Sent to Conveyor when asking to give conveyor a piece of glass
	
    public abstract void msgGlassIsReady();
	
	// Sent to Conveyor when handing Conveyor a piece of glass

    public abstract void msgHereIsGlass(Glass g);
	
	// Sent to Conveyor when the glass reaches the end of the conveyor (hits the exit sensor)
	
    public abstract void msgGlassAtEndSensor(Glass g);

	// Sent to Conveyor when the popup it wants to move its glass to is ready for the glass
	
    public abstract void msgReadyToTakeGlass();




}