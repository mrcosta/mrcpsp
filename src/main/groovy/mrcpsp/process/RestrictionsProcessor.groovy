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

	boolean checkNonRenewableResourcesAmount(Project project) {
		ResourceAvailabilities ra = project.resourceAvailabilities
		List<Job> jobs = project.jobs
		boolean checkAmount = true
		
		log.info("Checking the Non Renewable resources amount. Initial Amount: " + ra.nonRenewableInitialAmount)
		jobs.each { job ->
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

	private boolean setNonRenewableResourcesConsumedAmount(ResourceAvailabilities ra, Mode mode) {
		Integer count = 0
		boolean checkAmount = true
		
		mode.nonRenewable.each { amount ->
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

}
