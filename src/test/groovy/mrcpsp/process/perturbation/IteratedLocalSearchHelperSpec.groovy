package mrcpsp.process.perturbation

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
}
