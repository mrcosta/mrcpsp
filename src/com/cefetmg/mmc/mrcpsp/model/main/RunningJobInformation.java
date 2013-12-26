package com.cefetmg.mmc.mrcpsp.model.main;

import java.util.ArrayList;
import java.util.List;

public class RunningJobInformation {
	
	private List<Integer> staggeredPredecessors;	
	private Integer nisAmount;
	private Integer canAmount;
	private Integer niscanAmount;
	private boolean isEligible;
	
	public RunningJobInformation() {
		staggeredPredecessors = new ArrayList<Integer>();
		this.nisAmount = 0;
		this.canAmount = 0;
		this.niscanAmount = 0;
		isEligible = false;
	}
	
	public List<Integer> getStaggeredPredecessors() {
		return staggeredPredecessors;
	}
	
	public void setStaggeredPredecessors(List<Integer> staggeredPredecessors) {
		this.staggeredPredecessors = staggeredPredecessors;
	}
	
	public Integer getCanAmount() {
		return canAmount;
	}
	
	public void setCanAmount(Integer canAmount) {
		this.canAmount = canAmount;
	}
	
	public Integer getNiscanAmount() {
		return niscanAmount;
	}
	
	public void setNiscanAmount(Integer niscanAmount) {
		this.niscanAmount = niscanAmount;
	}

	public Integer getNisAmount() {
		return nisAmount;
	}

	public void setNisAmount(Integer nisAmount) {
		this.nisAmount = nisAmount;
	}

	public boolean isEligible() {
		return isEligible;
	}

	public void setEligible(boolean isEligible) {
		this.isEligible = isEligible;
	}
	
	@Override
	public String toString() {		
		return "{NIS:" + nisAmount + ", CAN: " + canAmount + ", NISCAN: " + niscanAmount + "}";
				
	}
	
}
