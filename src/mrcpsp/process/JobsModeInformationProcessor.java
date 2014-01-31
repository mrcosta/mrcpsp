package mrcpsp.process;

import mrcpsp.model.main.Job;
import mrcpsp.process.mode.ModeComparatorProcessor;
import org.apache.log4j.Logger;

import java.util.List;

public class JobsModeInformationProcessor {
	
	private static final Logger log = Logger.getLogger(JobsModeInformationProcessor.class);

	public List<Job> getJobModesInformation(List<Job> jobs) {
        ModeComparatorProcessor modeComparatorProcessor = new ModeComparatorProcessor();
		
		for (Job job : jobs) {
			// by duration
			job.setModesInformation(modeComparatorProcessor.orderModeByDuration(job));
			
			// by renewable amount
			job.setModesInformation(modeComparatorProcessor.orderModeByRenewableAmountResources(job));
			
			// by non renewable amount
			job.setModesInformation(modeComparatorProcessor.orderModeByNonRenewableAmountResources(job));
			
			// by sum of all resources
			job.setModesInformation(modeComparatorProcessor.orderModeBySumResources(job));
			
			log.info("JOB ID: " + job.getId() + " - MODES INFORMATION: " + job.getModesInformation().toString());
		}
		
		return jobs;
	}

}
