package mrcpsp.process.localsearch

import mrcpsp.model.main.Job;
import mrcpsp.model.main.Mode;
import mrcpsp.model.main.Project;
import mrcpsp.model.main.ResourceAvailabilities
import mrcpsp.process.mode.ModeOperations;
import mrcpsp.utils.CloneUtils;

class LowerNonRenewableConsumption {

    ModeOperations modeOperations

    LowerNonRenewableConsumption() {
        modeOperations = new ModeOperations()
    }

	def Project changeExecutionModeJob(Project project, jobId) {
		Project neighborProject = CloneUtils.cloneProject(project)
		
		if (checkNonRenewableResourcesRestriction(neighborProject, jobId)) {
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
	def checkNonRenewableResourcesRestriction(Project project, Integer jobId) {
		def job = project.staggeredJobs.find { it.id == jobId }
		Mode shorterMode = job.availableModes.find{ it.id == job.modesInformation.shorter}

        modeOperations.removingNonRenewableResources(project.resourceAvailabilities, job.mode)

        def checkResources = modeOperations.checkNonRenewableResources(project.resourceAvailabilities, shorterMode)
        if (checkResources) {
            modeOperations.addingNonRenewableResources(project.resourceAvailabilities, shorterMode)
            job.mode = shorterMode
        } else {
            modeOperations.addingNonRenewableResources(project.resourceAvailabilities, job.mode)
        }
		
		return checkResources
	}
}
