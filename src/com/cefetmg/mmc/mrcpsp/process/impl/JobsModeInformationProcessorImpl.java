package com.cefetmg.mmc.mrcpsp.process.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.cefetmg.mmc.mrcpsp.model.main.Job;
import com.cefetmg.mmc.mrcpsp.process.JobsModeInformationProcessor;
import com.cefetmg.mmc.mrcpsp.process.mode.ModeComparatorProcessor;
import com.cefetmg.mmc.mrcpsp.process.mode.impl.ModeComparatorProcessorImpl;

public class JobsModeInformationProcessorImpl implements JobsModeInformationProcessor {
	
	private static final Logger log = Logger.getLogger(JobsModeInformationProcessorImpl.class);

	@Override
	public List<Job> getJobModesInformation(List<Job> jobs) {
		ModeComparatorProcessor modeComparatorProcessor = new ModeComparatorProcessorImpl();
		
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
