package glassLine.agents;

import glassLine.Glass;
import glassLine.interfaces.Conveyor;
import glassLine.interfaces.Machine;
import glassLine.test.EventLog;
import glassLine.test.LoggedEvent;
import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

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

    private int myEntrySensorIndex;

    private int myExitSensorIndex;

    private Transducer transducer;

    private Semaphore movingToMachine;

    private Conveyor conveyor;

    private enum ConveyorType {TWO_MACHINES, SHUTTLE_ENTRY, SHUTTLE_EXIT, BEGIN, END }

    private ConveyorType myType;

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

    public ConveyorAgent(String machine, Transducer t, int index){

        super("ConveyorAgent");

        glassOnMe = new LinkedList<MyGlass>();
        movingToMachine = new Semaphore(0);
        myMachine = machine;
        moving = false;
        entryMachine = null;
        exitMachine = null;
        conveyor = null;
        glassInQueue = false;
        log = new EventLog();
        transducer = t;
        myConveyorIndex = index;
        myType = null;

        //Register for channels

        transducer.register(this, TChannel.SENSOR);


    }

    public void setTwoMachines(Machine enter, Machine exit){
        myType = ConveyorType.TWO_MACHINES;
        entryMachine = enter;
        exitMachine = exit;
    }

    public void setOneMachineExitShuttle(Machine enter, Conveyor c){
        myType = ConveyorType.SHUTTLE_EXIT;
        entryMachine = enter;
        conveyor = c;
    }

    public void setOneMachineEntryShuttle(Conveyor c, Machine exit){
        myType = ConveyorType.SHUTTLE_ENTRY;
        conveyor = c;
        exitMachine = exit;

    }

    public void setOneMachineWithGRA(Machine glassRobotAgent, Machine exit){
        myType = ConveyorType.BEGIN;
        entryMachine = glassRobotAgent;
        exitMachine = exit;

    }

    public void setOneMachineWithTruck(Machine enter, Machine truck){
        myType = ConveyorType.END;
        entryMachine = enter;
        exitMachine = truck;

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

        Object args[] = new Object[myConveyorIndex];

        transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_START, args);
        moving = true;

        try {
            movingToMachine.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if(myType == ConveyorType.BEGIN){
            exitMachine.msgHereIsGlass(g.glass);
        } else if(myType == ConveyorType.END) {
            exitMachine.msgHereIsGlass(g.glass);
        } else if(myType == ConveyorType.TWO_MACHINES){
            exitMachine.msgHereIsGlass(g.glass);
        } else if(myType == ConveyorType.SHUTTLE_ENTRY){
            exitMachine.msgHereIsGlass(g.glass);
        } else if(myType == ConveyorType.SHUTTLE_EXIT){
            conveyor.msgHereIsGlass(g.glass);
        }

        glassOnMe.remove(g);

        if(glassOnMe.isEmpty()){
            transducer.fireEvent(TChannel.CONVEYOR, TEvent.CONVEYOR_DO_STOP, args);
            moving = false;
        }
    }

    private void requestMoveGlass(MyGlass g){
        log.add(new LoggedEvent("Carrying out action : requestMoveGlass"));
        if(myType != ConveyorType.SHUTTLE_EXIT){
            if(myType == ConveyorType.END){
                exitMachine.msgGlassIsReady();
            } else {
                if(g.glass.getProcesses().contains(myMachine)){
                    exitMachine.msgGlassIsReady();
                } else {
                    exitMachine.msgGlassNeedsThrough();
                }
            }
        } else {
            conveyor.msgGlassIsReady();
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
        moving = true;



    }

    private void prepareToTakeGlass(){
        log.add(new LoggedEvent("Carrying out action : prepareToTakeGlass"));


        if(myType == ConveyorType.BEGIN){
            entryMachine.msgReadyToTakeGlass();
        } else if(myType == ConveyorType.END) {
            entryMachine.msgReadyToTakeGlass();
        } else if(myType == ConveyorType.TWO_MACHINES){
            entryMachine.msgReadyToTakeGlass();
        } else if(myType == ConveyorType.SHUTTLE_ENTRY){
            conveyor.msgReadyToTakeGlass();
        } else if(myType == ConveyorType.SHUTTLE_EXIT){
            entryMachine.msgReadyToTakeGlass();
        }

        entryMachine.msgReadyToTakeGlass();

        glassInQueue = false;

    }


    /*

    Gui Stuff

     */

    public void eventFired(TChannel channel, TEvent event, Object[] args){
        if(channel == TChannel.SENSOR){
            if(event == TEvent.SENSOR_GUI_PRESSED)  {
                if((Integer) args[0] == myExitSensorIndex){
                    Glass g = null;
                    for(MyGlass mg : glassOnMe){
                        if(mg.glass.getID() == (Integer) args[1]){
                           g = mg.glass;
                        }
                    }
                    msgGlassAtEndSensor(g);
                }

            } else if(event == TEvent.SENSOR_GUI_RELEASED){

                if((Integer) args[0] == myExitSensorIndex){
                    movingToMachine.release();
                }


            }
        }
    }

}
