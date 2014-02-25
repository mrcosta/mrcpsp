package mrcpsp.process.mode

import mrcpsp.model.main.Mode
import mrcpsp.model.main.Project
import mrcpsp.model.main.ResourceAvailabilities

/**
 * Created by mateus on 2/24/14.
 */
class ShortestFeasibleMode {

    def setJobsMode(Project project) {



        return project.jobs
    }


    /*def checkSolution(Project project) {

        if (checkResources(project.resourceAvailabilities, mode)) {
            updateResources(ra, mode)
            job.mode = mode
        }
    }*/

    boolean checkResources(ResourceAvailabilities ra, Mode mode) {
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

    def updateResources(ResourceAvailabilities ra, Mode mode) {
        Integer count = 0

        mode.nonRenewable.each { amount ->
            ra.nonRenewableConsumedAmount[count] = ra.nonRenewableConsumedAmount[count] + amount
            ra.remainingNonRenewableAmount[count] = ra.nonRenewableInitialAmount[count] - ra.nonRenewableConsumedAmount[count]

            count++
        }
    }

    def test() {
        perm([1], [1, 2, 3])
    }

    def perm(List<Integer> x, List<Integer> a) {

        if (x.size() == a.size()) {
            println x
        } else {
            a.each {
                x.add(it)
                if (valid(x, it)) {
                    perm(x, a)
                }
            }
        }
    }

    def valid(List<Integer> list, def valueToAdd) {

        list.each {
            if (it == valueToAdd) {
                return false
            }
        }

        return true

        /*for(int j = 0; j < list.size() - 1; j++) {
            if (list[j] == list[j + 1]) {
                list.remove(j + 1)
                return false
            }
        }
        return true*/
    }
}
