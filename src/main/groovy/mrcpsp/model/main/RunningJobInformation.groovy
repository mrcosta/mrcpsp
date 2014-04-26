package mrcpsp.model.main;

import java.util.ArrayList;
import java.util.List;

class RunningJobInformation {
	
	List<Integer> staggeredPredecessors;
	Integer nisAmount;
	Integer slackAmount;
	boolean eligible;
	
	RunningJobInformation() {
		staggeredPredecessors = new ArrayList<Integer>();
		this.nisAmount = 0;
		this.slackAmount = 0;
        eligible = false;
	}
	
	@Override
	def String toString() {
		return """{NIS: $nisAmount, SLK: $slackAmount}""";
				
	}
	
}
