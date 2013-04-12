
package gui.panels;

import glassLine.*;
import glassLine.agents.*;
import glassLine.interfaces.*;
import gui.drivers.FactoryFrame;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import transducer.Transducer;

/**
 * The FactoryPanel is highest level panel in the actual kitting cell. The
 * FactoryPanel makes all the back end components, connects them to the
 * GuiComponents in the DisplayPanel. It is responsible for handing
 * communication between the back and front end.
 */
@SuppressWarnings("serial")
public class FactoryPanel extends JPanel
{
	/** The frame connected to the FactoryPanel */
	private FactoryFrame parent;

	/** The control system for the factory, displayed on right */
	private ControlPanel cPanel;

	/** The graphical representation for the factory, displayed on left */
	private DisplayPanel dPanel;

	/** Allows the control panel to communicate with the back end and give commands */
	private Transducer transducer;
	
	/** agent counter */
	int agentNumber = 0;
	
	/*
	 * Factory Agents
	 */
	public GlassRobotAgent gRobot;
	public OnlineWorkStationAgent cutter;
	public OnlineWorkStationAgent breakout;
	public OnlineWorkStationAgent mbreakout;
	public PopupAgent crossSeamer;
	public PopupAgent grinder;
	public PopupAgent drill;
	public PopupRobotAgent csRobot1;
	public PopupRobotAgent csRobot2;
	public PopupRobotAgent dRobot1;
	public PopupRobotAgent dRobot2;
	public PopupRobotAgent gRobot1;
	public PopupRobotAgent gRobot2;
	public OnlineWorkStationAgent washer;
	public OnlineWorkStationAgent uv;
	public OnlineWorkStationAgent painter;
	public OnlineWorkStationAgent baker;
	public TruckAgent truck;
	public ConveyorAgent conv0;
	public ConveyorAgent conv1;
	public ConveyorAgent conv2;
	public ConveyorAgent conv3;
	public ConveyorAgent conv4;
	public ConveyorAgent conv5;
	public ConveyorAgent conv6;
	public ConveyorAgent conv7;
	public ConveyorAgent conv8;
	public ConveyorAgent conv9;
	public ConveyorAgent conv10;
	public ConveyorAgent conv11;
	public ConveyorAgent conv12;
	public ConveyorAgent conv13;
	public ConveyorAgent conv14;
	

	/**
	 * Constructor links this panel to its frame
	 */
	public FactoryPanel(FactoryFrame fFrame)
	{
		parent = fFrame;

		// initialize transducer
		transducer = new Transducer();
		transducer.startTransducer();

		// use default layout
		// dPanel = new DisplayPanel(this);
		// dPanel.setDefaultLayout();
		// dPanel.setTimerListeners();

		// initialize and run
		this.initialize();
		this.initializeBackEnd();
		
		// initialize the agents and other components
		this.startAgents();
		
		// pass glass robot to selection panel
		cPanel.glassSelectPanel.setGlassRobot(this.gRobot);
	}

	/**
	 * Initializes all elements of the front end, including the panels, and lays
	 * them out
	 */
	private void initialize()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		// initialize control panel
		cPanel = new ControlPanel(this, transducer);

		// initialize display panel
		dPanel = new DisplayPanel(this, transducer);

		// add panels in
		// JPanel tempPanel = new JPanel();
		// tempPanel.setPreferredSize(new Dimension(830, 880));
		// this.add(tempPanel);

		this.add(dPanel);
		this.add(cPanel);
		
	}
	
	private void startAgents(){

	}

	/**
	 * Feel free to use this method to start all the Agent threads at the same time
	 */
	private void initializeBackEnd()
	{
		gRobot = new GlassRobotAgent(transducer, cPanel.getTracePanel(),  "GlassBin");
		cutter = new OnlineWorkStationAgent("CUTTER", 0, 1, transducer, cPanel.getTracePanel());
		breakout = new OnlineWorkStationAgent("BREAKOUT", 111, 1, transducer, cPanel.getTracePanel());
		mbreakout= new OnlineWorkStationAgent("MANUAL_BREAKOUT", 222, 1, transducer, cPanel.getTracePanel());
		crossSeamer = new PopupAgent(transducer, 0, 0 ,1, cPanel.getTracePanel());
		csRobot1 = new PopupRobotAgent("CROSS_SEAMER",1,true,crossSeamer,transducer);
		csRobot2 = new PopupRobotAgent("CROSS_SEAMER",2,false,crossSeamer,transducer);
		drill = new PopupAgent(transducer, 1, 2, 3, cPanel.getTracePanel());
		dRobot1 = new PopupRobotAgent("DRILL",1,true,drill,transducer);
		dRobot2 = new PopupRobotAgent("DRILL",2,false,drill,transducer);
		grinder = new PopupAgent(transducer, 2, 3, 4, cPanel.getTracePanel());
		gRobot1 = new PopupRobotAgent("GRINDER",1,true,grinder,transducer);
		gRobot2 = new PopupRobotAgent("GRINDER",2,false,grinder,transducer);
		
		washer = new OnlineWorkStationAgent("WASHER", 1, 1, transducer, cPanel.getTracePanel());
		painter = new OnlineWorkStationAgent("PAINTER", 2, 1, transducer, cPanel.getTracePanel());
		uv = new OnlineWorkStationAgent("UV_LAMP", 3, 1, transducer, cPanel.getTracePanel());
		baker = new OnlineWorkStationAgent("OVEN", 4, 1, transducer, cPanel.getTracePanel());
		conv0 = new ConveyorAgent("CUTTER", transducer,0, cPanel.getTracePanel());
		conv1 = new ConveyorAgent("SHUTTLE1", transducer,1,cPanel.getTracePanel());
		conv2 = new ConveyorAgent("BREAKOUT", transducer,2,cPanel.getTracePanel());
		conv3 = new ConveyorAgent("MANUAL_BREAKOUT", transducer,3,cPanel.getTracePanel());
		conv4 = new ConveyorAgent("SHUTTLE2", transducer,4,cPanel.getTracePanel());
		conv5 = new ConveyorAgent("CROSS_SEAMER", transducer,5,cPanel.getTracePanel());
		conv6 = new ConveyorAgent("DRILL", transducer,6,cPanel.getTracePanel());
		conv7 = new ConveyorAgent("GRINDER", transducer,7,cPanel.getTracePanel());
		conv8 = new ConveyorAgent("WASHER", transducer,8,cPanel.getTracePanel());
		conv9 = new ConveyorAgent("SHUTTLE3", transducer,9,cPanel.getTracePanel());
		conv10 = new ConveyorAgent("UV_LAMP", transducer,10,cPanel.getTracePanel());
		conv11 = new ConveyorAgent("PAINTER", transducer,11,cPanel.getTracePanel());
		conv12 = new ConveyorAgent("SHUTLE4", transducer,12,cPanel.getTracePanel());
		conv13 = new ConveyorAgent("OVEN", transducer,13,cPanel.getTracePanel());
		conv14 = new ConveyorAgent("EXIT", transducer,14,cPanel.getTracePanel());
		truck = new TruckAgent("TRUCK", transducer, conv14);
		
		gRobot.setConveyor(conv0);
		conv0.setTwoMachines(gRobot, cutter);
		conv1.setTwoMachines(cutter, conv2);
		conv2.setTwoMachines(conv1, breakout);
		conv3.setTwoMachines(breakout, mbreakout);
		conv4.setTwoMachines(mbreakout, conv5);
		conv5.setTwoMachines(conv4, crossSeamer);
		conv6.setTwoMachines(crossSeamer, drill);
		conv7.setTwoMachines(drill, grinder);
		conv8.setTwoMachines(grinder,washer);
		conv9.setTwoMachines(washer, conv10);
		conv10.setTwoMachines(conv9, painter);
		conv11.setTwoMachines(painter, uv);
		conv12.setTwoMachines(uv, conv13);
		conv13.setTwoMachines(conv12, baker);
		conv14.setTwoMachines(baker,truck);
		
		cutter.setConveyors(conv0, conv1);
		breakout.setConveyors(conv2, conv3);
		mbreakout.setConveyors(conv3, conv4);
		crossSeamer.setConveyors(conv5, conv6);
		drill.setConveyors(conv6, conv7);
		grinder.setConveyors(conv7, conv8);
		washer.setConveyors(conv8, conv9);
		painter.setConveyors(conv10, conv11);
		uv.setConveyors(conv11, conv12);
		baker.setConveyors(conv13, conv14);
		
		gRobot.startThread();
		cutter.startThread();
		breakout.startThread();
		mbreakout.startThread();
		crossSeamer.startThread();
		csRobot1.startThread();
		csRobot2.startThread();
		drill.startThread();
		dRobot1.startThread();
		dRobot2.startThread();
		grinder.startThread();
		gRobot1.startThread();
		gRobot2.startThread();
		washer.startThread();
		painter.startThread();
		uv.startThread();
		baker.startThread();
		truck.startThread();
		conv0.startThread();
		conv1.startThread();
		conv2.startThread();
		conv3.startThread();
		conv4.startThread();
		conv5.startThread();
		conv6.startThread();
		conv7.startThread();
		conv8.startThread();
		conv9.startThread();
		conv10.startThread();
		conv11.startThread();
		conv12.startThread();
		conv13.startThread();
		conv14.startThread();

		System.out.println("Back end initialization finished.");
	}

	/**
	 * Returns the parent frame of this panel
	 * 
	 * @return the parent frame
	 */
	public FactoryFrame getGuiParent()
	{
		return parent;
	}

	/**
	 * Returns the control panel
	 * 
	 * @return the control panel
	 */
	public ControlPanel getControlPanel()
	{
		return cPanel;
	}

	/**
	 * Returns the display panel
	 * 
	 * @return the display panel
	 */
	public DisplayPanel getDisplayPanel()
	{
		return dPanel;
	}
	
	public void makeGlass(Glass g){
		parent.glasses.add(g);
	}
}
