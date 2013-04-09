package glassLine.agents;

import transducer.TChannel;
import transducer.TEvent;
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
	public GlassRobotAgent(){
		
		glasses = new ArrayList<Glass>();
		entranceReady = false;
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
		entranceReady = false;						//set entrance to not ready after sending glass
		stateChanged();
	}
	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// TODO Auto-generated method stub
		
	}

	/** Setters/Getters **/
	public void setParent(GlassSelectPanel gsp){
		parent = gsp;
	}
	
	public void addGlass(Glass g){
		glasses.add(g);
	}
}
