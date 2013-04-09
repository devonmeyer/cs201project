package glassLine.agents;

import transducer.TChannel;
import transducer.TEvent;
import engine.agent.Agent;
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
	
	/** Messages *******************/
	public void msgReadyForGlass(){
		// TODO
		stateChanged();
	}
	
	/** Scheduler *******************/
	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	/** Actions *********************/
	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// TODO Auto-generated method stub
		
	}

	
}
