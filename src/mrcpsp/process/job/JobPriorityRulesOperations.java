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

    /**
     * only the priority rules that doesn't no need update information at runtime
     * @param jobs
     * @return
     */
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

    /**
     * Check the priority rule passed in the config file and call the relativer method to order the
     * eligible jobs list
     * @param elegibleJobsList
     * @return
     */
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

    /**
     * order the elegible Jobs by the its MAX_NIS (Maximum Number of Immediate Successors)
     * the jobs that have only the 'x' task as its predecessor. Ex: 8 has only 5 as your predecessors, so we can say
     * that the 8 its a immediate successor of 5
     * @param elegibleJobs
     * @return
     */
	public List<Job> getJobListOrderByMaxNis(List<Job> elegibleJobs) {
		JobComparator jobComparator = new JobComparator();
		
		jobComparator.setComparatorType(EnumJobPriorityRules.MAX_NIS);
		Collections.sort(elegibleJobs, Collections.reverseOrder(jobComparator));		
		
		return elegibleJobs;
	}

    /**
     * order the elegible Jobs by the its MAX_CAN (Maximum Number of Subsequent Candidates)
     * Ex: 8 has 5 and 3 as your predecessors. 3 is already scheduled. so we can say that 8 is a subsequent candidate of 5,
     * because when 5 is scheduled 8 is eligible
     * @param elegibleJobs
     * @return
     */
	public List<Job> getJobListOrderByMaxCan(List<Job> elegibleJobs) {
		JobComparator jobComparator = new JobComparator();
		
		jobComparator.setComparatorType(EnumJobPriorityRules.MAX_CAN);
		Collections.sort(elegibleJobs, Collections.reverseOrder(jobComparator));			
		
		return elegibleJobs;
	}

    /**
     * the combination of the MAX_NIS and MAX_CAN
     * @param elegibleJobs
     * @return
     */
	public List<Job> getJobListOrderByMaxNiscan(List<Job> elegibleJobs) {
		JobComparator jobComparator = new JobComparator();
		
		jobComparator.setComparatorType(EnumJobPriorityRules.MAX_NISCAN);
		Collections.sort(elegibleJobs, Collections.reverseOrder(jobComparator));		
		
		return elegibleJobs;
	}

    /**
     * Back the order of the elegible jobs list - by the job ID
     * @param elegibleJobsList
     * @return
     */
	public List<Job> backOrderEligibleJobsList(List<Job> elegibleJobsList) {
		JobComparator jobComparator = new JobComparator();
		
		jobComparator.setComparatorType(EnumJobPriorityRules.BY_ID);
		Collections.sort(elegibleJobsList, jobComparator);		
		
		return elegibleJobsList;
	}

    /**
     * Update the job NIS - Maximum Number of Immediate Successors, run only one time.
     * There is no need to update this type of priority rule operation
     * @param jobs
     */
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

    /**
     * Update the job CAN - Maximum Number of Subsequent Candidates
     * @param remainingJobs
     * @param job
     * @return
     */
	public List<Job> updateJobCAN(List<Job> remainingJobs, Job job) {
		return null;
	}

    /**
     * updating the job NISCAN -NIS + CAN
     * @param remainingJobs
     * @param randomizedJob
     * @return
     */
	public List<Job> updateJobNISCAN(List<Job> remainingJobs, Job randomizedJob) {
		return null;
	}

    /**
     * order the predecessor Jobs by the its end time. Used to set the start and the finish time of each job
     * @param jobs
     * @return
     */
	public List<Job> getJobListOrderByEndTime(List<Job> jobs) {
		JobComparator jobComparator = new JobComparator();
		
		jobComparator.setComparatorType(EnumJobPriorityRules.BY_END_TIME);

        // the second criteria ir the job's order in the list
        Collections.sort(jobs, jobComparator);
		//Collections.sort(jobs, Collections.reverseOrder(jobComparator));
		
		return jobs;
	}	

}
