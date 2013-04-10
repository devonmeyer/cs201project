package glassLine.agents;

import glassLine.Glass;
import glassLine.interfaces.Conveyor;
import glassLine.interfaces.Machine;
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

    private Machine entryMachine;

    private Machine exitMachine;

    private String myMachine;

    private int myConveyorIndex;

    private class MyGlass{
        public Glass glass;
        public GlassState state;

        public MyGlass(Glass g){
            this.glass = g;
            this.state = GlassState.MID_CONVEYOR;
        }


    }

    private boolean glassInQueue;

    public EventLog log;

    public ConveyorAgent(String machine){
        glassOnMe = new LinkedList<MyGlass>();
        myMachine = machine;
        moving = false;
        entryMachine = null;
        exitMachine = null;
        glassInQueue = false;
        log = new EventLog();
    }

    public void setConveyorIndex(int i){
        myConveyorIndex = i;
    }

    public void setMachines(Machine enter, Machine exit){
        entryMachine = enter;
        exitMachine = exit;
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

    public void msgGlassIsReady(){
        log.add(new LoggedEvent("Received message : msgGlassIsReady"));


        glassInQueue = true;
        stateChanged();

    }

    public void msgHereIsGlass(Glass g){
        log.add(new LoggedEvent("Received message : msgHereIsGlass"));

        glassInQueue = false;


        glassOnMe.add(new MyGlass(g));
        stateChanged();

    }

    public void msgGlassAtEndSensor(Glass g){
        log.add(new LoggedEvent("Received message : msgGlassAtEndSensor"));


        for(MyGlass mg : glassOnMe){
            if(mg.glass == g){
                mg.state = GlassState.END_CONVEYOR;
                stateChanged();
            }
        }

    }

    public void msgReadyToTakeGlass(){
        log.add(new LoggedEvent("Received message : msgReadyToTakeGlass"));


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
                moveGlassToMachine(mg);
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

    private void moveGlassToMachine(MyGlass g){
        log.add(new LoggedEvent("Carrying out action : moveGlassToMachine"));

        //doMoveGlassToSensor

        exitMachine.msgHereIsGlass(g.glass);

        glassOnMe.remove(g);

        if(!glassOnMe.isEmpty()){

            Object args[] = new Object[myConveyorIndex];

            transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_START, args);
            moving = true;
        }
    }

    private void requestMoveGlass(MyGlass g){
        log.add(new LoggedEvent("Carrying out action : requestMoveGlass"));

        if(g.glass.getProcesses().contains(myMachine)){
            exitMachine.msgGlassIsReady();
        } else {
            exitMachine.msgGlassNeedsThrough();
        }

        g.state = GlassState.WAITING_TO_EXIT;

        Object args[] = new Object[myConveyorIndex];

        transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_STOP, args);
        moving = false;

    }

    private void startConveyor(){
        log.add(new LoggedEvent("Carrying out action : startConveyor"));

        moving = true;


        Object args[] = new Object[myConveyorIndex];

        transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_START, args);



    }

    private void prepareToTakeGlass(){
        log.add(new LoggedEvent("Carrying out action : prepareToTakeGlass"));

        entryMachine.msgReadyToTakeGlass();

        glassInQueue = false;

    }


    /*

    Gui Stuff

     */

    public void eventFired(TChannel channel, TEvent event, Object[] args){

    }

}
