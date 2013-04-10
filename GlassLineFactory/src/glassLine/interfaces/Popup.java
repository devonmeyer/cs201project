package glassLine.interfaces;

import glassLine.Glass;

public interface Popup {

    public abstract void msgRobotReady();

    public abstract void msgRobotGlassIsReady();

    public abstract void msgRobotHereIsGlass(Glass g);

}