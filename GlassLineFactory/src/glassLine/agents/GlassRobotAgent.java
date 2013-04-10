package glassLine.agents;

import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;
import glassLine.*;
import gui.panels.subcontrolpanels.GlassSelectPanel;

import java.util.*;

public class GlassRobotAgent extends Agent{
	
	/** DATA *******************/
	/*
	 * -List of glasses
	 * -The GlassSelectedPanel where a glass is created
	 * -Connection to the entry ConveyorFamily
	 */
	List<Glass> glasses;
	GlassSelectPanel parent;
	//ConveyorFamily entrance;
	
	boolean entranceReady;
	
	/** Constructor ****/
	public GlassRobotAgent(Transducer trans, String name){
		
		super(name);
		
		this.glasses = new ArrayList<Glass>();
		this.entranceReady = false;
		
		transducer = trans;
//		transducer.register(this, TChannel.BIN);
		
	}
	
	/** Messages *******************/
	public void msgReadyForGlass(){
		entranceReady = true;
		stateChanged();
	}
	
	/** Scheduler *******************/
	@Override
	public boolean pickAndExecuteAnAction() {
		if(!glasses.isEmpty()){
			if(entranceReady){
				sendGlassToEntrance();
				return true;
			}
		}
		
		return false;
	}

	/** Actions *********************/
	
	/*
	 * Send the first glass in the list to the entrance conveyor 
	 */
	private void sendGlassToEntrance(){
		//entrance.msgHereIsGlass(glasses.get(0));
		glasses.remove(0);		// remove the glass thats sent to the conveyor
		entranceReady = false;						//set entrance to not ready after sending glass
		eventFired(TChannel.BIN, TEvent.BIN_CREATE_PART, null);
		stateChanged();
	}
	public void addGlass(Glass g){
		System.out.println("Making Glass");
		glasses.add(g);
		stateChanged();
	}
	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		transducer.fireEvent(channel, event, null);
		stateChanged();
	}

	/** Setters/Getters **/
	public void setParent(GlassSelectPanel gsp){
		parent = gsp;
	}
	
}
