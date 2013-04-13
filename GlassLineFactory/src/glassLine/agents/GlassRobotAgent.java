package glassLine.agents;

import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;
import glassLine.*;
import glassLine.interfaces.Machine;
import gui.panels.subcontrolpanels.*;

import java.util.*;

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
	enum ConveyorState {Ready, Requested, NotReady};
	ConveyorState conveyorState;
	AgentState state;
	
	/** Constructor ****/
	public GlassRobotAgent(Transducer trans, TracePanel tp, String name){
		
		super(name);
		
		this.glasses = new ArrayList<Glass>();
		this.conveyorState = ConveyorState.NotReady;
		this.state = AgentState.Ready;
		transducer = trans;
		tracePanel = tp;
		transducer.register(this, TChannel.BIN);
		transducer.register(this, TChannel.SENSOR);
		
	}
	
	/** Messages *******************/
	public void msgReadyForGlass(){
		
		stateChanged();
	}
	
	@Override
	public void msgReadyToTakeGlass() {
		print("Conveyor " + entrance.getConveyorIndex() +" is ready to take glass\n");
		conveyorState = ConveyorState.Ready;
		stateChanged();
	}
	
	/** Scheduler *******************/
	@Override
	public boolean pickAndExecuteAnAction() {
		if(!glasses.isEmpty() && this.state == AgentState.Ready){
			if(conveyorState == ConveyorState.NotReady){
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
		entrance.msgGlassIsReady();
		conveyorState = ConveyorState.Requested;
		stateChanged();
	}
	/*
	 * Send the first glass in the list to the entrance conveyor 
	 */
	private void sendGlassToEntrance(){
		print("sending glass to conveyor " + entrance.getConveyorIndex() +"\n");
		entrance.msgHereIsGlass(glasses.get(0));
		
		conveyorState = ConveyorState.NotReady;						//set entrance to not ready after sending glass
		this.state = AgentState.NotReady;
		transducer.fireEvent(TChannel.BIN, TEvent.BIN_CREATE_PART, null);
		stateChanged();
	}
	public void addGlass(Glass g){
//		System.out.println("Making Glass");
		print("A new piece of glass is made\n");
		glasses.add(g);
		this.state = AgentState.Ready;
		if(glasses.size() > 1){
			this.state = AgentState.NotReady;
		}
		stateChanged();
	}
	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if(channel == TChannel.SENSOR){
			if(event == TEvent.SENSOR_GUI_PRESSED){
				if((Integer)args[0] == 0){
					this.state = AgentState.Ready;

					glasses.remove(0);		// remove the glass thats sent to the conveyor
					
					
				}
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
