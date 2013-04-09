package glassLine.interfaces;

import glassLine.Glass;

public interface Popup {

    public abstract void msgGlassIsReady();

    public abstract void msgGlassNeedsThrough();

    public abstract void msgHereIsGlass(Glass g);

    public abstract void msgConveyorReady();

    public abstract void msgRobotReady();

    public abstract void msgRobotGlassIsReady();

    public abstract void msgRobotHereIsGlass(Glass g);

}