package mrcpsp.process.perturbation

import mrcpsp.model.enums.EnumLocalSearch
import mrcpsp.model.main.Job
import mrcpsp.model.main.Project
import mrcpsp.process.MmProcessor
import mrcpsp.process.job.JobOperations
import mrcpsp.process.localsearch.LowerNonRenewableConsumption
import mrcpsp.process.localsearch.ShortestFeasibleModeLS
import mrcpsp.utils.PropertyConstants
import mrcpsp.utils.UrlUtils
import org.apache.log4j.Level
import org.apache.log4j.Logger

/**
 * Created by mateus on 4/27/14.
 */
class LocalSearchForPerturbation {

    static final Logger log = Logger.getLogger(LocalSearchForPerturbation.class);

    Project bestProject
    Project bestNeighbor
    boolean checkSolution
    MmProcessor mmProcessor
    LowerNonRenewableConsumption lnrc
    JobOperations jobOperations

    LocalSearchForPerturbation() {
        mmProcessor = new MmProcessor()
        lnrc = new LowerNonRenewableConsumption()
        jobOperations = new JobOperations()
    }

    def Project executeLocalSearchForPerturbation(Project project, Job randomizedJob) {
        def localSearch = UrlUtils.instance.localSearch
        bestProject = project
        bestNeighbor = project
        checkSolution = true

        log.info("LOCAL SEARCH: " + localSearch)
        def realJobs = jobOperations.getOnlyRealJobs(project.staggeredJobsId, project.instanceInformation.jobsAmount)
        realJobs = realJobs.findAll { it.id != randomizedJob.id }
        jobsBlockSFM(project, realJobs, randomizedJob)

        if (bestProject) {
            return bestProject
        } else {
            return null
        }
    }

    def jobsBlockSFM(Project project, List<Job> jobs, Job randomizedJob) {
        ShortestFeasibleModeLSForPerturbation sfmLSForPerturbation = new ShortestFeasibleModeLSForPerturbation()

        while (checkSolution) {

            jobs.each { job ->
                def neighborProject = sfmLSForPerturbation.changeExecutionModeBlockJob(bestProject, job.id, randomizedJob.id)

                if (neighborProject) {
                    mmProcessor.project = neighborProject
                    mmProcessor.executeGetJobTimes()
                    mmProcessor.setProjectMakespan()
                }

                if (neighborProject) {
                    checkBestNeighbor(neighborProject);
                }
            }

            checkBestSolution(bestNeighbor, project)
        }

        return bestProject
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
}
