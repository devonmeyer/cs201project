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
	private enum GlassState{processing, processed, removing};
	
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
	private boolean isTop;
	public PopupRobotAgent(String type, int guiIndex, boolean isTop, PopupAgent popup, Transducer transducer){
		this.type = type;
		this.guiIndex = guiIndex;
		this.isTop = isTop;
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
	
	/**MESSAGES**/
	
	//popup messaging robot that glass is ready to be transferred
	public void msgPopupGlassIsReady() {
		pstate = PopupState.glassready;
		stateChanged();
	}
	//popup giving glass to robot
	public void msgPopupHereIsGlass(Glass g) {
		myglasses.add(new MyGlass(g));
		stateChanged();
	}
	//popup notifying robot that it is ready to receive glass
	public void msgPopupReady() {
		pstate = PopupState.popupready;
		stateChanged();
	}
	//message that animation is done processing glass
	public void msgGlassDoneProcessing(){
		myglasses.get(0).gstate = GlassState.processed;
		stateChanged();
	}
	//message that the animation is removing glass
	public void msgRemoveGlass(){
		myglasses.get(0).gstate = GlassState.removing;
	}

	/**SCHEDULER**/
	public boolean pickAndExecuteAnAction() {
		
		//if there is a glass that needs processing
		if(myglasses != null && myglasses.get(0).gstate == GlassState.processing){
			processGlass();
			return true;
		}
		//if there is a glass that is processed
		if(myglasses != null && myglasses.get(0).gstate == GlassState.processed){
			requestPopup();
			return true;
		}
		//if there is a glass that is processed and the popup is ready
		if(myglasses !=null && myglasses.get(0).gstate == GlassState.processed && pstate == PopupState.popupready){
			giveGlassToPopup();
			return true;
		}
		//if the robot is ready to receive glass
		if(pstate == PopupState.robotready){
			notifyPopupThatRobotIsReady();
			return true;
		}
		//if the robot is removing the glass
		if(myglasses != null && myglasses.get(0).gstate == GlassState.removing){
			removeGlass();
			return true;
		}
		
		return false;
	}

	/**ACTIONS**/
	
	//fires the animation to process the glass
	private void processGlass() {
		
		Object args[] = new Object[1];
		args[0] = this.guiIndex;
		if(type.equals("DRILL"))
			this.transducer.fireEvent(TChannel.DRILL, TEvent.WORKSTATION_DO_ACTION, args); 
		else if (type.equals("GRINDER"))
			this.transducer.fireEvent(TChannel.GRINDER, TEvent.WORKSTATION_DO_ACTION, args); 
		else if (type.equals("CROSS_SEAMER"))
			this.transducer.fireEvent(TChannel.CROSS_SEAMER, TEvent.WORKSTATION_DO_ACTION, args); 

		stateChanged();
	}
	//requests permission to transfer glass to popup
	private void requestPopup(){
		Popup.msgRobotGlassIsReady(this.isTop);
		pstate = PopupState.requested;
		stateChanged();
	}
	//fires animation to remove glass
	private void giveGlassToPopup(){
		Popup.msgRobotHereIsGlass(myglasses.get(0).glass, this.isTop);
		Object args[] = new Object[1];
		args[0] = this.guiIndex;
		if(type.equals("DRILL"))
			this.transducer.fireEvent(TChannel.DRILL, TEvent.WORKSTATION_RELEASE_GLASS, args); 
		else if (type.equals("CROSS_SEAMER"))
			this.transducer.fireEvent(TChannel.CROSS_SEAMER, TEvent.WORKSTATION_RELEASE_GLASS, args); 
		else if (type.equals("GRINDER"))
			this.transducer.fireEvent(TChannel.GRINDER, TEvent.WORKSTATION_RELEASE_GLASS, args); 
		stateChanged();
	}
	//notifies the popup that the robot is ready to take glass
	private void notifyPopupThatRobotIsReady(){
		Popup.msgRobotReady(this.isTop);
		pstate = PopupState.notified;
		stateChanged();	
	}
	//removes the glass from the robot
	private void removeGlass(){
		myglasses.remove(0);
		pstate = PopupState.robotready;
		stateChanged();
	}
	
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if(type.equals("DRILL"))
		{
			if(channel == TChannel.DRILL)
			{
				if(event == TEvent.WORKSTATION_GUI_ACTION_FINISHED)
					this.msgGlassDoneProcessing();
			}
			else if (event == TEvent.WORKSTATION_RELEASE_FINISHED){
				msgRemoveGlass();
			}
		}
		else if(type.equals("GRINDER"))
		{
			if(channel == TChannel.GRINDER)
			{
				if(event == TEvent.WORKSTATION_GUI_ACTION_FINISHED)
					this.msgGlassDoneProcessing();
			}else if (event == TEvent.WORKSTATION_RELEASE_FINISHED){
				msgRemoveGlass();
			}
		}
		else if(type.equals("CROSS_SEAMER"))
		{
			if(channel == TChannel.CROSS_SEAMER)
			{
				if(event == TEvent.WORKSTATION_GUI_ACTION_FINISHED)
					this.msgGlassDoneProcessing();
			}
			else if (event == TEvent.WORKSTATION_RELEASE_FINISHED){
				msgRemoveGlass();
			}
		}
		
	}

}