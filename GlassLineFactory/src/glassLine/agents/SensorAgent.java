package glassLine.agents;

import engine.agent.Agent;
import glassLine.Glass;
import glassLine.interfaces.Conveyor;
import glassLine.interfaces.Popup;
import glassLine.interfaces.Sensor;
import glassLine.test.EventLog;
import glassLine.test.LoggedEvent;
import shared.enums.MachineType;
import transducer.TChannel;
import transducer.TEvent;

/**
 * Created with IntelliJ IDEA.
 * User: devon
 * Date: 3/31/13
 * Time: 12:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class SensorAgent extends Agent implements Sensor {

    enum GlassState {NONE, PENDING_ANALYSIS, NEED_TO_MOVE, WAITING_TO_MOVE, READY_TO_MOVE}

    private Conveyor conveyor;

    private Popup popup;

    private Glass myGlass;

    private GlassState myGlassState;

    private SensorType myType;

    private boolean glassQueued;

    public EventLog log;

    private MachineType myMachine;


    public SensorAgent(String type, String machine){
        if(type.equals("conveyor")){
            myType = SensorType.CONVEYOR;
        } else if(type.equals("popup")) {
            myType = SensorType.POPUP;
        } else {
            myType = null;
        }

        for (MachineType m : MachineType.values()){
            if(m.toString().equals(machine)){
                this.myMachine = m;
            }
        }

        log = new EventLog();
        conveyor = null;
        popup = null;
        myGlass = null;
        myGlassState = GlassState.NONE;
        glassQueued = false;

    }

    public void setConveyor(Conveyor c){
        conveyor = c;
    }

    public void setPopup(Popup p){
        popup = p;
    }

    public boolean getQueued(){
        return glassQueued;
    }

    public Glass getMyGlass(){
        return myGlass;
    }

    /* Messaging
     */

    public void msgGlassIsReady(){
        log.add(new LoggedEvent("Received message : msgGlassIsReady"));
        glassQueued = true;
        stateChanged();
    }

    public void msgHereIsGlass(Glass g){
        log.add(new LoggedEvent("Received message : msgHereIsGlass"));
        myGlass = g;
        myGlassState = GlassState.PENDING_ANALYSIS;
        glassQueued = false;
        stateChanged();
    }

    public void msgReadyToTakeGlass(){
        log.add(new LoggedEvent("Received message : msgReadyToTakeGlass"));
        myGlassState = GlassState.READY_TO_MOVE;
        stateChanged();
    }

    /*

    Scheduler

     */

    public boolean pickAndExecuteAnAction(){
        if(myGlass != null){
            if(myGlassState == GlassState.PENDING_ANALYSIS){
                analyzeGlass();
                return true;
            }
            if(myGlassState == GlassState.NEED_TO_MOVE){
                informGlassIsReady();
                return true;
            }
            if(myGlassState == GlassState.READY_TO_MOVE){
                moveGlass();
                return true;
            }
        } else {
            if(glassQueued){
                notifyImReady();
                return true;
            }
        }
        return false;
    }

    /*

    Actions

     */

    private void analyzeGlass(){
        log.add(new LoggedEvent("Carrying out action : analyzeGlass"));
        myGlassState = GlassState.NEED_TO_MOVE;
        stateChanged();
    }

    private void informGlassIsReady(){
        if(myType == SensorType.POPUP){
            log.add(new LoggedEvent("Carrying out action : informPopupGlassIsReady"));
            if(myGlass.getProcesses().contains(myMachine.toString())){
                popup.msgSensorGlassIsReady();
            } else {
                popup.msgSensorGlassNeedsThrough();
            }
        } else if(myType == SensorType.CONVEYOR){
            log.add(new LoggedEvent("Carrying out action : informConveyorGlassIsReady"));
            conveyor.msgSensorGlassIsReady();
        }
        myGlassState = GlassState.WAITING_TO_MOVE;
    }

    private void moveGlass(){
        if(myType == SensorType.POPUP){
            log.add(new LoggedEvent("Carrying out action : moveGlassToPopup"));


            //doMoveGlassToPopup()
            popup.msgSensorHereIsGlass(myGlass);


        } else if(myType == SensorType.CONVEYOR){
            log.add(new LoggedEvent("Carrying out action : moveGlassToConveyor"));


            //doMoveGlassToConveyor()
            conveyor.msgSensorHereIsGlass(myGlass);

        }

        myGlass = null;
        myGlassState = GlassState.NONE;
        if(glassQueued){
            stateChanged();
        }
    }

    private void notifyImReady(){
        if(myType == SensorType.POPUP){
            log.add(new LoggedEvent("Carrying out action : notifyConveyorImReady"));


            conveyor.msgSensorReady();

        } else if(myType == SensorType.CONVEYOR){
            log.add(new LoggedEvent("Carrying out action : notifyPopupImReady"));


            popup.msgSensorReady();

        }
    }

    /*

    Gui Stuff

     */

    public void eventFired(TChannel channel, TEvent event, Object[] args){

    }



}
