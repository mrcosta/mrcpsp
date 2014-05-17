package mrcpsp.process.localsearch

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.ModesInformation
import mrcpsp.model.main.Project
import mrcpsp.model.main.ResourceAvailabilities
import spock.lang.Specification

/**
 * Created by mateus on 2/19/14.
 */
class LocalSearchSpec extends Specification {

    LocalSearch localSearch
    def project
    def job1, job2, job3, job4, job5, job6, job7, job8, job9, job10, job11, job12
    def staggeredJobs
    def criticalPath

    def setup() {
        localSearch = new LocalSearch()

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

    def "Should try to change the block of job's mode based in the shortest feasible mode"() {
        //The STAGGERED JOBS jobs list has this index: { 1, 2, 7, 4, 3, 5, 11, 10, 6, 8, 9, 12 } --> j1012_10.mm
        given:
        def dumbJob1 = new Job(id: 1, mode: new Mode(id: 1, nonRenewable: [0, 0], duration: 0), startTime: 0, endTime: 0)
        def dumbJob2 = new Job(id: 2, predecessors: [1], mode: new Mode(id: 3, nonRenewable: [0, 2], duration: 10), startTime: 0, endTime: 10, availableModes: [new Mode(id: 1, nonRenewable: [0, 6], duration: 5)], modesInformation: new ModesInformation(modesByOrderDuration: [1], minNonRenewableResourcesConsumption: [0, 0]))
        def dumbJob3 = new Job(id: 3, predecessors: [1], mode: new Mode(id: 2, nonRenewable: [0, 3], duration: 8), startTime: 0, endTime: 8, availableModes: [new Mode(id: 1, nonRenewable: [6, 0], duration: 2)], modesInformation: new ModesInformation(modesByOrderDuration: [1], minNonRenewableResourcesConsumption: [0, 0]))
        def dumbJob4 = new Job(id: 4, predecessors: [1], mode: new Mode(id: 3, nonRenewable: [3, 0], duration: 3), startTime: 0, endTime: 10, availableModes: [new Mode(id: 1, nonRenewable: [5, 0], duration: 1)], modesInformation: new ModesInformation(modesByOrderDuration: [1], minNonRenewableResourcesConsumption: [3, 0]))
        def dumbJob5 = new Job(id: 5, predecessors: [4], mode: new Mode(id: 3, nonRenewable: [2, 0], duration: 4), startTime: 10, endTime: 14, availableModes: [new Mode(id: 1, nonRenewable: [3, 0], duration: 2)], modesInformation: new ModesInformation(modesByOrderDuration: [1], minNonRenewableResourcesConsumption: [0, 0]))
        def dumbJob6 = new Job(id: 6, predecessors: [3, 2], mode: new Mode(id: 3, nonRenewable: [7, 0], duration: 10), startTime: 19, endTime: 29, availableModes: [new Mode(id: 1, nonRenewable: [0, 9], duration: 1)], modesInformation: new ModesInformation(modesByOrderDuration: [1], minNonRenewableResourcesConsumption: [0, 0]))
        def dumbJob7 = new Job(id: 7, predecessors: [2], mode: new Mode(id: 2, nonRenewable: [3, 0], duration: 7), startTime: 10, endTime: 17, availableModes: [new Mode(id: 1, nonRenewable: [0, 10], duration: 1)], modesInformation: new ModesInformation(modesByOrderDuration: [1], minNonRenewableResourcesConsumption: [0, 0]))
        def dumbJob8 = new Job(id: 8, predecessors: [7, 6], mode: new Mode(id: 3, nonRenewable: [0, 7], duration: 5), startTime: 29, endTime: 34, availableModes: [new Mode(id: 1, nonRenewable: [8, 0], duration: 1)], modesInformation: new ModesInformation(modesByOrderDuration: [1], minNonRenewableResourcesConsumption: [0, 0]))
        def dumbJob9 = new Job(id: 9, predecessors: [8, 4], mode: new Mode(id: 3, nonRenewable: [4, 0], duration: 6), startTime: 34, endTime: 40, availableModes: [new Mode(id: 1, nonRenewable: [0, 8], duration: 1)], modesInformation: new ModesInformation(modesByOrderDuration: [1], minNonRenewableResourcesConsumption: [0, 0]))
        def dumbJob10 = new Job(id: 10, predecessors: [7, 4, 3], mode: new Mode(id: 3, nonRenewable: [3, 0], duration: 10), startTime: 19, endTime: 29, availableModes: [new Mode(id: 1, nonRenewable: [0, 5], duration: 3)], modesInformation: new ModesInformation(modesByOrderDuration: [1], minNonRenewableResourcesConsumption: [0, 0]))
        def dumbJob11 = new Job(id: 11, predecessors: [5], mode: new Mode(id: 3, nonRenewable: [0, 1], duration: 5), startTime: 14, endTime: 19, availableModes: [new Mode(id: 1, nonRenewable: [0, 4], duration: 1)], modesInformation: new ModesInformation(modesByOrderDuration: [1], minNonRenewableResourcesConsumption: [0, 1]))
        def dumbJob12 = new Job(id: 12, predecessors: [11, 10, 9], mode: new Mode(id: 1, nonRenewable: [0, 0], duration: 0), startTime: 40, endTime: 40)
        def realJobs = [dumbJob2, dumbJob7, dumbJob4, dumbJob3, dumbJob5, dumbJob11, dumbJob10, dumbJob6, dumbJob8, dumbJob9]
        def dumbJobs = [dumbJob1, dumbJob2, dumbJob7, dumbJob4, dumbJob3, dumbJob5, dumbJob11, dumbJob10, dumbJob6, dumbJob8, dumbJob9, dumbJob12]
        project = new Project(staggeredJobs: dumbJobs, makespan: 40, resourceAvailabilities: new ResourceAvailabilities(nonRenewableConsumedAmount: [22, 13], nonRenewableInitialAmount: [46, 56], remainingNonRenewableAmount: [24, 43]))
        localSearch.checkSolution = true
        localSearch.bestProject = project
        localSearch.bestNeighbor = project

        when:
        def result = localSearch.jobsBlockSFM(project, realJobs)

        then:
        result != null
        localSearch.bestProject.makespan == 9
        //localSearch.bestProject.staggeredJobsId*.mode.id == [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
    }

}
