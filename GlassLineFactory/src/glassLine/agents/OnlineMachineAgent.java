package glassLine.agents;

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
	
	private enum PrecedingAgentState {none, requestingToSend, sendPending, sending};
	private enum FollowingAgentState {none, requestSent, receptionPending, readyToReceive};
	private enum GlassState {none, needsProcessing, doneProcessing}
	private PrecedingAgentState precedingAgentState;
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
		this.transducer = transducer;
		
		// Registering to the appropriate transducer channel
		try{
		if(type.equals("BREAKOUT"))
			this.transducer.register(this, TChannel.BREAKOUT);
		else if (type.equals("MANUAL_BREAKOUT"))
			this.transducer.register(this, TChannel.MANUAL_BREAKOUT);
		else if (type.equals("CUTTER"))
			this.transducer.register(this, TChannel.CUTTER);
		else if (type.equals("WASHER"))
			this.transducer.register(this, TChannel.WASHER);
		else if (type.equals("UV_LAMP"))
			this.transducer.register(this, TChannel.UV_LAMP);
		else if (type.equals("OVEN"))
			this.transducer.register(this, TChannel.OVEN);
		else if (type.equals("PAINTER"))
			this.transducer.register(this, TChannel.PAINTER);
		else
			throw new Exception("Invalid Machine Type");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		this.precedingAgentState = PrecedingAgentState.none;
		this.followingAgentState = FollowingAgentState.none;
		
	}

	/** MESSAGES **/

	/** This message is sent by the preceding ConveyorAgent or by a RobotAgent transferring a piece of glass. 
	 * @params : Glass g (instance of glass)
	 **/
	
	public void msgHereIsGlass(Glass g, boolean needsProcessing) {
		print("Receiving new piece of glass.");
		glassList.add(new MyGlass(g));
		if(needsProcessing)
			glassList.get(0).state = GlassState.needsProcessing;
		else 
			glassList.get(0).state = GlassState.doneProcessing;
		this.precedingAgentState = PrecedingAgentState.none;
		
		stateChanged();
	}
	
	/** This message is sent by the preceding ConveyorAgent or by a RobotAgent requesting to transfer a piece of glass. 
	 * @params : Glass g (instance of glass)
	 **/

	public void msgGlassTransferRequest(){
		print("Received a glass transfer request.");
		this.precedingAgentState = PrecedingAgentState.requestingToSend;
		
		stateChanged();
	}
	
	/** This message is sent by the following ConveyorAgent or by a RobotAgent requesting to transfer a piece of glass. 
	 * @params : Glass g (instance of glass)
	 **/

	public void msgReadyForGlass(){
		print("Received a confirmation that recipient is ready for glass transfer.");
		this.followingAgentState = FollowingAgentState.readyToReceive;
		
		stateChanged();
	}

	/** SCHEDULER **/

	@Override
	public boolean pickAndExecuteAnAction() {
		
		return false;
	}
	
	
	/** ACTIONS **/

	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// TODO Auto-generated method stub

	}

}
