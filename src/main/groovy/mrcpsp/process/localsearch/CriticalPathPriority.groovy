package mrcpsp.process.localsearch

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.Project
import mrcpsp.process.MmProcessor
import mrcpsp.process.ResultsProcessor
import mrcpsp.process.job.JobOperations
import mrcpsp.process.mode.ModeOperations
import mrcpsp.utils.ChronoWatch
import mrcpsp.utils.CloneUtils
import mrcpsp.utils.PropertyConstants
import org.apache.commons.lang.math.RandomUtils
import org.apache.log4j.Logger

/**
 * Created by mateus on 5/25/14.
 */
class CriticalPathPriority {

    static final Logger log = Logger.getLogger(LocalSearch.class);

    List<Job> criticalPath
    List<Integer> criticalPathIds

    Project bestProject
    Project bestNeighbor

    boolean checkSolution
    boolean existsJobToChange

    ResultsProcessor resultsProcessor
    JobOperations jobOperations
    MmProcessor mmProcessor
    ModeOperations modeOperations

    CriticalPathPriority() {
        resultsProcessor = new ResultsProcessor()
        jobOperations = new JobOperations()
        mmProcessor = new MmProcessor()
        modeOperations = new ModeOperations()
    }

    Project executeCriticalPathPriority(Project project) {
        bestProject = project
        checkSolution = true
        existsJobToChange = true
        bestNeighbor = project

        while (checkSolution && existsJobToChange) {
            criticalPath = resultsProcessor.getCriticalPath(bestProject)
            criticalPathIds = criticalPath.id

            def index = 0
            while (existsJobToChange && index < criticalPath.size()) {
                def job = criticalPath[index]

                if (job.mode.id != 1) {
                    def neighborProject = createJobNeighbor(bestProject, job.id)

                    if (neighborProject) {
                        mmProcessor.project = neighborProject
                        mmProcessor.executeGetJobTimes()
                        mmProcessor.setProjectMakespan()

                        checkBestNeighbor(neighborProject);
                    }
                }

                index++
            }

            checkBestSolution(bestNeighbor, project)
        }

        return bestProject
    }

    Project createJobNeighbor(Project project, Integer jobId) {
        ChronoWatch.instance.pauseSolutionTime()
        Project neighborProject = CloneUtils.cloneProject(project)
        ChronoWatch.instance.startSolutionTime()

        def modifiableJobs = getModifiableJobs(project)

        if (!modifiableJobs) {
            existsJobToChange = false
            return null
        }

        while (!modifiableJobs.isEmpty()) {
            Job worseningJob = modifiableJobs[RandomUtils.nextInt(modifiableJobs.size())]
            Mode worseningMode = worseningJob.availableModes.find { it.id == (worseningJob.mode.id + 1) }

            if (changeModeAndCheckNonRenewableResourcesRestriction(neighborProject, worseningJob, worseningMode)) {
                Job jobToChange = neighborProject.staggeredJobs.find { it.id == jobId }
                Mode modeToChange = jobToChange.availableModes.find { it.id == jobToChange.modesInformation.shorter }

                if (changeModeAndCheckNonRenewableResourcesRestriction(neighborProject, jobToChange, modeToChange)) {
                    return neighborProject
                } else {
                    return null
                }
            } else {
                modifiableJobs.remove(worseningJob)
            }
        }

        return neighborProject
    }

    List<Job> getModifiableJobs(Project neighborProject) {
        List<Job> realJobs = jobOperations.getOnlyRealJobs(neighborProject.staggeredJobs, neighborProject.instanceInformation.jobsAmount)
        List<Job> remainingJobs = realJobs.removeAll { criticalPathIds.contains(it.id) }

        return remainingJobs.findAll { it.mode.id != 3 }
    }

    private void checkBestNeighbor(Project project) {
        if (!bestNeighbor || bestNeighbor.makespan == PropertyConstants.INSTANCE_MAKESPAN_ERROR) {
            bestNeighbor = project
        } else {
            if ((project.makespan < bestNeighbor.makespan) && (project.makespan != PropertyConstants.INSTANCE_MAKESPAN_ERROR)) {
                bestNeighbor = project
            }
        }
    }

    private void checkBestSolution(Project bestNeighbor, Project project) {
        if (bestNeighbor.makespan < bestProject.makespan) {
            bestProject = bestNeighbor
            log.info("LOOKING FOR A BETTER SOLUTION - FILE: " + project.fileName + " - INITIAL SOLUTION MAKESPAN: " + project.makespan)
            log.info("LOOKING FOR A BETTER SOLUTION - FILE: " + bestProject.fileName + " - NEIGHBOR MAKESPAN: " + bestProject.makespan)
        } else  {
            checkSolution = false
        }
    }

    def changeModeAndCheckNonRenewableResourcesRestriction(Project project, Job job, Mode newMode) {
        modeOperations.removingNonRenewableResources(project.resourceAvailabilities, job.mode)

        def checkResources = modeOperations.checkNonRenewableResources(project.resourceAvailabilities, newMode)
        if (checkResources) {
            job.mode = newMode
        }

        modeOperations.addingNonRenewableResources(project.resourceAvailabilities, job.mode)

        return checkResources
    }
}

