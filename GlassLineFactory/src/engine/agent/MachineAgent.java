package engine.agent;

import java.util.List;

import glassLine.Glass;
import gui.drivers.FactoryDriver;
import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;

public class MachineAgent extends Agent{

	/**
	 * This class represents an online machine.
	 * Type : BREAKOUT, MANUAL_BREAKOUT, CUTTER, CROSS_SEAMER, DRILL,
	 * GRINDER, WASHER, UV_LAMP, OVEN, PAINTER
	 */
	
	private String type;
	private FactoryDriver factory;

	private int guiIndex;  // for communication with GUI through transducer
	private int capacity;
	
	private List<Glass> glassList;
	
	public MachineAgent(String type, int guiIndex, int capacity, FactoryDriver factory, Transducer transducer){
		this.type = type;
		this.guiIndex = guiIndex;
		this.factory = factory;
		this.transducer = transducer;
		
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
