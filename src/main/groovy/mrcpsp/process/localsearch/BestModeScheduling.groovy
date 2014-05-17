package mrcpsp.process.localsearch

import mrcpsp.model.main.Mode
import mrcpsp.model.main.Project
import mrcpsp.process.mode.ModeOperations
import mrcpsp.utils.ChronoWatch
import mrcpsp.utils.CloneUtils

/**
 * Created by mateus on 5/10/14.
 */
class BestModeScheduling {

    ModeOperations modeOperations

    BestModeScheduling() {
        modeOperations = new ModeOperations()
    }

    def Project changeExecutionModeJob(Project project, Integer jobId, Integer modeId) {
        ChronoWatch.instance.pauseSolutionTime()
        Project neighborProject = CloneUtils.cloneProject(project)
        ChronoWatch.instance.startSolutionTime()

        if (changeModeAndCheckNonRenewableResourcesRestriction(neighborProject, jobId, modeId)) {
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
    def changeModeAndCheckNonRenewableResourcesRestriction(Project project, Integer jobId, Integer modeId) {
        def job = project.staggeredJobsId.find { it.id == jobId }
        Mode newMode = job.availableModes.find{ it.id == modeId }

        modeOperations.removingNonRenewableResources(project.resourceAvailabilities, job.mode)

        def checkResources = modeOperations.checkNonRenewableResources(project.resourceAvailabilities, newMode)
        if (checkResources) {
            job.mode = newMode
        }

        modeOperations.addingNonRenewableResources(project.resourceAvailabilities, job.mode)

        return checkResources
    }


}
