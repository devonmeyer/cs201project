package glassLine;

import java.util.List;

public class Glass {
	/**
	 * This class represents a piece of glass being processed by the factory.
	 */
	private int ID;    
	private List<String> processes; // This contains the list of processes to be
									// performed on glass
	
	public Glass(int id, List<String> processes) {
		this.setID(id);
		this.setProcesses(processes);
	}
	
	/** Getters and Setters **/

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public List<String> getProcesses() {
		return processes;
	}

	public void setProcesses(List<String> processes) {
		this.processes = processes;
	}
}
