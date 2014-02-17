package mrcpsp.process.mode

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.ModesInformation
import mrcpsp.model.main.ResourceAvailabilities
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by mateus on 2/15/14.
 */
class ModeComparatorProcessorSpec extends Specification {

    @Shared job4, job8, job9, job11
    @Shared mi4, mi8, mi9, mi11

    @Shared ml2, ml4, ml6, ml7
    @Shared ra
    @Shared dumbJob2, dumbJob4, dumbJob6, dumbJob7

    def setupSpec() {
        mi4 = new ModesInformation(lowerNonRenewableConsumption: 3)
        mi8 = new ModesInformation(lowerNonRenewableConsumption: 2)
        mi9 = new ModesInformation(lowerNonRenewableConsumption: 2)
        mi11 = new ModesInformation(lowerNonRenewableConsumption: 3)
        job4 = new Job(id: 4, modesInformation: mi4, availableModes: [new Mode(id:1, amountNonRenewable: 5, duration: 1), new Mode(id:2, amountNonRenewable: 3, duration: 3), new Mode(id:3, amountNonRenewable: 2, duration: 6)])
        job8 = new Job(id: 8, modesInformation: mi8, availableModes: [new Mode(id:1, amountNonRenewable: 7, duration: 2), new Mode(id:2, amountNonRenewable: 6, duration: 5), new Mode(id:3, amountNonRenewable: 6, duration: 5)])
        job9 = new Job(id: 9, modesInformation: mi9, availableModes: [new Mode(id:1, amountNonRenewable: 2, duration: 1), new Mode(id:2, amountNonRenewable: 1, duration: 2), new Mode(id:3, amountNonRenewable: 3, duration: 4)])
        job11 = new Job(id: 11, modesInformation: mi11, availableModes: [new Mode(id:1, amountNonRenewable: 8, duration: 4), new Mode(id:2, amountNonRenewable: 8, duration: 10), new Mode(id:3, amountNonRenewable: 7, duration: 10)])

        // to second test
        ml2 = [new Mode(id:1, renewable: [0, 7], nonRenewable: [9, 5]), new Mode(id:2, renewable: [7, 0], nonRenewable: [7, 2]), new Mode(id:3, renewable: [0, 7], nonRenewable: [4, 1])]
        ml4 = [new Mode(id:1, renewable: [0, 7], nonRenewable: [10, 4]), new Mode(id:2, renewable: [6, 0], nonRenewable: [8, 3]), new Mode(id:3, renewable: [0, 5], nonRenewable: [3, 3])]
        ml6 = [new Mode(id:1, renewable: [0, 2], nonRenewable: [66, 5]), new Mode(id:2, renewable: [0, 2], nonRenewable: [7, 2]), new Mode(id:3, renewable: [1, 0], nonRenewable: [2, 55])]
        ml7 = [new Mode(id:1, renewable: [8, 0], nonRenewable: [5, 3]), new Mode(id:2, renewable: [0, 4], nonRenewable: [4, 2]), new Mode(id:3, renewable: [8, 0], nonRenewable: [67, 1])]
        dumbJob2 = new Job(id: 2, availableModes: ml2)
        dumbJob4 = new Job(id: 4, availableModes: ml4)
        dumbJob6 = new Job(id: 6, availableModes: ml6)
        dumbJob7= new Job(id: 7, availableModes: ml7)
        ra = new ResourceAvailabilities(renewableInitialAmount: [10, 3], nonRenewableInitialAmount: [65, 54])
    }

    def "should check if there are shorters mode near the lower non renewable consumption mode"() {
        given:

        def mcp = new ModeComparatorProcessor()

        when:
        def modesInformation = mcp.checkModeNearLowerNRConsumption(job)

        then:
        modesInformation != null
        modesInformation.shorterNearToLowerNonRenewableComsumption == shorterNearToLowerNonRenewableComsumption

        where:
        job		| shorterNearToLowerNonRenewableComsumption
        job4	| 2
        job8	| 1
        job9	| 1
        job11	| 1
    }

    def "should exclude the dumb modes (for the renewable amount) from the job's available modes"() {
        given:

        def mcp = new ModeComparatorProcessor()

        when:
        def result = mcp.excludeRenewableDumbModes(jobs, ra)

        then:
        result != null
        result.size() == modeListSize
        result[0].id == idModeFirstPosition

        where:
        jobs		| modeListSize | idModeFirstPosition
        dumbJob2	| 1            | 2
        dumbJob4	| 1            | 2
        dumbJob6	| 3            | 1
        dumbJob7	| 2            | 1
    }

    def "should exclude the dumb modes (for the non renewable amount) from the job's available modes"() {
        given:

        def mcp = new ModeComparatorProcessor()

        when:
        def result = mcp.excludeNonRenewableDumbModes(jobs, ra)

        then:
        result != null
        result.size() == modeListSize
        result[0].id == idModeFirstPosition

        where:
        jobs		| modeListSize | idModeFirstPosition
        dumbJob2	| 3            | 1
        dumbJob4	| 3            | 1
        dumbJob6	| 1            | 2
        dumbJob7	| 2            | 1
    }
}
