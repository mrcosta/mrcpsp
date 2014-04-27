package mrcpsp.process.perturbation

import mrcpsp.model.main.Job
import mrcpsp.model.main.Project
import mrcpsp.process.job.JobOperations
import mrcpsp.process.localsearch.LocalSearch
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
                def eligibleJobs = ilsHelper.getJobsThatCanChangeItsMode(intervalJobsId, bestProject.staggeredJobs)

                if (!eligibleJobs.isEmpty()) {
                    def randomIndex = RandomUtils.nextInt(eligibleJobs.size())
                    Job randomizedJob = eligibleJobs[randomIndex]

                    def remainingModes = ilsHelper.getRemaningModesForJob(randomizedJob)

                    remainingModes.each { mode ->
                        log.info("PERTURBATION - JOBS ID: " + project.staggeredJobs.id)
                        log.info("PERTURBATION - MODES ID: " + project.staggeredJobs.mode.id)
                        randomizedJob.mode = mode               // perturbation

                        log.info("PERTURBATION - MODES ID AFTER PERTURBATION: " + project.staggeredJobs.mode.id)
                        def neighborProject = localSearchForPerturbation.executeLocalSearchForPerturbation(project, randomizedJob)

                        if (neighborProject) {
                            checkBestNeighbor(neighborProject);
                        }
                    }
                }
            }

            checkBestSolution(bestNeighbor, project)
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
}
