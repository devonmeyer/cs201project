
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
	public OnlineWorkStationAgent washer;
	public OnlineWorkStationAgent uv;
	public OnlineWorkStationAgent painter;
	public OnlineWorkStationAgent baker;
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
		breakout = new OnlineWorkStationAgent("BREAKOUT", 1, 1, transducer, cPanel.getTracePanel());
		mbreakout= new OnlineWorkStationAgent("MANUAL_BREAKOUT", 2, 1, transducer, cPanel.getTracePanel());
		crossSeamer = new PopupAgent(transducer, 1, 1 ,2);
		grinder = new PopupAgent(transducer, 2, 1, 2);
		drill = new PopupAgent(transducer, 3, 1, 2);
		washer = new OnlineWorkStationAgent("WASHER", 6, 1, transducer, cPanel.getTracePanel());
		uv = new OnlineWorkStationAgent("UV_LAMP", 7, 1, transducer, cPanel.getTracePanel());
		painter = new OnlineWorkStationAgent("PAINTER", 8, 1, transducer, cPanel.getTracePanel());
		baker = new OnlineWorkStationAgent("OVEN", 9, 1, transducer, cPanel.getTracePanel());
		conv0 = new ConveyorAgent("Cutter", transducer,0);
		conv1 = new ConveyorAgent("SHUTTLE1", transducer,1);
		conv2 = new ConveyorAgent("BREAKOUT", transducer,2);
		conv3 = new ConveyorAgent("MANUAL_BREAKOUT", transducer,3);
		conv4 = new ConveyorAgent("SHUTTLE2", transducer,4);
		conv5 = new ConveyorAgent("CROSS_SEAMER", transducer,5);
		conv6 = new ConveyorAgent("DRILL", transducer,6);
		conv7 = new ConveyorAgent("GRINDER", transducer,7);
		conv8 = new ConveyorAgent("WASHER", transducer,8);
		conv9 = new ConveyorAgent("SHUTTLE3", transducer,9);
		conv10 = new ConveyorAgent("UV_LAMP", transducer,10);
		conv11 = new ConveyorAgent("PAINTER", transducer,11);
		conv12 = new ConveyorAgent("SHUTLE4", transducer,12);
		conv13 = new ConveyorAgent("OVEN", transducer,13);
		conv14 = new ConveyorAgent("EXIT", transducer,14);
		
		gRobot.setConveyor(conv1);
		conv1.setMachines(gRobot, cutter);
		
		gRobot.startThread();
		conv1.startThread();

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
