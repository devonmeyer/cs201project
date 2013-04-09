package glassLine.agents;

import glassLine.Glass;
import glassLine.interfaces.Conveyor;
import glassLine.interfaces.Sensor;
import glassLine.test.EventLog;
import glassLine.test.LoggedEvent;
import transducer.TChannel;
import transducer.TEvent;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: devon
 * Date: 3/31/13
 * Time: 12:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConveyorAgent extends Agent implements Conveyor {

    enum GlassState {MID_CONVEYOR, END_CONVEYOR, WAITING_TO_EXIT, EXIT_TO_SENSOR }

    private LinkedList<MyGlass> glassOnMe;

    private boolean moving;

    private class MyGlass{
        public Glass glass;
        public GlassState state;

        public MyGlass(Glass g){
            this.glass = g;
            this.state = GlassState.MID_CONVEYOR;
        }


    }

    private Sensor enterSensor;

    private Sensor exitSensor;

    private boolean glassInQueue;

    public EventLog log;

    public ConveyorAgent(){
        glassOnMe = new LinkedList<MyGlass>();
        moving = false;
        enterSensor = null;
        exitSensor = null;
        glassInQueue = false;
        log = new EventLog();
    }

    public void setSensors(Sensor enter, Sensor exit){
        enterSensor = enter;
        exitSensor = exit;
    }

    public boolean getQueued(){

        return glassInQueue;

    }

    public boolean getMoving(){

        return moving;
    }

    public int getNumGlassOnMe(){

        return glassOnMe.size();
    }

    /*

    Messaging

     */

    public void msgSensorGlassIsReady(){
        log.add(new LoggedEvent("Received message : msgSensorGlassIsReady"));


        glassInQueue = true;
        stateChanged();

    }

    public void msgSensorHereIsGlass(Glass g){
        log.add(new LoggedEvent("Received message : msgSensorHereIsGlass"));

        glassInQueue = false;


        glassOnMe.add(new MyGlass(g));
        stateChanged();

    }

    public void msgGlassAtEndOfConveyor(Glass g){
        log.add(new LoggedEvent("Received message : msgGlassAtEndOfConveyor"));


        for(MyGlass mg : glassOnMe){
            if(mg.glass == g){
                mg.state = GlassState.END_CONVEYOR;
                stateChanged();
            }
        }

    }

    public void msgSensorReady(){
        log.add(new LoggedEvent("Received message : msgSensorReady"));


        for(MyGlass mg : glassOnMe){
            if(mg.state == GlassState.WAITING_TO_EXIT){
                mg.state = GlassState.EXIT_TO_SENSOR;
                stateChanged();
            }
        }

    }

    /*

      Scheduler

     */

    public boolean pickAndExecuteAnAction(){

        for(MyGlass mg : glassOnMe){
            if(mg.state == GlassState.EXIT_TO_SENSOR){
                moveGlassToSensor(mg);
                return true;
            }
        }
        for(MyGlass mg : glassOnMe){
            if(mg.state == GlassState.END_CONVEYOR){
                requestMoveGlass(mg);
                return true;
            }
        }
        if(!moving){
            for(MyGlass mg : glassOnMe){
                if(mg.state == GlassState.MID_CONVEYOR){
                    startConveyor();
                    return true;
                }
            }
        }
        if(glassInQueue){
            boolean canTakeGlass = true;
            for(MyGlass mg : glassOnMe){
                if(mg.state == GlassState.WAITING_TO_EXIT){
                    canTakeGlass = false;
                }
            }
            if(canTakeGlass){
                prepareToTakeGlass();
            }
        }

        return false;
    }

    /*

    Actions

     */

    private void moveGlassToSensor(MyGlass g){
        log.add(new LoggedEvent("Carrying out action : moveGlassToSensor"));

        //doMoveGlassToSensor

        exitSensor.msgHereIsGlass(g.glass);

        glassOnMe.remove(g);

        if(!glassOnMe.isEmpty()){

            //doStartConveyor();
            moving = true;
        }
    }

    private void requestMoveGlass(MyGlass g){
        log.add(new LoggedEvent("Carrying out action : requestMoveGlass"));

        exitSensor.msgGlassIsReady();

        g.state = GlassState.WAITING_TO_EXIT;

        //doStopConveyor();
        moving = false;

    }

    private void startConveyor(){
        log.add(new LoggedEvent("Carrying out action : startConveyor"));

        moving = true;

        //doStartConveyor

    }

    private void prepareToTakeGlass(){
        log.add(new LoggedEvent("Carrying out action : prepareToTakeGlass"));

        enterSensor.msgReadyToTakeGlass();

        glassInQueue = false;

    }


    /*

    Gui Stuff

     */

    public void eventFired(TChannel channel, TEvent event, Object[] args){

    }

}
