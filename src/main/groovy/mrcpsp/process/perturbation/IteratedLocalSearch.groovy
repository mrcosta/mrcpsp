package mrcpsp.process.perturbation

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.Project
import mrcpsp.model.main.ResourceAvailabilities
import mrcpsp.process.job.JobOperations
import mrcpsp.process.localsearch.LocalSearch
import mrcpsp.process.mode.ModeOperations
import mrcpsp.utils.ChronoWatch
import mrcpsp.utils.CloneUtils
import mrcpsp.utils.PropertyConstants
import org.apache.commons.lang.math.RandomUtils
import org.apache.log4j.Logger

/**
 * Created by mateus on 4/26/14.
 */
class IteratedLocalSearch {

    private static final Logger log = Logger.getLogger(IteratedLocalSearch.class)

    Project bestProject
    Project bestNeighbor
    boolean checkSolution

    IteratedLocalSearchHelper ilsHelper
    LocalSearchForPerturbation localSearchForPerturbation

    IteratedLocalSearch() {
        ilsHelper = new IteratedLocalSearchHelper()
        localSearchForPerturbation = new LocalSearchForPerturbation()
    }

    def ils(Project project) {
        bestProject = project
        bestNeighbor = project
        checkSolution = true

        while (checkSolution) {

            def intervalJobsIdList = getAndMergeJobIdIntervals(bestProject.staggeredJobs)

            intervalJobsIdList.each { intervalJobsId ->
                ChronoWatch.instance.pauseSolutionTime()
                def clonedProject = CloneUtils.cloneProject(bestProject)
                log.info("PERTURBATION - JOBS ID: " + clonedProject.staggeredJobs.id)
                ChronoWatch.instance.startSolutionTime()
                def eligibleJobs = ilsHelper.getJobsThatCanChangeItsMode(intervalJobsId, clonedProject.staggeredJobs)

                if (!eligibleJobs.isEmpty()) {
                    def randomIndex = RandomUtils.nextInt(eligibleJobs.size())
                    Job randomizedJob = eligibleJobs[randomIndex]

                    def remainingModes = ilsHelper.getRemaningModesForJob(randomizedJob)

                    remainingModes.each { mode ->
                        log.info("PERTURBATION - MODES ID: " + clonedProject.staggeredJobs.mode.id)
                        def isModeChanged = changeJobModeAndUpdateResourcesAvailabilities(clonedProject.resourceAvailabilities, randomizedJob, mode) // perturbation

                        if (isModeChanged) {
                            log.info("PERTURBATION - MODES ID AFTER PERTURBATION: " + clonedProject.staggeredJobs.mode.id)
                            def neighborProject = localSearchForPerturbation.executeLocalSearchForPerturbation(clonedProject, randomizedJob)

                            if (neighborProject) {
                                checkBestNeighbor(neighborProject);
                            }
                        }
                    }
                }
            }

            checkBestSolution(bestNeighbor, project)
        }

        if (bestProject) {
            return bestProject
        } else {
            return null
        }
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

    private List<List<Integer>> getAndMergeJobIdIntervals(List<Job> staggeredJobs) {
        def beginIntervalIds = ilsHelper.getJobIntervals(staggeredJobs*.id, 1)
        def middleIntervalIds = ilsHelper.getJobIntervals(staggeredJobs*.id, 2)
        def endIntervalIds = ilsHelper.getJobIntervals(staggeredJobs*.id, 3)

        def intervalsList = []
        intervalsList.add(beginIntervalIds)
        intervalsList.add(middleIntervalIds)
        intervalsList.add(endIntervalIds)

        return intervalsList
    }

    private boolean changeJobModeAndUpdateResourcesAvailabilities(ResourceAvailabilities ra, Job randomizedJob, Mode modeToChange) {
        ModeOperations modeOperations = new ModeOperations()
        def isModeChanged = false

        modeOperations.removingNonRenewableResources(ra, randomizedJob.mode)
        if (modeOperations.checkNonRenewableResources(ra, modeToChange)) {
            randomizedJob.mode = modeToChange
            isModeChanged = true
        }
        modeOperations.addingNonRenewableResources(ra, randomizedJob.mode)

        return isModeChanged
    }
}
