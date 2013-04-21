package glassLine.agents;

import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;
import glassLine.*;
import glassLine.interfaces.Machine;
import gui.panels.subcontrolpanels.*;

import java.util.*;
import java.util.concurrent.Semaphore;

public class GlassRobotAgent extends Agent implements Machine{
	
	/** DATA *******************/
	/*
	 * -List of glasses
	 * -The GlassSelectedPanel where a glass is created
	 * -Connection to the entry ConveyorFamily
	 */
	List<Glass> glasses;
	GlassSelectPanel parent;
	ConveyorAgent entrance;
	enum AgentState {Ready, NotReady};
	enum ConveyorState {None, Ready, Requested, Sending};
	ConveyorState conveyorState;
	AgentState state;
	private Semaphore coolDown = new Semaphore(0,true);
	/** Constructor ****/
	public GlassRobotAgent(Transducer trans, TracePanel tp, String name){
		
		super(name);
		
		this.glasses = new ArrayList<Glass>();
		this.conveyorState = ConveyorState.None;
		this.state = AgentState.Ready;
		transducer = trans;
		tracePanel = tp;
		transducer.register(this, TChannel.BIN);
		transducer.register(this, TChannel.SENSOR);
		
	}
	
	/** Messages *******************/
	
	@Override
	public void msgReadyToTakeGlass() {
		print("Conveyor " + entrance.getConveyorIndex() +" is ready to take glass\n");
		System.out.println("Conveyor " + entrance.getConveyorIndex() +" is ready to take glass\n");
		conveyorState = ConveyorState.Ready;
		stateChanged();
	}
	
	/** Scheduler *******************/
	@Override
	public boolean pickAndExecuteAnAction() {
		if(!glasses.isEmpty()){
			if(conveyorState == ConveyorState.None){
				if(this.state == AgentState.Ready)
				requestSend();
				return true;
			}
			else if(conveyorState == ConveyorState.Ready){
				sendGlassToEntrance();
				return true;
			}
		}
	
		return false;
	}

	/** Actions *********************/
	
	private void requestSend(){
		System.out.println("Reqesting to send.");
		entrance.msgGlassIsReady();
		conveyorState = ConveyorState.Requested;
		this.state = AgentState.NotReady;
	
	}
	/*
	 * Send the first glass in the list to the entrance conveyor 
	 */
	private void sendGlassToEntrance(){
		print("sending glass to conveyor " + entrance.getConveyorIndex() +"\n");
		System.out.println("sending glass to conveyor " + entrance.getConveyorIndex() +"\n");
		
		
		conveyorState = ConveyorState.Sending;						//set entrance to not ready after sending glass
		this.state = AgentState.NotReady;
		transducer.fireEvent(TChannel.BIN, TEvent.BIN_CREATE_PART, null);
		
	}
	public void addGlass(Glass g){
		System.out.println("Glass added");
		print("A new piece of glass is made\n");
		glasses.add(g);
		System.out.println("total : " + glasses.size());
		
		if(glasses.size() == 1)
			stateChanged();
	}
	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if(channel == TChannel.BIN){
			if(event == TEvent.BIN_PART_CREATED){
			
					
					this.state = AgentState.Ready;
					this.conveyorState = ConveyorState.None;
					entrance.msgHereIsGlass(glasses.get(0));
					glasses.remove(0);		// remove the glass thats sent to the conveyor
					
					System.out.println("Glass removed");
					
				
			}
		}
		stateChanged();
	}

	/** Setters/Getters **/
	public void setParent(GlassSelectPanel gsp){
		parent = gsp;
	}
	public void setConveyor(ConveyorAgent c){
		entrance = c;
	}

	@Override
	public void msgGlassIsReady() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGlassNeedsThrough() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsGlass(Glass g) {
		// TODO Auto-generated method stub
		
	}
	
}
