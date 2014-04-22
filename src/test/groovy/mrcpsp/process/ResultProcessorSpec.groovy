package mrcpsp.process

import mrcpsp.model.main.Project
import mrcpsp.model.main.Job
import spock.lang.Specification

/**
 * Created by mateus on 2/17/14.
 */
class ResultProcessorSpec extends Specification {

    def resultsProcessor
    def project
    def job1, job2, job3, job4, job5, job6, job7, job8, job9, job10, job11, job12
    def staggeredJobs

    def setup() {
        resultsProcessor =  new ResultsProcessor()
        job1 = new Job(id: 1, predecessors: [], startTime: 0, endTime: 0)
        job2 = new Job(id: 2, predecessors: [1], startTime: 0, endTime: 5)
        job3 = new Job(id: 3, predecessors: [1], startTime: 1, endTime: 5)
        job4 = new Job(id: 4, predecessors: [1], startTime: 0, endTime: 1)
        job5 = new Job(id: 5, predecessors: [3], startTime: 5, endTime: 12)
        job6 = new Job(id: 6, predecessors: [5], startTime: 12, endTime: 16)
        job7 = new Job(id: 7, predecessors: [4, 6], startTime: 16, endTime: 18)
        job8 = new Job(id: 8, predecessors: [6], startTime: 18, endTime: 20)
        job9 = new Job(id: 9, predecessors: [3], startTime: 5, endTime: 7)
        job10 = new Job(id: 10, predecessors: [2, 4, 8], startTime: 20, endTime: 27)
        job11 = new Job(id: 11, predecessors: [2, 7, 8], startTime: 20, endTime: 24)
        job12 = new Job(id: 12, predecessors: [9, 10, 11], startTime: 27, endTime: 27)
        staggeredJobs = [job1, job2, job3, job4, job5, job6, job7, job8, job9, job10, job11, job12]
        project = new Project(staggeredJobs: staggeredJobs, criticalPath: [])

    }

    def "Should get the critical path for the given project" () {
        when:
        def criticalPath = resultsProcessor.getCriticalPath(project)

        then:
        criticalPath != null
        criticalPath.size == 7
        criticalPath.id == [12, 10, 8, 6, 5, 3, 1]
    }

    def "Should get the critical path for the second given project" () {
        given:
        job7 = new Job(id: 7, predecessors: [4, 6], startTime: 18, endTime: 20)
        job8 = new Job(id: 8, predecessors: [6], startTime: 16, endTime: 18)
        job10 = new Job(id: 10, predecessors: [2, 4, 8], startTime: 5, endTime: 12)
        job12 = new Job(id: 12, predecessors: [9, 10, 11], startTime: 24, endTime: 24)
        staggeredJobs = [job1, job2, job3, job4, job5, job6, job7, job8, job9, job10, job11, job12]
        project = new Project(staggeredJobs: staggeredJobs, criticalPath: [])

        when:
        def criticalPath = resultsProcessor.getCriticalPath(project)

        then:
        criticalPath != null
        criticalPath.size == 7
        criticalPath.id == [12, 11, 7, 6, 5, 3, 1]
    }
}
