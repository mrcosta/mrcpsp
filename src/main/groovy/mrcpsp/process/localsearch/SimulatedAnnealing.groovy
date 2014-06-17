package mrcpsp.process.localsearch

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.Project
import mrcpsp.model.main.ResourceAvailabilities
import mrcpsp.process.MmProcessor
import mrcpsp.process.job.JobOperations
import mrcpsp.process.mode.ModeOperations
import mrcpsp.utils.UrlUtils
import org.apache.commons.lang.math.RandomUtils
import org.apache.log4j.Logger

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

    Integer originalJobId
    Mode originalMode

    Project bestProject
    Project actualSolution

    SimulatedAnnealing() {
        modeOperations = new ModeOperations()
        mmProcessor = new MmProcessor()
        jobOperations = new JobOperations()

        bestProject = new Project(staggeredJobsId: [], modes: [:], times: [:])
        actualSolution = new Project(staggeredJobsId: [], modes: [:], times: [:])
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
        bestProject.setProject(project)
        actualSolution.setProject(project)

        def jobs = project.jobs
        def ra = project.resourceAvailabilities

        def jobsToRandomize = getOnlyJobsToRandomize(jobs, project.jobsAmount)
        def jobsIdToRandomize = jobsToRandomize.id
        setTotalJobsAndModes(jobsToRandomize)
        initParameterValues()

        while (temperature > stoppingCriterion) {

            totalNeighbor.times {
                def job = createModeNeighbor(jobs, jobsIdToRandomize, ra)

                if (job) {
                    project.modes."$job.id" = job.mode.id

                    mmProcessor.project = project
                    mmProcessor.executeGetJobTimesAndSetMakespan()
                    checkActualSolution(project)
                    checkBestSolution(project)
                }
            }

            temperature*= reductionCoefficient
        }

        return bestProject
    }

    Job createModeNeighbor(List<Job> jobs, List<Integer> jobsIdToRandomize, ResourceAvailabilities resourceAvailabilities) {
        def job = randomizeJob(jobsIdToRandomize, jobs)
        def newMode = randomizeModeFromJob(job)

        originalJobId = job.id
        originalMode = job.mode

        if (newMode && changeModeAndCheckNonRenewableResourcesRestriction(resourceAvailabilities, job, newMode)) {
            return job
        } else {
            return null
        }
    }

    Job randomizeJob(List<Integer> jobsIdToRandomize, List<Job> jobs) {
        def randomIndex = RandomUtils.nextInt(jobsIdToRandomize.size())
        return jobs[jobsIdToRandomize[randomIndex] - 1]
    }

    Mode randomizeModeFromJob(Job job) {
        if (job.availableModes.size() > 1) {
            def jobsToRandomize = job.availableModes
            jobsToRandomize.remove( jobsToRandomize.find { it.id == job.mode.id } )
            def randomModeIndex = RandomUtils.nextInt(jobsToRandomize.size())

            return jobsToRandomize[randomModeIndex]
        } else {
            return null
        }
    }

    def changeModeAndCheckNonRenewableResourcesRestriction(ResourceAvailabilities resourceAvailabilities, Job job, Mode newMode) {
        modeOperations.removingNonRenewableResources(resourceAvailabilities, job.mode)

        def checkResources = modeOperations.checkNonRenewableResources(resourceAvailabilities, newMode)
        if (checkResources) {
            job.mode = newMode
        }

        modeOperations.addingNonRenewableResources(resourceAvailabilities, job.mode)

        return checkResources
    }

    private void checkBestSolution(Project neighborProject) {
        if (neighborProject.makespan < bestProject.makespan) {
            bestProject.setProject(neighborProject)
            log.info("LOOKING FOR A BETTER SOLUTION - FILE: " + bestProject.fileName + " - NEIGHBOR MAKESPAN: " + bestProject.makespan)
        }
    }

    private void checkActualSolution(Project neighborProject) {
        if (neighborProject.makespan < actualSolution.makespan) {
            actualSolution.setProject(neighborProject)
        } else {
            def difference = neighborProject.makespan - actualSolution.makespan
            if (Math.exp(-difference / temperature) > RandomUtils.nextDouble()) {
                actualSolution.setProject(neighborProject)
            } else {
                backJobToOriginalMode(neighborProject)
            }
        }
    }

    List<Job> getOnlyJobsToRandomize(List<Job> jobs, Integer jobsAmount) {
        def realJobs = jobOperations.getOnlyRealJobs(jobs, jobsAmount)
        return realJobs.findAll { it.availableModes.size() > 1 }
    }

    Integer setTotalJobsAndModes(List<Job> jobs) {
        def total = 0

        jobs.each {
            total+= it.availableModes.size()
        }

        return totalJobsAndModes = total
    }

    private backJobToOriginalMode(Project project) {
        Job job = project.jobs[originalJobId - 1]

        modeOperations.removingNonRenewableResources(project.resourceAvailabilities, job.mode)
        job.mode = originalMode
        project.modes."$job.id" = originalMode.id
        modeOperations.addingNonRenewableResources(project.resourceAvailabilities, job.mode)
    }

}
