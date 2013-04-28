package glassLine;

import gui.components.GUIGlass;

import java.util.List;

public class Glass {
	/**
	 * This class represents a piece of glass being processed by the factory.
	 */
	private int ID;    
	
	public boolean broken;
	
	private List<String> processes; // This contains the list of processes to be
									// performed on glass
	public GUIGlass myGui;
	
	public Glass(List<String> processes) {
		this.setProcesses(processes);
		this.broken = false;
	}
	
	public Glass(int id, List<String> processes) {
		this.setID(id);
		this.setProcesses(processes);
		this.broken = false;
	}
	
	/** Getters and Setters **/

	public int getID() {
		return ID;
	}
	
	public GUIGlass getGui(){
		return myGui;
	}
	public void setID(int ID) {
		this.ID = ID;
	}

	public List<String> getProcesses() {
		return processes;
	}

	public void setGuiGlass(GUIGlass g){
		myGui = g;
	}
	
	public void setProcesses(List<String> processes) {
		this.processes = processes;
	}
	
	public void breakGlass(){
		myGui.msgPartBroken();
	}
}
