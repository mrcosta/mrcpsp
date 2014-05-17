package mrcpsp.model.main

class RunningJobInformation {
	
	List<Integer> staggeredPredecessors;
	Integer nisAmount;
	Integer slackAmount;
    Integer totalSuccessors
	boolean eligible;
	
	RunningJobInformation() {
		staggeredPredecessors = new ArrayList<Integer>()
		nisAmount = 0
		slackAmount = 0
        totalSuccessors = 0
        eligible = false
	}

    def restartEligibilityAndStaggeredPredecessors() {
        staggeredPredecessors.clear()
        eligible = false
    }
	
	@Override
	def String toString() {
		return """{NIS: $nisAmount, SLK: $slackAmount, TOTAL_SUCESSORS: $totalSuccessors}""";
				
	}
	
}
