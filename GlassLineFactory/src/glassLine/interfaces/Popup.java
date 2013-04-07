package glassLine.interfaces;

import glassLine.Glass;

public interface Popup {

    public abstract void msgSensorGlassIsReady();

    public abstract void msgSensorGlassNeedsThrough();

    public abstract void msgSensorHereIsGlass(Glass g);

    public abstract void msgSensorReady();

    public abstract void msgRobotReady();

    public abstract void msgRobotGlassIsReady();

    public abstract void msgRobotHereIsGlass(Glass g);

}