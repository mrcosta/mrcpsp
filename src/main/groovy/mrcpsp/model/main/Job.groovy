package mrcpsp.model.main

class Job {
	
	Integer id
	Integer modesAmount
	Integer successorsAmount
	Integer predecessorsAmount
	
	List<Integer> successors
	List<Integer> predecessors
	List<Mode> availableModes
	
	Integer startTime
	Integer endTime

    Integer totalSuccessors

	Mode mode
	RunningJobInformation runningJobInformation
		
	public Job(Integer id, Integer modesAmount, Integer successorsAmount) {		
		this.id = id;
		this.modesAmount = modesAmount;
		this.successorsAmount = successorsAmount;
	}

	public Job() {
		id = 0
		modesAmount = 0
		successorsAmount = 0
		predecessorsAmount = 0

		successors = new ArrayList<Integer>()
		predecessors = new ArrayList<Integer>()
		availableModes = new ArrayList<Mode>()
		runningJobInformation = new RunningJobInformation()

		startTime = 0
		endTime = 0
	}

	@Override
	public String toString() {
		return """Job [id: $id, modesAmount: $modesAmount, successorsAmount: $successorsAmount, predecessorsAmount: $predecessorsAmount, successors: $successors, predecessors: $predecessors, staggeredPredecessors: $runningJobInformation.staggeredPredecessors, {Start, End} =  { $startTime, $endTime }"""
	}
	
}
