package mrcpsp.process

import mrcpsp.model.enums.EnumJobsMode
import mrcpsp.model.main.Job
import mrcpsp.utils.UrlUtils
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Level
import org.apache.log4j.Logger

/**
 * @author mrcosta
 *
 */
public class JobsModeSelectProcessor {
	
	private static final Logger log = Logger.getLogger(JobsModeSelectProcessor.class);

    /**
     * each job get a mode that doesn't break the non renewable resources amount
     * @param jobs
     * @return
     */
	public List<Job> getModeToStaggeredJobs(List<Job> jobs) {
		String jobsMode = UrlUtils.getInstance().getJobsMode();
		List<Job> jobsWithMode;
		
		log.info("MODE SELECTION: " + jobsMode);
		if (StringUtils.equals(jobsMode, EnumJobsMode.LESS_DURATION.getDescription())) {
			
			jobsWithMode = setShorterDurationMode(jobs);
			
		} else if (StringUtils.equals(jobsMode, EnumJobsMode.MINIMUM_NON_RENEWABLE_RESOURCES.getDescription())) { 
			
			jobsWithMode = setJobsLowerNonRenewableConsumption(jobs);
			
		} else if (StringUtils.equals(jobsMode, EnumJobsMode.MINIMUM_RESOURCES.getDescription())) {
			
			jobsWithMode = setJobsLowerSumComsuption(jobs);
			
		} else {
			log.log(Level.ERROR, "MODE SELECTION is not valid! Please check the argument 'mode.jobs' in mrcpsp.properties file");
			throw new IllegalArgumentException("MODE SELECTION is not valid! Please check the argument 'mode.jobs' in mrcpsp.properties file");
		}		
		
		return jobsWithMode;
	}
	
	/**
	 * get a mode to each job, choosing the mode with the minimum duration to be execute
	 * @return
	 */
	public List<Job> setShorterDurationMode(List<Job> jobs) {		
		
		for (Job job: jobs) {
			Integer index = job.getModesInformation().getShorter() - 1;
			job.setMode(job.getAvailableModes().get(index));
			
			log.info("JOB: " + job.getId() + " - Selected mode: " + job.getMode().getId() + " - Values: " + job.getMode().getNonRenewable());
		}
		
		return jobs;
	}
	
	/**
	 * get a mode to each job, choosing the mode with the minimum non renewable required resources to be execute
	 * if the sum of resources are equals, then, get the mode that have the less duration.
	 * @return
	 */
	public List<Job> setJobsLowerNonRenewableConsumption(List<Job> jobs) {
		
		for (Job job: jobs) {
			Integer index = job.getModesInformation().getLowerNonRenewableConsumption() - 1;
			job.setMode(job.getAvailableModes().get(index));
			
			log.info("JOB: " + job.getId() + " - Selected mode: " + job.getMode().getId() + " - Values: " + job.getMode().getNonRenewable());
		}
		
		return jobs;
	}
	
	/**
	 * get a mode to each job, choosing the mode with the minimum required resources to be execute
	 * if the sum of resources are equals, then, get the mode that have the less duration.
	 * @return
	 */
	public List<Job> setJobsLowerSumComsuption(List<Job> jobs) {
		
		for (Job job: jobs) {
			Integer index = job.getModesInformation().getLowerSumComsuption() - 1;
			job.setMode(job.getAvailableModes().get(index));
			
			log.info("JOB: " + job.getId() + " - Selected mode: " + job.getMode().getId() + " - Values: " + job.getMode().getNonRenewable());
		}
		
		return jobs;
	}

}
