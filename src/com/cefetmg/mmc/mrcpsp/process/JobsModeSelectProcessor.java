package com.cefetmg.mmc.mrcpsp.process;

import java.util.List;

import com.cefetmg.mmc.mrcpsp.model.main.Job;

/**
 * @author mrcosta
 *
 */
public interface JobsModeSelectProcessor {
	
	/**
	 * each job get a mode that doesn't break the non renewable resources amount
	 * @param jobs
	 * @return
	 */
	public abstract List<Job> getModeToStaggeredJobs(List<Job> jobs);

}
