package mrcpsp.process.job

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.process.JobTimeProcessor
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by mateus on 3/18/14.
 */
class JobOperationsSpec extends Specification {

    @Shared job7, job8, job10, job12
    @Shared def jobs
    JobOperations jobOperations

    def setup() {
        jobOperations = new JobOperations()
    }

    def "should return the jobs between the interval of the job that was choosen to be scheduled"() {
        given:
        job7 = new Job(id: 7, mode: new Mode(renewable: [9, 0]), startTime: 15, endTime: 17)
        job8 = new Job(id: 8, mode: new Mode(renewable: [10, 0]), startTime: 13, endTime: 15)
        job10 = new Job(id: 10, mode: new Mode(renewable: [7, 0]), startTime: 15, endTime: 24)
        job12 = new Job(id: 12, mode: new Mode(renewable: [0, 0]), startTime: 0, endTime: 0)
        jobs = [job7, job8, job10, job12]

        when:
        def result = jobOperations.getJobsBetweenInterval(job10, jobs)

        then:
        result.size() == 1
        result[0].id == 7
    }

    def "should return the jobs between the interval of the job that was choosen to be scheduled - corner case"() {
        given:
        def dumbJob2 = new Job(id: 2, mode: new Mode(renewable: [0, 3]), startTime: 0, endTime: 5)
        def dumbJob4 = new Job(id: 4, mode: new Mode(renewable: [5, 0]), startTime: 0, endTime: 6)
        def dumbJob5 = new Job(id: 5, mode: new Mode(renewable: [0, 7]), startTime: 4, endTime: 9)
        def dumbJob9 = new Job(id: 9, mode: new Mode(renewable: [0, 2]), startTime: 4, endTime: 6)

        def dumbJob1 = new Job(id: 1, mode: new Mode(renewable: [0, 0]))
        def dumbJob6 = new Job(id: 6, mode: new Mode(renewable: [0, 6]))
        def dumbJob7 = new Job(id: 7, mode: new Mode(renewable: [9, 0]))
        def dumbJob11 = new Job(id: 11, mode: new Mode(renewable: [0, 10]))
        def dumbJob12 = new Job(id: 9, mode: new Mode(renewable: [0, 2]), startTime: 0, endTime: 0)

        jobs = [dumbJob1, dumbJob4, dumbJob2, dumbJob9, dumbJob5, dumbJob6, dumbJob7, dumbJob11, dumbJob12]

        when:
        def result = jobOperations.getJobsBetweenInterval(jobToSchedule, jobs)

        then:
        result.size() == 3
        result*.id  == idJobs

        where:
        jobToSchedule                                                                 | idJobs
        new Job(id: 2, mode: new Mode(renewable: [0, 3]), startTime: 0, endTime: 5)	  | [4, 9, 5]
        new Job(id: 4, mode: new Mode(renewable: [5, 0]), startTime: 0, endTime: 6)	  | [2, 9, 5]
        new Job(id: 5, mode: new Mode(renewable: [0, 7]), startTime: 4, endTime: 9)   | [4, 2, 9]
        new Job(id: 9, mode: new Mode(renewable: [0, 2]), startTime: 4, endTime: 6)	  | [4, 2, 5]
    }

    def "should return the jobs between the interval of the job that was choosen to be scheduled - corner case 2"() {
        given:
        def dumbJob2 = new Job(id: 7, mode: new Mode(renewable: [9, 0]), startTime: 0, endTime: 5)
        def dumbJob4 = new Job(id: 4, mode: new Mode(renewable: [9, 0]), startTime: 0, endTime: 6)
        def dumbJob5 = new Job(id: 8, mode: new Mode(renewable: [10, 0]), startTime: 4, endTime: 9)
        def dumbJob9 = new Job(id: 10, mode: new Mode(renewable: [7, 0]), startTime: 4, endTime: 6)
        def dumbJob10 = new Job(id: 10, mode: new Mode(renewable: [7, 0]), startTime: 0, endTime: 0)
        def dumbJobs = [dumbJob4, dumbJob2, dumbJob9, dumbJob5, dumbJob10]

        when:
        def result = jobOperations.getJobsBetweenInterval(dumbJob5, dumbJobs)

        then:
        result.size() == 3
        result[0].id == 4
    }

    def "should get all predecessors of a job"() {
        given:
        def dumbJob1 = new Job(id: 1, mode: new Mode(renewable: [0, 0]))
        def dumbJob2 = new Job(id: 2, mode: new Mode(renewable: [0, 3]))
        def dumbJob3 = new Job(id: 3, mode: new Mode(renewable: [0, 8]))
        def dumbJob4 = new Job(id: 4, mode: new Mode(renewable: [5, 0]))
        def dumbJob5 = new Job(id: 5, mode: new Mode(renewable: [0, 7]))
        def dumbJob6 = new Job(id: 6, mode: new Mode(renewable: [0, 6]))
        def dumbJob7 = new Job(id: 7, mode: new Mode(renewable: [9, 0]))

        def dumbjob8 = new Job(id: 8, mode: new Mode(renewable: [10, 0]), predecessors: [1, 3, 5])
        def dumbJobs = [dumbJob1, dumbJob5, dumbJob4, dumbJob2, dumbJob3, dumbJob6, dumbJob7]

        when:
        def predecessors = jobOperations.getJobPredecessors(dumbjob8, dumbJobs)

        then:
        predecessors != null
        predecessors.size() == 3
        predecessors*.id == [1, 5, 3]
        predecessors*.id != [2, 5, 6, 7]
    }

}
