package mrcpsp.process

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.ResourceAvailabilities
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by mateus on 3/13/14.
 */
class JobTimeProcessorSpec extends Specification {

    def ra
    @Shared job1, job2, job3, job4, job5, job6, job7, job8, job9, job10, job11, job12
    @Shared def jobs
    JobTimeProcessor jobTimeProcessor

    def setup() {
        jobTimeProcessor = new JobTimeProcessor()

        job1 = new Job(id: 1, mode: new Mode(duration: 0, renewable: [0, 0]), predecessors: [])
        job2 = new Job(id: 2, mode: new Mode(duration: 5, renewable: [0, 3]), predecessors: [1])
        job3 = new Job(id: 3, mode: new Mode(duration: 4, renewable: [0, 8]), predecessors: [1])
        job4 = new Job(id: 4, mode: new Mode(duration: 6, renewable: [5, 0]), predecessors: [1])
        job5 = new Job(id: 5, mode: new Mode(duration: 5, renewable: [0, 7]), predecessors: [3])
        job6 = new Job(id: 6, mode: new Mode(duration: 4, renewable: [0, 6]), predecessors: [5])
        job7 = new Job(id: 7, mode: new Mode(duration: 2, renewable: [9, 0]), predecessors: [6, 4])
        job8 = new Job(id: 8, mode: new Mode(duration: 2, renewable: [10, 0]), predecessors: [6])
        job9 = new Job(id: 9, mode: new Mode(duration: 2, renewable: [0, 2]), predecessors: [3])
        job10 = new Job(id: 10, mode: new Mode(duration: 9, renewable: [7, 0]), predecessors: [8, 4, 2])
        job11 = new Job(id: 11, mode: new Mode(duration: 4, renewable: [0, 10]), predecessors: [8, 2])
        job12 = new Job(id: 12, mode: new Mode(duration: 0, renewable: [0, 0]), predecessors: [11, 10, 9])
        // scheduling order: { 1, 3, 4, 2, 9, 5, 6, 8, 7, 11, 10, 12 } --

        jobs = [job1, job3, job4, job2, job9, job5, job6, job8, job7, job11, job10, job12]

        ra = new ResourceAvailabilities(remainingRenewableAmount : [10, 13], renewableConsumedAmount: [0, 0], renewableInitialAmount: [10, 13])
    }

    def "should set time using the scheduled jobs testing only one resource"() {
        given:
        def dumbJob2 = new Job(id: 2, mode: new Mode(renewable: [4, 0]), startTime: 0, endTime: 5)
        def dumbJob4 = new Job(id: 4, mode: new Mode(renewable: [4, 0]), startTime: 0, endTime: 6)
        def dumbJob9 = new Job(id: 9, mode: new Mode(renewable: [2, 0]), startTime: 4, endTime: 6)

        def dumbJob5 = new Job(id: 5, mode: new Mode(duration: 5, renewable: [9, 0]))

        def ra = new ResourceAvailabilities(remainingRenewableAmount : [0, 13], renewableConsumedAmount: [10, 0], renewableInitialAmount: [10, 13])
        ra.scheduledJobs = [dumbJob4, dumbJob2, dumbJob9]

        when:
        def job = jobTimeProcessor.setJobTimeUsingScheduledJobs(ra, dumbJob5)

        then:
        job != null
        job.startTime == 6
        job.endTime == 11
        ra.scheduledJobs.isEmpty()
        ra.scheduledJobs.size() == 0
        ra.renewableConsumedAmount == [0, 0]
        ra.remainingRenewableAmount == ra.renewableInitialAmount
    }

    def "should set time using the scheduled jobs testing two resources"() {
        given:
        def dumbJob2 = new Job(id: 2, mode: new Mode(renewable: [4, 5]), startTime: 0, endTime: 5)
        def dumbJob4 = new Job(id: 4, mode: new Mode(renewable: [4, 3]), startTime: 0, endTime: 6)
        def dumbJob9 = new Job(id: 9, mode: new Mode(renewable: [2, 3]), startTime: 4, endTime: 6)

        def dumbJob5 = new Job(id: 5, mode: new Mode(duration: 5, renewable: [3, 3]))

        def ra = new ResourceAvailabilities(remainingRenewableAmount : [0, 2], renewableConsumedAmount: [10, 11], renewableInitialAmount: [10, 13])
        ra.scheduledJobs = [dumbJob4, dumbJob2, dumbJob9]

        when:
        def job = jobTimeProcessor.setJobTimeUsingScheduledJobs(ra, dumbJob5)

        then:
        job != null
        job.startTime == 5
        job.endTime == 10
    }

    def "should set time using the scheduled jobs - corner case"() {
        given:
        def dumbJob7 = new Job(id: 2, mode: new Mode(renewable: [9, 0]), startTime: 15, endTime: 17)

        def dumbJob10 = new Job(id: 5, mode: new Mode(duration: 9, renewable: [7, 0]))

        def ra = new ResourceAvailabilities(remainingRenewableAmount : [1, 13], renewableConsumedAmount: [9, 0], renewableInitialAmount: [10, 13])
        ra.scheduledJobs = [dumbJob7]

        when:
        def job = jobTimeProcessor.setJobTimeUsingScheduledJobs(ra, dumbJob10)

        then:
        job != null
        job.startTime == 17
        job.endTime == 26
        ra.scheduledJobs.isEmpty()
        ra.scheduledJobs.size() == 0
        ra.renewableConsumedAmount == [0, 0]
        ra.remainingRenewableAmount == ra.renewableInitialAmount
    }

    def "should scheduling a job using its predecessors"() {
        given:
        def dumbJob1 = new Job(id: 1, mode: new Mode(renewable: [0, 0]))
        def dumbJob2 = new Job(id: 2, mode: new Mode(renewable: [0, 3]), startTime: 0, endTime: 5)
        def dumbJob3 = new Job(id: 3, mode: new Mode(renewable: [0, 8]))
        def dumbJob4 = new Job(id: 4, mode: new Mode(renewable: [5, 0]), startTime: 0, endTime: 6)
        def dumbJob5 = new Job(id: 5, mode: new Mode(renewable: [0, 7]))

        def dumbJob6 = new Job(id: 6, mode: new Mode(duration: 4, renewable: [0, 6]), predecessors: [2, 4])

        def ra = new ResourceAvailabilities(remainingRenewableAmount : [10, 13], renewableConsumedAmount: [0, 0], renewableInitialAmount: [10, 13])
        def dumbJobs = [dumbJob1, dumbJob5, dumbJob4, dumbJob2, dumbJob3, dumbJob6]

        when:
        def scheduledJob = jobTimeProcessor.setTimeJobWithPredecessors(ra, dumbJob6, dumbJobs)

        then:
        scheduledJob != null
        scheduledJob.startTime == 6
        scheduledJob.endTime == 10
        ra.scheduledJobs.isEmpty()
        ra.scheduledJobs.size() == 0
        ra.renewableConsumedAmount == [0, 0]
        ra.remainingRenewableAmount == ra.renewableInitialAmount
    }

    def "should scheduling a job using its scheduled jobs"() {
        given:
        def dumbJob2 = new Job(id: 1, mode: new Mode(duration: 3, renewable: [5, 3]))

        def ra = new ResourceAvailabilities(remainingRenewableAmount : [10, 13], renewableConsumedAmount: [0, 0], renewableInitialAmount: [10, 13])
        def dumbJobs = []

        when:
        def scheduledJob = jobTimeProcessor.setTimeJobWithoutPredecessors(ra, dumbJob2, dumbJobs)

        then:
        scheduledJob != null
        scheduledJob.startTime == 0
        scheduledJob.endTime == 3
        ra.scheduledJobs.isEmpty()
        ra.scheduledJobs.size() == 0
        ra.renewableConsumedAmount == [0, 0]
        ra.remainingRenewableAmount == ra.renewableInitialAmount
    }

    def "should scheduling a job using its scheduled jobs - corner case"() {
        given:
        def dumbJob2 = new Job(id: 2, mode: new Mode(duration: 2, renewable: [5, 3]), startTime: 0, endTime: 3)
        def dumbJob5 = new Job(id: 5, mode: new Mode(duration: 3, renewable: [5, 10]), startTime: 2, endTime: 4)

        def jobToSchedule = new Job(id: 7, mode: new Mode(duration: 5, renewable: [5, 3]))

        def ra = new ResourceAvailabilities(remainingRenewableAmount : [10, 13], renewableConsumedAmount: [0, 0], renewableInitialAmount: [10, 13])
        def dumbJobs = [dumbJob5, dumbJob2]

        when:
        def scheduledJob = jobTimeProcessor.setTimeJobWithoutPredecessors(ra, jobToSchedule, dumbJobs)

        then:
        scheduledJob != null
        scheduledJob.startTime == 3
        scheduledJob.endTime == 8
        ra.scheduledJobs.isEmpty()
        ra.scheduledJobs.size() == 0
        ra.renewableConsumedAmount == [0, 0]
        ra.remainingRenewableAmount == ra.renewableInitialAmount
    }

    def "should scheduling all jobs"() {
        when:
        def jobs = jobTimeProcessor.getJobTimes(ra, jobs)

        then:
        jobs.size() == 12
        ra.scheduledJobs.isEmpty()
        ra.scheduledJobs.size() == 0
        ra.renewableConsumedAmount == [0, 0]
        ra.remainingRenewableAmount == ra.renewableInitialAmount
    }
}
