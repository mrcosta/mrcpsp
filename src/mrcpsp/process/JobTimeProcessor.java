package mrcpsp.process;

import mrcpsp.model.enums.EnumLogUtils;
import mrcpsp.model.main.Job;
import mrcpsp.model.main.ResourceAvailabilities;
import mrcpsp.process.job.JobOperations;
import mrcpsp.process.job.JobPriorityRulesOperations;
import mrcpsp.utils.CloneUtils;
import mrcpsp.utils.LogUtils;
import mrcpsp.utils.PropertyConstants;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class JobTimeProcessor {

	private static final Logger log = Logger.getLogger(JobTimeProcessor.class);
	
	private RestrictionsProcessor restrictionsProcessor;
	private JobOperations jobOperations;
	private JobPriorityRulesOperations jprOperations;
	
	public JobTimeProcessor() {
		restrictionsProcessor = new RestrictionsProcessor();
		jobOperations = new JobOperations();
		jprOperations = new JobPriorityRulesOperations();
	}
	
	public boolean getJobTimes(ResourceAvailabilities ra, List<Job> jobs) {
		boolean checkResources = false;
		
		for (Job job: jobs) {
			log.debug("Getting start and finish time - JOB: " + job.getId());
			
			if (job.getPredecessors().isEmpty() && ra.getJobsUsingRenewableResources().isEmpty()) {				
				checkResources = setTimeJobWithoutPredecessors(ra, job);				
			} else {				
				checkResources = setTimeJobWithPredecessor(ra, job, jobs);				
			}
			
			log.info(LogUtils.generateJobRenewableResourcesAndTimeLog(ra, job));
			log.debug(LogUtils.generateJobsIDListLog(ra.getJobsUsingRenewableResources(), EnumLogUtils.REMAINING_JOBS_RENEWABLE));
		}
		
		return checkResources;
	}
	
	private boolean setTimeJobWithoutPredecessors(ResourceAvailabilities ra, Job job) {
		boolean checkResourcesAmount = false;
		job.setStartTime(0);
		job.setEndTime(0);
		
		checkResourcesAmount = restrictionsProcessor.setRenewableResourcesConsumedAmount(ra, job.getMode(), PropertyConstants.ADD);
		ra.getJobsUsingRenewableResources().add(CloneUtils.cloneJob(job));
		log.debug("JOB " + job.getId() + " was added in the 'using renewable resources list'.");
		log.debug("SET RENEWABLE OPERATION: ADD - " + ra.toStringRenewable());
		
		return checkResourcesAmount;
	}
	
	private boolean setTimeJobWithPredecessor(ResourceAvailabilities ra, Job job, List<Job> jobs) {
		List<Job> predecessors = null;
		boolean checkResourcesAmount = false;
		
		predecessors = getPredecessorsList(ra, job, jobs);
		
		checkResourcesAmount = restrictionsProcessor.setRenewableResourcesConsumedAmount(ra, job.getMode(), PropertyConstants.ADD);
		
		if (checkResourcesAmount) {
			log.debug("Is using its PREDECESSORS to set the time.");
			setTimeUsingPredecessors(ra, predecessors, job);
		} else {
			log.debug("Is necessary remove the OTHERS SCHEDULED JOBS to set the time - But the time can be provide from its predecessors yet.");
			checkResourcesAmount = setTimeRemovingCurrentJobsResources(ra, predecessors, job);
		}
		
		return checkResourcesAmount;
	}
	
	private void subtractRenewableResourcesAmount(ResourceAvailabilities ra, List<Job> predecessorsRemoved) {
		
		for (Job job: predecessorsRemoved) {
			restrictionsProcessor.setRenewableResourcesConsumedAmount(ra, job.getMode(), PropertyConstants.SUBTRACT);
			log.debug("SET RENEWABLE OPERATION: SUBTRACT - " + ra.toStringRenewable());
		}
	}
	
	/**
	 * like the above, but remove just one job per time, calculating if the amount resources is enough to it
	 * be scheduled. Return the job's list that were necessary to remove
	 * first test if exists a unique job that have that amount of renewable resources equals one of the jobs
	 * that are already scheduled 
	 */
	private List<Job> subtractRRACheckingAmount(ResourceAvailabilities ra, List<Job> predecessorsRemoved, Job jobToScheduled) {
		Job betterJobToRemove = null;
		List<Job> jobsToRemove = new ArrayList<Job>();
				
		betterJobToRemove = restrictionsProcessor.getBestJobToRemoveRR(ra, predecessorsRemoved, jobToScheduled);
		
		/*
		 * means that we need to remove only just one job, or remove the jobs until we have the required amount to schedule the job 
		 */
		if (betterJobToRemove != null) {
			restrictionsProcessor.setRenewableResourcesConsumedAmount(ra, betterJobToRemove.getMode(), PropertyConstants.SUBTRACT);
			jobsToRemove.add(betterJobToRemove);
			log.debug("SET RENEWABLE OPERATION: SUBTRACT - " + ra.toStringRenewable() + " - only one job was removed: " + betterJobToRemove.getId());
		} else {
			Integer count = 0;
			boolean checkAmount = false;
			
			while (!checkAmount && count < predecessorsRemoved.size()) {
				restrictionsProcessor.setRenewableResourcesConsumedAmount(ra, predecessorsRemoved.get(count).getMode(), PropertyConstants.SUBTRACT);
				log.debug("SET RENEWABLE OPERATION: SUBTRACT - " + ra.toStringRenewable() + " - removing " + predecessorsRemoved.get(count).getId());
								
				checkAmount = checkExistingAmount(jobToScheduled, ra);			
				
				jobsToRemove.add(predecessorsRemoved.get(count));
				
				count++;
			}
		}

		return jobsToRemove;
	}
	
	/**
	 * Get the list of the predecessors that are using the renewable resources yet
	 * If there is any job, then update the amount of renewable resources
	 * If not, then get the predecessors list that are already were scheduled	
	 */
	private List<Job> getPredecessorsList(ResourceAvailabilities ra, Job job, List<Job> jobs) {
		List<Job> predecessors = null;
		
		predecessors = jobOperations.removeJobsFromListByIndexList(ra.getJobsUsingRenewableResources(), job.getPredecessors());
			
		// updating the renewable resources
		if (!predecessors.isEmpty()) {				
			subtractRenewableResourcesAmount(ra, predecessors);				
		} else {
			predecessors = jobOperations.getJobsCopyListByIndexList(jobs, job.getPredecessors());
		} 
		
		return predecessors;
	}
	
	/**
	 * Will set the time using the end time of its predecessors
	 */
	private void setTimeUsingPredecessors(ResourceAvailabilities ra, List<Job> predecessors, Job job) {		
		jprOperations.getJobListOrderByEndTime(predecessors);
		
		Job jobLatestEndTime = predecessors.get(PropertyConstants.INDEX_START);
		
		job.setStartTime(jobLatestEndTime.getEndTime());
		job.setEndTime(job.getStartTime() + job.getMode().getDuration());
		ra.getJobsUsingRenewableResources().add(CloneUtils.cloneJob(job));		
	}
	
	/**
	 * We have to update (remove) the jobs that are using the renewable resources 
	 * we added the predecessors too, because the latest time could be of the predecessors yet
	 */
	private boolean setTimeRemovingCurrentJobsResources(ResourceAvailabilities ra, List<Job> predecessors, Job job) {
		List<Job> jobsToRemove;
		boolean checkResourcesAmount = false;
		
		jobsToRemove = subtractRRACheckingAmount(ra, ra.getJobsUsingRenewableResources(), job);
						
		// adding the jobs that we are not using the resources anymore
		predecessors.addAll(jobsToRemove);
		
		// updating the job list that are using the resources
		ra.getJobsUsingRenewableResources().removeAll(jobsToRemove);		
		
		checkResourcesAmount = restrictionsProcessor.setRenewableResourcesConsumedAmount(ra, job.getMode(), PropertyConstants.ADD);
		
		if (checkResourcesAmount) {
			jprOperations.getJobListOrderByEndTime(predecessors);
			
			Job jobLatestEndTime = predecessors.get(PropertyConstants.INDEX_START);
			
			job.setStartTime(jobLatestEndTime.getEndTime());
			job.setEndTime(job.getStartTime() + job.getMode().getDuration());
			ra.getJobsUsingRenewableResources().add(CloneUtils.cloneJob(job));
		} else {
			return checkResourcesAmount = false;
		}
		
		return checkResourcesAmount;		
	}
	
	private boolean checkExistingAmount(Job jobToScheduled, ResourceAvailabilities ra) {
		Integer countMode = 0;
		Integer amount = 0;
		boolean checkAmount = false;
		boolean checkExecution = true;
		
		while (countMode < jobToScheduled.getMode().getRenewable().size() && checkExecution) {
			amount  = jobToScheduled.getMode().getRenewable().get(countMode);
			
			if (amount <= ra.getRemainingRenewableAmount().get(countMode)) {
				checkAmount = true;
			} else {
				checkAmount = false;
				checkExecution = false;
			}
			
			countMode++;
		}
		
		return checkAmount;
	}

}
