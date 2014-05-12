package mrcpsp.model.main;

import java.util.ArrayList;
import java.util.List;

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
	
	@Override
	def String toString() {
		return """{NIS: $nisAmount, SLK: $slackAmount, TOTAL_SUCESSORS: $totalSuccessors}""";
				
	}
	
}
