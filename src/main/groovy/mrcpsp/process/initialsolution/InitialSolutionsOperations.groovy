package mrcpsp.process.initialsolution;

import mrcpsp.model.enums.EnumJobPriorityRules;
import mrcpsp.model.enums.EnumLogUtils;
import mrcpsp.model.main.Job;
import mrcpsp.model.main.RunningJobInformation;
import mrcpsp.process.job.JobPriorityRulesOperations;
import mrcpsp.utils.ChronoWatch;
import mrcpsp.utils.CloneUtils;
import mrcpsp.utils.LogUtils;
import mrcpsp.utils.UrlUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author mateus
 * 
 */
class InitialSolutionsOperations {
	
	static final Logger log = Logger.getLogger(InitialSolutionsOperations.class);

    /**
     * If the predecessors list equals to staggeredPredecessors, so the job has all its predecessor schedulled.
     * Then the job is ready to be scheduled and added to the rclJobsList
     * @param remainingJobs
     * @param eligibleJobsList
     * @return
     */
	List<Integer> getEligibleJobsList(List<Job> remainingJobs, List<Integer> eligibleJobsListId) {
		log.debug("Getting the list of remaining jobs to scheduling...");
		remainingJobs.each { job ->

			if (job.predecessors.size() == job.runningJobInformation.staggeredPredecessors.size() && !job.runningJobInformation.isEligible()) {

                ChronoWatch.instance.pauseSolutionTime()
                eligibleJobsListId.add(job.id)
				job.runningJobInformation.eligible = true
                ChronoWatch.instance.startSolutionTime()

				log.debug("The job with id " + job.id + " was included in the Eligible Jobs List.")
			}
		}				

        log.info("The ELIGIBLE JOBS list has this index: $eligibleJobsListId")

		return eligibleJobsListId
	}

    /**
     * Return a randomized jobId from rclJobsList base in the RCList Size
     * If the RCList Size is lesser than the rclJobsList size then randomize a job from the entire list
     * @param rclJobsIdList
     * @return
     */
	Integer getRandomJobIdFromRCL(List<Integer> rclJobsIdList) {
		Integer randomIndex
		Integer rclSize = rclJobsIdList.size()
		
		// random between rclSize and 0
		if (rclSize < rclJobsIdList.size()) {
			randomIndex = RandomUtils.nextInt(rclSize)
		} else {
			// random between 0 and rclJobsList.size
			randomIndex = RandomUtils.nextInt(rclJobsIdList.size())
		}
		
		return rclJobsIdList[randomIndex]
	}

    /**
     * Update the RunningJobInformation - nis, can and niscan amount
     * Update the predecessors list of the successors jobs of the task that was picked to be scheduled
     * @param remainingJobs
     * @param  randomizedJob
     * @return
     */
    List<Job> updateRunningJobInformation(List<Job> remainingJobs, Integer randomizedJobId) {
		
		remainingJobs.each { job ->
			RunningJobInformation runningJobInformation = job.runningJobInformation
			
			/* if the predecessors list of the job has the id of the randomized job
			 * then update the staggered predecessor list of it 
			 */
			if (job.predecessors.contains(randomizedJobId)) {
				runningJobInformation.staggeredPredecessors.add(randomizedJobId);
			}
			
			log.debug(job.toString());
		}
		
		return remainingJobs
	}
	
}
