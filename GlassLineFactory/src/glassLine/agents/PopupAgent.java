package glassLine.agents;

import engine.agent.Agent;
import glassLine.Glass;
import glassLine.interfaces.Conveyor;
import glassLine.interfaces.Popup;
import glassLine.interfaces.Robot;
import glassLine.test.EventLog;
import glassLine.test.LoggedEvent;
import transducer.TChannel;
import transducer.TEvent;

/**
 * Created with IntelliJ IDEA.
 * User: devon
 * Date: 3/31/13
 * Time: 1:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class PopupAgent extends Agent implements Popup {

    /*

    Data

     */

    private enum GlassState {NONE, NEEDS_THROUGH, NEEDS_ROBOT, WAITING, WAITING_THROUGH, WAITING_ROBOT, MOVE_TO_ROBOT, MOVE_TO_CONVEYOR, PROCESSING}

    private Glass currentGlass;

/*
    private Sensor popupSensor;
*/

/*
    private Sensor conveyorSensor;
*/

    private Conveyor entryConveyor;

    private Conveyor exitConveyor;

    private Robot robot;

    private boolean popupEngaged;

    private GlassState conveyorGlassState;

    private GlassState myGlassState;

    private GlassState robotGlassState;

    public EventLog log;


    public PopupAgent(){
        currentGlass = null;
/*        popupSensor = null;
        conveyorSensor = null;*/
        robot = null;
        popupEngaged = false;
        conveyorGlassState = GlassState.NONE;
        myGlassState = GlassState.NONE;
        robotGlassState = GlassState.NONE;
        log = new EventLog();

    }

    public void setConveyors(Conveyor ent, Conveyor ex){
        entryConveyor = ent;
        exitConveyor = ex;
    }

    public void setRobot(Robot r){
        robot = r;
    }

    public boolean getConveyorGlassGoingToRobot(){

        if(conveyorGlassState == GlassState.WAITING_ROBOT){
            return true;
        }

        return false;
    }

    public boolean getPopupEngaged(){

        return popupEngaged;
    }

    public Glass getCurrentGlass(){

        return currentGlass;
    }

    /*

   Messaging

    */

    public void msgGlassIsReady(){
        log.add(new LoggedEvent("Received message : msgGlassIsReady"));

        conveyorGlassState = GlassState.NEEDS_ROBOT;
        stateChanged();

    }

    public void msgGlassNeedsThrough(){
        log.add(new LoggedEvent("Received message : msgGlassNeedsThrough"));

        conveyorGlassState = GlassState.NEEDS_THROUGH;
        stateChanged();

    }

    public void msgRobotGlassIsReady(){
        log.add(new LoggedEvent("Received message : msgRobotGlassIsReady"));

        robotGlassState = GlassState.NEEDS_THROUGH;

    }

    public void msgHereIsGlass(Glass g){
        log.add(new LoggedEvent("Received message : msgHereIsGlass"));

        currentGlass = g;

        if(conveyorGlassState == GlassState.WAITING_ROBOT){
            myGlassState = GlassState.NEEDS_ROBOT;
        } else {
            myGlassState = GlassState.NEEDS_THROUGH;
        }

        conveyorGlassState = GlassState.NONE;
        stateChanged();

    }

    public void msgRobotHereIsGlass(Glass g){
        log.add(new LoggedEvent("Received message : msgRobotHereIsGlass"));

        currentGlass = g;
        myGlassState = GlassState.NEEDS_THROUGH;
        robotGlassState = GlassState.NONE;
        stateChanged();

    }

    public void msgRobotReady(){
        log.add(new LoggedEvent("Received message : msgRobotReady"));

        myGlassState = GlassState.MOVE_TO_ROBOT;

    }

    public void msgConveyorReady(){
        log.add(new LoggedEvent("Received message : msgConveyorReady"));

        myGlassState = GlassState.MOVE_TO_CONVEYOR;

    }


    /*

    Scheduler

     */

    public boolean pickAndExecuteAnAction(){
        if(myGlassState == GlassState.MOVE_TO_ROBOT){

            moveMyGlassToRobot();
            return true;

        }
        if(myGlassState == GlassState.MOVE_TO_CONVEYOR){

            moveMyGlassToConveyor();
            return true;

        }
        if(myGlassState == GlassState.NEEDS_THROUGH){

            readyMoveToConveyor();
            return true;

        }
        if(myGlassState == GlassState.NEEDS_ROBOT){

            readyMoveToRobot();
            return true;

        }
        if(myGlassState == GlassState.NONE){

            if(robotGlassState == GlassState.NEEDS_THROUGH){

                readyMoveFromRobot();
                return true;

            }
            if(conveyorGlassState == GlassState.NEEDS_THROUGH){

                readyMoveFromConveyor();
                return true;

            }
            if(conveyorGlassState == GlassState.NEEDS_ROBOT && robotGlassState == GlassState.NONE){

                readyMoveFromConveyor();
                return true;

            }


        }


        return false;

    }


    /*

    Actions

    */

    private void readyMoveToConveyor(){
        log.add(new LoggedEvent("Carrying out action : readyMoveToConveyor"));

        exitConveyor.msgGlassIsReady();
        myGlassState = GlassState.WAITING;

    }

    private void readyMoveToRobot(){
        log.add(new LoggedEvent("Carrying out action : readyMoveToRobot"));

        robot.msgPopupGlassIsReady();
        myGlassState = GlassState.WAITING;

    }

    private void moveMyGlassToRobot(){
        log.add(new LoggedEvent("Carrying out action : moveMyGlassToRobot"));

        if(!popupEngaged){
            //doRaisePopup()
            popupEngaged = true;
        }

        //doMoveGlassToRobot

        robot.msgPopupHereIsGlass(currentGlass);

        currentGlass = null;

        myGlassState = GlassState.NONE;

        robotGlassState = GlassState.PROCESSING;

    }

    private void moveMyGlassToConveyor(){
        log.add(new LoggedEvent("Carrying out action : moveMyGlassToConveyor"));

        if(popupEngaged){
            popupEngaged = false;
            //doLowerPopup();
        }

        //doMoveGlassToSensor();

        exitConveyor.msgHereIsGlass(currentGlass);

        currentGlass = null;

        myGlassState = GlassState.NONE;

    }

    private void readyMoveFromRobot(){
        log.add(new LoggedEvent("Carrying out action : readyMoveFromRobot"));


        if(!popupEngaged){
            popupEngaged = true;
            //doRaisePopup();
        }

        myGlassState = GlassState.WAITING;

        robotGlassState = GlassState.WAITING;

        robot.msgPopupReady();

    }

    private void readyMoveFromConveyor(){
        log.add(new LoggedEvent("Carrying out action : readyMoveFromSensor"));

        if(popupEngaged){
            popupEngaged = false;
            //doLowerPopup();
        }

        if(conveyorGlassState == GlassState.NEEDS_ROBOT){
            conveyorGlassState = GlassState.WAITING_ROBOT;
        } else {
            conveyorGlassState = GlassState.WAITING_THROUGH;
        }

        myGlassState = GlassState.WAITING;

        entryConveyor.msgReadyToTakeGlass();

    }


    /*

   Gui Stuff

    */

    public void eventFired(TChannel channel, TEvent event, Object[] args){

    }


}
