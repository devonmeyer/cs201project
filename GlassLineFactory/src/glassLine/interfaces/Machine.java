package glassLine.interfaces;
import glassLine.Glass;

public interface Machine {

    public abstract void msgGlassIsReady();

    public abstract void msgGlassNeedsThrough();

    public abstract void msgHereIsGlass(Glass g);

    public abstract void msgConveyorReady();


}
