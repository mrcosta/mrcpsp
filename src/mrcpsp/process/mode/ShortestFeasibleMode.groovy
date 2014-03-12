package mrcpsp.process.mode

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.Project
import mrcpsp.model.main.ResourceAvailabilities
import org.apache.log4j.Logger

/**
 * Created by mateus on 2/24/14.
 */
class ShortestFeasibleMode {

    private static final Logger log = Logger.getLogger(ShortestFeasibleMode.class);

    Map<String, String> jobModes

    def setJobsMode(Project project) {

        sfm([:], project.resourceAvailabilities, project.jobs, false, 0)

        return this.jobModes
    }

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

    boolean checkMinimumResourcesRemainingJobs(ResourceAvailabilities ra, Mode mode, List<Job> jobs, int countJob) {
        List<Integer> nonRenewableConsumption = new ArrayList<>(mode.nonRenewable)
        def modes = ""

        for (int i = countJob; i < jobs.size(); i++) {
            Job job = jobs[i]

            modes+= job.modesInformation.minNonRenewableResourcesConsumption
            modes+= ","

            for (int j = 0; j < nonRenewableConsumption.size(); j++) {
                nonRenewableConsumption[j]+= job.modesInformation.minNonRenewableResourcesConsumption[j]
            }
        }

        def checkAmount = true
        for (int i = 0; i < nonRenewableConsumption.size(); i++) {
            if (nonRenewableConsumption[i] > ra.remainingNonRenewableAmount[i]) {
                checkAmount = false
            }
        }

        return checkAmount
    }

    def addingResources(ResourceAvailabilities ra, Mode mode) {
        Integer count = 0

        mode.nonRenewable.each { amount ->
            ra.nonRenewableConsumedAmount[count] = ra.nonRenewableConsumedAmount[count] + amount
            ra.remainingNonRenewableAmount[count] = ra.nonRenewableInitialAmount[count] - ra.nonRenewableConsumedAmount[count]

            count++
        }
    }

    def removingResources(ResourceAvailabilities ra, Mode mode) {
        Integer count = 0

        mode.nonRenewable.each { amount ->
            ra.nonRenewableConsumedAmount[count] = ra.nonRenewableConsumedAmount[count] - amount
            ra.remainingNonRenewableAmount[count] = ra.remainingNonRenewableAmount[count] + amount

            count++
        }
    }

    def sfm(Map<String, String> jobModes, ResourceAvailabilities ra, List<Job> jobs, boolean firstSolutionFound, int countJob) {

        if (jobModes.size() == jobs.size()) {
            log.info("SHORTEST FEASIBLE MODE --- Map with the job ids (key) and the mode id that was selected (value): $jobModes")
            this.jobModes = new HashMap<String, String>(jobModes)
            return true
        } else {

            for (int i = countJob; i < jobs.size(); i++) {
                Job job = jobs[i]

                for (int j = 0; j < job.modesInformation.modesByOrderDuration.size(); j++) {
                    def index = job.modesInformation.modesByOrderDuration[j]
                    Mode mode = job.availableModes.find { it.id == index }

                    if (!jobModes.containsKey(job.id) && checkResources(ra, mode) && checkMinimumResourcesRemainingJobs(ra, mode, jobs, i + 1)) {
                        addingResources(ra, mode)
                        jobModes.putAt(job.id, mode.id)

                        firstSolutionFound = sfm(jobModes, ra, jobs, firstSolutionFound, i + 1)

                        jobModes.remove(job.id)
                        removingResources(ra, mode)

                        if (firstSolutionFound) {
                            return firstSolutionFound
                        }
                    }
                }
            }
        }
    }

}
