package glassLine.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import glassLine.Glass;
import glassLine.interfaces.Machine;
import gui.panels.subcontrolpanels.TracePanel;
import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;

public class TruckAgent extends Agent implements Machine {

	private List<Glass> glassList;
	private ConveyorAgent conveyor;

	private enum ConveyorState {
		none, requestingToSend, sending
	};

	private ConveyorState cstate;

	private enum TruckState {
		loading, emptying, loaded, none
	};

	private TruckState tstate;
	private boolean outofgas;
	private Semaphore waitLoadAnimation = new Semaphore(0, true);
	private Semaphore waitEmptyAnimation = new Semaphore(0, true);

	public TruckAgent(String name, Transducer transducer,
			ConveyorAgent conveyor, TracePanel tp) {
		super(name);
		this.transducer = transducer;
		this.conveyor = conveyor;
		this.tstate = TruckState.none;
		transducer.register(this, TChannel.TRUCK);
		tracePanel = tp;
		outofgas = false;
		this.glassList = new ArrayList<Glass>();
	}

	/** MESSAGES **/

	public void msgToggleTruckOutOfGas(){
		if(!outofgas)
			outofgas=true;
		else{
			outofgas=false;
			stateChanged();
		}
		
	}
	public void msgGlassIsReady() {
		print("Truck has received msgGlassIsReady from Conveyor"
				+ this.conveyor.getName() + "\n");
		this.cstate = ConveyorState.requestingToSend;
		stateChanged();
	}

	public void msgGlassNeedsThrough() {
		print("Truck has received msgGlassIsReady from Conveyor"
				+ this.conveyor.getName() + "\n");
		this.cstate = ConveyorState.requestingToSend;
		stateChanged();
	}

	public void msgHereIsGlass(Glass g) {
		print("Truck has received msgHereIsGlass from Conveyor"
				+ this.conveyor.getName() + "\n");
		this.glassList.add(g);
		this.tstate = TruckState.loading;
		this.cstate = ConveyorState.none;
		this.transducer.fireEvent(TChannel.TRUCK, TEvent.TRUCK_DO_LOAD_GLASS,
				null);
		try {
			this.waitLoadAnimation.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		stateChanged();
	}

	private void msgDoneEmptying() {
		print("Truck has received msgDoneEmptying after animation is done emptying the glass \n");
		this.tstate = TruckState.none;
		this.glassList.remove(0);

		stateChanged();
	}

	private void msgDoneLoading() {
		print("Truck has received msgDoneLoading after animation is done loading \n");
		this.tstate = TruckState.loaded;

		stateChanged();
	}

	public void msgReadyToTakeGlass() {

	}

	/** SCHEDULER **/
	public boolean pickAndExecuteAnAction() {

		if (cstate == ConveyorState.requestingToSend) {
			checkIfReadyToReceiveGlass();
			return true;
		}
		if (!this.glassList.isEmpty() && tstate == TruckState.loaded) {
			processGlass();
			return true;
		}

		return false;
	}

	/** ACTIONS **/

	private void checkIfReadyToReceiveGlass() {
		print("Truck action: receiveGlass from conveyor " + conveyor.getName()
				+ "\n");
		if (glassList.isEmpty()) {
			cstate = ConveyorState.sending;
			conveyor.msgReadyToTakeGlass();
		} else {
			cstate = ConveyorState.requestingToSend;
		}
		// stateChanged();
	}

	private void processGlass() {
		if(outofgas){
			return;
		}
		
		print("Truck action: processGlass \n");
		this.tstate = TruckState.emptying;
		this.transducer.fireEvent(TChannel.TRUCK, TEvent.TRUCK_DO_EMPTY, null);
		try {
			this.waitEmptyAnimation.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// stateChanged();
	}

	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if(outofgas){
			return;
		}
		
		if (channel == TChannel.TRUCK) {
			if (event == TEvent.TRUCK_GUI_LOAD_FINISHED) {
				this.waitLoadAnimation.release();
				this.msgDoneLoading();
			} else if (event == TEvent.TRUCK_GUI_EMPTY_FINISHED) {
				this.waitEmptyAnimation.release();
				this.msgDoneEmptying();
			}
		}

	}

}
