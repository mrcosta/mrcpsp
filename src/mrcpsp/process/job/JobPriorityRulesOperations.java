package mrcpsp.process.job;

import mrcpsp.model.enums.EnumJobPriorityRules;
import mrcpsp.model.main.Job;
import mrcpsp.utils.PropertyConstants;
import mrcpsp.utils.UrlUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;

public class JobPriorityRulesOperations {

	private static final Logger log = Logger.getLogger(JobPriorityRulesOperations.class);
	
	public List<Job> setJobsPriorityRuleInformation(List<Job> jobs) {
		String priorityRule = UrlUtils.getInstance().getJobPriorityRule();
		
		if (StringUtils.equals(priorityRule, EnumJobPriorityRules.MAX_NIS.getName())) {
			jobs = getJobsNIS(jobs);
		} else {
			log.log(Level.ERROR, "JOB PRIORITY RULE is not valid! Please check the argument 'job.priority.rule' in mrcpsp.properties file");
			throw new IllegalArgumentException("JOB PRIORITY RULE is not valid! Please check the argument 'job.priority.rule' in mrcpsp.properties file");
		}
		
		return jobs;
	}
	
	public List<Job> orderEligibleJobsList(List<Job> elegibleJobsList) {
		String priorityRule = UrlUtils.getInstance().getJobPriorityRule(); 
		
		if (StringUtils.equals(priorityRule, EnumJobPriorityRules.MAX_NIS.getName())) {			
			
			getJobListOrderByMaxNis(elegibleJobsList);
			
		} else if (StringUtils.equals(priorityRule, EnumJobPriorityRules.MAX_CAN.getName())) {
			
			getJobListOrderByMaxCan(elegibleJobsList);
			
		} else if (StringUtils.equals(priorityRule, EnumJobPriorityRules.MAX_NISCAN.getName())) {
			
			getJobListOrderByMaxNiscan(elegibleJobsList);
			
		} else {
			log.log(Level.ERROR, "JOB PRIORITY RULE is not valid! Please check the argument 'job.priority.rule' in mrcpsp.properties file");
			throw new IllegalArgumentException("JOB PRIORITY RULE is not valid! Please check the argument 'job.priority.rule' in mrcpsp.properties file");
		}
		
		return elegibleJobsList;
	}
	
	public List<Job> getJobListOrderByMaxNis(List<Job> elegibleJobs) {
		JobComparator jobComparator = new JobComparator();
		
		jobComparator.setComparatorType(EnumJobPriorityRules.MAX_NIS);
		Collections.sort(elegibleJobs, Collections.reverseOrder(jobComparator));		
		
		return elegibleJobs;
	}

	public List<Job> getJobListOrderByMaxCan(List<Job> elegibleJobs) {
		JobComparator jobComparator = new JobComparator();
		
		jobComparator.setComparatorType(EnumJobPriorityRules.MAX_CAN);
		Collections.sort(elegibleJobs, Collections.reverseOrder(jobComparator));			
		
		return elegibleJobs;
	}

	public List<Job> getJobListOrderByMaxNiscan(List<Job> elegibleJobs) {
		JobComparator jobComparator = new JobComparator();
		
		jobComparator.setComparatorType(EnumJobPriorityRules.MAX_NISCAN);
		Collections.sort(elegibleJobs, Collections.reverseOrder(jobComparator));		
		
		return elegibleJobs;
	}

	public List<Job> backOrderEligibleJobsList(List<Job> elegibleJobsList) {
		JobComparator jobComparator = new JobComparator();
		
		jobComparator.setComparatorType(EnumJobPriorityRules.BY_ID);
		Collections.sort(elegibleJobsList, jobComparator);		
		
		return elegibleJobsList;
	}

	public List<Job> getJobsNIS(List<Job> jobs) {
		
		for (Job job: jobs) {
			
			// so we update the nis amount of predecessor amount. the job has just one connection with predecessor
			if (job.getPredecessorsAmount() == 1) {
				Integer predecessorIndex = job.getPredecessors().get(PropertyConstants.INDEX_START) - 1;
				Job j = jobs.get(predecessorIndex);
				Integer countNIS = j.getRunningJobInformation().getNisAmount();
				
				countNIS++;
				
				j.getRunningJobInformation().setNisAmount(countNIS);
				log.info("JOB " + job.getId() + " has JOB " + j.getId() + " as your only predecessor. "
						+ "JOB: " + j.getId() + " PRI: " + j.getRunningJobInformation().toString());
			}
		}
		
		return jobs;
	}

	public List<Job> updateJobCAN(List<Job> remainingJobs, Job job) {
		return null;
	}

	public List<Job> updateJobNISCAN(List<Job> remainingJobs, Job randomizedJob) {
		return null;
	}

	public List<Job> getJobListOrderByEndTime(List<Job> jobs) {
		JobComparator jobComparator = new JobComparator();
		
		jobComparator.setComparatorType(EnumJobPriorityRules.BY_END_TIME);
		Collections.sort(jobs, Collections.reverseOrder(jobComparator));		
		
		return jobs;
	}	

}
