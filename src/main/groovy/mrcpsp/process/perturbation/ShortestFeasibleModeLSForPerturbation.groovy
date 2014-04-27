package mrcpsp.process.perturbation

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.Project
import mrcpsp.process.job.JobOperations
import mrcpsp.process.mode.ModeOperations
import mrcpsp.process.mode.ShortestFeasibleMode
import mrcpsp.utils.ChronoWatch
import mrcpsp.utils.CloneUtils

/**
 * Created by mateus on 4/27/14.
 */
class ShortestFeasibleModeLSForPerturbation {

    JobOperations jobOperations
    ModeOperations modeOperations
    ShortestFeasibleMode sfm

    ShortestFeasibleModeLSForPerturbation() {
        jobOperations = new JobOperations()
        modeOperations = new ModeOperations()
        sfm = new ShortestFeasibleMode()
    }

    def Project changeExecutionModeBlockJob(Project project, jobId, randomizedJobId) {
        ChronoWatch.instance.pauseSolutionTime()
        Project neighborProject = CloneUtils.cloneProject(project)
        ChronoWatch.instance.startSolutionTime()

        if (checkShortestFeasibleMode(neighborProject, jobId, randomizedJobId)) {
            return neighborProject
        } else {
            return null
        }
    }

    def checkShortestFeasibleMode(Project project, Integer jobId, Integer randomizedJobId) {
        def job = project.staggeredJobs.find { it.id == jobId }
        def jobsBetweenInterval = jobOperations.getJobsBetweenInterval(job, project.staggeredJobs)
        jobsBetweenInterval.add(0, job)
        jobsBetweenInterval = removeRandomizedJobFromJobsBetweenInterval(jobsBetweenInterval, randomizedJobId)

        def checkModes = jobsBetweenInterval.findAll { it.mode.id == it.modesInformation.shorter }.size() == jobsBetweenInterval.size()

        if (!checkModes) {
            jobsBetweenInterval.each {
                modeOperations.removingNonRenewableResources(project.resourceAvailabilities, it.mode)
            }

            sfm.sfm([:], project.resourceAvailabilities, jobsBetweenInterval, false, 0)

            sfm.jobModes.each { key, value ->
                Job jobToChange = jobsBetweenInterval.find { it.id == key}
                Mode mode = jobToChange.availableModes.find { it.id == value}

                jobToChange.mode = mode
            }

            jobsBetweenInterval.each {
                modeOperations.addingNonRenewableResources(project.resourceAvailabilities, it.mode)
            }
        }
    }

    def removeRandomizedJobFromJobsBetweenInterval(List<Job> jobsBetweenInterval, Integer randomizedJobId) {
        return jobsBetweenInterval.findAll { it.id != randomizedJobId }
    }

}
