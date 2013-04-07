package engine.agent;

import java.util.List;


import glassLine.Glass;
import gui.drivers.FactoryDriver;
import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;

public class OnlineMachineAgent extends Agent{

	/**
	 * This class represents an online machine.
	 * Type : BREAKOUT, MANUAL_BREAKOUT, CUTTER,
	 * WASHER, UV_LAMP, OVEN, PAINTER
	 */

	private String type;
	private FactoryDriver factory;

	private int guiIndex;  // for communication with GUI through transducer
	private int capacity;
	private List<MyGlass> glassList; 
	
	private enum PreviousAgentState {none, requestingToSend, sendPending, sending};
	private enum FollowingAgentState {none, requestSent, receptionPending, readyToReceive, occupied, readyToReturn};
	private enum GlassState {none, needsProcessing, notNeedProcessing, doneProcessing}
	private PreviousAgentState previousAgentState;
	private FollowingAgentState followingAgentState;
	
	
	private class MyGlass {
		private Glass g;
		private GlassState state;
		
		public MyGlass(Glass g){
			this.state = GlassState.none;
		}
	}
	
	public OnlineMachineAgent(String type, int guiIndex, int capacity, FactoryDriver factory, Transducer transducer){
		this.type = type;
		this.guiIndex = guiIndex;
		this.capacity = capacity;
		this.factory = factory;
		
		this.previousAgentState = PreviousAgentState.none;
		this.followingAgentState = FollowingAgentState.none;
	}

	/** MESSAGES **/

	/** This message is sent by the ConveyorAgent transferring a piece of glass. 
	 * @params : int id, List<String> processes (list of processed to be performed)
	 **/
	
	public void msgHereIsGlass(int id, List<String> processes) {
		glassList.add(new MyGlass(new Glass(id, processes)));
		this.previousAgentState = PreviousAgentState.none;
		
		stateChanged();
	}

	public void msgGlassTransferRequest(){
		this.previousAgentState = PreviousAgentState.requestingToSend;
		
		stateChanged();
	}


	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// TODO Auto-generated method stub

	}

}
