package mrcpsp.process.mode

import mrcpsp.model.main.Mode
import mrcpsp.model.main.ResourceAvailabilities
import spock.lang.Specification

/**
 * Created by mateus on 2/24/14.
 */
class ShortestFeasibleModeSpec extends Specification {

    def ra

    def setup() {
        ra = new ResourceAvailabilities(remainingNonRenewableAmount : [12, 7], nonRenewableConsumedAmount: [17, 23], nonRenewableInitialAmount: [29, 30])
    }

    def "Should check if is possible to added an specific mode for a job given the resources amount"() {
        given:
        def sfm = new ShortestFeasibleMode()

        when:
        def checkAmount = sfm.checkResources(ra, mode)

        then:
        checkAmount == check

        where:
        mode                               | check
        new Mode(nonRenewable: [13, 0])	   | false
        new Mode(nonRenewable: [3, 8])	   | false
        new Mode(nonRenewable: [6, 0])     | true
        new Mode(nonRenewable: [15, 9])	   | false
        new Mode(nonRenewable: [0, 1])	   | true
        new Mode(nonRenewable: [12, 7])    | true
        new Mode(nonRenewable: [10, 10])   | false
        new Mode(nonRenewable: [3, 5])     | true
    }

    def "Should update the consumed and remaining resources amount"() {
        given:
        def sfm = new ShortestFeasibleMode()

        when:
        sfm.updateResources(ra, mode)

        then:
        ra.remainingNonRenewableAmount == remainingNonRenewableAmount
        ra.nonRenewableConsumedAmount == nonRenewableConsumedAmount

        where:
        mode                               | remainingNonRenewableAmount | nonRenewableConsumedAmount
        new Mode(nonRenewable: [6, 0])	   | [6, 7]               	     | [23, 23]
        new Mode(nonRenewable: [1, 1])     | [11, 6]             	     | [18, 24]
        new Mode(nonRenewable: [12, 7])	   | [0, 0] 			  		 | [29, 30]
        new Mode(nonRenewable: [0, 1])	   | [12, 6] 			  		 | [17, 24]
        new Mode(nonRenewable: [5, 5])     | [7, 2]            		     | [22, 28]
    }

    def "Should test this madness"() {
        given:
        def sfm = new ShortestFeasibleMode()

        when:
        def result = sfm.test()

        then:
        result == null
    }
}
