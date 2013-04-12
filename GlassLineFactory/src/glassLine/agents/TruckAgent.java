package glassLine.agents;

import glassLine.Glass;
import glassLine.interfaces.Machine;
import gui.panels.subcontrolpanels.TracePanel;
import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;

public class TruckAgent extends Agent implements Machine {

	private Glass glass;
	
	private ConveyorAgent conveyor;
	private enum ConveyorState{ready, transferred, permission};
	private ConveyorState cstate;
	
	private enum TruckState{loading, emptying, loaded, none};
	private TruckState tstate;
	
	public TruckAgent(String name, Transducer transducer, ConveyorAgent conveyor, TracePanel tp){
		super(name);
		this.transducer = transducer;
		this.conveyor = conveyor;
		this.tstate = TruckState.none;
		transducer.register(this, TChannel.TRUCK);
		tracePanel = tp;
	}
	
	/**MESSAGES**/
	
	public void msgGlassIsReady(){
		print("Truck has received msgGlassIsReady from Conveyor" + this.conveyor.getName() + "\n");
		this.cstate = ConveyorState.ready;
		stateChanged();
	}
	
	public void msgHereIsGlass(Glass g){
		print("Truck has received msgHereIsGlass from Conveyor" + this.conveyor.getName() + "\n");
		this.glass = g;
		this.transducer.fireEvent(TChannel.TRUCK, TEvent.TRUCK_DO_LOAD_GLASS, null); 
		this.tstate = TruckState.loading;
		this.cstate = ConveyorState.transferred;
		stateChanged();
	}
	
	private void msgDoneEmptying() {
		print("Truck has received msgDoneEmptying after animation is done emptying the glass \n");
		this.tstate = TruckState.none;
		this.glass = null;
		stateChanged();		
	}

	private void msgDoneLoading() {
		print("Truck has received msgDoneLoading after animation is done loading \n");
		this.tstate = TruckState.loaded;
		stateChanged();
	}

	public void msgGlassNeedsThrough() {
	
	}

	public void msgReadyToTakeGlass() {
		
	}
	
	/**SCHEDULER**/
	public boolean pickAndExecuteAnAction() {
		
		if(glass == null && cstate == ConveyorState.ready){
			receiveGlass();
			return true;
		}
		if(glass != null && cstate == ConveyorState.transferred && tstate == TruckState.loaded){
			processGlass();
		}
		
		return false;
	}

	/**ACTIONS**/
	private void receiveGlass(){
		print("Truck action: receiveGlass from conveyor " +conveyor.getName() + "\n");
		conveyor.msgReadyToTakeGlass();
		cstate = ConveyorState.permission;
		stateChanged();
	}
	
	private void processGlass(){
		print("Truck action: processGlass \n");
		this.transducer.fireEvent(TChannel.TRUCK, TEvent.TRUCK_DO_EMPTY, null); 
		this.tstate = TruckState.emptying;
		stateChanged();
	}
	
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if(channel == TChannel.TRUCK)
		{
			if(event == TEvent.TRUCK_GUI_LOAD_FINISHED)
				this.msgDoneLoading();
		}
		else if (event == TEvent.TRUCK_GUI_EMPTY_FINISHED){
				this.msgDoneEmptying();
		}
		
	}

}
