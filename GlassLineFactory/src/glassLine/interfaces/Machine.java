package glassLine.interfaces;
import glassLine.Glass;

public interface Machine {
	
	public abstract void msgReadyToTakeGlass();
	
	public abstract void msgGlassProcessed();
	
	public abstract void msgHereIsGlass(Glass g);

}
