package mrcpsp.process.localsearch

import mrcpsp.model.main.Job;
import mrcpsp.model.main.Mode;
import mrcpsp.model.main.Project;
import mrcpsp.model.main.ResourceAvailabilities;
import mrcpsp.utils.CloneUtils;

class LowerNonRenewableConsumption {
	
	def Project changeExecutionModeJob(project, jobPosition) {
		Project neighborProject = CloneUtils.cloneProject(project)
		
		if (checkNonRenewableResourcesRestriction(neighborProject, jobPosition)) {
			return neighborProject
		} else {
			return null
		}	
	}
	
	/**
	 * Check if the new job's mode is feasible to apply for the actual solution
	 * @param project
	 * @return
	 */
	def checkNonRenewableResourcesRestriction(Project project, Integer jobPosition) {
		def job = project.staggeredJobs[jobPosition]
		Mode shorterMode = job.availableModes.find{ it.id == job.modesInformation.shorter}
		ResourceAvailabilities ra = CloneUtils.cloneResourceAvailabilities(project.resourceAvailabilities)
		
		def count = 0	
		def checkResources = true	
		job.mode.nonRenewable.each {
			ra.remainingNonRenewableAmount[count]+= it
			ra.nonRenewableConsumedAmount[count]-= it
			
			def remainingResources = ra.remainingNonRenewableAmount[count] - shorterMode.nonRenewable[count]
			
			if (remainingResources < 0) {
				checkResources = false
			} 
			
			count++ 
		}
		
		if (checkResources) {
			job.mode = shorterMode			
			project.resourceAvailabilities = resetProjectResourceAvailabilities(job, ra)
		}		
		
		return checkResources
	}
	
	/**
	 * reseting the resource amounts to check the NR restrictions again
	 * @param job
	 * @param ra
	 */
	def resetProjectResourceAvailabilities(Job job, ResourceAvailabilities ra) {
		def count = 0
		
		job.mode.nonRenewable.each {
			ra.remainingNonRenewableAmount[count] = ra.nonRenewableInitialAmount[count]
			ra.nonRenewableConsumedAmount[count] = 0
			count++
		}
		
		count = 0
		job.mode.renewable.each {
			ra.remainingRenewableAmount[count] = ra.renewableInitialAmount[count]
			ra.renewableConsumedAmount[count] = 0
			count++
		}
		
		return ra
	}
}
