package glassLine.agents;

import glassLine.Glass;
import glassLine.interfaces.Conveyor;
import glassLine.interfaces.Machine;
import glassLine.interfaces.Popup;
import glassLine.interfaces.Robot;
import glassLine.test.EventLog;
import gui.panels.subcontrolpanels.TracePanel;
import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Created with IntelliJ IDEA.
 * User: devon
 * Date: 3/31/13
 * Time: 1:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class PopupAgent extends Agent implements Popup, Machine {

    /*

    Data

     */
	private Semaphore robotGUI;
	public Semaphore conveyorAction;

	public Timer timer = new Timer();
	
	private boolean toldRobot;		//hack
	
    private enum GlassState {NONE, NEEDS_THROUGH, ASKEDCONVEYOR, NEEDS_ROBOT, WAITING, WAITING_THROUGH, WAITING_ROBOT, MOVE_TO_TOP_ROBOT, MOVE_TO_BOTTOM_ROBOT, MOVE_TO_CONVEYOR, PROCESSING}

    private Glass currentGlass;

    private Conveyor entryConveyor;

    private Conveyor exitConveyor;

    private Robot topRobot;

    private Robot bottomRobot;

    private boolean popupEngaged;

    private GlassState conveyorGlassState;

    private GlassState myGlassState;

    private GlassState robotTopGlassState;

    private GlassState robotBottomGlassState;

    private int myPopupIndex;

    private int myTopRobotIndex;

    private int myBottomRobotIndex;

    private TChannel myMachineChannel;

    private Transducer transducer;

    private Semaphore animation;

    public EventLog log;


    public PopupAgent(Transducer t, int popupIndex, int topRobotIndex, int bottomRobotIndex, TracePanel tp){
        super("PopupAgent");
        currentGlass = null;
        popupEngaged = false;
        conveyorGlassState = GlassState.NONE;
        myGlassState = GlassState.NONE;
        robotTopGlassState = GlassState.NONE;
        robotBottomGlassState = GlassState.NONE;
        animation = new Semaphore(0);
        robotGUI = new Semaphore(0,true);
        conveyorAction = new Semaphore(0,true);
        transducer = t;

        tracePanel = tp;
        
        myPopupIndex = popupIndex;

        myTopRobotIndex = topRobotIndex;

        myBottomRobotIndex = bottomRobotIndex;

        transducer.register(this, TChannel.POPUP);
  
	
        log = new EventLog();
        
        toldRobot = false;

    }

    public void setConveyors(Conveyor ent, Conveyor ex){
        entryConveyor = ent;
        exitConveyor = ex;
    }

    public void setRobots(Robot t, Robot b){
        topRobot = t;
        bottomRobot = b;
    }

    public void setMachineChannel(TChannel m){

        myMachineChannel = m;

        transducer.register(this, m);

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
        print("Popup"+myPopupIndex+"Received message : msgGlassIsReady");

        conveyorGlassState = GlassState.NEEDS_ROBOT;
        stateChanged();

    }

    public void msgGlassNeedsThrough(){
        print("Popup"+myPopupIndex+"Received message : msgGlassNeedsThrough");

        conveyorGlassState = GlassState.NEEDS_THROUGH;
        stateChanged();

    }

    public void msgRobotGlassIsReady(boolean isTop){
        print("Popup"+myPopupIndex+"Received message : msgRobotGlassIsReady");

        if(isTop){
            robotTopGlassState = GlassState.NEEDS_THROUGH;
        } else {
            robotBottomGlassState = GlassState.NEEDS_THROUGH;
        }
        
        stateChanged();

    }

    public void msgHereIsGlass(Glass g){
        print("Popup"+myPopupIndex+"Received message : msgHereIsGlass");

        currentGlass = g;

        if(conveyorGlassState == GlassState.WAITING_ROBOT){
            myGlassState = GlassState.NEEDS_ROBOT;
        } else {
            myGlassState = GlassState.NEEDS_THROUGH;
        }

        conveyorGlassState = GlassState.NONE;
        stateChanged();

    }

    public void msgRobotHereIsGlass(Glass g, boolean isTop){
        print("Popup"+myPopupIndex+"Received message : msgRobotHereIsGlass");
        
        currentGlass = g;
        myGlassState = GlassState.NEEDS_THROUGH;

        toldRobot = false;	//hack

        if(isTop){
            robotTopGlassState = GlassState.NONE;
        } else {
            robotBottomGlassState = GlassState.NONE;
        } 

        stateChanged();

    }

    public void msgRobotReady(boolean isTop){
        print("Popup"+myPopupIndex+" Received message : msgRobotReady");
//        print("Popup"+myPopupIndex+" myGlassState is none? "+(myGlassState == GlassState.NONE ? "TRUE" : "FALSE"));

        if(isTop){
            myGlassState = GlassState.MOVE_TO_TOP_ROBOT;
        } else {
            myGlassState = GlassState.MOVE_TO_BOTTOM_ROBOT;
        }
        robotGUI.release();
        stateChanged();
    }

    public void msgReadyToTakeGlass(){
        print("Popup"+myPopupIndex+"Received message : msgReadyToTakeGlass");
        conveyorAction.release();
        myGlassState = GlassState.MOVE_TO_CONVEYOR;
        
        stateChanged();

    }


    /*

    Scheduler

     */

    public boolean pickAndExecuteAnAction(){
//        if(myGlassState == GlassState.MOVE_TO_TOP_ROBOT || myGlassState == GlassState.MOVE_TO_BOTTOM_ROBOT){
//
//            moveMyGlassToRobot();
//            return true;
//
//        }
        if(myGlassState == GlassState.MOVE_TO_CONVEYOR){

            moveMyGlassToConveyor();
            return true;

        }
        if(myGlassState == GlassState.NEEDS_THROUGH){
 //       	if(myStation()){
 //       	  System.out.println("Waiting for robot glass release animation");

            readyMoveToConveyor();
            return true;

        }
        if(myGlassState == GlassState.NEEDS_ROBOT){

            readyMoveToRobot();
            return true;

        }
        if(myGlassState == GlassState.NONE || myGlassState == GlassState.WAITING){

            if((robotTopGlassState == GlassState.NEEDS_THROUGH || robotBottomGlassState == GlassState.NEEDS_THROUGH)){
            	if(!toldRobot){
            		readyMoveFromRobot();
                	return true;
            	}
            }
            if(conveyorGlassState == GlassState.NEEDS_THROUGH){

                readyMoveFromConveyor();
                return true;

            }
            if(conveyorGlassState == GlassState.NEEDS_ROBOT && (robotTopGlassState == GlassState.NONE || robotBottomGlassState == GlassState.NONE)){

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
        print("Popup"+myPopupIndex+"Carrying out action : readyMoveToConveyor");
//    	Object args[];
//
//        if(popupEngaged){
//            args = new Object[1];
//            args[0] = myPopupIndex;
//        	transducer.fireEvent(TChannel.POPUP, TEvent.POPUP_DO_MOVE_DOWN, args);
//        	try {
//                animation.acquire();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        	popupEngaged = false;
//        }
        myGlassState = GlassState.ASKEDCONVEYOR;
        exitConveyor.msgGlassIsReady();
        try {
			conveyorAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//  	}
       
    }

    private void readyMoveToRobot(){
        print("Popup"+myPopupIndex+"Carrying out action : readyMoveToRobot");
        if(robotTopGlassState == GlassState.NONE){
//        	print("GOT HERE");
            topRobot.msgPopupGlassIsReady();
//            print("AND HERE"); 
        } else {
            bottomRobot.msgPopupGlassIsReady();
        }
        myGlassState = GlassState.WAITING;
        try {
			robotGUI.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        moveMyGlassToRobot();
    }

    private void moveMyGlassToRobot(){
        print("Popup"+myPopupIndex+"Carrying out action : moveMyGlassToRobot");

        Object args[];

        if(!popupEngaged){
            args = new Object[1];
            args[0] = myPopupIndex;
            
             transducer.fireEvent(TChannel.POPUP, TEvent.POPUP_DO_MOVE_UP, args);

            try {
                animation.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            popupEngaged = true;
        }



        //doMoveGlassToRobot

        if(myGlassState == GlassState.MOVE_TO_TOP_ROBOT){
            args = new Object[1];
            args[0] = myTopRobotIndex;

            transducer.fireEvent(myMachineChannel, TEvent.WORKSTATION_DO_LOAD_GLASS, args);

            try {
                animation.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            topRobot.msgPopupHereIsGlass(currentGlass);
            robotTopGlassState = GlassState.PROCESSING;

        } else {

            args = new Object[1];
            args[0] = myBottomRobotIndex;

            transducer.fireEvent(myMachineChannel, TEvent.WORKSTATION_DO_LOAD_GLASS, args);

            try {
                animation.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            bottomRobot.msgPopupHereIsGlass(currentGlass);
            robotBottomGlassState = GlassState.PROCESSING;

        }


        currentGlass = null;
        myGlassState = GlassState.NONE;

    }

    private void moveMyGlassToConveyor() {
        print("Popup"+myPopupIndex+"Carrying out action : moveMyGlassToConveyor");
        Object[] args = new Object[1];
        args[0] = myPopupIndex;

        if(popupEngaged){

            transducer.fireEvent(TChannel.POPUP, TEvent.POPUP_DO_MOVE_DOWN, args);

            try {
                animation.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            popupEngaged = false;

        }
        transducer.fireEvent(TChannel.POPUP, TEvent.POPUP_RELEASE_GLASS, args);
        try {
            animation.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        exitConveyor.msgHereIsGlass(currentGlass);
        
        resetState();

    }

    private void readyMoveFromRobot(){
        print("Popup"+myPopupIndex+"Carrying out action : readyMoveFromRobot");


        if(!popupEngaged){
            Object[] args = new Object[1];
            args[0] = myPopupIndex;

            transducer.fireEvent(TChannel.POPUP, TEvent.POPUP_DO_MOVE_UP, args);

            try {
                animation.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            popupEngaged = true;
        }

        myGlassState = GlassState.WAITING;

        if(robotTopGlassState == GlassState.NEEDS_THROUGH){
            robotTopGlassState = GlassState.WAITING;
            topRobot.msgPopupReady();
            toldRobot = true;
        } else{
            robotBottomGlassState = GlassState.WAITING;
            bottomRobot.msgPopupReady();
            toldRobot = true;
        }
        try {
			animation.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void readyMoveFromConveyor(){
        print("Popup"+myPopupIndex+"Carrying out action : readyMoveFromSensor");

        if(popupEngaged){
            Object[] args = new Object[1];
            args[0] = myPopupIndex;

            transducer.fireEvent(TChannel.POPUP, TEvent.POPUP_DO_MOVE_DOWN, args);

            try {
                animation.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            popupEngaged = false;
        }

        if(conveyorGlassState == GlassState.NEEDS_ROBOT){
            conveyorGlassState = GlassState.WAITING_ROBOT;
        } else {
            conveyorGlassState = GlassState.WAITING_THROUGH;
        }

        myGlassState = GlassState.WAITING;

        entryConveyor.msgReadyToTakeGlass();
    	try{
    		animation.acquire();
    	} catch(Exception e){
    		e.printStackTrace();
    	}

    }


    /*

   Gui Stuff

    */

    public void eventFired(TChannel channel, TEvent event, Object[] args){

        if(channel == TChannel.POPUP){
            if(event == TEvent.POPUP_GUI_LOAD_FINISHED)  {
        		if((Integer) args[0] == this.myPopupIndex){
        			animation.release();
        			if(robotTopGlassState == GlassState.WAITING){
        				topRobot.finishTransfer();
        			}
        			else if(robotBottomGlassState == GlassState.WAITING){
        				bottomRobot.finishTransfer();
        			}
        		}

            } else if(event == TEvent.POPUP_GUI_RELEASE_FINISHED){

        		if((Integer) args[0] == this.myPopupIndex){
        			animation.release();
        		}
            } else if(event == TEvent.POPUP_GUI_MOVED_UP){

        		if((Integer) args[0] == this.myPopupIndex){
        			animation.release();
        			System.out.println("PopupRaised, Permit " + animation.availablePermits());
        		}
            } else if(event == TEvent.POPUP_GUI_MOVED_DOWN){

        		if((Integer) args[0] == this.myPopupIndex){
        			animation.release();
        		}
            }
        } 
        else if(channel == myMachineChannel){

            if(event == TEvent.WORKSTATION_LOAD_FINISHED){
            	
                animation.release();
                System.out.println("Permits: " + animation.availablePermits());

            }
//            else if (event == TEvent.WORKSTATION_RELEASE_FINISHED){
//            		animation.release();
//            	    System.out.println("Permits: " + animation.availablePermits());
//        		if(robotTopGlassState == GlassState.NEEDS_THROUGH){
//    				topRobot.finishTransfer();
//    			}
//    			else if(robotBottomGlassState == GlassState.NEEDS_THROUGH){
//    				bottomRobot.finishTransfer();
//    			}
//           }
            }
    }
    
    private boolean myStation(){
    	String myStation;
    	if(myPopupIndex == 0){
    		myStation = "DRILL";
    	}
    	else if(myPopupIndex == 1){
    		myStation = "CROSS_SEAMER";
    	}
    	else{
    		myStation = "GRINDER";
    	}
    	for(int i=0; i<currentGlass.getProcesses().size(); i++){
    		if(currentGlass.getProcesses().get(i) == myStation){
    			return true;
    		}
    	}
    	return false;
    }
    
    private void resetState(){
        //reset popup to initial state
        conveyorGlassState = GlassState.NONE;
        myGlassState = GlassState.NONE;
        robotTopGlassState = GlassState.NONE;
        robotBottomGlassState = GlassState.NONE;
        
        currentGlass = null;
        
        popupEngaged = false;
    }


}
