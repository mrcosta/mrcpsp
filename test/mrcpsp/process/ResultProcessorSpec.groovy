package mrcpsp.process

import mrcpsp.model.main.Project
import mrcpsp.model.main.Job
import spock.lang.Specification

/**
 * Created by mateus on 2/17/14.
 */
class ResultProcessorSpec extends Specification {

   /* [id: 1, modesAmount: 1, successorsAmount: 3, predecessorsAmount: 0, successors: [2, 3, 4], predecessors: [], staggeredPredecessors: [], {Start, End} =  { 0, 0 }
    [id: 2, modesAmount: 3, successorsAmount: 2, predecessorsAmount: 1, successors: [10, 11], predecessors: [1], staggeredPredecessors: [1], {Start, End} =  { 0, 5 }
    [id: 3, modesAmount: 3, successorsAmount: 2, predecessorsAmount: 1, successors: [5, 9], predecessors: [1], staggeredPredecessors: [1], {Start, End} =  { 1, 5 }
    [id: 4, modesAmount: 3, successorsAmount: 2, predecessorsAmount: 1, successors: [7, 10], predecessors: [1], staggeredPredecessors: [1], {Start, End} =  { 0, 1 }
    [id: 5, modesAmount: 3, successorsAmount: 1, predecessorsAmount: 1, successors: [6], predecessors: [3], staggeredPredecessors: [3], {Start, End} =  { 5, 12 }
    [id: 6, modesAmount: 3, successorsAmount: 2, predecessorsAmount: 1, successors: [7, 8], predecessors: [5], staggeredPredecessors: [5], {Start, End} =  { 12, 16 }
    [id: 7, modesAmount: 3, successorsAmount: 1, predecessorsAmount: 2, successors: [11], predecessors: [6, 4], staggeredPredecessors: [4, 6], {Start, End} =  { 18, 20 }
    [id: 8, modesAmount: 3, successorsAmount: 2, predecessorsAmount: 1, successors: [10, 11], predecessors: [6], staggeredPredecessors: [6], {Start, End} =  { 16, 18 }
    [id: 9, modesAmount: 3, successorsAmount: 1, predecessorsAmount: 1, successors: [12], predecessors: [3], staggeredPredecessors: [3], {Start, End} =  { 5, 7 }
    [id: 10, modesAmount: 3, successorsAmount: 1, predecessorsAmount: 3, successors: [12], predecessors: [8, 4, 2], staggeredPredecessors: [2, 4, 8], {Start, End} =  { 5, 12 }
    [id: 11, modesAmount: 3, successorsAmount: 1, predecessorsAmount: 3, successors: [12], predecessors: [8, 7, 2], staggeredPredecessors: [2, 8, 7], {Start, End} =  { 20, 24 }
    [id: 12, modesAmount: 1, successorsAmount: 0, predecessorsAmount: 3, successors: [], predecessors: [11, 10, 9], staggeredPredecessors: [9, 10, 11], {Start, End} =  { 24, 24 }
    criticalPath: 11, 7, 6, 5, 3, 1*/

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
