package mrcpsp.process.perturbation

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.RunningJobInformation
import spock.lang.Specification

/**
 * Created by mateus on 4/26/14.
 */
class IteratedLocalSearchHelperSpec extends Specification {

    IteratedLocalSearchHelper ilsHelper

    def setup() {
        ilsHelper = new IteratedLocalSearchHelper()
    }

    def "should get the job begin interval (begin, middle and end)"() {
        when:
        def result = ilsHelper.getJobIntervals(jobsId, interval)

        then:
        result == jobsIdResult

        where:
        jobsId                                          | interval | jobsIdResult
        [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]         | 1        | [1, 2, 3, 4]
        [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14] | 1        | [1, 2, 3, 4]
        [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]         | 2        | [5, 6, 7, 8]
        [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14] | 2        | [5, 6, 7, 8]
        [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]         | 3        | [9, 10, 11, 12]
        [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14] | 3        | [9, 10, 11, 12, 13, 14]
    }

    def "should thrown an exception when passing an invalid interval"() {
        given:
        def jobsId = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14]
        def interval = 4

        when:
        ilsHelper.getJobIntervals(jobsId, interval)

        then:
        def e = thrown(IllegalArgumentException)
    }

    def "should get the jobs that aren't in the last mode"() {
        when:
        def result = ilsHelper.getJobsThatCanChangeItsMode(jobsIntervalId, jobs)

        then:
        result*.id == jobsId

        where:
        jobsIntervalId | jobs                                                                                                                                                           | jobsId
        [1, 2]         | [new Job(id: 1, mode: new Mode(id: 1)), new Job(id: 2, mode: new Mode(id: 3)), new Job(id: 3, mode: new Mode(id: 3)), new Job(id: 4, mode: new Mode(id: 3))]   | [1]
        [3, 4]         | [new Job(id: 1, mode: new Mode(id: 1)), new Job(id: 2, mode: new Mode(id: 2)), new Job(id: 3, mode: new Mode(id: 1)), new Job(id: 4, mode: new Mode(id: 2))]   | [3, 4]
        [1, 2, 3, 4]   | [new Job(id: 1, mode: new Mode(id: 1)), new Job(id: 2, mode: new Mode(id: 2)), new Job(id: 3, mode: new Mode(id: 3)), new Job(id: 4, mode: new Mode(id: 3))]   | [1, 2]
        [1, 4]         | [new Job(id: 1, mode: new Mode(id: 1)), new Job(id: 2, mode: new Mode(id: 1)), new Job(id: 3, mode: new Mode(id: 1)), new Job(id: 4, mode: new Mode(id: 2))]   | [1, 4]
        [2, 3]         | [new Job(id: 1, mode: new Mode(id: 3)), new Job(id: 2, mode: new Mode(id: 3)), new Job(id: 3, mode: new Mode(id: 3)), new Job(id: 4, mode: new Mode(id: 3))]   | []
    }

    def "should get the modes id that a job can change"() {
        when:
        def result = ilsHelper.getRemaningModesForJob(job)

        then:
        result*.id == modesId

        where:
        job                                                                                                          | modesId
        new Job(id: 1, mode: new Mode(id: 1), availableModes: [new Mode(id: 1), new Mode(id: 2), new Mode(id: 3)])   | [2, 3]
        new Job(id: 2, mode: new Mode(id: 2), availableModes: [new Mode(id: 1), new Mode(id: 2), new Mode(id: 3)])   | [3]
    }
}
