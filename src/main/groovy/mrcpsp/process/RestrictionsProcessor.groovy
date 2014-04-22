package mrcpsp.process

import org.apache.log4j.Level
import org.apache.log4j.Logger

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.Project
import mrcpsp.model.main.ResourceAvailabilities

import mrcpsp.utils.LogUtils
import mrcpsp.utils.PropertyConstants

public class RestrictionsProcessor {

	private static final Logger log = Logger.getLogger(RestrictionsProcessor.class)

    /**
     * Checks if there is some NON RENEWABLE resources to be used.
     * If not, then the scheduled is broken
     * @return
     */
	public boolean checkNonRenewableResourcesAmount(Project project) {
		ResourceAvailabilities ra = project.resourceAvailabilities
		List<Job> jobs = project.staggeredJobs
		boolean checkAmount = true
		
		log.info("Checking the Non Renewable resources amount. Initial Amount: " + ra.nonRenewableInitialAmount)
		for (Job job: jobs) {
			Mode mode = job.mode
			
			checkAmount = setNonRenewableResourcesConsumedAmount(ra, mode)
			
			if (checkAmount == false) {				
				log.log(Level.ERROR, "FILE instance: "  + project.fileName + " .Problem calculating the non renewable resources amount of the job " + job.id + ".")
                log.info("Consumed non renewable amounts after jobs execution would be: " + ra.nonRenewableConsumedAmount)
                log.info("Remaining non renewable amounts after jobs execution would be: " + ra.remainingNonRenewableAmount)
				return checkAmount
			} 
			
			log.debug("Consumed non renewable amounts after job " + job.id + " execution will be: " + ra.nonRenewableConsumedAmount)
		}
		
		log.info("Consumed non renewable amounts after jobs execution will be: " + ra.nonRenewableConsumedAmount)
		log.info("Remaining non renewable amounts after jobs execution will be: " + ra.remainingNonRenewableAmount)
		return checkAmount
	}

    /**
     * Setting the RENEWABLE resources amount usage
     * @param ra
     * @param mode
     * @return
     */
	private boolean setNonRenewableResourcesConsumedAmount(ResourceAvailabilities ra, Mode mode) {
		Integer count = 0
		boolean checkAmount = true
		
		for (Integer amount : mode.nonRenewable) {
			Integer nonRenewableAmount = ra.nonRenewableConsumedAmount[count]
			Integer remainingResources = 0			
			nonRenewableAmount+= amount
			ra.nonRenewableConsumedAmount[count] = nonRenewableAmount			
			
			remainingResources = ra.nonRenewableInitialAmount[count] - ra.nonRenewableConsumedAmount[count]
			ra.remainingNonRenewableAmount[count] = remainingResources
			
			if (remainingResources < 0) {
				checkAmount = false
			}
			
			count++
		}	
		
		return checkAmount
	}	

	public boolean setRenewableResourcesConsumedAmount(ResourceAvailabilities ra, Mode mode, Integer operation) {
		boolean checkAmount = false
		
		checkAmount = checkResourcesBeforeSetValues(ra, mode, operation, checkAmount)
		
		if (checkAmount) {
			checkResourcesBeforeSetValues(ra, mode, operation, checkAmount)
		}
		
		return checkAmount
	}
	
	private boolean checkResourcesBeforeSetValues(ResourceAvailabilities ra, Mode mode, Integer operation, boolean setValues) {
		Integer count = 0
		boolean checkAmount = true
		
		for (Integer amount : mode.renewable) {
			Integer renewableAmount = ra.renewableConsumedAmount[count]
			Integer remainingResources = 0			
			
			if (operation == PropertyConstants.ADD) {
				renewableAmount+= amount
			} else if (operation == PropertyConstants.SUBTRACT) {
				renewableAmount-= amount
			} else {
				log.log(Level.ERROR, "Operation not supported!" + LogUtils.generateErrorLog(Thread.currentThread().getStackTrace()))
				throw new IllegalArgumentException("Operation not supported!" + LogUtils.generateErrorLog(Thread.currentThread().getStackTrace()))
			}
			
			remainingResources = ra.renewableInitialAmount[count] - renewableAmount
			
			if (remainingResources < 0) {
				return checkAmount = false				
			} 
			
			if (setValues) {
				ra.remainingRenewableAmount[count] = remainingResources
				ra.renewableConsumedAmount[count] = renewableAmount
			}
			
			count++			
		}	
		
		return checkAmount
	}

    /**
     * Should find a unique job that freeing its resources is enough to add the new job in the scheduling.
     * @param ra
     * @param jobs
     * @param jobToScheduled
     * @return
     */
	public Job getBestJobToRemoveRR(ResourceAvailabilities ra, List<Job> jobs, Job jobToScheduled) {
		Integer newAmount
		Integer countJob = 0
		boolean checkJob = false
		Job job = null	
		
		while (countJob < jobs.size() && !checkJob) {
			Integer countMode = 0
			boolean checkAmount = false
			boolean checkExecution = true
			
			job = jobs[countJob]
			
			while (countMode < job.mode.renewable.size() && checkExecution) {
				newAmount = ra.remainingRenewableAmount[countMode] + job.mode.renewable[countMode]				
				
				if (jobToScheduled.mode.renewable[countMode] <= newAmount) {
					checkAmount = true
				} else {
					checkAmount = false
					checkExecution = false
				}				
				
				countMode++
			}
			
			if (checkAmount && checkExecution) {
				checkJob = true
				return job
			}
			
			countJob++
		}
		
		return null
	}

}
