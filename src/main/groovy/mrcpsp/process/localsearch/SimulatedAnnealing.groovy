package mrcpsp.process.localsearch

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.Project
import mrcpsp.process.MmProcessor
import mrcpsp.process.job.JobOperations
import mrcpsp.process.job.JobPriorityRulesOperations
import mrcpsp.process.mode.ModeOperations
import mrcpsp.utils.ChronoWatch
import mrcpsp.utils.CloneUtils
import mrcpsp.utils.UrlUtils
import org.apache.commons.lang.math.RandomUtils
import org.apache.log4j.Logger

/**
 * Created by mateus on 5/24/14.
 */
class SimulatedAnnealing {

    static final Logger log = Logger.getLogger(LocalSearch.class);

    Double temperature
    Double reductionCoefficient
    Double stoppingCriterion

    Integer totalNeighbor
    Integer totalJobsAndModes

    ModeOperations modeOperations
    MmProcessor mmProcessor
    JobOperations jobOperations

    Project bestProject
    Project actualSolution

    SimulatedAnnealing() {
        modeOperations = new ModeOperations()
        mmProcessor = new MmProcessor()
        jobOperations = new JobOperations()
    }

    def initParameterValues() {
        temperature = UrlUtils.instance.SATemperature
        reductionCoefficient = UrlUtils.instance.SAReductionCoefficient
        stoppingCriterion = UrlUtils.instance.SAStoppingCriterion
        totalNeighbor = UrlUtils.instance.SATotalNeighbor

        if (totalNeighbor == 0) {
            totalNeighbor = totalJobsAndModes
        }
    }

    Project executeSimulatedAnnealing(Project project) {
        bestProject = project
        actualSolution = project

        def jobsToRandomize = getOnlyJobsToRandomize(project)
        def jobsIdToRandomize = jobsToRandomize.id
        setTotalJobsAndModes(jobsToRandomize)
        initParameterValues()

        while (temperature > stoppingCriterion) {

            totalNeighbor.times { neighbor ->
                def neighborProject = createModeNeighbor(actualSolution, jobsIdToRandomize)

                if (neighborProject) {
                    mmProcessor.project = neighborProject
                    mmProcessor.executeGetJobTimes()
                    mmProcessor.setProjectMakespan()
                    checkActualSolution(neighborProject)
                    checkBestSolution(neighborProject)
                }
            }

            temperature*= reductionCoefficient
        }

        return bestProject
    }

    Project createModeNeighbor(Project project, List<Integer> jobsToRandomize) {
        ChronoWatch.instance.pauseSolutionTime()
        Project neighborProject = CloneUtils.cloneProject(project)
        ChronoWatch.instance.startSolutionTime()

        def changedJobMode = false
        def times = 0

        while (!changedJobMode && times < totalJobsAndModes) {
            def job = randomizeJob(jobsToRandomize, neighborProject.staggeredJobs)
            def newMode = randomizeModeFromJob(job)

            if (!checkIfIsSameModeFromOriginalProject(project, job, newMode) && changeModeAndCheckNonRenewableResourcesRestriction(neighborProject, job, newMode)) {
                changedJobMode = true
            }

            times++
        }

        if (changedJobMode) {
            return neighborProject
        } else {
            return null
        }
    }

    Job randomizeJob(List<Integer> jobsToRandomize, List<Job> jobs) {
        def randomIndex = RandomUtils.nextInt(jobsToRandomize.size())

        return jobs.find { it.id == jobsToRandomize[randomIndex] }
    }

    Mode randomizeModeFromJob(Job job) {
        if (job.availableModes.size() > 1) {
            def clonedModes = CloneUtils.cloneModeList(job.availableModes)
            clonedModes.remove( clonedModes.find { it.id == job.mode.id } )
            def randomModeIndex = RandomUtils.nextInt(clonedModes.size())

            return clonedModes[randomModeIndex]
        } else {
            return job.mode
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

    private void checkBestSolution(Project neighborProject) {
        if (neighborProject.makespan < bestProject.makespan) {
            bestProject = neighborProject
            log.info("LOOKING FOR A BETTER SOLUTION - FILE: " + bestProject.fileName + " - NEIGHBOR MAKESPAN: " + bestProject.makespan)
        }
    }

    private void checkActualSolution(Project neighborProject) {
        if (neighborProject.makespan < actualSolution.makespan) {
            actualSolution = neighborProject
        } else {
            def difference = neighborProject.makespan - actualSolution.makespan
            if (Math.exp(-difference / temperature) > RandomUtils.nextDouble()) {
                actualSolution = neighborProject
            }
        }
    }

    private boolean checkIfIsSameModeFromOriginalProject(Project originalProject, Job jobTochange, Mode newMode) {
        Job originalJob = originalProject.staggeredJobs.find { it.id == jobTochange.id }

        if (originalJob.mode.id == newMode.id) {
            return true
        } else {
            return false
        }
    }

    List<Job> getOnlyJobsToRandomize(Project project) {
        def realJobs = jobOperations.getOnlyRealJobs(project.staggeredJobs, project.instanceInformation.jobsAmount)
        return realJobs.findAll { it.availableModes.size() > 1 }
    }

    Integer setTotalJobsAndModes(List<Job> jobs) {
        def total = 0

        jobs.each {
            total+= it.availableModes.size()
        }

        return totalJobsAndModes = total
    }

}
