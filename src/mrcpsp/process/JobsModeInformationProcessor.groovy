package mrcpsp.process;

import mrcpsp.model.main.Job
import mrcpsp.model.main.ResourceAvailabilities;
import mrcpsp.process.mode.ModeComparatorProcessor;
import org.apache.log4j.Logger;

class JobsModeInformationProcessor {
	
	static final Logger log = Logger.getLogger(JobsModeInformationProcessor.class);

	def List<Job> getJobModesInformation(List<Job> jobs, ResourceAvailabilities ra) {
        def modeComparatorProcessor = new ModeComparatorProcessor();

        jobs.each { job ->

            // excluding dumb modes
            job.availableModes = modeComparatorProcessor.excludeRenewableDumbModes(job, ra)
            job.availableModes = modeComparatorProcessor.excludeNonRenewableDumbModes(job, ra)

            // by duration
            job.modesInformation = modeComparatorProcessor.orderModeByDuration(job)

            // by renewable amount
            job.modesInformation = modeComparatorProcessor.orderModeByRenewableAmountResources(job)

            // by non renewable amount
            job.modesInformation = modeComparatorProcessor.orderModeByNonRenewableAmountResources(job)

            // by sum of all resources
            job.modesInformation = modeComparatorProcessor.orderModeBySumResources(job)

            // check if exists a mode near lower non renewable resources (its consumption is near the lower mode) that's shorter
            job.modesInformation = modeComparatorProcessor.checkModeNearLowerNRConsumption(job)

            job.modesInformation = modeComparatorProcessor.getMinNonRenewableResourceConsumption(job)

            log.info("JOB ID: " + job.getId() + " - MODES INFORMATION: " + job.getModesInformation().toString())
        }

		return jobs
	}

}
