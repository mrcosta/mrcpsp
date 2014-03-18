package mrcpsp.process.localsearch

import mrcpsp.model.main.Job
import mrcpsp.model.main.Project
import spock.lang.Specification

/**
 * Created by mateus on 2/19/14.
 */
class LocalSearchSpec extends Specification {

    def localSearch
    def project
    def job1, job2, job3, job4, job5, job6, job7, job8, job9, job10, job11, job12
    def staggeredJobs
    def criticalPath

    def setup() {
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
        criticalPath = [job12, job10, job8, job6, job5, job3, job1]
        project = new Project(staggeredJobs: staggeredJobs, criticalPath: criticalPath, makespan: 37, fileName: "test.txt")
    }

    def "Should try to change the job's mode only for the ones that are in the critical path"() {
        //criticalPath.id == [12, 10, 8, 6, 5, 3, 1]
        given:
        localSearch = Spy(LocalSearch)
        localSearch.checkSolution = true
        localSearch.bestProject = project
        localSearch.bestNeighbor = project
        localSearch.lnrc = Mock(LowerNonRenewableConsumption)

        when:
        localSearch.lowerNonRenewableComsumption(project, project.criticalPath)

        then:
        1 * localSearch.lnrc.changeExecutionModeJob(localSearch.bestProject, 10) >> null
        1 * localSearch.lnrc.changeExecutionModeJob(localSearch.bestProject, 8)  >> null
        1 * localSearch.lnrc.changeExecutionModeJob(localSearch.bestProject, 6)  >> null
        1 * localSearch.lnrc.changeExecutionModeJob(localSearch.bestProject, 5)  >> null
        1 * localSearch.lnrc.changeExecutionModeJob(localSearch.bestProject, 3)  >> null
    }


}
