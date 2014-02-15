package mrcpsp.process.mode
/**
 * Created by mateus on 2/15/14.
 */
class ModeComparatorProcessorSpec {

    def "should check if there are shorters mode near the lower non renewable consumption mode"() {
        given:
        def mcp = new ModeComparatorProcessor()

        when:
        def result = mcp.checkModeNearLowerNRConsumption(project, jobPosition)

        then:
        result == check
        project.resourceAvailabilities.remainingNonRenewableAmount == remainingNonRenewableAmount
        project.resourceAvailabilities.nonRenewableConsumedAmount == nonRenewableConsumedAmount
        project.staggeredJobs[jobPosition].mode.nonRenewable == modeNonRenewable

        where:
        job		| indexLowerNRConsumption | nonRenewableConsumedAmount  | check  | modeNonRenewable
        2				| [12, 7]               	  |	[17, 23]					| false  | [6, 0]
        5				| [29, 30]             	      | [0, 0]  					| true   | [0, 5]
        7				| [29, 30] 			  		  |	[0, 0]	    				| true   | [7, 0]
        9				| [29, 30] 			  		  | [0, 0]  					| true   | [8, 0]
        10				| [12, 7]            		  |	[17, 23]					| false  | [2, 0]
    }
}
