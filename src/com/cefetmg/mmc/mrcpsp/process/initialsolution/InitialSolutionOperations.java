package com.cefetmg.mmc.mrcpsp.process.initialsolution;

import java.util.List;

import com.cefetmg.mmc.mrcpsp.model.main.Job;

/**
 * @author mateus
 *
 */
public interface InitialSolutionOperations {
	
	/**
	 * If the predecessors list equals to staggeredPredecessors, so the job has all its predecessor schedulled. 
	 * Then the job is ready to be scheduled and added to the rclJobsList
	 * @param remainingJobs
	 * @param rclJobsList
	 * @return
	 */
	public List<Job> getEligibleJobsList(List<Job> remainingJobs, List<Job> eligibleJobsList);
	
	/**
	 * Return a randomized job from rclJobsList base in the RCList Size
	 * If the RCList Size is lesser than the rclJobsList size then randomize a job from the entire list
	 * @param rclJobsList
	 * @param rclSize
	 * @return
	 */
	public Job getRandomJobFromRCL(List<Job> rclJobsList);
	
	/**
	 * Update the RunningJobInformation - nis, can and niscan amount
	 * Update the predecessors list of the successors jobs of the task that was picked to be scheduled
	 * @param jobs
	 * @return
	 */	
	public List<Job> updateRunningJobInformation(List<Job> remainingJobs, Job randomizedJob);	

}
