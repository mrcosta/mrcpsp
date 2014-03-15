package mrcpsp.model.main;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mrcosta
 *
 */
class Job {
	
	Integer id;
	Integer modesAmount;
	Integer successorsAmount;
	Integer predecessorsAmount;
	
	List<Integer> successors;
	List<Integer> predecessors;
	List<Mode> availableModes;
	
	Integer startTime;
	Integer endTime;
	
	Mode mode;
	ModesInformation modesInformation;
	RunningJobInformation runningJobInformation;
		
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

    def resetTime() {
        this.startTime = 0
        this.endTime = 0
    }

	@Override
	public String toString() {
		return """Job [id: $id, modesAmount: $modesAmount, successorsAmount: $successorsAmount, predecessorsAmount: $predecessorsAmount, successors: $successors, predecessors: $predecessors, staggeredPredecessors: $runningJobInformation.staggeredPredecessors, {Start, End} =  { $startTime, $endTime }"""
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
		availableModesString+= "], amountRenewable: $mode.amountRenewable, amountNonRenewable: $mode.amountNonRenewable}";
		
		return availableModesString;
				
	}
	
}
