package mrcpsp.process.perturbation

import mrcpsp.model.main.Job
import mrcpsp.model.main.Project
import mrcpsp.utils.PropertyConstants

/**
 * Created by mateus on 4/26/14.
 */
class IteratedLocalSearch {

    Project bestProject
    Project bestNeighbor
    boolean checkSolution

    IteratedLocalSearchHelper ilsHelper

    IteratedLocalSearch() {
        ilsHelper = new IteratedLocalSearchHelper()
    }

    def ils(Project project) {
        bestProject = project
        bestNeighbor = project
        checkSolution = true

//        while (checkSolution) {

            def intervalJobsIdList = getAndMergeJobIdIntervals(project.staggeredJobs)

            println intervalJobsIdList

//            def neighborProject = sfmLS.changeExecutionModeBlockJob(bestProject, job.id)
//
//            if (neighborProject) {
//                mmProcessor.project = neighborProject
//                mmProcessor.executeGetJobTimes()
//                mmProcessor.setProjectMakespan()
//            }
//
//            if (neighborProject) {
//                checkBestNeighbor(neighborProject);
//            }
//
//            checkBestSolution(bestNeighbor, project)
//        }


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
