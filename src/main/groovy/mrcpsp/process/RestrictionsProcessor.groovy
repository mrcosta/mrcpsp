package mrcpsp.process

import mrcpsp.model.main.Job
import mrcpsp.model.main.Project
import mrcpsp.model.main.ResourceAvailabilities
import mrcpsp.process.mode.ModeOperations
import org.apache.log4j.Level
import org.apache.log4j.Logger

public class RestrictionsProcessor {

	private static final Logger log = Logger.getLogger(RestrictionsProcessor.class)

    ModeOperations modeOperations

    RestrictionsProcessor() {
        modeOperations = new ModeOperations()
    }

    def boolean checkAndSetTheNonRenewableResourcesAmount(Project project) {
        ResourceAvailabilities ra = project.resourceAvailabilities
        List<Job> jobs = project.jobs
        boolean checkAmount = true

        log.info("Checking the Non Renewable resources amount. Initial Amount: " + ra.nonRenewableInitialAmount)
        for (Job job: jobs) {
            checkAmount = modeOperations.checkNonRenewableResources(ra, job.mode)

            if (checkAmount) {
                modeOperations.addingNonRenewableResources(ra, job.mode)
            } else {
                log.log(Level.ERROR, "FILE instance: "  + project.fileName + " .Problem calculating the non renewable resources amount of the job " + job.id + ".")
                log.info("Consumed RN after execution will be: " + ra.nonRenewableConsumedAmount + " and remaining amout: " + ra.remainingNonRenewableAmount)
                return false
            }

            log.debug("Consumed non renewable amounts after job " + job.id + " execution will be: " + ra.nonRenewableConsumedAmount)
        }

        log.info("Consumed RN after execution will be: " + ra.nonRenewableConsumedAmount + " and remaining amout: " + ra.remainingNonRenewableAmount)
        return checkAmount
    }
}
