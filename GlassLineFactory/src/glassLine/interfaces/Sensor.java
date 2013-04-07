package glassLine.interfaces;

import glassLine.Glass;

public interface Sensor {

    public abstract void msgGlassIsReady();

    public abstract void msgHereIsGlass(Glass g);

    public abstract void msgReadyToTakeGlass();

    enum SensorType {CONVEYOR, POPUP}


}
