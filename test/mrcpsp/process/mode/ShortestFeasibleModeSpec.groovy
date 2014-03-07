package mrcpsp.process.mode

import com.sun.xml.internal.bind.v2.TODO
import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.ModesInformation
import mrcpsp.model.main.ResourceAvailabilities
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by mateus on 2/24/14.
 */
class ShortestFeasibleModeSpec extends Specification {

    def ra
    ShortestFeasibleMode sfm
    def ml2, ml4, ml6, ml7
    def dumbJob, dumbJob2, dumbJob3, dumbJob4

    def setup() {
        sfm = new ShortestFeasibleMode()
        ra = new ResourceAvailabilities(remainingNonRenewableAmount : [12, 7], nonRenewableConsumedAmount: [17, 23], nonRenewableInitialAmount: [29, 30])

        ml2 = [new Mode(id:1, nonRenewable: [9, 5]), new Mode(id:2, nonRenewable: [7, 2]), new Mode(id:3, nonRenewable: [4, 1])]
        ml4 = [new Mode(id:1, nonRenewable: [10, 4]), new Mode(id:2, nonRenewable: [8, 3]), new Mode(id:3, nonRenewable: [3, 3])]
        ml6 = [new Mode(id:1, nonRenewable: [66, 5]), new Mode(id:2, nonRenewable: [7, 2]), new Mode(id:3, nonRenewable: [2, 55])]
        ml7 = [new Mode(id:1, nonRenewable: [5, 3]), new Mode(id:2, nonRenewable: [4, 2]), new Mode(id:3, nonRenewable: [67, 1])]
        dumbJob = new Job(id: 1, availableModes: ml2, modesInformation: new ModesInformation(modesByOrderDuration: [1, 2, 3]))
        dumbJob2 = new Job(id: 2, availableModes: ml4, modesInformation: new ModesInformation(modesByOrderDuration: [1, 2, 3]))
        dumbJob3 = new Job(id: 3, availableModes: ml6, modesInformation: new ModesInformation(modesByOrderDuration: [1, 2, 3]))
        dumbJob4= new Job(id: 4, availableModes: ml7, modesInformation: new ModesInformation(modesByOrderDuration: [1, 2, 3]))
    }

    def "Should check if is possible to added an specific mode for a job given the resources amount"() {
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
        when:
        sfm.addingResources(ra, mode)

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

    def "Should update the consumed and remaining resources amount when removing a job's mode"() {
        when:
        sfm.removingResources(ra, mode)

        then:
        ra.remainingNonRenewableAmount == remainingNonRenewableAmount
        ra.nonRenewableConsumedAmount == nonRenewableConsumedAmount

        where:
        mode                               | remainingNonRenewableAmount | nonRenewableConsumedAmount
        new Mode(nonRenewable: [6, 0])	   | [18, 7]               	     | [11, 23]
        new Mode(nonRenewable: [1, 1])     | [13, 8]             	     | [16, 22]
        new Mode(nonRenewable: [12, 7])	   | [24, 14] 			  		 | [5, 16]
        new Mode(nonRenewable: [0, 1])	   | [12, 8] 			  		 | [17, 22]
        new Mode(nonRenewable: [5, 5])     | [17, 12]            		 | [12, 18]
    }

    def "Should find the job's mode using the shortest feasible mode criteria"() {
        given:
        List<Job> jobs = [dumbJob, dumbJob2, dumbJob3, dumbJob4]
        ResourceAvailabilities ra = new ResourceAvailabilities(remainingNonRenewableAmount : [29, 30], nonRenewableConsumedAmount: [0, 0], nonRenewableInitialAmount: [29, 30])

        when:
        def result = sfm.sfm([:], ra, jobs, false, 0)

        then:
        result != null
        sfm.jobModes == [1:1, 2:2, 3:2, 4:1]
        ra.remainingNonRenewableAmount == [29, 30]
        ra.nonRenewableConsumedAmount == [0, 0]
    }

    /** @TODO Testing jobs that is not possible to find a feasbile solution for the modes distribution **/

}
