package com.cefetmg.mmc.mrcpsp.process.job;

import java.util.List;

import com.cefetmg.mmc.mrcpsp.model.main.Job;

public interface JobOperations {
	
	/**
	 * remove the job, using as base the index of the clonedJob
	 * @param clonedJob
	 * @return
	 */
	public Job removeJobFromListByIndex(List<Job> jobs, Integer index);
	
	/**
	 * remove the jobs, using as base the each index in the list passed by parameter
	 * @param clonedJob
	 * @return
	 */
	public List<Job> removeJobsFromListByIndexList(List<Job> jobs, List<Integer> indexList);
		
	/**
	 * get a cloned list with only the jobs passed in the index list 
	 * @param jobs
	 * @param indexList
	 * @return
	 */
	public List<Job> getJobsCopyListByIndexList(List<Job> jobs, List<Integer> indexList);

}
