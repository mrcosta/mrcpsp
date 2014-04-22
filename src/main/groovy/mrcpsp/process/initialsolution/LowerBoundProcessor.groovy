package mrcpsp.process.initialsolution

import mrcpsp.model.main.Job
import mrcpsp.model.main.Project

/**
 * Created by mateus on 2/22/14.
 */
class LowerBoundProcessor {

    def getLowerBoundFromSolution(Project project) {

        project.jobs.each { job ->

            if (job.predecessors.isEmpty()) {
                job.startTime = 0
                job.endTime = job.availableModes.find{ it.id == job.modesInformation.shorter }.duration
            } else {
                List<Job> predecessorsJobs = []
                job.predecessors.each { predecessor ->
                    predecessorsJobs.add( project.jobs.find{it.id == predecessor} )
                }

                predecessorsJobs.sort {it.endTime}
                job.startTime = predecessorsJobs.last().endTime
                job.endTime = job.startTime + job.availableModes.find{ it.id == job.modesInformation.shorter }.duration
            }
        }

        project.lowerBound = project.jobs.last().endTime

        return true
    }
}
