package mrcpsp.model.main;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mrcosta
 *
 */
public class Job {
	
	private Integer id;
	private Integer modesAmount;
	private Integer successorsAmount;
	private Integer predecessorsAmount;
	
	private List<Integer> successors;
	private List<Integer> predecessors;	
	private List<Mode> availableModes;
	
	private Integer startTime;
	private Integer endTime;
	
	private Mode mode;
	private ModesInformation modesInformation;	
	private RunningJobInformation runningJobInformation;
		
	public Job(Integer id, Integer modesAmount, Integer successorsAmount) {		
		this.id = id;
		this.modesAmount = modesAmount;
		this.successorsAmount = successorsAmount;
	}

	public Job() {
		id = 0;
		modesAmount = 0;
		successorsAmount = 0;
		predecessorsAmount = 0;
		
		successors = new ArrayList<Integer>();
		predecessors = new ArrayList<Integer>();
		availableModes = new ArrayList<Mode>();		
		modesInformation = new ModesInformation();
		runningJobInformation = new RunningJobInformation();
		
		startTime = 0;
		endTime = 0;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getModesAmount() {
		return modesAmount;
	}
	
	public void setModesAmount(Integer modesAmount) {
		this.modesAmount = modesAmount;
	}
	
	public Integer getSuccessorsAmount() {
		return successorsAmount;
	}
	
	public void setSuccessorsAmount(Integer successorsAmount) {
		this.successorsAmount = successorsAmount;
	}
	
	public List<Integer> getSuccessors() {
		return successors;
	}
	
	public void setSuccessors(List<Integer> successors) {
		this.successors = successors;
	}
	
	public List<Mode> getAvailableModes() {
		return availableModes;
	}
	
	public void setAvailableModes(List<Mode> modes) {
		this.availableModes = modes;
	}

	public List<Integer> getPredecessors() {
		return predecessors;
	}

	public void setPredecessors(List<Integer> predecessors) {
		this.predecessors = predecessors;
	}

	public Integer getPredecessorsAmount() {
		return predecessorsAmount;
	}

	public void setPredecessorsAmount(Integer predecessorsAmount) {
		this.predecessorsAmount = predecessorsAmount;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}	

	public ModesInformation getModesInformation() {
		return modesInformation;
	}

	public void setModesInformation(ModesInformation modesInformation) {
		this.modesInformation = modesInformation;
	}

	public RunningJobInformation getRunningJobInformation() {
		return runningJobInformation;
	}

	public void setRunningJobInformation(RunningJobInformation runningJobInformation) {
		this.runningJobInformation = runningJobInformation;
	}

	public Integer getStartTime() {
		return startTime;
	}

	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}

	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "Job [id:" + id + ", modesAmount: " + modesAmount
				+ ", successorsAmount: " + successorsAmount
				+ ", predecessorsAmount: " + predecessorsAmount
				+ ", successors: " + successors + ", predecessors: "+ predecessors
				+ ", staggeredPredecessors: " + runningJobInformation.getStaggeredPredecessors()
                + ", {Start, End} = " + "{ " + startTime + ", " + endTime + "}";
	}
	
	public String toStringAvailableModes(Mode mode) {
		String availableModesString = "{id: " + mode.getId() + ", duration: " + mode.getDuration();
		
		availableModesString+= " , renewable: [";
		availableModesString+= "R" + 1 + ": " + mode.getRenewable().get(0);
		for(int i = 1; i < mode.getRenewable().size(); i++) {
			availableModesString+= ", R" + (i + 1) + ": " + mode.getRenewable().get(i);
		}
		availableModesString+= "], nonRenewable: [";
				
		availableModesString+= "N" + 1 + ": " + mode.getNonRenewable().get(0);
		for(int i = 1; i < mode.getRenewable().size(); i++) {
			availableModesString+= ", N" + (i + 1) + ": " + mode.getNonRenewable().get(i);
		}
		availableModesString+= "]}";
		
		return availableModesString;
				
	}
	
}
