package com.cefetmg.mmc.mrcpsp.process.mode;

import com.cefetmg.mmc.mrcpsp.model.main.Job;
import com.cefetmg.mmc.mrcpsp.model.main.ModesInformation;

public interface ModeComparatorProcessor {
	
	/**
	 * order job modes list by renewable amount resources
	 * @param job
	 * @return
	 */
	public ModesInformation orderModeByRenewableAmountResources(Job job);
	
	/**
	 * order job modes list by non renewable amount resources
	 * @param job
	 * @return
	 */
	public ModesInformation orderModeByNonRenewableAmountResources(Job job);
	
	/**
	 * order job modes list by duration of the mode
	 * @param job
	 * @return
	 */	
	public ModesInformation orderModeByDuration(Job job);
	
	/**
	 * order job modes list by the sum of resources (renewable + non renewable)
	 * @param job
	 * @return
	 */
	public ModesInformation orderModeBySumResources(Job job);

}
