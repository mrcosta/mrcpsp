package mrcpsp.process.initialsolution

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.ModesInformation
import mrcpsp.model.main.Project
import spock.lang.Specification

/**
 * Created by mateus on 2/22/14.
 */
class LowerBoundProcessorSpec extends Specification {


    LowerBoundProcessor lowerBoundProcessor
    def project
    def job1, job2, job3, job4, job5, job6, job7, job8, job9, job10, job11, job12
    def jobs

    def setup() {
        lowerBoundProcessor = new LowerBoundProcessor()
    }


    def "Should get the lower bound for an especific project"() {
       given:
       job1 = new Job(id: 1, predecessors: [], availableModes: [new Mode(id: 1, duration: 0)], modesInformation: new ModesInformation(shorter: 1))
       job2 = new Job(id: 2, predecessors: [1], availableModes: [new Mode(id: 1, duration: 2)], modesInformation: new ModesInformation(shorter: 1))
       job3 = new Job(id: 3, predecessors: [1], availableModes: [new Mode(id: 1, duration: 4)], modesInformation: new ModesInformation(shorter: 1))
       job4 = new Job(id: 4, predecessors: [1], availableModes: [new Mode(id: 1, duration: 1)], modesInformation: new ModesInformation(shorter: 1))
       job5 = new Job(id: 5, predecessors: [3], availableModes: [new Mode(id: 1, duration: 5)], modesInformation: new ModesInformation(shorter: 1))
       job6 = new Job(id: 6, predecessors: [5], availableModes: [new Mode(id: 1, duration: 4)], modesInformation: new ModesInformation(shorter: 1))
       job7 = new Job(id: 7, predecessors: [4, 6], availableModes: [new Mode(id: 1, duration: 2)], modesInformation: new ModesInformation(shorter: 1))
       job8 = new Job(id: 8, predecessors: [6], availableModes: [new Mode(id: 1, duration: 2)], modesInformation: new ModesInformation(shorter: 1))
       job9 = new Job(id: 9, predecessors: [3], availableModes: [new Mode(id: 1, duration: 1)], modesInformation: new ModesInformation(shorter: 1))
       job10 = new Job(id: 10, predecessors: [2, 4, 8], availableModes: [new Mode(id: 1, duration: 7)], modesInformation: new ModesInformation(shorter: 1))
       job11 = new Job(id: 11, predecessors: [2, 7, 8], availableModes: [new Mode(id: 1, duration: 4)], modesInformation: new ModesInformation(shorter: 1))
       job12 = new Job(id: 12, predecessors: [9, 10, 11], availableModes: [new Mode(id: 1, duration: 0)], modesInformation: new ModesInformation(shorter: 1))
       jobs = [job1, job2, job3, job4, job5, job6, job7, job8, job9, job10, job11, job12]
       project = new Project(jobs: jobs, fileName: "test.txt")

        when:
        def result = lowerBoundProcessor.getLowerBoundFromSolution(project)

       then:
       result
       project.lowerBound == 22
    }

    def "Should get the lower bound for another project"() {
        given:
        job1 = new Job(id: 1, predecessors: [], availableModes: [new Mode(id: 1, duration: 0)], modesInformation: new ModesInformation(shorter: 1))
        job2 = new Job(id: 2, predecessors: [1], availableModes: [new Mode(id: 1, duration: 2)], modesInformation: new ModesInformation(shorter: 1))
        job3 = new Job(id: 3, predecessors: [1], availableModes: [new Mode(id: 1, duration: 3)], modesInformation: new ModesInformation(shorter: 1))
        job4 = new Job(id: 4, predecessors: [1], availableModes: [new Mode(id: 1, duration: 1)], modesInformation: new ModesInformation(shorter: 1))
        job5 = new Job(id: 5, predecessors: [2], availableModes: [new Mode(id: 1, duration: 4)], modesInformation: new ModesInformation(shorter: 1))
        job6 = new Job(id: 6, predecessors: [4], availableModes: [new Mode(id: 1, duration: 2)], modesInformation: new ModesInformation(shorter: 1))
        job7 = new Job(id: 7, predecessors: [3], availableModes: [new Mode(id: 1, duration: 1)], modesInformation: new ModesInformation(shorter: 1))
        job8 = new Job(id: 8, predecessors: [7, 2], availableModes: [new Mode(id: 1, duration: 7)], modesInformation: new ModesInformation(shorter: 1))
        job9 = new Job(id: 9, predecessors: [8, 6], availableModes: [new Mode(id: 1, duration: 4)], modesInformation: new ModesInformation(shorter: 1))
        job10 = new Job(id: 10, predecessors: [8, 6], availableModes: [new Mode(id: 1, duration: 4)], modesInformation: new ModesInformation(shorter: 1))
        job11 = new Job(id: 11, predecessors: [8, 5, 4], availableModes: [new Mode(id: 1, duration: 2)], modesInformation: new ModesInformation(shorter: 1))
        job12 = new Job(id: 12, predecessors: [11, 10, 9], availableModes: [new Mode(id: 1, duration: 0)], modesInformation: new ModesInformation(shorter: 1))
        jobs = [job1, job2, job3, job4, job5, job6, job7, job8, job9, job10, job11, job12]
        project = new Project(jobs: jobs, fileName: "test.txt")

        when:
        def result = lowerBoundProcessor.getLowerBoundFromSolution(project)

        then:
        result
        project.lowerBound == 15
    }

}
