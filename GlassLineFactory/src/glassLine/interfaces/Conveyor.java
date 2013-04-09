package glassLine.interfaces;

import glassLine.Glass;

public interface Conveyor {

    public abstract void msgGlassIsReady();

    public abstract void msgHereIsGlass(Glass g);

    public abstract void msgGlassAtEndSensor(Glass g);

    public abstract void msgReadyToTakeGlass();




}