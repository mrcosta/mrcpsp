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

    def Map<Integer, Integer> changeExecutionModeJob(Project project, Job job, Mode mode) {
        def neighborJobsModesId = changeModeAndCheckNonRenewableResourcesRestriction(project, job, mode)

        if (neighborJobsModesId) {
            return neighborJobsModesId
        } else {
            return null
        }
    }

    /**
     * Check if the new job's mode is feasible to apply for the actual solution
     * @param project
     * @return
     */
    def changeModeAndCheckNonRenewableResourcesRestriction(Project project, Job job, Mode newMode) {
        def staggeredJobsModesId = [:]
        staggeredJobsModesId.putAll(project.staggeredJobsModesId)
        modeOperations.removingNonRenewableResources(project.resourceAvailabilities, job.mode)

        def checkResources = modeOperations.checkNonRenewableResources(project.resourceAvailabilities, newMode)
        if (checkResources) {
            staggeredJobsModesId[job.id] = newMode.id
        } else {
            staggeredJobsModesId = null
        }

        modeOperations.addingNonRenewableResources(project.resourceAvailabilities, job.mode)

        return staggeredJobsModesId
    }


}
