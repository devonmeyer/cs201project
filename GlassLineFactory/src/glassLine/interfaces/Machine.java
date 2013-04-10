package glassLine.interfaces;
import glassLine.Glass;

public interface Machine {

    // Sent to Machine when asking to give machine piece of glass, and the piece of glass does need processed by that machine

    public abstract void msgGlassIsReady();

    // Sent to Machine when asking to give machine piece of glass, but the piece of glass does not need processed by that machine

    public abstract void msgGlassNeedsThrough();

    // Sent to Machine when handing machine a piece of glass

    public abstract void msgHereIsGlass(Glass g);

    // Sent to Machine when the conveyor it wants to move its glass to is ready to take the glass.

    public abstract void msgReadyToTakeGlass();


}
