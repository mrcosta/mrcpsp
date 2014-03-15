package mrcpsp.process.mode

import mrcpsp.model.main.Mode
import mrcpsp.model.main.ResourceAvailabilities

/**
 * Created by mateus on 2/16/14.
 */
class ModeOperations {

    boolean checkRenewableResourcesAmount(ResourceAvailabilities ra, Mode mode) {
        Integer count = 0
        boolean checkAmount = true

        mode.renewable.each { amount ->
            def rConsumedAmount = ra.renewableConsumedAmount[count]
            def remainingResources

            rConsumedAmount+= amount
            remainingResources = ra.renewableInitialAmount[count] - rConsumedAmount

            if (remainingResources < 0) {
                checkAmount = false
            }

            count++
        }

        return checkAmount
    }

    def addingRenewableResources(ResourceAvailabilities ra, Mode mode) {
        Integer count = 0

        mode.renewable.each { amount ->
            ra.renewableConsumedAmount[count] = ra.renewableConsumedAmount[count] + amount
            ra.remainingRenewableAmount[count] = ra.renewableInitialAmount[count] - ra.renewableConsumedAmount[count]

            count++
        }
    }

    def removingRenewableResources(ResourceAvailabilities ra, Mode mode) {
        Integer count = 0

        mode.renewable.each { amount ->
            ra.renewableConsumedAmount[count] = ra.renewableConsumedAmount[count] - amount
            ra.remainingRenewableAmount[count] = ra.remainingRenewableAmount[count] + amount

            count++
        }
    }

    def addingNonRenewableResources(ResourceAvailabilities ra, Mode mode) {
        Integer count = 0

        mode.nonRenewable.each { amount ->
            ra.nonRenewableConsumedAmount[count] = ra.nonRenewableConsumedAmount[count] + amount
            ra.remainingNonRenewableAmount[count] = ra.nonRenewableInitialAmount[count] - ra.nonRenewableConsumedAmount[count]

            count++
        }
    }

    def removingNonRenewableResources(ResourceAvailabilities ra, Mode mode) {
        Integer count = 0

        mode.nonRenewable.each { amount ->
            ra.nonRenewableConsumedAmount[count] = ra.nonRenewableConsumedAmount[count] - amount
            ra.remainingNonRenewableAmount[count] = ra.remainingNonRenewableAmount[count] + amount

            count++
        }
    }

    boolean checkNonRenewableResources(ResourceAvailabilities ra, Mode mode) {
        Integer count = 0
        boolean checkAmount = true

        mode.nonRenewable.each { amount ->
            def nrConsumedAmount = ra.nonRenewableConsumedAmount[count]
            def remainingResources

            nrConsumedAmount+= amount
            remainingResources = ra.nonRenewableInitialAmount[count] - nrConsumedAmount

            if (remainingResources < 0) {
                checkAmount = false
            }

            count++
        }

        return checkAmount
    }
}
