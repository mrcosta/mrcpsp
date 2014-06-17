package mrcpsp.model.main

class RunningJobInformation {
	
	List<Integer> staggeredPredecessors;
	boolean eligible;
	
	RunningJobInformation() {
		staggeredPredecessors = new ArrayList<Integer>()
        eligible = false
	}
	
}
