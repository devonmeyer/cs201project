package glassLine.agents;

import java.util.ArrayList;
import java.util.List;

import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;
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
	
	private enum PopupState{none, glassready, popupready, requested, robotready, notified};
	private PopupState pstate = PopupState.none;
	
	private String type;
	private int guiIndex;
	public PopupRobotAgent(String type, int guiIndex, PopupAgent popup, Transducer transducer){
		this.type = type;
		this.guiIndex = guiIndex;
		this.Popup = popup;
		this.transducer = transducer;
		
		try{
			if(type.equals("DRILL"))
				this.transducer.register(this, TChannel.DRILL);
			else if (type.equals("GRINDER"))
				this.transducer.register(this, TChannel.GRINDER);
			else if (type.equals("CROSS_SEAMER"))
				this.transducer.register(this, TChannel.CROSS_SEAMER);
			else
				throw new Exception("Invalid Machine Type");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	//MESSAGES
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
		
		if(myglasses != null && myglasses.get(0).gstate == GlassState.processing){
			processGlass();
			return true;
		}
		
		if(myglasses != null && myglasses.get(0).gstate == GlassState.processed){
			requestPopup();
			return true;
		}
		
		if(myglasses !=null && myglasses.get(0).gstate == GlassState.processed && pstate == PopupState.popupready){
			giveGlassToPopup();
			return true;
		}
		
		if(pstate == PopupState.robotready){
			notifyPopupThatRobotIsReady();
			return true;
		}
		
		return false;
	}

	private void processGlass() {
		
		Object args[] = new Object[1];
		args[0] = this.guiIndex;
		if(type.equals("DRILL"))
			this.transducer.fireEvent(TChannel.DRILL, TEvent.WORKSTATION_DO_ACTION, args); 
		else if (type.equals("GRINDER"))
			this.transducer.fireEvent(TChannel.GRINDER, TEvent.WORKSTATION_DO_ACTION, args); 
		else if (type.equals("CROSS_SEAMER"))
			this.transducer.fireEvent(TChannel.CROSS_SEAMER, TEvent.WORKSTATION_DO_ACTION, args); 
	
		myglasses.get(0).gstate = GlassState.processed;
		stateChanged();
	}
	
	private void requestPopup(){
		Popup.msgRobotGlassIsReady();
		pstate = PopupState.requested;
		stateChanged();
	}
	
	private void giveGlassToPopup(){
		Popup.msgRobotHereIsGlass(myglasses.get(0).glass);
		myglasses.remove(0);
		pstate = PopupState.robotready;
		stateChanged();
	}
	
	private void notifyPopupThatRobotIsReady(){
		Popup.msgRobotReady();
		pstate = PopupState.notified;
		stateChanged();	
	}

	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// TODO Auto-generated method stub
		
	}

}
