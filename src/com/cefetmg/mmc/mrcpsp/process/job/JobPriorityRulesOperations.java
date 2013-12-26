package com.cefetmg.mmc.mrcpsp.process.job;

import java.util.List;

import com.cefetmg.mmc.mrcpsp.model.main.Job;

public interface JobPriorityRulesOperations {
	
	/**
	 * only the priority rules that doesn't no need update information at runtime
	 * @param jobs
	 * @return
	 */
	public List<Job> setJobsPriorityRuleInformation(List<Job> jobs);
	
	/**
	 * order the elegible Jobs by the its MAX_NIS (Maximum Number of Immediate Successors)
	 * the jobs that have only the 'x' task as its predecessor. Ex: 8 has only 5 as your predecessors, so we can say
	 * that the 8 its a immediate successor of 5
	 * @param elegibleJobs
	 * @return
	 */
	public List<Job> getJobListOrderByMaxNis(List<Job> elegibleJobs);
	
	/**
	 * order the elegible Jobs by the its MAX_CAN (Maximum Number of Subsequent Candidates)
	 * Ex: 8 has 5 and 3 as your predecessors. 3 is already scheduled. so we can say that 8 is a subsequent candidate of 5,
	 * because when 5 is scheduled 8 is eligible
	 * @param elegibleJobs
	 * @return
	 */
	public List<Job> getJobListOrderByMaxCan(List<Job> elegibleJobs);
	
	/**
	 * the combination of the MAX_NIS and MAX_CAN
	 * @param elegibleJobs
	 * @return
	 */
	public List<Job> getJobListOrderByMaxNiscan(List<Job> elegibleJobs);	
	
	/**
	 * Check the priority rule passed in the config file and call the relativer method to order the 
	 * eligible jobs list
	 * @param elegibleJobsList
	 * @return
	 */
	public List<Job> orderEligibleJobsList(List<Job> elegibleJobsList);
	
	/**
	 * Back the order of the elegible jobs list - by the job ID
	 * @param elegibleJobsList
	 * @return
	 */
	public List<Job> backOrderEligibleJobsList(List<Job> elegibleJobsList);
	
	/**
	 * Update the job NIS - Maximum Number of Immediate Successors, run only one time.
	 * There is no need to update this type of priority rule operation
	 * @param jobs	 
	 */
	public List<Job> getJobsNIS(List<Job> jobs);
	
	/**
	 * Update the job CAN - Maximum Number of Subsequent Candidates
	 * @param remainingJobs
	 * @param randomizedJob
	 * @return
	 */
	public List<Job> updateJobCAN(List<Job> remainingJobs, Job randomizedJob);
	
	/**
	 * updating the job NISCAN -NIS + CAN
	 * @param remainingJobs
	 * @param randomizedJob
	 * @return
	 */
	public List<Job> updateJobNISCAN(List<Job> remainingJobs, Job randomizedJob);
	
	/**
	 * order the predecessor Jobs by the its end time. Used to set the start and the finish time of each job
	 * @param elegibleJobs
	 * @return
	 */
	public List<Job> getJobListOrderByEndTime(List<Job> jobs);

}
