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
        dumbJob2 = new Job(id: 2, availableModes: ml4, modesInformation: new ModesInformation(modesByOrderDuration: [1, 2, 3], minNonRenewableResourcesConsumption: [3, 3]))
        dumbJob3 = new Job(id: 3, availableModes: ml6, modesInformation: new ModesInformation(modesByOrderDuration: [1, 2, 3], minNonRenewableResourcesConsumption: [2, 2]))
        dumbJob4= new Job(id: 4, availableModes: ml7, modesInformation: new ModesInformation(modesByOrderDuration: [1, 2, 3], minNonRenewableResourcesConsumption: [4, 1]))
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

    def "Should check if is possible to use the mode based in the minimum resources (non renewable) consumption for the jobs that the mode was not set yet"() {
        given:
        List<Job> jobs = [dumbJob, dumbJob2, dumbJob3, dumbJob4]
        Mode mode = new Mode(id:1, nonRenewable: [9, 5])
        ResourceAvailabilities ra = new ResourceAvailabilities(remainingNonRenewableAmount : [29, 11], nonRenewableConsumedAmount: [0, 0], nonRenewableInitialAmount: [29, 30])

        when:
        def result = sfm.checkMinimumResourcesRemainingJobs(ra, mode, jobs, 1)

        then:
        result != null
        result == true
    }

    /** @TODO Testing jobs that is not possible to find a feasbile solution for the modes distribution **/

}
