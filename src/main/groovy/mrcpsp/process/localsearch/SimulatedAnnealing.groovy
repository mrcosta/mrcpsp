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

    Integer totalSteps
    Integer totalNeighbor

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
        temperature = 50
        reductionCoefficient = 0.98
        stoppingCriterion = 30

        totalSteps = 10
        totalNeighbor = 10
    }

    Project executeSimulatedAnnealing(Project project) {
        bestProject = project
        actualSolution = project
        initParameterValues()

        while (temperature > stoppingCriterion) {

            totalSteps.times { step ->
                actualSolution.staggeredJobs = executeJobsPerturbation(actualSolution.staggeredJobs) ?: actualSolution.staggeredJobs

                totalNeighbor.times { neighbor ->
                    def neighborProject = createModeNeighbor(actualSolution)

                    if (neighborProject) {
                        mmProcessor.project = neighborProject
                        mmProcessor.executeGetJobTimes()
                        mmProcessor.setProjectMakespan()
                    }

                    if (neighborProject) {
                        checkActualSolution(neighborProject)
                        checkBestSolution(neighborProject);
                    }
                }

                temperature*= reductionCoefficient
            }

        }

        return bestProject
    }

    List<Job> executeJobsPerturbation(List<Job> jobs) {
        List<Job> jobsAfterPerturbation = CloneUtils.cloneJobList(jobs)
        jobsAfterPerturbation = new JobPriorityRulesOperations().getJobListOrderByEndTime(jobs)
        if (jobs.id != jobsAfterPerturbation.id) {
            return jobsAfterPerturbation
        } else {
            return null
        }
    }

    Project createModeNeighbor(Project project) {
        ChronoWatch.instance.pauseSolutionTime()
        Project neighborProject = CloneUtils.cloneProject(project)
        ChronoWatch.instance.startSolutionTime()

        def changedJobMode = false
        def jobsToRandomize = jobOperations.getOnlyRealJobs(neighborProject.staggeredJobs, neighborProject.instanceInformation.jobsAmount).id

        while (!changedJobMode && !jobsToRandomize.isEmpty()) {
            def job = randomizeJob(jobsToRandomize, neighborProject.staggeredJobs)
            def newMode = randomizeModeFromJob(job)

            if (changeModeAndCheckNonRenewableResourcesRestriction(neighborProject, job, newMode)) {
                changedJobMode = true
            } else {
                jobsToRandomize.remove( (Object) job.id)
            }
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

            if (Math.exp(-difference / temperature) > RandomUtils.nextInt(1)) {
                actualSolution = neighborProject
            }
        }
    }

}
