package glassLine.agents;

import java.util.ArrayList;
import java.util.List;

import transducer.TChannel;
import transducer.TEvent;
import glassLine.Glass;
import glassLine.interfaces.Robot;

public class PopupRobotAgent extends Agent implements Robot{
	
	private List<MyGlass> myglasses = new ArrayList<MyGlass>();
	
	private enum GlassState{processing, processed};
	
	public class MyGlass{
		private Glass glass;
		private GlassState gstate;
		
		public MyGlass(Glass glass){
			this.glass = glass;
			this.gstate = GlassState.processing;
		}
	}
	
	private PopupAgent Popup;
	
	private enum PopupState{up, down, glassready, popupready};
	private PopupState pstate;
	
	public void msgPopupGlassIsReady() {
		pstate = PopupState.glassready;
		stateChanged();
	}

	public void msgPopupHereIsGlass(Glass g) {
		myglasses.add(new MyGlass(g));
		stateChanged();
	}

	public void msgPopupReady() {
		pstate = PopupState.popupready;
		stateChanged();
	}

	public boolean pickAndExecuteAnAction() {
		
		
		return false;
	}

	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// TODO Auto-generated method stub
		
	}

}
