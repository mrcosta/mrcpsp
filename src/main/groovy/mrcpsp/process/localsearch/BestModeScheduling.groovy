package mrcpsp.process.localsearch

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.Project
import mrcpsp.process.mode.ModeOperations

/**
 * Created by mateus on 5/10/14.
 */
class BestModeScheduling {

    ModeOperations modeOperations

    BestModeScheduling() {
        modeOperations = new ModeOperations()
    }

    def Project changeExecutionModeJob(Project project, Job job, Mode mode) {
        if (changeModeAndCheckNonRenewableResourcesRestriction(project, job, mode)) {
            return project
        } else {
            return null
        }
    }

    /**
     * Check if the new job's mode is feasible to apply for the actual solution
     * @param project
     * @return
     */
    def changeModeAndCheckNonRenewableResourcesRestriction(Project project, Job job, Mode newMode, List<Integer> staggeredJobsModesId) {
        modeOperations.removingNonRenewableResources(project.resourceAvailabilities, job.mode)

        def checkResources = modeOperations.checkNonRenewableResources(project.resourceAvailabilities, newMode)
        if (checkResources) {
            staggeredJobsModesId[job.id - 1] = newMode.id
        }

        modeOperations.addingNonRenewableResources(project.resourceAvailabilities, job.mode)

        return checkResources
    }


}
