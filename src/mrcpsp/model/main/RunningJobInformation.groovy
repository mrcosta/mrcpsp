package mrcpsp.model.main;

import java.util.ArrayList;
import java.util.List;

class RunningJobInformation {
	
	List<Integer> staggeredPredecessors;
	Integer nisAmount;
	Integer canAmount;
	Integer niscanAmount;
	boolean eligible;
	
	RunningJobInformation() {
		staggeredPredecessors = new ArrayList<Integer>();
		this.nisAmount = 0;
		this.canAmount = 0;
		this.niscanAmount = 0;
        eligible = false;
	}
	
	@Override
	def String toString() {
		return """{NIS: $nisAmount, CAN: $canAmount, NISCAN: $niscanAmount}""";
				
	}
	
}
