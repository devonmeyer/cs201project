package glassLine.interfaces;

import glassLine.Glass;

public interface Conveyor {

    public abstract void msgSensorGlassIsReady();

    public abstract void msgSensorHereIsGlass(Glass g);

    public abstract void msgGlassAtEndOfConveyor(Glass g);

    public abstract void msgSensorReady();




}