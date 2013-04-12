package glassLine.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import transducer.TChannel;
import transducer.TEvent;
import transducer.Transducer;
import glassLine.Glass;
import glassLine.interfaces.Robot;
import gui.panels.subcontrolpanels.TracePanel;

public class PopupRobotAgent extends Agent implements Robot{
	
	private MyGlass myglass;
	private Glass temp;
	private Timer timer;
	public enum GlassState{none,needProcessing, processing, processed, removing};
	
	private Semaphore sem;
	public Semaphore animation;

	public class MyGlass{
		private Glass glass;
		public GlassState gstate;
		
		public MyGlass(){
//			this.glass = glass;
			gstate = GlassState.none;
		}
		
		public void setGlass(Glass g){
			this.glass = g;
		}
		public void setState(GlassState s){
			gstate = s;
		}
	}

	private PopupAgent Popup;
	public enum PopupState{none, glassready, popupready, requested, robotready, notified};
	private PopupState pstate;

	private String type;
	private int guiIndex;
	private boolean isTop;
	public enum RobotState{ready, busy, none};
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
		this.myglass = new MyGlass();
		this.timer = new Timer();
		
		sem = new Semaphore(0,true);
		animation = new Semaphore(0,true);
	
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
		print("PopupRobot " + this.type + "received msgPopupGlassIsReady from Popup " + this.Popup.getName() + "\n");
	
		pstate = PopupState.glassready;
		stateChanged();
	}
	
	@Override
	public void msgPopupHereIsGlass(Glass g) {
		print("PopupRobot " + this.type + " received glass from Popup" + this.Popup.getName() + "\n");
		myglass.setGlass(g);
		myglass.setState(GlassState.needProcessing);
		stateChanged();
	}

	//popup notifying robot that it is ready to receive glass
	public void msgPopupReady() {
		sem.release();
		print("PopupRobot " + this.type + "received msgPopupReady from Popup " + this.Popup.getName() + "\n");
		pstate = PopupState.popupready;
		stateChanged();
	}
	//message that animation is done processing glass
//	public void msgGlassDoneProcessing(){
//		print("PopupRobot " + this.type + "received msgGlassDoneProcessing after animation is done processing \n");
//		myglass.gstate = GlassState.processed;
//		//stateChanged();
//	}

	//message that the animation is removing glass
	public void msgRemoveGlass(){
		print("PopupRobot " + this.type + "received msgRemoveGlass after animation starts removing \n");
		myglass.gstate = GlassState.removing;
		
		print("PopupAgent " + this.type + "received msgRemoveGlass after animation starts removing \n");
		System.out.println("PopupAgent " + this.type + "received msgRemoveGlass after animation starts removing \n");
		stateChanged();

	}

	/**SCHEDULER**/
	public boolean pickAndExecuteAnAction() {
		
		//if the robot is ready to receive glass
		if(pstate == PopupState.glassready && rstate == RobotState.ready){

			notifyPopupThatRobotIsReady();
			return true;
		}
		
		if(myglass.gstate != GlassState.none){
				
			if(myglass.gstate == GlassState.needProcessing){
	
				processGlass();
				return true;
			}
			if(pstate == PopupState.popupready && myglass.gstate == GlassState.removing){
				giveGlassToPopup();
				return true;
			}
	
		//if there is a glass that is processed and the popup is ready
			if(myglass.gstate == GlassState.processed){	
				requestPopup();
				return true;
			}
		}
			//if the robot is removing the glass
//				if(myglass.gstate == GlassState.removing){
//					try {
//						sem.acquire();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					removeGlass();
//					return true;
//				}
		
		//if there is a glass that needs processing

		return false;
	}

	/**ACTIONS**/

	//fires the animation to process the glass
	private void processGlass() {
		print("PopupRobot " + this.type + "action: processGlass \n");
		System.out.println("PopupAgent " + this.type + "action: processGlass \n");

		Object args[] = new Object[1];
		args[0] = this.guiIndex;
		myglass.gstate = GlassState.processing;
		if(type.equals("DRILL")){
			this.transducer.fireEvent(TChannel.DRILL, TEvent.WORKSTATION_DO_ACTION, args); 
			try {
	            animation.acquire();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		}
		else if (type.equals("GRINDER")){
			this.transducer.fireEvent(TChannel.GRINDER, TEvent.WORKSTATION_DO_ACTION, args); 
			try {
	            animation.acquire();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		}
		else if (type.equals("CROSS_SEAMER")){
			this.transducer.fireEvent(TChannel.CROSS_SEAMER, TEvent.WORKSTATION_DO_ACTION, args); 
			try {
	            animation.acquire();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		}

//		stateChanged();

	}
	//requests permission to transfer glass to popup
	private void requestPopup(){
		print("PopupRobot " + this.type + "action: requestPopup to popup " + this.Popup.getName() + "\n");
		System.out.println("PopupAgent " + this.type + "action: requestPopup to popup " + this.Popup.getName() + "\n");
		Popup.msgRobotGlassIsReady(this.isTop);
//		pstate = PopupState.requested;
		rstate = RobotState.none;
		myglass.gstate = GlassState.removing;
		try {
			sem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//fires animation to remove glass
	private void giveGlassToPopup(){
		print("PopupRobot " + this.type + "action: giveGlassToPopup to popup " + this.Popup.getName() + "\n");	
		Object args[] = new Object[1];
		args[0] = this.guiIndex;
		if(type.equals("DRILL"))
			this.transducer.fireEvent(TChannel.DRILL, TEvent.WORKSTATION_RELEASE_GLASS, args); 
		else if (type.equals("CROSS_SEAMER"))
			this.transducer.fireEvent(TChannel.CROSS_SEAMER, TEvent.WORKSTATION_RELEASE_GLASS, args); 
		else if (type.equals("GRINDER"))
			this.transducer.fireEvent(TChannel.GRINDER, TEvent.WORKSTATION_RELEASE_GLASS, args); 
		try {
            animation.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		removeGlass();
		Popup.msgRobotHereIsGlass(myglass.glass, this.isTop);
		myglass.gstate = GlassState.none;
//		stateChanged();

	}

	//notifies the popup that the robot is ready to take glass
	private void notifyPopupThatRobotIsReady(){
		print("PopupRobot " + this.type + "action: notifyPopupThatRobotIsReady to popup " + this.Popup.getName() + "\n");
		System.out.println("PopupAgent " + this.type + "action: notifyPopupThatRobotIsReady to popup " + this.Popup.getName() + "\n");
		Popup.msgRobotReady(this.isTop);
		pstate = PopupState.notified;
//		stateChanged();	

	}
	//removes the glass from the robot
	private void removeGlass(){
	//	temp = myglass.glass;
		print("PopupAgent " + this.type + "action: removeGlass \n");

		pstate = PopupState.none;
		rstate = RobotState.ready;
//		stateChanged();
		myglass.gstate = GlassState.removing;

	}
	
	public void finishTransfer(){
		animation.release();
	}

	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if(type.equals("DRILL")){
			if(channel == TChannel.DRILL){
				if(event == TEvent.WORKSTATION_GUI_ACTION_FINISHED){
					if(myglass.gstate != GlassState.processed){
						myglass.gstate = GlassState.processed;
						animation.release();
					}
					else if (event == TEvent.WORKSTATION_RELEASE_FINISHED){
					
					rstate = RobotState.ready;
					}
			//	stateChanged();
				}

			}
		}
		else if(type.equals("GRINDER")){
			if(channel == TChannel.GRINDER){
				if(event == TEvent.WORKSTATION_GUI_ACTION_FINISHED){
					if(myglass.gstate != GlassState.processed){
						myglass.gstate = GlassState.processed;
						animation.release();
					}
					else if (event == TEvent.WORKSTATION_RELEASE_FINISHED){
					
					rstate = RobotState.ready;
					}
				}
			}
		}
		else if(type.equals("CROSS_SEAMER")){
			if(channel == TChannel.CROSS_SEAMER){
				if(event == TEvent.WORKSTATION_GUI_ACTION_FINISHED){
					if(myglass.gstate != GlassState.processed){
						myglass.gstate = GlassState.processed;
						animation.release();
					}
					else if (event == TEvent.WORKSTATION_RELEASE_FINISHED){
					
					rstate = RobotState.ready;
					}
				}
			}
			
		}
		
	}

}


