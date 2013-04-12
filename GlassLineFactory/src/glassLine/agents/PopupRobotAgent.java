package glassLine.agents;

import java.util.ArrayList;
import java.util.List;

import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;
import glassLine.Glass;
import glassLine.interfaces.Robot;
import gui.panels.subcontrolpanels.TracePanel;

public class PopupRobotAgent extends Agent implements Robot{


	private List<MyGlass> glassList;
	private enum GlassState{processing, processed, removing, remove, none};

	public class MyGlass{
		private Glass glass;
		public GlassState gstate;

		public MyGlass(Glass g){
			glass = g;
			gstate = GlassState.processing;
		}
	}

	private PopupAgent Popup;
	private enum PopupState{none, glassready, popupready, requested, robotready, notified};
	private PopupState pstate;

	private String type;
	private int guiIndex;
	private boolean isTop;
	private enum RobotState{ready, busy, none};
	private RobotState rstate;

	public PopupRobotAgent(String type, int guiIndex, boolean isTop, PopupAgent popup, Transducer transducer, TracePanel tp){
		super(type);
		this.type = type;
		this.guiIndex = guiIndex;
		this.isTop = isTop;
		this.Popup = popup;
		this.transducer = transducer;
		this.tracePanel = tp;
		this.pstate = PopupState.none;
		this.rstate = RobotState.ready;
		this.glassList = new ArrayList<MyGlass>();

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
			print(e.getMessage());
		}
	}

	/**MESSAGES**/

	//popup messaging robot that glass is ready to be transferred
	public void msgPopupGlassIsReady() {
		print("PopupAgent " + this.type + "received msgPopupGlassIsReady from Popup " + this.Popup.getName() + "\n");
		System.out.println("PopupAgent " + this.type + "received msgPopupGlassIsReady from Popup " + this.Popup.getName() + "\n");

		pstate = PopupState.glassready;
		stateChanged();
	}
	//popup giving glass to robot
	public void msgPopupHereIsGlass(Glass g) {
		print("PopupAgent " + this.type + "received msgPopupHereIsGlass from Popup " + this.Popup.getName() + "\n");
		System.out.println("PopupAgent " + this.type + "received msgPopupHereIsGlass from Popup " + this.Popup.getName() + "\n");

		this.glassList.add(new MyGlass(g));
		rstate = RobotState.busy;
		stateChanged();
	}
	//popup notifying robot that it is ready to receive glass
	public void msgPopupReady() {
		print("PopupAgent " + this.type + "received msgPopupReady from Popup " + this.Popup.getName() + "\n");
		System.out.println("PopupAgent " + this.type + "received msgPopupReady from Popup " + this.Popup.getName() + "\n");
		pstate = PopupState.popupready;
		stateChanged();
	}
	//message that animation is done processing glass
	public void msgGlassDoneProcessing(){
		print("PopupAgent " + this.type + "received msgGlassDoneProcessing after animation is done processing \n");
		System.out.println("PopupAgent " + this.type + "received msgGlassDoneProcessing after animation is done processing \n");

		this.glassList.get(0).gstate = GlassState.processed;
		System.out.println("CHECK");
		stateChanged();
	}
	//message that the animation is removing glass
	public void msgRemoveGlass(){
		print("PopupAgent " + this.type + "received msgRemoveGlass after animation starts removing \n");
		System.out.println("PopupAgent " + this.type + "received msgRemoveGlass after animation starts removing \n");
		this.glassList.get(0).gstate = GlassState.removing;
		stateChanged();

	}

	/**SCHEDULER**/
	public boolean pickAndExecuteAnAction() {
		//if the robot is ready to receive glass
		if(pstate == PopupState.glassready && rstate == RobotState.ready){
			notifyPopupThatRobotIsReady();
			return true;
		}
		//if there is a glass that needs processing
		if(!glassList.isEmpty()){
			if(this.glassList.get(0).gstate == GlassState.processing){
				processGlass();
				return true;
			}
			if(this.glassList.get(0).gstate ==  GlassState.processed && pstate != PopupState.popupready && pstate != PopupState.requested){
				requestPopup();
				return true;
			}
			//if there is a glass that is processed and the popup is ready
			if(this.glassList.get(0).gstate == GlassState.processed && pstate == PopupState.popupready && pstate != PopupState.requested){
				giveGlassToPopup();
				return true;
			}
			//if the robot is removing the glass
			if(this.glassList.get(0).gstate == GlassState.removing && pstate == PopupState.popupready){
				removeGlass();
				return true;
			}
		}




		return false;
	}

	/**ACTIONS**/

	//fires the animation to process the glass
	private void processGlass() {
		print("PopupAgent " + this.type + "action: processGlass \n");
		System.out.println("PopupAgent " + this.type + "action: processGlass \n");
		this.glassList.get(0).gstate = GlassState.none;

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
		print("PopupAgent " + this.type + "action: requestPopup to popup " + this.Popup.getName() + "\n");
		System.out.println("PopupAgent " + this.type + "action: requestPopup to popup " + this.Popup.getName() + "\n");
		Popup.msgRobotGlassIsReady(this.isTop);
		pstate = PopupState.requested;

	}
	//fires animation to remove glass
	private void giveGlassToPopup(){
		print("PopupAgent " + this.type + "action: giveGlassToPopup to popup " + this.Popup.getName() + "\n");
		System.out.println("PopupAgent " + this.type + "action: giveGlassToPopup to popup " + this.Popup.getName() + "\n");
		Popup.msgRobotHereIsGlass(glassList.get(0).glass, this.isTop);
		this.glassList.get(0).gstate = GlassState.remove;
		Object args[] = new Object[1];
		args[0] = this.guiIndex;
		if(type.equals("DRILL"))
			this.transducer.fireEvent(TChannel.DRILL, TEvent.WORKSTATION_RELEASE_GLASS, args); 
		else if (type.equals("CROSS_SEAMER"))
			this.transducer.fireEvent(TChannel.CROSS_SEAMER, TEvent.WORKSTATION_RELEASE_GLASS, args); 
		else if (type.equals("GRINDER"))
			this.transducer.fireEvent(TChannel.GRINDER, TEvent.WORKSTATION_RELEASE_GLASS, args); 

	}

	//notifies the popup that the robot is ready to take glass
	private void notifyPopupThatRobotIsReady(){
		print("PopupAgent " + this.type + "action: notifyPopupThatRobotIsReady to popup " + this.Popup.getName() + "\n");
		System.out.println("PopupAgent " + this.type + "action: notifyPopupThatRobotIsReady to popup " + this.Popup.getName() + "\n");
		Popup.msgRobotReady(this.isTop);
		pstate = PopupState.notified;

	}
	//removes the glass from the robot
	private void removeGlass(){
		print("PopupAgent " + this.type + "action: removeGlass \n");
		System.out.println("PopupAgent " + this.type + "action: removeGlass \n");
		glassList.remove(0);
		pstate = PopupState.none;
		rstate = RobotState.ready;

	}

	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if(type.equals("DRILL"))
		{
			if(channel == TChannel.DRILL)
			{
				if(event == TEvent.WORKSTATION_GUI_ACTION_FINISHED){
					if((Integer) args[0] == this.guiIndex){
						this.msgGlassDoneProcessing();
					}
				}
				else if (event == TEvent.WORKSTATION_RELEASE_FINISHED){
					if((Integer) args[0] == this.guiIndex){
						this.msgRemoveGlass();
					}
				}
			}

		}
		else if(type.equals("GRINDER"))
		{
			if(channel == TChannel.GRINDER)
			{
				if(event == TEvent.WORKSTATION_GUI_ACTION_FINISHED){
					if((Integer) args[0] == this.guiIndex){
						this.msgGlassDoneProcessing();
					}
				}else if (event == TEvent.WORKSTATION_RELEASE_FINISHED){
					if((Integer) args[0] == this.guiIndex){
						this.msgRemoveGlass();
					}
				}
			}
		}
		else if(type.equals("CROSS_SEAMER"))
		{
			if(channel == TChannel.CROSS_SEAMER)
			{
				if(event == TEvent.WORKSTATION_GUI_ACTION_FINISHED){
					if((Integer) args[0] == this.guiIndex){
						this.msgGlassDoneProcessing();
					}
				}
				else if (event == TEvent.WORKSTATION_RELEASE_FINISHED){
					if((Integer) args[0] == this.guiIndex){
						this.msgRemoveGlass();
					}
				}
			}

		}

	}

}
