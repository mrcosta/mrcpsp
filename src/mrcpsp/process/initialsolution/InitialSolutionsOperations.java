package mrcpsp.process.initialsolution;

import mrcpsp.model.enums.EnumJobPriorityRules;
import mrcpsp.model.enums.EnumLogUtils;
import mrcpsp.model.main.Job;
import mrcpsp.model.main.RunningJobInformation;
import mrcpsp.process.job.JobPriorityRulesOperations;
import mrcpsp.utils.CloneUtils;
import mrcpsp.utils.LogUtils;
import mrcpsp.utils.UrlUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author mateus
 * 
 */
public class InitialSolutionsOperations {
	
	private static final Logger log = Logger.getLogger(InitialSolutionsOperations.class);

    /**
     * If the predecessors list equals to staggeredPredecessors, so the job has all its predecessor schedulled.
     * Then the job is ready to be scheduled and added to the rclJobsList
     * @param remainingJobs
     * @param eligibleJobsList
     * @return
     */
	public List<Job> getEligibleJobsList(List<Job> remainingJobs, List<Job> eligibleJobsList) {
		
		log.debug("Getting the list of remaining jobs to scheduling...");
		for (Job job : remainingJobs) {

			if (job.getPredecessors().size() == job.getRunningJobInformation().getStaggeredPredecessors().size() &&
			    !job.getRunningJobInformation().isEligible()) {
				
				eligibleJobsList.add(CloneUtils.cloneJob(job));
				job.getRunningJobInformation().setEligible(true);

				log.debug("The job with id " + job.getId() + " was included in the Eligible Jobs List.");
			}
		}				

		// logging the eligible jobs list
		log.info(LogUtils.generateJobsIDListLog(eligibleJobsList, EnumLogUtils.ELIGIBLE_JOBS));
		
		return eligibleJobsList;
	}

    /**
     * Return a randomized job from rclJobsList base in the RCList Size
     * If the RCList Size is lesser than the rclJobsList size then randomize a job from the entire list
     * @param rclJobsList
     * @return
     */
	public Job getRandomJobFromRCL(List<Job> rclJobsList) {
		Integer randomIndex;
		Integer rclSize = rclJobsList.size();
		
		// random between rclSize and 0
		if (rclSize < rclJobsList.size()) {
			randomIndex = RandomUtils.nextInt(rclSize);
		} else {
			// random between 0 and rclJobsList.size
			randomIndex = RandomUtils.nextInt(rclJobsList.size());
		}
		
		return rclJobsList.get(randomIndex);
	}

    /**
     * Update the RunningJobInformation - nis, can and niscan amount
     * Update the predecessors list of the successors jobs of the task that was picked to be scheduled
     * @param remainingJobs
     * @param  randomizedJob
     * @return
     */
    public List<Job> updateRunningJobInformation(List<Job> remainingJobs, Job randomizedJob) {
		
		for (Job job : remainingJobs) {
			RunningJobInformation runningJobInformation = job.getRunningJobInformation();
			
			/* if the predecessors list of the job has the id of the randomized job
			 * then update the staggered predecessor list of it 
			 */
			if (job.getPredecessors().contains(randomizedJob.getId())) {
				runningJobInformation.getStaggeredPredecessors().add(randomizedJob.getId());
			}
			
			log.debug(job.toString());
		}		
		
		String priorityRule = UrlUtils.getInstance().getJobPriorityRule();
		JobPriorityRulesOperations jprOperations = new JobPriorityRulesOperations();
		
		// TO IMPLEMENT
		if (StringUtils.equals(priorityRule, EnumJobPriorityRules.MAX_CAN.getName())) {
			
			jprOperations.updateJobCAN(remainingJobs, randomizedJob);
			
		} else if (StringUtils.equals(priorityRule, EnumJobPriorityRules.MAX_NISCAN.getName())) {
			
			jprOperations.updateJobNISCAN(remainingJobs, randomizedJob);
			
		}
		
		return remainingJobs;
	}
	
}
